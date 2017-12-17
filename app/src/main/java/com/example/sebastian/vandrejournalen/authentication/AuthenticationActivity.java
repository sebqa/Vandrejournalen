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
    FragmentManager fm = getSupportFragmentManager();
    SharedPreferences prefs;
    User user = new User();
    FragmentTransaction ft;
    public static Locale mylocale;
    public static int theme;
    static boolean canExit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //For implementation of changing themes.
        //Get the theme set previously, form SP
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        theme = prefs.getInt("theme",0);
        //Set theme
        if (theme == 0){
            setTheme(R.style.BlueTheme);
        } else{
            setTheme(R.style.PinkTheme);
        }

        super.onCreate(savedInstanceState);

        //boolean for navigation. If this value is true, the phone's back button
        // will exit the app otherwise it will pop the backstack.
        canExit = true;

        //Get the language previously set and set it for current session
        String language = prefs.getString("language","en");
        setLanguage(language);

        setContentView(R.layout.activity_authentication);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get the access token if it exists
        String tokenString = prefs.getString("token","");
        if (!tokenString.equals("")){
            //If it exists, go to login screen
            user.setToken(tokenString);
            Log.d(TAG, "onCreate: "+user.getToken());
            goToLogin();
            Log.d(TAG, "onCreate: user is set "+user.getRole());

        } else {
            //If the token does not exist, create the RegisterFragment
            Fragment fragment= RegisterFragment.newInstance();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public void loginExists(User user) {
        //Update User
        this.user = user;
        // Attach the User object to the fragment
        Fragment fragment = addBundle(letIDFragment.newInstance());

        //Custom animation
        ft = addAnim();
        ft.replace(R.id.fragment_container,fragment).addToBackStack(null).commit();

        canExit = false;
    }

    @Override
    public void popStack() {
        //This method replaces the current fragment with the one previously added to the backstack
        fm.popBackStack();
    }


    @Override
    public void onSuccessfulLogin(User user) {
        //Transition to MainActivity
        Intent intent = new Intent(this, MainActivity.class);

        //Force closing of keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        //Put User as string
        Gson gson = new Gson();
        String obj = gson.toJson(user);

        //Save token in SP
        prefs.edit().putString("token",user.getToken()).apply();
        intent.putExtra("user", obj);
        startActivity(intent);
        finish();
    }



    protected void setLanguage(String language){
        //Set language locale, to appropriate string resources
        mylocale=new Locale(language);
        //Save setting in SP
        prefs.edit().putString("language",language).apply();
        Resources resources=getResources();
        DisplayMetrics dm=resources.getDisplayMetrics();
        Configuration conf= resources.getConfiguration();
        conf.locale=mylocale;
        resources.updateConfiguration(conf,dm);
    }

    @Override
    public void goToLogin() {
        Fragment fragment = addBundle(LoginFragment.newInstance());
        ft = addAnim();
        ft.replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
        canExit = false;
    }

    @Override
    public void goToInfo(User user) {
        this.user = user;
        Fragment fragment = addBundle(RegisterInfoFragment.newInstance());
        ft = addAnim();
        ft.replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
        canExit = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        //Recreate the activity and reset the access token
        Intent intent=new Intent(AuthenticationActivity.this,AuthenticationActivity.class);
        user.setToken(null);
        prefs.edit().putString("token",null).apply();
        finish();
        startActivity(intent);

    }

    private Fragment addBundle (Fragment fragment){
        //Create a bundle to hold attributes
        Bundle args = new Bundle();
        Gson gson = new Gson();
        //Convert it to a JSON string
        String obj = gson.toJson(user);
        args.putString("user" , obj);
        //Add the bundle to the fragment
        fragment.setArguments(args);
        return fragment;
    }

    private FragmentTransaction addAnim(){
        //Create fragment transaction
        ft = fm.beginTransaction();
        //Define animations
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter,
                R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_right_exit);
        return ft;
    }
}