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
    MaterialDialog dialog;
    ArrayList<String> patients = new ArrayList<>();
    FrameLayout constraintLayout;
    Fragment currentFragment;
    NavigationView navigationView;
    SharedPreferences prefs;
    User user;
    boolean edited;
    Patient patient = new Patient();
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
                //Uncheck the first menu item
                navigationView.getMenu().getItem(0).setChecked(false);
                //Check which item was clicked
                if (id == R.id.nav_schedule) {
                    //Create fragment in new thread
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadMainFragment();
                        }},300);

                } else if (id == R.id.nav_results) {
                    //Hide sliding panel
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Create fragment and replace current
                            patient = new Patient();
                            //Add User object
                            currentFragment = addBundle(SectionSelectionFragment.newInstance());
                            fn.beginTransaction().replace(R.id.content_frame, currentFragment).addToBackStack(null).commit();
                        }},300);

                } else if (id == R.id.nav_patients) {
                    //Create fragment and replace current
                    currentFragment = PatientsList.newInstance(user);
                    fn.beginTransaction().replace(R.id.content_frame,currentFragment).addToBackStack(null).commit();

                } else if (id == R.id.nav_register_patient) {
                    //Hide and disable sliding pane
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                    slidingUpPanelLayout.setEnabled(false);
                    constraintLayout.removeView(slidingUpPanelLayout);
                    slidingUpPanelLayout.setEnabled(false);

                    //Create new thread
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Create fragment and replace current
                            fn.beginTransaction().replace(R.id.content_frame, RegisterPatientFragment.newInstance(user)).addToBackStack(null).commit();
                        }},300);

                } else if (id == R.id.nav_logout) {

                    //Remove information from SP
                    prefs.edit().remove("token").apply();
                    prefs.edit().remove("user").apply();
                    prefs.edit().remove("userMain").apply();

                    //Return to Authentication
                    startActivity(new Intent(MainActivity.this, AuthenticationActivity.class));
                    finish();
                }

                //Close nav drawer after press
                final DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });
        navigationView.bringToFront();

        //Create the first fragment
        loadMainFragment();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
            //Check if the drawer is open and close it
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            //Check if sliding panel is shown and close it
            else if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
            //Check if anything is unsaved in a fragment
            else if(edited){
                //Create dialog box
                new MaterialDialog.Builder(this)
                    .title(R.string.exit)
                    .content("You have unsaved changes. Are you sure you want to return?")
                    .positiveText("Yes")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            //If yes is pressed, return to previous fragment
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

            }
            //Find out which fragment is currently shown, and if any fragments are alive in the backstack
            else if(currentFragment instanceof Schedule || currentFragment instanceof SearchFragment || currentFragment instanceof CalendarTab || fn.getBackStackEntryCount() <2){
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
                            }
                        })
                        .negativeText("No")
                        .show();
            }
            else{
                //Replace with previous fragment
                fn.popBackStack();
            }
    }
    private void onYesClick() {
        //Close keyboard and activity
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        MainActivity.this.finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        //Check which item was clicked
        //For themes
        if (id == R.id.action_settings) {
            if(theme == 1){
                prefs.edit().putInt("theme",0).apply();
                theme = 0;
            } else{
                prefs.edit().putInt("theme",1).apply();
                theme = 1;
            }
        }
        //Change language
        if (id == R.id.change_lang) {
            if(mylocale == null ){
                setLanguage("da");
            } else if(mylocale.getLanguage().equals("en")){
                setLanguage("da");
            } else{
                setLanguage("en");
            }
        }
        restartActivity();

        return super.onOptionsItemSelected(item);
    }

    private void restartActivity() {
        Intent intent=new Intent(MainActivity.this,MainActivity.class);
        finish();
        startActivity(intent);
    }



    public void loadMainFragment(){
        //Add User object to fragment
        currentFragment = addBundle(RoleHelper.getMainFragment(user));
        fn.beginTransaction().replace(R.id.content_frame, currentFragment).addToBackStack(null).commit();

        //Enable the sliding panel
        slidingUpPanelLayout.setEnabled(true);
        slidingUpPanelLayout.setClickable(true);
        slidingUpPanelLayout.setTouchEnabled(true);

        //Highlight first menu item
        navigationView.getMenu().getItem(0).setChecked(true);

    }
    private Fragment addBundle (Fragment fragment){
        //Create a bundle to hold attributes
        Bundle args = new Bundle();
        Gson gson = new Gson();
        //Convert it to a JSON string
        String obj = gson.toJson(user);
        args.putString("user" , obj);
        String obj1 = new Gson().toJson(patient);
        args.putString("patient" , obj1);
        //Add the bundle to the fragment
        fragment.setArguments(args);
        return fragment;
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
        //Set language
        mylocale=new Locale(language);
        //Save language settings
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
        switch(user.getRole()){
            case "Patient":
                //Add Bundle with User and Appointment objects to fragment
                Bundle args = new Bundle();
                Fragment fragment = AppointmentFragment.newInstance();
                String obj2 = new Gson().toJson(user);
                args.putString("user" , obj2);
                String obj1 = new Gson().toJson(arrayList.get(0));
                args.putString("appointment" , obj1);
                fragment.setArguments(args);

                fn.beginTransaction().replace(R.id.sliding,fragment).commit();
                //Collapse sliding panel
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                break;
            default:
                createDialog(arrayList);
        }
    }

    @Override
    public void onToday(ArrayList<Appointment> arrayList, int pos) {
        if(user.getRole().equals("Patient")) {
            //Add Bundle with User and Appointment objects to fragment
            Bundle args = new Bundle();
            Fragment fragment = AppointmentFragment.newInstance();
            String obj2 = new Gson().toJson(user);
            args.putString("user", obj2);
            String obj1 = new Gson().toJson(arrayList.get(0));
            args.putString("appointment", obj1);
            fragment.setArguments(args);
            fn.beginTransaction().replace(R.id.sliding, fragment).commit();

            //Collapse sliding panel
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }


    @Override
    public void removePreview() {
        //Hide sliding panel
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }

    @Override
    public void setNames(String midwife, String specialist) {
        //Add professional names from Appointment to User object
        user.setMidwifeName(midwife);
        user.setSpecialistName(specialist);
    }

    public void createDialog(final ArrayList<Appointment> arrayList){
        //Add patients' names to a list
        for (int i=0; i< arrayList.size();i++){
            patients.add(arrayList.get(i).getName());
        }
        DateFormat formatter = new SimpleDateFormat("EEE dd/MM");
        if(dialog == null) {
           MaterialDialog.Builder builder = new MaterialDialog.Builder(this)

                   //Show number of patients and date
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

                           //Add attributes of Appointment to Patient object
                           Patient patient1 = new Patient();
                           patient1.setAddress(arrayList.get(which).getAddress());
                           patient1.setEmail(arrayList.get(which).getEmail());
                           patient1.setPhonework(arrayList.get(which).getPhonework());
                           patient1.setPhoneprivate(arrayList.get(which).getPhoneprivate());
                           patient1.setName(arrayList.get(which).getName());
                           patient1.setJournalID(arrayList.get(which).getJournalID());
                           patient1.setMidwifeName(arrayList.get(which).getJournalMidwifeName());
                           patient1.setSpecialistName(arrayList.get(which).getJournalSpecialistName());
                           patient = patient1;

                           //Add User and Patient object to fragment
                           currentFragment = addBundle(SectionSelectionFragment.newInstance());
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
        //Update objects
        this.patient = patient;
        this.user = user;
        currentFragment = addBundle(SectionSelectionFragment.newInstance());
        fn.beginTransaction().replace(R.id.content_frame, currentFragment).addToBackStack(null).commit();
    }


    @Override
    public void updateFragment(Fragment fragment) {
        fn.beginTransaction().replace(R.id.content_frame,fragment,"section").addToBackStack(null).commit();
    }

    @Override
    public void sectionSelection(Patient patient) {
        this.patient = patient;
        currentFragment = addBundle(SectionSelectionFragment.newInstance());

        fn.beginTransaction().replace(R.id.content_frame,currentFragment).addToBackStack(null).commit();
    }

    @Override
    public void popStack() {
        //Return to previous fragment
        fn.popBackStack();
    }

    @Override
    public void showDatePicker() {
        //Call method from fragment to show date picker
        ResultsPager frag = (ResultsPager) fn.findFragmentByTag("section");
        try {
            frag.showDatePickerDialog();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateEdited(boolean edited) {
        //Update value of edited
        this.edited = edited;
    }
}

