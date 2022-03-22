package com.home.glx.uwallet.datas;

import java.util.List;

public class UserRepayFeeData {

    private String overdueDateStr;
    private String cardId;
    private String fee;
    private String cardType;
    private String feeType;
    private String cardNo;
    private String feeRate;
    private String cardCcType;
    private List<OrderDataBean> orderData;

    public String getOverdueDateStr() {
        return overdueDateStr;
    }

    public void setOverdueDateStr(String overdueDateStr) {
        this.overdueDateStr = overdueDateStr;
    }

    public List<OrderDataBean> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<OrderDataBean> orderData) {
        this.orderData = orderData;
    }

    public String getCardCcType() {
        return cardCcType;
    }

    public void setCardCcType(String cardCcType) {
        this.cardCcType = cardCcType;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(String feeRate) {
        this.feeRate = feeRate;
    }

    public static class OrderDataBean {
        /**
         * practicalName : cake
         * repayAmount : 14.35
         * transNo : BINS570465761490456576
         * borrowId : 570465776367652864
         */

        private String practicalName;
        private String repayAmount;
        private String transNo;
        private String borrowId;

        public String getPracticalName() {
            return practicalName;
        }

        public void setPracticalName(String practicalName) {
            this.practicalName = practicalName;
        }

        public String getRepayAmount() {
            return repayAmount;
        }

        public void setRepayAmount(String repayAmount) {
            this.repayAmount = repayAmount;
        }

        public String getTransNo() {
            return transNo;
        }

        public void setTransNo(String transNo) {
            this.transNo = transNo;
        }

        public String getBorrowId() {
            return borrowId;
        }

        public void setBorrowId(String borrowId) {
            this.borrowId = borrowId;
        }
    }
}
