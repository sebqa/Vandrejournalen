package com.example.sebastian.vandrejournalen.Results;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.RoleHelper;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.authentication.RegisterFragment;
import com.example.sebastian.vandrejournalen.calendar.Appointment;
import com.example.sebastian.vandrejournalen.calendar.Schedule;
import com.google.gson.Gson;
import com.sembozdemir.viewpagerarrowindicator.library.ViewPagerArrowIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

import br.com.jpttrindade.calendarview.view.CalendarView;

public class ResultsPager extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "RESULTSPAGER";
    int day,month,year,hour, min;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<Appointment> arrayList = new ArrayList<Appointment>();
    ArrayList<Appointment> allAppointments = new ArrayList<Appointment>();
    ViewPagerArrowIndicator viewPagerArrowIndicator;

    ArrayList<Date> dates = new ArrayList<Date>();
    long now;
    Date today;
    ViewPager viewPager;
    Context context;
    ResultsPagerAdapter adapter;
    User user;
    public ResultsPager() {
        // Required empty public constructor
    }


    public static ResultsPager newInstance(User user) {
        ResultsPager fragment = new ResultsPager();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String obj = gson.toJson(user);
        args.putString("obj" , obj);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            user = gson.fromJson(getArguments().getString("obj"), User.class);        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_results_pager, container, false);
        viewPager = rootView.findViewById(R.id.resultsPager);
        arrayList = RoleHelper.getAllAppointments(user);
        final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        today = calendar.getTime();
        viewPagerArrowIndicator =  rootView.findViewById(R.id.viewPagerArrowIndicator);

        //Sort by date, low to high
        Collections.sort(arrayList, new Comparator<Appointment>() {
            public int compare(Appointment o1, Appointment o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        int[] attrs = {R.attr.colorPrimary};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        int color = ta.getResourceId(0, android.R.color.black);
        ta.recycle();
        Log.d(TAG, "onCreateView: "+user.getRole());
        adapter = new ResultsPagerAdapter(getFragmentManager(),getContext(),arrayList,user);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(arrayList.size()-1);
        viewPagerArrowIndicator.bind(viewPager);
        viewPagerArrowIndicator.setArrowColor(getResources().getColor(color));

        getNearestDate();
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        String role = user.getRole();
        switch(role) {
            case "PL":
                fab.hide();
                break;
            default:

                //Open dialog here to request appointment
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePickerDialog();



                }
            });
        }

        return rootView;


    }
    public void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int nyear,
                                          int monthOfYear, int dayOfMonth) {
                        year = nyear;
                        month = monthOfYear;
                        day = dayOfMonth;
                        Log.d(TAG, "showDatePickerDialog: "+year+month+day);
                        showTimePickerDialog(c);


                    }
                }, year, month, day);
        datePickerDialog.show();

    }
    public void showTimePickerDialog(Calendar c){
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        hour = hourOfDay;
                        min = minute;
                        setDate();
                    }
                }, hour, min, false);
        timePickerDialog.show();
    }


    public void getNearestDate(){
        now = System.currentTimeMillis();

        //Check if it's today
        for (int i=0; i< arrayList.size();i++){
            dates.add(arrayList.get(i).getDate());


            /* if(calendar.get(Calendar.DAY_OF_MONTH) == arrayList.get(i).getDay() && calendar.get(Calendar.MONTH)+1 == arrayList.get(i).getMonth() && calendar.get(Calendar.YEAR) == arrayList.get(i).getYear()){
                viewPager.setCurrentItem(i,false);

            }*/
        }

        Date closest = Collections.min(dates, new Comparator<Date>() {
            public int compare(Date d1, Date d2) {
                long diff1 = Math.abs(d1.getTime() - now);
                long diff2 = Math.abs(d2.getTime() - now);
                return Long.compare(diff1, diff2);
            }
        });

        afterToday(closest);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    public void afterToday(Date closest){
        if(!closest.after(today)) {
            viewPager.setCurrentItem(dates.indexOf(closest));

        }
        else {
            afterToday(dates.get(dates.indexOf(closest)-1));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }



    public void setDate() {

        Date date = new Date();
        Appointment appointment = new Appointment(date, "Læge");
        appointment.setDate(day, month, year, hour, min);
        arrayList.add(appointment);
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(arrayList.size(), true);
        Toast.makeText(getActivity(), "New appointment", Toast.LENGTH_SHORT).show();
    }
}
