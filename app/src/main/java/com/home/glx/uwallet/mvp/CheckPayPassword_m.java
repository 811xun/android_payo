package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class CheckPayPassword_m implements CheckPayPassword_in.Model {

    private Context context;
    private CheckPayPassword_p present;

    public CheckPayPassword_m(Context context, CheckPayPassword_p present) {
        this.context = context;
        this.present = present;
    }

    @Override
    public void checkPayPassword(Map<String, Object> maps) {
        final String payPassword = (String) maps.get("payPassword");
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.MainUrl,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("校验PIN参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.checkPinNumber(headerMap, id, payPassword, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("校验PIN:" + dataStr);
                        if (code.equals("100200")) {
                            present.resPasswordSrarus("1");
                        } else {
                            present.resPasswordSrarus(null);
                        }
                    }

                    @Override
                    public void resError(String error) {
                        L.log("校验PIN:" + error);
                        present.resPasswordSrarus(null);
                    }
                });
    }

//    @Override
//    public void checkPayPassword(Map<String, Object> maps) {
//        RequestUtils requestUtils1 = new RequestUtils(context, maps,
//                new RequestUtils.RequestDataStr() {
//                    @Override
//                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
//                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {
//
//                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
//                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
//
//                        RequestBody requestBody =
//                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
//
//                        L.log("参数：" + new Gson().toJson(maps));
//                        Call<ResponseBody> call = requestInterface.checkPayPassword(headerMap, requestBody);
//                        requestUtils.Call(call);
//                    }
//
//                    @Override
//                    public void resData(String dataStr, String pc, String code) {
//                        L.log("校验支付密码:" + dataStr);
//                        present.resPasswordSrarus(dataStr);
//                    }
//
//
//                    @Override
//                    public void resError(String error) {
//                        present.resPasswordSrarus(null);
//                    }
//                });
//    }
}
