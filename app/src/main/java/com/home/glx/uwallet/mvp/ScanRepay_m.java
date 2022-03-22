package com.home.glx.uwallet.mvp;

import android.content.Context;
import android.text.TextUtils;

import com.braze.Braze;
import com.google.gson.Gson;
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

public class ScanRepay_m implements ScanRepay_in.Model {

    private Context context;
    private ScanRepay_p present;

    public ScanRepay_m(Context context, ScanRepay_p present) {
        this.context = context;
        this.present = present;
    }

    @Override
    public void getFenQiData(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("分期信息参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.userInfoCredit(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("分期信息:" + dataStr);
                        present.resFenQiData(dataStr);

                    }

                    @Override
                    public void resError(String error) {
                        present.resFenQiData(null);

                    }
                });
    }

    @Override
    public void getFenQiLimitData(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("分期信息参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.userInfoCredit(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("分期信息:" + dataStr);
                        present.resFenQiLimitData(dataStr);

                    }

                    @Override
                    public void resError(String error) {
                        present.resFenQiData(null);

                    }
                });
    }

    @Override
    public void getFenQiLimitData1(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("分期信息参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.userInfoCredit(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("分期信息:" + dataStr);
                        present.resFenQiLimitData1(dataStr);

                    }

                    @Override
                    public void resError(String error) {
                        present.resFenQiData(null);

                    }
                });
    }

    /**
     * 账户、余额支付
     *
     * @param maps
     */
    @Override
    public void reqRepqy(final Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.createBorrow(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("账户、余额支付:" + dataStr);

                        //提交AF事件(支付完成)
                        maps.put(BzEventParameterName.REVENUE, maps.get("transAmount"));
                        maps.put(BzEventParameterName.RECEIPT_ID, maps.get("merchantId"));
                        maps.put("repayType", 2);
//                        AppsFlyerLib.getInstance().logEvent(context, "completed_purchase", maps);
                        Braze.getInstance(context).getCurrentUser()
                                .setCustomUserAttribute(BzEventType.COMPLETED_PURCHASE, maps.toString());
                        present.resRepayData(dataStr);

                    }

                    @Override
                    public void resError(String error) {
                        present.resRepayData(null);

                    }
                });
    }

    /**
     * 扫码支付
     * @param maps
     */
    @Override
    public void reqQrPay(final Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.MainUrl,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("分期付支付参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.qrPay(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("支付:" + dataStr);

                        //提交AF事件(支付完成)
                        maps.put(BzEventParameterName.REVENUE, maps.get("borrowAmount"));
                        maps.put(BzEventParameterName.RECEIPT_ID, maps.get("merchantId"));
                        maps.put("repayType", 1);
//                        AppsFlyerLib.getInstance().logEvent(context, "completed_purchase", maps);
                        Braze.getInstance(context).getCurrentUser()
                                .setCustomUserAttribute(BzEventType.COMPLETED_PURCHASE, maps.toString());
                        present.resQrPayData(dataStr);

                    }

                    @Override
                    public void resError(String error) {
                        present.resQrPayData(null);

                    }
                });
    }

    /**
     * 扫码支付
     * @param maps
     */
    @Override
    public void reqQrPayNew(final Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.MainUrl,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("分期付支付参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.qrPayNew(headerMap, requestBody);
                        requestUtils.Call(call, null, false);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("分期付支付结果:" + dataStr);

                        String borrowCode = "";
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            if (jsonObject.has("flowId")) {
                                borrowCode = jsonObject.getString("flowId");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
                                    .setCustomUserAttribute("completed_purchase", mapp.toString());
                            Map<String, Object> eventValue = new HashMap<String, Object>();
                            eventValue.put(BzEventParameterName.SUCCESS, 1);
//                            AppsFlyerLib.getInstance().logEvent(context, AFInAppEventType.ADD_PAYMENT_INFO, eventValue);
                            Braze.getInstance(context).getCurrentUser()
                                    .setCustomUserAttribute(BzEventType.ADD_PAYMENT_INFO, eventValue.toString());
                        }

                        present.resQrPayData(dataStr);

                    }

                    @Override
                    public void resError(String error) {
                        present.resQrPayData(null);

                    }
                });
    }

    @Override
    public void reqFenQiRepqy(final Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.MainUrl,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("分期付支付参数：" + new Gson().toJson(maps));
                        //Call<ResponseBody> call = requestInterface.fqCreateBorrow(headerMap, requestBody);
                        Call<ResponseBody> call = requestInterface.interestCredistOrder(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("分期付支付:" + dataStr);

//                        //提交AF事件(支付完成)
//                        maps.put(AFInAppEventParameterName.REVENUE, maps.get("borrowAmount"));
//                        maps.put(AFInAppEventParameterName.RECEIPT_ID, maps.get("merchantId"));
//                        maps.put("repayType", 1);
//                        AppsFlyerLib.getInstance().logEvent(context, "completed_purchase", maps, new AppsFlyerRequestListener() {
//                            @Override
//                            public void onSuccess() {
//                                Toast.makeText(context, "AppsFlyer successful", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onError(int i, @NonNull String s) {
//                                Toast.makeText(context, "AppsFlyer failed", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                        present.resFenQiRepayData(dataStr);

                    }

                    @Override
                    public void resError(String error) {
                        present.resFenQiRepayData(null);

                    }
                });
    }

    /**
     * 获取未开通分期付折扣
     *
     * @param maps
     */
    @Override
    public void reqUnOpenFQzhekou(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("分期折扣参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.merchantRate(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("分期折扣:" + dataStr);
                        present.resUnOpenFQzhekou(dataStr);

                    }

                    @Override
                    public void resError(String error) {
                        present.resUnOpenFQzhekou(null);

                    }
                });
    }
}
