package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.tools.PublicTools;


public class TiShiDialogThree {

    private Context context;

    public TiShiDialogThree(Context context) {
        this.context = context;
    }

    public interface GuanBi {
        void GuanBi();
    }

    public GuanBi guanBi;

    public void setOnGuanBi(GuanBi guanBi) {
        this.guanBi = guanBi;
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
    public void ShowDialog(String text) {
        if (context == null) {
            return;
        }
        try {
            final MyDialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.dialog_tishi);
            dialog.show();
            PublicTools.setDialogSize(context, dialog);
            dialog.setCancelable(false);

            TextView queding = (TextView) dialog.findViewById(R.id.id_queding);
            TextView tex = (TextView) dialog.findViewById(R.id.id_tex);
            tex.setText(text);
            queding.setText(R.string.next);

            queding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (guanBi != null) {
                        guanBi.GuanBi();
                    }
                }
            });
        } catch (Exception e) {

        }
    }

}
