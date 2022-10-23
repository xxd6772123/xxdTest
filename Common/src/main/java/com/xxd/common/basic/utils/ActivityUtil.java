package com.xxd.common.basic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Stack;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:管理所有Activity的实例
 */
public class ActivityUtil {


    private static Stack<Activity> sStack;
    private static ActivityUtil sManager;

    /**
     * 获取实例
     */
    public static synchronized ActivityUtil getInstance() {
        if (sManager == null) {
            sManager = new ActivityUtil();
            sStack = new Stack<>();
        }
        return sManager;
    }

    /**
     * 添加Activity
     */
    public synchronized void addActivity(Activity activity) {
        sStack.add(activity);
    }

    /**
     * 移除Activity
     */
    public synchronized void removeActivity(Activity activity) {
        sStack.remove(activity);
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : sStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                return;
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            sStack.remove(activity);
        }
    }

    /**
     * 是否存在Activity
     */
    public boolean containsActivity(Class<?> cls) {
        for (Activity activity : sStack) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (Activity activity : sStack) {
            if (activity != null) {
                activity.finish();
            }
        }
        sStack.clear();
    }

    /**
     * 启动clazz目标的activity，若reqCode不为null，则以startActivityForResult方式跳转，否则以startActivity的方式跳转
     *
     * @param context
     * @param clazz
     * @param data
     * @param reqCode
     */
    public static void startActivityForRet(Context context, Class<? extends Activity> clazz,
                                           @Nullable Bundle data, @Nullable Integer reqCode) {
        startActivityForRet((Object) context, clazz, data, reqCode);
    }

    /**
     * 启动clazz目标的activity，若reqCode不为null，则以startActivityForResult方式跳转，否则以startActivity的方式跳转
     *
     * @param obj     该参数必须为Context或者Fragment的子类
     * @param clazz
     * @param data
     * @param reqCode
     */
    public static void startActivityForRet(Object obj, Class<? extends Activity> clazz,
                                           @Nullable Bundle data, @Nullable Integer reqCode) {
        if (!(obj instanceof Context) && !(obj instanceof Fragment)) {
            throw new IllegalArgumentException("The param obj must be subclass of Context or Fragment.");
        }
        Context context = null;
        Fragment fragment = null;
        if (obj instanceof Context) {
            context = (Context) obj;
        } else {
            fragment = (Fragment) obj;
            context = fragment.getActivity();
        }
        //Intent intent = new Intent(context, clazz);
        Intent intent = IntentUtil.getIntent(context, clazz);
        if (data != null) {
            intent.putExtras(data);
        }

        if (reqCode == null) {
            context.startActivity(intent);
        } else {
            if (fragment != null) {
                fragment.startActivityForResult(intent, reqCode);
            } else {
                if (!(context instanceof Activity)) {
                    throw new IllegalArgumentException("ActivityUtil：startActivityForRet：the param obj must be subclass of Activity " +
                            "when you want to start activity for result.");
                }
                ((Activity) context).startActivityForResult(intent, reqCode);
            }
        }
    }
}
