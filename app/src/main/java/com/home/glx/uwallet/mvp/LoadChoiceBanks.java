package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.home.glx.uwallet.datas.ChoiceBanksData;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 获取推荐银行
 */
public class LoadChoiceBanks {

    private Context context;

    public LoadChoiceBanks(Context context) {
        this.context = context;
    }


    public void getChoiceBanks(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("推荐银行列表参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getBankLogo(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("推荐银行列表:" + dataStr);
                        Gson gson = new Gson();
//                        ChoiceBanksData choiceBanksData = gson.fromJson(dataStr, ChoiceBanksData.class);
                        List<ChoiceBanksData> listData = gson.fromJson(dataStr, new TypeToken<List<ChoiceBanksData>>() {
                        }.getType());
                        if (onBanks != null) {
                            onBanks.allBanks(listData);
                        }
                    }

                    @Override
                    public void resError(String error) {

                    }
                });
    }

    public interface OnBanks {
        void allBanks(List<ChoiceBanksData> bankList);
    }

    private OnBanks onBanks;

    public void setOnFindList(OnBanks onBanks) {
        this.onBanks = onBanks;
    }

}
