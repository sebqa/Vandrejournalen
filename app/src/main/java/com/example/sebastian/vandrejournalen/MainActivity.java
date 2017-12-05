package com.example.sebastian.vandrejournalen;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Results.ResultsPager;
import com.example.sebastian.vandrejournalen.authentication.AuthenticationActivity;
import com.example.sebastian.vandrejournalen.authentication.RegisterPatient;
import com.example.sebastian.vandrejournalen.calendar.Appointment;
import com.example.sebastian.vandrejournalen.calendar.CalendarTab;
import com.example.sebastian.vandrejournalen.calendar.NotesListTab;
import com.example.sebastian.vandrejournalen.calendar.Schedule;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;
import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements CalendarTab.OnFragmentInteractionListener{
    private static final String TAG = "MAINACTIVITY" ;
    final android.support.v4.app.FragmentManager fn = getSupportFragmentManager();
    public static Locale mylocale;
    public static int theme = 0;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    String role;
    MaterialDialog dialog;
    ArrayList<String> patients = new ArrayList<>();
    SlidingUpPanelLayout.PanelSlideListener panelSlideListener;
    FrameLayout constraintLayout;
    Fragment currentFragment;
    NavigationView navigationView;
    SharedPreferences prefs;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Get saved preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        theme = prefs.getInt("theme",0);


        //Set theme
        if (theme == 0){
            setTheme(R.style.BlueTheme);
        } else{
            setTheme(R.style.PinkTheme);
        }
        super.onCreate(savedInstanceState);
        String language = prefs.getString("language","en");
        setLanguage(language);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String jsonUser = getIntent().getStringExtra("user");
        Gson gson = new Gson();
        if(jsonUser ==null){
            jsonUser = prefs.getString("userMain","");
        } else{
            prefs.edit().putString("userMain",jsonUser).apply();

        }
        user = gson.fromJson(jsonUser, User.class);

        //Layout
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        constraintLayout = findViewById(R.id.sliding);
        navigationView = findViewById(R.id.nav_view);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);


        if(user.getRole() == null){
            user.setRole("PL");
        }

        if(user.getRole().equals("PL")){
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            slidingUpPanelLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }
            });
        } else {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.inflateMenu(RoleHelper.getOptionsMenu(user));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                Log.d(""+id, "onNavigationItemSelected: ");
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

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            currentFragment = ResultsPager.newInstance(user);
                            fn.beginTransaction().replace(R.id.content_frame, currentFragment).addToBackStack(null).commit();
                        }},300);

                } else if (id == R.id.nav_slideshow) {

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
                            fn.beginTransaction().replace(R.id.content_frame, RegisterPatient.newInstance(user)).addToBackStack(null).commit();
                        }},400);

                } else if (id == R.id.nav_send) {
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
            } else if(currentFragment instanceof ResultsPager){
                loadMainFragment();
            }

            else{
                new MaterialDialog.Builder(this)
                        .title("Exit")
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
        intent.putExtra("role",role);
        finish();
        startActivity(intent);
    }



    public void loadMainFragment(){
        currentFragment = RoleHelper.getMainFragment(user);
        fn.beginTransaction().replace(R.id.content_frame, currentFragment).commit();
        slidingUpPanelLayout.setEnabled(true);
        slidingUpPanelLayout.setClickable(true);
        slidingUpPanelLayout.setTouchEnabled(true);
        navigationView.getMenu().getItem(0).setChecked(true);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (theme == 0) {
            setTheme(R.style.BlueTheme);
        } else {
            setTheme(R.style.PinkTheme);

        }

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
            case "PL":
                fn.beginTransaction().replace(R.id.sliding,RoleHelper.getSlidingFragment(user,arrayList.get(0))).commit();
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                //TODO NETWORKING
                //TEST AF NETWORKING
               /* Appointment appointment = arrayList.get(0);
                appointment.setGestationsalder("ksndfkjn");
                appointment.setInitialer("BOESBOI");
                ServerClient client = ServiceGenerator.createService(ServerClient.class);
                Call<User> call = client.addAppointment("addAppointment.php", appointment);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: "+response.body().getUserID());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Network failure", Toast.LENGTH_SHORT).show();

                    }
                });*/
                break;
            case "Midwife":
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
            case "PL":
                fn.beginTransaction().replace(R.id.sliding,RoleHelper.getSlidingFragment(user,arrayList.get(pos))).commit();
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

    public void createDialog(ArrayList<Appointment> arrayList){
        for (int i=0; i< arrayList.size();i++){
            patients.add(arrayList.get(i).fullName+" + CPR + Tid");

        }
       if(dialog == null) {
           MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                   .title(getResources().getString(R.string.patients)+" - "+patients.size()).items(patients)
                   .dismissListener(new DialogInterface.OnDismissListener() {
                       @Override
                       public void onDismiss(DialogInterface dialogInterface) {
                           dialog = null;
                       }
                   })
                   .itemsCallback(new MaterialDialog.ListCallback() {
                       @Override
                       public void onSelection(MaterialDialog mdialog, View view, int which, CharSequence text) {
                           Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                           currentFragment = ResultsPager.newInstance(user);
                           fn.beginTransaction().replace(R.id.content_frame, currentFragment, "sliding").addToBackStack(null).commit();
                           dialog = null;

                       }
                   });
           dialog = builder.build();
           dialog.show();
       } else if(dialog.isShowing()){
           dialog = null;
       }
       patients.clear();

    }

}

