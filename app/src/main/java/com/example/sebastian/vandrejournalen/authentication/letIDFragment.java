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
    ServerClient client;
    User user;
    TextView tvLetTag,tvLetID, tvName, tvNotYou;
    Button continueBtn;
    MaterialEditText etLetInput;
    LetID letID;
    private OnFragmentInteractionListener mListener;

    public letIDFragment() {
        // Required empty public constructor
    }

    public static letIDFragment newInstance() {
        letIDFragment fragment = new letIDFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Gson gson = new Gson();
            //Get the User object from the Bundle
            user = gson.fromJson(getArguments().getString("user"), User.class);
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
        tvNotYou = rootView.findViewById(R.id.tvNotYou);
        etLetInput = rootView.findViewById(R.id.letInput);

        //Prevent characters from obscuring
        etLetInput.setTransformationMethod(null);
        etLetInput.setEnabled(false);
        //Set the colors of errors
        etLetInput.setHelperTextColor(Color.parseColor("#D50000"));
        etLetInput.requestFocus();

        tvNotYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.restartActivity();
            }
        });

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
        //Retry counter
        final int[] retry = {0};
        //Use userID to request a LetIDTag
        Call<LetID> call = client.getLetTag("logInLetIdKeytag.php", user.getUserID());
        call.enqueue(new Callback<LetID>() {
            @Override
            public void onResponse(Call<LetID> call, Response<LetID> response) {
                if (response.body() !=null) {
                    //If keyTag is not 0
                    if(response.body().getKeyTag() != 0){
                        //Create letID object from response
                        letID = response.body();

                        //Add info to the view
                        tvLetID.setText("Card ID: "+letID.getKeyID());
                        tvLetTag.setText(""+letID.getKeyTag());
                        letID.setUserID(user.getUserID());

                        //Check if the user is set
                        if(user.getName() != null){
                            tvName.setText(user.getName());
                        } else{
                            //Else use the name received in the letID object
                            tvName.setText(letID.getName());
                        }
                        etLetInput.setEnabled(true);

                    } else {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
           }

            @Override
            public void onFailure(Call<LetID> call, Throwable t) {
                //If request fails, check the number of retry attempts
                //If less than 3, try again
                if(retry[0] < 3){
                    getLetIDKeyTag();
                    //Increase counter
                    retry[0]++;
                } else{
                    Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkLet() {
        String rawInput = etLetInput.getText().toString();
        //Check validity of input
        if((validate(rawInput))) {
            //Convert input to int
            letID.setInKeyCode(Integer.parseInt(rawInput));
            
            Call<User> call = client.checkLet("logInCheckLetIdKeyCode.php", letID);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.body() != null) {
                        //Check if a role is contained in response
                        if (response.body().getRole() != null) {
                            //Set the response as attributes of the user
                            user.setRole(response.body().getRole());
                            user.setName(response.body().getName());
                            mListener.onSuccessfulLogin(user);
                        } else {
                            //If the input was incorrect, get another tag
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
        } else {
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
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onSuccessfulLogin(User user);
        void restartActivity();
    }
}
