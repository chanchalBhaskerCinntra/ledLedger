package com.cinntra.ledure.globals;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.cinntra.ledure.activities.Login;
import com.cinntra.ledure.model.LogInDetail;
import com.cinntra.ledure.model.NewLogINResponse;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pixplicity.easyprefs.library.Prefs;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainBaseActivity extends AppCompatActivity {
   public static boolean boolean_permission = false;
    private static final int REQUEST_PERMISSIONS = 100;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fn_permission();


        /******For Running in background********/




    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
    private void fn_permission()
      {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainBaseActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION))) {


            } else {
                ActivityCompat.requestPermissions(MainBaseActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION

                        },
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;
        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean_permission = true;

                } else {
                    Toast.makeText(getApplicationContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

                }
            }
        }
    }

    private void givepermission()
            {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                })
                .onSameThread()
                .check();
    }

    private void checkTimer(long session_time)
            {
         MyApp.timer = new CountDownTimer(session_time, 5000) {
         public void onTick(long millisUntilFinished)
            {
          long spend_time =  Prefs.getLong(Globals.SESSION_REMAIN_TIME,5000)+5000;
          Prefs.putLong(Globals.SESSION_REMAIN_TIME,spend_time);
             }

            public void onFinish() {
                setReloginAlert();

            }
        }.start();
    }

    private void setReloginAlert()
            {



    AlertDialog.Builder ab = new AlertDialog.Builder(MainBaseActivity.this);
    ab.setTitle("Session");
    ab.setMessage("Your Session Expired!");

                ab.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     Globals.APILog = "APILog";

                     String uName = Prefs.getString(Globals.USER_NAME,"");
                     String uPass = Prefs.getString(Globals.USER_PASSWORD,"");
                     LogInDetail logInDetail = new LogInDetail();
                     logInDetail.setUserName(uName);
                     logInDetail.setPassword(uPass);
                     logInDetail.setFcm("");
                     userLogin(logInDetail);
                    }
                });
         ab.setNegativeButton("LogOut", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
         //  Toast.makeText(MainBaseActivity.this, "Testing Relogin.", Toast.LENGTH_SHORT).show();

           Prefs.putLong(Globals.SESSION_REMAIN_TIME,0);
           Intent intent = new Intent(MainBaseActivity.this,
           Login.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
           startActivity(intent);
           finish();
             }
         });
        ab.show();

    }


    private void userLogin(LogInDetail in)
             {
        Call<NewLogINResponse> call = NewApiClient.getInstance().getApiService().loginEmployee(in);
        call.enqueue(new Callback<NewLogINResponse>() {
            @Override
            public void onResponse(Call<NewLogINResponse> call, Response<NewLogINResponse> response) {
                if(response.code()==200) {

                    long session = Long.parseLong("30");
                    session = session*60*1000;

                    Prefs.putLong(Globals.SESSION_TIMEOUT,session);
                    Prefs.putLong(Globals.SESSION_REMAIN_TIME,0);


                    long session_time = Prefs.getLong(Globals.SESSION_TIMEOUT, 2000);
                    long spend_time = Prefs.getLong(Globals.SESSION_REMAIN_TIME, 1000);
                    session_time = session_time - spend_time;
                    checkTimer(session_time);


                }
                else {
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, QuotationResponse.class);
                        Toast.makeText(MainBaseActivity.this, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        // handle failure to read error
                    }
                }

            }
            @Override
            public void onFailure(Call<NewLogINResponse> call, Throwable t) {

                Toast.makeText(MainBaseActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
