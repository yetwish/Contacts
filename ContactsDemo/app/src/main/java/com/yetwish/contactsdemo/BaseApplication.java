package com.yetwish.contactsdemo;

import android.app.Application;

import com.yetwish.contactsdemo.database.DbContactsManager;

/**
 *
 * Created by yetwish on 2016/9/11.
 */
public class BaseApplication extends Application {

    private static BaseApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DbContactsManager.getInstance().closeDb(); //app 退出时关闭db
    }

    public static BaseApplication getInstance(){
        return sInstance;
    }
}
