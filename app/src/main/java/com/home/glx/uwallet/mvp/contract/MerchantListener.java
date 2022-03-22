package com.home.glx.uwallet.mvp.contract;

import java.util.Map;

public interface MerchantListener {

    //POS扫码获取订单金额
    void showPosTransAmount(Map<String, Object> maps, ActionCallBack callBack);

    //通过订单单号查询卡支付/分期付交易明细
    void getRecordDetail(Map<String, Object> maps, String transNo, ActionCallBack callBack);

    //通过订单单号查询卡支付/分期付交易明细
    void getRecordDetailV2(Map<String, Object> maps, String transNo, ActionCallBack callBack);

    //收藏商户-添加/删除收藏
    void addNewFavorite(Map<String, Object> maps, ActionCallBack callBack);

    //判断是否在商家附近支付
    void checkPayDistance(Map<String, Object> maps, ActionCallBack callBack);

    //获取首页市场推广、自定义分类数据
    void getAppHomePageBottomData(Map<String, Object> maps, ActionCallBack callBack);

    //获取首页banner
    void getAppHomePageTopBanner(Map<String, Object> maps, ActionCallBack callBack);

    //商户搜素 列表页接口
    void merchantList(Map<String, Object> maps, ActionCallBack callBack);

    //首页自定义分类栏view all获取图片
    void getAppHomePageCategoryAllImgData(Map<String, Object> maps, ActionCallBack callBack);

    //获取市场推广view all数据
    void getAppHomePageViewAllExclusiveData(Map<String, Object> maps, ActionCallBack callBack);

    //获取tag,top10,全量表信息
    void getTagInfo(Map<String, Object> maps, ActionCallBack callBack);

    //商户搜索页是否有数据
    void haveData(Map<String, Object> maps, ActionCallBack callBack);

    //商户是否被用户收藏
    void isUserFavorite(Map<String, Object> maps, String merchantId, ActionCallBack callBack);
}
