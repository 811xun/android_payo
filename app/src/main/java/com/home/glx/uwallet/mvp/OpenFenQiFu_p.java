package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.home.glx.uwallet.datas.WhetherOpenFenQiDatas;

import java.util.Map;

public class OpenFenQiFu_p implements OpenFenQiFu_in.Present {

    private Context context;
    private OpenFenQiFu_in.View view;
    private OpenFenQiFu_m model;

    public OpenFenQiFu_p(Context context, OpenFenQiFu_in.View view) {
        this.context = context;
        this.view = view;
        model = new OpenFenQiFu_m(context, this);
    }

    @Override
    public void loadFenQiFuStatus(Map<String, Object> maps) {
        model.getFenQiFuStatus(maps);
    }

    @Override
    public void resFenQiFuStatus(String dataStr) {
        if (dataStr != null) {
            Gson gson = new Gson();
            WhetherOpenFenQiDatas whetherOpenFenQiDatas = gson.fromJson(dataStr, WhetherOpenFenQiDatas.class);
            view.setFenQiFuStatus(whetherOpenFenQiDatas.getState(), whetherOpenFenQiDatas.getIframeUrl());
        } else {
            view.setFenQiFuStatus(null, null);
        }
    }
}
