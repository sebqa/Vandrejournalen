package com.example.sebastian.vandrejournalen.calendar;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
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
import android.widget.TextView;

import com.example.sebastian.journalapp.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CalendarTab.OnFragmentInteractionListener{
    ArrayList<Appointment> arrayList = new ArrayList<Appointment>();
    Schedule scheduleFrag = new Schedule();
    final android.support.v4.app.FragmentManager fn = getSupportFragmentManager();
    public static Locale mylocale;
    public static int theme = 0;
    TextView previewDate,appointText;
    private SlidingUpPanelLayout slidingUpPanelLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (theme == 0){
            setTheme(R.style.BlueTheme);
        } else{
            setTheme(R.style.PinkTheme);

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//setLanguage("en");
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        previewDate = findViewById(R.id.previewDate);
        appointText = findViewById(R.id.appointTitle);

        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        loadCalendar();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        final android.support.v4.app.FragmentManager fn = getSupportFragmentManager();

        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            fn.beginTransaction().replace(R.id.content_frame, new Schedule()).commit();

        } else if (id == R.id.nav_slideshow) {


        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadCalendar(){
        fn.beginTransaction().replace(R.id.content_frame, scheduleFrag).commit();


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
        Intent refreshIntent=new Intent(MainActivity.this,MainActivity.class);
        finish();
        startActivity(refreshIntent);
    }

    @Override
    public void showPreview(Appointment appointment) {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        appointText.setText(appointment.event);
        previewDate.setText(appointment.getDay()+"/"+ appointment.getMonth()+"/"+ appointment.getYear()+"\n"+appointment.getTime());


    }

    @Override
    public void removePreview() {
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
    }
}