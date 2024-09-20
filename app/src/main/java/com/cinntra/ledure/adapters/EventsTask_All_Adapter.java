package com.cinntra.ledure.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.cinntra.ledure.R;
import com.cinntra.ledure.fragments.EventDetailFragment;
import com.cinntra.ledure.fragments.UpdateTaskDetailFragment;
import com.cinntra.ledure.model.EventResponse;
import com.cinntra.ledure.model.EventValue;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventsTask_All_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>   {
    Context context;
    private static int TYPE_EVENT = 1;
    private static int TYPE_TASK  = 2;

    ArrayList<EventValue> allEventTaskList;
    public EventsTask_All_Adapter(Context context, ArrayList<EventValue> allEventTaskList)
       {
    this.context = context;
    this.allEventTaskList = allEventTaskList;

      }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == TYPE_TASK) { // for call layout
            view= LayoutInflater.from(context).inflate(R.layout.task_item,parent,false);
            return new TaskViewHolder(view);

        } else { // for email layout
            view= LayoutInflater.from(context).inflate(R.layout.events_item,parent,false);
            return new EventViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(holder.getAdapterPosition())==TYPE_TASK){
            ((TaskViewHolder)holder).title.setText(allEventTaskList.get(holder.getAdapterPosition()).getTitle());
            ((TaskViewHolder)holder).location.setText(allEventTaskList.get(holder.getAdapterPosition()).getDescription());
            ((TaskViewHolder)holder).timing.setText(allEventTaskList.get(holder.getAdapterPosition()).getTime());


            if(allEventTaskList.get(holder.getAdapterPosition()).getPriority().equalsIgnoreCase("high")){
                ((TaskViewHolder)holder).priority_dot.setBackground(context.getDrawable(R.drawable.ic_red_dot));
            }else if (allEventTaskList.get(holder.getAdapterPosition()).getPriority().equalsIgnoreCase("medium")){
                ((TaskViewHolder)holder).priority_dot.setBackground(context.getDrawable(R.drawable.ic_green_dot));
            }else{
                ((TaskViewHolder)holder).priority_dot.setBackground(context.getDrawable(R.drawable.yellow_dot));
            }

            if(allEventTaskList.get(holder.getAdapterPosition()).getStatus().equals("1"))
            ((TaskViewHolder) holder).completed.setChecked(true);
            else
            ((TaskViewHolder) holder).completed.setChecked(false);

            ((TaskViewHolder) holder).completed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    callcompletedApi(allEventTaskList.get(holder.getAdapterPosition()).getId());
                }
            });


        }
        else {
            ((EventViewHolder)holder).title.setText(allEventTaskList.get(holder.getAdapterPosition()).getTitle());
            ((EventViewHolder)holder).location.setText(allEventTaskList.get(holder.getAdapterPosition()).getDescription());
            ((EventViewHolder)holder).timing.setText(allEventTaskList.get(holder.getAdapterPosition()).getTime());


            if(allEventTaskList.get(holder.getAdapterPosition()).getPriority().equalsIgnoreCase("high")){
                ((EventViewHolder)holder).priority_dot.setBackground(context.getDrawable(R.drawable.ic_red_dot));
            }else if (allEventTaskList.get(holder.getAdapterPosition()).getPriority().equalsIgnoreCase("medium")){
                ((EventViewHolder)holder).priority_dot.setBackground(context.getDrawable(R.drawable.ic_green_dot));
            }else{
                ((EventViewHolder)holder).priority_dot.setBackground(context.getDrawable(R.drawable.yellow_dot));
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(allEventTaskList.get(position).getType().equals("Task"))
            return TYPE_TASK;
        else
            return TYPE_EVENT;
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous

    }



    @Override
    public int getItemCount() {
        return allEventTaskList.size();
    }






    class TaskViewHolder extends RecyclerView.ViewHolder   {
        LinearLayout taskView;
        TextView title,location,timing;
        ImageView priority_dot;
        CheckBox completed;
        CardView card;
       public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
//            itemView.setOnCreateContextMenuListener(this);

           taskView = itemView.findViewById(R.id.taskView);
           title = itemView.findViewById(R.id.title);
           location = itemView.findViewById(R.id.location);
           timing = itemView.findViewById(R.id.timing);
           priority_dot = itemView.findViewById(R.id.priority_dot);
           completed = itemView.findViewById(R.id.status);
           card = itemView.findViewById(R.id.card);

           card.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   PopupMenu popup = new PopupMenu(v.getContext(),card);
                   popup.getMenuInflater().inflate(R.menu.longpress_menu,popup.getMenu());
                   popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                       @Override
                       public boolean onMenuItemClick(MenuItem item) {
                           if(item.getItemId() == R.id.edit){
                               Bundle b = new Bundle();
                               b.putSerializable("View",allEventTaskList.get(getAdapterPosition()));
                               b.putInt("Position",getAdapterPosition());
                               UpdateTaskDetailFragment fragment = new UpdateTaskDetailFragment();
                               fragment.setArguments(b);
                               FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                               transaction.replace(R.id.frame,fragment);
                               transaction.addToBackStack("");
                               transaction.commit();
                           }else{
                               callDeletApi(allEventTaskList.get(getAdapterPosition()).getId());
                               allEventTaskList.remove(getAdapterPosition());
                               notifyDataSetChanged();

                           }
                           return true;
                       }
                   });
                   popup.show();
               }
           });


       }




    }

    private void callcompletedApi(Integer id) {
        EventValue eventValue = new EventValue();
        eventValue.setId(id);

        Call<EventResponse> call = NewApiClient.getInstance().getApiService(context).completeEvent(eventValue);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if(response.code()==200)
                {
                    Toast.makeText(context, "Completed Successfully.", Toast.LENGTH_SHORT).show();
                    // notifyDataSetChanged();
                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s,QuotationResponse.class);
                        Toast.makeText(context, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void callDeletApi(Integer id) {

        EventValue eventValue = new EventValue();
        eventValue.setId(id);

        Call<EventResponse> call = NewApiClient.getInstance().getApiService(context).deleteEvent(eventValue);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if(response.code()==200)
                {
                    Toast.makeText(context, "Deleted Successfully.", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s,QuotationResponse.class);
                        Toast.makeText(context, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    class EventViewHolder extends RecyclerView.ViewHolder {

        LinearLayout eventView;
        TextView title,location,timing;
        ImageView priority_dot;
        CardView card;
        EventViewHolder(@NonNull View itemView) {
        super(itemView);
        eventView = itemView.findViewById(R.id.eventView);
        title     = itemView.findViewById(R.id.title);
        location  = itemView.findViewById(R.id.location);
        timing  = itemView.findViewById(R.id.timing);
        priority_dot  = itemView.findViewById(R.id.priority_dot);
        card  = itemView.findViewById(R.id.card);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(v.getContext(),card);
                    popup.getMenuInflater().inflate(R.menu.longpress_menu,popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getItemId() == R.id.edit){
                                Bundle b = new Bundle();
                                b.putSerializable("View",allEventTaskList.get(getAdapterPosition()));
                                b.putInt("Position",getAdapterPosition());
                                EventDetailFragment fragment = new EventDetailFragment();
                                fragment.setArguments(b);
                                FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.frame,fragment);
                                transaction.addToBackStack("");
                                transaction.commit();
                            }else{
                                callDeletApi(allEventTaskList.get(getAdapterPosition()).getId());
                                allEventTaskList.remove(getAdapterPosition());
                                notifyDataSetChanged();

                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });



        }
    }
}
