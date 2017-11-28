package com.example.sebastian.vandrejournalen.calendar;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.sebastian.journalapp.R;

/**
 * Created by Sebastian on 03-02-2017.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    Context context;
    String role;

    public MyPagerAdapter(FragmentManager fragmentManager, Context mContext, String role) {
        super(fragmentManager);
        context = mContext;
        this.role = role;
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
            case 0: // Fragment # 0 - This will show FirstFragment
                return CalendarTab.newInstance(role);
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return NotesListTab.newInstance(role);

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
