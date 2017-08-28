package com.example.andyk.mageapp20170828.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.andyk.mageapp20170828.catalog.Category;

/**
 * Created by foo on 6/11/17.
 */

public class SharedPref {

    protected static final String PREFS_CATEGORY = "prefsCategory";
    protected static final String PREFS_LAST_ORDER_NUMBER = "lastOrderNumber";
    protected static final String PREFS_IS_PURCHASE_SERVICE_ON = "isPurchaseServiceOn";

    protected static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void putCategory(Context c, Category cat) {
        getSharedPreferences(c).edit()
                .putString(PREFS_CATEGORY, cat.toJson())
                .apply();
    }

    public static Category getCategory(Context c) {
        String catJson = getSharedPreferences(c).getString(PREFS_CATEGORY, null);
        return Category.fromJson(catJson);
    }

    public static void putLastOrderNumber(Context c, String num) {
        getSharedPreferences(c).edit()
                .putString(PREFS_LAST_ORDER_NUMBER, num)
                .apply();
    }

    public static String getLastOrderNumber(Context c) {
        return getSharedPreferences(c).getString(PREFS_LAST_ORDER_NUMBER, null);
    }

    public static void putIsPurchseServiceOn(Context c, boolean isOn) {
        getSharedPreferences(c).edit()
                .putBoolean(PREFS_IS_PURCHASE_SERVICE_ON, isOn)
                .apply();
    }

    public static boolean getIsPurchaseServiceOn(Context c) {
        return getSharedPreferences(c).getBoolean(PREFS_IS_PURCHASE_SERVICE_ON, false);
    }
}
