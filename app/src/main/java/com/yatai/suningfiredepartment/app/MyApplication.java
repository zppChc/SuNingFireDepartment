package com.yatai.suningfiredepartment.app;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

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
        initLogger();
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
