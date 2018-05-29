package com.yatai.suningfiredepartment.di.components;

import com.yatai.suningfiredepartment.di.modules.LoginModule;
import com.yatai.suningfiredepartment.di.scopes.UserScope;
import com.yatai.suningfiredepartment.view.activity.LoginActivity;

import dagger.Component;

@UserScope
@Component(modules = LoginModule.class,dependencies = NetComponent.class)
public interface LoginComponent {
    void inject(LoginActivity loginActivity);
}
