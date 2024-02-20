package com.cinntra.ledure.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cinntra.ledure.databinding.ActivityForgetMpinactivityBinding;
import com.cinntra.ledure.globals.Globals;
import com.pixplicity.easyprefs.library.Prefs;

public class ForgotMPINActivity extends AppCompatActivity {

    private ActivityForgetMpinactivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetMpinactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.ivBackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEditTexts(binding.mpinEditText, binding.confirmMpinEditText)) {
                    Prefs.putString(Globals.MPIN_VALUE, binding.mpinEditText.getText().toString().trim());
                    finish();
                    Toast.makeText(ForgotMPINActivity.this, "Changed Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ForgotMPINActivity.this, "Please enter MPIN correctly", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private boolean validateEditTexts(EditText editText1, EditText editText2) {
        String text1 = editText1.getText().toString().trim();
        String text2 = editText2.getText().toString().trim();

        if (TextUtils.isEmpty(text1) || TextUtils.isEmpty(text2)) {
            // If any of the EditTexts is empty, return false

            return false;
        }

        // If both EditTexts are not empty, compare their contents
        return text1.equals(text2);
    }
}