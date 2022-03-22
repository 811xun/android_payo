package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.home.glx.uwallet.datas.UserAllMsgDatas;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import java.util.Map;

public class GetUserAllMsg_p implements GetUserAllMsg_in.Present {

    private Context context;
    private GetUserAllMsg_in.View view;
    private GetUserAllMsg_m model;

    public GetUserAllMsg_p(Context context, GetUserAllMsg_in.View view) {
        this.context = context;
        this.view = view;
        model = new GetUserAllMsg_m(context, this);
    }

    @Override
    public void loadUserAllMsg(Map<String, Object> maps) {
        model.getUserAllMsg(maps);
    }

    @Override
    public void resUserAllMsg(String dataStr) {
        if (dataStr == null) {
            view.setUserMsg(null);
            return;
        }
        Gson gson = new Gson();
        UserAllMsgDatas userAllMsgDatas = gson.fromJson(dataStr, UserAllMsgDatas.class);
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
        sharePreferenceUtils.put(StaticParament.USER_ALL_MSG, dataStr);
        sharePreferenceUtils.put(StaticParament.USER_NAME1, userAllMsgDatas.getUserFirstName());
        sharePreferenceUtils.put(StaticParament.USER_NAME2, userAllMsgDatas.getUserMiddleName());
        sharePreferenceUtils.put(StaticParament.USER_NAME3, userAllMsgDatas.getUserLastName());
        sharePreferenceUtils.put(StaticParament.USER_EMAIL, userAllMsgDatas.getPhone());
        sharePreferenceUtils.save();
        view.setUserMsg(userAllMsgDatas);
    }
}
