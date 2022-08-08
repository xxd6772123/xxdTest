package com.example.xpagebasejava.common;

import android.app.Activity;

import java.util.Stack;

/**
 * 同一应用程序中所有的Activity的栈管理（单例）
 * 涉及到activity的添加、删除指定、删除当前、删除所有、返回栈的大小的方法
 */
public class ActivityManager {

    /**
     * 单例模式：饿汉式
     */
    private ActivityManager() {

    }

    private static ActivityManager activityManager = new ActivityManager();

    public static ActivityManager getInstance() {
        return activityManager;
    }

    /**
     * 提供栈的对象
     */
    private final Stack<Activity> activityStack = new Stack<>();

    /**
     * activity的添加
     *
     * @param activity
     */
    public void add(Activity activity) {
        if (activity != null) {
            activityStack.add(activity);
        }
    }

    /**
     * 删除指定的activity
     */
    public void remove(Activity activity) {
        if (activity != null) {
            for (int i = 0; i < activityStack.size(); i++) {
                Activity currentActivity = activityStack.get(i);
                if (currentActivity.getClass().equals(activity.getClass())) {
                    //消除当前activity
                    currentActivity.finish();
                    //从栈空间中移除
                    activityStack.remove(i);
                }
            }
        }
    }

    /**
     * 删除当前的activity
     */
    public void removeCurrent() {
        //方式一：
        //Activity activity = activityStack.get(activityStack.size() - 1);
        //activity.finish();
        //activityStack.remove(activityStack.size() - 1);

        //方式二：
        Activity activity = activityStack.lastElement();
        activity.finish();
        activityStack.remove(activity);
    }

    /**
     * 删除所有的activity
     */
    public void removeAll() {
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            activity.finish();
            activityStack.remove(activity);
        }
    }

    /**
     * 返回栈的大小
     */
    public int size() {
        return activityStack.size();
    }
}
