package com.example.sebastian.vandrejournalen.authentication;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class letIDFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ServerClient client;
    User user;
    TextView tvLetTag,tvLetID, tvName;
    Button continueBtn;
    MaterialEditText etLetInput;
    LetID letID;



    private OnFragmentInteractionListener mListener;

    public letIDFragment() {
        // Required empty public constructor
    }

    public static letIDFragment newInstance(User user) {
        letIDFragment fragment = new letIDFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_let_id, container, false);
        // Inflate the layout for this fragment
        client = ServiceGenerator.createService(ServerClient.class);
        tvLetTag = rootView.findViewById(R.id.letTag);
        tvLetID = rootView.findViewById(R.id.letID);
        tvName = rootView.findViewById(R.id.tvUserName);
        etLetInput = rootView.findViewById(R.id.letInput);
        etLetInput.setTransformationMethod(null);
        etLetInput.setEnabled(false);
        etLetInput.setHelperTextColor(Color.parseColor("#D50000"));


        if(user.getName() != null){
            tvName.setText(user.getName());
        }
        continueBtn = rootView.findViewById(R.id.continueBtn);
        etLetInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (i == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    checkLet();
                    return true;
                }
                return false;
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                continueBtn.setEnabled(false);
                checkLet();
                continueBtn.setEnabled(true);

            }
        });
        getLetIDKeyTag();


        return rootView;
    }

    private void getLetIDKeyTag() {
        final int[] retry = {0};

        Call<LetID> call = client.getLetTag("logInLetIdKeytag.php", user.getUserID());
        call.enqueue(new Callback<LetID>() {
            @Override
            public void onResponse(Call<LetID> call, Response<LetID> response) {

                Log.d(TAG, "onResponse: "+response.body());
                if (response.body() !=null) {
                    if(response.body().getKeyTag() != 0){
                        letID = response.body();

                        Log.d(TAG, "onResponse: "+letID.getKeyID());
                        tvLetID.setText("Card ID: "+letID.getKeyID());
                        tvLetTag.setText(""+letID.getKeyTag());
                        letID.setUserID(user.getUserID());

                        etLetInput.setEnabled(true);

                    } else {
                        Log.d(TAG, "onResponse: "+"Den er null");
                    }

                }else {
                    Log.d(TAG, "onResponse: "+"Den er null");
                }
                //                    mListener.loginExists(user);

            }

            @Override
            public void onFailure(Call<LetID> call, Throwable t) {
                if(retry[0] < 3){
                    getLetIDKeyTag();
                    retry[0]++;
                } else{
                    Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void checkLet() {
        String rawInput = etLetInput.getText().toString();



            //TODO VALIDATION OF INPUT

                if((validate(rawInput))) {
                    letID.setInKeyCode(Integer.parseInt(rawInput));
                    Call<User> call = client.checkLet("logInCheckLetIdKeyCode.php", letID);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {

                            Log.d(TAG, "onResponse: " + response.body());
                            if (response.body() != null) {
                                if (response.body().getRole() != null) {
                                    user.setRole(response.body().getRole());
                                    user.setName(response.body().getName());
                                    mListener.onSuccessfulLogin(user);

                                } else {
                                    etLetInput.setHelperText("Incorrect LET-ID key code. Key has changed");
                                    etLetInput.setText("");
                                    getLetIDKeyTag();
                                }

                            } else {
                                Toast.makeText(getActivity(), "Wrong CPR or password", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

    }

    private boolean validate(String input) {
        int validated = 0;
        if (!input.equals("")) {
            try {
                validated = Integer.parseInt(input);

            } catch (NumberFormatException e) {
                etLetInput.setHelperText("Numbers only, pls");
            }
        } else{
            etLetInput.setHelperText("Field is empty");

            return false;
        }
        return true;
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
        void onSuccessfulLogin(User user);
    }
}
