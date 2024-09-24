package com.cinntra.ledure.globals;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.Login;
import com.cinntra.ledure.databinding.BottomSheetDialogShareReportBinding;
import com.cinntra.ledure.databinding.BottomSheetDialogShowCustomerDataBinding;
import com.cinntra.ledure.model.Branch;
import com.cinntra.ledure.model.ContactPersonData;
import com.cinntra.ledure.model.CountryData;
import com.cinntra.ledure.model.DocumentLines;
import com.cinntra.ledure.model.IndustryItem;
import com.cinntra.ledure.model.Items;
import com.cinntra.ledure.model.LeadTypeData;
import com.cinntra.ledure.model.OwnerItem;
import com.cinntra.ledure.model.PayMentTerm;
import com.cinntra.ledure.model.QuotationDocumentLines;
import com.cinntra.ledure.model.QuotationItem;
import com.cinntra.ledure.model.QuotationResponse;
import com.cinntra.ledure.model.SalesEmployeeItem;
import com.cinntra.ledure.model.StagesItem;
import com.cinntra.ledure.model.StateData;
import com.cinntra.ledure.model.WareHouseData;
import com.cinntra.ledure.newapimodel.NewOpportunityRespose;
import com.cinntra.ledure.receivers.ConnectivityReceiver;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.pixplicity.easyprefs.library.Prefs;
import com.webviewtopdf.PdfView;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import es.dmoral.toasty.Toasty;

import static android.content.Context.INPUT_METHOD_SERVICE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

public class Globals {

    public static String cardTypeCCustomer = "cCustomer";
    public static String cardTypeCSupplier = "cSupplier";
    public static String payLoadCardType = "CardType";
    public static String isCheckingStart = "_isCheckingStart";
    public static String cardNameGlobal = "";


    // Static method to get the current date and time in the desired format
    public static String getCurrentDateTimeFormatted() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
    public static String foo(double value) //Got here 6.743240136E7 or something..
    {
        DecimalFormat formatter;

        if (value - (int) value > 0.0)
            formatter = new DecimalFormat("0.00"); //Here you can also deal with rounding if you wish..
        else
            formatter = new DecimalFormat("0.00");

        return formatter.format(value);
    }


    public static double currentlattitude = 28.622827380895615;
    public static double currentlongitude = 77.36626848578453;

    public static int TYPE_EVENT = 1;
    public static int TYPE_TASK = 2;
    public static int TopItem = 100;
    public static String APILog = "Login";
    public static String IP = "103.118.16.194";      // HANA

    public static String PORT = "1433";             //HANA
    public static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    public static String database = "SLDModel.SLDData";
    public static String username = "sa";
    public static String password = "sa@2019";
    public static String MYEmployeeID = "";

    public static String Query = "Exec";

    public static ArrayList<Branch> branchData = new ArrayList<Branch>();

    //Demo
    public static String SelectedDB = "TEST";
    public static String OTP = "_Otp";
    public static int QUERY_PAGE_SIZE = 20;
    public static String PAGE_NO_STRING = "PageNo=";
    public static String QUERY_MAX_PAGE_PDF = "&MaxSize=";
    public static String QUERY_SALEPERSON_CODE_PDF = "&SalesEmployeeCode=";
    //todo for purchase
    public static String ISPURCHASE = "_ISPURCHASE";

    //todo SECURITY KEY FOR CAPTCHA

    public static String CAPTCHA_SECURITY_KEY = "6LcYlHMpAAAAALVWt8TQsNDNczOS4FL9WzXP-HnH";
    // public static String CAPTCHA_SITE_KEY = "6LcYlHMpAAAAAPbjfJ51sU3IzcjIu5_Gcjx9s2bA";
    public static String CAPTCHA_SITE_KEY = "6LetdnMpAAAAAKusxdWskGdOan0MwiR7LR5ZZpUt";


    //public static String SelectedDB  = "TEST2304";

    public static String locationcondition = "_locationcondition";
    public static String curntDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

    public static String BaseURL = "http://103.107.67.94:50001/";  // OFFICE HANA URL
    public static String Acess_Js_BaseURL = "http://103.107.67.94:8000/";   // OFFICE HANA URL
    // public static   String PDFURL      =  "http://103.234.187.197:8002/static/html/";


    /***********Live******************/
    /* public static String NewBaseUrl   = "http://103.107.67.160:8001/";            //Live
       public static String PDFURL         = "http://ledure.bridgexd.com/assets/html/";//Live*/


    /************ Live Development *******************/
    public static String NewBaseUrl = "http://103.107.67.160:8002/";
    public static String PDFURL = "http://ledure.bridgexd.com/assets/newhtml/";

    public static String ImageURL = "http://103.107.67.160:8002";

    /************ Test Testing ONLY FOR PAYMENT_DUE *******************/

/*    public static String NewBaseUrl = "http://103.107.67.160:8003/";
    public static String PDFURL = "http://103.107.67.160:8003/assets/html/";
    public static String ImageURL = "http://103.107.67.160:8003";*/


    /************ Test Development *******************/

    /*public static String NewBaseUrl = "http://103.107.67.160:8005/";
    public static String PDFURL = "http://103.234.187.197:4309/assets/newhtml/";
    public static String ImageURL = "http://103.107.67.160:8005";*/
    public static String pdfGlobalUrl = "http://103.234.187.197:4214/";
    public static String attachmentBaseUrl = "http://103.107.67.160:8005/";
    public static String LedgerUrl = "" + PDFURL + "Saleledger.html?";
    public static String creditNoteUrl = "" + PDFURL + "ar_credit_note.html?";
    public static String Ap_creditNoteUrl = "" + PDFURL + "ap_debit_note.html?";
    public static String creditNoteUrl_Service = "" + PDFURL + "ar_credit_note_service.html?";
    public static String debitNoteUrl = "" + PDFURL + "ar_debit_note.html?";

    public static String invoiceUrl = "" + PDFURL + "CompanyInvoice.html?";

    public static String apInvoiceUrl = "" + PDFURL + "ap_Invoice.html?";
    public static String customerPdfUrl = "" + PDFURL + "Customerledger.html?";

    public static String journalVoucher = "" + PDFURL + "journalvoucher.html?id=";
    public static String allCustomerPdfUrl = "" + PDFURL + "PartList.html?SalesPersonCode=";
    public static String allCreditNote = "" + PDFURL + "Credit_Note.html?";

    public static String allDebitNote = "" + PDFURL + "Debit_Note.html?";
    public static String perticularCreditNote = "" + PDFURL + "PerticularCredit.html?";

    public static String receiptAllPdfUl = "" + PDFURL + "ReceiptReportSharing.html?Type=Gross&";
    public static String overAllReceivable = "" + PDFURL + "OverAllReceivable.html?Type=Gross&";
    public static String overAllLedger = "" + PDFURL + "OverAllLedger.html?";
    public static String overAllPaymentCollectionOverDue = "" + PDFURL + "PaymentcollectionAll.html?";
    public static String perticularBpPaymentCollection = "" + PDFURL + "PaymentcollectionOne.html?";
    public static String perticularBpPaymentCollectionPurchase = "" + PDFURL + "purchase/PaymentcollectionOne.html?";
    public static String particularBpRceipt = "" + PDFURL + "PerticularBPReceipt.html?Type=Gross&";
    public static String saleReportsPdf = "" + PDFURL + "SalesReportSharing.html?";
    public static String saleGroupReportsPdf = "" + PDFURL + "filter_sale.html?";
    public static String itemInPartySectionPdf = "" + PDFURL + "ListItem1.html?";
    public static String GroupStockReportsPdf = "" + PDFURL + "stockgroup.html?";
    public static String ItemStockReportsPdf = "" + PDFURL + "itemstock.html?";

    public static String itemSaleReportsPdf = "" + PDFURL + "SalesItemReportSharingItem.html?ItemCode=";

    // public static   String overAllReceivable =  "http://103.234.187.197:4214/assets/html/OverAllReceivable.html?Type=Gross&FromDate=&ToDate=";
    public static String particularBpSales = "" + PDFURL + "PerticularBPSharing.html?";

    public static String itemParticularBpSales = "" + PDFURL + "ItemPerticularBPSharing.html?";
    public static String orderOnePdfUr = "" + PDFURL + "PendingOrder.html?";
    public static String receiptVoucherPdf = "" + PDFURL + "ReceiptVoucher.html?";
    public static String receivableParticular = "" + PDFURL + "ReceivablePerticular.html?";
    // public static   String receiptPdfUr      =  "http://103.234.187.197:4214/src/assets/html/ReceiptVoucher.html";


    /**************************  Get APIs Urls  **************************/

    public static String deliveryOverDueList = "b1s/v1/DeliveryNotes?$filter=DocDueDate lt '";

    public static String GetUserID = "b1s/v1/Users?$select=InternalKey&$filter=UserCode eq '";
    public static String GetProfile = "b1s/v1/Users(";
    public static String GetCountry = "b1s/v1/Countries";
    public static String GetStates = "b1s/v1/States";
    public static String GetOpportunity = "b1s/v1/SalesOpportunities";
    public static String GetOpportunityOpen = "b1s/v1/SalesOpportunities?$filter=Status eq 'sos_Open'&$orderby=SequentialNo desc";
    public static String GetOpportunityWon = "b1s/v1/SalesOpportunities?$filter=Status eq 'sos_Sold'&$orderby=SequentialNo desc";
    public static String GetOpportunityLost = "b1s/v1/SalesOpportunities?$filter=Status eq 'sos_Missed'&$orderby=SequentialNo desc";
    public static String GetInvoice = "b1s/v1/Invoices";

    public static final String MyID = "_my_employee_id";
    public static String LeadDetails = "_LeadDetails";
    public static String SelectedDatabase = "_Selected_Database";
    public static String SelectedBranch = "_Selected_Branch";
    public static String SelectedBranchID = "_Selected_Branch_ID";
    public static String BranchId = "_Branch_ID";
    public static String ZONE = "_ZONE_ID";
    public static String ADDRESS_LOGIN = "_ADDRESS_LOGIN_ID";
    public static String MPIN_VALUE = "_MPIN_VALUE";
    public static String SelectedWareHose = "_Selected_WareHouse_Code";
    public static String Total_Receivables = "_Total_Receivables_";
    public static String SelectedSqlIP = "_SelectedSqlIP";
    public static String SelectedSqlUser = "_SelectedSqlUser";
    public static String SelectedSqlPass = "_SelectedSqlPass";
    public static String SAPUser = "_SAPUser";
    public static String SAPPassword = "_SAPPassword";
    public static String BussinessPageType = "_BussinessPageType";
    public static String App_USERID = "_User_ID_";
    public static String SESSION_TIMEOUT = "_SESSION_TIMEOUT";
    public static String GROSS_NET = "_GROSS_NET";
    public static String PREFS_ATOZ = "_PREFS_ATOZ";
    public static String PREFS_Amount = "_PREFS_Amount";
    public static String SESSION_REMAIN_TIME = "_SESSION_REMAIN_TIME";
    public static String USER_TYPE = "_USER_TYPE";
    public static String USER_LABEL = "_USER_LABEL";
    public static String CountriesList = "_CountriesList";
    public static String StateList = "_StateList";
    public static String SalesEmployeeList = "_SalesEmployeeList";
    public static String USER_NAME = "_User_Name";
    public static String USER_PASSWORD = "_User_Password";
    public static String EmployeeID = "_Emp_Id";
    public static String Employee_Name = "_Employee_Name";
    public static String CHECK_IN_STATUS = "_checkInStatus";
    public static String EXPENSE_STATUS = "_checkInStatus";
    public static String Role = "_Role";
    public static String SalesEmployeeCode = "_SalesEmployeeCode";  //For TEam Selection
    public static String SalesEmployeeName = "_SalesEmployeeName";
    public static String AddContactPerson = "_AddContactPerson";
    public static String QuotationListing = "_QuotationList";
    public static String BP_TYPE_CHECK_IN = "_bpType";
    public static String BP_NAME_CHECK_IN = "_bpName";
    public static String START_LAT = "_startLat";
    public static String START_LONG = "_startLong";
    public static String MODE_OF_TRANSPORT = "_modeOfTransport";
    public static String START_DATE = "_modeOfTransport";
    public static String TRIP_ID = "_tripId";
    public static String FROM_DATE = "_FromDate_";
    public static String FROM_DATE_receivable = "_FromDate_receivable";
    public static String OVER_DUE_DATE_PREF = "_OVER_DUE_DATE_PREF";
    public static String TO_DATE = "_ToDate_";
    public static String TO_DATE_Receivable = "_ToDate_Receivable";
    public static String OpportunityQuotation = "_OpportunityQuotation";
    public static String REMEMBER_ME = "_REMEMBER_ME";

    public static String SelectQuotation = "_SelectQuotation";
    public static final String QuotationData = "_QuotationData";
    public static final String TOKEN = "_TOKEN";
    public static String TeamSalesEmployeCode = "";  //For login user
    public static String AppUserDetails = "_AppUserDetails";
    public static String TeamRole = "";
    public static String TeamEmployeeID = "";


    public static NewOpportunityRespose opp = null;
    /********************* Variables Names **********************/
    public static final String SessionID = "_session_id";
    public static final String QuotationItem = "_Quotation_Item";
    public static final String QuotationLine = "_Quotation_Line";
    public static final String OpportunityItem = "_OpportunityItem";
    public static final String LedgerCompanyData = "_LedgerCompanyData";
    public static final String OpportunityItemData = "_OpportunityItemData";
    public static final String CustomerItemData = "_cus_itemData";
    public static final String OwnerItemData = "_Owner_itemData";
    public static final String BussinessItemData = "_Bussiness_itemData";
    public static final String TaskEventList = "_TaskEventList";
    public static final String UserAdmin = "_UserAdmin";
    public static String SelectAddress = "_SelectAddress";
    public static String CampaignData = "_CampaignData";
    public static String Sale_Purchse_Diff = "_Sale_Purchse_Diff";

    public static String IS_SALE_OR_PURCHASE = "_IS_SALE_OR_PURCHASE";

    /************************* Global Lists ****************************/


    public static ArrayList<Items> contentList = new ArrayList<>();
    public static ArrayList<DocumentLines> SelectedItems = new ArrayList<>();
    public static ArrayList<OwnerItem> OwnerList = new ArrayList<>();
    public static ArrayList<NewOpportunityRespose> opportunityData = new ArrayList<>();


    public static boolean inventory_item_close = false;
    public static String CurrentSelectedDate = Globals.getTodaysDatervrsfrmt();
    public static String USERNAME = "_User_Name";
    public static String COMMENT = "";
    public static String CURRENT_CLASS = "com.cinntra.ledure.activities.MainActivity_B2C";
    public static String SelectOpportnity = "_SelectOpportnity";
    public static String AddBp = "_AddBp";
    public static String Lead_Data = "_Lead_Data";
    public static String FromQuotation = "_FromQuotation";

    public static ArrayList<QuotationItem> quotationOrder = new ArrayList<>();
    public static String videoUrl = "";
    public static String locationPermission = "locationPermission";
    public static String CheckMode = "_CheckMode";
    public static String PrefsItemATOZ = "_PrefsItemATOZ";
    public static String PrefsPendingATOZ = "_PrefsPendingATOZ";
    public static String PrefsItemAmount = "_PrefsItemAmount";
    public static String PrefsPendingAmount = "_PrefsPendingAmount";


    public static final String KEY_Total_Sale = "myList_Total_Sale_";
    public static final String KEY_Total_Collection = "myList_Total_Collection_";
    public static final String KEY_Total_Receivable = "myList_Total_Receivable_";
    public static final String KEY_Sale_Diff = "_Sale_Diff";
    public static final String KEY_Sale_Pending = "_SaleLedger";




    public static String getCurrentFinancialYear() {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int startYear, endYear;

        if (today.getMonthValue() >= 4) { // Financial year starts in April
            startYear = currentYear;
            endYear = currentYear + 1;
        } else {
            startYear = currentYear - 1;
            endYear = currentYear;
        }

        return startYear + "-" + String.format("%02d", endYear % 100); // Format as "YYYY-YY"
    }

    public static String getPreviousFinancialYear() {
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int startYear, endYear;

        if (today.getMonthValue() >= 4) { // Financial year starts in April
            startYear = currentYear - 1;
            endYear = currentYear;
        } else {
            startYear = currentYear - 2;
            endYear = currentYear - 1;
        }

        return startYear + "-" + String.format("%02d", endYear % 100); // Format as "YYYY-YY"
    }


    public static void logoutScreen(Context context) {
        Intent mainIntent = new Intent(context, Login.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(mainIntent);
        ((Activity) context).finish();
    }



    public static void showAlertDialog(
            Context context,
            String title,
            String message,
            String positiveButtonText,
            String negativeButtonText,
            int iconImg,
            Runnable onDelete,
            Runnable onCancel
    ) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setTitle(title)
                .setIcon(iconImg)
                .setPositiveButton(positiveButtonText, (dialogInterface, i) -> {
                    onDelete.run();
                    dialogInterface.dismiss();
                })
                .setNegativeButton(negativeButtonText, (dialogInterface, i) -> {
                    onCancel.run();
                    dialogInterface.dismiss();
                })
                .create()
                .show();
    }


  /*  public static void errorMessage(Context context, String message) {

        MotionTo.createColorToast((Activity) context, "Error", message, MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular)
        );
    }

    public static void successMessage(Context context, String message) {
        MotionToast.createColorToast((Activity) context, "Success", message, MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular)
        );
    }


    public static void warningMessage(Context context, String message) {
        MotionToast.createColorToast((Activity) context, "Warning", message, MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.helvetica_regular)
        );
    }*/

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static int getDeviceWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getDisplay().getRealMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;

        return width;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static int getDeviceHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getDisplay().getRealMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;


        return height;
    }

    public static int findPositionInStringArray(String[] dataArray, String target) {
        for (int i = 0; i < dataArray.length; i++) {
            if (dataArray[i].equals(target)) {
                return i;
            }
        }
        return -1; // Return -1 if not found
    }

    public static String getStatus(String v) {

        if (v.equalsIgnoreCase("O"))
            return "Open";
        else
            return "Close";
    }


    public static String getRoundOffUpTOTwo(String v) {
        Log.e("TAG", "getRoundOffUpTOTwo: " + v);
        double roundOffSales = Math.round(Double.valueOf(v) * 100.0) / 100.0;
        return String.valueOf(roundOffSales);
    }

    public static String viewStatus(String v) {

        if (v.equalsIgnoreCase("bost_Open"))
            return "Open";
        else
            return "Close";
    }


    public static String convertDateFormatInReadableFormat(String inputDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yy", Locale.getDefault());
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String getAmmount(String currency, String ammount) {
        if (currency.equalsIgnoreCase("INR"))
            return "\u20B9 " + ammount;
        else
            return ammount + " $";
    }

    public static String convertDecemal(String input) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(Double.parseDouble(input)) + "%";
    }

    public static String changeDecemal(String input) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(Double.parseDouble(input));
    }


    public static int pxFromDp(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    public static void ErrorMessage(Context context, String errorBlock) {
        Gson gson = new GsonBuilder().create();
        QuotationResponse mError = new QuotationResponse();

        try {
            // String s =response.errorBody().string();
            mError = gson.fromJson(errorBlock, QuotationResponse.class);
            Toast.makeText(context, mError.getError().getMessage().getValue(), Toast.LENGTH_LONG).show();
            Log.e("Test=>", mError.getError().getMessage().getValue());
        } catch (JsonSyntaxException e) {
            // handle failure to read error
        }
    }

    public static String getTodaysDate() {

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        return date;
    }

    public static String getTodaysDatervrsfrmt() {

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return date;
    }

    public static String getTCurrentTime() {
        String currentTime = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        return currentTime;
    }

    public static int skipTo(int page) {
        return TopItem * page;
    }

    public static void showMessage(Context context, String message) {
        Toasty.warning(context, message, Toast.LENGTH_LONG).show();
    }

    public static void selectDate(Context context, EditText textView) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String s = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        try {
                            Date strDate = dateFormatter.parse(s);
                            textView.setText(dateFormatter.format(strDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
//                textView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);


                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.setMessage(textView.getHint().toString());
        datePickerDialog.show();
    }

    public static ArrayList<Items> ItemarrayListConverter(ArrayList<QuotationDocumentLines> arr) {
        ArrayList<Items> items = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            Items item = new Items();

            item.setItemCode(arr.get(i).getItemCode());
            item.setItemName(arr.get(i).getItemDescription());
            item.setQuantity(arr.get(i).getQuantity());
            item.setItemUnitPrice(arr.get(i).getUnitPrice());
            item.setItemTaxCode(arr.get(i).getTaxCode());
            items.add(item);
        }

        return items;
    }

    public static int getSelectedSalesP(List<SalesEmployeeItem> list, String salesCode) {
        int po = -1;
        for (SalesEmployeeItem obj : list) {
            if (obj.getSalesEmployeeCode().trim().equalsIgnoreCase(salesCode.trim())) {
                po = list.indexOf(obj);
                break;
            }
        }
        return po;
    }

    public static int getselectedwarehouse(List<WareHouseData> list, String salesCode) {
        int po = -1;
        for (WareHouseData obj : list) {
            if (obj.getWarehouseCode().trim().equalsIgnoreCase(salesCode.trim())) {
                po = list.indexOf(obj);
                break;
            }
        }
        return po;
    }


    public static int getSelectedStage(List<StagesItem> list, String salesCode) {
        int po = -1;

        for (StagesItem obj : list) {
            if (obj.getStageno().trim().equalsIgnoreCase(salesCode.trim())) {
                po = list.indexOf(obj);
                break;
            }

        }
        return po;
    }

    public static int getSelectedContact(List<ContactPersonData> list, String salesCode) {
        int po = -1;

        for (ContactPersonData obj : list) {
            if (obj.getInternalCode().trim().equalsIgnoreCase(salesCode.trim())) {
                po = list.indexOf(obj);
                break;
            }

        }
        return po;
    }


    public static String getContactperson(List<ContactPersonData> list, String salesCode) {
        String contactper = "No contact person";

        for (ContactPersonData obj : list) {
            if (obj.getInternalCode().trim().equalsIgnoreCase(salesCode.trim())) {
                contactper = obj.getFirstName();
                return contactper;
            }

        }
        return contactper;
    }

    public static double calculatetotal(int i, double text) {
        double total = 0.00;
        total = text / 10;
        total = text + total;
        return total;
    }

    public static double calculatetotalofitem(ArrayList<DocumentLines> selectedItems, String head) {
        double sum = 0;
        double headerDiscount = 0.0;
        if (!head.isEmpty())
            headerDiscount = Double.valueOf(head);

        for (DocumentLines i : selectedItems) {
            double total = Double.parseDouble(String.valueOf(Double.parseDouble(i.getUnitPrice()) * Double.parseDouble(i.getQuantity())));
            double headDiscountValue = total * headerDiscount / 100;
            total = total - headDiscountValue;
            String taxRate = "0";
            String itemDis = String.valueOf(i.getDiscountPercent());
            double itemDisValue = total * Double.parseDouble(itemDis) / 100;
            total = total - itemDisValue;
            double itemTax = total * 0;
            total = total + itemTax;
            // sum += Double.parseDouble(String.valueOf(Double.parseDouble(i.getUnitPrice())*Double.parseDouble(i.getQuantity())));
            sum = total + sum;
        }
        return sum;
    }


    public static double ItemTotalAtAddWithTax(ArrayList<DocumentLines> selectedItems, double headerDiscount) {
        double sum = 0;
        double totaltaxAmm = 0;

        for (DocumentLines i : selectedItems) {
            double total = Double.parseDouble(String.valueOf(Double.parseDouble(i.getUnitPrice()) * Double.parseDouble(i.getQuantity())));

            String itemDis = String.valueOf(i.getDiscountPercent());
            double itemDisValue = (total * Double.parseDouble(itemDis) / 100);
            total = total - itemDisValue;
            double headDiscountValue = total * headerDiscount / 100;
            total = total - headDiscountValue;
            //  double itemDisValue     = total*0;

            double taxRate = 0.0;
            String itemTax = String.valueOf(i.getTaxRate());
            if (!itemTax.isEmpty())
                taxRate = Double.parseDouble(itemTax);

            double itemTaxValue = total * taxRate / 100;
            // total = total +itemTaxValue;
            totaltaxAmm += itemTaxValue;
            // sum += Double.parseDouble(String.valueOf(Double.parseDouble(i.getUnitPrice())*Double.parseDouble(i.getQuantity())));
            sum = sum + total;

        }
        return (sum + totaltaxAmm);
    }

    public static double ItemTotalAtAdd(ArrayList<DocumentLines> selectedItems, double headerDiscount) {
        double sum = 0;

        for (DocumentLines i : selectedItems) {
            double total = Double.parseDouble(String.valueOf(Double.parseDouble(i.getUnitPrice()) * Double.parseDouble(i.getQuantity())));
            double headDiscountValue = total * headerDiscount / 100;
            total = total - headDiscountValue;
            double itemDisValue = total * 0;
            // double itemDisValue  = total*Double.parseDouble("0.0")/100;
            total = total - itemDisValue;
            // sum += Double.parseDouble(String.valueOf(Double.parseDouble(i.getUnitPrice())*Double.parseDouble(i.getQuantity())));
            sum = total + sum;

        }
        return sum;
    }

    public static double calculatetotalofitemPrevious(ArrayList<DocumentLines> selectedItems) {
        double sum = 0;
        for (DocumentLines i : selectedItems)
            sum += Double.parseDouble(String.valueOf(Double.parseDouble(i.getUnitPrice()) * Double.parseDouble(i.getQuantity())));
        return sum;
    }


    public static int getOwenerPo(ArrayList<OwnerItem> list, String code) {

        int po = -1;
        for (OwnerItem obj : list) {
            if (obj.getEmployeeID().trim().equalsIgnoreCase(code.trim())) {
                po = list.indexOf(obj);
                break;
            }

        }
        return po;
    }

    public static int getIndustryPo(List<IndustryItem> list, String code) {

        int po = -1;
        for (IndustryItem obj : list) {
            if (obj.getIndustryCode().equalsIgnoreCase(code.trim())) {
                po = list.indexOf(obj);
                break;
            }

        }
        return po;
    }

    public static int getPaymentTermPo(List<PayMentTerm> list, String Code) {
        int po = -1;

        for (PayMentTerm obj : list) {
            if (obj.getGroupNumber().equalsIgnoreCase(Code.trim())) {
                po = list.indexOf(obj);
                break;
            }

        }
        return po;
    }

    public static boolean isvalidateemail(EditText email_value) {
        String checkEmail = email_value.getText().toString();
        boolean hasSpecialEmail = Patterns.EMAIL_ADDRESS.matcher(checkEmail).matches();
        if (!hasSpecialEmail) {
            email_value.setError("This E-Mail address is not valid");
            return false;
        }
        return true;
    }

    public static boolean checkInternet(Context context) {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected)
            return isConnected;
        else {
            Dialog dialog = new Dialog(context, R.style.DialogTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.no_internet_connection);
            dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.color.transparent));
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            Button tryagain = dialog.findViewById(R.id.try_again);
            tryagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            dialog.show();

            return isConnected;
        }
    }

    public static void setmessage(Context context) {
        Toast.makeText(context, context.getString(R.string.not_found), Toast.LENGTH_SHORT).show();
    }

    public static void openKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void hideKeybaord(View v, Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    public static void saveSaleEmployeeArrayList(List<SalesEmployeeItem> list, String key) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        Prefs.putString(key, json);
    }


    public static int SkipItem(int pageNo) {
        return 20 * pageNo;
    }

    public static int getShipTypePo(String[] list, String code) {

        int index = -1;
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(code)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static String getCountryCode(ArrayList<CountryData> list, String code) {

        String index = "IN";
        for (CountryData cd : list) {
            if (cd.getName().equals(code)) {
                index = cd.getCode();
                break;
            }
        }
        return index;
    }

    public static int getCountrypos(ArrayList<CountryData> list, String code) {

        int index = -1;
        for (CountryData cd : list) {
            if (cd.getName().equals(code)) {
                index = list.indexOf(cd);
                break;
            }
        }
        return index;
    }

    public static int getStatePo(ArrayList<StateData> list, String code) {

        int index = 0;
        for (StateData sd : list) {
            if (sd.getName().equals(code)) {
                index = list.indexOf(sd);
                break;
            }
        }
        return index;
    }

    public static String selectDat(Context context) {
        final String[] Date = {""};
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        Date[0] = year + "-" + monthOfYear + 1 + "-" + dayOfMonth;

                    }
                }, mYear, mMonth, mDay);

        // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
        return Date[0];
    }

    public static String getTimestamp() {
        final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return sdf3.format(timestamp);
    }


    public static ArrayList<SalesEmployeeItem> getSaleEmployeeArrayList(String key) {
        Gson gson = new Gson();
        String json = Prefs.getString(key, null);
        Type type = new TypeToken<List<SalesEmployeeItem>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    public static void noItemDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.no_internet_connection);
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.color.transparent));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        Button tryagain = dialog.findViewById(R.id.try_again);
        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });
        dialog.show();

    }

    public static void selectTime(Context context, EditText editText) {


        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar myTime = Calendar.getInstance();
                myTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myTime.set(Calendar.MINUTE, minute);
                myTime.set(Calendar.SECOND, 0);
                myTime.set(Calendar.MILLISECOND, 0);
                editText.setText(DateFormat.format("hh:mm aa", myTime).toString().toUpperCase());

            }
        }, 12, 0, false
        );

        timePickerDialog.show();
    }


    public static int getContactPos(List<ContactPersonData> data, String contactPerson) {
        int index = -1;
//        +  " " +data.get(i).getLastName()
        for (int i = 0; i < data.size(); i++) {
            String cp = data.get(i).getFirstName();
            if (cp.equals(contactPerson)) {
                index = i;
                break;
            }
        }
        return index;

    }

    public static int getleadType(List<LeadTypeData> data, String contactPerson) {
        int index = -1;
//        +  " " +data.get(i).getLastName()
        for (int i = 0; i < data.size(); i++) {
            String cp = data.get(i).getName();
            if (cp.equals(contactPerson)) {
                index = i;
                break;
            }
        }
        return index;

    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }


    public static long getDateInMilliSeconds(String givenDateString, String format) {
        String DATE_TIME_FORMAT = format;
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.US);
        long timeInMilliseconds = 1;
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    public static String Date_yyyy_mm_dd(double input) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return simpleFormat.format(input);
    }

    public static String numberToK1(String input) {

        int number = 0;
        if (!input.isEmpty())
            number = Integer.valueOf(input);
        String numberString = "";
        if (Math.abs(number / 1000000) > 1) {
            numberString = "" + (number / 1000000) + "m";

        } else if (Math.abs(number / 1000) > 1) {
            numberString = "" + (number / 1000) + "k";

        } else {
            numberString = "" + number;

        }
        return numberString;
    }

    public static boolean autoCheck() {
        final boolean[] bool = {false};
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bool[0] = true;
            }
        }, 2000);

        return bool[0];
    }


    public static String getDateForReceivable(int daysToAddOrSubtract) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Calculate the date by adding or subtracting days
        LocalDate resultDate = currentDate.plusDays(daysToAddOrSubtract);

        // Define the date format
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the result date as a string
        String formattedDate = resultDate.format(dateFormat);

        return formattedDate;
    }

    public static String numberToK(String number) {

        if (number == null || number.equalsIgnoreCase("null"))
            number = "00";
        else if (number.isEmpty())
            number = "00";
        // DecimalFormat df = new DecimalFormat("0.00");
        DecimalFormat df = new DecimalFormat("0");
        double amount = Double.parseDouble(df.format(Double.parseDouble(number)));
        NumberFormat format = NumberFormat.getInstance(new Locale("en", "IN"));
        String formattedNumber = format.format(amount);

        return formattedNumber;
    }

    public static String numberTokOnlyForStd(String number) {


        if (number == null || number.equalsIgnoreCase("null"))
            number = "00";
        else if (number.isEmpty())
            number = "00";
         DecimalFormat df = new DecimalFormat("0.00");
//        DecimalFormat df = new DecimalFormat("0");
        double amount = Double.parseDouble(df.format(Double.parseDouble(number)));
        NumberFormat format = NumberFormat.getInstance(new Locale("en", "IN"));
        String formattedNumber = format.format(amount);

        return formattedNumber;
    }

    public static String formatNumberToIndianStyle(long value) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("en", "IN"));
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        return decimalFormat.format(value);
    }


    public static String roundOffToNearestInteger(String stringValue) {
        // Parse the string to a BigDecimal
        BigDecimal decimalValue = new BigDecimal(stringValue);

        // Round off to the nearest integer
        BigDecimal roundedValue = decimalValue.setScale(0, BigDecimal.ROUND_HALF_UP);

        // Convert the rounded value to a string
        return roundedValue.toPlainString();
    }

    public static String numberToK_(String input) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        Number number = 0;
        float in = 0;
        if (!input.isEmpty() || input != null)
            number = Float.valueOf(input);

        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }

  /*  public static String convertToLakhAndCroreFromString(String input) {
        double result = convertStringToDouble(input);
*//*        String formattedNumber = "";
        if (result >= 1_00_00_000) {

            formattedNumber = (result / 1_00_00_000) + " Cr";
        } else if (result >= 1_00_000) {
            formattedNumber = result / 1_00_000 + " Lakh";
        } else {
            formattedNumber = "" + result;
        }

        return formattedNumber;*//*

        DecimalFormat indianFormat = new DecimalFormat("#.##");

        // Format the number with Cr and Lakh
        String formattedNumber;

      *//*  if (result >= 1_00_00_000) {
            formattedNumber = indianFormat.format(result / 1_00_00_000) + " Cr";
        } else if (result >= 1_00_000) {
            formattedNumber = indianFormat.format(result / 1_00_000) + " Lakh";
        } else {
            formattedNumber = indianFormat.format(result);
        }*//*  //todo comment by demand--
//        formattedNumber = indianFormat.format(result / 1_00_000) + " Lakh"; //todo comment on 31/01/2024


        if (result >= 10000000) { // If value is in crores
            double crore = result / 10000000;
            formattedNumber = String.format("%.2f Cr", crore);
        } else if (result >= 100000) { // If value is in lakhs
            double lakh = result / 100000;
            formattedNumber = String.format("%.2f Lakh", lakh);
        } else if (result >= 1000) { // If value is in thousands
            double thousand = result / 1000;
            formattedNumber = String.format("%.2f K", thousand);
        } else { // For values less than 1 lakh
            formattedNumber = String.format("%.0f", result);
        }

        return formattedNumber;

    }*/


    public static String convertToLakhAndCroreFromString(String input) {
        double result = convertStringToDouble(input);

        // Check if the result is negative
        boolean isNegative = result < 0;
        if (isNegative) {
            result = Math.abs(result); // Work with the absolute value for formatting
        }

        String formattedNumber;
        if (result >= 10000000) { // If value is in crores
            double crore = result / 10000000;
            formattedNumber = String.format("%.2f Cr", crore);
        } else if (result >= 100000) { // If value is in lakhs
            double lakh = result / 100000;
            formattedNumber = String.format("%.2f Lakh", lakh);
        } else if (result >= 1000) { // If value is in thousands
            double thousand = result / 1000;
            formattedNumber = String.format("%.2f K", thousand);
        } else { // For values less than 1 lakh
            formattedNumber = String.format("%.0f", result);
        }

        if (isNegative) {
            formattedNumber = "-" + formattedNumber; // Add the negative sign back
        }

        return formattedNumber;
    }





    public static String convertToLakhAndCroreFromFloat(float input) {
        //    double result = convertStringToDouble(input);
/*        String formattedNumber = "";
        if (result >= 1_00_00_000) {

            formattedNumber = (result / 1_00_00_000) + " Cr";
        } else if (result >= 1_00_000) {
            formattedNumber = result / 1_00_000 + " Lakh";
        } else {
            formattedNumber = "" + result;
        }

        return formattedNumber;*/

        DecimalFormat indianFormat = new DecimalFormat("#.##");

        // Format the number with Cr and Lakh
        String formattedNumber;

      /*  if (result >= 1_00_00_000) {
            formattedNumber = indianFormat.format(result / 1_00_00_000) + " Cr";
        } else if (result >= 1_00_000) {
            formattedNumber = indianFormat.format(result / 1_00_000) + " Lakh";
        } else {
            formattedNumber = indianFormat.format(result);
        }*/

        //todo comment by demand--
//        formattedNumber = indianFormat.format(result / 1_00_000) + " Lakh"; //todo comment on 31/01/2024


        if (input >= 10000000) { // If value is in crores
            double crore = input / 10000000;
            formattedNumber = String.format("%.2f Cr", crore);
        } else if (input >= 100000) { // If value is in lakhs
            double lakh = input / 100000;
            formattedNumber = String.format("%.2f Lakh", lakh);
        } else if (input >= 1000) { // If value is in thousands
            double thousand = input / 1000;
            formattedNumber = String.format("%.2f K", thousand);
        } else { // For values less than 1 lakh
            formattedNumber = String.format("%.0f", input);
        }

        return formattedNumber;

    }


    public static int removeDecimal(String input) {
        try {
            // Convert the string to a double
            double number = Double.parseDouble(input);

            // Format the double as an integer
            int integerValue = (int) number;

            // Convert the integer back to a string
            return integerValue;
        } catch (NumberFormatException e) {
            // Handle the case where the input is not a valid number
            return 0;
        }
    }

    public static double convertStringToDouble(String input) {
        try {
            // Convert the string to a double
            double number = Double.parseDouble(input);

            // Format the double as an integer
            int integerValue = (int) number;

            // Convert the integer back to a string
            return number;
        } catch (NumberFormatException e) {
            // Handle the case where the input is not a valid number
            return 0.0;
        }
    }


    public static Calendar yesterDayCal() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, -1); // ! clear would not reset the hour of day !
        return cal;
    }

    public static Calendar thisWeekCal() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, -7); // ! clear would not reset the hour of day !


        return cal;
    }

    public static Calendar thisMonthCal() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1); // ! clear would not reset the hour of day !


        return cal;
    }

    public static String firstDateOfMonth() {
        Calendar calendar = Calendar.getInstance();
        //Format the output as a string in the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.US);
        String currentMonthAndYear = dateFormat.format(calendar.getTime());

        int firstDay = Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH);
        String firstDayString;
        if (firstDay < 10) {
            firstDayString = "0" + firstDay;
        } else {
            firstDayString = "" + firstDay;
        }


        return String.valueOf("" + currentMonthAndYear + "-" + firstDayString);
    }


    public static String firstDateOfFinancialYear() {
        Calendar calendar = Calendar.getInstance();
        //  calendar.set(Calendar.YEAR,-1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        // Calculate financial year starting and ending dates
        String startDate;
        String endDate;

        if (month >= Calendar.APRIL) {
            // If the current month is April or later, the financial year starts from the current year
            startDate = year + "-04-01";
            endDate = (year + 1) + "-03-31";
        } else {
            // If the current month is before April, the financial year starts from the previous year
            startDate = (year - 1) + "-04-01";
            endDate = year + "-03-31";
        }

        // Format dates in "yyyy-MM-dd" format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStartDate = dateFormat.format(calendar.getTime());
        String formattedEndDate = dateFormat.format(calendar.getTime());
        // return "2023-03-31";
        return startDate;
    }

    public static String lastDateOfFinancialYear() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        // Calculate financial year starting and ending dates
        String startDate;
        String endDate;

        if (month >= Calendar.APRIL) {
            // If the current month is April or later, the financial year starts from the current year
            startDate = year + "-04-01";
            endDate = (year + 1) + "-03-31";
        } else {
            // If the current month is before April, the financial year starts from the previous year
            startDate = (year - 1) + "-04-01";
            endDate = year + "-03-31";
        }

        // Format dates in "yyyy-MM-dd" format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStartDate = dateFormat.format(calendar.getTime());
        String formattedEndDate = dateFormat.format(calendar.getTime());

        return endDate;
    }


    public static String thisWeekfirstDayOfMonth() {


// Create a Calendar instance and set the input month and year
        Calendar calendar = Calendar.getInstance();
        int inputMonth = calendar.get(Calendar.MONTH);  // May (month value is 4, since January is 0-based)
        int inputYear = calendar.get(Calendar.YEAR);
        ;
        calendar.set(Calendar.YEAR, inputYear);
        calendar.set(Calendar.MONTH, inputMonth);

// Get the first day of the week
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String firstDayOfWeek = dateFormat.format(calendar.getTime());

// Get the last day of the week
        calendar.add(Calendar.WEEK_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_WEEK, -1);
        String lastDayOfWeek = dateFormat.format(calendar.getTime());

        return String.valueOf(firstDayOfWeek);
    }


    public static String thisWeekLastDayOfMonth() {
        // Create a Calendar instance and set the input month and year
        Calendar calendar = Calendar.getInstance();
        int inputMonth = calendar.get(Calendar.MONTH);  // May (month value is 4, since January is 0-based)
        int inputYear = calendar.get(Calendar.YEAR);
        ;
        calendar.set(Calendar.YEAR, inputYear);
        calendar.set(Calendar.MONTH, inputMonth);

// Get the first day of the week
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String firstDayOfWeek = dateFormat.format(calendar.getTime());

// Get the last day of the week
        calendar.add(Calendar.WEEK_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_WEEK, -1);
        String lastDayOfWeek = dateFormat.format(calendar.getTime());

        return lastDayOfWeek;
    }

    public static String firstDayOFHalfYearly() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();

        // Get the current month
        int currentMonth = calendar.get(Calendar.MONTH);

        // Calculate the start and end dates of the half-year
        int startMonth, endMonth;
        if (currentMonth >= Calendar.APRIL && currentMonth <= Calendar.SEPTEMBER) {
            // Current or next half-year
            startMonth = Calendar.APRIL;
            endMonth = Calendar.SEPTEMBER;
        } else {
            // Previous half-year
            startMonth = Calendar.OCTOBER;
            endMonth = Calendar.MARCH;
        }

        // Set the calendar to the first day of the half-year
        calendar.set(Calendar.MONTH, startMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String startDate = formatDate(calendar.getTime());

        // Set the calendar to the last day of the half-year
        calendar.set(Calendar.MONTH, endMonth);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endDate = formatDate(calendar.getTime());

        // Print the start and end dates
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);

        return startDate;
    }

    public static String lastDayOFHalfYearly() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();

        // Get the current month
        int currentMonth = calendar.get(Calendar.MONTH);

        // Calculate the start and end dates of the half-year
        int startMonth, endMonth;
        if (currentMonth >= Calendar.APRIL && currentMonth <= Calendar.SEPTEMBER) {
            // Current or next half-year
            startMonth = Calendar.APRIL;
            endMonth = Calendar.SEPTEMBER;
        } else {
            // Previous half-year
            startMonth = Calendar.OCTOBER;
            endMonth = Calendar.MARCH;
        }

        // Set the calendar to the first day of the half-year
        calendar.set(Calendar.MONTH, startMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String startDate = formatDate(calendar.getTime());

        // Set the calendar to the last day of the half-year
        calendar.set(Calendar.MONTH, endMonth);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endDate = formatDate(calendar.getTime());

        // Print the start and end dates
        System.out.println("Start Date: " + startDate);
        System.out.println("End Date: " + endDate);

        return endDate;
    }


    public static String lastMonthFirstDate() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();

// Set the calendar to the first day of the current month
        calendar.set(Calendar.DAY_OF_MONTH, 1);

// Subtract one month to get to the previous month
        calendar.add(Calendar.MONTH, -1);

// Get the first day of the previous month
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String firstDayOfPreviousMonth = dateFormat.format(calendar.getTime());


// Get the last day of the previous month
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String lastDayOfPreviousMonth = dateFormat.format(calendar.getTime());

        return firstDayOfPreviousMonth;
    }


    public static String lastQuarterFirstDate() {
        Calendar calendar = Calendar.getInstance();

// Get the current quarter
        int currentQuarter = (calendar.get(Calendar.MONTH) / 3) + 1;

// Get the first day of the quarter
        calendar.set(Calendar.MONTH, (currentQuarter - 1) * 3);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String firstDayOfQuarter = dateFormat.format(calendar.getTime());

// Get the last day of the quarter
        calendar.add(Calendar.MONTH, 3);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String lastDayOfQuarter = dateFormat.format(calendar.getTime());

        return firstDayOfQuarter;
    }


    public static String lastQuarterlastDate() {
        Calendar calendar = Calendar.getInstance();

// Get the current quarter
        int currentQuarter = (calendar.get(Calendar.MONTH) / 3) + 1;

// Get the first day of the quarter
        calendar.set(Calendar.MONTH, (currentQuarter - 1) * 3);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String firstDayOfQuarter = dateFormat.format(calendar.getTime());

// Get the last day of the quarter
        calendar.add(Calendar.MONTH, 3);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String lastDayOfQuarter = dateFormat.format(calendar.getTime());

        return lastDayOfQuarter;
    }


    public static String thisYearFirstDate() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();

// Set the calendar to the first day of the current year
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

// Get the first day of the current year
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String firstDayOfYear = dateFormat.format(calendar.getTime());

// Set the calendar to the last day of the current year
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);

// Get the last day of the current year
        String lastDayOfYear = dateFormat.format(calendar.getTime());

        return firstDayOfYear;
    }


    public static String thisYearLastDate() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();

// Set the calendar to the first day of the current year
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

// Get the first day of the current year
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String firstDayOfYear = dateFormat.format(calendar.getTime());

// Set the calendar to the last day of the current year
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 31);

// Get the last day of the current year
        String lastDayOfYear = dateFormat.format(calendar.getTime());

        return lastDayOfYear;
    }


    public static String lastYearLastDate() {


        Calendar calendar = Calendar.getInstance();
        //  calendar.set(Calendar.YEAR,-1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int previousYear = year - 1;
        Log.e("TAG==========>", "lastYearLastDate: " + previousYear);
        // Calculate financial year starting and ending dates
        String startDate;
        String endDate;

        if (month >= Calendar.APRIL) {
            // If the current month is April or later, the financial year starts from the current year
            startDate = previousYear + "-04-01";
            endDate = (previousYear + 1) + "-03-31";
        } else {
            // If the current month is before April, the financial year starts from the previous year
            startDate = (previousYear - 1) + "-04-01";
            endDate = previousYear + "-03-31";
        }

        // Format dates in "yyyy-MM-dd" format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStartDate = dateFormat.format(calendar.getTime());
        String formattedEndDate = dateFormat.format(calendar.getTime());
        // return "2023-03-31";
        return endDate;

    }


    public static String lastYearFirstDate() {



        Calendar calendar = Calendar.getInstance();
        //  calendar.set(Calendar.YEAR,-1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int previousYear = year - 1;

        // Calculate financial year starting and ending dates
        String startDate;
        String endDate;

        if (month >= Calendar.APRIL) {
            // If the current month is April or later, the financial year starts from the current year
            startDate = previousYear + "-04-01";
            endDate = (previousYear + 1) + "-03-31";
        } else {
            // If the current month is before April, the financial year starts from the previous year
            startDate = (previousYear - 1) + "-04-01";
            endDate = previousYear + "-03-31";
        }

        // Format dates in "yyyy-MM-dd" format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedStartDate = dateFormat.format(calendar.getTime());
        String formattedEndDate = dateFormat.format(calendar.getTime());
        // return "2023-03-31";
        return startDate;
    }


    public static String getCurrentDateInLastFinancialYear() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        int previousYear = currentYear - 1;

        // Check if the current date is in the current financial year (starting April 1st)
        if (currentMonth >= Calendar.APRIL) {
            // If it's April or later, the previous financial year started in the previous year
            calendar.set(Calendar.YEAR, previousYear);
        } else {
            // If it's before April, the previous financial year started two years ago
            calendar.set(Calendar.YEAR, previousYear - 1);
        }

        // Set the date to today's month and day but for the last financial year
        calendar.set(Calendar.MONTH, currentMonth);
        calendar.set(Calendar.DAY_OF_MONTH, currentDay);

        // Format the date as "yyyy-MM-dd"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }


    private static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return dateFormat.format(date);
    }


    public static String lastMonthlastDate() {
// Get the current date
        Calendar calendar = Calendar.getInstance();

// Set the calendar to the first day of the current month
        calendar.set(Calendar.DAY_OF_MONTH, 1);

// Subtract one month to get to the previous month
        calendar.add(Calendar.MONTH, -1);

// Get the first day of the previous month
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String firstDayOfPreviousMonth = dateFormat.format(calendar.getTime());

// Get the last day of the previous month
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String lastDayOfPreviousMonth = dateFormat.format(calendar.getTime());

        return lastDayOfPreviousMonth;
    }


    public static String lastDateOfMonth() {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH); // returns 0-based index (i.e., January is 0)
        int currentYear = calendar.get(Calendar.YEAR);

// Format the output as a string in the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.US);
        String currentMonthAndYear = dateFormat.format(calendar.getTime());

        return String.valueOf("" + currentMonthAndYear + "-" + Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
    }


    public static Calendar lastMonthCal() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -2); // ! clear would not reset the hour of day !


        return cal;
    }

    public static Calendar thisQuarterCal() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -3); // ! clear would not reset the hour of day !


        return cal;
    }

    public static Calendar thisyearCal() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1); // ! clear would not reset the hour of day !


        return cal;
    }

    public static Calendar lastyearCal() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -2); // ! clear would not reset the hour of day !


        return cal;
    }


    /***shubh****/
    public static void showBottomSheetDialog(Context context, LayoutInflater layoutInflater, String pdfUrl, WebView dialogWebView, FragmentActivity activity) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait");


        BottomSheetDialogShareReportBinding binding;
        binding = BottomSheetDialogShareReportBinding.inflate(layoutInflater);
        bottomSheetDialog.setContentView(binding.getRoot());

        progressDialog.dismiss();
        setUpWebViewDialog(binding.webViewBottomSheetDialog, pdfUrl, false, binding.loader, binding.linearWhatsappShare, binding.linearGmailShare, binding.linearOtherShare);


        bottomSheetDialog.show();

        binding.headingBottomSheetShareReport.setText(R.string.share_customer_list);

        binding.ivCloseBottomSheet.setOnClickListener(view -> {
            progressDialog.dismiss();
            bottomSheetDialog.dismiss();
        });
        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                progressDialog.dismiss();
            }
        });


        binding.linearWhatsappShare.setOnClickListener(view ->
        {
            String f_name = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));

            lab_pdf(dialogWebView, f_name, context, activity);

        });


        binding.linearOtherShare.setOnClickListener(view ->
                {
                    String f_name = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));
                    lab_other_pdf(dialogWebView, f_name, context, activity);

                }
        );
        binding.linearGmailShare.setOnClickListener(view -> {

                    String f_name = String.format("%s.pdf", new SimpleDateFormat("dd_MM_yyyyHH_mm_ss", Locale.US).format(new Date()));
                    lab_gmail_pdf(dialogWebView, f_name, activity, context);
                }
        );

    }

    /***shubh****/
    public static void lab_gmail_pdf(WebView webView, String f_name, Activity activity, Context context) {
        //  String path = Environment.getExternalStorageDirectory().getPath()+"/hana/";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/";
        File f = new File(path);
        final String fileName = f_name;


        PdfView.createWebPrintJob(activity, webView, f, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                // progressDialog.dismiss();
                gmailShare(fileName, context, activity);
                //PdfView.openPdfFile(Pdf_Test.this,getString(R.string.app_name),"Do you want to open the pdf file?"+fileName,path);
            }

            @Override
            public void failure() {
                //progressDialog.dismiss();

            }
        });
    }

    /***shubh****/
    public static void gmailShare(String fName, Context context, Activity activity) {

        String stringFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/" + "/" + fName;
        File file = new File(stringFile);
        Uri apkURI = FileProvider.getUriForFile(
                context,
                activity.getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(context, "File Not exist", Toast.LENGTH_SHORT).show();

        }
        //    Uri path = Uri.fromFile(file);
        //  Log.e("Path==>", path.toString());
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, apkURI);

        // share.setData(Uri.parse("mailto:" + recipientEmail));


        share.setPackage("com.google.android.gm");
        activity.startActivity(share);


    }


    /***shubh****/
    public static void lab_other_pdf(WebView webView, String f_name, Context context, FragmentActivity activity) {
        //  String path = Environment.getExternalStorageDirectory().getPath()+"/hana/";
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/";
        File f = new File(path);
        final String fileName = f_name;

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        PdfView.createWebPrintJob(activity, webView, f, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                progressDialog.dismiss();
                otherShare(fileName, context, activity);
                //PdfView.openPdfFile(Pdf_Test.this,getString(R.string.app_name),"Do you want to open the pdf file?"+fileName,path);
            }

            @Override
            public void failure() {
                progressDialog.dismiss();

            }
        });
    }


    /***shubh****/
    public static void whatsappShare(String fName, Context context) {
        String stringFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/" + "/" + fName;
        File file = new File(stringFile);
        Uri apkURI = null;
        try {
            apkURI = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".FileProvider", file);
        } catch (Exception e) {
            Log.e("whatsapp", "showBottomSheetDialog: ");
            e.printStackTrace();
        }


        if (!file.exists()) {
            Toast.makeText(context, "File Not exist", Toast.LENGTH_SHORT).show();

        }
        //    Uri path = Uri.fromFile(file);
        //  Log.e("Path==>", path.toString());

        try {
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, apkURI);
            if (isAppInstalledActivity("com.whatsapp"))
                share.setPackage("com.whatsapp");
            else if (isAppInstalledActivity("com.whatsapp.w4b"))
                share.setPackage("com.whatsapp.w4b");

            context.startActivity(share);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, " WhatsApp is not currently installed on your phone.", Toast.LENGTH_LONG).show();
        }

//        if (share.resolveActivity(requireActivity().getPackageManager()) != null) {
//            //startActivity(Intent.createChooser(intent, "Share PDF using"));
//            startActivity(share);
//        } else {
//            startActivity(Intent.createChooser(share, "Share PDF using"));
//        }

    }


    /***shubh****/
    public static void otherShare(String fName, Context context, FragmentActivity activity) {

        String stringFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/" + "/" + fName;
        File file = new File(stringFile);
        Uri apkURI = FileProvider.getUriForFile(
                context,
                activity
                        .getPackageName() + ".FileProvider", file);


        if (!file.exists()) {
            Toast.makeText(context, "File Not exist", Toast.LENGTH_SHORT).show();

        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, apkURI);


        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            context.startActivity(Intent.createChooser(intent, "Share PDF using"));
        }
    }

    /***shubh****/
    public static void lab_pdf(WebView webView, String f_name, Context context, FragmentActivity activity) {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/hana/";
        File f = new File(path);
        //        try {
        //            if (!f.getParentFile().exists())
        //                f.getParentFile().mkdirs();
        //            if (!f.exists())
        //                f.createNewFile();
        //        } catch (IOException e) {
        //            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        //        }
        final String fileName = f_name;


        PdfView.createWebPrintJob(activity, webView, f, fileName, new PdfView.Callback() {

            @Override
            public void success(String path) {
                //  progressDialog.dismiss();
                whatsappShare(fileName, context);
                //PdfView.openPdfFile(Pdf_Test.this,getString(R.string.app_name),"Do you want to open the pdf file?"+fileName,path);
            }

            @Override
            public void failure() {
                // progressDialog.dismiss();

            }
        });
    }


    /***shubh****/
    public static void setUpWebViewDialog(WebView webView, String url, Boolean isZoomAvailable, ProgressBar dialog, LinearLayout whatsapp, LinearLayout gmail, LinearLayout other) {

        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        webView.getSettings().setBuiltInZoomControls(isZoomAvailable);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        // webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        // Setting we View Client

        whatsapp.setEnabled(false);
        gmail.setEnabled(false);
        other.setEnabled(false);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap btm) {
                super.onPageStarted(view, url, null);
                dialog.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // initializing the printWeb Object
                dialog.setVisibility(View.GONE);
                // parentWebview = webView;
                whatsapp.setEnabled(true);
                gmail.setEnabled(true);
                other.setEnabled(true);

            }
        });


        webView.loadUrl(url);
    }


    public static boolean isAppInstalled(String packageName, FragmentActivity activity) {
        try {
            activity.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
            return false;
        }
    }


    public static boolean isAppInstalledActivity(String packageName) {
//        try {
//            getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
//            return true;
//        }
//        catch (PackageManager.NameNotFoundException ignored) {
//            return false;
//        }
        return false;
    }


    public static void showCustomerBottomSheetDialog(Context context, String title, String groupName, String creditLimit, String creditDate, String mobile, String address, String email, LayoutInflater layoutInflater, String gstIn, String contactPersonName) {
        BottomSheetDialogShowCustomerDataBinding binding;
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        binding = BottomSheetDialogShowCustomerDataBinding.inflate(layoutInflater);
        bottomSheetDialog.setContentView(binding.getRoot());
        binding.ivCloseBottomSheet.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
        });
        binding.tvCustomerNameBottomSheetDialog.setText(title);
        /***shubh****/
        binding.etGroupName.setText(groupName);
        binding.etCreditLimit.setText(numberToK(creditLimit));
        binding.etCreditDate.setText(creditDate);
        binding.etEmail.setText(email);
        binding.etMobileNumber.setText(mobile);
        binding.etAddress.setText(address);
        binding.etCstNumber.setText(gstIn);
        binding.etContactName.setText(contactPersonName);
        if (!bottomSheetDialog.isShowing())
            bottomSheetDialog.show();

    }

    public static String reversedString(String date) {
        String str = "Geeks", nstr = "";
        char ch;

        System.out.print("Original word: ");
        System.out.println("Geeks"); //Example word

        for (int i = 0; i < date.length(); i++) {
            ch = date.charAt(i); //extracts each character
            nstr = ch + nstr; //adds each character in front of the existing string
        }
        return nstr;
        //System.out.println("Reversed word: "+ nstr);
    }

    public static String convertDateFormat(String dateString) {

        String convertedDate = "";

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd");
            Date date = inputFormat.parse(dateString);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-mm-yyyy");
            convertedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate;
    }

    public static String payLoadOrderByName = "OrderByName";
    public static String payLoadOrderByAMt = "OrderByAmt";
    public static String payLoadDueDaysGroup = "DueDaysGroup";
    public static String payLoadFilter = "Filter";
    public static String payLoadCode = "Code";
    public static String ATOZ = "a-z";
    public static String ZTOA = "z-a";
    public static String ASC = "asc";
    public static String DESC = "desc";

    public static void setUpDateTextView(String startDate, String endDate, boolean shouldShowSimpleText, String simpleText, TextView dateTextview) {
        if (shouldShowSimpleText) {
            dateTextview.setText(simpleText);
        } else {
            dateTextview.setText(startDate + " to " + endDate);
        }


    }


    public static String allCustomerPdfUrlPurchase = "" + PDFURL + "PartList.html?SalesPersonCode=";

    public static String perticularPurchaseCreditNote = "" + PDFURL + "purchaseinvoices.html?";

    //todo pdf
    public static String receiptAllPdfUlPurchase = "" + PDFURL + "purchase/ReceiptReportSharing.html?Type=Gross&";

    //todo pdf
    public static String overAllReceivablePurchase = "" + PDFURL + "purchase/OverAllReceivable.html?Type=Gross&";

    //todo pdf
    public static String overAllLedgerPurchase = "" + PDFURL + "purchase/OverAllLedger.html?";

    //todo pdf
    public static String overAllPaymentCollectionOverDuePurchase = "" + PDFURL + "purchase/PaymentcollectionAll.html?";

    //todo pdf
    public static String perticularBpPaymentCollectionPurchse = "" + PDFURL + "purchase/PaymentcollectionOne.html?";

    //todo pdf
    public static String particularBpRceiptPurchase = "" + PDFURL + "purchase/PerticularBPReceipt.html?Type=Gross&";

    //todo pdf
    public static String saleReportsPurchasePdf = "" + PDFURL + "purchase/SalesReportSharing.html?";

    //todo pdf
    public static String saleGroupReportsPurchasePdf = "" + PDFURL + "purchase/filter_sale.html?";

    //todo pdf
    public static String GroupStockReportsPurchasePdf = "" + PDFURL + "purchase/stockgroup.html?";

    //todo pdf
    public static String particularBpPurchase = "" + PDFURL + "purchase/PerticularBPSharing.html?";

    //todo pdf
    public static String itemParticularBpPurchase = "" + PDFURL + "purchase/ItemPerticularBPSharing.html?";

    //todo pdf
    public static String orderOnePdfUrPurchase = "" + PDFURL + "purchase/PendingOrder.html?";

    //todo pdf
    public static String receiptVoucherPurchasePdf = "" + PDFURL + "purchase/ReceiptVoucher.html?";

    //todo pdf
    public static String receivableParticularpurchase = "" + PDFURL + "ReceivablePerticular.html?";


}
