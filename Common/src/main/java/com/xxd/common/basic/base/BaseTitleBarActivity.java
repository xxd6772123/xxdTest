package com.xxd.common.basic.base;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:带TitleBar控件页面基类
 */
public abstract class BaseTitleBarActivity<DB extends ViewDataBinding> extends BaseNoModelActivity<DB>
        implements OnTitleBarListener {

    protected TitleBar mTitleBar;

    @Override
    protected void initView() {
        mTitleBar = getTitleBar();
        mTitleBar.setOnTitleBarListener(this);
    }

    @NonNull
    protected abstract TitleBar getTitleBar();

    @Override
    public void onLeftClick(View view) {
        onBackPressed();
    }

    @Override
    public void onTitleClick(View view) {
    }

    @Override
    public void onRightClick(View view) {
    }
}
