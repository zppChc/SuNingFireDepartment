package com.yatai.suningfiredepartment.presenter;

import com.orhanobut.logger.Logger;
import com.yatai.suningfiredepartment.model.api.ApiService;
import com.yatai.suningfiredepartment.model.entity.LoginEntity;
import com.yatai.suningfiredepartment.util.PreferenceUtils;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by CHC
 * DATE 2018/8/3
 * Login
 */
public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View mView;
    private ApiService apiService;
    private LoginEntity entity;

    @Inject
    public LoginPresenter(LoginContract.View view, ApiService apiService){
        this.mView = view;
        this.apiService=apiService;
    }

    @Override
    public void login(String mobile, String password) {
        apiService.postLogin(mobile, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginEntity>() {
                    Disposable d;
                    @Override
                    public void onSubscribe(Disposable d) {
                        this.d = d;
                    }

                    @Override
                    public void onNext(LoginEntity loginEntity) {
                        entity = loginEntity;
                        Logger.d("On Next: " + loginEntity.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.loginFailed();
                        Logger.d("error"+e.toString());
                        d.dispose();
                    }

                    @Override
                    public void onComplete() {
                        mView.loginSuccess(entity.getData().getToken());
                        Logger.d("MAIN OnComplete：" );
                        d.dispose();
                    }
                });
    }
}
