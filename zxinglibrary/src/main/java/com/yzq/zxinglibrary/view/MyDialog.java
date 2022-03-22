package com.yzq.zxinglibrary.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by guoluxiang on 2016/6/28.
 * 修改时间：2016/6/28 14:51
 */

public class MyDialog extends Dialog {

    private int layout;

    public MyDialog(Context context, int theme, int layout) {
        super(context, theme);
        this.layout = layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
    }
}
