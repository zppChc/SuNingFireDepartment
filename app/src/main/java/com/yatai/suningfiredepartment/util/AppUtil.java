package com.yatai.suningfiredepartment.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

/**
 * Created by CHC
 * DATE 2018/7/21
 * 肃宁 消防
 */
public class AppUtil {
    /**
     * 获取APP 版本名
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context){
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(),0);
            return  pi.versionName;
        } catch (PackageManager.NameNotFoundException mE) {
            mE.printStackTrace();
        }
        return  "";
    }

    /**
     * 获取应用程序版本名称信息
     * @return
     */
    public static String getVersionName(Context context){
        try{
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取APP版本号
     */
    public static int getAppVersionCode(Context context){
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try{
            pi = pm.getPackageInfo(context.getPackageName(),0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
    *获取设备ID
     */
    public static String getDeviceId(Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return  tm.getDeviceSoftwareVersion();
    }

    /**
     * dp to px
     * @param paramContext
     * @param paramFloat
     * @return
     */
    public static int dp2px(Context paramContext, float paramFloat){
        float scale = paramContext.getResources().getDisplayMetrics().density;
        return (int)(0.5F+paramFloat*scale);
    }

}
