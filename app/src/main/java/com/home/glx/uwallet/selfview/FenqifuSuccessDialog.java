package com.home.glx.uwallet.selfview;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by GLX on 2017/5/18.
 */

public class FenqifuSuccessDialog {

    private Context context;
    private List<ChoiceDatas> htmlList = new ArrayList<>();
    private Activity mActivity;

    public FenqifuSuccessDialog(Context context, Activity activity) {
        this.context = context;
        this.mActivity = activity;
    }

    public interface GuanBi {
        void GuanBi();

        void queding();
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

        final MyDialog dialog = new MyDialog(context, R.style.tipDialog, R.layout.fenqifu_success_dialog);
        dialog.show();
        PublicTools.setDialogSize(context, dialog);
        dialog.setCancelable(false);
        TypefaceUtil.replaceFont(dialog.findViewById(R.id.id_jine), "fonts/gilroy_bold.ttf");

        TextView jine = dialog.findViewById(R.id.jine);
        TypefaceUtil.replaceFont(jine, "fonts/gilroy_bold.ttf");
        jine.setText(text);

        TypefaceUtil.replaceFont(dialog.findViewById(R.id.avi), "fonts/gilroy_semiBold.ttf");

        ImageButton cancel = dialog.findViewById(R.id.close);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (guanBi != null) {
                    guanBi.GuanBi();
                }

            }
        });
        new Handler().postDelayed(new Runnable() {//五秒钟后弹窗关闭 关闭该页然后从哪儿来到哪儿去。
            @Override
            public void run() {
                if (!mActivity.isFinishing() && (dialog != null) && dialog.isShowing() && !mActivity.isDestroyed()) {//可能不到5s 手动关闭了弹窗 所以需要加个判断。
                    dialog.dismiss();
                    if (guanBi != null) {
                        guanBi.GuanBi();
                    }
                }
            }
        }, 5000);
    }
}
