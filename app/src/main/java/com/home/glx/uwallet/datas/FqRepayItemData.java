package com.home.glx.uwallet.datas;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.home.glx.uwallet.BR;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.list_adapter.BindingAdapterItem;
import com.home.glx.uwallet.tools.PublicTools;

/**
 * 分期付支付记录item
 */
public class FqRepayItemData extends BaseObservable implements BindingAdapterItem {


    /**
     * productInterestPenaltyWay :
     * productInterestPenaltyType : 1
     * code : INVT2020092400000025
     * settleStatus : 10
     * settleAmount : 2.0000
     * productRepaymentSort : 1
     * redEnvelopeAmount : 2.0000
     * merchantName : 新月测试二
     * settleDelay : 0
     * orderAmount : 2.0000
     * productGracePeriodDays : 1
     * productGracePeriodRate : 0.0001
     * periodQuantity : 0
     * merchantId : 461807535454375936
     * overdueDays : 0
     * productRepaymentWay : 1
     * modifiedBy : 468323594051211264
     * borrowCloseReasonId :
     * id : 469078216856522752
     * state : 30
     * productInterestRate :
     * productBorrowUnit : 2
     * productFixedRepaymentDate : 08:00:00 01-01-1970
     * productId : 397253654985920512
     * productOverdueFine : 1.0000
     * ip : 112.238.25.231
     * productPenaltyUpperLimit :
     * truelyInterestAmount :
     * userName : tdgb  yygh
     * userId : 468323594051211264
     * productServiceRate :
     * productPeriod : 1
     * borrowAmount : 0.0000
     * createdDate : 19:56:12 24-09-2020
     * createdBy : 468323594051211264
     * phone : 610426666006
     * productInterestPenaltyRate : 0.0000
     * modifiedDate : 19:56:12 24-09-2020
     * status : 1
     */

    private String productInterestPenaltyWay;
    private int productInterestPenaltyType;
    private String code;
    private int settleStatus;
    private String settleAmount;
    private int productRepaymentSort;
    private String redEnvelopeAmount;
    private String merchantName;
    private int settleDelay;
    private String orderAmount;
    private int productGracePeriodDays;
    private String productGracePeriodRate;
    private String periodQuantity;
    private long merchantId;
    private int overdueDays;
    private int productRepaymentWay;
    private long modifiedBy;
    private String borrowCloseReasonId;
    private long id;
    private int state;
    private String productInterestRate;
    private int productBorrowUnit;
    private String productFixedRepaymentDate;
    private long productId;
    private String productOverdueFine;
    private String ip;
    private String productPenaltyUpperLimit;
    private String truelyInterestAmount;
    private String userName;
    private long userId;
    private String productServiceRate;
    private int productPeriod;
    private String borrowAmount;
    private String createdDate;
    private long createdBy;
    private String phone;
    private String productInterestPenaltyRate;

    @Override
    public int getViewType() {
        return R.layout.item_fq_repay;
    }

    @Override
    public Object getViewItem() {
        return this;
    }

    public String getProductInterestPenaltyWay() {
        return productInterestPenaltyWay;
    }

    public void setProductInterestPenaltyWay(String productInterestPenaltyWay) {
        this.productInterestPenaltyWay = productInterestPenaltyWay;
    }

    public int getProductInterestPenaltyType() {
        return productInterestPenaltyType;
    }

    public void setProductInterestPenaltyType(int productInterestPenaltyType) {
        this.productInterestPenaltyType = productInterestPenaltyType;
    }

    @Bindable
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(int settleStatus) {
        this.settleStatus = settleStatus;
    }

    @Bindable
    public String getSettleAmount() {
        return settleAmount;
    }

    public void setSettleAmount(String settleAmount) {
        this.settleAmount = settleAmount;
    }

    public int getProductRepaymentSort() {
        return productRepaymentSort;
    }

    public void setProductRepaymentSort(int productRepaymentSort) {
        this.productRepaymentSort = productRepaymentSort;
    }

    public String getRedEnvelopeAmount() {
        return redEnvelopeAmount;
    }

    public void setRedEnvelopeAmount(String redEnvelopeAmount) {
        this.redEnvelopeAmount = redEnvelopeAmount;
    }

    @Bindable
    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public int getSettleDelay() {
        return settleDelay;
    }

    public void setSettleDelay(int settleDelay) {
        this.settleDelay = settleDelay;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getProductGracePeriodDays() {
        return productGracePeriodDays;
    }

    public void setProductGracePeriodDays(int productGracePeriodDays) {
        this.productGracePeriodDays = productGracePeriodDays;
    }

    public String getProductGracePeriodRate() {
        return productGracePeriodRate;
    }

    public void setProductGracePeriodRate(String productGracePeriodRate) {
        this.productGracePeriodRate = productGracePeriodRate;
    }

    @Bindable
    public String getPeriodQuantity() {
        return periodQuantity;
    }

    public void setPeriodQuantity(String periodQuantity) {
        this.periodQuantity = periodQuantity;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public int getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(int overdueDays) {
        this.overdueDays = overdueDays;
    }

    public int getProductRepaymentWay() {
        return productRepaymentWay;
    }

    public void setProductRepaymentWay(int productRepaymentWay) {
        this.productRepaymentWay = productRepaymentWay;
    }

    public long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getBorrowCloseReasonId() {
        return borrowCloseReasonId;
    }

    public void setBorrowCloseReasonId(String borrowCloseReasonId) {
        this.borrowCloseReasonId = borrowCloseReasonId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getProductInterestRate() {
        return productInterestRate;
    }

    public void setProductInterestRate(String productInterestRate) {
        this.productInterestRate = productInterestRate;
    }

    public int getProductBorrowUnit() {
        return productBorrowUnit;
    }

    public void setProductBorrowUnit(int productBorrowUnit) {
        this.productBorrowUnit = productBorrowUnit;
    }

    public String getProductFixedRepaymentDate() {
        return productFixedRepaymentDate;
    }

    public void setProductFixedRepaymentDate(String productFixedRepaymentDate) {
        this.productFixedRepaymentDate = productFixedRepaymentDate;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductOverdueFine() {
        return productOverdueFine;
    }

    public void setProductOverdueFine(String productOverdueFine) {
        this.productOverdueFine = productOverdueFine;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getProductPenaltyUpperLimit() {
        return productPenaltyUpperLimit;
    }

    public void setProductPenaltyUpperLimit(String productPenaltyUpperLimit) {
        this.productPenaltyUpperLimit = productPenaltyUpperLimit;
    }

    public String getTruelyInterestAmount() {
        return truelyInterestAmount;
    }

    public void setTruelyInterestAmount(String truelyInterestAmount) {
        this.truelyInterestAmount = truelyInterestAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getProductServiceRate() {
        return productServiceRate;
    }

    public void setProductServiceRate(String productServiceRate) {
        this.productServiceRate = productServiceRate;
    }

    public int getProductPeriod() {
        return productPeriod;
    }

    public void setProductPeriod(int productPeriod) {
        this.productPeriod = productPeriod;
    }

    @Bindable
    public String getBorrowAmount() {
//        if (borrowAmount.length() > 1) {
//            String start_t = borrowAmount.substring(0, 1);
//            if (start_t.equals("-") || start_t.equals("+")) {
//                return start_t + "$" + PublicTools.twoend(borrowAmount.substring(1));
//            }
//        }

        return "-$" + PublicTools.twoend(borrowAmount);
    }

    public void setBorrowAmount(String borrowAmount) {
        this.borrowAmount = borrowAmount;
    }

    @Bindable
    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProductInterestPenaltyRate() {
        return productInterestPenaltyRate;
    }

    public void setProductInterestPenaltyRate(String productInterestPenaltyRate) {
        this.productInterestPenaltyRate = productInterestPenaltyRate;
    }
}
