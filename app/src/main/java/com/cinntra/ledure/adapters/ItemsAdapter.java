package com.cinntra.ledure.adapters;

 import android.app.Dialog;
import android.content.Context;
 import android.content.Intent;
 import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
 import android.os.Parcelable;
 import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cinntra.ledure.R;
 import com.cinntra.ledure.globals.Globals;
 import com.cinntra.ledure.model.DocumentLines;
 import com.cinntra.ledure.model.TaxItem;
 import com.cinntra.ledure.viewModel.ItemViewModel;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Locale;

 import static android.app.Activity.RESULT_OK;


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private String SelectedTaxSlab = "";
    private int currentItemPo = 0;
    Context context;
    List<DocumentLines> ItemsList;

    public ItemsAdapter(Context context, List<DocumentLines> ItemsList)
       {
    this.context      = context;
    this.ItemsList    = ItemsList;
    tempList.addAll(ItemsList);
      }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View rootView = LayoutInflater.from(context).inflate(R.layout.product_item,parent,false);
    return new ViewHolder(rootView);
     }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
      {
   final DocumentLines obj=getItem(position);
   holder.customerName.setText(obj.getItemName());
   holder.cardNumber.setText(obj.getItemCode());
   holder.unitPrice.setText("Unit Price : "+obj.getUnitPrice());
    }

    @Override
    public int getItemCount()
     {
    return ItemsList.size();
     }
    public DocumentLines getItem(int position)
      {
   return ItemsList.get(position);
      }


    //List<DocumentLines> tempList =null ;
    List<DocumentLines> tempList =new ArrayList<>() ;
    public void filter(String newText)
    {
        String  charText = newText.toLowerCase(Locale.getDefault()).trim();
        ItemsList.clear();
        if (charText.length() == 0) {
            ItemsList.addAll(tempList);
        } else {
            for (DocumentLines st : tempList) {
                /*if(st.getOpportunityName()!=null&&!st.getOpportunityName().isEmpty()) {
                    if (st.getCustomerName().toLowerCase().trim().contains(charText)) {*/
                if(st.getItemCode()!=null&&!st.getItemCode().isEmpty()&&st.getItemName()!=null&&!st.getItemName().isEmpty()) {
                    if (st.getItemCode().toLowerCase().trim().contains(charText)||st.getItemName().toLowerCase().trim().contains(charText)) {
                        ItemsList.add(st);
                    }
                }
            }

        }
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName,cardNumber,unitPrice;
        public ViewHolder(@NonNull View itemView) {
        super(itemView);
        customerName = itemView.findViewById(R.id.customerName);
        cardNumber   = itemView.findViewById(R.id.cardNumber);
        unitPrice    = itemView.findViewById(R.id.unitPrice);

        itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
         currentItemPo = getAdapterPosition();

         DocumentLines itemObj = ItemsList.get(currentItemPo);
         itemObj.setUnitPrice(ItemsList.get(currentItemPo).getUnitPrice());
         itemObj.setUomNo(ItemsList.get(currentItemPo).getUomNo());
         setQuantity(context,itemObj);
          /*
          ItemsList.get(currentItemPo).setItemUnitPrice(ItemsList.get(currentItemPo).getItemPrices().get(CreateContact.PriceListNum-1).getPrice());
          ItemsList.get(currentItemPo).setItemTaxCode(SelectedTaxSlab);
           */
          }
        });
      }
    }

           /*********** Make Custom Views and Data *************/
    private void setQuantity(Context context,DocumentLines itemsObj)
           {
        EditText editText,discount_value,unit_price;
        Button button;
        Dialog dialog = new Dialog(context);
       // LayoutInflater layoutInflater = context.getLayoutInflater();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View custom_dialog =layoutInflater.inflate(R.layout.quantity_alert,null);
        editText       = custom_dialog.findViewById(R.id.editText);
        discount_value = custom_dialog.findViewById(R.id.discount_value);
        button = custom_dialog.findViewById(R.id.button);
        unit_price = custom_dialog.findViewById(R.id.unit_price);
        dialog.setContentView(custom_dialog);
          //dialog.setTitle("");
        dialog.getWindow().setBackgroundDrawable(new
        ColorDrawable(Color.TRANSPARENT));
        dialog.show();
         //editText.getText();
       unit_price.setText(""+itemsObj.getUnitPrice());
       button.setOnClickListener(new View.OnClickListener() {
        @Override
      public void onClick(View v)
            {
      boolean alertStatus = true;
     if(!editText.getText().toString().isEmpty()&&Integer.parseInt(editText.getText().toString())>0)
        {
            if(!discount_value.getText().toString().trim().isEmpty()) {
                if (Globals.SelectedItems.size() > 0) {
                    for (DocumentLines item : Globals.SelectedItems) {
                        if (item.getItemCode().equals(itemsObj.getItemCode())) {
                            int total_qty = Integer.parseInt(editText.getText().toString()) + (int)Double.parseDouble(item.getQuantity());
                            Globals.SelectedItems.get(Globals.SelectedItems.indexOf(item)).setQuantity("" + total_qty);
                            float disPer = Float.parseFloat(discount_value.getText().toString().trim());
                            Globals.SelectedItems.get(Globals.SelectedItems.indexOf(item)).setDiscountPercent(disPer);
                            alertStatus = false;
                            dialog.dismiss();
                            ((AppCompatActivity) context).finish();
                            break;
                        }
                    }
                }
                if (Globals.SelectedItems.size() > 0 && alertStatus) {
                    itemsObj.setQuantity(editText.getText().toString());
                    float dcValue=0;
                    String dc = discount_value.getText().toString().trim();
                    if(!dc.isEmpty())
                        dcValue = Float.valueOf(dc) ;
                    itemsObj.setDiscountPercent(dcValue);
                    dialog.dismiss();
                   Globals.SelectedItems.add(postjson(itemsObj));
                    Intent intent = new Intent();
                    intent.putExtra(Globals.CustomerItemData, (Parcelable) itemsObj);
                    ((AppCompatActivity)context).setResult(RESULT_OK, intent);
                    ((AppCompatActivity)context).finish();

//                    setTaxes(context, itemsObj);
                }

                else if (Globals.SelectedItems.size()==0)
                       {
                    itemsObj.setQuantity(editText.getText().toString());
                    float dcValue=0;
                    String dc = discount_value.getText().toString().trim();
                    if(!dc.isEmpty())
                        dcValue = Float.valueOf(dc) ;
                    itemsObj.setDiscountPercent(dcValue);

                    dialog.dismiss();
                    Globals.SelectedItems.add(postjson(itemsObj));
                    Intent intent = new Intent();
                    intent.putExtra(Globals.CustomerItemData, (Parcelable) itemsObj);
                    ((AppCompatActivity)context).setResult(RESULT_OK, intent);
                    ((AppCompatActivity)context).finish();

           // setTaxes(context, itemsObj);
                  }
            }
            else{
                Toast.makeText(context,"Enter Discount",Toast.LENGTH_SHORT).show();
            }



         }
     else {
      Toast.makeText(context,"Enter valid Quanity",Toast.LENGTH_SHORT).show();
         }
            }
        });

    }
    Dialog TaxListdialog;
    List<TaxItem> taxSlabList = new ArrayList<>();


    private DocumentLines postjson(DocumentLines itemsObj) {
        DocumentLines dc = new DocumentLines();
        dc.setItemCode(itemsObj.getItemCode());
        dc.setQuantity(itemsObj.getQuantity());
        dc.setTaxCode(itemsObj.getTaxCode());//BED+VAT
        dc.setUnitPrice(itemsObj.getUnitPrice());
        dc.setUnitPriceown(itemsObj.getUnitPrice());
        dc.setDiscountPercent(itemsObj.getDiscountPercent());
        dc.setItemDescription(itemsObj.getItemName());
        dc.setDiscountPercent(Float.valueOf("0.0"));
        dc.setFreeText("");
        dc.setUomNo(itemsObj.getUos());
        dc.setUnitWeight("30");

        return dc;
    }



    private void setTaxes(Context context,DocumentLines itemsObj)
        {
    RelativeLayout backPress;
    TextView head_title;
    RecyclerView recyclerview;
    ProgressBar loader;

    TaxListdialog = new Dialog(context);
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    View custom_dialog =layoutInflater.inflate(R.layout.taxes_alert,null);
    recyclerview = custom_dialog.findViewById(R.id.recyclerview);
    backPress    = custom_dialog.findViewById(R.id.back_press);
    head_title   = custom_dialog.findViewById(R.id.head_title);
    loader       = custom_dialog.findViewById(R.id.loader);
    head_title.setText(context.getString(R.string.select_tax));
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
    /************** Call Api **************/

    ItemViewModel model = ViewModelProviders.of((AppCompatActivity)context).get(ItemViewModel.class);
    model.getTaxList(loader).observe((AppCompatActivity)context, new Observer<List<TaxItem>>() {
    @Override
    public void onChanged(@Nullable List<TaxItem> itemsList) {
        taxSlabList.clear();
        taxSlabList.addAll(itemsList);
      TaxItemAdapter adapter = new TaxItemAdapter(context, taxSlabList,itemsObj,TaxListdialog);
      recyclerview.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL,false));
      recyclerview.setAdapter(adapter);

        }
    });

   }

      }
