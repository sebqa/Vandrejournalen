package com.example.sebastian.vandrejournalen;

import android.content.Context;
import android.content.res.Resources;
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

    public static int getOptionsMenu(User user){
        String role = user.getRole();
        switch(role){
            case "Patient":
                return R.menu.pl_drawer;
            case "Midwife":
                return R.menu.mw_drawer;
            case "General Practitioner":
                return R.menu.dr_drawer;
            case "Specialist":
                return R.menu.dr_drawer;
            default:
                return R.menu.activity_main_drawer;
        }
    }
    public static int getContentmain(User user){
        String role = user.getRole();
        switch(role){
            case "Patient":
                return R.layout.content_main;
            case "Midwife":
                return R.layout.mw_content_main;
            case "General Practitioner":
                return R.menu.mw_drawer;
            case "Specialist":
                return R.menu.dr_drawer;
            default:
                return R.menu.activity_main_drawer;
        }
    }

    public static Fragment getMainFragment(User user) {
        String role = user.getRole();
        switch (role) {
            case "Patient":
                return Schedule.newInstance(role);
            case "Midwife":
                return SearchFragment.newInstance(user);
            case "General Practitioner":
                return SearchFragment.newInstance(user);
            case "Specialist":
                return SearchFragment.newInstance(user);
            default:
                return null;
        }
    }
    public static Fragment getSlidingFragment(User user, Appointment appointment) {
        String role = user.getRole();
        switch (role) {
            case "Patient":
                return AppointmentFragment.newInstance(user,appointment);
            case "Midwife":
                return ResultsPager.newInstance(user);
            case "General Practitioner":
                return ResultsPager.newInstance(user);
            case "Specialist":
                return ResultsPager.newInstance(user);
            default:
                return null;
        }
    }

    public static int getAppointmentLayout(User user){
        String role = user.getRole();
        switch(role){
            case "Patient":
                return R.layout.fragment_appointment;
            case "Midwife":
                return R.layout.mw_content_main;
            case "General Practitioner":
                return R.layout.mw_content_main;
            case "Specialist":
                return R.layout.mw_content_main;
            default:
                return R.menu.activity_main_drawer;
        }
    }


    public static String translateRole(String role){
        switch(role){
            case "Patient":
                return "Patient";
            case "Midwife":
                return "Jordemoder";
            case "General Practitioner":
                return "Praktiserende læge";

        }
        return "Specialist";
    }

    public static ArrayList<Appointment> getAllAppointments(User user){
        String role = user.getRole();
        ArrayList<Appointment> allAppointments = new ArrayList<Appointment>();
        final Appointment event = new Appointment(3,12,2017,"13.00","Læge");
        event.setDate(3,12,2017, 13,00);
        final Appointment event2 = new Appointment(1,12,2017,"11.00","Jordemoder");
        event2.setDate(1,12,2017,11,00);

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
