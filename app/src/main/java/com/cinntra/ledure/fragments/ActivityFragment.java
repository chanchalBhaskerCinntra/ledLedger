package com.cinntra.ledure.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.OrderPagerAdapter;
import com.cinntra.ledure.calender.DatePickerListener;
import com.cinntra.ledure.calender.HorizontalPicker;
import com.cinntra.ledure.globals.Globals;

import com.cinntra.ledure.model.EventValue;
import com.cinntra.ledure.newapimodel.NewOpportunityRespose;
import com.google.android.material.tabs.TabLayout;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ActivityFragment extends Fragment implements DatePickerListener, View.OnClickListener {
    public static  int currentItem =0;

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tab_layout)
    TabLayout tableLayout;
    @BindView(R.id.add_new)
    LinearLayout add_new;

    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.datePicker)
    HorizontalPicker picker;
    NewOpportunityRespose opportunityItem;

    ArrayList<EventValue> alleventlist = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();

        if(pagerAdapter!=null)
            pagerAdapter.notifyDataSetChanged();
    }

    public ActivityFragment() {
        // Required empty public constructor
    }
    Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;

        super.onAttach(context);

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b      = getArguments();
            opportunityItem =(NewOpportunityRespose) b.getParcelable(Globals.OpportunityItem);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.activityfragment_notifications, container, false);
        ButterKnife.bind(this,v);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        head_title.setText(getString(R.string.activity));
        setDefaults();
        setcalender();

        return v;

    }



    private  OrderPagerAdapter pagerAdapter;
    ArrayList<Fragment> fragmentList =new  ArrayList<Fragment>();
    private String tabs[] = {"Events","Tasks"};
    private void setDefaults() {
        fragmentList.clear();
        fragmentList.add(new ActivityEvent_Fragment(opportunityItem.getId()));
        fragmentList.add(new ActivityTasks_Fragment(opportunityItem.getId()));
      //  fragmentList.add(new Log_Fragment());

        pagerAdapter = new OrderPagerAdapter(getChildFragmentManager(),fragmentList,tabs);
        viewpager.setAdapter(pagerAdapter);
        tableLayout.setupWithViewPager(viewpager);
        add_new.setOnClickListener(this);
        back_press.setOnClickListener(this);
        add_new.setVisibility(View.VISIBLE);

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentItem = tab.getPosition();
               /* if(currentItem==0)
                    add_new.setVisibility(View.GONE);
                else
                    add_new.setVisibility(View.VISIBLE);*/



            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void setcalender() {

        // initialize it and attach a listener
        picker .setListener(this).setDays(2000).setOffset(500)
                .init();
        picker.setDate(new DateTime());
    }






    @Override
    public void onDateSelected(DateTime dateSelected) {

        int mn = dateSelected.getMonthOfYear();
        DecimalFormat formatter = new DecimalFormat("00");
        String month = formatter.format(mn);

        int day =dateSelected.getDayOfMonth();
        DecimalFormat formatter1 = new DecimalFormat("00");
        String dd = formatter1.format(day);

        Globals.CurrentSelectedDate = dateSelected.getYear()+"-"+month+"-"+dd;
        Log.e("Date=>C",Globals.CurrentSelectedDate);
        viewpager.getAdapter().notifyDataSetChanged();
    }


    private void showTaskDialog() {
        Bundle b = new Bundle();
        b.putParcelable(Globals.OpportunityItem,opportunityItem);
        FragmentManager fm = getChildFragmentManager();
        ActivityAddTaskDialogue editNameDialogFragment = ActivityAddTaskDialogue.newInstance("Some Title");
        editNameDialogFragment.setArguments(b);
        editNameDialogFragment.show(fm, "");
    }
    private void showEventDialog() {
        Bundle b = new Bundle();
        b.putParcelable(Globals.OpportunityItem,opportunityItem);
        FragmentManager fm = getChildFragmentManager();
        ActivityAddEventDialogue editNameDialogFragment = ActivityAddEventDialogue.newInstance("Some Title");
        editNameDialogFragment.setArguments(b);
        editNameDialogFragment.show(fm, "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.add_new:
                if(currentItem==0)
                    showEventDialog();
                else if(currentItem==1)
                    showTaskDialog();
                else if (currentItem == 2)
                    showLogDialog();
                break;
            case R.id.back_press:
                getActivity().onBackPressed();
                break;
        }
    }

    private void showLogDialog() {
        FragmentManager fm = getChildFragmentManager();
        AddLogDialogue editNameDialogFragment = AddLogDialogue.newInstance("Some Title");
        editNameDialogFragment.show(fm, "");
    }
}