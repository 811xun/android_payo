package com.home.glx.uwallet.datas;

import java.io.Serializable;
import java.util.List;

public class TransAmountData implements Serializable {

    /**
     * totalRedEnvelopeAmount : 0.00
     * mixPayAmount : 0
     * wholeSaleUserDiscountAmount : 0.00
     * normalSaleUserDiscountAmount : 1.98
     * transNo : BTPC567495794545479680
     * redEnvelopeAmount : 0
     * normalSalesUserDiscount : 3.00
     * transAmount : 66
     * payAmount : 64.02
     * channelFeeAmount : 1.79
     * transChannelFeeRate : 2.80
     * normalSaleAmount : 66.00
     * wholeSalesUserDiscount : 0.00
     * repeatSaleState : 0
     * wholeSalesAmount : 0.00
     */
    private String creditRealPayAmount;
    private String merchantName;
    private String availableCredit;
    private String allPayAmount;
    private String creditAverageAmount;
    private String totalRedEnvelopeAmount;
    private String period;
    private String cardId;
    private String cardCcType;
    private String cardNo;
    private String creditFirstCardPayAmount;
    private String mixPayAmount;
    private String wholeSaleUserDiscountAmount;
    private String normalSaleUserDiscountAmount;
    private String transNo;
    private String redEnvelopeAmount;
    private String normalSalesUserDiscount;
    private String transAmount;//输入金额页 输入的金额
    private String payAmount;
    private String channelFeeAmount;
    private String transChannelFeeRate;
    private String normalSaleAmount;
    private String wholeSalesUserDiscount;
    private String repeatSaleState;
    private String wholeSalesAmount;
    private String donationAmount;
    private String tipAmount;
    private String toShowFeeAllAmount;
    private DonationData donationData;
    private List<PreviewRepayPlanListBean> previewRepayPlanList;
    private List<TipListBean> tipList;
    private String customerCcExpyr;
    private String customerCcExpmo;

    public String getCustomerCcExpyr() {
        return customerCcExpyr;
    }

    public void setCustomerCcExpyr(String customerCcExpyr) {
        this.customerCcExpyr = customerCcExpyr;
    }

    public String getCustomerCcExpmo() {
        return customerCcExpmo;
    }

    public void setCustomerCcExpmo(String customerCcExpmo) {
        this.customerCcExpmo = customerCcExpmo;
    }

    public String getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(String tipAmount) {
        this.tipAmount = tipAmount;
    }

    public List<TipListBean> getTipList() {
        return tipList;
    }

    public void setTipList(List<TipListBean> tipList) {
        this.tipList = tipList;
    }

    public String getToShowFeeAllAmount() {
        return toShowFeeAllAmount;
    }

    public void setToShowFeeAllAmount(String toShowFeeAllAmount) {
        this.toShowFeeAllAmount = toShowFeeAllAmount;
    }

    public String getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(String donationAmount) {
        this.donationAmount = donationAmount;
    }

    public DonationData getDonationData() {
        return donationData;
    }

    public void setDonationData(DonationData donationData) {
        this.donationData = donationData;
    }

    public String getCreditRealPayAmount() {
        return creditRealPayAmount;
    }

    public void setCreditRealPayAmount(String creditRealPayAmount) {
        this.creditRealPayAmount = creditRealPayAmount;
    }

    public String getAllPayAmount() {
        return allPayAmount;
    }

    public void setAllPayAmount(String allPayAmount) {
        this.allPayAmount = allPayAmount;
    }

    public String getCreditAverageAmount() {
        return creditAverageAmount;
    }

    public void setCreditAverageAmount(String creditAverageAmount) {
        this.creditAverageAmount = creditAverageAmount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardCcType() {
        return cardCcType;
    }

    public void setCardCcType(String cardCcType) {
        this.cardCcType = cardCcType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCreditFirstCardPayAmount() {
        return creditFirstCardPayAmount;
    }

    public void setCreditFirstCardPayAmount(String creditFirstCardPayAmount) {
        this.creditFirstCardPayAmount = creditFirstCardPayAmount;
    }

    public String getAvailableCredit() {
        return availableCredit;
    }

    public void setAvailableCredit(String availableCredit) {
        this.availableCredit = availableCredit;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getTotalRedEnvelopeAmount() {
        return totalRedEnvelopeAmount;
    }

    public void setTotalRedEnvelopeAmount(String totalRedEnvelopeAmount) {
        this.totalRedEnvelopeAmount = totalRedEnvelopeAmount;
    }

    public String getMixPayAmount() {
        return mixPayAmount;
    }

    public void setMixPayAmount(String mixPayAmount) {
        this.mixPayAmount = mixPayAmount;
    }

    public String getWholeSaleUserDiscountAmount() {
        return wholeSaleUserDiscountAmount;
    }

    public void setWholeSaleUserDiscountAmount(String wholeSaleUserDiscountAmount) {
        this.wholeSaleUserDiscountAmount = wholeSaleUserDiscountAmount;
    }

    public String getNormalSaleUserDiscountAmount() {
        return normalSaleUserDiscountAmount;
    }

    public void setNormalSaleUserDiscountAmount(String normalSaleUserDiscountAmount) {
        this.normalSaleUserDiscountAmount = normalSaleUserDiscountAmount;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getRedEnvelopeAmount() {
        return redEnvelopeAmount;
    }

    public void setRedEnvelopeAmount(String redEnvelopeAmount) {
        this.redEnvelopeAmount = redEnvelopeAmount;
    }

    public String getNormalSalesUserDiscount() {
        return normalSalesUserDiscount;
    }

    public void setNormalSalesUserDiscount(String normalSalesUserDiscount) {
        this.normalSalesUserDiscount = normalSalesUserDiscount;
    }

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

    public String getChannelFeeAmount() {
        return channelFeeAmount;
    }

    public void setChannelFeeAmount(String channelFeeAmount) {
        this.channelFeeAmount = channelFeeAmount;
    }

    public String getTransChannelFeeRate() {
        return transChannelFeeRate;
    }

    public void setTransChannelFeeRate(String transChannelFeeRate) {
        this.transChannelFeeRate = transChannelFeeRate;
    }

    public String getNormalSaleAmount() {
        return normalSaleAmount;
    }

    public void setNormalSaleAmount(String normalSaleAmount) {
        this.normalSaleAmount = normalSaleAmount;
    }

    public String getWholeSalesUserDiscount() {
        return wholeSalesUserDiscount;
    }

    public void setWholeSalesUserDiscount(String wholeSalesUserDiscount) {
        this.wholeSalesUserDiscount = wholeSalesUserDiscount;
    }

    public String getRepeatSaleState() {
        return repeatSaleState;
    }

    public void setRepeatSaleState(String repeatSaleState) {
        this.repeatSaleState = repeatSaleState;
    }

    public String getWholeSalesAmount() {
        return wholeSalesAmount;
    }

    public void setWholeSalesAmount(String wholeSalesAmount) {
        this.wholeSalesAmount = wholeSalesAmount;
    }

    public List<PreviewRepayPlanListBean> getPreviewRepayPlanList() {
        return previewRepayPlanList;
    }

    public void setPreviewRepayPlanList(List<PreviewRepayPlanListBean> previewRepayPlanList) {
        this.previewRepayPlanList = previewRepayPlanList;
    }

    public static class DonationData implements Serializable{
        private String id;
        private String instituteName;
        private String donationAmount;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInstituteName() {
            return instituteName;
        }

        public void setInstituteName(String instituteName) {
            this.instituteName = instituteName;
        }

        public String getDonationAmount() {
            return donationAmount;
        }

        public void setDonationAmount(String donationAmount) {
            this.donationAmount = donationAmount;
        }
    }

    public static class TipListBean implements Serializable{
        /**
         * shouldPayAmount : 17.08
         * expectRepayTime : 1624442510233
         * periodSort : 1
         */

        private String amount;
        private String ratio;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }
    }

    public static class PreviewRepayPlanListBean implements Serializable{
        /**
         * shouldPayAmount : 17.08
         * expectRepayTime : 1624442510233
         * periodSort : 1
         */

        private String shouldPayAmount;
        private String expectRepayTime;
        private String periodSort;

        public String getShouldPayAmount() {
            return shouldPayAmount;
        }

        public void setShouldPayAmount(String shouldPayAmount) {
            this.shouldPayAmount = shouldPayAmount;
        }

        public String getExpectRepayTime() {
            return expectRepayTime;
        }

        public void setExpectRepayTime(String expectRepayTime) {
            this.expectRepayTime = expectRepayTime;
        }

        public String getPeriodSort() {
            return periodSort;
        }

        public void setPeriodSort(String periodSort) {
            this.periodSort = periodSort;
        }
    }
}
