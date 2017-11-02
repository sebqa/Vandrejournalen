package com.example.sebastian.vandrejournalen.authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.MainActivity;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        initializeRetro();
    }

    public void authenticate(){

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

    public void initializeRetro(){
        //Test API
        String API_BASE_URL = "https://jsonplaceholder.typicode.com/";

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        ServerClient serverClient = retrofit.create(ServerClient.class);

        Call<List<Person>> call =
                serverClient.users();

        call.enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                // The network call was a success and we got a response
                // TODO: use the repository list and display it

                Log.d(""+response.body().get(0).getName(), "onResponse: ");
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                // the network call was a failure
                // TODO: handle error
                Toast.makeText(AuthenticationActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}