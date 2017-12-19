package com.example.sebastian.vandrejournalen.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;


import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.SecureUtil;
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
    SharedPreferences sharedPrefs;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    User user;
    SecureUtil secureUtil;
    ArrayList<Note> notesList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Get User from object
            Gson gson = new Gson();
            user = gson.fromJson(getArguments().getString("user"), User.class);        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_noteslist,container,false);
        setHasOptionsMenu(true);
        recyclerView =  rootView.findViewById(R.id.recentRecyclerView);
        notesList = new ArrayList<Note>();
        secureUtil = new SecureUtil(getActivity());

        //Initiate SP
        sharedPrefs = getContext().getSharedPreferences("notes", Context.MODE_PRIVATE);
            //Check if anything is saved
            if(sharedPrefs !=null) {
                //Get the notes as a string
                String json = sharedPrefs.getString("notes" ,null);
                if(json != null){
                    //Check if the string is empty
                    Type type = new TypeToken<ArrayList<Note>>() {
                    }.getType();
                    //Decrypt the string and add the array of notes to the list
                    notesList = new Gson().fromJson(secureUtil.decrypt(json), type);
                }
                if(notesList != null) {
                    initList();
                }
        }

        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Create dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("New note for today");
                final Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                // Set up the input
                InputFilter[] inputFilter = new InputFilter[1];
                inputFilter[0] = new InputFilter.LengthFilter(300);
                final MaterialEditText input = new MaterialEditText(getActivity());
                //Configure input
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                input.setSingleLine(false);
                input.setMaxCharacters(300);
                input.setFilters(inputFilter);
                builder.setView(input);
                input.requestFocus();


                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Add attributes to new note object
                        Note note = new Note();
                        note.setDate(calendar.getTime());
                        note.setText(input.getText().toString());
                        //If the list is not null, show the list
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
                        //Hide keyboard
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
        //Save notes
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        //Convert to string and encrypt notes
        String json = secureUtil.encrypt(gson.toJson(notesList));
        editor.putString("notes", json);
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
