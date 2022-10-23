package com.xxd.common.basic.floating;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.Utils;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:悬浮窗视图基类
 */
public abstract class BaseFloatView implements IFloatView {

    protected WindowManager mWindowManager;
    protected WindowManager.LayoutParams mLayoutParams;
    protected View mContentView;
    private boolean isShow = false;
    protected Context mContext;

    protected BaseFloatView() {
        mContext = Utils.getApp();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mContentView = createContentView();
        mLayoutParams = createLayoutParams();
    }

    @NonNull
    protected abstract View createContentView();

    @NonNull
    protected abstract WindowManager.LayoutParams createLayoutParams();

    @Override
    public void show() {
        if (isShow) return;
        mWindowManager.addView(mContentView, mLayoutParams);
        isShow = true;
    }

    @Override
    public void hide() {
        if (!isShow) return;
        mWindowManager.removeView(mContentView);
        isShow = false;
    }

    @Override
    public boolean isShow() {
        return isShow;
    }

    protected void updateViewLayoutParams() {
        if (mContentView != null && mLayoutParams != null) {
            mWindowManager.updateViewLayout(mContentView, mLayoutParams);
        }
    }
}
