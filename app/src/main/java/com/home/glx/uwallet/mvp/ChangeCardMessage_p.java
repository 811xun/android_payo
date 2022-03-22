package com.home.glx.uwallet.mvp;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ChangeCardMessage_p implements ChangeCardMessage_in.Present{
    private Context context;
    private ChangeCardMessage_in.View view;
    private ChangeCardMessage_m model;

    public ChangeCardMessage_p(Context context, ChangeCardMessage_in.View view) {
        this.context = context;
        this.view = view;
        model = new ChangeCardMessage_m(context, this);
    }

    @Override
    public void reqChangeCardMessage(Map<String, Object> maps) {
        model.reqChangeCardMessage(maps);
    }

    @Override
    public void resChangeCardMessage(String dataStr) {
        if (dataStr != null) {
            view.setChangeCardMessage(dataStr);
        } else {
            view.setChangeCardMessage(null);
        }
    }
}
