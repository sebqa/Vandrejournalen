package com.example.sebastian.vandrejournalen.Results;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.RoleHelper;
import com.example.sebastian.vandrejournalen.calendar.Appointment;
import com.example.sebastian.vandrejournalen.calendar.Schedule;
import com.sembozdemir.viewpagerarrowindicator.library.ViewPagerArrowIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

public class ResultsPager extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<Appointment> arrayList = new ArrayList<Appointment>();
    ArrayList<Appointment> allAppointments = new ArrayList<Appointment>();
    ViewPagerArrowIndicator viewPagerArrowIndicator;
    Schedule schedule = new Schedule();
    String role;
    ArrayList<Date> dates = new ArrayList<Date>();
    long now;
    public ResultsPager() {
        // Required empty public constructor
    }


    public static ResultsPager newInstance(String role) {
        ResultsPager fragment = new ResultsPager();
        Bundle args = new Bundle();
        args.putString("role",role);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            role = getArguments().getString("role");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_results_pager, container, false);
        ViewPager viewPager = rootView.findViewById(R.id.resultsPager);
        arrayList = RoleHelper.getAllAppointments(role);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        viewPagerArrowIndicator =  rootView.findViewById(R.id.viewPagerArrowIndicator);


        ResultsPagerAdapter adapter = new ResultsPagerAdapter(getFragmentManager(),getContext(),arrayList,role);
        viewPager.setAdapter(adapter);
        viewPagerArrowIndicator.bind(viewPager);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss 'GMT'Z yyyy");
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
        Log.d(""+closest, "onCreateView: ");
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "New appointment", Toast.LENGTH_SHORT).show();



            }
        });

        return rootView;


    }



    @Override
    public void onDetach() {
        super.onDetach();
    }



}