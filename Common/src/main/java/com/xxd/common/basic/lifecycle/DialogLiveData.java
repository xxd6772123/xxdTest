package com.xxd.common.basic.lifecycle;

import androidx.lifecycle.MutableLiveData;

import com.xxd.common.basic.bean.DialogBean;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */

public final class DialogLiveData<T> extends MutableLiveData<T> {

    private DialogBean bean = new DialogBean();

    public void setValue(boolean isShow) {
        bean.setShow(isShow);
        bean.setMsg("");
        setValue((T) bean);
    }

    public void setValue(boolean isShow, String msg) {
        bean.setShow(isShow);
        bean.setMsg(msg);
        setValue((T) bean);
    }
}
