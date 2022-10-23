package com.xxd.common.basic.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:与asset相关的工具类
 */
public class AssetUtil {

    public static final String ASSET_FILE_PREFIX = "file:///android_asset";

    /**
     * 根据所传的asset中的目录及文件名字生成文件路径
     *
     * @param dir      目录
     * @param fileName 文件名字
     * @return 文件路径
     */
    public static String getFilePath(String dir, @NonNull String fileName) {
        if (TextUtils.isEmpty(dir)) {
            return ASSET_FILE_PREFIX + File.separator + fileName;
        }
        if (dir.startsWith(File.separator)) {
            dir = dir.substring(1);
        }
        if (dir.endsWith(File.separator)) {
            dir = dir.substring(0, dir.length() - 1);
        }
        return ASSET_FILE_PREFIX + File.separator + dir + File.separator + fileName;
    }

    /**
     * 获取Asset下的dir目录下的所有资源文件的路径
     *
     * @param context
     * @param dir
     * @return
     */
    public static List<String> getFilePaths(Context context, String dir) {
        AssetManager assetManager = context.getAssets();
        try {
            if (dir == null) {
                dir = "";
            }
            if (dir.endsWith(File.separator)) {
                dir = dir.substring(0, dir.length() - 1);
            }
            String[] fileNames = assetManager.list(dir);
            if (fileNames != null) {
                List<String> filePathList = new ArrayList<>();
                for (String fileName : fileNames) {
                    filePathList.add(dir + File.separator + fileName);
                }
                return filePathList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取Asset下的dir目录下的所有资源文件的路径(file:///android_asset)
     *
     * @param context
     * @param dir
     * @return
     */
    public static List<String> getFilePathsWithAssetPrefix(Context context, String dir) {
        List<String> filePathList = getFilePaths(context, dir);
        if (filePathList != null) {
            List<String> retList = new ArrayList<>();
            for (String path : filePathList) {
                if (path.startsWith(File.separator)) {
                    retList.add(ASSET_FILE_PREFIX + path);
                } else {
                    retList.add(ASSET_FILE_PREFIX + File.separator + path);
                }
            }
            return retList;
        }
        return null;
    }

    /**
     * 将asset下的filePath对应的文件读取成字符串
     *
     * @param filePath asset下的文件路径
     * @return
     */
    public static String readFile2Str(@NonNull String filePath) {
        return readFile2Str(filePath, Charset.defaultCharset());
    }

    /**
     * 将asset下的filePath对应的文件读取成字符串
     *
     * @param filePath asset下的文件路径
     * @param charset  字符集
     * @return
     */
    public static String readFile2Str(@NonNull String filePath, Charset charset) {
        AssetManager assetManager = Utils.getApp().getAssets();
        try {
            InputStream is = assetManager.open(filePath);
            return IOUtil.readStream2Str(is, charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据图片路径获取assets下的图片的bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmap(@NonNull String filePath) {
        Bitmap bitmap = null;
        AssetManager assetManager = Utils.getApp().getAssets();
        InputStream is = null;
        try {
            is = assetManager.open(filePath);
            bitmap = ImageUtils.getBitmap(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(is);
        }
        return bitmap;
    }

    /**
     * 将assets目录下的某个文件保存到sdcard存储中的文件中去
     *
     * @param assetFilePath 待保存的assets目录下的文件路径
     * @param saveFilePath  要保存到的文件的路径
     * @return
     */
    public static boolean saveAssetFileToSdcard(String assetFilePath, String saveFilePath) {
        InputStream is = null;
        boolean ret = false;
        try {
            is = Utils.getApp().getAssets().open(assetFilePath);
            ret = FileIOUtils.writeFileFromIS(saveFilePath, is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(is);
        }
        return ret;
    }

}
