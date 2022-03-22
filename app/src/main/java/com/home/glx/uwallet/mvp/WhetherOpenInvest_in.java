package com.home.glx.uwallet.mvp;

import com.home.glx.uwallet.datas.UserMsgDatas;

import java.util.Map;

/**
 * 是否开通理财产品
 */
public interface WhetherOpenInvest_in {


    interface View {
        void openInvestStatus(String code, String fenqiCard, String zhifu, String fenqi);
    }

    interface Model {
        void getStatus(String code, Map<String, Object> maps);
    }

    interface Present {
        void loadOpenInvestStatus(String code);

        void resOpenInvestStatus(String reqCode, String code, UserMsgDatas userMsgDatas);
    }

}
