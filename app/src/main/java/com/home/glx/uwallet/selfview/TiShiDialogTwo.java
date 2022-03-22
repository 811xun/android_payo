package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.graphics.Color;
import android.text.Spanned;
import android.view.View;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

public class TiShiDialogTwo {

    private MyDialog dialog;

    private TextView idSetNet;
    private TextView idAgain;
    private TextView idTitle;
    private TextView idMessage;


    public void show(final Context context, String title, String message, String cancel, String shure) {
        dialog = new MyDialog(context, R.style.MyDialog, R.layout.dialog_no_network);
        dialog.show();
        dialog.setCancelable(false);
        PublicTools.setDialogSize(context, dialog, 0.8f);

        idTitle = (TextView) dialog.findViewById(R.id.id_title);
        idMessage = (TextView) dialog.findViewById(R.id.id_message);
        idSetNet = (TextView) dialog.findViewById(R.id.id_set_net);
        idAgain = (TextView) dialog.findViewById(R.id.id_again);
        //切换字体
        TypefaceUtil.replaceFont(idTitle, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(idMessage, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(idSetNet, "fonts/acumin_regularPro.ttf");
        TypefaceUtil.replaceFont(idAgain, "fonts/acumin_regularPro.ttf");
        if (title.equals("")) {
            idTitle.setVisibility(View.GONE);
        }
        idTitle.setText(title);
        idMessage.setText(message);
        idSetNet.setText(cancel);
        idAgain.setText(shure);
        if (title.equals("Is this the right amount?")) {
            idSetNet.setTextColor(Color.parseColor("#717171"));
        }
        idSetNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancel != null) {
                    onCancel.cancel();
                }
                dismiss();
            }
        });
        idAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onShure != null) {
                    onShure.shure();
                }
                dismiss();
            }
        });
    }

    public void show(final Context context, String title, String message, String cancel, String shure, String color) {
        dialog = new MyDialog(context, R.style.MyDialog, R.layout.dialog_no_network);
        dialog.show();
        dialog.setCancelable(false);
        PublicTools.setDialogSize(context, dialog, 0.8F);

        idTitle = (TextView) dialog.findViewById(R.id.id_title);
        idMessage = (TextView) dialog.findViewById(R.id.id_message);
        idSetNet = (TextView) dialog.findViewById(R.id.id_set_net);
        idAgain = (TextView) dialog.findViewById(R.id.id_again);
        idAgain.setTextColor(context.getResources().getColor(R.color.colorBackGround));
        if (title.equals("")) {
            idTitle.setVisibility(View.GONE);
        }

        //切换字体
        TypefaceUtil.replaceFont(idTitle, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(idMessage, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(idSetNet, "fonts/acumin_regularPro.ttf");
        TypefaceUtil.replaceFont(idAgain, "fonts/acumin_regularPro.ttf");
        idTitle.setText(title);
        idMessage.setText(message);
        idSetNet.setText(cancel);
        idAgain.setText(shure);

        idSetNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancel != null) {
                    onCancel.cancel();
                }
                dismiss();
            }
        });
        idAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onShure != null) {
                    onShure.shure();
                }
                dismiss();
            }
        });
    }

    public void show(final Context context, String title, Spanned message, String cancel, String shure, String color) {
        dialog = new MyDialog(context, R.style.MyDialog, R.layout.dialog_no_network);
        dialog.show();
        dialog.setCancelable(false);
        PublicTools.setDialogSize(context, dialog, 0.8F);

        idTitle = (TextView) dialog.findViewById(R.id.id_title);
        idMessage = (TextView) dialog.findViewById(R.id.id_message);
        idSetNet = (TextView) dialog.findViewById(R.id.id_set_net);
        idAgain = (TextView) dialog.findViewById(R.id.id_again);
        idAgain.setTextColor(context.getResources().getColor(R.color.colorBackGround));
        if (title.equals("")) {
            idTitle.setVisibility(View.GONE);
        }

        //切换字体
        TypefaceUtil.replaceFont(idTitle, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(idMessage, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(idSetNet, "fonts/acumin_regularPro.ttf");
        TypefaceUtil.replaceFont(idAgain, "fonts/acumin_regularPro.ttf");
        idTitle.setText(title);
        idMessage.setText(message);
        idSetNet.setText(cancel);
        idAgain.setText(shure);

        idSetNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancel != null) {
                    onCancel.cancel();
                }
                dismiss();
            }
        });
        idAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onShure != null) {
                    onShure.shure();
                }
                dismiss();
            }
        });
    }


    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    public interface OnShure {
        void shure();
    }

    private OnShure onShure;

    public void setOnShure(OnShure onShure) {
        this.onShure = onShure;
    }

    public interface OnCancel {
        void cancel();
    }

    private OnCancel onCancel;

    public void setOnCancel(OnCancel onCancel) {
        this.onCancel = onCancel;
    }

}
