package com.example.sebastian.vandrejournalen.calendar;

import android.content.Context;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.sebastian.journalapp.R;
import java.util.ArrayList;


/**
 * Created by Sebastian on 02-11-2016.
 */

public class RecyclerAdapterNotesList extends RecyclerView.Adapter<RecyclerAdapterNotesList.RecyclerViewHolder> {
    int position;
    private ArrayList<Appointment> arrayList = new ArrayList<Appointment>();
    private int selectedPos = 0;

    Context ctx;

    //Constructor for the RecyclerAdapterNotesList
    public RecyclerAdapterNotesList(ArrayList<Appointment> arrayList, Context ctx){
        this.arrayList = arrayList;
        this.ctx =ctx;

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
        final Appointment event = arrayList.get(position);

        holder.date.setText(event.getDay()+"/"+ event.getMonth()+"/"+ event.getYear()+"\n"+event.getTime());
        holder.event.setText(event.getEvent());
        holder.container.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Here goes your desired onClick behaviour. Like:
                //You can change the fragment, something like this, not tested, please correct for your desired output:
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                /*EventPreview ep = EventPreview.newInstance(event);
                //Create a bundle to pass data, add data, set the bundle to your fragment and:
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.previewContainer, ep).addToBackStack(null).commit();
*/
            }
        });

        this.position = position;
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{



        TextView date,event;
        private View container;
        ArrayList<Appointment> events = new ArrayList<Appointment>();
        Context ctx;

        //Constructor for the view holder.
        public RecyclerViewHolder(View view, Context ctx, ArrayList<Appointment> events){
            super(view);
            this.events = events;
            this.ctx = ctx;

            //Cast the values to a Text or ImageView in the layout.
            date = view.findViewById(R.id.eventDate);
            event = view.findViewById(R.id.eventText);
            container = view.findViewById(R.id.item_container);

            //Set an onClickListener to the entire view.
            view.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {

            //Find out which item was clicked
            int position = getAdapterPosition();
            Appointment event = this.events.get(position);
            if(event != null) {
                //Create new intent that gets us to the next activity.
                //Intent intent = new Intent(ctx, ItemDetails.class);
                //Toast.makeText(view.getContext(), "Clicked "+event.getDate(),
                        //Toast.LENGTH_SHORT).show();








                //Start the new activity.
                //this.ctx.startActivity(intent);
            }else{
                //Toast.makeText(view.getContext(), "Item no longer exists",
                        //Toast.LENGTH_SHORT).show();

            }


        }

    }


}

