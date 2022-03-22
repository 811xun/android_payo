package com.home.glx.uwallet.datas;

import java.util.List;

public class BorrowByInstalmentData {

    /**
     * repayDayStr : Wed 16 Jun
     * borrowIdList : [560605903294517248,560665912263004160,560666445522620416,561774421813710848]
     * borrowDetail : [{"tradingName":"cake shop","maxOverdueDays":0,"repayId":560605903315488768,"repayAmount":4.95,"totalPrincipal":4.95,"dueInDays":7,"categoriesStr":"Cafe","logoUrl":"paper/534507936909316096.jpeg","haveOverDue":false,"repayDayStr":"Wed 16 Jun","merchantId":534507564966825984,"borrowId":560605903294517248,"categories":2},{"tradingName":"zhengticeshi","maxOverdueDays":0,"repayId":560665912279781376,"repayAmount":5,"totalPrincipal":5,"dueInDays":7,"categoriesStr":"Cafe","logoUrl":"merchantIntroduction/537522966059765760.jpg","haveOverDue":false,"repayDayStr":"Wed 16 Jun","merchantId":534892409735827456,"borrowId":560665912263004160,"categories":2},{"tradingName":"zhengticeshi","maxOverdueDays":0,"repayId":560666445539397632,"repayAmount":5,"totalPrincipal":5,"dueInDays":7,"categoriesStr":"Cafe","logoUrl":"merchantIntroduction/537522966059765760.jpg","haveOverDue":false,"repayDayStr":"Wed 16 Jun","merchantId":534892409735827456,"borrowId":560666445522620416,"categories":2},{"tradingName":"cake shop","maxOverdueDays":0,"repayId":561774421834682368,"repayAmount":5,"totalPrincipal":5,"dueInDays":7,"categoriesStr":"Cafe","logoUrl":"paper/534507936909316096.jpeg","haveOverDue":false,"repayDayStr":"Wed 16 Jun","merchantId":534507564966825984,"borrowId":561774421813710848,"categories":2}]
     * repayDay : 1623772800000
     */

    private String repayDayStr;
    private List<String> borrowIdList;
    private List<BorrowDetailBean> borrowDetail;
    private String repayDay;

    public String getRepayDayStr() {
        return repayDayStr;
    }

    public void setRepayDayStr(String repayDayStr) {
        this.repayDayStr = repayDayStr;
    }

    public List<String> getBorrowIdList() {
        return borrowIdList;
    }

    public void setBorrowIdList(List<String> borrowIdList) {
        this.borrowIdList = borrowIdList;
    }

    public List<BorrowDetailBean> getBorrowDetail() {
        return borrowDetail;
    }

    public void setBorrowDetail(List<BorrowDetailBean> borrowDetail) {
        this.borrowDetail = borrowDetail;
    }

    public String getRepayDay() {
        return repayDay;
    }

    public void setRepayDay(String repayDay) {
        this.repayDay = repayDay;
    }

    public static class BorrowDetailBean {
        /**
         * tradingName : cake shop
         * maxOverdueDays : 0
         * repayId : 560605903315488768
         * repayAmount : 4.95
         * totalPrincipal : 4.95
         * dueInDays : 7
         * categoriesStr : Cafe
         * logoUrl : paper/534507936909316096.jpeg
         * haveOverDue : false
         * repayDayStr : Wed 16 Jun
         * merchantId : 534507564966825984
         * borrowId : 560605903294517248
         * categories : 2
         */

        private String tradingName;
        private String maxOverdueDays;
        private String repayId;
        private String repayAmount;
        private String totalPrincipal;
        private String dueInDays;
        private String categoriesStr;
        private String logoUrl;
        private String haveOverDue;
        private String repayDayStr;
        private String merchantId;
        private String borrowId;
        private String categories;

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

        public String getDueInDays() {
            return dueInDays;
        }

        public void setDueInDays(String dueInDays) {
            this.dueInDays = dueInDays;
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

        public String getHaveOverDue() {
            return haveOverDue;
        }

        public void setHaveOverDue(String haveOverDue) {
            this.haveOverDue = haveOverDue;
        }

        public String getRepayDayStr() {
            return repayDayStr;
        }

        public void setRepayDayStr(String repayDayStr) {
            this.repayDayStr = repayDayStr;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
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
    }
}
