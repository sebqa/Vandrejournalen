package com.example.sebastian.vandrejournalen.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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


public class LoginFragment extends Fragment {
    String token;
    TextView tvNotReg;
    MaterialEditText passwordInput, usernameInput;
    Button button;
    User user;
    ServerClient client;
    SharedPreferences prefs;


    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Retreive token string and User object from the bundle
            token = getArguments().getString("token");
            user = new Gson().fromJson(getArguments().getString("user"), User.class);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        passwordInput = rootView.findViewById(R.id.passwordInput);
        usernameInput = rootView.findViewById(R.id.usernameInput);
        button = rootView.findViewById(R.id.button);
        tvNotReg = rootView.findViewById(R.id.tvNotReg);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        //Create Http Client
        client = ServiceGenerator.createService(ServerClient.class);

        //If a users token has been set
        if(user.getToken() != null){
            checkToken();
        }

        //Add a listener to make "Enter" press execute method
        passwordInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    checkCred();
                    return true;
                }
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    checkCred();
                }
            }
        });

        tvNotReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Callback to go to previous fragment
                mListener.popStack();
            }
        });


        return rootView;
    }

    private void checkToken() {
        //Check token
        Call<String> call = client.checkToken("logInCheckToken.php",user.getToken() );
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Check if any content is set
                if (response.body() != null) {
                    //Check if the query was successful
                    if (!response.body().trim().equals("FALSE")){
                        //Create new user with the user id received
                        user = new User();
                        user.setUserID(response.body().trim());
                        //Callback to change fragment, sending along the User object
                        mListener.loginExists(user);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkCred() {
        //Validate format of CPR-number and add it to User object
        String cpr = usernameInput.getText().toString().trim();
        if (cpr.contains("-")){
            user.setCpr(cpr);
        } else{
            StringBuilder str = new StringBuilder(cpr);
            str.insert(6,"-");
            user.setCpr(str.toString());
            Log.d(TAG, "checkCred: "+user.getCpr());
        }
        //Add password input to User object
        user.setPassword(passwordInput.getText().toString().trim());
        //Remove potential error text
        passwordInput.setHelperTextAlwaysShown(false);

        //Make a request with user CPR and password
        Call<User> call = client.login("logInCprPw.php", user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                //Check if there are any contents
                if (response.body() !=null) {
                    //Check if there is a user ID set
                    if(response.body().getUserID() ==null){
                        //Set error messages
                        passwordInput.setHelperTextColor(Color.parseColor("#D50000"));
                        passwordInput.setHelperTextAlwaysShown(true);
                        passwordInput.setHelperText("Wrong CPR or Password");

                    } else {
                        //Add the user id and token to the User object
                        user.setUserID(response.body().getUserID());
                        user.setToken(response.body().getToken());
                        //Clear CPR and Password attributes
                        user.setCpr("");
                        user.setPassword("");
                        //Callback to change fragment
                        mListener.loginExists(user);
                        passwordInput.setHelperText("");
                    }

                }else {
                    Log.d(TAG, "onResponse: "+"Den er null");
                    Toast.makeText(getActivity(), "Wrong CPR or password", Toast.LENGTH_SHORT).show();
                }
                //                    mListener.loginExists(user);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });



    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Initialize interface with activity context
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        //Interface methods
        void loginExists(User user);
        void popStack();
    }
}
