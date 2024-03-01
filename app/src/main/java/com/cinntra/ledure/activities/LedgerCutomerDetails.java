package com.cinntra.ledure.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.cinntra.ledure.R;
import com.cinntra.ledure.adapters.InvoicePagerAdapter;
import com.cinntra.ledure.databinding.ActivityLedgerCustomerDetailsBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogSelectDateBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShowCustomerDataBinding;
import com.cinntra.ledure.fragments.Customer_Summary;
import com.cinntra.ledure.fragments.StockGroupBPWiseFragment;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.interfaces.DateCallBacks;
import com.cinntra.ledure.interfaces.FragmentClickListener;
import com.cinntra.ledure.model.BusinessPartnerData;
import com.cinntra.ledure.model.ResponseBusinessPartner;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LedgerCutomerDetails extends AppCompatActivity implements FragmentClickListener, DateCallBacks {

    @BindView(R.id.new_quatos)
    RelativeLayout new_quatos;
    @BindView(R.id.relativeeSummary)
    RelativeLayout relativeeSummary;

    @BindView(R.id.relativeSold)
    RelativeLayout relativeSold;

    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.filterView)
    RelativeLayout filterView;

    @BindView(R.id.filter)
    ImageView ledgerImage;

    @BindView(R.id.current_text)
    TextView current_text;
    @BindView(R.id.past_text)
    TextView past_text;

    @BindView(R.id.relativeInfoView)
    RelativeLayout relativeInfoView;

    @BindView(R.id.relativeCalView)
    RelativeLayout relativeCalView;

    @BindView(R.id.search)
    RelativeLayout search;


    Long startDatelng = (long) 0.0;
    Long endDatelng = (long) 0.0;
    TextView from_to_date;
    MaterialDatePicker<Pair<Long, Long>> materialDatePicker;
    public String startDate = "";
    public String endDate = "";


    private String[] tabs = {"SUMMARY", "SOLD"};
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    public static String nameCustomer;
    String cardCode, email, mobile, address;
    int currentItem = 0;
    InvoicePagerAdapter pagerAdapter;
    private String name, gstNo, PaymentDays, GroupName, GSTIN, contactName, creditimit;

    @Override
    public void fragmentClickListener(String type) {

        Toast.makeText(LedgerCutomerDetails.this, type, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onItemClickOnItem() {

    }


    public interface MyFragmentListener {
        void onDataPassed(String startDate, String endDate);
    }

    private MyFragmentListener listener;

    public interface MyFragmentCustomerListener {
        void onDataPassedCustomer(String startDate, String endDate);
    }

    private MyFragmentCustomerListener listenerCustomer;

    private void setOnDataListener(MyFragmentListener listener1) {
        listener = listener1;
    }

    private void setOnCustomerDataListener(MyFragmentCustomerListener listener1) {
        listenerCustomer = listener1;
    }


    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {

        if (fragment instanceof MyFragmentListener) {
            listener = (MyFragmentListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement OnDataReceivedListener");
        }


        if (fragment instanceof MyFragmentCustomerListener) {
            listenerCustomer = (MyFragmentCustomerListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString()
                    + " must implement OnDataReceivedListener");
        }


    }

    ActivityLedgerCustomerDetailsBinding activityLedgerCustomerDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLedgerCustomerDetailsBinding = ActivityLedgerCustomerDetailsBinding.inflate(getLayoutInflater());


        setContentView(activityLedgerCustomerDetailsBinding.getRoot());
        ButterKnife.bind(this);
        //listener = (MyFragmentListener) this;
        //listenerCustomer = (MyFragmentCustomerListener) this;

        if (Prefs.getBoolean(Globals.ISPURCHASE,false)){
            activityLedgerCustomerDetailsBinding.pastText.setText("Purchase");
        }else {
            activityLedgerCustomerDetailsBinding.pastText.setText("Sold");
        }

        filterView.setVisibility(View.GONE);
        new_quatos.setVisibility(View.GONE);
        relativeInfoView.setVisibility(View.GONE);
        filterView.setVisibility(View.VISIBLE);
        search.setVisibility(View.GONE);
        // relativeCalView.setVisibility(View.VISIBLE);
        ledgerImage.setImageResource(R.drawable.ic_ledger);
        relativeCalView.setVisibility(View.GONE);
        startDate = Globals.firstDateOfFinancialYear();
        endDate = Globals.lastDateOfFinancialYear();


        Prefs.putString(Globals.FROM_DATE, startDate);
        Prefs.putString(Globals.TO_DATE, endDate);

        activityLedgerCustomerDetailsBinding.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to "
                + Globals.convertDateFormatInReadableFormat(endDate));


        nameCustomer = getIntent().getStringExtra("cardName");
        String title = getIntent().getStringExtra("cardName");
        cardCode = getIntent().getStringExtra("cardCode");

        /***shubh****/
        callApi();
        String groupName = getIntent().getStringExtra("group_name");
        String creditLimit = getIntent().getStringExtra("credit_limit");
        String creditDate = getIntent().getStringExtra("credit_date");

        activityLedgerCustomerDetailsBinding.ibDropToViewData.setOnClickListener(view -> {
            if (activityLedgerCustomerDetailsBinding.constraintDropDown.getVisibility() == View.VISIBLE) {
                activityLedgerCustomerDetailsBinding.constraintDropDown.setVisibility(View.GONE);
                activityLedgerCustomerDetailsBinding.hideMore.setVisibility(View.GONE);
                activityLedgerCustomerDetailsBinding.ibDropToViewData.setImageResource(R.drawable.ic_arrow_down);
            } else {
                activityLedgerCustomerDetailsBinding.constraintDropDown.setVisibility(View.VISIBLE);
                activityLedgerCustomerDetailsBinding.hideMore.setVisibility(View.VISIBLE);
                activityLedgerCustomerDetailsBinding.ibDropToViewData.setImageResource(R.drawable.ic_arrow_up);
            }

        });

        activityLedgerCustomerDetailsBinding.linearDate.setOnClickListener(view -> {
            showDateBottomSheetDialog(this);
        });


        head_title.setText(cardCode);

        //  Customer_Summary customer_summary=new Customer_Summary(cardCode, from_to_date);

        //  setOnDataListener(listener);
        // setOnCustomerDataListener(listenerCustomer);
        fragments.add(new Customer_Summary(cardCode, from_to_date, relativeCalView));
//        fragments.add(new Sold_ItemBases(cardCode, startDate, endDate, relativeCalView));
        fragments.add(new StockGroupBPWiseFragment(cardCode, startDate, endDate, relativeCalView));

        Customer_Summary fragment2 = new Customer_Summary(cardCode, from_to_date, relativeCalView);
//        Sold_ItemBases fragmentSold = new Sold_ItemBases(cardCode, startDate, endDate, relativeCalView);
        StockGroupBPWiseFragment fragmentSold = new StockGroupBPWiseFragment(cardCode, startDate, endDate, relativeCalView);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.containerSummarySold, fragment2);
        fragmentTransaction.commit();

        relativeeSummary.setOnClickListener(view -> {

            current_text.setTextColor(getResources().getColor(R.color.colorPrimary));
            past_text.setTextColor(getResources().getColor(R.color.grey));

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerSummarySold, fragment2)
                    .commit();
        });


        relativeSold.setOnClickListener(view -> {
            current_text.setTextColor(getResources().getColor(R.color.grey));
            past_text.setTextColor(getResources().getColor(R.color.colorPrimary));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerSummarySold, fragmentSold)
                    .commit();
        });


        //   tabLayout.setVisibility(View.VISIBLE);
        pagerAdapter = new InvoicePagerAdapter(getSupportFragmentManager(), fragments, tabs);


//        viewpager.setAdapter(pagerAdapter);
//        tabLayout.setupWithViewPager(viewpager);
        pagerAdapter.notifyDataSetChanged();
        back_press.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        /***shubh****/
        relativeInfoView.setOnClickListener(view -> {
            showCustomerBottomSheetDialog(this, name, GroupName, creditLimit, PaymentDays, contactName, address, email);


        });

//        relativeCalView.setOnClickListener(view -> {
////            if(currentItem==0)
////                Customer_Summary.newInstance(cardCode, from_to_date);
////            else
////                Sold_ItemBases.newInstance(cardCode, "", "");
//            showDateBottomSheetDialog(LedgerCutomerDetails.this);
//        });

        relativeCalView.setOnClickListener(view -> {
            showDateBottomSheetDialog(LedgerCutomerDetails.this);
        });

        filterView.setOnClickListener(view -> {
            Prefs.putString(Globals.FROM_DATE, startDate);
            Prefs.putString(Globals.TO_DATE, endDate);
            Prefs.putString("where", "Customer");
            Intent intent = new Intent(LedgerCutomerDetails.this, LedgerReports.class);
            intent.putExtra("cardCode", cardCode);
            intent.putExtra("where", "details");
            startActivity(intent);
        });

    }


    BusinessPartnerData Dataobj;

    private void callApi() {
        HashMap<String, String> hde = new HashMap<>();
        hde.put("CardCode", cardCode);

        Call<ResponseBusinessPartner> call = NewApiClient.getInstance().getApiService().bp_one(hde);
        call.enqueue(new Callback<ResponseBusinessPartner>() {
            @Override
            public void onResponse(Call<ResponseBusinessPartner> call, Response<ResponseBusinessPartner> response) {
                if (response.isSuccessful()) {
                    activityLedgerCustomerDetailsBinding.loader.loader.setVisibility(View.GONE);
                    if(response.body().getData().size()>0) {
                        Dataobj = response.body().getData().get(0);
                        //   salesamount.setText("Rs." + response.body().getTotalSales());
                        //    total_amount.setText("Rs." + response.body().getTotalSales());
                        /***shubh****/
//                        creditDate = response.body().getbPData().get(0).getCreditLimitDayes();
//                        creditLimit = response.body().getbPData().get(0).getCreditLimit();
//                        groupName = response.body().getbPData().get(0).getGroupName();
//                        gstNo = response.body().getbPData().get(0).getGstIn();
                        if(response.body().getData().get(0).getContactEmployees().size()>0)
                        mobile = response.body().getData().get(0).getContactEmployees().get(0).getMobilePhone();
                        email = response.body().getData().get(0).getEmailAddress();
                        address = "" + response.body().getData().get(0).getBPAddresses().get(0).getAddressName() + ", "
                                + " " + response.body().getData().get(0).getBPAddresses().get(0).getStreet() + ", "
                                + response.body().getData().get(0).getBPAddresses().get(0).getBlock() + ", " + response.body().getData().get(0).getBPAddresses().get(0).getState()
                                + ", " + response.body().getData().get(0).getBPAddresses().get(0).getZipCode();
                        name = response.body().getData().get(0).getCardName();
                        Globals.cardNameGlobal=name;
                        // gstNo=response.body().getData().get(0).getGstIn();
                        if (Dataobj.getBPAddresses().size() > 0) {
                            GSTIN = response.body().getData().get(0).getBPAddresses().get(0).getGSTIN();
                        }

                        GroupName = response.body().getData().get(0).getGroupName();
                        // ZoneName = response.body().getData().get(0).getZone();

                        PaymentDays = response.body().getData().get(0).getPayTermsGrpCode().get(0).getPaymentTermsGroupName();

                        if (response.body().getData().get(0).getContactEmployees().size() > 0) {
                            contactName = response.body().getData().get(0).getContactEmployees().get(0).getFirstName();
                        }

                        creditimit = response.body().getData().get(0).getCreditLimit();
                    }
                    ColorGenerator generator = ColorGenerator.MATERIAL;
                    int color1 = generator.getRandomColor();
                    String defaultKey = "UNKNOWN";

                    if (name.isEmpty()) {
                        Drawable drawable = TextDrawable.builder()
                                .beginConfig()
                                .withBorder(4) /* thickness in px */
                                .endConfig()
                                .buildRound(
                                        String.valueOf(defaultKey.charAt(0)).toUpperCase(Locale.getDefault()), color1
                                );
                        activityLedgerCustomerDetailsBinding.ivCustomerProflePic.setImageDrawable(drawable);
                    } else {
                        Drawable drawable = TextDrawable.builder()
                                .beginConfig()
                                .withBorder(4) /* thickness in px */
                                .endConfig()
                                .buildRound(
                                        String.valueOf(name.charAt(0)).toUpperCase(Locale.getDefault()), color1
                                );
                        activityLedgerCustomerDetailsBinding.ivCustomerProflePic.setImageDrawable(drawable);
                    }
                    setUpCustomerDetails();


                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseBusinessPartner> call, Throwable t) {
                activityLedgerCustomerDetailsBinding.loader.loader.setVisibility(View.GONE);
            }
        });


    }


    private void setUpCustomerDetails() {
        activityLedgerCustomerDetailsBinding.tvCustomerAddress.setText(address);
        activityLedgerCustomerDetailsBinding.tvCustomerEmail.setText(email);
        activityLedgerCustomerDetailsBinding.tvCustomerName.setText(nameCustomer);
        activityLedgerCustomerDetailsBinding.tvCustomerMobile.setText(mobile);
        activityLedgerCustomerDetailsBinding.tvGrouptypeDropDown.setText(GroupName);
        activityLedgerCustomerDetailsBinding.tvCreditDaysDropDown.setText(PaymentDays);
        activityLedgerCustomerDetailsBinding.tvCreditLimitDropDown.setText(creditimit);
        activityLedgerCustomerDetailsBinding.tvGstNumDropDown.setText(GSTIN);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.transaction_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.ledger:
                Prefs.putString(Globals.FROM_DATE, startDate);
                Prefs.putString(Globals.TO_DATE, endDate);
                Intent intent = new Intent();
                intent.putExtra("cardCode", cardCode);
                Toast.makeText(this, cardCode, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LedgerReports.class));
                break;
            case R.id.calendar:
                showDateBottomSheetDialog(LedgerCutomerDetails.this);

                // Globals.selectDat(this);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    BottomSheetDialogSelectDateBinding binding;

    private void showDateBottomSheetDialog(Context context) {

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
        binding = BottomSheetDialogSelectDateBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());
        binding.ivCloseBottomSheet.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
        });
        binding.tvCustomDateBottomSheetSelectDate.setOnClickListener(view ->
        {
            // Toast.makeText(context, "today", Toast.LENGTH_SHORT).show();

            bottomSheetDialog.dismiss();
            // dateRangeSelector();

        });
        binding.tvTodayDateBottomSheetSelectDate.setOnClickListener(view -> {
            listener.onDataPassed(startDate, endDate);
            startDatelng = Calendar.getInstance().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(endDatelng);
            Log.e("TODAY====>>", "showDateBottomSheetDialog: ");
            //  listener.onDataPassed(startDate,endDate);
            //  listenerCustomer.onDataPassedCustomer(startDate,endDate);
            if (currentItem == 0)
                Customer_Summary.newInstance(cardCode, from_to_date, relativeCalView);
            else
//                Sold_ItemBases.newInstance(cardCode, startDate, endDate, relativeCalView);
                StockGroupBPWiseFragment.newInstance(cardCode, startDate, endDate, relativeCalView);
            Prefs.putString(Globals.FROM_DATE, startDate);
            Prefs.putString(Globals.TO_DATE, endDate);
            listener.onDataPassed(startDate, endDate);

            activityLedgerCustomerDetailsBinding.tvDateText.setText("Today");
            bottomSheetDialog.dismiss();
        });

        binding.tvYesterdayDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.yesterDayCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.Date_yyyy_mm_dd(startDatelng);
            endDate = Globals.Date_yyyy_mm_dd(startDatelng);
            //  from_to_date.setText(startDate + " - " + endDate);
            //   Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            //  customerLedger(cardCode,startDate,endDate);
            //  from_to_date.setText(binding.tvYesterdayDateBottomSheetSelectDate.getText().toString());
            //  listener.onDataPassed(startDate,endDate);
            Log.e("Yesterday====>>", "showDateBottomSheetDialog: ");
            Prefs.putString(Globals.FROM_DATE, startDate);
            Prefs.putString(Globals.TO_DATE, endDate);
            listener.onDataPassed(startDate, endDate);
            activityLedgerCustomerDetailsBinding.tvDateText.setText("Yesterday");

            bottomSheetDialog.dismiss();
        });
        binding.tvThisWeekDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisWeekCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.thisWeekfirstDayOfMonth();
            endDate = Globals.thisWeekLastDayOfMonth();
            Prefs.putString(Globals.FROM_DATE, startDate);
            Prefs.putString(Globals.TO_DATE, endDate);
            listener.onDataPassed(startDate, endDate);

            Log.e("ThisWeek====>>", "showDateBottomSheetDialog: ");
            activityLedgerCustomerDetailsBinding.tvDateText.setText("This Week");

            bottomSheetDialog.dismiss();
        });


        binding.tvThisMonthBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisMonthCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfMonth();
            endDate = Globals.lastDateOfMonth();
            Prefs.putString(Globals.FROM_DATE, startDate);
            Prefs.putString(Globals.TO_DATE, endDate);
            listener.onDataPassed(startDate, endDate);
            activityLedgerCustomerDetailsBinding.tvDateText.setText("This Month");

            bottomSheetDialog.dismiss();
        });
        binding.tvLastMonthDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastMonthCal().getTimeInMillis();
            endDatelng = Globals.thisMonthCal().getTimeInMillis();
            startDate = Globals.lastMonthFirstDate();
            endDate = Globals.lastMonthlastDate();
            Prefs.putString(Globals.FROM_DATE, startDate);
            Prefs.putString(Globals.TO_DATE, endDate);
            listener.onDataPassed(startDate, endDate);
            activityLedgerCustomerDetailsBinding.tvDateText.setText(Globals.convertDateFormat(startDate)+" to "+Globals.convertDateFormat(endDate));

            bottomSheetDialog.dismiss();
        });
        binding.tvThisQuarterDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisQuarterCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.lastQuarterFirstDate();
            endDate = Globals.lastQuarterlastDate();
            Prefs.putString(Globals.FROM_DATE, startDate);
            Prefs.putString(Globals.TO_DATE, endDate);
            listener.onDataPassed(startDate, endDate);

            activityLedgerCustomerDetailsBinding.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to "
                    + Globals.convertDateFormatInReadableFormat(endDate));

            bottomSheetDialog.dismiss();
        });
        binding.tvThisYearDateBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.thisyearCal().getTimeInMillis();
            endDatelng = Calendar.getInstance().getTimeInMillis();
            startDate = Globals.firstDateOfFinancialYear();
            endDate = Globals.lastDateOfFinancialYear();
            Prefs.putString(Globals.FROM_DATE, startDate);
            Prefs.putString(Globals.TO_DATE, endDate);
            listener.onDataPassed(startDate, endDate);
            //    from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
            activityLedgerCustomerDetailsBinding.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to "
                    + Globals.convertDateFormatInReadableFormat(endDate));

            bottomSheetDialog.dismiss();
        });
        binding.tvLastYearBottomSheetSelectDate.setOnClickListener(view -> {
            startDatelng = Globals.lastyearCal().getTimeInMillis();
            endDatelng = Globals.thisyearCal().getTimeInMillis();
            startDate = Globals.lastYearFirstDate();
            endDate = Globals.lastYearLastDate();
            Prefs.putString(Globals.FROM_DATE, startDate);
            Prefs.putString(Globals.TO_DATE, endDate);
            listener.onDataPassed(startDate, endDate);
            //  from_to_date.setText(startDate + " - " + endDate);
            Log.e("Today==>", "startDate=>" + startDate + "  endDate=>" + endDate);
//            callledgerapi(reportType, startDate, endDate);
            activityLedgerCustomerDetailsBinding.tvDateText.setText("" + Globals.convertDateFormatInReadableFormat(startDate) + " to "
                    + Globals.convertDateFormatInReadableFormat(endDate));

            bottomSheetDialog.dismiss();
        });
        binding.tvAllBottomSheetSelectDate.setOnClickListener(view -> {
            startDate = "";
            endDate = "";
            Prefs.putString(Globals.FROM_DATE, startDate);
            Prefs.putString(Globals.TO_DATE, endDate);
            listener.onDataPassed(startDate, endDate);

            activityLedgerCustomerDetailsBinding.tvDateText.setText("All");

            bottomSheetDialog.dismiss();
        });


        bottomSheetDialog.show();

    }

    private void dateRangeSelector()
            {


        if (startDatelng == 0.0) {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(), MaterialDatePicker.todayInUtcMilliseconds())).build();
        } else {
            materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(startDatelng, endDatelng)).build();

        }

        materialDatePicker.show(getSupportFragmentManager(), "Tag_Picker");
       /* materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {

                Log.e("FromDate=>",String.valueOf(selection));

            }
        });
*/
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                startDatelng = selection.first;
                endDatelng = selection.second;
                startDate = Globals.Date_yyyy_mm_dd(startDatelng);
                endDate = Globals.Date_yyyy_mm_dd(endDatelng);
                Prefs.putString(Globals.FROM_DATE, startDate);
                Prefs.putString(Globals.TO_DATE, endDate);
                listener.onDataPassed(startDate, endDate);
                //   listener.onDataPassed(startDate,endDate);
                listenerCustomer.onDataPassedCustomer(startDate, endDate);

            }
        });


    }


    private void showCustomerBottomSheetDialog(Context context, String title, String groupName, String creditLimit, String creditDate, String mobile, String address, String email) {
        BottomSheetDialogShowCustomerDataBinding binding;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
        binding = BottomSheetDialogShowCustomerDataBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(binding.getRoot());
        binding.ivCloseBottomSheet.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
        });

        binding.tvCustomerNameBottomSheetDialog.setText(title);
        //  Toast.makeText(context, ""+groupName +creditDate +creditLimit, Toast.LENGTH_SHORT).show();
        /***shubh****/
        binding.etGroupName.setText(groupName);
        binding.etCreditLimit.setText(creditLimit);
        binding.etCreditDate.setText(creditDate);
        binding.etEmail.setText(email);
        binding.etMobileNumber.setText(mobile);
        binding.etAddress.setText(address);
        binding.etCstNumber.setText(GSTIN);
        binding.etContactName.setText(contactName);


        bottomSheetDialog.show();

    }


}