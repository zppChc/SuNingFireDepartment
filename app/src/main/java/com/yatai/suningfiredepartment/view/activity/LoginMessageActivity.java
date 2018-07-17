package com.yatai.suningfiredepartment.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.widget.CountDownButton;

import net.tsz.afinal.FinalHttp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginMessageActivity extends AppCompatActivity {

    @BindView(R.id.tele_code)
    EditText teleCode;
    @BindView(R.id.identify_code)
    EditText identifyCode;
    @BindView(R.id.obtain_code_button)
    CountDownButton obtainButton;
    @BindView(R.id.message_login_button)
    Button LoginButton;

    FinalHttp mHttp;
    @BindView(R.id.title_name)
    TextView titleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_login_message);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mHttp = new FinalHttp();
        titleName.setText("短信登陆");
        LoginButton.setEnabled(false);
        LoginButton.setBackground(getResources().getDrawable(R.drawable.login_button_gray));
    }


    @OnClick({R.id.obtain_code_button, R.id.message_login_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.obtain_code_button:
                //这里判断是否倒计时结束，避免在倒计时时多次点击导致重复请求接口
                String tele = teleCode.getText().toString();
                if (validTele(tele)) {
                    if (obtainButton.isFinish()) {
                        obtainButton.start();
                        getMessageCode(tele);
                    }
                } else {
                    ToastUtil.show(this, "请输入正确的手机号码");
                }

                break;
            case R.id.message_login_button:
                login();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!obtainButton.isFinish()) {
            obtainButton.cancel();
        }
    }

    private boolean validTele(String tele) {
        String mobileRegex = "^1(3|4|5|7|8)\\d{9}$";
        if (tele.matches(mobileRegex)) {
            return true;
        }
        return false;
    }

    //获取验证码
    private void getMessageCode(String tele) {

    }

    private void login() {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginMessageActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
