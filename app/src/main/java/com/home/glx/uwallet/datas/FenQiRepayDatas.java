package com.home.glx.uwallet.datas;

import java.util.List;

public class FenQiRepayDatas {


    /**
     * accountId : 374433866596495360
     * memberState : 1
     * availableCredit : 510
     * bankcardNumber : 1234********4365
     * rate : 0.08
     * id : 374363212320878592
     * state : 1
     * userId : 374363212320878592
     * memberId : 369706125389381632
     * products : [{"StringerestRate":0.0309,"gracePeriodDays":3,"penaltyUpperLimit":0,"repaymentWay":1,"ip":"0:0:0:0:0:0:0:1","StringerestPenaltyRate":0,"overdueFine":15,"borrowUnit":1,"gracePeriodRate":0.03,"productPeriod":7,"StringerestPenaltyType":1,"repaymentSort":1,"createdDate":1577071032255,"periodQuantity":3,"createdBy":1,"modifiedDate":1578129179884,"modifiedBy":1,"id":368928555681103872,"serviceRate":0.02,"state":1,"status":1},{"StringerestRate":0.03,"gracePeriodDays":3,"repaymentWay":1,"ip":"0:0:0:0:0:0:0:1","StringerestPenaltyRate":0,"overdueFine":100,"borrowUnit":1,"gracePeriodRate":0,"productPeriod":7,"StringerestPenaltyType":1,"repaymentSort":1,"createdDate":1578130337283,"periodQuantity":6,"createdBy":1,"modifiedDate":1578130337283,"modifiedBy":1,"id":373371602972098560,"serviceRate":0.03,"state":1,"status":1},{"StringerestRate":0.01,"gracePeriodDays":1,"repaymentWay":1,"ip":"192.168.2.78","StringerestPenaltyRate":0,"overdueFine":1,"borrowUnit":1,"gracePeriodRate":0.01,"productPeriod":1,"StringerestPenaltyType":1,"repaymentSort":1,"createdDate":1578297247773,"periodQuantity":9,"createdBy":368914927875563520,"modifiedDate":1578297247773,"modifiedBy":368914927875563520,"id":374071676311973888,"serviceRate":0.01,"state":1,"status":1}]
     */

    private String accountId;
    private String bankcardBank;
    private String memberState;
    private String availableCredit;
    private String temporaryQuota;
    private String bankcardNumber;
    private float rate;
    private String id;
    private String state;
    private String userId;
    private String memberId;
    private List<ProductsBean> products;


    public String getTemporaryQuota() {
        return temporaryQuota;
    }

    public void setTemporaryQuota(String temporaryQuota) {
        this.temporaryQuota = temporaryQuota;
    }

    public String getBankcardBank() {
        return bankcardBank;
    }

    public void setBankcardBank(String bankcardBank) {
        this.bankcardBank = bankcardBank;
    }

    public String getMemberState() {
        return memberState;
    }

    public void setMemberState(String memberState) {
        this.memberState = memberState;
    }

    public String getAvailableCredit() {
        return availableCredit;
    }

    public void setAvailableCredit(String availableCredit) {
        this.availableCredit = availableCredit;
    }

    public String getBankcardNumber() {
        return bankcardNumber;
    }

    public void setBankcardNumber(String bankcardNumber) {
        this.bankcardNumber = bankcardNumber;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public List<ProductsBean> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsBean> products) {
        this.products = products;
    }

    public static class ProductsBean {
        /**
         * StringerestRate : 0.0309
         * gracePeriodDays : 3
         * penaltyUpperLimit : 0
         * repaymentWay : 1
         * ip : 0:0:0:0:0:0:0:1
         * StringerestPenaltyRate : 0
         * overdueFine : 15
         * borrowUnit : 1
         * gracePeriodRate : 0.03
         * productPeriod : 7
         * StringerestPenaltyType : 1
         * repaymentSort : 1
         * createdDate : 1577071032255
         * periodQuantity : 3
         * createdBy : 1
         * modifiedDate : 1578129179884
         * modifiedBy : 1
         * id : 368928555681103872
         * serviceRate : 0.02
         * state : 1
         * status : 1
         */

        private String StringerestRate;
        private String gracePeriodDays;
        private String penaltyUpperLimit;
        private String repaymentWay;
        private String StringerestPenaltyRate;
        private String overdueFine;
        private String borrowUnit;
        private String gracePeriodRate;
        private String productPeriod;
        private String StringerestPenaltyType;
        private String repaymentSort;
        private String createdDate;
        private String periodQuantity;
        private String modifiedDate;
        private String modifiedBy;
        private String id;
        private String serviceRate;
        private String state;
        private String status;


        public String getGracePeriodDays() {
            return gracePeriodDays;
        }

        public void setGracePeriodDays(String gracePeriodDays) {
            this.gracePeriodDays = gracePeriodDays;
        }

        public String getPenaltyUpperLimit() {
            return penaltyUpperLimit;
        }

        public void setPenaltyUpperLimit(String penaltyUpperLimit) {
            this.penaltyUpperLimit = penaltyUpperLimit;
        }

        public String getRepaymentWay() {
            return repaymentWay;
        }

        public void setRepaymentWay(String repaymentWay) {
            this.repaymentWay = repaymentWay;
        }


        public String getStringerestPenaltyRate() {
            return StringerestPenaltyRate;
        }

        public void setStringerestPenaltyRate(String StringerestPenaltyRate) {
            this.StringerestPenaltyRate = StringerestPenaltyRate;
        }

        public String getOverdueFine() {
            return overdueFine;
        }

        public void setOverdueFine(String overdueFine) {
            this.overdueFine = overdueFine;
        }

        public String getBorrowUnit() {
            return borrowUnit;
        }

        public void setBorrowUnit(String borrowUnit) {
            this.borrowUnit = borrowUnit;
        }


        public String getProductPeriod() {
            return productPeriod;
        }

        public void setProductPeriod(String productPeriod) {
            this.productPeriod = productPeriod;
        }

        public String getStringerestPenaltyType() {
            return StringerestPenaltyType;
        }

        public void setStringerestPenaltyType(String StringerestPenaltyType) {
            this.StringerestPenaltyType = StringerestPenaltyType;
        }

        public String getRepaymentSort() {
            return repaymentSort;
        }

        public void setRepaymentSort(String repaymentSort) {
            this.repaymentSort = repaymentSort;
        }


        public String getPeriodQuantity() {
            return periodQuantity;
        }

        public void setPeriodQuantity(String periodQuantity) {
            this.periodQuantity = periodQuantity;
        }


        public String getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public String getStringerestRate() {
            return StringerestRate;
        }

        public void setStringerestRate(String stringerestRate) {
            StringerestRate = stringerestRate;
        }

        public String getGracePeriodRate() {
            return gracePeriodRate;
        }

        public void setGracePeriodRate(String gracePeriodRate) {
            this.gracePeriodRate = gracePeriodRate;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getServiceRate() {
            return serviceRate;
        }

        public void setServiceRate(String serviceRate) {
            this.serviceRate = serviceRate;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
