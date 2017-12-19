package com.example.sebastian.vandrejournalen.Results;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Patient;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class SectionSelectionFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "SECTIONSELECTION";
    User user;
    TextView tvHeadline, tvRole, tvName, tvAddress, tvPhone,tvEmail, tvMidwifeName, tvSpecialistName, tvCloseBtn;
    LinearLayout tvBasic,tvCons,tvTests,tvUltra,tvDiab,tvPoB, secContent;
    private OnFragmentInteractionListener mListener;
    Context context;
    Patient patient;
    Patient prof;
    ServerClient client;
    Button newSpecialist,newMidwife;
    String profCpr;

    public SectionSelectionFragment() {
        // Required empty public constructor
    }

    public static SectionSelectionFragment newInstance() {
        SectionSelectionFragment fragment = new SectionSelectionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //Get objects from Bundle
            if(getArguments().getString("patient",null)!= null) {
                patient = new Gson().fromJson(getArguments().getString("patient", "Patient"), Patient.class);
            }
            user = new Gson().fromJson(getArguments().getString("user","Patient"),User.class);        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        client = ServiceGenerator.createService(ServerClient.class);
        View rootView =inflater.inflate(R.layout.fragment_section_selection, container, false);
        secContent = rootView.findViewById(R.id.sectionContent);
        tvHeadline = rootView.findViewById(R.id.tvHeadline);
        tvRole= rootView.findViewById(R.id.tvRole);
        tvName= rootView.findViewById(R.id.tvName);
        tvAddress= rootView.findViewById(R.id.tvAddress);
        tvPhone= rootView.findViewById(R.id.tvPhone);
        tvEmail= rootView.findViewById(R.id.tvEmail);
        tvBasic= rootView.findViewById(R.id.basicInfoLink);
        tvCons= rootView.findViewById(R.id.consultationLink);
        tvTests= rootView.findViewById(R.id.testLink);
        tvUltra= rootView.findViewById(R.id.ultraSoundLink);
        tvDiab= rootView.findViewById(R.id.diabeetesLink);
        tvPoB= rootView.findViewById(R.id.birthPlaceLink);
        tvCloseBtn = rootView.findViewById(R.id.closeBtn);
        //Create buttons and tv's
        newMidwife = new Button(context);
        newSpecialist = new Button(context);
        newMidwife.setText(R.string.attMid);
        newSpecialist.setText(R.string.attSpec);
        tvMidwifeName = new TextView(context);
        tvSpecialistName = new TextView(context);

        //Set click listener
        tvBasic.setOnClickListener(this);
        tvCons.setOnClickListener(this);
        tvTests.setOnClickListener(this);
        tvUltra.setOnClickListener(this);
        tvDiab.setOnClickListener(this);
        tvPoB.setOnClickListener(this);

        //Create theme color
        TypedValue typedValue = new TypedValue();
        if (this.getActivity().getTheme().resolveAttribute(android.R.attr.colorPrimary, typedValue, true))
        {
            //Set color of buttons
            int colorWindowBackground = typedValue.data;//
            newMidwife.setBackgroundColor(colorWindowBackground);
            newMidwife.setTextColor(Color.parseColor("#FFFFFF"));
            newSpecialist.setBackgroundColor(colorWindowBackground);
            newSpecialist.setTextColor(Color.parseColor("#FFFFFF"));
        }

        if(user.getRole().equals("Patient")) {
            getProfInfo();
            //Show attached profs
            if(user.getMidwifeName() != null) {
                newMidwife.setText(getString(R.string.mw) + ": " + user.getMidwifeName());
            }
            if(user.getSpecialistName() != null) {
                newSpecialist.setText("Specialist: " + user.getSpecialistName());
            }
            //show buttons
            secContent.addView(newMidwife);
            secContent.addView(newSpecialist);


        } else if(user.getRole().equals("General Practitioner") ||user.getRole().equals("Praktiserende læge")){
            getPatientInfo();

            tvCloseBtn.setVisibility(View.VISIBLE);
            tvCloseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Create dialog
                    new MaterialDialog.Builder(context)
                            .title(R.string.closeJourn)
                            .positiveText(getString(R.string.yes))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                   closeJournal();
                                }
                            })
                            .negativeText(getString(R.string.no))
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                }
                            })
                            .negativeText(getString(R.string.canc))
                            .show();
                }
            });

            secContent.addView(newMidwife);
            newMidwife.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Create dialog
                    new MaterialDialog.Builder(context)
                            .title(R.string.atprof)
                            .positiveText(getString(R.string.cont))
                            .inputType(InputType.TYPE_CLASS_NUMBER)
                            .input("CPR", null, false, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                    profCpr = input.toString();

                                }
                            })
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    profCpr = dialog.getInputEditText().getText().toString();
                                    attachProf("Midwife");
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                }
                            })
                            .negativeText(getString(R.string.canc))
                            .show();
                }
            });

            secContent.addView(newSpecialist);
            newSpecialist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Create dialog
                    new MaterialDialog.Builder(context)
                            .title(R.string.atprof)
                            .positiveText(getString(R.string.cont))
                            .inputType(InputType.TYPE_CLASS_NUMBER)
                            .input("CPR", null, false, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                    profCpr = input.toString();
                                }
                            })
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    profCpr = dialog.getInputEditText().getText().toString();
                                    attachProf("Specialist");
                                }
                            })
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                }
                            })
                            .negativeText(getString(R.string.canc))
                            .show();
                }
            });
        } else{
            getPatientInfo();
        }

        return rootView;
    }

    private void closeJournal() {
        //To close a journal, set profid of patient
        patient.setProfUserID(user.getUserID());
        //Network call with Patient object
        Call<String> call = client.attachProf("closeJournal.php",patient);
        //Listen for response
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Check response content
                if(response.body() != null) {
                    //Check response value
                    if(response.body().trim().equals("TRUE")){
                        Toast.makeText(context, R.string.journalclosed, Toast.LENGTH_SHORT).show();
                        //Callback to replace fragment with previous
                        mListener.popStack();
                    } else{
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attachProf(final String role) {
        //Check format of CPR-number
        String cpr = profCpr;
        if (cpr.contains("-")){
            patient.setProfCPR(cpr);
        } else{
            StringBuilder str = new StringBuilder(cpr);
            str.insert(6,"-");
            patient.setProfCPR(str.toString());
        }
        //Add the role of the prof to be attached, to Patient object
        patient.setProfRole(role);
        //Send Patient object
        Call<String> call = client.attachProf("addProfToJournal.php",patient);
        //Listen for response
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Check response content
                if(response.body() != null) {
                    //Set text, depending on the role
                    if(role.equals("Midwife")){
                        newMidwife.setText(getString(R.string.mw)+ ": "+response.body().trim());
                    } else{
                        newSpecialist.setText("Specialist: "+response.body().trim());
                    }
                }  else {
                    //if there was no respone, set text to default
                    newMidwife.setText(R.string.attMid);
                    newSpecialist.setText(R.string.attSpec);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPatientInfo() {
        //Use the existing Patient object to populate textviews
        tvRole.setText("Patient");
        tvName.append(patient.getName());
        tvAddress.append(patient.getAddress());
        tvEmail.append(patient.getEmail());
        tvPhone.append("" + patient.getPhonework() + "\n" + patient.getPhoneprivate());

        if (patient.getMidwifeName() != null) {
            newMidwife.setText(getString(R.string.mw) + ": " + patient.getMidwifeName());

        }
        if (patient.getSpecialistName() != null) {
            newSpecialist.setText("Specialist: " + patient.getSpecialistName());
        }
    }

    private void getProfInfo() {
        //Get information about the GP
        //Send user id
        Call<Patient> call = client.getProfInfo("findGpInfo.php",user.getUserID() );
        //Listen for response
        call.enqueue(new Callback<Patient>() {
            @Override
            public void onResponse(Call<Patient> call, Response<Patient> response) {
                //Check response content
                if(response.body() != null) {
                    //set object to response body
                    prof = response.body();
                    try {
                        //Populate textviews with object
                        tvRole.setText(getResources().getString(R.string.gp));
                        tvName.append(prof.getName());
                        tvAddress.append(prof.getAddress());
                        tvEmail.append(prof.getEmail());
                        tvPhone.append("" + prof.getPhonework());
                        user.setJournalID(prof.getPatientJournalID());
                    }catch (NullPointerException e){
                    }
                }
            }

            @Override
            public void onFailure(Call<Patient> call, Throwable t) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        //Create fragment
        Fragment fragment = new Fragment();
        //Create Bundle
        Bundle args = new Bundle();
        //Convert objects to jsonstrings
        String obj = new Gson().toJson(user);
        args.putString("user" , obj);
        String obj1 = new Gson().toJson(patient);
        args.putString("patient" , obj1);
        //Replace fragment based on button click
        switch (view.getId()){
            case R.id.basicInfoLink:
                fragment = BasicHealthInfoFragment.newInstance();
                fragment.setArguments(args);
                break;
            case R.id.consultationLink:
                fragment = ResultsPager.newInstance(user);
                fragment.setArguments(args);
                break;
            case R.id.testLink:
                break;
            case R.id.ultraSoundLink:
                break;
            case R.id.diabeetesLink:
                break;
            case R.id.birthPlaceLink:
                break;
        }
        mListener.updateFragment(fragment);
    }


    public interface OnFragmentInteractionListener {
        //Interface methods
        void updateFragment(Fragment fragment);
        void popStack();
    }
}
