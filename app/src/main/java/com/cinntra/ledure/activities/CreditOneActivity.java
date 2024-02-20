package com.cinntra.ledure.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.CustomersItemsAdapter;
import com.cinntra.ledure.fragments.WebViewBottomSheetFragment;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.CreditNoteInvoiceResponse;
import com.cinntra.ledure.model.InvoiceNewData;
import com.cinntra.ledure.webservices.NewApiClient;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class CreditOneActivity extends AppCompatActivity {
    @BindView(R.id.total_amount)
    TextView total_amount;
    @BindView(R.id.company_name)
    TextView company_name;
    @BindView(R.id.invoice_date)
    TextView invoice_date;
    @BindView(R.id.due_date)
    TextView due_date;
    @BindView(R.id.net_amount)
    TextView net_amount;
    @BindView(R.id.cgst)
    TextView cgst;
    @BindView(R.id.sgst)
    TextView sgst;
    @BindView(R.id.gross_total)
    TextView gross_total;
    @BindView(R.id.status_back)
    LinearLayout status_back;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.credit_memo_txt)
    TextView credit_memo_txt;

    @BindView(R.id.item_recyclerview)
    RecyclerView item_recyclerview;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btnShareInVoice)
    Button btnShareInVoice;
    String id="";
    String heading="";
   // String paymentStatus="";
    String url="";
    WebView dialogWeb;
    String title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_one);
        ButterKnife.bind(this);
        id      = getIntent().getStringExtra("ID");
        heading = getIntent().getStringExtra("Heading");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

       // String id     = getIntent().getStringExtra("ID");
        //paymentStatus = getIntent().getStringExtra("status");
        Log.e("ID===>", "onCreate: "+id);



//
        callOneInvoice(id);
        //setList();

        btnShareInVoice.setOnClickListener(view ->
        {
            shareLedgerData();
            //showBottomSheetDialog();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transaction_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        MenuItem calenderItem = menu.findItem(R.id.calendar);
        MenuItem shareItem = menu.findItem(R.id.ledger);
        MenuItem infoItem = menu.findItem(R.id.info_trans);
        MenuItem ledger = menu.findItem(R.id.ledger);
        MenuItem share = menu.findItem(R.id.share_received);
        MenuItem atoz = menu.findItem(R.id.filterAtoZ);
        MenuItem ztoa = menu.findItem(R.id.filterZtoA);
        MenuItem clearAllFilter = menu.findItem(R.id.clearAllFilter);
        searchItem.setVisible(false);
        calenderItem.setVisible(false);
        shareItem.setVisible(false);
        share.setVisible(false);
        atoz.setVisible(false);
        ztoa.setVisible(false);
        clearAllFilter.setVisible(false);



        /**shubh***/
        infoItem.setVisible(false);
        //   ledger.setVisible(true);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.calendar:
                Globals.selectDat(this);
                break;
        }
        return true;
    }

    private void setData(InvoiceNewData invoiceNewData) {
        try {
            String result = java.net.URLDecoder.decode(invoiceNewData.getCardName(), StandardCharsets.UTF_8.name());
            company_name.setText(result);
        } catch (UnsupportedEncodingException e) {
            // not going to happen - value came from JDK's own StandardCharsets
        }
        if(heading.equalsIgnoreCase("ttAPCreditNote"))
        {
            btnShareInVoice.setText("Share Debit Note");
            credit_memo_txt.setText("Debit Memo Date");
            url = Globals.Ap_creditNoteUrl + "id="+id;
            title=getString(R.string.share_debit_note);
        }
         else if(invoiceNewData.getDocType().equalsIgnoreCase("dDocument_Items"))
         {
             url = Globals.creditNoteUrl + "id="+id;
             title=getString(R.string.share_credit_note);
         }
         else if(invoiceNewData.getDocType().equalsIgnoreCase("dDocument_Service"))
         {
             url = Globals.creditNoteUrl_Service + "id="+id;
             title=getString(R.string.share_credit_note);
         }



        total_amount.setText("₹ " + Globals.numberToK(invoiceNewData.getDocTotal()));
        String reverseInvoiceDate= Globals.convertDateFormat(invoiceNewData.getDocDate());
        String reverseDocDueDate= Globals.convertDateFormat(invoiceNewData.getDocDueDate());
        invoice_date.setText(reverseInvoiceDate);
        due_date.setText(reverseDocDueDate);
        toolbar.setTitle("Voucher no: " + invoiceNewData.getDocNum());
        float net = Float.valueOf(invoiceNewData.getDocTotal()) - Float.valueOf(invoiceNewData.getVatSum());
        net_amount.setText("₹ " + Globals.numberToK(String.valueOf(net)));
        // cgst.setText("₹ "+invoiceNewData.getDocTotal());
        sgst.setText("₹ " +Globals.numberToK( invoiceNewData.getVatSum()));
        gross_total.setText("₹ " +Globals.numberToK( invoiceNewData.getDocTotal()));
        CustomersItemsAdapter adapter = new CustomersItemsAdapter(this, invoiceNewData.getDocumentLines());
        item_recyclerview.setAdapter(adapter);




    }
    Call<CreditNoteInvoiceResponse> call;
    private void callOneInvoice(String invoiceID) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, String> hde = new HashMap<>();
                hde.put("id", invoiceID);

                if(Prefs.getString(Globals.Sale_Purchse_Diff, "").equalsIgnoreCase("ttAPCreditNote"))
                {
                call = NewApiClient.getInstance().getApiService().purchase_credit_One(hde);
                }
                else
                {
                    call = NewApiClient.getInstance().getApiService().invoice_credit_One(hde);
                }



                try {
                    Response<CreditNoteInvoiceResponse> response = call.execute();
                    if (response.isSuccessful()) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {
                                    if (response.body().getValue().size() > 0)
                                        setData(response.body().getValue().get(0));
                                }
                            }
                        });
                        // Handle successful response

                    } else {
                        // Handle failed response
                    }
                } catch (IOException e) {
                    // Handle exception
                }
            }
        }).start();
    }
    private void shareLedgerData()
    {


        WebViewBottomSheetFragment addPhotoBottomDialogFragment =
                WebViewBottomSheetFragment.newInstance(dialogWeb,url,title);
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                "");
    }

    public  boolean isAppInstalled(String packageName) {
        try {
            getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException ignored) {
            Log.e("NameNotFoundException=>","ignored");
            return false;
        }
    }



}