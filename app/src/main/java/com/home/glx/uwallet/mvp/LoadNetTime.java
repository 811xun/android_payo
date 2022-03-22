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
 * 获取服务器时间
 */
public class LoadNetTime {

    private Context context;

    public LoadNetTime(Context context) {
        this.context = context;
    }

    public void getTime(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("获取服务器时间参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getTimeMillis(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("获取服务器时间返回:" + dataStr);

                        if (OnResTiem != null) {
                            OnResTiem.netTime(Long.parseLong(dataStr));
                        }

                    }

                    @Override
                    public void resError(String error) {

                    }
                });
    }

    public interface OnResTiem {
        void netTime(long num);
    }

    private OnResTiem OnResTiem;

    public void setOnKycNumer(OnResTiem OnResTiem) {
        this.OnResTiem = OnResTiem;
    }
}
