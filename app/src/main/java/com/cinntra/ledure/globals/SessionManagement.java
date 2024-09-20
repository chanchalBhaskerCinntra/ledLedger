package com.cinntra.ledure.globals;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {

    private static final String PREF_NAME = "AndroidHivePref";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int PRIVATE_MODE = 0;

    public SessionManagement(Context _context) {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setSharedPrefernce(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    private String getDataFromSharedPreferences(String key) {
        try {
            return pref.getString(key, "");
        } catch (Exception e) {
            return "";
        }
    }

    public void ClearSession() {
        editor.clear();
        editor.commit();
    }

    public void setMPINValue(String mpinValue) {
        if (mpinValue != null) {
            setSharedPrefernce("mpinValue", mpinValue);
        }
    }

    public String getMPINValue() {
        return getDataFromSharedPreferences("mpinValue");
    }

    public void setMobileNo(String mobile) {
        if (mobile != null) {
            setSharedPrefernce("mobile", mobile);
        }
    }

    public String getMobileNO() {
        return getDataFromSharedPreferences("mobile");
    }

    public void setCardCode(String cardCode) {
        if (cardCode != null) {
            setSharedPrefernce("_cardCode", cardCode);
        }
    }

    public String getCardCode() {
        return getDataFromSharedPreferences("_cardCode");
    }

    public void setCardName(String cardName) {
        if (cardName != null) {
            setSharedPrefernce("card_name", cardName);
        }
    }

    public String getCardName() {
        return getDataFromSharedPreferences("card_name");
    }

    public void setDistributorID(String distributor_id) {
        if (distributor_id != null) {
            setSharedPrefernce("_distributor_id", distributor_id);
        }
    }

    public String getDistributorID() {
        return getDataFromSharedPreferences("_distributor_id");
    }

    public void setFromWhere(String fromWhere) {
        if (fromWhere != null) {
            setSharedPrefernce("fromWhere", fromWhere);
        }
    }

    public String getFromWhere() {
        return getDataFromSharedPreferences("fromWhere");
    }

    public void setToken(String token) {
        if (token != null) {
            setSharedPrefernce("token", token);
        }
    }

    public String getToken() {
        return getDataFromSharedPreferences("token");
    }

    public void setSalesEmployeeCode(String salesEmployeeCode) {
        if (salesEmployeeCode != null) {
            setSharedPrefernce("salesEmployeeCode", salesEmployeeCode);
        }
    }

    public String getSalesEmployeeCode() {
        return getDataFromSharedPreferences("salesEmployeeCode");
    }
}
