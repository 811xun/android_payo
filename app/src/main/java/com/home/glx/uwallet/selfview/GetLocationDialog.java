package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.tools.PublicTools;

public class GetLocationDialog {
    private Context context;

    public GetLocationDialog(Context context) {
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
    public void ShowDialog() {
        if (context == null) {
            return;
        }
        try {
            final MyDialog dialog = new MyDialog(context, R.style.MyDialog, R.layout.dialog_get_location);
            dialog.show();
            PublicTools.setDialogSize(context, dialog);
            dialog.setCancelable(false);

            final TextView idUpdate = (TextView) dialog.findViewById(R.id.id_update);
            TextView idClose = (TextView) dialog.findViewById(R.id.id_close);
            TextView tex = (TextView) dialog.findViewById(R.id.id_tex);
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
