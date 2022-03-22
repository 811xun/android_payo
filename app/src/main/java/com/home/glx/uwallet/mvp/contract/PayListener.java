package com.home.glx.uwallet.mvp.contract;

import java.util.Map;

public interface PayListener {
    //获取交易金额
    void getPayTransAmountDetail(Map<String, Object> maps, ActionCallBack callBack);

    //交易
    void qrPay(Map<String, Object> maps, ActionCallBack callBack);

    //还款交易查询信息
    void getUserRepayFeeData(Map<String, Object> maps, ActionCallBack callBack);

    //批量主动还款
    void repaymentV2(Map<String, Object> maps, ActionCallBack callBack);
}
