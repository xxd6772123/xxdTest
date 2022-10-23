package com.xxd.common.basic.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.xxd.common.basic.R;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:对话框基类
 */
public abstract class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Context context) {
        this(context, R.style.BaseDialog);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(cancelable());
        setCanceledOnTouchOutside(canceledOnTouchOutside());
        View contentView = getContentView();
        setContentView(contentView);
        initView(contentView);
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = configWindowAttribute();
        if (layoutParams != null) {
            getWindow().setAttributes(layoutParams);
        }
    }

    /**
     * 点击返回键后默认可取消，子类可覆盖该方法进行控制
     * @return
     */
    protected boolean cancelable() {
        return true;
    }

    /**
     * 点击对话框外面默认不可取消，子类可覆盖该方法进行控制
     * @return
     */
    protected boolean canceledOnTouchOutside(){
        return false;
    }

    protected abstract View getContentView();

    protected abstract void initView(View view);

    /**
     * 配置对话框显示的属性，默认返回null不做配置，子类可根据自己需求来做配置
     * 以做个性化的显示
     * @return
     */
    protected WindowManager.LayoutParams configWindowAttribute() {
        return null;
    }

}
