package com.xxd.common.basic.utils;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */
public final class MathUtil {

    public static int randomInt(int min,int max){
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static double randomDouble(double min,double max){
        return (Math.random() * (max - min) + min);
    }

}
