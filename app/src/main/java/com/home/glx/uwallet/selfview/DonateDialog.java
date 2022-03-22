package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.tools.TypefaceUtil;

public class DonateDialog {
    private MyDialog dialog;
    private Context context;
    private TextView titleView;
    private TextView textView;
    private TextView msgView;
    private ImageView close;

    public DonateDialog(Context context) {
        this.context = context;
    }

    public void show() {
        dialog = new MyDialog(context, R.style.NoRadiusDialog, R.layout.dialog_donate);
        dialog.show();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        titleView = dialog.findViewById(R.id.title);
        textView = dialog.findViewById(R.id.text);
        msgView = dialog.findViewById(R.id.msg);
        close = dialog.findViewById(R.id.close);

        //切换字体
        TypefaceUtil.replaceFont(titleView,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(textView,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(msgView,"fonts/gilroy_medium.ttf");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
