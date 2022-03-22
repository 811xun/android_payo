package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * 等待
 */
public class NoLoadingDialog {//加个涂层 用于提交信息时不能点击。

    private Context context;
    private MyDialog dialog;


    public NoLoadingDialog(Context context) {
        this.context = context;
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public void show(boolean white) {
        try {

         /*   if (context instanceof MainActivity) {
                ((MainActivity) context).loadDialog = dialog;
            }*/

            if (context != null) {
                dialog = new MyDialog(context, R.style.TucengDialog, R.layout.dialog_no_loading);
                dialog.setCancelable(false);
//                Window window = dialog.getWindow();
//                assert window != null;
//                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  //清除灰色背景
                Window window = dialog.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  //清除灰色背景
//                window.setNavigationBarColor(context.getColor(R.color.transparent));

//                window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
// 把 DecorView 的默认 padding 取消，同时 DecorView 的默认大小也会取消
                window.getDecorView().setPadding(0, 0, 0, 0);
                WindowManager.LayoutParams layoutParams = window.getAttributes();
// 设置宽度
                Log.d("ddssssss", "show: "+window.getDecorView().getHeight());
                Log.d("ddssssss", "show: "+(window.getDecorView().getHeight()-dip2px(context,15))+"");
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.height =  window.getDecorView().getHeight()-dip2px(context,18);
                window.setAttributes(layoutParams);
// 给 DecorView 设置背景颜色，很重要，不然导致 Dialog 内容显示不全，有一部分内容会充当 padding，上面例子有举出
                window.getDecorView().setBackgroundColor(ContextCompat.getColor(context,R.color.transparent));
                window.setNavigationBarColor(ContextCompat.getColor(context,R.color.transparent));
                dialog.show();
                dialog.findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        mShowTCDialog.ShowTCDialog();
                    }
                });
            }
        } catch (Exception e) {

        }
    }

//    public void showNotMiss() {
//        try {
//
//            if (context instanceof MainActivity) {
//                ((MainActivity) context).loadDialog = dialog;
//            }
//
//            if (context != null) {
//                dialog = new MyDialog(context, R.style.TDialog, R.layout.dialog_loading);
//                dialog.setCancelable(false);
//                dialog.show();
//
//                avi = (AVLoadingIndicatorView) dialog.findViewById(R.id.avi);
//                avi.show();
//            }
//        } catch (Exception e) {
//
//        }
//    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public interface ShowTCDialog {
        void ShowTCDialog();
    }

    public ShowTCDialog mShowTCDialog;

    public void setShowTCDialog(ShowTCDialog guanBi) {
        this.mShowTCDialog = guanBi;
    }

}
