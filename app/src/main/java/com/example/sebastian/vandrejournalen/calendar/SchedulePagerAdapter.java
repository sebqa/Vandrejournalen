package com.example.sebastian.vandrejournalen.calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.User;
import com.google.gson.Gson;

/**
 * Created by Sebastian on 03-02-2017.
 */
public class SchedulePagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    Context context;
    User user;

    public SchedulePagerAdapter(FragmentManager fragmentManager, Context mContext, User user) {
        super(fragmentManager);
        context = mContext;
        this.user = user;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // This will show CalendarTab
                CalendarTab fragment = CalendarTab.newInstance();
                Bundle args = new Bundle();
                String obj = new Gson().toJson(user);
                args.putString("user" , obj);
                fragment.setArguments(args);
                return fragment;
            case 1: // This will show NotesListTab
                NotesListTab fragment2 = NotesListTab.newInstance();
                Bundle args2 = new Bundle();
                String obj2 = new Gson().toJson(user);
                args2.putString("user" , obj2);
                fragment2.setArguments(args2);

                return fragment2;
            default:
                return null;
        }
    }
    @Override
    public long getItemId(int position) {
        return System.currentTimeMillis();
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return context.getResources().getString(R.string.calendar);
        } else {
            return context.getResources().getString(R.string.notes);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position,  object);
    }}
