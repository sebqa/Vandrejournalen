package com.example.sebastian.vandrejournalen.Results;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Patient;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BasicHealthInfoFragment extends Fragment {

    private static final String TAG = "BasicHealthInfoFragment";
    private OnFragmentInteractionListener mListener;
    MaterialEditText etMensDag, etCyklus, etGrav, ethojde, etBMI;
    RadioButton calcYes, calcNo,hepYes,hepNo, bloodyes, bloodNo, mrhesYes, mrhesNo, iregYes, iregNo, crhesYes, crhesNo, antiYes, antiNo, antiDYes, antiDNo;
    CheckBox urinDyrk;
    Context context;
    LinearLayout cLayout;
    Patient patient;
    User user;
    Button button;
    ServerClient client;
    BasicInfo basicInfo;

    public BasicHealthInfoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BasicHealthInfoFragment newInstance() {
        BasicHealthInfoFragment fragment = new BasicHealthInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            patient = new Gson().fromJson(getArguments().getString("patient","Patient"),Patient.class);
            user = new Gson().fromJson(getArguments().getString("user","Patient"),User.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_basic_health_info, container, false);
        cLayout = rootView.findViewById(R.id.cLayout);
        Log.d(TAG, "onCreateView: user og patient data"+user.getName()+patient.getName());

        initLayout(rootView);
        if(user.getRole().equals("Patient")){
            setEditable();
        }
        getBasicInfo();
        return rootView;
    }

    private void setEditable() {
        etMensDag.setFocusable(false);
        etCyklus.setFocusable(false);
        etGrav.setFocusable(false);
        ethojde.setFocusable(false);
        etBMI.setFocusable(false);
        calcYes.setFocusable(false);
        calcNo.setFocusable(false);
    }

    private void getBasicInfo() {
        client = ServiceGenerator.createService(ServerClient.class);
        Log.d(TAG, "getBasicInfo: JOURNALID"+ patient.getJournalID());
        Call<BasicInfo> call = client.getBasicInfo("returnJournalBasicHealth.php", patient.getJournalID());
        call.enqueue(new Callback<BasicInfo>() {
            @Override
            public void onResponse(Call<BasicInfo> call, Response<BasicInfo> response) {
                if(response.body() != null){
                    basicInfo = response.body();
                    basicInfo.setJournalID(patient.getJournalID());/*
                    Log.d(TAG, "onResponse: "+basicInfo.getMensDag());
                    Log.d(TAG, "onResponse: "+basicInfo.getCyklus());
                    Log.d(TAG, "onResponse: "+basicInfo.isbSikker());
                    Log.d(TAG, "onResponse: "+basicInfo.getGrav());
                    Log.d(TAG, "onResponse: "+basicInfo.getHojde());
                    Log.d(TAG, "onResponse: "+basicInfo.getBMI());
                    Log.d(TAG, "onResponse: "+basicInfo.isRhesus());
                    Log.d(TAG, "onResponse: "+basicInfo.isIrreg());
                    Log.d(TAG, "onResponse: "+basicInfo.isAntiD());
                    Log.d(TAG, "onResponse: "+basicInfo.getAntiDDate());
                    Log.d(TAG, "onResponse: "+basicInfo.getAntiDIni());
                    Log.d(TAG, "onResponse: "+basicInfo.getNaegel());
                    Log.d(TAG, "onResponse: "+basicInfo.getUltralydtermin());
                    Log.d(TAG, "onResponse: "+basicInfo.isHep());
                    Log.d(TAG, "onResponse: "+basicInfo.isBlodTaget());
                    Log.d(TAG, "onResponse: "+basicInfo.isBarnRhes());
                    Log.d(TAG, "onResponse: "+basicInfo.isAntiStof());
                    Log.d(TAG, "onResponse: "+basicInfo.isUrin());
                    Log.d(TAG, "onResponse: "+basicInfo.getUrinDate());
                    Log.d(TAG, "onResponse: "+basicInfo.getUrinIni());*/

                    try {
                        etMensDag.setText(basicInfo.getMensDag());
                        etCyklus.setText(basicInfo.getCyklus());
                        etGrav.setText(""+basicInfo.getGrav());
                        ethojde.setText(""+basicInfo.getHojde());
                        etBMI.setText(""+basicInfo.getBMI());
                        if(basicInfo.isbSikker()){
                            calcYes.toggle();
                        } else{
                            calcNo.toggle();
                        }
                        if(basicInfo.isbSikker()){
                            calcYes.toggle();
                        } else{
                            calcNo.toggle();
                        }
                        if(basicInfo.isHep()){
                            hepYes.toggle();
                        } else{
                            hepNo.toggle();
                        }
                        if(basicInfo.isBlodTaget()){
                            bloodyes.toggle();
                        } else{
                            bloodNo.toggle();
                        }
                        if(basicInfo.isRhesus()){
                            mrhesYes.toggle();
                        } else{
                            mrhesNo.toggle();
                        }
                        if(basicInfo.isIrreg()){
                            iregYes.toggle();
                        } else{
                            iregNo.toggle();
                        }
                        if(basicInfo.isBarnRhes()){
                            crhesYes.toggle();
                        } else{
                            crhesNo.toggle();
                        }
                        if(basicInfo.isAntiStof()){
                            antiYes.toggle();
                        } else{
                            antiNo.toggle();
                        }if(basicInfo.isAntiD()){
                            antiDYes.toggle();
                        } else{
                            antiDNo.toggle();
                        }
                        if(basicInfo.isUrin()){
                            urinDyrk.setChecked(true);
                        } else{
                            urinDyrk.setChecked(false);
                        }


                    } catch (NullPointerException e){
                        Log.d(TAG, "onResponse: Noget er null"+response.body().getMensDag());
                    }

                }

            }

            @Override
            public void onFailure(Call<BasicInfo> call, Throwable t) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initLayout(View rootView) {
        etMensDag = rootView.findViewById(R.id.lastMens);
        etMensDag.setHint(R.string.last_mens);
        etCyklus  = rootView.findViewById(R.id.cycle);
        etCyklus.setText(R.string.cycle);
        calcYes = rootView.findViewById(R.id.calcYes);
        calcNo = rootView.findViewById(R.id.calcNo);
        etGrav = rootView.findViewById(R.id.grav);
        ethojde = rootView.findViewById(R.id.hojde);
        etBMI = rootView.findViewById(R.id.BMI);
        hepYes = rootView.findViewById(R.id.hepYes);
        hepNo = rootView.findViewById(R.id.hepNo);
        bloodyes =rootView.findViewById(R.id.bloodYes);
        bloodNo = rootView.findViewById(R.id.bloodNo);
        mrhesYes = rootView.findViewById(R.id.mrhesYes);
        mrhesNo = rootView.findViewById(R.id.mrhesNo);
        iregYes = rootView.findViewById(R.id.iregYes);
        iregNo = rootView.findViewById(R.id.iregNo);
        crhesYes = rootView.findViewById(R.id.crhesYes);
        crhesNo = rootView.findViewById(R.id.crhesNo);
        antiYes = rootView.findViewById(R.id.antiYes);
        antiNo = rootView.findViewById(R.id.antiNo);
        antiDYes = rootView.findViewById(R.id.antidYes);
        antiDNo = rootView.findViewById(R.id.antidNo);
        urinDyrk = rootView.findViewById(R.id.urinYes);

        button = rootView.findViewById(R.id.savExitBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInfo();
                mListener.popStack();
            }
        });






    }

    private void sendInfo() {
        /*if(etMensDag.equals("")){

        } else{
            basicInfo.setMensDag("i morgen");
            basicInfo.setCyklus("rundt og rundt");
            basicInfo.setbSikker(true);
            basicInfo.setGrav("81");
            basicInfo.setHojde("152");
            basicInfo.setBMI("91");
            basicInfo.setHep(true);
            basicInfo.setBlodTaget(true);
            basicInfo.setRhesus(false);
            basicInfo.setIrreg(true);
            basicInfo.setBarnRhes(false);
            basicInfo.setAntiStof(false);
            basicInfo.setAntiD(true);
            basicInfo.setAntiDDate("I går");
            basicInfo.setAntiDIni("DINMOR");
            basicInfo.setUrin(true);
            basicInfo.setUrinDate("hold hænderne frem");
            basicInfo.setUrinIni("OKOS");
        }*/


        Call<String> call = client.sendBasic("returnJournalBasicHealth.php",basicInfo);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body() != null){

                    try {
                        if(response.body().equals("TRUE")) {
                            Toast.makeText(context, R.string.info_saved, Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(context, "Something happened", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NullPointerException e){
                        Toast.makeText(context, "Nullpointer", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void popStack();
    }
}
