package com.home.glx.uwallet.datas;

import java.io.Serializable;
import java.util.List;

public class BorrowByPayDayV2Data implements Serializable {

    /**
     * repayIdList : [560665912279781377,560666445539397633,561774421834682369]
     * repayDayTotalAmount : 0
     * repayDayStr : Wed 23 Jun
     * overdueFeeIdList : []
     * borrowIdList : [560665912263004160,560666445522620416,561774421813710848]
     * list : [{"tradingName":"zhengticeshi","maxOverdueDays":6,"repayId":560665912279781377,"violateAmount":0,"repayAmount":0,"totalPrincipal":3.73,"repayState":0,"shouldAmount":0,"dueInDays":-6,"borrowState":20,"type":1,"categoriesStr":"Cafe","logoUrl":"merchantIntroduction/537522966059765760.jpg","principal":3.73,"haveOverDue":true,"repayTimeStr":1624377600000,"repayDayStr":"Wed 23 Jun","expectRepayTime":1624377600000,"merchantId":534892409735827456,"periodSort":2,"overdueDays":6,"borrowId":560665912263004160,"categories":2},{"tradingName":"zhengticeshi","maxOverdueDays":6,"repayId":560666445539397633,"violateAmount":0,"repayAmount":0,"totalPrincipal":3.73,"repayState":0,"shouldAmount":0,"dueInDays":-6,"borrowState":20,"type":1,"categoriesStr":"Cafe","logoUrl":"merchantIntroduction/537522966059765760.jpg","principal":3.73,"haveOverDue":true,"repayTimeStr":1624377600000,"repayDayStr":"Wed 23 Jun","expectRepayTime":1624377600000,"merchantId":534892409735827456,"periodSort":2,"overdueDays":6,"borrowId":560666445522620416,"categories":2},{"tradingName":"cake ","maxOverdueDays":6,"repayId":561774421834682369,"violateAmount":0,"repayAmount":0,"totalPrincipal":3.91,"repayState":0,"shouldAmount":0,"dueInDays":-6,"borrowState":20,"type":1,"categoriesStr":"Cafe","logoUrl":"paper/534507936909316096.jpeg","principal":3.91,"haveOverDue":true,"repayTimeStr":1624377600000,"repayDayStr":"Wed 23 Jun","expectRepayTime":1624377600000,"merchantId":534507564966825984,"periodSort":2,"overdueDays":6,"borrowId":561774421813710848,"categories":2}]
     * repayDay : 1624377600000
     */

    private List<String> repayIdList;
    private String repayDayTotalAmount;
    private String repayDayStr;
    private List<String> overdueFeeIdList;
    private List<String> borrowIdList;
    private List<ListBean> list;
    private String repayDay;

    public List<String> getRepayIdList() {
        return repayIdList;
    }

    public void setRepayIdList(List<String> repayIdList) {
        this.repayIdList = repayIdList;
    }

    public String getRepayDayTotalAmount() {
        return repayDayTotalAmount;
    }

    public void setRepayDayTotalAmount(String repayDayTotalAmount) {
        this.repayDayTotalAmount = repayDayTotalAmount;
    }

    public String getRepayDayStr() {
        return repayDayStr;
    }

    public void setRepayDayStr(String repayDayStr) {
        this.repayDayStr = repayDayStr;
    }

    public List<String> getOverdueFeeIdList() {
        return overdueFeeIdList;
    }

    public void setOverdueFeeIdList(List<String> overdueFeeIdList) {
        this.overdueFeeIdList = overdueFeeIdList;
    }

    public List<String> getBorrowIdList() {
        return borrowIdList;
    }

    public void setBorrowIdList(List<String> borrowIdList) {
        this.borrowIdList = borrowIdList;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public String getRepayDay() {
        return repayDay;
    }

    public void setRepayDay(String repayDay) {
        this.repayDay = repayDay;
    }

    public static class ListBean implements Serializable {
        /**
         * tradingName : zhengticeshi
         * maxOverdueDays : 6
         * repayId : 560665912279781377
         * violateAmount : 0
         * repayAmount : 0
         * totalPrincipal : 3.73
         * repayState : 0
         * shouldAmount : 0
         * dueInDays : -6
         * borrowState : 20
         * type : 1
         * categoriesStr : Cafe
         * logoUrl : merchantIntroduction/537522966059765760.jpg
         * principal : 3.73
         * haveOverDue : true
         * repayTimeStr : 1624377600000
         * repayDayStr : Wed 23 Jun
         * expectRepayTime : 1624377600000
         * merchantId : 534892409735827456
         * periodSort : 2
         * overdueDays : 6
         * borrowId : 560665912263004160
         * categories : 2
         */

        private String tradingName;
        private String maxOverdueDays;
        private String repayId;
        private String violateAmount;
        private String repayAmount;
        private String totalPrincipal;
        private String repayState;
        private String shouldAmount;
        private String dueInDays;
        private String borrowState;
        private String type;
        private String categoriesStr;
        private String logoUrl;
        private String principal;
        private String haveOverDue;
        private String repayTimeStr;
        private String repayDayStr;
        private String expectRepayTime;
        private String merchantId;
        private String periodSort;
        private String overdueDays;
        private String borrowId;
        private String categories;
        private String overdueFeeId;

        public String getTradingName() {
            return tradingName;
        }

        public void setTradingName(String tradingName) {
            this.tradingName = tradingName;
        }

        public String getMaxOverdueDays() {
            return maxOverdueDays;
        }

        public void setMaxOverdueDays(String maxOverdueDays) {
            this.maxOverdueDays = maxOverdueDays;
        }

        public String getRepayId() {
            return repayId;
        }

        public void setRepayId(String repayId) {
            this.repayId = repayId;
        }

        public String getViolateAmount() {
            return violateAmount;
        }

        public void setViolateAmount(String violateAmount) {
            this.violateAmount = violateAmount;
        }

        public String getRepayAmount() {
            return repayAmount;
        }

        public void setRepayAmount(String repayAmount) {
            this.repayAmount = repayAmount;
        }

        public String getTotalPrincipal() {
            return totalPrincipal;
        }

        public void setTotalPrincipal(String totalPrincipal) {
            this.totalPrincipal = totalPrincipal;
        }

        public String getRepayState() {
            return repayState;
        }

        public void setRepayState(String repayState) {
            this.repayState = repayState;
        }

        public String getShouldAmount() {
            return shouldAmount;
        }

        public void setShouldAmount(String shouldAmount) {
            this.shouldAmount = shouldAmount;
        }

        public String getDueInDays() {
            return dueInDays;
        }

        public void setDueInDays(String dueInDays) {
            this.dueInDays = dueInDays;
        }

        public String getBorrowState() {
            return borrowState;
        }

        public void setBorrowState(String borrowState) {
            this.borrowState = borrowState;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getPrincipal() {
            return principal;
        }

        public void setPrincipal(String principal) {
            this.principal = principal;
        }

        public String getHaveOverDue() {
            return haveOverDue;
        }

        public void setHaveOverDue(String haveOverDue) {
            this.haveOverDue = haveOverDue;
        }

        public String getRepayTimeStr() {
            return repayTimeStr;
        }

        public void setRepayTimeStr(String repayTimeStr) {
            this.repayTimeStr = repayTimeStr;
        }

        public String getRepayDayStr() {
            return repayDayStr;
        }

        public void setRepayDayStr(String repayDayStr) {
            this.repayDayStr = repayDayStr;
        }

        public String getExpectRepayTime() {
            return expectRepayTime;
        }

        public void setExpectRepayTime(String expectRepayTime) {
            this.expectRepayTime = expectRepayTime;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getPeriodSort() {
            return periodSort;
        }

        public void setPeriodSort(String periodSort) {
            this.periodSort = periodSort;
        }

        public String getOverdueDays() {
            return overdueDays;
        }

        public void setOverdueDays(String overdueDays) {
            this.overdueDays = overdueDays;
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

        public String getOverdueFeeId() {
            return overdueFeeId;
        }

        public void setOverdueFeeId(String overdueFeeId) {
            this.overdueFeeId = overdueFeeId;
        }
    }
}
