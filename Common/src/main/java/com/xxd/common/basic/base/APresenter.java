package com.xxd.common.basic.base;

import androidx.annotation.Keep;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:MVP架构中的Presenter抽象类
 */
@Keep
public abstract class APresenter<V extends IView> {

    protected V view;

    public void attachView(V view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
    }

    protected boolean isViewAttached() {
        return this.view != null;
    }

}
