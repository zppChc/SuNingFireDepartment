package com.yatai.suningfiredepartment.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.PeopleEntity;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.adapter.FocusGroupAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 重点人群
 */
public class FocusGroupActivity extends AppCompatActivity {

    @BindView(R.id.title_image_back)
    ImageView mBackIv;
    @BindView(R.id.title_name)
    TextView mTitleNameTv;
    @BindView(R.id.focus_group_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.focus_group_refresh)
    SmartRefreshLayout mRefresh;

    FocusGroupAdapter mAdapter;
    List<PeopleEntity> peopleList;
    LinearLayoutManager mLayoutManager;
    String gridId;
    FinalHttp mHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_focus_group);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        gridId = intent.getStringExtra("gridId");
        initView();

        mBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initView(){
        mTitleNameTv.setText("重点人群");
        peopleList = new ArrayList<>();
        mHttp = new FinalHttp();
        mAdapter = new FocusGroupAdapter(this);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setList(peopleList);
        getList();
    }

    private void getList(){
        String token = PreferenceUtils.getPerfString(FocusGroupActivity.this, "token", "");
        String url = getString(R.string.base_url)+"grid/"+ gridId+"/importantPerson";
        mHttp.addHeader("Authorization", "Bearer " + token);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200){
                        JSONArray data = jb.getJSONArray("data");
                        if (data.length()>0){
                            Gson gson = new Gson();
                            peopleList = new ArrayList<>();
                            for (int i = 0 ; i<data.length(); i++){
                                PeopleEntity peopleEntity =gson.fromJson(data.getJSONObject(i).toString(),PeopleEntity.class);
                                peopleList.add(peopleEntity);
                            }
                            mAdapter.setList(peopleList);
//                            for(int j = 0; j<peopleList.size(); j++){
//                                if (peopleList.get(j).getId() ==peopleId){
//                                    mLayoutManager.scrollToPositionWithOffset(j, 0);
//                                }
//                            }
                        }
                    }else{
                        ToastUtil.show(FocusGroupActivity.this,jb.getString("message"));
                        peopleList.clear();
                        mAdapter.setList(peopleList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(FocusGroupActivity.this,strMsg);
            }
        });
    }

}
