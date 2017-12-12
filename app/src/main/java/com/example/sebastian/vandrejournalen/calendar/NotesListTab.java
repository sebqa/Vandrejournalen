package com.example.sebastian.vandrejournalen.calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.RoleHelper;
import com.example.sebastian.vandrejournalen.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Sebastian on 30-12-2016.
 */

public class NotesListTab extends Fragment {

    private static final String TAG = "NOTESLISTTAB";
    EditText input;
    Button showBtn;
    SharedPreferences sharedPrefs;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    User user;

    ArrayList<Note> notesList;
    Calendar calendar;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            user = gson.fromJson(getArguments().getString("user"), User.class);        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_noteslist,container,false);
        setHasOptionsMenu(true);
        recyclerView =  rootView.findViewById(R.id.recentRecyclerView);
        calendar = Calendar.getInstance(TimeZone.getDefault());
        Gson gson = new Gson();
        notesList = new ArrayList<Note>();

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(sharedPrefs !=null) {
            String json = sharedPrefs.getString("notes"+user.getUserID(), null);
            Type type = new TypeToken<ArrayList<Note>>() {
            }.getType();
            notesList = gson.fromJson(json, type);
            if(notesList != null) {
                initList();
            }
        }

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("New note for today");


                // Set up the input
                InputFilter[] inputFilter = new InputFilter[1];
                inputFilter[0] = new InputFilter.LengthFilter(100);
                final MaterialEditText input = new MaterialEditText(getActivity());

                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                input.setSingleLine(false);
                input.setMaxCharacters(100);
                input.setFilters(inputFilter);
                builder.setView(input);
                input.requestFocus();


                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Note note = new Note();
                        note.setDate(calendar.getTime());
                        note.setText(input.getText().toString());
                        if(notesList != null) {
                            initList();

                        } else{
                            notesList = new ArrayList<Note>();
                            initList();
                        }
                        notesList.add(0,note);

                        adapter.notifyDataSetChanged();
                        saveNotes();

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        getActivity().getWindow().setSoftInputMode(
                                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                        );
                    }
                });
                builder.show();
            }


        });


        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        //Detach listeners
        saveNotes();
    }

    public static NotesListTab newInstance() {
        NotesListTab fragment = new NotesListTab();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void saveNotes(){
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(notesList);
        editor.putString("notes"+user.getUserID(), json);
        editor.apply();
    }

    public void initList(){

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));

        adapter = new RecyclerAdapter(notesList, getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


}
