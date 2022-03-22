package com.home.glx.uwallet.mvp;

import com.home.glx.uwallet.datas.FenQiMsgDatas;

import java.util.Map;

/**
 * 激活、变更或关闭会员等级
 */
public interface JiHuoFenQi_in {

    interface View {
        void setFenQiStatus(String status);

        void setFenQiMsg(FenQiMsgDatas fenQiMsg);

        void setFenQiLimit(String limit);

        void setNoLoginFenQiLimit(String limit);
    }

    interface InitFenQiView {
        void setStatus(String status);
    }

    interface ActivationInstallmentView {
        void setFKStatus(String status, String rejectMessage);
    }

    interface bankStatementsIsResultView {
        void setStatus(String status);
    }

    interface Model {
        void reqJiHuo(Map<String, Object> maps);

        void getFenQiLimit(Map<String, Object> maps);

        void getFenQiMsg(Map<String, Object> maps);

        void getNoLoginFenQiLimit(Map<String, Object> maps);

        void initFenQi(Map<String, Object> maps);

        void activationInstallment(Map<String, Object> maps);

        void bankStatementsIsResult(Map<String, Object> maps);
    }

    interface Present {
        void reqJiHuo(Map<String, Object> maps);

        void resJiHuoStatus(String dataStr);

        void loadFenQiMsg(Map<String, Object> maps);

        void loadFenQiLimitMsg(Map<String, Object> maps);

        void loadNoLoginFenQiLimitMsg(Map<String, Object> maps);

        void resFenQiMsg(String dataStr);

        void resFenQiLimit(String dataStr);

        void resNoLoginFenQiLimit(String dataStr);

        void initFenQi(Map<String, Object> maps);

        void resFenQi(String dataStr);

        void reqActivationInstallment(Map<String, Object> maps);

        void resActivationInstallment(String status, String rejectMessage);

        void reqBankStatementsIsResult(Map<String, Object> maps);

        void resBankStatementsIsResult(String status);
    }

}
