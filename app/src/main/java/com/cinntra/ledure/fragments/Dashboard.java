package com.cinntra.ledure.fragments;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.BussinessPartners;
import com.cinntra.ledure.activities.CampaignActivity;
import com.cinntra.ledure.activities.DeliveryActivity;
import com.cinntra.ledure.activities.InventoryActivity;
import com.cinntra.ledure.activities.InvoiceActivity;
import com.cinntra.ledure.activities.LeadsActivity;
import com.cinntra.ledure.activities.Notifications;
import com.cinntra.ledure.activities.Opportunities_Pipeline_Activity;
import com.cinntra.ledure.activities.OrderActivity;
import com.cinntra.ledure.activities.QuotationActivity;
import com.cinntra.ledure.adapters.DeliveryItemAdapter;
import com.cinntra.ledure.adapters.GridViewAdapter;
import com.cinntra.ledure.adapters.InventoryAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.interfaces.ChangeTeam;
import com.cinntra.ledure.model.CounterResponse;
import com.cinntra.ledure.model.CountryData;
import com.cinntra.ledure.model.CountryResponse;
import com.cinntra.ledure.model.EmployeeValue;
import com.cinntra.ledure.model.HeirarchiResponse;
import com.cinntra.ledure.model.Inventory;
import com.cinntra.ledure.model.SalesEmployeeItem;
import com.cinntra.ledure.viewModel.ItemViewModel;
import com.cinntra.ledure.webservices.NewApiClient;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends Fragment implements View.OnClickListener, ChangeTeam {
    @BindView(R.id.invetory_recycler)
    RecyclerView invetory_recycler;
    @BindView(R.id.delivery_recycler)
    RecyclerView delivery_recycler;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.view_all_delivery)
    TextView view_all_delivery;
    @BindView(R.id.see_all)
    TextView see_all;
    @BindView(R.id.balnce)
    TextView balnce;
    @BindView(R.id.project_sale)
    TextView project_sale;
    @BindView(R.id.different_sale)
    TextView different_sale;
    @BindView(R.id.grid)
    GridView grid;
    @BindView(R.id.lead_card)
    CardView lead_card;
    @BindView(R.id.opportunity_card)
    CardView opportunity_card;
    @BindView(R.id.quotation_card)
    CardView quotation_card;
    @BindView(R.id.order_card)
    CardView order_card;
    @BindView(R.id.invoices_card)
    CardView invoices_card;
    @BindView(R.id.userRole)
    LinearLayout userRole;
    @BindView(R.id.notification)
    ImageView notification;
    @BindView(R.id.lead_counter)
    TextView lead_counter;
    @BindView(R.id.opportunity_counter)
    TextView opportunity_counter;
    @BindView(R.id.userRole_txt)
    TextView userRole_txt;
    @BindView(R.id.quotation_counter)
    TextView quotation_counter;
    @BindView(R.id.order_counter)
    TextView order_counter;
    @BindView(R.id.invoices_counter)
    TextView invoices_counter;
    @BindView(R.id.customer_counter)
    TextView customer_counter;
    @BindView(R.id.count_layout)
    ConstraintLayout count_layout;
    @BindView(R.id.customer_card)
    CardView customer_card;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.pro_pic)
    TextView pro_pic;
    @BindView(R.id.count)
    TextView count;
    @BindView(R.id.campaign_counter)
    TextView campaign_counter;
    @BindView(R.id.campaign_card)
    CardView campaign_card;

    @BindView(R.id.notification_view)
    RelativeLayout notification_view;
    DeliveryItemAdapter adpt;
    InventoryAdapter intAdpt;


    String userTyep;
    /*String []mainItems = {"Lead Generation/Opportunities","Inventory","Sales Quotation",
    "Sales Order","Delivery","Invoice "};*/
    String[] mainItems = {"Opportunities", "Quotations", "Orders", "Invoice "};
    String[] inventoryItem = {"Fast Moving", "Slow Moving", "Non Moving"};
    int[] inventoryIcons = {R.drawable.ic_inventory, R.drawable.ic_inventory, R.drawable.ic_inventory};
    String[] deliveryItem = {"Overdue", "Open", "Closed"};
    String[] colorarray = {"#F3425F", "#763EE7", "#1878F3"};
    int[] deliveryIcons = {R.drawable.ic_delivery_overdue, R.drawable.ic_delivery_open, R.drawable.ic_delivery_closed};

    int[] images = {R.drawable.ic_opportunity, R.drawable.ic_quotation, R.drawable.ic_order, R.drawable.ic_invoice};

    public Dashboard() {
        //Required empty public constructor

    }


    // TODO: Rename and change types and number of parameters
    public static Dashboard newInstance(String param1, String param2) {
        Dashboard fragment = new Dashboard();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setDashboardCounters();
        userRole_txt.setText(Prefs.getString(Globals.SalesEmployeeName, ""));
        Globals.inventory_item_close = false;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard2, container, false);
        ButterKnife.bind(this, v);
        Globals.CURRENT_CLASS = getClass().getName();


        userTyep = Prefs.getString(Globals.USER_TYPE, "manager");
        if (Globals.checkInternet(getActivity())) {
            setDefaults(userTyep);
            //setSqlDetails();
            user_name.setText(Prefs.getString(Globals.SalesEmployeeName, ""));
            pro_pic.setText(String.valueOf(Prefs.getString(Globals.Employee_Name, "").charAt(0)).toUpperCase());
        }


        return v;
    }

    public static ArrayList<CountryData> countrylist = new ArrayList<>();

    private void setDefaults(String type) {

        callCountryApi();

        loadHeirarchi();


        GridViewAdapter adapter = new GridViewAdapter(getActivity(), mainItems, images);
        grid.setAdapter(adapter);

        view_all_delivery.setOnClickListener(this);
        see_all.setOnClickListener(this);
        opportunity_card.setOnClickListener(this);
        quotation_card.setOnClickListener(this);
        order_card.setOnClickListener(this);
        invoices_card.setOnClickListener(this);
        userRole.setOnClickListener(this);
        notification.setOnClickListener(this);
        customer_card.setOnClickListener(this);
        pro_pic.setOnClickListener(this);
        notification_view.setOnClickListener(this);
        lead_card.setOnClickListener(this);
        campaign_card.setOnClickListener(this);


        intAdpt = new InventoryAdapter(getActivity(), inventoryItem, inventoryIcons);
        invetory_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        invetory_recycler.setAdapter(intAdpt);
        invetory_recycler.smoothScrollToPosition(0);
        intAdpt.notifyDataSetChanged();


    }

    private void callCountryApi() {

        Call<CountryResponse> call = NewApiClient.getInstance().getApiService(getActivity()).getCountryList();
        call.enqueue(new Callback<CountryResponse>() {
            @Override
            public void onResponse(Call<CountryResponse> call, Response<CountryResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getData().size() > 0) {
                        countrylist.clear();
                        countrylist.addAll(response.body().getData());
                    }


                }
            }

            @Override
            public void onFailure(Call<CountryResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_all_delivery:
                startActivity(new Intent(getActivity(), DeliveryActivity.class));
                break;
            case R.id.see_all:
                Intent i = new Intent(getActivity(), InventoryActivity.class);
                i.putExtra("IN_Type", "All");
                startActivity(i);
                break;
            case R.id.lead_card:
                Prefs.putString(Globals.BussinessPageType, "DashBoard");
                startActivity(new Intent(getActivity(), LeadsActivity.class));
                break;
            case R.id.opportunity_card:
                // startActivity(new Intent(getActivity(), OpportunitiesActivity.class));
                Prefs.putString(Globals.SelectOpportnity, "Dashboard");
                startActivity(new Intent(getActivity(), Opportunities_Pipeline_Activity.class));
                break;
            case R.id.quotation_card:
                Prefs.putString(Globals.QuotationListing, "null");
                Prefs.putBoolean(Globals.SelectQuotation, true);
                startActivity(new Intent(getActivity(), QuotationActivity.class));
                break;
            case R.id.order_card:
                startActivity(new Intent(getActivity(), OrderActivity.class));
                break;
            case R.id.invoices_card:
                startActivity(new Intent(getActivity(), InvoiceActivity.class));
                break;
            case R.id.campaign_card:
                startActivity(new Intent(getActivity(), CampaignActivity.class));

                break;
            case R.id.userRole:
                //createPdf();
                showBottomSheet();
                break;
            case R.id.notification:
            case R.id.notification_view:
                // openNotification();
                //startActivity(new Intent(getActivity(), Navigation_drawer.class));
                // startActivity(new Intent(getActivity(), AdminMainActivity.class));
                // startActivity(new Intent(getActivity(), MapsActivity.class));
                startActivity(new Intent(getActivity(), Notifications.class));
                break;
            case R.id.customer_card:
                Prefs.putString(Globals.BussinessPageType, "DashBoard");
                startActivity(new Intent(getActivity(), BussinessPartners.class));
                break;
            case R.id.pro_pic:
                // startActivity(new Intent(getActivity(), AdminMainActivity.class));
                break;
        }
    }


    private void showBottomSheet() {

        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment(Dashboard.this);
        bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());

    }

    private void setDashboardCounters() {
        dashboardcounter();
    }


    public static ArrayList<EmployeeValue> teamList_Hearchi = new ArrayList<>();

    private void loadHeirarchi() {
        EmployeeValue employeeValue = new EmployeeValue();
        employeeValue.setSalesEmployeeCode(Globals.TeamSalesEmployeCode);
        Call<HeirarchiResponse> call = NewApiClient.getInstance().getApiService(getActivity()).getAllEmployeelist(employeeValue);
        call.enqueue(new Callback<HeirarchiResponse>() {
            @Override
            public void onResponse(Call<HeirarchiResponse> call, Response<HeirarchiResponse> response) {
                if (response != null) {
                    teamList_Hearchi.clear();
                    if (response.body().getData().size() > 0) {
                        teamList_Hearchi.addAll(response.body().getData());
                    }


                }
            }

            @Override
            public void onFailure(Call<HeirarchiResponse> call, Throwable t) {

            }
        });
    }


    private void dashboardcounter() {
        SalesEmployeeItem salesEmployeeItem = new SalesEmployeeItem();
        salesEmployeeItem.setSalesEmployeeCode(Prefs.getString(Globals.SalesEmployeeCode, ""));
        Call<CounterResponse> call = NewApiClient.getInstance().getApiService(getActivity()).dashboardcounter(salesEmployeeItem);
        call.enqueue(new Callback<CounterResponse>() {
            @Override
            public void onResponse(Call<CounterResponse> call, Response<CounterResponse> response) {
                if (response != null) {

                    String[] deliverycounter = new String[3];
                    deliverycounter[0] = response.body().getValue().get(0).getOver();
                    deliverycounter[1] = response.body().getValue().get(0).getOpen();
                    deliverycounter[2] = response.body().getValue().get(0).getClose();
                    delivery_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
                    adpt = new DeliveryItemAdapter(getActivity(), deliveryItem, deliveryIcons, deliverycounter);
                    delivery_recycler.setAdapter(adpt);
                    adpt.notifyDataSetChanged();
                    lead_counter.setText(response.body().getValue().get(0).getLeads());
                    balnce.setText("₹ " + response.body().getValue().get(0).getSale());
                    different_sale.setText("₹ " + response.body().getValue().get(0).getSale_diff());
                    project_sale.setText("₹ " + response.body().getValue().get(0).getAmount());
                    opportunity_counter.setText(response.body().getValue().get(0).getOpportunity());
                    quotation_counter.setText(response.body().getValue().get(0).getQuotation());
                    order_counter.setText(response.body().getValue().get(0).getOrder());
                    customer_counter.setText(response.body().getValue().get(0).getCustomer());
                    campaign_counter.setText(response.body().getValue().get(0).getCampaign_count());
                    if (response.body().getValue().get(0).getNotification().equalsIgnoreCase("0")) {
                        count_layout.setVisibility(View.GONE);
                    } else {
                        count_layout.setVisibility(View.VISIBLE);
                        count.setText(response.body().getValue().get(0).getNotification());
                    }
                    adpt.notifyDataSetChanged();

                    Invoicescounter();
                }
            }

            @Override
            public void onFailure(Call<CounterResponse> call, Throwable t) {

            }
        });
    }


    private void Invoicescounter() {
        SalesEmployeeItem salesEmployeeItem = new SalesEmployeeItem();
        salesEmployeeItem.setSalesEmployeeCode(Prefs.getString(Globals.SalesEmployeeCode, ""));
        Call<CounterResponse> call = NewApiClient.getInstance().getApiService(getActivity()).InvoicesCount(salesEmployeeItem);
        call.enqueue(new Callback<CounterResponse>() {
            @Override
            public void onResponse(Call<CounterResponse> call, Response<CounterResponse> response) {
                if (response != null && response.code() == 200) {
                    if (response.body().getValue().size() > 0)
                        invoices_counter.setText(response.body().getValue().get(0).getInvoice());
                    else
                        invoices_counter.setText("0");


                } else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CounterResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }


    /******************** Save User ID**************************/


    /************************ Delievry Counter ************************/


    public static ArrayList<Inventory> fastInventoryList = new ArrayList<>();
    public static ArrayList<Inventory> mediumInventoryList = new ArrayList<>();
    public static ArrayList<Inventory> nonInventoryList = new ArrayList<>();
    public static ArrayList<Inventory> allInventoryList = new ArrayList<>();


    private void callSalesApi() {
        ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
        model.getSalesEmployeeList().observe(getActivity(), new Observer<List<SalesEmployeeItem>>() {
            @Override
            public void onChanged(@Nullable List<SalesEmployeeItem> itemsList) {
                if (itemsList != null) {
                    Globals.saveSaleEmployeeArrayList(itemsList, Globals.SalesEmployeeList);
                }

            }
        });
    }


    private void openNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "NewNotification")
                .setContentTitle("Hey,")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText("You are meeting with Cinntra ! ");
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getContext());
        managerCompat.notify(999, builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("NewNotification", "NewNotification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void changeTeam(String name) {
        userRole_txt.setText(name);
        setDashboardCounters();
    }


}