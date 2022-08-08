package com.example.xpagebasejava.common;

import android.app.Application;

import androidx.multidex.MultiDex;

import com.xuexiang.xpage.PageConfig;
import com.xuexiang.xpage.base.XPageActivity;

/**
 * author:XiaoDan
 * time:2022/6/25 13:47
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化MultiDex
        MultiDex.install(this);
        PageConfig.getInstance()
                .debug("PageLog")       //开启调试
                .setContainActivityClazz(XPageActivity.class) //设置默认的容器Activity
                .init(this);            //初始化页面配置
    }
}
