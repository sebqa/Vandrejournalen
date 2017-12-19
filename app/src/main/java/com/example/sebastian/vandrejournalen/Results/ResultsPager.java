package com.example.sebastian.vandrejournalen.Results;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
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

    private static final String TAG = "RESULTSPAGER";
    int day,month,year,hour, min;
    ServerClient client;
    String journalID;

    ArrayList<Consultation> arrayList = new ArrayList<Consultation>();
    ViewPagerArrowIndicator viewPagerArrowIndicator;
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
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Get objects from Bundle
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
        //set today's date
        final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        today = calendar.getTime();

        viewPagerArrowIndicator =  rootView.findViewById(R.id.viewPagerArrowIndicator);
        //Create Http Client
        client = ServiceGenerator.createService(ServerClient.class);

        getConsultations();
        setUpPager();
        return rootView;
    }

    private void setUpPager() {
        //Create theme color
        int[] attrs = {R.attr.colorPrimary};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        int color = ta.getResourceId(0, android.R.color.black);
        ta.recycle();

        adapter = new ResultsPagerAdapter(getFragmentManager(),getContext(),arrayList,user, patient);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(arrayList.size()-1);
        viewPagerArrowIndicator.bind(viewPager);
        //Add theme color to arrows
        viewPagerArrowIndicator.setArrowColor(getResources().getColor(color));
    }

    private void getConsultations() {
        //Check if we need to send User's journal id or Patient's journal id
        if(user.getRole().equals("Patient")){
            journalID = user.getJournalID();
        } else {
            journalID = patient.getJournalID();
        }
        //Send journal ID and get an array of Consultations
        Call<ArrayList<Consultation>> call = client.getConsultations("returnJournalConsultations.php", journalID );
        //Listen for response
        call.enqueue(new Callback<ArrayList<Consultation>>() {
            @Override
            public void onResponse(Call<ArrayList<Consultation>> call, Response<ArrayList<Consultation>> response) {
                //Check response content
                if(response.body() != null){
                    //Reset list of Consultations
                    arrayList.clear();
                    //Add all Consultaions in response body
                    arrayList.addAll(response.body());
                    //Sort the Consultations by date
                    Collections.sort(arrayList, new Comparator<Consultation>() {
                        public int compare(Consultation o1, Consultation o2) {
                            return o1.getDate().compareTo(o2.getDate());
                        }
                    });
                    //If the list is empty and the user is not a patient or specialist, they automatically create a new consultation
                    if(arrayList.isEmpty() && !user.getRole().equals("Patient") &&!user.getRole().equals("Specialist")){
                        setTodayDate();
                    }
                    adapter.notifyDataSetChanged();

                    //If there are Consultations in the list
                    if(!arrayList.isEmpty()){
                        getNearestDate();
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Consultation>> call, Throwable t) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showDatePickerDialog() throws ParseException {
        //Initialize calendar
        final Calendar c = Calendar.getInstance();
        //Set today's date
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        //get correct language of dialog
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        //Create dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    boolean mFirst = true;
                    @Override
                    public void onDateSet(DatePicker view, int nyear,
                                          int monthOfYear, int dayOfMonth) {
                        //Check if it is run for the first time. Bug causes it to double run. (not our fault!)
                        if (mFirst) {
                            mFirst = false;
                            //Update date
                            year = nyear;
                            month = monthOfYear+1;
                            day = dayOfMonth;
                            setDate();
                        }
                    }
                }, year, month, day);

        //Don't allow selection of dates before today
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
        //Add all Consultation's dates to a list
        for (int i=0; i< arrayList.size();i++){
            dates.add(arrayList.get(i).getDate());
        }
        //Compare the dates
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
        //If date is not after today
        if(!closest.after(today)) {
            //Move to this consultation
            viewPager.setCurrentItem(dates.indexOf(closest));
        }
        else {
            //If there are more Consultations in the list
            if(dates.indexOf(closest)-1 >1) {
                //Run method again with the previous consultation entry in the list
                afterToday(dates.get(dates.indexOf(closest) - 1));
            } else{
                //Move to this consultation
                viewPager.setCurrentItem(dates.indexOf(closest));
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void setTodayDate() {

        final Calendar c = Calendar.getInstance();
        //Get today's date
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH)+1;
        day = c.get(Calendar.DAY_OF_MONTH);

        //Create new Date object
        Date date = new Date();
        //Create new Consultation object
        Consultation consultation = new Consultation(date, null);
        //Set the date with values
        consultation.setDate(day, month, year);
        //Add consultations to the list
        arrayList.add(consultation);
        //Tell adapter an item was added
        adapter.notifyDataSetChanged();
        //Move to this consultation
        viewPager.setCurrentItem(arrayList.size(), true);
        Toast.makeText(getActivity(), "New consultation", Toast.LENGTH_SHORT).show();
    }

    public void setDate() {
        //Create new Date object
        Date date = new Date();
        //Create new Consultation object
        Consultation consultation = new Consultation(date, null);
        //Set the date with values
        consultation.setDate(day, month, year);
        //Add consultations to the list
        arrayList.add(consultation);
        //Tell adapter an item was added
        adapter.notifyDataSetChanged();
        //Move to this consultation
        viewPager.setCurrentItem(arrayList.size(), true);
        Toast.makeText(getActivity(), "New consultation", Toast.LENGTH_SHORT).show();
    }

}
