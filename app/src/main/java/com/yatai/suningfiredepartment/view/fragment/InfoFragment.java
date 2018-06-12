package com.yatai.suningfiredepartment.view.fragment;

import android.app.ProgressDialog;
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
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.CategoryEntity;
import com.yatai.suningfiredepartment.entity.InfoEntity;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.activity.InfoDetailActivity;
import com.yatai.suningfiredepartment.view.adapter.InfoAdapter;
import com.yatai.suningfiredepartment.view.adapter.InfoCategoryAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class InfoFragment extends Fragment {
    private RecyclerView mCategoryRecyclerView;
    private RecyclerView mInfoRecyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private FinalHttp mHttp;
    private InfoCategoryAdapter mCategoryAdapter;
    private InfoAdapter mInfoAdapter;
    private  List<CategoryEntity> categoryList;
    private List<InfoEntity> infoList;
    private ProgressDialog mProgressDialog;
    private int refreshFlag=0;

    public static InfoFragment newInstance(String data) {
        Bundle args = new Bundle();
        args.putString("gridId", data);
        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        categoryList = new ArrayList<>();
        infoList = new ArrayList<>();

        mProgressDialog = new ProgressDialog(getContext(),ProgressDialog.THEME_HOLO_DARK);
        mProgressDialog.setMessage("正在加载...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mCategoryAdapter = new InfoCategoryAdapter(getContext());
        mCategoryAdapter.setOnItemClickListener(new InfoCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ToastUtil.show(getContext(),"Position: "+position);
                mProgressDialog.show();
                refreshFlag = position;
                if (position == 0){
                    getAllInfoList();
                }else {
                    getInfoDataByCategoryId(categoryList.get(position).getId());
                }
            }
        });

        mCategoryRecyclerView=(RecyclerView) view.findViewById(R.id.info_category_recycler_view);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        mCategoryRecyclerView.setAdapter(mCategoryAdapter);
        mCategoryAdapter.setCategoryEntityList(categoryList);

        mInfoAdapter=new InfoAdapter(getContext());
        mInfoAdapter.setClickListener(new InfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showInfoDetail(infoList.get(position).getId());
            }
        });
        mInfoRecyclerView=(RecyclerView)view.findViewById(R.id.info_recycler_view);
        mInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mInfoRecyclerView.setAdapter(mInfoAdapter);
        mInfoAdapter.setInfoEntityList(infoList);

        mHttp = new FinalHttp();


        mRefreshLayout = (SmartRefreshLayout)view.findViewById(R.id.info_refresh_layout);
        mRefreshLayout.setOnLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1);
            }
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (refreshFlag == 0){
                    getAllInfoList();
                }else {
                    getInfoDataByCategoryId(categoryList.get(refreshFlag).getId());
                }
            }
        });

        getCategoryData();
        getAllInfoList();

    }

    /**
     * 获取分类列表
     */
    private void getCategoryData() {
        String url = getString(R.string.base_url) + "infoCategory";
        String token = "Bearer " + PreferenceUtils.getPerfString(getContext(), "token", "");
        mHttp.addHeader("Authorization", token);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    categoryList.clear();
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200) {
                        JSONArray data = jb.getJSONArray("data");
//                        Logger.d("Data : " + data.toString());
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
                            mCategoryAdapter.notifyDataSetChanged();
                        }else{
                            mCategoryAdapter.notifyDataSetChanged();
                        }
                    }else{
                        ToastUtil.show(getContext(),jb.getString("message"));
                        mCategoryAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(getContext(), strMsg);
            }
        });
    }

    /**
     * 获取全部咨询列表
     */
    private void getAllInfoList(){
        String url =getString(R.string.base_url)+"infoList";
        String token = "Bearer " + PreferenceUtils.getPerfString(getContext(), "token", "");
        mHttp.addHeader("Authorization", token);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    infoList=new ArrayList<>();
                    JSONObject jb  = new JSONObject(s);
                    if (jb.getInt("code") == 200) {
                        JSONArray data = jb.getJSONArray("data");
                        Gson gson = new Gson();
                        if (data.length()>0) {
                            for (int i = 0; i < data.length(); i++) {
                                InfoEntity infoEntity = gson.fromJson(data.getJSONObject(i).toString(), InfoEntity.class);
                                infoList.add(infoEntity);
                            }
                            mInfoAdapter.setInfoEntityList(infoList);
                        }else{
                            mInfoAdapter.setInfoEntityList(infoList);
                        }
                    }else {
                        ToastUtil.show(getContext(),jb.getString("message"));
                        mInfoAdapter.setInfoEntityList(infoList);
                    }
                    mProgressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(getContext(),strMsg);
                mProgressDialog.dismiss();
            }
        });
    }

    /**
     * 根据分类Id 获取咨询列表
     * @param categoryId
     */
    private void getInfoDataByCategoryId(String categoryId){
        String url = getString(R.string.base_url) + "infoList/"+categoryId;
        String token = "Bearer " + PreferenceUtils.getPerfString(getContext(), "token", "");
        mHttp.addHeader("Authorization", token);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    infoList=new ArrayList<>();
                    JSONObject jb  = new JSONObject(s);
                    if (jb.getInt("code") == 200) {
                        JSONArray data = jb.getJSONArray("data");
//                        Logger.d("Data : " + data.toString());
                        Gson gson = new Gson();
                        if(data.length()>0) {
                            for (int i = 0; i < data.length(); i++) {
                                InfoEntity infoEntity = gson.fromJson(data.getJSONObject(i).toString(), InfoEntity.class);
                                infoList.add(infoEntity);
                            }
                            mInfoAdapter.setInfoEntityList(infoList);
                        }else{
                            mInfoAdapter.setInfoEntityList(infoList);
                        }
                    }else{
                        ToastUtil.show(getContext(),jb.getString("message"));
                        mInfoAdapter.setInfoEntityList(infoList);
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

    private void showInfoDetail(String infoId){
        Intent intent = new Intent();
        intent.setClass(getActivity().getApplicationContext(), InfoDetailActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("infoId",infoId);//压入数据
        intent.putExtras(mBundle);
        startActivity(intent);
    }
}
