package com.home.glx.uwallet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.xzc.LoginActivity_inputNumber;
import com.home.glx.uwallet.tools.TypefaceUtil;

public class NewGuideActivity extends MainActivity {

    private RelativeLayout guide1;
    private RelativeLayout guide2;
    private TextView start;

    @Override
    public int getLayout() {
        setGetPermission(false);
        getPermission = false;
        //全屏设置
//        hideSystemNavigationBar();
        return R.layout.activity_new_guide;
    }

    @Override
    public void getLayoutTop(Bundle savedInstanceState) {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Window window = getWindow();
        //After LOLLIPOP not translucent status bar
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //Then call setStatusBarColor.
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.orange1));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.orange1));
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
            //getWindow().setNavigationBarColor(Color.BLUE);
        }
        super.getLayoutTop(savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();
        guide1 = findViewById(R.id.guide1);
        guide2 = findViewById(R.id.guide2);
        start = findViewById(R.id.start);
        TextView title = findViewById(R.id.title);
        TextView text = findViewById(R.id.msg);
        TextView title2 = findViewById(R.id.title2);
        TextView text2 = findViewById(R.id.msg2);
        //切换字体
        TypefaceUtil.replaceFont(title,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(text,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(title2,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(text2,"fonts/gilroy_medium.ttf");
        guide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guide1.setVisibility(View.GONE);
                guide2.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        start.setVisibility(View.VISIBLE);
                        Animation animBottomOut = AnimationUtils.loadAnimation(NewGuideActivity.this, R.anim.alpah_anim);
                        start.startAnimation(animBottomOut);
                    }
                }, 1000);
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewGuideActivity.this, LoginActivity_inputNumber.class);
                startActivity(intent);
                finish();
            }
        });
    }
}