package com.xxd.common.basic.base;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleObserver;

import com.xxd.common.basic.lifecycle.BaseViewModel;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc: 懒加载Fragment基类，适用于一个页面多个Tab页面
 */
public abstract class BaseLazyFragment<VM extends BaseViewModel, DB extends ViewDataBinding>
        extends BaseFragment<VM, DB> implements LifecycleObserver {

    private boolean mVisibleToUser;

    @Override
    public void onResume() {
        super.onResume();
        if (!mVisibleToUser) {
            mVisibleToUser = true;
            lazyLoad();
        }
    }

    /**
     * 懒加载，只有在Fragment第一次创建且第一次对用户可见
     */
    protected abstract void lazyLoad();
}
