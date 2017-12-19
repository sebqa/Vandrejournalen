package com.example.sebastian.vandrejournalen.calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.MainActivity;
import com.example.sebastian.vandrejournalen.RoleHelper;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import br.com.jpttrindade.calendarview.view.CalendarView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class CalendarTab extends Fragment {

    private static final String TAG = "CALENDARTAB";
    public CalendarView calendarView;
    ArrayList<Appointment> arrayList = new ArrayList<Appointment>();
    private CalendarTab.OnFragmentInteractionListener mListener;
    User user;
    FloatingActionButton fab;
    Context context;
    ServerClient client;
    int day,month,year,hour,min;
    MaterialEditText etDate;
    Dialog dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Get object from Bundle
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

        //Create Http Client
        client = ServiceGenerator.createService(ServerClient.class);

        //Show or hide fab based on user role
        if(user.getRole().equals("Patient")){
            fab.setVisibility(View.INVISIBLE);
        } else{
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createAppointment();
                }
            });
        }
        getAppointments();
        return rootView;

    }

    private void createAppointment() {
        //Create a new dialog
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.new_appointment_layout);
        dialog.setTitle(R.string.newAppoi);

        // set the custom dialog components - input fields and button
        etDate = dialog.findViewById(R.id.etDate);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showDatePickerDialog();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        final MaterialEditText etCPR = dialog.findViewById(R.id.etCPR);

        //prevent characters from obscuring
        etCPR.setTransformationMethod(null);

        Button dialogButton =  dialog.findViewById(R.id.dialogButton);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etCPR.getText().toString().length() >=10) {
                    sendAppointment(etCPR.getText().toString());
                }
            }
        });
    }


    private void sendAppointment(String cpr) {
        //Create Appointment object
        final Appointment appointment = new Appointment();

        //Set date attribute to today's date
        appointment.setDate( new GregorianCalendar(year, month-1, day).getTime());

        //Check format of CPR-number
        if (cpr.contains("-")){
            appointment.setCpr(cpr);
        } else{
            StringBuilder str = new StringBuilder(cpr);
            str.insert(6,"-");
            appointment.setCpr(str.toString());
        }

        //Add prof's attributes to object
        appointment.setProfUserID(user.getUserID());
        appointment.setProfRole(user.getRole());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //Add the date in correct format
        appointment.setDateString(dateFormat.format(appointment.getDate()));

        //Network call to send appointment to server
        Call<String> call = client.postAppointment("addAppointment.php", appointment);
        //Listen for response
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Check response content
                if (response.body() != null) {
                    //Check response content value
                    if (response.body().trim().equals("TRUE")) {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                        //Close dialog
                        dialog.dismiss();
                        //Reload appointments
                        getAppointments();
                    } else{
                        Toast.makeText(context, R.string.cantcreate, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void showDatePickerDialog() throws ParseException {
        //Create instance of Calendar
        final Calendar c = Calendar.getInstance();

        //Get current date
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        //Make sure the language is appropriate
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);
        //Create new picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    //Check if the click has been registered already. There is bug with this.(Not our fault)
                    boolean mFirst = true;

                    @Override
                    public void onDateSet(DatePicker view, int nyear,
                                          int monthOfYear, int dayOfMonth) {

                        if (mFirst) {
                            mFirst = false;
                            //Set values to picked ones
                            year = nyear;
                            month = monthOfYear+1;
                            day = dayOfMonth;

                            showTimePickerDialog(c);
                        }
                    }
                }, year, month, day);

        //Set date of dialog
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = sdf.parse(day+"/"+(month+1)+"/"+year);
        datePickerDialog.getDatePicker().setMinDate(d.getTime());
        datePickerDialog.show();


    }

    public void showTimePickerDialog(Calendar c){

        //Get current time
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    //Check if first time running
                    boolean mFirst = true;

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (mFirst) {
                            mFirst = false;

                            //Set values to picked ones
                            hour = hourOfDay;
                            min = minute;
                            etDate.setText(day+"/"+month+"/"+year+" - "+ hour+":"+min);
                        }
                    }
                }, hour, min, false);
        timePickerDialog.show();
    }

    private void setUpCalendar() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());

        //Put all appointments in list
        for(int i=0; i<arrayList.size();i++){
            Appointment appointment = arrayList.get(i);
            //Set the calendar's time to that of the appointment retrieved
            cal.setTime(appointment.getDate());

            //Get current date
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH)+1;
            int day = cal.get(Calendar.DAY_OF_MONTH);

            //Add the date values to the object
            appointment.setDay(day);
            appointment.setMonth(month);
            appointment.setYear(year);

            //Add object to calendar
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
                //If there is, add this event to thisDayList
                if (hasEvent) {
                    for (int i=0; i< arrayList.size();i++){
                        if(day == arrayList.get(i).getDay() && month == arrayList.get(i).getMonth() && year == arrayList.get(i).getYear()){
                            thisDayList.add(arrayList.get(i));
                        }
                    }
                    //Callback to do something with this appointment
                    mListener.onDateClick(thisDayList);
                } else{
                    //Callback to "collapse" sliding panel
                    mListener.removePreview();
                }
            }
        });
    }

    private void getAppointments() {
        //Network call to retrieve all appointments for this user
        Call<ArrayList<Appointment>> call = client.getAppointments("returnAppointments.php", user );
        //Listen for response
        call.enqueue(new Callback<ArrayList<Appointment>>() {
            @Override
            public void onResponse(Call<ArrayList<Appointment>> call, Response<ArrayList<Appointment>> response) {
                //Check response content
                if(response.body() != null){
                    //Reset the list of appointments
                    arrayList.clear();
                    //Add all appointments from response contents to list
                    arrayList.addAll(response.body());

                    //Make sure list is not empty
                    if(!arrayList.isEmpty()){
                        getActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run(){
                                setUpCalendar();
                                //Callback to set the names of the professionals
                                mListener.setNames(arrayList.get(0).getJournalMidwifeName(), arrayList.get(0).getJournalSpecialistName());
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
        //Interface methods
        void onDateClick(ArrayList<Appointment> arrayList);
        void onToday(ArrayList<Appointment> arrayList, int pos);
        void removePreview();
        void setNames(String midwife, String specialist);
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
