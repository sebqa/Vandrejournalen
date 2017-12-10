package com.example.sebastian.vandrejournalen.calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.MainActivity;
import com.example.sebastian.vandrejournalen.RoleHelper;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.TimeZone;

import br.com.jpttrindade.calendarview.view.CalendarView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarTab extends Fragment {
    private static final String TAG = "CALENDARTAB";
    public CalendarView calendarView;
    ArrayList<Appointment> arrayList = new ArrayList<Appointment>();
    private CalendarTab.OnFragmentInteractionListener mListener;
    User user;
    FloatingActionButton fab;
    Context context;
    ServerClient client;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            user = gson.fromJson(getArguments().getString("user"), User.class);
        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_calendartab,container,false);
        setHasOptionsMenu(true);
        calendarView =  rootView.findViewById(R.id.calendarView);
        calendarView.setLanguage(MainActivity.language);
        fab = rootView.findViewById(R.id.fabStartJournal);
        client = ServiceGenerator.createService(ServerClient.class);
        Log.d(TAG, "onCreateView: "+user.getRole());
        if(user.getRole().equals("Patient")){
            fab.setVisibility(View.INVISIBLE);
        }

        getAppointments();
        return rootView;

    }

    private void setUpCalendar() {
        //Get appointments based on role somehow. Maybe from rolehelper.


        Calendar cal = Calendar.getInstance(TimeZone.getDefault());


        //Put all appointments in list
        for(int i=0; i<arrayList.size();i++){
            Appointment appointment = arrayList.get(i);
            cal.setTime(appointment.getDate());

            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;
            int day = cal.get(Calendar.DAY_OF_MONTH);

            appointment.setDay(day);
            appointment.setMonth(month);
            appointment.setYear(year);

            Log.d(TAG, "setUpCalendar: "+appointment.getDay());
            calendarView.addEvent(appointment.getDay(), appointment.getMonth(), appointment.getYear());
            //Check if consultation is today

            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

            if(appointment.getMonth()== calendar.get(Calendar.MONTH)+1 && appointment.getDay() == calendar.get(Calendar.DAY_OF_MONTH) && appointment.getYear() == calendar.get(Calendar.YEAR)){
                    //show today's consultation
                    mListener.onToday(arrayList,i);
                }
        }

        calendarView.setOnDayClickListener(new CalendarView.OnDayClickListener() {
            @Override
            public void onClick(int day, int month, int year, boolean hasEvent) {
                //Check if there is an event on this day
                ArrayList<Appointment> thisDayList = new ArrayList<>();

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

    private void getAppointments() {
        Call<ArrayList<Appointment>> call = client.getAppointments("returnAppointments.php", user );

        call.enqueue(new Callback<ArrayList<Appointment>>() {
            @Override
            public void onResponse(Call<ArrayList<Appointment>> call, Response<ArrayList<Appointment>> response) {
                if(response.body() != null){
                    arrayList.clear();
                    arrayList.addAll(response.body());
                    Log.d(TAG, "onResponse: "+response.body().get(0).getDate());

                    if(arrayList.isEmpty()&& !user.getRole().equals("Patient")){
                    }
                    if(!arrayList.isEmpty()){
                        getActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                setUpCalendar();
                            }
                        });
                    }

                }

            }

            @Override
            public void onFailure(Call<ArrayList<Appointment>> call, Throwable t) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();

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


    public static CalendarTab newInstance() {
        CalendarTab fragment = new CalendarTab();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    public interface OnFragmentInteractionListener {
        void onDateClick(ArrayList<Appointment> arrayList);
        void onToday(ArrayList<Appointment> arrayList, int pos);
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
