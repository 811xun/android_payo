package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 理财，分期付协议同意状态
 */
public class AgreementStgatus {

    private Context context;

    public AgreementStgatus(Context context) {
        this.context = context;
    }

    public void loadAgreementStatus(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("协议同意参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getAgreementState(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("协议同意状态返回:" + dataStr);

                        try {
                            JSONObject jsb = new JSONObject(dataStr);
                            String isInvestAgree = jsb.getString("isInvestAgree");
                            String isCreditAgree = jsb.getString("isCreditAgree");

                            String creditAgreement = jsb.getString("creditAgreement");
                            String investAgreement = jsb.getString("investAgreement");
                            //creditAgreement = "http://imagetest-image.loancloud.cn/h5/html/creditDeal.html";
                            //investAgreement = "http://imagetest-image.loancloud.cn/h5/html/investAgreement.html";

                            if (onIllionStatus != null) {
                                onIllionStatus.agreementStatus(isInvestAgree, investAgreement, isCreditAgree, creditAgreement);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void resError(String error) {

                    }
                });
    }


    /**
     * 同意协议
     *
     * @param maps
     */
    public void agreeAgreement(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("协议同意参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.updateAgreementState(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("协议同意返回:" + dataStr);
                        if (onIllionStatus != null) {
                            onIllionStatus.agreeAgreement(dataStr);
                        }

                    }

                    @Override
                    public void resError(String error) {

                    }
                });
    }


    public interface OnAgreementStatus {
        void agreementStatus(String isInvestAgree, String investAgreeUrl, String isCreditAgree, String creditAgreeUrl);

        void agreeAgreement(String status);
    }

    private OnAgreementStatus onIllionStatus;

    public void setOnIllionStatus(OnAgreementStatus onIllionStatus) {
        this.onIllionStatus = onIllionStatus;
    }
}
