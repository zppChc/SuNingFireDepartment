package com.yatai.suningfiredepartment.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.view.adapter.MainViewPagerAdapter;
import com.yatai.suningfiredepartment.view.fragment.CensusFragment;
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

    private String[] mMenus = {
            "首页",
            "工作",
            "咨询",
            "统计",
            "我的"
    };
    private int[] images= new int[]{
            R.drawable.home_page,
            R.drawable.work,
            R.drawable.info,
            R.drawable.calc,
            R.drawable.personal
    };
    private int[] imagesSelected= new int[]{
            R.drawable.home_page_blue,
            R.drawable.work_blue,
            R.drawable.info_blue,
            R.drawable.calc_blue,
            R.drawable.personal_blue
    };
    private List<Fragment> fragments;
    private MainViewPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        initFragments();
        mPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(),mMenus,fragments);
        mainViewPager.setAdapter(mPagerAdapter);
        mainMenuTab.setupWithViewPager(mainViewPager);

        for (int i = 0; i <mPagerAdapter.getCount(); i++ ){
           TabLayout.Tab tab =  mainMenuTab.getTabAt(i);//获得每一个tab
           tab.setCustomView(R.layout.item_menu);
            TextView textView = (TextView)tab.getCustomView().findViewById(R.id.tab_text);
            textView.setText(mMenus[i]);
            ImageView imageView=(ImageView)tab.getCustomView().findViewById(R.id.tab_img);
            imageView.setImageDrawable(getResources().getDrawable(images[i]));
           if (i == 0){
               imageView.setSelected(true);
//               tab.getCustomView().findViewById(R.id.tab_layout).setSelected(true);
               imageView.setImageDrawable(getResources().getDrawable(imagesSelected[i]));
           }
        }

        mainMenuTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                tab.getCustomView().findViewById(R.id.tab_layout).setSelected(true);
                ImageView imageView = (ImageView)tab.getCustomView().findViewById(R.id.tab_img);
                imageView.setSelected(true);
                imageView.setImageDrawable(getResources().getDrawable(imagesSelected[tab.getPosition()]));
                mainViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.getCustomView().findViewById(R.id.tab_layout).setSelected(false);
                ImageView imageView = (ImageView)tab.getCustomView().findViewById(R.id.tab_img);
                imageView.setSelected(false);
                imageView.setImageDrawable(getResources().getDrawable(images[tab.getPosition()]));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

//    private void initMenus(){
//        mMenus = new ArrayList<>();
//        mMenus.add("首页");
//        mMenus.add("工作");
//        mMenus.add("咨询");
//        mMenus.add("统计");
//        mMenus.add("我的");
//    }

    private void initFragments(){
        fragments = new ArrayList<>();
        HomePageFragment homePageFragment = HomePageFragment.newInstance("");
        WorkFragment workFragment = WorkFragment.newInstance("");
        InformationFragment informationFragment = InformationFragment.newInstance("");
        CensusFragment censusFragment = CensusFragment.newInstance("");
        PersonalFragment personalFragment = PersonalFragment.newInstance("");

        fragments.add(homePageFragment);
        fragments.add(workFragment);
        fragments.add(informationFragment);
        fragments.add(censusFragment);
        fragments.add(personalFragment);
    }

}
