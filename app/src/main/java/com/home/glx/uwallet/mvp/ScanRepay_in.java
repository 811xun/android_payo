package com.home.glx.uwallet.mvp;

import com.home.glx.uwallet.datas.FenQiRepayDatas;

import java.util.Map;

/**
 * 扫码支付获取信息
 */
public interface ScanRepay_in {

    interface View {
        void setFenQiData(FenQiRepayDatas fenQiRepayDatas);

        void setFenQiLimitData(FenQiRepayDatas fenQiRepayDatas);

        void setFenQiLimitData1(FenQiRepayDatas fenQiRepayDatas);

        void setRepayStatus(String status, String data);

        void setFenQiRepayStatus(String status);

        void setQrPayStatus(String status);

        //没有开通分期付时返回折扣
        void unOpenFQzhekou(String zhekou);
    }

    interface Model {

        void getFenQiLimitData(Map<String, Object> maps);

        void getFenQiLimitData1(Map<String, Object> maps);

        void getFenQiData(Map<String, Object> maps);

        void reqRepqy(Map<String, Object> maps);

        void reqQrPay(Map<String, Object> maps);

        void reqQrPayNew(Map<String, Object> maps);

        void reqFenQiRepqy(Map<String, Object> maps);

        void reqUnOpenFQzhekou(Map<String, Object> maps);
    }

    interface Present {
        void getFenQiLimitData(Map<String, Object> maps);

        void getFenQiLimitData1(Map<String, Object> maps);

        void getFenQiData(Map<String, Object> maps);

        void resFenQiLimitData(String data);

        void resFenQiLimitData1(String data);

        void resFenQiData(String data);

        void reqRepqy(Map<String, Object> maps);

        void resRepayData(String dataStr);

        void reqFenQiRepqy(Map<String, Object> maps);

        void reqQrPqy(Map<String, Object> maps);

        void reqQrPqyNew(Map<String, Object> maps);

        void resFenQiRepayData(String dataStr);

        void resQrPayData(String dataStr);

        void reqUnOpenFQzhekou(Map<String, Object> maps);

        void resUnOpenFQzhekou(String dataStr);
    }

}
