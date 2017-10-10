package com.example.sebastian.vandrejournalen.calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by Sebastian on 03-02-2017.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
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
                return UpcomingTab.newInstance();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return RecentTab.newInstance();
            case 2: // Fragment # 1 - This will show SecondFragment
                return CalendarTab.newInstance();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0){
            return "Upcoming";
        } else if(position == 1) {
            return "Recent";
        }
         else return "Schedule";
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position,  object);
    }}
