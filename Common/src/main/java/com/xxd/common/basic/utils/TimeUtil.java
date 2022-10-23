package com.xxd.common.basic.utils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.constant.TimeConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:时间相关工具类
 */
public class TimeUtil {


    /***
     * 时间戳转换为时间 yyyy-MM-dd HH:mm:ss
     */
    public static final String FORMAT_yyyy_MM_dd_hh_mm_ss = "yyyy-MM-dd HH:mm:ss";

    /***
     * 时间戳转换为时间 "yyyy-MM-dd"
     */
    public static final String FORMAT_yyyy_MM_dd = "yyyy-MM-dd";
    /***
     * 时间戳转换为时间 "HH:mm:ss"
     */
    public static final String FORMAT_hh_mm_ss = "HH:mm:ss";
    /***
     * 时间戳转换为时间 "mm:ss"
     */
    public static final String FORMAT_mm_ss = "mm:ss";
    /**
     * 时间戳转换为时间 "yyyy年MM月dd日"
     */
    public static final String FORMAT_CN_YMD = "yyyy年MM月dd日";
    /**
     * 时间戳转换为时间 "yyyy年MM月"
     */
    public static final String FORMAT_CN_YM = "yyyy年MM月";
    /**
     * 时间戳转换为时间 "yyyy年"
     */
    public static final String FORMAT_CN_YYYY = "yyyy年";
    /**
     * 时间戳转换为时间 "MM月"
     */
    public static final String FORMAT_CN_MM = "MM月";

    private TimeUtil() {
    }

    /***
     * 时间戳转换为时间
     *
     * @param time    时间
     * @param pattern 字符串输出的时间格式
     * @return 按照format样式返回时间字符串
     */
    public static String timeByPattern(long time, String pattern) {
        return timeByPattern(time, pattern, null);
    }

    /**
     * 时间戳转换为时间
     *
     * @param time     单位：毫秒
     * @param pattern
     * @param timeZone
     * @return
     */
    public static String timeByPattern(long time, String pattern, TimeZone timeZone) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        if (timeZone != null) {
            format.setTimeZone(timeZone);
        }
        Date date = new Date(time);
        return format.format(date);
    }

    /***
     * 将当前时间戳System.currentTimeMillis()转换为时间
     *
     * @param pattern 字符串输出的时间格式
     * @return 按照format样式返回时间字符串
     */
    public static String timeByPattern(String pattern) {
        return timeByPattern(System.currentTimeMillis(), pattern);
    }

    /**
     * 将时长转换为形如mm:ss的字符串
     *
     * @param milliseconds
     * @return
     */
    public static String getMmss(long milliseconds) {
        //return timeByPattern(milliseconds, FORMAT_mm_ss);
        long seconds = milliseconds / 1000L;
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }

    /**
     * 将时长转换为形如 hh:mm:ss的字符串
     *
     * @param milliseconds
     * @return
     */
    public static String getHHmmss(long milliseconds) {
        /*TimeZone timeZone = TimeZone.getTimeZone("UTC");
        return timeByPattern(milliseconds, FORMAT_hh_mm_ss, timeZone);*/
        long seconds = milliseconds / 1000L;
        return String.format("%02d:%02d:%02d", seconds / 3600, seconds / 60 % 60, seconds % 60);
    }

    /**
     * 根据传入的时间获取形如yyyy年MM月dd日的字符串
     *
     * @param milliseconds
     * @return
     */
    public static String getCnYmd(long milliseconds) {
        return TimeUtil.timeByPattern(milliseconds, FORMAT_CN_YMD);
    }

    /**
     * 根据传入的时间获取形如yyyy年MM月的字符串
     *
     * @param milliseconds
     * @return
     */
    public static String getCnYm(long milliseconds) {
        return TimeUtil.timeByPattern(milliseconds, FORMAT_CN_YM);
    }

    /**
     * 根据传入的时间获取形如yyyy年的字符串
     *
     * @param milliseconds
     * @return
     */
    public static String getCnYyyy(long milliseconds) {
        return TimeUtil.timeByPattern(milliseconds, FORMAT_CN_YYYY);
    }

    /**
     * 根据传入的时间获取形如MM月的字符串
     *
     * @param milliseconds
     * @return
     */
    public static String getCnMm(long milliseconds) {
        return TimeUtil.timeByPattern(milliseconds, FORMAT_CN_MM);
    }

    /**
     * 获取某一时间为今天或明天，则返回对应的今天、明天字符串 否则返回“”
     *
     * @param milliseconds
     * @return
     */
    public static String getFriendlyTodayTomorrow(long milliseconds) {
        long now = System.currentTimeMillis();
        //先得到当天的0点整那一刻
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.setTimeInMillis(now);
        nowCalendar.set(Calendar.HOUR_OF_DAY, 0);
        nowCalendar.set(Calendar.MINUTE, 0);
        nowCalendar.set(Calendar.SECOND, 0);
        nowCalendar.set(Calendar.MILLISECOND, 0);

        //传入时间所表示的当天0点整那一刻
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long interval = nowCalendar.getTimeInMillis() - calendar.getTimeInMillis();
        if (interval <= 0) {
            return "今天";
        } else if (interval == TimeConstants.DAY) {
            return "昨天";
        }
        return "";
    }

    /**
     * 获取Date所在的月的天数
     *
     * @param date
     * @return
     */
    public static int getDaysOfMonth(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
