package com.jc.bookbrowser.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by HaohaoChang on 2016/10/24.
 */

public class SharedPrefUtil {

    private SharedPrefUtil() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    private static SharedPreferences getSharedPreference() {
        return PreferenceManager.getDefaultSharedPreferences(UIHelper.getContext());
    }

    public static void putString(String key, String value) {

        getSharedPreference().edit().putString(key, value).apply();

    }

    public static String getString(String key, String defValue) {
        return getSharedPreference().getString(key, defValue);
    }

    public static void putInt(String key, int value) {
        getSharedPreference().edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defValue) {
        return getSharedPreference().getInt(key, defValue);
    }

    public static void putBoolean(String key, boolean value) {
        getSharedPreference().edit().putBoolean(key, value).apply();

    }

    public static boolean getrBoolean(String key , boolean defValue) {
        return getSharedPreference().getBoolean(key, defValue);
    }

    public static boolean hasKey(String key) {
        return getSharedPreference().contains(key);
    }

    public static void clearPreference() {
        getSharedPreference().edit().clear().apply();
    }


}

