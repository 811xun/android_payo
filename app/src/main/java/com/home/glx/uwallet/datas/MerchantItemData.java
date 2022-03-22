package com.home.glx.uwallet.datas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MerchantItemData {

    /**
     * tradingName : eujgg3
     * isAvailable : 1
     * lng : 121.373155
     * userId : 470406657698648064
     * userDiscount : 0.03
     * tags :["Stir Fried Food","Pubs & Bars","Gold Coast"]
     * cardPayDiscount : 0.03
     * merchantId : 502272658144710656
     * haveWholeSell : 0
     * lat : 37.548856
     * installmentDiscount : 0.02
     * isFavorite : 1
     */

    private String tradingName;
    private String isAvailable;
    private String lng;
    private String userId;
    private String userDiscount;
    private String cardPayDiscount;
    private String merchantId;
    private String haveWholeSell;
    private String lat;
    private String installmentDiscount;
    private String isFavorite;
    private String logoUrl;
    private List<String> tags;
    private String distance;

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getTradingName() {
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserDiscount() {
        return userDiscount;
    }

    public void setUserDiscount(String userDiscount) {
        this.userDiscount = userDiscount;
    }

    public String getCardPayDiscount() {
        return cardPayDiscount;
    }

    public void setCardPayDiscount(String cardPayDiscount) {
        this.cardPayDiscount = cardPayDiscount;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getHaveWholeSell() {
        return haveWholeSell;
    }

    public void setHaveWholeSell(String haveWholeSell) {
        this.haveWholeSell = haveWholeSell;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getInstallmentDiscount() {
        return installmentDiscount;
    }

    public void setInstallmentDiscount(String installmentDiscount) {
        this.installmentDiscount = installmentDiscount;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }
}
