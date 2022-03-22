package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ScanCodeData_m implements ScanCodeData_in.Model {

    private Context context;
    private ScanCodeData_p present;

    public ScanCodeData_m(Context context, ScanCodeData_p present) {
        this.context = context;
        this.present = present;
    }

    @Override
    public void getImgCodeData(final String code) {
        Map<String, Object> maps = new HashMap<>();
        maps.put("code", code);
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("获取二维码信息参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.findUserInfoByQRCode(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("获取二维码信息:" + dataStr);
                        present.resImgCodeData(dataStr);

                    }

                    @Override
                    public void resError(String error) {
                        present.resImgCodeData(null);

                    }
                });
    }

    @Override
    public void getNfcCodeData(String code) {
        Map<String, Object> maps = new HashMap<>();
        maps.put("code", code);
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("获取NFC信息参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.findUserInfoByNFCCode(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("获取NFC信息:" + dataStr);
                        present.resImgCodeData(dataStr);

                    }

                    @Override
                    public void resError(String error) {
                        present.resImgCodeData(null);

                    }
                });
    }
}
