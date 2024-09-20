package com.cinntra.ledure.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.EventsTask_All_Adapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.EventResponse;
import com.cinntra.ledure.model.EventValue;
import com.cinntra.ledure.model.NewEvent;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.model.SalesEmployeeItem;
import com.cinntra.ledure.webservices.NewApiClient;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class All_Event_Task_Fragment extends Fragment {
    @BindView(R.id.task_event_List)
    RecyclerView task_event_List;
    @BindView(R.id.loader)
    SpinKitView loader;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;

    private ArrayList<EventValue> TaskEventList;
    public All_Event_Task_Fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static All_Event_Task_Fragment newInstance(String param1, String param2) {
        All_Event_Task_Fragment fragment = new All_Event_Task_Fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_all_event_task, container, false);
        ButterKnife.bind(this,v);
       // loadData();
        if(Globals.checkInternet(getContext())) {
            loader.setVisibility(View.VISIBLE);
            callApi();
        }
        return v;
    }
    private void callApi() {

        TaskEventList= new ArrayList<>();


        SalesEmployeeItem eventValue = new SalesEmployeeItem();
        eventValue.setEmp(Prefs.getString(Globals.EmployeeID,""));
        eventValue.setDate(Globals.CurrentSelectedDate);


        Call<EventResponse> call = NewApiClient.getInstance().getApiService(getActivity()).getcalendardata(eventValue);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {

                if(response.code()==200)
                {
                    if(response.body().getData().size()>0){
                        no_datafound.setVisibility(View.GONE);
                        TaskEventList.clear();
                        TaskEventList.addAll(response.body().getData());
                        setAdapter();
                    }
                    else {
                        no_datafound.setVisibility(View.VISIBLE);
                    }
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
                loader.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }


    private ArrayList<NewEvent> geAllTasksEvents(ArrayList<NewEvent> list)
          {
        ArrayList<NewEvent> events = new ArrayList<>();
        for (NewEvent event :list
        ) {
            if(event.getType()==Globals.TYPE_EVENT) {
                try {
                    String to = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    Date FromDate = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDateFrom().trim());
                    Date ToDate = new SimpleDateFormat("yyyy-MM-dd").parse(event.getDateTo().trim());
                    Date ToDayDate = new SimpleDateFormat("yyyy-MM-dd").parse(to);

                    boolean dateStatus = isDateInBetweenIncludingEndPoints(FromDate, ToDate, ToDayDate);


                   /* Log.e("DATE From",""+event.getDateFrom().trim());
                    Log.e("DATE TO",""+event.getDateTo().trim());
                    Log.e("DATE TODAY",""+to);
                    Log.e("DATE STATUS",""+dateStatus);*/
                    if (dateStatus)
                        events.add(event);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
           else if(Globals.CurrentSelectedDate.equalsIgnoreCase(event.getDateFrom()))
                events.add(event);
        }

        return events;
    }

    public static boolean isDateInBetweenIncludingEndPoints(final Date min, final Date max, final Date date){
        return !(date.before(min) || date.after(max));
    }

    /********************** Manage List in local *******************************/



    private void setAdapter() {
        EventsTask_All_Adapter all_adapter ;
        task_event_List.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
       all_adapter = new EventsTask_All_Adapter(getActivity(),filter(TaskEventList));
        task_event_List.setAdapter(all_adapter);
        //all_adapter.notifyDataSetChanged();


        if(all_adapter.getItemCount()==0)
            no_datafound.setVisibility(View.VISIBLE);
        else
            no_datafound.setVisibility(View.GONE);


    }

    private ArrayList<EventValue> filter(ArrayList<EventValue> task_event_list) {
        ArrayList<EventValue> filterlist = new ArrayList<>();
        for (EventValue ev : task_event_list){
            if(ev.getType().equalsIgnoreCase("Task")||ev.getType().equalsIgnoreCase("Event")){
                filterlist.add(ev);
            }
        }
        return filterlist;

    }
}