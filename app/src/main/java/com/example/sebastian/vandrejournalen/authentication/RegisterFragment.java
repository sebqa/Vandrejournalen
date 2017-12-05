package com.example.sebastian.vandrejournalen.authentication;

import android.content.Context;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ServerClient client;
    MaterialEditText etInput;
    Button continueBtn;
    TextView tvSignedUp;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        etInput = rootView.findViewById(R.id.keyInput);
        tvSignedUp = rootView.findViewById(R.id.tvSignedUp);

        tvSignedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToLogin();
            }
        });

        etInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    onContinue();
                    return true;
                }
                return false;
            }
        });
        continueBtn = rootView.findViewById(R.id.continueBtn);

        client = ServiceGenerator.createService(ServerClient.class);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onContinue();
            }
        });
        // Inflate the layout for this fragment
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void goToLogin();
    }


    public void onContinue(){
        etInput.setEnabled(false);
        final String input = etInput.getText().toString();
        if (input.length() == 36) {
            Call<User> call = client.checkKey("checkKey.php", etInput.getText().toString());
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    User user = response.body();
                    Log.d(TAG, "onClick: " + user.getInstitution());

                                new MaterialDialog.Builder(getActivity())
                                        .title("Verification")
                                        .content("Please verify that you are a " + user.getRole() + " at " + user.getInstitution())
                                        .positiveText("Yes, continue")
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                            }
                                        })
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                etInput.setEnabled(true);
                                            }
                                        })
                                        .negativeText("No, exit")
                                        .show();
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                }


                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                    etInput.setEnabled(true);

                }
            });
        } else if(input.length() == 11) {
            Call<String> call = client.checkCPR("checkCPR.php", etInput.getText().toString());

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(TAG, "onResponse: "+response.body().trim());
                    if(response.body().trim().equals("1")){
                        Toast.makeText(getActivity(), "Match!", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                    etInput.setEnabled(true);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Wrong key length!", Toast.LENGTH_SHORT).show();
            etInput.setEnabled(true);
        }



    }
}
