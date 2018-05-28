package com.yatai.suningfiredepartment.view.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polygon;
import com.amap.api.maps2d.model.PolygonOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.orhanobut.logger.Logger;
import com.sunfusheng.marqueeview.MarqueeView;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.app.SuNingFireDepartmentApplication;
import com.yatai.suningfiredepartment.di.components.DaggerHomePageComponent;
import com.yatai.suningfiredepartment.di.modules.HomePageModule;
import com.yatai.suningfiredepartment.presenter.HomePageContract;
import com.yatai.suningfiredepartment.presenter.HomePagePresenter;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.activity.FocusGroupActivity;
import com.yatai.suningfiredepartment.view.adapter.HomeAccountAdapter;
import com.yatai.suningfiredepartment.view.adapter.HomeRegionAdapter;
import com.yatai.suningfiredepartment.view.adapter.HomeUnitAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomePageFragment extends Fragment implements HomePageContract.View, DistrictSearch.OnDistrictSearchListener, AMap.OnMapClickListener {

    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.marquee_new_view)
    MarqueeView marqueeNewView;
    //相关网格 中网格
    @BindView(R.id.home_page_region_recycler_view)
    RecyclerView mRegionRecyclerView;
    //相关单位
    @BindView(R.id.home_page_unit_recycler_view)
    RecyclerView mUnitRecyclerView;
    //工作台账
    @BindView(R.id.home_page_account_recycler_view)
    RecyclerView mAccountRecyclerView;
    @BindView(R.id.home_page_people_button)
    Button focusPeopleBtn;

    @Inject
    HomePagePresenter mPresenter;

    List<String> info;//滚动新闻
    AMap mAMap;
    UiSettings mUiSettings;//定义一个UiSettings对象
    PolylineOptions polylineOption;
    Marker marker;
    Polygon polygon;
    Unbinder mUnbinder;

    List<Drawable> images ;

    HomeRegionAdapter mHomeRegionAdapter;
    HomeUnitAdapter mHomeUnitAdapter;
    HomeAccountAdapter mHomeAccountAdapter;


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
        initView();
        initPresenter();
        //在activity执行onCreate时执行mapView.onCreate(saveInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        initMap();

        return view;
    }

    private void initView() {
        info = new ArrayList<>();
        //垂直广告 或者新闻
        info.add("1. 大家好，我是新闻一号。");
        info.add("2. 大家好，我是新闻二号");
        info.add("3. 大家好，我是新闻三号");
        info.add("4. 大家好，我是新闻四号");

        marqueeNewView.startWithList(info, R.anim.anim_bottom_in, R.anim.anim_top_out);

        images= new ArrayList<>();
        Resources resources = getContext().getResources();
        images.add(resources.getDrawable(R.drawable.a));
        images.add(resources.getDrawable(R.drawable.b));
        images.add(resources.getDrawable(R.drawable.c));
        images.add(resources.getDrawable(R.drawable.d));
        images.add(resources.getDrawable(R.drawable.e));
        images.add(resources.getDrawable(R.drawable.f));

        mRegionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mHomeRegionAdapter = new HomeRegionAdapter(getContext());
        mRegionRecyclerView.setAdapter(mHomeRegionAdapter);
        mHomeRegionAdapter.setImgs(images);

        mUnitRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mHomeUnitAdapter = new HomeUnitAdapter(getContext());
        mUnitRecyclerView.setAdapter(mHomeUnitAdapter);
        mHomeUnitAdapter.setImgs(images);

        mAccountRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mHomeAccountAdapter = new HomeAccountAdapter(getContext());
        mAccountRecyclerView.setAdapter(mHomeAccountAdapter);
        mHomeAccountAdapter.setImgs(images);

        focusPeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), FocusGroupActivity.class);
                startActivity(intent);
            }
        });

    }


    private void initMap() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            setUpMap();
        } else {
            mAMap.clear();
            mAMap = mMapView.getMap();
            setUpMap();
        }

//        setUpMap();
    }

    private void setUpMap() {
        mAMap.setOnMapClickListener(this);
        //UiSettings 主要是对地图上的控件的管理，比如指南针、logo位置（不能隐藏）.....
        mUiSettings = mAMap.getUiSettings();

//        //隐藏缩放按钮
//        mUiSettings.setZoomControlsEnabled(false);
//        //缩放手势
//        mUiSettings.setZoomGesturesEnabled(false);
//        //滑动手势
//        mUiSettings.setScrollGesturesEnabled(false);

//
//        LatLng latLng = new LatLng(38.428446,115.834866);//构造一个位置
//        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,12));

        DistrictSearch search = new DistrictSearch(getContext());
        DistrictSearchQuery query = new DistrictSearchQuery();
        query.setKeywords("肃宁县");
        query.setShowBoundary(true);
        search.setQuery(query);
        search.setOnDistrictSearchListener(this);

        search.searchDistrictAsyn();
        //115.749215,38.448218   115.806893,38.370739 115.871438,38.336277  115.937356,38.44284

        // 定义多边形的4个点点坐标
        LatLng latLng1 = new LatLng(38.448218, 115.749215);
        LatLng latLng2 = new LatLng(38.370739, 115.806893);
        LatLng latLng3 = new LatLng(38.336277, 115.871438);
        LatLng latLng4 = new LatLng(38.44284, 115.937356);

        List<LatLng> latlngs = new ArrayList<>();
        latlngs.add(latLng1);
        latlngs.add(latLng2);
        latlngs.add(latLng3);
        latlngs.add(latLng4);

        // 绘制一个长方形
        polygon = mAMap.addPolygon(new PolygonOptions()
                .addAll(latlngs)
                .fillColor(Color.LTGRAY)
                .strokeColor(Color.RED)
                .strokeWidth(1));
    }

    private void initPresenter() {
        DaggerHomePageComponent.builder()
                .netComponent(SuNingFireDepartmentApplication.get(getContext()).getNetComponent())
                .homePageModule(new HomePageModule(this))
                .build()
                .inject(this);
    }


    @Override
    public void onDistrictSearched(DistrictResult districtResult) {
        if (districtResult == null || districtResult.getDistrict() == null) {
            return;
        }
        if (districtResult.getAMapException() != null && districtResult.getAMapException().getErrorCode() == AMapException.CODE_AMAP_SUCCESS) {
            final DistrictItem item = districtResult.getDistrict().get(0);
            if (item == null) {
                return;
            }
            LatLonPoint centerLatLng = item.getCenter();
            if (centerLatLng != null) {
                mAMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(new LatLng(centerLatLng.getLatitude(), centerLatLng.getLongitude()), 11.5f));
            }


            new Thread() {
                @Override
                public void run() {

                    String[] polyStr = item.districtBoundary();
                    if (polyStr == null || polyStr.length == 0) {
                        return;
                    }
                    for (String str : polyStr) {
                        String[] lat = str.split(";");
                        polylineOption = new PolylineOptions();
                        boolean isFirst = true;
                        LatLng firstLatLng = null;
                        for (String latstr : lat) {
                            String[] lats = latstr.split(",");
                            if (isFirst) {
                                isFirst = false;
                                firstLatLng = new LatLng(Double
                                        .parseDouble(lats[1]), Double
                                        .parseDouble(lats[0]));
                            }
                            polylineOption.add(new LatLng(Double
                                    .parseDouble(lats[1]), Double
                                    .parseDouble(lats[0])));
                        }
                        if (firstLatLng != null) {
                            polylineOption.add(firstLatLng);
                        }

                        polylineOption.width(10).color(Color.BLUE);
                        Message message = new Message();
                        message.what = 1;
                        mHandler.sendMessage(message);
                    }
                }
            }.start();
        } else {
            if (districtResult.getAMapException() != null)
                ToastUtil.showerror(getContext(), districtResult.getAMapException().getErrorCode());
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        if (marker != null) {
            marker.remove();
        }
        marker = mAMap.addMarker(new MarkerOptions().position(latLng));
        boolean b1 = polygon.contains(latLng);
        Toast.makeText(getContext(), "是否在围栏里面：" + b1, Toast.LENGTH_SHORT).show();
    }

    /**
     * 必须重写以下方法
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        Logger.d("HomePageFragment Resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mUnbinder.unbind();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void updateListUI() {

    }

    @Override
    public void showOnFailuer() {

    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAMap.addPolyline(polylineOption);
        }
    };

}
