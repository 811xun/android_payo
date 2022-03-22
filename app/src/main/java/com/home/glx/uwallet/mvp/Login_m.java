package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.braze.Braze;
import com.google.gson.Gson;
import com.home.glx.uwallet.datas.LoginDatas;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.BzEventType;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class Login_m implements Login_in.Model {

    private Context context;
    private Login_p present;

    public Login_m(Context context, Login_p present) {
        this.context = context;
        this.present = present;
    }

    @Override
    public void reqLogin(final Map<String, Object> maps, final boolean regist) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestData<LoginDatas>() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("登录参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.Login(headerMap, requestBody);
                        requestUtils.Call(call, LoginDatas.class);
                    }

                    @Override
                    public void resData(LoginDatas data, String dataStr, String pc, String code) {
                        L.log("登录:" + dataStr);
                        Gson gson = new Gson();
                        LoginDatas loginDatas = gson.fromJson(dataStr, LoginDatas.class);
                        present.resLoginData(loginDatas, regist);
                        resAFEvent(maps);
                    }


                    @Override
                    public void resError(String error) {
                        present.resLoginData(null, false);
                    }
                });
    }


    /**
     * 提交AF事件
     */
    private void resAFEvent(Map<String, Object> eventValue) {
        L.log("登录AF参数：" + eventValue.toString());
//        AppsFlyerLib.getInstance().logEvent(context, AFInAppEventType.LOGIN, eventValue);

        Braze.getInstance(context).getCurrentUser()
                .setCustomUserAttribute(BzEventType.LOGIN, eventValue.toString());

    }

}
