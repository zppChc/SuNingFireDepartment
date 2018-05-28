package com.yatai.suningfiredepartment.view.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class WorkViewPagerAdapter extends FragmentPagerAdapter {

    String[] menus;
    List<Fragment> fragments;

    public WorkViewPagerAdapter(FragmentManager fm, String[] menus, List<Fragment> fragments) {
        super(fm);
        this.menus = menus;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return menus.length==fragments.size()?fragments.size():0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return menus[position];
    }
}
