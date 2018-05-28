package com.yatai.suningfiredepartment.di.components;

import com.yatai.suningfiredepartment.di.modules.HomePageModule;
import com.yatai.suningfiredepartment.di.scopes.UserScope;
import com.yatai.suningfiredepartment.view.fragment.HomePageFragment;

import dagger.Component;

/**
 * @author : CHC
 * DATE :   9:15
 * Discription:
 **/
@UserScope
@Component(modules = HomePageModule.class, dependencies = NetComponent.class)
public interface HomePageComponent {
    void inject(HomePageFragment fragment);
}
