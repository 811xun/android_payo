package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.Map;

public class BannerClickDialog {
    private MyDialog dialog;
    private Context context;
    private LayoutInflater inflater;
    private View thisView;
    private String text;
    private String title;
    private String url;
    private TextView titleView;
    private TextView textView;
    private ImageView imageView;
    private ImageView close;


    public BannerClickDialog(Context context, String title, String text, String url) {
        this.context = context;
        this.title = title;
        this.text = text;
        this.url = url;
        inflater = LayoutInflater.from(context);
        thisView = inflater.inflate(R.layout.dialog_banner_click, null);
    }

    public void show() {
        dialog = new MyDialog(context, R.style.MyRadiusDialog, R.layout.dialog_banner_click);
        dialog.show();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);

        titleView = dialog.findViewById(R.id.title);
        textView = dialog.findViewById(R.id.text);
        imageView = dialog.findViewById(R.id.img);
        close = dialog.findViewById(R.id.close);

        //切换字体
        TypefaceUtil.replaceFont(titleView,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(textView,"fonts/gilroy_medium.ttf");

        Glide.with(context)
                .load(StaticParament.ImgUrl + url)
                .into(imageView);
        titleView.setText(title);
        textView.setText(Html.fromHtml(text));
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
