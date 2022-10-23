package com.xxd.common.basic.utils;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:判断是否为快速点击处理的工具类
 */
public class FastClickUtil {

    /**两次点击之间的最小间隔时长 单位毫秒*/
    private static final long MIN_CLICK_INTERVAL = 500;
    /**记录的上次点击时间*/
    private static long sLastClickTime = 0;

    /**
     * 快速点击判断处理
     * @return true：表示当前收到的点击事件为快速点击的事件
     */
    public static boolean isFastClick() {
        long curClickTime = System.currentTimeMillis();
        if (curClickTime - sLastClickTime < MIN_CLICK_INTERVAL) {
            return true;
        }
        sLastClickTime = curClickTime;
        return false;
    }
}
