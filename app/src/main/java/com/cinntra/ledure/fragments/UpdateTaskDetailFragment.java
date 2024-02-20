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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cinntra.ledure.R;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.EventResponse;
import com.cinntra.ledure.model.EventValue;
import com.cinntra.ledure.model.NewEvent;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.receivers.NotificationPublisher;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pixplicity.easyprefs.library.Prefs;

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

public class UpdateTaskDetailFragment extends Fragment implements  View.OnClickListener {

    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.date_value)
    EditText date_value;
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
    @BindView(R.id.ok)
    ImageView ok;
    EventValue newEvent;
    int Position ;
    EventPrerioritySpinner eventPrerioritySpinner;
    EventTextSpinner eventTextSpinner;
    TaskProgressSpinner taskProgressSpinner;
    int t1hr,t1min;
    ArrayList<String> categories      = new ArrayList<>();
    ArrayList<Integer> circleimage    = new ArrayList<>();
    ArrayList<String> progress_status = new ArrayList<>();
    final Calendar myCalendar         = Calendar.getInstance();

    private AlarmManager alarmManager;
    private Calendar myTime;
    private String priority,repeated,progresstatus;

    public UpdateTaskDetailFragment() {

    }

    // TODO: Rename and change types and number of parameters
    public static UpdateTaskDetailFragment newInstance(String param1) {
        UpdateTaskDetailFragment fragment = new UpdateTaskDetailFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b      = getArguments();
            newEvent =(EventValue) b.getParcelable("View");
            Position = b.getInt("Position");

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_task, container, false);
        ButterKnife.bind(this, v);
        head_title.setText("Update Task");
        back_press.setOnClickListener(this);
        submit_button.setOnClickListener(this);
        ok.setOnClickListener(this);
        add.setOnClickListener(this);
        setDisable();
        setDefaults();
        setData();
//        loadData();

        return v;
    }

    private void setData() {
        title_text.setText(newEvent.getTitle());
        date_value.setText(newEvent.getFrom());
        settime.setText(newEvent.getTime());
        add_location_text.setText(newEvent.getLocation());
        host_text.setText(newEvent.getHost());
        description_text.setText(newEvent.getDescription());
        

    }

    private void setDisable() {
        title_text.setClickable(false);
        title_text.setFocusable(false);
        title_text.setFocusableInTouchMode(false);

        date_value.setClickable(false);
        date_value.setFocusable(false);
        date_value.setFocusableInTouchMode(false);

        settime.setFocusableInTouchMode(false);
        settime.setFocusable(false);
        settime.setClickable(false);

        add_location_text.setClickable(false);
        add_location_text.setFocusableInTouchMode(false);
        add_location_text.setFocusable(false);

        host_text.setFocusable(false);
        host_text.setClickable(false);
        host_text.setFocusableInTouchMode(false);


        description_text.setFocusable(false);
        description_text.setClickable(false);
        description_text.setFocusableInTouchMode(false);

        colorspin.setEnabled(false);
        preority_spinner.setEnabled(false);

        submit_button.setVisibility(View.GONE);
        ok.setVisibility(View.GONE);
        add.setVisibility(View.VISIBLE);
    }

    private void setDefaults()
    {
        circleimage.add(R.drawable.red_dot);
        circleimage.add(R.drawable.ic_green_dot);
        circleimage.add(R.drawable.yellow_dot);

        categories.add("Daily");
        categories.add("Weekly");
        categories.add("Monthly");
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

        if(newEvent.getPriority().equalsIgnoreCase("high")) {
            colorspin.setSelection(0);
        }
        else if (newEvent.getPriority().equalsIgnoreCase("medium")) {
        colorspin.setSelection(1);
    }
        else{
        colorspin.setSelection(2);
    }

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

        preority_spinner.setSelection(categories.indexOf(newEvent.getRepeated()));

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

        progress_spinner.setSelection(progress_status.indexOf(newEvent.getProgressStatus()));

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
            Globals.selectDate(getContext(),date_value);

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
                        //setAlarm();
                    }
                },12,0,false
                );
                timePickerDialog.updateTime(t1hr,t1min);
                timePickerDialog.show();

            }

        });



    }

    private ArrayList<EventValue> TaskEventList;
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
    private void saveData(ArrayList<EventValue> taskEventList)
    {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(taskEventList);
        editor.putString(Globals.TaskEventList, json);
        editor.apply();
        // Toast.makeText(getActivity(), "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_press:
                getActivity().onBackPressed();
                break;
            case R.id.submit_button:
            case R.id.ok:
                String title      = title_text.getText().toString().trim();
                String date       = date_value.getText().toString().trim();
                String location   = add_location_text.getText().toString().trim();
                String host       = host_text.getText().toString().trim();
                String desc       = description_text.getText().toString().trim();
                String time       = settime.getText().toString().trim();

//                NewEvent task = new NewEvent(title,date,null,true,"Repeate",location,
//                        host,"top",null,desc,null,2,time);



                newEvent.setTitle(title);
                newEvent.setFrom(date);
                newEvent.setLocation(location);
                newEvent.setHost(host);
                newEvent.setDescription(desc);
                newEvent.setTime(time);

//                saveData(TaskEventList);


                if(validation(title,location,host,desc,time,date)){
                    setDisable();
                    EventValue eventValue = new EventValue();
                    eventValue.setOppId(newEvent.getOppId());
                    eventValue.setId(newEvent.getId());
                    eventValue.setTitle(title);
                    eventValue.setDescription(desc);
                    eventValue.setFrom(date);
                    eventValue.setTo(date);
                    eventValue.setEmp(Integer.parseInt(Prefs.getString(Globals.EmployeeID,"")));
                    eventValue.setCreateTime(Globals.getTCurrentTime());
                    eventValue.setCreateDate(Globals.getTodaysDate());
                    eventValue.setType("Task");
                    eventValue.setParticipants("");
                    eventValue.setComment("");
                    eventValue.setSubject("");
                    eventValue.setTime(Globals.getTCurrentTime());
                    eventValue.setDocument("");
                    eventValue.setRelatedTo("");
                    eventValue.setLocation(location);
                    eventValue.setHost(host);
                    eventValue.setAllday("");
                    eventValue.setName(newEvent.getName());
                    eventValue.setProgressStatus(progresstatus);
                    eventValue.setPriority(priority);
                    eventValue.setRepeated("");




                    if(Globals.checkInternet(getContext()))
                        callApi(eventValue);




             /* NewEvent event = new NewEvent(title,fromDate,toDate,true,"Repeate",location,
             host,"top",partcipant,desc,related,1,time);
              TaskEventList.add(event);*/

                    // saveData();


                }


                break;
            case R.id.add:
                setEnable();
                break;
        }
    }

    private void callApi(EventValue eventValue) {

        Call<EventResponse> call = NewApiClient.getInstance().getApiService().updateevent(eventValue);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if(response.code()==200)
                {

                    Toast.makeText(getContext(), "Posted Successfully.", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();

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



    private void setEnable() {
        add.setVisibility(View.GONE);
        ok.setVisibility(View.VISIBLE);
        title_text.setClickable(true);
        title_text.setFocusable(true);
        title_text.setFocusableInTouchMode(true);

        date_value.setClickable(true);
        date_value.setFocusable(true);

        settime.setFocusable(true);
        settime.setClickable(true);

        add_location_text.setClickable(true);
        add_location_text.setFocusableInTouchMode(true);
        add_location_text.setFocusable(true);

        host_text.setFocusable(true);
        host_text.setClickable(true);
        host_text.setFocusableInTouchMode(true);

        description_text.setFocusable(true);
        description_text.setClickable(true);
        description_text.setFocusableInTouchMode(true);

        colorspin.setEnabled(true);
        preority_spinner.setEnabled(true);

        submit_button.setVisibility(View.VISIBLE);
    }

    private void setAlarm()
         {
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getContext(), NotificationPublisher.class);
        i.putExtra("value",getActivity().getResources().getString(R.string.task_notification));
        i.putExtra("title",getActivity().getResources().getString(R.string.ur_task));
        final int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),id,i,PendingIntent.FLAG_ONE_SHOT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,myTime.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

        }


   }
