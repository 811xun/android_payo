package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.home.glx.uwallet.datas.PayPasswordDatas;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import java.util.Map;

public class PresencePayPassword_p implements PresencePayPassword_in.Present {

    private Context context;
    private PresencePayPassword_in.View view;
    private PresencePayPassword_m model;

    public PresencePayPassword_p(Context context, PresencePayPassword_in.View view) {
        this.context = context;
        this.view = view;
        model = new PresencePayPassword_m(context, this);
    }

    @Override
    public void loadPresenceStatus(String code, Map<String, Object> maps) {
        model.getPresenceStatus(code, maps);
    }

    @Override
    public void resPresenceStatus(String code, String dataStr) {
        if (dataStr != null) {
            Gson gson = new Gson();
            PayPasswordDatas payPasswordDatas = gson.fromJson(dataStr, PayPasswordDatas.class);
            SharePreferenceUtils user_sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
            if (!payPasswordDatas.getPayPassword().equals("")) {
                //有支付密码
                user_sharePreferenceUtils.put(StaticParament.USER_PAY_PWD, "1");
                user_sharePreferenceUtils.save();
                L.log("zzzzzzzzzzzzzzz");
                view.setPresenceStatus(code, "1");
            } else {
                L.log("xxxxxxxxxxxxxxx");
                view.setPresenceStatus(code, "0");
            }
        } else {
            view.setPresenceStatus(code, "0");
        }
    }
}
