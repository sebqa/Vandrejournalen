package com.example.sebastian.vandrejournalen.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.User;


public class LoginFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    EditText passwordInput, usernameInput;
    private String mParam1;
    private String mParam2;
    Button button,qrbutton, drbutton, mwbutton;
    private View rootView;
    TextView cipherText;
    String encryptedString;
    String decryptedString;

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
        qrbutton = rootView.findViewById(R.id.qrbutton);
        cipherText = rootView.findViewById(R.id.cipherText);
        drbutton = rootView.findViewById(R.id.drbutton);
        mwbutton = rootView.findViewById(R.id.mwbutton);


        mwbutton.setOnClickListener(new View.OnClickListener() {
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
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.loginSuccessful(passwordInput.getText().toString());
                }
            }
        });
        qrbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cpr = usernameInput.getText().toString();
                String pass = passwordInput.getText().toString();
                if (mListener != null && !cpr.equals("") && !pass.equals("")){
                    User user = new User("PL",cpr, pass);
                    mListener.register(user);
                }
            }
        });
        return rootView;
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
        void loginSuccessful(String role);

        void startQR();
        void notSuccessful();
        String encrypt(String passwordString);
        String decrypt(String passwordString);

        void register(User user);
    }
}
