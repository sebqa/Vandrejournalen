package com.example.sebastian.vandrejournalen.networking;

import com.example.sebastian.vandrejournalen.Results.BasicInfo;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.authentication.LetID;
import com.example.sebastian.vandrejournalen.Patient;
import com.example.sebastian.vandrejournalen.calendar.Appointment;
import com.example.sebastian.vandrejournalen.calendar.Consultation;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
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
            @Body Consultation body
    );

    @POST("/{page}")
    Call<User> checkKey(
            @Path("page") String page,
            @Body String key
    );

    @POST("/{page}")
    Call<String> checkCPR(
            @Path("page") String page,
            @Body String cpr
    );


    @POST("/{page}")
    Call<String> registerInfo(
            @Path("page") String page,
            @Body User user
    );


    @POST("/{page}")
    Call<User> login(
            @Path("page") String page,
            @Body User user
    );

    @POST("/{page}")
    Call<LetID> getLetTag(
            @Path("page") String page,
            @Body String id
    );

    @POST("/{page}")
    Call<User> checkLet(
            @Path("page") String page,
            @Body LetID letID
    );

    @POST("/{page}")
    Call<String> cprExp(
            @Path("page") String page,
            @Body Patient patient
            );
    @POST("/{page}")

    Call<Patient> getPatientInfo(
            @Path("page") String page,
            @Body String cpr
    );

    @POST("/{page}")
    Call<User> startJournal(
            @Path("page") String page,
            @Body String userID
    );

    @POST("/{page}")
    Call<Patient> getProfInfo(
            @Path("page") String page,
            @Body String userID
    );

    @POST("/{page}")
    Call<String> sendBasic(
            @Path("page") String page,
            @Body BasicInfo basicInfo
            );

    @POST("/{page}")
    Call<ArrayList<Patient>> getPatients(
            @Path("page") String page,
            @Body String userID
    );

    @POST("/{page}")
    Call<BasicInfo> getBasicInfo(
            @Path("page") String page,
            @Body String journalID
    );

    @POST("/{page}")
    Call<ArrayList<Consultation>> getConsultations(
            @Path("page") String page,
            @Body String journalID
    );

    @POST("/{page}")
    Call<String> postConsultation(
            @Path("page") String page,
            @Body Consultation consultation
    );

    @POST("/{page}")
    Call<ArrayList<Appointment>> getAppointments(
            @Path("page") String page,
            @Body User user
    );

    @POST("/{page}")
    Call<String> postAppointment(
            @Path("page") String page,
            @Body Appointment appointment
    );

    @POST("/{page}")
    Call<ArrayList<String>> getInstitutions(
            @Path("page") String page,
            @Body User user
    );
}
