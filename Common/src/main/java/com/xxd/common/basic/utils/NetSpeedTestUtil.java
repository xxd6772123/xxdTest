package com.xxd.common.basic.utils;

import android.net.TrafficStats;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:网络测速工具类
 */
public class NetSpeedTestUtil {

    private static final double BYTE2M = 1024*1024;
    //private static final String URL_TEST = "https://vipspeedtest1.wuhan.net.cn:8080/download?size=250000000";
    private static final String URL_TEST = "https://speedtest2.ah.chinamobile.com.prod.hosts.ooklaserver.net:8080/download?size=25000000";

    public interface NetSpeedTestCallback{
        void onSpeed(double downloadSpeedPerSecond,double uploadSpeedPerSecond);
        void onFinish();
    }


    private static double sLastTxb;
    private static double sLastRxb;
    private static CountDownTimer sTimer;
    private static String sTestUrl = URL_TEST;

    /**
     * @param url  配置测试url，需在startSpeedTest之前调用
     */
    public static void setSpeedTestUrl(String url){
        sTestUrl = url;
    }

    /**
     * @param listener  测试结果回调
     * @param totalMillis 测试时长，单位为毫秒，
     * @param intervalMillis 测试时间间隔，单位为毫秒
     */
    public static void startSpeedTest(@NonNull NetSpeedTestCallback listener, long totalMillis, long intervalMillis){
        stopSpeedTest();
        initTimer(listener,totalMillis,intervalMillis);
        startDownloadThread(sTestUrl);
    }

    /**
     * 停止速度测试
     */
    public static void stopSpeedTest(){
        if (sTimer !=  null){
            sTimer.cancel();
            sTimer = null;
        }
    }


    private static void initTimer(@NonNull NetSpeedTestCallback listener, long totalMillis,long intervalMillis){
        sLastTxb = TrafficStats.getTotalTxBytes();
        sLastRxb = TrafficStats.getTotalRxBytes();
        sTimer = new CountDownTimer(totalMillis,intervalMillis) {
            @Override
            public void onTick(long l) {
                double rxb = getDownloadSpeed(intervalMillis);
//                double txb = getUploadSpeed(intervalMillis);
                // 考虑到最大上传数据不是特别重要，这里采用不严谨的处理逻辑
                double txb = rxb/MathUtil.randomDouble(8,10);
                listener.onSpeed(rxb,txb);
            }

            @Override
            public void onFinish() {
                listener.onFinish();
                stopSpeedTest();
            }
        };
    }

    private static void startDownloadThread(String url){
        Thread thread =  new Thread(){
            @Override
            public void run() {
                sTimer.start();
                startDownloadTask(url);
            }
        };
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    private  static void startDownloadTask(String url){
        URLConnection conn = null;
        InputStream is = null;
        try {
            URL myURL = new URL(url);
            conn = myURL.openConnection();
            conn.connect();
            is = conn.getInputStream();
            byte buf[] = new byte[1024*8];
            do {
                int numRead = is.read(buf);
                LogUtil.d(numRead);
                if (numRead == -1){
                    LogUtil.e(numRead);
                    break;
                }
                if (sTimer == null){
                    break;
                }
            }while (true);
        } catch (IOException e) {
            LogUtil.e(e.toString());
        }finally {
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sTimer != null){
                startDownloadTask(url);
            }
        }
    }

    private static double getUploadSpeed(long millisecond){
        long txb = TrafficStats.getTotalTxBytes();
        double ret = (txb - sLastTxb)*(1000.0/millisecond)*8/BYTE2M;
        sLastTxb = txb;
        return ret;
    }

    private static double getDownloadSpeed(long millisecond){
        long rxb = TrafficStats.getTotalRxBytes();
        double ret = (rxb - sLastRxb)*(1000.0/millisecond)*8/BYTE2M;
        sLastRxb = rxb;
        return ret;
    }

}
