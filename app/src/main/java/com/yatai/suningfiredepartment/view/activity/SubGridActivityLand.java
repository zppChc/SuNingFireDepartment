package com.yatai.suningfiredepartment.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
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
import com.yatai.suningfiredepartment.R;
import com.yatai.suningfiredepartment.entity.DepartmentEntity;
import com.yatai.suningfiredepartment.entity.GridEntity;
import com.yatai.suningfiredepartment.entity.HomeWorkCategoryEntity;
import com.yatai.suningfiredepartment.entity.PeopleEntity;
import com.yatai.suningfiredepartment.entity.PlaceEntity;
import com.yatai.suningfiredepartment.util.ColorUtil;
import com.yatai.suningfiredepartment.util.LngLat2LatLng;
import com.yatai.suningfiredepartment.util.PreferenceUtils;
import com.yatai.suningfiredepartment.util.ToastUtil;
import com.yatai.suningfiredepartment.view.adapter.HomeCategoryAdapter;
import com.yatai.suningfiredepartment.view.adapter.HomePeopleAdapter;
import com.yatai.suningfiredepartment.view.adapter.HomePlaceAdapter;
import com.yatai.suningfiredepartment.view.adapter.HomeRegionAdapter;
import com.yatai.suningfiredepartment.view.adapter.HomeUnitAdapter;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubGridActivityLand extends AppCompatActivity implements AMap.OnMapClickListener {
    @BindView(R.id.title_name)
    TextView mGridNameTv;
    @BindView(R.id.title_image_back)
    ImageView mBackImg;
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.sub_grid_region_recycler_view)
    RecyclerView mRegionRecyclerView;
    @BindView(R.id.sub_grid_unit_recycler_view)
    RecyclerView mUnitRecyclerView;
    @BindView(R.id.sub_grid_place_recycler_view)
    RecyclerView mPlaceRecyclerView;
    @BindView(R.id.sub_grid_work_recycler_view)
    RecyclerView mWorkRecyclerView;
    @BindView(R.id.temp_sub_grid_unit)
    LinearLayout mUnitLayout;


    @BindView(R.id.temp_sub_grid_place)
    LinearLayout mPlaceLayout;
    @BindView(R.id.temp_sub_grid_region)
    LinearLayout mRegionLayout;
    @BindView(R.id.sub_grid_people_recycler_view)
    RecyclerView mPeopleRecyclerView;
    @BindView(R.id.temp_sub_grid_people)
    LinearLayout mPeopleLayout;
    @BindView(R.id.temp_sub_grid_work)
    LinearLayout mWorkLayout;


    private String gridId;
    private String gridName;
    private FinalHttp mHttp;
    private Context mContext;

    private AMap mAMap;
    private UiSettings mUiSettings;//定义一个UiSettings对象
    private PolylineOptions polylineOption;
    private Marker marker;
    private Polygon polygon;
    private List<Polygon> childPolygons;


    private HomeRegionAdapter mHomeRegionAdapter;
    private HomeUnitAdapter mHomeDepartmentAdapter;
    private HomePlaceAdapter mHomePlaceAdapter;
    private HomePeopleAdapter mHomePeopleAdapter;
    private HomeCategoryAdapter mHomeCategoryAdapter;


    private GridEntity mGridEntity;
    private List<GridEntity> childrenGridList;
    private List<DepartmentEntity> departmentList;
    private List<PeopleEntity> peopleList;
    private List<PlaceEntity> placeList;
    private List<HomeWorkCategoryEntity> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_sub_grid_land);

        ButterKnife.bind(this);
        Intent intent = getIntent();
        gridId = intent.getStringExtra("gridId");
        gridName = intent.getStringExtra("gridName");
        mGridNameTv.setText(gridName);
//        ToastUtil.show(this, "GridId: " + gridId);

        //在activity执行onCreate时执行mapView.onCreate(saveInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        initMap();
        initView();
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
    }

    private void initView() {
        childrenGridList = new ArrayList<>();
        departmentList = new ArrayList<>();
        childPolygons = new ArrayList<>();
        peopleList = new ArrayList<>();
        placeList = new ArrayList<>();
        categoryList = new ArrayList<>();

        mContext = this;

        mRegionRecyclerView.setLayoutManager(new LinearLayoutManager(SubGridActivityLand.this, LinearLayoutManager.HORIZONTAL, false));
        mHomeRegionAdapter = new HomeRegionAdapter(SubGridActivityLand.this);
        mHomeRegionAdapter.setListener(new HomeRegionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SubGridActivityLand.this, SubGridActivityLand.class);
                String gridId = String.valueOf(childrenGridList.get(position).getId());
                intent.putExtra("gridId", gridId);
                intent.putExtra("gridName",childrenGridList.get(position).getName());
                startActivity(intent);
            }
        });
        mRegionRecyclerView.setAdapter(mHomeRegionAdapter);
        mHomeRegionAdapter.setGridList(childrenGridList);
        mUnitRecyclerView.setLayoutManager(new LinearLayoutManager(SubGridActivityLand.this, LinearLayoutManager.HORIZONTAL, false));
        mHomeDepartmentAdapter = new HomeUnitAdapter(SubGridActivityLand.this);
        mHomeDepartmentAdapter.setListener(new HomeUnitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SubGridActivityLand.this, DepartmentWorkActivity.class);
                String departmentId = String.valueOf(departmentList.get(position).getId());
                intent.putExtra("id",departmentId);
                startActivity(intent);
            }
        });
        mUnitRecyclerView.setAdapter(mHomeDepartmentAdapter);
        mHomeDepartmentAdapter.setDepartmentList(departmentList);

        //重点地点。。。。没有实体类，等接口中出现数据在进行更改
        mPlaceRecyclerView.setLayoutManager(new LinearLayoutManager(SubGridActivityLand.this, LinearLayoutManager.HORIZONTAL, false));
        mHomePlaceAdapter = new HomePlaceAdapter(SubGridActivityLand.this);
        mPlaceRecyclerView.setAdapter(mHomePlaceAdapter);
        mHomePlaceAdapter.setList(placeList);

        mPeopleRecyclerView.setLayoutManager(new LinearLayoutManager(SubGridActivityLand.this, LinearLayoutManager.HORIZONTAL, false));
        mHomePeopleAdapter = new HomePeopleAdapter(SubGridActivityLand.this);
        mHomePeopleAdapter.setListener(new HomePeopleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SubGridActivityLand.this,FocusGroupActivity.class);
                Gson gson =new Gson();
                String peopleStr = gson.toJson(peopleList.get(position));
                intent.putExtra("people",peopleStr);
                startActivity(intent);
            }
        });
        mPeopleRecyclerView.setAdapter(mHomePeopleAdapter);
        mHomePeopleAdapter.setList(peopleList);

        mWorkRecyclerView.setLayoutManager(new LinearLayoutManager(SubGridActivityLand.this, LinearLayoutManager.HORIZONTAL, false));
        mHomeCategoryAdapter = new HomeCategoryAdapter(SubGridActivityLand.this);
        mHomeCategoryAdapter.setListener(new HomeCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SubGridActivityLand.this, SubWorkActivity.class);
                intent.putExtra("gridId", gridId);
                intent.putExtra("categoryId",categoryList.get(position).getId());
                startActivity(intent);
            }
        });
        mWorkRecyclerView.setAdapter(mHomeCategoryAdapter);
        mHomeCategoryAdapter.setList(categoryList);

        initRecyclerViewData();
        mBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpMap() {
        mAMap.setOnMapClickListener(this);
        //UiSettings 主要是对地图上的控件的管理，比如指南针、logo位置（不能隐藏）.....
        mUiSettings = mAMap.getUiSettings();

        //隐藏缩放按钮
        mUiSettings.setZoomControlsEnabled(true);
        //缩放手势
        mUiSettings.setZoomGesturesEnabled(true);
        //滑动手势
        mUiSettings.setScrollGesturesEnabled(true);
    }

    /**
     * 必须重写以下方法
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        int mCurrentOrientation = getResources().getConfiguration().orientation;
        //竖屏
        if ( mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT ) {

            Intent intent = new Intent(SubGridActivityLand.this, SubGridActivity.class);
            intent.putExtra("gridId", gridId);
            intent.putExtra("gridName",gridName);
            startActivity(intent);
            finish();
        }
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int mCurrentOrientation = getResources().getConfiguration().orientation;
        //竖屏
        if ( mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT ) {

            Intent intent = new Intent(SubGridActivityLand.this, SubGridActivity.class);
            intent.putExtra("gridId", gridId);
            intent.putExtra("gridName",gridName);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
                Intent intent = new Intent(SubGridActivityLand.this, SubGridActivity.class);
                String gridId = String.valueOf(childrenGridList.get(i).getId());
                intent.putExtra("gridId", gridId);
                intent.putExtra("gridName",childrenGridList.get(i).getName());
                startActivity(intent);
                break;
            }
        }
    }

    private void initRecyclerViewData() {
        String token = PreferenceUtils.getPerfString(mContext, "token", "");
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
                            mHomeDepartmentAdapter.setDepartmentList(departmentList);
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
                        ToastUtil.show(mContext, jb.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToastUtil.show(mContext, strMsg);
            }
        });
    }

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
}
