package com.yatai.suningfiredepartment.model.util;

import com.yatai.suningfiredepartment.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Author: CHC
 * Date: 2018/5/9  9:27
 * Description:
 **/
public class HttpUtils {
    private HttpUtils(){}

    public static final OkHttpClient  client  = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20,TimeUnit.SECONDS)
            .addInterceptor(createHttpLoggingInterceptor())
            .build();
    private static HttpLoggingInterceptor createHttpLoggingInterceptor(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG?HttpLoggingInterceptor.Level.BODY:HttpLoggingInterceptor.Level.NONE);
        return  loggingInterceptor;
    }
}
