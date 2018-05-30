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

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.view.adapter.WorkRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 2018/5/24
 * 工作-巡查
 */
public class SubInspectionFragment extends Fragment {

    @BindView(R.id.work_inspection_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.work_inspection_refresh)
    SmartRefreshLayout mRefresh;
    Unbinder unbinder;
    WorkRecyclerViewAdapter mAdapter;
    List<String> datas=new ArrayList<>();
    public static SubInspectionFragment newInstance(String data) {
        Bundle args = new Bundle();
        args.putString("key", data);
        SubInspectionFragment fragment = new SubInspectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_inspection, container, false);

        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }
    private void initView(){
        mAdapter = new WorkRecyclerViewAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        mRecyclerView.setAdapter(mAdapter);
        for (int i = 0; i< 10 ;i++){
            datas.add(String.valueOf(i));
        }
        mAdapter.setDataList(datas);

        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}