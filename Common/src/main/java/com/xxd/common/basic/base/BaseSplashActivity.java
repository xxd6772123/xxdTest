package com.xxd.common.basic.base;

import com.xxd.common.basic.R;
import com.xxd.common.basic.databinding.ActivityBaseSplashBinding;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */
public class BaseSplashActivity extends BaseNoModelActivity<ActivityBaseSplashBinding> {
    @Override
    protected int onCreate() {
        return R.layout.activity_base_splash;
    }

    @Override
    protected void initView() {
        mDataBinding.imageView.setBorderRadius(20);
        int resId =  getSplashLogoId();
        if (resId > 0) {
            mDataBinding.imageView.setImageResource(resId);
        }
    }

    @Override
    protected void initData() {

    }

    protected int getSplashLogoId(){
        return 0;
    }

    protected void goToMainActivity() {
        this.finish();
    }

}
