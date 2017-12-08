package com.example.sebastian.vandrejournalen.calendar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.Locale;


public class AppointmentFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String apDate, apTitle;
    private TextView previewDate, appointTitle;
    RecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private View rootView;
    private RecyclerView recyclerView;
    TextView apText;
    Appointment appointment;
    User user;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    public static AppointmentFragment newInstance(User user, Appointment appointment) {
        AppointmentFragment fragment = new AppointmentFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            appointment = new Gson().fromJson(getArguments().getString("appointment","Midwife"),Appointment.class);

            user = new Gson().fromJson(getArguments().getString("user","Midwife"),User.class);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(RoleHelper.getAppointmentLayout(user), container, false);
        if(user.getRole().equals("Patient")) {
            previewDate = rootView.findViewById(R.id.previewDate);
            appointTitle = rootView.findViewById(R.id.appointTitle);

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy\nkk:mm");


            previewDate.setText(formatter.format(appointment.getDate()));
            appointTitle.setText(appointment.getEvent());

            apText = rootView.findViewById(R.id.appointText);

            recyclerView = rootView.findViewById(R.id.apNotesList);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                    DividerItemDecoration.VERTICAL));
            adapter = new RecyclerAdapter(RoleHelper.getAllAppointments(user), getActivity());
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }


        return rootView;

    }



    public interface OnFragmentInteractionListener {
        void makeScrollable(View view);
    }


}
