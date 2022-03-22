package com.home.glx.uwallet.mvp.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.home.glx.uwallet.datas.IllionListData;
import com.home.glx.uwallet.datas.IllionMfaData;
import com.home.glx.uwallet.datas.IllionPreLoadData;
import com.home.glx.uwallet.datas.PcDatas;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.KycAndIllionListener;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class KycAndIllionModel implements KycAndIllionListener {

    private Context context;
    public KycAndIllionModel(Context context) {
        this.context = context;
    }

    @Override
    public void setDefaultCard(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("卡列表页面,设置默认银行卡参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.presetCard(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("卡列表页面,设置默认银行卡:" + dataStr);
                        callBack.onSuccess(dataStr);
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void getInstitutions(final Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("获取illion机构列表参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getInstitutions(headerMap, requestBody);
                        if (maps.get("data").toString().contains("isAll")) {
                            requestUtils.Call(call, null, false);
                        } else {
                            requestUtils.Call(call);
                        }

                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("获取illion机构列表:" + dataStr);
                        Gson gson = new Gson();
                        int maxPages = 0;
                        if (maps.get("isAll") == null) {
                            maxPages = gson.fromJson(pc, PcDatas.class).getMaxPages();
                        }
                        
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            String ll = jsonObject.getString("Institutions");
                            List<IllionListData> list = gson.fromJson(ll, new TypeToken<List<IllionListData>>() {
                            }.getType());
                            callBack.onSuccess(list, maxPages);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void getKycResult(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("KYC新风控接口参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.riskCheckNew(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("KYC新风控接口:" + dataStr);
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            int status = jsonObject.getInt("status");
                            //返回状态(0,”请求成功”),(1,”系统错误”),(2,”通用失败”),
                            // (3,”仅剩一次尝试次数”),(4,”错误次数达到上限”),(5,”人工审核”);
                            String phone = jsonObject.getString("phone");
                            callBack.onSuccess(status, phone);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void preLoadIllion(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("预加载机构信息参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.preload(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("预加载机构信息:" + dataStr);
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            String userToken = jsonObject.getString("userToken");
                            String img = jsonObject.getString("img");
                            String name = jsonObject.getString("name");
                            String slug = jsonObject.getString("slug");
                            String credentials = jsonObject.getString("credentials");
                            List<IllionPreLoadData> data = gson.fromJson(credentials, new TypeToken<List<IllionPreLoadData>>() {
                            }.getType());

                            callBack.onSuccess(data, img, name, slug, userToken);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void fetchIllion(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("illion获取报告参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.fetchAll(headerMap, requestBody);
                        requestUtils.Call(call,null,false);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("illion获取报告:" + dataStr);
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            String result = jsonObject.getString("result");
                            String title = "";
                            String userToken = "";
                            List<IllionMfaData> data = new ArrayList<>();
                            if (result.equals("false")) {
                                String mfa = jsonObject.getString("mfa");
                                JSONObject jsonObject1 = new JSONObject(mfa);
                                title = jsonObject1.getString("title");
                                userToken =jsonObject1.getString("userToken");
                                String fields = jsonObject1.getString("fields");
                                data = gson.fromJson(fields, new TypeToken<List<IllionMfaData>>() {
                                }.getType());
                            }
                            callBack.onSuccess(result, title, data, userToken);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void mfaSubmit(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("illion-mfa二次验证提交参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.mfaSubmit(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("illion-mfa二次验证提交:" + dataStr);
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            String result = jsonObject.getString("result");
                            callBack.onSuccess(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void sendIllionMessage(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("获取报告失败发送短信给运营:" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.sendIllionMessage(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("获取报告失败发送短信给运营:" + dataStr);

                    }


                    @Override
                    public void resError(String error) {
                    }
                });
    }

    @Override
    public void saveUserToKycPageLog(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("进入kyc页面次数接口参数:" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.saveUserToKycPageLog(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("进入kyc页面次数接口:" + dataStr);

                    }


                    @Override
                    public void resError(String error) {
                    }
                });
    }
}
