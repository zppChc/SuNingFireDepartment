package com.yatai.suningfiredepartment.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.activity.LoginActivity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalFragment extends Fragment {
    @BindView(R.id.personal_portrait)
    CircleImageView mPortrait;
    @BindView(R.id.personal_name)
    TextView mName;
    @BindView(R.id.personal_phone)
    TextView mPhone;
    @BindView(R.id.personal_id_card)
    TextView mIdCard;
    @BindView(R.id.personal_address)
    TextView mAddress;
    @BindView(R.id.personal_info_modify)
    TextView mInfoModifyTv;
    @BindView(R.id.personal_password_modify)
    TextView mPasswordModifyTv;
    @BindView(R.id.personal_logout)
    TextView mLogout;
    Unbinder unbinder;
    private FinalHttp mHttp;

    private String name;
    private String mobile;
    private String address;
    private String idCard;
    private ProgressDialog mProgressDialog;

    public static PersonalFragment newInstance(String data) {
        Bundle args = new Bundle();
        args.putString("key", data);
        PersonalFragment fragment = new PersonalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        unbinder = ButterKnife.bind(this, view);

        mProgressDialog = new ProgressDialog(getContext(),ProgressDialog.THEME_HOLO_DARK);
        mProgressDialog.setMessage("正在加载...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        initData();
        return view;
    }

    private void initData() {
        mHttp = new FinalHttp();
        String token = "Bearer " + PreferenceUtils.getPerfString(getContext(), "token", "");
        String url = getString(R.string.base_url) + "myInfo";
        mHttp.addHeader("Authorization", token);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200) {
                        JSONObject data = jb.getJSONObject("data");
                        name = data.getString("name");
                        mName.setText(name);
                        mobile = data.getString("mobile");
                        mPhone.setText(mobile);
                        idCard = data.getString("id_card");
                        mIdCard.setText(idCard);
                        address = data.getString("address");
                        mAddress.setText(address);
                        Glide.with(getContext()).load(data.getString("image")).into(mPortrait);
                    } else {
                        ToastUtil.show(getContext(), jb.getString("message"));
                    }
                    mProgressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(getContext(), strMsg);
                mProgressDialog.dismiss();
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void logout(){
        mHttp=new FinalHttp();
        String url = getString(R.string.base_url) + "logout";
        String token = "Bearer " + PreferenceUtils.getPerfString(getContext(), "token", "");
        mHttp.addHeader("Authorization", token);
        mHttp.post(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200){
                        Intent intent = new Intent();
                        intent.setClass(getContext(), LoginActivity.class);
                        getContext().startActivity(intent);
                    }else{
                        ToastUtil.show(getContext(),jb.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(getContext(),strMsg);
            }
        });
    }
}
