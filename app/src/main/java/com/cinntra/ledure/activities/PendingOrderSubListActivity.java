package com.cinntra.ledure.activities;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.PendingSubListOrderAdapter;
import com.cinntra.ledure.databinding.ActivityPendingOrderSubListBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.globals.SearchViewUtils;
import com.cinntra.ledure.model.OrderWisePendingOrderData;
import com.cinntra.ledure.model.PendingOrderSubResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.pixplicity.easyprefs.library.Prefs;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingOrderSubListActivity extends MainBaseActivity {
    ActivityPendingOrderSubListBinding binding;

    LinearLayoutManager layoutManager;
    ArrayList<OrderWisePendingOrderData> AllItemList = new ArrayList<>();
    //PartyWisePendingOrder response;
    PendingSubListOrderAdapter adapter;
    String cardCode = "";
    String cardName = "";

    @Override
    public void onBackPressed() {

        if (binding.toolbarPendingList.mainHeaderLay.getVisibility() == View.GONE) {
            binding.toolbarPendingList.searchLay.setVisibility(View.GONE);
            binding.toolbarPendingList.mainHeaderLay.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPendingOrderSubListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();


        binding.toolbarPendingList.relativeCalView.setVisibility(View.GONE);
        binding.toolbarPendingList.newQuatos.setVisibility(View.GONE);
        binding.toolbarPendingList.relativeInfoView.setVisibility(View.GONE);
        binding.toolbarPendingList.search.setVisibility(View.GONE);


        binding.toolbarPendingList.filter.setVisibility(View.VISIBLE);
        binding.toolbarPendingList.back.setOnClickListener(view ->
        {
            finish();
        });
        binding.toolbarPendingList.search.setOnClickListener(view ->
        {
            binding.toolbarPendingList.mainHeaderLay.setVisibility(View.GONE);
            binding.toolbarPendingList.searchLay.setVisibility(View.VISIBLE);

            binding.toolbarPendingList.searchView.setIconifiedByDefault(true);
            binding.toolbarPendingList.searchView.setFocusable(true);
            binding.toolbarPendingList.searchView.setIconified(false);
            binding.toolbarPendingList.searchView.requestFocusFromTouch();
        });

        // response = (PartyWisePendingOrder) getIntent().getSerializableExtra("content");
        cardCode = getIntent().getStringExtra("CardCode");
        cardName = getIntent().getStringExtra("CardName");
        binding.toolbarPendingList.headTitle.setText(cardCode + " " + cardName);
        binding.loader.setVisibility(View.VISIBLE);
        pendingOrderWiseApi();


        eventSearchManager();
    }


    private void eventSearchManager() {
        binding.toolbarPendingList.searchView.setBackgroundColor(Color.parseColor("#00000000"));
        binding.toolbarPendingList.searchLay.setVisibility(View.VISIBLE);
        binding.toolbarPendingList.searchView.setVisibility(View.VISIBLE);


        SearchViewUtils.setupSearchView(binding.toolbarPendingList.searchView, 9000, new SearchViewUtils.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("NEWSHUBH", "onQueryTextSubmit: " + query);
                // Handle search query submission
                // db.myDataDao().deleteAll();
                binding.toolbarPendingList.searchView.clearFocus();
                //AllitemsList
               /* pageNo = 1;
                searchTextValue = query;
                seachable = true;
                if (!searchTextValue.isEmpty())
                    cashDiscountOneApi(searchTextValue);*/
                return false;
            }

            @Override
            public void onQueryTextChange(String newText) {
                // Perform API call or any other action with the newText
                Log.e("NEWSHUBH", "onQueryTextChange: " + newText);
                binding.toolbarPendingList.searchView.clearFocus();
                //AllitemsList
                // pageNo = 1;
             /*   searchTextValue = newText;
                //  db.myDataDao().deleteAll();
               // seachable = true;
                if (!searchTextValue.isEmpty())
                    cashDiscountOneApi(searchTextValue);*/

            }
        });

        binding.toolbarPendingList.filterView.setVisibility(View.GONE);

        binding.toolbarPendingList.filterView.setOnClickListener(view -> {
            //   openpopup();
            openNewPopUpCustomize();
        });
    }

    String ammount = "";
    String quantity = "";

    private void openNewPopUpCustomize() {
        PopupMenu popupMenu = new PopupMenu(PendingOrderSubListActivity.this, binding.toolbarPendingList.filterView);

        // Inflate the menu resource
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu_pending_order, popupMenu.getMenu());

        // Set a menu item click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                // Handle menu item clicks
                switch (menuItem.getItemId()) {

                    case R.id.menuAToz:
                        quantity = Globals.ASC;
                        ammount = "";
                        return true;
                    case R.id.menuZtoA:
                        quantity = Globals.DESC;
                        ammount = "";
                        return true;

                    case R.id.menuAmountDesc:
                        ammount = Globals.DESC;
                        quantity = "";
                        return true;

                    case R.id.menuAmountAsc:
                        ammount = Globals.ASC;
                        quantity = "";

                        return true;

                    case R.id.clearAllFilter:
                        ammount = "";
                        quantity = "";
                        return true;
                    // Add more cases as needed
                    default:
                        return false;
                }
            }
        });

        // Show the PopupMenu
        popupMenu.show();
    }

    private void openpopup() {
// .addItem(new PowerMenuItem("New BP",R.drawable.ic_newbp, false)) // add an item.

        PowerMenu powerMenu = new PowerMenu.Builder(this)
                .addItem(new PowerMenuItem("Asc Quantity", R.drawable.ic_filter_black, false)) // aad an item list.
                .addItem(new PowerMenuItem("Desc Quantity", R.drawable.ic_filter_black, false)) // aad an item list.
                .addItem(new PowerMenuItem("Asc Amount", R.drawable.ic_rupee_symbol, false)) // aad an item list.
                .addItem(new PowerMenuItem("Dsc Amount", R.drawable.ic_rupee_symbol, false)) // aad an item list.
                .addItem(new PowerMenuItem("Clear All Filter", R.drawable.ic_filter_black, false)) // aad an item list.
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | LEFT).
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(this, R.color.black))
                .setTextGravity(Gravity.START)
                .setTextSize(12)
                .setTextTypeface(Typeface.createFromAsset(getAssets(), "poppins_regular.ttf"))
                .setSelectedTextColor(Color.BLACK)
                .setWidth(Globals.pxFromDp(this, 220f))
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .build();
        powerMenu.showAsDropDown(binding.toolbarPendingList.filterView);


        OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
            @Override
            public void onItemClick(int position, PowerMenuItem item) {
                powerMenu.setSelectedPosition(position); // change selected item
                // TODO: 19-09-2023 Add cashdiscount api on every call on item click right now only text is going to chnage on the basis of click

                switch (position) {

                    case 0:
                        quantity = Globals.ASC;
                        ammount = "";
                        //  cashDiscountOneApi(searchTextValue);

                        break;
                    case 1:
                        quantity = Globals.DESC;
                        ammount = "";
                        break;
                    case 2:
                        ammount = Globals.ASC;
                        quantity = "";
                        break;
                    case 3:
                        ammount = Globals.DESC;
                        quantity = "";
                        break;

                    case 4:
                        ammount = "";
                        quantity = "";
                        break;


                }

                binding.loader.setVisibility(View.VISIBLE);
                pendingOrderWiseApi();


                powerMenu.dismiss();
            }
        };
        powerMenu.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    private void pendingOrderWiseApi() {
        HashMap<String, String> hde = new HashMap<>();
        hde.put("CardCode", cardCode);
        hde.put("OrderByName", quantity);
        hde.put("OrderByAmt", ammount);//
        hde.put("MaxSize", "All");
        hde.put("PageNo", "1");

        Call<PendingOrderSubResponse> call;
        if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
            hde.put("CardCode", cardCode);
            hde.put("OrderByName", quantity);
            hde.put("OrderByAmt", ammount);//
            hde.put("MaxSize", "All");
            hde.put("PageNo", "1");
            hde.put("FromDate", "");
            hde.put("ToDate", "");
            hde.put("SearchText", "");

            call = NewApiClient.getInstance().getApiService().getPendingSubOrderPurchase(hde);
        } else {
            hde.put("CardCode", cardCode);
            hde.put("OrderByName", quantity);
            hde.put("OrderByAmt", ammount);//
            hde.put("MaxSize", "All");
            hde.put("PageNo", "1");
            call = NewApiClient.getInstance().getApiService().getPendingSubOrder(hde);
        }


//        Call<PendingOrderSubResponse> call = NewApiClient.getInstance().getApiService().getPendingSubOrder(hde);
        call.enqueue(new Callback<PendingOrderSubResponse>() {
            @Override
            public void onResponse(Call<PendingOrderSubResponse> call, Response<PendingOrderSubResponse> response) {
                if (response.code() == 200) {
                    binding.loader.setVisibility(View.GONE);
                    if (response.body().getPendingOrderData() == null || response.body().getPendingOrderData().getOrderwise().size() == 0) {
                        Globals.setmessage(getApplicationContext());

                    } else {
                        AllItemList.clear();
                        AllItemList.addAll(response.body().getPendingOrderData().getOrderwise());

                        adapter = new PendingSubListOrderAdapter(PendingOrderSubListActivity.this, AllItemList, cardCode + " " + cardName);
                        layoutManager = new LinearLayoutManager(PendingOrderSubListActivity.this, RecyclerView.VERTICAL, false);
                        binding.recyclerview.setAdapter(adapter);
                        binding.recyclerview.setLayoutManager(layoutManager);
                        if (adapter != null)
                            adapter.notifyDataSetChanged();

                    }


                }
            }

            @Override
            public void onFailure(Call<PendingOrderSubResponse> call, Throwable t) {
                binding.loader.setVisibility(View.GONE);
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(PendingOrderSubListActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}