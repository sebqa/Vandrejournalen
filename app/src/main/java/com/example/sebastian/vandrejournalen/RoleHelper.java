package com.example.sebastian.vandrejournalen;

import android.support.v4.app.Fragment;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.calendar.Appointment;
import com.example.sebastian.vandrejournalen.calendar.AppointmentFragment;
import com.example.sebastian.vandrejournalen.calendar.CalendarTab;
import com.example.sebastian.vandrejournalen.calendar.NotesListTab;
import com.example.sebastian.vandrejournalen.calendar.Schedule;

/**
 * Created by Sebastian on 07-11-2017.
 */

public class RoleHelper {

    public static int getOptionsMenu(String role){
        switch(role){
            case "PL":
                return R.menu.pl_drawer;
            case "MW":
                return R.menu.mw_drawer;
            case "DR":
                return R.menu.mw_drawer;
            default:
                return R.menu.activity_main_drawer;
        }
    }
    public static int getContentmain(String role){
        switch(role){
            case "PL":
                return R.layout.content_main;
            case "MW":
                return R.layout.mw_content_main;
            case "DR":
                return R.menu.mw_drawer;
            default:
                return R.menu.activity_main_drawer;
        }
    }

    public static Fragment getMainFragment(String role) {
        switch (role) {
            case "PL":
                return Schedule.newInstance();
            case "MW":
                return CalendarTab.newInstance();
            case "DR":
                return Schedule.newInstance();
            default:
                return null;
        }
    }
    public static Fragment getSlidingFragment(String role, Appointment appointment) {
        switch (role) {
            case "PL":
                return AppointmentFragment.newInstance(appointment.getDay()+"/"+ appointment.getMonth()+"/"+ appointment.getYear()+"\n"+appointment.getTime(),appointment.getEvent());
            case "MW":
                return Schedule.newInstance();
            case "DR":
                return Schedule.newInstance();
            default:
                return null;
        }
    }
}
