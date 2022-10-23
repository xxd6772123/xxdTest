package com.xxd.common.basic.utils;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:字符串处理工具类
 */
public class StringUtil {
    //中文汉字unicode编码正则表达式
    public static final Pattern PATTERN_ZW_UNICODE = Pattern.compile("\\\\u[a-fA-F0-9]{4}");

    /**
     * 获取源字符串src中通过分隔符delimiter分隔后的子字符串列表
     *
     * @param src
     * @param delimiter
     * @return
     */
    @Nullable
    public static List<String> split2List(String src, String delimiter) {
        if (TextUtils.isEmpty(src)) {
            return null;
        }

        List<String> strList = new ArrayList<>();
        if (TextUtils.isEmpty(delimiter)) {
            strList.add(src);
            return strList;
        }

        String[] splits = src.split(delimiter);
        strList = Arrays.asList(splits);
        return strList;
    }

    /**
     * 获取源字符串src中通过分隔符delimiter分隔后的子字符串数组
     *
     * @param src
     * @param delimiter
     * @return
     */
    @Nullable
    public static String[] split2Array(String src, String delimiter) {
        List<String> list = split2List(src, delimiter);
        if (list == null) {
            return null;
        }
        String[] strs = new String[list.size()];
        list.toArray(strs);
        return strs;
    }

    /**
     * 将输入的带形如(例：\u409F)的汉字的unicode编码的字符串转为中文
     * 示例：input为："\u767d\u7d20\u8d1e-zhu_a1_1"，则返回结果为："白素贞--zhu_a1_1"
     *
     * @param input 包含汉字unicode编码的字符串
     * @return
     */
    public static String decodeCnUnicode(String input) {
        if (TextUtils.isEmpty(input)) {
            return input;
        }
        StringBuilder sb = new StringBuilder();
        Matcher matcher = PATTERN_ZW_UNICODE.matcher(input);
        int start = 0, end = 0;
        String subStr = null;
        while (matcher.find()) {
            start = matcher.start();
            if (start > 0) {
                sb.append(input.substring(0, start));
            }
            end = matcher.end();
            subStr = input.substring(start + 2, end);
            sb.append((char) Integer.parseInt(subStr, 16));

            input = input.substring(end);
            matcher = PATTERN_ZW_UNICODE.matcher(input);
        }
        sb.append(input);
        return sb.toString();
    }
}
