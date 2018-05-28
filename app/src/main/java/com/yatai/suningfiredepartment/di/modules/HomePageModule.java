package com.yatai.suningfiredepartment.di.modules;

import com.yatai.suningfiredepartment.presenter.HomePageContract;

import dagger.Module;
import dagger.Provides;

/**
 * @author : CHC
 * Date : 2018/5/17 9:17
 * Discription:
 **/
@Module
public class HomePageModule {
    private HomePageContract.View mView;

    public HomePageModule(HomePageContract.View view){
        this.mView = view;
    }

    @Provides
    public HomePageContract.View provideView(){
        return mView;
    }
}
