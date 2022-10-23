package com.xxd.common.basic.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.AppUtils;
import com.xxd.common.basic.R;
import com.xxd.common.basic.utils.ObjectUtil;
import com.xxd.common.basic.utils.StatusBarUtils;
import com.xxd.common.basic.utils.ToastUtil;

import java.util.List;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:点击某选项显示对应fragment的主页面基类
 */
public abstract class BaseTabFragmentHomeActivity<DB extends ViewDataBinding> extends BaseNoModelActivity<DB> {

    protected List<FragmentViewBinder> mViewBinderList;
    protected long firstPressedTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onHandleStatusBar();
    }

    /**
     * 状态栏处理，子类可覆盖重写该方法定制对应的状态栏
     */
    protected void onHandleStatusBar() {
        StatusBarUtils.with(this).init();
        StatusBarUtils.setStatusBarTranslate(this, View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstPressedTime < 2000) {
            AppUtils.exitApp();
        } else {
            ToastUtil.shortToast(this, R.string.common_again_to_exit);
            firstPressedTime = System.currentTimeMillis();
        }
    }

    @Override
    protected void initView() {
        List<FragmentViewBinder> viewBinders = getFragmentViewBinders();
        mViewBinderList = viewBinders;
        if (viewBinders == null || viewBinders.size() == 0) {
            return;
        }

        View view;
        boolean isFirst = true;
        for (FragmentViewBinder viewBinder : viewBinders) {
            for (int viewId : viewBinder.viewIds) {
                view = findViewById(viewId);
                view.setOnClickListener(v -> {
                    showFragment(viewBinder);
                    super.onClick(v);
                    onFragmentViewClick(v);
                });
                if (isFirst) {
                    isFirst = false;
                    //view.performClick();
                    showFragment(viewBinder);
                    onFragmentViewClick(view);
                }
            }
        }

    }

    @Override
    protected void initData() {
    }

    /**
     * 获取Fragment与View的绑定对象列表
     *
     * @return
     */
    @NonNull
    protected abstract List<FragmentViewBinder> getFragmentViewBinders();

    /**
     * 点击与Fragment进行了绑定的View的点击处理，子类实现去处理对应的View的变换
     *
     * @param view
     */
    protected abstract void onFragmentViewClick(View view);

    /**
     * 获取Fragment的容器ID
     *
     * @return
     */
    @IdRes
    protected abstract int getFragmentContainerId();

    /**
     * 当默认的实现不能满足需求时，子类可覆盖重写该方法
     *
     * @param fragmentClazz
     * @return
     */
    @NonNull
    protected Fragment getFragment(@NonNull Class<? extends Fragment> fragmentClazz) {
        return ObjectUtil.createFragment(fragmentClazz);
    }

    private void showFragment(FragmentViewBinder showViewBinder) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment fragment;
        for (FragmentViewBinder hideViewBinder : mViewBinderList) {
            if (hideViewBinder == showViewBinder) {
                continue;
            }
            fragment = fragmentManager.findFragmentByTag(hideViewBinder.fragmentClass.getSimpleName());
            if (fragment != null) {
                transaction.hide(fragment);
            }
        }
        fragment = fragmentManager.findFragmentByTag(showViewBinder.fragmentClass.getSimpleName());
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            fragment = getFragment(showViewBinder.fragmentClass);
            showViewBinder.fragmentClass = fragment.getClass();
            transaction.add(getFragmentContainerId(), fragment, showViewBinder.fragmentClass.getSimpleName());
        }
        transaction.commit();
    }

    /**
     * Fragment与View绑定对象（即点击某view时要显示对应的fragment）
     */
    public class FragmentViewBinder {

        public Class<? extends Fragment> fragmentClass;
        public int[] viewIds;

        public FragmentViewBinder(Class<? extends Fragment> fragmentClass, int... viewIds) {
            this.fragmentClass = fragmentClass;
            this.viewIds = viewIds;
        }

    }
}
