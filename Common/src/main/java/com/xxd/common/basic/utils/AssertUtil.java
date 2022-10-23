package com.xxd.common.basic.utils;

import android.text.TextUtils;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:中断工具类
 */
public class AssertUtil {

    /**
     * 判断checkStr是否为空，若为空则抛异常中断
     *
     * @param checkStr
     * @param assertMsg
     */
    public static void assertForEmpty(String checkStr, String assertMsg) {
        if (TextUtils.isEmpty(checkStr)) {
            throw new RuntimeException(assertMsg);
        }
    }

    /**
     * 判断obj是否为空，若为空则抛异常中断
     *
     * @param obj
     * @param assertMsg
     */
    public static void assertForNull(Object obj, String assertMsg) {
        if (obj == null) {
            throw new RuntimeException(assertMsg);
        }
    }

}
