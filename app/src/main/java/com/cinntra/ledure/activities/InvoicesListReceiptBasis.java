package com.cinntra.ledure.activities;



import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.baoyz.widget.PullRefreshLayout;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.InvoiceReceiptAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.IncomingPaymentInvoices;
import com.cinntra.ledure.model.ReceiptResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InvoicesListReceiptBasis extends MainBaseActivity implements View.OnClickListener {

    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;
    @BindView(R.id.search)
    RelativeLayout search;
    @BindView(R.id.searchLay)
    RelativeLayout searchLay;
    @BindView(R.id.mainHeaderLay)
    RelativeLayout mainHeaderLay;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.filterView)
    RelativeLayout filterView;
    @BindView(R.id.new_quatos)
    RelativeLayout new_quatos;
    @BindView(R.id.customer_lead_List)
    RecyclerView customer_lead_List;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;
     LinearLayoutManager layoutManager;
     int skipSize = 20;
     int pageSize =100;
     int pageNo =1;


    //private int currentPage = PAGE_START;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_customer);
        ButterKnife.bind(this);
        filterView.setVisibility(View.GONE);
        new_quatos.setVisibility(View.GONE);

        setDefaults();

        eventSearchManager();






    }

    @Override
    public void onBackPressed()
    {

        if(mainHeaderLay.getVisibility()==View.GONE)
        {
            searchLay.setVisibility(View.GONE);
            mainHeaderLay.setVisibility(View.VISIBLE);
        }
        else {
            super.onBackPressed();
        }
    }

    private void eventSearchManager()
    {
        searchView.setBackgroundColor(Color.parseColor("#00000000"));
        searchLay.setVisibility(View.GONE);
        searchView.setVisibility(View.GONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
           {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText)
            {
               /* if(adapter!=null){
                    adapter.filter(newText);
                }*/
                return true;
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setDefaults()
       {

    back_press.setOnClickListener(this);
    search.setOnClickListener(this);


    String receipt = getIntent().getStringExtra("receiptID");

     loader.setVisibility(View.VISIBLE);
        if (Globals.checkInternet(getApplicationContext())&&!receipt.isEmpty())
               cashDiscountApi(Integer.valueOf(receipt));
               //cashDiscountApiTest();
       }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
    case R.id.back_press:

        finish();
        break;
        case R.id.search:
            mainHeaderLay.setVisibility(View.GONE);
            searchLay.setVisibility(View.VISIBLE);

            searchView.setIconifiedByDefault(true);
            searchView.setFocusable(true);
            searchView.setIconified(false);
            searchView.requestFocusFromTouch();
            break;

          }
       }


    boolean dataoverFromAPI =true;


    InvoiceReceiptAdapter adapter;
    private ArrayList<IncomingPaymentInvoices> cashList = new ArrayList<>();
    private void cashDiscountApi(int receiptId)
           {
        HashMap<String,Integer> hde = new HashMap<>();
        hde.put("ReceiptId",receiptId);
        Call<ReceiptResponse> call = NewApiClient.getInstance().getApiService(this).oneReceipt(hde);
        call.enqueue(new Callback<ReceiptResponse>() {
            @Override
            public void onResponse(Call<ReceiptResponse> call, Response<ReceiptResponse> response) {
                if(response.code()==200) {
                    loader.setVisibility(View.GONE);
                    if(response.body().getValue() == null || response.body().getValue().size() == 0){
                        Globals.setmessage(getApplicationContext());
                        no_datafound.setVisibility(View.VISIBLE);
                    }else {
                        cashList.clear();
                        head_title.setText(response.body().getValue().get(0).getCardName());
                        cashList.addAll(response.body().getValue().get(0).getIncomingPaymentInvoices());
                         adapter = new InvoiceReceiptAdapter(InvoicesListReceiptBasis.this,cashList);
                        customer_lead_List.setLayoutManager(new LinearLayoutManager(InvoicesListReceiptBasis.this, RecyclerView.VERTICAL,false));
                        customer_lead_List.setAdapter(adapter);

                        adapter.notifyDataSetChanged();
                        no_datafound.setVisibility(View.GONE);

                        }




                }
            }
            @Override
            public void onFailure(Call<ReceiptResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(InvoicesListReceiptBasis.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}