package com.home.glx.uwallet.mvp;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.home.glx.uwallet.activity.MessageActivity;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 获取是否有未读消息
 */
public class LoadNotReadMessage {

    private Context context;

    public LoadNotReadMessage(Context context) {
        this.context = context;
    }

    /**
     * 未读消息数量
     */
    public void loadData() {
        Map<String, Object> maps = new HashMap<>();

        RequestUtils requestUtils1 = new RequestUtils(context, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("消息已读参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.allNoticeHasRead(headerMap, id, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("消息已读:" + dataStr);
                if (onNotRead != null) {
                    if (dataStr != null && dataStr.equals("true")) {
                        onNotRead.notReadCount(1);
                    } else {
                        onNotRead.notReadCount(0);
                    }
                }
            }

            @Override
            public void resError(String error) {

            }
        });
    }

    /**
     * 未读消息数量
     */
    public void getAllNoticeHasRead() {
        Map<String, Object> maps = new HashMap<>();

        RequestUtils requestUtils1 = new RequestUtils(context, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("消息已读参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.getAllNoticeHasRead(headerMap, id, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("消息已读:" + dataStr);
                String isNeedRead = "";
                int notReadNum = 0;
                try {
                    JSONObject jsonObject =new JSONObject(dataStr);
                    isNeedRead = jsonObject.getString("isNeedRead");
                    notReadNum = jsonObject.getInt("notReadNum");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(isNeedRead)) {
                    if (isNeedRead.equals("true")) {
                        onNotRead.notReadCount(notReadNum);
                    } else {
                        onNotRead.notReadCount(0);
                    }
                }
            }

            @Override
            public void resError(String error) {

            }
        });
    }

    public interface OnNotRead {
        void notReadCount(int num);
    }

    private OnNotRead onNotRead;

    public void setOnNotRead(OnNotRead onNotRead) {
        this.onNotRead = onNotRead;
    }
}
