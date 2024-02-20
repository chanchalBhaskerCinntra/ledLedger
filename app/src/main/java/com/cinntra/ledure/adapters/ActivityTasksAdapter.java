package com.cinntra.ledure.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.fragments.UpdateActivityTaskDetailFragment;
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


public class ActivityTasksAdapter extends RecyclerView.Adapter<ActivityTasksAdapter.TaskViewHolder> {
    Context context;
    ArrayList<EventValue> allEventTaskList;
    public ActivityTasksAdapter(Context context, ArrayList<EventValue> allEventTaskList)
    {
        this.context = context;
        this.allEventTaskList = allEventTaskList;
        this.templist = new ArrayList<>();
        this.templist.addAll(allEventTaskList);

    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(context).inflate(R.layout.activitytask_item,parent,false);
        return new TaskViewHolder(rootView);


    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {


        holder.title.setText(allEventTaskList.get(position).getTitle());
        holder.location.setText(allEventTaskList.get(position).getDescription());
        holder.timing.setText(allEventTaskList.get(position).getTime());

        /*if(allEventTaskList.get(position).getPriority().equalsIgnoreCase("high")){
            holder.priority_dot.setBackgroundResource(R.drawable.ic_red_dot);
        }else if (allEventTaskList.get(position).getPriority().equalsIgnoreCase("medium")){
            holder.priority_dot.setBackgroundResource(R.drawable.ic_green_dot);
        }else{
            holder.priority_dot.setBackgroundResource(R.drawable.yellow_dot);
        }*/



    }

   /* @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return allEventTaskList.get(position).getType();
    }*/



    @Override
    public int getItemCount() {
        return allEventTaskList.size();
    }


    class TaskViewHolder extends RecyclerView.ViewHolder {
        LinearLayout taskView;
        TextView title,location,timing;
        ImageView priority_dot;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            taskView = itemView.findViewById(R.id.taskView);
            title = itemView.findViewById(R.id.title);
            location = itemView.findViewById(R.id.location);
            timing = itemView.findViewById(R.id.timing);
            priority_dot = itemView.findViewById(R.id.priority_dot);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    PopupMenu popup = new PopupMenu(v.getContext(),itemView);
                    popup.getMenuInflater().inflate(R.menu.longpress_menu,popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getItemId() == R.id.edit){

                                Bundle b = new Bundle();
                                b.putParcelable("View",allEventTaskList.get(getAdapterPosition()));
                                b.putInt("Position",getAdapterPosition());
                                UpdateActivityTaskDetailFragment fragment = new UpdateActivityTaskDetailFragment();
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
                    return true;
                }
            });
        }
    }

    ArrayList<EventValue> templist;
    public void filter(String text ) {

        allEventTaskList.clear();

        for (EventValue st : templist) {

            if(st.getType().equals(text)) {

                allEventTaskList.add(st);

            }


        }
        notifyDataSetChanged();

    }

    private void callDeletApi(Integer id) {

        EventValue eventValue = new EventValue();
        eventValue.setId(id);

        Call<EventResponse> call = NewApiClient.getInstance().getApiService().deleteEvent(eventValue);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if(response.code()==200)
                {
                    Toast.makeText(context, "Deleted Successfully.", Toast.LENGTH_SHORT).show();

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
}
