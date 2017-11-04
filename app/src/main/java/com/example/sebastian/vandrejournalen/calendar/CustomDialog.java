package com.example.sebastian.vandrejournalen.calendar;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.example.sebastian.journalapp.R;

/**
 * Created by Sebastian on 04-11-2017.
 */

public class CustomDialog extends DialogFragment {
    LayoutInflater inflater;
    View v;


    public static CustomDialog newInstance(String msg)
    {
        CustomDialog frag = new CustomDialog();
        Bundle args = new Bundle();
        args.putString("message", msg);
        frag.setArguments(args);

        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String message = getArguments().getString("message");

        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.new_note,null);


        return super.onCreateDialog(savedInstanceState);
    }}
