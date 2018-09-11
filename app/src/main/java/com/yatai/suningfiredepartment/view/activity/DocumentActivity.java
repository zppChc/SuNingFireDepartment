package com.yatai.suningfiredepartment.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.util.PreferenceUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DocumentActivity extends AppCompatActivity {
    @BindView(R.id.info_detail_back)
    ImageView mBackImageView;
    @BindView(R.id.info_detail_title_tv)
    TextView mTitleTv;
    @BindView(R.id.info_detail_content)
    EditText mContent;


    private FinalHttp mHttp;
    private int documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_document);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        documentId = intent.getIntExtra("documentId",0);
        getInfoDetail(String.valueOf(documentId));

        initView();
    }

    private void initView() {
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getInfoDetail(String infoId) {
        mHttp = new FinalHttp();
        String url = getString(R.string.base_url) + "document/" + infoId;
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
                        String title = data.getString("name");
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
