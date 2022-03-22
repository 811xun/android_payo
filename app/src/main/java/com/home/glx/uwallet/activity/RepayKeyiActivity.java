package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.tools.TypefaceUtil;

public class RepayKeyiActivity extends MainActivity {
    private TextView done;
    private TextView payTitle;


    @Override
    public int getLayout() {
        return R.layout.activity_repay_keyi;
    }

    @Override
    public void initView() {
        super.initView();


        payTitle = (TextView) findViewById(R.id.title);
        done = findViewById(R.id.done);

        //切换字体
        TypefaceUtil.replaceFont(payTitle, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.msg), "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(done, "fonts/gilroy_semiBold.ttf");

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepayKeyiActivity.this, MainTab.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //do something.
            //禁用返回键
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}