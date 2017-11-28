package com.example.sebastian.vandrejournalen.authentication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.MainActivity;

import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AuthenticationActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener, QRReader.OnFragmentInteractionListener{
    SecureUtil secureUtil;
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
        secureUtil = new SecureUtil(this);


    }

    @Override
    public void login(String role) {
        if(role.equals("")){
            role = "PL";
        }
        Intent intent = new Intent(this,MainActivity.class);
        //Get role from server and put here
        intent.putExtra("role",role.toUpperCase());
        startActivity(intent);
        finish();
    }

    @Override
    public void startQR() {



/*

        QRReader fragment = QRReader.newInstance("","");

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();*/
    }



    @Override
    public void notSuccessful() {
        Toast.makeText(this,"Unsuccessful",Toast.LENGTH_LONG).show();
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
        Call<User> call = client.addUser("testtest.php",user);
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
    protected void onResume() {
        super.onResume();
        secureUtil.checkLockScreen();
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