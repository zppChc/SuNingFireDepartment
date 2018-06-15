package com.yatai.suningfiredepartment.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.util.Validator;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity  {

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

    private FinalHttp mHttp;
    private long exitTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(LoginActivity.this);
        mHttp =new FinalHttp();
    }

    @OnClick({R.id.login_button, R.id.login_use_message, R.id.login_forgot_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                String mobile = userNameEd.getText().toString();
                String password = passwordEd.getText().toString();
                if (Validator.validUsername(mobile) && Validator.validPassword(password)){
                    login(mobile,password);
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

    private void login(String mobile,String password){
        String url = getString(R.string.base_url)+"login";
        AjaxParams params=new AjaxParams();
        params.put("mobile",mobile);
        params.put("password",password);
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
                        ToastUtil.show(getApplicationContext(),jb.getString("message"));
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
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    /**
     * 当按返回键时
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.show(LoginActivity.this,"再按一次退出程序");
                exitTime = System.currentTimeMillis();
                return false;
            } else {
                this.finish();
                return true;
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

}
