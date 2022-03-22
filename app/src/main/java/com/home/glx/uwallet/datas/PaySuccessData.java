package com.home.glx.uwallet.datas;

import java.io.Serializable;

public class PaySuccessData implements Serializable {
    /**
     * period : 3
     * cardPayRate : 1.10
     * creditNeedCardPayNoFeeAmount : 7.50
     * wholeSaleUserDiscountAmount : 0.00
     * normalSaleUserDiscountAmount : 0.00
     * transNo : BTPC568232608898011136
     * cardNo : **** **** **** 9999
     * normalSalesUserDiscount : 0.00
     * transAmount : 30
     * cardPayFee : 0.08
     * payAmount : 30.0000
     * creditNeedCardPayAmount : 7.58
     * cardCcType : 10
     * cardId : 559987277067669504
     * remainingCreditAmount : 22.5000
     * orderCreatedDate : 10:47 25-06-2021
     * wholeSalesUserDiscount : 2.00
     */
    private String id;
    private String payType;
    private String merchantName;
    private String creditNextRepayAmount;
    private String useRedEnvelopeAmount;
    private String period;
    private String cardPayRate;
    private String creditNeedCardPayNoFeeAmount;
    private String wholeSaleUserDiscountAmount;
    private String normalSaleUserDiscountAmount;
    private String transNo;
    private String cardNo;
    private String normalSalesUserDiscount;
    private String transAmount;
    private String cardPayFee;
    private String payAmount;
    private String creditNeedCardPayAmount;
    private String cardCcType;
    private String cardId;
    private String remainingCreditAmount;
    private String orderCreatedDate;
    private String wholeSalesUserDiscount;
    private String donationAmount;
    private String totalAmount;
    private String tipAmount;
    private String firstName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(String tipAmount) {
        this.tipAmount = tipAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(String donationAmount) {
        this.donationAmount = donationAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getCreditNextRepayAmount() {
        return creditNextRepayAmount;
    }

    public void setCreditNextRepayAmount(String creditNextRepayAmount) {
        this.creditNextRepayAmount = creditNextRepayAmount;
    }

    public String getUseRedEnvelopeAmount() {
        return useRedEnvelopeAmount;
    }

    public void setUseRedEnvelopeAmount(String useRedEnvelopeAmount) {
        this.useRedEnvelopeAmount = useRedEnvelopeAmount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getCardPayRate() {
        return cardPayRate;
    }

    public void setCardPayRate(String cardPayRate) {
        this.cardPayRate = cardPayRate;
    }

    public String getCreditNeedCardPayNoFeeAmount() {
        return creditNeedCardPayNoFeeAmount;
    }

    public void setCreditNeedCardPayNoFeeAmount(String creditNeedCardPayNoFeeAmount) {
        this.creditNeedCardPayNoFeeAmount = creditNeedCardPayNoFeeAmount;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
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

    public String getCardPayFee() {
        return cardPayFee;
    }

    public void setCardPayFee(String cardPayFee) {
        this.cardPayFee = cardPayFee;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getCreditNeedCardPayAmount() {
        return creditNeedCardPayAmount;
    }

    public void setCreditNeedCardPayAmount(String creditNeedCardPayAmount) {
        this.creditNeedCardPayAmount = creditNeedCardPayAmount;
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

    public String getRemainingCreditAmount() {
        return remainingCreditAmount;
    }

    public void setRemainingCreditAmount(String remainingCreditAmount) {
        this.remainingCreditAmount = remainingCreditAmount;
    }

    public String getOrderCreatedDate() {
        return orderCreatedDate;
    }

    public void setOrderCreatedDate(String orderCreatedDate) {
        this.orderCreatedDate = orderCreatedDate;
    }

    public String getWholeSalesUserDiscount() {
        return wholeSalesUserDiscount;
    }

    public void setWholeSalesUserDiscount(String wholeSalesUserDiscount) {
        this.wholeSalesUserDiscount = wholeSalesUserDiscount;
    }
}
