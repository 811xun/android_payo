package com.home.glx.uwallet.mvp;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.home.glx.uwallet.datas.MerchantInfoData;
import com.home.glx.uwallet.datas.PcDatas;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ReqAllMerchant {
    private Context context;
    private List<MerchantInfoData> listData = new ArrayList<>();

    public ReqAllMerchant(Context context) {
        this.context = context;
    }

    public void reqAllMerchant(final int page, int maxPages, final String searchKeyword, String orderType) {
        if (page != 1 && page > maxPages) {
            return;
        }

        final SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.APP);
        Map<String, Object> maps = new HashMap<>();
        if (!TextUtils.isEmpty(searchKeyword)) {
            maps.put("searchKeyword", searchKeyword);
        }
        if (orderType.equals("21")) {
            maps.put("orderType", "2");
            maps.put("tagAdd", "1");
        } else if (orderType.equals("20")) {
            maps.put("orderType", "2");
            maps.put("tagAdd", "0");
        } else {
            maps.put("orderType", orderType);
        }

        maps.put("s", 10);
        maps.put("p", page);
        maps.put("lat", appMsgSharePreferenceUtils.get(StaticParament.LAT, ""));
        maps.put("lng", appMsgSharePreferenceUtils.get(StaticParament.LNG, ""));
        maps.put("no_user_id", 1);
        if (orderType.equals("1") && (maps.get("lat").equals("") ||  maps.get("lng").equals(""))) {
            return;
        }
        if (orderType.equals("2") && maps.get("searchKeyword").equals("")) {
            return;
        }
        //maps.put("scs", "created_date(desc)");

        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("商户列表,折扣排序,带搜索参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.merchantSearchList(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, final String code) {
                        L.log("商户列表,折扣排序,带搜索：" + dataStr);
                        Gson gson = new Gson();
                        int maxPage = gson.fromJson(pc, PcDatas.class).getMaxPages();
                        if (page == 1) {
                            listData.clear();
                        }

                        if (!TextUtils.isEmpty(dataStr) && !dataStr.equals("null")) {
                            List<MerchantInfoData> list= gson.fromJson(dataStr, new TypeToken<List<MerchantInfoData>>() {
                            }.getType());
                            listData.addAll(list);
                            String lat = (String) appMsgSharePreferenceUtils.get(StaticParament.LAT, "");
                            String lng = (String) appMsgSharePreferenceUtils.get(StaticParament.LNG, "");
                            if (TextUtils.isEmpty(lat) && TextUtils.isEmpty(lng)) {
                                onGetAllMerchant.getAllMerchant(listData, maxPage, "0", searchKeyword);
                            } else {
                                onGetAllMerchant.getAllMerchant(listData, maxPage, "1", searchKeyword);
                            }
                        }
                    }

                    @Override
                    public void resError(String error) {
                    }
                });
    }

    public OnGetAllMerchant onGetAllMerchant;

    public interface OnGetAllMerchant{
        //flag: 0-按折扣排序 1-按距离排序
        void getAllMerchant(List<MerchantInfoData> listData, int maxPage, String falg, String searchKeyword);
    }

    public void setOnGetAllMerchant(OnGetAllMerchant onGetAllMerchant) {
        this.onGetAllMerchant = onGetAllMerchant;
    }
}
