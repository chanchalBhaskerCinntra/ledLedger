package com.cinntra.ledure.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.OrderPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Add_Item_Fragment extends Fragment implements View.OnClickListener {

    String tabs[] = {"Items","Recommendation"};

    ArrayList<Fragment> fragmentList =new  ArrayList<Fragment>();

    OrderPagerAdapter pagerAdapter;

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.save)
    TextView save;

    public Add_Item_Fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Add_Item_Fragment newInstance(String param1, String param2) {
        Add_Item_Fragment fragment = new Add_Item_Fragment();
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
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_edit_item_add, container, false);
        ButterKnife.bind(this,v);

        setDefaults();


        return v;
    }

    private void setDefaults() {
        save.setVisibility(View.GONE);
        head_title.setText(getActivity().getString(R.string.items));
        fragmentList.add(new Item_Fragment());
        fragmentList.add(new Item_Recommendation_Fragment());

        back_press.setOnClickListener(this);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pagerAdapter = new OrderPagerAdapter(getChildFragmentManager(),fragmentList,tabs);
         viewpager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
    }

    @Override
    public void onClick(View v) {
     switch (v.getId()){
     case R.id.back_press:
     getActivity().onBackPressed();

        }
    }
}