package com.example.foo.mageapp.helper;

import android.content.Context;

import com.example.foo.mageapp.catalog.Category;

/**
 * Created by foo on 6/11/17.
 */

public class Preference {

    protected static final String PREFS_CATEGORY = "prefsCategory";

    public static void saveCategory(Context c, Category cat) {
        c.getSharedPreferences(PREFS_CATEGORY, 0).edit()
            .putString(PREFS_CATEGORY, cat.toJson())
            .apply();
    }

    public static Category getCategory(Context c) {
        String catJson = c.getSharedPreferences(PREFS_CATEGORY, 0)
                .getString(PREFS_CATEGORY, null);
        return Category.fromJson(catJson);
    }
}
