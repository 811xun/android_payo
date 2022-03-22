package com.home.glx.uwallet.mvp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.home.glx.uwallet.datas.HomeBannerData;
import com.home.glx.uwallet.datas.HomeMerchantListData;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class GetHomeMerchantList {
    /**
     * 单例模式
     */
    public static GetHomeMerchantList getAllMerchantList;

    //首页banner更新时间
    private String bannerTimestamp = "";
    //自定义分类栏更新时间
    private String category1Timestamp = "";
    private String category2Timestamp = "";
    private String category3Timestamp = "";
    private String category4Timestamp = "";
    private String category5Timestamp = "";
    //市场推广栏更新时间
    private String exclusiveTimestamp = "";
    private String bannerId = "";

    private HomeBannerData homeBannerData = new HomeBannerData();
    private List<HomeMerchantListData.ExclusiveBean.ListBean> moreList = new CopyOnWriteArrayList<HomeMerchantListData.ExclusiveBean.ListBean>();
    private HomeMerchantListData.Category1Bean.DataBean merchantList1 = new HomeMerchantListData.Category1Bean.DataBean();
    private HomeMerchantListData.Category2Bean.DataBean merchantList2 = new HomeMerchantListData.Category2Bean.DataBean();
    private HomeMerchantListData.Category3Bean.DataBean merchantList3 = new HomeMerchantListData.Category3Bean.DataBean();
    private HomeMerchantListData.Category4Bean.DataBean merchantList4 = new HomeMerchantListData.Category4Bean.DataBean();
    private HomeMerchantListData.Category5Bean.DataBean merchantList5 = new HomeMerchantListData.Category5Bean.DataBean();


    /**
     * 获得单例对象
     */
    public static GetHomeMerchantList getInstance() {
        if (getAllMerchantList == null) {
            synchronized (GetHomeMerchantList.class) {
                if (getAllMerchantList == null) {
                    getAllMerchantList = new GetHomeMerchantList();
                }
            }
        }
        return getAllMerchantList;
    }

    public void clearCache(Context context) {
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.HOME);
        sharePreferenceUtils.clear();
        //不再清banner缓存
//        setBannerId("");
//        setBannerTimestamp("");
//        setHomeBannerData(new HomeBannerData());
        setMoreList(new ArrayList<HomeMerchantListData.ExclusiveBean.ListBean>());
        setExclusiveTimestamp("");
        setMerchantList1(new HomeMerchantListData.Category1Bean.DataBean());
        setCategory1Timestamp("");
        setMerchantList2(new HomeMerchantListData.Category2Bean.DataBean());
        setCategory2Timestamp("");
        setMerchantList3(new HomeMerchantListData.Category3Bean.DataBean());
        setCategory3Timestamp("");
        setMerchantList4(new HomeMerchantListData.Category4Bean.DataBean());
        setCategory4Timestamp("");
        setMerchantList5(new HomeMerchantListData.Category5Bean.DataBean());
        setCategory5Timestamp("");
        getCache(context);
    }

    // 解析JSON文件
    protected String btJson(Context context) {
        try {
            // 获取json文件数据源,流的方式呈现
            String fileName = "";
            if (StaticParament.server.equals("1")) {
                fileName = "homeDataTest.json";
            } else {
                fileName = "homeDataFinal.json";
            }
            InputStream inputStream = context.getAssets().open(fileName);
            // 读取JSON文件流
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String temp = "";
            StringBuilder jsonSource = new StringBuilder();
            //一行一行的读取
            while ((temp = bufferedReader.readLine()) != null) {
                jsonSource.append(temp);
            }
            //关闭
            bufferedReader.close();
            inputStream.close();
            // JSON解析对象
            JSONObject jsonObject = new JSONObject(jsonSource.toString());
            // 获取JOSN文件当中的数据对象weatherinfo【可知JSON文件数据只有两种，一是对象{}，二是数组[]】
            JSONObject jsonObjectWeatherinfo = jsonObject.getJSONObject("data");
            // JSOn文件某一数据对象的属性，例如weatherinfo对象的属性city
            String result = jsonObject.getString("data");
            L.log("--tttttttttttttt----", result);
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("lhy", "JSONException:" + e.toString());
            return "";
        } catch (Exception e) {
            Log.i("lhy", "Exception:" + e.toString());
            e.printStackTrace();
            return "";
        }
    }

    public void getCache(Context context) {
        Gson gson = new Gson();
//        String dataStr = "";
//        HomeMerchantListData homeMerchantListData = gson.fromJson(dataStr, HomeMerchantListData.class);
        String jsonStr = btJson(context);
        HomeMerchantListData homeMerchantListData = gson.fromJson(jsonStr, HomeMerchantListData.class);
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.HOME);
        String bannerId = (String) sharePreferenceUtils.get(StaticParament.BANNER_ID, "");
        String bannerTime = (String) sharePreferenceUtils.get(StaticParament.BANNER_TIMESTAMP, "");
        String bannerData = (String) sharePreferenceUtils.get(StaticParament.BANNER_DATA, "");
        String moreList = (String) sharePreferenceUtils.get(StaticParament.EXCLUSIVE_LIST, "");
        String time = (String) sharePreferenceUtils.get(StaticParament.EXCLUSIVE_TIMESTAMP, "");
        String cateList1 = (String) sharePreferenceUtils.get(StaticParament.CATEGORY1_LIST, "");
        String time1 = (String) sharePreferenceUtils.get(StaticParament.CATEGORY1_TIMESTAMP, "");
        String cateList2 = (String) sharePreferenceUtils.get(StaticParament.CATEGORY2_LIST, "");
        String time2 = (String) sharePreferenceUtils.get(StaticParament.CATEGORY2_TIMESTAMP, "");
        String cateList3 = (String) sharePreferenceUtils.get(StaticParament.CATEGORY3_LIST, "");
        String time3 = (String) sharePreferenceUtils.get(StaticParament.CATEGORY3_TIMESTAMP, "");
        String cateList4 = (String) sharePreferenceUtils.get(StaticParament.CATEGORY4_LIST, "");
        String time4 = (String) sharePreferenceUtils.get(StaticParament.CATEGORY4_TIMESTAMP, "");
        String cateList5 = (String) sharePreferenceUtils.get(StaticParament.CATEGORY5_LIST, "");
        String time5 = (String) sharePreferenceUtils.get(StaticParament.CATEGORY5_TIMESTAMP, "");
        if (!TextUtils.isEmpty(bannerId)) {
            setBannerId(bannerId);
        }
        if (!TextUtils.isEmpty(bannerTime)) {
            setBannerTimestamp(bannerTime);
        }
        if (!TextUtils.isEmpty(bannerData)) {
            HomeBannerData homeBannerData = gson.fromJson(bannerData, HomeBannerData.class);
            setHomeBannerData(homeBannerData);
        }
        if (!TextUtils.isEmpty(moreList)) {
            List<HomeMerchantListData.ExclusiveBean.ListBean> list = gson.fromJson(moreList, new TypeToken<List<HomeMerchantListData.ExclusiveBean.ListBean>>() {
            }.getType());
            setMoreList(list);
        } else {
            setMoreList(homeMerchantListData.getExclusive().getList());
        }
        if (!TextUtils.isEmpty(time)) {
            setExclusiveTimestamp(time);
        }
        if (!TextUtils.isEmpty(cateList1)) {
            HomeMerchantListData.Category1Bean.DataBean list1 = gson.fromJson(cateList1, HomeMerchantListData.Category1Bean.DataBean.class);
            setMerchantList1(list1);
        } else {
            setMerchantList1(homeMerchantListData.getCategory1().getData());
        }
        if (!TextUtils.isEmpty(time1)) {
            setCategory1Timestamp(time1);
        }
        if (!TextUtils.isEmpty(cateList2)) {
            HomeMerchantListData.Category2Bean.DataBean list2 = gson.fromJson(cateList2, HomeMerchantListData.Category2Bean.DataBean.class);
            setMerchantList2(list2);
        } else {
            setMerchantList2(homeMerchantListData.getCategory2().getData());
        }
        if (!TextUtils.isEmpty(time2)) {
            setCategory2Timestamp(time2);
        }
        if (!TextUtils.isEmpty(cateList3)) {
            HomeMerchantListData.Category3Bean.DataBean list3 = gson.fromJson(cateList3, HomeMerchantListData.Category3Bean.DataBean.class);
            setMerchantList3(list3);
        } else {
            setMerchantList3(homeMerchantListData.getCategory3().getData());
        }
        if (!TextUtils.isEmpty(time3)) {
            setCategory3Timestamp(time3);
        }
        if (!TextUtils.isEmpty(cateList4)) {
            HomeMerchantListData.Category4Bean.DataBean list4 = gson.fromJson(cateList4, HomeMerchantListData.Category4Bean.DataBean.class);
            setMerchantList4(list4);
        } else {
            setMerchantList4(homeMerchantListData.getCategory4().getData());
        }
        if (!TextUtils.isEmpty(time4)) {
            setCategory4Timestamp(time4);
        }
        if (!TextUtils.isEmpty(cateList5)) {
            HomeMerchantListData.Category5Bean.DataBean list5 = gson.fromJson(cateList5, HomeMerchantListData.Category5Bean.DataBean.class);
            setMerchantList5(list5);
        } else {
            setMerchantList5(homeMerchantListData.getCategory5().getData());
        }
        if (!TextUtils.isEmpty(time5)) {
            setCategory5Timestamp(time5);
        }
    }

    public void getAppHomePageBottomData(final Context context, Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("获取首页市场推广、自定义分类数据参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getAppHomePageBottomData(headerMap, requestBody);
                        requestUtils.Call(call, null, false);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("获取首页市场推广、自定义分类数据:" + dataStr);
                        Gson gson = new Gson();
                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.HOME);
                        HomeMerchantListData homeMerchantListData = gson.fromJson(dataStr, HomeMerchantListData.class);
                        List<String> haveChange = new ArrayList<>();
                        if (homeMerchantListData.getCategory1().getUpdateStatus().equals("true")) {
                            setMerchantList1(homeMerchantListData.getCategory1().getData());
                            setCategory1Timestamp(homeMerchantListData.getCategory1().getCategory1Timestamp());
                            String cateList1 = new Gson().toJson(homeMerchantListData.getCategory1().getData());
                            sharePreferenceUtils.put(StaticParament.CATEGORY1_LIST, cateList1);
                            haveChange.add("1");
                        }
                        sharePreferenceUtils.put(StaticParament.CATEGORY1_TIMESTAMP, homeMerchantListData.getCategory1().getCategory1Timestamp());
                        if (homeMerchantListData.getCategory2().getUpdateStatus().equals("true")) {
                            setMerchantList2(homeMerchantListData.getCategory2().getData());
                            setCategory2Timestamp(homeMerchantListData.getCategory2().getCategory2Timestamp());
                            String cateList2 = new Gson().toJson(homeMerchantListData.getCategory2().getData());
                            sharePreferenceUtils.put(StaticParament.CATEGORY2_LIST, cateList2);
                            haveChange.add("2");
                        }
                        sharePreferenceUtils.put(StaticParament.CATEGORY2_TIMESTAMP, homeMerchantListData.getCategory2().getCategory2Timestamp());

                        if (homeMerchantListData.getCategory3().getUpdateStatus().equals("true")) {
                            setMerchantList3(homeMerchantListData.getCategory3().getData());
                            setCategory3Timestamp(homeMerchantListData.getCategory3().getCategory3Timestamp());
                            String cateList3 = new Gson().toJson(homeMerchantListData.getCategory3().getData());
                            sharePreferenceUtils.put(StaticParament.CATEGORY3_LIST, cateList3);
                            haveChange.add("3");
                        }
                        sharePreferenceUtils.put(StaticParament.CATEGORY3_TIMESTAMP, homeMerchantListData.getCategory3().getCategory3Timestamp());

                        if (homeMerchantListData.getCategory4().getUpdateStatus().equals("true")) {
                            setMerchantList4(homeMerchantListData.getCategory4().getData());
                            setCategory4Timestamp(homeMerchantListData.getCategory4().getCategory4Timestamp());
                            String cateList4 = new Gson().toJson(homeMerchantListData.getCategory4().getData());
                            sharePreferenceUtils.put(StaticParament.CATEGORY4_LIST, cateList4);
                            haveChange.add("4");
                        }
                        sharePreferenceUtils.put(StaticParament.CATEGORY4_TIMESTAMP, homeMerchantListData.getCategory4().getCategory4Timestamp());

                        if (homeMerchantListData.getCategory5().getUpdateStatus().equals("true")) {
                            setMerchantList5(homeMerchantListData.getCategory5().getData());
                            setCategory5Timestamp(homeMerchantListData.getCategory5().getCategory5Timestamp());
                            String cateList5 = new Gson().toJson(homeMerchantListData.getCategory5().getData());
                            sharePreferenceUtils.put(StaticParament.CATEGORY5_LIST, cateList5);
                            haveChange.add("5");
                        }
                        sharePreferenceUtils.put(StaticParament.CATEGORY5_TIMESTAMP, homeMerchantListData.getCategory5().getCategory5Timestamp());

                        if (homeMerchantListData.getExclusive().getUpdateStatus().equals("true")) {
                            setMoreList(homeMerchantListData.getExclusive().getList());
                            setExclusiveTimestamp(homeMerchantListData.getExclusive().getExclusiveTimestamp());
                            String cateList = new Gson().toJson(homeMerchantListData.getExclusive().getList());
                            sharePreferenceUtils.put(StaticParament.EXCLUSIVE_LIST, cateList);
                            haveChange.add("6");
                        }
                        sharePreferenceUtils.put(StaticParament.EXCLUSIVE_TIMESTAMP, homeMerchantListData.getExclusive().getExclusiveTimestamp());

                        sharePreferenceUtils.save();
                        callBack.onSuccess(haveChange);
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    public void getAppHomePageTopBanner(final Context context, Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("获取首页banner参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getAppHomePageTopBanner(headerMap, requestBody);
                        requestUtils.Call(call, null, false);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("获取首页banner:" + dataStr);
                        Gson gson = new Gson();
                        JSONObject jsonObject = null;
                        String updateStatus = "";
                        String timestamp = "";
                        String banner = "";
                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.HOME);
                        try {
                            jsonObject = new JSONObject(dataStr);
                            updateStatus = jsonObject.getString("updateStatus");
                            timestamp = jsonObject.getString("timestamp");
                            banner = jsonObject.getString("banner");
                            setBannerTimestamp(timestamp);
                            sharePreferenceUtils.put(StaticParament.BANNER_TIMESTAMP, timestamp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (updateStatus.equals("true")) {
                            sharePreferenceUtils.put(StaticParament.BANNER_DATA, banner);
                            HomeBannerData homeBannerData = gson.fromJson(banner, HomeBannerData.class);
                            setHomeBannerData(homeBannerData);
                            sharePreferenceUtils.put(StaticParament.BANNER_ID, homeBannerData.getId());
                            sharePreferenceUtils.save();
                        }
                        callBack.onSuccess(updateStatus);
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerTimestamp() {
        return bannerTimestamp;
    }

    public void setBannerTimestamp(String bannerTimestamp) {
        this.bannerTimestamp = bannerTimestamp;
    }

    public String getCategory1Timestamp() {
        return category1Timestamp;
    }

    public void setCategory1Timestamp(String category1Timestamp) {
        this.category1Timestamp = category1Timestamp;
    }

    public String getCategory2Timestamp() {
        return category2Timestamp;
    }

    public void setCategory2Timestamp(String category2Timestamp) {
        this.category2Timestamp = category2Timestamp;
    }

    public String getCategory3Timestamp() {
        return category3Timestamp;
    }

    public void setCategory3Timestamp(String category3Timestamp) {
        this.category3Timestamp = category3Timestamp;
    }

    public String getCategory4Timestamp() {
        return category4Timestamp;
    }

    public void setCategory4Timestamp(String category4Timestamp) {
        this.category4Timestamp = category4Timestamp;
    }

    public String getCategory5Timestamp() {
        return category5Timestamp;
    }

    public void setCategory5Timestamp(String category5Timestamp) {
        this.category5Timestamp = category5Timestamp;
    }

    public String getExclusiveTimestamp() {
        return exclusiveTimestamp;
    }

    public void setExclusiveTimestamp(String exclusiveTimestamp) {
        this.exclusiveTimestamp = exclusiveTimestamp;
    }

    public HomeBannerData getHomeBannerData() {
        return homeBannerData;
    }

    public void setHomeBannerData(HomeBannerData homeBannerData) {
        this.homeBannerData = homeBannerData;
        setBannerId(homeBannerData.getId());
    }

    public HomeMerchantListData.Category1Bean.DataBean getMerchantList1() {
        return merchantList1;
    }

    public void setMerchantList1(HomeMerchantListData.Category1Bean.DataBean merchantList1) {
        this.merchantList1 = merchantList1;
    }

    public HomeMerchantListData.Category2Bean.DataBean getMerchantList2() {
        return merchantList2;
    }

    public void setMerchantList2(HomeMerchantListData.Category2Bean.DataBean merchantList2) {
        this.merchantList2 = merchantList2;
    }

    public HomeMerchantListData.Category3Bean.DataBean getMerchantList3() {
        return merchantList3;
    }

    public void setMerchantList3(HomeMerchantListData.Category3Bean.DataBean merchantList3) {
        this.merchantList3 = merchantList3;
    }

    public HomeMerchantListData.Category4Bean.DataBean getMerchantList4() {
        return merchantList4;
    }

    public void setMerchantList4(HomeMerchantListData.Category4Bean.DataBean merchantList4) {
        this.merchantList4 = merchantList4;
    }

    public HomeMerchantListData.Category5Bean.DataBean getMerchantList5() {
        return merchantList5;
    }

    public void setMerchantList5(HomeMerchantListData.Category5Bean.DataBean merchantList5) {
        this.merchantList5 = merchantList5;
    }

    public List<HomeMerchantListData.ExclusiveBean.ListBean> getMoreList() {
        return moreList;
    }

    public void setMoreList(List<HomeMerchantListData.ExclusiveBean.ListBean> moreList) {
        this.moreList = moreList;
    }
}
