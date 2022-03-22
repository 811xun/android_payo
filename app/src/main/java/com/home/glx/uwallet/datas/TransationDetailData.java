package com.home.glx.uwallet.datas;

import com.home.glx.uwallet.tools.PublicTools;

import java.util.List;

public class TransationDetailData {

    /**
     * id : 481983481377411073
     * createdBy : 470406657698648064
     * createdDate : 10:37 30/10/2020
     * modifiedBy : 470406657698648064
     * modifiedDate : 10:37 30/10/2020
     * status : 1
     * ip : 112.238.25.231
     * gatewayId : null
     * payUserId : 470406657698648064
     * payUserType : 10
     * payAccountType : null
     * recUserId : 479491763625709568
     * recUserType : 20
     * recAccountType : null
     * merchantId : 479491777026510848
     * transAmount : 20
     * payAmount : 19.6
     * recAmount : 0
     * feeDirection : null
     * fee : 0
     * platformFee : 0
     * rate : 0
     * batchId : null
     * isNeedClear : 1
     * clearState : 1
     * clearTime : 10:37 30/10/2020
     * errorMessage : null
     * errorCode : null
     * transType : 22
     * remark : null
     * state : 1
     * transAmountTotal : null
     * recAmountTotal : null
     * count : null
     * corporateName : null
     * bsb : null
     * accountNo : null
     * bankName : null
     * phone : null
     * accessPartyOrderNo :
     * accessPartyNotifyUrl :
     * accessPartyServerFee : 0
     * accPltFeeClearState : 0
     * accPltFeeClearBatch : null
     * accessPartyDiscount : 0
     * creditOrderNo : 481983481742315520
     * orderSource : 0
     * baseDiscountAmount : 0
     * extraDiscountAmount : 0
     * markingDiscountAmount : 0
     * wholeSalesAmount : 20
     * normalSaleAmount : 0
     * redEnvelopeAmount : 0
     * clearAmount : 0
     * platformId : null
     * saleType : 1
     * transNo : WSINS481983481377411073
     * wholeSalesDiscount : null
     * cardId : null
     * transTime : null
     * logoUrl : null
     * tradingName : null
     * discountAmt : 0
     * repayList : [{"gracePeriodAmount":0,"truelyRepayTime":null,"truelyRepayAmount":0,"truelyOverdueDays":6,"shouldPayAmount":11,"expectRepayTime":"00:00:00 11/11/2020","periodSort":1,"modifiedBy":"0","id":"481983481759092736","state":2,"payPrincipal":10,"payInterestPenalty":0,"truelyOverdueAmount":null,"payInterest":0,"ip":"127.0.0.1","violateAmount":1,"repayCount":1,"billDate":1604592000000,"truelyInterestAmount":0,"truelyPayPrincipal":0,"truelyViolateAmount":0,"truelyPenaltyInterestAmount":0,"createdDate":"10:37:07 30/10/2020","createdBy":"0","truelyGracyPeriodAmountReal":0,"modifiedDate":"00:20:02 18/11/2020","borrowId":481983481742315520,"paidAmount":0,"truelyGracyPeriodDaysReal":1,"status":1},{"gracePeriodAmount":0,"truelyRepayTime":null,"truelyRepayAmount":0,"truelyOverdueDays":0,"shouldPayAmount":9.6,"expectRepayTime":"00:00:00 18/11/2020","periodSort":2,"modifiedBy":"0","id":"481983481759092737","state":2,"payPrincipal":9.6,"payInterestPenalty":0,"truelyOverdueAmount":null,"payInterest":0,"ip":"127.0.0.1","violateAmount":0,"repayCount":1,"billDate":1605196800000,"truelyInterestAmount":0,"truelyPayPrincipal":0,"truelyViolateAmount":0,"truelyPenaltyInterestAmount":0,"createdDate":"10:37:07 30/10/2020","createdBy":"0","truelyGracyPeriodAmountReal":0,"modifiedDate":"00:20:02 18/11/2020","borrowId":481983481742315520,"paidAmount":0,"truelyGracyPeriodDaysReal":1,"status":1}]
     * transStateStr : success
     * displayDate : 20-10 10:07
     */
    private String donationAmount;
    private String cardCcType;
    private String cardNo;
    private String failedMsg;
    private String discountRate;
    private String id;
    private String createdBy;
    private String createdDate;
    private String modifiedBy;
    private String modifiedDate;
    private int status;
    private String ip;
    private Object gatewayId;
    private String payUserId;
    private int payUserType;
    private Object payAccountType;
    private String recUserId;
    private int recUserType;
    private Object recAccountType;
    private long merchantId;
    private String transAmount;
    private String payAmount;
    private String recAmount;
    private String feeDirection;
    private String fee;
    private String platformFee;
    private String rate;
    private Object batchId;
    private int isNeedClear;
    private int clearState;
    private String clearTime;
    private Object errorMessage;
    private Object errorCode;
    private int transType;
    private Object remark;
    private int state;
    private String transAmountTotal;
    private String recAmountTotal;
    private String count;
    private String corporateName;
    private String bsb;
    private String accountNo;
    private String bankName;
    private String phone;
    private String accessPartyOrderNo;
    private String accessPartyNotifyUrl;
    private String accessPartyServerFee;
    private int accPltFeeClearState;
    private Object accPltFeeClearBatch;
    private String accessPartyDiscount;
    private String creditOrderNo;
    private String orderSource;
    private String baseDiscountAmount;
    private String extraDiscountAmount;
    private String markingDiscountAmount;
    private String wholeSalesAmount;
    private String normalSaleAmount;
    private String redEnvelopeAmount;
    private String clearAmount;
    private String platformId;
    private int saleType;
    private String transNo;
    private String wholeSalesDiscount;
    private String cardId;
    private String transTime;
    private String logoUrl;
    private String tradingName;
    private String discountAmt;
    private String transStateStr;
    private String displayDate;
    private String wholeSaleDiscountRate;
    private String normalSaleDiscountRate;
    private String normalSaleOffAmt;
    private String wholeSaleOffAmt;
    private List<RepayListBean> repayList;
    private String  tipAmount;

    public String getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(String tipAmount) {
        this.tipAmount = tipAmount;
    }

    public String getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(String donationAmount) {
        this.donationAmount = donationAmount;
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

    public String getNormalSaleOffAmt() {
        return normalSaleOffAmt;
    }

    public void setNormalSaleOffAmt(String normalSaleOffAmt) {
        this.normalSaleOffAmt = normalSaleOffAmt;
    }

    public String getWholeSaleOffAmt() {
        return wholeSaleOffAmt;
    }

    public void setWholeSaleOffAmt(String wholeSaleOffAmt) {
        this.wholeSaleOffAmt = wholeSaleOffAmt;
    }

    public String getWholeSaleDiscountRate() {
        return wholeSaleDiscountRate;
    }

    public void setWholeSaleDiscountRate(String wholeSaleDiscountRate) {
        this.wholeSaleDiscountRate = wholeSaleDiscountRate;
    }

    public String getNormalSaleDiscountRate() {
        return normalSaleDiscountRate;
    }

    public void setNormalSaleDiscountRate(String normalSaleDiscountRate) {
        this.normalSaleDiscountRate = normalSaleDiscountRate;
    }

    public String getFailedMsg() {
        return failedMsg;
    }

    public void setFailedMsg(String failedMsg) {
        this.failedMsg = failedMsg;
    }

    public String getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(String discountRate) {
        this.discountRate = discountRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Object getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(Object gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getPayUserId() {
        return payUserId;
    }

    public void setPayUserId(String payUserId) {
        this.payUserId = payUserId;
    }

    public int getPayUserType() {
        return payUserType;
    }

    public void setPayUserType(int payUserType) {
        this.payUserType = payUserType;
    }

    public Object getPayAccountType() {
        return payAccountType;
    }

    public void setPayAccountType(Object payAccountType) {
        this.payAccountType = payAccountType;
    }

    public String getRecUserId() {
        return recUserId;
    }

    public void setRecUserId(String recUserId) {
        this.recUserId = recUserId;
    }

    public int getRecUserType() {
        return recUserType;
    }

    public void setRecUserType(int recUserType) {
        this.recUserType = recUserType;
    }

    public Object getRecAccountType() {
        return recAccountType;
    }

    public void setRecAccountType(Object recAccountType) {
        this.recAccountType = recAccountType;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public String getPayAmount() {
        return PublicTools.twoend(payAmount);
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getRecAmount() {
        return recAmount;
    }

    public void setRecAmount(String recAmount) {
        this.recAmount = recAmount;
    }

    public String getFeeDirection() {
        return feeDirection;
    }

    public void setFeeDirection(String feeDirection) {
        this.feeDirection = feeDirection;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getPlatformFee() {
        return platformFee;
    }

    public void setPlatformFee(String platformFee) {
        this.platformFee = platformFee;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Object getBatchId() {
        return batchId;
    }

    public void setBatchId(Object batchId) {
        this.batchId = batchId;
    }

    public int getIsNeedClear() {
        return isNeedClear;
    }

    public void setIsNeedClear(int isNeedClear) {
        this.isNeedClear = isNeedClear;
    }

    public int getClearState() {
        return clearState;
    }

    public void setClearState(int clearState) {
        this.clearState = clearState;
    }

    public String getClearTime() {
        return clearTime;
    }

    public void setClearTime(String clearTime) {
        this.clearTime = clearTime;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

    public int getTransType() {
        return transType;
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTransAmountTotal() {
        return transAmountTotal;
    }

    public void setTransAmountTotal(String transAmountTotal) {
        this.transAmountTotal = transAmountTotal;
    }

    public String getRecAmountTotal() {
        return recAmountTotal;
    }

    public void setRecAmountTotal(String recAmountTotal) {
        this.recAmountTotal = recAmountTotal;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getBsb() {
        return bsb;
    }

    public void setBsb(String bsb) {
        this.bsb = bsb;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccessPartyOrderNo() {
        return accessPartyOrderNo;
    }

    public void setAccessPartyOrderNo(String accessPartyOrderNo) {
        this.accessPartyOrderNo = accessPartyOrderNo;
    }

    public String getAccessPartyNotifyUrl() {
        return accessPartyNotifyUrl;
    }

    public void setAccessPartyNotifyUrl(String accessPartyNotifyUrl) {
        this.accessPartyNotifyUrl = accessPartyNotifyUrl;
    }

    public String getAccessPartyServerFee() {
        return accessPartyServerFee;
    }

    public void setAccessPartyServerFee(String accessPartyServerFee) {
        this.accessPartyServerFee = accessPartyServerFee;
    }

    public int getAccPltFeeClearState() {
        return accPltFeeClearState;
    }

    public void setAccPltFeeClearState(int accPltFeeClearState) {
        this.accPltFeeClearState = accPltFeeClearState;
    }

    public Object getAccPltFeeClearBatch() {
        return accPltFeeClearBatch;
    }

    public void setAccPltFeeClearBatch(Object accPltFeeClearBatch) {
        this.accPltFeeClearBatch = accPltFeeClearBatch;
    }

    public String getAccessPartyDiscount() {
        return accessPartyDiscount;
    }

    public void setAccessPartyDiscount(String accessPartyDiscount) {
        this.accessPartyDiscount = accessPartyDiscount;
    }

    public String getCreditOrderNo() {
        return creditOrderNo;
    }

    public void setCreditOrderNo(String creditOrderNo) {
        this.creditOrderNo = creditOrderNo;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getBaseDiscountAmount() {
        return baseDiscountAmount;
    }

    public void setBaseDiscountAmount(String baseDiscountAmount) {
        this.baseDiscountAmount = baseDiscountAmount;
    }

    public String getExtraDiscountAmount() {
        return extraDiscountAmount;
    }

    public void setExtraDiscountAmount(String extraDiscountAmount) {
        this.extraDiscountAmount = extraDiscountAmount;
    }

    public String getMarkingDiscountAmount() {
        return markingDiscountAmount;
    }

    public void setMarkingDiscountAmount(String markingDiscountAmount) {
        this.markingDiscountAmount = markingDiscountAmount;
    }

    public String getWholeSalesAmount() {
        return wholeSalesAmount;
    }

    public void setWholeSalesAmount(String wholeSalesAmount) {
        this.wholeSalesAmount = wholeSalesAmount;
    }

    public String getNormalSaleAmount() {
        return normalSaleAmount;
    }

    public void setNormalSaleAmount(String normalSaleAmount) {
        this.normalSaleAmount = normalSaleAmount;
    }

    public String getRedEnvelopeAmount() {
        return redEnvelopeAmount;
    }

    public void setRedEnvelopeAmount(String redEnvelopeAmount) {
        this.redEnvelopeAmount = redEnvelopeAmount;
    }

    public String getClearAmount() {
        return clearAmount;
    }

    public void setClearAmount(String clearAmount) {
        this.clearAmount = clearAmount;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public int getSaleType() {
        return saleType;
    }

    public void setSaleType(int saleType) {
        this.saleType = saleType;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getWholeSalesDiscount() {
        return wholeSalesDiscount;
    }

    public void setWholeSalesDiscount(String wholeSalesDiscount) {
        this.wholeSalesDiscount = wholeSalesDiscount;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getTradingName() {
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public String getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(String discountAmt) {
        this.discountAmt = discountAmt;
    }

    public String getTransStateStr() {
        return transStateStr;
    }

    public void setTransStateStr(String transStateStr) {
        this.transStateStr = transStateStr;
    }

    public String getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    public List<RepayListBean> getRepayList() {
        return repayList;
    }

    public void setRepayList(List<RepayListBean> repayList) {
        this.repayList = repayList;
    }

    public static class RepayListBean {
        /**
         * gracePeriodAmount : 0
         * truelyRepayTime : null
         * truelyRepayAmount : 0
         * truelyOverdueDays : 6
         * shouldPayAmount : 11
         * expectRepayTime : 00:00:00 11/11/2020
         * periodSort : 1
         * modifiedBy : 0
         * id : 481983481759092736
         * state : 2
         * payPrincipal : 10
         * payInterestPenalty : 0
         * truelyOverdueAmount : null
         * payInterest : 0
         * ip : 127.0.0.1
         * violateAmount : 1
         * repayCount : 1
         * billDate : 1604592000000
         * truelyInterestAmount : 0
         * truelyPayPrincipal : 0
         * truelyViolateAmount : 0
         * truelyPenaltyInterestAmount : 0
         * createdDate : 10:37:07 30/10/2020
         * createdBy : 0
         * truelyGracyPeriodAmountReal : 0
         * modifiedDate : 00:20:02 18/11/2020
         * borrowId : 481983481742315520
         * paidAmount : 0
         * truelyGracyPeriodDaysReal : 1
         * status : 1
         */

        private String gracePeriodAmount;
        private Object truelyRepayTime;
        private String truelyRepayAmount;
        private String truelyOverdueDays;
        private String shouldPayAmount;
        private String expectRepayTime;
        private String periodSort;
        private String modifiedBy;
        private String id;
        private int state;
        private String payPrincipal;
        private String payInterestPenalty;
        private String truelyOverdueAmount;
        private String payInterest;
        private String ip;
        private String violateAmount;
        private String repayCount;
        private String billDate;
        private String truelyInterestAmount;
        private String truelyPayPrincipal;
        private String truelyViolateAmount;
        private String truelyPenaltyInterestAmount;
        private String createdDate;
        private String createdBy;
        private String truelyGracyPeriodAmountReal;
        private String modifiedDate;
        private long borrowId;
        private String paidAmount;
        private String truelyGracyPeriodDaysReal;
        private String status;

        public String getGracePeriodAmount() {
            return gracePeriodAmount;
        }

        public void setGracePeriodAmount(String gracePeriodAmount) {
            this.gracePeriodAmount = gracePeriodAmount;
        }

        public Object getTruelyRepayTime() {
            return truelyRepayTime;
        }

        public void setTruelyRepayTime(Object truelyRepayTime) {
            this.truelyRepayTime = truelyRepayTime;
        }

        public String getTruelyRepayAmount() {
            return truelyRepayAmount;
        }

        public void setTruelyRepayAmount(String truelyRepayAmount) {
            this.truelyRepayAmount = truelyRepayAmount;
        }

        public String getTruelyOverdueDays() {
            return truelyOverdueDays;
        }

        public void setTruelyOverdueDays(String truelyOverdueDays) {
            this.truelyOverdueDays = truelyOverdueDays;
        }

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

        public String getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getPayPrincipal() {
            return PublicTools.twoend(payPrincipal);
        }

        public void setPayPrincipal(String payPrincipal) {
            this.payPrincipal = payPrincipal;
        }

        public String getPayInterestPenalty() {
            return payInterestPenalty;
        }

        public void setPayInterestPenalty(String payInterestPenalty) {
            this.payInterestPenalty = payInterestPenalty;
        }

        public String getTruelyOverdueAmount() {
            return truelyOverdueAmount;
        }

        public void setTruelyOverdueAmount(String truelyOverdueAmount) {
            this.truelyOverdueAmount = truelyOverdueAmount;
        }

        public String getPayInterest() {
            return payInterest;
        }

        public void setPayInterest(String payInterest) {
            this.payInterest = payInterest;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getViolateAmount() {
            return PublicTools.twoend(violateAmount);
        }

        public void setViolateAmount(String violateAmount) {
            this.violateAmount = violateAmount;
        }

        public String getRepayCount() {
            return repayCount;
        }

        public void setRepayCount(String repayCount) {
            this.repayCount = repayCount;
        }

        public String getBillDate() {
            return billDate;
        }

        public void setBillDate(String billDate) {
            this.billDate = billDate;
        }

        public String getTruelyInterestAmount() {
            return truelyInterestAmount;
        }

        public void setTruelyInterestAmount(String truelyInterestAmount) {
            this.truelyInterestAmount = truelyInterestAmount;
        }

        public String getTruelyPayPrincipal() {
            return truelyPayPrincipal;
        }

        public void setTruelyPayPrincipal(String truelyPayPrincipal) {
            this.truelyPayPrincipal = truelyPayPrincipal;
        }

        public String getTruelyViolateAmount() {
            return truelyViolateAmount;
        }

        public void setTruelyViolateAmount(String truelyViolateAmount) {
            this.truelyViolateAmount = truelyViolateAmount;
        }

        public String getTruelyPenaltyInterestAmount() {
            return truelyPenaltyInterestAmount;
        }

        public void setTruelyPenaltyInterestAmount(String truelyPenaltyInterestAmount) {
            this.truelyPenaltyInterestAmount = truelyPenaltyInterestAmount;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getTruelyGracyPeriodAmountReal() {
            return truelyGracyPeriodAmountReal;
        }

        public void setTruelyGracyPeriodAmountReal(String truelyGracyPeriodAmountReal) {
            this.truelyGracyPeriodAmountReal = truelyGracyPeriodAmountReal;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public long getBorrowId() {
            return borrowId;
        }

        public void setBorrowId(long borrowId) {
            this.borrowId = borrowId;
        }

        public String getPaidAmount() {
            return paidAmount;
        }

        public void setPaidAmount(String paidAmount) {
            this.paidAmount = paidAmount;
        }

        public String getTruelyGracyPeriodDaysReal() {
            return truelyGracyPeriodDaysReal;
        }

        public void setTruelyGracyPeriodDaysReal(String truelyGracyPeriodDaysReal) {
            this.truelyGracyPeriodDaysReal = truelyGracyPeriodDaysReal;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
