package com.xxd.common.basic.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:Toast 和 Snackbar 封装工具类
 */
public final class ToastUtil {
    private ToastUtil(){}

    public static void shortToast(Context context, String tip){
        Toast.makeText(context,tip,Toast.LENGTH_SHORT).show();
    }

    public static void shortToast(Context context,int tip){
        Toast.makeText(context,tip,Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context context,String tip){
        Toast.makeText(context,tip,Toast.LENGTH_LONG).show();
    }

    public static void longToast(Context context,int tip){
        Toast.makeText(context,tip,Toast.LENGTH_SHORT).show();
    }

}
