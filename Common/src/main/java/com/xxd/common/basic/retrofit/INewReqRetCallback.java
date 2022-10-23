package com.xxd.common.basic.retrofit;

import androidx.annotation.Nullable;
/**
 * @Author: XiaoDan
 * @Date: 2022/8/14
 * @Description: 请求结果回调
 */
public interface INewReqRetCallback<T> {

    /**
     * 请求结果回调接口
     *
     * @param code   结果码
     * @param msg    消息
     * @param result 请求到的数据
     */
    void onResult(int code, String msg, @Nullable T result);

}
