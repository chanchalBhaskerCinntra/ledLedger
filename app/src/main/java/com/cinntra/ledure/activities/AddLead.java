package com.cinntra.ledure.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.LeadTypeAdapter;
import com.cinntra.ledure.databinding.CreateLeadFromBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.CreateLead;
import com.cinntra.ledure.model.LeadTypeData;
import com.cinntra.ledure.model.LeadTypeResponse;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddLead extends MainBaseActivity {


    @BindView(R.id.create)
    Button done;
    @BindView(R.id.companyname)
    EditText companyname;
    @BindView(R.id.comment)
    EditText comment;
    @BindView(R.id.product_interest)
    EditText product_interest;
    @BindView(R.id.source_spinner)
    Spinner source;
    @BindView(R.id.location)
    EditText location;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.turnover)
    EditText turnover;
    @BindView(R.id.designation)
    EditText designation;
    @BindView(R.id.contact_no)
    EditText contact_no;
    @BindView(R.id.full_name)
    EditText full_name;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.status_spinner)
    Spinner status_spinner;
    @BindView(R.id.leadType_spinner)
    Spinner leadType_spinner;
/*    @BindView(R.id.itemCount)
    TextView itemCount;
    @BindView(R.id.item_frame)
    RelativeLayout item_frame;*/
    public  int ITEMSVIEWCODE   = 10000;
    FusedLocationProviderClient client;

    CreateLeadFromBinding binding;


    String status = "Follow up";
        String leadtype = "";
        String sourcetype = "";
        ArrayList<LeadTypeData> leadTypeData = new ArrayList<>();
        ArrayList<LeadTypeData> sourceData = new ArrayList<>();


    @Override
    protected void onResume() {
        super.onResume();
      //  itemCount.setText("Item ("+Globals.SelectedItems.size()+")");

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=CreateLeadFromBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        client = LocationServices.getFusedLocationProviderClient(this);
        ButterKnife.bind(this);
        head_title.setText("Add Lead");
        Globals.SelectedItems.clear();
        callleadTypeApi();
        eventmanager();

/*
        item_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Globals.SelectedItems.size()==0) {
                    */
/*Globals.ItemType = "Paid";
                    openCategorydailog();*//*

                    Intent intent = new Intent(AddLead.this, ItemsList.class);
                    intent.putExtra("CategoryID",0);
                    startActivityForResult(intent, ITEMSVIEWCODE);
                }
                else {
                    Intent intent = new Intent(AddLead.this, SelectedItems.class);
                    intent.putExtra("FromWhere","addQt");
                    startActivityForResult(intent, ITEMSVIEWCODE);
                }
            }
        });
*/

    }



    @Override
    public void onBackPressed()
    {
        if(setAlertDataDiscard(AddLead.this))
        {
            super.onBackPressed();
        }


     }

    private void callleadTypeApi()
            {

        Call<LeadTypeResponse> call = NewApiClient.getInstance().getApiService().getLeadType();
        call.enqueue(new Callback<LeadTypeResponse>() {
            @Override
            public void onResponse(Call<LeadTypeResponse> call, Response<LeadTypeResponse> response) {

                if(response.code()==200)
                {
                   leadTypeData.clear();
                   leadTypeData.addAll(response.body().getData());
                    leadType_spinner.setAdapter(new LeadTypeAdapter(AddLead.this,leadTypeData));
                    if(leadTypeData.size()>0)
                    leadtype = leadTypeData.get(0).getName();
                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s, LeadResponse.class);
                        Toast.makeText(AddLead.this, mError.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                }
                loader.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<LeadTypeResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(AddLead.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        callSourceApi();
    }

    private void callSourceApi()
            {


        Call<LeadTypeResponse> call = NewApiClient.getInstance().getApiService().getsourceType();
        call.enqueue(new Callback<LeadTypeResponse>() {
            @Override
            public void onResponse(Call<LeadTypeResponse> call, Response<LeadTypeResponse> response) {

                if(response.code()==200)
                {
                    sourceData.clear();
                   /* LeadTypeData ld = new LeadTypeData();
                    ld.setName("Choose Select");
                    sourceData.add(ld);*/
                    sourceData.addAll(response.body().getData());
                    source.setAdapter(new LeadTypeAdapter(AddLead.this,sourceData));
                    sourcetype = sourceData.get(0).getName();
                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s, LeadResponse.class);
                        Toast.makeText(AddLead.this, mError.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                }
                loader.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<LeadTypeResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(AddLead.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void eventmanager()
            {

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcreatelead();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = parent.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                status = parent.getSelectedItem().toString();
            }
        });

        leadType_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                leadtype = leadTypeData.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                leadtype = leadTypeData.get(0).getName();
            }
        });

        source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sourcetype = sourceData.get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sourcetype = sourceData.get(0).getName();
            }
        });

        binding.checkBoxLoaction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    if (MainBaseActivity.boolean_permission){
                        getmyCurrentLocation();
                    }else {
                        givepermission();
                    }
                }

            }
        });

    }




    double latittude;
    double longitude;

    @SuppressLint("MissingPermission")
    private void getmyCurrentLocation() {
        // Initialize Location manager
        LocationManager locationManager
                = (LocationManager)
                getSystemService(
                        Context.LOCATION_SERVICE);
        // Check condition
        if (locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)) {
            // When location service is enabled
            // Get last location
            client.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(
                                @NonNull Task<Location> task) {

                            // Initialize location
                            Location location
                                    = task.getResult();
                            // Check condition
                            if (location != null) {
                                // When location result is not
                                // null set latitude
                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(AddLead.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(location
                                            .getLatitude(), location
                                            .getLongitude(), 1);

                                    latittude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    Log.e("TAG", "onComplete: "+latittude+" "+longitude);
                                    //  Toast.makeText(context, ""+latittude+longitude, Toast.LENGTH_SHORT).show();

                                    // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                } catch (IOException e) {

                                    e.printStackTrace();
                                }


                                String address = addresses != null ? addresses.get(0).getAddressLine(0) : ""; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                           /* String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();*/


                            } else {
                                // When location result is null
                                // initialize location request
                                LocationRequest locationRequest = new LocationRequest()
                                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                        .setInterval(10000)
                                        .setFastestInterval(
                                                1000)
                                        .setNumUpdates(1);

                                // Initialize location call back
                                LocationCallback
                                        locationCallback
                                        = new LocationCallback() {
                                    @Override
                                    public void
                                    onLocationResult(
                                            LocationResult
                                                    locationResult) {
                                        // Initialize
                                        // location
                                        Location location1
                                                = locationResult
                                                .getLastLocation();

                                        Geocoder geocoder;
                                        List<Address> addresses = null;
                                        geocoder = new Geocoder(AddLead.this, Locale.getDefault());

                                        try {
                                            addresses = geocoder.getFromLocation(location1
                                                    .getLatitude(), location1
                                                    .getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                        String city = addresses.get(0).getLocality();
                                        String state = addresses.get(0).getAdminArea();
                                        String country = addresses.get(0).getCountryName();
                                        String postalCode = addresses.get(0).getPostalCode();
                                        String knownName = addresses.get(0).getFeatureName();
//                                        callApi(location1
//                                                .getLatitude(), location1
//                                                .getLongitude(), mapData, address);
                                    }
                                };

                                // Request location updates
                                client.requestLocationUpdates(
                                        locationRequest,
                                        locationCallback,
                                        Looper.myLooper());
                            }
                        }
                    });
        } else {
            // When location service is not enabled
            // open location setting
            startActivity(
                    new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            .setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void addcreatelead()
             {
        if(validation(companyname,full_name,contact_no,location,comment,email)){




            CreateLead lv = new CreateLead();



            if (binding.checkBoxLoaction.isChecked()){
                lv.setCompanyName(companyname.getText().toString());
                lv.setContactPerson(full_name.getText().toString());
                lv.setPhoneNumber(contact_no.getText().toString());
                lv.setEmail(email.getText().toString());
                lv.setLocation(location.getText().toString());
                lv.setSource(sourcetype);
                lv.setProductInterest(product_interest.getText().toString());
                lv.setAssignedTo(Prefs.getString(Globals.SalesEmployeeCode,""));
                lv.setNumOfEmployee(10);
                lv.setTurnover(turnover.getText().toString());
                lv.setDesignation(designation.getText().toString());
                lv.setEmployeeId(Integer.valueOf(Prefs.getString(Globals.EmployeeID, "")));
                lv.setMessage(comment.getText().toString());
                lv.setDate(Globals.getTodaysDatervrsfrmt());
                lv.setTimestamp(Globals.getTimestamp());
                lv.setStatus(status);
                lv.setLeadType(leadtype);
                lv.setAttach("");
                lv.setCaption("");
                lv.setProjectAmount("");
                lv.setCustomerType("");
                lv.setStages("");

                lv.setU_LAT(String.valueOf(latittude));
                lv.setU_LONG(String.valueOf(longitude));
            }else {
                lv.setCompanyName(companyname.getText().toString());
                lv.setContactPerson(full_name.getText().toString());
                lv.setPhoneNumber(contact_no.getText().toString());
                lv.setEmail(email.getText().toString());
                lv.setLocation(location.getText().toString());
                lv.setSource(sourcetype);
                lv.setProductInterest(product_interest.getText().toString());
                lv.setAssignedTo(Globals.TeamEmployeeID);
                lv.setNumOfEmployee(10);
                lv.setTurnover(turnover.getText().toString());
                lv.setDesignation(designation.getText().toString());
                lv.setEmployeeId(Integer.valueOf(Prefs.getString(Globals.EmployeeID, "")));
                lv.setMessage(comment.getText().toString());
                lv.setDate(Globals.getTodaysDatervrsfrmt());
                lv.setTimestamp(Globals.getTimestamp());
                lv.setStatus(status);
                lv.setLeadType(leadtype);
                lv.setAttach("");
                lv.setCaption("");
                lv.setProjectAmount("");
                lv.setCustomerType("");
                lv.setStages("");
                lv.setU_LAT("");
                lv.setU_LONG("");
            }

            if(Globals.checkInternet(AddLead.this)) {
                loader.setVisibility(View.VISIBLE);
                callcreateLeadApi(lv);
            }
    }


}

    private void callcreateLeadApi(CreateLead lv)
            {
        ArrayList<CreateLead> createLeads = new ArrayList<>();
        createLeads.add(lv);
        Call<LeadResponse> call = NewApiClient.getInstance().getApiService().createLead(createLeads);
        call.enqueue(new Callback<LeadResponse>() {
            @Override
            public void onResponse(Call<LeadResponse> call, Response<LeadResponse> response) {

                if(response.code()==200)
                {
                    if(response.body().getMessage().equalsIgnoreCase("successful")){

                        Globals.SelectedItems.clear();
                        Toasty.success(AddLead.this, "Add Successfully", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                    else{
                        Toasty.warning(AddLead.this,response.body().getMessage(),Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    LeadResponse mError = new LeadResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s, LeadResponse.class);
                        Toast.makeText(AddLead.this, mError.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                }
                loader.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<LeadResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toasty.error(AddLead.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validation(EditText companyname, EditText full_name, EditText contact_no, EditText location, EditText product_interest, EditText email) {

        if(companyname.length()==0){
            companyname.requestFocus();
            companyname.setError("Enter Company Name");
            Toasty.warning(this,"Enter Company Name",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(full_name.length()==0){
            full_name.requestFocus();
            full_name.setError("Enter Full Name");
            Toasty.warning(this,"Enter Full Name",Toast.LENGTH_SHORT).show();

            return false;
        }
        else if(contact_no.length()!=10){
            contact_no.requestFocus();
            contact_no.setError("Enter Contact No");
            Toasty.warning(this,"Enter Contact No",Toast.LENGTH_SHORT).show();
            return false;
        }

       else if (!email.getText().toString().isEmpty()){
            if(isvalidateemail()){
                email.requestFocus();
                email.setError("This email address is not valid");
                return false;
            }
        }
        return true;

    }

    private boolean isvalidateemail(){
        String checkEmail = email.getText().toString();
        boolean hasSpecialEmail = Patterns.EMAIL_ADDRESS.matcher(checkEmail).matches();
        if(!hasSpecialEmail){
            email.setError("This email address is not valid");
            return true;
        }
        return false;
    }


    boolean Dcstatus = true;
    private boolean setAlertDataDiscard(Context context)
         {
        Dcstatus =false;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.data_discard)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Dcstatus =true;
                       finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        Dcstatus =false;
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
       // alert.setTitle(R.string.data_discard_sub);
        alert.show();
        return Dcstatus;

    }

    private void givepermission() {
        Dexter.withActivity(AddLead.this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            MainBaseActivity.boolean_permission = true;
                            getmyCurrentLocation();
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



}
