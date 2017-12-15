package com.example.sebastian.vandrejournalen.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.MainActivity;

import com.example.sebastian.vandrejournalen.SecureUtil;
import com.example.sebastian.vandrejournalen.User;
import com.google.gson.Gson;


import java.util.Locale;


public class AuthenticationActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener,
        RegisterInfoFragment.OnFragmentInteractionListener, letIDFragment.OnFragmentInteractionListener{
    private static final String TAG = "AUTHENTICATIONACTIVITY";
    SecureUtil secureUtil;
    FragmentManager fm = getSupportFragmentManager();
    SharedPreferences prefs;
    User user = new User();
    FragmentTransaction ft;
    public static Locale mylocale;
    public static int theme;
    String token ="";
    static boolean canExit = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        theme = prefs.getInt("theme",0);
        //Set theme
        if (theme == 0){
            setTheme(R.style.BlueTheme);
        } else{
            setTheme(R.style.PinkTheme);
        }
        super.onCreate(savedInstanceState);
        canExit = true;

        ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter,
                R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_right_exit);
        String language = prefs.getString("language","en");
        setLanguage(language);

        setContentView(R.layout.activity_authentication);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String tokenString = prefs.getString("token","");
        if (!tokenString.equals("")){
            user.setToken(tokenString);
            Log.d(TAG, "onCreate: "+user.getToken());
            goToLogin();
            Log.d(TAG, "onCreate: user is set "+user.getRole());

        } else if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            Fragment fragment= RegisterFragment.newInstance();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for gingerbread and newer versions
            secureUtil = new SecureUtil(this);

        }

    }

    @Override
    public void loginExists(User user) {
        Bundle args = new Bundle();
        String obj = new Gson().toJson(user);
        args.putString("user" , obj);
            Fragment fragment = letIDFragment.newInstance(user);
            fragment.setArguments(args);
            ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.fragment_slide_left_enter,
                R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_right_exit);
            ft.replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            //user.setRole("Midwife");
        canExit = false;
    }

    @Override
    public void popStack() {
        fm.popBackStack();
    }


    @Override
    public void onSuccessfulLogin(User user) {
        Intent intent = new Intent(this, MainActivity.class);
        //Get role from server and put here

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        Gson gson = new Gson();
        String obj = gson.toJson(user);
        prefs.edit().putString("token",user.getToken()).apply();
        intent.putExtra("user", obj);


        Log.d(TAG, "onSuccessfulLogin: "+user.getToken());
        startActivity(intent);
        finish();
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
    public void goToLogin() {
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String obj = gson.toJson(user);
        args.putString("user",obj);
        token = user.getToken();
        Fragment fragment = LoginFragment.newInstance(token,"");
        fragment.setArguments(args);
        ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter,
                R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_right_exit);
        ft.replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
        canExit = false;
    }

    @Override
    public void goToInfo(User user) {
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String obj = gson.toJson(user);
        args.putString("obj" , obj);
        Fragment fragment = RegisterInfoFragment.newInstance(user);
        fragment.setArguments(args);
        ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter,
                R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_right_exit);
        ft.replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
        canExit = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //secureUtil.checkLockScreen();

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "User confirmed", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "User confirmation failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!canExit) {
            restartActivity();
        } else{
            finish();
        }
        }

    @Override
    public void restartActivity() {
        Intent intent=new Intent(AuthenticationActivity.this,AuthenticationActivity.class);
        user.setToken(null);
        prefs.edit().putString("token",null).apply();
        token = null;
        finish();
        startActivity(intent);

    }
}