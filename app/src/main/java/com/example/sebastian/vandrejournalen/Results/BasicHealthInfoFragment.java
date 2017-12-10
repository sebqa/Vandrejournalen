package com.example.sebastian.vandrejournalen.Results;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Patient;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BasicHealthInfoFragment extends Fragment {

    private static final String TAG = "BasicHealthInfoFragment";
    private OnFragmentInteractionListener mListener;
    MaterialEditText etMensDag, etCyklus, etGrav, ethojde, etBMI;
    RadioButton calcYes, calcNo,hepYes,hepNo, bloodyes, bloodNo, mrhesYes, mrhesNo, iregYes, iregNo, crhesYes, crhesNo, antiYes, antiNo, antiDYes, antiDNo;
    CheckBox urinDyrk;
    Context context;
    LinearLayout cLayout;
    Patient patient;
    User user;
    Button button;
    ServerClient client;
    int day,month,year;
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String journalID;
    BasicInfo basicInfo;

    public BasicHealthInfoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BasicHealthInfoFragment newInstance() {
        BasicHealthInfoFragment fragment = new BasicHealthInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            patient = new Gson().fromJson(getArguments().getString("patient","Patient"),Patient.class);
            user = new Gson().fromJson(getArguments().getString("user","Patient"),User.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_basic_health_info, container, false);
        cLayout = rootView.findViewById(R.id.cLayout);
        Log.d(TAG, "onCreateView: user og patient data"+user.getName()+patient.getName());

        initLayout(rootView);
        if(user.getRole().equals("Patient")){
            setEditable();
        } else{
            etMensDag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePicker();
                }
            });
        }
        getBasicInfo();
        return rootView;
    }

    private void setEditable() {
        etMensDag.setFocusable(false);
        etCyklus.setFocusable(false);
        etGrav.setFocusable(false);
        ethojde.setFocusable(false);
        etBMI.setFocusable(false);
        calcYes.setClickable(false);
        calcNo.setClickable(false);
        button.setVisibility(View.INVISIBLE);
        etMensDag.setClickable(false);
        hepYes.setClickable(false);
        hepNo .setClickable(false);
        bloodyes .setClickable(false);
        bloodNo .setClickable(false);
        mrhesYes .setClickable(false);
        mrhesNo .setClickable(false);
        iregYes .setClickable(false);
        iregNo .setClickable(false);
        crhesYes .setClickable(false);
        crhesNo .setClickable(false);
        antiYes .setClickable(false);
        antiNo .setClickable(false);
        antiDYes .setClickable(false);
        antiDNo .setClickable(false);
        urinDyrk.setClickable(false);
    }

    private void getBasicInfo() {

        if(user.getRole().equals("Patient")){
            journalID = user.getJournalID();
        } else{
            journalID = patient.getJournalID();
            etMensDag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDatePicker();
                }
            });
        }

        client = ServiceGenerator.createService(ServerClient.class);
        Log.d(TAG, "getBasicInfo: JOURNALID"+ user.getJournalID());
        Call<BasicInfo> call = client.getBasicInfo("returnJournalBasicHealth.php", journalID);
        call.enqueue(new Callback<BasicInfo>() {
            @Override
            public void onResponse(Call<BasicInfo> call, Response<BasicInfo> response) {
                if(response.body() != null){
                    basicInfo = response.body();
                    basicInfo.setJournalID(patient.getJournalID());

                    try {
                        etMensDag.setText(basicInfo.getMensDag());
                        etCyklus.setText(basicInfo.getCyklus());
                        etGrav.setText(""+basicInfo.getGrav());
                        ethojde.setText(""+basicInfo.getHojde());
                        etBMI.setText(""+basicInfo.getBMI());
                        if(basicInfo.isbSikker()){
                            calcYes.toggle();
                        } else{
                            calcNo.toggle();
                        }
                        if(basicInfo.isbSikker()){
                            calcYes.toggle();
                        } else{
                            calcNo.toggle();
                        }
                        if(basicInfo.isHep()){
                            hepYes.toggle();
                        } else{
                            hepNo.toggle();
                        }
                        if(basicInfo.isBlodTaget()){
                            bloodyes.toggle();
                        } else{
                            bloodNo.toggle();
                        }
                        if(basicInfo.isRhesus()){
                            mrhesYes.toggle();
                        } else{
                            mrhesNo.toggle();
                        }
                        if(basicInfo.isIrreg()){
                            iregYes.toggle();
                        } else{
                            iregNo.toggle();
                        }
                        if(basicInfo.isBarnRhes()){
                            crhesYes.toggle();
                        } else{
                            crhesNo.toggle();
                        }
                        if(basicInfo.isAntiStof()){
                            antiYes.toggle();
                        } else{
                            antiNo.toggle();
                        }if(basicInfo.isAntiD()){
                            antiDYes.toggle();
                        } else{
                            antiDNo.toggle();
                        }
                        if(basicInfo.isUrin()){
                            urinDyrk.setChecked(true);
                        } else{
                            urinDyrk.setChecked(false);
                        }


                    } catch (NullPointerException e){
                        Log.d(TAG, "onResponse: Noget er null"+response.body().getMensDag());
                    }

                }

            }

            @Override
            public void onFailure(Call<BasicInfo> call, Throwable t) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initLayout(View rootView) {
        etMensDag = rootView.findViewById(R.id.lastMens);
        etMensDag.setHint(R.string.last_mens);

        etCyklus  = rootView.findViewById(R.id.cycle);
        etCyklus.setText(R.string.cycle);
        calcYes = rootView.findViewById(R.id.calcYes);
        calcNo = rootView.findViewById(R.id.calcNo);
        etGrav = rootView.findViewById(R.id.grav);
        ethojde = rootView.findViewById(R.id.hojde);
        etBMI = rootView.findViewById(R.id.BMI);
        hepYes = rootView.findViewById(R.id.hepYes);
        hepNo = rootView.findViewById(R.id.hepNo);
        bloodyes =rootView.findViewById(R.id.bloodYes);
        bloodNo = rootView.findViewById(R.id.bloodNo);
        mrhesYes = rootView.findViewById(R.id.mrhesYes);
        mrhesNo = rootView.findViewById(R.id.mrhesNo);
        iregYes = rootView.findViewById(R.id.iregYes);
        iregNo = rootView.findViewById(R.id.iregNo);
        crhesYes = rootView.findViewById(R.id.crhesYes);
        crhesNo = rootView.findViewById(R.id.crhesNo);
        antiYes = rootView.findViewById(R.id.antiYes);
        antiNo = rootView.findViewById(R.id.antiNo);
        antiDYes = rootView.findViewById(R.id.antidYes);
        antiDNo = rootView.findViewById(R.id.antidNo);
        urinDyrk = rootView.findViewById(R.id.urinYes);

        button = rootView.findViewById(R.id.savExitBtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendInfo();
                mListener.popStack();
            }
        });






    }

    private void sendInfo() {
        basicInfo.setCyklus(etCyklus.getText().toString());
        if(calcNo.isChecked()){
            basicInfo.setbSikker(false);
        } else{
            basicInfo.setbSikker(true);
        }
        basicInfo.setGrav(Integer.parseInt(etGrav.getText().toString()));
        basicInfo.setHojde(Integer.parseInt(ethojde.getText().toString()));
        basicInfo.setBMI(Float.parseFloat(etBMI.getText().toString()));

        basicInfo.setHep(true);
        basicInfo.setBlodTaget(bloodyes.isChecked());
        basicInfo.setRhesus(mrhesYes.isChecked());
        basicInfo.setIrreg(iregYes.isChecked());
        basicInfo.setBarnRhes(crhesYes.isChecked());
        basicInfo.setAntiStof(antiYes.isChecked());
        basicInfo.setAntiD(antiDYes.isChecked());
        basicInfo.setUrin(urinDyrk.isChecked());

        Call<String> call = client.sendBasic("addJournalBasicHealth.php",basicInfo);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body() != null){
                    Log.d(TAG, "onResponse: "+response.body());
                    try {
                        if(response.body().trim().equals("TRUE")) {
                            Toast.makeText(context, R.string.info_saved, Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(context, "Something happened", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NullPointerException e){
                        Toast.makeText(context, "Nullpointer", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    boolean mFirst = true;

                    @Override
                    public void onDateSet(DatePicker view, int nyear,
                                          int monthOfYear, int dayOfMonth) {

                        if (mFirst) {
                            mFirst = false;
                            year = nyear;
                            month = monthOfYear+1;
                            day = dayOfMonth;
                            Log.d(TAG, "showDatePickerDialog: "+year+month+day);
                            try {
                                Date d = sdf.parse(year+"-"+(month)+"-"+day);
                                basicInfo.setMensDag(sdf.format(d));
                                etMensDag.setText(sdf.format(d));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }



                    }
                }, year, month, day);

        datePickerDialog.show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void popStack();
    }
}
