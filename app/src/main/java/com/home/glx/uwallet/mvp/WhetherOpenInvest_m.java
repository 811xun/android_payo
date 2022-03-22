package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.home.glx.uwallet.datas.UserMsgDatas;
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

public class WhetherOpenInvest_m implements WhetherOpenInvest_in.Model {

    private Context context;
    private WhetherOpenInvest_p present;

    public WhetherOpenInvest_m(Context context, WhetherOpenInvest_p present) {
        this.context = context;
        this.present = present;
    }

    @Override
    public void getStatus(final String reqCode, Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestData<UserMsgDatas>() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("状态参数：" + new Gson().toJson(maps));

                        Call<ResponseBody> call = requestInterface.OpenInvestStatus(headerMap, id, requestBody);
                        requestUtils.Call(call, UserMsgDatas.class, true);
                    }

                    @Override
                    public void resData(UserMsgDatas data, String dataStr, String pc, String code) {
                        L.log("是否开通理财:" + dataStr);
                        present.resOpenInvestStatus(reqCode, code, data);

                    }

                    @Override
                    public void resError(String error) {
                        present.resOpenInvestStatus(reqCode, null, null);
                    }
                });
    }
}
