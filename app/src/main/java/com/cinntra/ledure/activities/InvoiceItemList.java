package com.cinntra.ledure.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.Invoice_Item_Adapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.globals.MainBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cinntra.ledure.activities.CreateContact.ITEMSCODE;


public class InvoiceItemList extends MainBaseActivity implements View.OnClickListener {

     @BindView(R.id.head_title)
    TextView head_title;
     @BindView(R.id.back_press)
     RelativeLayout back_press;
     @BindView(R.id.new_quatos)
     RelativeLayout new_quatos;
     @BindView(R.id.itemsRecycler)
     RecyclerView itemsRecycler;
     @BindView(R.id.done)
     Button done;
     @BindView(R.id.loader)
     ProgressBar loader;


    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item);
        ButterKnife.bind(this);
        setDefaults();


    }

    private void setDefaults() {
        String fromwhere = getIntent().getStringExtra("FromWhere");
        if(fromwhere.equalsIgnoreCase("invoices")){
            new_quatos.setVisibility(View.GONE);
            new_quatos.setClickable(false);
        }

        head_title.setText(getResources().getString(R.string.selected_items));
        back_press.setOnClickListener(this);
        new_quatos.setOnClickListener(this);
        done.setOnClickListener(this);

       setAdapter();
    }
    Invoice_Item_Adapter adapter;
    private void setAdapter() {
        adapter = new Invoice_Item_Adapter(com.cinntra.ledure.activities.InvoiceItemList.this, Globals.SelectedItems);
        itemsRecycler.setLayoutManager(new LinearLayoutManager(com.cinntra.ledure.activities.InvoiceItemList.this, RecyclerView.VERTICAL,false));
        itemsRecycler.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
    case R.id.back_press:
        Intent ii = new Intent();
        setResult(RESULT_OK, ii);
        finish();
        break;
        case R.id.new_quatos:
            Intent intent = new Intent(com.cinntra.ledure.activities.InvoiceItemList.this, ItemsList.class);
            startActivityForResult(intent, ITEMSCODE);
            break;
        case R.id.done:
            Intent i = new Intent();
            setResult(RESULT_OK, i);
            finish();
            break;
          }
       }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (resultCode == RESULT_OK) {
          setAdapter();

        }
    }




   }