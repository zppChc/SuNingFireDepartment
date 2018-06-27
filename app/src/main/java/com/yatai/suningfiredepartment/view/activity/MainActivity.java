package com.yatai.suningfiredepartment.view.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.util.CommonUtil;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.adapter.MainViewPagerAdapter;
import com.yatai.suningfiredepartment.view.fragment.CensusFragment;
import com.yatai.suningfiredepartment.view.fragment.HomePageFragment;
import com.yatai.suningfiredepartment.view.fragment.HomePageFragmentLand;
import com.yatai.suningfiredepartment.view.fragment.InfoFragment;
import com.yatai.suningfiredepartment.view.fragment.PersonalFragment;
import com.yatai.suningfiredepartment.view.fragment.WorkCalendarFragment;
import com.yatai.suningfiredepartment.view.fragment.WorkFragment;
import com.yatai.suningfiredepartment.view.widget.ViewPagerSlide;

import net.tsz.afinal.FinalHttp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_view_pager)
    ViewPagerSlide mainViewPager;
    @BindView(R.id.main_menu_tab)
    TabLayout mainMenuTab;
    //记录用户首次点击返回键的时间
    private long firstTime = 0;

//    private String[] mMenus = {
//            "首页",
//            "工作",
//            "资讯",
//            "统计",
//            "我的"
//    };
//    private int[] images= new int[]{
//            R.drawable.home_page,
//            R.drawable.work,
//            R.drawable.info,
//            R.drawable.calc,
//            R.drawable.personal
//    };
//    private int[] imagesSelected= new int[]{
//            R.drawable.home_page_blue,
//            R.drawable.work_blue,
//            R.drawable.info_blue,
//            R.drawable.calc_blue,
//            R.drawable.personal_blue
//    };
    private String[] mMenus = {
            "首页",
            "工作",
            "资讯",
            "我的"
    };
    private int[] images= new int[]{
            R.drawable.home_page,
            R.drawable.work,
            R.drawable.info,
            R.drawable.personal
    };
    private int[] imagesSelected= new int[]{
            R.drawable.home_page_blue,
            R.drawable.work_blue,
            R.drawable.info_blue,
            R.drawable.personal_blue
    };
    private List<Fragment> fragments;
    private MainViewPagerAdapter mPagerAdapter;
    private String gridId;
    private long exitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


//
//        Intent intent = getIntent();
//        String gridId = intent.getStringExtra("gridId");

        gridId = PreferenceUtils.getPerfString(MainActivity.this,"gridId","");

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        initFragments(gridId);
    }

    private void initFragments(String gridId){
        fragments = new ArrayList<>();
        HomePageFragment homePageFragment = HomePageFragment.newInstance(gridId);
        WorkFragment workFragment = WorkFragment.newInstance(gridId);
//        WorkCalendarFragment workFragment  = WorkCalendarFragment.newInstance(gridId,0);
        InfoFragment infoFragment = InfoFragment.newInstance(gridId);
//        CensusFragment censusFragment = CensusFragment.newInstance(gridId);
        PersonalFragment personalFragment = PersonalFragment.newInstance(gridId);

        fragments.add(homePageFragment);
        fragments.add(workFragment);
        fragments.add(infoFragment);
//        fragments.add(censusFragment);
        fragments.add(personalFragment);

        mPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(),mMenus,fragments);
        mainViewPager.setAdapter(mPagerAdapter);
        mainMenuTab.setupWithViewPager(mainViewPager);

        for (int i = 0; i <mPagerAdapter.getCount(); i++ ){
            TabLayout.Tab tab =  mainMenuTab.getTabAt(i);//获得每一个tab
            tab.setCustomView(R.layout.item_menu);
            TextView textView = (TextView)tab.getCustomView().findViewById(R.id.tab_text);
            textView.setText(mMenus[i]);
            textView.setTextSize(10);
            ImageView imageView=(ImageView)tab.getCustomView().findViewById(R.id.tab_img);
            imageView.setImageDrawable(getResources().getDrawable(images[i]));
            imageView.setPadding(0,5,0,0);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Logger.i("Main 横屏");
            Intent intent = new Intent(MainActivity.this,MainActivityLand.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Logger.i("Main 横屏");
            Intent intent = new Intent(MainActivity.this,MainActivityLand.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
//        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            // port do nothing is ok
//            Logger.i("Main 竖屏");
//        }
    }

    /**
     * 当按返回键时
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.show(MainActivity.this,"再按一次退出程序");
                exitTime = System.currentTimeMillis();
                return false;
            } else {
                MainActivity.this.finish();
                return true;
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
