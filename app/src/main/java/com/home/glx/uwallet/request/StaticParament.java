package com.home.glx.uwallet.request;

public class StaticParament {

    //--------------------测试服务器------------------
    public static String server = "1";
    public static String zhifu = "http://paytest-api.loancloud.cn";//默认都是支付的
    //    public static String zhifu = "http://192.168.2.150:6010"; //泽垣本地地址
    //public static String licai = "http://investtest-api.loancloud.cn";.
    public static String fenqi = "http://credittest-api.loancloud.cn";

    //理财图片
    public static String ImgUrl = "http://imagetest-image.loancloud.cn/";
    public static String BannerUrl = "http://imagetest-image.loancloud.cn/";

    public static String H5 = "http://imagetest-image.loancloud.cn/h5/d";

    //---------------------澳洲测试服务器(准生产)---------------------------

//    public static String server = "2";
//    public static String zhifu = "https://paytest-api.payo.com.au";
//    //public static String licai = "https://investtest-api.payo.com.au";
//    public static String fenqi = "https://credittest-api.payo.com.au";
//
//    //理财图片
//    //public static String ImgUrl = "https://h5test.payo.com.au/h5";
//    public static String ImgUrl = "http://uwallet.s3-ap-southeast-2.amazonaws.com/";
//    public static String BannerUrl = ImgUrl;
//
//    public static String H5 = "https://h5test.payo.com.au/h5/";


    //--------------------正式------------------------
    /**
     * pay.api.uwallet.net.au        支付服务端 6010
     * invest.api.uwallet.net.au    理财服务端 6020
     * credit.api.uwallet.net.au    分期付服务端 6030
     */

//    public static String server = "3";
//    public static String zhifu = "https://pay-api.payo.com.au";
//    //public static String licai = "https://invest-api.payo.com.au";
//    public static String fenqi = "https://credit-api.payo.com.au";
//
//    //理财图片
//    public static String ImgUrl = "https://uwallet.s3-ap-southeast-2.amazonaws.com/";
//    public static String BannerUrl = "https://uwallet.s3-ap-southeast-2.amazonaws.com/";
//    //二维码图片地址
//    public static String ERImgUrl = "https://qr.payo.com.au/";
//    public static String H5 = "https://h5.payo.com.au/";


    //-----------------------------------------------
    public static String MainUrl = zhifu + "";
    //public static String LiCaiUrl = licai + "";
    public static String FENQIFU = fenqi + "";

    //    public static String H5 = "https://h5.uwallet.net.au";

    //关于我们
    public static String AboutUs = H5 + "html/aboutUs.html?version=";
    //隐私协议
    public static String YinSi = H5 + "html/aggrement.html";
    //用户协议
    public static String agreement = H5 + "html/CustomersTermsAndConditions.html";
    public static String YinSi2 = H5 + "html/CustomersTermsAndConditions.html";
    public static String fq_agreement = H5 + "html/Click here for our fee charge policy.html";

    public static String all_agreement = H5 + "html/contractList.html?path=" + zhifu + "/";


    //商户详情
    public static String SHMsg = H5 + "html/merchantDetails.html?userId=";
    //理财协议
    public static String InvestAgreemen = H5 + "html/investAgreement.html";
    //分期付协议
    public static final String creditDeal = H5 + "html/creditDeal.html";
    //账户添加协议
    public static final String bank_account_add = H5 + "html/DirectDebitCustomerApplicationForm.html";

    public static final String deal = H5 + "html/direct.html";


    public static final String apiKey = "3.1415926UWallet&LoanCloud";

    public static final String Language = "lang";
    public static final String USER = "user";
    public static final String DEVICE = "device";

    public static final String USERDATA = "user_data";
    public static final String USER_ID = "user_id";
    public static final String USER_TOKEN = "user_token";
    //分期还款账户
    public static final String USER_FQ_ACCOUNT = "user_fq_account";

    //服务费类型、金额
    public static final String USER_FEE_TYPE = "user_fee_type";

    public static final String USER_RATE = "user_rate";

    public static final String USER_NAME1 = "user_name1";
    public static final String USER_NAME2 = "user_name2";
    public static final String USER_NAME3 = "user_name3";

    public static String USER_EMAIL = "user_email";
    public static String USER_MSG = "user_msg";
    public static String USER_ALL_MSG = "user_all_msg";
    public static String USER_PHONE = "user_phone";

    public static String USER_LICAI = "user_licai";
    public static String USER_FENQI = "user_fenqi";
    public static String USER_ZHIFU = "user_zhifu";
    public static String USER_PAY_PWD = "user_pwd";

    //控制用户在支付页面开通分期付成功后返回支付页面的标识
    public static String USER_RETURN_PAY = "user_return_pay";

    //控制app是否弹split标识
    public static String SPLIT_FLAG1 = "fenqifu_split_flag";
    //控制首页只弹一次的标识
    //首页的获取位置弹窗
    public static String LOCATION_FLAG = "location_flag";
    //地图的位置提示弹窗
    public static String MAP_LOCATION_FLAG = "map_location_flag";
    public static String DISTANCE_LOCATION_FLAG = "distance_location_flag";
    public static String SPLIT_FLAG = "split_flag";
    public static String DEVICE_AD = "device_ad";
    public static String CHANGE_FAVORITE = "change_favorite";
    public static String Merchant_HEIGHT = "merchant_logo_height";
    public static String DEVICE_GUIDE = "device_guide";
    public static String CHECK_FLAG = "check_flag";
    public static String APP_EMAIL = "app_email";
    public static String APP_PHONE = "app_phone";
    //是否是从付款页面进的开通分期付流程
    public static String FROM_PAY_MONEY = "from_pay_money";
    //是否是从二选一页面进的开通分期付流程
    public static String FROM_SELECT_PAY = "from_select_pay";
    //完成还款，刷新分期付订单列表
    public static String REPAY_SUCCESS = "repay_success";
    //完成付款，刷新订单和分期付订单列表
    public static String PAY_SUCCESS = "pay_success";
    public static String GO_AWAY_TIME = "go_away_time";
    public static String PIN_NUMBER = "pin_number";
    public static String FAILE_NUMBER = "faile_number";


    public static String APP = "app";
    public static String HOME = "home";
    public static String PUSH_TOKEN = "push_token";
    public static String LAT = "lat";
    public static String LNG = "lng";
    public static String CITY = "city";
    public static String STREET = "street";
    public static String LANGUAGE = "language";
    public static String CHANGE_LANGUAGE = "change_language";
    public static String LOOK_MONEY = "look_money";

    //一些样式配置参数
    public static int GONE_ADD_BANK = 1;
    public static int GONE_ITEM_ = 2;
    public static int GONE_ADD_CHECK = 3;

    //首页缓存数据
    public static String BANNER_TIMESTAMP = "banner_CHECKtimestamp";
    public static String BANNER_ID = "banner_id";
    public static String BANNER_DATA = "banner_data";
    public static String CATEGORY1_TIMESTAMP = "category1_timestamp";
    public static String CATEGORY2_TIMESTAMP = "category2_timestamp";
    public static String CATEGORY3_TIMESTAMP = "category3_timestamp";
    public static String CATEGORY4_TIMESTAMP = "category4_timestamp";
    public static String CATEGORY5_TIMESTAMP = "category5_timestamp";
    public static String EXCLUSIVE_TIMESTAMP = "exclusive_timestamp";
    public static String CATEGORY1_LIST = "category1_list";
    public static String CATEGORY2_LIST = "category2_list";
    public static String CATEGORY3_LIST = "category3_list";
    public static String CATEGORY4_LIST = "category4_list";
    public static String CATEGORY5_LIST = "category5_list";
    public static String EXCLUSIVE_LIST = "exclusive_list";


}
