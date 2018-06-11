package com.yatai.suningfiredepartment.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.WorkItemEntity;
import com.yatai.suningfiredepartment.entity.WorkTemplateEntity;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.adapter.WorkDetailFinishPicAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkDetailFinishActivity extends AppCompatActivity {

    @BindView(R.id.work_detail_finish_linear_layout)
    LinearLayout mLinearLayout;
    @BindView(R.id.work_detail_finish_pic_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.work_detail_finish_title_text_view)
    TextView mTitleTextView;
    @BindView(R.id.work_finish_back)
    ImageView mBack;

    WorkDetailFinishPicAdapter mAdapter;
    FinalHttp mHttp;
    List<String> imgs;
    List<WorkTemplateEntity> mTemplates;
    Gson gson;
    int recordId;
    String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_detail_finish);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String workItem = intent.getStringExtra("workItem");
        gson = new Gson();
        WorkItemEntity workItemEntity = gson.fromJson(workItem, WorkItemEntity.class);
        recordId = workItemEntity.getRecord_id();
        title = workItemEntity.getName();
        initView();
    }

    private void initView() {
        mHttp = new FinalHttp();
        imgs = new ArrayList<>();
        mTemplates = new ArrayList<>();

        mTitleTextView.setText(title);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAdapter = new WorkDetailFinishPicAdapter(WorkDetailFinishActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setImgs(imgs);


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getData(recordId);
    }

    private void getData(int recordId) {
        String token = "Bearer " + PreferenceUtils.getPerfString(this, "token", "");
        mHttp.addHeader("Authorization", token);
        String url = getString(R.string.base_url) + "taskRecord/" + String.valueOf(recordId);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200) {
                        JSONObject data = jb.getJSONObject("data");
                        JSONArray content = data.getJSONArray("content");
                        if (content.length() > 0) {
                            for (int i = 0; i < content.length(); i++) {
                                WorkTemplateEntity templateEntity = gson.fromJson(content.getJSONObject(i).toString(), WorkTemplateEntity.class);
                                mTemplates.add(templateEntity);
                            }
                            dynamicAdd(mTemplates);
                        }
                        JSONArray imgArray = data.getJSONArray("images");
                        if (imgArray.length() > 0) {
                            for (int i = 0; i < imgArray.length(); i++) {
                                imgs.add(imgArray.get(i).toString());
                            }
                            mAdapter.setImgs(imgs);
                        }
                    } else {
                        ToastUtil.show(WorkDetailFinishActivity.this, jb.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(WorkDetailFinishActivity.this, strMsg);
            }
        });
    }

    private void dynamicAdd(List<WorkTemplateEntity> list) {
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);

                TextView name = new TextView(this);
                name.setText(list.get(i).getName());
                name.setTextSize(16);
                name.setGravity(Gravity.CENTER_VERTICAL);

                TextView content = new TextView(this);
                content.setText(list.get(i).getContent());
                content.setTextSize(16);
                content.setGravity(Gravity.CENTER_VERTICAL);

                View view = new View(this);
                view.setBackgroundColor(getResources().getColor(R.color.work_detail_gray));

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
                layoutParams.setMargins(30,0,10,0);
                LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
                LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 3.0f);
                LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,2);

                layout.addView(name, nameParams);
                layout.addView(content, contentParams);
                mLinearLayout.addView(layout, layoutParams);
                if (i+1<list.size()){
                    mLinearLayout.addView(view,viewParams);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
