package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.home.glx.uwallet.datas.LoginDatas;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import java.util.HashMap;
import java.util.Map;

public class Login_p implements Login_in.Present, PinNumber_in {

    private Context context;
    private Login_in.View view;
    private Login_m model;
    private PinNumberAbout pinNumberAbout;

    public Login_p(Context context, Login_in.View view) {
        this.context = context;
        this.view = view;
        model = new Login_m(context, this);
        pinNumberAbout = new PinNumberAbout(context, this);
    }

    @Override
    public void reqLogin(Map<String, Object> maps, boolean regist) {
        SharePreferenceUtils app_sharePreferenceUtils =
                new SharePreferenceUtils(context, StaticParament.APP);
        maps.put("pushToken", app_sharePreferenceUtils.get(StaticParament.PUSH_TOKEN, ""));
        model.reqLogin(maps, regist);
    }

    @Override
    public void resLoginData(LoginDatas loginDatas, boolean regist) {
        if (loginDatas != null) {
            SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
            sharePreferenceUtils.put(StaticParament.USER_TOKEN, loginDatas.getAuthorization());
            sharePreferenceUtils.put(StaticParament.USER_ID, loginDatas.getUserInfo().getId());
            sharePreferenceUtils.put(StaticParament.USER_MSG, new Gson().toJson(loginDatas.getUserInfo()));
            sharePreferenceUtils.put(StaticParament.USER_EMAIL, loginDatas.getUserInfo().getPhone());

            sharePreferenceUtils.put(StaticParament.USER_NAME1, loginDatas.getFirstName());
            sharePreferenceUtils.put(StaticParament.USER_NAME2, loginDatas.getMiddleName());
            sharePreferenceUtils.put(StaticParament.USER_NAME3, loginDatas.getLastName());
            sharePreferenceUtils.save();

            SharePreferenceUtils sharePreferenceUtils_app = new SharePreferenceUtils(context, StaticParament.APP);
            sharePreferenceUtils_app.put(StaticParament.USER_PHONE, loginDatas.getUserInfo().getPhone());
            sharePreferenceUtils_app.save();
            if (regist) {
                pinNumberAbout.selectPin(new HashMap<String, Object>());
            } else {
                view.setLoginStatus("1");
            }
        } else {
            view.setLoginStatus("0");
        }
    }

    /**
     * 查询PIN
     *
     * @param pin
     */
    @Override
    public void selectPin(String pin) {
        if (pin != null && !pin.equals("")) {
            SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
            sharePreferenceUtils.put(StaticParament.PIN_NUMBER, pin);
            sharePreferenceUtils.save();
            view.setLoginStatus("1");
        } else {
            //没有设置PIN
            view.setLoginStatus("6");
        }
    }

    @Override
    public void changePin(String status) {

    }

    @Override
    public void checkPin(String status) {

    }

}
