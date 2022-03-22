package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class JiHuoFenQi_m implements JiHuoFenQi_in.Model {

    private Context context;
    private JiHuoFenQi_p present;

    public JiHuoFenQi_m(Context context, JiHuoFenQi_p present) {
        this.context = context;
        this.present = present;
    }

    @Override
    public void reqJiHuo(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.activateOrCloseMember(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("激活分期付:" + dataStr);
                        present.resJiHuoStatus(dataStr);
                    }


                    @Override
                    public void resError(String error) {
                        present.resJiHuoStatus(null);
                    }
                });
    }

    @Override
    public void getNoLoginFenQiLimit(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.maxCreditAmount(headerMap);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("用户分期信息:" + dataStr);
                        JSONObject jsonObject = null;
                        String limit = null;
                        try {
                            jsonObject = new JSONObject(dataStr);
                            limit = jsonObject.getString("maxCredit");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        present.resNoLoginFenQiLimit(limit);
                    }

                    @Override
                    public void resError(String error) {
                        present.resFenQiMsg(null);
                    }
                });
    }

    @Override
    public void getFenQiLimit(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.userCreditMessage(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("用户分期信息:" + dataStr);
                        JSONObject jsonObject = null;
                        String limit = null;
                        String state = null;
                        try {
                            jsonObject = new JSONObject(dataStr);
                            state = jsonObject.getString("maxState");
                            if (state.equals("1")) {
                                limit = jsonObject.getString("maxCredit");
                                present.resNoLoginFenQiLimit(limit);
                            } else {
                                limit = jsonObject.getString("creditAmount");
                                present.resFenQiLimit(limit);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void resError(String error) {
                        present.resFenQiMsg(null);
                    }
                });
    }

    @Override
    public void getFenQiMsg(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.userCreditMessage(headerMap, requestBody);
                        requestUtils.Call(call, null, false);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("用户分期信息:" + dataStr);
                        present.resFenQiMsg(dataStr);
                    }

                    @Override
                    public void resError(String error) {
                        present.resFenQiMsg(null);
                    }
                });
    }

    @Override
    public void initFenQi(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, /*StaticParament.FENQIFU*/StaticParament.MainUrl,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("参数：" + new Gson().toJson(maps));
                        //Call<ResponseBody> call = requestInterface.bankcardBinding(headerMap, requestBody);
                        Call<ResponseBody> call = requestInterface.creditTieOnCard(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("初始化分期付:" + dataStr);
                        present.resFenQi(dataStr);
                    }


                    @Override
                    public void resError(String error) {
                        present.resFenQi(null);
                    }
                });
    }

    /**
     * 分期付跑风控
     *
     * @param maps
     */
    @Override
    public void activationInstallment(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("分期付跑风控参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.activationInstallment(headerMap, requestBody);
                        requestUtils.Call(call, null, false);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("分期付跑风控:" + dataStr);
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            String state = jsonObject.getString("state");
                            String rejectMessage = jsonObject.getString("rejectMessage");
                            //-1 失败, 0 复审, 1 成功
                            present.resActivationInstallment(state, rejectMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void resError(String error) {
                        present.resActivationInstallment(null, null);
                    }
                });
    }

    @Override
    public void bankStatementsIsResult(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("查看illion报告是否返回参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.bankStatementsIsResult(headerMap, id, requestBody);
                        requestUtils.Call(call, null, false);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("查看illion报告是否返回:" + dataStr);
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            String state = jsonObject.getString("state");
                            //1：illion报告成功
                            present.resBankStatementsIsResult(state);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                    @Override
                    public void resError(String error) {
                        present.resBankStatementsIsResult(null);
                    }
                });
    }
}
