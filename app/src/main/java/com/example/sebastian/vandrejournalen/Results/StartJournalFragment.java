package com.example.sebastian.vandrejournalen.Results;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Patient;
import com.example.sebastian.vandrejournalen.User;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;


public class StartJournalFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    MaterialEditText etMensDag, etCyklus, etBeregningSikker, etGrav, ethojde, etBMI,etHep, etBlodTaget, etRhesus, etIrreg, etBarnRhe, etAntistof, etAntiD, etInit, etUrinDyrk;
    Context context;
    LinearLayout cLayout;
    Patient patient;
    User user;



    public StartJournalFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static StartJournalFragment newInstance() {
        StartJournalFragment fragment = new StartJournalFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            patient = new Gson().fromJson(getArguments().getString("patient","Midwife"),Patient.class);

            user = new Gson().fromJson(getArguments().getString("user","Midwife"),User.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_start_journal, container, false);
        cLayout = rootView.findViewById(R.id.cLayout);

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
