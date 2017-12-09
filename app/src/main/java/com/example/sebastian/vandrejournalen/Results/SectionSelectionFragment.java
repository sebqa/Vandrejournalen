package com.example.sebastian.vandrejournalen.Results;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Patient;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class SectionSelectionFragment extends Fragment implements View.OnClickListener{
    User user;
    TextView tvHeadline, tvRole, tvName, tvAddress, tvPhone,tvEmail;
    LinearLayout tvBasic,tvCons,tvTests,tvUltra,tvDiab,tvPoB;
    private OnFragmentInteractionListener mListener;
    Context context;
    Patient patient;
    Patient prof;
    ServerClient client;

    public SectionSelectionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SectionSelectionFragment newInstance() {
        SectionSelectionFragment fragment = new SectionSelectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if(getArguments().getString("patient",null)!= null) {
                patient = new Gson().fromJson(getArguments().getString("patient", "Patient"), Patient.class);
            }
            user = new Gson().fromJson(getArguments().getString("user","Patient"),User.class);        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        client = ServiceGenerator.createService(ServerClient.class);
        View rootView =inflater.inflate(R.layout.fragment_section_selection, container, false);

        tvHeadline = rootView.findViewById(R.id.tvHeadline);
        tvRole= rootView.findViewById(R.id.tvRole);
        tvName= rootView.findViewById(R.id.tvName);
        tvAddress= rootView.findViewById(R.id.tvAddress);
        tvPhone= rootView.findViewById(R.id.tvPhone);
        tvEmail= rootView.findViewById(R.id.tvEmail);
        tvBasic= rootView.findViewById(R.id.basicInfoLink);
        tvCons= rootView.findViewById(R.id.consultationLink);
        tvTests= rootView.findViewById(R.id.testLink);
        tvUltra= rootView.findViewById(R.id.ultraSoundLink);
        tvDiab= rootView.findViewById(R.id.diabeetesLink);
        tvPoB= rootView.findViewById(R.id.birthPlaceLink);


        tvBasic.setOnClickListener(this);
        tvCons.setOnClickListener(this);
        tvTests.setOnClickListener(this);
        tvUltra.setOnClickListener(this);
        tvDiab.setOnClickListener(this);
        tvPoB.setOnClickListener(this);

        if(user.getRole().equals("Patient")) {
            getProfInfo();
        } else{
            getPatientInfo();
        }


        return rootView;
    }

    private void getPatientInfo() {
        tvHeadline.append(" - "+patient.getCpr());
        tvRole.setText("Patient");
        tvName.append(patient.getName());
        tvAddress.append(patient.getAddress());
        tvEmail.append(patient.getEmail());
        tvPhone.append("" + patient.getPhonework()+"\n"+patient.getPhoneprivate() );
    }

    private void getProfInfo() {
        Call<Patient> call = client.getProfInfo("findGpInfo.php",user.getUserID() );

        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                if(response.body() != null) {
                    prof = response.body();
                    Log.d(TAG, "onResponse: "+prof.getName());
                    try {
                        tvRole.setText(getResources().getString(R.string.gp));
                        tvName.append(prof.getName());
                        tvAddress.append(prof.getAddress());
                        tvEmail.append(prof.getEmail());
                        tvPhone.append("" + prof.getPhonework());
                    }catch (NullPointerException e){
                        Log.d(TAG, "onResponse: noget var null");
                    }
                }
                else{
                    Log.d(TAG, "onResponse: body is null"+response.body());
                }

            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = new Fragment();
        switch (view.getId()){
            case R.id.basicInfoLink:
                Bundle args = new Bundle();
                String obj = new Gson().toJson(user);
                args.putString("user" , obj);
                String obj1 = new Gson().toJson(patient);
                args.putString("patient" , obj1);
                fragment = BasicHealthInfoFragment.newInstance();
                fragment.setArguments(args);
                break;
            case R.id.consultationLink:
                fragment = ResultsPager.newInstance(user);

                break;
            case R.id.testLink:
                break;
            case R.id.ultraSoundLink:
                break;
            case R.id.diabeetesLink:
                break;
            case R.id.birthPlaceLink:
                break;
        }
        mListener.updateFragment(fragment);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void updateFragment(Fragment fragment);
        void openBasicHealth(Patient patient,User user);
    }
}
