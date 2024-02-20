package com.cinntra.ledure.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cinntra.ledure.R;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;
import com.cinntra.ledure.model.AddQuotation;
import com.cinntra.ledure.model.CustomerItem;
import com.cinntra.ledure.model.DocumentLines;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.webservices.APIsClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateContact extends MainBaseActivity implements View.OnClickListener {
     public static int PARTNERCODE = 100;
     public static int ITEMSCODE   = 1000;

     @BindView(R.id.head_title)
     TextView head_title;
     @BindView(R.id.back_press)
     RelativeLayout back_press;
     @BindView(R.id.businessPartners)
     LinearLayout businessPartners;

    @BindView(R.id.series_value)
    EditText series_value;
    @BindView(R.id.bussiness_partner_value)
    EditText bussiness_partner_value;
    @BindView(R.id.currency_value)
    EditText currency_value;
    @BindView(R.id.sales_employee_value)
    EditText sales_employee_value;
    @BindView(R.id.posting_value)
    TextView posting_value;
    @BindView(R.id.valid_till_value)
    TextView valid_till_value;
    @BindView(R.id.document_date_value)
    TextView document_date_value;
    @BindView(R.id.remark_value)
    EditText remark_value;
    @BindView(R.id.ship_to_value)
    EditText ship_to_value;
    @BindView(R.id.bill_to_value)
    EditText bill_to_value;
    @BindView(R.id.shipping_type_value)
    EditText shipping_type_value;
    @BindView(R.id.payment_term_value)
    EditText payment_term_value;
    @BindView(R.id.payment_method_value)
    EditText payment_method_value;
    @BindView(R.id.item_Value)
    TextView item_Value;
    @BindView(R.id.itemsView)
    LinearLayout itemsView;
    @BindView(R.id.submit)
    Button submit;
    public static String CardValue;
    public static int PriceListNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.new_contact);
    ButterKnife.bind(this);
    setDefaults();

     }

    private void setDefaults() {
        head_title.setText(getResources().getString(R.string.new_contact));
        back_press.setOnClickListener(this);
        businessPartners.setOnClickListener(this);
        itemsView.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
    case R.id.back_press:
        finish();
        break;
        case R.id.businessPartners:
            Prefs.putString(Globals.BussinessPageType,"CreateCon");
           Intent i = new Intent(CreateContact.this, BussinessPartners.class);
           startActivityForResult(i,PARTNERCODE);
           //startActivity(i);
            break;
        case R.id.itemsView:
            if(Globals.SelectedItems.size()==0) {
            Intent intent = new Intent(CreateContact.this, ItemsList.class);
            startActivityForResult(intent, ITEMSCODE);
            }
            else {
            Intent intent = new Intent(CreateContact.this, SelectedItems.class);
            intent.putExtra("FromWhere","NewContact");
            startActivityForResult(intent, ITEMSCODE);
            }
            break;
        case R.id.submit:

            if(Globals.checkInternet(getApplicationContext())) {
                if (postJson(Globals.SelectedItems) != null) {
                    AddQuotation obj = new AddQuotation();
                    obj.setCardCode(CardValue);
                    obj.setDocumentLines(postJson(Globals.SelectedItems));
                    addQuotation(obj);
                } else {
                    Toast.makeText(CreateContact.this, getString(R.string.something_wron_msz), Toast.LENGTH_SHORT).show();
                }
            }
            break;
          }
       }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PARTNERCODE) {
    if(resultCode == RESULT_OK) {
    CustomerItem customerItem = (CustomerItem) data.getSerializableExtra(Globals.CustomerItemData);
    setData(customerItem);
        }
      }
    else if(requestCode == ITEMSCODE)
    {
   item_Value.setText(""+Globals.SelectedItems.size());
    }
    }

    private void setData(CustomerItem customerItem)
      {
     PriceListNum = customerItem.getPriceListNum();
     CardValue = customerItem.getCardCode();
     series_value.setText(customerItem.getSeries());
     bussiness_partner_value.setText(customerItem.getCardName());
     //sales_employee_value.setText(customerItem.get);
     currency_value.setText(customerItem.getCurrency());
     posting_value.setText(customerItem.getCreateDate());
     valid_till_value.setText(customerItem.getUpdateDate());
     document_date_value.setText(customerItem.getUpdateDate());
     remark_value.setText(customerItem.getRemarks());
     ship_to_value.setText(customerItem.getShipToDefault());
     //bill_to_value.setText(customerItem.getb);
      shipping_type_value.setText(customerItem.getShippingType());
      //payment_term_value.setText(customerItem.getShippingType());
      //payment_method_value.setText(customerItem.getShippingType());



      }
    ArrayList<DocumentLines> postlist;
      private ArrayList<DocumentLines> postJson(ArrayList<DocumentLines> list){
        postlist = new ArrayList<>();
          for (int i=0;i<list.size();i++
               ) {
              DocumentLines dc = new DocumentLines();
              dc.setItemCode(Globals.SelectedItems.get(i).getItemCode());
              dc.setQuantity(Globals.SelectedItems.get(i).getQuantity());
              dc.setTaxCode(Globals.SelectedItems.get(i).getTaxCode());//BED+VAT
              dc.setUnitPrice(Globals.SelectedItems.get(i).getUnitPrice());
              postlist.add(dc);
          }

          return postlist;
      }


    private void addQuotation(AddQuotation in)
        {
   Call<QuotationResponse> call = APIsClient.getInstance().getApiService().addQuotation(in);
   call.enqueue(new Callback<QuotationResponse>() {
      @Override
   public void onResponse(Call<QuotationResponse> call, Response<QuotationResponse> response) {
    if(response.code()==201)
          {
     Globals.SelectedItems.clear();
    Toast.makeText(CreateContact.this, "Posted Successfully.", Toast.LENGTH_SHORT).show();
        }
       else
        {
        //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
        Gson gson = new GsonBuilder().create();
        QuotationResponse mError = new QuotationResponse();
       try {
        String s =response.errorBody().string();
        mError= gson.fromJson(s,QuotationResponse.class);
        Toast.makeText(CreateContact.this, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
         } catch (IOException e) {
       //handle failure to read error
         }
       //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
            }
       }
       @Override
       public void onFailure(Call<QuotationResponse> call, Throwable t) {
       Toast.makeText(CreateContact.this, t.getMessage(), Toast.LENGTH_SHORT).show();
           }
        });
       }
   }