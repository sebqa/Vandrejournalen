package com.example.sebastian.vandrejournalen.Results;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.RoleHelper;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.calendar.Appointment;
import com.example.sebastian.vandrejournalen.calendar.RecyclerAdapter;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ResultsFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String apDate, apText,role;
    private TextView tvShowNotes;
    private View rootView;
    boolean notesShowing = false;
    Context context;
    LinearLayout cLinearLayout,hLinearLayout,notesLayout;


    Appointment appointment;
    MaterialEditText etGestationsalder, etVaegt, etBlodtryk, etUrinASLeuNit, etOedem, etSymfyseFundus, etFosterpraes, etFosterskoen, etFosteraktivitet, etUndersoegelsessted, etInitialer;


    public ResultsFragment() {
        // Required empty public constructor
    }

    public static ResultsFragment newInstance(String role, Appointment appointment) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putString("role",role);
        Gson gson = new Gson();
        String obj = gson.toJson(appointment);
        args.putString("obj" , obj);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            apDate = getArguments().getString("date");
            apText = getArguments().getString("text");
            role = getArguments().getString("role");
            Gson gson = new Gson();
            appointment = gson.fromJson(getArguments().getString("obj"), Appointment.class);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_results, container, false);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        role = args.getString("role");
        //etName = rootView.findViewById(R.id.name);
        cLinearLayout = rootView.findViewById(R.id.resultsLayout);
        hLinearLayout = rootView.findViewById(R.id.hLayout);
        tvShowNotes = rootView.findViewById(R.id.tvShowNotes);
        notesLayout = rootView.findViewById(R.id.notesLayout);

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

        //txt.setText(appointment.getDay()+"/"+appointment.getMonth()+"/"+appointment.getYear());
        setEditable();
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(isAdded())
                initLayout();

            }
        });



        //etName.setText(appointment.getEvent());
        return rootView;

    }

    private void updateAppointment() {

    // Make

    }

    @Override
    public void onResume() {
        super.onResume();
        if(etGestationsalder.getText().toString().equals("")) {
            etGestationsalder.requestFocus();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        updateAppointment();
    }

    public interface OnFragmentInteractionListener {
        void makeScrollable(View view);
    }

    public void initLayout() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = 16;
        // Fullname
        TextView tvDate = new TextView(context);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        tvDate.setLayoutParams(params);
        tvDate.setText(dateFormat.format(appointment.getDate()));

        hLinearLayout.addView(tvDate,0);

        // Gestationsalder
        etGestationsalder.setText("" + appointment.getGestationsalder());
        etGestationsalder.setFloatingLabelAlwaysShown(true);
        etGestationsalder.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etGestationsalder.setFloatingLabelText("Gestationsalder");
        cLinearLayout.addView(etGestationsalder);

        // Vaegt
        etVaegt.setText("" + appointment.vaegt);
        etVaegt.setFloatingLabelAlwaysShown(true);
        etVaegt.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etVaegt.setFloatingLabelText("Vægt");
        cLinearLayout.addView(etVaegt);

        // Blodtryk
        etBlodtryk.setText(appointment.getBlodtryk());
        etBlodtryk.setFloatingLabelAlwaysShown(true);
        etBlodtryk.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etBlodtryk.setFloatingLabelText("Blodtryk");
        cLinearLayout.addView(etBlodtryk);

        // UrinASLeuNit
        etUrinASLeuNit.setText(appointment.getUrinASLeuNit());
        etUrinASLeuNit.setFloatingLabelAlwaysShown(true);
        etUrinASLeuNit.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etUrinASLeuNit.setFloatingLabelText("Urin: A, S, Leu, Nit");
        cLinearLayout.addView(etUrinASLeuNit);

        // Oedem
        etOedem.setText(appointment.oedem);
        etOedem.setFloatingLabelAlwaysShown(true);
        etOedem.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etOedem.setFloatingLabelText("Oedem");
        cLinearLayout.addView(etOedem);

        // SymfyseFundus
        etSymfyseFundus.setText("" + appointment.symfyseFundus);
        etSymfyseFundus.setFloatingLabelAlwaysShown(true);
        etSymfyseFundus.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etSymfyseFundus.setFloatingLabelText("SymfyseFundus");
        cLinearLayout.addView(etSymfyseFundus);

        // Fosterpraes
        etFosterpraes.setText("" + appointment.getFosterpraes());
        etFosterpraes.setFloatingLabelAlwaysShown(true);
        etFosterpraes.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etFosterpraes.setFloatingLabelText("Fosterpræs");
        cLinearLayout.addView(etFosterpraes);

        // Fosterskoen
        etFosterskoen.setText(appointment.fosterskoen);
        etFosterskoen.setFloatingLabelAlwaysShown(true);
        etFosterskoen.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etFosterskoen.setFloatingLabelText("Fosterskøn");
        cLinearLayout.addView(etFosterskoen);

        // Fosteraktivitet
        etFosteraktivitet.setText(appointment.fosteraktivitet);
        etFosteraktivitet.setFloatingLabelAlwaysShown(true);
        etFosteraktivitet.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etFosteraktivitet.setFloatingLabelText("Fosteraktivitet");
        cLinearLayout.addView(etFosteraktivitet);

        // Undersoegelsessted
        etUndersoegelsessted.setText(appointment.undersoegelsessted);
        etUndersoegelsessted.setFloatingLabelAlwaysShown(true);
        etUndersoegelsessted.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etUndersoegelsessted.setFloatingLabelText("Undersøgelsessted");
        cLinearLayout.addView(etUndersoegelsessted);

        // Initialer
        etInitialer.setText(appointment.initialer);
        etInitialer.setFloatingLabelAlwaysShown(true);
        etInitialer.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etInitialer.setFloatingLabelText("Initialer");
        cLinearLayout.addView(etInitialer);

        final RecyclerView recyclerView = new RecyclerView(getActivity());
        recyclerView.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));

        //Get notes for this appointment
        User user = new User();
        user.setRole("PL");
        RecyclerAdapter adapter = new RecyclerAdapter(RoleHelper.getAllAppointments(user), context);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        final TextView notesTitle = new TextView(context);
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
        });

    }
    public void setEditable() {
        switch(role) {
            case "MW":

                return;

            case "DR":
                etGestationsalder.requestFocus();
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
        this.context = context;
    }
}
