package com.cinntra.ledure.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.AddContact;
import com.cinntra.ledure.adapters.ContactAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.ContactPerson;
import com.cinntra.ledure.model.ContactPersonData;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class BusinessPartnerContact extends Fragment {

    @BindView(R.id.new_contact)
    FloatingActionButton new_contact;
    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.loader)
    ProgressBar loader;
    @BindView(R.id.no_datafound)
    ImageView no_datafound;
    BusinessPartnerData customerItem;
    ContactAdapter contactAdapter ;
    private ArrayList<ContactPersonData> ContactEmployeesList;
    public BusinessPartnerContact(BusinessPartnerData customerItem) {
        this.customerItem = customerItem;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contact_view,container,false);
        ButterKnife.bind(this,v);
        if(Globals.checkInternet(getContext())) {
            loader.setVisibility(View.VISIBLE);
            callContactEmployeeApi(customerItem.getCardCode());

        }
        new_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs.putString(Globals.AddContactPerson,"BusinessPartner");
                Bundle bundle = new Bundle();
                bundle.putSerializable(Globals.AddContactPerson,customerItem);
                Intent intent = new Intent(getContext(), AddContact.class);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });
       swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    if(Globals.checkInternet(getActivity())){
                        callContactEmployeeApi(customerItem.getCardCode());
                    }
                    else
                        swipeRefreshLayout.setRefreshing(false);

                }
            });


        return v;
    }
    private void callContactEmployeeApi(String id) {
        ContactPersonData contactPersonData = new ContactPersonData();
        contactPersonData.setCardCode(id);

        Call<ContactPerson> call = NewApiClient.getInstance().getApiService(getActivity()).contactemplist(contactPersonData);
        call.enqueue(new Callback<ContactPerson>() {
            @Override
            public void onResponse(Call<ContactPerson> call, Response<ContactPerson> response) {

                if(response.code()==200)
                {
                    loader.setVisibility(View.GONE);
                    if(response.body().getData().size()>0) {
                        no_datafound.setVisibility(View.GONE);
                        ContactEmployeesList = new ArrayList<>();
                        ContactEmployeesList.clear();
                        ContactEmployeesList.addAll(response.body().getData());
                    }else{
                        no_datafound.setVisibility(View.VISIBLE);

                    }
                    if(ContactEmployeesList==null)
                        ContactEmployeesList = new ArrayList<>();
                    contactAdapter =new ContactAdapter(getActivity(),ContactEmployeesList);
                    recyclerView.setAdapter(contactAdapter);
                    if(contactAdapter!=null)
                    contactAdapter.notifyDataSetChanged();

                }
                else
                {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s =response.errorBody().string();
                        mError= gson.fromJson(s,QuotationResponse.class);
                        Toast.makeText(getContext(), mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<ContactPerson> call, Throwable t) {
                loader.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }


}
