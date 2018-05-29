package com.yatai.suningfiredepartment.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.app.SuNingFireDepartmentApplication;
import com.yatai.suningfiredepartment.di.components.DaggerLoginComponent;
import com.yatai.suningfiredepartment.di.modules.LoginModule;
import com.yatai.suningfiredepartment.presenter.LoginContract;
import com.yatai.suningfiredepartment.presenter.LoginPresenter;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.util.Validator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @BindView(R.id.login_user_name_ed)
    EditText userNameEd;
    @BindView(R.id.login_password_ed)
    EditText passwordEd;
    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.login_use_message)
    TextView useMessage;
    @BindView(R.id.login_forgot_password)
    TextView forgotPassword;

    @Inject
    LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(LoginActivity.this);
        initPresenter();
    }

    private void initPresenter(){
        DaggerLoginComponent.builder()
                .loginModule(new LoginModule(this))
                .netComponent(SuNingFireDepartmentApplication.get(this).getNetComponent())
                .build()
                .inject(this);
    }

    @OnClick({R.id.login_button, R.id.login_use_message, R.id.login_forgot_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                String mobile = userNameEd.getText().toString();
                String password = passwordEd.getText().toString();
                if (Validator.validUsername(mobile) && Validator.validPassword(password)){
                    mPresenter.login(mobile,password);
                }else{
                    ToastUtil.show(this,"请输入手机号和密码");
                }
                break;
            case R.id.login_use_message:
                break;
            case R.id.login_forgot_password:
                break;
        }
    }

    @Override
    public void loginFailed() {
        ToastUtil.show(this,"手机号或密码错误，请重新输入");
    }

    @Override
    public void loginSuccess(String token) {
        PreferenceUtils.setPrefString(this,"token",token);
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
