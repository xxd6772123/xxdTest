package com.xxd.common.basic.device;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresPermission;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:获取手机SIM
 */
public class SIMCardUtil {

    private static final String FUN_SIM_STATE = "getSimState";//sim卡状态
    private static final String FUN_SIM_SERIAL_NUMBER = "getSimSerialNumber";//手机卡序列号:898xxxxxxxxxxxxxxx
    private static final String FUN_SIM_COUNTRY_ISO = "getSimCountryIso";//手机卡国家简称：cn
    private static final String FUN_SIM_OPERATOR = "getSimOperator";//手机卡运营商：46001
    private static final String FUN_SIM_IMSI = "getSubscriberId";//手机卡的IMSI：4600179xxxxxxxx
//    private static final String FUN_IMEI = "getDeviceIdGemini";//设备IMEI：4600179xxxxxxxx---MTX
//    private static final String FUN_IMEI2 = "getDeviceId";//和上面的方法一样的功能，不同平台自己定制的内容不一样

    private static final String FUN_NETWORK_TYPE = "getNetworkType";//当前使用的网络类型
    private static final String FUN_NETWORK_OPERATOR_NAME = "getNetworkOperatorName";//(当前已注册的用户)运营商名字:
    private static final String FUN_PHONE_TYPE = "getPhoneType";//手机类型
    private static final String FUN_DATA_STATE = "getDataState";//获取数据连接状态
    private static final String FUN_IS_NETWORK_ROAMING = "isNetworkRoaming";//是否漫游

    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static List<SIMCard> getSIMCardList(Context context) {
        List<SIMCard> SIMCards = new ArrayList<>();
        List<String> ids = getSIMIDList(context);
        for (String id : ids) {
            SIMCard card = new SIMCard();
            card.simState = getOperatorBySlot(context, FUN_SIM_STATE, id);
            card.simSerialNumber = getOperatorBySlot(context, FUN_SIM_SERIAL_NUMBER, id);
            card.simCountryIso = getOperatorBySlot(context, FUN_SIM_COUNTRY_ISO, id);
            card.simOperator = getOperatorBySlot(context, FUN_SIM_OPERATOR, id);
            card.imsi = getOperatorBySlot(context, FUN_SIM_IMSI, id);
            card.networkType = getOperatorBySlot(context, FUN_NETWORK_TYPE, id);
            card.networkOperatorName = getOperatorBySlot(context, FUN_NETWORK_OPERATOR_NAME, id);
            card.phoneType = getOperatorBySlot(context, FUN_PHONE_TYPE, id);
            card.dataState = getOperatorBySlot(context, FUN_DATA_STATE, id);
            card.isNetworkRoaming = getOperatorBySlot(context, FUN_IS_NETWORK_ROAMING, id);
            SIMCards.add(card);
        }
        return SIMCards;
    }

    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    private static List<String> getSIMIDList(Context context) {
        List<String> arr = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager manager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            List<SubscriptionInfo> activeSubscriptionInfoList = manager.getActiveSubscriptionInfoList();//手机SIM卡信息
            if (activeSubscriptionInfoList == null) return arr;
            for (SubscriptionInfo info : activeSubscriptionInfoList) {
                arr.add(info.getSubscriptionId() + "");
            }
        } else {
            Uri uri = Uri.parse("content://telephony/siminfo"); //访问raw_contacts表
            ContentResolver resolver = context.getContentResolver();
            Cursor cursor = resolver.query(uri, new String[]{"_id", "icc_id", "sim_id", "display_name", "carrier_name", "name_source", "color", "number", "display_number_format", "data_roaming", "mcc", "mnc"}, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    //当simId==-1时说明卡槽没有插入sim卡，其他情况下说明有卡
                    String id = cursor.getString(cursor.getColumnIndex("_id"));
                    String sim_id = cursor.getString(cursor.getColumnIndex("sim_id"));
                    if (Integer.valueOf(sim_id) >= 0) {
                        arr.add(id);
                    }
                }
                cursor.close();
            }
        }
        return arr;
    }

    private static String getOperatorBySlot(Context context, String predictedMethodName, String slotID) {
        String inumeric = null;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);
            Object[] obParameter = new Object[1];
            obParameter[0] = Integer.valueOf(slotID);
            Object ob_phone = getSimID.invoke(telephony, obParameter);
            if (ob_phone != null) {
                inumeric = ob_phone.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inumeric;
    }

    /**
     * SIM卡
     */
    public static class SIMCard {

        private SIMCard() {
        }

        /**
         * 按照字母次序的current registered operator(当前已注册的用户)的名字<br/>
         * 注意：仅当用户已在网络注册时有效。<br/>
         * 在CDMA网络中结果也许不可靠。
         */
        public String networkOperatorName;
        /**
         * 当前使用的网络类型：<br/>
         * NETWORK_TYPE_UNKNOWN 网络类型未知 0<br/>
         * NETWORK_TYPE_GPRS GPRS网络 1<br/>
         * NETWORK_TYPE_EDGE EDGE网络 2<br/>
         * NETWORK_TYPE_UMTS UMTS网络 3<br/>
         * NETWORK_TYPE_HSDPA HSDPA网络 8<br/>
         * NETWORK_TYPE_HSUPA HSUPA网络 9<br/>
         * NETWORK_TYPE_HSPA HSPA网络 10<br/>
         * NETWORK_TYPE_CDMA CDMA网络,IS95A 或 IS95B. 4<br/>
         * NETWORK_TYPE_EVDO_0 EVDO网络, revision 0. 5<br/>
         * NETWORK_TYPE_EVDO_A EVDO网络, revision A. 6<br/>
         * NETWORK_TYPE_1xRTT 1xRTT网络 7<br/>
         * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO<br/>
         */
        public String networkType;
        /**
         * 是否漫游:(在GSM用途下)
         */
        public String isNetworkRoaming;
        /**
         * 获取数据连接状态<br/>
         * DATA_CONNECTED 数据连接状态：已连接<br/>
         * DATA_CONNECTING 数据连接状态：正在连接<br/>
         * DATA_DISCONNECTED 数据连接状态：断开<br/>
         * DATA_SUSPENDED 数据连接状态：暂停<br/>
         */
        public String dataState;
        /**
         * 返回移动终端的类型：<br/>
         * PHONE_TYPE_CDMA 手机制式为CDMA，电信<br/>
         * PHONE_TYPE_GSM 手机制式为GSM，移动和联通<br/>
         * PHONE_TYPE_NONE 手机制式未知<br/>
         */
        public String phoneType;

        /**
         * sim卡状态
         */
        public String simState;

        /**
         * 手机卡序列号:898xxxxxxxxxxxxxxx
         */
        public String simSerialNumber;

        /**
         * 手机卡国家简称：cn
         */
        public String simCountryIso;
        /**
         * 手机卡运营商：46001
         */
        public String simOperator;
        /**
         * 手机卡运营商名称：中国联通
         */
        public String simOperatorName;
        /**
         * 手机卡的IMSI：4600179xxxxxxxx
         */
        public String imsi;

        /**
         * sim卡槽对应的设备号IMEI
         */
        public String imei;

        @Override
        public String toString() {
            return "SIMCard{" +
                    "networkOperatorName='" + networkOperatorName + '\'' +
                    ", networkType='" + networkType + '\'' +
                    ", isNetworkRoaming='" + isNetworkRoaming + '\'' +
                    ", dataState='" + dataState + '\'' +
                    ", phoneType='" + phoneType + '\'' +
                    ", simState='" + simState + '\'' +
                    ", simSerialNumber='" + simSerialNumber + '\'' +
                    ", simCountryIso='" + simCountryIso + '\'' +
                    ", simOperator='" + simOperator + '\'' +
                    ", simOperatorName='" + simOperatorName + '\'' +
                    ", imsi='" + imsi + '\'' +
                    ", imei='" + imei + '\'' +
                    '}';
        }
    }
}
