package com.example.sebastian.vandrejournalen.authentication;

import android.content.Context;
import android.net.Uri;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private View rootView;
    private OnFragmentInteractionListener mListener;
    LinearLayout vLinearLayout;
    Context context;
    MaterialEditText etCPR;
    MaterialEditText etRegisterPatient;
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
        Bundle args = new Bundle();
        Gson gson = new Gson();
        String obj = gson.toJson(user);
        args.putString("obj" , obj);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = new Gson().fromJson(getArguments().getString("obj"), User.class);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_register_patient, container, false);
        setHasOptionsMenu(true);
        vLinearLayout = rootView.findViewById(R.id.layoutRegisterPatient);
        Log.d(TAG, "onCreateView: "+user.getUserID());
        etRegisterPatient = new MaterialEditText(context);
        client = ServiceGenerator.createService(ServerClient.class);
        etCPR = rootView.findViewById(R.id.etCPR);
        btnCreateUser = rootView.findViewById(R.id.btnCreateUser);
        startJournal = rootView.findViewById(R.id.startBtn);

        // Get CPR number from EditText to string
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cpr = etCPR.getText().toString();

                Log.d(TAG, "onClick: "+cpr);

                registerExpectedUser(cpr);
                }
        });

        startJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                cpr = etCPR.getText().toString();
                checkCPR();



            }
        });

        return rootView;
    }
    private void registerExpectedUser(String cpr) {
        if (cpr.contains("-")){

        } else{
            StringBuilder str = new StringBuilder(cpr);
            str.insert(6,"-");
            cpr = str.toString();
        }
        Patient nUser = new Patient();
        nUser.setCpr(cpr);
        nUser.setProfUserID(user.getUserID());

        Call<String> call = client.cprExp("registerExpectedPatient.php",nUser );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: " + response.body());
                if (response.body() != null) {

                    if (response.body().trim().equals("1")) {
                        Toast.makeText(getActivity(), "CPR received", Toast.LENGTH_SHORT).show();
                        startJournal.setVisibility(View.VISIBLE);

                    } else if (response.body().trim().equals("0")) {
                        Toast.makeText(context, "CPR Exists", Toast.LENGTH_SHORT).show();
                    } else {
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
        patient.setCpr(cpr);
        patient.setProfUserID(user.getUserID());

        Call<Patient> call = client.checkPatientExists("returnPatientInformation.php",patient.getCpr() );

        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if(response.body() != null) {
                    patient = response.body();
                    sendProfUserID();
                }

            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendProfUserID(){
        Call<User> call = client.startJournal("returnProfInformation.php",patient.getProfUserID() );

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "onResponse: "+patient.getName());

                user.setName(response.body().getName());
                user.setAddress(response.body().getAddress());
                user.setEmail(response.body().getEmail());
                user.setPhonework(response.body().getPhonework());

                mListener.startJournal(patient,user);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
       // TODO: Update argument type and name
       void startJournal(Patient patient, User user);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
        this.context = context;
    }
}