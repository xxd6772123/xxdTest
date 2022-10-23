package com.xxd.common.basic.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
 * @desc:将文件从应用私有目录和公共目录进行互相拷贝的工具类
 *  * （主要为Android10以上的分区存储而设计，同时兼容了Android10以下的版本）
 */
public class FileP2pUtil {

    private static final String TAG = FileP2pUtil.class.getSimpleName();

    /**
     * 媒体类型枚举常量类
     */
    private enum MediaType {
        VIDEO,
        AUDIO,
        IMAGE
    }

    private static boolean isAndroidQ() {
        return SdkVerUtil.isAndroidQ();
    }

    /**
     * 根据dirType获取公共存储目录下对应的目录
     *
     * @param context
     * @param dirType
     * @return
     */
    @Nullable
    private static String getDir(Context context, String dirType) {
        String dirPath = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(dirType);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    return null;
                }
            }
            if (context == null) {
                dirPath = dir.getAbsolutePath();
            } else {
                dirPath = dir.getAbsolutePath() + File.separator + context.getPackageName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirPath;
    }

    /**
     * 获取外部公共存储目录下我们专门保存视频的目录（默认加了应用包名的）
     * 考虑到Android10及后续系统版本的分区存储，不建议这样直接用，现在因开发人员有需求
     * 才提供该方法
     *
     * @param context
     * @return
     */
    @Nullable
    @Deprecated
    public static String getVideoDir(@NonNull Context context) {
        return getVideoDir(context, true);
    }

    /**
     * 获取外部公共存储目录下我们专门保存视频的目录
     * 考虑到Android10及后续系统版本的分区存储，不建议这样直接用，现在因开发人员有需求
     * 才提供该方法
     *
     * @param context
     * @param addPackage true:表示加了应用包名的目录，false为没加的
     * @return
     */
    @Nullable
    @Deprecated
    public static String getVideoDir(@NonNull Context context, boolean addPackage) {
        return getDir(addPackage ? context : null, Environment.DIRECTORY_MOVIES);
    }

    /**
     * 获取外部公共存储目录下我们专门保存音频的目录（默认加了应用包名的）
     * 考虑到Android10及后续系统版本的分区存储，不建议这样直接用，现在因开发人员有需求
     * 才提供该方法
     *
     * @param context
     * @return
     */
    @Nullable
    @Deprecated
    public static String getAudioDir(@NonNull Context context) {
        return getAudioDir(context, true);
    }

    /**
     * 获取外部公共存储目录下我们专门保存音频的目录
     * 考虑到Android10及后续系统版本的分区存储，不建议这样直接用，现在因开发人员有需求
     * 才提供该方法
     *
     * @param context
     * @param addPackage true:表示加了应用包名的目录，false为没加的
     * @return
     */
    @Nullable
    @Deprecated
    public static String getAudioDir(@NonNull Context context, boolean addPackage) {
        return getDir(addPackage ? context : null, Environment.DIRECTORY_MUSIC);
    }

    /**
     * 获取外部公共存储目录下我们专门保存图片的目录（默认加了应用包名的）
     * 考虑到Android10及后续系统版本的分区存储，不建议这样直接用，现在因开发人员有需求
     * 才提供该方法
     *
     * @param context
     * @return
     */
    @Nullable
    @Deprecated
    public static String getImgDir(@NonNull Context context) {
        return getImgDir(context, true);
    }

    /**
     * 获取外部公共存储目录下我们专门保存图片的目录
     * 考虑到Android10及后续系统版本的分区存储，不建议这样直接用，现在因开发人员有需求
     * 才提供该方法
     *
     * @param context
     * @param addPackage true:表示加了应用包名的目录，false为没加的
     * @return
     */
    @Nullable
    @Deprecated
    public static String getImgDir(@NonNull Context context, boolean addPackage) {
        return getDir(addPackage ? context : null, Environment.DIRECTORY_PICTURES);
    }

    /**
     * 获取文件路径
     *
     * @param context
     * @param dirType     取值如Environment.DIRECTORY_MOVIES、Environment.DIRECTORY_MUSIC等
     * @param displayName 文件名称
     * @return
     */
    private static String getFilePath(Context context, String dirType, String displayName) {
        if (isAndroidQ()) {
            return null;
        }

        File dir = Environment.getExternalStoragePublicDirectory(dirType);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return null;
            }
        }
        String filePath = null;
        if (context == null) {
            filePath = dir.getAbsolutePath() + File.separator + displayName;
        } else {
            filePath = dir.getAbsolutePath() + File.separator + context.getPackageName() + File.separator + displayName;
        }
        return filePath;
    }

    /**
     * 获取视频文件路径
     *
     * @param context
     * @param displayName
     * @return
     */
    private static String getVideoFilePath(Context context, String displayName) {
        return getFilePath(context, Environment.DIRECTORY_MOVIES, displayName);
    }

    /**
     * 获取音频文件路径
     *
     * @param context
     * @param displayName
     * @return
     */
    private static String getAudioFilePath(Context context, String displayName) {
        return getFilePath(context, Environment.DIRECTORY_MUSIC, displayName);
    }

    /**
     * 获取图片文件路径
     *
     * @param context
     * @param displayName
     * @return
     */
    private static String getImgFilePath(Context context, String displayName) {
        return getFilePath(context, Environment.DIRECTORY_PICTURES, displayName);
    }

    /**
     * 从文件路径中获取文件的名称
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }
        String fileName = filePath;
        int index = filePath.lastIndexOf("/");
        if (index >= 0) {
            fileName = filePath.substring(index + 1);
        }
        return fileName;
    }

    /**
     * 将应用专属目录下的私有的视频文件拷贝到公共目录下去
     *
     * @param context
     * @param videoPath 原视频路径
     * @return 拷贝到公共目录下的文件所对应的uri
     */
    public static Uri copyPrivateVideoToPublic(Context context, String videoPath) {
        String fileName = getFileName(videoPath);
        return copyPrivateVideoToPublic(context, videoPath, fileName);
    }

    /**
     * 将应用专属目录下的私有的视频文件拷贝到公共目录下去
     *
     * @param context
     * @param videoPath   原视频路径
     * @param displayName 视频文件名称
     * @return 拷贝到公共目录下的文件所对应的uri
     */
    public static Uri copyPrivateVideoToPublic(Context context, String videoPath, String displayName) {
        return copyPrivateMediaFileToPublic(context, MediaType.VIDEO, videoPath, displayName);
    }

    /**
     * 将应用专属目录下的私有的音频文件拷贝到公共目录下去
     *
     * @param context
     * @param audioPath 原音频路径
     * @return 拷贝到公共目录下的文件所对应的uri
     */
    public static Uri copyPrivateAudioToPublic(Context context, String audioPath) {
        String fileName = getFileName(audioPath);
        return copyPrivateAudioToPublic(context, audioPath, fileName);
    }

    /**
     * 将应用专属目录下的私有的音频文件拷贝到公共目录下去
     *
     * @param context
     * @param audioPath   原音频路径
     * @param displayName 音频文件名称
     * @return 拷贝到公共目录下的文件所对应的uri
     */
    public static Uri copyPrivateAudioToPublic(Context context, String audioPath, String displayName) {
        return copyPrivateMediaFileToPublic(context, MediaType.AUDIO, audioPath, displayName);
    }

    /**
     * 将应用专属目录下的私有的图片文件拷贝到公共目录下去
     *
     * @param context
     * @param imgPath 原图片路径
     * @return 拷贝到公共目录下的文件所对应的uri
     */
    public static Uri copyPrivateImgToPublic(Context context, String imgPath) {
        String fileName = getFileName(imgPath);
        return copyPrivateImgToPublic(context, imgPath, fileName);
    }

    /**
     * 将应用专属目录下的私有的图片文件拷贝到公共目录下去
     *
     * @param context
     * @param imgPath     原图片路径
     * @param displayName 图片显示名称
     * @return 拷贝到公共目录下的文件所对应的uri
     */
    public static Uri copyPrivateImgToPublic(Context context, String imgPath, String displayName) {
        return copyPrivateMediaFileToPublic(context, MediaType.IMAGE, imgPath, displayName);
    }

    /**
     * 将应用专属目录下的媒体文件拷贝到公共的媒体路径下
     *
     * @param context
     * @param type        拷贝文件所属的媒体类型
     * @param srcFilePath 要拷贝的文件路径
     * @param displayName 拷贝后的文件的名称
     * @return 拷贝到公共目录下的文件所对应的uri
     */
    public static Uri copyPrivateMediaFileToPublic(Context context, MediaType type, String srcFilePath, String displayName) {
        return copyPrivateMediaFileToPublic(context, type, srcFilePath, displayName, true);
    }

    /**
     * 将应用专属目录下的媒体文件拷贝到公共的媒体路径下
     *
     * @param context
     * @param type          拷贝文件所属的媒体类型
     * @param srcFilePath   要拷贝的文件路径
     * @param displayName   拷贝后的文件的名称
     * @param addPackageDir 表示文件存储路径中是否要加入应用包名 true：为加入 false：不加入
     * @return 拷贝到公共目录下的文件所对应的uri
     */
    public static Uri copyPrivateMediaFileToPublic(Context context, MediaType type, String srcFilePath, String displayName, boolean addPackageDir) {
        String relativePath = null;
        String dataPath = null;
        Uri externalContentUri = null;
        switch (type) {
            case VIDEO:
                relativePath = Environment.DIRECTORY_MOVIES;
                dataPath = getVideoFilePath(addPackageDir ? context : null, displayName);
                externalContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                break;
            case AUDIO:
                relativePath = Environment.DIRECTORY_MUSIC;
                dataPath = getAudioFilePath(addPackageDir ? context : null, displayName);
                externalContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                break;
            case IMAGE:
                relativePath = Environment.DIRECTORY_PICTURES;
                dataPath = getImgFilePath(addPackageDir ? context : null, displayName);
                externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                break;
        }
        //加上应用包名，将文件放到应用自己的包名目录中去
        if (addPackageDir) {
            relativePath += File.separator + context.getPackageName();
        }

        //insert
        ContentResolver resolver = context.getContentResolver();
        Uri uri = insert(resolver, externalContentUri, relativePath, dataPath, displayName);
        if (uri == null) {
            Log.e(TAG, "copyPrivateMediaFileToPublic: insert failure, the returned uri is null.");
            return null;
        }

        //copy
        FileInputStream fis = null;
        OutputStream os = null;
        try {
            fis = new FileInputStream(srcFilePath);
            os = resolver.openOutputStream(uri);
            boolean isOk = IOUtil.write(fis, os);
            if (isOk) {
                if (!isAndroidQ()) {
                    String[] paths = new String[]{dataPath};
                    MediaScannerConnection.scanFile(context, paths, null, null);
                }
                return uri;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 往数据库中插入相关条目信息
     *
     * @param resolver
     * @param uri          待插入到的数据库对应的uri
     * @param relativePath
     * @param dataPath
     * @param displayName
     * @return 插入后返回的条目的uri
     */
    private static Uri insert(ContentResolver resolver, Uri uri, String relativePath, String dataPath, String displayName) {
        ContentValues values = new ContentValues();
        //display name
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, displayName);
        //mimetype
        String mimeType = MimeUtils.guessMimeTypeFromExtension(FileUtils.getFileExtension(displayName));
        if (mimeType != null) {
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
        }
        //path
        if (isAndroidQ()) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath);
        } else {
            //这里一定要先把文件创建好，否则后续resolver.openOutputStream(uri)方法会调用失败
            FileUtils.createOrExistsFile(dataPath);
            values.put(MediaStore.MediaColumns.DATA, dataPath);
        }
        Uri insertUri = resolver.insert(uri, values);
        return insertUri;
    }

    /**
     * 根据位图的压缩格式来获取对应的后缀名称
     *
     * @param compressFormat
     * @return 后缀名称
     */
    public static String getSuffixBy(Bitmap.CompressFormat compressFormat) {
        String suffix = ".png";
        if (compressFormat == null) {
            return suffix;
        }
        switch (compressFormat) {
            case JPEG:
                suffix = ".jpg";
                break;
            case WEBP:
            case WEBP_LOSSLESS:
            case WEBP_LOSSY:
                suffix = "webp";
                break;
        }
        return suffix;
    }

    /**
     * 保存位图
     *
     * @param context
     * @param bitmap
     * @param compressFormat
     * @return 保存后的位图文件对应的uri
     */
    public static Uri saveBitmap(Context context, Bitmap bitmap, Bitmap.CompressFormat compressFormat) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        if (compressFormat == null) {
            compressFormat = Bitmap.CompressFormat.PNG;
        }

        String displayName = "Bmp_" + System.currentTimeMillis() + getSuffixBy(compressFormat);
        String relativePath = Environment.DIRECTORY_PICTURES + File.separator + context.getPackageName();
        String dataPath = getImgFilePath(context, displayName);
        Uri externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        ContentResolver resolver = context.getContentResolver();
        Uri uri = insert(resolver, externalContentUri, relativePath, dataPath, displayName);
        if (uri == null) {
            return null;
        }
        try {
            OutputStream os = resolver.openOutputStream(uri);
            boolean isOk = bitmap.compress(compressFormat, 100, os);
            os.close();
            if (isOk && !isAndroidQ()) {
                String[] paths = new String[]{dataPath};
                MediaScannerConnection.scanFile(context, paths, null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            uri = null;
        }
        return uri;
    }

    /**
     * 将位图以png的格式保存
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static Uri saveBitmap2Png(Context context, Bitmap bitmap) {
        return saveBitmap(context, bitmap, Bitmap.CompressFormat.PNG);
    }

    /**
     * 将位图以jpeg的格式保存
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static Uri saveBitmap2Jpg(Context context, Bitmap bitmap) {
        return saveBitmap(context, bitmap, Bitmap.CompressFormat.JPEG);
    }

    /**
     * 将输入流保存到外部公共存储目录中的文件中去
     *
     * @param context
     * @param type
     * @param displayName   保存的文件名字
     * @param addPackageDir 保存的路径中是否添加该应用的包名
     * @param is            待写入的输入流
     * @return
     */
    public static Uri saveInputStreamToPublic(Context context, MediaType type, String displayName,
                                              boolean addPackageDir, InputStream is) {
        String relativePath = null;
        String dataPath = null;
        Uri externalContentUri = null;
        switch (type) {
            case VIDEO:
                relativePath = Environment.DIRECTORY_MOVIES;
                dataPath = getVideoFilePath(addPackageDir ? context : null, displayName);
                externalContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                break;
            case AUDIO:
                relativePath = Environment.DIRECTORY_MUSIC;
                dataPath = getAudioFilePath(addPackageDir ? context : null, displayName);
                externalContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                break;
            case IMAGE:
                relativePath = Environment.DIRECTORY_PICTURES;
                dataPath = getImgFilePath(addPackageDir ? context : null, displayName);
                externalContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                break;
        }
        //加上应用包名，将文件放到应用自己的包名目录中去
        if (addPackageDir) {
            relativePath += File.separator + context.getPackageName();
        }

        //insert
        ContentResolver resolver = context.getContentResolver();
        Uri uri = insert(resolver, externalContentUri, relativePath, dataPath, displayName);
        if (uri == null) {
            Log.e(TAG, "saveInputStreamToPublic: insert failure, the returned uri is null.");
            return null;
        }

        //copy
        OutputStream os = null;
        try {
            os = resolver.openOutputStream(uri);
            boolean isOk = IOUtil.write(is, os);
            if (isOk) {
                if (!isAndroidQ()) {
                    String[] paths = new String[]{dataPath};
                    MediaScannerConnection.scanFile(context, paths, null, null);
                }
                return uri;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 保存音频输入流到文件中
     *
     * @param context
     * @param displayName
     * @param is
     * @return
     */
    public static Uri saveAudioInputStreamToPublic(Context context, String displayName, InputStream is) {
        return saveInputStreamToPublic(context, MediaType.AUDIO, displayName, true, is);
    }

    /**
     * 保存视频输入流到文件中
     *
     * @param context
     * @param displayName
     * @param is
     * @return
     */
    public static Uri saveVideoInputStreamToPublic(Context context, String displayName, InputStream is) {
        return saveInputStreamToPublic(context, MediaType.VIDEO, displayName, true, is);
    }

    /**
     * 保存图片输入流到文件中
     *
     * @param context
     * @param displayName
     * @param is
     * @return
     */
    public static Uri saveImgInputStreamToPublic(Context context, String displayName, InputStream is) {
        return saveInputStreamToPublic(context, MediaType.IMAGE, displayName, true, is);
    }

    /**
     * 获取通过该工具类保存到外部公共存储目录下的文件所在的目录的相对目录路径
     *
     * @param type
     * @param addPackage
     * @return
     */
    private static String getPublicMediaRelativeDir(@NonNull MediaType type, boolean addPackage) {
        String relativePath = null;
        boolean isAndroidQ = isAndroidQ();
        switch (type) {
            case IMAGE:
                if (isAndroidQ) {
                    relativePath = Environment.DIRECTORY_PICTURES;
                } else {
                    relativePath = PathUtils.getExternalPicturesPath();
                }
                break;
            case VIDEO:
                if (isAndroidQ) {
                    relativePath = Environment.DIRECTORY_MOVIES;
                } else {
                    relativePath = PathUtils.getExternalMoviesPath();
                }
                break;
            case AUDIO:
                if (isAndroidQ) {
                    relativePath = Environment.DIRECTORY_MUSIC;
                } else {
                    relativePath = PathUtils.getExternalMusicPath();
                }
                break;
        }
        if (addPackage) {
            relativePath += File.separator + Utils.getApp().getPackageName();
        }
        return relativePath;
    }

    /**
     * 获取通过该工具类保存到外部公共存储图片目录下的文件所在的目录的相对目录路径
     *
     * @param addPackage 表示路径中是否有包含包名
     * @return
     */
    public static String getImgRelativeDir(boolean addPackage) {
        return getPublicMediaRelativeDir(MediaType.IMAGE, addPackage);
    }

    /**
     * 获取通过该工具类保存到外部公共存储视频目录下的文件所在的目录的相对目录路径
     *
     * @param addPackage 表示路径中是否有包含包名
     * @return
     */
    public static String getVideoRelativeDir(boolean addPackage) {
        return getPublicMediaRelativeDir(MediaType.VIDEO, addPackage);
    }

    /**
     * 获取通过该工具类保存到外部公共存储音频目录下的文件所在的目录的相对目录路径
     *
     * @param addPackage 表示路径中是否有包含包名
     * @return
     */
    public static String getAudioRelativeDir(boolean addPackage) {
        return getPublicMediaRelativeDir(MediaType.AUDIO, addPackage);
    }

    /**
     * 将uri所对应的文件拷贝到privateFilePath对应的文件中去
     *
     * @param uri             待拷贝的资源
     * @param privateFilePath 应用专属目录下的文件路径（要拷贝到的路径）
     * @return
     */
    public static String copyToPrivate(Uri uri, String privateFilePath) {
        String retPath = "";
        if (uri == null || TextUtils.isEmpty(privateFilePath)) {
            return retPath;
        }
        ContentResolver resolver = Utils.getApp().getContentResolver();
        InputStream is = null;
        OutputStream os = null;
        try {
            is = resolver.openInputStream(uri);
            boolean isOk = FileUtils.createOrExistsFile(privateFilePath);
            if (isOk) {
                os = new FileOutputStream(privateFilePath);
                isOk = IOUtil.write(is, os);
                retPath = isOk ? privateFilePath : retPath;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } finally {
            IOUtil.close(is);
            IOUtil.close(os);

        }
        return retPath;
    }

}

