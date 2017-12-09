package com.example.sebastian.vandrejournalen.Results;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.RoleHelper;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.calendar.RecyclerAdapter;
import com.google.gson.Gson;

/**
 * Created by Sebastian on 30-12-2016.
 */

public class PatientsList extends Fragment {

    EditText input;
    Button showBtn;

    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    User user;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            user = gson.fromJson(getArguments().getString("obj"), User.class);        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patients_list,container,false);
        setHasOptionsMenu(true);
        recyclerView =  rootView.findViewById(R.id.recyclerView);

        initList();
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        //Detach listeners
    }

    public static PatientsList newInstance(User user) {
        PatientsList fragment = new PatientsList();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String obj = gson.toJson(user);
        args.putString("obj" , obj);
        fragment.setArguments(args);
        return fragment;
    }

    public void initList(){

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        adapter = new RecyclerAdapter(RoleHelper.getAllAppointments(user), getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


}
