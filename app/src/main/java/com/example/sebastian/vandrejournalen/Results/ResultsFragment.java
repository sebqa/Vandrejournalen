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

        //Get all appointments for this person
        ArrayList<Appointment> someText = RoleHelper.getAllAppointments(role);

        //txt.setText(appointment.getDay()+"/"+appointment.getMonth()+"/"+appointment.getYear());
        initLayout();

        //etName.setText(appointment.getEvent());
        return rootView;

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
        MaterialEditText etGestationsalder = new MaterialEditText(getContext());
        etGestationsalder.setText(appointment.gestationsalder);
        linearLayout.addView(etGestationsalder);

        // Vaegt
        MaterialEditText etVaegt = new MaterialEditText(getContext());
        etVaegt.setText("" + appointment.vaegt);
        linearLayout.addView(etVaegt);

        // Blodtryk
        MaterialEditText etBlodtryk = new MaterialEditText(getContext());
        etBlodtryk.setText(appointment.blodtryk);
        linearLayout.addView(etBlodtryk);

        // UrinASLeuNit
        MaterialEditText etUrinASLeuNit = new MaterialEditText(getContext());
        etUrinASLeuNit.setText(appointment.urinASLeuNit);
        linearLayout.addView(etUrinASLeuNit);

        // Oedem
        MaterialEditText etOedem = new MaterialEditText(getContext());
        etOedem.setText(appointment.oedem);
        linearLayout.addView(etOedem);

        // SymfyseFundus
        MaterialEditText etSymfyseFundus = new MaterialEditText(getContext());
        etSymfyseFundus.setText("" + appointment.symfyseFundus);
        linearLayout.addView(etSymfyseFundus);


    }
}
