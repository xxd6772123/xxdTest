package com.xxd.common.basic.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.blankj.utilcode.util.Utils;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:位置工具类
 */
public class LocationUtil {

    /**
     * 判断系统当前是否已开启定位功能
     *
     * @return
     */
    public static boolean isLocationEnabled() {
        Context context = Utils.getApp();
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        }
        return false;
    }

    /**
     * 获取俩经纬度点之间的直线距离，单位为米
     *
     * @param startLatitude  起始点的纬度
     * @param startLongitude 起始点的经度
     * @param endLatitude    结束点的纬度
     * @param endLongitude   结束点的经度
     * @return 两点间的直线距离
     */
    public static float distanceBetween(double startLatitude, double startLongitude,
                                        double endLatitude, double endLongitude) {
        float[] ret = new float[1];
        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, ret);
        return ret[0];
    }
}
