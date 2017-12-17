package com.example.sebastian.vandrejournalen;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Results.ResultsPager;
import com.example.sebastian.vandrejournalen.calendar.Appointment;
import com.example.sebastian.vandrejournalen.Results.Consultation;
import com.example.sebastian.vandrejournalen.calendar.AppointmentFragment;
import com.example.sebastian.vandrejournalen.calendar.Schedule;
import com.example.sebastian.vandrejournalen.calendar.SearchFragment;

import java.util.ArrayList;

/**
 * Created by Sebastian on 07-11-2017.
 */

public class RoleHelper {

    private static final String TAG = "ROLEHELPER";

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
                return R.menu.mw_drawer;
            default:
                return R.menu.activity_main_drawer;
        }
    }

    public static int getTheme(User user){
        String role = user.getRole();
        int theme = 0;
        switch (role){
            case "Patient":
                theme = R.style.PinkTheme;
                break;
            case "General Practitioner":
                theme = R.style.BlueTheme;
                break;
            case "Midwife":
                theme = R.style.YellowTheme;
                break;
            case "Specialist":
                theme = R.style.GreenTheme;
        }
        return theme;
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
        Log.d(TAG, "getMainFragment: "+user.getRole());
        switch (user.getRole()) {
            case "Patient":
                return Schedule.newInstance();
            case "Midwife":
                return SearchFragment.newInstance();
            case "General Practitioner":
                return SearchFragment.newInstance();
            case "Specialist":
                return SearchFragment.newInstance();
            default:
                return null;
        }
    }
    public static Fragment getSlidingFragment(User user, Appointment appointment) {
        String role = user.getRole();
        switch (role) {
            case "Patient":
                return AppointmentFragment.newInstance();
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

    public static ArrayList<Consultation> getAllAppointments(User user){
        String role = user.getRole();
        ArrayList<Consultation> allConsultations = new ArrayList<Consultation>();
        final Consultation event = new Consultation(3,12,2017,"13.00","Læge");
        event.setDate(3,12,2017);
        final Consultation event2 = new Consultation(1,12,2017,"11.00","Jordemoder");
        event2.setDate(1,12,2017);

        final Consultation event3 = new Consultation(28,11,2017,"11.00","Jordemoder");
        event3.setDate(28,11,2017);

        final Consultation event4 = new Consultation(27,11,2017,"11.00","Jordemoder");
        event4.setDate(27,11,2017);

        final Consultation event5 = new Consultation(6,12,2017,"11.00","Jordemoder");
        event5.setDate(6,12,2017);

        allConsultations.add(event);
        allConsultations.add(event2);
        allConsultations.add(event3);
        allConsultations.add(event4);
        allConsultations.add(event5);

                event.setFullName("Lars");
                event2.setFullName("Hanne");
                event3.setFullName("Gitte");
                event4.setFullName("Peter");
                event5.setFullName("Jussi");

        return allConsultations;

    }
}
