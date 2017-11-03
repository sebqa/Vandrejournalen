package com.example.sebastian.vandrejournalen.authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.calendar.MainActivity;

import retrofit2.Call;


public class AuthenticationActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, QRReader.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            LoginFragment fragment = LoginFragment.newInstance("","");

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment).commit();
        }
    }

    public void serverCom(){

    }

    @Override
    public void login() {
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

    @Override
    public void startQR() {
        QRReader fragment = QRReader.newInstance("","");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void notSuccessful() {
        Toast.makeText(this,"Unsuccessful",Toast.LENGTH_LONG);
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}