package com.cinntra.ledure.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.cinntra.ledure.databinding.ActivityShowCheckInImageBinding;
import com.cinntra.ledure.globals.Globals;

public class ShowCheckInImageActivity extends AppCompatActivity {
    ActivityShowCheckInImageBinding binding;
    String image,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityShowCheckInImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        image=getIntent().getStringExtra("image");
        binding.tvTitleOfImage.setText("   "+getIntent().getStringExtra("name"));
        binding.tvTitleOfImage.setOnClickListener(view -> {
            finish();
        });

        Glide.with(this).load(Globals.attachmentBaseUrl+image).into(binding.ivImageCHeck);




    }
}