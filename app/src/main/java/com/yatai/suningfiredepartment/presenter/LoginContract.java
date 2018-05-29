package com.yatai.suningfiredepartment.presenter;

/**
 * chc
 * 登陆页面
 * 2018.5.29
 */
public interface LoginContract {
    interface Presenter{
        void login(String mobile, String password);
    }

    interface View{
        void loginFailed();
        void loginSuccess(String token);
    }
}
