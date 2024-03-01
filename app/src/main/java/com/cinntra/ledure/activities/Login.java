package com.cinntra.ledure.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements View.OnClickListener, DatabaseClick {
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
    private AppCompatActivity activity;
    private String token = "";

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
                            /* LogInRequest in = new LogInRequest();
                            in.setCompanyDB(response.body().getSap().getCompanyDB());  //HANA
                            in.setPassword(response.body().getSap().getPassword());//"manager"//8097
                            in.setUserName(response.body().getSap().getUserName());//"manager"
                             userLogin(in);*/
                    gotoHome();


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