package com.home.glx.uwallet.mvp;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.widget.Toast;

import com.google.gson.Gson;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.xzc.KycActivity_second_AddressChoose;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.yzq.zxinglibrary.common.Constant;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 修改手机号接口
 */
public class ChangeLoginPhone {

    private Context context;
    private ChangeLoginPhone_in changeLoginPhone_in;

    public ChangeLoginPhone(Context context, ChangeLoginPhone_in changeLoginPhone_in) {
        this.context = context;
        this.changeLoginPhone_in = changeLoginPhone_in;
    }

    public void sendSecurityCodeSMSToOld(final String phone) {
        RequestUtils requestUtils1 = new RequestUtils(context, new HashMap<String, Object>(),
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.sendSecurityCodeSMSToOld(headerMap, phone, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("向旧手机发送验证码:" + dataStr);
                        changeLoginPhone_in.sendOldPhoneCodeRes();
                    }


                    @Override
                    public void resError(String error) {


                    }
                });
    }

    public void checkOldPhoneCode(final String phone, final String signCode) {
        RequestUtils requestUtils1 = new RequestUtils(context, new HashMap<String, Object>(),
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.checkOldPhoneCode(headerMap, phone, signCode, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("验证旧手机号验证码:" + dataStr);
                        changeLoginPhone_in.checkOldPhoneCodeRes();
                    }


                    @Override
                    public void resError(String error) {


                    }
                });
    }


    public void sendSecurityCodeSMSToNew(final String phone) {
        RequestUtils requestUtils1 = new RequestUtils(context, new HashMap<String, Object>(),
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.sendSecurityCodeSMSToNew(headerMap, phone, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("向新手机号发送验证码:" + dataStr);
                        changeLoginPhone_in.sendNewPhoneCodeRes();
                    }


                    @Override
                    public void resError(String error) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//大于等于9.0
                            Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/gilroy_medium.ttf");
                            SpannableString efr = new SpannableString(error);
                            efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Toast.makeText(context, efr, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }

//                        TiShiDialog tiShiDialog = new TiShiDialog(context);
//                        tiShiDialog.ShowDialog(error, "Confirm");

                    }
                });
    }


    public void updatePhone(final String phone, final String oldPhone, final String signCode) {
        RequestUtils requestUtils1 = new RequestUtils(context, new HashMap<String, Object>(),
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("参数：" + new Gson().toJson(maps));
                        L.log(phone);
                        L.log(oldPhone);
                        L.log(signCode);
                        Call<ResponseBody> call = requestInterface.updatePhone(headerMap, phone, oldPhone, signCode, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        SharePreferenceUtils sharePreferenceUtils_app = new SharePreferenceUtils(context, StaticParament.APP);
                        sharePreferenceUtils_app.put(StaticParament.USER_PHONE, phone);
                        sharePreferenceUtils_app.save();
                        L.log("校验新手机号验证码:" + dataStr);
                        changeLoginPhone_in.cheskNewPhoneCodeRes();

                    }


                    @Override
                    public void resError(String error) {

                        changeLoginPhone_in.errorSexCodeRes();
                    }
                });
    }

}
