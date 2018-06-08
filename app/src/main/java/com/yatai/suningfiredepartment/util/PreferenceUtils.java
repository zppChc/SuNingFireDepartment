package com.yatai.suningfiredepartment.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

public class PreferenceUtils {
    public static String getPerfString(Context context, String key, final  String defaultValue){
        final SharedPreferences settings = context.getSharedPreferences("yatai",Context.MODE_PRIVATE);
        return settings.getString(key,defaultValue);
    }

    public static void setPrefString(Context context, final String key, final String value){
        final SharedPreferences settings = context.getSharedPreferences("yatai",Context.MODE_PRIVATE);
        settings.edit().putString(key,value).apply();
    }

    public static void setPrefInt(Context context, final String key, final int value){
        final SharedPreferences settings = context.getSharedPreferences("yatai",Context.MODE_PRIVATE);
        settings.edit().putInt(key,value).apply();
    }

    public static int getPrefInt(Context context, final String key, final int defaultVaule){
        final SharedPreferences settings = context.getSharedPreferences("yatai",Context.MODE_PRIVATE);
        return settings.getInt(key,defaultVaule);
    }

    public static void setPrefFloat(Context context, final String key , final float value){
        final SharedPreferences settings = context.getSharedPreferences("yatai",Context.MODE_PRIVATE);
        settings.edit().putFloat(key,value).apply();
    }

    public static float getPrefFloat(Context context, final String key , final float defaultValue){
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return  settings.getFloat(key,defaultValue);
    }

    public static void clearPreference(Context  context){
        final SharedPreferences settings = context.getSharedPreferences("yatai",Context.MODE_PRIVATE);
        final  SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
    }
}
