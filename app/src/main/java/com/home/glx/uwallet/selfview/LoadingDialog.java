package com.home.glx.uwallet.selfview;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * 等待
 */
public class LoadingDialog {

    private Context context;
    private MyDialog dialog;

    private AVLoadingIndicatorView avi;

    public LoadingDialog(Context context) {
        this.context = context;
    }

    public void show(boolean white) {
        try {

            if (context instanceof MainActivity) {
                ((MainActivity) context).loadDialog = dialog;
            }

            if (context != null) {
                dialog = new MyDialog(context, R.style.TDialog, R.layout.dialog_loading);
                Window window = dialog.getWindow();
                if (white) {
                    assert window != null;
                    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  //清除灰色背景
                }
                dialog.show();

                avi = (AVLoadingIndicatorView) dialog.findViewById(R.id.avi);
                avi.show();
            }
        } catch (Exception e) {

        }
    }

    public void showCannotCancel() {//滚动时是白色的界面 但是滚动条不能通过点击消失。
        try {

            if (context instanceof MainActivity) {
                ((MainActivity) context).loadDialog = dialog;
            }

            if (context != null) {
                dialog = new MyDialog(context, R.style.TDialog, R.layout.dialog_loading);
                dialog.setCancelable(false);
                Window window = dialog.getWindow();
                assert window != null;
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  //清除灰色背景
                dialog.show();

                avi = (AVLoadingIndicatorView) dialog.findViewById(R.id.avi);
                avi.show();
            }
        } catch (Exception e) {

        }
    }


    public void showNotMiss() {
        try {

            if (context instanceof MainActivity) {
                ((MainActivity) context).loadDialog = dialog;
            }

            if (context != null) {
                dialog = new MyDialog(context, R.style.TDialog, R.layout.dialog_loading);
                dialog.setCancelable(false);
                dialog.show();

                avi = (AVLoadingIndicatorView) dialog.findViewById(R.id.avi);
                avi.show();
            }
        } catch (Exception e) {

        }
    }

    public void dismiss() {
        if (context instanceof Application) {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } else {
            if (!((Activity) context).isFinishing() && dialog != null && dialog.isShowing() && !((Activity) context).isDestroyed()) {
                dialog.dismiss();
            }
        }
    }
}
