package com.xxd.common.basic.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 * <pre>
 *  *     author : cfans
 *  *     time   : 2020/10/14
 *  *     desc   : 文件操作工具类,需添加如下权限
 *  *   <!-- 在SD卡中创建与删除文件权限 -->
 *  *   <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
 *  *   <!-- 向SD卡写入数据权限 -->
 *  *   <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 *  *   android:requestLegacyExternalStorage="true"
 *  *    version: 1.0
 *  * </pre>
 */

public final class FileUtil {

    private FileUtil() {
    }

    /**
     * 判断文件是否存在
     *
     * @param file The file.
     * @return {@code true}: 存在<br>{@code false}: 不存在
     */
    public static boolean isFileExists(Context context, final File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return true;
        }
        return isFileExistsApi29(context, file.getAbsolutePath());
    }

    /**
     * 判断是否是文件夹
     *
     * @param file The file.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isDirectory(final File file) {
        return file != null && file.exists() && file.isDirectory();
    }

    /**
     * 删除file这个文件，或者此目录,需要权限
     *
     * @param file 文件，或者目录
     */
    public static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }
        }
        return file.delete();
    }

    /**
     * 该方法已作废，请用FileP2pUtil工具类中的保存位图的相关方法
     *
     * @param path 该参数已作废，可传null
     * @param b
     */
    @Deprecated
    public static void saveBitmap(String path, Bitmap b) {
        /*if (!TextUtils.isEmpty(path) && b != null) {
            FileOutputStream out = null;
            try {
                File file = new File(path);
                if (file == null) {
                    return;
                }
                file.getParentFile().mkdirs();
                file.createNewFile();
                out = new FileOutputStream(file);
                b.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        FileP2pUtil.saveBitmap2Jpg(Utils.getApp(), b);
    }

    private static boolean isFileExistsApi29(Context context, String filePath) {
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                Uri uri = Uri.parse(filePath);
                ContentResolver cr = context.getContentResolver();
                AssetFileDescriptor afd = cr.openAssetFileDescriptor(uri, "r");
                if (afd == null) {
                    return false;
                }
                try {
                    afd.close();
                } catch (IOException ignore) {
                }
            } catch (FileNotFoundException e) {
                return false;
            }
            return true;
        }
        return false;
    }


    /**
     * 将Asset目录下文件拷贝到缓存中，返回文件路径
     *
     * @param context
     * @param fileName asset下文件名
     * @return 返回文件路径，
     */
    public static String getAssetsCacheFile(Context context, String fileName) {
//        File cacheFile = new File(Environment.getExternalStorageDirectory(), fileName);
        File cacheFile = new File(context.getCacheDir(), fileName);
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            try {

                FileOutputStream outputStream = new FileOutputStream(cacheFile);
                try {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        outputStream.write(buf, 0, len);
                    }
                } finally {
                    outputStream.close();
                }
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cacheFile.getAbsolutePath();
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 创建文件夹及文件
     *
     * @param folderName 文件夹名字
     * @param suffix     文件后缀名
     * @return
     */
    public static File createMediaFile(String folderName, String suffix) {
        if (isSDCardEnable()) {
            if ((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
                // Constants.video_url 是一个常量，代表存放视频的文件夹
                String path = Environment.getExternalStorageDirectory().getPath() + folderName;
                File mediaStorageDir = new File(path);
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.e("SDCardUtils", "文件夹创建失败");
                        return null;
                    }
                }

                // 文件根据当前的毫秒数给自己命名
                String timeStamp = String.valueOf(System.currentTimeMillis());
                timeStamp = timeStamp.substring(7);
                String imageFileName = "V" + timeStamp;
                File mediaFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
                return mediaFile;
            }
        }
        return null;
    }

    /**
     * 在应用的专属私有目录下对应的目录下生成临时文件,文件后缀名由源文件srcFilePath获得
     *
     * @param relativeDirPath 相对于应用专有的files目录下的相对路径
     * @param defSuffix       默认的文件后缀名
     * @param srcFilePath     源文件路径
     * @return 生成的文件的路径
     */
    public static String generateFilePath(String relativeDirPath, String defSuffix, String srcFilePath) {
        String suffix = defSuffix;
        if (!TextUtils.isEmpty(srcFilePath)) {
            int index = srcFilePath.lastIndexOf(".");
            if (index >= 0) {
                suffix = srcFilePath.substring(index);
            }
        }
        return generateFilePath(relativeDirPath, suffix);
    }

    /**
     * 在应用的专属私有目录下对应的目录下生成临时文件
     *
     * @param relativeDirPath 相对于应用专有的files目录下的相对路径
     * @param suffix          要生成的文件名的后缀
     * @return 生成的文件的路径
     */
    public static String generateFilePath(String relativeDirPath, String suffix) {
        String fileName = "Tmp_" + System.currentTimeMillis() + suffix;
        return generateFilePathByName(relativeDirPath, fileName);
    }

    /**
     * 在应用的专属私有目录下对应的目录下生成临时文件
     *
     * @param relativeDirPath 可为空，为空时，则files根目录下创建fileName文件
     * @param fileName        文件名称（带后缀）
     * @return
     */
    public static String generateFilePathByName(String relativeDirPath, String fileName) {
        return generateFilePathByName(relativeDirPath, fileName, true);
    }

    /**
     * @param relativeDirPath 可为空，为空时，则files根目录下创建fileName文件
     * @param fileName        文件名称（带后缀）
     * @param autoCreate      是否需要创建该文件的标志 true为生成该文件路径的同时创建该文件，否则只组装得到该路径
     * @return
     */
    public static String generateFilePathByName(String relativeDirPath, String fileName, boolean autoCreate) {
        //处理相对路径relativeDirPath
        if (!TextUtils.isEmpty(relativeDirPath)) {
            if (!relativeDirPath.startsWith("/")) {
                relativeDirPath = "/" + relativeDirPath;
            }
        } else {
            relativeDirPath = "";
        }

        //获取rootPath
        String rootPath = PathUtils.getFilesPathExternalFirst();
        if (TextUtils.isEmpty(rootPath)) {
            return null;
        }
        //得到文件要保存的目录路径
        String dirPath = rootPath + relativeDirPath;

        //create file
        String filePath = null;
        if (dirPath.endsWith("/")) {
            filePath = dirPath + fileName;
        } else {
            filePath = dirPath + File.separator + fileName;
        }

        if (!autoCreate) {
            return filePath;
        }
        boolean isOK = FileUtils.createOrExistsFile(filePath);
        if (isOK) {
            return filePath;
        }
        return null;
    }

    /**
     * 拷贝单个文件
     *
     * @param fromFile 旧文件路径
     * @param toFile   新文件路径
     * @return
     */
    public static int copySdcardFile(String fromFile, String toFile) {
        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath 文件的路径
     */
    public static void removeFile(String filePath) {
        if (filePath == null || filePath.length() == 0) {
            return;
        }
        try {
            File file = new File(filePath);
            if (file.exists()) {
                removeFile(file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void removeFile(File file) {
        //如果是文件直接删除
        if (file.isFile()) {
            file.delete();
            return;
        }
        //如果是目录，递归判断，如果是空目录，直接删除，如果是文件，遍历删除
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                removeFile(f);
            }
            file.delete();
        }
    }

    /**
     * 执行删除公共存储目录下的文件时调用该方法
     *
     * @param filePath
     */
    public static void delPublicFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }
            //判断是否为目录
            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    //空目录，则删除目录
                    file.delete();
                    return;
                }
                for (File childFile : childFiles) {
                    delPublicFile(childFile.getAbsolutePath());
                }
                return;
            }
            //为文件，执行删除
            boolean ret = file.delete();
            if (ret) {
                String[] paths = new String[]{filePath};
                MediaScannerConnection.scanFile(Utils.getApp(), paths, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
