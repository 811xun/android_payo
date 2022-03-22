package com.home.glx.uwallet.selfview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.home.glx.uwallet.MyApplication;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.tools.PublicTools;

/**
 * 没有NetWork提示
 */
public class NoNetWorkDialog {

    private MyDialog dialog;

    private TextView idSetNet;
    private TextView idAgain;

    private static volatile NoNetWorkDialog instance;

    private NoNetWorkDialog() {
    }

    public static NoNetWorkDialog getInstance(Activity activity) {
        mActivity = activity;

        if (instance == null) {
            synchronized (NoNetWorkDialog.class) {
                if (instance == null) {
                    instance = new NoNetWorkDialog();
                }
            }
        }
        return instance;
    }

    public static Activity mActivity;

    public void show(final Context context, Activity activity) {
        mActivity = activity;
        dialog = new MyDialog(context, R.style.MyDialog, R.layout.dialog_no_network);
        dialog.show();
        PublicTools.setDialogSize(context, dialog, 0.8F);

        idSetNet = (TextView) dialog.findViewById(R.id.id_set_net);
        idAgain = (TextView) dialog.findViewById(R.id.id_again);
        idSetNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {

                    dialog.cancel();
//                openSetting();
                    dialog.dismiss();
                }
            }
        });
        idAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                    show(context, mActivity);
                }
            }
        });
    }

    private void openSetting() {
        Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.context.startActivity(intent);
    }

    public void dismiss() {
        if (!mActivity.isFinishing() && (dialog != null) && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
