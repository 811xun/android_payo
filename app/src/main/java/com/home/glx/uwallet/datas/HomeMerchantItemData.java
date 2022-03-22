package com.home.glx.uwallet.datas;

import java.util.List;

public class HomeMerchantItemData {
    /**
     * practicalName : cake shop
     * logoUrl : paper/534507936909316096.jpeg
     * tags : ["Ice Cream","Korean","Spanish"]
     * collectionStatus : 0
     * distance : 8512.12
     * categoryName : Casual Dining
     */

    private String practicalName;
    private String logoUrl;
    private String logoBase64;
    private List<String> tags;
    private String collectionStatus;
    private String distance;
    private String categoryName;
    private String merchantId;
    private String userDiscount;

    public String getLogoBase64() {
        return logoBase64;
    }

    public void setLogoBase64(String logoBase64) {
        this.logoBase64 = logoBase64;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getUserDiscount() {
        return userDiscount;
    }

    public void setUserDiscount(String userDiscount) {
        this.userDiscount = userDiscount;
    }

    public String getPracticalName() {
        return practicalName;
    }

    public void setPracticalName(String practicalName) {
        this.practicalName = practicalName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCollectionStatus() {
        return collectionStatus;
    }

    public void setCollectionStatus(String collectionStatus) {
        this.collectionStatus = collectionStatus;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
