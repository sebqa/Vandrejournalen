package com.example.sebastian.vandrejournalen.Results;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.RoleHelper;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.calendar.Appointment;
import com.example.sebastian.vandrejournalen.calendar.RecyclerAdapter;
import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


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
    MaterialSpinner typeSpinner;
    Appointment appointment;
    MaterialEditText etGestationsalder, etVaegt, etBlodtryk, etUrinASLeuNit, etOedem, etSymfyseFundus, etFosterpraes, etFosterskoen, etFosteraktivitet, etUndersoegelsessted, etInitialer,etType;


    public ResultsFragment() {
        // Required empty public constructor
    }

    public static ResultsFragment newInstance(User user, Appointment appointment) {
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
            user.setRole("Midwife");
            Log.d(TAG, "onCreate: "+user.getRole());
            appointment = new Gson().fromJson(getArguments().getString("obj"), Appointment.class);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_results, container, false);
        setHasOptionsMenu(true);

        Log.d(TAG, "onCreateView: "+user.getRole());
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

        //Type of consultation
        if(appointment.getEvent() == null){
            initSpinner();
            cLinearLayout.addView(typeSpinner);
        } else{
            etType = new MaterialEditText(context);
            etType.setText(appointment.getEvent());
            etType.setFloatingLabelAlwaysShown(true);
            etType.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
            etType.setFloatingLabelText(getString(R.string.responsible));
            etType.setFocusable(false);
            cLinearLayout.addView(etType);
        }



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

    private void initSpinner() {
        typeSpinner = new MaterialSpinner(context);
        String[] ITEMS = {getString(R.string.gp), getString(R.string.mw), getString(R.string.sp)};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setTextSize(12);
        typeSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                if(item.equals(user.getRole()) || item.equals(RoleHelper.translateRole(user.getRole()))){
                    Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                } else{
                    if(dialog == null) {
                        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                                .title(getResources().getString(R.string.choose) +" "+item)
                                .items("Person","Anden person","Tredje person")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .dismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {
                                        dialog = null;
                                    }
                                })
                                .itemsCallback(new MaterialDialog.ListCallback() {
                                    @Override
                                    public void onSelection(MaterialDialog mdialog, View view, int which, CharSequence text) {
                                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

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
