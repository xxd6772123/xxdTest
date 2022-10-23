package com.xxd.common.basic.utils;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:进度转换工具类
 */
public class ProgressConvertUtil {


    /**
     * 将当前值转换为对应的进度值
     * @param curValue 当前取值
     * @param minValue 最小取值
     * @param maxValue 最大取值
     * @param maxProgress minValue到maxValue范围对应的最大进度
     * @return
     */
    public static int value2Progress(float curValue, float minValue, float maxValue, int maxProgress) {
        if (curValue < minValue) {
            curValue = minValue;
        }
        if (curValue > maxValue) {
            curValue = maxValue;
        }
        float percent = (curValue - minValue) / (maxValue - minValue);
        int progress = (int) (percent * maxProgress);
        return progress;
    }

    /**
     * 将当前进度值转换为对应实际的值
     * @param curProgress 当前进度
     * @param minValue 最小取值
     * @param maxValue 最大取值
     * @param maxProgress 最大进度
     * @return
     */
    public static float progress2value(int curProgress, float minValue, float maxValue, int maxProgress) {
        if (curProgress < 0) {
            curProgress = 0;
        }
        if (curProgress > maxProgress) {
            curProgress = maxProgress;
        }
        float curValue = minValue + (maxValue - minValue) * (curProgress * 1f / maxProgress);
        curValue = ((int)(curValue * 100)) / 100f;//保留小数位数2位
        return curValue;
    }
}
