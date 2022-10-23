package com.xxd.common.basic.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.Utils;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */
public final class XddDrawableUtil {


    private static final String TAG = XddDrawableUtil.class.getSimpleName();


    /**
     * 获取控件背景
     *
     * @param solidColor  填充颜色
     * @param width       边框宽度
     * @param strokeColor 边框颜色
     * @param radius      圆角半径
     * @return
     */
    public static GradientDrawable getBgDrawable(int solidColor, int width, int strokeColor, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(width, strokeColor);
        drawable.setColor(solidColor);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    public static GradientDrawable getBgDrawable(int width, int strokeColor, float radius) {
        return getBgDrawable(Color.TRANSPARENT, width, strokeColor, radius);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static GradientDrawable getBgDrawable(int[] solidColors, int width, ColorStateList strokeState, float radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(width, strokeState);
        drawable.setColors(solidColors);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    /**
     * 调整drawableId对应的GradientDrawable的圆角半径
     *
     * @param drawableId
     * @param radius
     * @return
     */
    public static Drawable changeGradientDrawableRadius(@DrawableRes int drawableId, float radius) {
        Drawable drawable = Utils.getApp().getResources().getDrawable(drawableId);
        if (drawable instanceof GradientDrawable) {
            ((GradientDrawable) drawable).setCornerRadius(radius);
        }
        return drawable;
    }

}
