package com.example.sebastian.vandrejournalen.calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.MainActivity;
import com.example.sebastian.vandrejournalen.RoleHelper;
import com.example.sebastian.vandrejournalen.User;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import br.com.jpttrindade.calendarview.view.CalendarView;

public class CalendarTab extends Fragment {
    public CalendarView calendarView;
    ArrayList<Consultation> arrayList = new ArrayList<Consultation>();
    private CalendarTab.OnFragmentInteractionListener mListener;
    User user;
    Context context;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            user = gson.fromJson(getArguments().getString("obj"), User.class);
        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_calendartab,container,false);
        setHasOptionsMenu(true);
        calendarView =  rootView.findViewById(R.id.calendarView);
        calendarView.setLanguage(MainActivity.language);

        getActivity().runOnUiThread(new Runnable(){
            @Override
            public void run(){
                setUpCalendar();

            }
        });

        return rootView;

    }

    private void setUpCalendar() {
        //Get appointments based on role somehow. Maybe from rolehelper.
        User user = new User();

        arrayList = RoleHelper.getAllAppointments(user);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

        //Put all appointments in list
        for(int i=0; i<arrayList.size();i++){
            Consultation consultation = arrayList.get(i);
            calendarView.addEvent(consultation.getDay(), consultation.getMonth(), consultation.getYear());
            //Check if consultation is today
            if(consultation.getDay() ==calendar.get(Calendar.DAY_OF_MONTH)){

                if(consultation.getMonth()== calendar.get(Calendar.MONTH)+1){
                    //show today's consultation
                    mListener.onToday(arrayList,i);
                }
            }
        }

        calendarView.setOnDayClickListener(new CalendarView.OnDayClickListener() {
            @Override
            public void onClick(int day, int month, int year, boolean hasEvent) {
                //Check if there is an event on this day
                ArrayList<Consultation> thisDayList = new ArrayList<>();

                if (hasEvent) {
                    for (int i=0; i< arrayList.size();i++){
                        if(day == arrayList.get(i).getDay() && month == arrayList.get(i).getMonth() && year == arrayList.get(i).getYear()){
                            thisDayList.add(arrayList.get(i));


                        }

                    }
                    mListener.onDateClick(thisDayList);
                } else{
                    mListener.removePreview();
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        //Detach listeners

    }

    @Override
    public void onPause() {
        super.onPause();



    }

    @Override
    public void onResume() {
        super.onResume();


    }


    public static CalendarTab newInstance(User user) {
        CalendarTab fragment = new CalendarTab();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String obj = gson.toJson(user);
        args.putString("obj" , obj);
        fragment.setArguments(args);
        return fragment;
    }


    public interface OnFragmentInteractionListener {
        void onDateClick(ArrayList<Consultation> arrayList);
        void onToday(ArrayList<Consultation> arrayList, int pos);
        void removePreview();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof CalendarTab.OnFragmentInteractionListener) {
            mListener = (CalendarTab.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
