package com.xxd.common.basic.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:输入输出流处理工具类
 */
public class IOUtil {

    /**
     * 将输入流中的数据写到输出流中去
     *
     * @param is
     * @param os
     * @return
     */
    public static boolean write(InputStream is, OutputStream os) {
        if (is == null || os == null) {
            return false;
        }

        boolean isOk = false;
        try {
            byte[] data = new byte[4096];
            int readLen = 0;
            while ((readLen = is.read(data)) >= 0) {
                os.write(data, 0, readLen);
            }
            os.flush();
            isOk = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(is);
            IOUtil.close(os);
        }
        return isOk;
    }

    /**
     * 默认以UTF_8的方式读取成字符串
     *
     * @param inputStream
     * @return
     */
    public static String readStream2Str(InputStream inputStream) {
        return readStream2Str(inputStream, Charset.defaultCharset());
    }

    /**
     * 将输入流读取成字符串
     *
     * @param inputStream
     * @param charset
     * @return
     */
    public static String readStream2Str(InputStream inputStream, Charset charset) {
        if (inputStream == null) {
            return null;
        }
        String ret = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] datas = new byte[1024];
        try {
            int readLen = 0;
            while ((readLen = inputStream.read(datas)) > 0) {
                baos.write(datas, 0, readLen);
            }
            baos.flush();
            ret = new String(baos.toByteArray(), charset);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(inputStream);
            IOUtil.close(baos);
        }

        return ret;
    }

    /**
     * 关闭可关闭对象，简易封装
     *
     * @param closeable
     */
    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
