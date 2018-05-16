package com.yatai.suningfiredepartment.presenter;

import com.orhanobut.logger.Logger;
import com.yatai.suningfiredepartment.model.api.ApiService;

import javax.inject.Inject;

/**
 *Author: CHC
 * Date:
 * Description:
 **/
public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View mView;
    private ApiService mApiService;

    @Inject
    public SplashPresenter(SplashContract.View view, ApiService apiService){
        this.mView = view;
        this.mApiService = apiService;
        Logger.d("app Splash :" + apiService);
    }


    @Override
    public void getSplash(String deviceId) {
        String client = "android";
        String version = "";

    }
}
