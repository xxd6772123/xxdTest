package com.xxd.common.basic.utils;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:线程工具类
 */
public class XddThreadUtil {

    /**
     * 线程睡眠方法，内部处理了异常
     *
     * @param millis
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
