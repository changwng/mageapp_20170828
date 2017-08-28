package com.example.foo.mageapp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import com.example.foo.mageapp.catalog.Category;

import java.util.StringTokenizer;
import java.util.prefs.Preferences;

/**
 * Created by foo on 6/11/17.
 */

public class SharedPref {

    protected static final String PREFS_CATEGORY = "prefsCategory";
    protected static final String PREFS_LAST_ORDER_ID = "lastOrderId";


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

    public static void putLastOrderId(Context c, String id) {
        getSharedPreferences(c).edit()
                .putString(PREFS_LAST_ORDER_ID, id)
                .apply();
    }

    public static String getLastOrderId(Context c) {
        return getSharedPreferences(c).getString(PREFS_LAST_ORDER_ID, null);
    }
}
