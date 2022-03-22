package com.home.glx.uwallet.datas;

import java.util.List;

public class BorrowByPaydayData {


    /**
     * haveOverDue : false
     * repayDayStr : 12-05-2021
     * repayAmount : 33.54
     * borrowIdList : [542877186820624384,542907028781879296,542971215574945792,543278848303386624,543314760810844160]
     * repayDay : 1620748800000
     */

    private String haveOverDue;
    private String repayDayStr;
    private String repayAmount;
    private List<String> borrowIdList;
    private String repayDay;
    private String dueInDays;
    private String maxOverdueDays;
    private String totalPrincipal;

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

    public String getDueInDays() {
        return dueInDays;
    }

    public void setDueInDays(String dueInDays) {
        this.dueInDays = dueInDays;
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

    public String getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
    }

    public List<String> getBorrowIdList() {
        return borrowIdList;
    }

    public void setBorrowIdList(List<String> borrowIdList) {
        this.borrowIdList = borrowIdList;
    }

    public String getRepayDay() {
        return repayDay;
    }

    public void setRepayDay(String repayDay) {
        this.repayDay = repayDay;
    }
}
