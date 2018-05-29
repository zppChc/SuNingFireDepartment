package com.yatai.suningfiredepartment.di.modules;

import com.yatai.suningfiredepartment.BuildConfig;
import com.yatai.suningfiredepartment.model.api.ApiService;
import com.yatai.suningfiredepartment.model.api.StringConverterFactory;
import com.yatai.suningfiredepartment.model.util.EntityUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: CHC
 * Date: 2018/5/9  10:08
 * Description:
 **/
@Module
public class NetModule {
    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(){
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG?HttpLoggingInterceptor.Level.BODY:HttpLoggingInterceptor.Level.NONE);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();
        return okHttpClient;
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient okHttpClient){
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://grid.lenong365.com/api/")
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(EntityUtils.gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return  retrofit;
    }

    @Provides
    @Singleton
    public ApiService provideApiService(Retrofit retrofit){
        return retrofit.create(ApiService.class);
    }
}
