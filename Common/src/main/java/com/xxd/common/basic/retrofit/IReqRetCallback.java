package com.xxd.common.basic.retrofit;

import androidx.annotation.Nullable;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:请求结果回调
 */

public interface IReqRetCallback<T> {

    /**
     * 请求结果回调方法
     *
     * @param success true:表示请求成功 false：表示请求失败
     * @param msg 请求结果消息
     * @param result 当success为true时，该对象才不为空, 否则为空
     */
    void onResult(boolean success, String msg, @Nullable T result);

}
