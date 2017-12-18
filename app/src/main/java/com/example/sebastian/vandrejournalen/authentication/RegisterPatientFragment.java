package com.example.sebastian.vandrejournalen.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Patient;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class RegisterPatientFragment extends Fragment {

    //This fragment is for registration of new patients by the GP, as well as starting a new journal for those patients.
    private OnFragmentInteractionListener mListener;
    LinearLayout vLinearLayout;
    Context context;
    MaterialEditText etCPR;
    Button btnCreateUser, startJournal;
    User user;
    String cpr;
    Patient patient = new Patient();
    ServerClient client;

    public RegisterPatientFragment() {
        // Required empty public constructor
    }

    public static RegisterPatientFragment newInstance(User user) {
        RegisterPatientFragment fragment = new RegisterPatientFragment();
        //Add Bundle to fragment
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String obj = gson.toJson(user);
        args.putString("user" , obj);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Get User object from Bundle
            user = new Gson().fromJson(getArguments().getString("user"), User.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register_patient, container, false);
        setHasOptionsMenu(true);
        vLinearLayout = rootView.findViewById(R.id.layoutRegisterPatient);
        etCPR = rootView.findViewById(R.id.etCPR);
        btnCreateUser = rootView.findViewById(R.id.btnCreateUser);
        startJournal = rootView.findViewById(R.id.startBtn);

        //Create Http Client
        client = ServiceGenerator.createService(ServerClient.class);

        // Get CPR number from EditText to string
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cpr = etCPR.getText().toString();
                registerExpectedUser(cpr);
                }
        });

        startJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cpr = etCPR.getText().toString();
                checkCPR();
            }
        });
        return rootView;
    }

    private void registerExpectedUser(String cpr) {
        //Check CPR-number format
        if (cpr.contains("-")){

        } else{
            StringBuilder str = new StringBuilder(cpr);
            str.insert(6,"-");
            cpr = str.toString();
        }

        //Create Patient and add attributes
        Patient nUser = new Patient();
        nUser.setCpr(cpr);
        nUser.setProfUserID(user.getUserID());

        //Send Patient object
        Call<String> call = client.cprExp("registerExpectedPatient.php",nUser );

        //Listen for response
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Check contents of response
                if (response.body() != null) {
                    //If the query was successful
                    if (response.body().trim().equals("1")) {
                        Toast.makeText(getActivity(), "CPR received", Toast.LENGTH_SHORT).show();
                    }
                    //If the patient has already registered
                    else if (response.body().trim().equals("0")) {
                        Toast.makeText(context, "CPR Exists, start journal", Toast.LENGTH_SHORT).show();
                    } else {
                        //If query fails
                        Toast.makeText(getActivity(), "CPR not received", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void checkCPR() {

        //Check format of CPR-number
        String cpr =etCPR.getText().toString();
        if (cpr.contains("-")){
            patient.setCpr(cpr);
        } else{
            StringBuilder str = new StringBuilder(cpr);
            str.insert(6,"-");
            patient.setCpr(str.toString());
        }

        //Add User id to patient
        patient.setProfUserID(user.getUserID());

        //Network request to retreive information about the patient, i.e. name, address, email, phones
        Call<Patient> call = client.getPatientInfo("returnPatientInformation.php",patient.getCpr() );
        //Listen for request response
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                //Check if contents are not null
                if(response.body() != null) {
                    //Add attributes of response to Patient object
                    patient = response.body();
                    //Proceed to next network call
                    sendProfUserID();
                } else{
                    Toast.makeText(context, R.string.patient_not_registered, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendProfUserID(){
        //Make network call for prof info
        Call<User> call = client.startJournal("returnProfInformation.php",user.getUserID() );
        //Listen for response
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                //Check response contents
                if(response.body() != null){
                    //Add attributes to User object
                    user.setName(response.body().getName());
                    user.setAddress(response.body().getAddress());
                    user.setEmail(response.body().getEmail());
                    user.setPhonework(response.body().getPhonework());

                    //Proceed to next network call
                    createJournal();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createJournal() {
        //Check format of CPR-number
        String cpr = etCPR.getText().toString();
        if (cpr.contains("-")){

        } else{
            StringBuilder str = new StringBuilder(cpr);
            str.insert(6,"-");
            cpr = str.toString();
        }
        //Add attributes to Patient object
        patient.setCpr(cpr);
        patient.setProfUserID(user.getUserID());

        //Network call to start a new journal for this user
        Call<String> call = client.cprExp("createJournal.php",patient );
        //Listen for response
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Check response contents
                if (response.body() != null) {
                    //Check response value
                    if(!response.body().trim().equals("0")) {
                        //Update patient values, add journalID, remove CPR
                        patient.setCpr("");
                        patient.setJournalID(response.body());
                        //Callback to replace fragment
                        mListener.startJournal(patient, user);
                    } else {
                        Toast.makeText(context, "Journal exists or patient doesn't", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        //Interface methods
        void startJournal(Patient patient, User user);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        this.context = context;
    }
}