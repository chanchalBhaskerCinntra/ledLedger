package com.cinntra.ledure.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.AddBPCustomer;
import com.cinntra.ledure.activities.MainActivity_B2C;
import com.cinntra.ledure.adapters.BottomSheetAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.interfaces.ChangeTeam;
import com.cinntra.ledure.interfaces.FragmentRefresher;
import com.cinntra.ledure.model.MapData;
import com.cinntra.ledure.model.MapResponse;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.newapimodel.LeadValue;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateBPSelectionSheet extends BottomSheetDialogFragment implements BottomSheetAdapter.ItemListener {
    Context context;
    /*@BindView(R.id.database_listing)
    RecyclerView database_listing;*/
    @BindView(R.id.btnCancelDialog)
    TextView btnCancelDialog;
    @BindView(R.id.feedback)
    TextView feedback;
    @BindView(R.id.reminder)
    TextView reminder;
    double latittude;
    double longitude;
    String addressFromLatLong;

    @BindView(R.id.updateGeoLocation)
    TextView updateGeoLocation;
    FragmentRefresher fragmentRefresher;
    ChangeTeam changeTeam;

    LeadValue lv;
    String name;
    FusedLocationProviderClient client;

    public CreateBPSelectionSheet(LeadValue id) {
        // this.changeTeam = dashboard;
        this.lv = id;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.custom_bottom_sheet, container, false);

        ButterKnife.bind(this, v);
        // fragmentRefresher =(FragmentRefresher)getActivity();
        context = getContext();
        feedback.setText("Create Business Partner");
        reminder.setText("Update");
        client = LocationServices.getFusedLocationProviderClient(context);

        /*database_listing.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        database_listing.setAdapter(new BottomSheetAdapter(getActivity(),Dashboard.teamList_Hearchi,this));


*/

        getmyCurrentLocation();

        btnCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Bundle bundle = new Bundle();
                bundle.putParcelable(Globals.Lead_Data,lv);
                LeadFollowUpFragment chatterFragment = new LeadFollowUpFragment();
                chatterFragment.setArguments(bundle);
                FragmentTransaction chattransaction =  ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                chattransaction.add(R.id.customer_lead, chatterFragment).addToBackStack(null);
                chattransaction.commit();
                dismiss();*/

                Bundle b = new Bundle();
                b.putParcelable(Globals.LeadDetails, lv);
                b.putString("From", "Lead");
                LeadDetail fragment = new LeadDetail(context);
                fragment.setArguments(b);
                FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.customer_lead, fragment).addToBackStack(null);
                transaction.commit();
                dismiss();
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs.putString(Globals.AddBp, "Lead");
                Intent intent = new Intent(context, AddBPCustomer.class);
                intent.putExtra(Globals.AddBp, lv);
                context.startActivity(intent);
                dismiss();
            }
        });

        updateGeoLocation.setOnClickListener(view -> {
            openAddressDetailDialog(String.valueOf(lv.getId()),latittude,longitude);

           // openConfirmDialog();

          //  callApi(latittude,longitude);
        });
        return v;
    }

    private void openAddressDetailDialog(String id, double latitude, double longitude){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Current Address")
                .setContentText(addressFromLatLong)
                .setConfirmText("Update the address")
                .setCancelText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        openConfirmDialog(id, latitude, longitude);
                        // callApi(id, latitude, longitude);
                        //  deleteapi(id);

                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();

                    }
                })

                .show();
    }

    private void openConfirmDialog(String id, double latitude, double longitude) {

        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You Want to Update Lead location!")
                .setConfirmText("Yes, Update!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                      callApi(latitude,longitude);
                        //  deleteapi(id);

                    }
                })

                .show();

    }

    private void callApi(double latitude, double longitude) {
        HashMap<String,String> geo = new HashMap<>();
        geo.put("id",String.valueOf(lv.getId()));
        geo.put("U_LAT",String.valueOf(latitude));
        geo.put("U_LONG",String.valueOf(longitude));


        Call<LeadResponse> call = NewApiClient.getInstance().getApiService().updateLeadGeoLocation(geo);

        call.enqueue(new Callback<LeadResponse>() {
            @Override
            public void onResponse(Call<LeadResponse> call, Response<LeadResponse> response) {
                if (response != null) {
                    if (response.code() == 200) {
                        Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                }
            }

            @Override
            public void onFailure(Call<LeadResponse> call, Throwable t) {

            }
        });
    }


    @SuppressLint("MissingPermission")
    private void getmyCurrentLocation() {
        // Initialize Location manager
        LocationManager locationManager
                = (LocationManager) context
                .getSystemService(
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
                                geocoder = new Geocoder(context, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(location
                                            .getLatitude(), location
                                            .getLongitude(), 1);

                                    latittude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    if (addresses != null && !addresses.isEmpty()) {
                                        Address address = addresses.get(0);

                                        // Access address details
                                        String addressLine = address.getAddressLine(0);
                                        String city = address.getLocality();
                                        String state = address.getAdminArea();
                                        String country = address.getCountryName();
                                        String postalCode = address.getPostalCode();

                                        addressFromLatLong=""+addressLine+" "+city+" "+state+" "+postalCode+" "+country;

                                        // Use the address details as needed
                                        // ...


                                    }
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
                                        geocoder = new Geocoder(context, Locale.getDefault());

                                        try {
                                            addresses = geocoder.getFromLocation(location1
                                                    .getLatitude(), location1
                                                    .getLongitude(), 1);


                                            // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                                            if (addresses != null && !addresses.isEmpty()) {
                                                Address address = addresses.get(0);

                                                // Access address details
                                                String addressLine = address.getAddressLine(0);
                                                String city = address.getLocality();
                                                String state = address.getAdminArea();
                                                String country = address.getCountryName();
                                                String postalCode = address.getPostalCode();

                                                addressFromLatLong=""+addressLine+" "+city+" "+state+" "+postalCode+" "+country;

                                                // Use the address details as needed
                                                // ...


                                            }
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


    @Override
    public void onClickAt(int position) {
      /*  Prefs.putString(Globals.SalesEmployeeCode,Dashboard.teamList_Hearchi.get(position).getSalesEmployeeCode());
        Prefs.putString(Globals.SalesEmployeeName,Dashboard.teamList_Hearchi.get(position).getSalesEmployeeName());

        Globals.TeamEmployeeID = Dashboard.teamList_Hearchi.get(position).getId().toString();
        Prefs.putString(Globals.Role,Dashboard.teamList_Hearchi.get(position).getRole());
        changeTeam.changeTeam(Dashboard.teamList_Hearchi.get(position).getSalesEmployeeName());
        fragmentRefresher.onRefresh();*/
        dismiss();
    }


}