package com.xxd.common.basic.base;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.xxd.common.basic.utils.DensityUtil;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:基类Dialog，自动调整dialog大小为layout设计的大小
 */
public abstract class BaseSmartDialog<DB extends ViewDataBinding> extends BaseNoModelDialog<DB> {

    public BaseSmartDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected WindowManager.LayoutParams configWindowAttribute() {
        View contentView = getLayoutInflater().inflate(getLayoutId(), new FrameLayout(getContext()), false);
        FrameLayout.LayoutParams contentViewLayoutParams = (FrameLayout.LayoutParams) contentView.getLayoutParams();

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        if (contentViewLayoutParams.width == WindowManager.LayoutParams.MATCH_PARENT) {
            layoutParams.width = DensityUtil.getWith(getContext()) - contentViewLayoutParams.leftMargin - contentViewLayoutParams.rightMargin;

        } else if (contentViewLayoutParams.width == WindowManager.LayoutParams.WRAP_CONTENT) {
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;

        } else {
            layoutParams.width = contentViewLayoutParams.width;
        }
        layoutParams.gravity = getGravity();
        return layoutParams;
    }

    /**
     * 子类可根据想要摆放的位置返回对应的值
     *
     * @return
     */
    protected int getGravity() {
        return Gravity.CENTER;
    }
}
