package com.yatai.suningfiredepartment.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.CategoryEntity;
import com.yatai.suningfiredepartment.entity.WorkItemEntity;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.adapter.WorkCategoryAdapter;
import com.yatai.suningfiredepartment.view.adapter.WorkItemAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DepartmentWorkActivity extends AppCompatActivity {

    private static final String TAG_ALL = "99999";
    @BindView(R.id.sub_work_category_recycler_view)
    RecyclerView workCategoryRecyclerView;
    @BindView(R.id.sub_work_recycler_view)
    RecyclerView workRecyclerView;
    @BindView(R.id.sub_work_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.title_image_back)
    ImageView mBackImg;
    @BindView(R.id.title_name)
    TextView mTitleTv;

    private String gridId;
    private int categoryId;
    private List<CategoryEntity> categoryList;
    private List<WorkItemEntity> workList;
    private FinalHttp mHttp;
    private WorkCategoryAdapter mCategoryAdapter;
    private WorkItemAdapter mWorkItemAdapter;
    private ProgressDialog mProgressDialog;
    private int refreshFlag = 0;
    private LinearLayoutManager categoryLLM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_department_work);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        gridId = intent.getStringExtra("id");
        categoryId = intent.getIntExtra("categoryId",Integer.valueOf(TAG_ALL));
        Logger.i("categoryId : "+categoryId);
//        refreshFlag = categoryId;
        initView();
    }

    private void initView() {
        categoryList = new ArrayList<>();
        workList = new ArrayList<>();
        mHttp = new FinalHttp();

        mTitleTv.setText("工 作");

        mProgressDialog = new ProgressDialog(DepartmentWorkActivity.this, ProgressDialog.THEME_HOLO_DARK);
        mProgressDialog.setMessage("正在加载...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mCategoryAdapter = new WorkCategoryAdapter(DepartmentWorkActivity.this);
        mCategoryAdapter.setClickListener(new WorkCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ToastUtil.show(getContext(), "Position: "+ String.valueOf(position));
                mProgressDialog.show();
                refreshFlag = position;
                categoryId = Integer.valueOf(categoryList.get(position).getId());
                mCategoryAdapter.setDefSelect(position);
                if (categoryId == Integer.valueOf(TAG_ALL)) {
                    getAllWorkList();
                } else {
                    getWorkListByCategoryId(categoryId);
                }
            }
        });

        categoryLLM = new LinearLayoutManager(DepartmentWorkActivity.this, LinearLayoutManager.HORIZONTAL, false);
        workCategoryRecyclerView.setLayoutManager(categoryLLM);
        workCategoryRecyclerView.setAdapter(mCategoryAdapter);
        mCategoryAdapter.setCategoryEntityList(categoryList);

        mWorkItemAdapter = new WorkItemAdapter(DepartmentWorkActivity.this);
        mWorkItemAdapter.setListener(new WorkItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Gson gson = new Gson();
                String workItemDetail = gson.toJson(workList.get(position));
                //工作状态为未完成，跳转到一个未完成界面
                if (workList.get(position).getStatus() == 0) {
                    Intent intent = new Intent(DepartmentWorkActivity.this, WorkDetailActivity.class);
                    intent.putExtra("workItem", workItemDetail);
                    startActivity(intent);
                } else {
                    //跳转到 查看单个任务界面
                    Intent intent = new Intent(DepartmentWorkActivity.this, WorkDetailFinishActivity.class);
                    intent.putExtra("workItem", workList.get(position).getRecord_id());
                    //从workItem 中获取ID,可以用来查询单个数据
                    startActivity(intent);
                }
            }
        });
        workRecyclerView.setLayoutManager(new LinearLayoutManager(DepartmentWorkActivity.this, LinearLayoutManager.VERTICAL, false));
        workRecyclerView.setAdapter(mWorkItemAdapter);

        mRefreshLayout.setOnLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (categoryId == Integer.valueOf(TAG_ALL)) {
                    getAllWorkList();
                } else {
                    getWorkListByCategoryId(categoryId);
                }
            }
        });

        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        getCategoryData();
//        if (refreshFlag == 0){
//            getAllWorkList();
//        }else{
//            getWorkListByCategoryId(String.valueOf(categoryId));
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getCategoryData();
        if (categoryId == Integer.valueOf(TAG_ALL)){
            getAllWorkList();
        }else{
            getWorkListByCategoryId(categoryId);
        }
    }

    private void getCategoryData() {
        String url = getString(R.string.base_url) + "taskCategory";
        String token = "Bearer " + PreferenceUtils.getPerfString(DepartmentWorkActivity.this, "token", "");
        mHttp.addHeader("Authorization", token);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                categoryList.clear();
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200) {
                        JSONArray data = jb.getJSONArray("data");
                        Logger.d("Data : " + data.toString());
                        Gson gson = new Gson();
                        if (data.length() > 0) {
                            CategoryEntity temp = new CategoryEntity();
                            temp.setId(TAG_ALL);
                            temp.setName("全部");
                            categoryList.add(temp);
                            for (int i = 0; i < data.length(); i++) {
                                CategoryEntity categoryEntity = gson.fromJson(data.getJSONObject(i).toString(), CategoryEntity.class);
                                categoryList.add(categoryEntity);
                            }
                            mCategoryAdapter.setCategoryEntityList(categoryList);
                            if (categoryId == Integer.valueOf(TAG_ALL)){
                                refreshFlag = 0;
                                mCategoryAdapter.setDefSelect(refreshFlag);
                            }else{
                                for (int i = 0;i<categoryList.size();i++){
                                    if (categoryList.get(i).getId().equals(String.valueOf(categoryId))){
                                        refreshFlag = i;
                                        mCategoryAdapter.setDefSelect(refreshFlag);
                                    }
                                }
                            }

                            for(int j = 0; j<categoryList.size(); j++) {
                                if (categoryList.get(j).getId().equals(String.valueOf(categoryId))) {
                                    categoryLLM.scrollToPositionWithOffset(j, 0);
                                }
                            }
                        } else {
                            mCategoryAdapter.setCategoryEntityList(categoryList);
                        }
                    } else {
                        ToastUtil.show(DepartmentWorkActivity.this, jb.getString("message"));
                        mCategoryAdapter.setCategoryEntityList(categoryList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(DepartmentWorkActivity.this, strMsg);
            }
        });
    }

    private void getAllWorkList() {
        String url = getString(R.string.base_url) + "department/" + gridId + "/task";
        String token = "Bearer " + PreferenceUtils.getPerfString(DepartmentWorkActivity.this, "token", "");
        mHttp.addHeader("Authorization", token);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                workList.clear();
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200) {
                        JSONArray data = jb.getJSONArray("data");
                        if (data.length() > 0) {
                            Gson gson = new Gson();
                            for (int i = 0; i < data.length(); i++) {
                                WorkItemEntity workItemEntity = gson.fromJson(data.getJSONObject(i).toString(), WorkItemEntity.class);
                                workList.add(workItemEntity);
                            }
                            mWorkItemAdapter.setList(workList);
                        } else {
                            mWorkItemAdapter.setList(workList);
                        }
                    } else {
                        ToastUtil.show(DepartmentWorkActivity.this, jb.getString("message"));
                        mWorkItemAdapter.setList(workList);
                    }
                    mProgressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(DepartmentWorkActivity.this, strMsg);
                mProgressDialog.dismiss();
            }
        });
    }

    private void getWorkListByCategoryId(int categoryId) {
        String url = getString(R.string.base_url) + "department/" + gridId + "/task/" + categoryId;
        String token = "Bearer " + PreferenceUtils.getPerfString(DepartmentWorkActivity.this, "token", "");
        mHttp.addHeader("Authorization", token);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                workList.clear();
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200) {
                        JSONArray data = jb.getJSONArray("data");
                        if (data.length() > 0) {
                            Gson gson = new Gson();
                            for (int i = 0; i < data.length(); i++) {
                                WorkItemEntity workItemEntity = gson.fromJson(data.getJSONObject(i).toString(), WorkItemEntity.class);
                                workList.add(workItemEntity);
                            }
                            mWorkItemAdapter.setList(workList);
                        } else {
                            mWorkItemAdapter.setList(workList);
                            mWorkItemAdapter.notifyDataSetChanged();
                        }
                    } else {
                        ToastUtil.show(DepartmentWorkActivity.this, jb.getString("message"));
                        mWorkItemAdapter.setList(workList);
                    }
                    mProgressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(DepartmentWorkActivity.this, strMsg);
                mProgressDialog.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
