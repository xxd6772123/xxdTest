package com.xxd.common.basic.utils;

import androidx.annotation.NonNull;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:数据处理工具类
 */
public final class DataUtil {

    /**
     * byte数组转成16进制字符串;
     * 16进制字符串转数组BigInteger类提供函数实现
     * new BigInteger(str,16).toByteArray()
     *
     * @return String bytes to string.
     */
    public static String bytesToHex(byte[] array) {
        StringBuffer hexString = new StringBuffer();
        for (byte b : array) {
            int intVal = b & 0xff;
            if (intVal < 0x10)
                hexString.append("0");
            hexString.append(Integer.toHexString(intVal));
        }
        return hexString.toString();
    }

    /**
     * 将16进制字符串转成byte数组;new BigInteger(content,16).toByteArray()这个转换函数有bug，返回数组长度不是字符串长度的一般
     *
     * @param hex 需转换的字符串
     * @return 返回长度是字符串一半的数组
     */
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 将int类型的值转换为对应的字节数组
     *
     * @param value
     * @return
     */
    public static byte[] int2Bytes(int value) {
        byte[] datas = new byte[4];
        int moveBit = 0;
        for (int i = 0; i < datas.length; i++) {
            moveBit = (datas.length - 1 - i) * 8;
            datas[i] = (byte) ((value >> moveBit) & 0xFF);
        }
        return datas;
    }


    /**
     * 将byte数组转换为整型数值
     *
     * @param datas 字节数组 长度不能超过4
     * @return
     */
    public static int bytes2Int(@NonNull byte[] datas) {
        if (datas.length > 4) {
            throw new IllegalArgumentException("bytes2Int: the param datas length can not bigger than 4");
        }
        int value = 0;
        int moveBit = 0;
        for (int i = 0; i < datas.length; i++) {
            moveBit = (datas.length - 1 - i) * 8;
            value |= (datas[i] & 0xFF) << moveBit;
        }
        return value;
    }

}

