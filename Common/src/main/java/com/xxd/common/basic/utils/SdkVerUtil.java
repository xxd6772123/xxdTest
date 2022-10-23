package com.xxd.common.basic.utils;

import android.os.Build;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:安卓平台版本工具类
 */
public class SdkVerUtil {

    /**
     * 是否为Android 10以上版本（Android10及以上版本采用了分区存储）
     *
     * @return
     */
    public static boolean isAndroidQ() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    /**
     * 是否为Android8.0以上版本
     *
     * @return
     */
    public static boolean isAndroidO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

}
