package com.xxd.common.basic.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;

import com.blankj.utilcode.util.Utils;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 * * 注意事项：
 *  * 1、需在AndroidManifest.xml中添加以下4中权限
 *  * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 *  * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 *  * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 *  * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 *  * 2、需动态申请权限："android.permission.ACCESS_FINE_LOCATION"
 */
public class WiFiUtil {


    private static final String TAG = WiFiUtil.class.getSimpleName();

    //获取wifi名称ssid 和 bssid mac地址 返回wifiInfo对象
    public static WifiInfo ssid_data(Context context) {
        final WifiManager wifiManager1 = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifiManager1.getConnectionInfo();
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                return intIP2StringIP(wifiInfo.getIpAddress());
            }
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static String getIpAddrMaskForInterfaces() {
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();    //获取本机所有的网络接口
            while (networkInterfaceEnumeration.hasMoreElements()) { //判断 Enumeration 对象中是否还有数据
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement(); //获取 Enumeration 对象中的下一个数据
                if (!networkInterface.isUp() && !"eth0".equals(networkInterface.getDisplayName())) { //判断网口是否在使用，判断是否时我们获取的网口
                    continue;
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {    //
                    if (interfaceAddress.getAddress() instanceof Inet4Address) {    //仅仅处理ipv4
                        return calcMaskByPrefixLength(interfaceAddress.getNetworkPrefixLength());   //获取掩码位数，通过 calcMaskByPrefixLength 转换为字符串
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "error";
    }

    //通过子网掩码的位数计算子网掩码
    private static String calcMaskByPrefixLength(int length) {
        int mask = 0xffffffff << (32 - length);
        int partsNum = 4;
        int bitsOfPart = 8;
        int[] maskParts = new int[partsNum];
        int selector = 0x000000ff;

        for (int i = 0; i < maskParts.length; i++) {
            int pos = maskParts.length - 1 - i;
            maskParts[pos] = (mask >> (i * bitsOfPart)) & selector;
        }

        StringBuilder result = new StringBuilder();
        result.append(maskParts[0]);
        for (int i = 1; i < maskParts.length; i++) {
            result.append(".").append(maskParts[i]);
        }
        return result.toString();
    }

    private static WifiManager.LocalOnlyHotspotReservation sReservation;

    /**
     * 关闭wifi创建并打开热点
     *
     * @param callback
     */
    @SuppressLint("MissingPermission")
    @RequiresPermission(allOf = {
            android.Manifest.permission.CHANGE_WIFI_STATE,
            android.Manifest.permission.ACCESS_FINE_LOCATION
    })
    public static void openHotSpot(HotSpotCallback callback) {
        WifiManager wifiManager = (WifiManager) Utils.getApp().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                wifiManager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {

                    @Override
                    public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                        super.onStarted(reservation);
                        sReservation = reservation;
                        WifiConfiguration wifiConfiguration = reservation.getWifiConfiguration();
                        if (callback != null) {
                            String ip = getIpAddressString();
                            callback.onSuccess(wifiConfiguration.SSID, wifiConfiguration.preSharedKey, ip);
                        }
                    }

                    @Override
                    public void onStopped() {
                        super.onStopped();
                    }

                    @Override
                    public void onFailed(int reason) {
                        super.onFailed(reason);
                        Log.e(TAG, "startLocalOnlyHotspot: onFailed: reason = " + reason);
                        if (callback != null) {
                            callback.onFail(reason);
                        }
                    }
                }, null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            final String hotSpotName = "AndroidShare_" + System.currentTimeMillis();
            boolean ret = createWifiHot(hotSpotName);
            if (callback != null) {
                if (ret) {
                    String ip = getIpAddressString();
                    callback.onSuccess(hotSpotName, null, ip);
                } else {
                    callback.onFail(0);
                }
            }
        }
    }

    /**
     * 创建并打开wifi热点
     *
     * @param name
     */
    private static boolean createWifiHot(String name) {
        try {
            WifiManager mWifiManager = (WifiManager) Utils.getApp().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            Method method = WifiManager.class.getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);

            WifiConfiguration config = new WifiConfiguration();
            config.SSID = name;
            config.status = WifiConfiguration.Status.ENABLED;
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

            boolean ret = (boolean) method.invoke(mWifiManager, config, true);
            return ret;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭之前通过openHotSpot打开的热点
     */
    public static void closeHotSpot() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (sReservation != null) {
                    sReservation.close();
                    sReservation = null;
                }
            } else {
                WifiManager WifiManager = (WifiManager) Utils.getApp().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                Method method = WifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                method.invoke(WifiManager, null, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 手机上热点默认ip
     */
    private static final String DEF_IP = "192.168.43.1";

    /**
     * 获取IPV4的地址
     *
     * @return
     */
    private static String getIpAddressString() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return DEF_IP;
    }

    /**
     * 创建wifi热点结果回调接口
     */
    public interface HotSpotCallback {

        /**
         * 创建热点成功回调方法
         *
         * @param ssId 热点名称
         * @param psk  热点密钥
         * @param ip   该热点的ip地址
         */
        void onSuccess(String ssId, String psk, String ip);

        /**
         * 创建热点失败回调方法
         *
         * @param reason
         */
        void onFail(int reason);
    }


    public static final String SECURITY_NONE = "OPEN";
    public static final String SECURITY_WEP = "WEP";
    public static final String SECURITY_PSK = "PSK";
    public static final String SECURITY_EAP = "EAP";

    /**
     * 获取热点的加密类型
     *
     * @param result
     * @return
     */
    public static String getSecurity(@NonNull ScanResult result) {
        String security = SECURITY_NONE;
        if (result.capabilities.contains(SECURITY_WEP)) {
            security = SECURITY_WEP;

        } else if (result.capabilities.contains(SECURITY_PSK)) {
            security = SECURITY_PSK;

        } else if (result.capabilities.contains(SECURITY_EAP)) {
            security = SECURITY_EAP;

        }

        return security;
    }

    /**
     * 判断某一wifi热点是否为开放即非加密类型热点
     *
     * @param result
     * @return true：表明该热点为开放的非加密类型热点
     */
    public static boolean isOpened(@NonNull ScanResult result) {
        String security = getSecurity(result);
        return SECURITY_NONE.equals(security);
    }

    /**
     * 判断ssid对应的热点是否已连接
     *
     * @param ssid
     * @return
     */
    public static boolean isConnected(@NonNull String ssid) {
        WifiManager wifiManager = (WifiManager) Utils.getApp().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null) {
            return false;
        }
        String connectedSsid = wifiInfo.getSSID();
        if (isJellyBeanOrLater()) {
            ssid = convertToQuotedString(ssid);
        }
        return connectedSsid != null && connectedSsid.equals(ssid);
    }

    /**
     * 判断result对应的热点是否已连接
     *
     * @param result
     * @return
     */
    public static boolean isConnected(@NonNull ScanResult result) {
        return isConnected(result.SSID);
    }

    /**
     * 判断某一wifi热点之前是否已配置连接过
     *
     * @param result
     * @return
     */
    @Deprecated
    @SuppressLint("MissingPermission")
    public static boolean isConfigured(@NonNull ScanResult result) {
        WifiManager wifiManager = (WifiManager) Utils.getApp().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configList = wifiManager.getConfiguredNetworks();
        if (configList == null) {
            Log.i(TAG, "isConfigured: not configured network");
            return false;
        }

        for (WifiConfiguration configuration : configList) {
            Log.i(TAG, "isConfigured:result(" + result.BSSID + ", " + result.SSID + "), config(" + configuration.BSSID + ", " + configuration.SSID + ")");
            /*if (Objects.equals(result.BSSID, configuration.BSSID) && Objects.equals(result.SSID, trimQuotes(configuration.SSID))) {
                return true;
            }*/
            if (Objects.equals(result.SSID, trimQuotes(configuration.SSID))) {
                boolean ret = true;
                if (!TextUtils.isEmpty(result.BSSID) && !TextUtils.isEmpty(configuration.BSSID)) {
                    ret = result.BSSID.equals(configuration.BSSID);
                }
                return ret;
            }
        }
        return false;
    }

    private static boolean isJellyBeanOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    /**
     * 去除ssid中双引号
     *
     * @param ssid
     * @return
     */
    @Nullable
    public static String trimQuotes(@Nullable String ssid) {
        if (ssid != null && !ssid.isEmpty()) {
            return ssid.replaceAll("^\"*", "").replaceAll("\"*$", "");
        }
        return ssid;
    }

    /**
     * 给ssid添加上双引号
     *
     * @param ssid
     * @return
     */
    public static String convertToQuotedString(@NonNull String ssid) {
        if (TextUtils.isEmpty(ssid)) {
            return "";
        }

        final int lastPos = ssid.length() - 1;
        if (lastPos < 0 || (ssid.charAt(0) == '"' && ssid.charAt(lastPos) == '"')) {
            return ssid;
        }

        return "\"" + ssid + "\"";
    }
}
