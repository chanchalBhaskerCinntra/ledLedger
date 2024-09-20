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


public class Calender extends Fragment implements DatePickerListener, View.OnClickListener {
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
    Context context;

    @Override
    public void onResume() {
        super.onResume();

        if(pagerAdapter!=null)
            pagerAdapter.notifyDataSetChanged();
    }

    public Calender() {
        // Required empty public constructor
    }


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
        View v=inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this,v);
        Globals.CURRENT_CLASS = getClass().getName();
        setDefaults();
        setcalender();

        return v;

    }



    private  OrderPagerAdapter pagerAdapter;
    ArrayList<Fragment> fragmentList =new  ArrayList<Fragment>();
    private String tabs[] = {"All","Events","Tasks","Follow Up"};
    private void setDefaults() {
        fragmentList.clear();
        fragmentList.add(new All_Event_Task_Fragment());
        fragmentList.add(new Event_Fragment());
        fragmentList.add(new Tasks_Fragment());
        fragmentList.add(new Followup_Fragment());

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
                if(currentItem==0 || currentItem==3)
                    add_new.setVisibility(View.GONE);
                else
                    add_new.setVisibility(View.VISIBLE);



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
      //  picker.setDateSelectedColor(getResources().getColor(R.color.yellow));
        Globals.CurrentSelectedDate = dateSelected.getYear()+"-"+month+"-"+dd;
//        Log.e("Date=>C",Globals.CurrentSelectedDate);
      //  callApi();
        viewpager.getAdapter().notifyDataSetChanged();
    }



    private void showTaskDialog() {
        Bundle b = new Bundle();
        b.putParcelable(Globals.OpportunityItem,opportunityItem);
        FragmentManager fm = getChildFragmentManager();
        AddTaskDialogue editNameDialogFragment = AddTaskDialogue.newInstance("Some Title");
        editNameDialogFragment.setArguments(b);
        editNameDialogFragment.show(fm, "");
    }
    private void showEventDialog() {
        Bundle b = new Bundle();
        b.putParcelable(Globals.OpportunityItem,opportunityItem);
        FragmentManager fm = getChildFragmentManager();
        AddEventDialogue editNameDialogFragment = AddEventDialogue.newInstance("Some Title");
        editNameDialogFragment.setArguments(b);
        editNameDialogFragment.show(fm, "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.add_new:
                if(currentItem==1)
                showEventDialog();
                else if (currentItem == 2)
                showTaskDialog();
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