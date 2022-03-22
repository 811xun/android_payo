package com.home.glx.uwallet.request;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * 网络请求
 */
public interface RequestInterface {

    /**
     * 注册
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/userRegisterV1")
    Call<ResponseBody> Regist(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 注册
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/appEpoch/userRegisterV2")
    Call<ResponseBody> NewRegist(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 营销码列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/appEpoch/promotionList")
    Call<ResponseBody> promotionList(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取验证码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/sendSecurityCodeSMS/{phone}/{sendNode}/10")
    Call<ResponseBody> GetRegistCode(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody
            , @Path("phone") String phone
            , @Path("sendNode") String sendNode);


    /**
     * 登录
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/login")
    Call<ResponseBody> Login(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 登录
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @GET("/app/user/maxCreditAmount")
    Call<ResponseBody> maxCreditAmount(@HeaderMap Map<String, String> headers);

    /**
     * 注册
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/appEpoch/saveUserPageLog")
    Call<ResponseBody> addMaidian(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 分期付记录
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/user/transactionRecord")
    Call<ResponseBody> transactionRecord(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    //查询当前支付通道
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/appEpoch/getPayGateWay")
    Call<ResponseBody> getPayGateWay(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 交易明细列表页
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/getTransactionRecordNew")
    Call<ResponseBody> getTransactionRecordNew(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * delete按钮交易明细列表页
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/updateRecordIsShow/{id}")
    Call<ResponseBody> updateRecordIsShow(@HeaderMap Map<String, String> headers
            , @Path("id") String id
            , @Body RequestBody requestBody);

    /**
     * 查询卡支付/分期付交易明细
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/getRecordDetail/{id}")
    Call<ResponseBody> getRecordDetail(@HeaderMap Map<String, String> headers
            , @Path("id") String id
            , @Body RequestBody requestBody);

    /**
     * 查询卡支付/分期付交易明细
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/getTransactionRecordById")
    Call<ResponseBody> getTransactionRecordById(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 查询卡支付/分期付交易明细
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/appEpoch/getRecordDetail/{transNo}")
    Call<ResponseBody> getRecordDetails(@HeaderMap Map<String, String> headers
            , @Path("transNo") String transNo
            , @Body RequestBody requestBody);

    /**
     * 查询卡支付/分期付交易明细
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/appEpoch/v2/getRecordDetail/{transNo}")
    Call<ResponseBody> getRecordDetailsV2(@HeaderMap Map<String, String> headers
            , @Path("transNo") String transNo
            , @Body RequestBody requestBody);

    /**
     * 红包交易记录
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/selectWalletTransaction")
    Call<ResponseBody> selectWalletTransaction(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 查询获得的营销码红包列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/promotionRecords")
    Call<ResponseBody> promotionRecords(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 输入营销码或者邀请码，获得红包
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/enterPromotionCode")
    Call<ResponseBody> enterPromotionCode(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 是否有支付密码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/findPayPassword")
    Call<ResponseBody> findPayPassword(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 找回密码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/forgetPassword")
    Call<ResponseBody> forgetPassword(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 记录用户信息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
//    @POST("/app/infoSupplement")
    @POST("/app/riskCheck")
    Call<ResponseBody> riskCheck(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 获取用户收款码
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/codePath")
    Call<ResponseBody> UserCodePath(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 首页Banner
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/getBannerImg")
    Call<ResponseBody> GetBanner(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 会员等级列表
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/user/memberList")
    Call<ResponseBody> memberList(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 所有用户信息
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/findUserInfoId")
    Call<ResponseBody> findUserInfoId(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 首页广告
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/findOneAds")
    Call<ResponseBody> findOneAds(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 协议同意状态
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/getAgreementState")
    Call<ResponseBody> getAgreementState(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 同意协议
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/updateAgreementState")
    Call<ResponseBody> updateAgreementState(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 发现列表
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/merchant")
    Call<ResponseBody> merchant(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 地图展示用-商户列表(全量)
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/getMerchantLocationList")
    Call<ResponseBody> getMerchantLocationList(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 修改邮箱
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/modifyEmail")
    Call<ResponseBody> modifyEmail(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 获取分享数据
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/getInviteCode")
    Call<ResponseBody> getInviteCode(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 理财账户
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/invest/getCompanyTrustCardInfo")
    Call<ResponseBody> getCompanyTrustCardInfo(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 理财账户状态
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/invest/accountState")
    Call<ResponseBody> accountState(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 解绑账户
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/cardUnbundling")
    Call<ResponseBody> cardUnbundling(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 删除、更新理财账户
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/invest/updateDeleteCardInfo")
    Call<ResponseBody> updateDeleteCardInfo(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 饼图数据
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/invest/chartInfo")
    Call<ResponseBody> chartInfo(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * illion认证状态
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/user/illionVerify")
    Call<ResponseBody> illionVerify(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 首页推荐
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/invest/recommendFinancial")
    Call<ResponseBody> homeRecommend(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 推荐银行
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/getBankLogoList")
    Call<ResponseBody> getBankLogo(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 剩余KYC次数
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/kycResult")
    Call<ResponseBody> kycResult(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 获取网路时间
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/getTimeMillis")
    Call<ResponseBody> getTimeMillis(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 理财列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/invest/SimWithImInfoList")
    Call<ResponseBody> SimWithImInfoList(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 交易明细
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/transactionDetails/{id}")
    Call<ResponseBody> transactionDetails(@HeaderMap Map<String, String> headers
            , @Path("id") String u_id
            , @Body RequestBody requestBody);

    /**
     * 我的投资已结束
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/invest/completedInvest")
    Call<ResponseBody> completedInvest(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 我的投资持有中
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/invest/myAssetSimByType")
    Call<ResponseBody> myAssetSimByType(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 是否开通理财
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/user/{id}")
    Call<ResponseBody> OpenInvestStatus(@HeaderMap Map<String, String> headers
            , @Path("id") String u_id, @Body RequestBody requestBody);


    /**
     * 理财详情(投资前)
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/financialDetails")
    Call<ResponseBody> financialDetails(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 理财详情(已投资)
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/selectSimDetails")
    Call<ResponseBody> selectSimDetails(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 派系计划
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/selectDividendPlanBySimId")
    Call<ResponseBody> selectDividendPlanBySimId(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 还本记录
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/alsoMoney")
    Call<ResponseBody> alsoMoney(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 首页Deals
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/getToDealsImg")
    Call<ResponseBody> getToDealsImg(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 投资记录
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/slectInvestmentRecord")
    Call<ResponseBody> slectInvestmentRecord(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 是否购买过该理财
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/countBySimIdAndUserId")
    Call<ResponseBody> whetherBuyProduct(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 未完成订单总数
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/unfinishedOrderCount")
    Call<ResponseBody> undoneOrder(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 校验支付密码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/checkPayPassword")
    Call<ResponseBody> checkPayPassword(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 收益明细
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/dividendPlan")
    Call<ResponseBody> dividendPlan(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 收益明细
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/getCurrentMonthDividendSum")
    Call<ResponseBody> getCurrentMonthDividendSum(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 修改分期付还款账号
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/user/updateUserRepayBank")
    Call<ResponseBody> updateUserRepayBank(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 绑定还款账户
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/creditTieOnCard")
    Call<ResponseBody> creditTieOnCard(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 修改登陆密码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/changePassword")
    Call<ResponseBody> changePassword(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 理财概览
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/financialStatusReview")
    Call<ResponseBody> financialStatusReview(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取tag,top10
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/getTagInfo")
    Call<ResponseBody> getTagInfo(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 商户搜索页是否有数据
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/haveData")
    Call<ResponseBody> haveData(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 商户搜索页是否有数据
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/isUserFavorite/{merchantId}")
    Call<ResponseBody> isUserFavorite(@HeaderMap Map<String, String> headers
            , @Path("merchantId") String merchantId
            , @Body RequestBody requestBody);

    /**
     * 通过4个经纬度信息获取范围内商户id列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/getMerchantIdInRange")
    Call<ResponseBody> getMerchantIdInRange(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 邀请人列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/invitedUserList")
    Call<ResponseBody> invitedUserList(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 营销码信息查询
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/getPromotionCodeMessage")
    Call<ResponseBody> getPromotionCodeMessage(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 用户收藏列表, 分页展示
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/favoriteList")
    Call<ResponseBody> favoriteList(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 一键清除所有通知信息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/noticeClearAll")
    Call<ResponseBody> noticeClearAll(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 首页新增推荐商户列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/getNewVenus")
    Call<ResponseBody> getNewVenus(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取首页TOP10商户列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/getTopTenList")
    Call<ResponseBody> getTopTenList(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 首页新增推荐商户列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/merchantSearchList")
    Call<ResponseBody> merchantSearchList(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * street
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/findCityStInfo")
    Call<ResponseBody> findCityStInfo(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 商户详情
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/getMerchantDetail/{merchantId}")
    Call<ResponseBody> getMerchantDetail(@HeaderMap Map<String, String> headers
            , @Path("merchantId") String merchantId
            , @Body RequestBody requestBody);

    /**
     * 分期付状态
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/user/creditInfo")
    Call<ResponseBody> creditInfo(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取扫码支付的交易金额
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/getQrPayTransAmount")
    Call<ResponseBody> getQrPayTransAmount(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * ip定位
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/findLocationByIp")
    Call<ResponseBody> findLocationByIp(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取钱包余额
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/account/{id}")
    Call<ResponseBody> getWalletMoney(@HeaderMap Map<String, String> headers
            , @Path("id") String u_id, @Body RequestBody requestBody);


    /**
     * 理财余额
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/getBalance")
    Call<ResponseBody> getBalance(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 充值
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/recharge")
    Call<ResponseBody> recharge(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 用户充值手续费
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/serviceCharge")
    Call<ResponseBody> serviceCharge(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 扫码付款初始化信息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/user/userInfoCredit")
    Call<ResponseBody> userInfoCredit(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 扫码付款，创建订单
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/qrPay")
    Call<ResponseBody> createBorrow(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 扫码付款，分期付创建订单
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/interestCredistOrder")
    Call<ResponseBody> interestCredistOrder(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 扫码付款，分期付创建订单
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/user/createBorrow")
    Call<ResponseBody> fqCreateBorrow(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 未开通分期付商家折扣
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app//user/merchantRate")
    Call<ResponseBody> merchantRate(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取二维码信息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/findUserInfoByQRCode")
    Call<ResponseBody> findUserInfoByQRCode(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 获取NFC信息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/findUserInfoByNFCCode")
    Call<ResponseBody> findUserInfoByNFCCode(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 未完成订单
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/unfinishedOrderInfo")
    Call<ResponseBody> unfinishedOrderInfo(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 银行账户列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/stripeCardList")
    Call<ResponseBody> getBankCardList(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 查询是否有逾期/还款失败订单
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/user/queryRepayInfo")
    Call<ResponseBody> queryRepayInfo(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 查询邀请数据
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/getReceived")
    Call<ResponseBody> getReceived(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 交易详情列表+条件搜索
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/getRecordTwo")
    Call<ResponseBody> getRecordNew(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 还款时间-还款计划列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/borrowByPayday")
    Call<ResponseBody> borrowByPayday(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 还款时间-还款计划列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/borrowByPaydayV2")
    Call<ResponseBody> borrowByPaydayV2(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 还款日订单列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/payDayBorrowDetailV2")
    Call<ResponseBody> payDayBorrowDetail(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 还款卡列表 （latpay+stripe卡）
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/allCardList")
    Call<ResponseBody> allCardList(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 订单-待还款列表信息查询
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/upcomingBorrowList")
    Call<ResponseBody> upcomingBorrowList(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 修改查询邀请状态
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/saveReceivedIsShow")
    Call<ResponseBody> saveReceivedIsShow(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 银行账户列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/getCardList/{userId}")
    Call<ResponseBody> getCardList(@HeaderMap Map<String, String> headers
            , @Path("userId") String userId
            , @Body RequestBody requestBody);

    /**
     * 账户信息更新
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/changeCardMessage")
    Call<ResponseBody> changeCardMessage(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 银行账户列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/getAboutUs")
    Call<ResponseBody> getAboutUs(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 获取支付参数
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/getApiToken")
    Call<ResponseBody> getApiToken(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取支付参数
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/qrPayNew")
    Call<ResponseBody> qrPayNew(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取支付参数新
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/qrPay")
    Call<ResponseBody> qrPay(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 记录支付宝微信支付订单
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/saveOmiPayOrderNo")
    Call<ResponseBody> saveOmiPayOrderNo(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取支付状态
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/qrPayStatusCheck/{flowId}")
    Call<ResponseBody> qrPayStatusCheck(@HeaderMap Map<String, String> headers
            , @Path("flowId") String flowId
            , @Body RequestBody requestBody);

    /**
     * 获取下拉列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/findByCodeList")
    Call<ResponseBody> findByCodeList(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 银行账户列表
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/addTransferApply")
    Call<ResponseBody> addTransferApply(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 添加银行账户
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/tieOnCard")
    Call<ResponseBody> addBankAccount(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 添加银行账户
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/stripeCheckCardNoRedundancy")
    Call<ResponseBody> stripeCheckCardNoRedundancy(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 添加银行账户
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/stripeBindCardByToken")
    Call<ResponseBody> stripeBindCardByToken(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取验证码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/sendSMSVerificationCode/{phoneNumber}/{nodeType}")
    Call<ResponseBody> getYzm(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody, @Path("phoneNumber") String phoneNumber, @Path("nodeType") String nodeType);

    /**
     * 获取验证码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/findPromotionCode")
    Call<ResponseBody> findPromotionCode(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取验证码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/addPromotionCode")
    Call<ResponseBody> addPromotionCode(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 校验验证码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/checkVerificationCode")
    Call<ResponseBody> checkVerificationCode(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 更新卡信息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/updateLatpayCardInfo")
    Call<ResponseBody> updateLatpayCardInfo(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取银行卡详情
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/getCardDetails")
    Call<ResponseBody> getCardDetails(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 绑卡获取卡类型接口
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/latpayGetCardType")
    Call<ResponseBody> latpayGetCardType(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/updateUserCreditCardAgreementState")
    Call<ResponseBody> updateUserCreditCardAgreementState(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/defaultCardInfo")
    Call<ResponseBody> defaultCardInfo(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    //APP首页弹窗集合
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/getAppHomePageReminder")
    Call<ResponseBody> getAppHomePageReminder(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/getUserBusinessStatus")
    Call<ResponseBody> jiaoyanFenqifu(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/updateUserAgreementState")
    Call<ResponseBody> updateUserAgreementState(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 添加理财银行账户
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/addOrUpdateCard")
    Call<ResponseBody> addOrUpdateCard(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 上传图片
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/paperMultiUploadFile")
    Call<ResponseBody> multiUploadFile(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 上传位置
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/location")
    Call<ResponseBody> location(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 上传流水图片
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/dividendImgUpload")
    Call<ResponseBody> dividendImgUpload(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 上传打款图片
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/fileUpload")
    Call<ResponseBody> fileUpload(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 设置支付密码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/updatePayPassword")
    Call<ResponseBody> updatePayPassword(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 立即投资
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/newSimApply")
    Call<ResponseBody> newSimApply(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 理财账户信息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/getCardInfo")
    Call<ResponseBody> getCardInfo(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 激活、变更或关闭会员等级
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/user/activateOrCloseMember")
    Call<ResponseBody> activateOrCloseMember(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 分期订单
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/user/borrowList")
    Call<ResponseBody> borrowList(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 批量还款
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/user/borrowRepayment")
    Call<ResponseBody> borrowRepayment(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 批量主动还款(根据: 订单/还款计划)
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/user/repaymentV2")
    Call<ResponseBody> repaymentV2(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 批量主动还款(根据: 订单/还款计划)
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/repayTotalInfoV2")
    Call<ResponseBody> repayTotalInfo(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * app校验验证码是否正确
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/verifyCode")
    Call<ResponseBody> verifyCode(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 账户信息查询
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/selectAccountUser/{id}")
    Call<ResponseBody> selectAccountUser(@HeaderMap Map<String, String> headers
            , @Path("id") String id
            , @Body RequestBody requestBody);


    /**
     * 激活、变更或关闭会员等级
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/user/userCreditMessage")
    Call<ResponseBody> userCreditMessage(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 激活、变更或关闭会员等级
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/appEpoch/riskCheckNew")
    Call<ResponseBody> saveKycInfo(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 分期付账单详情
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/user/repayList")
    Call<ResponseBody> repayList(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 分期还款
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/user/repayment")
    Call<ResponseBody> repayment(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/user/bankcardBinding")
    Call<ResponseBody> bankcardBinding(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 分期付跑风控
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/user/activationInstallment")
    Call<ResponseBody> activationInstallment(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 查看illion报告是否返回
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/bankStatementsIsResult/{userId}")
    Call<ResponseBody> bankStatementsIsResult(@HeaderMap Map<String, String> headers
            , @Path("userId") String userId
            , @Body RequestBody requestBody);

    /**
     * 检查版本更新接口
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/appVersionVerify")
    Call<ResponseBody> appVersionVerify(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 检查版本更新接口新
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/appVersionVerifyV3")
    Call<ResponseBody> appVersionVerifyV2(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 投资结果（上传打款凭证页面数据）
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/investResult")
    Call<ResponseBody> investResult(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 转让记录
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/transferList")
    Call<ResponseBody> transferApply(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 上传打款凭证
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/invest/addSimApplyImage")
    Call<ResponseBody> addSimApplyImage(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 消息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/notice")
    Call<ResponseBody> notice(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 删除消息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/noticeDelete/{id}")
    Call<ResponseBody> noticeDelete(@HeaderMap Map<String, String> headers
            , @Path("id") String id
            , @Body RequestBody requestBody);

    /**
     * 消息已读
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/noticeModify")
    Call<ResponseBody> noticeModify(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 消息已读
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/allNoticeHasRead/{id}")
    Call<ResponseBody> allNoticeHasRead(@HeaderMap Map<String, String> headers
            , @Path("id") String id
            , @Body RequestBody requestBody);

    /**
     * 获取用户是否未读消息
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/getAllNoticeHasRead/{id}")
    Call<ResponseBody> getAllNoticeHasRead(@HeaderMap Map<String, String> headers
            , @Path("id") String id
            , @Body RequestBody requestBody);

    /**
     * 收款记录
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/appQrPayFlowList/id")
    Call<ResponseBody> appQrPayFlowList(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 是否开通分期付
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/user/verify")
    Call<ResponseBody> openFenQiFu(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);


    /**
     * 向旧手机发送验证码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/sendSecurityCodeSMSToOld/{phone}/15/10")
    Call<ResponseBody> sendSecurityCodeSMSToOld(@HeaderMap Map<String, String> headers
            , @Path("phone") String phone
            , @Body RequestBody requestBody);


    /**
     * 验证旧手机号验证码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/checkOldPhoneCode/{oldPhone}/{signCode}/10")
    Call<ResponseBody> checkOldPhoneCode(@HeaderMap Map<String, String> headers
            , @Path("oldPhone") String phone
            , @Path("signCode") String signCode
            , @Body RequestBody requestBody);


    /**
     * 验证旧手机号验证码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/sendSecurityCodeSMSToNew/{phone}/15/10")
    Call<ResponseBody> sendSecurityCodeSMSToNew(@HeaderMap Map<String, String> headers
            , @Path("phone") String phone
            , @Body RequestBody requestBody);


    /**
     * 验证旧手机号验证码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/updatePhone/{phone}/{oldPhone}/{signCode}/10")
    Call<ResponseBody> updatePhone(@HeaderMap Map<String, String> headers
            , @Path("phone") String phone
            , @Path("oldPhone") String oldPhone
            , @Path("signCode") String signCode
            , @Body RequestBody requestBody);


    /**
     * 查询PIN number
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/queryPinNumber/{id}")
    Call<ResponseBody> queryPinNumber(@HeaderMap Map<String, String> headers
            , @Path("id") String id
            , @Body RequestBody requestBody);


    /**
     * 查询PIN number
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/updatePinNumber/{id}/{pinNumber}")
    Call<ResponseBody> updatePinNumber(@HeaderMap Map<String, String> headers
            , @Path("id") String id
            , @Path("pinNumber") String pinNumber
            , @Body RequestBody requestBody);

    /**
     * 查询PIN number
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/checkPinNumber/{id}/{pinNumber}")
    Call<ResponseBody> checkPinNumber(@HeaderMap Map<String, String> headers
            , @Path("id") String id
            , @Path("pinNumber") String pinNumber
            , @Body RequestBody requestBody);


    /**
     * 发送邮箱验证码
     *
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("/app/sendSecurityCode/{email}/3/10")
    Call<ResponseBody> sendSecurityCode(@HeaderMap Map<String, String> headers
            , @Path("email") String email
            , @Body RequestBody requestBody);

    /**
     * 查询还款手续费
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/getChannelFee")
    Call<ResponseBody> getChannelFee(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取illion机构列表
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/getInstitutions")
    Call<ResponseBody> getInstitutions(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * KYC新风控接口
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/riskCheckNew")
    Call<ResponseBody> riskCheckNew(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 预加载机构信息
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/preload")
    Call<ResponseBody> preload(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * illion获取报告
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/fetchAll")
    Call<ResponseBody> fetchAll(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * illion-mfa二次验证提交
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/mfaSubmit")
    Call<ResponseBody> mfaSubmit(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取报告失败发送短信给运营
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/sendIllionMessage")
    Call<ResponseBody> sendIllionMessage(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 记录用户点击申请分期付进入kyc页面次数接口
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/appEpoch/saveUserToKycPageLog")
    Call<ResponseBody> saveUserToKycPageLog(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 卡列表页面,设置默认银行卡
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/presetCard")
    Call<ResponseBody> presetCard(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * POS扫码获取订单金额
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/showPosTransAmount")
    Call<ResponseBody> showPosTransAmount(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 查询近6个月省钱金额
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/getUserSavedAmount")
    Call<ResponseBody> getUserSavedAmount(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取交易金额
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/v3/getPayTransAmountDetail")
    Call<ResponseBody> getPayTransAmountDetail(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 还款交易查询信息
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/getUserRepayFeeData")
    Call<ResponseBody> getUserRepayFeeData(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 验证当前用户 密码是否正确
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/appEpoch/verifyUserPassword/{pwd}")
    Call<ResponseBody> verifyUserPassword(@HeaderMap Map<String, String> headers
            , @Path("pwd") String pwd
            , @Body RequestBody requestBody);

    /**
     * POS扫码获取订单金额
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/appEpoch/addNewFavorite")
    Call<ResponseBody> addNewFavorite(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 支付接口
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/v4/qrPay")
    Call<ResponseBody> pay(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * POS扫码获取订单金额
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/app/checkPayDistanceV2")
    Call<ResponseBody> checkPayDistance(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取首页市场推广、自定义分类数据
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/appEpoch/getAppHomePageBottomData")
    Call<ResponseBody> getAppHomePageBottomData(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取首页banner
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/appEpoch/getAppHomePageTopBanner")
    Call<ResponseBody> getAppHomePageTopBanner(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 商户搜素 列表页接口
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/appEpoch/merchantList")
    Call<ResponseBody> merchantList(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 首页自定义分类栏view all获取图片
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/appEpoch/getAppHomePageCategoryAllImgData")
    Call<ResponseBody> getAppHomePageCategoryAllImgData(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * 获取市场推广view all数据
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("/appEpoch/getAppHomePageViewAllExclusiveData")
    Call<ResponseBody> getAppHomePageViewAllExclusiveData(@HeaderMap Map<String, String> headers
            , @Body RequestBody requestBody);

    /**
     * OMIPAY测试
     *
     * @return
     */
    @POST
    Call<ResponseBody> OMIPAY(@Url String fileUrl);

}
