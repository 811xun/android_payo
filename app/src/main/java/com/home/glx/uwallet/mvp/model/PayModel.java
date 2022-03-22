package com.home.glx.uwallet.mvp.model;

import android.content.Context;
import android.text.TextUtils;

import com.braze.Braze;
import com.google.gson.Gson;
import com.home.glx.uwallet.datas.RepaySuccessData;
import com.home.glx.uwallet.datas.TransAmountData;
import com.home.glx.uwallet.datas.UserRepayFeeData;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.PayListener;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.BzEventParameterName;
import com.home.glx.uwallet.tools.BzEventType;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PayModel implements PayListener {
    private Context context;

    public PayModel(Context context) {
        this.context = context;
    }

    @Override
    public void getPayTransAmountDetail(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("获取交易金额参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getPayTransAmountDetail(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("获取交易金额:" + dataStr);
                        Gson gson = new Gson();
                        TransAmountData data = gson.fromJson(dataStr, TransAmountData.class);
                        callBack.onSuccess(data, code);
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void qrPay(final Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
        new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("获扫码支付参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.pay(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("扫码支付:" + dataStr);
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
//                    AppsFlyerLib.getInstance().logEvent(context, "completed_purchase", mapp);
                    Braze.getInstance(context).getCurrentUser()
                            .setCustomUserAttribute(BzEventType.COMPLETED_PURCHASE, mapp.toString());

                    Map<String, Object> eventValue = new HashMap<String, Object>();
                    eventValue.put(BzEventParameterName.SUCCESS, 1);
//                    AppsFlyerLib.getInstance().logEvent(context, AFInAppEventType.ADD_PAYMENT_INFO, eventValue);
                    Braze.getInstance(context).getCurrentUser()
                            .setCustomUserAttribute(BzEventType.ADD_PAYMENT_INFO, eventValue.toString());
                }

            }


            @Override
            public void resError(String error) {
                callBack.onError(error);
            }
        }, 30);
    }

    @Override
    public void getUserRepayFeeData(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("还款交易查询信息参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getUserRepayFeeData(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("还款交易查询信息:" + dataStr);
                        Gson gson = new Gson();
                        UserRepayFeeData data = gson.fromJson(dataStr, UserRepayFeeData.class);
                        callBack.onSuccess(data);
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void repaymentV2(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("批量主动还款(根据: 订单/还款计划)参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.repaymentV2(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("批量主动还款(根据: 订单/还款计划):" + dataStr);
                        Gson gson = new Gson();
                        RepaySuccessData data = gson.fromJson(dataStr, RepaySuccessData.class);
                        callBack.onSuccess(data);
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }
}
