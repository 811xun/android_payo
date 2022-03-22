package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.PdfActivity;
import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.datas.GetChoiceDatas;
import com.home.glx.uwallet.mvp.GetChoiceList_in;
import com.home.glx.uwallet.mvp.GetChoiceList_p;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by GLX on 2017/5/18.
 */

public class TCDialog implements GetChoiceList_in.View {
    private GetChoiceList_p choiceList_p;

    private Context context;
    private List<ChoiceDatas> htmlList = new ArrayList<>();

    public TCDialog(Context context) {
        this.context = context;
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
        choiceList_p = new GetChoiceList_p(context, this);
        Map<String, Object> maps = new HashMap<>();
        String[] keys = new String[]{"payNowTermsConditions", "payLaterTermsConditions"};
        maps.put("code", keys);
        choiceList_p.loadChoiceList(maps);
        try {
            final MyDialog dialog = new MyDialog(context, R.style.tipDialog, R.layout.dialog_t_and_c);
            dialog.show();
            PublicTools.setDialogSize(context, dialog);
            dialog.setCancelable(false);
            final TextView queding = (TextView) dialog.findViewById(R.id.btn_get_code);
            TypefaceUtil.replaceFont(queding, "fonts/gilroy_semiBold.ttf");

            //切换字体
            TypefaceUtil.replaceFont(dialog.findViewById(R.id.id_title), "fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(dialog.findViewById(R.id.hint), "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(dialog.findViewById(R.id.proText1), "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(dialog.findViewById(R.id.proText2), "fonts/gilroy_medium.ttf");

            TextView cancel = (TextView) dialog.findViewById(R.id.id_cancel);
            TypefaceUtil.replaceFont(cancel, "fonts/gilroy_semiBold.ttf");
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (guanBi != null) {
                        guanBi.GuanBi();
                    }

                }
            });

            queding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    guanBi.queding();
                }
            });
            dialog.findViewById(R.id.proText1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, PdfActivity.class);
                    intent.putExtra("url", htmlList.get(0).getCName());
                    context.startActivity(intent);
                }
            });
            dialog.findViewById(R.id.proText2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(context, PdfActivity.class);
                    intent.putExtra("url", htmlList.get(1).getCName());
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public void setChoiceList(GetChoiceDatas getChoiceDatas) {
        if (getChoiceDatas != null) {
            htmlList.addAll(getChoiceDatas.getPayNowTermsConditions());
            htmlList.addAll(getChoiceDatas.getPayLaterTermsConditions());
        }
    }
}
