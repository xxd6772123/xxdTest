package com.xxd.common.basic.utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:MD5工具类
 */
public class MD5Utils {
    /**
     * 获取单个文件的MD5值！
     *
     * @param file
     * @return
     */

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 获取文件夹中文件的MD5值
     *
     * @param file
     * @param listChild ;true递归子目录中的文件
     * @return
     */
    public static Map<String, String> getDirMD5(File file, boolean listChild) {
        if (!file.isDirectory()) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        String md5;
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory() && listChild) {
                map.putAll(getDirMD5(f, listChild));
            } else {
                md5 = getFileMD5(f);
                if (md5 != null) {
                    map.put(f.getPath(), md5);
                }
            }
        }
        return map;
    }

    public static boolean checkFileMd5(String path, String md5) {
        if (FileUtils.isFileExists(path)) {
            String hexdigest = getFileMD5(new File(path));
            assert hexdigest != null;
            return !TextUtils.isEmpty(hexdigest) && hexdigest.equals(md5);
        } else {
            return false;
        }
    }

    public static String strToMd5By32(String str) {
        String reStr = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bytes) {
                int bt = b & 0xff;
                if (bt < 16) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            reStr = stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return reStr;
    }

    public static String strToMd5By16(String str) {
        String reStr = strToMd5By32(str);
        if (reStr != null) {
            reStr = reStr.substring(8, 24);
        }
        return reStr;
    }

    public static String getMd5(@NonNull Uri fileUri) {
        ContentResolver resolver = Utils.getApp().getContentResolver();
        InputStream inputStream = null;
        String md5Value = null;
        try {
            inputStream = resolver.openInputStream(fileUri);
            md5Value = getMd5Str(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(inputStream);
        }
        return md5Value;
    }

    public static String getMd5Str(@NonNull InputStream inputStream) {
        byte[] datas = getMd5(inputStream);
        return ConvertUtils.bytes2HexString(datas);
    }

    public static byte[] getMd5(@NonNull InputStream inputStream) {
        DigestInputStream digestInputStream = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            digestInputStream = new DigestInputStream(inputStream, md);
            byte[] buffer = new byte[256 * 1024];
            while (true) {
                if (!(digestInputStream.read(buffer) > 0)) {
                    break;
                }
            }
            md = digestInputStream.getMessageDigest();
            return md.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtil.close(digestInputStream);
        }
    }
}
