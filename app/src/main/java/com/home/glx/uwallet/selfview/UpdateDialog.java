package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

public class UpdateDialog {
    private Context context;

    public UpdateDialog(Context context) {
        this.context = context;
    }

    public interface Close {
        void onClose();
    }

    public Close close;

    public void setOnClose(Close close) {
        this.close = close;
    }

    public interface Update {
        void onUpdate();
    }

    public Update update;

    public void setOnUpdate(Update update) {
        this.update = update;
    }

    /**
     * 展示dialog
     */
    public void ShowDialog(String version) {
        if (context == null) {
            return;
        }
        try {
            final MyDialog dialog = new MyDialog(context, R.style.tipDialog, R.layout.dialog_update);
            dialog.show();
            PublicTools.setTipDialogSize(context, dialog);
            dialog.setCancelable(false);

            final TextView idUpdate = (TextView) dialog.findViewById(R.id.id_update);
            TextView idClose = (TextView) dialog.findViewById(R.id.id_close);
            TextView tex = (TextView) dialog.findViewById(R.id.id_tex);
            //切换字体
            TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(idClose, "fonts/acumin_regularPro.ttf");
            TypefaceUtil.replaceFont(idUpdate, "fonts/acumin_regularPro.ttf");
            tex.setText("Payo v" + version + " is now available, download to explore the new features");
            idUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (update != null) {
                        update.onUpdate();
                    }
                }
            });
            idClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (close != null) {
                        close.onClose();
                    }
                }
            });

        } catch (Exception e) {

        }
    }


}
