package com.cinntra.ledure.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cinntra.ledure.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DelieveryDetails extends Fragment implements  View.OnClickListener {
    @BindView(R.id.checkbox1)
    CheckBox checkbox;
    @BindView(R.id.edit_ship_type)
    ImageView edit_ship_type;
    @BindView(R.id.edit_ship)
    ImageView edit_ship;
    @BindView(R.id.ship_mode_edit)
    ImageView ship_mode_edit;
    @BindView(R.id.head_title)
    TextView head_title;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.track_deleivery_detail, container, false);
        ButterKnife.bind(this,v);
        setDefaults();
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    edit_ship.setVisibility(View.VISIBLE);
                    edit_ship_type.setVisibility(View.VISIBLE);
                    ship_mode_edit.setVisibility(View.VISIBLE);
                }else{
                    edit_ship.setVisibility(View.GONE);
                    edit_ship_type.setVisibility(View.GONE);
                    ship_mode_edit.setVisibility(View.GONE);
                }
            }
        });


        return v;
    }

    private void setDefaults() {
        head_title.setText(R.string.delivery_detail);
        ship_mode_edit.setOnClickListener(this);
        edit_ship.setOnClickListener(this);
        edit_ship_type.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ship_mode_edit:
                ship_mode_dialog();
                break;
            case R.id.edit_ship:
                ship_edit_dialog();
                break;
            case R.id.edit_ship_type:
                ship_type_diualog();
                break;
        }
    }

    private void ship_type_diualog() {
        Dialog dialog = new Dialog(getContext());
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View custom_dialog =layoutInflater.inflate(R.layout.shipping_type_alertbox,null);
        dialog.setContentView(custom_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        //dialog.setTitle("");
        dialog.getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void ship_mode_dialog() {
        Dialog dialog = new Dialog(getContext());
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View custom_dialog =layoutInflater.inflate(R.layout.shipped_with_alertbox,null);
        dialog.setContentView(custom_dialog);
        //dialog.setTitle("");
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void ship_edit_dialog() {
        Dialog dialog = new Dialog(getContext());
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View custom_dialog =layoutInflater.inflate(R.layout.shipping_address_alertbox,null);
        dialog.setContentView(custom_dialog);
        //dialog.setTitle("");
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
