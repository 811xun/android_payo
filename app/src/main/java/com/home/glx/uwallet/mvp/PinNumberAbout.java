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

/**
 * pin查询修改校验
 */
public class PinNumberAbout {

    private Context context;
    private PinNumber_in pinNumber_in;

    public PinNumberAbout(Context context, PinNumber_in pinNumber_in) {
        this.context = context;
        this.pinNumber_in = pinNumber_in;
    }

    public void selectPin(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.MainUrl,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("查询PIN参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.queryPinNumber(headerMap, id, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("查询PIN:" + dataStr);
                        pinNumber_in.selectPin(dataStr);
                    }


                    @Override
                    public void resError(String error) {


                    }
                });
    }


    /**
     * 修改PIN
     *
     * @param maps
     */
    public void changePin(Map<String, Object> maps, final String pinNumber) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.MainUrl,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("修改PIN参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.updatePinNumber(headerMap, id, pinNumber, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("修改PIN:" + dataStr);
                        if (code.equals("100200")) {
                            pinNumber_in.changePin("1");
                        }
                    }

                    @Override
                    public void resError(String error) {

                    }
                });
    }


    /**
     * 校验PIN
     *
     * @param maps
     */
    public void checkPin(Map<String, Object> maps, final String pinNumber) {
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
                        Call<ResponseBody> call = requestInterface.checkPinNumber(headerMap, id, pinNumber, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("校验PIN:" + dataStr);
                        if (code.equals("100200")) {
                            pinNumber_in.checkPin("1");
                        } else {
                            pinNumber_in.checkPin("0");
                        }
                    }

                    @Override
                    public void resError(String error) {
                        L.log("校验PIN:" + error);
                        if (error.equals("pinNumber错误") || error.equals("Wrong Pin Number")) {
                            pinNumber_in.checkPin("0");
                        } else {
                            pinNumber_in.checkPin("2");
                        }
                    }
                });
    }

}
