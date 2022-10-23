package com.xxd.common.basic.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.Utils;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */
public class DensityUtil {

    private static Context sContext = Utils.getApp();

    /**
     * 获取屏幕宽度（像素）
     *
     * @param context 上下文
     * @return px
     */
    public static int getWith(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度（像素）
     *
     * @param context 上下文
     * @return px
     */
    public static int getHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    public static float getDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     * 获取状态栏的高度
     *
     * @param context 上下文
     * @return px
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 获取标题栏（ActionBar）的高度
     *
     * @param context 上下文
     * @return px
     */
    public static int getActionBarHeight(Context context) {
        TypedArray values = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int actionBarHeight = values.getDimensionPixelSize(0, 0);
        values.recycle();
        return actionBarHeight;
    }

    /**
     * 将单位为dp的值转换为单位为px的对应的值
     *
     * @param dpValue
     * @return
     */
    public static int dip2px(float dpValue) {
        return SizeUtils.dp2px(dpValue);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        return SizeUtils.dp2px(dpValue);
    }

    /**
     * 将单位为px的值转换为单位为dp的对应的值
     *
     * @param pxValue
     * @return
     */
    public static int px2dip(float pxValue) {
        return SizeUtils.px2dp(pxValue);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        return SizeUtils.px2dp(pxValue);
    }

    /**
     * 将单位为sp的值转换为单位px的对应的值
     *
     * @param spValue The value of sp.
     * @return value of px
     */
    public static int sp2px(final float spValue) {
        return SizeUtils.sp2px(spValue);
    }

    /**
     * 将单位为px的值转换为单位为sp的对应的值
     *
     * @param pxValue The value of px.
     * @return value of sp
     */
    public static int px2sp(final float pxValue) {
        return SizeUtils.px2sp(pxValue);
    }

}
