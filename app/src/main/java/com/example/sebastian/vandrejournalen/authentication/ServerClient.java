package com.example.sebastian.vandrejournalen.authentication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Sebastian on 01-11-2017.
 */

public interface ServerClient {

    //Specify path
    @GET("/users")
    Call<List<Person>> users(

    );
}
