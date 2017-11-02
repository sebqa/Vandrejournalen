package com.example.sebastian.vandrejournalen.calendar;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.authentication.LoginFragment;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import br.com.jpttrindade.calendarview.view.CalendarView;

public class CalendarTab extends Fragment {
    public CalendarView calendarView;
    ArrayList<CalendarEvent> arrayList = new ArrayList<CalendarEvent>();
    Schedule calendar = new Schedule();

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private CalendarTab.OnFragmentInteractionListener mListener;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendartab,container,false);
        setHasOptionsMenu(true);
        calendarView =  rootView.findViewById(R.id.calendarView);

        calendar.getRecentEvents();

        for (int i=0;i<arrayList.size();i++){
            arrayList.get(i).getDate();
        }


        calendarView.addEvent(3,11,2017);

        calendarView.addEvent(6,11,2017);
        calendarView.addEvent(9,11,2017);

        calendarView.setOnDayClickListener(new CalendarView.OnDayClickListener() {
            @Override
            public void onClick(int day, int month, int year, boolean hasEvent) {
                //Toast.makeText(getActivity(), day+"/"+month+"/"+year + " hasEvent="+hasEvent, Toast.LENGTH_SHORT).show();
                if (hasEvent) {
                    mListener.showPreview(new CalendarEvent(""+day+"/"+month+"/"+year,"FEEEEST"));
                } else{
                    mListener.removePreview();
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


    public interface OnFragmentInteractionListener {
        void showPreview(CalendarEvent calendarEvent);

        void removePreview();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CalendarTab.OnFragmentInteractionListener) {
            mListener = (CalendarTab.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
