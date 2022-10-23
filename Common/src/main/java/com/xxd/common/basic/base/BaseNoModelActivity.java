package com.xxd.common.basic.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.blankj.utilcode.util.ThreadUtils;
import com.xxd.common.basic.event.IEventStatListener;
import com.xxd.common.basic.utils.ActivityUtil;
import com.xxd.common.basic.view.LoadingDialog;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc: TODO 不需要ViewModel的页面基类
 */
public abstract class BaseNoModelActivity<DB extends ViewDataBinding> extends AppCompatActivity implements IEventStatListener {

    protected DB mDataBinding;
    protected Context mContext;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ActivityUtil.getInstance().addActivity(this);
        int layoutId = onCreate();
//        setContentView(layoutId);

        mDataBinding = initDataBinding(layoutId);
        initView();
        initData();
    }

    /**
     * 初始化要加载的布局资源ID
     * 此函数优先执行于onCreate()可以做window操作
     */
    protected abstract int onCreate();


    /**
     * 初始化DataBinding
     */
    protected DB initDataBinding(@LayoutRes int layoutId) {
        return DataBindingUtil.setContentView(this, layoutId);
    }

    /**
     * 初始化视图
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();


    /**
     * 加载框是否可被取消的标识，默认可取消，子类可根据实际情况
     * 覆盖该方法做对应的控制
     *
     * @return true：可取消 false：不可取消
     */
    protected boolean loadingDialogCancelable() {
        return true;
    }

    /**
     * 显示用户等待框
     *
     * @param msg 提示信息
     */
    protected void showDialog(String msg) {
        //放在主线程上处理，不用担心外部调用showDialog是否是在主线程上
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void run() {
                executeShowDialog(msg);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void executeShowDialog(String msg) {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext);
            mLoadingDialog.setCancelable(loadingDialogCancelable());
            mLoadingDialog.setLoadingMsg(msg);
            mLoadingDialog.show();
        } else {
            mLoadingDialog.setLoadingMsg(msg);
            if (!mLoadingDialog.isShowing()) {
                mLoadingDialog.show();
            }
        }
    }

    protected void dismissDialog() {
        //放在主线程上处理，不用担心外部调用dismissDialog是否是在主线程上
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                executeDismissDialog();
            }
        });
    }

    /**
     * 隐藏等待框
     */
    protected void hideDialog() {
        //放在主线程上处理，不用担心外部调用dismissDialog是否是在主线程上
        ThreadUtils.runOnUiThreadDelayed(new Runnable() {
            @Override
            public void run() {
                executeHideDialog();
            }
        }, 10);
    }

    private void executeHideDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.hide();
        }
    }

    private void executeDismissDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
        if (mDataBinding != null) {
            mDataBinding.unbind();
        }
        ActivityUtil.getInstance().removeActivity(this);
    }

    /**
     * 启动Activity的简易方法
     *
     * @param activityClazz
     */
    public void startActivity(Class<? extends Activity> activityClazz) {
        Intent intent = new Intent(this, activityClazz);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
    }

}