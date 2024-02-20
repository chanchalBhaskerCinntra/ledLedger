package com.cinntra.ledure.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.CategoryListFromZoneItemsActivity;
import com.cinntra.ledure.activities.ItemDashboardListActivity;

import com.cinntra.ledure.activities.SubGroupStockActivity;
import com.cinntra.ledure.globals.Globals;

import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.model.ResSubCatItems;
import com.cinntra.ledure.newapimodel.DataItemFilterDashBoard;

import com.cinntra.ledure.newapimodel.SubGroupItemStock;
import com.cinntra.ledure.test.ChildAdapter;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemOnStockGroupAdapter extends RecyclerView.Adapter<ItemOnStockGroupAdapter.ContactViewHolder> {
    Context context;
    List<DataItemFilterDashBoard> branchList;

    String flag = "";
    String zoneCode = "";
    public ItemOnStockGroupAdapter(Context context1, List<DataItemFilterDashBoard> branchList, String flag, String zoneCode) {
        this.branchList = branchList;
        this.context = context1;
        this.tempList = new ArrayList<DataItemFilterDashBoard>();
        this.flag = flag;
        this.zoneCode = zoneCode;

    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_stock_group, parent, false);
        return new ContactViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        holder.itemName.setText("" + branchList.get(position).getGroupName());
        holder.itemPriceIndividual.setText("Std price : " + branchList.get(position).getTotalPrice());

        if (flag.equalsIgnoreCase("Zone")){
            holder.itemPriceTotal.setVisibility(View.GONE);
//            holder.itemPriceIndividual.setVisibility(View.GONE);
            holder.quantity.setText("" + Globals.numberToK(String.valueOf(branchList.get(position).getTotalPrice())));
        }else {
            holder.itemPriceTotal.setVisibility(View.VISIBLE);
            holder.quantity.setText("Qty : " + Globals.numberToK(String.valueOf(branchList.get(position).getTotalQty())));//Nos :
            holder.itemPriceTotal.setText(" " + Globals.numberToK(String.valueOf(branchList.get(position).getTotalPrice())));
        }
        ArrayList<SubGroupItemStock> childItemArrayList = new ArrayList<>();
        if (branchList.get(position).getSubGroup().size() > 0) {
            childItemArrayList.addAll(branchList.get(position).getSubGroup());
        } else {

        }

        ChildAdapter childAdapter = new ChildAdapter(childItemArrayList, "", "", "", "", "", "");
        holder.recyclerView.setAdapter(childAdapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));


    }


    @Override
    public int getItemCount() {
        return branchList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPriceTotal, itemPriceIndividual, quantity;
        ConstraintLayout constraintLayout;
        ImageView edit;
        RecyclerView recyclerView;
        ImageButton ivArrow;


        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.tvItemName);
            itemPriceTotal = itemView.findViewById(R.id.tvtotAlPrice);
            itemPriceIndividual = itemView.findViewById(R.id.tvStandardPrice);
            quantity = itemView.findViewById(R.id.tvQuantityNos);
            recyclerView = itemView.findViewById(R.id.rvInnerStockItem);
            constraintLayout = itemView.findViewById(R.id.constraintInnerItem);
            ivArrow = itemView.findViewById(R.id.ivArrowDrop);
            itemPriceIndividual.setVisibility(View.INVISIBLE);


            ivArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    grpCode = String.valueOf(branchList.get(getAdapterPosition()).getGroupCode());
                    showBottomSheetDialog(context, grpCode, branchList.get(getAdapterPosition()).getGroupName());

                }
            });

            itemView.setOnClickListener(view -> {
                if (flag.equalsIgnoreCase("Zone")) {
                 /*   Intent i = new Intent(context, ItemDashboardListActivity.class);
                    i.putExtra("fromwhere", flag);
                    i.putExtra("ItemGroupCode", "" + branchList.get(getAdapterPosition()).getGroupCode());
                    i.putExtra("ItemGroupName", "" + branchList.get(getAdapterPosition()).getGroupName());
                    context.startActivity(i);*/



                    Intent i = new Intent(context, CategoryListFromZoneItemsActivity.class);
                    i.putExtra("fromwhere", "Zone");
                    i.putExtra("zoneName", "" + branchList.get(getAdapterPosition()).getGroupCode());
                    i.putExtra("ItemGroupCode", "" + branchList.get(getAdapterPosition()).getGroupCode());
                    i.putExtra("ItemGroupName", "" + branchList.get(getAdapterPosition()).getGroupName());
                    context.startActivity(i);
                }
                else if (flag.equalsIgnoreCase("Sales")){
                    Intent i = new Intent(context, SubGroupStockActivity.class);
                    i.putExtra("fromwhere", "fromSaleCategory");
                    i.putExtra("zoneName", "");
                    i.putExtra("ItemGroupCode", "" + branchList.get(getAdapterPosition()).getGroupCode());
                    i.putExtra("ItemGroupName", "" + branchList.get(getAdapterPosition()).getGroupName());
                    context.startActivity(i);
                   /* Intent i = new Intent(context, ItemDashboardListActivity.class);
                    i.putExtra("fromwhere", "");
                    i.putExtra("ItemGroupCode", "" + branchList.get(getAdapterPosition()).getGroupCode());
                    i.putExtra("ItemGroupName", "" + branchList.get(getAdapterPosition()).getGroupName());
                    context.startActivity(i);*/
                }else if (flag.equalsIgnoreCase("zoneSub")){
                    Intent i = new Intent(context, SubGroupStockActivity.class);
                    i.putExtra("fromwhere", "zonesub");
                    i.putExtra("zoneName", zoneCode);
                    i.putExtra("ItemGroupCode", "" + branchList.get(getAdapterPosition()).getGroupCode());
                    i.putExtra("ItemGroupName", "" + branchList.get(getAdapterPosition()).getGroupName());
                    context.startActivity(i);
                }else if (flag.equalsIgnoreCase("zoneStock")){
                    Intent i = new Intent(context, SubGroupStockActivity.class);
                    i.putExtra("fromwhere", "zoneStock");
                    i.putExtra("zoneName", zoneCode);
                    i.putExtra("ItemGroupCode", "" + branchList.get(getAdapterPosition()).getGroupCode());
                    i.putExtra("ItemGroupName", "" + branchList.get(getAdapterPosition()).getGroupName());
                    context.startActivity(i);
                }
                else {
                    Intent i = new Intent(context, SubGroupStockActivity.class);
                    i.putExtra("fromwhere", "");
                    i.putExtra("zoneName", "");
                    i.putExtra("ItemGroupCode", "" + branchList.get(getAdapterPosition()).getGroupCode());
                    i.putExtra("ItemGroupName", "" + branchList.get(getAdapterPosition()).getGroupName());
                    context.startActivity(i);
                }


            });


        }

    }

    int pageNo = 1;
    String grpCode;

    List<DataItemFilterDashBoard> tempList = null;

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        branchList.clear();
        if (charText.length() == 0) {
            branchList.addAll(tempList);
        } else {
            for (DataItemFilterDashBoard st : tempList) {
                if (st.getGroupName() != null && !st.getGroupName().isEmpty()) {
                    if (st.getGroupName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        branchList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }

    public void AllData(List<DataItemFilterDashBoard> tmp) {
        tempList.clear();
        tempList.addAll(tmp);
        notifyDataSetChanged();
    }


    RecyclerView item_list;
    TextView total;
    ProgressBar loader;

    private void showBottomSheetDialog(Context context, String cat_grp_code, String GrpName) {


        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        View sheetView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_item_list, null);
        bottomSheetDialog.setContentView(sheetView);

        ImageView ivCloseBottomSheet = (ImageView) bottomSheetDialog.findViewById(R.id.ivCloseBottomSheet);
        item_list = (RecyclerView) bottomSheetDialog.findViewById(R.id.item_list);
        total = (TextView) bottomSheetDialog.findViewById(R.id.total);
        loader = (ProgressBar) bottomSheetDialog.findViewById(R.id.loader);
        TextView headingBottomSheetShareReport = (TextView) bottomSheetDialog.findViewById(R.id.headingBottomSheetShareReport);
        bottomSheetDialog.show();
        headingBottomSheetShareReport.setText(GrpName);
        item_list.addOnScrollListener(scrollListener);
        callApiGroupItemStock("", Prefs.getString(Globals.FROM_DATE, ""), Prefs.getString(Globals.TO_DATE, ""), cat_grp_code);


        ivCloseBottomSheet.setOnClickListener(view -> {

            bottomSheetDialog.dismiss();
        });

    }

    ChildAdapter childAdapter;
    LinearLayoutManager layoutManager;
    ArrayList<SubGroupItemStock> subList = new ArrayList<>();

    private void callApiGroupItemStock(String searchValue, String startDate, String endDate, String cardcode) {
        pageNo = 1;
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);

        loader.setVisibility(View.GONE);

        HashMap<String, String> hde = new HashMap<>();
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        //  hde.put("MaxSize", "All");
        hde.put("SearchText", searchValue);
        hde.put("FromDate", startDate);
        hde.put("ToDate", endDate);
        hde.put("CategoryCode", cardcode);
        hde.put("OrderByName", "");
        hde.put("OrderByAmt", "");
        hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));


        Call<ResSubCatItems> call = NewApiClient.getInstance().getApiService().sub_category_items_dashboard(hde);
        call.enqueue(new Callback<ResSubCatItems>() {
            @Override
            public void onResponse(Call<ResSubCatItems> call, Response<ResSubCatItems> response) {
                if (response.code() == 200) {
                    subList.clear();
                    subList.addAll(response.body().getSubGroup());

                    total.setText("Total Sale \u20B9 :" + response.body().getTotalSales());
                    childAdapter = new ChildAdapter(subList, "", "", "", "", "", "");
                    item_list.setAdapter(childAdapter);
                    layoutManager = new LinearLayoutManager(item_list.getContext());
                    item_list.setLayoutManager(layoutManager);
                    loader.setVisibility(View.GONE);

                } else {
                    loader.setVisibility(View.GONE);
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, QuotationResponse.class);
                        Toast.makeText(context, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResSubCatItems> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    double totalSale = 0.0;

    private String getTotal(String number) {


        DecimalFormat df = new DecimalFormat("0");
        double amount = totalSale + Double.parseDouble(df.format(Double.parseDouble(number)));
        NumberFormat format = NumberFormat.getInstance(new Locale("en", "IN"));
        String formattedNumber = format.format(amount);
        return formattedNumber;
    }

    private void callPageItemStock(String searchValue, String startDate, String endDate, String cardcode) {

        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);

        loader.setVisibility(View.GONE);

        HashMap<String, String> hde = new HashMap<>();
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        // hde.put("MaxSize", "All");
        hde.put("SearchText", searchValue);
        hde.put("FromDate", startDate);
        hde.put("ToDate", endDate);
        hde.put("CategoryCode", cardcode);
        hde.put("OrderByName", "");
        hde.put("OrderByAmt", "");
        hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));


        Call<ResSubCatItems> call = NewApiClient.getInstance().getApiService().sub_category_items_dashboard(hde);
        call.enqueue(new Callback<ResSubCatItems>() {
            @Override
            public void onResponse(Call<ResSubCatItems> call, Response<ResSubCatItems> response) {
                if (response.code() == 200) {

                    //  AllitemsList.clear();
                    total.setText("Total Sale \u20B9 :" + getTotal(response.body().getTotalSales()));
                    subList.addAll(response.body().getSubGroup());
                    childAdapter.notifyDataSetChanged();
                    loader.setVisibility(View.GONE);

                } else {
                    loader.setVisibility(View.GONE);
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, QuotationResponse.class);
                        Toast.makeText(context, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResSubCatItems> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    boolean isLoading = false;
    boolean islastPage = false;
    boolean isScrollingpage = false;

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {


        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            Log.e("'TAG'", "onScrolled: ");

            // layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleitempositon = layoutManager.findFirstVisibleItemPosition(); //first item
            int visibleItemCount = layoutManager.getChildCount(); //total number of visible item
            int totalItemCount = layoutManager.getItemCount();   //total number of item

            boolean isNotLoadingAndNotLastPage = !isLoading && !islastPage;
            boolean isAtLastItem = firstVisibleitempositon + visibleItemCount >= totalItemCount;
            boolean isNotAtBeginning = firstVisibleitempositon >= 0;
            boolean isTotaolMoreThanVisible = totalItemCount >= Globals.QUERY_PAGE_SIZE;
            boolean shouldPaginate = isNotLoadingAndNotLastPage && isNotAtBeginning && isAtLastItem && isTotaolMoreThanVisible
                    && isScrollingpage;

            if (isScrollingpage && (visibleItemCount + firstVisibleitempositon == totalItemCount)) {
                pageNo++;
                callPageItemStock("", Prefs.getString(Globals.FROM_DATE, ""), Prefs.getString(Globals.TO_DATE, ""), grpCode);


                isScrollingpage = false;


            } else {
                // Log.d(TAG, "onScrolled:not paginate");
                recyclerView.setPadding(0, 0, 0, 0);
            }
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) { //it means we are scrolling
                isScrollingpage = true;
            }
        }
    };


}
