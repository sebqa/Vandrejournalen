package com.example.sebastian.vandrejournalen.calendar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.RoleHelper;
import com.example.sebastian.vandrejournalen.User;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class AppointmentFragment extends Fragment  {
    TextView apText;
    Appointment appointment;
    User user;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    public static AppointmentFragment newInstance() {
        AppointmentFragment fragment = new AppointmentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appointment = new Gson().fromJson(getArguments().getString("appointment","Patient"),Appointment.class);
            user = new Gson().fromJson(getArguments().getString("user","Patient"),User.class);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_appointment, container, false);
       if(user.getRole().equals("Patient")) {
           TextView previewDate = rootView.findViewById(R.id.previewDate);
           TextView appointTitle = rootView.findViewById(R.id.appointTitle);

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy\nkk:mm:ss");

            previewDate.setText(formatter.format(appointment.getDate()));

            if(appointment.getName() != null){
                appointTitle.setText(appointment.getName());
            }



            apText = rootView.findViewById(R.id.appointText);

           RecyclerView recyclerView = rootView.findViewById(R.id.apNotesList);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                    DividerItemDecoration.VERTICAL));

        }


        return rootView;

    }


}
