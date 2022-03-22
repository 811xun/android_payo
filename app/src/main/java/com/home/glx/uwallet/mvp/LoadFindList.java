package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.home.glx.uwallet.datas.PcDatas;
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

public class LoadFindList {

    private Context context;

    public LoadFindList(Context context) {
        this.context = context;
    }

    public void getFindList(final Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("发现列表参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.getMerchantLocationList(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("发现列表:" + dataStr);
                Gson gson = new Gson();
                int maxPages = 1;
                onFindList.findList(dataStr,maxPages);
            }

            @Override
            public void resError(String error) {

            }
        });
    }


    public interface OnFindList {
        void findList(String dataStr, int maxPages);
    }

    private OnFindList onFindList;

    public void setOnFindList(OnFindList onFindList) {
        this.onFindList = onFindList;
    }
}
