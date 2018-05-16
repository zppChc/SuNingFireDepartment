package com.yatai.suningfiredepartment.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yatai.suningfiredepartment.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(LoginActivity.this);
    }

    @OnClick({R.id.login_button, R.id.login_use_message, R.id.login_forgot_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.login_use_message:
                break;
            case R.id.login_forgot_password:
                break;
        }
    }
}
