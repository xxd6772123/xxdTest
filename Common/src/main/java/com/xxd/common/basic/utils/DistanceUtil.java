package com.xxd.common.basic.utils;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:距离工具类
 */
public class DistanceUtil {
    private static final float ONE_KM = 1000f;

    /**
     * 将以米为单位的长度转换为合适字符串长度
     *
     * @param len
     * @param precision
     * @return
     */
    public static String meter2FitStr(float len, int precision) {
        if (len < ONE_KM) {
            return String.format("%." + precision + "fM", len);
        }

        return String.format("%." + precision + "fKM", len / ONE_KM);
    }

}
