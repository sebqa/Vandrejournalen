package com.example.sebastian.vandrejournalen.Results;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.RoleHelper;
import com.example.sebastian.vandrejournalen.calendar.Appointment;
import com.example.sebastian.vandrejournalen.calendar.RecyclerAdapter;
import com.example.sebastian.vandrejournalen.calendar.Schedule;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;


public class ResultsFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String apDate, apText,role;
    private TextView previewDate, appointText;
    RecyclerAdapter adapter;
    private EditText etName;
    RecyclerView.LayoutManager layoutManager;
    private View rootView;
    private ResultsFragment.OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    Schedule schedule = new Schedule();
    LinearLayout linearLayout;
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
        linearLayout = rootView.findViewById(R.id.resultsLayout);

        etGestationsalder = new MaterialEditText(getContext());
        etVaegt = new MaterialEditText(getContext());
        etBlodtryk = new MaterialEditText(getContext());
        etUrinASLeuNit= new MaterialEditText(getContext());
        etOedem= new MaterialEditText(getContext());
        etSymfyseFundus= new MaterialEditText(getContext());
        etFosterpraes = new MaterialEditText(getContext());
        etFosterskoen = new MaterialEditText(getContext());
        etFosteraktivitet = new MaterialEditText(getContext());
        etUndersoegelsessted = new MaterialEditText(getContext());
        etInitialer = new MaterialEditText(getContext());


        //Get all appointments for this person
        ArrayList<Appointment> someText = RoleHelper.getAllAppointments(role);

        //txt.setText(appointment.getDay()+"/"+appointment.getMonth()+"/"+appointment.getYear());
        initLayout();
        setEditable();


        //etName.setText(appointment.getEvent());
        return rootView;

    }

    private void updateAppointment() {

    // Make

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
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // Fullname
        MaterialEditText etFullname = new MaterialEditText(getContext());
        etFullname.setText(appointment.fullName);
        linearLayout.addView(etFullname);

        // Gestationsalder
        etGestationsalder.setText("" + appointment.getGestationsalder());
        etGestationsalder.setFloatingLabelAlwaysShown(true);
        etGestationsalder.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etGestationsalder.setFloatingLabelText("Gestationsalder");
        linearLayout.addView(etGestationsalder);

        // Vaegt
        etVaegt.setText("" + appointment.vaegt);
        etVaegt.setFloatingLabelAlwaysShown(true);
        etVaegt.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etVaegt.setFloatingLabelText("Vægt");
        linearLayout.addView(etVaegt);

        // Blodtryk
        etBlodtryk.setText(appointment.getBlodtryk());
        etBlodtryk.setFloatingLabelAlwaysShown(true);
        etBlodtryk.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etBlodtryk.setFloatingLabelText("Blodtryk");
        linearLayout.addView(etBlodtryk);

        // UrinASLeuNit
        etUrinASLeuNit.setText(appointment.getUrinASLeuNit());
        etUrinASLeuNit.setFloatingLabelAlwaysShown(true);
        etUrinASLeuNit.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etUrinASLeuNit.setFloatingLabelText("Urin: A, S, Leu, Nit");
        linearLayout.addView(etUrinASLeuNit);

        // Oedem
        etOedem.setText(appointment.oedem);
        etOedem.setFloatingLabelAlwaysShown(true);
        etOedem.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etOedem.setFloatingLabelText("Oedem");
        linearLayout.addView(etOedem);

        // SymfyseFundus
        etSymfyseFundus.setText("" + appointment.symfyseFundus);
        etSymfyseFundus.setFloatingLabelAlwaysShown(true);
        etSymfyseFundus.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etSymfyseFundus.setFloatingLabelText("SymfyseFundus");
        linearLayout.addView(etSymfyseFundus);

        // Fosterpraes
        etFosterpraes.setText("" + appointment.getFosterpraes());
        etFosterpraes.setFloatingLabelAlwaysShown(true);
        etFosterpraes.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etFosterpraes.setFloatingLabelText("Fosterpræs");
        linearLayout.addView(etFosterpraes);

        // Fosterskoen
        etFosterskoen.setText(appointment.fosterskoen);
        etFosterskoen.setFloatingLabelAlwaysShown(true);
        etFosterskoen.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etFosterskoen.setFloatingLabelText("Fosterskøn");
        linearLayout.addView(etFosterskoen);

        // Fosteraktivitet
        etFosteraktivitet.setText(appointment.fosteraktivitet);
        etFosteraktivitet.setFloatingLabelAlwaysShown(true);
        etFosteraktivitet.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etFosteraktivitet.setFloatingLabelText("Fosteraktivitet");
        linearLayout.addView(etFosteraktivitet);

        // Undersoegelsessted
        etUndersoegelsessted.setText(appointment.undersoegelsessted);
        etUndersoegelsessted.setFloatingLabelAlwaysShown(true);
        etUndersoegelsessted.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etUndersoegelsessted.setFloatingLabelText("Undersøgelsessted");
        linearLayout.addView(etUndersoegelsessted);

        // Initialer
        etInitialer.setText(appointment.initialer);
        etInitialer.setFloatingLabelAlwaysShown(true);
        etInitialer.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        etInitialer.setFloatingLabelText("Initialer");
        linearLayout.addView(etInitialer);

    }
    public void setEditable() {
        switch(role) {
            case "MW":
                return;

            case "DR":
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
}
