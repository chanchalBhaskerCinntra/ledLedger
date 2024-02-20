package com.cinntra.ledure.webservices;


import com.cinntra.ledure.model.*;
import com.cinntra.ledure.newapimodel.AddOpportunityModel;
import com.cinntra.ledure.newapimodel.LeadResponse;
import com.cinntra.ledure.newapimodel.LeadValue;
import com.cinntra.ledure.newapimodel.OpportunityValue;
import com.cinntra.ledure.newapimodel.ResponseItemFilterDashboard;
import com.cinntra.ledure.newapimodel.ResponseParticularCustomerPaymentDue;
import com.cinntra.ledure.newapimodel.ResponsePayMentDueCounter;
import com.cinntra.ledure.newapimodel.ResponsePaymentDueDashboardCustomerList;
import com.cinntra.ledure.newapimodel.ResponseReceivableGraph;
import com.google.gson.JsonObject;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiServices {
    @POST("b1s/v1/Login")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LogInResponse> LogIn(@Body LogInRequest data);

    @POST("api/register/")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<String> Register(@Body String data);


    @GET("b1s/v1/Quotations?$select=DocNum,CardCode,DocEntry,DocDate&$orderby=DocEntry desc")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> QuotationList_Decending();

    @GET("b1s/v1/Quotations?$orderby=DocEntry desc")
    Call<QuotationResponse> quotationList();

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> QuotationList(@Url String url);

    //@GET("b1s/v1/Quotations?$filter=DocumentStatus eq 'bost_Open'&$orderby=DocEntry desc &$top={top}")
    @POST("quotation/all_filter")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> OpenQuotationList(@Body AddQuotation opportunityValue);

    /*   @GET("b1s/v1/SalesOpportunities/")
       @Headers({ "Content-Type: application/json;charset=UTF-8"})
       Call<OpportunitiesResponse> OpportunitiesList();*/
    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<OpportunitiesResponse> OpportunitiesList(@Url String url);

    @GET("b1s/v1/SalesOpportunities?$filter=Status eq 'sos_Open'&$orderby=SequentialNo desc")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<OpportunitiesResponse> OpportunitiesOpenList();

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<OpportunitiesResponse> OpportunitiesOpenList(@Url String url);

    @GET("b1s/v1/SalesOpportunities?$filter=Status eq 'sos_Sold'&$orderby=SequentialNo desc")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<OpportunitiesResponse> OpportunitiesWonList();

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<OpportunitiesResponse> OpportunitiesWonList(@Url String url);

    @GET("b1s/v1/SalesOpportunities?$filter=Status eq 'sos_Missed'&$orderby=SequentialNo desc")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<OpportunitiesResponse> OpportunitiesLostList();

    @POST("item/sold_items_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseItemDashboard> getItemOnDashboard(@Body HashMap<String, String> obj);


    @POST("item/sold_items_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseItemInSalesCard> getItemInSaleCard(@Body HashMap<String, String> obj);

    @POST("item/filter_item_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseItemFilterDashboard> getFilterGroupItemStock(@Body HashMap<String, String> obj);

    @POST("item/filter_bpgroup_item")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseItemFilterDashboard> getFilterGroupItemStockBpWise(@Body HashMap<String, String> obj);

    @POST("item/filter_item_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseItemFilterDashboard> getFilterItemsZone(@Body HashMap<String, String> obj);


    @POST("item/sub_category_items_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResSubCatItems> sub_category_items_dashboard(@Body HashMap<String, String> obj);

    @POST("item/filter_bpsubgroup_item")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResSubCatItems> soldItemSubGroupApi(@Body HashMap<String, String> obj);

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<OpportunitiesResponse> OpportunitiesLostList(@Url String url);

    //@GET("b1s/v1/Invoices?$filter=DocDueDate lt '2021-02-05'")
    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<InvoiceResponse> InvoicesOverDueList(@Url String url);

    @GET("b1s/v1/Invoices/")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> InvoicesList();

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<InvoiceResponse> InvoicesList(@Url String url);

    @POST("order/all_filter")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> OrdersList(@Body AddQuotation opportunityValue);

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> OrdersList(@Url String url);

    @GET("b1s/v1/Orders?$filter=DocumentStatus eq 'bost_Open'&$orderby=DocEntry desc")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> OrdersOpenList();

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> OrdersOpenList(@Url String url);

    @GET("b1s/v1/DeliveryNotes?$filter=DocumentStatus eq 'bost_Open'")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> deliveryOpenList();

    @GET("b1s/v1/DeliveryNotes?$filter=DocumentStatus eq 'C' and Cancelled eq 'tNO'")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> deliveryCloseList();

    // @GET("b1s/v1/DeliveryNotes?$filter=DocDueDate lt '2021-02-05'")
    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> deliveryOverDueList(@Url String url);

    @GET("b1s/v1/DeliveryNotes/")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> deliveryList();

    @GET("b1s/v1/InventoryGenEntries/")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<InvoiceResponse> inventoryList();


    @POST("item/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ItemResponse> ItemsList(@Body ItemCategoryData url);

    @GET("b1s/v1/SalesTaxCodes?$select=Code")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<TaxItemResponse> taxcodes();

    /*******************  Add New APIs  *********************/
    @POST("b1s/v1/Quotations")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> NewQuotation(@Body NewQuotation in);

    @POST("order/create")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> addOrder(@Body AddQuotation in);

    @POST("quotation/create")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> addQuotation(@Body AddQuotation in);

    @POST("b1s/v1/SalesOpportunities")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> addOpportunity(@Body AddOpportunity in);

    /*******************  Update APIs  *********************/

    //@PUT("b1s/v1/Quotations({id})")
    @POST("quotation/update")
    Call<QuotationResponse> updateQuotation(@Body UpdateQuotationModel model);


    @PATCH("b1s/v1/SalesOpportunities({id})")
    Call<QuotationResponse> updateOpportunity(@Path("id") String id, @Body AddOpportunity model);

    @PATCH("b1s/v1/SalesOpportunities({id})")
    Call<QuotationResponse> updateFavorite(@Path("id") String id, @Body UpdateFavourites model);

    @POST("opportunity/fav")
    Call<NewOppResponse> updateoppFavorite(@Body UpdateFavourites model);

    @POST("order/update")
    Call<QuotationResponse> updateOrder(@Body UpdateQuotationModel model);

    @PATCH("b1s/v1/BusinessPartners('{id}')")
    Call<QuotationResponse> AddContact(@Path("id") String id, @Body ContactExtension model);

    @POST("b1s/v1/BusinessPartners")
    Call<QuotationResponse> AddBP(@Body PostBP model);

    @POST("opportunity/update")
    Call<NewOppResponse> newUpdateOpportunity(@Body AddOpportunityModel model);

    @PATCH("b1s/v1/BusinessPartners('{id}')")
    Call<QuotationResponse> AddBP(@Path("id") String id, @Body PostBP model);


    /*  @GET("b1s/v1/SalesPersons/")
      @Headers({ "Content-Type: application/json;charset=UTF-8"})
      Call<SaleEmployeeResponse> getSalesEmplyeeList();*/
   /* @GET("employee/all")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<SaleEmployeeResponse> getSalesEmplyeeList();*/
    @POST("employee/all_filter")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<SaleEmployeeResponse> getSalesEmplyeeList(@Body EmployeeValue employeeValue);

    @GET("employee/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<SaleEmployeeResponse> getAllEmployee();

    @POST("businesspartner/employee/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ContactPerson> ContactEmployeesList(@Body ContactPersonData businessPartnerData);

    @GET("b1s/v1/Countries/")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<AdressDetail> getCountryName();

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<AdressDetail> getCountryName(@Url String url);

    @GET("b1s/v1/States/")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<StateDetail> getStateName();

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<StateDetail> getStateName(@Url String url);

    /*@GET("b1s/v1/Departments/")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<DepartMentDetail> getDepartMent();*/

    @GET("businesspartner/department/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<DepartMentDetail> getDepartMent();

    /*@GET("b1s/v1/EmployeePosition/")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<RoleListDetail> getRole();*/

    @GET("businesspartner/position/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<RoleListDetail> getRole();

    @GET("XSJS/SalesDashBoard.xsjs?User=E02558&DBName=JCSPL&Position=Level1")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<String> getNewData();


    /******************** Counter Apis****************************/

    @GET("b1s/v1/EmployeesInfo?$select=EmployeeID,FirstName,MiddleName,LastName")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<OwnerResponse> Employees_Owener_List();

    @GET("b1s/v1/Quotations?$apply=aggregate($count as Count)")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CounterResponse> QuotationCount();

    @GET("b1s/v1/SalesOpportunities?$apply=aggregate($count as Count)")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CounterResponse> OpportunityCount();

    @GET("b1s/v1/Orders?$apply=aggregate($count as Count)")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CounterResponse> OrdersCount();


    @POST("employee/invoice_counter")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CounterResponse> InvoicesCount(@Body SalesEmployeeItem salesEmployeeItem);

    @GET("b1s/v1/BusinessPartners?$apply=aggregate($count as Count)")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CounterResponse> CustomerCount();

    /************************** one Time/Same Apis  *****************************/

    @GET("b1s/v1/SalesStages/")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<StagesResponse> getStagesList();

    @GET("industries/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<IndustryResponse> getIndustryList();

    /************************** Profile  Apis  *****************************/
    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<UserIDResponse> getUserID(@Url String url);

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<UserResponse> getUserProfile(@Url String url);

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<UserProfile> getUserProfileDetail(@Url String url);


    /******************************* Hana Apis **********************************/

    @GET("SalesApp_Cinntra_Test/Opportunity/GetOpportunity.xsjs?DBName=TEST")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<String> TestApi();

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<InventoryResponse> getAllInventories(@Url String url);


    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<DeliveryResponse> getOpenDeliveries(@Url String url);

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<DeliveryResponse> getCloseDeliveries(@Url String url);

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<DeliveryResponse> getOverDueDeliveries(@Url String url);

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<SalesTargetResponse> getSalesTarget(@Url String url);

    @POST("employee/top5itembyamount")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<Top5ItemResponse> getTop5Items(@Body HashMap<String, String> hd);

    @POST("item/item_overview")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseItemOverView> getItemOverView(@Body HashMap<String, String> hd);

    @POST("item/item_invoices")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseItemInvoices> getItemInvoices(@Body HashMap<String, String> hd);


    @POST("employee/top5bp")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<Top5CustomerResponse> getTop5Customer(@Body HashMap<String, String> hd);
  /*  @GET
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<HeirarchiResponse> getHeirarchi(@Url String url);*/


    @POST("employee/all_filter")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<HeirarchiResponse> getAllEmployeelist(@Body EmployeeValue employeeValue);

    @POST("businesspartner/branch/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<BranchResponse> getBranch(@Body Branch branch);

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<UserCounterResponse> getUserCounter(@Url String url);

    @GET
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<UserLoginCredential> getLogIn(@Url String url);

    /*    @GET("b1s/v1/PaymentTermsTypes/")
        @Headers({"Content-Type: application/json;charset=UTF-8"})
        Call<PayMentTermsDetail> getPaymentTerm();*/
    @GET("paymenttermstypes/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<PayMentTermsDetail> getPaymentTerm();


    @POST("quotation/fav")
    Call<QuotationResponse> updateFavQuotation(@Body QuotationItem model);


    @POST("stage/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<OpportunityStageResponse> getAllStages(@Body OpportunityItem model);

    @POST("stage/create")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<OpportunityStageResponse> createStages(@Body StagesValue model);


    @POST("demo/create")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<DemoResponse> createDemo(@Body DemoValue model);

    /*@GET("opportunity/all")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<NewOppResponse> allopportinitylist();*/

    @POST("opportunity/all_filter")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<NewOppResponse> allopportinitylist(@Body OpportunityValue opportunityValue);


    @POST("opportunity/one")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<NewOppResponse> getparticularopportunity(@Body OpportunityValue opportunityValue);

    @POST("activity/chatter_all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ChatResponse> getAllChat(@Body StagesValue opportunityValue);

    @POST("activity/chatter")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ChatResponse> createChat(@Body ChatModel opportunityValue);

    @POST("opportunity/create")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<NewOppResponse> createopportunity(@Body AddOpportunityModel opportunityValue);


    @POST("lead/all_filter")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LeadResponse> getAllLead(@Body LeadFilter leadValue);

    @POST("lead/update_lat_long")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LeadResponse> updateLeadGeoLocation(@Body HashMap<String, String> geoLoaction);

    @POST("businesspartner/update_lat_long")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LeadResponse> updateBusinessPartnerGeoLocation(@Body HashMap<String, String> geoLoaction);

    @POST("activity/create")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<EventResponse> createnewevent(@Body EventValue eventValue);

    @POST("activity/followup")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<FollowUpResponse> createfollowUP(@Body FollowUpData eventValue);


    @POST("activity/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<EventResponse> getallevent(@Body EventValue eventValue);


    @POST("activity/delete")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<EventResponse> deleteEvent(@Body EventValue eventValue);


    @POST("activity/one")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<EventResponse> particularevent(@Body EventValue eventValue);


    @POST("activity/update")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<EventResponse> updateevent(@Body EventValue eventValue);


    @POST("opportunity/change_stage")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<OpportunityStageResponse> updatestage(@Body StagesValue stval);


    @POST("opportunity/complete")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<OpportunityStageResponse> completestage(@Body CompleteStageResponse stval);


    @POST("employee/login")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<NewLogINResponse> loginEmployee(@Body LogInDetail logInDetail);


    @POST("stage/stage_detail")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<OpportunityStageResponse> getStagesComment(@Body StagesValue oppitem);


    @POST("lead/one")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LeadResponse> particularlead(@Body LeadValue leadValue);

    @GET("businesspartner/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerBusinessRes> getAllBusinessPartner();

    @POST("businesspartner/all_data")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerBusinessRes> getAllBusinessPartnerpagination(@Body HashMap<String, String> hde);

    @POST("businesspartner/all_data_pagination")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerBusinessRes> getAllBusinessPartnerWithPagination(@Body HashMap<String, String> hde);

    @POST("businesspartner/all_bp_filter")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerBusinessRes> getAllBpFilterZoneWise(@Body HashMap<String, String> hde);

    @POST("businesspartner/create")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerBusinessRes> addnewCustomer(@Body AddBusinessPartnerData in);

    @POST("businesspartner/update")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerBusinessRes> updatecustomer(@Body AddBusinessPartnerData businessPartnerData);

    @POST("businesspartner/one")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerBusinessRes> particularcustomerdetails(@Body BusinessPartnerData businessPartnerData);

    @POST("businesspartner/employee/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ContactPerson> contactemplist(@Body ContactPersonData contactPersonData);


    @POST("businesspartner/employee/create")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> createcontact(@Body CreateContactData contactData);


    @POST("businesspartner/branch/create")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<BranchResponse> addBranch(@Body Branch branch);


    @GET("countries/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CountryResponse> getCountryList();


    @POST("states/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<StateRespose> getStateList(@Body StateData stateData);


    @POST("businesspartner/branch/update")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<BranchResponse> updateBranch(@Body Branch branch);


    @POST("employee/dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CounterResponse> dashboardcounter(@Body SalesEmployeeItem salesEmployeeItem);   //all_incoming_payments

    @POST("invoice/all_incoming_payments")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CounterResponse> all_incoming_payments(@Body HashMap<String, String> pay);


    @POST("employee/analytics")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CounterResponse> projectiondata(@Body SalesEmployeeItem salesEmployeeItem);


    @POST("activity/maps")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<MapResponse> sendMaplatlong(@Body MapData mapData);


    @Multipart
    @POST("activity/maps")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<MapResponse> sendMaplatLongMultipart(
            @Part MultipartBody.Part ExpenseAttach,
            @Part("Lat") RequestBody Lat,
            @Part("Long") RequestBody Long,
            @Part("Emp_Id") RequestBody Emp_Id,
            @Part("Emp_Name") RequestBody Emp_Name,
            @Part("UpdateTime") RequestBody UpdateTime,
            @Part("Address") RequestBody Address,
            @Part("type") RequestBody type,
            @Part("shape") RequestBody shape,
            @Part("remark") RequestBody remark,
            @Part("ResourceId") RequestBody ResourceId,
            @Part("SourceType") RequestBody SourceType,
            @Part("ContactPerson") RequestBody ContactPerson,
            @Part("ExpenseCost") RequestBody ExpenseCost,
            @Part("ExpenseDistance") RequestBody ExpenseDistance,
            @Part("ExpenseType") RequestBody ExpenseType,
            @Part("ExpenseRemark") RequestBody ExpenseRemark

    );

    @POST("notification/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<NotificationResponse> allnotification(@Body SalesEmployeeItem salesEmployeeItem);

    @POST("activity/all_filter")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<EventResponse> getcalendardata(@Body SalesEmployeeItem eventValue);


    @POST("businesspartner/top5Activity")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<EventResponse> getrecentactivity(@Body SalesEmployeeItem eventValue);

    @POST("businesspartner/ledger_dashboard_count")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<DashboardCounterResponse> getDashBoardCounterForLedger(@Body HashMap<String, String> salesEmployeeItem);

    @POST("businesspartner/due_payment_dashboard_count")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponsePayMentDueCounter> getPaymentDueCounter(@Body JsonObject salesEmployeeItem);


    @POST("businesspartner/due_payment_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponsePaymentDueDashboardCustomerList> getPaymentDueDashboardCustomerList(@Body HashMap<String, String> salesEmployeeItem);

    @POST("businesspartner/bp_due_payment")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseParticularCustomerPaymentDue> getPaymentDueDashboardParticularCustomer(@Body HashMap<String, String> salesEmployeeItem);


    @POST("order/delivery")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseDeliveryOrder> orderlist(@Body HashMap<String, String> salesEmployeeItem);


    @POST("activity/status")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<EventResponse> completeEvent(@Body EventValue eventValue);

    @POST("lead/create")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LeadResponse> createLead(@Body ArrayList<CreateLead> createLeads);
/*
    @POST("lead/chatter_all")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<ChatResponse> getAllLeadChat(@Body LeadChatModel opportunityValue);*/


    @POST("activity/chatter_all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ChatResponse> getAllLeadChat(@Body FollowUpData opportunityValue);

    @POST("deliverynote/pending_byorder")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseDeliveryNotePendingByOrder> getDeliveryNotePendingByOrder(@Body HashMap<String, String> data);


    @POST("purchaseorders/pending_items_by_purchase_order")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseDeliveryNotePendingByOrder> getDeliveryNotePendingByOrderPurchase(@Body HashMap<String, String> data);


    @POST("lead/chatter")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ChatResponse> createleadChat(@Body ChatModel chatModel);


    @GET("invoice/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<InvoiceResponse> getallinvoice();

    @POST("invoice/one")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<InvoiceResponse> invoice_One(@Body HashMap<String, String> logInDetail);

    @POST("purchaseinvoices/one")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<InvoiceResponse> purchase_invoice_One(@Body HashMap<String, String> logInDetail);


    @POST("purchaseinvoices/ap_credit_notes_one")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CreditNoteInvoiceResponse> purchase_credit_One(@Body HashMap<String, String> logInDetail);

    @POST("invoice/credit_notes_one")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CreditNoteInvoiceResponse> invoice_credit_One(@Body HashMap<String, String> logInDetail);

    @POST("order/one")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResPonseOrderOne> order_one(@Body HashMap<String, String> logInDetail);

    @POST("purchaseorders/one")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResPonseOrderOne> order_one_purchase(@Body HashMap<String, String> logInDetail);

    @POST("employee/one")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<EmployeeProfile> getProfileDetail(@Body EmpDetails empDetails);


    @GET("lead/type_all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LeadTypeResponse> getLeadType();

    @GET("lead/source_all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LeadTypeResponse> getsourceType();

    @POST("notification/read")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<NotificationResponse> readnotification(@Body NotificationData nd);


    @POST("lead/update")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LeadResponse> updateLead(@Body CreateLead lv);


    @GET("item/category_all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ItemCategoryResponse> getAllCategory();


    @POST("employee/create")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LeadResponse> createdemoEmployee(@Body NewEmployeeUser newEmployeeUser);


    @GET("campset/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CampaignResponse> getAllCampaign();


    @POST("campset/create")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CampaignResponse> createCampaign(@Body AddCampaignModel campaignModel);


    @POST("campset/one")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CampaignResponse> getCampsetDetails(@Body CampaignModel cm);

    @POST("camp/filter_campaign")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CampaignListModel> getmemberlist(@Body CampaignListResponse cm);

    @GET("businesspartner/alltype")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<BPTypeResponse> getBptypelist();

    @GET("opportunity/alltype")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<BPTypeResponse> getopptypelist();

    @POST("login/")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<NewLogINResponse> sessionlogin(@Body HashMap<String, String> logInDetail);

    @GET("businesspartner/ledger_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerBusinessRes> getledgerlist();

    @POST("businesspartner/ledger_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerBusinessRes> getledgerlistPost(@Body HashMap<String, String> pay);


    @POST("businesspartner/receipt_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ReceiptCustomerBusinessRes> receipt_dashboard(@Body HashMap<String, String> pay);

    @POST("purchaseinvoices/purchase_receipt_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ReceiptCustomerBusinessRes> receipt_dashboard_purchase(@Body HashMap<String, String> pay);


    @GET("businesspartner/receivable_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerBusinessRes> receivable_dashboard(@Body HashMap<String, String> pay);

    @POST("businesspartner/receivable_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ReceivableResponse> receivable_dashboard_post(@Body HashMap<String, String> pay);


    @POST("businesspartner/bp_ledger")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LedgerCustomerResponse> getparticularledgerdetails(@Body HashMap<String, String> logInDetail);

    @POST("businesspartner/bp_purchase_invoices")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LedgerCustomerResponse> getBppurchasedetails(@Body HashMap<String, String> logInDetail);


    @POST("item/bp_item_invoices")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseItemParticularCustomerInfo> getItemParticularBpLedger(@Body HashMap<String, String> logInDetail);

    @GET("expense/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ExpenseResponse> getAllExpense();


    @GET("expense/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ExpenseNewModelResponse> getAllNewExpense();


    @POST("businesspartner/bp_receivable")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LedgerCustomerResponse> bp_receivable(@Body HashMap<String, String> logInDetail);

    @POST("businesspartner/bp_receipt")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LedgerCustomerResponse> bp_receipt(@Body HashMap<String, String> logInDetail);

    @POST("purchaseinvoices/bp_purchase_receipt")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LedgerCustomerResponse> bp_receipt_purchase(@Body HashMap<String, String> logInDetail);

    @POST("expense/create")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ExpenseResponse> expense_create(@Body ExpenseDataModel logInDetail);


    @POST("invoice/payment_collection_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CounterResponse> colection_invoicewise(@Body HashMap<String, String> pay);


    @POST("invoice/credit_note_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerBusinessRes> credit_note_dashboard(@Body HashMap<String, String> pay);

    @POST(" invoice/bp_credit_note")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LedgerCustomerResponse> bp_credit_note(@Body HashMap<String, String> logInDetail);

    @POST("purchaseinvoices/bp_ap_credit_notes")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LedgerCustomerResponse> purchase_bp_credit_note(@Body HashMap<String, String> logInDetail);

    @POST("invoice/payment_collection_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerBusinessRes> payment_collection_invoiceBases(@Body HashMap<String, String> pay);

    @POST("businesspartner/updatebpcreditlimitbybp")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LedgerCustomerResponse> updatebpcreditlimitbybp(@Body HashMap<String, String> logInDetail);


    @POST("order/top5order")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<QuotationResponse> top5order(@Body HashMap<String, String> obj);

    @POST("item/all_filter_pagination")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ItemResponse> itemAllFilter(@Body BodyItemList obj);


    @POST("invoice/pending_payment_collection")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CashDiscountResponse> cashDiscountList(@Body HashMap<String, String> obj);

    @POST("deliverynote/pending")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<PendingOrderResponse> getPendingOrder(@Body HashMap<String, String> obj);


    @POST("purchaseorders/pending_purchase_order_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<PendingOrderResponse> getPendingOrderPurchase(@Body HashMap<String, String> obj);

    @POST("deliverynote/pending_orderwise")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<PendingOrderSubResponse> getPendingSubOrder(@Body HashMap<String, String> obj);

    @POST("purchaseorders/bp_pending_purchase_order")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<PendingOrderSubResponse> getPendingSubOrderPurchase(@Body HashMap<String, String> obj);

    @GET("invoice/pending_payment_collection")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<String> cashDiscountListTest();


    @POST("businesspartner/bp_overview")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerLedgerResponse> BPLedgerDetails(@Body HashMap<String, String> obj);

    @POST("order/bp_wise_sold_items")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerItemResponse> bp_wise_sold_items_order(@Body HashMap<String, String> obj);

    @POST("invoice/bp_wise_sold_items")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerItemResponse> bp_wise_sold_items(@Body HashMap<String, String> obj);

    @POST("item/filter_bpitem")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerItemResponse> bpWiseSoldFilterItems(@Body HashMap<String, String> obj);

    @POST("businesspartner/bp_debit_credit")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LedgerResponse> bp_debit_credit(@Body HashMap<String, String> obj);


    @POST("journalentries/bp_wise")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseJournalEntryBpWise> bp_general_entries(@Body HashMap<String, String> obj);


    @Multipart
    @POST("tripexpense/trip_checkin")
    Call<ResponseTripCheckIn> tripCheckIn(
            @Part MultipartBody.Part CheckInAttach,
            @Part("BPType") RequestBody bpType,
            @Part("BPName") RequestBody bpName,
            @Part("CardCode") RequestBody cardCode,
            @Part("SalesPersonCode") RequestBody SalesPersonCode,
            @Part("ModeOfTransport") RequestBody ModeOfTransport,
            @Part("CheckInDate") RequestBody CheckInDate,
            @Part("CheckInTime") RequestBody CheckInTime,
            @Part("CheckInLat") RequestBody CheckInLat,
            @Part("CheckInLong") RequestBody CheckInLong,
            @Part("CheckInRemarks") RequestBody CheckInRemarks
    );

    @Multipart
    @POST("attachment/create")
    Call<AttachmentModel> uploadProfileAttachment(
            @Part MultipartBody.Part File,
            @Part("id") RequestBody id,
//            @Part("File") RequestBody File,
            @Part("LinkType") RequestBody LinkType,
            @Part("LinkID") RequestBody LinkID,
            @Part("Caption") RequestBody Caption,
            @Part("CreateDate") RequestBody CreateDate,
            @Part("CreateTime") RequestBody CreateTime,
            @Part("UpdateDate") RequestBody UpdateDate,
            @Part("UpdateTime") RequestBody UpdateTime
    );

    @POST("attachment/all")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<AttachmentModel> getAllAttachment(@Body JsonObject jsonObject);


    @Multipart
    @POST("tripexpense/trip_checkout")
    Call<ResponseTripCheckOut> tripCheckOut(
            @Part MultipartBody.Part CheckOutAttach,
            @Part("TotalDistanceAuto") RequestBody totalDistanceAuto,
            @Part("TotalDistanceManual") RequestBody totalDistanceManual,
            @Part("TotalExpenses") RequestBody totalExpense,

            @Part("SalesPersonCode") RequestBody SalesPersonCode,
            @Part("id") RequestBody id,

            @Part("CheckOutDate") RequestBody CheckOutDate,
            @Part("CheckOutTime") RequestBody CheckOutTime,
            @Part("CheckOutLat") RequestBody CheckOutLat,
            @Part("CheckOutLong") RequestBody CheckOutLong,
            @Part("CheckOutRemarks") RequestBody CheckOutRemarks
    );


    @Multipart
    @POST("expense/create")
    Call<ExpenseResponse> expense_create_multipart(
            @Part MultipartBody.Part Attach,
            @Part("id") RequestBody id,
            @Part("trip_name") RequestBody trip_name,
            @Part("type_of_expense") RequestBody type_of_expense,
            @Part("expense_from") RequestBody expense_from,
            @Part("expense_to") RequestBody expense_to,
            @Part("totalAmount") RequestBody totalAmount,
            @Part("createDate") RequestBody createDate,
            @Part("createTime") RequestBody createTime,
            @Part("createdBy") RequestBody createdBy,
            @Part("updateDate") RequestBody updateDate,
            @Part("updateTime") RequestBody updateTime,
            @Part("remarks") RequestBody remarks,
            @Part("employeeId") RequestBody employeeId,
            @Part("startLat") RequestBody startLat,
            @Part("startLong") RequestBody startLong,
            @Part("endLat") RequestBody endLat,
            @Part("endLong") RequestBody endLong,
            @Part("travelDistance") RequestBody travelDistance,
            @Part("tripId") RequestBody tripId


    );


    @POST("businesspartner/one")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseBusinessPartner> bp_one(@Body HashMap<String, String> obj);


    @POST("tripexpense/one_tripexpenses")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<AllTripExpenseResponse> trip_expense_one(@Body HashMap<String, String> obj);

    @POST("businesspartner/ledger_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseCustomerLedger> ledger_dashboard(@Body HashMap<String, String> obj);

    @POST("businesspartner/ledger_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseCustomerLedger> ledger_dashboard2(@Body JsonObject jsonObject);


    @POST("invoice/one_receipt")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ReceiptResponse> oneReceipt(@Body HashMap<String, Integer> obj);

    @POST("purchaseinvoices/one_receipt")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ReceiptResponse> purchaseOneReceipt(@Body HashMap<String, Integer> obj);

    @POST("activity/map_filter")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<MapResponse> getmaplocation(@Body HashMap<String, String> mapData);

    @POST("tripexpense/all_filter_tripexpenses")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<AllTripExpenseResponse> getAllTripExpense(@Body HashMap<String, String> mapData);


    @POST("businesspartner/filter_ledger_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseLedgerGroup> getLedgerGroupSales(@Body HashMap<String, String> mapData);

    @POST("businesspartner/filter_ledger_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseZoneGroup> getLedgerZoneGroupSales(@Body HashMap<String, String> mapData);

    @POST("businesspartner/filter_receivable_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseZoneGroup> getGroupReceivables(@Body HashMap<String, String> mapData);

    @POST("businesspartner/filter_due_payment_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseZoneGroup> getGroupDues(@Body HashMap<String, String> mapData);


    @POST("expense/delete")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ExpenseNewModelResponse> deleteexpense(@Body HashMap<String, List<String>> hd);

    @POST("warehouse/all_filter")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<WareHouseResponse> warehouseList(@Body HashMap<String, String> hd);

    @POST("businesspartner/monthly_sales_chart")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<SalesGraphResponse> salesGraph(@Body HashMap<String, String> hd);

    @POST("businesspartner/monthly_receipts_chart")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<SalesGraphResponse> receiptGraph(@Body HashMap<String, String> hd);

    @POST("businesspartner/monthly_receivable_chart")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<SalesGraphResponse> receivableGraph(@Body HashMap<String, String> hd);


    @POST("businesspartner/monthly_receivable_group_chart")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseReceivableGraph> receivableDueMonthGraph(@Body HashMap<String, String> hd);


    @POST("businesspartner/monthly_receivable_group_chart_filter")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseReceivableGraph> receivableZoneMonthGraph(@Body HashMap<String, String> hd);


    /****Purchase APi's ***/


    //todo filetzonepurchase
    @POST("purchaseinvoices/filter_purchase_ledger_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseZoneGroup> getLedgerZoneGroupPurchase(@Body HashMap<String, String> mapData);

    //todo purchase counter
    @POST("purchaseinvoices/purchase_ledger_dashboard_count")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<DashboardCounterResponse> getDashBoardCounterForPurchaseLedger(@Body HashMap<String, String> salesEmployeeItem);

    //todo purchaselist of customer
    @POST("purchaseinvoices/purchase_ledger_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<CustomerBusinessRes> getledgerlistPurchasePost(@Body HashMap<String, String> pay);

    //todo particular bp purchase
    @POST("purchaseinvoices/bp_purchase_ledger")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<LedgerCustomerResponse> getparticularledgerdetailsPurchase(@Body HashMap<String, String> logInDetail);


    //todo group purchase
    @POST("purchaseinvoices/filter_purchase_ledger_dashboard")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ResponseLedgerGroup> getLedgerGroupPurchase(@Body HashMap<String, String> mapData);


}


