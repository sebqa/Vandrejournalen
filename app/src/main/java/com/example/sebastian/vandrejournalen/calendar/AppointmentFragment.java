package com.example.sebastian.vandrejournalen.calendar;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.PLActivity;


public class AppointmentFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String apDate, apText;
    private TextView previewDate, appointText;

    private View myFragmentView;


    public AppointmentFragment() {
        // Required empty public constructor
    }

    public static AppointmentFragment newInstance(String date, String text) {
        AppointmentFragment fragment = new AppointmentFragment();
        Bundle args = new Bundle();
        args.putString("date", date);
        args.putString("text", text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            apDate = getArguments().getString("date");
            apText = getArguments().getString("text");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.fragment_appointment, container, false);
        previewDate = myFragmentView.findViewById(R.id.previewDate);
        appointText = myFragmentView.findViewById(R.id.appointTitle);

        previewDate.setText(apDate);
        appointText.setText(apText);

        return myFragmentView;
    }

}
