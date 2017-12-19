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
import android.widget.Toast;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Patient;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            user = gson.fromJson(getArguments().getString("user"), User.class);
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patients_list,container,false);
        setHasOptionsMenu(true);
        recyclerView =  rootView.findViewById(R.id.recyclerView);
        //Create Http Client
        client = ServiceGenerator.createService(ServerClient.class);

        //send User object to get a list of patients
        Call<ArrayList<Patient>> call = client.getPatients("returnMyPatients.php", user);
        //Load list
        initList();
        //Listen for response
        call.enqueue(new Callback<ArrayList<Patient>>() {
            @Override
            public void onResponse(Call<ArrayList<Patient>> call, Response<ArrayList<Patient>> response) {
                //Check response contents
                if(response.body() != null){
                    //Reset list
                    patients.clear();
                    //Add response body list to patients list
                    patients.addAll(response.body());
                    //Tell the adapter that we have new information
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Patient>> call, Throwable t) {
                Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
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
        //Create fragment instance
        PatientsList fragment = new PatientsList();
        //Create package
        Bundle args = new Bundle();
        Gson gson = new Gson();
        //Convert User object to a jsonstring
        String obj = gson.toJson(user);
        //Add jsonstring to package
        args.putString("user" , obj);
        fragment.setArguments(args);
        return fragment;
    }

    public void initList(){
        //Add vertical divider lines
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
        //Create adapter with list of patients and context
        adapter = new RecyclerAdapterPatientList(patients, getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


}
