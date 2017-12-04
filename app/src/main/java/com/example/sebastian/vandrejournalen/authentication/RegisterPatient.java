package com.example.sebastian.vandrejournalen.authentication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.sebastian.journalapp.R;
import com.rengwuxian.materialedittext.MaterialEditText;



public class RegisterPatient extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    private OnFragmentInteractionListener mListener;
    LinearLayout vLinearLayout;
    Context context;

    MaterialEditText etRegisterPatient;

    public RegisterPatient() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RegisterPatient newInstance(String role) {
        RegisterPatient fragment = new RegisterPatient();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, role);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_register_patient, container, false);
        setHasOptionsMenu(true);
        vLinearLayout = rootView.findViewById(R.id.layoutRegisterPatient);

        etRegisterPatient = new MaterialEditText(context);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
 
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

   /* public void initLayout() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = 16;

        // Register Patient EditTextView
        etRegisterPatient.setText("Register User");
        etRegisterPatient.setFloatingLabelAlwaysShown(true);
        etRegisterPatient.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etRegisterPatient.setFloatingLabelText("Register User");
        vLinearLayout.addView(etRegisterPatient);
    }   */
}