package com.xxd.common.basic.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:MVP架构的BaseActivity类
 */
public abstract class BaseMvpActivity<P extends APresenter, DB extends ViewDataBinding> extends BaseNoModelActivity<DB> {

    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        mPresenter.attachView(getIView());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    protected abstract P createPresenter();

    protected abstract IView getIView();
}
