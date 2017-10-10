package com.example.sebastian.vandrejournalen.calendar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
    ArrayList<CalendarEvents> upcomingEvents = new ArrayList<CalendarEvents>();
    ArrayList<CalendarEvents> recentEvents = new ArrayList<CalendarEvents>();




    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.test,container,false);
        getActivity().setTitle("Schedule");

        ViewPager vpPager = rootView.findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        vpPager.setOffscreenPageLimit(3);


        // Give the TabLayout the ViewPager
        TabLayout tabLayout = rootView.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add new note", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        return rootView;
    }



    public ArrayList<CalendarEvents> getUpcomingEvents(ArrayList<CalendarEvents> list){

        CalendarEvents event = new CalendarEvents("Friday 6-10-17","KJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdb ");
        list.add(0,event);
        CalendarEvents event2 = new CalendarEvents("Thursday 5-10-17","KJsd f3");
        list.add(0,event2);
        CalendarEvents event3 = new CalendarEvents("Wednesday 4-10-17","KJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdbKJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdbKJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdbKJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdb ");
        list.add(0,event3);
        CalendarEvents event4 = new CalendarEvents("Tuesday 3-10-17","KJSD kjasdasjkdksjaDBAKSJ jksakd ");
        list.add(0,event4);
        return list;
    }

    public ArrayList<CalendarEvents> getRecentEvents(ArrayList<CalendarEvents> list){

        CalendarEvents event = new CalendarEvents("Friday 29-09-17","KJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdb ");
        list.add(event);
        CalendarEvents event2 = new CalendarEvents("Thursday 28-09-17","KJsd f3");
        list.add(event2);
        CalendarEvents event3 = new CalendarEvents("Wednesday 27-09-17","KJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdbKJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdbKJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdbKJSD kjasdasjkdksjaDBAKSJ jksakdj BAKSJdb ");
        list.add(event3);
        CalendarEvents event4 = new CalendarEvents("Tuesday 26-09-17","KJSD kjasdasjkdksjaDBAKSJ jksakd ");
        list.add(event4);
        list.add(event3);
        list.add(event3);
        list.add(event3);
        list.add(event3);
        list.add(event);
        return list;
    }


}

