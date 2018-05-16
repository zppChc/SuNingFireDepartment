package com.yatai.suningfiredepartment.di.components;

import com.yatai.suningfiredepartment.di.modules.SplashModule;
import com.yatai.suningfiredepartment.di.scopes.UserScope;
import com.yatai.suningfiredepartment.view.activity.SplashActivity;

import dagger.Component;

/**
 * Author: CHC
 * Date: 2018/5/9  10:31
 * Description:
 **/
@UserScope
@Component(modules = SplashModule.class, dependencies = NetComponent.class)
public interface SplashComponent {
    void inject(SplashActivity splashActivity);
}
