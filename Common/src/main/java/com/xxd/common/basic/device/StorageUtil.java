package com.xxd.common.basic.device;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:存储工具类  https://blog.csdn.net/u010937230/article/details/73303034
 */
public final class StorageUtil {

    /**
     * 判断SD是否挂载
     */
    public static boolean isSDCardMount() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取 手机 RAM 信息
     * @param context
     * @return MemoryInfo对象,通过 {@link Formatter.formatFileSize(Context,long) }来转换为字符串
     */
    public static ActivityManager.MemoryInfo getRAMInfo(Context context){
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    /**
     * 获取内置存储的外部存储文件系统信息
     * @return 返回外部存储StatFs
     */
    public static StatFs getExternalMemory(){
        return new StatFs(Environment.getExternalStorageDirectory().getPath());
    }

    /**
     * 获取内置存储的内部存储文件系统信息
     * @return 返回内部存储StatFs
     */
    public static StatFs getInternalMemory(){
        return new StatFs(Environment.getDataDirectory().getPath());
    }

    /**
     * 获取外置外部存储的文件系统信息
     * @return 返回外置外部存储 StatFs数组，可能有多个TF卡信息,为0表示无外置SD
     */
    public static List<StatFs> getSDCardMemory(Context context){
        List<StatFs> list = new ArrayList<>();
        String internalRoot = Environment.getExternalStorageDirectory().getAbsolutePath().toLowerCase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            for (File f:context.getExternalFilesDirs(null)){
                if(f != null) {
                    String path = f.getPath().split("/Android")[0];
                    if (path.toLowerCase().equals(internalRoot)) {
                        continue;
                    }
                    boolean validPath = false;
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        validPath = Environment.isExternalStorageRemovable(f);
                    }
                    else{
                        validPath = Environment.MEDIA_MOUNTED.equals(Environment.getStorageState(f));
                    }
                    if(validPath){
                        list.add(new StatFs(path));
                    }
                }
            }
        }
        return list;
    }

    /**
     * 从文件系统信息中获取可用存储空间
     * @return 返回可用存储空间
     */
    public static String getAvailableMemorySize(Context context,StatFs statFs){
        long availableBlocks = 0;
        long blockSize = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            availableBlocks = statFs.getAvailableBlocksLong();
            blockSize = statFs.getBlockSizeLong();
        }
        return Formatter.formatFileSize(context, availableBlocks
                * blockSize);
    }

    /**
     * 从文件系统信息中获取总存储空间
     * @return 返回总存储空间
     */
    public static String getTotalMemorySize(Context context, StatFs statFs){
        long totalBlocks = 0;
        long blockSize = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            totalBlocks = statFs.getBlockCountLong();
            blockSize = statFs.getBlockSizeLong();
        }
        return Formatter.formatFileSize(context, totalBlocks
                * blockSize);
    }
}
