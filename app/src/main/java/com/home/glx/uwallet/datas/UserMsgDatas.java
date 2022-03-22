package com.home.glx.uwallet.datas;

public class UserMsgDatas {

    private String splitAddInfoState;
    private String paymentState;
    private String installmentState;
    private String investState;
    //分期付绑卡状态 0未绑卡 1已绑卡
    private String creditCardState;
    // 0 没绑卡, 1: 绑卡了
    private String cardState;

    public String getCreditCardState() {
        return creditCardState;
    }

    public void setCreditCardState(String creditCardState) {
        this.creditCardState = creditCardState;
    }

    public String getSplitAddInfoState() {
        return splitAddInfoState;
    }

    public void setSplitAddInfoState(String splitAddInfoState) {
        this.splitAddInfoState = splitAddInfoState;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }

    public String getInstallmentState() {
        return installmentState;
    }

    public void setInstallmentState(String installmentState) {
        this.installmentState = installmentState;
    }

    public String getInvestState() {
        return investState;
    }

    public void setInvestState(String investState) {
        this.investState = investState;
    }

    public String getCardState() {
        return cardState;
    }

    public void setCardState(String cardState) {
        this.cardState = cardState;
    }
}
