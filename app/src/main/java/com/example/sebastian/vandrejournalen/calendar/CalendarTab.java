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
    MaterialSpinner institutionSpinner;
    ArrayList<String> institutions = new ArrayList<>();
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
        } else{
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getInstitutions();
                }
            });
        }

        getAppointments();
        return rootView;

    }

    private void createAppointment() {
        dialog = new Dialog(context);

        dialog.setContentView(R.layout.new_appointment_layout);
        dialog.setTitle(R.string.newAppoi);
        institutionSpinner = dialog.findViewById(R.id.placeSpinner);
        institutionSpinner.setItems(institutions);

        final MaterialSpinner roleSpinner = dialog.findViewById(R.id.requestedRoleSpinner);

        roleSpinner.setItems(getString(R.string.gp),getString(R.string.mw),"Specialist");





        // set the custom dialog components - text, image and button
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
        etCPR.setTransformationMethod(null);

        Button dialogButton =  dialog.findViewById(R.id.dialogButton);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etCPR.getText().toString().length() >=10) {
                    sendAppointment(etCPR.getText().toString(),institutionSpinner.getText().toString(),roleSpinner.getText().toString());
                }
            }
        });


    }

    private void getInstitutions() {

        Call<ArrayList<String>> call = client.getInstitutions("returnInstitutions.php", user );
        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                Log.d(TAG, "onResponse: "+response.body());
                if (response.body() != null) {
                    institutions.addAll(response.body());
                    Log.d(TAG, "onResponse: "+institutions.get(0));
                    createAppointment();
                    dialog.show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void sendAppointment(String cpr, String institution, String requestedRole) {
        final Appointment appointment = new Appointment();
        appointment.setDate( new GregorianCalendar(year, month-1, day).getTime());
        if (cpr.contains("-")){
            appointment.setCpr(cpr);
        } else{
            StringBuilder str = new StringBuilder(cpr);
            str.insert(6,"-");
            appointment.setCpr(str.toString());
        }
        Log.d(TAG, "sendAppointment: "+appointment.getDate());
        appointment.setInstitution(institution);
        appointment.setRequestedRole(requestedRole);

        Call<String> call = client.postAppointment("addAppointment.php", appointment);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    Log.d(TAG, "onResponse: " + response.body().trim());
                    if (response.body().trim().equals("1")) {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, "onResponse: " + response.body().trim());
                }
                Log.d(TAG, "onResponse: " + response.message());
            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void showDatePickerDialog() throws ParseException {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    boolean mFirst = true;

                    @Override
                    public void onDateSet(DatePicker view, int nyear,
                                          int monthOfYear, int dayOfMonth) {

                        if (mFirst) {
                            mFirst = false;
                            year = nyear;
                            month = monthOfYear+1;
                            day = dayOfMonth;
                            Log.d(TAG, "showDatePickerDialog: "+year+month+day);
                            showTimePickerDialog(c);

                        }



                    }
                }, year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = sdf.parse(day+"/"+(month+1)+"/"+year);
        datePickerDialog.getDatePicker().setMinDate(d.getTime());
        datePickerDialog.show();


    }

    public void showTimePickerDialog(Calendar c){
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    boolean mFirst = true;

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (mFirst) {
                            mFirst = false;
                            hour = hourOfDay;
                            min = minute;
                            etDate.setText(day+"/"+month+"/"+year+" - "+ hour+":"+min);
                        }
                    }
                }, hour, min, false);
        timePickerDialog.show();
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
