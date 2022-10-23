package com.xxd.common.basic.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:MVP架构的Fragment基类
 */
public abstract class BaseMvpFragment<P extends APresenter, DB extends ViewDataBinding> extends BaseNoModelFragment<DB> {

    protected P mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = createPresenter();
        mPresenter.attachView(getIView());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    protected abstract P createPresenter();

    protected abstract IView getIView();
}
