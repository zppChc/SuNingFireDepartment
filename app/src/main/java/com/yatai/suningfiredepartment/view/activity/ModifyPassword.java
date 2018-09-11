package com.yatai.suningfiredepartment.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ModifyPassword extends AppCompatActivity {

    @BindView(R.id.title_image_back)
    ImageView titleImageBack;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.tele_code)
    TextView teleCode;
    @BindView(R.id.identify_code)
    EditText identifyCode;
    @BindView(R.id.obtain_code_button)
    CountDownButton obtainCodeButton;
    @BindView(R.id.et_password_one)
    EditText etPasswordOne;
    @BindView(R.id.et_password_two)
    EditText etPasswordTwo;
    @BindView(R.id.modify_password_button)
    Button modifyPasswordButton;

    String mobile;

    FinalHttp mHttp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        ButterKnife.bind(this);
        mobile = getIntent().getStringExtra("mobile");
        initView();
    }

    private void initView(){
        titleName.setText("修改密码");
        titleName.setVisibility(View.VISIBLE);
        teleCode.setText(mobile);
        mHttp = new FinalHttp();
        modifyPasswordButton.setEnabled(false);
        modifyPasswordButton.setBackground(getResources().getDrawable(R.drawable.login_button_gray));
    }

    @OnClick({R.id.title_image_back, R.id.obtain_code_button, R.id.modify_password_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.title_image_back:
                this.finish();
                break;
            case R.id.obtain_code_button:
                //这里判断是否倒计时结束，避免在倒计时时多次点击导致重复请求接口
                String tele = teleCode.getText().toString();
                if (obtainCodeButton.isFinish()) {
                    obtainCodeButton.start();
                    getMessageCode(tele);
                }
                break;
            case R.id.modify_password_button:
                if (identifyCode.getText().toString().equals("")){
                    ToastUtil.show(ModifyPassword.this,"请填写验证码");
                }else {
                    if (etPasswordOne.getText().toString().equals("") || etPasswordTwo.getText().toString().equals("")) {
                        ToastUtil.show(ModifyPassword.this, "密码不能为空");
                    } else {
                        if (!etPasswordOne.getText().toString().equals(etPasswordTwo.getText().toString())) {
                            ToastUtil.show(ModifyPassword.this, "两次输入密码不一致请重新输入");
                        } else {
                            postModifyPassword(identifyCode.getText().toString(),etPasswordOne.getText().toString(),etPasswordTwo.getText().toString());
                        }
                    }
                }

                break;
        }
    }


    //获取验证码
    private void getMessageCode(String tele) {
        String token = "Bearer " + PreferenceUtils.getPerfString(this, "token", "");
        mHttp.addHeader("Authorization", token);
        String url = getString(R.string.base_url)+"sendResetPasswordCode";
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200){
                        ToastUtil.show(ModifyPassword.this,"发送成功");
                        modifyPasswordButton.setEnabled(true);
                        modifyPasswordButton.setBackground(getResources().getDrawable(R.drawable.login_button_bg));
                    }else{
                        ToastUtil.show(ModifyPassword.this,jb.getString("message"));
                        modifyPasswordButton.setEnabled(false);
                        modifyPasswordButton.setBackground(getResources().getDrawable(R.drawable.login_button_gray));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                modifyPasswordButton.setEnabled(false);
                modifyPasswordButton.setBackground(getResources().getDrawable(R.drawable.login_button_gray));
                ToastUtil.show(ModifyPassword.this,strMsg);
            }
        });
    }

    private void postModifyPassword(String code, String password1, String password2){
        String token = "Bearer " + PreferenceUtils.getPerfString(this, "token", "");
        mHttp.addHeader("Authorization", token);
        String url = getString(R.string.base_url)+"resetPassword";
        AjaxParams params =new AjaxParams();
        params.put("code",code);
        params.put("password1",password1);
        params.put("password2",password2);
        mHttp.post(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200){
                        ToastUtil.show(ModifyPassword.this,"密码修改成功");
                        ModifyPassword.this.finish();
                    }else{
                        ToastUtil.show(ModifyPassword.this,jb.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(ModifyPassword.this,strMsg);
            }
        });
    }
}
