package com.example.sebastian.vandrejournalen.authentication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;
import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.MainActivity;

import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;
import com.google.gson.Gson;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AuthenticationActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, QRReader.OnFragmentInteractionListener, RegisterFragment.OnFragmentInteractionListener, RegisterInfoFragment.OnFragmentInteractionListener, letIDFragment.OnFragmentInteractionListener{
    private static final String TAG = "AUTHENTICATIONACTIVITY";
    SecureUtil secureUtil;
    FragmentManager fm = getSupportFragmentManager();
    SharedPreferences prefs;
    User user;
    public static int theme;
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
        setContentView(R.layout.activity_authentication);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            Fragment fragment= RegisterFragment.newInstance("","");

            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment).commit();
        }


        String userString = prefs.getString("user","");
        if (!userString.equals("")){
            user = new Gson().fromJson(userString,User.class);
            loginExists(user);
            Log.d(TAG, "onCreate: user is set"+user.getRole());
        } else{

            Log.d(TAG, "onCreate: no user set");

        }
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for gingerbread and newer versions
            secureUtil = new SecureUtil(this);

        }


    }

    @Override
    public void loginExists(User user) {
            Fragment fragment = letIDFragment.newInstance(user);
            fm.beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
            //user.setRole("Midwife");
    }




    @Override
    public String encrypt(String passwordString) {
        return secureUtil.encrypt(passwordString);
    }

    @Override
    public String decrypt(String passwordString) {
        return secureUtil.decrypt(passwordString);
    }

    @Override
    public void register(User user) {
        ServerClient client = ServiceGenerator.createService(ServerClient.class);
        Call<User> call = client.addUser("loginCprPw.php",user);
        // Execute the call asynchronously. Get a positive or negative callback.
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                // The network call was a success and we got a response
                Toast.makeText(AuthenticationActivity.this, ""+response.body().getRole(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // the network call was a failure
                Toast.makeText(AuthenticationActivity.this, "Network Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSuccessfulLogin(User user) {
        Intent intent = new Intent(this, MainActivity.class);
        //Get role from server and put here
        Gson gson = new Gson();
        String obj = gson.toJson(user);
        prefs.edit().putString("user",obj).apply();
        intent.putExtra("user", obj);
        startActivity(intent);
        finish();
    }

    @Override
    public void goToLogin() {
        Fragment fragment = LoginFragment.newInstance("","");
        fm.beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
    }

    @Override
    public void goToInfo(User user) {
        Fragment fragment = RegisterInfoFragment.newInstance(user);
        fm.beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
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
}