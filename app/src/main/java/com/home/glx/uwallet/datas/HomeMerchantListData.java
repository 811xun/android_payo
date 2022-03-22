package com.home.glx.uwallet.datas;

import java.util.List;

public class HomeMerchantListData {

    /**
     * category2 : {"data":{"displayOrder":2,"categoryName":"Cafe","description":"bbbb","merchants":[{"practicalName":"cake shop","logoUrl":"paper/534507936909316096.jpeg","tags":["Ice Cream","Korean","Spanish"],"collectionStatus":0,"distance":8512.12,"categoryName":"Casual Dining"},{"practicalName":"zhengticeshi","logoUrl":"merchantIntroduction/537522966059765760.jpg","tags":["Cocktail","Stir Fried Food","Ice Cream"],"collectionStatus":0,"distance":8521,"categoryName":"Casual Dining"}]},"updateStatus":true,"category2Timestamp":"1618364415365"}
     * exclusive : {"list":[{"displayOrder":1,"title":"市场营销1","subTitle":"1","imageUrl":"appBanner/400513936671723520.png","limitTimes":33,"redirectType":3,"redirectH5LinkAddress":"","redirectAppLinkType":0,"redirectCustomizedTitle":"","redirectCustomizedImageUrl":"","turnOffEffectStatus":0,"turnOffTextDisplay":"","turnOffRedirectType":0,"turnOffRedirectH5Link":"","turnOffRedirectAppLinkType":0},{"displayOrder":2,"title":"市场推广2","subTitle":"222","imageUrl":"appBanner/400513936671723520.png","limitTimes":4,"redirectType":1,"redirectH5LinkAddress":"www.baidu.com","redirectAppLinkType":0,"redirectCustomizedTitle":"","redirectCustomizedImageUrl":"","turnOffEffectStatus":0,"turnOffTextDisplay":"","turnOffRedirectType":0,"turnOffRedirectH5Link":"","turnOffRedirectAppLinkType":0},{"displayOrder":3,"title":"市场推广3","subTitle":"333","imageUrl":"appBanner/400513936671723520.png","limitTimes":7,"redirectType":2,"redirectH5LinkAddress":"","redirectAppLinkType":2,"redirectCustomizedTitle":"","redirectCustomizedImageUrl":"","turnOffEffectStatus":0,"turnOffTextDisplay":"","turnOffRedirectType":0,"turnOffRedirectH5Link":"","turnOffRedirectAppLinkType":0},{"displayOrder":4,"title":"市场推广4","subTitle":"4444","imageUrl":"appBanner/400513936671723520.png","limitTimes":8,"redirectType":4,"redirectH5LinkAddress":"","redirectAppLinkType":0,"redirectCustomizedTitle":"abc","redirectCustomizedImageUrl":"appBanner/400513936671723520.png","redirectCustomizedContent":"asdhdshhdsa","turnOffEffectStatus":0,"turnOffTextDisplay":"","turnOffRedirectType":0,"turnOffRedirectH5Link":"","turnOffRedirectAppLinkType":0}],"updateStatus":true,"exclusiveTimestamp":"1618364412204"}
     * category3 : {"data":{"displayOrder":3,"categoryName":"Bar","description":"cccccc","merchants":[{"practicalName":"cake shop","logoUrl":"paper/534507936909316096.jpeg","tags":["Ice Cream","Korean","Spanish"],"collectionStatus":0,"distance":8512.12,"categoryName":"Casual Dining"},{"practicalName":"zhengticeshi","logoUrl":"merchantIntroduction/537522966059765760.jpg","tags":["Cocktail","Stir Fried Food","Ice Cream"],"collectionStatus":0,"distance":8521,"categoryName":"Casual Dining"}]},"updateStatus":true,"category3Timestamp":"1618364416575"}
     * category4 : {"data":{"displayOrder":4,"categoryName":"Asian","description":"ddddd","merchants":[{"practicalName":"cake shop","logoUrl":"paper/534507936909316096.jpeg","tags":["Ice Cream","Korean","Spanish"],"collectionStatus":0,"distance":8512.12,"categoryName":"Casual Dining"},{"practicalName":"zhengticeshi","logoUrl":"merchantIntroduction/537522966059765760.jpg","tags":["Cocktail","Stir Fried Food","Ice Cream"],"collectionStatus":0,"distance":8521,"categoryName":"Casual Dining"}]},"updateStatus":true,"category4Timestamp":"1618364417451"}
     * category5 : {"data":{"displayOrder":5,"categoryName":"Fast Food","description":"eeeeeee","merchants":[{"practicalName":"zhengticeshi","logoUrl":"merchantIntroduction/537522966059765760.jpg","tags":["Cocktail","Stir Fried Food","Ice Cream"],"collectionStatus":0,"distance":8521,"categoryName":"Casual Dining"},{"practicalName":"cake shop","logoUrl":"paper/534507936909316096.jpeg","tags":["Ice Cream","Korean","Spanish"],"collectionStatus":0,"distance":8512.12,"categoryName":"Casual Dining"}]},"updateStatus":true,"category5Timestamp":"1618364418791"}
     * category1 : {"data":{"displayOrder":1,"categoryName":"Casual Dining","description":"aaaaa","merchants":[{"practicalName":"cake shop","logoUrl":"paper/534507936909316096.jpeg","tags":["Ice Cream","Korean","Spanish"],"collectionStatus":0,"distance":8512.12,"categoryName":"Casual Dining"},{"practicalName":"zhengticeshi","logoUrl":"merchantIntroduction/537522966059765760.jpg","tags":["Cocktail","Stir Fried Food","Ice Cream"],"collectionStatus":0,"distance":8521,"categoryName":"Casual Dining"}]},"updateStatus":true,"category1Timestamp":"1618364412509"}
     */

    private Category2Bean category2;
    private ExclusiveBean exclusive;
    private Category3Bean category3;
    private Category4Bean category4;
    private Category5Bean category5;
    private Category1Bean category1;

    public Category2Bean getCategory2() {
        return category2;
    }

    public void setCategory2(Category2Bean category2) {
        this.category2 = category2;
    }

    public ExclusiveBean getExclusive() {
        return exclusive;
    }

    public void setExclusive(ExclusiveBean exclusive) {
        this.exclusive = exclusive;
    }

    public Category3Bean getCategory3() {
        return category3;
    }

    public void setCategory3(Category3Bean category3) {
        this.category3 = category3;
    }

    public Category4Bean getCategory4() {
        return category4;
    }

    public void setCategory4(Category4Bean category4) {
        this.category4 = category4;
    }

    public Category5Bean getCategory5() {
        return category5;
    }

    public void setCategory5(Category5Bean category5) {
        this.category5 = category5;
    }

    public Category1Bean getCategory1() {
        return category1;
    }

    public void setCategory1(Category1Bean category1) {
        this.category1 = category1;
    }

    public static class Category2Bean {
        /**
         * data : {"displayOrder":2,"categoryName":"Cafe","description":"bbbb","merchants":[{"practicalName":"cake shop","logoUrl":"paper/534507936909316096.jpeg","tags":["Ice Cream","Korean","Spanish"],"collectionStatus":0,"distance":8512.12,"categoryName":"Casual Dining"},{"practicalName":"zhengticeshi","logoUrl":"merchantIntroduction/537522966059765760.jpg","tags":["Cocktail","Stir Fried Food","Ice Cream"],"collectionStatus":0,"distance":8521,"categoryName":"Casual Dining"}]}
         * updateStatus : true
         * category2Timestamp : 1618364415365
         */

        private DataBean data;
        private String updateStatus;
        private String category2Timestamp;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getUpdateStatus() {
            return updateStatus;
        }

        public void setUpdateStatus(String updateStatus) {
            this.updateStatus = updateStatus;
        }

        public String getCategory2Timestamp() {
            return category2Timestamp;
        }

        public void setCategory2Timestamp(String category2Timestamp) {
            this.category2Timestamp = category2Timestamp;
        }

        public static class DataBean {
            /**
             * displayOrder : 2
             * categoryName : Cafe
             * description : bbbb
             * merchants : [{"practicalName":"cake shop","logoUrl":"paper/534507936909316096.jpeg","tags":["Ice Cream","Korean","Spanish"],"collectionStatus":0,"distance":8512.12,"categoryName":"Casual Dining"},{"practicalName":"zhengticeshi","logoUrl":"merchantIntroduction/537522966059765760.jpg","tags":["Cocktail","Stir Fried Food","Ice Cream"],"collectionStatus":0,"distance":8521,"categoryName":"Casual Dining"}]
             */

            private String displayOrder;
            private String categoryType;
            private String categoryName;
            private String description;
            private List<HomeMerchantItemData> merchants;

            public String getCategoryType() {
                return categoryType;
            }

            public void setCategoryType(String categoryType) {
                this.categoryType = categoryType;
            }

            public String getDisplayOrder() {
                return displayOrder;
            }

            public void setDisplayOrder(String displayOrder) {
                this.displayOrder = displayOrder;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public List<HomeMerchantItemData> getMerchants() {
                return merchants;
            }

            public void setMerchants(List<HomeMerchantItemData> merchants) {
                this.merchants = merchants;
            }
        }
    }

    public static class ExclusiveBean {
        /**
         * list : [{"displayOrder":1,"title":"市场营销1","subTitle":"1","imageUrl":"appBanner/400513936671723520.png","limitTimes":33,"redirectType":3,"redirectH5LinkAddress":"","redirectAppLinkType":0,"redirectCustomizedTitle":"","redirectCustomizedImageUrl":"","turnOffEffectStatus":0,"turnOffTextDisplay":"","turnOffRedirectType":0,"turnOffRedirectH5Link":"","turnOffRedirectAppLinkType":0},{"displayOrder":2,"title":"市场推广2","subTitle":"222","imageUrl":"appBanner/400513936671723520.png","limitTimes":4,"redirectType":1,"redirectH5LinkAddress":"www.baidu.com","redirectAppLinkType":0,"redirectCustomizedTitle":"","redirectCustomizedImageUrl":"","turnOffEffectStatus":0,"turnOffTextDisplay":"","turnOffRedirectType":0,"turnOffRedirectH5Link":"","turnOffRedirectAppLinkType":0},{"displayOrder":3,"title":"市场推广3","subTitle":"333","imageUrl":"appBanner/400513936671723520.png","limitTimes":7,"redirectType":2,"redirectH5LinkAddress":"","redirectAppLinkType":2,"redirectCustomizedTitle":"","redirectCustomizedImageUrl":"","turnOffEffectStatus":0,"turnOffTextDisplay":"","turnOffRedirectType":0,"turnOffRedirectH5Link":"","turnOffRedirectAppLinkType":0},{"displayOrder":4,"title":"市场推广4","subTitle":"4444","imageUrl":"appBanner/400513936671723520.png","limitTimes":8,"redirectType":4,"redirectH5LinkAddress":"","redirectAppLinkType":0,"redirectCustomizedTitle":"abc","redirectCustomizedImageUrl":"appBanner/400513936671723520.png","redirectCustomizedContent":"asdhdshhdsa","turnOffEffectStatus":0,"turnOffTextDisplay":"","turnOffRedirectType":0,"turnOffRedirectH5Link":"","turnOffRedirectAppLinkType":0}]
         * updateStatus : true
         * exclusiveTimestamp : 1618364412204
         */

        private List<ListBean> list;
        private String updateStatus;
        private String exclusiveTimestamp;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public String getUpdateStatus() {
            return updateStatus;
        }

        public void setUpdateStatus(String updateStatus) {
            this.updateStatus = updateStatus;
        }

        public String getExclusiveTimestamp() {
            return exclusiveTimestamp;
        }

        public void setExclusiveTimestamp(String exclusiveTimestamp) {
            this.exclusiveTimestamp = exclusiveTimestamp;
        }

        public static class ListBean {
            /**
             * displayOrder : 1
             * title : 市场营销1
             * subTitle : 1
             * imageUrl : appBanner/400513936671723520.png
             * limitTimes : 33
             * redirectType : 3
             * redirectH5LinkAddress :
             * redirectAppLinkType : 0
             * redirectCustomizedTitle :
             * redirectCustomizedImageUrl :
             * turnOffEffectStatus : 0
             * turnOffTextDisplay :
             * turnOffRedirectType : 0
             * turnOffRedirectH5Link :
             * turnOffRedirectAppLinkType : 0
             * redirectCustomizedContent : asdhdshhdsa
             */

            private String displayOrder;
            private String logoBase64;
            private String title;
            private String subTitle;
            private String imageUrl;
            private String limitTimes;
            private String redirectType;
            private String redirectH5LinkAddress;
            private String redirectAppLinkType;
            private String redirectCustomizedTitle;
            private String redirectCustomizedImageUrl;
            private String turnOffEffectStatus;
            private String turnOffTextDisplay;
            private String turnOffRedirectType;
            private String turnOffRedirectH5Link;
            private String turnOffRedirectAppLinkType;
            private String redirectCustomizedContent;
            private String redirectCustomizedDisplayType;

            public String getLogoBase64() {
                return logoBase64;
            }

            public void setLogoBase64(String logoBase64) {
                this.logoBase64 = logoBase64;
            }

            public String getRedirectCustomizedDisplayType() {
                return redirectCustomizedDisplayType;
            }

            public void setRedirectCustomizedDisplayType(String redirectCustomizedDisplayType) {
                this.redirectCustomizedDisplayType = redirectCustomizedDisplayType;
            }

            public String getDisplayOrder() {
                return displayOrder;
            }

            public void setDisplayOrder(String displayOrder) {
                this.displayOrder = displayOrder;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getSubTitle() {
                return subTitle;
            }

            public void setSubTitle(String subTitle) {
                this.subTitle = subTitle;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }

            public String getLimitTimes() {
                return limitTimes;
            }

            public void setLimitTimes(String limitTimes) {
                this.limitTimes = limitTimes;
            }

            public String getRedirectType() {
                return redirectType;
            }

            public void setRedirectType(String redirectType) {
                this.redirectType = redirectType;
            }

            public String getRedirectH5LinkAddress() {
                return redirectH5LinkAddress;
            }

            public void setRedirectH5LinkAddress(String redirectH5LinkAddress) {
                this.redirectH5LinkAddress = redirectH5LinkAddress;
            }

            public String getRedirectAppLinkType() {
                return redirectAppLinkType;
            }

            public void setRedirectAppLinkType(String redirectAppLinkType) {
                this.redirectAppLinkType = redirectAppLinkType;
            }

            public String getRedirectCustomizedTitle() {
                return redirectCustomizedTitle;
            }

            public void setRedirectCustomizedTitle(String redirectCustomizedTitle) {
                this.redirectCustomizedTitle = redirectCustomizedTitle;
            }

            public String getRedirectCustomizedImageUrl() {
                return redirectCustomizedImageUrl;
            }

            public void setRedirectCustomizedImageUrl(String redirectCustomizedImageUrl) {
                this.redirectCustomizedImageUrl = redirectCustomizedImageUrl;
            }

            public String getTurnOffEffectStatus() {
                return turnOffEffectStatus;
            }

            public void setTurnOffEffectStatus(String turnOffEffectStatus) {
                this.turnOffEffectStatus = turnOffEffectStatus;
            }

            public String getTurnOffTextDisplay() {
                return turnOffTextDisplay;
            }

            public void setTurnOffTextDisplay(String turnOffTextDisplay) {
                this.turnOffTextDisplay = turnOffTextDisplay;
            }

            public String getTurnOffRedirectType() {
                return turnOffRedirectType;
            }

            public void setTurnOffRedirectType(String turnOffRedirectType) {
                this.turnOffRedirectType = turnOffRedirectType;
            }

            public String getTurnOffRedirectH5Link() {
                return turnOffRedirectH5Link;
            }

            public void setTurnOffRedirectH5Link(String turnOffRedirectH5Link) {
                this.turnOffRedirectH5Link = turnOffRedirectH5Link;
            }

            public String getTurnOffRedirectAppLinkType() {
                return turnOffRedirectAppLinkType;
            }

            public void setTurnOffRedirectAppLinkType(String turnOffRedirectAppLinkType) {
                this.turnOffRedirectAppLinkType = turnOffRedirectAppLinkType;
            }

            public String getRedirectCustomizedContent() {
                return redirectCustomizedContent;
            }

            public void setRedirectCustomizedContent(String redirectCustomizedContent) {
                this.redirectCustomizedContent = redirectCustomizedContent;
            }
        }
    }

    public static class Category3Bean {
        /**
         * data : {"displayOrder":3,"categoryName":"Bar","description":"cccccc","merchants":[{"practicalName":"cake shop","logoUrl":"paper/534507936909316096.jpeg","tags":["Ice Cream","Korean","Spanish"],"collectionStatus":0,"distance":8512.12,"categoryName":"Casual Dining"},{"practicalName":"zhengticeshi","logoUrl":"merchantIntroduction/537522966059765760.jpg","tags":["Cocktail","Stir Fried Food","Ice Cream"],"collectionStatus":0,"distance":8521,"categoryName":"Casual Dining"}]}
         * updateStatus : true
         * category3Timestamp : 1618364416575
         */

        private DataBean data;
        private String updateStatus;
        private String category3Timestamp;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getUpdateStatus() {
            return updateStatus;
        }

        public void setUpdateStatus(String updateStatus) {
            this.updateStatus = updateStatus;
        }

        public String getCategory3Timestamp() {
            return category3Timestamp;
        }

        public void setCategory3Timestamp(String category3Timestamp) {
            this.category3Timestamp = category3Timestamp;
        }

        public static class DataBean {
            /**
             * displayOrder : 3
             * categoryName : Bar
             * description : cccccc
             * merchants : [{"practicalName":"cake shop","logoUrl":"paper/534507936909316096.jpeg","tags":["Ice Cream","Korean","Spanish"],"collectionStatus":0,"distance":8512.12,"categoryName":"Casual Dining"},{"practicalName":"zhengticeshi","logoUrl":"merchantIntroduction/537522966059765760.jpg","tags":["Cocktail","Stir Fried Food","Ice Cream"],"collectionStatus":0,"distance":8521,"categoryName":"Casual Dining"}]
             */

            private String displayOrder;
            private String categoryType;
            private String categoryName;
            private String description;
            private List<HomeMerchantItemData> merchants;

            public String getCategoryType() {
                return categoryType;
            }

            public void setCategoryType(String categoryType) {
                this.categoryType = categoryType;
            }

            public String getDisplayOrder() {
                return displayOrder;
            }

            public void setDisplayOrder(String displayOrder) {
                this.displayOrder = displayOrder;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public List<HomeMerchantItemData> getMerchants() {
                return merchants;
            }

            public void setMerchants(List<HomeMerchantItemData> merchants) {
                this.merchants = merchants;
            }
        }
    }

    public static class Category4Bean {
        /**
         * data : {"displayOrder":4,"categoryName":"Asian","description":"ddddd","merchants":[{"practicalName":"cake shop","logoUrl":"paper/534507936909316096.jpeg","tags":["Ice Cream","Korean","Spanish"],"collectionStatus":0,"distance":8512.12,"categoryName":"Casual Dining"},{"practicalName":"zhengticeshi","logoUrl":"merchantIntroduction/537522966059765760.jpg","tags":["Cocktail","Stir Fried Food","Ice Cream"],"collectionStatus":0,"distance":8521,"categoryName":"Casual Dining"}]}
         * updateStatus : true
         * category4Timestamp : 1618364417451
         */

        private DataBean data;
        private String updateStatus;
        private String category4Timestamp;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getUpdateStatus() {
            return updateStatus;
        }

        public void setUpdateStatus(String updateStatus) {
            this.updateStatus = updateStatus;
        }

        public String getCategory4Timestamp() {
            return category4Timestamp;
        }

        public void setCategory4Timestamp(String category4Timestamp) {
            this.category4Timestamp = category4Timestamp;
        }

        public static class DataBean {
            /**
             * displayOrder : 4
             * categoryName : Asian
             * description : ddddd
             * merchants : [{"practicalName":"cake shop","logoUrl":"paper/534507936909316096.jpeg","tags":["Ice Cream","Korean","Spanish"],"collectionStatus":0,"distance":8512.12,"categoryName":"Casual Dining"},{"practicalName":"zhengticeshi","logoUrl":"merchantIntroduction/537522966059765760.jpg","tags":["Cocktail","Stir Fried Food","Ice Cream"],"collectionStatus":0,"distance":8521,"categoryName":"Casual Dining"}]
             */

            private String displayOrder;
            private String categoryType;
            private String categoryName;
            private String description;
            private List<HomeMerchantItemData> merchants;

            public String getCategoryType() {
                return categoryType;
            }

            public void setCategoryType(String categoryType) {
                this.categoryType = categoryType;
            }

            public String getDisplayOrder() {
                return displayOrder;
            }

            public void setDisplayOrder(String displayOrder) {
                this.displayOrder = displayOrder;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public List<HomeMerchantItemData> getMerchants() {
                return merchants;
            }

            public void setMerchants(List<HomeMerchantItemData> merchants) {
                this.merchants = merchants;
            }
        }
    }

    public static class Category5Bean {
        /**
         * data : {"displayOrder":5,"categoryName":"Fast Food","description":"eeeeeee","merchants":[{"practicalName":"zhengticeshi","logoUrl":"merchantIntroduction/537522966059765760.jpg","tags":["Cocktail","Stir Fried Food","Ice Cream"],"collectionStatus":0,"distance":8521,"categoryName":"Casual Dining"},{"practicalName":"cake shop","logoUrl":"paper/534507936909316096.jpeg","tags":["Ice Cream","Korean","Spanish"],"collectionStatus":0,"distance":8512.12,"categoryName":"Casual Dining"}]}
         * updateStatus : true
         * category5Timestamp : 1618364418791
         */

        private DataBean data;
        private String updateStatus;
        private String category5Timestamp;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getUpdateStatus() {
            return updateStatus;
        }

        public void setUpdateStatus(String updateStatus) {
            this.updateStatus = updateStatus;
        }

        public String getCategory5Timestamp() {
            return category5Timestamp;
        }

        public void setCategory5Timestamp(String category5Timestamp) {
            this.category5Timestamp = category5Timestamp;
        }

        public static class DataBean {
            /**
             * displayOrder : 5
             * categoryName : Fast Food
             * description : eeeeeee
             * merchants : [{"practicalName":"zhengticeshi","logoUrl":"merchantIntroduction/537522966059765760.jpg","tags":["Cocktail","Stir Fried Food","Ice Cream"],"collectionStatus":0,"distance":8521,"categoryName":"Casual Dining"},{"practicalName":"cake shop","logoUrl":"paper/534507936909316096.jpeg","tags":["Ice Cream","Korean","Spanish"],"collectionStatus":0,"distance":8512.12,"categoryName":"Casual Dining"}]
             */

            private String displayOrder;
            private String categoryType;
            private String categoryName;
            private String description;
            private List<HomeMerchantItemData> merchants;

            public String getCategoryType() {
                return categoryType;
            }

            public void setCategoryType(String categoryType) {
                this.categoryType = categoryType;
            }

            public String getDisplayOrder() {
                return displayOrder;
            }

            public void setDisplayOrder(String displayOrder) {
                this.displayOrder = displayOrder;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public List<HomeMerchantItemData> getMerchants() {
                return merchants;
            }

            public void setMerchants(List<HomeMerchantItemData> merchants) {
                this.merchants = merchants;
            }
        }
    }

    public static class Category1Bean {
        /**
         * data : {"displayOrder":1,"categoryName":"Casual Dining","description":"aaaaa","merchants":[{"practicalName":"cake shop","logoUrl":"paper/534507936909316096.jpeg","tags":["Ice Cream","Korean","Spanish"],"collectionStatus":0,"distance":8512.12,"categoryName":"Casual Dining"},{"practicalName":"zhengticeshi","logoUrl":"merchantIntroduction/537522966059765760.jpg","tags":["Cocktail","Stir Fried Food","Ice Cream"],"collectionStatus":0,"distance":8521,"categoryName":"Casual Dining"}]}
         * updateStatus : true
         * category1Timestamp : 1618364412509
         */

        private DataBean data;
        private String updateStatus;
        private String category1Timestamp;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getUpdateStatus() {
            return updateStatus;
        }

        public void setUpdateStatus(String updateStatus) {
            this.updateStatus = updateStatus;
        }

        public String getCategory1Timestamp() {
            return category1Timestamp;
        }

        public void setCategory1Timestamp(String category1Timestamp) {
            this.category1Timestamp = category1Timestamp;
        }

        public static class DataBean {
            /**
             * displayOrder : 1
             * categoryName : Casual Dining
             * description : aaaaa
             * merchants : [{"practicalName":"cake shop","logoUrl":"paper/534507936909316096.jpeg","tags":["Ice Cream","Korean","Spanish"],"collectionStatus":0,"distance":8512.12,"categoryName":"Casual Dining"},{"practicalName":"zhengticeshi","logoUrl":"merchantIntroduction/537522966059765760.jpg","tags":["Cocktail","Stir Fried Food","Ice Cream"],"collectionStatus":0,"distance":8521,"categoryName":"Casual Dining"}]
             */

            private String displayOrder;
            private String categoryType;
            private String categoryName;
            private String description;
            private List<HomeMerchantItemData> merchants;

            public String getDisplayOrder() {
                return displayOrder;
            }

            public void setDisplayOrder(String displayOrder) {
                this.displayOrder = displayOrder;
            }

            public String getCategoryType() {
                return categoryType;
            }

            public void setCategoryType(String categoryType) {
                this.categoryType = categoryType;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public List<HomeMerchantItemData> getMerchants() {
                return merchants;
            }

            public void setMerchants(List<HomeMerchantItemData> merchants) {
                this.merchants = merchants;
            }
        }
    }
}
