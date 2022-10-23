package com.xxd.common.basic.device;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

import com.xxd.common.basic.utils.LogUtil;

import java.util.List;
import java.util.Locale;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:设备基本信息汇总
 */
public class DeviceInfoUtil {

    /**
     * 获取设备宽度（px）
     *
     */
    public static int getDeviceWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取设备高度（px）
     */
    public static int getDeviceHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static String getHWRate(int h,int w){
        return String.format("%d:%d",h/maxNumber(h,w),w/maxNumber(h,w));
    }
    static int maxNumber(int m, int n) {
        int temp;
        if (n > m) {
            temp = n;
            n = m;
            m = temp;
        }
        if (m % n == 0) {
            return n;
        }
        return maxNumber(n, m % n);
    }

    /**
     * 获取设备的唯一标识， 必须“android.permission.READ_Phone_STATE”权限
     */
    @SuppressLint("android.permission.READ_Phone_STATE")
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (deviceId == null) {
            return "UnKnown";
        } else {
            return deviceId;
        }
    }

    /**
     * 获取厂商名
     */
    public static String getDeviceManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取产品名
     */
    public static String getDeviceProduct() {
        return android.os.Build.PRODUCT;
    }

    /**
     * 获取硬件
     */
    public static String getHardWare() {
        return Build.HARDWARE;
    }

    /**
     * 获取手机品牌
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机型号
     */
    public static String getDeviceModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机主板名
     */
    public static String getDeviceBoard() {
        return android.os.Build.BOARD;
    }

    /**
     * 设备名
     */
    public static String getDeviceDevice() {
        return android.os.Build.DEVICE;
    }

    /**
     * fingerprint 指纹信息
     */
    public static String getDeviceFingerprint() {
        return android.os.Build.FINGERPRINT;
    }

    /**
     * 硬件名
     */
    public static String getDeviceHardware() {
        return android.os.Build.HARDWARE;
    }

    /**
     * 主机
     */
    public static String getDeviceHost() {
        return android.os.Build.HOST;
    }

    /**
     * 版本显示
     */
    public static String getDeviceDisplay() {
        return android.os.Build.DISPLAY;
    }

    /**
     * 设备ID
     */
    public static String getDeviceId() {
        return android.os.Build.ID;
    }

    /**
     * 获取手机用户名
     */
    public static String getDeviceUser() {
        return android.os.Build.USER;
    }

    /**
     * 获取手机 硬件序列号
     */
    public static String getDeviceSerial() {
        return android.os.Build.SERIAL;
    }

    /**
     * 获取手机Android 系统SDK
     */
    public static int getDeviceSDK() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取手机Android 版本
     */
    public static String getDeviceAndroidVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取当前手机系统语言。
     */
    public static String getDeviceDefaultLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * @param context
     * @return
     */
    public static String getDeviceAllInfo(Context context) {

        for (StatFs statFs : StorageUtil.getSDCardMemory(context)){
            LogUtil.e("可用/总共：" + StorageUtil.getAvailableMemorySize(context,statFs)+ "/" +StorageUtil.getTotalMemorySize(context,statFs));

        }

        ActivityManager.MemoryInfo memoryInfo = StorageUtil.getRAMInfo(context);
        StatFs intStatFs = StorageUtil.getInternalMemory();
        StatFs extStatFs =  StorageUtil.getExternalMemory();
        List<StatFs> sdStatFs = StorageUtil.getSDCardMemory(context);

        String strRAM = "可用/总共：" + Formatter.formatFileSize(context, memoryInfo.availMem)
                + "/" + Formatter.formatFileSize(context, memoryInfo.totalMem);
        String strExt = "可用/总共：" + StorageUtil.getAvailableMemorySize(context,extStatFs)+ "/" +StorageUtil.getTotalMemorySize(context,extStatFs);
        String strInt = "可用/总共：" + StorageUtil.getAvailableMemorySize(context,intStatFs)+ "/" +StorageUtil.getTotalMemorySize(context,intStatFs);


        return "\n\n  1. IMEI:\n\t\t" + "getIMEI(context)"

                + "\n\n2. 设备宽度:\n\t\t" + getDeviceWidth(context)

                + "\n\n3. 设备高度:\n\t\t" + getDeviceHeight(context)

                + "\n\n4. 是否有内置SD卡:\n\t\t" + StorageUtil.isSDCardMount()

                + "\n\n5. RAM 信息:\n\t\t" +  strRAM

                + "\n\n6. 内置内部存储信息\n\t\t" + strInt

                + "\n\n7. 内置外部存储卡 信息:\n\t\t" + strExt

                + "\n\n7. SD卡 信息:\n\t\t" + (sdStatFs.isEmpty() ? "无SD卡" : "可用/总共：" + StorageUtil.getAvailableMemorySize(context,sdStatFs.get(0))+ "/" +StorageUtil.getTotalMemorySize(context,sdStatFs.get(0)))

                + "\n\n8. 是否联网:\n\t\t" + NetUtil.isNetworkConnected(context)
//
                + "\n\n9. 网络类型:\n\t\t" + NetUtil.getNetworkType(context)

                + "\n\n10. 系统默认语言:\n\t\t" + getDeviceDefaultLanguage()

                + "\n\n11. 硬件序列号(设备名):\n\t\t" + android.os.Build.SERIAL

                + "\n\n12. 手机型号:\n\t\t" + android.os.Build.MODEL

                + "\n\n13. 生产厂商:\n\t\t" + android.os.Build.MANUFACTURER

                + "\n\n14. 手机Fingerprint标识:\n\t\t" + android.os.Build.FINGERPRINT

                + "\n\n15. Android 版本:\n\t\t" + android.os.Build.VERSION.RELEASE

                + "\n\n16. Android SDK版本:\n\t\t" + android.os.Build.VERSION.SDK_INT

                + "\n\n17. 安全patch 时间:\n\t\t" + android.os.Build.VERSION.SECURITY_PATCH

//                + "\n\n18. 发布时间:\n\t\t" + Utils.Utc2Local(android.os.Build.TIME)

                + "\n\n19. 版本类型:\n\t\t" + android.os.Build.TYPE

                + "\n\n20. 用户名:\n\t\t" + android.os.Build.USER

                + "\n\n21. 产品名:\n\t\t" + android.os.Build.PRODUCT

                + "\n\n22. ID:\n\t\t" + android.os.Build.ID

                + "\n\n23. 显示ID:\n\t\t" + android.os.Build.DISPLAY

                + "\n\n24. 硬件名:\n\t\t" + android.os.Build.HARDWARE

                + "\n\n25. 产品名:\n\t\t" + android.os.Build.DEVICE

                + "\n\n26. Bootloader:\n\t\t" + android.os.Build.BOOTLOADER

                + "\n\n27. 主板名:\n\t\t" + android.os.Build.BOARD

                + "\n\n28. CodeName:\n\t\t" + android.os.Build.VERSION.CODENAME

                + "\n\n29. 无线电固件版本:\n\t\t" + Build.getRadioVersion();

    }
}
