package com.home.glx.uwallet.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.TypefaceUtil;

public class BannerClickActivity extends MainActivity {
    private String text;
    private String title;
    private String url;
    private TextView titleView;
    private TextView textView;
    private ImageView imageView;
    private ImageView close;

    @Override
    public int getLayout() {
        return R.layout.activity_banner_click;
    }

    @Override
    public void initView() {
        super.initView();
        text = getIntent().getStringExtra("text");
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        titleView = (TextView) findViewById(R.id.title);
        textView = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.img);
        close = (ImageView) findViewById(R.id.id_back);

        //切换字体
        TypefaceUtil.replaceFont(titleView,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(textView,"fonts/gilroy_medium.ttf");

        Glide.with(this)
                .load(StaticParament.ImgUrl + url)
                .into(imageView);
        titleView.setText(title);
        textView.setText(Html.fromHtml(text));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}