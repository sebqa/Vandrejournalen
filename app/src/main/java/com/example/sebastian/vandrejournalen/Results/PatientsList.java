package com.example.sebastian.vandrejournalen.Results;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Patient;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.calendar.RecyclerAdapterNotesList;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Sebastian on 30-12-2016.
 */

public class PatientsList extends Fragment {

    private static final String TAG = "PATIENTSLIT";
    ArrayList<Patient> patients = new ArrayList<Patient>();
    RecyclerView recyclerView;
    RecyclerAdapterPatientList adapter;
    RecyclerView.LayoutManager layoutManager;
    User user;
    ServerClient client;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            user = gson.fromJson(getArguments().getString("obj"), User.class);        }

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patients_list,container,false);
        setHasOptionsMenu(true);
        recyclerView =  rootView.findViewById(R.id.recyclerView);
        client = ServiceGenerator.createService(ServerClient.class);
        Call<ArrayList<Patient>> call = client.getPatients("returnMyPatients.php", user.getUserID() );
        initList();

        call.enqueue(new Callback<ArrayList<Patient>>() {
            @Override
            public void onResponse(Call<ArrayList<Patient>> call, Response<ArrayList<Patient>> response) {
                if(response.body() != null){
                    patients.clear();
                    Log.d(TAG, "onResponse: første user name"+response.body().get(0).getName());
                    patients.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Patient>> call, Throwable t) {

            }
        });


        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        //Detach listeners
    }

    public static PatientsList newInstance(User user) {
        PatientsList fragment = new PatientsList();
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String obj = gson.toJson(user);
        args.putString("obj" , obj);
        fragment.setArguments(args);
        return fragment;
    }

    public void initList(){

        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        adapter = new RecyclerAdapterPatientList(patients, getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


}
