package com.home.glx.uwallet.datas;

import java.io.Serializable;
import java.util.List;

public class PayAllData implements Serializable {


    /**
     * haveData : true
     * tradingNameList : ["zhengticeshi","cake shop"]
     * successList : [{"tradingName":"zhengticeshi","repayAmount":78.4},{"tradingName":"cake shop","repayAmount":65.34}]
     * borrowIds : [552358084435726336,552359208253345792]
     * transFee : 0.22
     * repayAmount : 143.74
     * repayIds : [552358084448309248,552358084448309249,552358084448309250,552358084448309251,552359208265928704,552359208265928705,552359208265928706,552359208265928707]
     */

    private String haveData;
    private List<String> tradingNameList;
    private List<SuccessListBean> successList;
    private List<String> borrowIds;
    private List<String> overDueIds;
    private String transFee;
    private String repayAmount;
    private String installmentAmount;
    private String violateAmount;
    private String totalAmount;
    private List<String> repayIds;

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

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getHaveData() {
        return haveData;
    }

    public void setHaveData(String haveData) {
        this.haveData = haveData;
    }

    public List<String> getTradingNameList() {
        return tradingNameList;
    }

    public void setTradingNameList(List<String> tradingNameList) {
        this.tradingNameList = tradingNameList;
    }

    public List<SuccessListBean> getSuccessList() {
        return successList;
    }

    public void setSuccessList(List<SuccessListBean> successList) {
        this.successList = successList;
    }

    public List<String> getBorrowIds() {
        return borrowIds;
    }

    public void setBorrowIds(List<String> borrowIds) {
        this.borrowIds = borrowIds;
    }

    public String getTransFee() {
        return transFee;
    }

    public void setTransFee(String transFee) {
        this.transFee = transFee;
    }

    public String getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
    }

    public List<String> getRepayIds() {
        return repayIds;
    }

    public void setRepayIds(List<String> repayIds) {
        this.repayIds = repayIds;
    }

    public static class SuccessListBean implements Serializable{
        /**
         * tradingName : zhengticeshi
         * repayAmount : 78.4
         */

        private String tradingName;
        private String repayAmount;

        public String getTradingName() {
            return tradingName;
        }

        public void setTradingName(String tradingName) {
            this.tradingName = tradingName;
        }

        public String getRepayAmount() {
            return repayAmount;
        }

        public void setRepayAmount(String repayAmount) {
            this.repayAmount = repayAmount;
        }
    }
}
