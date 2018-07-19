package com.yatai.suningfiredepartment.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.widget.CountDownButton;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

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
    Button loginButton;

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
        loginButton.setEnabled(false);
        loginButton.setBackground(getResources().getDrawable(R.drawable.login_button_gray));
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
                loginButton.setEnabled(false);
                loginButton.setBackground(getResources().getDrawable(R.drawable.login_button_gray));
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
        String url = getString(R.string.base_url)+"sendCode";
        AjaxParams params =new AjaxParams();
        params.put("mobile",tele);
        mHttp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200){
                        ToastUtil.show(LoginMessageActivity.this,"发送成功");
                        loginButton.setEnabled(true);
                        loginButton.setBackground(getResources().getDrawable(R.drawable.login_button_bg));
                    }else{
                        ToastUtil.show(LoginMessageActivity.this,jb.getString("message"));
                        loginButton.setEnabled(false);
                        loginButton.setBackground(getResources().getDrawable(R.drawable.login_button_gray));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                loginButton.setEnabled(false);
                loginButton.setBackground(getResources().getDrawable(R.drawable.login_button_gray));
                ToastUtil.show(LoginMessageActivity.this,strMsg);
            }
        });

    }

    private void login() {
        String tele = teleCode.getText().toString();
        String identify = identifyCode.getText().toString();

        String url = getString(R.string.base_url)+"verifyCode";

        AjaxParams params = new AjaxParams();
        params.put("mobile",tele);
        params.put("code",identify);

        mHttp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200){
                        JSONObject data = jb.getJSONObject("data");
                        String token = data.getString("token");
                        String gridId = data.getString("grid_id");
                        loginSuccess(token,gridId);
                    }else{
                        ToastUtil.show(LoginMessageActivity.this,jb.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                loginFailed();
            }
        });
    }

    public void loginFailed() {
        ToastUtil.show(this,"手机号或密码错误，请重新输入");
    }

    public void loginSuccess(String token,String gridId) {
        PreferenceUtils.setPrefString(this,"token",token);
        PreferenceUtils.setPrefString(this,"gridId",gridId);
        Intent intent = new Intent(LoginMessageActivity.this,MainActivity.class);
        startActivity(intent);
        LoginMessageActivity.this.finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginMessageActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
