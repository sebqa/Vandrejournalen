package com.example.sebastian.vandrejournalen.Results;

import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sebastian.journalapp.R;
import com.example.sebastian.vandrejournalen.Patient;
import com.example.sebastian.vandrejournalen.User;
import com.example.sebastian.vandrejournalen.authentication.RegisterPatientFragment;
import com.example.sebastian.vandrejournalen.networking.ServerClient;
import com.example.sebastian.vandrejournalen.networking.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


/**
 * Created by Sebastian on 02-11-2016.
 */

public class RecyclerAdapterPatientList extends RecyclerView.Adapter<RecyclerAdapterPatientList.RecyclerViewHolder> {
    int position;
    private ArrayList<Patient> arrayList = new ArrayList<Patient>();
    private int selectedPos = 0;
    ServerClient client;
    OnFragmentInteractionListener mListener;
    Context ctx;

    //Constructor for the RecyclerAdapterNotesList
    public RecyclerAdapterPatientList(ArrayList<Patient> arrayList, Context ctx){
        this.arrayList = arrayList;
        this.ctx =ctx;
        client = ServiceGenerator.createService(ServerClient.class);
        mListener = (OnFragmentInteractionListener) ctx;


    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Specify the layout of the RecyclerView, in this case 'item_layout'.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_layout,parent,false);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view,ctx,arrayList);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {

        //Attach the values we retrieve from the Item class to the values.
        final Patient patient = arrayList.get(position);

        holder.date.setText(patient.getName());
        holder.event.setText(patient.getAddress());
        this.position = position;
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{



        TextView date,event;
        private View container;
        ArrayList<Patient> patients = new ArrayList<Patient>();
        Context ctx;

        //Constructor for the view holder.
        public RecyclerViewHolder(View view, Context ctx, ArrayList<Patient> patients){
            super(view);
            this.patients = patients;
            this.ctx = ctx;

            //Cast the values to a Text or ImageView in the layout.
            date = view.findViewById(R.id.noteDate);
            event = view.findViewById(R.id.noteText);
            container = view.findViewById(R.id.item_container);

            //Set an onClickListener to the entire view.
            view.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {

            //Find out which item was clicked
            int position = getAdapterPosition();
            final Patient patient = this.patients.get(position);
            Log.d(TAG, "onClick: Clicked"+patient.getName());
            if(patient != null) {
                Call<Patient> call = client.getPatientInfo("returnPatientInformationFromPatientID.php",patient.getUserID() );

                call.enqueue(new Callback<Patient>() {
                    @Override
                    public void onResponse(Call<Patient> call, Response<Patient> response) {
                        if(response.body() != null) {
                            Patient cPatient = response.body();
                            mListener.sectionSelection(cPatient);

                        } else{
                            Toast.makeText(ctx, R.string.patient_not_registered, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Patient> call, Throwable t) {
                        Toast.makeText(ctx, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });


            }else{
                //Toast.makeText(view.getContext(), "Item no longer exists",
                        //Toast.LENGTH_SHORT).show();

            }


        }

    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void sectionSelection(Patient patient);
    }

}

