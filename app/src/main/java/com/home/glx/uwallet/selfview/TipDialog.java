package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

public class TipDialog {
    private Context context;
    private TextView idTitle;
    private TextView queding;
    private TextView tex;

    public TipDialog(Context context) {
        this.context = context;
    }

    public interface Close {
        void Close();
    }

    public Close close;

    public void setOnClose(Close close) {
        this.close = close;
    }

    boolean yincang = true;

    /**
     * 设置点击弹窗外部是否可以隐藏弹窗
     */
    public void setYinCang(boolean yincang) {
        this.yincang = yincang;
    }

    /**
     * 展示dialog
     */
    public void showDialog(String text) {
        if (context == null) {
            return;
        }
        try {
            final MyDialog dialog = new MyDialog(context, R.style.tipDialog, R.layout.dialog_tip);
            dialog.show();
            PublicTools.setTipDialogSize(context, dialog);
            dialog.setCancelable(false);

            queding = (TextView) dialog.findViewById(R.id.id_queding);
            queding.setFocusable(false);
            queding.setFocusableInTouchMode(false);
            tex = (TextView) dialog.findViewById(R.id.id_tex);
            tex.setText(text);
            //切换字体
            TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(queding, "fonts/gilroy_medium.ttf");
            queding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (close != null) {
                        close.Close();
                    }

                }
            });
            queding.setFocusable(false);
            queding.setFocusableInTouchMode(false);
        } catch (Exception e) {

        }
    }

    /**
     * 展示dialog
     */
    public void showDialog(String title, String text, String closeText) {
        if (context == null) {
            return;
        }
        try {
            final MyDialog dialog = new MyDialog(context, R.style.tipDialog, R.layout.dialog_tip);
            dialog.show();
            PublicTools.setTipDialogSize(context, dialog);
            dialog.setCancelable(false);

            idTitle = (TextView) dialog.findViewById(R.id.id_title);
            queding = (TextView) dialog.findViewById(R.id.id_queding);
            queding.setFocusable(false);
            queding.setFocusableInTouchMode(false);
            tex = (TextView) dialog.findViewById(R.id.id_tex);
            //切换字体
            TypefaceUtil.replaceFont(idTitle, "fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(queding, "fonts/gilroy_medium.ttf");
            tex.setText(text);
            if (!TextUtils.isEmpty(title)) {
                idTitle.setVisibility(View.VISIBLE);
                idTitle.setText(title);
            }

            if (!TextUtils.isEmpty(closeText)) {
                queding.setText(closeText);
            }
            queding.setFocusable(false);
            queding.setFocusableInTouchMode(false);
            queding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (close != null) {
                        close.Close();
                    }

                }
            });
            queding.setFocusable(false);
            queding.setFocusableInTouchMode(false);
        } catch (Exception e) {

        }
    }

    /**
     * 展示dialog
     */
    public void showDialog(String title, String text, String closeText, String color) {
        if (context == null) {
            return;
        }
        try {
            final MyDialog dialog = new MyDialog(context, R.style.tipDialog, R.layout.dialog_tip);
            dialog.show();
            PublicTools.setTipDialogSize(context, dialog);
            dialog.setCancelable(false);

            idTitle = (TextView) dialog.findViewById(R.id.id_title);
            queding = (TextView) dialog.findViewById(R.id.id_queding);
            tex = (TextView) dialog.findViewById(R.id.id_tex);
            //切换字体
            TypefaceUtil.replaceFont(idTitle, "fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(queding, "fonts/gilroy_medium.ttf");

            if (!TextUtils.isEmpty(title)) {
                idTitle.setVisibility(View.VISIBLE);
                idTitle.setText(title);
            }

            tex.setText(text);
            if (color.equals("gray")) {
                queding.setTextColor((context.getResources().getColor(R.color.pay_text_gray)));
            } else if (color.equals("blue_circle")) {
                queding.setTextColor((context.getResources().getColor(R.color.colorBackGround)));
            }
            queding.setText(closeText);
            queding.setFocusable(false);
            queding.setFocusableInTouchMode(false);
            queding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (close != null) {
                        close.Close();
                    }
                }
            });
        } catch (Exception e) {

        }
    }
}
