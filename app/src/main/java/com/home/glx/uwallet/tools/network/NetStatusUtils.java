package com.home.glx.uwallet.tools.network;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

/**
 * 网络链接状态
 */
public class NetStatusUtils {

    //判断网络连接状态
    public static boolean isNetWorkConnect(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    //判断wifi连接
    public static boolean isWifiConnect(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    //判断移动网络
    public static boolean isMobileConnect(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }

    //获取连接类型
    public static int connectType(Context context) {
        if (context != null) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                return networkInfo.getType();
            }
        }
        return -1;
    }

    //隐式跳转设置网络
    public static void netWorkSetting(Context context) {
        if (context != null) {
            if (Build.VERSION.SDK_INT > 10) {
                context.startActivity(new Intent("android.settings.SETTINGS"));
                return;
            }
            context.startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
        }
    }

    //吐丝方法
    public static void showToast(Context context, String str) {
//        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

}
