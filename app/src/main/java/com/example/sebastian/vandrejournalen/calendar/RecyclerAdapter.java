package com.example.sebastian.vandrejournalen.calendar;

import android.content.Context;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.sebastian.journalapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * Created by Sebastian on 02-11-2016.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    int position;
    private ArrayList<Note> arrayList = new ArrayList<Note>();

    Context ctx;

    //Constructor for the RecyclerAdapter
    public RecyclerAdapter(ArrayList<Note> arrayList, Context ctx){
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
        final Note note = arrayList.get(position);
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - kk:mm");
        holder.date.setText(""+formatter.format(note.getDate()));
        holder.text.setText(note.getText());
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



        TextView date, text;
        private View container;
        ArrayList<Note> notes = new ArrayList<Note>();
        Context ctx;

        //Constructor for the view holder.
        public RecyclerViewHolder(View view, Context ctx, ArrayList<Note> notes){
            super(view);
            this.notes = notes;
            this.ctx = ctx;

            //Cast the values to a Text or ImageView in the layout.
            date = view.findViewById(R.id.noteDate);
            text = view.findViewById(R.id.noteText);
            container = view.findViewById(R.id.item_container);

            //Set an onClickListener to the entire view.
            view.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {

            //Find out which item was clicked
            int position = getAdapterPosition();
            Note note = this.notes.get(position);
            if(note != null) {
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

