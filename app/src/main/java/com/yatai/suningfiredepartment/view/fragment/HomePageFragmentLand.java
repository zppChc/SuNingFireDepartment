package com.yatai.suningfiredepartment.view.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polygon;
import com.amap.api.maps2d.model.PolygonOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.google.gson.Gson;
import com.sunfusheng.marqueeview.MarqueeView;
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.DepartmentEntity;
import com.yatai.suningfiredepartment.entity.GridEntity;
import com.yatai.suningfiredepartment.entity.HomeWorkCategoryEntity;
import com.yatai.suningfiredepartment.entity.InfoEntity;
import com.yatai.suningfiredepartment.entity.PeopleEntity;
import com.yatai.suningfiredepartment.entity.PlaceEntity;
import com.yatai.suningfiredepartment.util.ColorUtil;
import com.yatai.suningfiredepartment.util.LngLat2LatLng;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.activity.DepartmentWorkActivity;
import com.yatai.suningfiredepartment.view.activity.FocusGroupActivity;
import com.yatai.suningfiredepartment.view.activity.FocusPlaceActivity;
import com.yatai.suningfiredepartment.view.activity.InfoListActivity;
import com.yatai.suningfiredepartment.view.activity.SubGridActivity;
import com.yatai.suningfiredepartment.view.activity.SubGridActivityLand;
import com.yatai.suningfiredepartment.view.activity.SubWorkActivity;
import com.yatai.suningfiredepartment.view.adapter.HomeCategoryAdapter;
import com.yatai.suningfiredepartment.view.adapter.HomePeopleAdapter;
import com.yatai.suningfiredepartment.view.adapter.HomePlaceAdapter;
import com.yatai.suningfiredepartment.view.adapter.HomeRegionAdapter;
import com.yatai.suningfiredepartment.view.adapter.HomeUnitAdapter;
import com.yatai.suningfiredepartment.view.widget.MapContainer;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomePageFragmentLand extends Fragment implements AMap.OnMapClickListener  {

    @BindView(R.id.title_name)
    TextView mPageNameTv;
    @BindView(R.id.grid_num)
    TextView mGridNum;
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.marquee_new_view_one)
    MarqueeView marqueeNewViewOne;
    //相关网格 中网格
    @BindView(R.id.home_page_region_recycler_view)
    RecyclerView mRegionRecyclerView;
    //相关单位
    @BindView(R.id.home_page_unit_recycler_view)
    RecyclerView mUnitRecyclerView;
    //工作台账
    @BindView(R.id.home_page_place_recycler_view)
    RecyclerView mPlaceRecyclerView;
    //重点人群
    @BindView(R.id.home_page_people_recycler_view)
    RecyclerView mPeopleRecyclerView;
    //工作台账
    @BindView(R.id.home_page_work_recycler_view)
    RecyclerView mWorkRecyclerView;

    //相关网格 layout
    @BindView(R.id.temp_home_page_region)
    LinearLayout mRegionLayout;
    //相关单位 layout
    @BindView(R.id.temp_home_page_unit)
    LinearLayout mUnitLayout;
    //重点区域
    @BindView(R.id.temp_home_page_place)
    LinearLayout mPlaceLayout;
    //重点人群 layout
    @BindView(R.id.temp_home_page_people)
    LinearLayout mPeopleLayout;
    //工作台账 layout
    @BindView(R.id.temp_home_page_work)
    LinearLayout mWorkLayout;

    List<String> info;
    List<String> infoOne;//滚动新闻
    List<String> infoTwo;
    AMap mAMap;
    UiSettings mUiSettings;//定义一个UiSettings对象
    PolylineOptions polylineOption;
    Marker marker;
    Polygon polygon;
    List<Polygon> childPolygons;
    Unbinder mUnbinder;

    HomeRegionAdapter mHomeRegionAdapter;
    HomeUnitAdapter mHomeUnitAdapter;
    HomePlaceAdapter mHomePlaceAdapter;
    HomePeopleAdapter mHomePeopleAdapter;
    HomeCategoryAdapter mHomeCategoryAdapter;


    private FinalHttp mHttp;
    private String gridId;
    private GridEntity mGridEntity;
    private List<GridEntity> childrenGridList;
    private List<DepartmentEntity> departmentList;
    private List<PeopleEntity> peopleList;
    private List<PlaceEntity> placeList;
    private List<HomeWorkCategoryEntity> categoryList;
//    private ProgressDialog mProgressDialog;


    public static HomePageFragmentLand newInstance(String gridId){
        Bundle args = new Bundle();
        args.putString("gridId", gridId);
        HomePageFragmentLand fragment = new HomePageFragmentLand();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //接收传来的数据getArguments
        Bundle data = getArguments();
        gridId = data.getString("gridId");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page_land,container,false);
        mUnbinder = ButterKnife.bind(this, view);
        initView();

        //在activity执行onCreate时执行mapView.onCreate(saveInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        return view;
    }

    private void initView() {
        initMap();
        childrenGridList = new ArrayList<>();
        departmentList = new ArrayList<>();
        peopleList = new ArrayList<>();
        placeList = new ArrayList<>();
        categoryList = new ArrayList<>();

        info = new ArrayList<>();
        infoOne = new ArrayList<>();
        infoTwo = new ArrayList<>();
        childPolygons = new ArrayList<>();
//
//        mProgressDialog = new ProgressDialog(getContext(),ProgressDialog.THEME_HOLO_DARK);
//        mProgressDialog.setMessage("正在加载...");
//        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        mProgressDialog.setCancelable(false);
//        mProgressDialog.show();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        info.add("欢迎使用 "+str);


        marqueeNewViewOne.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, TextView textView) {
                Intent intent = new Intent(getActivity(), InfoListActivity.class);
                intent.putExtra("gridId",gridId);
                startActivity(intent);
            }
        });
        startNews(info);

        mRegionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mHomeRegionAdapter = new HomeRegionAdapter(getContext());
        mHomeRegionAdapter.setListener(new HomeRegionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), SubGridActivityLand.class);
                String gridId = String.valueOf(childrenGridList.get(position).getId());
                intent.putExtra("gridId", gridId);
                intent.putExtra("gridName",childrenGridList.get(position).getName());
                intent.putExtra("gridLevel",childrenGridList.get(position).getGrid_level());
                startActivity(intent);
            }
        });
        mRegionRecyclerView.setAdapter(mHomeRegionAdapter);
        mHomeRegionAdapter.setGridList(childrenGridList);

        mUnitRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mHomeUnitAdapter = new HomeUnitAdapter(getContext());
        mHomeUnitAdapter.setListener(new HomeUnitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), DepartmentWorkActivity.class);
                String departmentId = String.valueOf(departmentList.get(position).getId());
                intent.putExtra("id",departmentId);
                startActivity(intent);
            }
        });
        mUnitRecyclerView.setAdapter(mHomeUnitAdapter);
        mHomeUnitAdapter.setDepartmentList(departmentList);

        //重点地点。。。。没有实体类，等接口中出现数据在进行更改
        mPlaceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mHomePlaceAdapter = new HomePlaceAdapter(getContext());
        mHomePlaceAdapter.setListener(new HomePlaceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity().getApplicationContext(), FocusPlaceActivity.class);
                Gson gson = new Gson();
                String placeStr = gson.toJson(placeList.get(position));
                intent.putExtra("place",placeStr);
                startActivity(intent);
            }
        });
        mPlaceRecyclerView.setAdapter(mHomePlaceAdapter);
        mHomePlaceAdapter.setList(placeList);

        mPeopleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mHomePeopleAdapter =  new HomePeopleAdapter(getContext());
        mHomePeopleAdapter.setListener(new HomePeopleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity().getApplicationContext(),FocusGroupActivity.class);
                Gson gson =new Gson();
                String peopleStr = gson.toJson(peopleList.get(position));
                intent.putExtra("people",peopleStr);
                startActivity(intent);
            }
        });
        mPeopleRecyclerView.setAdapter(mHomePeopleAdapter);
        mHomePeopleAdapter.setList(peopleList);

        mWorkRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mHomeCategoryAdapter = new HomeCategoryAdapter(getContext());
        mHomeCategoryAdapter.setListener(new HomeCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), SubWorkActivity.class);
                intent.putExtra("gridId", gridId);
                intent.putExtra("categoryId",categoryList.get(position).getId());
                startActivity(intent);
            }
        });
        mWorkRecyclerView.setAdapter(mHomeCategoryAdapter);
        mHomeCategoryAdapter.setList(categoryList);

        initNewsData();
        initRecyclerViewData();
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
        LatLng latLng = new LatLng(38.423343,115.829381);//构造一个位置
        mAMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,11));
        mAMap.setOnMapClickListener(this);
        //UiSettings 主要是对地图上的控件的管理，比如指南针、logo位置（不能隐藏）.....
        mUiSettings = mAMap.getUiSettings();

        //隐藏缩放按钮
        mUiSettings.setZoomControlsEnabled(false);
        //缩放手势
        mUiSettings.setZoomGesturesEnabled(false);
        //滑动手势
        mUiSettings.setScrollGesturesEnabled(false);
        //所有手势
        mUiSettings.setAllGesturesEnabled(false);
        //设置放缩图标在右下
        mUiSettings.setZoomPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (marker != null) {
            marker.remove();
        }
        marker = mAMap.addMarker(new MarkerOptions().position(latLng));
        marker.setVisible(false);
        for (int i = 0; i < childPolygons.size(); i++) {
            boolean b1 = childPolygons.get(i).contains(latLng);
            if (b1) {
                Intent intent = new Intent(getActivity(), SubGridActivityLand.class);
                String gridId = String.valueOf(childrenGridList.get(i).getId());
                intent.putExtra("gridId", gridId);
                intent.putExtra("gridName",childrenGridList.get(i).getName());
                startActivity(intent);
                break;
            }
        }
    }

    /**
     * 必须重写以下方法
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        startNews(info);
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


    private void initRecyclerViewData() {
        String token = PreferenceUtils.getPerfString(getContext(), "token", "");
        String url = getString(R.string.base_url) + "grid/" + gridId;
        mHttp = new FinalHttp();
        mHttp.addHeader("Authorization", "Bearer " + token);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200) {
                        JSONObject data = jb.getJSONObject("data");
                        Gson gson = new Gson();
                        //获取当前用户 grid 信息
                        JSONObject gridJb = data.getJSONObject("grid");
                        mGridEntity = gson.fromJson(gridJb.toString(), GridEntity.class);
                        mPageNameTv.setText(mGridEntity.getName());
                        switch (mGridEntity.getGrid_level()){
                            case 1:
                                mGridNum.setText("大网格  ");
                                break;
                            case 2:
                                mGridNum.setText("中网格  ");
                                break;
                            case 3:
                                mGridNum.setText("小网格  ");
                                break;
                            default:
                                mGridNum.setText("");
                                break;
                        }
                        String childrenCount = data.getString("childrenGridCount");
                        if (Integer.valueOf(childrenCount) != 0) {
                            mGridNum.append(getString(R.string.grid_start) + data.getString("childrenGridCount") + getString(R.string.grid_end));
                        }
                        mGridNum.setVisibility(View.VISIBLE);
                        List<LatLng> bottomLatLng = LngLat2LatLng.convertLngLat2LatLng(mGridEntity.getPolygon());
                        // 绘制一个长方形
                        addArea(ColorUtil.randomStrokeRgb(), ColorUtil.transparentColor(), bottomLatLng, 8);

                        LatLngBounds.Builder bottombounds = new LatLngBounds.Builder();
                        for (int i = 0; i < bottomLatLng.size(); i++) {
                            bottombounds.include(bottomLatLng.get(i));
                        }
                        //第二个参数为四周留空宽度
                        mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bottombounds.build(), 15));

                        //获取当前用户 下级 grid信息
                        JSONArray gridChildrenArray = data.getJSONArray("childrenGrid");
                        childrenGridList.clear();
                        childPolygons.clear();
                        if (gridChildrenArray.length() > 0) {
                            for (int i = 0; i < gridChildrenArray.length(); i++) {
                                GridEntity tempEntity = gson.fromJson(gridChildrenArray.getJSONObject(i).toString(), GridEntity.class);
                                childrenGridList.add(tempEntity);
                                List<LatLng> childLatLng = LngLat2LatLng.convertLngLat2LatLng(tempEntity.getPolygon());
                                addArea(ColorUtil.randomStrokeRgb(), ColorUtil.randomFillArgb(), childLatLng, 1);
                            }
                            mHomeRegionAdapter.setGridList(childrenGridList);
                        } else {
                            mRegionLayout.setVisibility(View.GONE);
                        }

                        //获取相关部门信息
                        JSONArray departmentArray = data.getJSONArray("department");
                        departmentList.clear();
                        if (departmentArray.length() > 0) {
                            for (int i = 0; i < departmentArray.length(); i++) {
                                DepartmentEntity departmentEntity = gson.fromJson(departmentArray.getJSONObject(i).toString(), DepartmentEntity.class);
                                departmentList.add(departmentEntity);
                            }
                            mHomeUnitAdapter.setDepartmentList(departmentList);
                        } else {
                            mUnitLayout.setVisibility(View.GONE);
                        }

                        //重点区域
                        JSONArray importantPlaceArray = data.getJSONArray("importantPlace");
                        placeList.clear();
                        if (importantPlaceArray.length() > 0) {
                            for (int i  =0; i<importantPlaceArray.length(); i++){
                                PlaceEntity placeEntity  = gson.fromJson(importantPlaceArray.getJSONObject(i).toString(),PlaceEntity.class);
                                placeList.add(placeEntity);
                            }
                            mHomePlaceAdapter.notifyDataSetChanged();
                        } else {
                            mPlaceLayout.setVisibility(View.GONE);
                        }

                        //重点人群
                        JSONArray importantPersonArray = data.getJSONArray("importantPerson");
                        peopleList.clear();
                        if (importantPersonArray.length() > 0) {
                            for (int i = 0; i<importantPersonArray.length(); i++){
                                PeopleEntity peopleEntity = gson.fromJson(importantPersonArray.get(i).toString(),PeopleEntity.class);
                                peopleList.add(peopleEntity);
                            }
                            mHomePeopleAdapter.notifyDataSetChanged();
                        } else {
                            mPeopleLayout.setVisibility(View.GONE);
                        }
                        //工作台账
                        JSONArray categoryArray = data.getJSONArray("taskCategory");
                        categoryList.clear();
                        if (categoryArray.length()>0){
                            for (int i = 0; i<categoryArray.length(); i++){
                                HomeWorkCategoryEntity workCategoryEntity = gson.fromJson(categoryArray.get(i).toString(),HomeWorkCategoryEntity.class);
                                categoryList.add(workCategoryEntity);
                            }
                            mHomeCategoryAdapter.notifyDataSetChanged();
                        }else{
                            mWorkLayout.setVisibility(View.GONE);
                        }
                    } else {
                        ToastUtil.show(getContext(), jb.getString("message"));
                    }
//                    mProgressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(getContext(), strMsg);
//                mProgressDialog.dismiss();
            }
        });
    }

    /**
     * 获取新闻信息
     */
    private void initNewsData() {
        String token = PreferenceUtils.getPerfString(getContext(), "token", "");
        String url = getString(R.string.base_url) + "infoListRecent";
        mHttp = new FinalHttp();
        mHttp.addHeader("Authorization", "Bearer " + token);
        mHttp.get(url, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject jb = new JSONObject(s);
                    if (jb.getInt("code") == 200) {
                        JSONArray data = jb.getJSONArray("data");
                        if (data.length() > 0) {
                            info.clear();
                            Gson gson = new Gson();
                            for (int i = 0; i < data.length(); i++) {
                                InfoEntity infoEntity = gson.fromJson(data.getJSONObject(i).toString(), InfoEntity.class);
                                info.add(infoEntity.getTitle());
                            }
                            startNews(info);
                        }
                    } else {
                        ToastUtil.show(getContext(), jb.getString("message"));
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


    // 绘制区域

    /**
     * @param strokeColor 边界颜色
     * @param fillColor   填充颜色
     * @param latLnglist
     */
    private void addArea(int strokeColor, int fillColor, List<LatLng> latLnglist, float strokeWidth) {
        // 定义多边形的属性信息
        PolygonOptions polygonOptions = new PolygonOptions();
        // 添加多个多边形边框的顶点
        for (LatLng latLng : latLnglist) {
            polygonOptions.add(latLng);
        }
        // 设置多边形的边框颜色，32位 ARGB格式，默认为黑色
        polygonOptions.strokeColor(strokeColor);
        // 设置多边形的边框宽度，单位：像素
        polygonOptions.strokeWidth(strokeWidth);
        // 设置多边形的填充颜色，32位ARGB格式
        polygonOptions.fillColor(fillColor); // 注意要加前两位的透明度
        // 在地图上添加一个多边形（polygon）对象
        polygon = mAMap.addPolygon(polygonOptions);
        childPolygons.add(polygon);
    }


    private void startNews(List<String> list){
        marqueeNewViewOne.startWithList(list, R.anim.anim_bottom_in, R.anim.anim_top_out);
    }
}
