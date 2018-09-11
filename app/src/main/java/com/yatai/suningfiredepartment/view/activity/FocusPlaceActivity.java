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
import com.yatai.suningfiredepartment.entity.PlaceEntity;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.adapter.FocusGroupAdapter;
import com.yatai.suningfiredepartment.view.adapter.FocusPlaceAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 重点单位
 */
public class FocusPlaceActivity extends AppCompatActivity {

    @BindView(R.id.title_image_back)
    ImageView mImageBack;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.focus_unit_recycler_view)
    RecyclerView mRecyclerView;
        @BindView(R.id.focus_group_refresh)
    SmartRefreshLayout mRefresh;

    FocusPlaceAdapter mAdapter;
    List<PlaceEntity> placeList;
    LinearLayoutManager mLayoutManager;
    String gridId;
    FinalHttp mHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_focus_unit);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        gridId = intent.getStringExtra("gridId");
        Logger.i("GridId : " + gridId);
        initView();
    }
    private void initView(){
        mTitleName.setText("重点单位");
        placeList = new ArrayList<>();
        mHttp = new FinalHttp();
        mAdapter = new FocusPlaceAdapter(this);
        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setList(placeList);

        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getList();
    }

    private void getList(){
        String token = PreferenceUtils.getPerfString(FocusPlaceActivity.this, "token", "");
        String url = getString(R.string.base_url)+"grid/"+ gridId+"/importantPlace";
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
                            placeList = new ArrayList<>();
                            for (int i = 0 ; i<data.length(); i++){
                                PlaceEntity peopleEntity =gson.fromJson(data.getJSONObject(i).toString(),PlaceEntity.class);
                                placeList.add(peopleEntity);
                            }
                            mAdapter.setList(placeList);
//                            for(int j = 0; j<placeList.size(); j++){
//                                if (placeList.get(j).getId() ==placeId){
//                                    mLayoutManager.scrollToPositionWithOffset(j, 0);
//                                }
//                            }
                        }
                    }else{
                        ToastUtil.show(FocusPlaceActivity.this,jb.getString("message"));
                        placeList.clear();
                        mAdapter.setList(placeList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(FocusPlaceActivity.this,strMsg);
            }
        });
    }

}
