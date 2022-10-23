package com.xxd.common.basic.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * @author:XiaoDan
 * @time:2022/8/14
 * @desc:监听App前台切换状态
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    /**
     * 当前Acitity个数
     */
    private int activityAmount = 0;
    /**
     * Activity 生命周期监听，用于监控app前后台状态切换
     */
    private  ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (activityAmount == 0) {
                onForeground(activity);
            }
            activityAmount++;
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }
        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            activityAmount--;
            if (activityAmount == 0) {
                onBackground();
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }
        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };

    public void onForeground(Activity activity){}
    public void onBackground(){}

}
