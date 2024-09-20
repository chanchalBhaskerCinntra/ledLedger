package com.cinntra.ledure.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.CreditCostomersAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.CustomerBusinessRes;
import com.cinntra.ledure.model.Customers_Report;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.pixplicity.easyprefs.library.Prefs;
import java.util.ArrayList;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreditNotes_Fragment extends Fragment {


    ArrayList<Customers_Report> customerList = new ArrayList<>();

    @BindView(R.id.customers_recyclerview)
    RecyclerView customerRecyclerView;
    @BindView(R.id.receive_pending_layout)
    LinearLayout receive_pending_layout;
    TextView salesvalue;
    TextView from_to_date;
    Spinner type_dropdown;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;

    @BindView(R.id.loader)
    ProgressBar loader;







    public CreditNotes_Fragment(TextView salesvalue, TextView from_to_date, Spinner type_dropdown) {
        // Required empty public constructor
        this.salesvalue = salesvalue;
        this.from_to_date = from_to_date;
        this.type_dropdown = type_dropdown;
    }


    /*// TODO: Rename and change types and number of parameters
    public static CreditNotes_Fragment newInstance(String param1, String param2) {
        CreditNotes_Fragment fragment = new CreditNotes_Fragment(salesvalue, type_dropdown);
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b = getArguments();
            customerList = (ArrayList<Customers_Report>) b.getSerializable(Globals.LedgerCompanyData);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.ledger_item_layout, container, false);
        ButterKnife.bind(this, v);
        receive_pending_layout.setVisibility(View.GONE);
        type_dropdown.setVisibility(View.GONE);
        layoutManager=new LinearLayoutManager(requireContext());
        // Toast.makeText(requireContext(), "Stock Group", Toast.LENGTH_SHORT).show();
        startDate = Prefs.getString(Globals.FROM_DATE,"");
        endDate   = Prefs.getString(Globals.TO_DATE,"");
        TotalCreditNotesOnePage(startDate, endDate);
        customerRecyclerView.addOnScrollListener(scrollListener);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       /* CustomersReportAdapter adapter =new CustomersReportAdapter(requireContext(),customerList);
        customerRecyclerView.setAdapter(adapter);*/

    }


    boolean isLoading = false;
    boolean islastPage = false;
    boolean isScrollingpage = false;

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener()
      {

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
            boolean shouldPaginate =
                    isNotLoadingAndNotLastPage && isNotAtBeginning && isAtLastItem && isTotaolMoreThanVisible
                            && isScrollingpage;

            if (isScrollingpage && (visibleItemCount + firstVisibleitempositon == totalItemCount)) {
                pageNo++;
                //  itemOnPageBasis(pageNo);
                TotalCreditNotesALlPageBasis("", "");
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

    int pageNo = 1;
    CreditCostomersAdapter adapter;
    ArrayList<BusinessPartnerData> AllItemList = new ArrayList<>();
    LinearLayoutManager layoutManager;

    private void TotalCreditNotesOnePage(String fromDate, String toDate) {
        HashMap<String, String> hde = new HashMap<>();
        hde.put("FromDate", fromDate);
        hde.put("ToDate", toDate);
        hde.put("Code", "");
        hde.put("Filter", "");
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));

        Call<CustomerBusinessRes> call;

        if (Prefs.getBoolean(Globals.ISPURCHASE,false)){
            call = NewApiClient.getInstance().getApiService(getActivity()).credit_note_dashboard_purchase(hde);
        }else {
            call = NewApiClient.getInstance().getApiService(getActivity()).credit_note_dashboard(hde);
        }

//        Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService(getActivity()).credit_note_dashboard(hde);
        call.enqueue(new Callback<CustomerBusinessRes>() {
            @Override
            public void onResponse(Call<CustomerBusinessRes> call, Response<CustomerBusinessRes> response) {
                if (response != null) {
                    salesvalue.setText(getResources().getString(R.string.Rs)+" " + Globals.numberToK(response.body().getDifferenceAmount()));
                    AllItemList.clear();
                    if (response.body().getData().size()>0){
                        AllItemList.addAll(response.body().getData());
                    }else {
                        Toast.makeText(requireContext(), "No Data Found", Toast.LENGTH_SHORT).show();
                    }

                     adapter = new CreditCostomersAdapter(getContext(), AllItemList);

                    customerRecyclerView.setAdapter(adapter);
                    customerRecyclerView.setLayoutManager(layoutManager);



                }
            }

            @Override
            public void onFailure(Call<CustomerBusinessRes> call, Throwable t) {
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void TotalCreditNotesALlPageBasis(String fromDate, String toDate) {
        HashMap<String, String> hde = new HashMap<>();

        hde.put("Code", "");
        hde.put("Filter", "");
        hde.put("FromDate", fromDate);
        hde.put("ToDate", toDate);
        hde.put("PageNo", String.valueOf(pageNo));
        hde.put("MaxSize", String.valueOf(Globals.QUERY_PAGE_SIZE));
        Call<CustomerBusinessRes> call;

        if (Prefs.getBoolean(Globals.ISPURCHASE,false)){
            call = NewApiClient.getInstance().getApiService(getActivity()).credit_note_dashboard_purchase(hde);
        }else {
            call = NewApiClient.getInstance().getApiService(getActivity()).credit_note_dashboard(hde);
        }
//        Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService(getActivity()).credit_note_dashboard(hde);
        call.enqueue(new Callback<CustomerBusinessRes>() {
            @Override
            public void onResponse(Call<CustomerBusinessRes> call, Response<CustomerBusinessRes> response) {
                if (response != null) {
                    if (response.body().getStatus() == 200) {
                        //  AllItemList.clear();
                        AllItemList.addAll(response.body().getData());


                        adapter.notifyDataSetChanged();
                        if (response.body().getData().size() == 0) {
                            pageNo++;
                            no_datafound.setVisibility(View.VISIBLE);
                        } else {
                            no_datafound.setVisibility(View.INVISIBLE);
                        }
                        loader.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<CustomerBusinessRes> call, Throwable t) {

            }
        });
    }



    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    String startDate = "";
    String endDate = "";
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;

    private void dateRangeSelector() {


        if (startDatelng == 0.0) {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();
        } else {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(startDatelng, endDatelng)).build();

        }

        materialDatePicker.show(getActivity().getSupportFragmentManager(), "Tag_Picker");
       /* materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                Log.e("FromDate=>",String.valueOf(selection));

            }
        });
*/
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                startDatelng = selection.first;
                endDatelng = selection.second;
                startDate = Globals.Date_yyyy_mm_dd(startDatelng);
                endDate = Globals.Date_yyyy_mm_dd(endDatelng);
                from_to_date.setText(startDate + " - " + endDate);
                TotalCreditNotesOnePage(startDate, endDate);
            }
        });


    }

}