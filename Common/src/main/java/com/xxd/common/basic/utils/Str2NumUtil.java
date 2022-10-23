package com.xxd.common.basic.utils;

import androidx.annotation.NonNull;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:将文本转换为数值的工具类
 */
public class Str2NumUtil {

    /**
     * 将数字文本text解析得到对应的float数值
     *
     * @param text 待解析的数字字符串
     * @return
     */
    public static float parse(String text) {
        return parse(text, 0f);
    }

    /**
     * 将数字文本text解析得到对应的float数值
     *
     * @param text     待解析的数字字符串
     * @param defValue 解析出错时返回的默认值
     * @return
     */
    public static float parse(String text, float defValue) {
        float value = defValue;
        try {
            value = Float.parseFloat(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 将数字文本解析得到对应的int数值
     *
     * @param text     待解析的数字字符串
     * @param defValue 解析出错时返回的默认值
     * @return
     */
    public static int parseInt(@NonNull String text, int defValue) {
        int ret = defValue;
        try {
            ret = Integer.parseInt(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
