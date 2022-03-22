package com.home.glx.uwallet.mvp;

import android.content.Context;

import java.util.Map;

public class CheckPayPassword_p implements CheckPayPassword_in.Present {

    private Context context;
    private CheckPayPassword_in.View view;
    private CheckPayPassword_m model;

    public CheckPayPassword_p(Context context, CheckPayPassword_in.View view) {
        this.context = context;
        this.view = view;
        model = new CheckPayPassword_m(context, this);
    }

    @Override
    public void checkPayPassword(Map<String, Object> maps) {
        model.checkPayPassword(maps);
    }

    @Override
    public void resPasswordSrarus(String data) {
        if (data != null) {
            view.setPasswordStatus("1");
        } else {
            view.setPasswordStatus(null);
        }
    }
}
