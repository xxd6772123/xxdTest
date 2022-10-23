package com.xxd.common.basic.utils;

import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:颜色处理工具类
 */
public final class XddColorUtil {

    private static final String TAG = XddColorUtil.class.getSimpleName();

    /**
     * 将颜色字符串数组解析成颜色值列表
     *
     * @param strColors
     * @return
     */
    public static List<Integer> getColors(CharSequence[] strColors) {
        List<Integer> intColorList = null;
        if (strColors == null || strColors.length == 0) {
            return intColorList;
        }

        String strColor = null;
        int intColor = 0;
        intColorList = new ArrayList<>();
        for (CharSequence charColor : strColors) {
            if (charColor == null) {
                Log.e(TAG, "getColors: the char color is null.");
                continue;
            }
            strColor = charColor.toString().trim();
            try {
                intColor = Color.parseColor(strColor);
                if (intColorList.contains(intColor)) {
                    Log.e(TAG, "getColors: the " + strColor + " is repeat");
                    continue;
                }
                intColorList.add(intColor);
            } catch (Exception e) {
                //e.printStackTrace();
                Log.e(TAG, "getColors: the " + strColor + " is error color string, the color string must like #FFFFFF or #00FFFFFF");
            }
        }
        return intColorList;
    }

}
