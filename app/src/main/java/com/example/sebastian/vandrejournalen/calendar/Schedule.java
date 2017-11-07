package com.example.sebastian.vandrejournalen.calendar;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sebastian.journalapp.R;

import java.util.ArrayList;

/**
 * Created by Sebastian on 02-11-2016.
 */

public class Schedule extends Fragment  {
    FragmentPagerAdapter adapterViewPager;
    ArrayList<Appointment> upcomingEvents = new ArrayList<Appointment>();
    ArrayList<Appointment> allEvents = new ArrayList<Appointment>();




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule,container,false);

        ViewPager vpPager = rootView.findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getFragmentManager(),getContext());
        vpPager.setAdapter(adapterViewPager);
        vpPager.setOffscreenPageLimit(3);



        // Give the TabLayout the ViewPager
        TabLayout tabLayout = rootView.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);




        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public ArrayList<Appointment> getUpcomingEvents(){

        Appointment event = new Appointment("Friday 6-10-17","KJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdb ");
        upcomingEvents.add(0,event);
        Appointment event2 = new Appointment("Thursday 5-10-17","KJsd f3");
        upcomingEvents.add(0,event2);
        Appointment event3 = new Appointment("Wednesday 4-10-17","KJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdbKJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdbKJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdbKJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdb ");
        upcomingEvents.add(0,event3);
        Appointment event4 = new Appointment("Tuesday 3-10-17","KJSD kjasdasjkdksjaDBAKSJ jksakd ");
        upcomingEvents.add(0,event4);
        return upcomingEvents;
    }

    public ArrayList<Appointment> getAllEvents(){


        final Appointment event = new Appointment(7,11,2017,"13.00","Læge");
        final Appointment event2 = new Appointment(17,11,2017,"11.00","Jordemoder");

        allEvents.add(0,event);
        allEvents.add(0,event2);


        return allEvents;
    }

}

