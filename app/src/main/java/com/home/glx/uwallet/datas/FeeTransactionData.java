package com.home.glx.uwallet.datas;

public class FeeTransactionData {

    /**
     * transAmount : 10.0
     * payAmount : 10.0
     * displayDate : Wed 07 Jul 2021
     * fee :
     * tradingFee : 0.0
     * transState : 0
     * repayList : {"transAmount":10,"expectRepayTime":" 07 Jul 2021","transState":0}
     */

    private String transAmount;
    private String payAmount;
    private String displayDate;
    private String fee;
    private String tradingFee;
    private String transState;
    private RepayListBean repayList;

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getTradingFee() {
        return tradingFee;
    }

    public void setTradingFee(String tradingFee) {
        this.tradingFee = tradingFee;
    }

    public String getTransState() {
        return transState;
    }

    public void setTransState(String transState) {
        this.transState = transState;
    }

    public RepayListBean getRepayList() {
        return repayList;
    }

    public void setRepayList(RepayListBean repayList) {
        this.repayList = repayList;
    }

    public static class RepayListBean {
        /**
         * transAmount : 10.0
         * expectRepayTime :  07 Jul 2021
         * transState : 0
         */

        private String transAmount;
        private String expectRepayTime;
        private String transState;

        public String getTransAmount() {
            return transAmount;
        }

        public void setTransAmount(String transAmount) {
            this.transAmount = transAmount;
        }

        public String getExpectRepayTime() {
            return expectRepayTime;
        }

        public void setExpectRepayTime(String expectRepayTime) {
            this.expectRepayTime = expectRepayTime;
        }

        public String getTransState() {
            return transState;
        }

        public void setTransState(String transState) {
            this.transState = transState;
        }
    }
}
