package com.cinntra.ledure.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.Login;
import com.cinntra.ledure.databinding.SettingPageBinding;
import com.pixplicity.easyprefs.library.Prefs;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SettingFragment extends Fragment {

    SettingPageBinding settingPageBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        settingPageBinding = SettingPageBinding.inflate(getLayoutInflater());
        settingPageBinding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });

        settingPageBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfirmDialog();
            }
        });


        return settingPageBinding.getRoot();

    }

    private void openConfirmDialog() {

        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You Want to Logout!")
                .setConfirmText("Yes,LogOut!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Prefs.clear();
                        Intent intent = new Intent(getActivity(), Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                })

                .show();

    }
}
