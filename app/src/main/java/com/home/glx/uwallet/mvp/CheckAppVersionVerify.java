package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class CheckAppVersionVerify {
    private Context context;

    public CheckAppVersionVerify(Context context) {
        this.context = context;
    }
    public void appVersionVerify(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("检查更新参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.appVersionVerifyV2(headerMap, requestBody);
                        requestUtils.Call(call, null, false);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("检查更新:" + dataStr);
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            String needUpdate = jsonObject.getString("needUpdate");
                            //是否需要强制更新, 0:非强制更新,1:强制更新，2:不更新
                            onCheckVersion.getFlag(needUpdate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void resError(String error) {
                        onCheckVersion.getFlag(null);
                    }
                });
    }

    public onCheckVersion onCheckVersion;
    public interface onCheckVersion {
        void getFlag(String flag);
    }
    public void setOnCheckVersion(onCheckVersion onCheckVersion) {
        this.onCheckVersion = onCheckVersion;
    }
}
