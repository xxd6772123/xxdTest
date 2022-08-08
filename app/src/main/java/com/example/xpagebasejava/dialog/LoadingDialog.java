package com.example.xpagebasejava.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.xpagebasejava.R;
import com.example.xpagebasejava.databinding.DialogLoadingBinding;
import com.example.xpagebasejava.util.DensityUtil;

/**
 * author:XiaoDan
 * time:2022/6/25 17:16
 * desc:
 */
public class LoadingDialog extends Dialog {

    private DialogLoadingBinding mBinding;


    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.LoadingDialog);
        setCanceledOnTouchOutside(false);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.dialog_loading, null, false);
        setContentView(mBinding.getRoot());
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = DensityUtil.dip2px(context, 150);
        lp.height = DensityUtil.dip2px(context, 110);
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);
    }

    /**
     * 设置等待提示信息
     */
    public void setLoadingMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        mBinding.tvMsg.setText(msg);
    }
}
