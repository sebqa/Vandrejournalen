package com.example.sebastian.vandrejournalen.authentication;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class LoginFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MaterialEditText passwordInput, usernameInput;
    private String mParam1;
    private String mParam2;
    Button button;
    private View rootView;
    User user = new User();
    ServerClient client;

    int keyDel;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        passwordInput = rootView.findViewById(R.id.passwordInput);
        usernameInput = rootView.findViewById(R.id.usernameInput);
        button = rootView.findViewById(R.id.button);

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
        /*qrbutton = rootView.findViewById(R.id.qrbutton);
        cipherText = rootView.findViewById(R.id.cipherText);
        drbutton = rootView.findViewById(R.id.drbutton);
        mwbutton = rootView.findViewById(R.id.mwbutton);*/

        client = ServiceGenerator.createService(ServerClient.class);

        /*mwbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {

                    encryptedString = mListener.encrypt(passwordInput.getText().toString());
                    cipherText.setText(encryptedString);
                }
            }
        });
        drbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    decryptedString = mListener.decrypt(encryptedString);
                    cipherText.setText(decryptedString);
                }
            }
        });*/
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    checkCred();
                }
            }
        });


        return rootView;
    }

    private void checkCred() {
        String cpr = usernameInput.getText().toString().trim();
        if (cpr.contains("-")){
            user.setCpr(cpr);
        } else{
            StringBuilder str = new StringBuilder(cpr);
            str.insert(6,"-");
            user.setCpr(str.toString());
            Log.d(TAG, "checkCred: "+user.getCpr());
        }
        user.setPassword(passwordInput.getText().toString().trim());

        passwordInput.setHelperTextAlwaysShown(false);

        Call<User> call = client.login("logInCprPw.php", user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if (response.body() !=null) {
                    if(response.body().getUserID() ==null){
                        passwordInput.setHelperTextColor(Color.parseColor("#D50000"));
                        passwordInput.setHelperTextAlwaysShown(true);
                        passwordInput.setHelperText("Wrong CPR or Password");

                    } else {
                        Log.d(TAG, "onResponse: " + response.body().getUserID());
                        user.setUserID(response.body().getUserID());
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
        void loginExists(User user);
        String encrypt(String passwordString);
        String decrypt(String passwordString);

        void register(User user);
    }
}
