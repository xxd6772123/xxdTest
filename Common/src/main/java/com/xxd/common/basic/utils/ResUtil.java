package com.xxd.common.basic.utils;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

import com.blankj.utilcode.util.Utils;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:资源工具类
 */
public class ResUtil {
    /**
     * 获取字符串资源
     *
     * @param resId
     * @return
     */
    public static String getStr(@StringRes int resId) {
        return Utils.getApp().getResources().getString(resId);
    }

    /**
     * 获取颜色值
     *
     * @param resId
     * @return
     */
    @ColorInt
    public static int color(@ColorRes int resId) {
        return Utils.getApp().getResources().getColor(resId);
    }
}
