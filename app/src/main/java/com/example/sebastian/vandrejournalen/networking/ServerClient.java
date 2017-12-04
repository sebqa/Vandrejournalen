package com.example.sebastian.vandrejournalen.networking;

import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.calendar.Appointment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Sebastian on 28-11-2017.
 */

public interface ServerClient {
    @POST("/{page}")
    Call<User> addUser(
            @Path("page") String page,
            @Body User body
    );

    @POST("/{page}")
    Call<User> addAppointment(
            @Path("page") String page,
            @Body Appointment body
    );



}
