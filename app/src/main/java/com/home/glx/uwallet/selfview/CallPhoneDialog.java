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

public class CallPhoneDialog {
    private Context context;

    public CallPhoneDialog(Context context) {
        this.context = context;
    }

    public interface Call {
        void onCall();
        void onCopy();
    }

    public Call call;

    public void setOnCall(Call call) {
        this.call = call;
    }

    /**
     * 展示dialog
     */
    public void ShowDialog() {
        if (context == null) {
            return;
        }
        try {
            final MyDialog dialog = new MyDialog(context, R.style.TDialog, R.layout.dialog_call_phone);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            dialog.show();
            PublicTools.setDialogSize(context, dialog);
            dialog.setCancelable(true);
            Button idClose = (Button) dialog.findViewById(R.id.close);
            TextView idCall = (TextView) dialog.findViewById(R.id.call);
            TextView idCopy = (TextView) dialog.findViewById(R.id.copy);
            idClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            idCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    call.onCall();
                }
            });
            idCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    call.onCopy();
                }
            });
        } catch (Exception e) {

        }
    }
}
