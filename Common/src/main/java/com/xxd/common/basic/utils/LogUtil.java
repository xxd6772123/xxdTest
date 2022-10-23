package com.xxd.common.basic.utils;

import android.util.Log;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:log输出类
 */
public final class LogUtil {

    private static final int I = Log.INFO;
    private static final int D = Log.DEBUG;
    private static final int E = Log.ERROR;
    private static final String TAG = "LogUtil: ";
    private static final String LINE_SEP   = System.getProperty("line.separator");

    public static void d(final Object... contents) {
        log(D, TAG, contents);
    }

    public static void dTag(final String tag, final Object... contents) {
        log(D, tag, contents);
    }

    public static void i(final Object... contents) {
        log(I, TAG, contents);
    }

    public static void iTag(final String tag, final Object... contents) {
        log(I, tag, contents);
    }

    public static void e(final Object... contents) {
        log(E, TAG, contents);
    }

    public static void eTag(final String tag, final Object... contents) {
        log(E, tag, contents);
    }

    private static void log(final int type,final String tag,final Object... contents){
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = contents.length; i < len; ++i) {
            Object content = contents[i];
            sb.append("ARGS")
                    .append("[")
                    .append(i)
                    .append("]")
                    .append(" = ")
                    .append(content == null?"null":content.toString())
                    .append(LINE_SEP);
        }
        String body = sb.toString();
        Log.println(type,tag,body);
    }
}
