package com.yatai.suningfiredepartment.presenter;

import com.yatai.suningfiredepartment.model.api.ApiService;

import javax.inject.Inject;

/**
 * @author : CHC
 * Date :  9:07
 * Discription:
 **/
public class HomePagePresenter implements HomePageContract.Presenter {
    private HomePageContract.View mView;
    private ApiService mApiService;
    @Inject
    public HomePagePresenter(HomePageContract.View view, ApiService apiService){
        this.mApiService = apiService;
        this.mView = view;
    }

    @Override
    public void getNews() {

    }

    @Override
    public void getLat() {

    }

    @Override
    public void getGridInfo() {

    }
}
