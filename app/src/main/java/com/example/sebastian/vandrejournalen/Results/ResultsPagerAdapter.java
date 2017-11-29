package com.example.sebastian.vandrejournalen.Results;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.sebastian.vandrejournalen.calendar.Appointment;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Sebastian on 23-11-2017.
 */

public class ResultsPagerAdapter extends FragmentStatePagerAdapter {
    String role;
    ArrayList<Appointment> arraylist;
    Context context;
    public ResultsPagerAdapter(FragmentManager fragmentManager, Context mContext, ArrayList<Appointment> arraylist, String role) {
        super(fragmentManager);
        this.arraylist = arraylist;
        this.role = role;
        this.context = mContext;
    }
    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("role",role);
        Gson gson = new Gson();
        String obj = gson.toJson(arraylist.get(position));
        args.putString("obj", obj);
        Fragment fragment = ResultsFragment.newInstance(role, arraylist.get(position));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }
}
