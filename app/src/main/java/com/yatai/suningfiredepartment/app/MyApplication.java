package com.yatai.suningfiredepartment.app;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.yatai.suningfiredepartment.di.components.DaggerNetComponent;
import com.yatai.suningfiredepartment.di.components.NetComponent;
import com.yatai.suningfiredepartment.di.modules.NetModule;

public class MyApplication extends Application {

    private static MyApplication instance;
    private static Context sContext;

    public static MyApplication get(Context context){
        return (MyApplication)context.getApplicationContext();
    }

    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        sContext=getApplicationContext();
        initNet();
        initLogger();
    }

    private void initNet(){
        mNetComponent= DaggerNetComponent.builder()
                .netModule(new NetModule())
                .build();
    }

    private void initLogger(){
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public static MyApplication getInstance(){
        return  instance;
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }

    public static Context getContext() {
        return sContext;
    }
}
