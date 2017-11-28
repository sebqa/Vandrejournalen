package com.example.sebastian.vandrejournalen.Results;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.sebastian.vandrejournalen.calendar.Appointment;

import java.util.ArrayList;

/**
 * Created by Sebastian on 23-11-2017.
 */

public class ResultsPagerAdapter extends FragmentStatePagerAdapter {
    String role;
    ArrayList<Appointment> arraylist;
    public ResultsPagerAdapter(FragmentManager fragmentManager, Context mContext, ArrayList<Appointment> arraylist, String role) {
        super(fragmentManager);
        this.arraylist = arraylist;
        this.role = role;
    }
    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("role",role);
        Fragment fragment = ResultsFragment.newInstance(role);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }
}
