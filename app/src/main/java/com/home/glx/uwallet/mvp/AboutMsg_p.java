package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.home.glx.uwallet.datas.AboutDatas;

import java.util.Map;

public class AboutMsg_p implements AboutMsg_in.Present {

    private Context context;
    private AboutMsg_in.View view;
    private AboutMsg_m model;

    public AboutMsg_p(Context context, AboutMsg_in.View view) {
        this.context = context;
        this.view = view;
        model = new AboutMsg_m(context, this);
    }

    @Override
    public void getAboutMsg(Map<String, Object> map) {
        model.getAboutMsg(map);
    }

    @Override
    public void resAboutMsg(String dataStr) {
        if (dataStr != null) {
            Gson gson = new Gson();
            AboutDatas aboutDatas = gson.fromJson(dataStr, AboutDatas.class);
            view.setAboutMsg(aboutDatas);
        }
    }
}
