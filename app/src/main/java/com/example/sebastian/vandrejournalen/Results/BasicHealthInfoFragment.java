package com.example.sebastian.vandrejournalen.Results;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Patient;
import com.example.sebastian.vandrejournalen.User;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;


public class BasicHealthInfoFragment extends Fragment {

    private static final String TAG = "BasicHealthInfoFragment";
    private OnFragmentInteractionListener mListener;
    MaterialEditText etMensDag, etCyklus, etBeregningSikker, etGrav, ethojde, etBMI,etHep, etBlodTaget, etRhesus, etIrreg, etBarnRhe, etAntistof, etAntiD, etInit, etUrinDyrk;
    Context context;
    LinearLayout cLayout;
    Patient patient;
    User user;
    Button button;



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
        initLayout();
        return rootView;
    }

    private void initLayout() {
        etMensDag = new MaterialEditText(context);
        etCyklus = new MaterialEditText(context);
        etBeregningSikker = new MaterialEditText(context);
        etGrav = new MaterialEditText(context);
        ethojde = new MaterialEditText(context);
        etBMI = new MaterialEditText(context);
        etHep = new MaterialEditText(context);
        etBlodTaget = new MaterialEditText(context);
        etRhesus = new MaterialEditText(context);
        etIrreg = new MaterialEditText(context);
        etBarnRhe = new MaterialEditText(context);
        etAntistof = new MaterialEditText(context);
        etAntiD = new MaterialEditText(context);
        etInit = new MaterialEditText(context);
        etUrinDyrk = new MaterialEditText(context);
        button = new Button(context);
        button.setText(getResources().getString(R.string.cont));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInfo();
            }
        });
        cLayout.addView(etMensDag);
        cLayout.addView(etCyklus);
        cLayout.addView(etBeregningSikker);
        cLayout.addView(etGrav);
        cLayout.addView(ethojde);
        cLayout.addView(etBMI);
        cLayout.addView(etHep);
        cLayout.addView(etBlodTaget);
        cLayout.addView(etRhesus);
        cLayout.addView(etIrreg);
        cLayout.addView(etBarnRhe);
        cLayout.addView(etAntistof);
        cLayout.addView(etAntiD);
        cLayout.addView(etInit);
        cLayout.addView(etUrinDyrk);
        cLayout.addView(button);






    }

    private void sendInfo() {
        BasicInfo basicInfo = new BasicInfo();
        if(etMensDag.equals("")){

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
        }





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
        void startJournal(User user);
    }
}
