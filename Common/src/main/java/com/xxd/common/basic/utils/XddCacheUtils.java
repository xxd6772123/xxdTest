package com.xxd.common.basic.utils;

import androidx.annotation.Keep;

import com.blankj.utilcode.util.CleanUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PathUtils;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:应用缓存工具类
 */
@Keep
public class XddCacheUtils {



    /**
     * 获取应用内部缓存目录大小
     *
     * @return
     */
    public static long getAppInternalCacheSize() {
        String path = PathUtils.getInternalAppCachePath();
        return FileUtils.getLength(path);
    }

    /**
     * 获取应用内部缓存目录大小
     *
     * @return
     */
    public static String getAppInternalCacheSizeStr() {
        String path = PathUtils.getInternalAppCachePath();
        return FileUtils.getSize(path);
    }

    /**
     * 获取应用外部缓存目录大小
     *
     * @return
     */
    public static long getAppExternalCacheSize() {
        String path = PathUtils.getExternalAppCachePath();
        return FileUtils.getLength(path);
    }

    /**
     * 获取应用内部缓存目录大小
     *
     * @return
     */
    public static String getAppExternalCacheSizeStr() {
        String path = PathUtils.getExternalAppCachePath();
        return FileUtils.getSize(path);
    }

    /**
     * 获取应用内部和外部缓存总大小
     *
     * @return
     */
    public static long getAppIeCacheSize() {
        return getAppInternalCacheSize() + getAppExternalCacheSize();
    }

    /**
     * 获取应用内部和外部缓存总大小
     *
     * @return
     */
    public static String getAppIeCacheSizeStr() {
        long size = getAppIeCacheSize();
        return ConvertUtils.byte2FitMemorySize(size);
    }

    /**
     * 清除应用内部缓存
     *
     * @return
     */
    public static boolean cleanAppInternalCache() {
        return CleanUtils.cleanInternalCache();
    }

    /**
     * 清除应用外部缓存
     *
     * @return
     */
    public static boolean cleanAppExternalCache() {
        return CleanUtils.cleanExternalCache();
    }

    /**
     * 清除应用内部和外部所有的缓存
     *
     * @return
     */
    public static boolean cleanAppIeCache() {
        return cleanAppInternalCache() && cleanAppExternalCache();
    }
}
