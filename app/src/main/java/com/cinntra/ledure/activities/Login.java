package com.cinntra.ledure.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cinntra.ledure.R;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MyApp;
import com.cinntra.ledure.interfaces.DatabaseClick;
import com.cinntra.ledure.model.LogInDetail;
import com.cinntra.ledure.model.LogInRequest;
import com.cinntra.ledure.model.LogInResponse;
import com.cinntra.ledure.model.NewLogINResponse;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.webservices.APIsClient;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.recaptcha.Recaptcha;
import com.google.android.recaptcha.RecaptchaAction;
import com.google.android.recaptcha.RecaptchaTasksClient;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements View.OnClickListener, DatabaseClick, GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "Login";
    private Button signin;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar;
    @BindView(R.id.goto_reg)
    LinearLayout goto_reg;
    @BindView(R.id.sql_setting)
    RelativeLayout sql_setting;
    @BindView(R.id.login_username)
    EditText login_username;
    @BindView(R.id.login_password)
    EditText login_password;
    @BindView(R.id.register_here)
    TextView register_here;

    @BindView(R.id.rememberme)
    CheckBox rememberme;


    @BindView(R.id.checkBoxNOtARobot)
    CheckBox checkBoxNOtARobot;
    private AppCompatActivity activity;
    private String token = "";

    //  GoogleApiClient googleApiClient;


    //todo recaptcha
    @Nullable
    private RecaptchaTasksClient recaptchaTasksClient = null;


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (MyApp.timer != null) {
            MyApp.timer.cancel();
            MyApp.timer = null;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = Login.this;
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        overridePendingTransition(0, 0);
        View relativeLayout = findViewById(R.id.login_container);
        Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        relativeLayout.startAnimation(animation);

        FirebaseMessaging messaging = FirebaseMessaging.getInstance();
        messaging.getToken().addOnSuccessListener(s -> {

            if (Prefs.getString(Globals.TOKEN, "").isEmpty()) {
                token = s;
                Prefs.putString(Globals.TOKEN, s);
            }
        });

        progressBar.setVisibility(View.GONE);
        signin = findViewById(R.id.login_button);
        signin.setOnClickListener(this);
        goto_reg.setOnClickListener(this);
        sql_setting.setOnClickListener(this);
        register_here.setOnClickListener(this);

/*
        googleApiClient = new GoogleApiClient.Builder(Login.this).addApi(SafetyNet.API).addConnectionCallbacks(this).build();
        googleApiClient.connect();
        initializeRecaptchaClient();

        findViewById(R.id.checkBoxNOtARobot).setOnClickListener(this::executeLoginAction);
        findViewById(R.id.ivLogo).setOnClickListener(this::executeRedeemAction);*/

        checkBoxNOtARobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxNOtARobot.isChecked()) {
                /*    SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient, Globals.CAPTCHA_SITE_KEY)
                            .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                                @Override
                                public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                    Status status = recaptchaTokenResult.getStatus();
                                    if ((status != null) && status.isSuccess()) {
                                        Toast.makeText(Login.this, "Successfully Verified", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Log.e(TAG, "onResult: "+status.getStatusMessage());
                                    }
                                }
                            });*/
                    //  verifyGoogleReCAPTCHA();


                } else {
                    Toast.makeText(Login.this, "unSuccessfully", Toast.LENGTH_SHORT).show();

                }
            }
        });

     /*   checkBoxNOtARobot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(Login.this, "CHECKED", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Login.this, "UN_CHECKED", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        if (Prefs.getString(Globals.REMEMBER_ME, "").equalsIgnoreCase("rem")) {
            login_username.setText(Prefs.getString(Globals.USERNAME, ""));
            login_password.setText(Prefs.getString(Globals.USER_PASSWORD, ""));
            rememberme.setChecked(true);
        } else {
            login_username.setText("");
            login_password.setText("");
            rememberme.setChecked(false);
        }

        if (Prefs.getString(Globals.Employee_Name, "").isEmpty() && Prefs.getString(Globals.USER_PASSWORD, "").isEmpty()) {
            login_username.setText("");
            login_password.setText("");
        } else {
            login_username.setText(Prefs.getString(Globals.Employee_Name, ""));
            login_password.setText(Prefs.getString(Globals.USER_PASSWORD, ""));
        }
        login_username.setText("");
        login_password.setText("");
    }


    private void initializeRecaptchaClient() {
        Recaptcha
                .getTasksClient(getApplication(), Globals.CAPTCHA_SITE_KEY)
                .addOnSuccessListener(
                        this,
                        new OnSuccessListener<RecaptchaTasksClient>() {
                            @Override
                            public void onSuccess(RecaptchaTasksClient client) {
                                Login.this.recaptchaTasksClient = client;
                                Log.e(TAG, "onSuccess: " + client.toString());

                            }
                        })
                .addOnFailureListener(
                        this,
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle communication errors ...
                                // See "Handle communication errors" section
                                Log.e(TAG, "onFailure: " + e.getMessage());
                            }
                        });
    }

    private void executeLoginAction(View v) {
        assert recaptchaTasksClient != null;
        recaptchaTasksClient
                .executeTask(RecaptchaAction.LOGIN)
                .addOnSuccessListener(
                        this,
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String token) {
                                Log.e(TAG, "onSuccess: " + token);
                                // Handle success ...
                                // See "What's next" section for instructions
                                // about handling tokens.
                            }
                        })
                .addOnFailureListener(
                        this,
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "onFailure: " + e.getMessage());
                                // Handle communication errors ...
                                // See "Handle communication errors" section
                            }
                        });
    }

    private void executeRedeemAction(View v) {
        assert recaptchaTasksClient != null;
        recaptchaTasksClient
                .executeTask(RecaptchaAction.custom("redeem"))
                .addOnSuccessListener(
                        this,
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String token) {
                                // Handle success ...
                                // See "What's next" section for instructions
                                // about handling tokens.
                            }
                        })
                .addOnFailureListener(
                        this,
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle communication errors ...
                                // See "Handle communication errors" section
                            }
                        });
    }


/*    private void initializeRecaptcha() {
        SafetyNet.getClient(this).verifyWithRecaptcha(Globals.CAPTCHA_SITE_KEY)
                .addOnSuccessListener(this, response -> {
                    // Successfully received CAPTCHA token
                    String userResponseToken = response.getTokenResult();
                    Log.e(TAG, "initializeRecaptcha: " + userResponseToken);
                    // Now validate this token with your backend server
                    checkBoxNOtARobot.setChecked(true);
                })
                .addOnFailureListener(this, e -> {
                    // Error handling
                    Log.e(TAG, "initializeRecaptcha: " + e.getMessage());
                    checkBoxNOtARobot.setChecked(false);
                });
    }*/


    public void initializeRecaptcha() {
        SafetyNet.getClient(this).verifyWithRecaptcha(Globals.CAPTCHA_SITE_KEY)
                .addOnSuccessListener(this,
                        new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                            @Override
                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                                // Indicates communication with reCAPTCHA service was
                                // successful.
                                String userResponseToken = response.getTokenResult();
                                if (!userResponseToken.isEmpty()) {
                                    // Validate the user response token using the
                                    // reCAPTCHA siteverify API.
                                    Log.e(TAG, "onSuccess: " + userResponseToken);
                                }
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            // An error occurred when communicating with the
                            // reCAPTCHA service. Refer to the status code to
                            // handle the error appropriately.
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                            Log.d(TAG, "Error: " + CommonStatusCodes
                                    .getStatusCodeString(statusCode));
                        } else {
                            // A different, unknown type of error occurred.
                            Log.d(TAG, "Error: " + e.getMessage());
                        }
                    }
                });
    }


    private void verifyGoogleReCAPTCHA() {

        // below line is use for getting our safety
        // net client and verify with reCAPTCHA
        SafetyNet.getClient(this).verifyWithRecaptcha(Globals.CAPTCHA_SITE_KEY)
                // after getting our client we have
                // to add on success listener.
                .addOnSuccessListener(this, new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                    @Override
                    public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                        // in below line we are checking the response token.
                        if (!response.getTokenResult().isEmpty()) {
                            // if the response token is not empty then we
                            // are calling our verification method.
                            //   handleVerification(response.getTokenResult());
                            Log.e(TAG, "onSuccess: " + response.getTokenResult());
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // this method is called when we get any error.
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            // below line is use to display an error message which we get.
                            Log.d("TAG", "Error message: " +
                                    CommonStatusCodes.getStatusCodeString(apiException.getStatusCode()));
                        } else {
                            // below line is use to display a toast message for any error.
                            Toast.makeText(Login.this, "Error found is : " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

  /*  protected void handleVerification(final String responseToken) {
        // inside handle verification method we are
        // verifying our user with response token.
        // url to sen our site key and secret key
        // to below url using POST method.
        String url = "https://www.google.com/recaptcha/api/siteverify";

        // in this we are making a string request and
        // using a post method to pass the data.
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // inside on response method we are checking if the
                        // response is successful or not.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("success")) {
                                // if the response is successful then we are
                                // showing below toast message.
                                Toast.makeText(MainActivity.this, "User verified with reCAPTCHA", Toast.LENGTH_SHORT).show();
                            } else {
                                // if the response if failure we are displaying
                                // a below toast message.
                                Toast.makeText(getApplicationContext(), String.valueOf(jsonObject.getString("error-codes")), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception ex) {
                            // if we get any exception then we are
                            // displaying an error message in logcat.
                            Log.d("TAG", "JSON exception: " + ex.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // inside error response we are displaying
                        // a log message in our logcat.
                        Log.d("TAG", "Error message: " + error.getMessage());
                    }
                }) {
            // below is the getParams method in which we will
            // be passing our response token and secret key to the above url.
            @Override
            protected Map<String, String> getParams() {
                // we are passing data using hashmap
                // key and value pair.
                Map<String, String> params = new HashMap<>();
                params.put("secret", SECRET_KEY);
                params.put("response", responseToken);
                return params;
            }
        };
        // below line of code is use to set retry
        // policy if the api fails in one try.
        request.setRetryPolicy(new DefaultRetryPolicy(
                // we are setting time for retry is 5 seconds.
                50000,

                // below line is to perform maximum retries.
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // at last we are adding our request to queue.
        queue.add(request);
    }*/


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:


                if (Globals.checkInternet(this)) {

                    if (validation(login_username.getText().toString().trim(), login_password.getText().toString().trim())) {

                        Globals.APILog = "APILog";

                        Prefs.putString(Globals.SelectedBranch, "");
                        Prefs.putString(Globals.SelectedBranchID, "");
                        Prefs.putString(Globals.SelectedWareHose, "");
                        Prefs.putString(Globals.SessionID, "");


//             sessionloginApi();
                        //loginUser(Globals.SelectedDB,login_username.getText().toString().trim(),login_password.getText().toString().trim());

                        callLogInApi(login_username.getText().toString().trim(), login_password.getText().toString().trim());


                    }


                }

                break;
            case R.id.register_here:
                startActivity(new Intent(this, SignUp.class));

                break;

       /* case R.id.goto_reg:
            startActivity(new Intent(this,SignUp.class));
            break;*/
            case R.id.sql_setting:
           /* Intent intent=new Intent(this,SqlSetting.class);
            startActivityForResult(intent, 2);*/

                Intent intent = new Intent(this, DemoActivity.class);
                startActivity(intent);
                break;

        }
    }

    private void sessionloginApi() {
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> session = new HashMap<>();
        session.put("username", "root");
        session.put("password", "Sunil@123");


        Call<NewLogINResponse> call = NewApiClient.getInstance().getApiService().sessionlogin(session);
        call.enqueue(new Callback<NewLogINResponse>() {
            @Override
            public void onResponse(Call<NewLogINResponse> call, Response<NewLogINResponse> response) {


                Globals.APILog = "Not";
//                Prefs.putBoolean(Globals.AutoLogIn,true);
                Prefs.putString(Globals.SessionID, response.body().getToken());
                callLogInApi(login_username.getText().toString().trim(), login_password.getText().toString().trim());


            }

            @Override
            public void onFailure(Call<NewLogINResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    private void callLogInApi(String username, String password) {
        progressBar.setVisibility(View.VISIBLE);
        LogInDetail logInDetail = new LogInDetail();
        logInDetail.setUserName(username);
        logInDetail.setPassword(password);
        logInDetail.setFcm(token);
        Prefs.putString(Globals.USER_PASSWORD, password);

        Call<NewLogINResponse> call = NewApiClient.getInstance().getApiService().loginEmployee(logInDetail);
        call.enqueue(new Callback<NewLogINResponse>() {
            @Override
            public void onResponse(Call<NewLogINResponse> call, Response<NewLogINResponse> response) {

                if (response.body().getStatus() == 200) {

                    if (rememberme.isChecked()) {
                        Prefs.putString(Globals.REMEMBER_ME, "rem");
                    }


                    showMpinRegistrationProcessPopup(response);


                } else {
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(Login.this, "Check Login Credentials.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<NewLogINResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Login.this, "Check Login Credentials.", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void showMpinRegistrationProcessPopup(Response<NewLogINResponse> response) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this, R.style.CustomAlertDialog);
        View dialogView = LayoutInflater.from(Login.this).inflate(R.layout.dialog_setup_mpin, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(layoutParams);

        dialog.show();

        EditText etMpin = dialogView.findViewById(R.id.mpinEditText);
        EditText etReMpin = dialogView.findViewById(R.id.confirmMpinEditText);
        Button btnContinue = dialogView.findViewById(R.id.continueBtn);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateEditTexts(etMpin, etReMpin)) {
                    Prefs.putString(Globals.USERNAME, login_username.getText().toString().trim());
                    Prefs.putString(Globals.USER_PASSWORD, login_password.getText().toString().trim());
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
                    Prefs.putString(Globals.ADDRESS_LOGIN, String.valueOf(response.body().getLogInDetail().getAddress()));

                    //todo mpinPrefs
                    Prefs.putString(Globals.MPIN_VALUE, etMpin.getText().toString().trim());


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
                    Toast.makeText(Login.this, "MPIN Created SuccessFully Successfully", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(Login.this, "Please Enter MPIN Correctly", Toast.LENGTH_SHORT).show();
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


    private void userLogin(LogInRequest in) {

        Call<LogInResponse> call = APIsClient.getInstance().getApiService().LogIn(in);
        call.enqueue(new Callback<LogInResponse>() {
            @Override
            public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    Prefs.putString(Globals.USER_TYPE, "manager");
                    Prefs.putString(Globals.SessionID, response.body().getSessionId());
                    long session = Long.parseLong(response.body().getSessionTimeout());
                    session = session * 60 * 1000;

                    Prefs.putLong(Globals.SESSION_TIMEOUT, session);
                    Prefs.putLong(Globals.SESSION_REMAIN_TIME, 0);

                    // LoginHierarchy2ndLevel("manager");
                    gotoHome();
                } else {
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, QuotationResponse.class);
                        Toast.makeText(Login.this, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        // handle failure to read error
                    }
                }

            }

            @Override
            public void onFailure(Call<LogInResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void gotoHome() {
        // Intent i = new Intent(Login.this, MainActivity.class);
        Intent i = new Intent(Login.this, MainActivity_B2C.class);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

        finish();
    }


    private boolean validation(String user, String pass) {
        if (user.isEmpty()) {
            Toast.makeText(activity, getResources().getString(R.string.enter_user), Toast.LENGTH_SHORT).show();
            return false;
        } else if (pass.isEmpty()) {
            Toast.makeText(activity, getResources().getString(R.string.enter_sql_pass), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    /******************** Rest Client ***********************/


    /************ DataBase Alert *****************/


    Dialog dialog;


    @Override
    public void onClick(int po) {


        Prefs.putString(Globals.SessionID, "");
        Globals.APILog = "APILog";
        callLogInApi(login_username.getText().toString().trim(), login_password.getText().toString().trim());

        if (dialog != null)
            dialog.dismiss();
    }


}