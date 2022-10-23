package com.xxd.common.basic.base;

import android.content.Context;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:包含ViewDataBinding实例对象的基类Dialog
 */
public abstract class BaseNoModelDialog<DB extends ViewDataBinding> extends BaseDialog {

    protected DB mDataBinding;

    public BaseNoModelDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected View getContentView() {
        initDataBinding();
        return mDataBinding.getRoot();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDataBinding != null) {
            mDataBinding.unbind();
        }
    }

    private void initDataBinding() {
        int layoutId = getLayoutId();
        mDataBinding = DataBindingUtil.inflate(getLayoutInflater(), layoutId, null, false);
    }

    protected abstract @LayoutRes
    int getLayoutId();
}
