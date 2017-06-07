package com.app.justclap.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ratnadeep on 5/12/15.
 */
public class PrefUtils {





   public static final String s_Home="_session_";
    public static final String s_vendor_id="s_vendor_id_";
    public static final String s_v_image="v_image";
    public static final String s_v_fname="v_fname";
    public static final String s_v_lname="v_lname";
    public static final String s_v_contact_no="v_contact_no";

    public static void saveToPrefs(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key,value);
        editor.commit();
    }


    public static String getFromPrefs(Context context, String key, String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {

            return sharedPrefs.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }




}
