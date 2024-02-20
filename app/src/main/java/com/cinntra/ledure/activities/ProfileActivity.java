package com.cinntra.ledure.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.ZonesAdapter;
import com.cinntra.ledure.databinding.BottomSheetZonesBinding;
import com.cinntra.ledure.databinding.ProfilenewPageBinding;
import com.cinntra.ledure.fragments.SettingFragment;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.AttachmentModel;
import com.cinntra.ledure.model.MapData;
import com.cinntra.ledure.model.MapResponse;
import com.cinntra.ledure.model.NewLoginData;
import com.cinntra.ledure.services.GoogleService;
import com.cinntra.ledure.webservices.NewApiClient;
import com.cinntra.roomdb.ItemsDatabase;
import com.cinntra.roomdb.ItemsFilterDatabase;
import com.cinntra.roomdb.ItemsInSalesCardDatabase;
import com.cinntra.roomdb.LedgerGroupDatabase;
import com.cinntra.roomdb.LedgerZoneDatabase;
import com.cinntra.roomdb.PendingOrderDatabase;
import com.cinntra.roomdb.ReceiptDatabase;
import com.cinntra.roomdb.ReceivableDatabase;
import com.cinntra.roomdb.SaleLedgerDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    ProfilenewPageBinding pageBinding;
    public static String locationtype;
    FusedLocationProviderClient client;
    ItemsDatabase itemsDatabase;
    ItemsFilterDatabase itemsFilterDatabase;
    ItemsInSalesCardDatabase itemsInSalesCardDatabase;
    LedgerGroupDatabase ledgerGroupDatabase;
    LedgerZoneDatabase ledgerZoneDatabase;
    PendingOrderDatabase pendingOrderDatabase;
    ReceiptDatabase receiptDatabase;
    ReceivableDatabase receivableDatabase;
    SaleLedgerDatabase saleLedgerDatabase;
    long thirtyFiveMillis = 30 * 60 * 1000;


    //todo code by chanchal--
    private static final int REQUEST_CODE_PERMISSIONS = 2;
    private static final String TAG = "ProfileActivity";

    private List<Uri> mArrayUriList = new ArrayList<>();
    private List<Uri> mSelectedList = new ArrayList<>();
    private List<String> path = new ArrayList<>();
    private File file;
    private String picturePath;
    private Uri fileUri;
    private Random random = new Random();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageBinding = ProfilenewPageBinding.inflate(getLayoutInflater());
        setContentView(pageBinding.getRoot());
        Gson gson = new Gson();
        client = LocationServices.getFusedLocationProviderClient(this);
        long currentTimeMillis = System.currentTimeMillis();
        Prefs.putLong("MilliSeconds", 0);

        callAttachmentAllApi();

        if (currentTimeMillis - Prefs.getLong("MilliSeconds", 0) >= thirtyFiveMillis) {
            Prefs.putLong("MilliSeconds", currentTimeMillis);
            lastSync();
        }


        itemsDatabase = ItemsDatabase.getDatabase(this);
        itemsFilterDatabase = ItemsFilterDatabase.getDatabase(this);
        itemsInSalesCardDatabase = ItemsInSalesCardDatabase.getDatabase(this);
        ledgerGroupDatabase = LedgerGroupDatabase.getDatabase(this);
        ledgerZoneDatabase = LedgerZoneDatabase.getDatabase(this);
        pendingOrderDatabase = PendingOrderDatabase.getDatabase(this);
        receiptDatabase = ReceiptDatabase.getDatabase(this);
        receivableDatabase = ReceivableDatabase.getDatabase(this);
        saleLedgerDatabase = SaleLedgerDatabase.getDatabase(this);


        String json = Prefs.getString(Globals.AppUserDetails, "");
        NewLoginData obj = gson.fromJson(json, NewLoginData.class);
        pageBinding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        SetData(obj);

        pageBinding.linearZone.setOnClickListener(view -> {
            showBottomSheetDialog();
        });


        pageBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfirmDialog();
            }
        });

        pageBinding.ivPictureClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {
//                    captureImageFromCamera();
                    getPictureDialog();
                } else {
                    ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSIONS);
                }
            }
        });

        pageBinding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingFragment fragment = new SettingFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main_page, fragment);
                transaction.addToBackStack("Back");
                transaction.commit();
            }
        });

        pageBinding.mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageBinding.mode.isChecked()) {
                    Prefs.putString(Globals.locationcondition, "On");
                    locationtype = "Start";
                    //   Toast.makeText(ProfileActivity.this, Prefs.getString(Globals.locationcondition, "Off"), Toast.LENGTH_SHORT).show();
                    if (MainBaseActivity.boolean_permission) {
                        Log.e("start", "start");
                        Intent intent = new Intent(getApplicationContext(), GoogleService.class);
                        getApplicationContext().startService(intent);
                    } else {
                        givepermission(locationtype);
                    }
                } else {
                    Prefs.putString(Globals.locationcondition, "Off");
                    locationtype = "Stop";
                    Intent intent = new Intent(getApplicationContext(), GoogleService.class);
                    getApplicationContext().stopService(intent);
                }
            }
        });

        pageBinding.mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                }
            }
        });
        if (Prefs.getString(Globals.locationcondition, "Off").equalsIgnoreCase("On")) {
            pageBinding.mode.setChecked(true);
        } else {
            pageBinding.mode.setChecked(false);
        }


    }

    String attachID = "";
    String linkId = "";
    String linkType = "";


    private void callAttachmentAllApi() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("LinkID", Prefs.getString(Globals.MyID, ""));
        jsonObject.addProperty("LinkType", "ProfilePic");
        Call<AttachmentModel> call = NewApiClient.getInstance().getApiService().getAllAttachment(jsonObject);
        call.enqueue(new Callback<AttachmentModel>() {
            @Override
            public void onResponse(Call<AttachmentModel> call, Response<AttachmentModel> response) {
                if (response != null) {

                    if (response.body().getStatus() == 200) {
                        Log.e(TAG, "onResponse: " + response.body().getMessage());

                        if (response.body().getData().size() > 0) {
                            attachID = response.body().getData().get(0).getId();
                            linkId = response.body().getData().get(0).getLinkID();
                            linkType = response.body().getData().get(0).getLinkType();


                            String filePath = Globals.ImageURL + response.body().getData().get(0).getFile();

                            if (filePath != null) {
                                Glide.with(ProfileActivity.this)
                                        .load(filePath)
                                        .into(pageBinding.nameIcon);
                            } else {
                                pageBinding.nameIcon.setImageResource(R.drawable.ic_profileicon);
                            }

                        }

                    } else if (response.code() == 201) {
                        Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<AttachmentModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private static int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;
    private static int RESULT_LOAD_IMAGE = 101;
    private static int PICTURE_FROM_CAMERA = 1;


    public void getPictureDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().setWindowAnimations(R.style.AnimationsForDailog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.picturedialog);
        dialog.getWindow().getAttributes().width = ActionBar.LayoutParams.FILL_PARENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView cancel = dialog.findViewById(R.id.canceldialog);
        ImageView gallery = dialog.findViewById(R.id.gallerySelect);
        ImageView camera = dialog.findViewById(R.id.cameraSelect);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("image/*");
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                dialog.dismiss();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImageFromCamera();

                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    //todo code for camera picture click---
    private void captureImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, PICTURE_FROM_CAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            try {
                Bundle extras = data.getExtras();
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Log.e("picturePath", picturePath);

                    pageBinding.nameIcon.setImageURI(Uri.parse(picturePath));


                    callUploadImage();
                }


            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }

        else if (requestCode == PICTURE_FROM_CAMERA && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                file = saveBitmap(photo);
                picturePath = file.getPath();
                fileUri = Uri.fromFile(file);
                Log.e("fileUri---", fileUri.toString());
                Log.e("picturePath---", picturePath.toString());

                pageBinding.nameIcon.setImageBitmap(photo);


                callUploadImage();


            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
        else {
            Globals.showMessage(ProfileActivity.this, "No Image Found");
        }
    }


    private void callUploadImage() {
        File imageFile = new File(picturePath);


        Log.e("filePath>>>>>", "onCreate: " + picturePath);
        Log.e("fileNAme>>>>>", "onCreate: " + imageFile.getName());
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part File = MultipartBody.Part.createFormData("File", imageFile.getName(), requestBody);
        RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), attachID);
        RequestBody LinkType = RequestBody.create(MediaType.parse("multipart/form-data"), "ProfilePic");
        RequestBody LinkID = RequestBody.create(MediaType.parse("multipart/form-data"), Prefs.getString(Globals.MyID, ""));
        RequestBody Caption = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        RequestBody CreateDate = RequestBody.create(MediaType.parse("multipart/form-data"), Globals.getTodaysDatervrsfrmt());
        RequestBody CreateTime = RequestBody.create(MediaType.parse("multipart/form-data"), Globals.getTCurrentTime());
        RequestBody UpdateDate = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        RequestBody UpdateTime = RequestBody.create(MediaType.parse("multipart/form-data"), "");


        Call<AttachmentModel> call = NewApiClient.getInstance().getApiService().uploadProfileAttachment(
                File, id, LinkType, LinkID, Caption, CreateDate, CreateTime, UpdateDate, UpdateTime);
        call.enqueue(new Callback<AttachmentModel>() {
            @Override
            public void onResponse(Call<AttachmentModel> call, Response<AttachmentModel> response) {
                if (response != null) {

                    if (response.body().getStatus() == 200) {
                        Toast.makeText(ProfileActivity.this, "Successful", Toast.LENGTH_SHORT).show();

                    } else if (response.code() == 201) {
                        Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<AttachmentModel> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    private File saveBitmap(Bitmap bmp) {
        File extStorageDirectory = this.getCacheDir();
        OutputStream outStream = null;
        int num = random.nextInt(90) + 10;
        Log.e("extStorageDirectory---", extStorageDirectory.toString());
        File file = new File(extStorageDirectory, "temp" + num + ".png");

        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, "temp" + num + ".png");
        }

        try {
            outStream = new FileOutputStream(file);
            if (outStream != null) {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                outStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Log.e("file---", file.toString());
        return file;
    }


    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        );
        int write = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        );
        int read = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (write != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("in fragment on request", "Permission callback called-------");

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<>();
            // Initialize the map with both permissions
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

            // Fill with actual results from user
            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                }

                // Check for both permissions
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Log.d("in fragment on request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted");
                    // Process the normal flow
                    // Else any one or both the permissions are not granted
                } else {
                    Log.d("in fragment on request", "Some permissions are not granted ask again");

                    // Permission is denied (this is the first time when "never ask again" is not checked),
                    // so ask again explaining the usage of permission.
                    // shouldShowRequestPermissionRationale will return true
                    // Show the dialog or Snackbar saying it's necessary and try again, otherwise proceed with setup.
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        showDialogOK("Camera and Storage Permission required for this app", (dialog, which) -> {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    checkAndRequestPermissions();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    // Do nothing or handle accordingly
                                    break;
                            }
                        });
                    } else {
                        Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                        // Proceed with logic by disabling the related features or quit the app.
                        // You might want to show a dialog to the user here to inform them about the necessity of these permissions.
                    }
                }
            }
        }


    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }



    //todo this permission use while give camera provision only---
    private boolean allPermissionsGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

/*    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImageFromCamera();
            } else {
                Globals.showMessage(ProfileActivity.this, "denied permissions case");
            }
        }
    }*/


    private void SetData(NewLoginData obj) {
        pageBinding.empId.setText("EMP00" + obj.getId());

        pageBinding.phoneValue.setText(obj.getMobile());

        pageBinding.mail.setText(obj.getEmail());

        if (obj.getAddress().isEmpty()) {
            pageBinding.productInterest.setText("N/A");
        } else {
            pageBinding.productInterest.setText(obj.getAddress());
        }


        pageBinding.tvDesignation.setText(obj.getRole());

        pageBinding.emailUser.setText(obj.getEmail());

        if (obj.getActive().equalsIgnoreCase("tYES"))
            pageBinding.empTyp.setText("Active");
        else
            pageBinding.empTyp.setText("InActive");
        pageBinding.roleValue.setText(obj.getRole());
        pageBinding.lastlogin.setText("Last Login at " + obj.getLastLoginOn());
        pageBinding.companyName.setText(obj.getSalesEmployeeName());


        //  reporting_to.setText("Reporting to : "+ obj.getReportingName());


    }


    private void openConfirmDialog() {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You Want to Logout!")
                .setConfirmText("Yes!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Prefs.clear();
                        itemsDatabase.myDataDao().deleteAll();
                        itemsFilterDatabase.myDataDao().deleteAll();
                        itemsInSalesCardDatabase.myDataDao().deleteAll();
                        ledgerGroupDatabase.myDataDao().deleteAll();
                        ledgerZoneDatabase.myDataDao().deleteAll();
                        pendingOrderDatabase.myDataDao().deleteAll();
                        receiptDatabase.myDataDao().deleteAll();
                        receivableDatabase.myDataDao().deleteAll();
                        saleLedgerDatabase.myDataDao().deleteAll();


                        Intent intent = new Intent(ProfileActivity.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                })

                .show();

    }

    private void callApi(double latitude, double longitude, String type, String address) {
        MapData mapData = new MapData();
        mapData.setEmp_Id(Prefs.getString(Globals.MyID, ""));
        mapData.setEmp_Name(Prefs.getString(Globals.Employee_Name, ""));
        mapData.setLat(String.valueOf(latitude));
        mapData.setLong(String.valueOf(longitude));
        mapData.setUpdateDate(Globals.getTodaysDatervrsfrmt());
        mapData.setUpdateTime(Globals.getTCurrentTime());
        mapData.setAddress(address);
        mapData.setShape("meeting");
        mapData.setType(type);
        mapData.setRemark("");
        mapData.setResourceId("");
        mapData.setSourceType("");
        mapData.setContactPerson("");


        Call<MapResponse> call = NewApiClient.getInstance().getApiService().sendMaplatlong(mapData);
        call.enqueue(new Callback<MapResponse>() {
            @Override
            public void onResponse(Call<MapResponse> call, Response<MapResponse> response) {
                if (response != null) {
                    try {
                        if (response.isSuccessful()) {
                            Log.e("success", "success");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MapResponse> call, Throwable t) {

            }
        });
    }

    //todo permission for device location...
    private void givepermission(String locationtype) {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            getmyCurrentLocation(locationtype);
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            requestPermissions(new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                            }, 100);
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

    //todo getting current location lat and long...
    @SuppressLint("MissingPermission")
    private void getmyCurrentLocation(String type) {
        // Initialize Location manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Check condition
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // When location service is enabled
            // Get last location
            client.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(
                                @NonNull Task<Location> task) {

                            // Initialize location
                            Location location = task.getResult();
                            // Check condition
                            if (location != null) {
                                // When location result is not
                                // null set latitude
                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(ProfileActivity.this, Locale.getDefault());
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String knownName = addresses.get(0).getFeatureName();
                                callApi(location.getLatitude(), location.getLongitude(), type, address);
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
                                    onLocationResult(LocationResult locationResult) {
                                        // Initialize
                                        // location
                                        Location location1
                                                = locationResult
                                                .getLastLocation();

                                        Geocoder geocoder;
                                        List<Address> addresses = null;
                                        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

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
                                        callApi(location1
                                                .getLatitude(), location1
                                                .getLongitude(), type, address);
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


    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Please wait");


        BottomSheetZonesBinding bindingBottom;
        bindingBottom = BottomSheetZonesBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(bindingBottom.getRoot());
        String zones_all[] = {"All"};
        String zones[] = Prefs.getString(Globals.ZONE, "").split(",");


        int aLen = zones_all.length;
        int bLen = zones.length;
        String[] result = new String[aLen + bLen];

        System.arraycopy(zones_all, 0, result, 0, aLen);
        System.arraycopy(zones, 0, result, aLen, bLen);


        ZonesAdapter adapter = new ZonesAdapter(this, result, "ShowDetails");
        bindingBottom.zonesList.setAdapter(adapter);

        bottomSheetDialog.show();
        //    bindingBottom.headingBottomSheetShareReport.setText(R.string.share_customer_list);
        bindingBottom.ivCloseBottomSheet.setOnClickListener(view -> {
            progressDialog.dismiss();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                progressDialog.dismiss();
            }
        });


    }

    private void lastSync() {

        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime dateTime45MinutesBefore = currentDateTime.minus(45, ChronoUnit.MINUTES);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

// Format and print the current date and time
        String formattedTime = dateTime45MinutesBefore.format(formatter);
        String formattedDate = dateTime45MinutesBefore.format(dateformatter);

        pageBinding.lastSync.setText(" " + formattedTime + " & " + formattedDate);
        pageBinding.lastUpdate.setText(" 1:00 " + " & " + formattedDate);
    }
}
