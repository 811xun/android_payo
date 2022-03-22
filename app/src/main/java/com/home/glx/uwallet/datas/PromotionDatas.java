package com.home.glx.uwallet.datas;


import java.io.Serializable;
import java.util.List;

public class PromotionDatas implements Serializable {


    private int dataType;
    private List<Promotion> list;

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getDataType() {
        return dataType;
    }

    public void setList(List<Promotion> list) {
        this.list = list;
    }

    public List<Promotion> getList() {
        return list;
    }

    public class Promotion implements Serializable {
        public Promotion() {

        }

        private String couponId;
        private String marketingId;
        private String amount;
        private String code;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        private String description = "";
        private String usedTimeStr;
        private String usedAtMerchantName;
        private int validityLimitState;//有效期限制状态 0 不限制 1 有时间限制
        private String expiredTimeStr = "";
        private int amountLimitState;//限制使用金额状态 0 不限制 1 限制
        private String minTransAmount;
        private String terminatedTimeStr;//终止时间
        private int showState;
        private int type;
        private int payUseState;//1 可用 黄use 2 不可用 灰use

        public int getPayUseState() {
            return payUseState;
        }

        public void setPayUseState(int payUseState) {
            this.payUseState = payUseState;
        }

        public String getUsedTimeStr() {
            return usedTimeStr;
        }

        public void setUsedTimeStr(String usedTimeStr) {
            this.usedTimeStr = usedTimeStr;
        }

        public String getUsedAtMerchantName() {
            return usedAtMerchantName;
        }

        public void setUsedAtMerchantName(String usedAtMerchantName) {
            this.usedAtMerchantName = usedAtMerchantName;
        }

        public void setCouponId(String couponId) {
            this.couponId = couponId;
        }

        public String getCouponId() {
            return couponId;
        }

        public String getExpiredTimeStr() {
            return expiredTimeStr;
        }

        public void setExpiredTimeStr(String expiredTimeStr) {
            this.expiredTimeStr = expiredTimeStr;
        }

        public void setMarketingId(String marketingId) {
            this.marketingId = marketingId;
        }

        public String getMarketingId() {
            return marketingId;
        }


        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setValidityLimitState(int validityLimitState) {
            this.validityLimitState = validityLimitState;
        }

        public int getValidityLimitState() {
            return validityLimitState;
        }

        public void setAmountLimitState(int amountLimitState) {
            this.amountLimitState = amountLimitState;
        }

        public int getAmountLimitState() {
            return amountLimitState;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getMinTransAmount() {
            return minTransAmount;
        }

        public void setMinTransAmount(String minTransAmount) {
            this.minTransAmount = minTransAmount;
        }

        public void setShowState(int showState) {
            this.showState = showState;
        }

        public int getShowState() {
            return showState;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public String getTerminatedTimeStr() {
            return terminatedTimeStr;
        }

        public void setTerminatedTimeStr(String terminatedTimeStr) {
            this.terminatedTimeStr = terminatedTimeStr;
        }
    }

}
