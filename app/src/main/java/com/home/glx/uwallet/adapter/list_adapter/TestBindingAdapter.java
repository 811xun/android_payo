package com.home.glx.uwallet.adapter.list_adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;


/**
 * @author markzhai on 16/7/21
 * @version 1.3.0
 */
public class TestBindingAdapter {

    @BindingAdapter("android:img")
    public static void setResImg(ImageView imageView, int img) {
        imageView.setImageResource(img);
    }

    @BindingAdapter("android:setBackround")
    public static void setBackround(View view, int style) {
        view.setBackgroundResource(style);
    }


    @BindingAdapter({"android:dataText"})
    public static void setDataText(TextView textView, int resource) {
        textView.setText(resource);
    }

}