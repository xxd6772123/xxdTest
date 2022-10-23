package com.xxd.common.basic.inf;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:结果回调接口
 */
public interface IRetCallback<T> {

    /**
     * 结果回调方法
     *
     * @param result
     */
    void onResult(T result);

}
