package com.home.glx.uwallet.datas;

public class BorrowByOrderData {

    /**
     * tradingName : cake shop
     * merchantId : 534507564966825984
     * repayAmount : 5.47
     * dueInDays : 1
     * borrowId : 537418305344589824
     * categories : 1
     * borrowState : 10
     * categoriesStr : Casual Dining
     * logoUrl : paper/534507936909316096.jpeg
     * repayDay : 21-04-2021
     */

    private String totalPrincipal;
    private String tradingName;
    private String merchantId;
    private String repayAmount;
    private String dueInDays;
    private String borrowId;
    private String categories;
    private String borrowState;
    private String categoriesStr;
    private String logoUrl;
    private String repayDay;
    private String remainingAmount;
    private String maxOverdueDays;

    public String getTotalPrincipal() {
        return totalPrincipal;
    }

    public void setTotalPrincipal(String totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    public String getMaxOverdueDays() {
        return maxOverdueDays;
    }

    public void setMaxOverdueDays(String maxOverdueDays) {
        this.maxOverdueDays = maxOverdueDays;
    }

    public String getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(String remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getTradingName() {
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getDueInDays() {
        return dueInDays;
    }

    public void setDueInDays(String dueInDays) {
        this.dueInDays = dueInDays;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getBorrowState() {
        return borrowState;
    }

    public void setBorrowState(String borrowState) {
        this.borrowState = borrowState;
    }

    public String getCategoriesStr() {
        return categoriesStr;
    }

    public void setCategoriesStr(String categoriesStr) {
        this.categoriesStr = categoriesStr;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getRepayDay() {
        return repayDay;
    }

    public void setRepayDay(String repayDay) {
        this.repayDay = repayDay;
    }
}
