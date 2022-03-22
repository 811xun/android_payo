package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.home.glx.uwallet.datas.UserMsgDatas;

import java.util.HashMap;

public class WhetherOpenInvest_p implements WhetherOpenInvest_in.Present {

    private Context context;
    private WhetherOpenInvest_in.View view;
    private WhetherOpenInvest_m model;

    public WhetherOpenInvest_p(Context context, WhetherOpenInvest_in.View view) {
        this.context = context;
        this.view = view;
        model = new WhetherOpenInvest_m(context, this);
    }

    @Override
    public void loadOpenInvestStatus(String code) {
        model.getStatus(code, new HashMap<String, Object>());
    }

    /**
     * @param code
     * @param userMsgDatas investState	Integer	理财业务状态 0：不可用 1：可用 3：kyc通过、人 工审核 4:人工审核拒绝，但已过kyc 5:人工审核通过
     *                     paymentState	Integer	支付业务状态 0：不可用 1：可用
     *                     installmentState	Integer	分期付业务状态 0：不可用 1：可用 2：禁用 3：kyc通过、人工审核
     */
    @Override
    public void resOpenInvestStatus(String reqCode, String code, UserMsgDatas userMsgDatas) {
        if (userMsgDatas != null) {
            if (reqCode.equals("splitFlag")) {
                view.openInvestStatus(reqCode,
                        userMsgDatas.getSplitAddInfoState(),
                        "",
                        "");
            } else {
                if (reqCode.equals("card") || reqCode.equals("sm") || reqCode.equals("nfc") || reqCode.equals("qrCode") || reqCode.equals("posNo")) {
                    view.openInvestStatus(reqCode,
                            userMsgDatas.getCreditCardState() + "",
                            userMsgDatas.getCardState() + "",
                            userMsgDatas.getInstallmentState() + "");
                } else {
                    view.openInvestStatus(reqCode,
                            userMsgDatas.getInvestState() + "",
                            userMsgDatas.getPaymentState() + "",
                            userMsgDatas.getInstallmentState() + "");
                }
            }
        } else {
            view.openInvestStatus(reqCode, null, null, null);
        }
    }
}
