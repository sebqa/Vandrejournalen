package com.example.sebastian.vandrejournalen.authentication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class RegisterInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    ServerClient client;

    // TODO: Rename and change types of parameters
    MaterialEditText etCPR, etName, etAddress, etPrivateTlf, etWorkTlf, etPassword, etVerifypass;
    Context context;
    Button sendButton, backButton;
    LinearLayout cLayout;
    private OnFragmentInteractionListener mListener;
    private View rootView;
    User user;
    public RegisterInfoFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RegisterInfoFragment newInstance(User user) {
        RegisterInfoFragment fragment = new RegisterInfoFragment();
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
            Gson gson = new Gson();
            user = gson.fromJson(getArguments().getString("obj"), User.class);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_register_info, container, false);
        client = ServiceGenerator.createService(ServerClient.class);

        cLayout = rootView.findViewById(R.id.layoutRegisterInfo);

        etCPR = new MaterialEditText(context);
        etName = new MaterialEditText(context);
        etAddress = new MaterialEditText(context);
        etPrivateTlf = new MaterialEditText(context);
        etWorkTlf = new MaterialEditText(context);
        etPassword = new MaterialEditText(context);
        etVerifypass = new MaterialEditText(context);

        sendButton = rootView.findViewById(R.id.sendButton);
        backButton = rootView.findViewById(R.id.backButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getInstitution() != null) {
                    user.setInstitution(user.getInstitution());
                    user.setAddress(user.getAddress());
                }
                String cpr = etCPR.getText().toString().trim();
                if (cpr.contains("-")){
                    user.setCpr(cpr);
                } else{
                    StringBuilder str = new StringBuilder(cpr);
                    str.insert(6,"-");
                    user.setCpr(str.toString());
                    Log.d(TAG, "checkCred: "+user.getCpr());
                }
                user.setName(etName.getText().toString());
                user.setPhoneprivate(Integer.parseInt(etPrivateTlf.getText().toString()));
                user.setPhonework(Integer.parseInt(etWorkTlf.getText().toString()));
                user.setPassword(etPassword.getText().toString());

                Call<String> call = client.registerInfo("registerInformation.php", user);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(!response.body().trim().equals("FALSE")){
                            user.setUserID(response.body().trim());
                            mListener.onSuccessfulLogin(user);
                        }
                        Log.d(TAG, "onResponse: "+response.body().trim());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(isAdded())
                    initLayout();

            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
        this.context=context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void loginExists(User user);
        void onSuccessfulLogin(User user);
    }

    public void initLayout() {
        etCPR.setFloatingLabelAlwaysShown(true);
        etCPR.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etCPR.setInputType(InputType.TYPE_CLASS_NUMBER);
        etCPR.setFloatingLabelText("CPR-number");
        cLayout.addView(etCPR);
        if(user.getCpr() != null){
            etCPR.setText(user.getCpr());
            etCPR.setFocusable(false);
            user.setRole("Patient");
        }
        etName.setFloatingLabelAlwaysShown(true);
        etName.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etName.setFloatingLabelText("Name");
        cLayout.addView(etName);

        etAddress.setFloatingLabelAlwaysShown(true);
        etAddress.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etAddress.setFloatingLabelText("Address");
        cLayout.addView(etAddress);

        etPrivateTlf.setFloatingLabelAlwaysShown(true);
        etPrivateTlf.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etPrivateTlf.setInputType(InputType.TYPE_CLASS_NUMBER);
        etPrivateTlf.setFloatingLabelText("Private telephone number");
        cLayout.addView(etPrivateTlf);

        etWorkTlf.setFloatingLabelAlwaysShown(true);
        etWorkTlf.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etWorkTlf.setInputType(InputType.TYPE_CLASS_NUMBER);
        etWorkTlf.setFloatingLabelText("Work telephone number");
        cLayout.addView(etWorkTlf);

        etPassword.setFloatingLabelAlwaysShown(true);
        etPassword.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etPassword.setFloatingLabelText("Password");
        cLayout.addView(etPassword);

        etVerifypass.setFloatingLabelAlwaysShown(true);
        etVerifypass.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etVerifypass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etVerifypass.setFloatingLabelText("Verify password");
        cLayout.addView(etVerifypass);



    }

}
