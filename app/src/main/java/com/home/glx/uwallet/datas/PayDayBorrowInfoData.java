package com.home.glx.uwallet.datas;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PayDayBorrowInfoData implements Serializable {


    /**
     * repayAmount : 33.54
     * tradingNameList : ["测试shanghu","tew"]
     * needPayRepayIdList : [542971215587528706,542907028798656514,543314760823427074,543278848315969538,542877186833207298]
     * transFee : 0.22
     */

    private String repayAmount;
    private List<String> overDueIds;
    private List<String> tradingNameList;
    private List<String> needPayRepayIdList;
    private String transFee;
    private String totalAmount;
    private String installmentAmount;
    private String violateAmount;
    private List<SuccessListBean> successList;

    public List<String> getOverDueIds() {
        return overDueIds;
    }

    public void setOverDueIds(List<String> overDueIds) {
        this.overDueIds = overDueIds;
    }

    public String getInstallmentAmount() {
        return installmentAmount;
    }

    public void setInstallmentAmount(String installmentAmount) {
        this.installmentAmount = installmentAmount;
    }

    public String getViolateAmount() {
        return violateAmount;
    }

    public void setViolateAmount(String violateAmount) {
        this.violateAmount = violateAmount;
    }

    public List<SuccessListBean> getSuccessList() {
        return successList;
    }

    public void setSuccessList(List<SuccessListBean> successList) {
        this.successList = successList;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
    }

    public List<String> getTradingNameList() {
        return tradingNameList;
    }

    public void setTradingNameList(List<String> tradingNameList) {
        this.tradingNameList = tradingNameList;
    }

    public List<String> getNeedPayRepayIdList() {
        return needPayRepayIdList;
    }

    public void setNeedPayRepayIdList(List<String> needPayRepayIdList) {
        this.needPayRepayIdList = needPayRepayIdList;
    }

    public String getTransFee() {
        return transFee;
    }

    public void setTransFee(String transFee) {
        this.transFee = transFee;
    }

    public static class SuccessListBean implements Serializable{
        /**
         * tradingName : zhengticeshi
         * repayAmount : 78.4
         */

        private String tradingName;
        @SerializedName("repayAmount")
        private Double repayAmountX;

        public String getTradingName() {
            return tradingName;
        }

        public void setTradingName(String tradingName) {
            this.tradingName = tradingName;
        }

        public Double getRepayAmountX() {
            return repayAmountX;
        }

        public void setRepayAmountX(Double repayAmountX) {
            this.repayAmountX = repayAmountX;
        }
    }
}
