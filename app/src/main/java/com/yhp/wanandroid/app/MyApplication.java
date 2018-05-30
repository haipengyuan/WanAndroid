package com.yhp.wanandroid.app;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }

    /**
     * 全局获取Context
     * @return
     */
    public static Context getContext() {
        return mContext;
    }
}
