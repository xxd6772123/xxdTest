package com.xxd.common.basic.device;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.RequiresPermission;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:网络工具类
 */
public class NetUtil {

    /**
     * 判断是否有网络连接
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isWIFIConnected(Context context) {
        return isConnected(context, ConnectivityManager.TYPE_WIFI);
    }

    /**
     * 判断是否使用移动网络
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isMobileConnected(Context context) {
        return isConnected(context, ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * 获取当前网络连接的类型信息,-1表示无网络，{@link android.net.ConnectivityManager#TYPE_MOBILE }
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static int getNetworkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null) {
            return info.getType();
        }
        return -1;
    }

    /**
     * 获取IP地址
     *
     * @return IP地址字符串
     */
    public static String getIpAddressString() {
        InetAddress address = getInetAddress();
        if (address != null) {
            return address.getHostAddress();
        } else {
            return "";
        }
    }

    /**
     * 获取网络地址信息
     *
     * @return InetAddress
     */
    public static InetAddress getInetAddress() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * PING 工具接口
     *
     * @param url 对应链接
     * @return 返回ping结果
     */
    public static String ping(String url) {
        String str = "";
        try {
            Process process = Runtime.getRuntime().exec(
                    "/system/bin/ping -c 8 " + url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            int i;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((i = reader.read(buffer)) > 0)
                output.append(buffer, 0, i);
            reader.close();
            str = output.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    private static boolean isConnected(final Context context, int type) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            NetworkInfo networkInfo = manager.getNetworkInfo(type);
            return networkInfo != null && networkInfo.isConnected();
        } else {
            return isConnected(manager, type);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    private static boolean isConnected(ConnectivityManager connMgr, int type) {
        Network[] networks = connMgr.getAllNetworks();
        NetworkInfo info;
        for (Network mNetwork : networks) {
            info = connMgr.getNetworkInfo(mNetwork);
            if (info != null && info.getType() == type && info.isConnected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断俩ip地址是否在同一网段内
     *
     * @param ip1
     * @param ip2
     * @return
     */
    public static boolean isInSameGate(String ip1, String ip2) {
        if (TextUtils.isEmpty(ip1) || TextUtils.isEmpty(ip2)) {
            return false;
        }
        int idx1 = ip1.lastIndexOf(".");
        if (idx1 <= 0) {
            return false;
        }
        int idx2 = ip2.lastIndexOf(".");
        if (idx2 <= 0) {
            return false;
        }

        String gate1 = ip1.substring(0, idx1);
        String gate2 = ip2.substring(0, idx2);
        return gate1.equals(gate2);
    }

}
