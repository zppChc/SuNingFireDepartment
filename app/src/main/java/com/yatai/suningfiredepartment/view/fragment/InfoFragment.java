package com.yatai.suningfiredepartment.view.fragment;

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
import com.yatai.suningfiredepartment.model.entity.CategoryEntity;
import com.yatai.suningfiredepartment.model.entity.Info;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.adapter.InfoAdapter;
import com.yatai.suningfiredepartment.view.adapter.InfoCategoryAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class InfoFragment extends Fragment {
    private RecyclerView mCategoryRecyclerView;
    private RecyclerView mInfoRecyclerView;
    private FinalHttp mHttp;
    private InfoCategoryAdapter mCategoryAdapter;
    private InfoAdapter mInfoAdapter;
    private String token;

    public static InfoFragment newInstance(String data) {
        Bundle args = new Bundle();
        args.putString("key", data);
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
        mCategoryRecyclerView=(RecyclerView) view.findViewById(R.id.info_category_recycler_view);
        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        mCategoryAdapter = new InfoCategoryAdapter(getContext());
        mCategoryRecyclerView.setAdapter(mCategoryAdapter);

        mInfoRecyclerView=(RecyclerView)view.findViewById(R.id.info_recycler_view);
        mInfoAdapter=new InfoAdapter(getContext());
        mInfoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mInfoRecyclerView.setAdapter(mInfoAdapter);

        mHttp = new FinalHttp();
        token = PreferenceUtils.getPerfString(getContext(), "token", "");
        mHttp.addHeader("Authorization", "Bearer " + token);

        initCategoryData();
        getInfoList();
    }

    /**
     * 获取分类列表
     */
    private void initCategoryData() {
        String url = getString(R.string.base_url) + "infoCategory";
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200) {
                        List<CategoryEntity> list = new ArrayList<>();
                        JSONArray data = jb.getJSONArray("data");
                        Logger.d("Data : " + data.toString());
                        Gson gson = new Gson();
                        for (int i = 0; i < data.length(); i++) {
                            CategoryEntity categoryEntity = gson.fromJson(data.getJSONObject(i).toString(), CategoryEntity.class);
                            list.add(categoryEntity);
                        }
                        mCategoryAdapter.setCategoryEntityList(list);
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
    private void getInfoList(){
        String url =getString(R.string.base_url)+"infoList";
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb  = new JSONObject(s);
                    if (jb.getInt("code") == 200) {
                        List<Info> list =new ArrayList<>();
                        JSONArray data = jb.getJSONArray("data");
                        Logger.d("Data : " + data.toString());
                        Gson gson = new Gson();
                        for (int i = 0; i < data.length(); i++) {
                            Info info = gson.fromJson(data.getJSONObject(i).toString(),Info.class);
                            list.add(info);
                        }
                        mInfoAdapter.setInfoList(list);
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

    /**
     * 根据分类Id 获取咨询列表
     * @param categoryId
     */
    private void initInfoDataByCategoryId(String categoryId){
        String url = getString(R.string.base_url) + "info";
        AjaxParams params = new AjaxParams();
        params.put("id",categoryId);
        mHttp.get(url, params, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb  = new JSONObject(s);
                    if (jb.getInt("code") == 200) {
                        List<Info> list =new ArrayList<>();
                        JSONArray data = jb.getJSONArray("data");
                        Logger.d("Data : " + data.toString());
                        Gson gson = new Gson();
                        for (int i = 0; i < data.length(); i++) {
                            Info info = gson.fromJson(data.getJSONObject(i).toString(),Info.class);
                            list.add(info);
                        }
                        mInfoAdapter.setInfoList(list);
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
