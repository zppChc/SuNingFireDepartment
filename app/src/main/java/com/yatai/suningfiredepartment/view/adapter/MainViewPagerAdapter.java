package com.yatai.suningfiredepartment.view.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {
    List<String> mMenus;
    List<Fragment> mFragments;

    public MainViewPagerAdapter(FragmentManager fm, List<String> menus, List<Fragment> fragments) {
        super(fm);
        mMenus = menus;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mMenus.size()==mFragments.size()?mFragments.size():0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mMenus.get(position);
    }
}
