package com.xxd.common.basic.retrofit;

import androidx.annotation.NonNull;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */
public abstract class BaseApiSub<T> extends BaseApi {

    T apiService;

    public T getApiService() {
        if (apiService == null) {
            apiService = createApiService();
        }
        return apiService;
    }

    @NonNull
    protected abstract T createApiService();
}
