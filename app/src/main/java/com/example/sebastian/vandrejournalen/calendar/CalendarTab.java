package com.example.sebastian.vandrejournalen.calendar;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sebastian.journalapp.R;


import java.text.DateFormat;
import java.text.SimpleDateFormat;


import br.com.jpttrindade.calendarview.view.CalendarView;

public class CalendarTab extends Fragment {
    public CalendarView calendarView;

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendartab,container,false);
        setHasOptionsMenu(true);
        calendarView =  rootView.findViewById(R.id.calendarView);
        calendarView.addEvent(16,9,2017);
        calendarView.setOnDayClickListener(new CalendarView.OnDayClickListener() {
            @Override
            public void onClick(int day, int month, int year, boolean hasEvent) {
                Toast.makeText(getActivity(), day+"/"+month+"/"+year + " hasEvent="+hasEvent, Toast.LENGTH_SHORT).show();
                if (hasEvent) {

                }
            }
        });
        return rootView;

    }

    @Override
    public void onStop() {
        super.onStop();
        //Detach listeners

    }

    @Override
    public void onPause() {
        super.onPause();



    }

    @Override
    public void onResume() {
        super.onResume();


    }


    public static CalendarTab newInstance() {
        CalendarTab fragment = new CalendarTab();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }




}
