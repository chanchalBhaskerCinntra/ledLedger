package com.cinntra.ledure.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.AllTripExpenseAdapter;
import com.cinntra.ledure.adapters.ExpenseAdapter;
import com.cinntra.ledure.adapters.SelectedEmployeeForEventAdapter;
import com.cinntra.ledure.databinding.ExpenseDialogBinding;
import com.cinntra.ledure.databinding.ExpenseDialogInActivityBinding;
import com.cinntra.ledure.globals.Globals;

import com.cinntra.ledure.model.AllTripExpenseResponse;
import com.cinntra.ledure.model.DataAllTripExpense;
import com.cinntra.ledure.model.ExpenseDataModel;
import com.cinntra.ledure.model.ExpenseNewDataModel;
import com.cinntra.ledure.model.ExpenseNewModelResponse;
import com.cinntra.ledure.model.ExpenseResponse;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.model.SalesEmployeeItem;
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
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST_CODE = 11111;


    @BindView(R.id.customer_lead_List)
    RecyclerView recyclerView;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;


    double startlat;
    double startlong;
    double distanceMAP;
    String expenseString = "";

    String costSt = "";
    String distanceSt = "";
    String expenseRemarkSt = "";
    String attchmentName = "";
    String attachentUri = "";

    private String picturePath = "";

    EditText attachmentName;
    EditText etNewAttachemnt;
    FusedLocationProviderClient client;


    AlertDialog.Builder builder;
    AlertDialog dialogBuild;


    AlertDialog.Builder allTripBuilder;
    AlertDialog dialogTripBuild;


    @Override
    protected void onResume() {
        super.onResume();
        if (Globals.checkInternet(this)) {
            loader.setVisibility(View.VISIBLE);
            callexpenseapi("");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_campaign);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Expenses");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24);
        client = LocationServices.getFusedLocationProviderClient(ExpenseActivity.this);

        builder = new AlertDialog.Builder(ExpenseActivity.this);
        allTripBuilder = new AlertDialog.Builder(ExpenseActivity.this);

        builder.setTitle("Creating....")
                .setMessage("Please wait")
                .setCancelable(false);

        allTripBuilder.setTitle("Loading....")
                .setMessage("Please wait")
                .setCancelable(false);

        dialogBuild = builder.create();
        dialogTripBuild=allTripBuilder.create();

        getLatitudeLongitudeForCheckIn();
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Globals.checkInternet(ExpenseActivity.this)) {
                    loader.setVisibility(View.VISIBLE);
                    callexpenseapi("");
                } else
                    swipeRefreshLayout.setRefreshing(false);

            }
        });
    }

    ExpenseAdapter expenseAdapter;
    List<ExpenseNewDataModel> edp = new ArrayList<>();

    private void callexpenseapi(String customerName) {

        Call<ExpenseNewModelResponse> call = NewApiClient.getInstance().getApiService(this).getAllNewExpense();
        call.enqueue(new Callback<ExpenseNewModelResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<ExpenseNewModelResponse> call, Response<ExpenseNewModelResponse> response) {
                if (response.code() == 200) {
                    loader.setVisibility(View.GONE);
                    edp.clear();
                    edp.addAll(response.body().getData());
                    expenseAdapter = new ExpenseAdapter(ExpenseActivity.this, edp,"");
                    recyclerView.setLayoutManager(new LinearLayoutManager(ExpenseActivity.this, RecyclerView.VERTICAL, false));
                    recyclerView.setAdapter(expenseAdapter);
                    expenseAdapter.notifyDataSetChanged();
                } else {

                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, QuotationResponse.class);
                        Toast.makeText(ExpenseActivity.this, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
                nodatafoundimage();
                swipeRefreshLayout.setRefreshing(false);
                loader.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ExpenseNewModelResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                loader.setVisibility(View.GONE);
                Log.d("TAG", "onFailure: " + t.getMessage());
                Toast.makeText(ExpenseActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void nodatafoundimage() {
        if (expenseAdapter.getItemCount() == 0) {
            no_datafound.setVisibility(View.VISIBLE);
        } else {
            no_datafound.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.campaign_filter, menu);

        MenuItem item = menu.findItem(R.id.search);
        MenuItem itemPlus = menu.findItem(R.id.plus);
        SearchView searchView = new SearchView((this).getSupportActionBar().getThemedContext());
        itemPlus.setVisible(true);
        item.setVisible(true);

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setQueryHint("Search Expense");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("QUERY>>>>>>>", "onQueryTextChange: " + newText);
                if (expenseAdapter != null) {
                    expenseAdapter.filter(newText);
                }


                return true;
            }
        });


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:

                break;
            case R.id.plus:
                //  startActivity(new Intent(this, AddExpense.class));

                openExpenseDialog();
                break;


            case android.R.id.home:
                this.finish();
                return true;
        }
        return true;
    }


    double latAtCheckInMultipart;
    double longAtCheckInMultipart;

    @SuppressLint("MissingPermission")
    private void getLatitudeLongitudeForCheckIn() {
        // Initialize Location manager
        LocationManager locationManager
                = (LocationManager) ExpenseActivity.this
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
                                geocoder = new Geocoder(ExpenseActivity.this, Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(location
                                            .getLatitude(), location
                                            .getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                } catch (IOException e) {

                                    e.printStackTrace();
                                }


                                String address = addresses != null ? addresses.get(0).getAddressLine(0) : ""; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                           /* String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalCode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();*/
//                                callCheckInApi(imagePart, bptype, bpFullName,
//                                        cardCode, salesPersonCode, modeOfTransport,
//                                        checkInDate, CheckInTime,
//                                        location.getLatitude(), location.getLongitude(), checkinRemark);


//                                callApi(location
//                                        .getLatitude(), location
//                                        .getLongitude(), mapData, address);
                                latAtCheckInMultipart = location.getLatitude();
                                longAtCheckInMultipart = location.getLongitude();
                                Log.e("LAT AND LONG>>>>>", "onComplete: " + location.getLatitude() + "" + location.getLongitude());

                                //    Toast.makeText(MainActivity_B2C.this, "" + location.getLatitude() + "" + location.getLongitude(), Toast.LENGTH_SHORT).show();


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
                                        geocoder = new Geocoder(ExpenseActivity.this, Locale.getDefault());

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
                                        latAtCheckInMultipart = location1.getLatitude();
                                        longAtCheckInMultipart = location1.getLongitude();

                                        Toast.makeText(ExpenseActivity.this, "" + location1.getLatitude() + "" + location1.getLongitude(), Toast.LENGTH_SHORT).show();

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


    private ExpenseDialogInActivityBinding expenseDialogBinding;


    private void openExpenseDialog() {


        final Dialog dialog = new Dialog(ExpenseActivity.this);
        // dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        // dialog.setTitle("FollowUp FeedBack");
        expenseDialogBinding = ExpenseDialogInActivityBinding.inflate(getLayoutInflater());
        dialog.setContentView(expenseDialogBinding.getRoot());
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button add = dialog.findViewById(R.id.add);
        EditText comment_value = dialog.findViewById(R.id.comment_value);
        etNewAttachemnt = expenseDialogBinding.etAttachmentsName;


        Spinner expenseType = dialog.findViewById(R.id.spinnerTypeOfExpense);
        //    EditText expenseRemark = dialog.findViewById(R.id.comment_value);
        EditText cost = dialog.findViewById(R.id.etCostOfExpense);
        //  attachmentName = dialog.findViewById(R.id.etAttachmentsName);
        EditText distance = dialog.findViewById(R.id.etDistanceOfExpense);

        LinearLayout expenseFrom = dialog.findViewById(R.id.postingDate);
        EditText editTextExpenseFrom = dialog.findViewById(R.id.posting_value);
        EditText editTextExpenseTo = dialog.findViewById(R.id.expense_to_date);
        EditText editTextKm = dialog.findViewById(R.id.etDistanceKm);
        LinearLayout expenseToDate = dialog.findViewById(R.id.expenseToDate);
        Button btnChooseFile = dialog.findViewById(R.id.btnChooseFIle);
        ArrayAdapter<CharSequence> expenseAdapter;

        DecimalFormat df = new DecimalFormat("#.###");

        Double inMetre = distanceMAP * 1000;
        String formattedValue = df.format(inMetre);
        editTextKm.setText(formattedValue + " M");

      //  dialogTripBuild.show();
        expenseDialogBinding.loader.loader.setVisibility(View.VISIBLE);
        callExpenseApiTrip("","");


        expenseAdapter = ArrayAdapter.createFromResource(ExpenseActivity.this, R.array.new_type_of_expense_list, android.R.layout.simple_spinner_item);
        expenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseType.setAdapter(expenseAdapter);

        Calendar calendar = Calendar.getInstance();

        // Create date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        // Format date and time
        String date = dateFormat.format(calendar.getTime());
        String time = timeFormat.format(calendar.getTime());


        btnChooseFile.setOnClickListener(view -> {
            Intent intent = new Intent();

            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
        });


        expenseType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                expenseString = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                expenseString = expenseAdapter.getItem(0).toString();

            }
        });


        editTextExpenseFrom.setOnClickListener(view -> {

            Globals.selectDate(ExpenseActivity.this, editTextExpenseFrom);
        });

        editTextExpenseTo.setOnClickListener(view -> {

            Globals.selectDate(ExpenseActivity.this, editTextExpenseTo);
        });

        expenseDialogBinding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationExpense(cost, distance)) {
                    dialogBuild.show();
                    costSt = cost.getText().toString();
                    distanceSt = distance.getText().toString();
//                    ExpenseDataModel expense = new ExpenseDataModel();
//                    expense.setAttach(attachentUri);
//                    expense.setRemarks("");
//                    expense.setTripName(distanceSt);
//                    expense.setTypeOfExpense(expenseString);
//                    expense.setTravelDistance(String.valueOf(distanceMAP));
//                    expense.setStartLat(String.valueOf(startlat));
//                    expense.setStartLong(String.valueOf(startlong));
//                    expense.setEndLat("");
//                    expense.setEndLong("");
//                    expense.setExpenseFrom(editTextExpenseFrom.getText().toString());
//                    expense.setExpenseTo(editTextExpenseTo.getText().toString());
//
//                    expense.setTotalAmount(Integer.valueOf(costSt));
//                    expense.setCreateDate(date);
//                    expense.setCreateTime(time);
//                    expense.setCreatedBy(Prefs.getString(Globals.SalesEmployeeCode, ""));
//                    expense.setEmployeeId(Prefs.getString(Globals.EmployeeID, ""));

                    Calendar calendar = Calendar.getInstance();

                    // Create date format
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                    // Format date and time
                    String date = dateFormat.format(calendar.getTime());
                    String time = timeFormat.format(calendar.getTime());


                    editTextExpenseFrom.setOnClickListener(view1 -> {

                        Globals.selectDate(ExpenseActivity.this, editTextExpenseFrom);
                    });

                    editTextExpenseTo.setOnClickListener(view1 -> {

                        Globals.selectDate(ExpenseActivity.this, editTextExpenseTo);
                    });


                    File imageFile = new File(picturePath);
                    String curr_date = Globals.curntDate;

                    Log.e("filePath>>>>>", "onCreate: " + picturePath);
                    Log.e("fileNAme>>>>>", "onCreate: " + imageFile.getName());
                    costSt = expenseDialogBinding.etCostOfExpense.getText().toString();
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("Attach", imageFile.getName(), requestBody);
                    RequestBody tripName = RequestBody.create(MediaType.parse("multipart/form-data"), expenseDialogBinding.etDistanceOfExpense.getText().toString());
                    RequestBody cost = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(Integer.valueOf(costSt)));
                    RequestBody typeOfExpense = RequestBody.create(MediaType.parse("multipart/form-data"), expenseString);
                    RequestBody travelDistance = RequestBody.create(MediaType.parse("multipart/form-data"), " ");
                    RequestBody endLat = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(latAtCheckInMultipart));
                    RequestBody endLong = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(longAtCheckInMultipart));
                    RequestBody startlat = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(latAtCheckInMultipart));
                    RequestBody startlong = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(longAtCheckInMultipart));
                    RequestBody createDate = RequestBody.create(MediaType.parse("multipart/form-data"), date);
                    RequestBody createTime = RequestBody.create(MediaType.parse("multipart/form-data"), time);
                    RequestBody updateDate = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                    RequestBody updateTime = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                    RequestBody id = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                    RequestBody createBy = RequestBody.create(MediaType.parse("multipart/form-data"), Prefs.getString(Globals.SalesEmployeeCode, ""));
                    RequestBody employeeId = RequestBody.create(MediaType.parse("multipart/form-data"), Prefs.getString(Globals.EmployeeID, ""));
                    RequestBody expenseFrom = RequestBody.create(MediaType.parse("multipart/form-data"), editTextExpenseFrom.getText().toString());
                    RequestBody expenseTo = RequestBody.create(MediaType.parse("multipart/form-data"), editTextExpenseTo.getText().toString());
                    RequestBody remark = RequestBody.create(MediaType.parse("multipart/form-data"), expenseDialogBinding.commentValue.getText().toString());
                    RequestBody tripId = RequestBody.create(MediaType.parse("multipart/form-data"), "");

                    Call<ExpenseResponse> callExp = NewApiClient.getInstance()
                            .getApiService(ExpenseActivity.this).expense_create_multipart(imagePart, id, tripName, typeOfExpense,
                                    expenseFrom, expenseTo, cost, createDate, createTime, createBy, updateDate, updateTime, remark
                                    , employeeId, startlat, startlong, endLat, endLong, travelDistance,tripId);
                    callExp.enqueue(new Callback<ExpenseResponse>() {
                        @Override
                        public void onResponse(Call<ExpenseResponse> call, Response<ExpenseResponse> response) {
                            if (response != null && response.code() == 200) {
                                dialogBuild.dismiss();
                                Toast.makeText(ExpenseActivity.this, "Expense Added", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

//                                if (Prefs.getString(Globals.CheckMode, "CheckOut").equalsIgnoreCase("CheckOut")) {
//                                    Prefs.putString(Globals.CheckMode, "CheckIn");
//
//                                    binding.slideView.setText("Slide to Check Out");
//                                } else {
//                                    Prefs.putString(Globals.CheckMode, "CheckOut");
//
//                                    binding.slideView.setText("Slide to Check In");
//                                }
//                                Log.e("success", "success");


                            }
                        }

                        @Override
                        public void onFailure(Call<ExpenseResponse> call, Throwable t) {

                        }
                    });
                }


            }
        });


        dialog.show();

    }


    private boolean validationExpense(EditText companyname, EditText full_name) {

        if (companyname.length() == 0) {
            companyname.requestFocus();
            companyname.setError("Enter Cost");
            Toasty.warning(ExpenseActivity.this, "Enter Cost ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (full_name.length() == 0) {
            full_name.requestFocus();
            full_name.setError("Enter Trip");
            Toasty.warning(ExpenseActivity.this, "Enter Trip Name", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the URI of the selected file
            Uri uri = data.getData();
            attachentUri = uri.toString();
            attchmentName = getFileName(uri);
//            attachmentName.setText(attchmentName);
            expenseDialogBinding.etAttachmentsName.setText(attchmentName);
            picturePath = getRealPathFromURI(uri);
            //  Toast.makeText(this, picturePath, Toast.LENGTH_SHORT).show();


            // Use the URI to access the file
            // ...
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
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
            //  Toast.makeText(this, filePath, Toast.LENGTH_SHORT).show();

            // Do something with the file path, such as displaying it in a TextView
            // TextView filePathTextView = findViewById(R.id.file_path_text_view);
            //  filePathTextView.setText(filePath);
        }
        return result;

    }

    private ArrayList<DataAllTripExpense> dataAllTripExpenses = new ArrayList<>();

    private void callExpenseApiTrip(String fromdate, String toDate) {


        HashMap<String, String> mapData = new HashMap<>();
        mapData.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode, "1"));
        mapData.put("FromDate", fromdate);
        mapData.put("ToDate", toDate);
//        mapData.put("UpdateDate", dateText.getText().toString());
//        mapData.put("shape", "");

        Call<AllTripExpenseResponse> call = NewApiClient.getInstance().getApiService(this).getAllTripExpense(mapData);
        call.enqueue(new Callback<AllTripExpenseResponse>() {
            @Override
            public void onResponse(Call<AllTripExpenseResponse> call, Response<AllTripExpenseResponse> response) {
                if (response != null) {
                    ArrayList list = new ArrayList<DataAllTripExpense>();
                    list.addAll(response.body().getData());
                 //   binding.layoutLoader.loader.setVisibility(View.GONE);
                    //dialogTripBuild.dismiss();
                    expenseDialogBinding.loader.loader.setVisibility(View.GONE);
                    dataAllTripExpenses.clear();
                    dataAllTripExpenses.addAll(response.body().getData());
                    List<String> itemNames = new ArrayList<>();
                    //  List<String> itemCode = new ArrayList<>();
                    for (DataAllTripExpense item : dataAllTripExpenses) {
                        itemNames.add(item.getBPName());
                       // itemNames.add(i);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ExpenseActivity.this, android.R.layout.simple_dropdown_item_1line, itemNames);

                    expenseDialogBinding.etDistanceOfExpense.setAdapter(adapter);

                    expenseDialogBinding.etDistanceOfExpense.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String selectedData = (String) parent.getItemAtPosition(position);

//                            if (selectedData.isEmpty()){
//                                rvChipItem.setVisibility(View.GONE);
//                            }else {
//                                rvChipItem.setVisibility(View.VISIBLE);
//                            }
//
//
//                            if (!selectedData.isEmpty() && !selectedDataList.contains(selectedData)) {
//                                //   rvChipItem.setVisibility(View.VISIBLE);
//                                selectedDataList.add(selectedData);
//                                adapter.notifyDataSetChanged();
//                                Log.e("selected", "onItemClick: " + selectedDataList.size());
//
//                                gridLayoutManager = new GridLayoutManager(requireActivity(),3);
//                                adapterEmp = new SelectedEmployeeForEventAdapter(requireContext(), selectedDataList);
//                                rvChipItem.setLayoutManager(gridLayoutManager);
//                                rvChipItem.setAdapter(adapterEmp);
//                                adapterEmp.notifyDataSetChanged();
//                                adapter.notifyDataSetChanged();
//
//
//                            }

                            // Clear the AutoCompleteTextView after selecting the item
                            expenseDialogBinding.etDistanceOfExpense.setText(selectedData);
                        }
                    });





//                    Iterator<DataAllTripExpense> iterator = list.iterator();
//                    while (iterator.hasNext()) {
//
//                        String item = iterator.next().getBPName();
//                        if (item.isEmpty()) {
//                            iterator.remove();
//                        }
//                    }


                    //  dataAllTripExpenses.addAll(response.body().getData().get(0));


               //     allTripExpenseAdapter = new AllTripExpenseAdapter(LocationListing.this, dataAllTripExpenses);
                   // layoutManager = new LinearLayoutManager(LocationListing.this, RecyclerView.VERTICAL, false);
                    //   recyclerView.setLayoutManager(layoutManager);
                  //  recyclerView.setAdapter(allTripExpenseAdapter);
                    //  allTripExpenseAdapter.notifyDataSetChanged();
                  //  nodatafoundimageExpense();

                }
            }

            @Override
            public void onFailure(Call<AllTripExpenseResponse> call, Throwable t) {
                expenseDialogBinding.loader.loader.setVisibility(View.GONE);
             //   dialogTripBuild.dismiss();
              //  binding.layoutLoader.loader.setVisibility(View.GONE);
            }
        });

    }


    private void showTripNameDialog() {
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("loading")
                .setCancelable(false);
        androidx.appcompat.app.AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }



}
