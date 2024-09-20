package com.cinntra.ledure.fragments;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cinntra.ledure.R;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.EventResponse;
import com.cinntra.ledure.model.EventValue;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.receivers.NotificationPublisher;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivityEventDetailFragment extends Fragment implements  View.OnClickListener {

    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    TextView pass_date;
    @BindView(R.id.from_value)
    EditText from_date_value;
    @BindView(R.id.related_document_value)
    EditText related_document_value;
    @BindView(R.id.description_text)
    EditText description_text;
    @BindView(R.id.host_text)
    EditText host_text;
    @BindView(R.id.title_text)
    EditText title_text;
    @BindView(R.id.add_location_text)
    EditText add_location_text;
    @BindView(R.id.participant_value)
    EditText participant_value;
    @BindView(R.id.to_value)
    EditText to_date_value;
    @BindView(R.id.submit_button)
    Button submit;
    @BindView(R.id.upload_button)
    Button upload_button;
    @BindView(R.id.simple_switch)
    Switch simple_switch;
    @BindView(R.id.spinner)
    Spinner preority_spinner;
    @BindView(R.id.color_spinner)
    Spinner colorspin;
    @BindView(R.id.time_value)
    TextView time_value;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.ok)
    ImageView ok;
    EventValue newEvent;

    String priority = "";
    String allday = "";
    String repeated = "";
    private AlarmManager alarmManager;
    private Calendar myTime;
    EventValue setEventData = new EventValue();
    int t1hr,t1min;
    EventPrerioritySpinner eventPrerioritySpinner;
    EventTextSpinner eventTextSpinner;

    ArrayList<String> categories   = new ArrayList<>();
    ArrayList<Integer> circleimage = new ArrayList<>();
    final Calendar myCalendar      = Calendar.getInstance();

    public UpdateActivityEventDetailFragment() {

    }


    @Override
    public void onDetach() {
        super.onDetach();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    // TODO: Rename and change types and number of parameters
    public static UpdateActivityEventDetailFragment newInstance(String param1) {
        UpdateActivityEventDetailFragment fragment = new UpdateActivityEventDetailFragment();
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

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_event, container, false);
        ButterKnife.bind(this, v);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        head_title.setText("Update Event");
        back_press.setOnClickListener(this);
        submit.setOnClickListener(this);
        ok.setOnClickListener(this);
        add.setOnClickListener(this);
        setDisable();
        setDefaults();
        if(Globals.checkInternet(getContext()))
        callApi(newEvent.getId());



        return v;
    }

    private void callApi(Integer eventValue) {
        EventValue event = new EventValue();
        event.setId(eventValue);
        Call<EventResponse> call = NewApiClient.getInstance().getApiService(getActivity()).particularevent(event);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if(response.code()==200)
                {

                    setEventData = response.body().getData().get(0);
                    setData(setEventData);
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

    private void setDefaults()
    {

        circleimage.add(R.drawable.red_dot);
        circleimage.add(R.drawable.ic_green_dot);
        circleimage.add(R.drawable.yellow_dot);
        categories.add("Repeat");
        categories.add("Daily");
        categories.add("Monthly");
        categories.add("Weekly");



        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(pass_date, myCalendar);
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


        from_date_value.setOnClickListener(v -> {
            from_date_value.getText().toString();
            if (!from_date_value.getText().toString().isEmpty()) {
                String value = from_date_value.getText().toString();
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
            pass_date = from_date_value;

        });

        to_date_value.setOnClickListener(v -> {
            if (to_date_value.getText().toString() != null && !to_date_value.getText().toString().isEmpty()) {
                String value = to_date_value.getText().toString();
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
            pass_date = to_date_value;

        });

        time_value.setOnClickListener(new View.OnClickListener() {
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
                        time_value.setText(DateFormat.format("hh:mm aa",myTime));
                        setAlarm();
                    }
                },12,0,false
                );
                timePickerDialog.updateTime(t1hr,t1min);
                timePickerDialog.show();

            }

        });



    }

    private void setData(EventValue setEventData) {
        title_text.setText(setEventData.getTitle());
        from_date_value.setText(setEventData.getFrom());
        to_date_value.setText(setEventData.getTo());
        time_value.setText(setEventData.getTime());
        add_location_text.setText(setEventData.getLocation());
        host_text.setText(setEventData.getHost());
        participant_value.setText(setEventData.getParticipants());
        description_text.setText(setEventData.getDescription());
        related_document_value.setText(setEventData.getRelatedTo());
        if(setEventData.getAllday().equals("All Day"))
            simple_switch.setChecked(true);
        else
            simple_switch.setChecked(false);

        preority_spinner.setSelection(getposition(categories,setEventData.getRepeated()));
        colorspin.setSelection(getpos(circleimage,setEventData.getPriority()));

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

    private int getpos(ArrayList<Integer> circleimage, String priority) {
        int pos = -1;
        for (Integer s : circleimage){
            if (priority.equals("high"))
                pos = circleimage.indexOf(s);
            else if (priority.equals("medium"))
                pos = circleimage.indexOf(s);
            else
                pos = circleimage.indexOf(s);
        }
        return pos;
    }

    private int getposition(ArrayList<String> categories, String repeated) {
        int pos = -1;
        for(String s : categories){
            if(s.equals(repeated)){
                return categories.indexOf(s);
            }
        }
        return pos;
    }

    private void updateLabel(TextView pass_date, Calendar myCalendar)
    {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        pass_date.setText(sdf.format(myCalendar.getTime()));
    }
    private void setDisable() {
        title_text.setClickable(false);
        title_text.setFocusable(false);
        title_text.setFocusableInTouchMode(false);

        from_date_value.setClickable(false);
        from_date_value.setFocusable(false);
        from_date_value.setFocusableInTouchMode(false);

        to_date_value.setFocusableInTouchMode(false);
        to_date_value.setFocusable(false);
        to_date_value.setClickable(false);
        time_value.setClickable(false);
        time_value.setFocusable(false);
        time_value.setFocusableInTouchMode(false);
        add_location_text.setClickable(false);
        add_location_text.setFocusableInTouchMode(false);
        add_location_text.setFocusable(false);

        host_text.setFocusable(false);
        host_text.setClickable(false);
        host_text.setFocusableInTouchMode(false);

        participant_value.setFocusable(false);
        participant_value.setClickable(false);
        participant_value.setFocusableInTouchMode(false);

        related_document_value.setFocusable(false);
        related_document_value.setClickable(false);
        related_document_value.setFocusableInTouchMode(false);

        description_text.setFocusable(false);
        description_text.setClickable(false);
        description_text.setFocusableInTouchMode(false);

        colorspin.setEnabled(false);
        preority_spinner.setEnabled(false);
        upload_button.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_press:
                getActivity().onBackPressed();
                break;
            case R.id.add:
                setEnable();
                break;
            case R.id.ok:
            case R.id.submit_button:
                String title = title_text.getText().toString().trim();
                String fromDate = from_date_value.getText().toString().trim();
                String toDate = to_date_value.getText().toString().trim();
                String location = add_location_text.getText().toString().trim();
                String host = host_text.getText().toString().trim();
                String partcipant = participant_value.getText().toString().trim();
                String desc = description_text.getText().toString().trim();
                String related = related_document_value.getText().toString().trim();
                String time = time_value.getText().toString().trim();


                if (validation(title, fromDate, toDate, location, host, partcipant, desc, related, time)) {
                    EventValue eventValue = new EventValue();
                    eventValue.setOppId(setEventData.getOppId());
                    eventValue.setTitle(title);
                    eventValue.setDescription(desc);
                    eventValue.setFrom(fromDate);
                    eventValue.setTo(toDate);
                    eventValue.setEmp(setEventData.getEmp());
                    eventValue.setCreateTime(Globals.getTCurrentTime());
                    eventValue.setCreateDate(Globals.getTodaysDate());
                    eventValue.setType("Event");
                    eventValue.setSourceType("");
                    eventValue.setParticipants(partcipant);
                    eventValue.setComment("");
                    eventValue.setSubject("");
                    eventValue.setTime(Globals.getTCurrentTime());
                    eventValue.setDocument("");
                    eventValue.setRelatedTo(related);
                    eventValue.setLocation(location);
                    eventValue.setHost(host);
                    eventValue.setAllday(allday);
                    eventValue.setName(setEventData.getName());
                    eventValue.setProgressStatus("WIP");
                    eventValue.setPriority(priority);
                    eventValue.setRepeated(repeated);
                    eventValue.setId(setEventData.getId());


                    if (Globals.checkInternet(getContext())){
                        callupdateApi(eventValue);
                        setDisable();
                    }

                }
        }
        }

    private void callupdateApi(EventValue eventValue) {

        Call<EventResponse> call = NewApiClient.getInstance().getApiService(getActivity()).updateevent(eventValue);
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                if(response.code()==200)
                {

                    Toast.makeText(getActivity(), "Updated Successfully.", Toast.LENGTH_SHORT).show();
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

    private boolean validation(String title, String fromDate, String toDate, String location,
                               String host, String partcipant, String desc, String related, String time) {
        if(title.isEmpty()){
            title_text.setError(getResources().getString(R.string.title_error));
            return false;
        }else if (fromDate.isEmpty()){
            from_date_value.setError(getResources().getString(R.string.fromdate_error));
            return false;
        }else if (toDate.isEmpty()){
            to_date_value.setError(getResources().getString(R.string.todate_error));
            return false;
        }else if (location.isEmpty()){
            add_location_text.setError(getResources().getString(R.string.location_error));
            return false;
        }else if (host.isEmpty()){
            host_text.setError(getResources().getString(R.string.host_error));
            return false;
        } else if (partcipant.isEmpty()){
            participant_value.setError(getResources().getString(R.string.participant_error));
            return false;
        }else if (desc.isEmpty()){
            description_text.setError(getResources().getString(R.string.description_error));
            return false;
        }else if (related.isEmpty()){
            related_document_value.setError(getResources().getString(R.string.related_toerror));
            return false;
        }else if(time.isEmpty()){
            time_value.setError(getResources().getString(R.string.time_error));
            return false;
        }
        return true;
    }


        private void setEnable () {
            add.setVisibility(View.GONE);
            ok.setVisibility(View.VISIBLE);
            title_text.setClickable(true);
            title_text.setFocusable(true);
            title_text.setFocusableInTouchMode(true);

            from_date_value.setClickable(true);
            from_date_value.setFocusable(true);

            to_date_value.setFocusable(true);
            to_date_value.setClickable(true);
            time_value.setClickable(true);
            time_value.setFocusable(true);

            add_location_text.setClickable(true);
            add_location_text.setFocusableInTouchMode(true);
            add_location_text.setFocusable(true);

            host_text.setFocusable(true);
            host_text.setClickable(true);
            host_text.setFocusableInTouchMode(true);

            participant_value.setFocusable(true);
            participant_value.setClickable(true);
            participant_value.setFocusableInTouchMode(true);

            related_document_value.setFocusable(true);
            related_document_value.setClickable(true);
            related_document_value.setFocusableInTouchMode(true);

            description_text.setFocusable(true);
            description_text.setClickable(true);
            description_text.setFocusableInTouchMode(true);

            colorspin.setEnabled(true);
            preority_spinner.setEnabled(true);
            upload_button.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
        }


        private void setAlarm () {

            alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent i = new Intent(getContext(), NotificationPublisher.class);
            i.putExtra("value", getActivity().getResources().getString(R.string.meeting_notification));
            i.putExtra("title", getActivity().getResources().getString(R.string.meeting));
            final int id = (int) System.currentTimeMillis();
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, i, PendingIntent.FLAG_ONE_SHOT);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, myTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        }


}
