package com.home.glx.uwallet.tools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.home.glx.uwallet.R;


public class ChangeLineEdittext extends EditText { //控制edittext下边框颜色和高度  同时可以控制下边框距离文字的距离。
    public ChangeLineEdittext(Context context) {

        super(context);
    }

    public ChangeLineEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLineBg(context);
    }

    private void initLineBg(final Context context) {
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    setBackground(ContextCompat.getDrawable(context, R.drawable.edt_bg_selected));
                } else {
                    setBackground(ContextCompat.getDrawable(context, R.drawable.edt_bg_un_selected));

                }
            }
        });
    }
}
