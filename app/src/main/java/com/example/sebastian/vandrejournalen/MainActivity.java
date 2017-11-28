package com.example.sebastian.vandrejournalen;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Results.ResultsPager;
import com.example.sebastian.vandrejournalen.calendar.Appointment;
import com.example.sebastian.vandrejournalen.calendar.CalendarTab;
import com.example.sebastian.vandrejournalen.calendar.NotesListTab;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements CalendarTab.OnFragmentInteractionListener{
    final android.support.v4.app.FragmentManager fn = getSupportFragmentManager();
    public static Locale mylocale;
    public static int theme = 0;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    String role;
    MaterialDialog dialog;
    ArrayList<String> patients = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (theme == 0){
            setTheme(R.style.BlueTheme);
        } else{
            setTheme(R.style.PinkTheme);

        }
        super.onCreate(savedInstanceState);
        //setLanguage("da");
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        role = getIntent().getStringExtra("role");
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);

        if(role.equals("PL")){
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.inflateMenu(RoleHelper.getOptionsMenu(role));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final android.support.v4.app.FragmentManager fn = getSupportFragmentManager();

                int id = item.getItemId();
                Log.d(""+id, "onNavigationItemSelected: ");
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

                if (id == R.id.nav_camera) {
                    // Handle the camera action
                    loadMainFragment();
                } else if (id == R.id.nav_results) {
                    fn.beginTransaction().replace(R.id.content_frame, ResultsPager.newInstance(role)).addToBackStack(null).commit();
                } else if (id == R.id.nav_slideshow) {

                } else if (id == R.id.nav_manage) {

                } else if (id == R.id.nav_share) {

                } else if (id == R.id.nav_send) {

                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        } else{
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
                theme = 0;
            } else{
                theme = 1;
            }

            restartActivity();
            return true;
        }
        if (id == R.id.change_lang) {
            Log.d(""+mylocale, "onOptionsItemSelected: ");
            if(mylocale == null ){
                setLanguage("da");
            } else if(mylocale.getLanguage().equals("en")){
                setLanguage("da");
            } else{
                setLanguage("en");
            }

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
        fn.beginTransaction().replace(R.id.content_frame, RoleHelper.getMainFragment(role)).commit();
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
        Resources resources=getResources();
        DisplayMetrics dm=resources.getDisplayMetrics();
        Configuration conf= resources.getConfiguration();
        conf.locale=mylocale;
        resources.updateConfiguration(conf,dm);
        restartActivity();
    }

    @Override
    public void onDateClick(ArrayList<Appointment> arrayList) {
        //Delay showing new panel to see it animate

        /*slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {*/
        switch(role){
            case "PL":
                fn.beginTransaction().replace(R.id.sliding,RoleHelper.getSlidingFragment(role,arrayList.get(0))).commit();
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                break;
            case "MW":
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
        switch(role){
            case "PL":
                fn.beginTransaction().replace(R.id.sliding,RoleHelper.getSlidingFragment(role,arrayList.get(pos))).commit();
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                break;
            case "MW":

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
                           fn.beginTransaction().replace(R.id.content_frame, ResultsPager.newInstance(role), "sliding").addToBackStack(null).commit();
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

