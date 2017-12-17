package com.example.sebastian.vandrejournalen.authentication;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.MainActivity;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class RegisterFragment extends Fragment {

    ServerClient client;
    MaterialEditText etInput;
    Button continueBtn;
    TextView tvSignedUp;
    User user;
    private OnFragmentInteractionListener mListener;


    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        etInput = rootView.findViewById(R.id.keyInput);
        tvSignedUp = rootView.findViewById(R.id.tvSignedUp);

        //Initialize the Http client
        client = ServiceGenerator.createService(ServerClient.class);

        tvSignedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToLogin();
            }
        });

        //Add an onKeyLister to the button
        etInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                //Check if "Enter" button is pressed
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    onContinue();
                    return true;
                }
                return false;
            }
        });
        continueBtn = rootView.findViewById(R.id.continueBtn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onContinue();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Define the interface with the activity context
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        //Interface methods
        void goToLogin();
        void goToInfo(User user);
    }


    public void onContinue(){

        //Disable input field
        etInput.setEnabled(false);

        //Check input length
        final String input = etInput.getText().toString();
        if (input.length() == 36) {
            //If its a key...
            //Send the key to appropriate script
            Call<User> call = client.checkKey("checkKey.php", etInput.getText().toString());

            //Listen for response
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    //Response received in correct format
                    //Check if the contents are not null
                    if (response.body() != null) {
                        //Add all attributes of response to the user object
                        user = response.body();

                        //Create a dialog with User role and institution
                        new MaterialDialog.Builder(getActivity())
                                .title("Verification")
                                .content("Please verify that you are a " +user.getRole() + " at " + user.getInstitution())
                                .positiveText("Yes, continue")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        mListener.goToInfo(user);

                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        etInput.setEnabled(true);
                                    }
                                })
                                .negativeText("No, exit")
                                .dismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        etInput.setEnabled(true);
                                    }
                                })
                                .show();
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                    etInput.setEnabled(true);

                }
            });
        } else if(input.length() == 11 || input.length() == 10) {
            //If its a CPR
            //Check if hyphen was added
            String cpr = etInput.getText().toString().trim();
            if(input.length() == 10){
                if (cpr.contains("-")){

                } else{
                    //Add hyphen
                    StringBuilder str = new StringBuilder(cpr);
                    str.insert(6,"-");
                    cpr = str.toString();
                }
            }
            //Send the CPR to the appropriate script
            Call<String> call = client.checkCPR("checkCPR.php", cpr);
            final String finalCpr = cpr;
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.body() != null){
                        //If the response is "1"
                        if(response.body().trim().equals("1")) {
                            Toast.makeText(getActivity(), "Match!", Toast.LENGTH_SHORT).show();

                            //Add attributes to User object
                            User user = new User();
                            user.setCpr(finalCpr);

                            //Call for fragment transition
                            mListener.goToInfo(user);
                        } else{
                            Toast.makeText(getActivity(), "No Match!", Toast.LENGTH_SHORT).show();
                            etInput.setEnabled(true);
                        }


                    } else{
                        Toast.makeText(getActivity(), "No Match!", Toast.LENGTH_SHORT).show();
                        etInput.setEnabled(true);
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                    etInput.setEnabled(true);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Wrong input length!", Toast.LENGTH_SHORT).show();
            etInput.setEnabled(true);
        }



    }
}
