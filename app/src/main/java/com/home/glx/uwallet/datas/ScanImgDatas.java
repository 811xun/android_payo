package com.home.glx.uwallet.datas;

import java.util.List;

public class ScanImgDatas {


    /**
     * wholeSaleUserDiscount :
     * code :
     * routeDTOS : [{"ip":"117.50.104.130","rateType":2,"createdDate":1582013077031,"gatewayType":2,"createdBy":377350522369396736,"merchantId":389592706036879360,"rate":0.03,"enName":"Alipay","modifiedDate":1582013077031,"name":"支付宝","modifiedBy":377350522369396736,"id":389656993828786177,"status":1},{"ip":"117.50.104.130","rateType":1,"createdDate":1582013077031,"gatewayType":3,"createdBy":377350522369396736,"merchantId":389592706036879360,"rate":0.08,"enName":"Wechat","modifiedDate":1582013077031,"name":"微信","modifiedBy":377350522369396736,"id":389656993828786178,"status":1}]
     * extraDiscount : 0.0
     * userName :
     * userId : 422989436555972608
     * merchantName :
     * rateType :
     * baseRate : 0.01
     * balance : 990.0
     * merchantId : 422989436887322624
     * rate :
     * wholeSaleBalance : 0.0
     * userType : 20
     * state :
     * installmentState : 1
     * correlationTime :
     * marketingDiscount : 0.0
     * merchantNo :
     */

    private double wholeSaleUserDiscount;
    private String code;
    private double extraDiscount;
    private String userName;
    private String userId;
    private String merchantName;
    private String rateType;
    private double baseRate;
    private double balance;
    private long merchantId;
    private String rate;
    private double wholeSaleBalance;
    private int userType;
    private String state;
    private int installmentState;
    private String correlationTime;
    private double marketingDiscount;
    private String merchantNo;
    private List<RouteDTOSBean> routeDTOS;

    public double getWholeSaleUserDiscount() {
        return wholeSaleUserDiscount;
    }

    public void setWholeSaleUserDiscount(double wholeSaleUserDiscount) {
        this.wholeSaleUserDiscount = wholeSaleUserDiscount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getExtraDiscount() {
        return extraDiscount;
    }

    public void setExtraDiscount(double extraDiscount) {
        this.extraDiscount = extraDiscount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public double getWholeSaleBalance() {
        return wholeSaleBalance;
    }

    public void setWholeSaleBalance(double wholeSaleBalance) {
        this.wholeSaleBalance = wholeSaleBalance;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getInstallmentState() {
        return installmentState;
    }

    public void setInstallmentState(int installmentState) {
        this.installmentState = installmentState;
    }

    public String getCorrelationTime() {
        return correlationTime;
    }

    public void setCorrelationTime(String correlationTime) {
        this.correlationTime = correlationTime;
    }

    public double getMarketingDiscount() {
        return marketingDiscount;
    }

    public void setMarketingDiscount(double marketingDiscount) {
        this.marketingDiscount = marketingDiscount;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public List<RouteDTOSBean> getRouteDTOS() {
        return routeDTOS;
    }

    public void setRouteDTOS(List<RouteDTOSBean> routeDTOS) {
        this.routeDTOS = routeDTOS;
    }

    public static class RouteDTOSBean {
        /**
         * ip : 117.50.104.130
         * rateType : 2
         * createdDate : 1582013077031
         * gatewayType : 2
         * createdBy : 377350522369396736
         * merchantId : 389592706036879360
         * rate : 0.03
         * enName : Alipay
         * modifiedDate : 1582013077031
         * name : 支付宝
         * modifiedBy : 377350522369396736
         * id : 389656993828786177
         * status : 1
         */

        private String ip;
        private int rateType;
        private long createdDate;
        private int gatewayType;
        private long createdBy;
        private long merchantId;
        private double rate;
        private String enName;
        private long modifiedDate;
        private String name;
        private long modifiedBy;
        private long id;
        private int status;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getRateType() {
            return rateType;
        }

        public void setRateType(int rateType) {
            this.rateType = rateType;
        }

        public long getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(long createdDate) {
            this.createdDate = createdDate;
        }

        public int getGatewayType() {
            return gatewayType;
        }

        public void setGatewayType(int gatewayType) {
            this.gatewayType = gatewayType;
        }

        public long getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(long createdBy) {
            this.createdBy = createdBy;
        }

        public long getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(long merchantId) {
            this.merchantId = merchantId;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public String getEnName() {
            return enName;
        }

        public void setEnName(String enName) {
            this.enName = enName;
        }

        public long getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(long modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(long modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
