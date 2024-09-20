package com.cinntra.ledure.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.Login;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.EmpDetails;
import com.cinntra.ledure.model.EmployeeProfile;
import com.cinntra.ledure.model.NewLoginData;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Settings extends Fragment implements View.OnClickListener {

    @BindView(R.id.logout)
    ImageView logout;
    @BindView(R.id.edit_profile)
    ImageView edit_profile;
    @BindView(R.id.profile_pic)
    ImageView profile_pic;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.designation)
    TextView designation;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.mail)
    TextView mail;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.employee)
    TextView employee;
    @BindView(R.id.employee_status)
    TextView employee_status;
    @BindView(R.id.employee_type)
    TextView employee_type;
    @BindView(R.id.joining_date)
    TextView joining_date;
    @BindView(R.id.app_role)
    TextView app_role;
    @BindView(R.id.company_name)
    TextView company_name;
    @BindView(R.id.branch_name)
    TextView branch_name;
    @BindView(R.id.reporting_to)
    TextView reporting_to;
    @BindView(R.id.submit_feedback)
    TextView submit_feedback;
    @BindView(R.id.rate_our_app)
    TextView rate_our_app;
    @BindView(R.id.about_company)
    TextView about_company;
    @BindView(R.id.other_app)
    TextView other_app;
    @BindView(R.id.reset)
    Button reset;
    @BindView(R.id.currentlocation)
    LinearLayout currentlocation;

    LocationManager locationManager;

    public Settings() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Settings newInstance(String param1, String param2) {
        Settings fragment = new Settings();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, v);
        Globals.CURRENT_CLASS = getClass().getName();
        Gson gson = new Gson();
        String json = Prefs.getString(Globals.AppUserDetails, "");
        NewLoginData obj = gson.fromJson(json, NewLoginData.class);
        logout.setOnClickListener(this);
        currentlocation.setOnClickListener(this);
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .withBorder(4) /* thickness in px */
                .endConfig()
                .buildRound(Character.toString(Prefs.getString(Globals.Employee_Name,"").charAt(0)).toUpperCase(), Color.BLUE);
        profile_pic.setImageDrawable(drawable);
        String name = Prefs.getString(Globals.Employee_Name,"");
        String cap = name.substring(0, 1).toUpperCase() + name.substring(1);
        user_name.setText(cap);
       /* if (Globals.checkInternet(getActivity()))
            loadProfile(Prefs.getString(Globals.EmployeeID, ""));*/
        SetData(obj);
        return v;
    }

    private void SetData(NewLoginData obj){
        employee.setText("Employee ID : EMP00" +obj.getId());
        phone.setText(obj.getMobile());
        mail.setText(obj.getEmail());
        if(obj.getActive().equalsIgnoreCase("tYES"))
            employee_status.setText("Employee Status : Active");
        else
            employee_status.setText("Employee Status : InActive");
        app_role.setText("Designation :" + obj.getRole());
        designation.setText(obj.getRole());
        company_name.setText("Company Name : Vision Infotech");
      //  reporting_to.setText("Reporting to : "+ obj.getReportingName());



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                /*Prefs.clear();
                Intent intent = new Intent(getActivity(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/
                openConfirmDialog();

                break;
            case R.id.currentlocation:
                getCurrentLocation(getContext());
/*
//                  getcurrentLocation();
                LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                // get the last know location from your location manager.
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                  // now get the lat/lon from the location and do something with it.
                  Log.e("location","Current Location: " + location.getLatitude() + ", " + location.getLongitude());

                  Geocoder geocoder;
                  List<Address> addresses = null;
                  geocoder = new Geocoder(getContext(), Locale.getDefault());
                  try {
                      addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                  }catch (Exception e){
                      Log.e("address",e.getLocalizedMessage());
                  }
                  String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                  String uri = "https://www.google.com/maps/?q=" + location.getLatitude()+ "," +location.getLongitude() ;
                  Intent sendIntent = new Intent();
                  sendIntent.setAction(Intent.ACTION_SEND);
                  sendIntent.putExtra(Intent.EXTRA_TEXT, uri);
                 // sendIntent.putExtra(Intent.EXTRA_TEXT,address);
                  sendIntent.setType("text/plain");
                  sendIntent.setPackage("com.whatsapp");
                  startActivity(sendIntent);*/

                  break;
        }

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

    private void getCurrentLocation(Context context) {
        SingleShotLocationProvider.requestSingleUpdate(context,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(SingleShotLocationProvider.GPSCoordinates location) {
                  //      Log.d("Location", "my location is " + location.latitude);
                        Geocoder geocoder;
                        List<Address> addresses = null;
                        geocoder = new Geocoder(getContext(), Locale.getDefault());
                        try {
                            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        }catch (Exception e){
                            Log.e("address",e.getLocalizedMessage());
                        }
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                        String uri = "https://www.google.com/maps/?q=" + location.latitude+ "," +location.longitude ;
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, uri);
                        // sendIntent.putExtra(Intent.EXTRA_TEXT,address);
                        sendIntent.setType("text/plain");
                        sendIntent.setPackage("com.whatsapp");
                        startActivity(sendIntent);
                    }
                });
    }


    private void loadProfile(String userID)
       {
           EmpDetails empDetails = new EmpDetails();
           empDetails.setId(Integer.parseInt(userID));
        Call<EmployeeProfile> call = NewApiClient.getInstance().getApiService(getActivity()).getProfileDetail(empDetails);
        call.enqueue(new Callback<EmployeeProfile>() {
        @Override
        public void onResponse(Call<EmployeeProfile> call, Response<EmployeeProfile> response) {
            if(response !=null) {
                if (response.body().getData().size() > 0) {

//                    Prefs.putString(Globals.USERNAME,response.body().getValue().get(0).getUserId().getUserName());

                    phone.setText(response.body().getData().get(0).getMobile());
                    mail.setText(response.body().getData().get(0).getEmail());
                    if(response.body().getData().get(0).getActive().equalsIgnoreCase("tYes"))
                    employee_status.setText("Employee Status : Yes");
                    else
                    employee_status.setText("Employee Status : No");
                    employee.setText("Employee ID : "+response.body().getData().get(0).getEmployeeID());
                    designation.setText(response.body().getData().get(0).getRole());
                    branch_name.setText(response.body().getData().get(0).getBranch());
                    reporting_to.setText(response.body().getData().get(0).getReportingTo());


                }
            }
            }
            @Override
            public void onFailure(Call<EmployeeProfile> call, Throwable t) {

            }
        });
    }
}