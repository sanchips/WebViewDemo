package com.zrp.webviewdemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * 网络工具类
 */
public class NetUtils {

    /**
     * @return 判断网络是否可用
     */
    public static boolean isAvailable(Context context) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return manager.getActiveNetworkInfo().isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
