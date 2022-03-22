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

public class GetUserAllMsg_m implements GetUserAllMsg_in.Model {

    private Context context;
    private GetUserAllMsg_p present;

    public GetUserAllMsg_m(Context context, GetUserAllMsg_p present) {
        this.context = context;
        this.present = present;
    }

    @Override
    public void getUserAllMsg(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("用户信息参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.findUserInfoId(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("用户信息:" + dataStr);
                present.resUserAllMsg(dataStr);
            }


            @Override
            public void resError(String error) {
                present.resUserAllMsg(null);
            }
        });
    }
}
