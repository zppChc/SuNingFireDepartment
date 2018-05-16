package com.yatai.suningfiredepartment.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

public class PreferenceUtils {
    public static String getPerfString(Context context, String key, final  String defaultValue){
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(key,defaultValue);
    }

    public static void setPrefString(Context context, final String key, final String value){
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context);
        settings.edit().putString(key,value).apply();
    }

    public static boolean getPrefBoolean(Context context, final String key, final Boolean defaultValue){
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(key,defaultValue);
    }

    public static boolean hasKey(Context context, final String key){
        return  PreferenceManager.getDefaultSharedPreferences(context).contains(key);
    }

    public static void setPrefBoolean(Context context, final String key,
                                      final  boolean value){
        final SharedPreferences settings= PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putBoolean(key,value).apply();
    }

    public static void setPrefInt(Context context, final String key, final int value){
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putInt(key,value).apply();
    }

    public static int getPrefInt(Context context, final String key, final int defaultVaule){
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt(key,defaultVaule);
    }

    public static void setPrefFloat(Context context, final String key , final float value){
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putFloat(key,value);
    }

    public static float getPrefFloat(Context context, final String key , final float defaultValue){
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return  settings.getFloat(key,defaultValue);
    }

    public static void clearPreference(Context  context, final SharedPreferences p){
        final  SharedPreferences.Editor editor = p.edit();
        editor.clear();
        editor.apply();
    }
}
