package com.home.glx.uwallet.datas;

public class LoginDatas {


    /**
     * Authorization : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjdXJyZW50IjoxNTgyODc1NTg3NzYyLCJsb2dpbk5hbWUiOiI4NjE4ODY1NTM4OTQzIiwiaWQiOjM5MzI3NDQ2MDk1ODQ0NTU2OCwiYXBwVG9rZW5LZXkiOiI4NjE4ODY1NTM4OTQzXzEwIn0.q91Rl4kfabhdLVjEWrwqeXNuUFTG0sn2hMxZK_W9_IY
     * userInfo : {"id":"393274460958445568","createdBy":"0","createdDate":"15:39:08 28-02-2020 ","modifiedBy":"0","modifiedDate":"15:39:08 28-02-2020 ","status":1,"ip":"60.212.5.255","uuid":null,"userType":10,"password":"46f94c8de14fb36680850768ff1b7f2a","pinNumber":null,"payPassword":null,"paymentState":0,"installmentState":0,"investState":0,"phone":"8618865538943","email":null,"merchantId":null,"role":null,"code":null,"securityCode":null,"oldPassword":null,"newPassword":null,"confirmPassword":null,"fullName":null,"lat":null,"lng":null,"userState":null,"userCity":null}
     * firstName :
     * lastName :
     * isAvailable :
     * merchantId :
     * middleName :
     * merchantState :
     */

    private String Authorization;
    private UserInfoBean userInfo;
    private String firstName;
    private String lastName;
    private String isAvailable;
    private String merchantId;
    private String middleName;
    private String merchantState;

    public String getAuthorization() {
        return Authorization;
    }

    public void setAuthorization(String Authorization) {
        this.Authorization = Authorization;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMerchantState() {
        return merchantState;
    }

    public void setMerchantState(String merchantState) {
        this.merchantState = merchantState;
    }

    public static class UserInfoBean {
        /**
         * id : 393274460958445568
         * createdBy : 0
         * createdDate : 15:39:08 28-02-2020
         * modifiedBy : 0
         * modifiedDate : 15:39:08 28-02-2020
         * status : 1
         * ip : 60.212.5.255
         * uuid : null
         * userType : 10
         * password : 46f94c8de14fb36680850768ff1b7f2a
         * pinNumber : null
         * payPassword : null
         * paymentState : 0
         * installmentState : 0
         * investState : 0
         * phone : 8618865538943
         * email : null
         * merchantId : null
         * role : null
         * code : null
         * securityCode : null
         * oldPassword : null
         * newPassword : null
         * confirmPassword : null
         * fullName : null
         * lat : null
         * lng : null
         * userState : null
         * userCity : null
         */

        private String id;
        private String modifiedBy;
        private String modifiedDate;
        private String uuid;
        private String userType;
        private String password;
        private String pinNumber;
        private String payPassword;
        private String paymentState;
        private String installmentState;
        private String investState;
        private String phone;
        private String email;
        private String merchantId;
        private String role;
        private String code;
        private String securityCode;
        private String oldPassword;
        private String newPassword;
        private String confirmPassword;
        private String fullName;
        private String lat;
        private String lng;
        private String userState;
        private String userCity;
        private String registerFrom;//注册来源  0：app;1：h5

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getPaymentState() {
            return paymentState;
        }

        public void setPaymentState(String paymentState) {
            this.paymentState = paymentState;
        }

        public String getInstallmentState() {
            return installmentState;
        }

        public void setInstallmentState(String installmentState) {
            this.installmentState = installmentState;
        }

        public String getInvestState() {
            return investState;
        }

        public void setInvestState(String investState) {
            this.investState = investState;
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

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPinNumber() {
            return pinNumber;
        }

        public void setPinNumber(String pinNumber) {
            this.pinNumber = pinNumber;
        }

        public String getPayPassword() {
            return payPassword;
        }

        public void setPayPassword(String payPassword) {
            this.payPassword = payPassword;
        }


        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getSecurityCode() {
            return securityCode;
        }

        public void setSecurityCode(String securityCode) {
            this.securityCode = securityCode;
        }

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getUserState() {
            return userState;
        }

        public void setUserState(String userState) {
            this.userState = userState;
        }

        public String getUserCity() {
            return userCity;
        }

        public void setUserCity(String userCity) {
            this.userCity = userCity;
        }

        public String getRegisterFrom() {
            return registerFrom;
        }

        public void setRegisterFrom(String registerFrom) {
            this.registerFrom = registerFrom;
        }
    }
}
