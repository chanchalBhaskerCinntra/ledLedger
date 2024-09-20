package com.cinntra.ledure.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.SelectedItems;
import com.cinntra.ledure.adapters.ContactPersonAdapter;
import com.cinntra.ledure.adapters.CountryAdapter;
import com.cinntra.ledure.adapters.SalesEmployeeAdapter;
import com.cinntra.ledure.adapters.StateAdapter;
import com.cinntra.ledure.adapters.WareHouseDropdownAdapter;
import com.cinntra.ledure.globals.Globals;
import com.cinntra.ledure.model.AddressExtension;
import com.cinntra.ledure.model.ContactPersonData;
import com.cinntra.ledure.model.DocumentLines;
import com.cinntra.ledure.model.QuotationDocumentLines;
import com.cinntra.ledure.model.QuotationItem;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.model.SalesEmployeeItem;
import com.cinntra.ledure.model.StateData;
import com.cinntra.ledure.model.StateRespose;
import com.cinntra.ledure.model.UpdateQTDocumentLines;
import com.cinntra.ledure.model.UpdateQuotationModel;
import com.cinntra.ledure.model.WareHouseData;
import com.cinntra.ledure.model.WareHouseResponse;
import com.cinntra.ledure.viewModel.ItemViewModel;
import com.cinntra.ledure.webservices.NewApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class Order_Update_Fragment extends Fragment implements View.OnClickListener {
    public static int SelectItemCode = 102;
    private String QT_ID = "";
    public static String CardValue;
    public static String salePCode;

    UpdateQuotationModel addQuotationObj;

    FragmentActivity act;
    @BindView(R.id.add)
    ImageView add;

    @BindView(R.id.oppView)
    LinearLayout oppView;
    @BindView(R.id.head_title)
    TextView head_title;
    @BindView(R.id.back_press)
    RelativeLayout back_press;
    @BindView(R.id.loader)
    ProgressBar loader;

    /******General Tab********/

    @BindView(R.id.general)
    TextView general;
    @BindView(R.id.tab_1)
    LinearLayout tab_1;
    @BindView(R.id.general_frame)
    FrameLayout general_frame;
    @BindView(R.id.opportunity_name_value)
    EditText opportunity_name_value;

    @BindView(R.id.remark_value)
    EditText remark_value;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.companyNameCard)
    CardView companyNameCard;
    @BindView(R.id.company_name)
    TextView company_name;
    @BindView(R.id.qt_status)
    TextView qt_status;
    @BindView(R.id.valid_untill_date)
    TextView valid_untill_date;
    @BindView(R.id.contact_person_spinner)
    Spinner contact_person_spinner;

    /******Total Tab********/
    @BindView(R.id.tab_2)
    LinearLayout tab_2;
    @BindView(R.id.total_frame)
    FrameLayout total_frame;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.total_before_discont_value)
    EditText total_before_discont_value;
    @BindView(R.id._discont_value)
    EditText _discont_value;
    @BindView(R.id.tax_value)
    EditText tax_value;
    @BindView(R.id.total_value)
    EditText total_value;
    @BindView(R.id.shipping_value)
    EditText shipping_value;
    @BindView(R.id.next_button)
    Button next;
    @BindView(R.id.items_count)
    TextView items_count;
    @BindView(R.id.item_frame)
    RelativeLayout item_frame;


    /******Address Tab********/
    @BindView(R.id.tab_3)
    LinearLayout tab_3;
    @BindView(R.id.prepared_frame)
    FrameLayout prepared_frame;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.billing_name_value)
    EditText billing_name_value;
    @BindView(R.id.zip_code_value)
    EditText zip_code_value;
    @BindView(R.id.shipping_spinner)
    Spinner shipping_spinner;
    @BindView(R.id.billing_address_value)
    EditText billing_address_value;
    @BindView(R.id.done_button)
    Button done;


    @BindView(R.id.postingDate)
    LinearLayout postingDate;
    @BindView(R.id.posting_value)
    EditText posting_value;
    @BindView(R.id.postCal)
    ImageView postCal;


    @BindView(R.id.validDate)
    LinearLayout validDate;
    @BindView(R.id.valid_till_value)
    EditText valid_till_value;
    @BindView(R.id.validCal)
    ImageView validCal;

    @BindView(R.id.documentDate)
    LinearLayout documentDate;
    @BindView(R.id.document_date_value)
    EditText document_date_value;
    @BindView(R.id.docCal)
    ImageView docCal;


    @BindView(R.id.bpView)
    LinearLayout bpView;
    @BindView(R.id.quote_information)
    TextView quote_information;
    @BindView(R.id.checkbox1)
    CheckBox checkbox1;
    @BindView(R.id.ship_block)
    LinearLayout ship_block;
    @BindView(R.id.salesemployee_spinner)
    Spinner salesemployee_spinner;
    @BindView(R.id.shipping_name_value)
    EditText shipping_name_value;
    @BindView(R.id.shipping_address_value)
    EditText shipping_address_value;
    @BindView(R.id.zipcode_value2)
    EditText zipcode_value2;
    @BindView(R.id.shipping_spinner2)
    Spinner shipping_spinner2;
    @BindView(R.id.ok)
    ImageView ok;

    @BindView(R.id.country_value)
    Spinner country_value;
    @BindView(R.id.state_value)
    Spinner state_value;
    @BindView(R.id.ship_country_value)
    Spinner ship_country_value;
    @BindView(R.id.ship_state_value)
    Spinner ship_state_value;

    @BindView(R.id.tvWareHouseList)
    TextView tvWareHouseList;
    @BindView(R.id.spinnerWarehouse)
    Spinner spinnerWarehouse;

    public static boolean ESCAPED = true;
    public static boolean DISABLED = true;
    public static String wareHouseId = "";
    String[] shippinngType;
    String billtoState, billtoCountrycode, billtoCountryName, shiptoState, shiptoCountrycode, shiptoCountryName, shiptoStateCode, billtoStateCode;
    CountryAdapter countryAdapter;
    StateAdapter stateAdapter, shipstateAdapter;
    ArrayList<StateData> stateList = new ArrayList<>();
    ArrayList<StateData> shipstateList = new ArrayList<>();

    public Order_Update_Fragment() {
        //Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Order_Update_Fragment newInstance(String param1, String param2) {
        Order_Update_Fragment fragment = new Order_Update_Fragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    QuotationItem quotationItem;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle b = getArguments();
            quotationItem = (QuotationItem) b.getSerializable(Globals.QuotationItem);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        act = getActivity();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        View v = inflater.inflate(R.layout.edit_quotation, container, false);
        ButterKnife.bind(this, v);


        shippinngType = getActivity().getResources().getStringArray(R.array.shippingType);
        companyNameCard.setVisibility(View.VISIBLE);
        oppView.setVisibility(View.GONE);
        head_title.setText(getString(R.string.edit_order));
        quote_information.setText(getResources().getString(R.string.order_information));
        bpView.setVisibility(View.GONE);
        tab_1.setOnClickListener(this);
        tab_2.setOnClickListener(this);
        tab_3.setOnClickListener(this);
        general.setOnClickListener(this);
        item_frame.setOnClickListener(this);
        back_press.setOnClickListener(this);
        setDisable();
        setData();
        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ship_block.setVisibility(View.VISIBLE);
                } else {
                    ship_block.setVisibility(View.GONE);
                }
            }
        });


        if (Prefs.getString("FromLedger", "").equalsIgnoreCase("Ledger")) {
            setDisable();
            add.setVisibility(View.GONE);
        }

        return v;
    }

    private void setDisable() {

        add.setOnClickListener(this);
        bpView.setVisibility(View.GONE);

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(opportunity_name_value.getApplicationWindowToken(), 0);


        submit.setClickable(false);
        submit.setFocusable(false);


        opportunity_name_value.setFocusableInTouchMode(false);
        opportunity_name_value.setFocusable(false);
        opportunity_name_value.setClickable(false);

        contact_person_spinner.setEnabled(false);
        salesemployee_spinner.setEnabled(false);

        posting_value.setClickable(false);
        posting_value.setFocusable(false);
        posting_value.setFocusableInTouchMode(false);

        postingDate.setFocusable(false);
        postingDate.setClickable(false);
        postingDate.setFocusableInTouchMode(false);
        postingDate.setEnabled(false);

        valid_till_value.setFocusableInTouchMode(false);
        valid_till_value.setClickable(false);
        valid_till_value.setFocusable(false);


        validDate.setClickable(false);
        validDate.setFocusable(false);
        validDate.setEnabled(false);
        validDate.setFocusableInTouchMode(false);

        document_date_value.setFocusable(false);
        document_date_value.setClickable(false);
        document_date_value.setFocusableInTouchMode(false);

        documentDate.setFocusable(false);
        documentDate.setClickable(false);
        documentDate.setEnabled(false);
        documentDate.setFocusableInTouchMode(false);

        remark_value.setFocusableInTouchMode(false);
        remark_value.setFocusable(false);
        remark_value.setClickable(false);

        total_before_discont_value.setClickable(false);
        total_before_discont_value.setFocusable(false);
        total_before_discont_value.setFocusableInTouchMode(false);

        _discont_value.setFocusableInTouchMode(false);
        _discont_value.setFocusable(false);
        _discont_value.setClickable(false);

        tax_value.setFocusableInTouchMode(false);
        tax_value.setFocusable(false);
        tax_value.setClickable(false);

        shipping_value.setFocusableInTouchMode(false);
        shipping_value.setFocusable(false);
        shipping_value.setClickable(false);

        total_value.setFocusableInTouchMode(false);
        total_value.setFocusable(false);
        total_value.setClickable(false);

        next.setFocusable(false);
        next.setClickable(false);


        shipping_name_value.setFocusableInTouchMode(false);
        shipping_name_value.setFocusable(false);
        shipping_name_value.setClickable(false);

        zipcode_value2.setClickable(false);
        zipcode_value2.setFocusable(false);
        zipcode_value2.setFocusableInTouchMode(false);

        shipping_spinner2.setEnabled(false);

        done.setClickable(false);
        done.setFocusable(false);


        shipping_address_value.setFocusableInTouchMode(false);
        shipping_address_value.setFocusable(false);
        shipping_address_value.setClickable(false);

        billing_address_value.setClickable(false);
        billing_address_value.setFocusable(false);
        billing_address_value.setFocusableInTouchMode(false);

        billing_name_value.setFocusableInTouchMode(false);
        billing_name_value.setFocusable(false);
        billing_name_value.setClickable(false);

        zip_code_value.setClickable(false);
        zip_code_value.setFocusable(false);
        zip_code_value.setFocusableInTouchMode(false);
        country_value.setClickable(false);
        ship_state_value.setClickable(false);
        ship_country_value.setClickable(false);
        state_value.setClickable(false);
        shipping_spinner.setEnabled(false);

        add.setVisibility(View.VISIBLE);
        ok.setVisibility(View.GONE);

        ESCAPED = true;
        DISABLED = false;

    }

    String ContactPersonCode = "";
    String salesPersonCode = "";
    String billshiptype = "";
    String ship_shiptype = "";

    String warehouseCode = "";

    private ArrayList<WareHouseData> wareHouseList = new ArrayList<>();

    private void warehouseList(String zone) {
        HashMap<String, String> hde = new HashMap<>();
        hde.put("BusinessPlaceID", zone);
        Call<WareHouseResponse> call = NewApiClient.getInstance().getApiService(getActivity()).warehouseList(hde);
        call.enqueue(new Callback<WareHouseResponse>() {
            @Override
            public void onResponse(Call<WareHouseResponse> call, Response<WareHouseResponse> response) {
                if (response.code() == 200) {
                    // binding.loader.setVisibility(View.GONE);
                    if (response.body().getData() == null || response.body().getData().size() == 0) {
                      //  Globals.setmessage(requireContext());
                        //no_datafound.setVisibility(View.VISIBLE);
                    } else {
                        wareHouseList.clear();
                        wareHouseList.addAll(response.body().getData());
                        spinnerWarehouse.setAdapter(new WareHouseDropdownAdapter(requireContext(), wareHouseList));
                        Log.e("TAG", "onResponse:warehouseode " + warehouseCode);
                        spinnerWarehouse.setSelection(Globals.getselectedwarehouse(response.body().getData(), warehouseCode));
                        Log.e("TAG", "onResponse:warehouseode " + warehouseCode);

                        spinnerWarehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                wareHouseId = wareHouseList.get(i).getWarehouseCode();
                                addQuotationObj.setWarehouseCode(wareHouseId);
                                //  bpFullName = AllitemsList.get(i).getCardName();

                                //  callcontactpersonApi(cp_spinner,filterwithoutprospect(AllitemsList).get(i).getCardCode());
//                                callcontactpersonApi(cp_spinner, AllitemsList.get(i).getCardCode());
//                                bpReSourceID = AllitemsList.get(i).getCardCode();


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                wareHouseId = wareHouseList.get(0).getWarehouseCode();
                                addQuotationObj.setWarehouseCode(wareHouseId);
                            }
                        });


                    }


                }
            }

            @Override
            public void onFailure(Call<WareHouseResponse> call, Throwable t) {
                //  binding.loader.setVisibility(View.GONE);
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {

        Log.e("Data==>Q=>", quotationItem.toString());
        billtoCountrycode = quotationItem.getAddressExtension().getBillToCountry();
        billtoStateCode = quotationItem.getAddressExtension().getBillToState();
        billtoCountryName = quotationItem.getAddressExtension().getU_BCOUNTRY();
        billtoState = quotationItem.getAddressExtension().getU_BSTATE();
        shiptoCountrycode = quotationItem.getAddressExtension().getShipToCountry();
        shiptoStateCode = quotationItem.getAddressExtension().getShipToState();
        shiptoCountryName = quotationItem.getAddressExtension().getU_SCOUNTRY();
        shiptoState = quotationItem.getAddressExtension().getU_SSTATE();
        warehouseCode = quotationItem.getUnit();


        warehouseList(quotationItem.getUnit());
        callStateApi(billtoCountrycode, "billto");
        callStateApi(shiptoCountrycode, "shipto");
        countryAdapter = new CountryAdapter(getContext(), Dashboard.countrylist);
        country_value.setAdapter(countryAdapter);
        ship_country_value.setAdapter(countryAdapter);
        country_value.setSelection(Globals.getCountrypos(Dashboard.countrylist, billtoCountryName));
        ship_country_value.setSelection(Globals.getCountrypos(Dashboard.countrylist, shiptoCountryName));
        QT_ID = quotationItem.getDocEntry();
        Log.e("salesPersonCode=>", "" + quotationItem.getContactPersonCode().size());
        if (quotationItem.getContactPersonCode().size() > 0) {
            ContactPersonCode = quotationItem.getContactPersonCode().get(0).getId().toString();
            salesPersonCode = quotationItem.getSalesPersonCode().get(0).getSalesEmployeeCode().trim();


        }
        if (Globals.checkInternet(getActivity()))
            callContactApi(quotationItem.getCardCode());

        Globals.SelectedItems.clear();
       /* Globals.contentList.clear();
        Globals.contentList.addAll(quotationItem.getDocumentLines());*/


        /*********** Set Data In Header Section**************/
//          qt_status.setText(Globals.getStatus(quotationItem.getDocumentStatus()));

       /* if (Globals.viewStatus(quotationItem.getDocumentStatus()) == "Close"){
            qt_status.setText("Closed");
            qt_status.setBackground(getResources().getDrawable(R.drawable.closeroundsaffron));

        }else{
            qt_status.setText("Open");
            qt_status.setBackground(getResources().getDrawable(R.drawable.openroundedgreen));
        }*/

        Total_Before_Disscount = Total_Before_Disscount(quotationItem.getDocumentLines());
        Globals.SelectedItems.addAll(quotationItem.getDocumentLines());
        company_name.setText(quotationItem.getCardName());
        valid_untill_date.setText(getResources().getString(R.string.valid_untill) + ": " + quotationItem.getDocDueDate());

        _discont_value.setText(quotationItem.getDiscountPercent());//
        total_before_discont_value.setText(Globals.getAmmount(quotationItem.getDocCurrency(), String.valueOf(Total_Before_Disscount)));
        tax_value.setText(quotationItem.getAdditionalCharges());
        //total_value.setText(Globals.getAmmount(quotationItem.getDocCurrency(),quotationItem.getDocTotal())+" ( "+Globals.getAmmount(quotationItem.getDocCurrency(),quotationItem.getDocTotalSys())+" )");

        total_before_discont_value.setText(quotationItem.getDocCurrency() + " " + Globals.calculatetotalofitem(Globals.SelectedItems, quotationItem.getDiscountPercent()));
        // total_value.setText(quotationItem.getDocCurrency()+ " " +Globals.calculatetotal(10,Double.parseDouble(total_before_discont_value.getText().toString())));
        total_value.setText(quotationItem.getDocCurrency() + " " + Globals.ItemTotalAtAddWithTax(Globals.SelectedItems, Double.valueOf(quotationItem.getDiscountPercent())));
        document_date_value.setText(quotationItem.getTaxDate());
        valid_till_value.setText(quotationItem.getDocDueDate());
        posting_value.setText(quotationItem.getDocDate());
        remark_value.setText(quotationItem.getComments());

        if (quotationItem.getAddressExtension().getBillToBuilding() != null)
            billing_name_value.setText(quotationItem.getAddressExtension().getBillToBuilding());
        if (quotationItem.getAddressExtension().getBillToStreet() != null)
            billing_address_value.setText(quotationItem.getAddressExtension().getBillToStreet());
        if (quotationItem.getAddressExtension().getBillToZipCode() != null)
            zip_code_value.setText(quotationItem.getAddressExtension().getBillToZipCode());


        if (quotationItem.getAddressExtension().getShipToBuilding() != null)
            shipping_name_value.setText(quotationItem.getAddressExtension().getShipToBuilding());
        if (quotationItem.getAddressExtension().getShipToStreet() != null)
            shipping_address_value.setText(quotationItem.getAddressExtension().getShipToStreet());
        if (quotationItem.getAddressExtension().getShipToZipCode() != null)
            zipcode_value2.setText(quotationItem.getAddressExtension().getShipToZipCode());

        billshiptype = quotationItem.getAddressExtension().getU_SHPTYPB();
        if (billshiptype != null)
            shipping_spinner.setSelection(Globals.getShipTypePo(shippinngType, billshiptype));

        ship_shiptype = quotationItem.getAddressExtension().getU_SHPTYPS();
        if (ship_shiptype != null)
            shipping_spinner2.setSelection(Globals.getShipTypePo(shippinngType, ship_shiptype));

        items_count.setText("Items (" + quotationItem.getDocumentLines().size() + ")");


        /*********** Set Data In Content Section**************/
        frameManager(general_frame, total_frame, prepared_frame, general, total, address);


        /******************* Data for Api use ************************/
        addQuotationObj = new UpdateQuotationModel();
        if (quotationItem.getContactPersonCode().size() > 0)
            salePCode = quotationItem.getContactPersonCode().get(0).getId().toString();
        CardValue = quotationItem.getCardCode();
        addQuotationObj.setCardCode(CardValue);
        addQuotationObj.setSalesPerson(salePCode);


        contact_person_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ContactPersonCode = ContactEmployeesList.get(position).getInternalCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        shipping_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                billshiptype = shippinngType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (billshiptype != null)
                    shipping_spinner.setSelection(Globals.getShipTypePo(shippinngType, billshiptype));
            }
        });

        shipping_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ship_shiptype = shippinngType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (ship_shiptype != null)
                    shipping_spinner2.setSelection(Globals.getShipTypePo(shippinngType, ship_shiptype));
            }
        });


        salesemployee_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (salesEmployeeItemList.size() > 0 && position > 0) {
                    salesPersonCode = salesEmployeeItemList.get(position).getSalesEmployeeCode();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        salesEmployeeItemList = Globals.getSaleEmployeeArrayList(Globals.SalesEmployeeList);
        if (salesEmployeeItemList == null)
            callSalessApi();
        else {
            salesadapter = new SalesEmployeeAdapter(getActivity(), salesEmployeeItemList);
            salesemployee_spinner.setAdapter(salesadapter);
            if (!salesEmployeeItemList.isEmpty() && !salesPersonCode.isEmpty())
                salesemployee_spinner.setSelection(Globals.getSelectedSalesP(salesEmployeeItemList, salesPersonCode));
        }


        country_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                billtoCountrycode = Dashboard.countrylist.get(position).getCode();
                billtoCountryName = Dashboard.countrylist.get(position).getName();

                callStateApi(billtoCountrycode, "billto");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                billtoCountrycode = Dashboard.countrylist.get(0).getCode();
                billtoCountryName = Dashboard.countrylist.get(0).getName();
            }
        });

        state_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                billtoState = stateList.get(position).getName();
                billtoStateCode = stateList.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                billtoState = stateList.get(0).getName();
                billtoStateCode = stateList.get(0).getCode();
            }
        });


        ship_country_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shiptoCountrycode = Dashboard.countrylist.get(position).getCode();
                shiptoCountryName = Dashboard.countrylist.get(position).getName();

                callStateApi(shiptoCountrycode, "shipto");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                shiptoCountrycode = Dashboard.countrylist.get(0).getCode();
                shiptoCountryName = Dashboard.countrylist.get(0).getName();

            }
        });

        ship_state_value.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shiptoState = shipstateList.get(position).getName();
                shiptoStateCode = shipstateList.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                shiptoState = shipstateList.get(0).getName();
                shiptoStateCode = shipstateList.get(0).getCode();
            }
        });


    }


    private void setDefaults() {

        general.setOnClickListener(this);
        total.setOnClickListener(this);
        submit.setOnClickListener(this);
        address.setOnClickListener(this);
        add.setOnClickListener(this);
        next.setOnClickListener(this);
        done.setOnClickListener(this);
        posting_value.setOnClickListener(this);
        postingDate.setOnClickListener(this);
        postCal.setOnClickListener(this);
        valid_till_value.setOnClickListener(this);
        validDate.setOnClickListener(this);
        ok.setOnClickListener(this);
        validCal.setOnClickListener(this);
        document_date_value.setOnClickListener(this);
        docCal.setOnClickListener(this);
        documentDate.setOnClickListener(this);


    }

    private void callStateApi(String countryCode, String code) {

        StateData stateData = new StateData();
        stateData.setCountry(countryCode);
        Call<StateRespose> call = NewApiClient.getInstance().getApiService(getActivity()).getStateList(stateData);
        call.enqueue(new Callback<StateRespose>() {
            @Override
            public void onResponse(Call<StateRespose> call, Response<StateRespose> response) {

                if (response.code() == 200) {
                    if (code.equalsIgnoreCase("billto")) {


                        stateList.clear();
                        if (response.body().getData().size() > 0) {

                            stateList.addAll(response.body().getData());

                        } else {
                            StateData sta = new StateData();
                            sta.setName("Select State");
                            stateList.add(sta);
                        }
                        stateAdapter = new StateAdapter(getContext(), stateList);
                        state_value.setAdapter(stateAdapter);

                        stateAdapter.notifyDataSetChanged();
                        if (quotationItem.getAddressExtension().getU_BSTATE() != null)
                            state_value.setSelection(Globals.getStatePo(stateList, quotationItem.getAddressExtension().getU_BSTATE()));

                    } else {
                        shipstateList.clear();
                        if (response.body().getData().size() > 0) {

                            shipstateList.addAll(response.body().getData());

                        } else {
                            StateData sta = new StateData();
                            sta.setName("Select State");
                            shipstateList.add(sta);
                        }
                        shipstateAdapter = new StateAdapter(getContext(), shipstateList);
                        ship_state_value.setAdapter(shipstateAdapter);
                        shipstateAdapter.notifyDataSetChanged();
                        if (quotationItem.getAddressExtension().getU_SSTATE() != null)
                            ship_state_value.setSelection(Globals.getStatePo(shipstateList, quotationItem.getAddressExtension().getU_SSTATE()));
                    }
                } else {
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, QuotationResponse.class);
                        Log.e("Second", "onResponse: ");
                        Toast.makeText(getContext(), mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StateRespose> call, Throwable t) {
                Log.e("Third", "onResponse: ");
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.add:
                if (ESCAPED) {
                    Globals.openKeyboard(getContext());
                    setEnable();
                    setDefaults();
                } else {
                    setDisable();
                }
                break;
            case R.id.ok:

                String oppname = opportunity_name_value.getText().toString().trim();
                String poDate = posting_value.getText().toString().trim();
                String vDate = valid_till_value.getText().toString().trim();
                String docDate = document_date_value.getText().toString().trim();
                String discount = _discont_value.getText().toString().trim();
                String remark = remark_value.getText().toString().trim();

                if (valiadtion(ContactPersonCode, poDate, vDate, docDate, remark)) {

                    /*<================ Static Keys ===================>*/
                    addQuotationObj.setCardName("");
                    addQuotationObj.setPaymentType("");
                    addQuotationObj.setDeliveryMode("");
                    addQuotationObj.setDeliveryTerm("");
                    addQuotationObj.setAdditionalCharges(tax_value.getText().toString().trim());
                    addQuotationObj.setTermCondition("");
                    addQuotationObj.setDeliveryCharge("");
                    addQuotationObj.setUnit("");
                    addQuotationObj.setU_LAT("");
                    addQuotationObj.setU_LONG("");
                    addQuotationObj.setLink("");
                    addQuotationObj.setPayTermsGrpCode("");
                    addQuotationObj.setFreeDelivery("");


                    addQuotationObj.setOpportunityName(oppname);
                    addQuotationObj.setSalesPerson(ContactPersonCode);
                    addQuotationObj.setSalesPersonCode(salesPersonCode);
                    addQuotationObj.setPostingDate(poDate);
                    addQuotationObj.setValidDate(vDate);
                    addQuotationObj.setDiscountPercent(Float.valueOf(discount));
                    addQuotationObj.setDocumentDate(docDate);
                    addQuotationObj.setRemarks(remark);
                    addQuotationObj.setUpdateDate(Globals.getTodaysDate());
                    addQuotationObj.setUpdateTime(Globals.getTCurrentTime());
                    addQuotationObj.setCreateTime(Globals.getTCurrentTime());
                    addQuotationObj.setCreateDate(Globals.getTodaysDate());
                    addQuotationObj.setId(quotationItem.getId());


                    AddressExtension addressExtension = new AddressExtension();
                    addressExtension.setShipToBuilding(shipping_name_value.getText().toString());
                    addressExtension.setShipToStreet(shipping_address_value.getText().toString());
                    addressExtension.setShipToCity("");
                    addressExtension.setShipToZipCode(zipcode_value2.getText().toString());
                    addressExtension.setShipToState(shiptoStateCode);
                    addressExtension.setShipToCountry(shiptoCountrycode);   //countryCode2
                    addressExtension.setU_SSTATE(shiptoState);
                    addressExtension.setU_SCOUNTRY(shiptoCountryName);
                    addressExtension.setU_SHPTYPS(ship_shiptype);
                    addressExtension.setU_OPPRNM("");
                    addressExtension.setBillToBuilding(billing_name_value.getText().toString());
                    addressExtension.setBillToStreet(billing_address_value.getText().toString());
                    addressExtension.setBillToCity("");
                    addressExtension.setBillToZipCode(zip_code_value.getText().toString());
                    addressExtension.setBillToState(billtoStateCode);
                    addressExtension.setBillToCountry(billtoCountrycode);       //countryCode2
                    addressExtension.setU_BSTATE(billtoState);
                    addressExtension.setU_BCOUNTRY(billtoCountryName);
                    addressExtension.setU_SHPTYPB(billshiptype);
                    addressExtension.setId(quotationItem.getAddressExtension().getId());

                    addressExtension.setBillToDistrict("");
                    addressExtension.setShipToDistrict("");
                    addQuotationObj.setAddressExtension(addressExtension);

                    //  addQuotationObj.setDocumentLines(postJson(quotationItem.getDocumentLines()));
//                    addQuotationObj.setDocumentLines(postJsonCopy(Globals.SelectedItems,quotationItem.getDocumentLines()));
                    addQuotationObj.setDocumentLines(Globals.SelectedItems);


                    if (Globals.checkInternet(getActivity())) {

                        updateQuotation(QT_ID, addQuotationObj);
                    }
                    setDisable();
                }

                break;
            case R.id.customer_block:
                Bussiness_Partner_Fragment frg = new Bussiness_Partner_Fragment();
                FragmentTransaction tr = getFragmentManager().beginTransaction();
                tr.replace(R.id.quatoes_main_container, frg);
                tr.addToBackStack(null);
                tr.commit();
                break;
            case R.id.back_press:
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                Globals.hideKeybaord(v, getContext());
                getActivity().onBackPressed();
                break;
            case R.id.tab_1:
            case R.id.general:
                frameManager(general_frame, total_frame, prepared_frame, general, total, address);
                break;
            case R.id.tab_2:
            case R.id.total:
                frameManager(total_frame, general_frame, prepared_frame, total, general, address);
                break;
            case R.id.tab_3:
            case R.id.address:
                frameManager(prepared_frame, general_frame, total_frame, address, general, total);
                break;
            case R.id.posting_value:
            case R.id.postCal:
            case R.id.postingDate:
                if (DISABLED)
                    Globals.selectDate(getContext(), posting_value);
                break;
            case R.id.valid_till_value:
            case R.id.validDate:
            case R.id.validCal:
                if (DISABLED)
                    Globals.selectDate(getContext(), valid_till_value);
                break;
            case R.id.document_date_value:
            case R.id.docCal:
            case R.id.documentDate:
                if (DISABLED)
                    Globals.selectDate(getContext(), document_date_value);
                break;
            case R.id.submit:
                if (validation(remark_value.getText().toString().trim(), ContactPersonCode)) {
                    frameManager(total_frame, general_frame, prepared_frame, total, general, address);
                }
                break;
            case R.id.next_button:
                frameManager(prepared_frame, general_frame, total_frame, address, general, total);
                break;
            case R.id.done_button:
                // frameManager(prepared_frame,general_frame,total_frame,address,general,total);
                ok.performClick();
                break;

            case R.id.item_frame:
                Globals.hideKeybaord(v, getContext());
                if (DISABLED) {
                    Globals.inventory_item_close = false;
                    Intent intent = new Intent(act, SelectedItems.class);
                    intent.putExtra("FromWhere", "QT_UP");
                    startActivityForResult(intent, SelectItemCode);
                } else {
                    Globals.inventory_item_close = true;
                    Intent intent = new Intent(act, SelectedItems.class);
                    intent.putExtra("FromWhere", "invoices");
                    startActivityForResult(intent, SelectItemCode);
                }
                break;


        }


    }

    private void setEnable() {

        posting_value.setClickable(true);

        valid_till_value.setClickable(true);

        document_date_value.setClickable(true);

        submit.setClickable(true);
        submit.setFocusable(true);
        submit.setEnabled(true);

        opportunity_name_value.setFocusableInTouchMode(true);
        opportunity_name_value.setFocusable(true);
        opportunity_name_value.setClickable(true);

        contact_person_spinner.setEnabled(true);
        salesemployee_spinner.setEnabled(true);

        posting_value.setClickable(true);


        postCal.setEnabled(true);
        postCal.setClickable(true);
        postCal.setFocusable(true);
        postCal.setFocusableInTouchMode(true);


        postingDate.setClickable(true);

        valid_till_value.setClickable(true);


        validCal.setEnabled(true);
        validCal.setClickable(true);
        validDate.setClickable(true);

        document_date_value.setClickable(true);

        docCal.setEnabled(true);
        docCal.setClickable(true);

        documentDate.setClickable(true);

        remark_value.setFocusableInTouchMode(true);
        remark_value.setFocusable(true);
        remark_value.setClickable(true);

        total_before_discont_value.setClickable(true);
        total_before_discont_value.setFocusable(true);
        total_before_discont_value.setFocusableInTouchMode(true);

        _discont_value.setFocusableInTouchMode(true);
        _discont_value.setFocusable(true);
        _discont_value.setClickable(true);

        tax_value.setFocusableInTouchMode(true);
        tax_value.setFocusable(true);
        tax_value.setClickable(true);

        shipping_value.setFocusableInTouchMode(true);
        shipping_value.setFocusable(true);
        shipping_value.setClickable(true);

        total_value.setFocusableInTouchMode(true);
        total_value.setFocusable(true);
        total_value.setClickable(true);

        next.setFocusable(true);
        next.setEnabled(true);
        next.setClickable(true);

        shipping_name_value.setFocusableInTouchMode(true);
        shipping_name_value.setFocusable(true);
        shipping_name_value.setClickable(true);

        zipcode_value2.setClickable(true);
        zipcode_value2.setFocusable(true);
        zipcode_value2.setFocusableInTouchMode(true);
        shipping_spinner2.setEnabled(true);

        done.setClickable(true);
        done.setFocusable(true);
        done.setEnabled(true);

        shipping_address_value.setFocusableInTouchMode(true);
        shipping_address_value.setFocusable(true);
        shipping_address_value.setClickable(true);

        billing_address_value.setClickable(true);
        billing_address_value.setFocusable(true);
        billing_address_value.setFocusableInTouchMode(true);

        billing_name_value.setFocusableInTouchMode(true);
        billing_name_value.setFocusable(true);
        billing_name_value.setClickable(true);

        zip_code_value.setClickable(true);
        zip_code_value.setFocusable(true);
        zip_code_value.setFocusableInTouchMode(true);
        shipping_spinner.setEnabled(true);
        country_value.setClickable(true);
        state_value.setClickable(true);
        ship_state_value.setClickable(true);
        ship_country_value.setClickable(true);
        add.setVisibility(View.GONE);
        ok.setVisibility(View.VISIBLE);

        ESCAPED = false;
        DISABLED = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SelectItemCode) {
            items_count.setText("Item (" + Globals.SelectedItems.size() + ")");
            total_before_discont_value.setText(String.valueOf(Globals.calculatetotalofitem(Globals.SelectedItems, quotationItem.getDiscountPercent())));
            tax_value.setText("0");
            total_value.setText(String.valueOf(Globals.calculatetotal(10, Double.parseDouble(total_before_discont_value.getText().toString()))));


        }

    }

    private void frameManager(FrameLayout visiblle_frame, FrameLayout f1, FrameLayout f2,
                              TextView selected, TextView t1, TextView t2) {
        selected.setTextColor(getResources().getColor(R.color.colorPrimary));
        t1.setTextColor(getResources().getColor(R.color.black));
        t2.setTextColor(getResources().getColor(R.color.black));

        visiblle_frame.setVisibility(View.VISIBLE);
        f1.setVisibility(View.GONE);
        f2.setVisibility(View.GONE);

    }

    private boolean validation(
            String stagesCode, String remark) {
        if (stagesCode.isEmpty()) {
            Globals.showMessage(getContext(), getString(R.string.can_not_empty));
            return false;
        } else if (remark.isEmpty()) {
//            Globals.showMessage(getContext(), getString(R.string.can_not_empty));
//            return false;
        }

        return true;
    }


    /******************* Update API *********************/
    ArrayList<UpdateQTDocumentLines> postlist;

    private ArrayList<UpdateQTDocumentLines> postJson(ArrayList<QuotationDocumentLines> list) {
        postlist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++
        ) {
            UpdateQTDocumentLines dc = new UpdateQTDocumentLines();
            dc.setLineNum(list.get(i).getLineNum());
            dc.setItemCode(list.get(i).getItemCode());
            dc.setQuantity(list.get(i).getQuantity());
            dc.setTaxCode(list.get(i).getTaxCode());//BED+VAT
            dc.setUnitPrice(list.get(i).getUnitPrice());
            dc.setDiscountPercent(Float.valueOf(list.get(i).getDiscountPercent()));
            postlist.add(dc);
        }

        return postlist;
    }

    private ArrayList<UpdateQTDocumentLines> postJsonCopy(ArrayList<DocumentLines> list, ArrayList<QuotationDocumentLines> existingList) {
        int docNum = existingList.size();
        postlist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            UpdateQTDocumentLines dc = new UpdateQTDocumentLines();
            if (i < docNum) {
                dc.setLineNum(existingList.get(i).getLineNum());
            } else {
                docNum++;
                dc.setLineNum("" + docNum);
            }

            dc.setItemCode(list.get(i).getItemCode());
            dc.setQuantity(list.get(i).getQuantity());
            dc.setTaxCode(list.get(i).getTaxCode());//BED+VAT
            dc.setUnitPrice(list.get(i).getUnitPrice());
            dc.setItemDescription(list.get(i).getItemName());
            dc.setDiscountPercent(Float.valueOf("0.0"));
            postlist.add(dc);
        }

        return postlist;
    }


    float Total_Before_Disscount = 0;

    private float Total_Before_Disscount(ArrayList<DocumentLines> list) {
        float result = 0;
        for (int i = 0; i < list.size(); i++) {
            result = result + Float.parseFloat(list.get(i).getQuantity()) * Float.parseFloat(list.get(i).getUnitPrice());
        }
        return result;
    }

    private boolean valiadtion(String contactPerson, String postDate, String validDate,
                               String DocDate, String remarks) {
       /* if (contactPerson.isEmpty()) {
            Globals.showMessage(act, getResources().getString(R.string.can_not_empty));
            return false;
        } else */

        if (validDate.isEmpty()) {
            Globals.showMessage(act, getResources().getString(R.string.can_not_empty));
            return false;
        } else if (DocDate.isEmpty()) {
            Globals.showMessage(act, getResources().getString(R.string.can_not_empty));
            return false;
        } else if (postDate.isEmpty()) {
            Globals.showMessage(act, getResources().getString(R.string.can_not_empty));
            return false;
        } else if (remarks.isEmpty()) {
            Globals.showMessage(act, getResources().getString(R.string.can_not_empty));
            return false;
        }
        return true;
    }

    private void updateQuotation(String QT_ID, UpdateQuotationModel in) {
        loader.setVisibility(View.VISIBLE);
        Call<QuotationResponse> call = NewApiClient.getInstance().getApiService(getActivity()).updateOrder(in);
        call.enqueue(new Callback<QuotationResponse>() {
            @Override
            public void onResponse(Call<QuotationResponse> call, Response<QuotationResponse> response) {
                loader.setVisibility(View.GONE);
                if (response.code() == 200) {
                    Log.e("TAG", "onResponse: success ");
                    Globals.SelectedItems.clear();
                    Toast.makeText(act, "Posted Successfully.", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } else {
                    Log.e("TAG", "onResponse: else success ");
                    //Globals.ErrorMessage(CreateContact.this,response.errorBody().toString());
                    Gson gson = new GsonBuilder().create();
                    QuotationResponse mError = new QuotationResponse();
                    try {
                        Log.e("TAG", "onResponse: else try ");
                        String s = response.errorBody().string();
                        mError = gson.fromJson(s, QuotationResponse.class);
                        Log.e("Fourth", "onResponse: ");
                        Toast.makeText(act, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Log.e("TAG", "onResponse: else catch ");
                        //handle failure to read error
                    }
                    //Toast.makeText(CreateContact.this, msz, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuotationResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                if (t.getLocalizedMessage().contains("IllegalStateException")) {
                    getActivity().onBackPressed();
                    Toast.makeText(act, "Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Fifth", "onResponse: " + t.getLocalizedMessage());
                    Toast.makeText(act, t.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private List<ContactPersonData> ContactEmployeesList;
    ContactPersonAdapter contactPersonAdapter;

    private void callContactApi(String cardCode) {
        ContactEmployeesList = new ArrayList<>();
        ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
        model.getContactEmployeeList(cardCode).observe(getActivity(), new Observer<List<ContactPersonData>>() {
            @Override
            public void onChanged(@Nullable List<ContactPersonData> itemsList) {
                if (itemsList == null || itemsList.size() == 0) {
                    // Globals.setmessage(getActivity());
                } else {
                    ContactEmployeesList = itemsList;
                    contactPersonAdapter = new ContactPersonAdapter(getActivity(), ContactEmployeesList);
                    contact_person_spinner.setAdapter(contactPersonAdapter);
                    //int index = ContactEmployeesList.get


                    if (itemsList != null && ContactPersonCode != null)
                        contact_person_spinner.setSelection(Globals.getSelectedContact(itemsList, ContactPersonCode));

                }
            }
        });
    }

    SalesEmployeeAdapter salesadapter;
    public List<SalesEmployeeItem> salesEmployeeItemList = new ArrayList<>();

    private void callSalessApi() {
        ItemViewModel model = ViewModelProviders.of(this).get(ItemViewModel.class);
        model.getSalesEmployeeList().observe(getActivity(), new Observer<List<SalesEmployeeItem>>() {
            @Override
            public void onChanged(@Nullable List<SalesEmployeeItem> itemsList) {
                if (itemsList == null || itemsList.size() == 0) {
                  //Globals.setmessage(getActivity());
                } else {
                    salesEmployeeItemList = itemsList;
                    salesadapter = new SalesEmployeeAdapter(getActivity(), itemsList);
                    salesemployee_spinner.setAdapter(salesadapter);
                    Log.e("salesPersonCode=>B=>", "" + salesPersonCode);
                    salesPersonCode = Prefs.getString(Globals.SalesEmployeeCode, "");
                    if (!itemsList.isEmpty() && !salesPersonCode.isEmpty()) {
                        salesemployee_spinner.setSelection(Globals.getSelectedSalesP(itemsList, salesPersonCode));

                    }
                }
            }
        });
    }


}