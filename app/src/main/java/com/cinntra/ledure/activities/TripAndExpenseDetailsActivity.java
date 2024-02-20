package com.cinntra.ledure.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.bumptech.glide.Glide;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.AllTripExpenseAdapter;
import com.cinntra.ledure.adapters.ExpenseAdapter;
import com.cinntra.ledure.databinding.ActivityTripAndExpenseDetailsBinding;
import com.cinntra.ledure.databinding.ExpenseDialogInActivityBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.AllTripExpenseResponse;
import com.cinntra.ledure.model.DataAllTripExpense;
import com.cinntra.ledure.model.ExpenseNewDataModel;
import com.cinntra.ledure.model.ExpenseResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripAndExpenseDetailsActivity extends AppCompatActivity {
    ActivityTripAndExpenseDetailsBinding binding;
    String id;
    FusedLocationProviderClient client;
    public String tripIdGlobal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTripAndExpenseDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        id = getIntent().getStringExtra("id");
        Log.e("TAG", "onCreate" + id);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Trip Details");
        builder = new AlertDialog.Builder(TripAndExpenseDetailsActivity.this);
        builder.setTitle("Creating....")
                .setMessage("Please wait")
                .setCancelable(false);
        dialogBuild = builder.create();
        client = LocationServices.getFusedLocationProviderClient(TripAndExpenseDetailsActivity.this);


        if (Globals.checkInternet(TripAndExpenseDetailsActivity.this)) {
            binding.loader.setVisibility(View.VISIBLE);
            callOneApi(id);
        }

        binding.currentView.setOnClickListener(view -> {
            if (binding.constraintDetails.getVisibility() == View.VISIBLE) {

            }
            binding.constraintDetails.setVisibility(View.VISIBLE);
            binding.constraintExpense.setVisibility(View.GONE);
            binding.currentText.setTextColor(getResources().getColor(R.color.colorPrimary));
            binding.pastText.setTextColor(getResources().getColor(R.color.lightGrey));
        });


        binding.pastView.setOnClickListener(view -> {
            if (binding.constraintDetails.getVisibility() == View.VISIBLE) {

            }
            binding.constraintDetails.setVisibility(View.GONE);
            binding.constraintExpense.setVisibility(View.VISIBLE);
            binding.currentText.setTextColor(getResources().getColor(R.color.lightGrey));
            binding.pastText.setTextColor(getResources().getColor(R.color.colorPrimary));

        });

        binding.swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Globals.checkInternet(TripAndExpenseDetailsActivity.this)) {

                    callOneApi(id);
                } else
                    binding.swipeRefreshLayout.setRefreshing(false);

            }
        });


    }


    ExpenseAdapter expenseAdapter;
    List<ExpenseNewDataModel> edp = new ArrayList<>();

    private void callOneApi(String id) {


        HashMap<String, String> mapData = new HashMap<>();

        mapData.put("id", id);

//        mapData.put("UpdateDate", dateText.getText().toString());
//        mapData.put("shape", "");

        Call<AllTripExpenseResponse> call = NewApiClient.getInstance().getApiService().trip_expense_one(mapData);
        call.enqueue(new Callback<AllTripExpenseResponse>() {
            @Override
            public void onResponse(Call<AllTripExpenseResponse> call, Response<AllTripExpenseResponse> response) {
                if (response != null && response.code() == 200) {
                    binding.loader.setVisibility(View.GONE);
                    binding.tvCheckInDate.setText(response.body().getData().get(0).getCheckInDate());
                    binding.tvCheckInTime.setText(response.body().getData().get(0).getCheckInTime());
                    binding.tvCheckOutDate.setText(response.body().getData().get(0).getCheckOutDate());
                    binding.tvCheckOutTime.setText(response.body().getData().get(0).getCheckOutTime());
                    binding.tvmodeOfTransport.setText(response.body().getData().get(0).getModeOfTransport());
                    binding.tvDistance.setText(response.body().getData().get(0).getTotalDistanceAuto());
                    binding.tvCustomerName.setText(response.body().getData().get(0).getBPName());
                    binding.tvTripId.setText(response.body().getData().get(0).getId());
                    binding.tvCheckInRemarks.setText(response.body().getData().get(0).getCheckInRemarks());
                    binding.tvCheckOutRemarks.setText(response.body().getData().get(0).getCheckOutRemarks());
                    binding.tvRemarksExpnse.setText(response.body().getData().get(0).getCheckOutRemarks());
                    binding.tvDateExpense.setText(response.body().getData().get(0).getCheckOutDate());
                    binding.tvTripIdExpense.setText(response.body().getData().get(0).getId());
                    tripIdGlobal = response.body().getData().get(0).getId();

                    if (response.body().getData().get(0).getExpenses().size() > 0) {
                        edp.clear();
                        edp.addAll(response.body().getData().get(0).getExpenses());
                        expenseAdapter = new ExpenseAdapter(TripAndExpenseDetailsActivity.this, edp, response.body().getData().get(0).getBPName());
                        binding.rvExpenseOfTrip.setLayoutManager(new LinearLayoutManager(TripAndExpenseDetailsActivity.this, RecyclerView.VERTICAL, false));
                        binding.rvExpenseOfTrip.setAdapter(expenseAdapter);
                        expenseAdapter.notifyDataSetChanged();
                        binding.swipeRefreshLayout.setRefreshing(false);
                        binding.noDatafound.setVisibility(View.GONE);

                    } else {
                        binding.noDatafound.setVisibility(View.VISIBLE);
                    }


                    if (!response.body().getData().get(0).getTotalExpenses().isEmpty()) {
                        binding.tvTotalAmountExpense.setText("₹ " + response.body().getData().get(0).getTotalExpenses());
                    }

                    binding.tvTypeOfExpense.setText(" Trip");

                    Glide.with(TripAndExpenseDetailsActivity.this).load(Globals.attachmentBaseUrl + response.body().getData().get(0).getCheckInAttach()).into(binding.ivCheckINImage);
                    Glide.with(TripAndExpenseDetailsActivity.this).load(Globals.attachmentBaseUrl + response.body().getData().get(0).getCheckOutAttach()).into(binding.ivCheckOutImage);


                    if (response.body().getData().get(0).getCheckOutDate().isEmpty()) {
                        binding.headingCheckOutDetails.setVisibility(View.GONE);
                        binding.ivCheckOutImage.setVisibility(View.GONE);
                        binding.linearCheckOutDate.setVisibility(View.GONE);
                        binding.linearCheckOutRemark.setVisibility(View.GONE);
                        binding.linearCheckOutTime.setVisibility(View.GONE);
                    } else {
                        binding.headingCheckOutDetails.setVisibility(View.VISIBLE);
                        binding.ivCheckOutImage.setVisibility(View.VISIBLE);
                        binding.linearCheckOutDate.setVisibility(View.VISIBLE);
                        binding.linearCheckOutRemark.setVisibility(View.VISIBLE);
                        binding.linearCheckOutTime.setVisibility(View.VISIBLE);
                    }

                    binding.headingCheckInDetails.setOnClickListener(view -> {
                        Intent intent = new Intent(TripAndExpenseDetailsActivity.this, GoToMapActivity.class);
                        intent.putExtra("lat", response.body().getData().get(0).getCheckInLat());
                        intent.putExtra("long", response.body().getData().get(0).getCheckInLong());
                        startActivity(intent);
                    });

                    binding.headingCheckOutDetails.setOnClickListener(view -> {
                        Intent intent = new Intent(TripAndExpenseDetailsActivity.this, GoToMapActivity.class);
                        intent.putExtra("lat", response.body().getData().get(0).getCheckOutLat());
                        intent.putExtra("long", response.body().getData().get(0).getCheckOutLong());
                        startActivity(intent);
                    });


                    //    ArrayList list = new ArrayList<DataAllTripExpense>();
                    //list.addAll(response.body().getData());

                    //  dataAllTripExpenses.clear();
//                    Iterator<DataAllTripExpense> iterator = list.iterator();
//                    while (iterator.hasNext()) {
//
//                        String item = iterator.next().getBPName();
//                        if (item.isEmpty()) {
//                            iterator.remove();
//                        }
//                    }


                    //  dataAllTripExpenses.addAll(response.body().getData().get(0));

//                    dataAllTripExpenses.addAll(response.body().getData());
//                    allTripExpenseAdapter = new AllTripExpenseAdapter(LocationListing.this, dataAllTripExpenses);
//                    layoutManager = new LinearLayoutManager(LocationListing.this, RecyclerView.VERTICAL, false);
//                    recyclerView.setLayoutManager(layoutManager);
//                    recyclerView.setAdapter(allTripExpenseAdapter);
//                    //  allTripExpenseAdapter.notifyDataSetChanged();
//                    nodatafoundimageExpense();

                }
            }

            @Override
            public void onFailure(Call<AllTripExpenseResponse> call, Throwable t) {
                binding.loader.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.campaign_filter, menu);

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = new SearchView((this).getSupportActionBar().getThemedContext());
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.plus).setVisible(true);
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
                //   startActivity(new Intent(this, AddExpense.class));
                openExpenseDialog();
                break;


            case android.R.id.home:
                this.finish();
                return true;
        }
        return true;
    }


    private ExpenseDialogInActivityBinding expenseDialogBinding;
    EditText etNewAttachemnt;
    double distanceMAP;
    String expenseString = "";
    private static final int PICK_FILE_REQUEST_CODE = 11111;
    AlertDialog.Builder builder;
    AlertDialog dialogBuild;
    String costSt = "";
    String distanceSt = "";
    String expenseRemarkSt = "";
    String attchmentName = "";
    String attachentUri = "";

    private String picturePath = "";

    double latAtCheckInMultipart;
    double longAtCheckInMultipart;

    @SuppressLint("MissingPermission")
    private void getLatitudeLongitudeForCheckIn() {
        // Initialize Location manager
        LocationManager locationManager
                = (LocationManager) TripAndExpenseDetailsActivity.this
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
                                geocoder = new Geocoder(TripAndExpenseDetailsActivity.this, Locale.getDefault());

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
                                        geocoder = new Geocoder(TripAndExpenseDetailsActivity.this, Locale.getDefault());

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

                                        Toast.makeText(TripAndExpenseDetailsActivity.this, "" + location1.getLatitude() + "" + location1.getLongitude(), Toast.LENGTH_SHORT).show();

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

    private void openExpenseDialog() {


        final Dialog dialog = new Dialog(TripAndExpenseDetailsActivity.this);
        // dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        // dialog.setTitle("FollowUp FeedBack");
        expenseDialogBinding = ExpenseDialogInActivityBinding.inflate(getLayoutInflater());
        dialog.setContentView(expenseDialogBinding.getRoot());
        expenseDialogBinding.loader.loader.setVisibility(View.GONE);

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button add = dialog.findViewById(R.id.add);
        EditText comment_value = dialog.findViewById(R.id.comment_value);
        etNewAttachemnt = expenseDialogBinding.etAttachmentsName;
        expenseDialogBinding.etDistanceOfExpense.setVisibility(View.GONE);
        expenseDialogBinding.headingDistance.setVisibility(View.GONE);


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
        expenseDialogBinding.loader.loader.setVisibility(View.GONE);
        // callExpenseApiTrip("","");


        expenseAdapter = ArrayAdapter.createFromResource(TripAndExpenseDetailsActivity.this, R.array.new_type_of_expense_list, android.R.layout.simple_spinner_item);
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

            Globals.selectDate(TripAndExpenseDetailsActivity.this, editTextExpenseFrom);
        });

        editTextExpenseTo.setOnClickListener(view -> {

            Globals.selectDate(TripAndExpenseDetailsActivity.this, editTextExpenseTo);
        });

        expenseDialogBinding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationExpense(cost, expenseDialogBinding.etAttachmentsName, expenseDialogBinding.commentValue)) {
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

                        Globals.selectDate(TripAndExpenseDetailsActivity.this, editTextExpenseFrom);
                    });

                    editTextExpenseTo.setOnClickListener(view1 -> {

                        Globals.selectDate(TripAndExpenseDetailsActivity.this, editTextExpenseTo);
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
                    RequestBody tripId = RequestBody.create(MediaType.parse("multipart/form-data"), tripIdGlobal);
                    Call<ExpenseResponse> callExp = NewApiClient.getInstance()
                            .getApiService().expense_create_multipart(imagePart, id, tripName, typeOfExpense,
                                    expenseFrom, expenseTo, cost, createDate, createTime, createBy, updateDate, updateTime, remark
                                    , employeeId, startlat, startlong, endLat, endLong, travelDistance, tripId);
                    callExp.enqueue(new Callback<ExpenseResponse>() {
                        @Override
                        public void onResponse(Call<ExpenseResponse> call, Response<ExpenseResponse> response) {
                            if (response != null && response.code() == 200) {
                                dialogBuild.dismiss();
                                Toast.makeText(TripAndExpenseDetailsActivity.this, "Expense Added", Toast.LENGTH_SHORT).show();
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


    private boolean validationExpense(EditText companyname, EditText full_name, EditText remarks) {

        if (companyname.length() == 0) {
            companyname.requestFocus();
            companyname.setError("Enter Cost");
            Toasty.warning(TripAndExpenseDetailsActivity.this, "Enter Cost ", Toast.LENGTH_SHORT).show();
            return false;

        } else if (full_name.length() == 0) {
            full_name.requestFocus();
            full_name.setError("Please attach a file");
            Toasty.warning(TripAndExpenseDetailsActivity.this, "Please attach a file", Toast.LENGTH_SHORT).show();

            return false;
        } else if (remarks.length() == 0) {
            remarks.requestFocus();
            remarks.setError("Enter Remark");
            Toasty.warning(TripAndExpenseDetailsActivity.this, "Enter Remark", Toast.LENGTH_SHORT).show();

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


}