package com.example.sebastian.vandrejournalen.Results;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Patient;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;
import com.google.gson.Gson;
import com.sembozdemir.viewpagerarrowindicator.library.ViewPagerArrowIndicator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultsPager extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "RESULTSPAGER";
    int day,month,year,hour, min;
    ServerClient client;
    String journalID;


    // TODO: Rename and change types of parameters
    ArrayList<Consultation> arrayList = new ArrayList<Consultation>();
    ViewPagerArrowIndicator viewPagerArrowIndicator;
    FloatingActionButton fab;
    ArrayList<Date> dates = new ArrayList<Date>();
    long now;
    Date today;
    ViewPager viewPager;
    Context context;
    ResultsPagerAdapter adapter;
    User user;
    Patient patient;
    public ResultsPager() {
        // Required empty public constructor
    }


    public static ResultsPager newInstance(User user) {
        ResultsPager fragment = new ResultsPager();
        Bundle args = new Bundle();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            user = gson.fromJson(getArguments().getString("user"), User.class);
            if(getArguments().getString("patient",null)!= null) {
                patient = new Gson().fromJson(getArguments().getString("patient", "Patient"), Patient.class);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_results_pager, container, false);
        viewPager = rootView.findViewById(R.id.resultsPager);
        //arrayList = RoleHelper.getAllAppointments(user);
        final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        today = calendar.getTime();
        viewPagerArrowIndicator =  rootView.findViewById(R.id.viewPagerArrowIndicator);
        client = ServiceGenerator.createService(ServerClient.class);


        getAppointments();
        setUpPager();
        //Sort by date, low to high


        return rootView;


    }

    private void setUpPager() {

        int[] attrs = {R.attr.colorPrimary};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        int color = ta.getResourceId(0, android.R.color.black);
        ta.recycle();
        Log.d(TAG, "onCreateView: "+user.getRole());

        adapter = new ResultsPagerAdapter(getFragmentManager(),getContext(),arrayList,user, patient);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(arrayList.size()-1);
        viewPagerArrowIndicator.bind(viewPager);
        viewPagerArrowIndicator.setArrowColor(getResources().getColor(color));

    }

    private void getAppointments() {
        if(user.getRole().equals("Patient")){
            journalID = user.getJournalID();
        } else {
            journalID = patient.getJournalID();
        }
            Call<ArrayList<Consultation>> call = client.getConsultations("returnJournalConsultations.php", journalID );

            call.enqueue(new Callback<ArrayList<Consultation>>() {
                @Override
                public void onResponse(Call<ArrayList<Consultation>> call, Response<ArrayList<Consultation>> response) {
                    if(response.body() != null){
                        arrayList.clear();
                        arrayList.addAll(response.body());
                        Collections.sort(arrayList, new Comparator<Consultation>() {
                            public int compare(Consultation o1, Consultation o2) {
                                return o1.getDate().compareTo(o2.getDate());
                            }
                        });
                        if(arrayList.isEmpty()&& !user.getRole().equals("Patient")){
                            setTodayDate();
                        }
                        adapter.notifyDataSetChanged();
                        if(!arrayList.isEmpty()){
                            getNearestDate();
                        }

                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Consultation>> call, Throwable t) {

                }
            });
    }

    public void showDatePickerDialog() throws ParseException {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    boolean mFirst = true;

                    @Override
                    public void onDateSet(DatePicker view, int nyear,
                                          int monthOfYear, int dayOfMonth) {

                        if (mFirst) {
                            mFirst = false;
                            year = nyear;
                            month = monthOfYear+1;
                            day = dayOfMonth;
                            Log.d(TAG, "showDatePickerDialog: "+year+month+day);
                            //showTimePickerDialog(c);
                            setDate();

                        }



                    }
                }, year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = sdf.parse(day+"/"+(month+1)+"/"+year);
        datePickerDialog.getDatePicker().setMinDate(d.getTime());
        datePickerDialog.show();


    }
    public void showTimePickerDialog(Calendar c){
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    boolean mFirst = true;

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (mFirst) {
                            mFirst = false;
                            hour = hourOfDay;
                            min = minute;
                        }
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

    public void setTodayDate() {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);

        Date date = new Date();
        Consultation consultation = new Consultation(date, null);
        consultation.setDate(day, month, year);
        arrayList.add(consultation);
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(arrayList.size(), true);
        Toast.makeText(getActivity(), "New consultation", Toast.LENGTH_SHORT).show();


    }

    public void setDate() {


        Date date = new Date();
        Consultation consultation = new Consultation(date, null);
        consultation.setDate(day, month, year);
        arrayList.add(consultation);
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(arrayList.size(), true);
        Toast.makeText(getActivity(), "New consultation", Toast.LENGTH_SHORT).show();


    }

}
