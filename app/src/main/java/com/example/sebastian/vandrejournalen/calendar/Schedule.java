package com.example.sebastian.vandrejournalen.calendar;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sebastian.journalapp.R;

import java.util.ArrayList;

/**
 * Created by Sebastian on 02-11-2016.
 */

public class Schedule extends Fragment  {
    FragmentPagerAdapter adapterViewPager;
    String role;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            role = getArguments().getString("role");
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_schedule,container,false);

        ViewPager vpPager = rootView.findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getFragmentManager(),getContext(),role);
        vpPager.setAdapter(adapterViewPager);
        vpPager.setOffscreenPageLimit(3);



        // Give the TabLayout the ViewPager
        TabLayout tabLayout = rootView.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);




        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();

    }




    public static Schedule newInstance(String role) {
        Schedule fragment = new Schedule();
        Bundle args = new Bundle();
        args.putString("role",role);
        fragment.setArguments(args);
        return fragment;
    }

}

