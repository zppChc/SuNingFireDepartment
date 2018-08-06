package com.yatai.suningfiredepartment.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.util.JpushUtil;
import com.yatai.suningfiredepartment.util.PreferenceUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

public class JpushActivity extends AppCompatActivity {

    public static boolean isForeground = false;
    @BindView(R.id.info_detail_back)
    ImageView mBackImageView;
    @BindView(R.id.info_detail_title_tv)
    TextView mTitleTv;
    @BindView(R.id.info_detail_content)
    EditText mContent;
    private String infoId;
    private FinalHttp mHttp;
    String title = null;
    String extras = null;
    String content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_jpush);
        ButterKnife.bind(this);
        Intent intent = getIntent();

        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
//            for (String key: bundle.keySet()){
//                Logger.i("Bundle Content : "+"Key=" + key + ", content=" +bundle.getString(key));
//            }
            if (bundle != null) {
//                title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                title = bundle.getString(JPushInterface.EXTRA_ALERT);
                extras=bundle.getString(JPushInterface.EXTRA_EXTRA);
                Logger.i("Bundle extras =" + extras);
            }
        }
        registerMessageReceiver();  // used for receive msg
        initView();
        try {
            JSONObject nofination = new JSONObject(extras);
            Logger.i("JPush Extras : "+ extras);
            if (!nofination.getString("info_id").equals("0")) {
                getInfoDetail(nofination.getString("info_id"));
            } else {
                mTitleTv.setText(title);
                content= nofination.getString("content");
                mContent.setText(content);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                    if (!JpushUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
//                    setCostomMsg(showMsg.toString());
                }
            } catch (Exception e){
            }
        }
    }

    private void getInfoDetail(String infoId) {
        mHttp = new FinalHttp();
        String url = getString(R.string.base_url) + "info/" + infoId;
        String token = "Bearer " + PreferenceUtils.getPerfString(this, "token", "");
        mHttp.addHeader("Authorization", token);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200) {
                        JSONObject data = jb.getJSONObject("data");
                        String title = data.getString("title");
                        mTitleTv.setText(title);
                        String content = data.getString("content");
                        mContent.setText(content);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }
}
