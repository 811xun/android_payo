package com.home.glx.uwallet.tools.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * 网络状态监听广播
 */
public class NetStatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            boolean connect = NetStatusUtils.isNetWorkConnect(context);
            if (connect) {
//                NetStatusUtils.showToast(context, "网络连接成功");
                if (callBackNetWorkState != null) {
                    //有网络，接口回调一个true
                    callBackNetWorkState.callBackState(true);
                }
            } else {
                if (callBackNetWorkState != null) {
                    //没有网络，接口回调一个false
                    callBackNetWorkState.callBackState(false);
                }
//                NetStatusUtils.showToast(context, "网络连接出错");
            }
        }
    }


    public interface CallBackNetWorkState {
        void callBackState(boolean status);
    }

    private CallBackNetWorkState callBackNetWorkState;

    public void setOnCallBackNetWorkState(CallBackNetWorkState callBackNetWorkState) {
        this.callBackNetWorkState = callBackNetWorkState;
    }


}
