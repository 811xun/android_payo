package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.tools.PublicTools;

public class ChooseGoogleMapDialog {
    private Context context;

    public ChooseGoogleMapDialog(Context context) {
        this.context = context;
    }

    public interface Open {
        void onOpen();
    }

    public Open open;

    public void setOnUpdate(Open open) {
        this.open = open;
    }

    /**
     * 展示dialog
     */
    public void ShowDialog(String text) {
        if (context == null) {
            return;
        }
        try {
            final MyDialog dialog = new MyDialog(context, R.style.TDialog, R.layout.dialog_choose_google);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            dialog.show();
            PublicTools.setDialogSize(context, dialog);
            dialog.setCancelable(true);
            Button idClose = (Button) dialog.findViewById(R.id.close);
            Button idOpen = (Button) dialog.findViewById(R.id.open);
            if (!TextUtils.isEmpty(text)) {
                idOpen.setText(text);
            }
            idClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            idOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    open.onOpen();
                }
            });
        } catch (Exception e) {

        }
    }
}
