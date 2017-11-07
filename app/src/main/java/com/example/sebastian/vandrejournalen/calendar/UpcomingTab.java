package com.example.sebastian.vandrejournalen.calendar;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.sebastian.journalapp.R;

import java.util.ArrayList;

/**
 * Created by Sebastian on 30-12-2016.
 */

public class UpcomingTab extends Fragment {

    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Appointment> arrayList = new ArrayList<Appointment>();
    Schedule calendar = new Schedule();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_upcoming, container, false);
        setHasOptionsMenu(true);
        recyclerView =  rootView.findViewById(R.id.upcomingRecyclerView);

        initList();
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        //Detach listeners
    }



    public static UpcomingTab newInstance() {
        UpcomingTab fragment = new UpcomingTab();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public void initList(){

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        adapter = new RecyclerAdapter(calendar.getUpcomingEvents(), getActivity());

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }



}

