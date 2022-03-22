package com.home.glx.uwallet.datas;

import java.io.Serializable;

public class RepaySuccessData implements Serializable {

    /**
     * tradingName : cake
     * charge : 1.54
     * dateStr : 01 Jul 2021 4:18 PM
     * tansFee : 1.54
     * repayAmount : 139.85
     * chargeType : 1
     * violateAmount : 0
     * transNo : BINS570480792701587456
     * logoUrl : paper/534507936909316096.jpeg
     * chargeRate : 1.1
     * totalAmount : 141.39
     * orderAmount : 139.85
     * installmentAmount  : 139.85
     * state : 100200
     */
    private String cardCCType;
    private String cardNo;
    private String tradingName;
    private String charge;
    private String dateStr;
    private String tansFee;
    private String repayAmount;
    private String chargeType;
    private String violateAmount;
    private String transNo;
    private String logoUrl;
    private String chargeRate;
    private String totalAmount;
    private String orderAmount;
    private String installmentAmount;
    private String state;

    public String getCardCCType() {
        return cardCCType;
    }

    public void setCardCCType(String cardCCType) {
        this.cardCCType = cardCCType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getTradingName() {
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getTansFee() {
        return tansFee;
    }

    public void setTansFee(String tansFee) {
        this.tansFee = tansFee;
    }

    public String getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getViolateAmount() {
        return violateAmount;
    }

    public void setViolateAmount(String violateAmount) {
        this.violateAmount = violateAmount;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(String chargeRate) {
        this.chargeRate = chargeRate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(String installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
