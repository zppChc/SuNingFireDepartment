package com.yatai.suningfiredepartment.util;

import com.amap.api.maps2d.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class LngLat2LatLng {
    /**
     * 将经纬度 转化为 纬度经度 以用来适配 高德地图的api
     *  longitude 经度
     *  latitude 纬度
     */
    public static List<LatLng> convertLngLat2LatLng(List<List<Double>> list){
        List<LatLng>  latLngList = new ArrayList<>();
        for (int i = 0; i<list.size();i++){
            LatLng latLng = new LatLng(list.get(i).get(1),list.get(i).get(0));
            latLngList.add(latLng);
        }
        return latLngList;
    }
}
