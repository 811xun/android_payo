package com.home.glx.uwallet.mvp.contract;

import android.content.Context;

import java.util.Map;

public interface KycAndIllionListener {

    //设置默认卡
    void setDefaultCard(Map<String, Object> maps, ActionCallBack callBack);

    //获取illion机构列表
    void getInstitutions(Map<String, Object> maps, ActionCallBack callBack);

    //KYC新风控接口
    void getKycResult(Map<String, Object> maps, ActionCallBack callBack);

    //预加载illion信息
    void preLoadIllion(Map<String, Object> maps, ActionCallBack callBack);

    //illion获取报告
    void fetchIllion(Map<String, Object> maps, ActionCallBack callBack);

    //illion-mfa二次验证提交
    void mfaSubmit(Map<String, Object> maps, ActionCallBack callBack);

    //获取报告失败发送短信给运营
    void sendIllionMessage(Map<String, Object> maps);

    //记录用户点击申请分期付进入kyc页面次数接口
    void saveUserToKycPageLog(Map<String, Object> maps);
}
