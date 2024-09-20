package com.cinntra.ledure.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Explode;

import com.cinntra.ledure.R;
import com.cinntra.ledure.fragments.PaymentCollection;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomersAdapterDetals extends RecyclerView.Adapter<CustomersAdapterDetals.ViewHolder> {
    Context context;
    List<BusinessPartnerData> customerList;
    FusedLocationProviderClient client;

    // BusinessPartnerData obj;

    public CustomersAdapterDetals(Context context, List<BusinessPartnerData> customerList) {

        this.context = context;
        this.customerList = customerList;
        this.tempList = new ArrayList<BusinessPartnerData>();
        this.tempList.addAll(customerList);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.customers_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BusinessPartnerData  obj = getItem(position);
        client = LocationServices.getFusedLocationProviderClient(context);
        getmyCurrentLocation();
        holder.customerName.setText(obj.getCardName());
        holder.cardNumber.setText(obj.getCardCode());
        holder.gps_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddressDetailDialog(String.valueOf(obj.getId()), latittude, longitude);

           //     openConfirmDialog(String.valueOf(obj.getId()), latittude, longitude);

//                    Bundle b = new Bundle();
//                    b.putSerializable(Globals.BussinessItemData,customerList.get(getAdapterPosition()));
//                    Intent i = new Intent(context, MapsActivity.class);
//                    i.putExtras(b);
//                    context.startActivity(i);
            }
        });

        holder.map_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click_map", "onClick: " );
                Log.e("click_map", "onClick: "+obj.getU_LAT());
                Log.e("click_map", "onClick: "+obj.getU_LONG());
                getmyCurrentLocation();




                String uri = "http://maps.google.com/maps?saddr=lat1,long1&daddr=lat2,long2";
                uri = uri.replace("lat1", String.valueOf(latittude)); // Replace lat1 with current latitude
                uri = uri.replace("long1", String.valueOf(longitude)); // Replace long1 with current longitude
                uri = uri.replace("lat2", String.valueOf(obj.getU_LAT())); // Replace lat2 with destination latitude
                uri = uri.replace("long2", String.valueOf(obj.getU_LONG())); // Replace long2 with destination longitude

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mapIntent.setPackage("com.google.android.apps.maps"); // Specify the package name to open in Google Maps app

                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    // Start activity to open Google Maps app
                    context.startActivity(mapIntent);
                } else {
                    // Google Maps app is not installed, handle this case accordingly
                    // You can provide an alternative action, show an error message, or open maps website as fallback
                    Toast.makeText(context, "No application available to handle maps", Toast.LENGTH_SHORT).show();
                }

//                    Bundle bundle=new Bundle();
//                    bundle.putSerializable("location",obj);
//                    Intent intent=new Intent(context,MapsActivity.class);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);


                // 26.8485965,80.7776951,11

//                    String uri = "geo:" + 26.8485965 + "," + 80.7776951 + "?z=10"; // z=10 sets the zoom level
//                    Uri mapUri = Uri.parse(uri);
//
//                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
//                    mapIntent.setPackage("com.google.android.apps.maps"); // Specify the package name to open in Google Maps app
//
//                    if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
//                        context.startActivity(mapIntent);
//                    } else {
//                        // Google Maps app is not installed, open the maps website in a browser as an alternative
//                        String mapsUrl = "https://www.google.com/maps/search/?api=1&query=" + 26.8485965+ "," + 80.7776951;
//                        Uri mapsWebUri = Uri.parse(mapsUrl);
//
//                        Intent mapsWebIntent = new Intent(Intent.ACTION_VIEW, mapsWebUri);
//                        if (mapsWebIntent.resolveActivity(context.getPackageManager()) != null) {
//                            context.startActivity(mapsWebIntent);
//                        } else {
//                            // No application can handle the maps intent, show an error message
//                            Toast.makeText(context, "No application available to handle maps", Toast.LENGTH_SHORT).show();
//                        }
//                    }



            }
        });

        holder.date.setText(obj.getCreateDate());
        try {
            if (obj.getCreditLimitLeft() != null && !obj.getCreditLimitLeft().isEmpty())
                holder.amount.setText(Globals.getAmmount(obj.getCurrency(), Globals.changeDecemal(obj.getCreditLimitLeft())));
            if (obj.getCreditLimit() != null && !obj.getCreditLimit().isEmpty())
                holder.credit_limit_value.setText(Globals.getAmmount(obj.getCurrency(), Globals.changeDecemal(obj.getCreditLimit())));
        } catch (Exception e) {

        }
        if (obj.getURating() != null && obj.getURating().matches("\\d*\\.\\d\\d"))
            holder.ratingBar.setRating(Float.valueOf(obj.getURating()));
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public BusinessPartnerData getItem(int position) {
        return customerList.get(position);
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, cardNumber, date, amount, credit_limit_value, payment_collection;
        ImageView gps_icon, map_icon;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            cardNumber = itemView.findViewById(R.id.cardNumber);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
            credit_limit_value = itemView.findViewById(R.id.credit_limit_value);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            payment_collection = itemView.findViewById(R.id.payment_collection);
            gps_icon = itemView.findViewById(R.id.gps_icon);
            map_icon = itemView.findViewById(R.id.map_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  /*Bundle b = new Bundle();
                  b.putSerializable(Globals.BussinessItemData,customerList.get(getAdapterPosition()));
                  // Opportunity_Detail_Fragment fragment = new Opportunity_Detail_Fragment();
                  Update_BussinessPartner_Fragment fragment = new Update_BussinessPartner_Fragment();
                  fragment.setArguments(b);
                  FragmentTransaction transaction =  ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                  transaction.replace(R.id.main_edit_qt_frame, fragment);
                  transaction.addToBackStack(null);
                  transaction.commit();*/

                   /* Bundle b = new Bundle();
                    b.putSerializable(Globals.BussinessItemData, customerList.get(getAdapterPosition()));
                    // Opportunity_Detail_Fragment fragment = new Opportunity_Detail_Fragment();
                    BusinessPartnerDetail fragment = new BusinessPartnerDetail();
                    fragment.setArguments(b);
                    FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_edit_qt_frame, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();*/

                }
            });

            payment_collection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PaymentCollection fragment = new PaymentCollection();
                    Explode fade = new Explode();
                    fragment.setEnterTransition(fade);
                    fragment.setExitTransition(fade);
                    FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_edit_qt_frame, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });




        }
    }


    private void callApi(String id, double latitude, double longitude) {
        HashMap<String, String> geo = new HashMap<>();
        geo.put("id", String.valueOf(id));
        geo.put("U_LAT", String.valueOf(latitude));
        geo.put("U_LONG", String.valueOf(longitude));
        Call<LeadResponse> call = NewApiClient.getInstance().getApiService(context).updateBusinessPartnerGeoLocation(geo);

        call.enqueue(new Callback<LeadResponse>() {
            @Override
            public void onResponse(Call<LeadResponse> call, Response<LeadResponse> response) {
                if (response != null) {
                    if (response.code() == 200) {
                        Toast.makeText(context, "Updated Successfully", Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<LeadResponse> call, Throwable t) {

            }
        });
    }

    private void openConfirmDialog(String id, double latitude, double longitude) {

        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You Want to Update Customer location!")
                .setConfirmText("Yes,Update!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        callApi(id, latitude, longitude);
                        //  deleteapi(id);

                    }
                })

                .show();

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


    List<BusinessPartnerData> tempList = null;

    public void filter(String charText) {
        customerList.clear();
        tempList.addAll(customerList);
        charText = charText.toLowerCase(Locale.getDefault());
        if (charText.length() == 0) {
            customerList.addAll(tempList);
        } else {
            for (BusinessPartnerData st : tempList) {
                if (st.getCardName() != null && !st.getCardName().isEmpty() && st.getCardCode() != null && !st.getCardCode().isEmpty()) {
                    if (st.getCardName().toLowerCase(Locale.getDefault()).contains(charText) || st.getCardCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                        customerList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }


    public void AllData() {
        customerList.clear();
        customerList.addAll(tempList);
        notifyDataSetChanged();
    }

    public void Customerfilter() {
        customerList.clear();
        for (BusinessPartnerData st : tempList) {
            if (st.getCardName() != null && !st.getCardName().isEmpty())
                customerList.add(st);
        }

        Collections.sort(customerList, new Comparator<BusinessPartnerData>() {
            @Override
            public int compare(BusinessPartnerData o1, BusinessPartnerData o2) {

                return o1.getCardName().compareTo(o2.getCardName());
            }
        });
        notifyDataSetChanged();
    }

    double latittude;
    double longitude;
    String addressFromLatLong;

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
                                        addressFromLatLong=""+address+" "+city+" "+state+" "+postalCode+" "+country;


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
            context.startActivity(
                    new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            .setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
