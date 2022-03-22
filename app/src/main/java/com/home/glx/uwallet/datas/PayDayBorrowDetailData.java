package com.home.glx.uwallet.datas;

import java.util.List;

public class PayDayBorrowDetailData {


    /**
     * tradingName : 测试shanghu
     * haveOverDue : false
     * toRepayDay : 21
     * repayAmount : 0.84
     * borrowId : 542877186820624384
     * borrowDateStr : 16-04-2021
     * repayList : [{"periodSort":1,"repayId":542877186833207296,"overdueDays":0,"repayState":0,"repayAmount":11,"displayColor":3},{"periodSort":2,"repayId":542877186833207297,"overdueDays":0,"repayState":0,"repayAmount":11,"displayColor":3},{"periodSort":3,"repayId":542877186833207298,"overdueDays":0,"repayState":0,"repayAmount":11,"displayColor":1}]
     */

    private String tradingName;
    private String haveOverDue;
    private String toRepayDay;
    private String repayAmount;
    private String borrowId;
    private String borrowDateStr;
    private List<RepayListBean> repayList;

    public String getTradingName() {
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public String getHaveOverDue() {
        return haveOverDue;
    }

    public void setHaveOverDue(String haveOverDue) {
        this.haveOverDue = haveOverDue;
    }

    public String getToRepayDay() {
        return toRepayDay;
    }

    public void setToRepayDay(String toRepayDay) {
        this.toRepayDay = toRepayDay;
    }

    public String getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getBorrowDateStr() {
        return borrowDateStr;
    }

    public void setBorrowDateStr(String borrowDateStr) {
        this.borrowDateStr = borrowDateStr;
    }

    public List<RepayListBean> getRepayList() {
        return repayList;
    }

    public void setRepayList(List<RepayListBean> repayList) {
        this.repayList = repayList;
    }

    public static class RepayListBean {
        /**
         * periodSort : 1
         * repayId : 542877186833207296
         * overdueDays : 0
         * repayState : 0
         * repayAmount : 11
         * displayColor : 3
         */

        private String periodSort;
        private String repayId;
        private String overdueDays;
        private String repayState;
        private String repayAmount;
        private String displayColor;

        public String getPeriodSort() {
            return periodSort;
        }

        public void setPeriodSort(String periodSort) {
            this.periodSort = periodSort;
        }

        public String getRepayId() {
            return repayId;
        }

        public void setRepayId(String repayId) {
            this.repayId = repayId;
        }

        public String getOverdueDays() {
            return overdueDays;
        }

        public void setOverdueDays(String overdueDays) {
            this.overdueDays = overdueDays;
        }

        public String getRepayState() {
            return repayState;
        }

        public void setRepayState(String repayState) {
            this.repayState = repayState;
        }

        public String getRepayAmount() {
            return repayAmount;
        }

        public void setRepayAmount(String repayAmount) {
            this.repayAmount = repayAmount;
        }

        public String getDisplayColor() {
            return displayColor;
        }

        public void setDisplayColor(String displayColor) {
            this.displayColor = displayColor;
        }
    }
}
