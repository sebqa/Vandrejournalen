package com.example.sebastian.vandrejournalen.Results;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.sebastian.vandrejournalen.Patient;
import com.example.sebastian.vandrejournalen.User;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Sebastian on 23-11-2017.
 */

public class ResultsPagerAdapter extends FragmentStatePagerAdapter {
    private static final String TAG = "RESULTSPAGERADAPTER";
    User user;
    ArrayList<Consultation> arraylist;
    Context context;
    Patient patient;
    public ResultsPagerAdapter(FragmentManager fragmentManager, Context mContext, ArrayList<Consultation> arraylist, User user, Patient patient) {
        super(fragmentManager);
        this.arraylist = arraylist;
        this.user = user;
        this.patient = patient;
        this.context = mContext;

    }
    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        Fragment fragment = ResultsFragment.newInstance(user, arraylist.get(position));
        String obj2 = new Gson().toJson(user);
        args.putString("user" , obj2);
        String obj = new Gson().toJson(arraylist.get(position));
        args.putString("obj" , obj);
        String obj3 = new Gson().toJson(patient);
        args.putString("patient" , obj3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }
}
