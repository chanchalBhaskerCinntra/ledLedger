package com.cinntra.ledure.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.ItemPurchasedParticularCustomerInfo;
import com.cinntra.ledure.activities.ShowCheckInImageActivity;
import com.cinntra.ledure.activities.TripAndExpenseDetailsActivity;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.DataAllTripExpense;
import com.cinntra.ledure.model.ExpenseNewModelResponse;
import com.cinntra.ledure.model.ExpenseResponse;
import com.cinntra.ledure.model.MapData;

import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllTripExpenseAdapter extends RecyclerView.Adapter<AllTripExpenseAdapter.ViewHolder> {
    Context context;
    List<DataAllTripExpense> leadValueList;

    public AllTripExpenseAdapter(Context c, List<DataAllTripExpense> leadValueList) {
        this.context = c;
        this.leadValueList = leadValueList;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_all_trip_expense, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       DataAllTripExpense  lv = leadValueList.get(position);
       // holder.customerName.setText(lv.getBPName());

        holder.tripID.setText("Trip Id: "+lv.getId());

        /********use it for checkIn checkout time**********/
        holder.date.setText("" + lv.getCheckInDate() + " | " + lv.getCheckInTime());


        /********use it for checkIn checkout time**********/


        holder.assignedto.setText("Distance :");
        holder.assigned.setText(""+lv.getTotalDistanceAuto()+ "KM");
        holder.option.setVisibility(View.GONE);
        Log.e("TAG>>>>", "onBindViewHolder: " + Globals.attachmentBaseUrl + lv.getCheckInAttach());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(context, TripAndExpenseDetailsActivity.class);
                Log.e("TAG", "onClick: "+lv.getId());
                intent.putExtra("id",lv.getId());
                context.startActivity(intent);
            }
        });





        if(!lv.getCheckOutDate().isEmpty()){
            holder.tvCheckInStatus.setBackgroundResource(R.drawable.background_green_rounded);

            holder.tvCheckInStatus.setText("Completed");
            holder.customerName.setText("" + lv.getCheckInDate());
        }else{
            holder.tvCheckInStatus.setBackgroundResource(R.drawable.background_red_rounded);
            holder.customerName.setText("" + lv.getCheckInDate() + " - " + lv.getCheckOutDate());
            holder.tvCheckInStatus.setText("In Progress");
        }


//        if (lv.getCheckInAttach().isEmpty()){
//            holder.linearcheckInImage.setVisibility(View.GONE);
//        }else {
//            holder.linearcheckInImage.setVisibility(View.VISIBLE);
//        }
//
//        if (lv.getCheckOutAttach().isEmpty()){
//            holder.linearCheckOutImage.setVisibility(View.GONE);
//        }
//        else {
//            holder.linearCheckOutImage.setVisibility(View.VISIBLE);
//        }

//        Picasso.get()
//                .load(Globals.attachmentBaseUrl + lv.getCheckInAttach())
//                .resize(70, 70).centerCrop().onlyScaleDown()
//                .into(new Target() {
//
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
//                        byte[] byteArray = stream.toByteArray();
//
//                        // Create a new bitmap from the compressed byte array
//                        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//
//                        // Display the compressed bitmap
//                        holder.ivCheckInImage.setImageBitmap(compressedBitmap);
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                    }
//                });
          Glide.with(holder.ivCheckInImage.getContext()).load(Globals.attachmentBaseUrl+lv.getCheckInAttach()).placeholder(R.drawable.nodatafound).into(holder.ivCheckInImage);
        Glide.with(holder.ivCheckOutImage.getContext()).load(Globals.attachmentBaseUrl+lv.getCheckOutAttach()).dontAnimate().fitCenter().into(holder.ivCheckOutImage);



        if (lv.getCheckOutAttach().isEmpty()){
            holder.linearCheckOutImage.setVisibility(View.GONE);
        }else {
            holder.linearCheckOutImage.setVisibility(View.VISIBLE);
        }




        holder.linearcheckInImage.setOnClickListener(view -> {
            Intent intent=new Intent(context,ShowCheckInImageActivity.class);
            intent.putExtra("image",lv.getCheckInAttach());
            intent.putExtra("name","Check-IN Image");
            context.startActivity(intent);
        });


        holder.linearCheckOutImage.setOnClickListener(view -> {
            Intent intent=new Intent(context,ShowCheckInImageActivity.class);
            intent.putExtra("image",lv.getCheckOutAttach());
            intent.putExtra("name","Check-Out Image");
            context.startActivity(intent);
        });

//        if(lv.getType().equalsIgnoreCase("Start")){
//            holder.follow_up.setBackgroundResource(R.drawable.background_green_rounded);
//
//            holder.follow_up.setText("Check In");
//        }else{
//            holder.follow_up.setBackgroundResource(R.drawable.background_red_rounded);
//            holder.follow_up.setText("Check Out");
//        }

        //  holder.follow_up.setVisibility(View.VISIBLE);

     /*   holder.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConfirmDialog(lv.getId());
            }
        });*/
        // holder.amount.setText("Rs:" + lv.getTurnover());

    }


    private void openConfirmDialog(Integer id) {

        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You Want to Delete!")
                .setConfirmText("Yes,Delete!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        deleteapi(id);

                    }
                })

                .show();

    }

    private void deleteapi(Integer id) {
        List<String> ls = new ArrayList<>();
        ls.add(id.toString());
        HashMap<String, List<String>> hd = new HashMap<>();
        hd.put("id", ls);
        Call<ExpenseNewModelResponse> call = NewApiClient.getInstance().getApiService(context).deleteexpense(hd);
        call.enqueue(new Callback<ExpenseNewModelResponse>() {
            @Override
            public void onResponse(Call<ExpenseNewModelResponse> call, Response<ExpenseNewModelResponse> response) {

                if (response.code() == 200) {
                    if (response.body().getMessage().equalsIgnoreCase("successful")) {

                        Toasty.success(context, "Deleted Successfully", Toast.LENGTH_LONG).show();
                    } else {
                        Toasty.warning(context, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    ExpenseResponse mError = new ExpenseResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, ExpenseResponse.class);
                        Toast.makeText(context, mError.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                }
            }

            @Override
            public void onFailure(Call<ExpenseNewModelResponse> call, Throwable t) {

                Toasty.error(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return leadValueList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView customerName, cardNumber, date, assigned, assignedto, headDate,tvshowImage;
        TextView follow_up,tripID,tvCheckInStatus;
        LinearLayout assigned_view;
        LinearLayoutCompat option;
        LinearLayout linearSeeImage,linearcheckInImage,linearCheckOutImage;
        HorizontalScrollView horizontalScrollView;
        ImageView ivCheckInImage, ivCheckOutImage;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.item_title);
            cardNumber = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.item_date);
            assigned = itemView.findViewById(R.id.assigned);
            assignedto = itemView.findViewById(R.id.assignedto);
            assigned_view = itemView.findViewById(R.id.assigned_view);
            option = itemView.findViewById(R.id.option);
            follow_up = itemView.findViewById(R.id.follow_up);
            linearSeeImage = itemView.findViewById(R.id.linearSeeImage);
            linearcheckInImage = itemView.findViewById(R.id.linearcheckInImage);
            linearCheckOutImage = itemView.findViewById(R.id.linearcheckOutImage);
            ivCheckInImage = itemView.findViewById(R.id.ivCheckINImage);
            ivCheckOutImage = itemView.findViewById(R.id.ivCheckOutImage);
            tvshowImage = itemView.findViewById(R.id.tvshowImage);
            horizontalScrollView = itemView.findViewById(R.id.itemHorizontalScrollView);
            tvCheckInStatus=itemView.findViewById(R.id.tvStatusOfCheckIn);
            tripID=itemView.findViewById(R.id.tvTripId);
            cardView=itemView.findViewById(R.id.cardexpense);

            linearSeeImage.setOnClickListener(view -> {
                if (horizontalScrollView.getVisibility() == View.GONE) {
                   horizontalScrollView.setVisibility(View
                            .VISIBLE);
                   tvshowImage.setText("Hide Image");

                    // binding.ivArrow.setImageResource(R.drawable.ic_arrow_drop_up_24);
                } else {
                  horizontalScrollView.setVisibility(View
                            .GONE);
                  tvshowImage.setText("See Image");
                    //  holder.horizontalScrollView.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
                }

            });



//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent =new Intent(context, TripAndExpenseDetailsActivity.class);
//                    Log.e("TAG", "onClick: "+lv.getId() );
//                    intent.putExtra("id",lv.getId());
//                    context.startActivity(intent);
//
////                    Bundle b = new Bundle();
////                    b.putSerializable(Globals.ExpenseData, leadValueList.get(getAdapterPosition()));
////                    ExpenseDetailFragment fragment = new ExpenseDetailFragment();
////                    fragment.setArguments(b);
////                    FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
////                    transaction.add(R.id.customer_lead, fragment);
////                    transaction.addToBackStack("Back");
////                    transaction.commit();
//
//                }
//            });



        }

    }
}