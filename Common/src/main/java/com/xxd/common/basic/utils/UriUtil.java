package com.xxd.common.basic.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import java.io.File;

/**
 * author:XiaoDan
 * time:2022/6/25 17:00
 */
public final class UriUtil {

    private UriUtil() {
    }

    /**
     * File to uri.
     *
     * @param context
     * @param path    The file path
     * @return uri
     */
    public static Uri path2Uri(final Context context, final String path) {
        return file2Uri(context, new File(path));
    }

    /**
     * @param context
     * @param file    The File
     * @return Uri
     */
    public static Uri file2Uri(final Context context, @NonNull final File file) {
        Uri uri = Uri.EMPTY;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authority = context.getPackageName() + ".provider";
                uri = FileProvider.getUriForFile(context, authority, file);
            } else {
                uri = Uri.fromFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }

    /**
     * @param resID
     * @return
     */
    public static Uri res2Uri(final Context context, final int resID) {
        return Uri.parse("android.resource://" + context.getPackageName() + "/" + resID);
    }

    /**
     * 图片路径转成系统返回Uri，以便其他应用能正常访问
     */
    public static Uri getImageUri(Context context, String path) {
        return path2Uri(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, path);
    }

    /**
     * 音频路径转成系统返回Uri，以便其他应用能正常访问
     */
    public static Uri getAudioUri(Context context, String path) {
        return path2Uri(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, path);
    }

    /**
     * 视频路径转成系统返回Uri，以便其他应用能正常访问
     */
    public static Uri getVideoUri(Context context, String path) {
        return path2Uri(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, path);
    }

    /**
     * 文件路径转成系统返回Uri，以便其他应用能正常访问
     */
    public static Uri getFileUri(Context context, String path) {
        return path2Uri(context, MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), path);
    }

    protected static Uri path2Uri(Context context, Uri contentUri, String path) {
        Uri url = query(context, contentUri, path);
        if (null == url) {
            return insert(context, contentUri, path);
        }
        return url;
    }

    private static Uri query(Context context, Uri contentUri, String path) {
        Cursor cursor = context.getContentResolver().query(contentUri,
                new String[]{MediaStore.MediaColumns._ID}, MediaStore.MediaColumns.DATA + "=? ",
                new String[]{path}, null);
        Uri uri = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                if (MediaStore.VOLUME_EXTERNAL.equals(contentUri.toString())) {
                    uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL, id);
                } else {
                    uri = Uri.withAppendedPath(contentUri, "" + id);
                }
            }
            cursor.close();
        }
        return uri;
    }

    private static Uri insert(Context context, Uri contentUri, String path) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, path);
        return context.getContentResolver().insert(contentUri, values);
    }

    /**
     * user转换为file文件
     * 返回值为file类型
     *
     * @param uri
     * @return
     */
    private File uri2File(Activity activity, Uri uri) {
        String img_path;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
        if (cursor == null) {
            img_path = uri.getPath();
        } else {
            int actual_image_column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            img_path = cursor.getString(actual_image_column_index);
        }
        assert img_path != null;
        return new File(img_path);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String uri2Path(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 判断uri scheme是否为content的uri
     *
     * @param uri
     * @return
     */
    public static boolean isContentSchemeUri(Uri uri) {
        if (uri == null) return false;
        String scheme = uri.getScheme();
        return "content".equals(scheme);
    }

    /**
     * 判断uri scheme是否为file的uri
     *
     * @param uri
     * @return
     */
    public static boolean isFileSchemeUri(Uri uri) {
        if (uri == null) return false;
        String scheme = uri.getScheme();
        return "file".equals(scheme);
    }
}
