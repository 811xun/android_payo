package com.home.glx.uwallet.request;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.home.glx.uwallet.BaseActivity;
import com.home.glx.uwallet.BuildConfig;
import com.home.glx.uwallet.MyApplication;
import com.home.glx.uwallet.activity.ChangeEmailActivity;
import com.home.glx.uwallet.activity.ChangePhoneNumActivity;
import com.home.glx.uwallet.activity.ConfirmPayActivity;
import com.home.glx.uwallet.activity.ConfirmPayNextActivity;
import com.home.glx.uwallet.activity.InputPin_Activity;
import com.home.glx.uwallet.activity.InstalmentPendingNextActivity;
import com.home.glx.uwallet.activity.PayFailedActivity;
import com.home.glx.uwallet.activity.SetNewPinActivity;
import com.home.glx.uwallet.activity.StartActivity;
import com.home.glx.uwallet.activity.xzc.ChooseCardListActivity;
import com.home.glx.uwallet.activity.xzc.LoginActivity_inputNumber;
import com.home.glx.uwallet.activity.xzc.NewAddBankCardActivityDialog;
import com.home.glx.uwallet.activity.xzc.promotion.PromotionListActivity;
import com.home.glx.uwallet.datas.MainDatas;
import com.home.glx.uwallet.selfview.EnterPayPinDialog;
import com.home.glx.uwallet.selfview.LoadingDialog;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.MD5Utils;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 发送网络请求
 */
public class RequestUtils<T> {

    private Context context;
    private String reqUrl = "";
    private int times = 60;
    private Map<String, Object> maps;

    public RequestUtils() {
    }

    public RequestUtils(Context context) {
        this.context = context;
    }

    public RequestUtils(Context context, Map<String, Object> maps, RequestData requestData) {
        this.context = context;
        this.maps = maps;
        this.requestData = requestData;
        this.reqUrl = StaticParament.MainUrl;
        getRequestInterface(maps);
    }

    public RequestUtils(Context context, Map<String, Object> maps, RequestDataStr requestData) {
        this.context = context;
        this.maps = maps;
        this.requestDataStr = requestData;
        this.reqUrl = StaticParament.MainUrl;
        getRequestInterface(maps);
    }

    public RequestUtils(Context context, Map<String, Object> maps, String reqUrl, RequestData requestData) {
        this.context = context;
        this.maps = maps;
        this.requestData = requestData;
        this.reqUrl = reqUrl;
        getRequestInterface(maps);
    }

    public RequestUtils(Context context, Map<String, Object> maps, String reqUrl, RequestDataStr requestData) {
        this.context = context;
        this.maps = maps;
        this.requestDataStr = requestData;
        this.reqUrl = reqUrl;
        getRequestInterface(maps);
    }

    public RequestUtils(Context context, Map<String, Object> maps, String reqUrl, RequestDataStr requestData, int times) {
        this.context = context;
        this.maps = maps;
        this.requestDataStr = requestData;
        this.reqUrl = reqUrl;
        this.times = times;
        getRequestInterface(maps);
    }

    public RequestUtils(Context context, Map<String, Object> maps, String reqUrl, RequestDataStrMsg requestData) {
        this.context = context;
        this.maps = maps;
        this.requestDataStrMsg = requestData;
        this.reqUrl = reqUrl;
        getRequestInterface(maps);
    }

    private String getTime() {
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dff.setTimeZone(TimeZone.getTimeZone("GMT+10"));
        String ee = dff.format(new Date());
        L.log("时间1：" + ee);
        try {
            ee = PublicTools.dateToStamp5(ee);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ee;
    }

    /*
     * 将时间转换为时间戳
     * 2020-02-26 11:04:48
     */
    public static String dateToStamp5(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    private String getLangusge() {
        /*SharePreferenceUtils sharePreferenceUtils =
                new SharePreferenceUtils(MyApplication.context, StaticParament.APP);
        if (sharePreferenceUtils.get(StaticParament.LANGUAGE, "").equals("0")) {
            return "en";
        } else if (sharePreferenceUtils.get(StaticParament.LANGUAGE, "").equals("1")) {
            return "zh";
        } else {
            if (SystemUtils.getSysLangusge(MyApplication.context).equals("zh")) {
                return "zh";
            }
        }*/
        return "en";
    }

    public RequestInterface getRequestInterface(Map<String, Object> maps) {
        Map<String, Object> RMap = new HashMap<>();
        String time = (System.currentTimeMillis() + MyApplication.differenceTime) + "";
        String sign = MD5Utils.MD5(StaticParament.apiKey + time, 32);
        String lang = getLangusge();

        Map<String, String> headerMap = new HashMap<>();
        String token = "";
        if (context != null) {
            SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
            token = (String) sharePreferenceUtils.get(StaticParament.USER_TOKEN, "");
            String u_id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
            boolean containsUserKey = maps.containsKey("userId");
            boolean no_user_id = maps.containsKey("no_user_id");
            if (!containsUserKey && !no_user_id) {
                //map中不包含userId
                maps.put("userId", u_id);
            }

            if (no_user_id) {
                maps.remove("no_user_id");
            }

            boolean no_language = maps.containsKey("no_language");

            if (!no_language) {
                //map中不包含language
                if (lang.equals("zh")) {
                    //语言 0中文  1英文
                    maps.put("lang", 0);
                    headerMap.put(StaticParament.Language, "zh-CN");
                } else {
                    //语言 0中文  1英文
                    maps.put("lang", 1);
                    headerMap.put(StaticParament.Language, "en-US");
                }
            }

            headerMap.put("Authorization", token);

            //versionId    当前版本Id
            //appType    软件类型, 1:payo, 2:uBiz
            //storeType  商店类型, 1:苹果, 2:谷歌, 3:华为
            //versionNo 当前版本号
            headerMap.put("versionId", BuildConfig.currentVersionId);
            headerMap.put("appType", "1");
            if (BuildConfig.marketType == 0) {
                headerMap.put("storeType", "2");
            } else {
                headerMap.put("storeType", "3");
            }
            headerMap.put("versionNo", BuildConfig.VERSION_NAME);

            headerMap.put("isSplitAdapted", "true");

            //支付接口请求头中带token
            boolean containsKey = maps.containsKey("apiToken");
            if (containsKey) {
                headerMap.put("apiToken", (String) maps.get("apiToken"));
                maps.remove("apiToken");
            }
//            if (SystemUtils.getSysLangusge2(context).equals("zh")) {
//                headerMap.put(StaticParament.Language, "zh-CN");
//            } else {
//                headerMap.put(StaticParament.Language, "en-US");
//            }

            L.log("请求头：" + headerMap.toString());

            RMap.put("sign", sign);
            RMap.put("timestamp", time);
            RMap.put("data", maps);

        } else {
            Log.i("request", "context is null");
        }


        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient client;

        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                L.log("请求地址=" + request.url().toString());
                return chain.proceed(request);
            }
        };

        if (times == 60) {
            client = httpBuilder
                    .readTimeout(1, TimeUnit.MINUTES)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES) //设置超时
                    .addInterceptor(interceptor)
                    .build();
        } else {
            client = httpBuilder
                    .readTimeout(times, TimeUnit.SECONDS)
                    .connectTimeout(times, TimeUnit.SECONDS)
                    .writeTimeout(times, TimeUnit.SECONDS) //设置超时
                    .build();
        }

//        L.log(reqUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(reqUrl)
                .client(client)
                .build();


//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(StaticParament.MainUrl)
//                .build();
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        if (requestData != null) {
            requestData.topData(this, requestInterface, headerMap, sign, time, RMap);
        } else if (requestDataStr != null) {
            requestDataStr.topData(this, requestInterface, headerMap, sign, time, RMap);
        } else if (requestDataStrMsg != null) {
            requestDataStrMsg.topData(this, requestInterface, headerMap, sign, time, RMap);
        }

        return requestInterface;
    }


    private LoadingDialog loadingDialog;

    /**
     * 发送请求
     */
    public void Call(Call<ResponseBody> call, final Class<T> model) {
        Call(call, model, true);
    }

    /**
     * 发送请求
     */
    public void Call(Call<ResponseBody> call, final Class<T> model, boolean showLoading) {
        if (showLoading) {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(context);
            }
            if (context instanceof ChooseCardListActivity) {
                loadingDialog.showCannotCancel();
            } else if (/*context instanceof Login_Activity || */context instanceof StartActivity) {
                loadingDialog.show(false);
            } else if (context instanceof ConfirmPayActivity || context instanceof ConfirmPayNextActivity
                     || context instanceof PayFailedActivity
                    || context instanceof InstalmentPendingNextActivity /*|| context instanceof MainTab*/) {
                loadingDialog.showNotMiss();
            } else {
                loadingDialog.show(true);
            }
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //请求成功
                try {
                    String jsonStr = new String(response.body().bytes());
                    L.log("返回数据：" + jsonStr);
                    requestData(jsonStr, model);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                    if (requestData != null) {
                        requestData.resError("data error");
                    }
                    if (requestDataStr != null) {
                        requestDataStr.resError("data error");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                L.log("request Error", t.getMessage());
                L.log("request Error", t.toString());
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                if (requestData != null) {
                    requestData.resError("");
                }
                if (requestDataStr != null) {
                    requestDataStr.resError("");
                }
            }
        });
    }

    /**
     * 发送请求
     */
    public void Call(Call<ResponseBody> call) {
        L.log("请求连接：" + call.request().url().toString());
        L.log("请求Headers：" + call.request().headers().toString());
        Call(call, null);
    }


    /**
     * 初步处理返回的数据
     */
    private void requestData(String jsonStr, Class<T> model) {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
//        MainDatas mainDatas = new Gson().fromJson(jsonStr, MainDatas.class);
        MainDatas mainDatas = new MainDatas();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String code = jsonObject.getString("code");
            String message = jsonObject.getString("message");
            String data = jsonObject.getString("data");
            String pc = jsonObject.getString("pc");

            mainDatas.setCode(code);
            mainDatas.setData(data);
            mainDatas.setPc(pc);
            mainDatas.setMessage(message);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //100300  分期付还款处理中
        if (mainDatas.getCode().equals("100200") || mainDatas.getCode().equals("100300")) {
            if (model != null) {
                if (requestData != null) {
                    requestData.resData(new Gson().fromJson(mainDatas.getData(), model), mainDatas.getData(), mainDatas.getPc(), mainDatas.getCode());
                }
                if (requestDataStrMsg != null) {
                    requestDataStrMsg.resData(mainDatas.getData(), mainDatas.getPc(), mainDatas.getCode(), mainDatas.getMessage());
                }
                if (requestDataStr != null) {
                    requestDataStr.resData(mainDatas.getData(), mainDatas.getPc(), mainDatas.getCode());
                }
            } else {
                //没有指定返回类型（只能返回原始数据）
                if (requestData != null) {
                    requestData.resData(null, mainDatas.getData(), mainDatas.getPc(), mainDatas.getCode());
                }
                if (requestDataStrMsg != null) {
                    requestDataStrMsg.resData(mainDatas.getData(), mainDatas.getPc(), mainDatas.getCode(), mainDatas.getMessage());
                }
                if (requestDataStr != null) {
                    requestDataStr.resData(mainDatas.getData(), mainDatas.getPc(), mainDatas.getCode());
                }
            }
        } else if (mainDatas.getCode().equals("100900") || mainDatas.getCode().equals("100800")) {
            //投资时不符合投资条件
            if (requestData != null) {
                requestData.resData(null, mainDatas.getData(), mainDatas.getPc(), mainDatas.getCode());
            }
            if (requestDataStrMsg != null) {
                requestDataStrMsg.resData(mainDatas.getData(), mainDatas.getPc(), mainDatas.getCode(), mainDatas.getMessage());
            }
            if (requestDataStr != null) {
                requestDataStr.resData(mainDatas.getData(), mainDatas.getPc(), mainDatas.getCode());
            }
        } else {
            if (mainDatas.getCode().equals("100403")) {
                //Token过期
                final SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                if (!sharePreferenceUtils.get(StaticParament.USER_TOKEN, "").equals("")) {
                    TiShiDialog tiShiDialog = new TiShiDialog(context);
                    tiShiDialog.ShowDialog(mainDatas.getMessage(), "Confirm");
                    tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                        @Override
                        public void GuanBi() {
//                    Toast.makeText(context, R.string.login_timeout, Toast.LENGTH_SHORT).show();
                            for (Activity activity : BaseActivity.activityList) {
                                activity.finish();
                            }
                            sharePreferenceUtils.clear();
//                    Intent intent = new Intent(context, Login_Activity.class);
                            Intent intent = new Intent(context, LoginActivity_inputNumber.class);
                            context.startActivity(intent);
//                            ((Activity) context).finish();
                        }
                    });
                }
            } else {
                if (mainDatas.getMessage() != null && !mainDatas.getMessage().equals("") && !mainDatas.getMessage().equals("null") && !(context instanceof InputPin_Activity) && !(context instanceof ChangeEmailActivity)
                        && !(context instanceof ChangePhoneNumActivity) && !(context instanceof SetNewPinActivity) && EnterPayPinDialog.showTishgiDIalog
                        && !PromotionListActivity.showTishgiDialog && !(context instanceof NewAddBankCardActivityDialog)) {
                    TiShiDialog tiShiDialog = new TiShiDialog(context);
                    tiShiDialog.ShowDialog(mainDatas.getMessage());
                }
                if (requestData != null) {
                    requestData.resError(mainDatas.getMessage());
                }
                if (requestDataStrMsg != null) {
                    requestDataStrMsg.resError(mainDatas.getMessage());
                }
                if (mainDatas.getCode().equals("100410")) {
                    if (requestDataStr != null) {
                        requestDataStr.resError("100410" + mainDatas.getMessage());
                    }
                } else {

                    if (requestDataStr != null) {
                        requestDataStr.resError(mainDatas.getMessage());
                    }
                }
            }
        }

        PromotionListActivity.showTishgiDialog = false;
    }

    public interface RequestData<T> {
        void topData(RequestUtils requestUtils, RequestInterface requestInterface, Map<String, String> headerMap, String sign
                , String time, Map<String, Object> maps);

        void resData(T data, String dataStr, String pc, String code);

        void resError(String error);
    }

    private RequestData requestData;

    public void setOnRequestData(RequestData requestData) {
        this.requestData = requestData;
    }


    public interface RequestDataStr {
        void topData(RequestUtils requestUtils, RequestInterface requestInterface, Map<String, String> headerMap, String sign
                , String time, Map<String, Object> maps);

        void resData(String dataStr, String pc, String code);

        void resError(String error);
    }

    private RequestDataStr requestDataStr;

    public void setOnRequestData(RequestDataStr requestDataStr) {
        this.requestDataStr = requestDataStr;
    }


    public interface RequestDataStrMsg {
        void topData(RequestUtils requestUtils, RequestInterface requestInterface, Map<String, String> headerMap, String sign
                , String time, Map<String, Object> maps);

        void resData(String dataStr, String pc, String code, String message);

        void resError(String error);
    }

    private RequestDataStrMsg requestDataStrMsg;

    public void setOnRequestData(RequestDataStrMsg requestDataStrMsg) {
        this.requestDataStrMsg = requestDataStrMsg;
    }

}
