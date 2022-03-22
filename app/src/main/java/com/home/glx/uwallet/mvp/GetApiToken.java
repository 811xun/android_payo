package com.home.glx.uwallet.mvp;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class GetApiToken {

    private Context context;

    private String token;

    public GetApiToken(Context context) {
        this.context = context;
    }


    public void getApiToken(final Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("getApiToken：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.getApiToken(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
/*                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataStr);
                    token = jsonObject.getString("apiToken");
                    if (token != null) {
                        onGetApiToken.getToken(token);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                if (!TextUtils.isEmpty(dataStr)) {
                    onGetApiToken.getToken(dataStr);
                }

                L.log("getApiToken:" + dataStr);

            }

            @Override
            public void resError(String error) {
                onGetApiToken.getToken(null);
            }
        });
    }


    public interface OnGetApiToken {
        void getToken(String token);
    }

    private OnGetApiToken onGetApiToken;

    public void setOnGetApiToken(OnGetApiToken onGetApiToken) {
        this.onGetApiToken = onGetApiToken;
    }
}
