package com.cinntra.ledure.test;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cinntra.ledure.databinding.ActivityTestForDeveloperBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.BodyCheckInTime;
import com.cinntra.ledure.model.MapResponse;
import com.cinntra.ledure.model.ResponseTripCheckIn;
import com.cinntra.ledure.webservices.NewApiClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TestActivityForDeveloper extends AppCompatActivity {

    // private RecyclerView parentRecyclerView;
    private ParentAdapter parentAdapter;
    ActivityTestForDeveloperBinding binding;

    private static final int PICK_IMAGE_REQUEST = 11231;
    private static final int REQUEST_IMAGE_CAPTURE = 15112;
    String picturePath;
    Uri selectedImageUri;

    ActivityResultLauncher<Intent> imageCaptureLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestForDeveloperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Glide.with(this).load("" + Globals.attachmentBaseUrl + "/static/image/TripExpenses/12311.jpg").into(binding.ivCheckINImage);


        binding.button4.setOnClickListener(view -> {
            Intent intent = new Intent();

            intent.setAction(Intent.ACTION_PICK);
            intent.setType("*/*");
            Intent i = Intent.createChooser(intent, "File");
            startActivityForResult(i, PICK_IMAGE_REQUEST);
            //startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        });

        binding.buttonCamera.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            imageCaptureLauncher.launch(intent);
        });

        imageCaptureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Get the URI of the captured image
                        Uri imageUri = result.getData().getData();


                        picturePath = getRealPathFromURI(imageUri);
                        Toast.makeText(this, picturePath, Toast.LENGTH_SHORT).show();

                        // Do something with the URI, such as display the image in an ImageView
//                        ImageView imageView = findViewById(R.id.imageView);
//                        imageView.setImageURI(imageUri);


                    }
                });

        List<ParentItem> parentItemList = new ArrayList<>();

// Create child items for the first parent item
        List<ChildItem> childItemList1 = new ArrayList<>();
        childItemList1.add(new ChildItem("Child 1 - Text 1", "Child 1 - Text 2"));
        childItemList1.add(new ChildItem("Child 2 - Text 1", "Child 2 - Text 2"));

// Create child items for the second parent item
        List<ChildItem> childItemList2 = new ArrayList<>();
        childItemList2.add(new ChildItem("Child 3 - Text 1", "Child 3 - Text 2"));
        childItemList2.add(new ChildItem("Child 4 - Text 1", "Child 4 - Text 2"));

// Create parent items and add child items
        parentItemList.add(new ParentItem("Parent 1", childItemList1));
        parentItemList.add(new ParentItem("Parent 2", childItemList2));

       // parentAdapter = new ParentAdapter(parentItemList);


        parentAdapter = new ParentAdapter(parentItemList);
        binding.rvInnerList.setAdapter(parentAdapter);

//        parentAdapter.setOnItemClickListener(new ParentAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                ParentItem parentItem = parentAdapter.getItem(position);
//                parentItem.setExpanded(!parentItem.isExpanded());
//                parentAdapter.notifyItemChanged(position);
//            }
//        });

        // Set the layout manager for the parent RecyclerView
        //  p
        binding.rvInnerList.setLayoutManager(new LinearLayoutManager(this));


        binding.button2.setOnClickListener(view -> {
            File imageFile = new File(picturePath);


            Log.e("filePath>>>>>", "onCreate: " + picturePath);
            Log.e("fileNAme>>>>>", "onCreate: " + imageFile.getName());
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("CheckInAttach", imageFile.getName(), requestBody);
            RequestBody bptype = RequestBody.create(MediaType.parse("multipart/form-data"), "value1");
            RequestBody bpName = RequestBody.create(MediaType.parse("multipart/form-data"), "value2");
            RequestBody cardCode = RequestBody.create(MediaType.parse("multipart/form-data"), "c0001");
            RequestBody salesPersonCode = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
            RequestBody modeOfTransport = RequestBody.create(MediaType.parse("multipart/form-data"), "c0001");
            RequestBody checkInDate = RequestBody.create(MediaType.parse("multipart/form-data"), "2023-04-05");
            RequestBody CheckInTime = RequestBody.create(MediaType.parse("multipart/form-data"), "2023-04-05");
            RequestBody checkInLat = RequestBody.create(MediaType.parse("multipart/form-data"), "0.04232313123");
            RequestBody checkInLong = RequestBody.create(MediaType.parse("multipart/form-data"), "0.04232313123");
            RequestBody checkinRemark = RequestBody.create(MediaType.parse("multipart/form-data"), "sisi");

            HashMap<String, RequestBody> hashMap = new HashMap();
            hashMap.put("BPType", bptype);
            hashMap.put("BPName", bpName);
            hashMap.put("CardCode", cardCode);
            hashMap.put("SalesPersonCode", salesPersonCode);
            hashMap.put("ModeOfTransport", modeOfTransport);
            // hashMap.put("ModeOfTransport",modeOfTransport);
            hashMap.put("CheckInDate", checkInDate);
            hashMap.put("CheckInTime", CheckInTime);
            hashMap.put("CheckInLat", checkInLat);
            hashMap.put("CheckInLong", checkInLong);
            // hashMap.put("CheckInLong",checkInLong);
            hashMap.put("CheckInRemarks", checkinRemark);
            // hashMap.put("CheckInAttach",imagePart);

            BodyCheckInTime bodyCheckInTime = new BodyCheckInTime();
            bodyCheckInTime.setCheckInAttach(imagePart);
            bodyCheckInTime.setbPType(bptype);
            bodyCheckInTime.setbPName(bpName);
            bodyCheckInTime.setCardCode(cardCode);
            bodyCheckInTime.setSalesPersonCode(salesPersonCode);
            bodyCheckInTime.setModeOfTransport(modeOfTransport);
            // bodyCheckInTime.setModeOfTransport(modeOfTransport);
            bodyCheckInTime.setCheckInDate(checkInDate);
            bodyCheckInTime.setCheckInTime(CheckInTime);
            bodyCheckInTime.setCheckInLat(checkInLat);
            bodyCheckInTime.setCheckInLong(checkInLong);
            bodyCheckInTime.setCheckInRemarks(checkinRemark);


            Call<ResponseTripCheckIn> call = NewApiClient.getInstance().getApiService(this).tripCheckIn(
                    imagePart, bptype, bpName, cardCode, salesPersonCode, modeOfTransport, checkInDate, CheckInTime, checkInLat, checkInLong, checkinRemark
            );

            call.enqueue(new Callback<ResponseTripCheckIn>() {
                @Override
                public void onResponse(Call<ResponseTripCheckIn> call, Response<ResponseTripCheckIn> response) {
                    if (response != null) {

                        if (response.code() == 200) {
                            Toast.makeText(TestActivityForDeveloper.this, "Successful", Toast.LENGTH_SHORT).show();

                        } else if (response.code() == 201) {
                            Toast.makeText(TestActivityForDeveloper.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(TestActivityForDeveloper.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }

//                    if (response.body().getValue().size() > 0) {
//
//
//                        startlat = Double.parseDouble(response.body().getValue().get(0).getLat());
//                        startlong = Double.parseDouble(response.body().getValue().get(0).getLong());
//
//                        distanceMAP = distance(startlat, startlong, latitude, longitude);
//                    }
//
//                    if (locationtype.equalsIgnoreCase("Start")) {
//                        if (Prefs.getString(Globals.CHECK_IN_STATUS, "CheckOut").equalsIgnoreCase("Stop")) {
//                            Prefs.putString(Globals.CHECK_IN_STATUS, "Start");
//
//                            binding.slideView.setText(" Check Out");
//                        } else {
//                            Prefs.putString(Globals.CHECK_IN_STATUS, "Stop");
//
//                            binding.slideView.setText(" Check In");
//                        }
//                        Log.e("success", "success");
//                    } else {
//                        Prefs.putString(Globals.CHECK_IN_STATUS, "Stop");
//
//                        binding.slideView.setText(" Check In");
//                        //  openExpenseDialog();
//                    }


                    }
                }

                @Override
                public void onFailure(Call<ResponseTripCheckIn> call, Throwable t) {

                }
            });

        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST) {
            selectedImageUri = data.getData();
            Log.e("URI>>>", "onActivityResult: " + selectedImageUri.toString());


            picturePath = getRealPathFromURI(selectedImageUri);


//            String[] projection = { MediaStore.Images.Media.DATA };
//            Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
//            if (cursor != null && cursor.moveToFirst()) {
//                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                String filePath = cursor.getString(columnIndex);
//                cursor.close();
//                // Do something with the file path, such as displaying it in a TextView
//               // TextView filePathTextView = findViewById(R.id.file_path_text_view);
//               // filePathTextView.setText(filePath);
//                picturePath=filePath;
//                Toast.makeText(this,""+ filePath, Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "picture"+picturePath, Toast.LENGTH_SHORT).show();
//            }


//             picturePath = getPath(getApplicationContext(), selectedImageUri);
            //Log.d("Picture Path", picturePath);
            Toast.makeText(this, picturePath, Toast.LENGTH_SHORT).show();
        }


        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            Uri selectedImageUri = data.getData();
            picturePath = getRealPathFromURI(selectedImageUri);


//            String[] projection = { MediaStore.Images.Media.DATA };
//            Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
//            if (cursor != null && cursor.moveToFirst()) {
//                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                String filePath = cursor.getString(columnIndex);
//                cursor.close();
//                // Do something with the file path, such as displaying it in a TextView
//               // TextView filePathTextView = findViewById(R.id.file_path_text_view);
//               // filePathTextView.setText(filePath);
//                picturePath=filePath;
//                Toast.makeText(this,""+ filePath, Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "picture"+picturePath, Toast.LENGTH_SHORT).show();
//            }


//             picturePath = getPath(getApplicationContext(), selectedImageUri);
            //Log.d("Picture Path", picturePath);
            Toast.makeText(this, picturePath, Toast.LENGTH_SHORT).show();


        }

    }

    public String getRealPathFromURI(Uri uri) {
        String result = null;
        String[] projection = {MediaStore.Files.FileColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            picturePath = filePath;
            result = filePath;

            // Do something with the file path, such as displaying it in a TextView
            // TextView filePathTextView = findViewById(R.id.file_path_text_view);
            //  filePathTextView.setText(filePath);
        }
        return result;

    }

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }


}