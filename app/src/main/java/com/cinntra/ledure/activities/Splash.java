package com.cinntra.ledure.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cinntra.ledure.R;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.LogInDetail;
import com.cinntra.ledure.model.NewLogINResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Splash extends AppCompatActivity {

    private boolean isFirstAnimation = false;

    @BindView(R.id.header_icon)
    ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Animation hold = AnimationUtils.loadAnimation(this, R.anim.hold);

        final Animation translateScale = AnimationUtils.loadAnimation(this, R.anim.translate_scale);
        final ImageView imageView = findViewById(R.id.header_icon);
        Glide.with(this)
                .asGif()
                .load(R.drawable.gif_ledure_logo) // Replace with your GIF resource
                // Replace with a placeholder image
                .into(imageView);


//        translateScale.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//                if (!isFirstAnimation) {
//                    imageView.clearAnimation();
//
//
//                }
//
//
//                isFirstAnimation = true;
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//        hold.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
////                imageView.clearAnimation();
////                imageView.startAnimation(translateScale);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//
//        imageView.startAnimation(hold);


        // Create a Handler
        Handler handler = new Handler();

        // Define the delay (5 seconds = 5000 milliseconds)
        int delayMillis = 3000;

        // Post a delayed action to start the new activity
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Prefs.getString(Globals.USERNAME, "").isEmpty() && Prefs.getString(Globals.USER_PASSWORD, "").isEmpty())
                    AutoLogIn = false;
                callLogInApi(Prefs.getString(Globals.USERNAME, ""), Prefs.getString(Globals.USER_PASSWORD, ""));


            }
        }, delayMillis);


    }


    private void callLogInApi(String username, String password) {

        LogInDetail logInDetail = new LogInDetail();
        logInDetail.setUserName(username);
        logInDetail.setPassword(password);
        logInDetail.setFcm("");
        Prefs.putString(Globals.USER_PASSWORD, password);

        Call<NewLogINResponse> call = NewApiClient.getInstance().getApiService().loginEmployee(logInDetail);
        call.enqueue(new Callback<NewLogINResponse>() {
            @Override
            public void onResponse(Call<NewLogINResponse> call, Response<NewLogINResponse> response) {

                if (response.body().getStatus() == 200) {


                    Gson gson = new Gson();
                    String json = gson.toJson(response.body().getLogInDetail());
                    Prefs.putString(Globals.AppUserDetails, json);
                    // Globals.APILog = "APILog";
                    Globals.TeamSalesEmployeCode = response.body().getLogInDetail().getSalesEmployeeCode();
                    Globals.TeamRole = response.body().getLogInDetail().getRole();
                    Globals.TeamEmployeeID = String.valueOf(response.body().getLogInDetail().getId());
                    Globals.SelectedDB = String.valueOf(response.body().getSap().getCompanyDB());

                    Prefs.putString(Globals.USER_PASSWORD, response.body().getLogInDetail().getPassword());
                    Prefs.putString(Globals.Employee_Name, response.body().getLogInDetail().getUserName());
                    Prefs.putString(Globals.CHECK_IN_STATUS, response.body().getLogInDetail().getCheckInStatus());
                    Prefs.putString(Globals.EmployeeID, String.valueOf(response.body().getLogInDetail().getId()));
                    Prefs.putString(Globals.SalesEmployeeCode, String.valueOf(response.body().getLogInDetail().getSalesEmployeeCode()));
                    Prefs.putString(Globals.SalesEmployeeName, String.valueOf(response.body().getLogInDetail().getSalesEmployeeName()));
                    Prefs.putString(Globals.SelectedDB, String.valueOf(response.body().getSap().getCompanyDB()));
                    Prefs.putString(Globals.Role, String.valueOf(response.body().getLogInDetail().getRole()));
                    Prefs.putString(Globals.MyID, String.valueOf(response.body().getLogInDetail().getId()));
                    Prefs.putString(Globals.BranchId, String.valueOf(response.body().getLogInDetail().getBranch()));
                    Prefs.putString(Globals.ZONE, String.valueOf(response.body().getLogInDetail().getZone()));

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

                    //Prefs.putString(Globals.MYEmployeeID, String.valueOf(response.body().getLogInDetail().getId()));

                    long session = Long.parseLong("30");
                    session = session * 60 * 1000;

                    Prefs.putLong(Globals.SESSION_TIMEOUT, session);
                    Prefs.putLong(Globals.SESSION_REMAIN_TIME, 0);
                    AutoLogIn = true;
                    gotoHome();
                } else {


                }
            }

            @Override
            public void onFailure(Call<NewLogINResponse> call, Throwable t) {
                AutoLogIn = false;
                gotoHome();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    private static final String TAG = "Splash";
    private boolean AutoLogIn = false;

    private void gotoHome() {


        if (!AutoLogIn) {
            Intent intent = new Intent(Splash.this, Login.class);
            startActivity(intent);
            finish();


        } else {
            showMpinPopup();


        }

    }


    private void showMpinPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this, R.style.CustomAlertDialog);
        View dialogView = LayoutInflater.from(Splash.this).inflate(R.layout.enter_mpin_custom_popup_alert, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(layoutParams);

        dialog.show();


        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        OtpTextView otpView = dialogView.findViewById(R.id.otp_view);
        TextView tvForgotMpin = dialogView.findViewById(R.id.tvForgotMpin);

        tvForgotMpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(Splash.this, ForgotMPINActivity.class));
            }
        });

        otpView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // Fired when user types something in the Otpbox
            }

            @Override
            public void onOTPComplete(String otp) {
                if (Prefs.getString(Globals.MPIN_VALUE, "").equalsIgnoreCase(otp)) {
                    dialog.dismiss();
                    Intent i = new Intent(Splash.this, MainActivity_B2C.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                    Toast.makeText(Splash.this, "Verified Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Splash.this, "Please Enter Correct Pin", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}