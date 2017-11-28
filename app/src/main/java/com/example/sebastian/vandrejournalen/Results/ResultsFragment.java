package com.example.sebastian.vandrejournalen.Results;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.RoleHelper;
import com.example.sebastian.vandrejournalen.calendar.Appointment;
import com.example.sebastian.vandrejournalen.calendar.RecyclerAdapter;
import com.example.sebastian.vandrejournalen.calendar.Schedule;

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

    public ResultsFragment() {
        // Required empty public constructor
    }

    public static ResultsFragment newInstance(String role) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putString("role",role);
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
        etName = rootView.findViewById(R.id.name);

        //Get all appointments for this person
        ArrayList<Appointment> someText = RoleHelper.getAllAppointments(role);

        TextView txt =  rootView.findViewById(R.id.text_view);
        Appointment appointment = someText.get(args.getInt("position"));
        txt.setText(appointment.getDay()+"/"+appointment.getMonth()+"/"+appointment.getYear());

        etName.setText(appointment.getEvent());
        return rootView;

    }



    public interface OnFragmentInteractionListener {
        void makeScrollable(View view);
    }
}
