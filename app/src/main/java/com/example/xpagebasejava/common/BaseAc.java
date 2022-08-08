package com.example.xpagebasejava.common;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;


import com.example.xpagebasejava.util.StatusBarUtils;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;


public abstract class BaseAc<DB extends ViewDataBinding> extends BaseActivity<DB> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setStatusBarTranslate(this, SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
    }

}
