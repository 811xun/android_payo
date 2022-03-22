package com.home.glx.uwallet.datas;

import java.io.Serializable;

public class RepaymentSuccessData implements Serializable {


    /**
     * state : 100300
     * repayAmount : 45.63
     * orderAmount : 47.87
     * tansFee : 0.22
     * totalAmount : 45.85
     * dateStr : 12 May 2021 12:45 AM
     * transNo : WINS553122026465349632
     * tradingName : sdf
     */

    private String state;
    private String repayAmount;
    private String orderAmount;
    private String tansFee;
    private String totalAmount;
    private String dateStr;
    private String transNo;
    private String tradingName;
    private String hasOverDue;
    private String overdueAmount;
    private String installmentAmount;

    public String getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(String installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public String getHasOverDue() {
        return hasOverDue;
    }

    public void setHasOverDue(String hasOverDue) {
        this.hasOverDue = hasOverDue;
    }

    public String getOverdueAmount() {
        return overdueAmount;
    }

    public void setOverdueAmount(String overdueAmount) {
        this.overdueAmount = overdueAmount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getTansFee() {
        return tansFee;
    }

    public void setTansFee(String tansFee) {
        this.tansFee = tansFee;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getTradingName() {
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }
}
