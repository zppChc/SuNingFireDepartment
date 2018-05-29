package com.yatai.suningfiredepartment.di.modules;


import com.yatai.suningfiredepartment.presenter.LoginContract;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {
    private LoginContract.View mView;

    public LoginModule(LoginContract.View view){
        this.mView = view;
    }

    @Provides
    public LoginContract.View provideView(){
        return mView;
    }
}
