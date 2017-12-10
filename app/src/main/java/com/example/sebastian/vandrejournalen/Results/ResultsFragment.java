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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
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
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.calendar.Consultation;
import com.example.sebastian.vandrejournalen.calendar.RecyclerAdapter;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class ResultsFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private TextView tvShowNotes;
    private View rootView;
    boolean notesShowing = false;
    Context context;
    MaterialDialog dialog;
    LinearLayout cLinearLayout,hLinearLayout,notesLayout;
    User user;
    FloatingActionButton fab, fabCheck;
    MaterialSpinner typeSpinner;
    Consultation consultation;
    MaterialEditText etGestationsalder, etVaegt, etBlodtryk, etUrinASLeuNit, etOedem, etSymfyseFundus, etFosterpraes, etFosterskoen, etFosteraktivitet, etUndersoegelsessted, etInitialer,etType;
    boolean persSelected = false;
    ArrayList<String> ITEMS = new ArrayList<String>();
    Button saveExit;
    OnFragmentInteractionListener mListener;
    Patient patient;
    ServerClient client;
    private boolean edited;

    public ResultsFragment() {
        // Required empty public constructor
    }

    public static ResultsFragment newInstance(User user, Consultation consultation) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        Log.d(TAG, "newInstance: "+user.getRole());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = new Gson().fromJson(getArguments().getString("user","Midwife"),User.class);
            Log.d(TAG, "onCreate: "+user.getRole());
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
        ITEMS.add(getString(R.string.gp)+ " - id 12323437198");
        ITEMS.add(getString(R.string.mw));
        ITEMS.add(getString(R.string.sp));
        //etName = rootView.findViewById(R.id.name);
        cLinearLayout = rootView.findViewById(R.id.resultsLayout);
        hLinearLayout = rootView.findViewById(R.id.hLayout);
        /*tvShowNotes = rootView.findViewById(R.id.tvShowNotes);
        notesLayout = rootView.findViewById(R.id.notesLayout);*/

        client = ServiceGenerator.createService(ServerClient.class);

        //txt.setText(consultation.getDay()+"/"+consultation.getMonth()+"/"+consultation.getYear());

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(isAdded())
                consultationLayout();
                setEditable();


            }
        });



        //etName.setText(consultation.getEvent());
        return rootView;

    }

    private void updateAppointment() {

    // Make

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

        updateAppointment();
    }

    public interface OnFragmentInteractionListener {
        void showDatePicker();
        void updateEdited(boolean edited);
    }

    public void consultationLayout() {
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

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = 16;

        // Fullname
        TextView tvDate = new TextView(context);
        tvDate.setLayoutParams(params);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        tvDate.setText(dateFormat.format(consultation.getDate()));

        hLinearLayout.addView(tvDate,0);

       etType = new MaterialEditText(context);

        if (!user.getRole().equals("Patient")) {
            fab = rootView.findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
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

            if (consultation.getConsultationID() == null || consultation.getConsultationID().equals("")) {
                consultation.setInitialer(user.getUserID());
                consultation.setConsultationID("");
                edited = true;
                mListener.updateEdited(edited);
                fab.setVisibility(View.INVISIBLE);
                fabCheck.setVisibility(View.VISIBLE);
                //mListener.hideFab();
                etType.setText(user.getName());
            } else if (consultation.getInitialer() != null) {
                Log.d(TAG, "consultationLayout: " + consultation.getConsultationID());
                etType.setText(consultation.getInitialer());
                //mListener.showFab();
            }
        }

            etType.setFloatingLabelAlwaysShown(true);
            etType.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
            etType.setFloatingLabelText(getString(R.string.responsible));
            etType.setFocusable(false);
            cLinearLayout.addView(etType);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!edited) {
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



        //Get notes for this consultation
        RecyclerAdapter adapter = new RecyclerAdapter(RoleHelper.getAllAppointments(user), context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        /*final TextView notesTitle = new TextView(context);
        notesTitle.setText("NOTER");
        notesTitle.setTextSize(20);
        notesTitle.setLayoutParams(params);

        tvShowNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notesShowing) {
                    cLinearLayout.removeView(notesTitle);
                    cLinearLayout.removeView(recyclerView);
                    tvShowNotes.setText("Vis noter");

                    notesShowing = false;
                } else {
                    cLinearLayout.addView(notesTitle);
                    cLinearLayout.addView(recyclerView);
                    recyclerView.requestFocus();
                    tvShowNotes.setText("Skjul noter");
                    notesShowing = true;
                }
            }
        });*/

    }

    private void sendConsultation() {
        edited = false;
        mListener.updateEdited(edited);
        consultation.setJournalID(patient.getJournalID());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Log.d(TAG, "sendConsultation: "+consultation.getConsultationID());
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


            Call<String> call = client.postConsultation("addJournalConsultation.php", consultation);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.d(TAG, "onResponse: " + response.body().trim());
                    if (!response.body().trim().equals("FALSE")) {

                    }
                    Log.d(TAG, "onResponse: " + response.body().trim());
                    Log.d(TAG, "onResponse: "+consultation.getGestationsalder());
                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
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

    private void initSpinner() {
        typeSpinner = new MaterialSpinner(context);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setTextSize(16);
        typeSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if(item.equals(user.getRole()) || item.equals(RoleHelper.translateRole(user.getRole()))||position == 0 || item.length() > 20){
                    Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                } else {
                    if(dialog == null) {
                        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                                .title(getResources().getString(R.string.choose) +" "+item)
                                .items("Person - id 292837567198","Anden person - id 12323437198","Tredje person - id 077287234818")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .dismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        if(persSelected){
                                            persSelected = false;
                                        }else {

                                            typeSpinner.setSelectedIndex(0);
                                        }
                                        dialog = null;
                                    }
                                })
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog mdialog, View view, int which, CharSequence text) {
                                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                                        persSelected = true;
                                        if(!ITEMS.contains(text.toString())){
                                            ITEMS.add(0,text.toString());
                                            adapter.notifyDataSetChanged();
                                            typeSpinner.setDropdownHeight(600);

                                            typeSpinner.setSelectedIndex(0);
                                        } else{
                                            typeSpinner.setSelectedIndex(ITEMS.indexOf(text.toString()));
                                        }

                                        dialog = null;

                                    }
                                });
                        dialog = builder.build();
                        dialog.show();
                    } else if(dialog.isShowing()){
                        dialog = null;
                    }
                }

            }
        });
    }

    public void setEditable() {
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
