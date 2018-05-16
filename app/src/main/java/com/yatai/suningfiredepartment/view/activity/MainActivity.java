package com.yatai.suningfiredepartment.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.view.adapter.MainViewPagerAdapter;
import com.yatai.suningfiredepartment.view.fragment.HomePageFragment;
import com.yatai.suningfiredepartment.view.fragment.InformationFragment;
import com.yatai.suningfiredepartment.view.fragment.PersonalFragment;
import com.yatai.suningfiredepartment.view.fragment.WorkFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_view_pager)
    ViewPager mainViewPager;
    @BindView(R.id.main_menu_tab)
    TabLayout mainMenuTab;

    private List<String> mMenus;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initMenus();
        initFragments();
        mainViewPager.setAdapter(new MainViewPagerAdapter(getSupportFragmentManager(), mMenus,fragments));
        mainMenuTab.setupWithViewPager(mainViewPager);

    }

    private void initMenus(){
        mMenus = new ArrayList<>();
        mMenus.add("首页");
        mMenus.add("工作");
        mMenus.add("咨询");
        mMenus.add("我的");
    }

    private void initFragments(){
        fragments = new ArrayList<>();
        HomePageFragment homePageFragment = HomePageFragment.newInstance("");
        WorkFragment workFragment = WorkFragment.newInstance("");
        InformationFragment informationFragment = InformationFragment.newInstance("");
        PersonalFragment personalFragment = PersonalFragment.newInstance("");

        fragments.add(homePageFragment);
        fragments.add(workFragment);
        fragments.add(informationFragment);
        fragments.add(personalFragment);
    }

}
