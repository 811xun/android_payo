package com.home.glx.uwallet.mvp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.widget.Toast;

import com.google.gson.Gson;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 修改邮箱
 */
public class ChangeEmail {

    private Context context;

    public ChangeEmail(Context context) {
        this.context = context;
    }


    public void change(final Map<String, String> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("修改邮箱参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.modifyEmail(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                TiShiDialog tiShiDialog = new TiShiDialog(context);
                tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                    @Override
                    public void GuanBi() {
                        ((Activity) context).finish();
                    }
                });
                tiShiDialog.ShowDialog(context.getString(R.string.save_success));
                L.log("修改邮箱:" + dataStr);
                onChangeSatus.status("success");

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
 //                TiShiDialog tiShiDialog = new TiShiDialog(context);
//                tiShiDialog.ShowDialog(error);
            }
        });
    }


    public interface OnChangeSatus {
        void status(String status);
    }

    private OnChangeSatus onChangeSatus;

    public void setOnFindList(OnChangeSatus onChangeSatus) {
        this.onChangeSatus = onChangeSatus;
    }

}
