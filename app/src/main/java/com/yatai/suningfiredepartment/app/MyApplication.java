package com.yatai.suningfiredepartment.app;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.umeng.commonsdk.UMConfigure;
import com.yatai.suningfiredepartment.util.UmengUtil;

public class MyApplication extends Application {

    private static MyApplication instance;
    private static Context sContext;

    public static MyApplication get(Context context){
        return (MyApplication)context.getApplicationContext();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sContext=getApplicationContext();
        initUM();
        initLogger();
    }

    private void initUM(){
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
        UmengUtil.initUmeng();
    }
    private void initLogger(){
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static MyApplication getInstance(){
        return  instance;
    }

    public static Context getContext() {
        return sContext;
    }
}
