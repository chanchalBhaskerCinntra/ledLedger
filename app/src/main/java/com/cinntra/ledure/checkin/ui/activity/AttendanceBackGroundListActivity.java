package com.cinntra.ledure.checkin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cinntra.ledure.adapters.BackgroundLocationAdapter;
import com.cinntra.ledure.databinding.ActivityAttendanceBackGroundListBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.ResponseTripCheckIn;
import com.cinntra.ledure.newapimodel.ResponseBackGroundLocation;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.JsonObject;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceBackGroundListActivity extends AppCompatActivity {
    private ActivityAttendanceBackGroundListBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAttendanceBackGroundListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.tvNameOfEmployee.setText(Prefs.getString(Globals.SalesEmployeeName, ""));

        getListing();
    }

    private BackgroundLocationAdapter adapter;
    private ArrayList<ResponseBackGroundLocation.Datum> AllItemList = new ArrayList<>();

    private LinearLayoutManager layoutManager;

    private void getListing() {
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
        hde.addProperty("DateFilter", Globals.getTodaysDatervrsfrmt());


        Call<ResponseBackGroundLocation> call;
        call = NewApiClient.getInstance().getApiService(this).galaxyTrackingFilter(hde);


        call.enqueue(new Callback<ResponseBackGroundLocation>() {
            @Override
            public void onResponse(Call<ResponseBackGroundLocation> call, Response<ResponseBackGroundLocation> response) {
                if (response != null) {
                    if (response.body().getStatus().equalsIgnoreCase("200") && response.body().getStatus() != null) {
                        binding.etKm.setText(response.body().getTotal_distance());

                        AllItemList.clear();
                        AllItemList.addAll(response.body().getData());
                        layoutManager = new LinearLayoutManager(AttendanceBackGroundListActivity.this, RecyclerView.VERTICAL, false);
                        adapter = new BackgroundLocationAdapter(AllItemList, AttendanceBackGroundListActivity.this);
                        binding.rvBackGroundLocation.setLayoutManager(layoutManager);
                        binding.rvBackGroundLocation.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                     /*   if (response.body().getCustomerLedgerRes().size() == 0) {
                            binding.noDatafound.setVisibility(View.VISIBLE);
                        } else {
                            binding.noDatafound.setVisibility(View.INVISIBLE);
                        }*/
                        // binding.loaderLayout.loader.setVisibility(View.GONE);


                        Log.e(TAG, "onResponseBackground: " + response.body().getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBackGroundLocation> call, Throwable t) {
                // binding.loaderLayout.loader.setVisibility(View.GONE);
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    private static final String TAG = "AttendanceBackGroundLis";
}