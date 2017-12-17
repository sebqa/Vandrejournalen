package com.example.sebastian.vandrejournalen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Results.PatientsList;
import com.example.sebastian.vandrejournalen.Results.RecyclerAdapterPatientList;
import com.example.sebastian.vandrejournalen.Results.ResultsFragment;
import com.example.sebastian.vandrejournalen.Results.ResultsPager;
import com.example.sebastian.vandrejournalen.Results.SectionSelectionFragment;
import com.example.sebastian.vandrejournalen.Results.BasicHealthInfoFragment;
import com.example.sebastian.vandrejournalen.authentication.AuthenticationActivity;
import com.example.sebastian.vandrejournalen.authentication.RegisterPatientFragment;
import com.example.sebastian.vandrejournalen.calendar.Appointment;
import com.example.sebastian.vandrejournalen.calendar.AppointmentFragment;
import com.example.sebastian.vandrejournalen.calendar.CalendarTab;
import com.example.sebastian.vandrejournalen.calendar.Schedule;
import com.example.sebastian.vandrejournalen.calendar.SearchFragment;
import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements CalendarTab.OnFragmentInteractionListener, RegisterPatientFragment.OnFragmentInteractionListener, BasicHealthInfoFragment.OnFragmentInteractionListener, SectionSelectionFragment.OnFragmentInteractionListener, RecyclerAdapterPatientList.OnFragmentInteractionListener, ResultsFragment.OnFragmentInteractionListener{
    private static final String TAG = "MAINACTIVITY" ;
    final android.support.v4.app.FragmentManager fn = getSupportFragmentManager();
    public static Locale mylocale;
    public static int theme = 0;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    String role;
    MaterialDialog dialog;
    ArrayList<String> patients = new ArrayList<>();
    FrameLayout constraintLayout;
    Fragment currentFragment;
    NavigationView navigationView;
    SharedPreferences prefs;
    User user;
    boolean edited;
    public static String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Get saved preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        theme = prefs.getInt("theme",0);

 /*       //Set theme
        if (theme == 0){
            setTheme(R.style.BlueTheme);
        } else{
            setTheme(R.style.PinkTheme);
        }

        */

        super.onCreate(savedInstanceState);
        language = prefs.getString("language","en");
        setLanguage(language);

        //Get User json string from SP
        String jsonUser = getIntent().getStringExtra("user");

        //Convert User json string to User object
        Gson gson = new Gson();

        //If no user is set because of activity restart, get "userMain" instead
        if(jsonUser ==null){
            jsonUser = prefs.getString("userMain","");
        } else{
            //If a user is set, put it in SP in case of activity restart
            prefs.edit().putString("userMain",jsonUser).apply();

        }
        user = gson.fromJson(jsonUser, User.class);

        //Use User role to get theme
        setTheme(RoleHelper.getTheme(user));

        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Layout
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        constraintLayout = findViewById(R.id.sliding);
        navigationView = findViewById(R.id.nav_view);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);


        //Hide slide panel on start up
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        //Setup navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Use User role to choose menu layout
        navigationView.inflateMenu(RoleHelper.getOptionsMenu(user));
        View headerview = navigationView.getHeaderView(0);
        TextView tvRole = headerview.findViewById(R.id.tvRole);
        TextView tvName = headerview.findViewById(R.id.tvName);

        //Set text of navigation header
        if(mylocale == null ){
            tvRole.setText(user.getRole());
        } else if(mylocale.getLanguage().equals("en")){
            //If language is english
            tvRole.setText(user.getRole());
        } else{
            //If language is not english, translate the role
            tvRole.setText(RoleHelper.translateRole(user.getRole()));
        }
        tvName.setText(user.getName());

        //Set click listener for navigation drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                //Check the first menu item by default
                navigationView.getMenu().getItem(0).setChecked(false);

                if (id == R.id.nav_camera) {
                    // Handle the camera action
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadMainFragment();
                        }},300);

                } else if (id == R.id.nav_results) {
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);


                    if(user.getRole().equals("Patient")){
                        Log.d(TAG, "onNavigationItemSelected: "+user.getMidwifeName());
                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Bundle args = new Bundle();
                            String obj = new Gson().toJson(user);
                            args.putString("user" , obj);
                            Patient patient = new Patient();
                            String obj1 = new Gson().toJson(patient);
                            args.putString("patient" , obj1);
                            currentFragment = SectionSelectionFragment.newInstance();
                            currentFragment.setArguments(args);
                            fn.beginTransaction().replace(R.id.content_frame, currentFragment).addToBackStack(null).commit();
                        }},300);

                } else if (id == R.id.nav_slideshow) {

                    currentFragment = PatientsList.newInstance(user);
                    fn.beginTransaction().replace(R.id.content_frame,currentFragment).addToBackStack(null).commit();



                } else if (id == R.id.nav_manage) {

                } else if (id == R.id.nav_share) {

                } else if (id == R.id.nav_register_patient) {

                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                    slidingUpPanelLayout.setEnabled(false);
                    constraintLayout.removeView(slidingUpPanelLayout);
                    slidingUpPanelLayout.setEnabled(false);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fn.beginTransaction().replace(R.id.content_frame, RegisterPatientFragment.newInstance(user)).addToBackStack(null).commit();
                        }},400);

                } else if (id == R.id.nav_send) {
                    prefs.edit().remove("token").apply();
                    prefs.edit().remove("user").apply();
                    prefs.edit().remove("userMain").apply();

                    startActivity(new Intent(MainActivity.this, AuthenticationActivity.class));
                    finish();
                }

                final DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });
        navigationView.bringToFront();





        loadMainFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            } else if(edited){
                new MaterialDialog.Builder(this)
                    .title(R.string.exit)
                    .content("You have unsaved changes. Are you sure you want to return?")
                    .positiveText("Yes")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            popStack();
                            edited = false;
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        }
                    })
                    .negativeText("No")
                    .show();

            } else if(currentFragment instanceof Schedule || currentFragment instanceof SearchFragment || currentFragment instanceof CalendarTab || fn.getBackStackEntryCount() <2){
                new MaterialDialog.Builder(this)
                        .title(R.string.exit)
                        .content("Are you sure you want to exit?")
                        .positiveText("Yes")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                onYesClick();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                onNoClick();
                            }
                        })
                        .negativeText("No")
                        .show();
            }

            else{
                fn.popBackStack();

            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit");
            builder.setMessage("Are you sure you want to exit?").setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    onYesClick();

                                }


                            }).setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            onNoClick();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();*/
            }
    }
    private void onYesClick() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        MainActivity.this.finish();



    }private void onNoClick() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(theme == 1){
                prefs.edit().putInt("theme",0).apply();
                theme = 0;
            } else{
                prefs.edit().putInt("theme",1).apply();
                theme = 1;
            }

            restartActivity();
            return true;
        }
        if (id == R.id.change_lang) {
            if(mylocale == null ){
                setLanguage("da");
            } else if(mylocale.getLanguage().equals("en")){
                setLanguage("da");
            } else{
                setLanguage("en");
            }
            restartActivity();
        }

        return super.onOptionsItemSelected(item);
    }

    private void restartActivity() {
        Intent intent=new Intent(MainActivity.this,MainActivity.class);
        finish();
        startActivity(intent);
    }



    public void loadMainFragment(){

        Bundle args = new Bundle();
        String obj = new Gson().toJson(user);
        args.putString("user" , obj);
        Log.d(TAG, "loadMainFragment: "+user.getRole());
        currentFragment = RoleHelper.getMainFragment(user);
        currentFragment.setArguments(args);

        fn.beginTransaction().replace(R.id.content_frame, currentFragment).addToBackStack(null).commit();
        slidingUpPanelLayout.setEnabled(true);
        slidingUpPanelLayout.setClickable(true);
        slidingUpPanelLayout.setTouchEnabled(true);
        navigationView.getMenu().getItem(0).setChecked(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    protected void setLanguage(String language){
        mylocale=new Locale(language);
        prefs.edit().putString("language",language).apply();
        Resources resources=getResources();
        DisplayMetrics dm=resources.getDisplayMetrics();
        Configuration conf= resources.getConfiguration();
        conf.locale=mylocale;
        resources.updateConfiguration(conf,dm);
    }

    @Override
    public void onDateClick(ArrayList<Appointment> arrayList) {
        //Delay showing new panel to see it animate
        String role = user.getRole();
        /*slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {*/
        switch(role){
            case "Patient":

                Bundle args = new Bundle();
                Fragment fragment = AppointmentFragment.newInstance();
                String obj2 = new Gson().toJson(user);
                args.putString("user" , obj2);
                String obj1 = new Gson().toJson(arrayList.get(0));
                args.putString("appointment" , obj1);
                fragment.setArguments(args);

                fn.beginTransaction().replace(R.id.sliding,fragment).commit();
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                break;
            default:
                /*fn.beginTransaction().replace(R.id.sliding, NotesListTab.newInstance()).commit();
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);*/
                createDialog(arrayList);
        }
                //fn.beginTransaction().add(R.id.content_frame,RoleHelper.getSlidingFragment(role,appointment)).commit();

            /*}
        },400);*/
    }

    @Override
    public void onToday(ArrayList<Appointment> arrayList, int pos) {
        String role = user.getRole();
        switch(role){
            case "Patient":
                Log.d(TAG, "onDateClick: "+arrayList.get(0).getAppointmentID());
                Bundle args = new Bundle();
                Fragment fragment = AppointmentFragment.newInstance();
                String obj2 = new Gson().toJson(user);
                args.putString("user" , obj2);
                String obj1 = new Gson().toJson(arrayList.get(0));
                args.putString("appointment" , obj1);
                fragment.setArguments(args);
                fn.beginTransaction().replace(R.id.sliding,fragment).commit();
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                break;
            case "Midwife":

                //fn.beginTransaction().add(R.id.content_frame,RoleHelper.getSlidingFragment(role,appointment)).commit();


                break;

        }
    }


    @Override
    public void removePreview() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    @Override
    public void setNames(String midwife, String specialist) {
        user.setMidwifeName(midwife);
        user.setSpecialistName(specialist);
    }

    public void createDialog(final ArrayList<Appointment> arrayList){
        for (int i=0; i< arrayList.size();i++){
            patients.add(arrayList.get(i).getName());
        }
        DateFormat formatter = new SimpleDateFormat("EEE dd/MM");
       if(dialog == null) {
           MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                   .title(patients.size()+" "+getResources().getString(R.string.patients)+" - "+formatter.format(arrayList.get(0).getDate()))
                   .items(patients)
                   .dismissListener(new DialogInterface.OnDismissListener() {
                       @Override
                       public void onDismiss(DialogInterface dialogInterface) {
                           dialog = null;
                       }
                   })

                   .itemsCallback(new MaterialDialog.ListCallback() {
                       @Override
                       public void onSelection(MaterialDialog mdialog, View view, int which, CharSequence text) {
                           Patient patient = new Patient();
                           patient.setAddress(arrayList.get(which).getAddress());
                           patient.setEmail(arrayList.get(which).getEmail());
                           patient.setPhonework(arrayList.get(which).getPhonework());
                           patient.setPhoneprivate(arrayList.get(which).getPhoneprivate());
                           patient.setName(arrayList.get(which).getName());
                           patient.setJournalID(arrayList.get(which).getJournalID());
                           patient.setMidwifeName(arrayList.get(which).getJournalMidwifeName());
                           patient.setSpecialistName(arrayList.get(which).getJournalSpecialistName());
                           currentFragment = SectionSelectionFragment.newInstance();

                           Bundle args = new Bundle();
                           String obj = new Gson().toJson(user);
                           args.putString("user" , obj);
                           String obj1 = new Gson().toJson(patient);
                           args.putString("patient" , obj1);
                           currentFragment.setArguments(args);

                           fn.beginTransaction().replace(R.id.content_frame, currentFragment, "sliding").addToBackStack(null).commit();
                           dialog = null;

                       }
                   }).negativeText(getResources().getString(R.string.canc));
           dialog = builder.build();
           dialog.show();
       } else if(dialog.isShowing()){
           dialog = null;
       }
       patients.clear();

    }

    @Override
    public void startJournal(Patient patient, User user) {
        currentFragment = SectionSelectionFragment.newInstance();
        Bundle args = new Bundle();

        String obj = new Gson().toJson(user);
        args.putString("user" , obj);
        String obj1 = new Gson().toJson(patient);
        args.putString("patient" , obj1);
        currentFragment.setArguments(args);
        fn.beginTransaction().replace(R.id.content_frame, currentFragment).addToBackStack(null).commit();
    }


    @Override
    public void updateFragment(Fragment fragment) {

        fn.beginTransaction().replace(R.id.content_frame,fragment,"section").addToBackStack(null).commit();
    }

    @Override
    public void sectionSelection(Patient patient) {

        Bundle args = new Bundle();
        String obj = new Gson().toJson(user);
        args.putString("user" , obj);
        String obj1 = new Gson().toJson(patient);
        args.putString("patient" , obj1);
        currentFragment = SectionSelectionFragment.newInstance();
        currentFragment.setArguments(args);
        fn.beginTransaction().replace(R.id.content_frame,currentFragment).addToBackStack(null).commit();
    }

    @Override
    public void popStack() {
        fn.popBackStack();
    }

    @Override
    public void showDatePicker() {
        ResultsPager frag = (ResultsPager) fn.findFragmentByTag("section");
        try {
            frag.showDatePickerDialog();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateEdited(boolean edited) {
        this.edited = edited;
    }
}

