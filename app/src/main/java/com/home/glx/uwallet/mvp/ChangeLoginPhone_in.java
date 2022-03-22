package com.home.glx.uwallet.mvp;

/**
 * 修改登录手机号
 */
public interface ChangeLoginPhone_in {

    void sendOldPhoneCodeRes();

    void checkOldPhoneCodeRes();

    void sendNewPhoneCodeRes();

    void cheskNewPhoneCodeRes();

    void errorSexCodeRes();
}
