package com.example.sebastian.vandrejournalen.networking;

import com.example.sebastian.vandrejournalen.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Sebastian on 28-11-2017.
 */

public interface ServerClient {
    @POST("/{page}")
    Call<User> reposForUser(
            @Path("page") String page
    );
}
