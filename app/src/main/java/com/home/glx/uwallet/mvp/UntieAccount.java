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
 * 解绑个人银行账户
 */
public class UntieAccount {

    private Context context;

    public UntieAccount(Context context) {
        this.context = context;
    }

    public void getStatus(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("解绑账户参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.cardUnbundling(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("解绑账户:" + dataStr);
                        //解绑状态
                        // 0:解绑中 1：理财解绑失败 2： 分期付解绑失败
                        // 3：账户解绑失败 4：账户解绑成功 5：理财、分期付解绑失败 6：三方解绑失败
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            String status = jsonObject.getString("state");
                            if (onUntieAccount != null) {
                                onUntieAccount.status(status);
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

    public interface OnUntieAccount {
        void status(String status);
    }

    public OnUntieAccount onUntieAccount;

    public void setOnUntieAccount(OnUntieAccount onUntieAccount) {
        this.onUntieAccount = onUntieAccount;
    }


}
