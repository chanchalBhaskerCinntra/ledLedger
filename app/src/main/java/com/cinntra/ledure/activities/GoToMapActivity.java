package com.cinntra.ledure.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.cinntra.ledure.R;
import com.cinntra.ledure.databinding.ActivityGoToMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GoToMapActivity extends FragmentActivity implements OnMapReadyCallback {
    ActivityGoToMapBinding binding;
    String lat, longitudeTT;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoToMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        lat = getIntent().getStringExtra("lat");
        longitudeTT = getIntent().getStringExtra("long");
        Log.e("TAG", "onCreate: "+lat+" "+longitudeTT );
       // Toast.makeText(this, "" + lat+"  " + longitudeTT, Toast.LENGTH_SHORT).show();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapGoto);
        mapFragment.getMapAsync(this);

//        //todo place initialization...
//        if (!Places.isInitialized()) {
//            Places.initialize(applicationContext, apiKey)
     //   }

//        binding.mapViewMap.onCreate(savedInstanceState);
//        binding.mapViewMap.getMapAsync(googleMap -> {
//            // Customize the map as needed
//            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//            googleMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(Double.valueOf(lat), Double.valueOf(longitudeTT)))
//                    .title("location"));
//
//// Add a click event listener to the marker
//            googleMap.setOnMarkerClickListener(marker -> {
//                // Perform reverse geocoding to get the address
//                // Show the address in an info window or any other desired way
//                return false;
//            });
//
//        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return false;
    }

    LatLng homeLatLng;
    List<LatLng> lngList = new ArrayList<>();

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(4.0f);
//        mMap.setMaxZoomPreference(16.0f);
        float zoomLevel = 14.0f;

       // 28.6282243,77.3877188

        homeLatLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(longitudeTT));
       // homeLatLng = new LatLng(28.6282243, 77.3877188);
        lngList.add(homeLatLng);
        LatLng place1 = new LatLng(Double.parseDouble(lat), Double.parseDouble(longitudeTT));
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place1, zoomLevel));
        mMap.addMarker(new MarkerOptions().position(place1)
                .title(getAddressFromLatLng(GoToMapActivity.this, Double.parseDouble(lat), Double.parseDouble(longitudeTT)))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel));
    }

    public static String getAddressFromLatLng(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String addressText = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                addressText = address.getAddressLine(0);

                String addressLine = address.getAddressLine(0);
                String city = address.getLocality();
                String state = address.getAdminArea();
                String country = address.getCountryName();
                String postalCode = address.getPostalCode();

                addressText = "" + addressLine + " " + city + " " + state + " " + postalCode + " " + country;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressText;
    }


//
//    @Override
//    public void onStart() {
//        super.onStart();
//        binding.mapViewMap.onStart();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        binding.mapViewMap.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        binding.mapViewMap.onPause();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        binding.mapViewMap.onStop();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        binding.mapViewMap.onDestroy();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        binding.mapViewMap.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onLowMemory() {
//        super.onLowMemory();
//        binding.mapViewMap.onLowMemory();
//    }

}