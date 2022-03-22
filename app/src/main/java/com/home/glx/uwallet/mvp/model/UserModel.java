package com.home.glx.uwallet.mvp.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.home.glx.uwallet.datas.BankDatas;
import com.home.glx.uwallet.datas.BorrowByInstalmentData;
import com.home.glx.uwallet.datas.BorrowByOrderData;
import com.home.glx.uwallet.datas.BorrowByPayDayV2Data;
import com.home.glx.uwallet.datas.BorrowByPaydayData;
import com.home.glx.uwallet.datas.FeeTransactionData;
import com.home.glx.uwallet.datas.InvitedUserListData;
import com.home.glx.uwallet.datas.MerchantItemData;
import com.home.glx.uwallet.datas.PayAllData;
import com.home.glx.uwallet.datas.PayDayBorrowDetailData;
import com.home.glx.uwallet.datas.PayDayBorrowInfoData;
import com.home.glx.uwallet.datas.PcDatas;
import com.home.glx.uwallet.datas.RecordData;
import com.home.glx.uwallet.datas.TransationDetailData;
import com.home.glx.uwallet.datas.WhetherOpenFenQiDatas;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.RecordInstalmentView;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class UserModel implements UserListener {
    private Context context;

    public UserModel(Context context) {
        this.context = context;
    }
    @Override
    public void getUserSavedAmount(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("查询近6个月省钱金额参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getUserSavedAmount(headerMap, requestBody);
                        requestUtils.Call(call, null, false);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("查询近6个月省钱金额:" + dataStr);
                        JSONObject jsonObject =  null;
                        String savedAmount = "";
                        try {
                            jsonObject = new JSONObject(dataStr);
                            savedAmount = jsonObject.getString("savedAmount");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        callBack.onSuccess(savedAmount);
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void verifyUserPassword(Map<String, Object> maps, final String password, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("验证当前用户 密码是否正确参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.verifyUserPassword(headerMap, password, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("验证当前用户 密码是否正确:" + dataStr);
                        if (dataStr.equals("1")) {
                            callBack.onSuccess();
                        } else {
                            callBack.onError("0");
                        }
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void invitedUserList(Map<String, Object> maps, final ActionCallBack callBack) {

        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.invitedUserList(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, final String code) {
                        L.log("消费人列表：" + dataStr);
                        Gson gson = new Gson();
                        int maxPages = gson.fromJson(pc, PcDatas.class).getMaxPages();
                        List<InvitedUserListData> invitedUserListData = new ArrayList<>();
                        if (!dataStr.equals("null")) {
                            invitedUserListData = gson.fromJson(dataStr, new TypeToken<List<InvitedUserListData>>() {
                            }.getType());
                        }
                        callBack.onSuccess(maxPages, invitedUserListData);
                    }

                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void getPromotionCodeMessage(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("营销码信息查询参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getPromotionCodeMessage(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, final String code) {
                        L.log("营销码信息查询：" + dataStr);
                        Gson gson = new Gson();
                        String amount = "";
                        String message = "";
                        JSONObject jsonObject =  null;
                        try {
                            jsonObject = new JSONObject(dataStr);
                            message = jsonObject.getString("message");
                            amount = jsonObject.getString("amount");
                        } catch (JSONException e) {
                            callBack.onError(message);
                            e.printStackTrace();
                            return;
                        }
                        callBack.onSuccess(amount, message);
                    }

                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void favoriteList(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("用户收藏列表, 分页展示参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.favoriteList(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, final String code) {
                        L.log("用户收藏列表, 分页展示：" + dataStr);
                        Gson gson = new Gson();
                        int maxPages = gson.fromJson(pc, PcDatas.class).getMaxPages();
                        List<MerchantItemData> merchantItemDataList = new ArrayList<>();
                        if (!dataStr.equals("null")) {
                            merchantItemDataList = gson.fromJson(dataStr, new TypeToken<List<MerchantItemData>>() {
                            }.getType());
                        }
                        callBack.onSuccess(maxPages, merchantItemDataList);
                    }

                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void noticeClearAll(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("一键清除所有通知信息参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.noticeClearAll(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, final String code) {
                        L.log("一键清除所有通知信息：" + dataStr);
                        callBack.onSuccess();
                    }

                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void getCardList(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("银行列表参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.getBankCardList(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("银行列表：" + dataStr);
                Gson gson1 = new Gson();
               List<BankDatas> bankList = gson1.fromJson(dataStr, new TypeToken<List<BankDatas>>() {
                }.getType());
               callBack.onSuccess(bankList);
            }


            @Override
            public void resError(String error) {
                callBack.onError(error);
            }
        });
    }

    @Override
    public void queryRepayInfo(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("查询是否有逾期/还款失败订单参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.queryRepayInfo(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("查询是否有逾期/还款失败订单：" + dataStr);
                Gson gson1 = new Gson();
                JSONObject jsonObject = null;
                int overDueOrderCount = 0;
                int repayFailedOrderCount = 0;
                String repayFailedAmount = "";
                try {
                    jsonObject = new JSONObject(dataStr);
                    overDueOrderCount = jsonObject.getInt("overDueOrderCount");
                    repayFailedOrderCount = jsonObject.getInt("repayFailedOrderCount");
                    repayFailedAmount = jsonObject.getString("repayFailedAmount");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callBack.onSuccess(overDueOrderCount, repayFailedOrderCount, repayFailedAmount);
            }


            @Override
            public void resError(String error) {
                callBack.onError(error);
            }
        });
    }

    @Override
    public void getReceived(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.MainUrl, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("查询邀请数据参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.getReceived(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("查询邀请数据：" + dataStr);
                Gson gson1 = new Gson();
                JSONObject jsonObject = null;
                String info = "";
                String money = "";
                try {
                    jsonObject = new JSONObject(dataStr);
                    money = jsonObject.getString("money");
                    info = jsonObject.getString("info");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callBack.onSuccess(money, info);
            }


            @Override
            public void resError(String error) {
                callBack.onError(error);
            }
        });
    }

    @Override
    public void saveReceivedIsShow(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.MainUrl, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("修改查询邀请状态参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.saveReceivedIsShow(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("修改查询邀请状态：" + dataStr);
            }


            @Override
            public void resError(String error) {
            }
        });
    }

    @Override
    public void getRecordNew(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.MainUrl, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("交易详情列表+条件搜索参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.getRecordNew(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("交易详情列表+条件搜索：" + dataStr);
                Gson gson = new Gson();
                if (dataStr.equals("null")) {
                    callBack.onError("null");
                    return;
                }
                int maxPages = gson.fromJson(pc, PcDatas.class).getMaxPages();
                List<RecordData> recordDataList = gson.fromJson(dataStr, new TypeToken<List<RecordData>>() {
                }.getType());
                callBack.onSuccess(maxPages, recordDataList);
            }


            @Override
            public void resError(String error) {
                callBack.onError(error);
            }
        });
    }

    @Override
    public void borrowByPayday(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("还款时间-还款计划列表参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.borrowByPayday(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("还款时间-还款计划列表：" + dataStr);
                Gson gson = new Gson();
                if (dataStr.equals("null")) {
                    callBack.onError("null");
                    return;
                }
                List<BorrowByPaydayData> borrowByPaydayData = gson.fromJson(dataStr, new TypeToken<List<BorrowByPaydayData>>() {
                }.getType());
                callBack.onSuccess(borrowByPaydayData);
            }


            @Override
            public void resError(String error) {
                callBack.onError(error);
            }
        });
    }

    @Override
    public void upcomingBorrowList(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("还款日订单列表参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.upcomingBorrowList(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("还款日订单列表：" + dataStr);
                Gson gson = new Gson();
                if (dataStr.equals("null")) {
                    callBack.onError("null");
                    return;
                }
                int maxPages = gson.fromJson(pc, PcDatas.class).getMaxPages();
                List<BorrowByOrderData> borrowByOrderData = gson.fromJson(dataStr, new TypeToken<List<BorrowByOrderData>>() {
                }.getType());
                callBack.onSuccess(maxPages, borrowByOrderData);
            }


            @Override
            public void resError(String error) {
                callBack.onError(error);
            }
        });
    }

    @Override
    public void payDayBorrowDetail(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("还款日订单列表参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.payDayBorrowDetail(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("还款日订单列表：" + dataStr);
                Gson gson = new Gson();
                if (dataStr.equals("null")) {
                    callBack.onError("null");
                    return;
                }
                String borrowList = "";
                String repayInfo = "";
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataStr);
                    borrowList = jsonObject.getString("borrowList");
                    repayInfo = jsonObject.getString("repayInfo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<PayDayBorrowDetailData> borrowByOrderData = gson.fromJson(borrowList, new TypeToken<List<PayDayBorrowDetailData>>() {
                }.getType());
                PayDayBorrowInfoData borrowInfoData = gson.fromJson(repayInfo, PayDayBorrowInfoData.class);
                callBack.onSuccess(borrowInfoData, borrowByOrderData);
            }


            @Override
            public void resError(String error) {
                callBack.onError(error);
            }
        });
    }

    @Override
    public void repaymentV2(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("批量还款参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.repaymentV2(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("批量还款:" + dataStr);
                        callBack.onSuccess(dataStr);

                    }

                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void repayTotalInfo(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("Pay ALL 按钮展示详情信息参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.repayTotalInfo(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("Pay ALL 按钮展示详情信息:" + dataStr);
                        Gson gson = new Gson();
                        PayAllData data = gson.fromJson(dataStr, PayAllData.class);
                        callBack.onSuccess(data);
                    }

                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void selectAccountUser(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.MainUrl,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("账户信息查询参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.repayTotalInfo(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("账户信息查询:" + dataStr);
                        Gson gson = new Gson();
                        PayAllData data = gson.fromJson(dataStr, PayAllData.class);
                        callBack.onSuccess(data);
                    }

                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void verifyCode(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.MainUrl,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("app校验验证码是否正确参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.verifyCode(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("app校验验证码是否正确:" + dataStr);
                        Gson gson = new Gson();
                        callBack.onSuccess();
                    }

                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void borrowByInstalment(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("还款时间-还款计划列表参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.borrowByPayday(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("还款时间-还款计划列表：" + dataStr);
                Gson gson = new Gson();
                if (dataStr.equals("null")) {
                    callBack.onError("null");
                    return;
                }
                List<BorrowByInstalmentData> borrowByInstalmentData = gson.fromJson(dataStr, new TypeToken<List<BorrowByInstalmentData>>() {
                }.getType());
                callBack.onSuccess(borrowByInstalmentData);
            }


            @Override
            public void resError(String error) {
                callBack.onError(error);
            }
        });
    }

    @Override
    public void borrowByPaydayV2(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU, new RequestUtils.RequestDataStr() {
            private String list;

            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("还款时间-还款计划列表参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.borrowByPaydayV2(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("还款时间-还款计划列表：" + dataStr);
                Gson gson = new Gson();
                if (dataStr.equals("null")) {
                    callBack.onError("null");
                    return;
                }
                JSONObject jsonObject = null;
                String list = "";
                String totalAmount = "";
                try {
                    jsonObject = new JSONObject(dataStr);
                    list = jsonObject.getString("list");
                    totalAmount = jsonObject.getString("totalAmount");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<BorrowByPayDayV2Data> borrowByInstalmentData = gson.fromJson(list, new TypeToken<List<BorrowByPayDayV2Data>>() {
                }.getType());
                callBack.onSuccess(borrowByInstalmentData, totalAmount);
            }


            @Override
            public void resError(String error) {
                callBack.onError(error);
            }
        });
    }

    @Override
    public void userVerify(Map<String, Object> maps, final ActionCallBack callBack) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("是否开通分期付参数：" + new Gson().toJson(maps));

                        Call<ResponseBody> call = requestInterface.openFenQiFu(headerMap, requestBody);
                        requestUtils.Call(call, null, false);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("是否开通分期付:" + dataStr);
                        if (dataStr != null) {
                            Gson gson = new Gson();
                            WhetherOpenFenQiDatas whetherOpenFenQiDatas = gson.fromJson(dataStr, WhetherOpenFenQiDatas.class);
                            callBack.onSuccess(whetherOpenFenQiDatas);
                        } else {
                            callBack.onError(null);
                        }
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }

    @Override
    public void getTransactionRecordById(Map<String, Object> maps, final ActionCallBack callBack) {

        RequestUtils requestUtils1 = new RequestUtils(context, maps, StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("查询卡支付/分期付交易明细参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getTransactionRecordById(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("查询卡支付/分期付交易明细:" + dataStr);
                        Gson gson = new Gson();
                        FeeTransactionData data = gson.fromJson(dataStr, FeeTransactionData.class);
                        callBack.onSuccess(data);
                    }


                    @Override
                    public void resError(String error) {
                        callBack.onError(error);
                    }
                });
    }
}
