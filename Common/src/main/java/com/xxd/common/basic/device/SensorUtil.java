package com.xxd.common.basic.device;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.List;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:获取手机传感器信息
 */
public final class SensorUtil {

    /**
     * @param context
     * @return 设备支持的所有传感器
     */
    public static List<Sensor> getSensorList(Context context){
        SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        return manager.getSensorList(Sensor.TYPE_ALL);
    }

    /**
     * @param context
     * @param senor 传感器类型ID，见Sensor类里全部ID，
     * {@link android.hardware.Sensor.STRING_TYPE_GYROSCOPE}
     * @return
     */
    public static Sensor getSensor(Context context,int senor){
        SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        return manager.getDefaultSensor(senor);
    }
}
