package com.yatai.suningfiredepartment.di.modules;

import com.yatai.suningfiredepartment.presenter.SplashContract;

import dagger.Module;
import dagger.Provides;

/**
 * Author: CHC
 * Date: 2018/5/9  10:38
 * Description:
 **/
@Module
public class SplashModule {
    private SplashContract.View view;
    public SplashModule(SplashContract.View view){
        this.view = view;
    }

    @Provides
    public SplashContract.View provideView(){
        return  view;
    }
}
