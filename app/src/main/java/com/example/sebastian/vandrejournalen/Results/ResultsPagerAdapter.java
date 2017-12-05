package com.example.sebastian.vandrejournalen.Results;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.calendar.Appointment;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Sebastian on 23-11-2017.
 */

public class ResultsPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "RESULTSPAGERADAPTER";
    User user;
    ArrayList<Appointment> arraylist;
    Context context;
    public ResultsPagerAdapter(FragmentManager fragmentManager, Context mContext, ArrayList<Appointment> arraylist, User user) {
        super(fragmentManager);
        this.arraylist = arraylist;
        this.user = user;
        this.context = mContext;
    }
    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        Fragment fragment = ResultsFragment.newInstance(user, arraylist.get(position));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }
}
