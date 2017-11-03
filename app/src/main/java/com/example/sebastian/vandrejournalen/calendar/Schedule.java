package com.example.sebastian.vandrejournalen.calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.sebastian.journalapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import br.com.jpttrindade.calendarview.view.CalendarView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sebastian on 02-11-2016.
 */

public class Schedule extends Fragment  {
    FragmentPagerAdapter adapterViewPager;
    ArrayList<CalendarEvent> upcomingEvents = new ArrayList<CalendarEvent>();
    ArrayList<CalendarEvent> allEvents = new ArrayList<CalendarEvent>();




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule,container,false);
        getActivity().setTitle(rootView.getResources().getString(R.string.overview));

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

    public ArrayList<CalendarEvent> getUpcomingEvents(){

        CalendarEvent event = new CalendarEvent("Friday 6-10-17","KJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdb ");
        upcomingEvents.add(0,event);
        CalendarEvent event2 = new CalendarEvent("Thursday 5-10-17","KJsd f3");
        upcomingEvents.add(0,event2);
        CalendarEvent event3 = new CalendarEvent("Wednesday 4-10-17","KJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdbKJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdbKJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdbKJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdb ");
        upcomingEvents.add(0,event3);
        CalendarEvent event4 = new CalendarEvent("Tuesday 3-10-17","KJSD kjasdasjkdksjaDBAKSJ jksakd ");
        upcomingEvents.add(0,event4);
        return upcomingEvents;
    }

    public ArrayList<CalendarEvent> getAllEvents(){


        final CalendarEvent event = new CalendarEvent(7,11,2017,"Læge");
        final CalendarEvent event2 = new CalendarEvent(17,11,2017,"Jordemoder");

        allEvents.add(0,event);
        allEvents.add(0,event2);


        return allEvents;
    }

}

