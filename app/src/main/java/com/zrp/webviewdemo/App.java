package com.zrp.webviewdemo;

import android.app.Application;

/**
 * 应用application
 * Created by ZRP on 2016/1/5.
 */
public class App extends Application {

    public static String cookie = "uid=123456;sid=dfjakllagbzs";//用户信息的cookie：sid、uid等

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
