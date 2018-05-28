package com.yatai.suningfiredepartment.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.view.adapter.WorkViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WorkFragment extends Fragment {
    @BindView(R.id.work_tab_layout)
    TabLayout menuTabLayout;
    @BindView(R.id.work_view_pager)
    ViewPager workViewPager;
    Unbinder unbinder;

    private String[] workMenus = {
            "全部",
            "巡查",
            "会议",
            "培训"
    };
    private List<Fragment> mFragments;
    private WorkViewPagerAdapter mAdapter;

    public static WorkFragment newInstance(String data) {
        Bundle args = new Bundle();
        args.putString("key", data);
        WorkFragment fragment = new WorkFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work, container, false);
        unbinder = ButterKnife.bind(this, view);
        initFragments();
        mAdapter = new WorkViewPagerAdapter(getChildFragmentManager(), workMenus,mFragments);
        workViewPager.setAdapter(mAdapter);
        menuTabLayout.setupWithViewPager(workViewPager);

        return view;
    }

    private void initFragments(){
        mFragments = new ArrayList<>();
        //全部
        SubAllWorkFragment subAllWorkFragment = SubAllWorkFragment.newInstance("");
        //巡查
        SubInspectionFragment subInspectionFragment=SubInspectionFragment.newInstance("");
        //会议
        SubConferenceFragment subConferenceFragment = SubConferenceFragment.newInstance("");
        //培训
        SubTrainFragment subTrainFragment=SubTrainFragment.newInstance("");

        mFragments.add(subAllWorkFragment);
        mFragments.add(subInspectionFragment);
        mFragments.add(subConferenceFragment);
        mFragments.add(subTrainFragment);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
