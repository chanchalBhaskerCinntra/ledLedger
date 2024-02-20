package com.cinntra.ledure.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.NotificationAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.interfaces.FragmentRefresher;
import com.cinntra.ledure.model.NotificationResponse;
import com.cinntra.ledure.model.NotificatonValue;
import com.cinntra.ledure.model.SalesEmployeeItem;
import com.cinntra.ledure.webservices.NewApiClient;
import com.github.ybq.android.spinkit.SpinKitView;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notifications extends MainBaseActivity implements View.OnClickListener, FragmentRefresher {

    private AppCompatActivity activity;
    @BindView(R.id.notificationList)
    RecyclerView notificationList;
    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.No_notification_view)
    LinearLayout No_Notification_view;
    @BindView(R.id.notificationView)
    LinearLayout notificationView;
    @BindView(R.id.loader)
    SpinKitView loader;

    ArrayList<NotificatonValue> notificationData = new ArrayList<>();

    @Override
    protected void onResume() {
    super.onResume();
     }

    @Override
    protected void onCreate(Bundle savedInstanceState)
          {
    super.onCreate(savedInstanceState);
     activity = Notifications.this;
     setContentView(R.layout.activity_notifications);
     ButterKnife.bind(this);


     back_press.setOnClickListener(this);
     if (Globals.checkInternet(Notifications.this))
       callApi();
      swipeManager();



        }

    private void callApi() {
        loader.setVisibility(View.VISIBLE);
        SalesEmployeeItem salesEmployeeItem = new SalesEmployeeItem();
        salesEmployeeItem.setEmp(Prefs.getString(Globals.EmployeeID,""));
        Call<NotificationResponse> call = NewApiClient.getInstance().getApiService().allnotification(salesEmployeeItem);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response != null)
                {
                    if(response.body().getData().size()>0){

                        notificationData.clear();
                        notificationData.addAll(response.body().getData());
                        notificationList.setLayoutManager(new LinearLayoutManager(Notifications.this,RecyclerView.VERTICAL,false));
                        notificationList.setAdapter(new NotificationAdapter(activity,notificationData));
                    }else{
                        No_Notification_view.setVisibility(View.VISIBLE);
                        notificationView.setVisibility(View.GONE);

                    }
                    loader.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);

                }
            }
            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                swipeContainer.setRefreshing(false);
                loader.setVisibility(View.GONE);
            }
        });
    }

    private void swipeManager() {
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
     @Override
     public void onRefresh() {
      /*new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
        {
      @Override public void run() {

         }}, 2000);*/
         if(Globals.checkInternet(Notifications.this))
             callApi();
         else
         swipeContainer.setRefreshing(false);
            }
        });
    }


    @Override
    public void onClick(View v) {
    finish();
      }

    @Override
    public void onRefresh() {
        if(Globals.checkInternet(Notifications.this)) {
            loader.setVisibility(View.VISIBLE);
            callApi();
        }
    }
}