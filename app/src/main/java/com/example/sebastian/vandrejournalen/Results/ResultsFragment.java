package com.example.sebastian.vandrejournalen.Results;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Patient;
import com.example.sebastian.vandrejournalen.RoleHelper;
import com.example.sebastian.vandrejournalen.SecureUtil;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class ResultsFragment extends Fragment  {
    private View rootView;
    Context context;
    LinearLayout cLinearLayout,hLinearLayout;
    User user;
    FloatingActionButton fab, fabCheck;
    Consultation consultation;
    MaterialEditText etGestationsalder, etVaegt, etBlodtryk, etUrinASLeuNit, etOedem, etSymfyseFundus, etFosterpraes, etFosterskoen, etFosteraktivitet, etUndersoegelsessted, etInitialer,etType;
    OnFragmentInteractionListener mListener;
    Patient patient;
    ServerClient client;
    private boolean edited;

    public ResultsFragment() {
        // Required empty public constructor
    }

    public static ResultsFragment newInstance() {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Get objects from Bundle
            user = new Gson().fromJson(getArguments().getString("user","Midwife"),User.class);
            consultation = new Gson().fromJson(getArguments().getString("obj"), Consultation.class);
            patient = new Gson().fromJson(getArguments().getString("patient", "Patient"), Patient.class);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_results, container, false);
        setHasOptionsMenu(true);

        cLinearLayout = rootView.findViewById(R.id.resultsLayout);
        hLinearLayout = rootView.findViewById(R.id.hLayout);
        //Create Http Client
        client = ServiceGenerator.createService(ServerClient.class);

        //Create new thread to init layout
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(isAdded())
                consultationLayout();
                setEditable();
            }
        });
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public interface OnFragmentInteractionListener {
        //Interface methods
        void showDatePicker();
        void updateEdited(boolean edited);
    }

    public void consultationLayout() {
        //Create input fields
        etGestationsalder = new MaterialEditText(context);
        etVaegt = new MaterialEditText(context);
        etBlodtryk = new MaterialEditText(context);
        etUrinASLeuNit= new MaterialEditText(context);
        etOedem= new MaterialEditText(context);
        etSymfyseFundus= new MaterialEditText(context);
        etFosterpraes = new MaterialEditText(context);
        etFosterskoen = new MaterialEditText(context);
        etFosteraktivitet = new MaterialEditText(context);
        etUndersoegelsessted = new MaterialEditText(context);
        etInitialer = new MaterialEditText(context);
        etType = new MaterialEditText(context);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = 16;

        //Add date to top of layout
        TextView tvDate = new TextView(context);
        tvDate.setLayoutParams(params);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        tvDate.setText(dateFormat.format(consultation.getDate()));
        hLinearLayout.addView(tvDate,0);

        //Check if the user is a GP or MW
        if (user.getRole().equals("General Practitioner")||user.getRole().equals("Midwife")) {
            //init and show fab button
            fab = rootView.findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
            //init fabcheck button
            fabCheck = rootView.findViewById(R.id.fabCheck);
            fabCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendConsultation();
                    fabCheck.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.VISIBLE);
                }
            });
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.showDatePicker();
                }
            });

            //Check if consultationID is null or "". If so, this is a new consultation
            if (consultation.getConsultationID() == null || consultation.getConsultationID().equals("")) {
                //Set attributes
                consultation.setInitialer(user.getName());
                consultation.setConsultationID("");
                //Change edited value
                edited = true;
                //Callback to update edited in MainActivity
                mListener.updateEdited(edited);

                fab.setVisibility(View.INVISIBLE);
                fabCheck.setVisibility(View.VISIBLE);

                //Set text of responsible
                etType.setText(user.getName());
            }
        }
        //Check if any initials exists
        if (consultation.getInitialer() != null) {
            etType.setText(consultation.getInitialer());
        }
        etType.setFloatingLabelAlwaysShown(true);
        etType.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etType.setFloatingLabelText(getString(R.string.responsible));
        etType.setFocusable(false);
        cLinearLayout.addView(etType);


        //Create a new textwatcher instance
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Check if edited is false
                if(!edited) {
                    //Info has been edited
                    fab.setVisibility(View.INVISIBLE);
                    fabCheck.setVisibility(View.VISIBLE);
                    edited = true;
                    mListener.updateEdited(edited);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        //Init input fields - set textlistener to register if anyting is updated

        // Gestationsalder
        etGestationsalder.setText(String.valueOf(consultation.gestationsalder));
        etGestationsalder.setFloatingLabelAlwaysShown(true);
        etGestationsalder.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etGestationsalder.setFloatingLabelText("Gestationsalder");
        etGestationsalder.addTextChangedListener(textWatcher);
        cLinearLayout.addView(etGestationsalder);

        // Vaegt
        etVaegt.setText("" + consultation.vaegt);
        etVaegt.setFloatingLabelAlwaysShown(true);
        etVaegt.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etVaegt.setFloatingLabelText("Vægt");
        etVaegt.addTextChangedListener(textWatcher);
        cLinearLayout.addView(etVaegt);

        // Blodtryk
        etBlodtryk.setText(consultation.getBlodtryk());
        etBlodtryk.setFloatingLabelAlwaysShown(true);
        etBlodtryk.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etBlodtryk.setFloatingLabelText("Blodtryk");
        etBlodtryk.addTextChangedListener(textWatcher);
        cLinearLayout.addView(etBlodtryk);

        // UrinASLeuNit
        etUrinASLeuNit.setText(consultation.getUrinASLeuNit());
        etUrinASLeuNit.setFloatingLabelAlwaysShown(true);
        etUrinASLeuNit.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etUrinASLeuNit.setFloatingLabelText("Urin: A, S, Leu, Nit");
        etUrinASLeuNit.addTextChangedListener(textWatcher);
        cLinearLayout.addView(etUrinASLeuNit);

        // Oedem
        etOedem.setText(consultation.oedem);
        etOedem.setFloatingLabelAlwaysShown(true);
        etOedem.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etOedem.setFloatingLabelText("Oedem");
        etOedem.addTextChangedListener(textWatcher);
        cLinearLayout.addView(etOedem);

        // SymfyseFundus
        etSymfyseFundus.setText("" + consultation.symfyseFundus);
        etSymfyseFundus.setFloatingLabelAlwaysShown(true);
        etSymfyseFundus.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etSymfyseFundus.setFloatingLabelText("SymfyseFundus");
        etSymfyseFundus.addTextChangedListener(textWatcher);
        cLinearLayout.addView(etSymfyseFundus);

        // Fosterpraes
        etFosterpraes.setText("" + consultation.getFosterpraes());
        etFosterpraes.setFloatingLabelAlwaysShown(true);
        etFosterpraes.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etFosterpraes.setFloatingLabelText("Fosterpræs");
        etFosterpraes.addTextChangedListener(textWatcher);
        cLinearLayout.addView(etFosterpraes);

        // Fosterskoen
        etFosterskoen.setText(consultation.fosterskoen);
        etFosterskoen.setFloatingLabelAlwaysShown(true);
        etFosterskoen.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etFosterskoen.setFloatingLabelText("Fosterskøn");
        etFosterskoen.addTextChangedListener(textWatcher);
        cLinearLayout.addView(etFosterskoen);

        // Fosteraktivitet
        etFosteraktivitet.setText(consultation.fosteraktivitet);
        etFosteraktivitet.setFloatingLabelAlwaysShown(true);
        etFosteraktivitet.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etFosteraktivitet.setFloatingLabelText("Fosteraktivitet");
        cLinearLayout.addView(etFosteraktivitet);

        // Undersoegelsessted
        etUndersoegelsessted.setText(consultation.undersoegelsessted);
        etUndersoegelsessted.setFloatingLabelAlwaysShown(true);
        etUndersoegelsessted.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etUndersoegelsessted.setFloatingLabelText("Undersøgelsessted");
        etFosteraktivitet.addTextChangedListener(textWatcher);
        cLinearLayout.addView(etUndersoegelsessted);

        final RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));

    }

    private void sendConsultation() {
        //Update edited value
        edited = false;
        mListener.updateEdited(edited);
        //Set attributes of consultation
        consultation.setJournalID(patient.getJournalID());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            consultation.setDateString(dateFormat.format(consultation.getDate()));
            consultation.setBlodtryk(etBlodtryk.getText().toString());
            consultation.setGestationsalder(Integer.parseInt(etGestationsalder.getText().toString()));
            consultation.setVaegt(Float.parseFloat(etVaegt.getText().toString()));
            consultation.setUrinASLeuNit(etUrinASLeuNit.getText().toString());
            consultation.setOedem(etOedem.getText().toString());
            consultation.setSymfyseFundus(Float.parseFloat(etSymfyseFundus.getText().toString()));
            consultation.setFosterpraes(etFosterpraes.getText().toString());
            consultation.setFosterskoen(etFosterskoen.getText().toString());
            consultation.setFosteraktivitet(etFosteraktivitet.getText().toString());
            consultation.setUndersoegelsessted(etUndersoegelsessted.getText().toString());
            consultation.setInitialer(user.getUserID());

            //Send consultation object
            Call<String> call = client.postConsultation("addJournalConsultation.php", consultation);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    //Check response content
                    if(response.body() != null) {
                        if (!response.body().trim().equals("FALSE")) {
                            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                }
            });
        }catch (NumberFormatException e1){
            Toast.makeText(context, "Wrong input", Toast.LENGTH_SHORT).show();
        }
    }

    public void setEditable() {
        //Disallow editing for patients
        String role = user.getRole();
        switch(role) {
            case "Midwife":
                return;

            case "General Practitioner":
                return;
            case "Jordemoder":
                return;

            case "Praktiserende læge":
                return;

            default:
                etGestationsalder.setFocusable(false);
                etVaegt.setFocusable(false);
                etBlodtryk.setFocusable(false);
                etUrinASLeuNit.setFocusable(false);
                etOedem.setFocusable(false);
                etSymfyseFundus.setFocusable(false);
                etFosterpraes.setFocusable(false);
                etFosterskoen.setFocusable(false);
                etFosteraktivitet.setFocusable(false);
                etUndersoegelsessted.setFocusable(false);
                etInitialer.setFocusable(false);

        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ResultsFragment.OnFragmentInteractionListener) {
            mListener = (ResultsFragment.OnFragmentInteractionListener) context;
        } else {
        }
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
