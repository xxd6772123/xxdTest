package com.xxd.common.basic.bean;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;

import java.io.Serializable;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:实体类基类
 */
public class BaseBean implements Serializable {

    @NonNull
    @Override
    public String toString() {
        return GsonUtils.toJson(this);
    }
}
