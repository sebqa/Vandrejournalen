package com.example.sebastian.vandrejournalen.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sebastian.journalapp.R;


public class EventPreview extends Fragment {

    private CalendarEvents event;
    TextView previewText,blodtryk;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.eventpreview,null);
        setHasOptionsMenu(true);

        Log.d("EVNENTNETNET","WERWERWER");
        event = (CalendarEvents) getArguments().getSerializable("event");
        previewText = rootView.findViewById(R.id.previewText);
        blodtryk = rootView.findViewById(R.id.blodtryk);
        event.setBlodtryk("120 over 80");


        previewText.setText(event.getDate());
        blodtryk.setText(event.getBlodtryk());
        return rootView;

    }


    @Override
    public void onStop() {
        super.onStop();
        //Detach listeners
    }

    public static EventPreview newInstance(CalendarEvents calendarEvents) {
        EventPreview fragment = new EventPreview();
        Bundle args = new Bundle();
        args.putSerializable("event",calendarEvents);
        fragment.setArguments(args);
        return fragment;
    }


}
