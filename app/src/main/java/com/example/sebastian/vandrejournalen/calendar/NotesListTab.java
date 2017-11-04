package com.example.sebastian.vandrejournalen.calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;

/**
 * Created by Sebastian on 30-12-2016.
 */

public class NotesListTab extends Fragment {

    EditText input;
    Button showBtn;

    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<CalendarEvent> arrayList = new ArrayList<CalendarEvent>();
    Schedule calendar = new Schedule();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_noteslist,container,false);
        setHasOptionsMenu(true);
        recyclerView =  rootView.findViewById(R.id.recentRecyclerView);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alerttdialog box
            AlertDialog.Builder notesBuilder = new AlertDialog().Builder(this);
            notesBuilder.setMessage(getResources().getString(R.string.new_note));
               /* Snackbar.make(view, "Add new note", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/


            }
        });
        initList();
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        //Detach listeners
    }

    public static NotesListTab newInstance() {
        NotesListTab fragment = new NotesListTab();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void initList(){

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        adapter = new RecyclerAdapter(calendar.getAllEvents(), getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


}
