package com.yatai.suningfiredepartment.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.CategoryEntity;
import com.yatai.suningfiredepartment.entity.WorkItemEntity;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.activity.WorkDetailActivity;
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
import butterknife.Unbinder;

public class WorkFragment extends Fragment {


    @BindView(R.id.work_category_recycler_view)
    RecyclerView workCategoryRecyclerView;
    @BindView(R.id.work_recycler_view)
    RecyclerView workRecyclerView;
    Unbinder unbinder;

    private List<CategoryEntity> categoryList;
    private List<WorkItemEntity> workList;
    private FinalHttp mHttp;
    private String gridId;
    private WorkCategoryAdapter mCategoryAdapter;
    private WorkItemAdapter mWorkItemAdapter;

    public static WorkFragment newInstance(String data) {
        Bundle args = new Bundle();
        args.putString("gridId", data);
        WorkFragment fragment = new WorkFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        gridId = data.getString("gridId");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView(){
        categoryList = new ArrayList<>();
        workList = new ArrayList<>();
        mHttp = new FinalHttp();

        mCategoryAdapter = new WorkCategoryAdapter(getContext());
        mCategoryAdapter.setClickListener(new WorkCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ToastUtil.show(getContext(), "Position: "+ String.valueOf(position));
                if (position==0){
                    getAllWorkList();
                }else{
                    getWorkListByCategoryId(categoryList.get(position).getId());
                }
            }
        });

        workCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        workCategoryRecyclerView.setAdapter(mCategoryAdapter);
        mCategoryAdapter.setCategoryEntityList(categoryList);

        mWorkItemAdapter = new WorkItemAdapter(getContext());
        mWorkItemAdapter.setListener(new WorkItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), WorkDetailActivity.class);
                Gson gson  = new Gson();
                String workItemDetail = gson.toJson(workList.get(position));
                intent.putExtra("workItem",workItemDetail);
                startActivity(intent);
            }
        });
        workRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        workRecyclerView.setAdapter(mWorkItemAdapter);

        getCategoryData();
        getAllWorkList();
    }

    private void getCategoryData(){
        String url = getString(R.string.base_url) + "taskCategory";
        String token = "Bearer " + PreferenceUtils.getPerfString(getContext(), "token", "");
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
                        if (data.length()>0) {
                            CategoryEntity temp =new CategoryEntity();
                            temp.setId("99999");
                            temp.setName("全部");
                            categoryList.add(temp);
                            for (int i = 0; i < data.length(); i++) {
                                CategoryEntity categoryEntity = gson.fromJson(data.getJSONObject(i).toString(), CategoryEntity.class);
                                categoryList.add(categoryEntity);
                            }
                            mCategoryAdapter.setCategoryEntityList(categoryList);
                        }else{
                            mCategoryAdapter.setCategoryEntityList(categoryList);
                        }
                    }else{
                        ToastUtil.show(getContext(),jb.getString("message"));
                        mCategoryAdapter.setCategoryEntityList(categoryList);
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

    private void getAllWorkList(){
        String url = getString(R.string.base_url) + "grid/"+gridId+"/task";
        String token = "Bearer " + PreferenceUtils.getPerfString(getContext(), "token", "");
        mHttp.addHeader("Authorization", token);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                workList.clear();
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200){
                        JSONArray data = jb.getJSONArray("data");
                        if (data.length()>0){
                            Gson gson = new Gson();
                            for (int i=0; i< data.length(); i++){
                                WorkItemEntity workItemEntity = gson.fromJson(data.getJSONObject(i).toString(),WorkItemEntity.class);
                                workList.add(workItemEntity);
                            }
                            mWorkItemAdapter.setList(workList);
                        }else{
                            mWorkItemAdapter.setList(workList);
                        }
                    }else {
                        ToastUtil.show(getContext(),jb.getString("message"));
                        mWorkItemAdapter.setList(workList);
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

    private void getWorkListByCategoryId(String categoryId){
        String url = getString(R.string.base_url) + "grid/"+gridId+"/task/"+categoryId;
        String token = "Bearer " + PreferenceUtils.getPerfString(getContext(), "token", "");
        mHttp.addHeader("Authorization", token);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                workList.clear();
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200){
                        JSONArray data = jb.getJSONArray("data");
                        ToastUtil.show(getContext(),String.valueOf(data.length()));
                        if (data.length()>0){
                            Gson gson = new Gson();
                            for (int i=0; i< data.length(); i++){
                                WorkItemEntity workItemEntity = gson.fromJson(data.getJSONObject(i).toString(),WorkItemEntity.class);
                                workList.add(workItemEntity);
                            }
                            mWorkItemAdapter.setList(workList);
                        }else{
                            mWorkItemAdapter.setList(workList);
                        }
                    }else {
                        ToastUtil.show(getContext(),jb.getString("message"));
                        mWorkItemAdapter.setList(workList);
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
