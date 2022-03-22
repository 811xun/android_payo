package com.home.glx.uwallet.mvp.contract;

/**
 * 通用回调接口.
 */
public interface ActionCallBack {

    void onSuccess(Object... o);

    void onError(String e);
}
