package com.cinntra.ledure.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.AddOrderAct;
import com.cinntra.ledure.activities.AddQuotationAct;
import com.cinntra.ledure.activities.ItemsList;
import com.cinntra.ledure.activities.SelectedItems;
import com.cinntra.ledure.adapters.CategoryAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.interfaces.DatabaseClick;
import com.cinntra.ledure.model.DocumentLines;
import com.cinntra.ledure.model.ItemCategoryResponse;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.cinntra.ledure.activities.AddOrderAct.addQuotationObj;


public class AddQuotationForm_One_Fragment extends Fragment implements View.OnClickListener, DatabaseClick {

    @BindView(R.id.head_title)
    TextView head_title;
  @BindView(R.id.next_button)
  Button next_button;
  @BindView(R.id.item_frame)
  RelativeLayout item_frame;
  @BindView(R.id.total_before_discont_value)
  EditText total_before_discont_value;
  @BindView(R.id.tax_value)
  EditText tax_value;
  @BindView(R.id.itemCount)
  TextView itemCount;
  @BindView(R.id._discont_value)
  EditText discont_value;
    @BindView(R.id.posting_date_value)
    EditText posting_date_value;


    @BindView(R.id.delievery_term_value)
    TextView delievery_term_value;
    @BindView(R.id.term_condition)
    TextView term_condition;
    @BindView(R.id.status_value)
     EditText delivery_charges;

     FragmentActivity act;

    public static int ITEMSVIEWCODE   = 1000;

    public AddQuotationForm_One_Fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AddQuotationForm_One_Fragment newInstance(String param1, String param2) {
        AddQuotationForm_One_Fragment fragment = new AddQuotationForm_One_Fragment();
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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        act =getActivity();
        View v=inflater.inflate(R.layout.fragment_add_qt_form_one, container, false);
        ButterKnife.bind(this,v);
        Globals.SelectedItems.clear();
        head_title.setText(getResources().getString(R.string.add_quotation));
        next_button.setOnClickListener(this);
        item_frame.setOnClickListener(this);
        itemCount.setText("Item ("+Globals.SelectedItems.size()+")");
        return v;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.next_button){
          if(validation(Globals.SelectedItems.size(),total_before_discont_value.getText().toString().trim(),discont_value.getText().toString().trim(),tax_value.getText().toString().trim() ))
            {
                String deliveryCharges ="0";
                if(!delivery_charges.getText().toString().trim().isEmpty()&&delivery_charges.getText().toString()!=null)
                    deliveryCharges = delivery_charges.getText().toString().trim();
                AddQuotationAct.addQuotationObj.setDiscountPercent(Float.valueOf(discont_value.getText().toString().trim()));
                AddQuotationAct.addQuotationObj.setTermCondition(term_condition.getText().toString().trim());
                AddQuotationAct.addQuotationObj.setDeliveryTerm(delievery_term_value.getText().toString().trim());
                AddQuotationAct.addQuotationObj.setAdditionalCharges(tax_value.getText().toString().trim());
                AddQuotationAct.addQuotationObj.setDeliveryCharge(deliveryCharges);


                AddQuotationForm_Fianl_Fragment fragment = new AddQuotationForm_Fianl_Fragment();
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.temp, fragment).addToBackStack("");
                fragmentTransaction.commit();
            }
        }
        else if(v.getId()==R.id.item_frame)
        {
            if(Globals.SelectedItems.size()==0) {

                openCategorydailog();
            }
            else {
                Intent intent = new Intent(getContext(), SelectedItems.class);
                intent.putExtra("FromWhere","addQt");
                intent.putExtra("warehouse", AddQuotationAct.addQuotationObj.getWarehouseCode());
                startActivityForResult(intent, ITEMSVIEWCODE);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK&&requestCode==ITEMSVIEWCODE) {
        setData();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        itemCount.setText("Item ("+Globals.SelectedItems.size()+")");

    }

    private boolean validation(int Items, String Total, String discount,
                               String tax  )
    {
        if(Items<=0){
            Globals.showMessage(getActivity(),"Select atleast 1 item");
            return false;
        }
        else if(Total.isEmpty()){
            Globals.showMessage(getActivity(),"Enter total before discount");
            return false;
        }
        else if(discount.isEmpty()){
            Globals.showMessage(getActivity(),"Enter discount");
            return false;
        }

        else if(tax.isEmpty()){
            Globals.showMessage(getActivity(),"Enter tax value");
            return false;
        }

        return true;
    }
    Dialog TaxListdialog;
    private void openCategorydailog() {
        RelativeLayout backPress;
        TextView head_title;
        RecyclerView recyclerview;
        ProgressBar loader;

        TaxListdialog = new Dialog(getContext());
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View custom_dialog =layoutInflater.inflate(R.layout.taxes_alert,null);
        recyclerview = custom_dialog.findViewById(R.id.recyclerview);
        backPress    = custom_dialog.findViewById(R.id.back_press);
        head_title   = custom_dialog.findViewById(R.id.head_title);
        loader       = custom_dialog.findViewById(R.id.loader);
        head_title.setText(getContext().getString(R.string.select_tax));
        TaxListdialog.setContentView(custom_dialog);
        TaxListdialog.getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));
        TaxListdialog.show();

        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaxListdialog.dismiss();
            }
        });
        Call<ItemCategoryResponse> call = NewApiClient.getInstance().getApiService().getAllCategory();
        call.enqueue(new Callback<ItemCategoryResponse>() {
            @Override
            public void onResponse(Call<ItemCategoryResponse> call, Response<ItemCategoryResponse> response) {
                loader.setVisibility(View.GONE);
                if(response.code()==200)
                {

                    CategoryAdapter adapter = new CategoryAdapter(AddQuotationForm_One_Fragment.this, response.body().getData(),TaxListdialog);
                    recyclerview.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
                    recyclerview.setAdapter(adapter);
                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s,QuotationResponse.class);
                        Toast.makeText(act, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ItemCategoryResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(act, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onClick(int po) {
        Intent intent = new Intent(getContext(), ItemsList.class);
        intent.putExtra("CategoryID",po);
        intent.putExtra("warehouse",AddQuotationAct.addQuotationObj.getWarehouseCode());

        startActivityForResult(intent, ITEMSVIEWCODE);
    }

    private void setData(){
        itemCount.setText("Item ("+Globals.SelectedItems.size()+")");
        tax_value.setText("0");
        double taxdiscount = 0.0;
        if(!total_before_discont_value.getText().toString().isEmpty()||total_before_discont_value.getText().toString()=="")
            taxdiscount = Double.parseDouble(total_before_discont_value.getText().toString());

        posting_date_value.setText(String.valueOf(Globals.calculatetotal(10,taxdiscount)));

        double discount = 0.0;
        if(!discont_value.getText().toString().trim().isEmpty())
            discount = Double.valueOf(discont_value.getText().toString().trim());

       total_before_discont_value.setText(""+ItemTotalAtAdd(Globals.SelectedItems,discount));
      //  total_before_discont_value.setText(String.valueOf(ItemTotalAtAdd(Globals.SelectedItems,discount)));

    }
    private double ItemTotalAtAdd(ArrayList<DocumentLines> selectedItems, double headerDiscount)
    {
        double sum = 0;

        for (DocumentLines i : selectedItems)
        {
            double total              =     Double.parseDouble(String.valueOf(Double.parseDouble(i.getUnitPrice())*Double.parseDouble(i.getQuantity())));
            double headDiscountValue  =   total*headerDiscount/100;
            total                = total-headDiscountValue;
            String itemDis   = String.valueOf(i.getDiscountPercent());
            double itemDisValue  = total*Double.parseDouble(itemDis)/100;
            total                     = total-itemDisValue;
            double itemTax = total*0;
            total = total +itemTax;
            // sum += Double.parseDouble(String.valueOf(Double.parseDouble(i.getUnitPrice())*Double.parseDouble(i.getQuantity())));
            sum = sum+total;

        }
        return sum;
    }
}