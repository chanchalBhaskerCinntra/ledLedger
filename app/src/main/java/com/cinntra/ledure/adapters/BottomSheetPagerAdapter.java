package com.cinntra.ledure.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class BottomSheetPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> fragmentList;

    private String mSearchTerm;
    public BottomSheetPagerAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> fragmentList) {
    super(fm);
    this.fragmentList = fragmentList;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount()
      {
    return fragmentList.size();
      }

}
