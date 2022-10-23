package com.xxd.common.basic.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ThreadUtils;
import com.xxd.common.basic.view.LoadingDialog;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:
 */

public abstract class BaseNoModelFragment<DB extends ViewDataBinding> extends Fragment {

    protected DB mDataBinding;
    protected Context mContext;
    protected Activity mActivity;
    private LoadingDialog mLoadingDialog;
    protected String TAG = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = initDataBinding(inflater, onCreate(), container);
        return mDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        initView();
        initData();
    }

    /**
     * 初始化要加载的布局资源ID
     */
    protected abstract int onCreate();


    /**
     * 初始化DataBinding
     */
    protected DB initDataBinding(LayoutInflater inflater, @LayoutRes int layoutId, ViewGroup container) {
        return DataBindingUtil.inflate(inflater, layoutId, container, false);
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
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                executeShowDialog(msg);
            }
        });
    }

    private void executeShowDialog(String msg) {
        Activity activity = getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (activity == null || (activity.isFinishing() || activity.isDestroyed())) {
                return;
            }
        }

        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.setLoadingMsg(msg);
        } else {
            mLoadingDialog = new LoadingDialog(mContext);
            mLoadingDialog.setCancelable(loadingDialogCancelable());
            mLoadingDialog.setLoadingMsg(msg);
            mLoadingDialog.show();
        }
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

    /**
     * 隐藏等待框
     */
    protected void dismissDialog() {
        //放在主线程上处理，不用担心外部调用dismissDialog是否是在主线程上
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                executeDismissDialog();
            }
        });
    }

    private void executeDismissDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDataBinding != null) {
            mDataBinding.unbind();
        }
    }

    protected void startActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(getActivity(), activityClass);
        startActivity(intent);
    }
}
