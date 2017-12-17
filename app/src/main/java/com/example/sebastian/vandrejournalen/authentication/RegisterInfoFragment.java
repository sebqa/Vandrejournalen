package com.example.sebastian.vandrejournalen.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
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


    ServerClient client;
    MaterialEditText etCPR, etName, etAddress, etPrivateTlf, etWorkTlf, etPassword, etVerifypass;
    Context context;
    Button sendButton;
    LinearLayout cLayout;
    private OnFragmentInteractionListener mListener;
    User user;
    SharedPreferences prefs;

    public RegisterInfoFragment() {
        // Required empty public constructor
    }

    public static RegisterInfoFragment newInstance() {
        RegisterInfoFragment fragment = new RegisterInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            user = gson.fromJson(getArguments().getString("user"), User.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register_info, container, false);
        //Create Http Client
        client = ServiceGenerator.createService(ServerClient.class);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        cLayout = rootView.findViewById(R.id.layoutRegisterInfo);

        //Create ET's
        etCPR = new MaterialEditText(context);
        etName = new MaterialEditText(context);
        etAddress = new MaterialEditText(context);
        etPrivateTlf = new MaterialEditText(context);
        etWorkTlf = new MaterialEditText(context);
        etPassword = new MaterialEditText(context);
        etVerifypass = new MaterialEditText(context);

        sendButton = rootView.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPassword == etVerifypass) {

                    //Add attributes to the user object
                    if (user.getInstitution() != null) {
                        user.setInstitution(user.getInstitution());
                        user.setAddress(user.getAddress());
                    }
                    //Check CPR-number format
                    String cpr = etCPR.getText().toString().trim();
                    if (cpr.contains("-")) {
                        user.setCpr(cpr);
                    } else {
                        StringBuilder str = new StringBuilder(cpr);
                        str.insert(6, "-");
                        user.setCpr(str.toString());
                    }
                    user.setName(etName.getText().toString());

                    //Catch parse errors, in case input is not a number
                    try {
                        //Check if input is empty
                        if (!etPrivateTlf.getText().toString().equals("")) {
                            user.setPhoneprivate(Integer.parseInt(etPrivateTlf.getText().toString()));
                        }
                        user.setPhonework(Integer.parseInt(etWorkTlf.getText().toString()));
                        user.setPassword(etPassword.getText().toString());

                        Call<User> call = client.registerInfo("registerInformation.php", user);
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                //Check if the response is not null
                                if (response.body() != null) {
                                    //Add attributes to User object
                                    user.setUserID(response.body().getUserID());
                                    user.setToken(response.body().getToken());
                                    //Call to Activity
                                    mListener.onSuccessfulLogin(user);
                                } else {
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (NumberFormatException e) {
                        Toast.makeText(context, "Wrong input type", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(context, "Passwords don't match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Start a new UI thread to create the layout
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                //Check if the fragment has been properly added to the layout
                if(isAdded())
                    initLayout();
            }
        });

        return rootView;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        //Define the interface with the activity context
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        this.context=context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        //Interface methods
        void onSuccessfulLogin(User user);
    }

    public void initLayout() {
        //Initialize input fields
        //Set labels and the know information

        etCPR.setFloatingLabelAlwaysShown(true);
        etCPR.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etCPR.setInputType(InputType.TYPE_CLASS_NUMBER);
        etCPR.setFloatingLabelText("CPR-number");
        cLayout.addView(etCPR);

        etAddress.setFloatingLabelAlwaysShown(true);
        etAddress.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etAddress.setFloatingLabelText("Address");

        //Check if CPR-number is set
        if(user.getCpr() != null){
            //If a CPR-number is set, the user is a patient
            etCPR.setText(user.getCpr());
            etCPR.setFocusable(false);
            user.setRole("Patient");
        } else{
            //Else set attributes know about professional
            etAddress.setText(user.getInstitution());
            etAddress.setFloatingLabelText("Institution");
        }
        etName.setFloatingLabelAlwaysShown(true);
        etName.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etName.setFloatingLabelText("Name");
        cLayout.addView(etName);
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
