package com.xxd.common.basic.device;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.annotation.RequiresPermission;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:设置桌面壁纸或者锁屏壁纸，支持动态壁纸
 */
public class WallpaperUtil {


    /**
     * 设置手机桌面或者锁屏壁纸
     * @param context
     * @param b 图片
     * @param which {@link WallpaperManager.FLAG_SYSTEM} 设置桌面壁纸,{@link WallpaperManager.FLAG_LOCK} 设置锁屏壁纸.
     * @return true 设置成功或失败
     */
    @RequiresPermission(Manifest.permission.SET_WALLPAPER)
    public static boolean setWallpaper(final Context context, Bitmap b, int which){
        WallpaperManager manager = WallpaperManager.getInstance(context.getApplicationContext());
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                manager.setBitmap(b,null,false,which);
            }else{
                manager.setBitmap(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置手机桌面或者壁纸，支持动图gif资源
     * @param context
     * @param resId res下的图片或者动图资源id
     * @param which {@link WallpaperManager.FLAG_SYSTEM} 设置桌面壁纸,{@link WallpaperManager.FLAG_LOCK} 设置锁屏壁纸.
     * @return true 设置成功或失败
     */
    @RequiresPermission(Manifest.permission.SET_WALLPAPER)
    public static boolean setWallpaper(final Context context,int resId, int which){
        WallpaperManager manager = WallpaperManager.getInstance(context.getApplicationContext());
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                manager.setResource(resId,which);
            }else{
                manager.setResource(resId);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置手机桌面或者壁纸，支持动图gif和视频资源
     * @param context
     * @param is 图片或者动态图或者视频流
     * @param which WallpaperManager.FLAG_SYSTEM} 设置桌面壁纸,{@link WallpaperManager.FLAG_LOCK 设置锁屏壁纸.
     * @return true 设置成功或失败
     */
    @RequiresPermission(Manifest.permission.SET_WALLPAPER)
    public static boolean setWallpaper(final Context context, InputStream is, int which){
        WallpaperManager manager = WallpaperManager.getInstance(context.getApplicationContext());
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                manager.setStream(is,null,false,which);
            }else{
                manager.setStream(is);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
