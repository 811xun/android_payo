package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.home.glx.uwallet.datas.GetChoiceDatas;
import com.home.glx.uwallet.tools.L;

import java.util.Map;

public class GetChoiceList_p implements GetChoiceList_in.Present {

    private Context context;
    private GetChoiceList_in.View view;
    private GetChoiceList_m model;

    public GetChoiceList_p(Context context, GetChoiceList_in.View view) {
        this.context = context;
        this.view = view;
        model = new GetChoiceList_m(context, this);
    }

    @Override
    public void loadChoiceList(Map<String, Object> maps) {
        model.getChoiceList(maps);
    }

    @Override
    public void resChoiceList(String dataStr) {
        if (dataStr != null) {
            Gson gson = new Gson();
            GetChoiceDatas getChoiceDatas = gson.fromJson(dataStr, GetChoiceDatas.class);
            view.setChoiceList(getChoiceDatas);
        } else {
            view.setChoiceList(null);
        }
    }
}
