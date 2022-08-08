package com.example.xpagebasejava.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.UriUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author:XiaoDan
 * time:2022/6/25 16:59
 * desc:系统分享工具类
 */
public final class IntentUtil {


    private IntentUtil() {
    }

    private final static String TYPE_TEXT = "text/plain";
    private final static String TYPE_IMAGE = "image/*";
    private final static String TYPE_AUDIO = "audio/*";
    private final static String TYPE_VIDEO = "video/*";
    private final static String TYPE_MULTIPLE = "*/*";


    /**
     * 打电话
     *
     * @param phoneNumber 手机号码
     */
    public static void call(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    /**
     * 打开系统相册选择照片
     *
     * @param activity
     * @param requestCode 请求码
     */
    public static void pickImage(Activity activity, int requestCode) {
        try {
            Intent intent = getIntent(Intent.ACTION_PICK, TYPE_IMAGE);
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开系统相册选择照片
     *
     * @param fragment
     * @param requestCode 请求码
     */
    public static void pickImage(Fragment fragment, int requestCode) {
        try {
            Intent intent = getIntent(Intent.ACTION_PICK, TYPE_IMAGE);
            fragment.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 从系统文件管理器中获取音频文件
     *
     * @param activity
     * @param requestCode
     */
    public static void pickAudio(Activity activity, int requestCode) {
        try {
            Intent intent = getIntent(Intent.ACTION_GET_CONTENT, TYPE_AUDIO);
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从系统文件管理器中获取音频文件
     *
     * @param fragment
     * @param requestCode
     */
    public static void pickAudio(Fragment fragment, int requestCode) {
        try {
            Intent intent = getIntent(Intent.ACTION_GET_CONTENT, TYPE_AUDIO);
            fragment.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从系统文件管理器中获取视频文件
     *
     * @param activity
     * @param requestCode
     */
    public static void pickVideo(Activity activity, int requestCode) {
        try {
            Intent intent = getIntent(Intent.ACTION_GET_CONTENT, TYPE_VIDEO);
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从系统文件管理器中获取视频文件
     *
     * @param fragment
     * @param requestCode
     */
    public static void pickVideo(Fragment fragment, int requestCode) {
        try {
            Intent intent = getIntent(Intent.ACTION_GET_CONTENT, TYPE_VIDEO);
            fragment.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发短信
     *
     * @param phoneNumber 手机号码
     * @param content     短信内容
     */
    public static void sendSMS(Context context, String phoneNumber, final String content) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
        intent.putExtra("sms_body", content);
        context.startActivity(intent);
    }

    /**
     * app好评鼓励
     *
     * @param context 上下文
     */
    public static void appReview(final Context context) throws RuntimeException {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 分享纯文本
     *
     * @param context 上下文
     * @param text    文本内容
     */
    public static void shareText(final Context context, final String text) {
        Intent intent = getIntent(Intent.ACTION_SEND, TYPE_TEXT);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        share(context, intent);
    }

    /**
     * 分享单张图片
     *
     * @param context   上下文
     * @param text      分享文案，无文案输入null
     * @param imagePath 分享图片路径
     */
    public static void shareImage(Context context, String text, String imagePath) {
        shareImage(context, text, new File(imagePath));
    }

    /**
     * 分享单张图片
     *
     * @param context 上下文
     * @param uri     分享图片的uri
     */
    public static void shareImageUri(Context context, Uri uri) {
        Intent i = getIntent(Intent.ACTION_SEND, TYPE_IMAGE);
        share(context, i, uri);
    }

    /**
     * 分享单张图片
     *
     * @param context   上下文
     * @param text      分享文案，无文案输入null
     * @param imageFile 分享图片文件
     */
    public static void shareImage(Context context, String text, File imageFile) {
        Intent intent = getIntent(Intent.ACTION_SEND, TYPE_IMAGE);
        if (!TextUtils.isEmpty(text)) {
            intent.putExtra(Intent.EXTRA_TEXT, text);
        }
        intent.putExtra(Intent.EXTRA_STREAM, UriUtil.file2Uri(context, imageFile));
        share(context, intent);
    }

    /**
     * 分享多张图片
     *
     * @param imageFiles 多张图片文件
     */
    public static void shareImagesFile(Context context, List<File> imageFiles) {
        ArrayList<String> list = new ArrayList<>();
        for (File file : imageFiles) {
            list.add(file.getAbsolutePath());
        }
        shareImagesPath(context, list);
    }

    /**
     * 分享多张图片
     *
     * @param imagePaths 多张图片文件路径
     */
    public static void shareImagesPath(Context context, List<String> imagePaths) {
        ArrayList<Uri> list = new ArrayList<>();
        for (String path : imagePaths) {
            Uri uri = UriUtil.path2Uri(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, path);
            list.add(uri);
            LogUtil.e(uri);
        }
        Intent intent = getIntent(Intent.ACTION_SEND_MULTIPLE, TYPE_MULTIPLE);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, list);
        share(context, intent);
    }

    /**
     * 分享单张图片
     *
     * @param context   上下文
     * @param videoPath 分享视频路径
     */
    public static void shareVideo(final Context context, final String videoPath) {
        shareVideo(context, new File(videoPath));
    }

    /**
     * 分享视频
     *
     * @param context   上下文
     * @param videoFile 分享视频文件
     */
    public static void shareVideo(final Context context, final File videoFile) {
        Intent intent = getIntent(Intent.ACTION_SEND, TYPE_VIDEO);
        share(context, intent, UriUtil.file2Uri(context, videoFile));
    }

    /**
     * 分享音频
     *
     * @param context   上下文
     * @param audioPath 分享音频文件
     */
    public static void shareAudio(final Context context, final String audioPath) {
        Intent intent = getIntent(Intent.ACTION_SEND, TYPE_AUDIO);
        share(context, intent, UriUtil.path2Uri(context, audioPath));
    }

    /**
     * 分享其他文件
     *
     * @param context   上下文
     * @param otherFile 分享其他文件
     */
    public static void shareFile(final Context context, final String otherFile) {
        Intent intent = getIntent(Intent.ACTION_SEND, TYPE_MULTIPLE);
        share(context, intent, UriUtil.getFileUri(context, otherFile));
    }

    private static Intent getIntent(final String action, final String type) {
        Intent i = new Intent(action);
        i.setType(type);
        return i;
    }

    /**
     * 获取要跳去某个Activity的Intent
     *
     * @param context
     * @param clazz   想要跳转去目的Activity的class
     * @return
     */
    public static Intent getIntent(Context context, Class<? extends Activity> clazz) {
        Intent intent = new Intent(context, clazz);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    private static void share(final Context context, final Intent intent) {
        Intent i = Intent.createChooser(intent, "");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    private static void share(final Context context, final Intent intent, Uri uri) {
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        share(context, intent);
    }

    private static void startActivitySafely(Context context, Intent intent) {
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求第三方应用打开文档
     *
     * @param context
     * @param filePath 待打开的文档路径
     */
    public static void openDoc(@NonNull Context context, @NonNull String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = UriUtils.file2Uri(new File(filePath));
        String mimeType = MimeUtils.guessMimeTypeFromExtension(FileUtils.getFileExtension(filePath));
        intent.setDataAndType(data, mimeType);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivitySafely(context, intent);
    }

    /**
     * 请求第三方应用打开文档
     *
     * @param context
     * @param uri     待打开的文档的uri
     */
    public static void openDoc(@NonNull Context context, @NonNull Uri uri) {
        openUri(context, uri);
    }

    /**
     * 请求打开Uri
     *
     * @param context
     * @param uri     uri对象
     */
    public static void openUri(@NonNull Context context, @NonNull Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivitySafely(context, intent);
    }

    /**
     * 请求从系统文件选择器中选择文件
     *
     * @param activity
     * @param reqCode
     */
    public static void getFile(@NonNull Activity activity, int reqCode) {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(TYPE_MULTIPLE);
            activity.startActivityForResult(intent, reqCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求从系统文件选择器中选择文件
     *
     * @param fragment
     * @param reqCode
     */
    public static void getFile(@NonNull Fragment fragment, int reqCode) {
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType(TYPE_MULTIPLE);
            fragment.startActivityForResult(intent, reqCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
