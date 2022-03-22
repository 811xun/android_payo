package com.home.glx.uwallet.mvp.model;

import android.content.Context;
import android.text.TextUtils;

import com.braze.Braze;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.home.glx.uwallet.datas.HomeMerchantListData;
import com.home.glx.uwallet.datas.MerchantItemData;
import com.home.glx.uwallet.datas.PcDatas;
import com.home.glx.uwallet.datas.TagsData;
import com.home.glx.uwallet.datas.TransationNoData;
import com.home.glx.uwallet.datas.ViewBannerData;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.MerchantListener;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.BzEventParameterName;
import com.home.glx.uwallet.tools.BzEventType;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class MerchantModel implements MerchantListener {
    private Context context;

    public MerchantModel(Context context) {
        this.context = context;
    }

    @Override
    public void showPosTransAmount(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("POS扫码获取订单金额参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.showPosTransAmount(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("POS扫码获取订单金额:" + dataStr);
                        JSONObject jsonObject = null;
                        String transAmount = "";
                        String merchantId = "";
                        String posTransNo = "";
                        try {
                            jsonObject = new JSONObject(dataStr);
                            transAmount = jsonObject.getString("transAmount");
                            merchantId = jsonObject.getString("merchantId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callBack.onSuccess(transAmount, merchantId, posTransNo);
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void getRecordDetail(Map<String, Object> maps, final String transNo, final ActionCallBack callBack) {

        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("查询卡支付/分期付交易明细参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getRecordDetails(headerMap, transNo, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("查询卡支付/分期付交易明细:" + dataStr);
                        Gson gson = new Gson();
                        TransationNoData transationDetailData = gson.fromJson(dataStr, TransationNoData.class);
                        callBack.onSuccess(transationDetailData);
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void getRecordDetailV2(final Map<String, Object> maps, final String transNo, final ActionCallBack callBack) {

        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("查询卡支付/分期付交易明细参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getRecordDetailsV2(headerMap, transNo, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("查询卡支付/分期付交易明细:" + dataStr);
                        String borrowCode = "";
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            if (jsonObject.has("flowId")) {
                                borrowCode = jsonObject.getString("flowId");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callBack.onSuccess(dataStr);

                        if (!TextUtils.isEmpty(borrowCode)) {
                            Map<String, Object> mapp = new HashMap<>();
                            //提交AF事件(支付完成)
                            mapp.put(BzEventParameterName.REVENUE, maps.get("trulyPayAmount"));
                            mapp.put(BzEventParameterName.RECEIPT_ID, maps.get("merchantId"));
                            mapp.put(BzEventParameterName.CONTENT_TYPE, maps.get("payType"));
                            mapp.put(BzEventParameterName.ORDER_ID, borrowCode);
                            mapp.put(BzEventParameterName.CURRENCY, "AUD");
                            mapp.put(BzEventParameterName.QUANTITY, 1);
                            //maps.put("repayType", 1);
//                            AppsFlyerLib.getInstance().logEvent(context, "completed_purchase", mapp);

                            Braze.getInstance(context).getCurrentUser()
                                    .setCustomUserAttribute(BzEventType.COMPLETED_PURCHASE, mapp.toString());

                            Map<String, Object> eventValue = new HashMap<String, Object>();
                            eventValue.put(BzEventParameterName.SUCCESS, 1);
//                            AppsFlyerLib.getInstance().logEvent(context, AFInAppEventType.ADD_PAYMENT_INFO, eventValue);

                            Braze.getInstance(context).getCurrentUser()
                                    .setCustomUserAttribute(BzEventType.ADD_PAYMENT_INFO, eventValue.toString());
                        }
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void addNewFavorite(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("收藏商户-添加/删除收藏参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.addNewFavorite(headerMap, requestBody);
                        requestUtils.Call(call, null, false);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("收藏商户-添加/删除收藏:" + dataStr);
                        SharePreferenceUtils adSharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.DEVICE);
                        //是否收藏商户，若有首页刷新的标识
                        adSharePreferenceUtils.put(StaticParament.CHANGE_FAVORITE, "1");
                        adSharePreferenceUtils.save();
                        callBack.onSuccess();
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void checkPayDistance(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("判断是否在商家附近支付参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.checkPayDistance(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("判断是否在商家附近支付:" + dataStr);
                        callBack.onSuccess(dataStr);
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void getAppHomePageBottomData(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("获取首页市场推广、自定义分类数据参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getAppHomePageBottomData(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("获取首页市场推广、自定义分类数据:" + dataStr);
                        Gson gson = new Gson();
                        List<HomeMerchantListData> homeMerchantListData = new ArrayList<>();
                        if (!dataStr.equals("null")) {
                            homeMerchantListData = gson.fromJson(dataStr, new TypeToken<List<HomeMerchantListData>>() {
                            }.getType());
                        }
                        callBack.onSuccess(homeMerchantListData);
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void getAppHomePageTopBanner(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("获取首页banner参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getAppHomePageTopBanner(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("获取首页banner:" + dataStr);
                        Gson gson = new Gson();
                        JSONObject jsonObject = null;
                        String updateStatus = "";
                        try {
                            updateStatus = jsonObject.getString("updateStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callBack.onSuccess(updateStatus);
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void merchantList(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("商户搜素 列表页接口参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.merchantList(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("商户搜素 列表页接口:" + dataStr);
                        Gson gson = new Gson();
                        int maxPages = gson.fromJson(pc, PcDatas.class).getMaxPages();
                        List<MerchantItemData> merchantItemDataList = new ArrayList<>();
                        if (!dataStr.equals("null")) {
                            merchantItemDataList = gson.fromJson(dataStr, new TypeToken<List<MerchantItemData>>() {
                            }.getType());
                        }
                        callBack.onSuccess(maxPages, merchantItemDataList);
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void getAppHomePageCategoryAllImgData(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("首页自定义分类栏view all获取图片参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getAppHomePageCategoryAllImgData(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("首页自定义分类栏view all获取图片:" + dataStr);
                        Gson gson = new Gson();
                        JSONObject jsonObject = null;
                        String images = "";
                        List<ViewBannerData> list = new ArrayList<>();
                        try {
                            jsonObject = new JSONObject(dataStr);
                            images = jsonObject.getString("images");
                            list = gson.fromJson(images, new TypeToken<List<ViewBannerData>>() {
                            }.getType());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callBack.onSuccess(list);
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void getAppHomePageViewAllExclusiveData(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("获取市场推广view all数据参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getAppHomePageViewAllExclusiveData(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("获取市场推广view all数据:" + dataStr);
                        Gson gson = new Gson();
                        JSONObject jsonObject = null;
                        String images = "";
                        int maxPages = gson.fromJson(pc, PcDatas.class).getMaxPages();
                        List<HomeMerchantListData.ExclusiveBean.ListBean> list = new ArrayList<>();
                        list = gson.fromJson(dataStr, new TypeToken<List<HomeMerchantListData.ExclusiveBean.ListBean>>() {
                        }.getType());
                        callBack.onSuccess(maxPages, list);
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void getTagInfo(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("top10tag参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getTagInfo(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, final String code) {
                        L.log("top10tag：" + dataStr);
                        Gson gson = new Gson();
                        List<String> topTen = gson.fromJson(dataStr, TagsData.class).getTopTen();
                        List<String> tags = gson.fromJson(dataStr, TagsData.class).getTags();
                        callBack.onSuccess(topTen, tags);
                    }

                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void haveData(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("商户搜索页是否有数据参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.haveData(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, final String code) {
                        L.log("商户搜索页是否有数据：" + dataStr);
                        Gson gson = new Gson();
                        callBack.onSuccess(dataStr);
                    }

                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void isUserFavorite(Map<String, Object> maps, final String merchantId, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("商户是否被用户收藏参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.isUserFavorite(headerMap, merchantId, requestBody);
                        requestUtils.Call(call, null, false);
                    }

                    @Override
                    public void resData(String dataStr, String pc, final String code) {
                        L.log("商户是否被用户收藏：" + dataStr);
                        Gson gson = new Gson();
                        callBack.onSuccess(dataStr);
                    }

                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }
}
