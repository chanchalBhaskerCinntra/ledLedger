package com.cinntra.ledure.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.ActivityTasksAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.EventResponse;
import com.cinntra.ledure.model.EventValue;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BPTasks_Fragment extends Fragment {

  @BindView(R.id.taskList)
  RecyclerView taskList;
    ArrayList<EventValue> alltasklist ;
    ActivityTasksAdapter adapter;
    String id;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_tasks, container, false);
        ButterKnife.bind(this,v);

    //    loadData();
//        callApi(id);
       return v;
    }
    private void callApi(String id) {
        alltasklist = new ArrayList<>();

        EventValue eventValue = new EventValue();
        eventValue.setEmp(Integer.valueOf(Prefs.getString(Globals.EmployeeID,"")));


        Call<EventResponse> call = NewApiClient.getInstance().getApiService().getallevent(eventValue);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {

                if(response.code()==200||response.body()!=null)
                {
                    if(response.body().getData().size()>0){
                        alltasklist.clear();
                        alltasklist.addAll(response.body().getData());
                        setAdapter();
                    }
                    else
                        Toast.makeText(getContext(),"No data found",Toast.LENGTH_LONG).show();
                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s,QuotationResponse.class);
                        Toast.makeText(getActivity(), mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }
    public ArrayList<EventValue> filter(String text ) {

        ArrayList<EventValue> templist= new ArrayList<>();
        templist.clear();
        for (EventValue st : alltasklist) {

            if(st.getType().equals(text)) {

                templist.add(st);

            }


        }

        return templist;
    }
    private void setAdapter() {
        taskList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        adapter = new ActivityTasksAdapter(getActivity(), filter("Task"));

        taskList.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }
    private ArrayList<EventValue> geTasks(ArrayList<EventValue> list)
        {
        ArrayList<EventValue> events = new ArrayList<>();
        for (EventValue event :list
        ) {
            if(event.getType().equals("Task") &&Globals.CurrentSelectedDate.equalsIgnoreCase(event.getTo()))
                events.add(event);
        }

        return events;
    }












}