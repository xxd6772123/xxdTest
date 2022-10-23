package com.xxd.common.basic.utils;

import java.security.Key;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:AES加解密工具类
 */
public final class AESUtil {

    private static final String CRYPT_ALGORITHMS = "AES/CBC/PKCS7Padding";
    private static final String CRYPT_PASSWORD = "12395f0ba1c8a898201215d5018f7ab0a02";

    /**
     * AES数据加密
     */
    public static String encrypt(String content) {
        return encrypt(content, CRYPT_PASSWORD);
    }

    public static String encrypt(String content,String password) {
        try {
            byte[] data = crypt(content.getBytes("utf-8"),password,Cipher.ENCRYPT_MODE);
//            return new BigInteger(data).toString(16);
            return bytesToHex(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES数据加密
     */
    public static String decrypt(String content) {
        return decrypt(content, CRYPT_PASSWORD);
    }

    public static String decrypt(String content,String password) {
        try {
//            byte[] src = new BigInteger(content,16).toByteArray();
            byte[] src = hexToBytes(content);
            byte[] data = crypt(src,password,Cipher.DECRYPT_MODE);
            return new String(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            sb.append((hex.length() == 1) ? "0" + hex : hex);
        }
        return sb.toString();
    }

    public static byte[] hexToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }


    private static byte[] crypt(byte[] content,String password,int mode) throws Exception{
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] keys =  messageDigest.digest(password.getBytes());
        String strKeys = DataUtil.bytesToHex(keys);
        Key key = new SecretKeySpec(strKeys.getBytes(),0,24, "AES");
        Cipher cipher = Cipher.getInstance(CRYPT_ALGORITHMS);
        cipher.init(mode, key,new IvParameterSpec(new byte[16]));
        return cipher.doFinal(content);
    }


//    public static void main(String[] args) {
//        String content = "1456133878";
//        System.out.println("AES content:" + content );
//        String encrypted = encrypt(content);
//        System.out.println("AES encrypt:" + encrypted);
//        System.out.println("AES decrypt:" + decrypt(encrypted));
//        encrypted = "32ce6c47ee69a4975286039194ba905f9b554ddf4c445aab6d536e5d162665c303384b9bb47826d26c3495447ffa6bab436df3f225f81d45ae9aa805087249c0f534a1a7398d3427da3acb3f15ae7f44a9abbf86e9333eeb546d57ba4926561a2e48ba61589bdd5ebd5bf8ac4adf108f221f872adb60ceee377b979707b1e4cc3da6b5632d90c4a1d71ac3a7157ef4a6a80b73dcdac53010dab5b5fc8c5a655cbd165eabc5a7c535d82f486b1b591548cfd26a44b4253a2d5f3bf446d885daa5f92a15d45b04cd34a6c2b1f6a5a65b94afbf21a1626a6b5954531b37c0a9c88075a7e491515271c7d2e49da3acd76e615edf757426dd77e18472a01413d179be1d5a1cec1e9c1c93dfb79777f06d8c8e218d97de67d1efbd75c0d1c0d5a585652c8e1682dfb9e7f4fbd7c0d35067a4a40d3c78eb8b0e834528c10ecdcc43070a94c1443fd9ffee5dd9fa7d7cf8c58dc2830e0156c673f278181e77eb1f788f40a2b9c74760e2ccc33a718164550a033ef46e582d974f13166191de056c842b37b56ad15612ed1016a06d395abf514bb80b59a4d68de4e5d070b73db75af9d8b3";
//        System.out.println("AES decrypt:" + decrypt(encrypted));
//
//        encrypted = "83d67430e2bd9541b6bd1f02fa858448be69527209302945d1856e17455fba856cdee81a09bc321bec02ba6e97f16b68f6eb4487ee9b78106e22e0c45f761bcd6aea2bdd735179563e102a9bf828ff0b690b92e680f95d396de91c46b96b296b";
//        System.out.println("AES decrypt:" + decrypt(encrypted));
//
//    }
}
