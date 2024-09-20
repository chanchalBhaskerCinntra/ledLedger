package com.cinntra.ledure.fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cinntra.ledure.BuildConfig;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.LedgerCutomerDetails;
import com.cinntra.ledure.activities.LedgerReports;
import com.cinntra.ledure.activities.Login;
import com.cinntra.ledure.adapters.BottomsheetRecyclerviewAdapter;
import com.cinntra.ledure.adapters.LedgerGeneralEntriesAdapter;
import com.cinntra.ledure.adapters.SoldItem_Adapter;
import com.cinntra.ledure.databinding.FragmentMoreViewsFragmnetBinding;
import com.cinntra.ledure.globals.ApplicationModule;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.SessionManagement;
import com.cinntra.ledure.model.AttachmentModel;
import com.cinntra.ledure.model.CustomerItemResponse;
import com.cinntra.ledure.model.ResponseJournalEntryBpWise;
import com.cinntra.ledure.model.ResponseTripCheckIn;
import com.cinntra.ledure.services.AndroidLocationProvider;
import com.cinntra.ledure.services.LocationNotification;
import com.cinntra.ledure.services.LocationService;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;
import com.kingfisher.easyviewindicator.GridLayoutSnapHelper;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MoreViewsFragmnet extends Fragment {

    FragmentMoreViewsFragmnetBinding binding;
    BottomsheetRecyclerviewAdapter adapter;
    ArrayList<Integer> iconlist = new ArrayList<>();
    ArrayList<String> namelist = new ArrayList<>();
    AlertDialog.Builder builder;
    AlertDialog alertDialog;


    private long seconds;
    private boolean running = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_views_fragmnet, container, false);
    }


    SessionManagement sessionManagement;
    private static final String TAG = "MoreViewsFragmnet";

    private void callAttachmentAllApi() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
     /*   jsonObject.addProperty("LinkID", Prefs.getString(Globals.MyID, ""));
        jsonObject.addProperty("LinkType", "ProfilePic");*/
//        Call<AttachmentModel> call = NewApiClient.getInstance().getApiService(getActivity()).getAllAttachment(jsonObject);
        Call<AttachmentModel> call = NewApiClient.getInstance().getApiService(getActivity()).getNewAllAttachmentApi(jsonObject);
        call.enqueue(new Callback<AttachmentModel>() {
            @Override
            public void onResponse(Call<AttachmentModel> call, Response<AttachmentModel> response) {
                if (response != null) {

                    if (response.code() == 200) {
                        Log.e(TAG, "onResponse: " + response.body().getMessage());
                        if (response.body().getStatus() == 200) {
                            if (response.body().getData().size() > 0) {
                                String filePath = Globals.ImageURL + response.body().getData().get(0).getProfileImage();

                                if (filePath != null) {
                        /*        Glide.with(getActivity())
                                        .load(filePath)
                                        .into(binding.proImg);*/
                                } else {
                                    // binding.proImg.setImageResource(R.drawable.ic_profileicon);
                                }

                            }
                        } else if (response.body().getStatus() == 401) {
                            Toast.makeText(getActivity(), "Session Expired, Please Login Again", Toast.LENGTH_SHORT).show();

                            Prefs.clear();
                            Intent intent = new Intent(getActivity(), Login.class);
                            startActivity(intent);
                            getActivity().finish();
                            sessionManagement.ClearSession();
                        }

                    } else if (response.code() == 201) {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<AttachmentModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private static final int REQUEST_LOCATION_PERMISSION = 9865;
    private FusedLocationProviderClient fusedLocationClient;

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            //getCurrentLocation();
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMoreViewsFragmnetBinding.bind(view);

        builder = new AlertDialog.Builder(requireContext());
        builder.setView(R.layout.progress_dialog_alert)
                .setCancelable(false);


        alertDialog = builder.create();

        Globals.CURRENT_CLASS = getClass().getName();
        sessionManagement = new SessionManagement(getActivity());
        callAttachmentAllApi();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        requestCameraPermission();
        //  getCurrentLocation();


        requestLocationPermission();

        binding.tvCheckInTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });


        iconlist.add(R.drawable.ic_party_dashboard_deselected);
        iconlist.add(R.drawable.ic_quotation);
        iconlist.add(R.drawable.ic_customer_more);
        iconlist.add(R.drawable.ic_order);
        iconlist.add(R.drawable.ic_handshake_part);
        iconlist.add(R.drawable.ic_invoice_more);
        iconlist.add(R.drawable.campaign);
        iconlist.add(R.drawable.ic_delivery_more);
        iconlist.add(R.drawable.ic_inventory_more);
        iconlist.add(R.drawable.ic_ledger_icon);
        iconlist.add(R.drawable.ic_cash_discount);
        iconlist.add(R.drawable.ic_location_more);
        iconlist.add(R.drawable.ic_rupee_symbol_white);
        iconlist.add(R.drawable.ic_calender);
        iconlist.add(R.drawable.ic_baseline_delete_24);
        iconlist.add(R.drawable.ic_map_icon);


        namelist.add("Leads");
        namelist.add("Quotation");
        namelist.add("Customer");
        namelist.add("Order");
        namelist.add("Opportunity");
        namelist.add("Invoice");
        namelist.add("Campaign");
        namelist.add("Delivery");
        namelist.add("Inventory");
        namelist.add("Ledger");
        namelist.add("Cash Discount");
        namelist.add("Location");
        namelist.add("Expenses");
        namelist.add("Calender");
        namelist.add("Delete Account");
        namelist.add("Background Location");


        adapter = new BottomsheetRecyclerviewAdapter(requireContext(), iconlist, namelist);
        binding.rvMoreItems.setLayoutManager(new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false));
        binding.rvMoreItems.setHasFixedSize(true);
        // binding.rvMoreItems.setNestedScrollingEnabled(false);
        binding.rvMoreItems.setAdapter(adapter);
        GridLayoutSnapHelper gridLayoutSnapHelper = new GridLayoutSnapHelper(6);
        gridLayoutSnapHelper.attachToRecyclerView(binding.rvMoreItems);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onResume() {
        super.onResume();
        //todo timer work

        if (Prefs.getBoolean(Globals.isCheckingStart, false)) {
            // Set the start date-time string
            String startTimeString = Globals.getCurrentDateTimeFormatted();

            // Parse the start date-time string
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault());
            try {
                Date startDate = formatter.parse(startTimeString);

                // Get the current time and calculate the difference in seconds
                Date currentDate = new Date();
                long difference = (currentDate.getTime() - startDate.getTime()) / 1000;
                seconds = Math.max(difference, 0); // Ensure seconds are not negative

                // Start the timer
                runTimer();

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "onViewCreated: " + e.getMessage());
            }
        } else {
            binding.tvDuration.setText("Duration: 00:00:00");
            //  binding.tvDuration.setText("Duration: 00:00:00");
        }


    }

    private void runTimer() {
        // Get the text view (assuming you have a TextView with id 'timer')


        // Create a new Handler
        final Handler handler = new Handler();

        // Start the runnable task
        handler.post(new Runnable() {
            @Override
            public void run() {
                long hours = seconds / 3600;
                long minutes = (seconds % 3600) / 60;
                long secs = seconds % 60;

                // Format the time into hours, minutes, and seconds
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);

                // Set the text view text
                binding.tvDuration.setText("Duration: " + time);

                // If running is true, increment the seconds variable
                if (running) {
                    seconds++;
                }

                // Post the code again with a delay of 1 second
                handler.postDelayed(this, 1000);
            }
        });
    }


    private static final int REQUEST_IMAGE_ENGINEER_SIGN = 1;
    private String currentPhotoPath;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(requireContext(), "Error occurred while creating the file", Toast.LENGTH_SHORT).show();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),
                        BuildConfig.APPLICATION_ID + ".FileProvider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_ENGINEER_SIGN);
            }
        }
    }

    @NonNull
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + "/LedureBiz");

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

  /*  {
        "CheckIn_Lat": "101010101",
            "CheckIn_Long": "010101102",
            "CheckIn_Address": "a-3303 hshsh",
            "SalesEmployeeCode": "21",
            "Emp_Name": "",
            "Type": "start",
            "Total_Hour':"",
        "File":"",
            "Create_Date": "2024-03-15",
            "Create_Time": "11:55 AM",
            "Created_at":"2024-03-15 11:55 AM"
    }*/

    boolean ischeckedIn = false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_ENGINEER_SIGN && resultCode == RESULT_OK) {
            Log.e("YourFragment", "onActivityResult: " + currentPhotoPath);

            File imgFile = new File(currentPhotoPath);
            if (imgFile.exists()) {
                Uri fileUri = Uri.fromFile(imgFile);

            /*    Uri photoURI = FileProvider.getUriForFile(
                        this,
                        getApplicationContext().getPackageName() + ".fileprovider",
                        it
                );*/
                String imagePath = fileUri.getPath();
                Log.e("fileUri---", fileUri.toString());

                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
              /*  builder.addFormDataPart("LinkType", "CompletionEPMS");
                builder.addFormDataPart("LinkID", String.valueOf(ticketdata.getId()));*/

                builder.addFormDataPart("CheckIn_Lat", String.valueOf(latitude));
                builder.addFormDataPart("CheckIn_Long", String.valueOf(longitude));
                builder.addFormDataPart("CheckIn_Address", address);
                builder.addFormDataPart("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));//todo static
                builder.addFormDataPart("Emp_Name", Prefs.getString(Globals.SalesEmployeeName, ""));
                if (Prefs.getBoolean(Globals.isCheckingStart, false)) {
                    builder.addFormDataPart("Type", "stop");
                } else {
                    builder.addFormDataPart("Type", "start");
                }

                builder.addFormDataPart("Total_Hour", "");
                builder.addFormDataPart("Create_Date", Globals.getTodaysDate());
                builder.addFormDataPart("Create_Time", Globals.getTCurrentTime());
                builder.addFormDataPart("Created_at", "");


                try {
                    File file = new File(imagePath);
                    builder.addFormDataPart("File", file.getName(),
                            RequestBody.create(MediaType.parse("multipart/form-data"), file));
                } catch (Exception e) {
                    builder.addFormDataPart("File", "",
                            RequestBody.create(MediaType.parse("multipart/form-data"), ""));
                    e.printStackTrace();
                }

                MultipartBody requestBody = builder.build();
                Log.e("payload--->", requestBody.toString());


                createAttachment(requestBody);
            }
        }
    }

    double latitude = 0.0;
    double longitude = 0.0;
    String address = "";

    private void getCurrentLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(120000); // 10 seconds 120000
        locationRequest.setFastestInterval(120000); // 5 seconds 120000

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }

                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            getAddressFromLocation(latitude, longitude);
                        }
                    }
                }
            }, requireActivity().getMainLooper());
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        getAddressFromLocation(latitude, longitude);
                    }
                }
            }
        }, requireActivity().getMainLooper());
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressLine = address.getAddressLine(0);
           /*     Toast.makeText(requireContext(), "Latitude: " + latitude + "\nLongitude: " + longitude +
                        "\nAddress: " + addressLine, Toast.LENGTH_LONG).show();*/
                Log.e(TAG, "getAddressFromLocation: " + "Latitude: " + latitude + "\nLongitude: " + longitude +
                        "\nAddress: " + addressLine);
                if (Prefs.getBoolean(Globals.isCheckingStart, false)) {
                    createGalaxyTracking(latitude, longitude, addressLine);
                } else {
                    Log.e(TAG, "getAddressFromLocation: Location STopped");
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
           // Toast.makeText(requireContext(), "Unable to get address from latitude and longitude.", Toast.LENGTH_SHORT).show();
        }
    }


    private void createAttachment(MultipartBody requestBody) {
        alertDialog.show();

        Call<ResponseTripCheckIn> call = NewApiClient.getInstance().getApiService(requireContext()).punchDailyAttendance(requestBody);
        call.enqueue(new Callback<ResponseTripCheckIn>() {
            @Override
            public void onResponse(Call<ResponseTripCheckIn> call, Response<ResponseTripCheckIn> response) {
                if (response != null) {
                    alertDialog.dismiss();
                    if (response.body().getStatus().equalsIgnoreCase("200") && response.body().getStatus() != null) {
                        if (Prefs.getBoolean(Globals.isCheckingStart, false)) {
                            // Set the start date-time string
                            String startTimeString = Globals.getCurrentDateTimeFormatted();

                            // Parse the start date-time string
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault());
                            try {
                                Date startDate = formatter.parse(startTimeString);

                                // Get the current time and calculate the difference in seconds
                                Date currentDate = new Date();
                                long difference = (currentDate.getTime() - startDate.getTime()) / 1000;
                                seconds = Math.max(difference, 0); // Ensure seconds are not negative

                                // Start the timer
                                runTimer();

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG, "onViewCreated: " + e.getMessage());
                            }
                        } else {
                            binding.tvDuration.setText("Duration: 00:00:00");
                            //  binding.tvDuration.setText("Duration: 00:00:00");
                        }


                        if (Prefs.getBoolean(Globals.isCheckingStart, false)) {
                            Prefs.putBoolean(Globals.isCheckingStart, false);
                            Toast.makeText(requireContext(), "Stopped SuccessFully", Toast.LENGTH_SHORT).show();
                            currentPhotoPath = "";
                            binding.tvCheckInTextview.setText("Check In");
                            binding.tvDuration.setText("Duration- 00:00:00");
                            ischeckedIn = false;
                            // Create an Intent to stop the LocationService
                            Intent intent = new Intent(requireContext(), LocationService.class);
                            intent.setAction(LocationService.ACTION_STOP);

// Stop the service
                            requireActivity().stopService(intent);
                        } else {
                            Prefs.putBoolean(Globals.isCheckingStart, true);
                            Toast.makeText(requireContext(), "Started  SuccessFully", Toast.LENGTH_SHORT).show();
                            currentPhotoPath = "";
                            binding.tvCheckInTextview.setText("Check Out");
                            ischeckedIn = true;


                            // Create an Intent to start the LocationService
                            Intent intent = new Intent(requireContext(), LocationService.class);
                            intent.setAction(LocationService.ACTION_START);

                            // Start the service
                            requireActivity().startService(intent);


                            // Show a Toast message
                            Toast.makeText(requireActivity(), "Location sharing Started.", Toast.LENGTH_SHORT).show();
                        }


                    } else if (response.body().getStatus().equalsIgnoreCase("201")) {
                        Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseTripCheckIn> call, Throwable t) {
                Toast.makeText(requireContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
               /* loader.setVisibility(View.GONE);
                alertDialog.dismiss();*/
            }
        });
    }


    private void createGalaxyTracking(double latitude, double longitude, String address) {
        // binding.loaderLayout.loader.setVisibility(View.VISIBLE);

      /*  {
            "SalesEmployeeCode": "-1",
                "Latitude": "27.88807898",
                "Longitude": "28.000002",
                "Address": "abc homes",
                "Create_Date": "2024-07-11",
                "Create_Time": "08:20 PM"
        }*/
        JsonObject hde = new JsonObject();
        hde.addProperty("SalesEmployeeCode", "-1");
        hde.addProperty("Latitude", String.valueOf(latitude));
        hde.addProperty("Longitude", String.valueOf(longitude));
        hde.addProperty("Address", address);
        hde.addProperty("Create_Date", Globals.getTodaysDatervrsfrmt());
        hde.addProperty("Create_Time", Globals.getTCurrentTime());


        Call<ResponseTripCheckIn> call;
        call = NewApiClient.getInstance().getApiService(getActivity()).galaxyTrackingCreate(hde);


        call.enqueue(new Callback<ResponseTripCheckIn>() {
            @Override
            public void onResponse(Call<ResponseTripCheckIn> call, Response<ResponseTripCheckIn> response) {
                if (response != null) {
                    if (response.body().getStatus().equalsIgnoreCase("200") && response.body().getStatus() != null) {
                    /*    AllItemList.clear();
                        AllItemList.addAll(response.body().getCustomerLedgerRes());
                        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                        adapter = new SoldItem_Adapter(getContext(), AllItemList, LedgerCutomerDetails.nameCustomer, cardCode, startDateFrag, endDateFrag);
                        binding.recyclerview.setLayoutManager(layoutManager);
                        binding.recyclerview.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        if (response.body().getCustomerLedgerRes().size() == 0) {
                            binding.noDatafound.setVisibility(View.VISIBLE);
                        } else {
                            binding.noDatafound.setVisibility(View.INVISIBLE);
                        }
                        binding.loaderLayout.loader.setVisibility(View.GONE);*/
                        Log.e(TAG, "onResponseBackground: " + response.body().getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseTripCheckIn> call, Throwable t) {
                // binding.loaderLayout.loader.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private static final int REQUEST_CODE_PERMISSIONS = 10;

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(
                requireActivity(),
                new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_PERMISSIONS
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Globals.showMessage(requireContext(), "denied permissions case");
            }
        }

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // getCurrentLocation();
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}