package com.example.sebastian.vandrejournalen.Results;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;


import static android.content.ContentValues.TAG;

/**
 * Created by Sebastian on 06-12-2017.
 */

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    Calendar c;
    int day,month,year,hour, min;
    private OnFragmentInteractionListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        this.day = day;
        this.month = month;
        this.year= year;

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        new TimePickerDialog(getActivity(), this, hour, minute, true).show();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            Log.d(TAG, "onAttach: Not instance of");
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        this.hour = i;
        this.min = i1;
        Log.d(TAG, "onTimeSet: "+day+month+year+hour+day);
        //mListener.setDate(day,month,year,hour,min);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void setDate(int day,int month,int year, int hour, int min);
    }

}