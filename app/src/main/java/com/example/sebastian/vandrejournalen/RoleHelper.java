package com.example.sebastian.vandrejournalen;

import android.support.v4.app.Fragment;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Results.ResultsPager;
import com.example.sebastian.vandrejournalen.calendar.Appointment;
import com.example.sebastian.vandrejournalen.calendar.AppointmentFragment;
import com.example.sebastian.vandrejournalen.calendar.CalendarTab;
import com.example.sebastian.vandrejournalen.calendar.Schedule;

import java.util.ArrayList;

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
                return Schedule.newInstance(role);
            case "MW":
                return SearchFragment.newInstance(role);
            case "DR":
                return Schedule.newInstance(role);
            default:
                return null;
        }
    }
    public static Fragment getSlidingFragment(String role, Appointment appointment) {
        switch (role) {
            case "PL":
                return AppointmentFragment.newInstance(role,appointment);
            case "MW":
                return ResultsPager.newInstance(role);
            case "DR":
                return Schedule.newInstance(role);
            default:
                return null;
        }
    }

    public static int getAppointmentLayout(String role){
        switch(role){
            case "PL":
                return R.layout.fragment_appointment;
            case "MW":
                return R.layout.mw_content_main;
            case "DR":
                return R.menu.mw_drawer;
            default:
                return R.menu.activity_main_drawer;
        }
    }

    public static ArrayList<Appointment> getAllAppointments(String role){

        ArrayList<Appointment> allAppointments = new ArrayList<Appointment>();
        final Appointment event = new Appointment(17,11,2017,"13.00","Læge");
        event.setDate(17,11,2017, 13,00);
        final Appointment event2 = new Appointment(24,11,2017,"11.00","Jordemoder");
        event2.setDate(24,11,2017,11,00);

        final Appointment event3 = new Appointment(28,11,2017,"11.00","Jordemoder");
        event3.setDate(28,11,2017,11,00);

        final Appointment event4 = new Appointment(27,11,2017,"11.00","Jordemoder");
        event4.setDate(27,11,2017,11,00);

        final Appointment event5 = new Appointment(6,12,2017,"11.00","Jordemoder");
        event5.setDate(6,12,2017,11,00);

        allAppointments.add(event);
        allAppointments.add(event2);
        allAppointments.add(event3);
        allAppointments.add(event4);
        allAppointments.add(event5);

                event.setFullName("Lars");
                event2.setFullName("Hanne");
                event3.setFullName("Gitte");
                event4.setFullName("Peter");
                event5.setFullName("Jussi");

        return allAppointments;

    }
}
