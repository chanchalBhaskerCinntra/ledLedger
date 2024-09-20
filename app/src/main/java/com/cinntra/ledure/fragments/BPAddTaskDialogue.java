package com.cinntra.ledure.fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cinntra.ledure.R;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.EventResponse;
import com.cinntra.ledure.model.EventValue;
import com.cinntra.ledure.model.NewEvent;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.newapimodel.NewOpportunityRespose;
import com.cinntra.ledure.receivers.NotificationPublisher;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class BPAddTaskDialogue extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.date_value)
    TextView date_value;
    @BindView(R.id.time_value)
    TextView settime;
    @BindView(R.id.title_text)
    EditText title_text;
    @BindView(R.id.add_location_text)
    EditText add_location_text;
    @BindView(R.id.host_text)
    EditText host_text;
    @BindView(R.id.description_text)
    EditText description_text;
    @BindView(R.id.simple_switch)
    Switch simple_switch;
    @BindView(R.id.date_view)
    RelativeLayout date_view;
    @BindView(R.id.color_view)
    LinearLayout color_view;
    @BindView(R.id.time_view)
    RelativeLayout time_view;
    @BindView(R.id.progress_status_view)
    RelativeLayout progress_status_view;
    @BindView(R.id.spinnerview)
    RelativeLayout spinnerview;
    @BindView(R.id.spinner)
    Spinner preority_spinner;
    @BindView(R.id.color_spinner)
    Spinner colorspin;
    @BindView(R.id.progress_spinner)
    Spinner progress_spinner;
    @BindView(R.id.submit_button)
    Button submit_button;
    @BindView(R.id.add)
    ImageView add;

    String priority = "";
    String allday = "";
    String repeated = "";
    String progresstatus = "";
    int t1hr,t1min;
    EventPrerioritySpinner eventPrerioritySpinner;
    EventTextSpinner eventTextSpinner;
    TaskProgressSpinner taskProgressSpinner;
    NewOpportunityRespose opportunityItem;
    ArrayList<String> categories      = new ArrayList<>();
    ArrayList<Integer> circleimage    = new ArrayList<>();
    ArrayList<String> progress_status = new ArrayList<>();
    final Calendar myCalendar         = Calendar.getInstance();
    private AlarmManager alarmManager;
    private Calendar myTime;

    public BPAddTaskDialogue() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static BPAddTaskDialogue newInstance(String title) {
        BPAddTaskDialogue frag = new BPAddTaskDialogue();

        return frag;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,
                android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState)
      {
    View v = inflater.inflate(R.layout.fragment_add_task, container);
    ButterKnife.bind(this,v);

    setDefaults();
//     loadData();
    return v;
       }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
        head_title.setText("New Task");

        back_press.setOnClickListener(this);
        submit_button.setOnClickListener(this);
        simple_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    allday = "All day";
                }else{
                    allday = "One day";
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.back_press)
        {
            getDialog().dismiss();
        }
        else if(v.getId()==R.id.submit_button)
        {

            String title      = title_text.getText().toString().trim();
            String date       = date_value.getText().toString().trim();
            String location   = add_location_text.getText().toString().trim();
            String host       = host_text.getText().toString().trim();
            String desc       = description_text.getText().toString().trim();
            String time = settime.getText().toString().trim();


                if(validation(title,location,host,desc,time,date)){
                    EventValue eventValue = new EventValue();
                    eventValue.setOppId(Integer.valueOf(opportunityItem.getId()));
                    eventValue.setTitle(title);
                    eventValue.setDescription(desc);
                    eventValue.setFrom(Globals.getTodaysDate());
                    eventValue.setTo(date);
                    eventValue.setEmp(5);
                    eventValue.setCreateTime(Globals.getTCurrentTime());
                    eventValue.setCreateDate(Globals.getTodaysDate());
                    eventValue.setType("Task");
                    eventValue.setSourceType("");
                    eventValue.setParticipants("");
                    eventValue.setComment("");
                    eventValue.setSubject("");
                    eventValue.setTime(Globals.getTCurrentTime());
                    eventValue.setDocument("");
                    eventValue.setRelatedTo("");
                    eventValue.setLocation(location);
                    eventValue.setHost(host);
                    eventValue.setAllday(allday);
                    eventValue.setName(opportunityItem.getCustomerName());
                    eventValue.setProgressStatus(progresstatus);
                    eventValue.setPriority(priority);
                    eventValue.setRepeated("");




                /*    if(Globals.checkInternet(getContext()))
                        callApi(eventValue);*/




             /* NewEvent event = new NewEvent(title,fromDate,toDate,true,"Repeate",location,
             host,"top",partcipant,desc,related,1,time);
              TaskEventList.add(event);*/

                // saveData();


            }

            getDialog().dismiss();

        }
    }

    private void callApi(EventValue eventValue) {

        Call<EventResponse> call = NewApiClient.getInstance().getApiService(getActivity()).createnewevent(eventValue);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if(response.code()==200)
                {
                    Globals.SelectedItems.clear();
                    Toast.makeText(getContext(), "Posted Successfully.", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();

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


    private boolean validation(String title, String location, String host, String desc, String time, String date) {
        if(title.isEmpty()){
            title_text.setError(getResources().getString(R.string.title_error));
            return false;
        }else if (date.isEmpty()){
            date_value.setError(getResources().getString(R.string.todate_error));
            return false;
        }else if (location.isEmpty()){
            add_location_text.setError(getResources().getString(R.string.location_error));
            return false;
        }else if (host.isEmpty()){
            host_text.setError(getResources().getString(R.string.host_error));
            return false;
        } else if (desc.isEmpty()){
            description_text.setError(getResources().getString(R.string.description_error));
            return false;
        }
        return true;

    }


    private void setDefaults()
    {
        add.setVisibility(View.GONE);
        circleimage.add(R.drawable.red_dot);
        circleimage.add(R.drawable.ic_green_dot);
        circleimage.add(R.drawable.yellow_dot);
        categories.add("Repeat");
        categories.add("Daily");
        categories.add("Monthly");
        categories.add("Weekly");
        progress_status.add("Completed");
        progress_status.add("In Progress");
        progress_status.add("Not Started");
        progress_status.add("Waiting for input");



        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            date_value.setText(sdf.format(myCalendar.getTime()));
        };



        eventPrerioritySpinner = new EventPrerioritySpinner(getActivity(), circleimage);
        colorspin.setAdapter(eventPrerioritySpinner);
        colorspin.setDropDownVerticalOffset(120);
        colorspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    priority = "high";
                }else if  (position==1) {
                    priority = "medium";
                }else{
                    priority = "low";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                priority = "high";
            }
        });
        eventTextSpinner = new EventTextSpinner(getActivity(), categories);
        preority_spinner.setAdapter(eventTextSpinner);
        preority_spinner.setDropDownVerticalOffset(120);
        preority_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                repeated = preority_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                repeated = categories.get(0);
            }
        });

        taskProgressSpinner = new TaskProgressSpinner(getActivity(),progress_status);
        progress_spinner.setAdapter(taskProgressSpinner);
        progress_spinner.setDropDownVerticalOffset(120);
        progress_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                progresstatus = preority_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                progresstatus = progress_status.get(0);
            }
        });

        date_value.setOnClickListener(v -> {
            date_value.getText().toString();
            if (!date_value.getText().toString().isEmpty()) {
                String value = date_value.getText().toString();
                String[] dd = value.split("-");
                int y = Integer.parseInt(dd[2]);
                int m = Integer.parseInt(dd[1])-1;
                int d = Integer.parseInt(dd[0]);
                myCalendar.set(Calendar.DAY_OF_MONTH, y);
                myCalendar.set(Calendar.MONTH, m);
                myCalendar.set(Calendar.YEAR, d);
                new DatePickerDialog(getActivity(), date, d, m, y).show();
            } else {
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }

        });

        settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        t1hr = hourOfDay;
                        t1min = minute;
                        myTime = Calendar.getInstance();
//                        myTime.set(0,0,0,t1hr,t1min);
                        myTime.set(Calendar.HOUR_OF_DAY,t1hr);
                        myTime.set(Calendar.MINUTE,t1min);
                        myTime.set(Calendar.SECOND,0);
                        myTime.set(Calendar.MILLISECOND,0);
                        settime.setText(DateFormat.format("hh:mm aa",myTime));
                        setAlarm();
                    }
                },12,0,false
                );
                timePickerDialog.updateTime(t1hr,t1min);
                timePickerDialog.show();

            }

        });




    }



    /****************** Manage List in local ***************************/

    private void saveData()
      {
     SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(TaskEventList);
        editor.putString(Globals.TaskEventList, json);
        editor.apply();
       // Toast.makeText(getActivity(), "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }

    private ArrayList<NewEvent> TaskEventList;
    private void loadData()
        {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Globals.TaskEventList, null);
        Type type = new TypeToken<ArrayList<NewEvent>>() {}.getType();
        TaskEventList = gson.fromJson(json, type);
        if (TaskEventList == null) {
            TaskEventList = new ArrayList<>();
             }
    }
    private void setAlarm() {

        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getContext(), NotificationPublisher.class);
        i.putExtra("value",getActivity().getResources().getString(R.string.task_notification));
        i.putExtra("title",getActivity().getResources().getString(R.string.ur_task));
        final int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),id,i,PendingIntent.FLAG_ONE_SHOT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,myTime.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

    }



}
