package com.cinntra.ledure.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cinntra.ledure.R;
import com.cinntra.ledure.databinding.ActivityOtpBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.SessionManagement;
import com.cinntra.ledure.model.NewLogINResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.HashMap;
import java.util.Objects;

import in.aabhasjindal.otptextview.OTPListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {

    ActivityOtpBinding binding ;

    SessionManagement sessionManagement;

    String OTPval = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManagement = new SessionManagement(this);

        getSupportActionBar().hide();

        binding.spinKitLoader.setVisibility(View.GONE);

        binding.tvMobileNo.setText(sessionManagement.getMobileNO());


        binding.verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("OTP", OTPval);
                hashMap.put("mobile", sessionManagement.getMobileNO());
                verifyOtpApi(hashMap);
            }
        });


        binding.otpView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {}

            @Override
            public void onOTPComplete(String otp) {
                OTPval = otp;
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("OTP", OTPval);
                hashMap.put("mobile", sessionManagement.getMobileNO());
                verifyOtpApi(hashMap);

            }
        });


        binding.ivBackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }



    private void verifyOtpApi(HashMap<String, String> hashMap) {
        binding.spinKitLoader.setVisibility(View.VISIBLE);

        Call<NewLogINResponse> call = NewApiClient.getInstance().getApiService(this).getVerifyOtpApi(hashMap);
        call.enqueue(new Callback<NewLogINResponse>() {
            @Override
            public void onResponse(Call<NewLogINResponse> call, Response<NewLogINResponse> response) {
                try {
                    if (response.body().getStatus() == 200) {
                        binding.spinKitLoader.setVisibility(View.GONE);

                        sessionManagement.setCardCode(response.body().getLogInDetail().get(0).getCard_code());
                        sessionManagement.setCardName(response.body().getLogInDetail().get(0).getCard_name());
                        Prefs.putString(Globals.SalesEmployeeCode, response.body().getLogInDetail().get(0).getSalesEmployeeCode());

                        showMpinRegistrationProcessPopup(response);

                    } else if (response.body().getStatus() == 401) {
                        Prefs.clear();
                        Globals.logoutScreen(OtpActivity.this);
                    } else {
                        binding.spinKitLoader.setVisibility(View.GONE);

                        Toast.makeText(OtpActivity.this, "Warning "+ response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                    binding.spinKitLoader.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<NewLogINResponse> call, Throwable t) {

                binding.spinKitLoader.setVisibility(View.GONE);
                Toast.makeText(OtpActivity.this, "Error "+ t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    String android_ID = "";

    private void showMpinRegistrationProcessPopup(Response<NewLogINResponse> response) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OtpActivity.this, R.style.CustomAlertDialog);
        View dialogView = LayoutInflater.from(OtpActivity.this).inflate(R.layout.dialog_setup_mpin, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(layoutParams);

        dialog.show();

        android_ID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        EditText etMpin = dialogView.findViewById(R.id.mpinEditText);
        EditText etReMpin = dialogView.findViewById(R.id.confirmMpinEditText);
        Button btnContinue = dialogView.findViewById(R.id.continueBtn);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEditTexts(etMpin, etReMpin)) {

                    sessionManagement.setMPINValue(etMpin.getText().toString());

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("type", "login");
                    hashMap.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                    hashMap.put("device_id", android_ID);
                    hashMap.put("device_name", "bizz");
                    hashMap.put("timestamp", Globals.getTimestamp());

                    callMPINApi(hashMap, btnContinue, etMpin, dialog);

                }

                else {
                    Toast.makeText(OtpActivity.this, "Please Enter MPIN Correctly", Toast.LENGTH_SHORT).show();
                }
            }
        });


        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
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


    private void callMPINApi(HashMap<String, String> hashMap, Button btnContinue, EditText etMpin, AlertDialog dialog) {
        binding.spinKitLoader.setVisibility(View.VISIBLE);

        Call<NewLogINResponse> call = NewApiClient.getInstance().getApiService(this).callMPINApi(hashMap);
        call.enqueue(new Callback<NewLogINResponse>() {
            @Override
            public void onResponse(Call<NewLogINResponse> call, Response<NewLogINResponse> response) {
                try {
                    if (response.body().getStatus() == 200) {

                        dialog.dismiss();

                        binding.spinKitLoader.setVisibility(View.GONE);
                        btnContinue.setEnabled(false);
                        btnContinue.setClickable(false);

                        Globals.APILog = "APILog";


                        sessionManagement.setToken(response.body().getLogInDetail().get(0).getToken());
                        sessionManagement.setSalesEmployeeCode(response.body().getLogInDetail().get(0).getSalesEmployeeCode());
                        sessionManagement.setFromWhere("ElseCase");


                        //todo mpinPrefs
                        Prefs.putString(Globals.MPIN_VALUE, etMpin.getText().toString().trim());


                        Prefs.putString(Globals.TOKEN, response.body().getLogInDetail().get(0).getToken());

                        Prefs.putString(Globals.USERNAME, sessionManagement.getMobileNO());
//                    Prefs.putString(Globals.USER_PASSWORD, login_password.getText().toString().trim());
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body().getLogInDetail());
                        Prefs.putString(Globals.AppUserDetails, json);
                        // Globals.APILog = "APILog";
                        Globals.TeamSalesEmployeCode = response.body().getLogInDetail().get(0).getSalesEmployeeCode();
                        Globals.TeamRole = response.body().getLogInDetail().get(0).getRole();
                        Globals.TeamEmployeeID = String.valueOf(response.body().getLogInDetail().get(0).getId());
//                        Globals.SelectedDB = String.valueOf(response.body().getSap().getCompanyDB());

                        Prefs.putString(Globals.USER_PASSWORD, response.body().getLogInDetail().get(0).getPassword());
                        Prefs.putString(Globals.Employee_Name, response.body().getLogInDetail().get(0).getUserName());
                        Prefs.putString(Globals.CHECK_IN_STATUS, response.body().getLogInDetail().get(0).getCheckInStatus());
                        Prefs.putString(Globals.EmployeeID, String.valueOf(response.body().getLogInDetail().get(0).getId()));
                        Prefs.putString(Globals.SalesEmployeeCode, String.valueOf(response.body().getLogInDetail().get(0).getSalesEmployeeCode()));
                        Prefs.putString(Globals.SalesEmployeeName, String.valueOf(response.body().getLogInDetail().get(0).getSalesEmployeeName()));
//                        Prefs.putString(Globals.SelectedDB, String.valueOf(response.body().getSap().getCompanyDB()));
                        Prefs.putString(Globals.Role, String.valueOf(response.body().getLogInDetail().get(0).getRole()));
                        Prefs.putString(Globals.MyID, String.valueOf(response.body().getLogInDetail().get(0).getId()));
                        Prefs.putString(Globals.BranchId, String.valueOf(response.body().getLogInDetail().get(0).getBranch()));
                        Prefs.putString(Globals.ZONE, String.valueOf(response.body().getLogInDetail().get(0).getZone()));
                        Prefs.putString(Globals.ADDRESS_LOGIN, String.valueOf(response.body().getLogInDetail().get(0).getAddress()));


                        if (response.body().getTripExpenses().size() > 0) {
                            Prefs.putString(Globals.BP_TYPE_CHECK_IN, response.body().getTripExpenses().get(0).getBPType());
                            Prefs.putString(Globals.BP_NAME_CHECK_IN, response.body().getTripExpenses().get(0).getBPName());
                            Prefs.putDouble(Globals.START_LAT, Double.parseDouble(response.body().getTripExpenses().get(0).getCheckInLat()));
                            Prefs.putDouble(Globals.START_LONG, Double.parseDouble(response.body().getTripExpenses().get(0).getCheckInLong()));
                            Prefs.putString(Globals.START_DATE, response.body().getTripExpenses().get(0).getCheckInDate());
                            Prefs.putString(Globals.MODE_OF_TRANSPORT, response.body().getTripExpenses().get(0).getModeOfTransport());
                            Prefs.putString(Globals.TRIP_ID, response.body().getTripExpenses().get(0).getId());
                        } else {

                        }


                        long session = Long.parseLong("30");
                        session = session * 60 * 1000;

                        Prefs.putLong(Globals.SESSION_TIMEOUT, session);
                        Prefs.putLong(Globals.SESSION_REMAIN_TIME, 0);

                        gotoHome();
                        Toast.makeText(OtpActivity.this, "MPIN Created SuccessFully Successfully", Toast.LENGTH_SHORT).show();


                     /*   Intent intent = new Intent(OtpActivity.this, MainActivity_B2C.class);
                        startActivity(intent);*/


                    } else if (response.body().getStatus() == 401) {
                        Prefs.clear();
                        Globals.logoutScreen(OtpActivity.this);
                    } else if (response.body().getStatus() == 400) {
                        btnContinue.setEnabled(true);
                        btnContinue.setClickable(true);
                        binding.spinKitLoader.setVisibility(View.GONE);
                        Toast.makeText(OtpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else if (response.body().getStatus() == 404) {
                        btnContinue.setEnabled(true);
                        btnContinue.setClickable(true);
                        binding.spinKitLoader.setVisibility(View.GONE);
                        Toast.makeText(OtpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        btnContinue.setEnabled(true);
                        btnContinue.setClickable(true);
                        binding.spinKitLoader.setVisibility(View.GONE);

                        Toast.makeText(OtpActivity.this, "Warning "+ response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                    binding.spinKitLoader.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<NewLogINResponse> call, Throwable t) {

                binding.spinKitLoader.setVisibility(View.GONE);
                Toast.makeText(OtpActivity.this, "Error "+ t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void gotoHome() {
        // Intent i = new Intent(Login.this, MainActivity.class);
        Intent i = new Intent(OtpActivity.this, MainActivity_B2C.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

        finish();
    }



}