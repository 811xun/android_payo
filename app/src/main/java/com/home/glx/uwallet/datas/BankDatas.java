package com.home.glx.uwallet.datas;

import java.io.Serializable;

public class BankDatas implements Serializable {

    /**
     * lastName :
     * country :
     * customerCcExpyr :
     * accountName : q234ty
     * city :
     * unbindReason :
     * bankName : qwer56
     * type : 0
     * cardNo : 1256789
     * modifiedBy : 0
     * id : 372273629056126976
     * state :
     * email :
     * zip :
     * address2 :
     * customerCcType :
     * address1 :
     * ip : 127.0.0.1
     * subAccountType :
     * bsb : 1356
     * accountId : 372154536479084544
     * firstName :
     * createdDate : 01-01-2020 16:49:19
     * unbindTime :
     * createdBy : 0
     * phone :
     * customerCcCvc :
     * modifiedDate : 01-01-2020 16:49:19
     * middleName :
     * customerCcExpmo :
     * crdStrgToken :
     * status : 1
     */

    private String smallLogo;
    private String preset;
    private String order;
    private String typeStr;
    private String name;
    // 0:未验证,1:通过,2:验证失败
    private String verified;
    private boolean needInsert;
    private String lastName;
    private String country;
    private String customerCcExpyr;
    private String accountName;
    private String city;
    private String unbindReason;
    private String bankName;
    private int type;
    private String cardNo;
    private String modifiedBy;
    private String id;
    private String state;
    private String email;
    private String zip;
    private String address2;
    private String customerCcType;
    private String address1;
    private String subAccountType;
    private String bsb;
    private long accountId;
    private String firstName;
    private String createdDate;
    private String unbindTime;
    private String createdBy;
    private String phone;
    private String customerCcCvc;
    private String modifiedDate;
    private String middleName;
    private String customerCcExpmo;
    private String crdStrgToken;
    private int status;
    //账户类型  0：个人  1、理财
    private String accountTypee = "0";
    private String identityType = "";
    private boolean isChoice = false;
//    private int payState;
    private String stripeToken;

    public String getSmallLogo() {
        return smallLogo;
    }

    public void setSmallLogo(String smallLogo) {
        this.smallLogo = smallLogo;
    }

    public String getPreset() {
        return preset;
    }

    public void setPreset(String preset) {
        this.preset = preset;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public boolean isNeedInsert() {
        return needInsert;
    }

    public void setNeedInsert(boolean needInsert) {
        this.needInsert = needInsert;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCustomerCcExpyr() {
        return customerCcExpyr;
    }

    public void setCustomerCcExpyr(String customerCcExpyr) {
        this.customerCcExpyr = customerCcExpyr;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUnbindReason() {
        return unbindReason;
    }

    public void setUnbindReason(String unbindReason) {
        this.unbindReason = unbindReason;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCustomerCcType() {
        return customerCcType;
    }

    public void setCustomerCcType(String customerCcType) {
        this.customerCcType = customerCcType;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getSubAccountType() {
        return subAccountType;
    }

    public void setSubAccountType(String subAccountType) {
        this.subAccountType = subAccountType;
    }

    public String getBsb() {
        return bsb;
    }

    public void setBsb(String bsb) {
        this.bsb = bsb;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUnbindTime() {
        return unbindTime;
    }

    public void setUnbindTime(String unbindTime) {
        this.unbindTime = unbindTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCustomerCcCvc() {
        return customerCcCvc;
    }

    public void setCustomerCcCvc(String customerCcCvc) {
        this.customerCcCvc = customerCcCvc;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getCustomerCcExpmo() {
        return customerCcExpmo;
    }

    public void setCustomerCcExpmo(String customerCcExpmo) {
        this.customerCcExpmo = customerCcExpmo;
    }

    public String getCrdStrgToken() {
        return crdStrgToken;
    }

    public void setCrdStrgToken(String crdStrgToken) {
        this.crdStrgToken = crdStrgToken;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isChoice() {
        return isChoice;
    }

    public void setChoice(boolean choice) {
        isChoice = choice;
    }


    public String getAccountTypee() {
        return accountTypee;
    }

    public void setAccountTypee(String accountTypee) {
        this.accountTypee = accountTypee;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

//    public int getPayState() {
//        return payState;
//    }

//    public void setPayState(int payState) {
//        this.payState = payState;
//    }

    public String getStripeToken() {
        return stripeToken;
    }

    public void setStripeToken(String stripeToken) {
        this.stripeToken = stripeToken;
    }
}
