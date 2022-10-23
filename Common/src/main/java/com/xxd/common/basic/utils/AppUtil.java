package com.xxd.common.basic.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;
import androidx.core.content.pm.PackageInfoCompat;

import com.blankj.utilcode.util.Utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:App相关工具类
 */
public class AppUtil {

    private static final String FIRST_LAUNCH = "isFirstLaunch";

    /**
     * 安装一个apk
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param context 上下文
     * @param apk     安装包文件
     */
    public static void installApk(Context context, File apk) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.getPackageName(), apk);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(apk);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 获取当前app的名字
     *
     * @param context 上下文
     */
    public static String getName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.applicationInfo.loadLabel(packageManager).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取当前app的升级版本号
     *
     * @param context 上下文
     */
    public static long getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return PackageInfoCompat.getLongVersionCode(packageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * 获取当前app的版本号
     *
     * @param context 上下文
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0.0";
        }
    }

    /**
     * App 是否是第一次打开
     *
     * @param context
     * @return 默认为true
     */
    public static boolean isAppFirstLaunch(Context context) {
        return SPUtil.getBoolean(context, FIRST_LAUNCH, true);
    }

    /**
     * 设置App是否是第一次打开标记
     *
     * @param context
     * @param isFirst 是否是第一次打开
     */
    public static void setAppFirstLaunch(Context context, boolean isFirst) {
        SPUtil.putBoolean(context, FIRST_LAUNCH, isFirst);
    }

    /**
     * 获取设备上所有安装的拥有界面的包信息
     *
     * @return
     */
    public static List<PackageInfo> getPackagesWithLaunchIntent() {
        PackageManager pm = Utils.getApp().getPackageManager();
        List<PackageInfo> packageInfoList = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        PackageInfo packageInfo = null;
        for (int i = 0; i < packageInfoList.size(); i++) {
            packageInfo = packageInfoList.get(i);
            if (pm.getLaunchIntentForPackage(packageInfo.packageName) == null) {
                packageInfoList.remove(i);
                i--;
            }
        }
        return packageInfoList;
    }

    /**
     * 获取所有的可在桌面显示系统应用
     *
     * @return
     */
    public static List<PackageInfo> getSystemPackagesWithLaunchIntent() {
        PackageManager pm = Utils.getApp().getPackageManager();
        List<PackageInfo> packageInfoList = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        PackageInfo packageInfo = null;
        for (int i = 0; i < packageInfoList.size(); i++) {
            packageInfo = packageInfoList.get(i);
            if (pm.getLaunchIntentForPackage(packageInfo.packageName) == null ||
                    ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM)) {
                packageInfoList.remove(i);
                i--;
            }
        }
        return packageInfoList;
    }

    /**
     * 获取所有的可在桌面显示的非系统应用
     *
     * @return
     */
    public static List<PackageInfo> getNoSystemPackagesWithLaunchIntent() {
        PackageManager pm = Utils.getApp().getPackageManager();
        List<PackageInfo> packageInfoList = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        PackageInfo packageInfo = null;
        for (int i = 0; i < packageInfoList.size(); i++) {
            packageInfo = packageInfoList.get(i);
            if (pm.getLaunchIntentForPackage(packageInfo.packageName) == null ||
                    ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM)) {
                packageInfoList.remove(i);
                i--;
            }
        }
        return packageInfoList;
    }

    /**
     * 反射获取渠道信息
     */
    public static String getChannel(Context context) {
        String channel = "huawei";
        try {
            //下面的UmengUtil后续再添加
            Class<?> clazz = Class.forName("stark.common.core.util.UmengUtil");
            Method method = clazz.getMethod("getChannel", Context.class);
            channel = (String) method.invoke(null, context);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            //e.printStackTrace();
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        } catch (InvocationTargetException e) {
            //e.printStackTrace();
        }
        return channel;
    }

    /**
     * 获得属于桌面的应用的应用包名称
     *
     * @return 返回包含所有包名的字符串列表
     */
    public static List<String> getHomeApps(Context context) {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfos) {
            names.add(resolveInfo.activityInfo.packageName);
        }
        return names;
    }

}
