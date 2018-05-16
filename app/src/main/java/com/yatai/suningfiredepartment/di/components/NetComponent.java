package com.yatai.suningfiredepartment.di.components;

import com.yatai.suningfiredepartment.di.modules.NetModule;
import com.yatai.suningfiredepartment.model.api.ApiService;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by CHC
 * Date 2018/5/8
 */
@Component(modules = NetModule.class)
@Singleton
public interface NetComponent {
    ApiService getApiService();
    OkHttpClient getOkHttp();
    Retrofit getRetrofit();
}
