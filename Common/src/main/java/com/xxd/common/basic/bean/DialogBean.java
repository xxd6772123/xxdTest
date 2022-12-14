package com.xxd.common.basic.bean;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:封装的对话框实体类
 */
public final class DialogBean {
    private boolean isShow;
    private String msg;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
