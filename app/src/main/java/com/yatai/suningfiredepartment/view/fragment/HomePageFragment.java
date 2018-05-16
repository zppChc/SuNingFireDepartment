package com.yatai.suningfiredepartment.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.sunfusheng.marqueeview.MarqueeView;
import com.yatai.suningfiredepartment.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomePageFragment extends Fragment {

    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.marquee_new_view)
    MarqueeView marqueeNewView;

    private Unbinder mUnbinder;

    List<String> info;//滚动新闻
    AMap mAMap;

    public static HomePageFragment newInstance(String data) {
        Bundle args = new Bundle();
        args.putString("key", data);
        HomePageFragment fragment = new HomePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        mUnbinder = ButterKnife.bind(this, view);

        info=new ArrayList<>();
        //垂直广告 或者新闻
        info.add("1. 大家好，我是新闻一号。");
        info.add("2. 大家好，我是新闻二号");
        info.add("3. 大家好，我是新闻三号");
        info.add("4. 大家好，我是新闻四号");

        marqueeNewView.startWithList(info,R.anim.anim_bottom_in,R.anim.anim_top_out);

        //在activity执行oncreate时执行mapView.onCreate(saveInstanceState)，创建地图
        map.onCreate(savedInstanceState);

        if (mAMap == null) {
            mAMap = map.getMap();
        }
        return view;
    }


    /**
     * 必须重写以下方法
     */
    @Override
    public void onResume(){
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mUnbinder.unbind();

    }
}
