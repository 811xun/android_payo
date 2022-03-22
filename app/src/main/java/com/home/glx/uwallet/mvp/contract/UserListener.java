package com.home.glx.uwallet.mvp.contract;

import java.util.Map;

public interface UserListener {

    //查询近6个月省钱金额
    void getUserSavedAmount(Map<String, Object> maps, ActionCallBack callBack);

    //验证当前用户 密码是否正确
    void verifyUserPassword(Map<String, Object> maps, String password, ActionCallBack callBack);

    //消费人列表
    void invitedUserList(Map<String, Object> maps, ActionCallBack callBack);

    //营销码信息查询
    void getPromotionCodeMessage(Map<String, Object> maps, ActionCallBack callBack);

    //用户收藏列表, 分页展示
    void favoriteList(Map<String, Object> maps, ActionCallBack callBack);

    //一键清除所有通知信息
    void noticeClearAll(Map<String, Object> maps, ActionCallBack callBack);

    //卡列表
    void getCardList(Map<String, Object> maps, ActionCallBack callBack);

    //查询是否有逾期/还款失败订单
    void queryRepayInfo(Map<String, Object> maps, ActionCallBack callBack);

    //查询邀请数据
    void getReceived(Map<String, Object> maps, ActionCallBack callBack);

    //修改查询邀请状态
    void saveReceivedIsShow(Map<String, Object> maps);

    //交易详情列表+条件搜索
    void getRecordNew(Map<String, Object> maps, ActionCallBack callBack);

    //以还款日为纬度(ASC排序), 展示该日所有要还款的订单还款金额
    void borrowByPayday(Map<String, Object> maps, ActionCallBack callBack);

    //以订单为纬度(ASC排序), 展示该日所有要还款的订单还款金额
    void upcomingBorrowList(Map<String, Object> maps, ActionCallBack callBack);

    //还款日订单列表
    void payDayBorrowDetail(Map<String, Object> maps, ActionCallBack callBack);

    //批量主动还款(根据: 订单/还款计划)
    void repaymentV2(Map<String, Object> maps, ActionCallBack callBack);

    //Pay ALL 按钮展示详情信息
    void repayTotalInfo(Map<String, Object> maps, ActionCallBack callBack);

    //账户信息查询
    void selectAccountUser(Map<String, Object> maps, ActionCallBack callBack);

    //app校验验证码是否正确
    void verifyCode(Map<String, Object> maps, ActionCallBack callBack);

    //以还款日为纬度(ASC排序), 展示该日所有要还款的订单还款金额
    void borrowByInstalment(Map<String, Object> maps, ActionCallBack callBack);

    //25%版本-还款日订单列表
    void borrowByPaydayV2(Map<String, Object> maps, ActionCallBack callBack);

    //验证用户是否激活分期付业务
    void userVerify(Map<String, Object> maps, ActionCallBack callBack);

    //验证用户是否激活分期付业务
    void getTransactionRecordById(Map<String, Object> maps, ActionCallBack callBack);
}
