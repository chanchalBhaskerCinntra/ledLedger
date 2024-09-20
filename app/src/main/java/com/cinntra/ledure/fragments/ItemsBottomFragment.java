package com.cinntra.ledure.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.bumptech.glide.Glide;
import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.Login;
import com.cinntra.ledure.activities.MainActivity_B2C;
import com.cinntra.ledure.adapters.ItemOnStockGroupAdapter;
import com.cinntra.ledure.adapters.ItemStockAdapter;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.databinding.FragmentItemsBottomBinding;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.SessionManagement;
import com.cinntra.ledure.model.AttachmentModel;
import com.cinntra.ledure.model.DataItemDashBoard;
import com.cinntra.ledure.model.ResponseItemDashboard;
import com.cinntra.ledure.newapimodel.DataItemFilterDashBoard;
import com.cinntra.ledure.newapimodel.ResponseItemFilterDashboard;
import com.cinntra.ledure.webservices.NewApiClient;
import com.cinntra.roomdb.ItemsDatabase;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.JsonObject;
import com.pixplicity.easyprefs.library.Prefs;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ItemsBottomFragment extends Fragment {
    private FragmentItemsBottomBinding binding;
    String fromwhere = "";
    // ItemOnDashboardAdapter adapter;
    ItemStockAdapter adapter;
    private String searchTextValue = "";
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    String filterByName = Globals.ATOZ;
    String filterByAmount = "";


    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    String startDate = Globals.firstDateOfFinancialYear();
    String endDate = Globals.lastDateOfFinancialYear();

    private ItemsDatabase db;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Objects.requireNonNull(((MainActivity_B2C) requireActivity()).getSupportActionBar()).hide();
        getActivity().findViewById(R.id.app_bar).setVisibility(View.GONE);
        getActivity().findViewById(R.id.date_selector).setVisibility(View.GONE);
        MainActivity_B2C.dateSPinner.setVisibility(View.GONE);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_items_bottom, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentItemsBottomBinding.bind(view);
        //todo new change
        Prefs.putString(Globals.PrefsItemATOZ, "");
        Prefs.putString(Globals.PrefsItemAmount, "");

        filterByName = Prefs.getString(Globals.PrefsItemATOZ, "");
        filterByAmount = Prefs.getString(Globals.PrefsItemAmount, "");
        sessionManagement=new SessionManagement(getActivity());
        callAttachmentAllApi();

        //todo set defualt prefs
        Prefs.putBoolean(Globals.ISPURCHASE, false);
        binding.filterAtozShubh.setText("Filter : A To Z");
        binding.filterAtozShubh.setVisibility(View.VISIBLE);
        if (filterByName.equalsIgnoreCase(Globals.ATOZ)) {
            binding.filterAtozShubh.setText("Filter : A To Z");
            binding.filterAtozShubh.setVisibility(View.VISIBLE);

        } else if (filterByName.equalsIgnoreCase(Globals.ZTOA)) {
            binding.filterAtozShubh.setText("Filter : Z To A");
            binding.filterAtozShubh.setVisibility(View.VISIBLE);
        } else if (filterByAmount.equalsIgnoreCase(Globals.DESC)) {
            binding.filterAtozShubh.setText("Filter : By Amount");
            binding.filterAtozShubh.setVisibility(View.VISIBLE);
        } else {
            binding.filterAtozShubh.setText("Filter : Nothing");
            binding.filterAtozShubh.setVisibility(View.GONE);
        }

        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);

        startDate = Prefs.getString(Globals.FROM_DATE, "");
        endDate = Prefs.getString(Globals.TO_DATE, "");

        if (startDate.isEmpty()) {
            binding.tvSelectedDate.setText("All");
        } else {
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), false, "", binding.tvSelectedDate);
        }

        hideToolbarMenu();
        db = ItemsDatabase.getDatabase(requireContext());
        binding.loader.setVisibility(View.GONE);
        setRecyclerViewAdapter();
        //  setUPSpinnerGroup();


        binding.swipeRefreshItems.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Globals.checkInternet(requireContext())) {
                    pageNo = 1;
                    searchTextValue = "";
                    if (groupType.equalsIgnoreCase("Items")) {
                        setRecyclerViewAdapter();
                        callApi("");
                    } else if (groupType.equalsIgnoreCase("Category")) {//Stock Group
                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                    } else {
                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                    }


                }

            }
        });

        binding.toolbarItemDashBoard.search.setOnClickListener(viewe -> {
            binding.toolbarItemDashBoard.mainHeaderLay.setVisibility(View.GONE);
            binding.toolbarItemDashBoard.searchLay.setVisibility(View.VISIBLE);

            binding.toolbarItemDashBoard.searchView.setIconifiedByDefault(true);
            binding.toolbarItemDashBoard.searchView.setFocusable(true);
            binding.toolbarItemDashBoard.searchView.setIconified(false);
            binding.toolbarItemDashBoard.searchView.requestFocusFromTouch();

        });
        binding.toolbarItemDashBoard.searchLay.setOnClickListener(viewe -> {
            binding.toolbarItemDashBoard.searchView.setFocusable(true);
        });

        // binding.toolbarItemDashBoard.searchLay.setBackgroundColor(Color.parseColor("#00000000"));
        binding.toolbarItemDashBoard.searchLay.setVisibility(View.VISIBLE);
        binding.toolbarItemDashBoard.searchView.setVisibility(View.VISIBLE);

        binding.toolbarItemDashBoard.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("TAG", "onQueryTextSubmit: ");
                binding.toolbarItemDashBoard.searchView.clearFocus();
                //AllitemsList
                pageNo = 1;
                searchTextValue = query;

                if (Globals.checkInternet(requireContext())) {

                    if (groupType.equalsIgnoreCase("Items")) {
                        callApi("");
                    } else if (groupType.equalsIgnoreCase("Category")) {
                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                    } else {
                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                    }
                }

                //  cashDiscountApiTest(searchTextValue);
                return true;


                //  return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    binding.toolbarItemDashBoard.searchView.clearFocus();
                //AllitemsList
                pageNo = 1;
                searchTextValue = newText;

                if (Globals.checkInternet(requireContext())) {
                    pageNo = 1;

                    if (groupType.equalsIgnoreCase("Items")) {
                        setRecyclerViewAdapter();
                        callApi(searchTextValue);
                    } else if (groupType.equalsIgnoreCase("Category")) {
                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                    } else {
                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                    }
                }
                return true;
            }
        });

        binding.toolbarItemDashBoard.headTitle.setText("Items");

        binding.toolbarItemDashBoard.searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                binding.toolbarItemDashBoard.searchLay.setVisibility(View.GONE);
                binding.toolbarItemDashBoard.mainHeaderLay.setVisibility(View.VISIBLE);
                return true;
            }
        });


        binding.rvItemDash.addOnScrollListener(scrollListener);
        binding.toolbarItemDashBoard.relativeCalView.setOnClickListener(viewe -> {
            showDateBottomSheetDialog(requireContext());
        });

        binding.radioItemStockGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.radioItems:
                        groupType = "Items";

                        pageNo = 1;
                        /*  searchTextValue = "";*/
                        //  setRecyclerViewAdapter();
                        callApi(searchTextValue);


                        break;
                    case R.id.radioStockGroup:
//                        groupType = "Stock Group";
                        groupType = "Category";

                        pageNo = 1;
                        /*  searchTextValue = "";*/

                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");

                        break;
                    case R.id.radioZone:
                        groupType = "Zone";

                        pageNo = 1;
                        /*  searchTextValue = "";*/

                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");

                        break;


                }

            }
        });

    }

    SessionManagement sessionManagement;

    private void callAttachmentAllApi(){
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
                        Log.e(TAG, "onResponse: "+response.body().getMessage() );
                        if (response.body().getStatus() == 200){
                            if (response.body().getData().size() > 0) {
                                String filePath = Globals.ImageURL + response.body().getData().get(0).getProfileImage();

                                if (filePath != null) {
                          /*      Glide.with(getActivity())
                                        .load(filePath)
                                        .into(binding.proImg);*/
                                } else {
                                    //  binding.proImg.setImageResource(R.drawable.ic_profileicon);
                                }

                            }
                        }

                        else if (response.body().getStatus() == 401) {
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
                Log.e(TAG, "onFailure: "+t.getMessage() );
            }
        });
    }

    String groupType = "Category";
    String salePurchaseGroupType = "Sales";


    public boolean onBackPressed() {
        if (binding.toolbarItemDashBoard.mainHeaderLay.getVisibility() == View.GONE) {
            binding.toolbarItemDashBoard.searchLay.setVisibility(View.GONE);
            binding.toolbarItemDashBoard.searchView.setQuery("", false);
            binding.toolbarItemDashBoard.mainHeaderLay.setVisibility(View.VISIBLE);
        } else {
            requireActivity().finish();
            //   Toast.makeText(requireContext(), "fragment else", Toast.LENGTH_SHORT).show();
        }
        // Add your logic here
        //  Toast.makeText(getActivity(), "backPressed....", Toast.LENGTH_SHORT).show();
        return true; // Return true if you've handled the event, otherwise return false
    }


    private void showDateBottomSheetDialog(Context context) {
        BottomSheetDialogSelectDateBinding bindingDate;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        bindingDate = BottomSheetDialogSelectDateBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(bindingDate.getRoot());
        bindingDate.ivCloseBottomSheet.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
        });
        bindingDate.tvCustomDateBottomSheetSelectDate.setOnClickListener(view -> {

            bottomSheetDialog.dismiss();
            dateRangeSelector();

        });
        bindingDate.tvTodayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Calendar.getInstance().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            searchTextValue = "";
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), true, "Today", binding.tvSelectedDate);
            if (Globals.checkInternet(requireContext())) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }

            }


            bottomSheetDialog.dismiss();
        });

        bindingDate.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(startDatelng);
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            searchTextValue = "";
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), true, "Yesterday", binding.tvSelectedDate);
            if (Globals.checkInternet(requireContext())) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }

            }


            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            searchTextValue = "";
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), true, "This Week", binding.tvSelectedDate);
            if (Globals.checkInternet(requireContext())) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }

            }

            bottomSheetDialog.dismiss();
        });


        bindingDate.tvThisMonthBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            searchTextValue = "";
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), true, "This Month", binding.tvSelectedDate);
            if (Globals.checkInternet(requireContext())) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }
            }
            bottomSheetDialog.dismiss();

        });
        bindingDate.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            searchTextValue = "";
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), true, "Last Month", binding.tvSelectedDate);
            if (Globals.checkInternet(requireContext())) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }


            }

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastQuarterFirstDate();
            endDate = Globals.lastQuarterlastDate();
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            searchTextValue = "";
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), true, "This Quarter", binding.tvSelectedDate);
            if (Globals.checkInternet(requireContext())) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }

            }

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfFinancialYear();
            endDate = Globals.lastDateOfFinancialYear();
            binding.loader.setVisibility(View.VISIBLE);
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), false, "", binding.tvSelectedDate);
            pageNo = 1;
            searchTextValue = "";
            if (Globals.checkInternet(requireContext())) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }
            }

            bottomSheetDialog.dismiss();
        });
        bindingDate.tvLastYearBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            searchTextValue = "";
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), false, "", binding.tvSelectedDate);
            if (Globals.checkInternet(requireContext())) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }

            }

            bottomSheetDialog.dismiss();
        });

        bindingDate.tvLastYearTillDateBottomSheetSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDatelng = Globals.lastyearCal().getTimeInMillis();
                endDatelng = Globals.thisyearCal().getTimeInMillis();
                startDate = Globals.lastYearFirstDate();
                endDate = Globals.getCurrentDateInLastFinancialYear();
                binding.loader.setVisibility(View.VISIBLE);
                pageNo = 1;
                searchTextValue = "";
                Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), false, "", binding.tvSelectedDate);
                if (Globals.checkInternet(requireContext())) {
                    if (groupType.equalsIgnoreCase("Items")) {
                        setRecyclerViewAdapter();
                        callApi("");
                    } else if (groupType.equalsIgnoreCase("Category")) {
                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                    } else {
                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                    }

                }

                bottomSheetDialog.dismiss();
            }
        });

        bindingDate.tvAllBottomSheetSelectDate.setOnClickListener(view -> {
            startDate = "";
            endDate = "";
            binding.loader.setVisibility(View.VISIBLE);
            pageNo = 1;
            searchTextValue = "";
            Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), true, "All", binding.tvSelectedDate);
            if (Globals.checkInternet(requireContext())) {
                if (groupType.equalsIgnoreCase("Items")) {
                    setRecyclerViewAdapter();
                    callApi("");
                } else if (groupType.equalsIgnoreCase("Category")) {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                } else {
                    callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                }
            }

            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();

    }

    private void dateRangeSelector() {


        if (startDatelng == 0.0) {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();
        } else {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(startDatelng, endDatelng)).build();

        }

        materialDatePicker.show(getChildFragmentManager(), "Tag_Picker");

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                binding.loader.setVisibility(View.VISIBLE);
                startDatelng = selection.first;
                endDatelng = selection.second;
                startDate = Globals.Date_yyyy_mm_dd(startDatelng);
                endDate = Globals.Date_yyyy_mm_dd(endDatelng);
                binding.loader.setVisibility(View.VISIBLE);
                pageNo = 1;
                searchTextValue = "";
                Globals.setUpDateTextView(Globals.convertDateFormat(startDate), Globals.convertDateFormat(endDate), false, "", binding.tvSelectedDate);
                if (Globals.checkInternet(requireContext())) {

                    if (groupType.equalsIgnoreCase("Items")) {
                        setRecyclerViewAdapter();
                        callApi("");
                    } else if (groupType.equalsIgnoreCase("Category")) {
                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                    } else {
                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                    }
                }


            }
        });


    }

    public void hideToolbarMenu() {
        //  getSupportActionBar().hide();


        binding.toolbarItemDashBoard.relativeCalView.setVisibility(View.VISIBLE);
        binding.toolbarItemDashBoard.relativeInfoView.setVisibility(View.GONE);
        binding.toolbarItemDashBoard.filterView.setVisibility(View.VISIBLE);
        binding.toolbarItemDashBoard.search.setVisibility(View.VISIBLE);
        binding.toolbarItemDashBoard.newQuatos.setVisibility(View.GONE);
        binding.toolbarItemDashBoard.back.setVisibility(View.GONE);

        //todo sale purchase option is disable according to client
        if (Prefs.getString(Globals.SalesEmployeeCode, "").equalsIgnoreCase("28") ||
                Prefs.getString(Globals.SalesEmployeeCode, "").equalsIgnoreCase("21") ||
                Prefs.getString(Globals.SalesEmployeeCode, "").equalsIgnoreCase("32") ||
                Prefs.getString(Globals.SalesEmployeeCode, "").equalsIgnoreCase("31") ||
                Prefs.getString(Globals.SalesEmployeeCode, "").equalsIgnoreCase("-1")) {
            binding.toolbarItemDashBoard.salesAndPurchaseLayout.setVisibility(View.VISIBLE);
        } else {
            binding.toolbarItemDashBoard.salesAndPurchaseLayout.setVisibility(View.INVISIBLE);
        }
      //  binding.toolbarItemDashBoard.salesAndPurchaseLayout.setVisibility(View.VISIBLE);

        binding.toolbarItemDashBoard.filterView.setOnClickListener(view -> {
            //      openpopup();

            openNewPopUpCustomize();
        });


        //todo set zone --
        if (Prefs.getString(Globals.Role, "").trim().equalsIgnoreCase("admin") || Prefs.getString(Globals.Role, "").trim().equalsIgnoreCase("Director") || Prefs.getString(Globals.Role, "").trim().equalsIgnoreCase("Accounts")) {
            ArrayAdapter spinnerArrayAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.data_type, // Replace with your item array resource
                    R.layout.spinner_white_textview);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.toolbarItemDashBoard.typeDropdown.setAdapter(spinnerArrayAdapter);
        } else {

            ArrayAdapter spinnerArrayAdapter = ArrayAdapter.createFromResource(requireContext(),
                    R.array.data_type, // Replace with your item array resource
                    R.layout.spinner_white_textview);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.toolbarItemDashBoard.typeDropdown.setAdapter(spinnerArrayAdapter);
        }

        binding.toolbarItemDashBoard.typeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                salePurchaseGroupType = binding.toolbarItemDashBoard.typeDropdown.getSelectedItem().toString();
                pageNo = 1;
                searchTextValue = "";
                binding.toolbarItemDashBoard.searchView.setQuery("", false);
                if (salePurchaseGroupType.equals("Sales")) {
                    //  Prefs.putString(Globals.forSalePurchase, Globals.Sale);
                    Prefs.putBoolean(Globals.ISPURCHASE, false);
                    if (groupType.equalsIgnoreCase("Items")) {
                        callApi(searchTextValue);

                    } else if (groupType.equalsIgnoreCase("Category")) {


                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");


                    } else {
                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");

                    }

                } else {
                    //  Prefs.putString(Globals.forSalePurchase, Globals.Purchase);
                    Prefs.putBoolean(Globals.ISPURCHASE, true);
                    if (groupType.equalsIgnoreCase("Items")) {
                        callApi(searchTextValue);

                    } else if (groupType.equalsIgnoreCase("Category")) {


                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");


                    } else {
                        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");

                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    private void openNewPopUpCustomize() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), binding.toolbarItemDashBoard.filterView);

        // Inflate the menu resource
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu_pending_order, popupMenu.getMenu());

        // Set a menu item click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                // Handle menu item clicks
                switch (menuItem.getItemId()) {

                    case R.id.menuAToz:
                        binding.filterAtozShubh.setText("Filter : A To Z");
                        Prefs.putString(Globals.PrefsItemATOZ, Globals.ATOZ);
                        Prefs.putString(Globals.PrefsItemAmount, "");
                        filterByName = Globals.ATOZ;
                        filterByAmount = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);

                        if (Prefs.getString(Globals.PrefsItemATOZ, "").equalsIgnoreCase(Globals.ATOZ)) {
                            menuItem.setChecked(true);
                        } else {
                            menuItem.setChecked(false);
                        }
                        searchTextValue = "";
                        if (groupType.equalsIgnoreCase("Items")) {
                            setRecyclerViewAdapter();
                            callApi("");
                        } else if (groupType.equalsIgnoreCase("Category")) {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                        } else {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                        }
                        return true;
                    case R.id.menuZtoA:
                        binding.filterAtozShubh.setText("Filter : Z To A");
                        Prefs.putString(Globals.PrefsItemATOZ, Globals.ZTOA);
                        Prefs.putString(Globals.PrefsItemAmount, "");
                        filterByName = Globals.ZTOA;
                        filterByAmount = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        searchTextValue = "";
                        if (groupType.equalsIgnoreCase("Items")) {
                            setRecyclerViewAdapter();
                            callApi("");
                        } else if (groupType.equalsIgnoreCase("Category")) {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                        } else {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                        }
                        if (Prefs.getString(Globals.PrefsItemATOZ, "").equalsIgnoreCase(Globals.ZTOA)) {
                            menuItem.setChecked(true);
                        } else {
                            menuItem.setChecked(false);
                        }
                        return true;

                    case R.id.menuAmountDesc:
                        binding.filterAtozShubh.setText("Filter : Amount Desc");
                        Prefs.putString(Globals.PrefsItemAmount, Globals.DESC);
                        Prefs.putString(Globals.PrefsItemATOZ, "");
                        filterByAmount = Globals.DESC;
                        filterByName = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        searchTextValue = "";
                        if (groupType.equalsIgnoreCase("Items")) {
                            setRecyclerViewAdapter();
                            callApi("");
                        } else if (groupType.equalsIgnoreCase("Category")) {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                        } else {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                        }
                        if (Prefs.getString(Globals.PrefsItemAmount, "").equalsIgnoreCase(Globals.DESC)) {
                            menuItem.setChecked(true);
                        } else {
                            menuItem.setChecked(false);
                        }

                        return true;

                    case R.id.menuAmountAsc:
                        binding.filterAtozShubh.setText("Filter : Amount Asc");
                        Prefs.putString(Globals.PrefsItemAmount, Globals.DESC);
                        Prefs.putString(Globals.PrefsItemATOZ, "");
                        filterByAmount = Globals.ASC;
                        filterByName = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        searchTextValue = "";
                        if (groupType.equalsIgnoreCase("Items")) {
                            setRecyclerViewAdapter();
                            callApi("");
                        } else if (groupType.equalsIgnoreCase("Category")) {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                        } else {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                        }

                        return true;

                    case R.id.menuAllFilter:
                        binding.filterAtozShubh.setText("");
                        Prefs.putString(Globals.PrefsItemAmount, "");
                        Prefs.putString(Globals.PrefsItemATOZ, "");
                        filterByAmount = "";
                        filterByName = "";
                        binding.filterAtozShubh.setVisibility(View.GONE);
                        searchTextValue = "";
                        if (groupType.equalsIgnoreCase("Items")) {
                            setRecyclerViewAdapter();
                            callApi("");
                        } else if (groupType.equalsIgnoreCase("Category")) {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");
                        } else {
                            callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "Zone");
                        }
                        menuItem.setChecked(!menuItem.isChecked());
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


    int pageNo = 1;

    ArrayList<DataItemDashBoard> AllitemsList = new ArrayList<>();
    LinearLayoutManager layoutManager;

    boolean isLoading = false;
    boolean islastPage = false;
    boolean isScrollingpage = false;

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            // layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleitempositon = layoutManager.findFirstVisibleItemPosition(); //first item
            int visibleItemCount = layoutManager.getChildCount(); //total number of visible item
            int totalItemCount = layoutManager.getItemCount();   //total number of item

            boolean isNotLoadingAndNotLastPage = !isLoading && !islastPage;
            boolean isAtLastItem = firstVisibleitempositon + visibleItemCount >= totalItemCount;
            boolean isNotAtBeginning = firstVisibleitempositon >= 0;
            boolean isTotaolMoreThanVisible = totalItemCount >= Globals.QUERY_PAGE_SIZE;
            boolean shouldPaginate = isNotLoadingAndNotLastPage && isNotAtBeginning && isAtLastItem && isTotaolMoreThanVisible && isScrollingpage;

            if (isScrollingpage && (visibleItemCount + firstVisibleitempositon == totalItemCount)) {
                binding.loader.setVisibility(View.VISIBLE);


                if (Globals.checkInternet(requireContext())) {
                    if (groupType.equalsIgnoreCase("Items")) {
                        /*if (AllitemsList.size() != 0)
                            pageNo = AllitemsList.size() / Globals.QUERY_PAGE_SIZE;*/
                        pageNo++;
                        callAllPagesApi();
                    } else if (groupType.equalsIgnoreCase("Category")) {
                        pageNo++;
                        callAllPagesApi_GroupItemStock();
                    } else {
                        pageNo++;
                        callAllPagesApi_ZoneItem();
                    }


                }


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

    String StockGroupCode = "";

    private void callApi(String searchValue) {
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);


        binding.loader.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                if (fromwhere.equalsIgnoreCase("from")) {

                    hde.put("PageNo", String.valueOf(pageNo));
                    hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                    hde.put("SearchText", searchValue);
                    hde.put("FromDate", startDate);
                    hde.put("ToDate", endDate);
                    hde.put("SubGroupCode", StockGroupCode);
                    hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                    hde.put(Globals.payLoadOrderByName, filterByName);
                    hde.put(Globals.payLoadOrderByAMt, filterByAmount);
                } else {

                    hde.put("PageNo", String.valueOf(pageNo));
                    hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                    hde.put("SearchText", searchValue);
                    hde.put("FromDate", startDate);
                    hde.put("ToDate", endDate);
                    hde.put("GroupCode", StockGroupCode);
                    hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                    hde.put(Globals.payLoadOrderByName, filterByName);
                    hde.put(Globals.payLoadOrderByAMt, filterByAmount);
                }

                Call<ResponseItemDashboard> call;


                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService(getActivity()).getItemOnDashboardPurchase(hde);
                } else {
                    call = NewApiClient.getInstance().getApiService(getActivity()).getItemOnDashboard(hde);
                }


                try {
                    Response<ResponseItemDashboard> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {
                                    AllitemsList.clear();
                                    if (response.body().getData().size() > 0) {
                                        binding.noDatafound.setVisibility(View.GONE);
                                        AllitemsList.addAll(response.body().getData());
                                        adapter = new ItemStockAdapter(requireContext(), AllitemsList, "");
                                        adapter.AllData(AllitemsList);
                                        layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
                                        binding.rvItemDash.setLayoutManager(layoutManager);
                                        binding.rvItemDash.setAdapter(adapter);

                                       /* if (StockGroupCode.isEmpty() && searchValue.isEmpty() && filterByName.isEmpty() && filterByAmount.isEmpty()) {
                                            db.myDataDao().deleteAll();
                                            db.myDataDao().insertAll(AllitemsList);
                                        }*/
                                        Log.e("LOCALSIZE====>", "Size: " + db.myDataDao().getAll().size());

                                    } else {
                                        binding.noDatafound.setVisibility(View.VISIBLE);
                                    }


                                    binding.loader.setVisibility(View.GONE);
                                    // setData(response.body().getData().get(0));

                                    //setRecyclerViewAdapter();
                                    if (adapter != null){
                                        adapter.notifyDataSetChanged();
                                    }
                                    binding.swipeRefreshItems.setRefreshing(false);


                                }
                            }
                        });
                        // Handle successful response

                    } else {
                        binding.swipeRefreshItems.setRefreshing(false);
                        binding.loader.setVisibility(View.GONE);
                        // Handle failed response
                    }
                } catch (IOException e) {
                    binding.swipeRefreshItems.setRefreshing(false);
                    // Handle exception
                }
            }
        }).start();
    }

    private void setRecyclerViewAdapter() {

        callApiGroupItemStock(searchTextValue, startDate, endDate, groupType, "");

      /*  try {
            if (db.myDataDao().getAll().size() > 0) {
                AllitemsList.addAll(db.myDataDao().getAll());
            } else {

                if (Globals.checkInternet(requireContext())) {
                    callApi("");
                }

            }

            adapter = new ItemStockAdapter(requireContext(), AllitemsList);
            adapter.AllData(AllitemsList);
            layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
            binding.rvItemDash.setLayoutManager(layoutManager);
            binding.rvItemDash.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("TAG", "run: " + e.getMessage());
        }*/



              /*  if (Globals.checkInternet(requireContext())) {
                    callApi("");
                }
*/


    /*    adapter = new ItemStockAdapter(requireContext(), AllitemsList);
        adapter.AllData(AllitemsList);
        layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        binding.rvItemDash.setLayoutManager(layoutManager);
        binding.rvItemDash.setAdapter(adapter);*/


    }

    private static final String TAG = "ItemsBottomFragment";


    private void callAllPagesApi() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                if (fromwhere.equalsIgnoreCase("from")) {

                    hde.put("PageNo", String.valueOf(pageNo));
                    hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                    hde.put("SearchText", searchTextValue);
                    hde.put("FromDate", startDate);
                    hde.put("ToDate", endDate);
                    hde.put("SubGroupCode", StockGroupCode);
                    //todo added new filter
                    hde.put(Globals.payLoadOrderByName, filterByName);
                    hde.put(Globals.payLoadOrderByAMt, filterByAmount);
                } else {

                    hde.put("PageNo", String.valueOf(pageNo));
                    hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                    hde.put("SearchText", searchTextValue);
                    hde.put("FromDate", startDate);
                    hde.put("ToDate", endDate);
                    hde.put("GroupCode", StockGroupCode);
                    hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));

                    //todo added new filter
                    hde.put(Globals.payLoadOrderByName, filterByName);
                    hde.put(Globals.payLoadOrderByAMt, filterByAmount);
                }

                Call<ResponseItemDashboard> call;


                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService(getActivity()).getItemOnDashboardPurchase(hde);
                } else {
                    call = NewApiClient.getInstance().getApiService(getActivity()).getItemOnDashboard(hde);
                }
                try {
                    Response<ResponseItemDashboard> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {

                                    if (response.body().getData().size() > 0) ;
                                    AllitemsList.addAll(response.body().getData());
                                    adapter.AllData(AllitemsList);
                                    binding.loader.setVisibility(View.GONE);
                                   /* if (StockGroupCode.isEmpty() && searchTextValue.isEmpty() && filterByName.isEmpty() && filterByAmount.isEmpty()) {
                                        //todo remove delete all line
                                        db.myDataDao().insertAll(response.body().getData());
                                    }*/
                                    adapter.notifyDataSetChanged();

                                }
                            }
                        });
                        // Handle successful response

                    } else {
                        binding.loader.setVisibility(View.GONE);
                        // Handle failed response
                    }
                } catch (IOException e) {
                    // Handle exception
                }
            }
        }).start();
    }


    private void openpopup() {
// .addItem(new PowerMenuItem("New BP",R.drawable.ic_newbp, false)) // add an item.

        PowerMenu powerMenu = new PowerMenu.Builder(requireContext()).addItem(new PowerMenuItem("A to Z", R.drawable.ic_filter_black, false)) // aad an item list.
                .addItem(new PowerMenuItem("Z to A", R.drawable.ic_filter_black, false)) // aad an item list.
                .addItem(new PowerMenuItem("Amount Desc", R.drawable.ic_rupee_symbol, false)) // aad an item list.
                .addItem(new PowerMenuItem("Amount Asc", R.drawable.ic_rupee_symbol, false)) // aad an item list.
                .addItem(new PowerMenuItem("Clear All Filter", R.drawable.ic_filter_black, false)) // aad an item list.
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT) // Animation start point (TOP | LEFT).
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.black)).setTextGravity(Gravity.START).setTextSize(12).setTextTypeface(Typeface.createFromAsset(requireActivity().getAssets(), "poppins_regular.ttf")).setSelectedTextColor(Color.BLACK).setWidth(Globals.pxFromDp(requireContext(), 220f)).setMenuColor(Color.WHITE).setSelectedMenuColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary)).build();
        powerMenu.showAsDropDown(binding.toolbarItemDashBoard.filterView);


        OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
            @Override
            public void onItemClick(int position, PowerMenuItem item) {
                powerMenu.setSelectedPosition(position); // change selected item

                switch (position) {
                    /*case 0:
                    Prefs.putString(Globals.BussinessPageType,"DashBoard");
                    Prefs.putString(Globals.AddBp,"");
                    startActivity(new Intent(MainActivity_B2C.this, AddBPCustomer.class));
                    break;*/
                    case 0:
                        binding.filterAtozShubh.setText("Filter : A To Z");
                        Prefs.putString(Globals.PrefsItemATOZ, Globals.ATOZ);
                        Prefs.putString(Globals.PrefsItemAmount, "");
                        filterByName = Globals.ATOZ;
                        filterByAmount = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);

                        callApi("");

                        break;
                    case 1:
                        binding.filterAtozShubh.setText("Filter : Z To A");
                        Prefs.putString(Globals.PrefsItemATOZ, Globals.ZTOA);
                        Prefs.putString(Globals.PrefsItemAmount, "");
                        filterByName = Globals.ZTOA;
                        filterByAmount = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        callApi("");

                        break;
                    case 2:
                        binding.filterAtozShubh.setText("Filter : Amount Desc");
                        Prefs.putString(Globals.PrefsItemAmount, Globals.DESC);
                        Prefs.putString(Globals.PrefsItemATOZ, "");
                        filterByAmount = Globals.DESC;
                        filterByName = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        callApi("");

                        break;
                    case 3:
                        binding.filterAtozShubh.setText("Filter : Amount Asc");
                        Prefs.putString(Globals.PrefsItemAmount, Globals.DESC);
                        Prefs.putString(Globals.PrefsItemATOZ, "");
                        filterByAmount = Globals.ASC;
                        filterByName = "";
                        binding.filterAtozShubh.setVisibility(View.VISIBLE);
                        callApi("");

                        break;

                    case 4:
                        binding.filterAtozShubh.setText("");
                        Prefs.putString(Globals.PrefsItemAmount, "");
                        Prefs.putString(Globals.PrefsItemATOZ, "");
                        filterByAmount = "";
                        filterByName = "";
                        binding.filterAtozShubh.setVisibility(View.GONE);
                        callApi("");

                        break;


                }

                powerMenu.dismiss();
            }
        };
        powerMenu.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    ArrayList<DataItemFilterDashBoard> AllItemsGroupStockList = new ArrayList<>();
    ItemOnStockGroupAdapter stockGroupAdapter = null;

    private void callApiGroupItemStock(String searchValue, String startDate, String endDate, String type, String zone) {
        binding.loader.setVisibility(View.VISIBLE);
        pageNo = 1;
        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);
        new Thread(new Runnable() {
            @Override
            public void run() {
                binding.loader.setVisibility(View.VISIBLE);
                HashMap<String, String> hde = new HashMap<>();
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SearchText", searchValue);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                if (zone.equalsIgnoreCase("")) {
                    hde.put("Filter", "");
                } else {
                    hde.put("Filter", "Zone");
                }

                hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, filterByName);
                hde.put(Globals.payLoadOrderByAMt, filterByAmount);

                Call<ResponseItemFilterDashboard> call;


                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService(getActivity()).getFilterGroupItemStockPurchase(hde);
                } else {
                    call = NewApiClient.getInstance().getApiService(getActivity()).getFilterGroupItemStock(hde);
                }
                try {
                    Response<ResponseItemFilterDashboard> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {
                                    binding.swipeRefreshItems.setRefreshing(false);
                                    //  AllitemsList.clear();
                                    AllItemsGroupStockList.clear();
                                    if (response.body().getData().size() > 0) {
                                        binding.noDatafound.setVisibility(View.GONE);
                                        AllItemsGroupStockList.addAll(response.body().getData());
                                        //  itemsFilterDatabase.myDataDao().insertAll(AllItemsGroupStockList);

                                        // setData(response.body().getData().get(0));
                                        try {
                                            try {
                                                stockGroupAdapter = new ItemOnStockGroupAdapter(requireContext(), AllItemsGroupStockList, zone, "");
                                            } catch (Exception e) {
                                                Log.e(TAG, "runGRoupFIlter: " + e.getMessage());
                                            }
                                            stockGroupAdapter.AllData(AllItemsGroupStockList);
                                            try {
                                                layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
                                            } catch (Exception e) {
                                                Log.e(TAG, "runGroupLINEAR: " + e.getMessage());
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        binding.rvItemDash.setLayoutManager(layoutManager);
                                        binding.rvItemDash.setAdapter(stockGroupAdapter);
                                        stockGroupAdapter.notifyDataSetChanged();

                                    } else {
                                        binding.rvItemDash.setVisibility(View.GONE);
                                        binding.noDatafound.setVisibility(View.VISIBLE);
                                    }


                                    binding.loader.setVisibility(View.GONE);


                                }
                            }
                        });
                        // Handle successful response

                    } else {
                        binding.loader.setVisibility(View.GONE);
                        // Handle failed response
                    }
                } catch (IOException e) {
                    // Handle exception
                }
            }
        }).start();
    }


    private void callAllPagesApi_GroupItemStock() {
        binding.loader.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SearchText", searchTextValue);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("Filter", "");
                hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, filterByName);
                hde.put(Globals.payLoadOrderByAMt, filterByAmount);


                Call<ResponseItemFilterDashboard> call;


                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService(getActivity()).getFilterGroupItemStockPurchase(hde);
                } else {
                    call = NewApiClient.getInstance().getApiService(getActivity()).getFilterGroupItemStock(hde);
                }

                try {
                    Response<ResponseItemFilterDashboard> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {

                                    if (response.body().getData().size() > 0) ;
                                    AllItemsGroupStockList.addAll(response.body().getData());
                                    stockGroupAdapter.AllData(AllItemsGroupStockList);
                                    // itemsFilterDatabase.myDataDao().insertAll(AllItemsGroupStockList);
                                    binding.loader.setVisibility(View.GONE);
                                    // setData(response.body().getData().get(0));
                                    stockGroupAdapter.notifyDataSetChanged();

                                }
                            }
                        });
                        // Handle successful response

                    } else {
                        binding.loader.setVisibility(View.GONE);
                        // Handle failed response
                    }
                } catch (IOException e) {
                    // Handle exception
                }
            }
        }).start();
    }


    private void callAllPagesApi_ZoneItem() {
        binding.loader.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> hde = new HashMap<>();
                hde.put("PageNo", String.valueOf(pageNo));
                hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
                hde.put("SearchText", searchTextValue);
                hde.put("FromDate", startDate);
                hde.put("ToDate", endDate);
                hde.put("Filter", "Zone");
                hde.put("SalesEmployeeCode", Prefs.getString(Globals.SalesEmployeeCode, ""));
                hde.put(Globals.payLoadOrderByName, filterByName);
                hde.put(Globals.payLoadOrderByAMt, filterByAmount);


                Call<ResponseItemFilterDashboard> call;


                if (Prefs.getBoolean(Globals.ISPURCHASE, false)) {
                    call = NewApiClient.getInstance().getApiService(getActivity()).getFilterItemsZonePurchase(hde);
                } else {
                    call = NewApiClient.getInstance().getApiService(getActivity()).getFilterItemsZone(hde);
                }


                try {
                    Response<ResponseItemFilterDashboard> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {

                                    if (response.body().getData().size() > 0) ;
                                    AllItemsGroupStockList.addAll(response.body().getData());
                                    stockGroupAdapter.AllData(AllItemsGroupStockList);
                                    // itemsFilterDatabase.myDataDao().insertAll(AllItemsGroupStockList);
                                    binding.loader.setVisibility(View.GONE);
                                    // setData(response.body().getData().get(0));
                                    stockGroupAdapter.notifyDataSetChanged();

                                }
                            }
                        });
                        // Handle successful response

                    } else {
                        binding.loader.setVisibility(View.GONE);
                        // Handle failed response
                    }
                } catch (IOException e) {
                    // Handle exception
                }
            }
        }).start();
    }


}