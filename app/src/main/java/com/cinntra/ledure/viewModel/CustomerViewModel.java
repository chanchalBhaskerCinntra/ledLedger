package com.cinntra.ledure.viewModel;

import android.view.View;
import android.widget.ProgressBar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.CustomerBusinessRes;
import com.cinntra.ledure.webservices.NewApiClient;
import com.pixplicity.easyprefs.library.Prefs;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerViewModel extends ViewModel
         {
    MutableLiveData<List<BusinessPartnerData>> businessPartners;
    public LiveData<List<BusinessPartnerData>> getCustomersList(ProgressBar loader)
         {
             //if the list is null
        // if (businessPartners == null)
         {
         businessPartners = new MutableLiveData<List<BusinessPartnerData>>();
                 //we will load it asynchronously from server in this method
             loadCustomers(loader);
             }

             //finally we will return the list
             return businessPartners;
         }

    private void loadCustomers(ProgressBar loader)
          {

              HashMap<String,String> hde = new HashMap<>();
              hde.put("SalesPersonCode", Prefs.getString(Globals.SalesEmployeeCode,""));

        /*loader.setVisibility(View.VISIBLE);
        String url = Globals.GetCustomers+" &$skip="+Globals.SkipItem(pageNo);*/
        Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService().getAllBusinessPartnerpagination(hde);
        call.enqueue(new Callback<CustomerBusinessRes>() {
        @Override
        public void onResponse(Call<CustomerBusinessRes> call, Response<CustomerBusinessRes> response) {
            if(response.code()==200){
                loader.setVisibility(View.GONE);
                if (response.body().getData() != null )
                    businessPartners.setValue(response.body().getData());
            }
        }
         @Override
         public void onFailure(Call<CustomerBusinessRes> call, Throwable t) {
         loader.setVisibility(View.GONE);
             }
          });
         }




             MutableLiveData<List<BusinessPartnerData>> businessPartnersZone;
             public LiveData<List<BusinessPartnerData>> getCustomersListZone(ProgressBar loader)
             {
                 //if the list is null
                 // if (businessPartners == null)
                 {
                     businessPartnersZone = new MutableLiveData<List<BusinessPartnerData>>();
                     //we will load it asynchronously from server in this method
                     loadCustomersZone(loader);
                 }

                 //finally we will return the list
                 return businessPartnersZone;
             }

             private void loadCustomersZone(ProgressBar loader)
             {

                 HashMap<String,String> hde = new HashMap<>();
                 hde.put("bpZone", Prefs.getString(Globals.ZONE,""));
        /*loader.setVisibility(View.VISIBLE);
        String url = Globals.GetCustomers+" &$skip="+Globals.SkipItem(pageNo);*/
                 Call<CustomerBusinessRes> call = NewApiClient.getInstance().getApiService().getAllBpFilterZoneWise(hde);
                 call.enqueue(new Callback<CustomerBusinessRes>() {
                     @Override
                     public void onResponse(Call<CustomerBusinessRes> call, Response<CustomerBusinessRes> response) {
                         if(response.code()==200){
                             loader.setVisibility(View.GONE);
                             if (response.body().getData() != null )
                                 businessPartnersZone.setValue(response.body().getData());
                         }
                     }
                     @Override
                     public void onFailure(Call<CustomerBusinessRes> call, Throwable t) {
                         loader.setVisibility(View.GONE);
                     }
                 });
             }





     }
