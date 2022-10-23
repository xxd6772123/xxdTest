package com.xxd.common.basic.base;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.FragmentUtils;
import com.hjq.bar.TitleBar;
import com.xxd.common.basic.R;
import com.xxd.common.basic.databinding.ActivityCommonBaseTbarFragmentBinding;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */
public abstract class BaseTitleBarFragmentActivity extends BaseTitleBarActivity<ActivityCommonBaseTbarFragmentBinding>{
    @NonNull
    @Override
    protected TitleBar getTitleBar() {
        onConfigTitleBar(mDataBinding.titleBar);
        return mDataBinding.titleBar;
    }

    @Override
    protected int onCreate() {
        return R.layout.activity_common_base_tbar_fragment;
    }

    @Override
    protected void initView() {
        super.initView();
        //add fragment
        FragmentUtils.add(getSupportFragmentManager(), getFragment(), R.id.fragment_container);
    }

    @Override
    protected void initData() {
    }

    /**
     * 子类可覆盖该方法对TitleBar做相应的配置
     *
     * @param titleBar
     */
    protected void onConfigTitleBar(TitleBar titleBar) {
        //不做处理，由子类去做定制处理
    }

    @NonNull
    protected abstract Fragment getFragment();

}
