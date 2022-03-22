package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

public class HomeReceiveDialog {
    private MyDialog dialog;
    private TextView moneyView;
    private TextView infoView;
    private TextView share;
    private ImageView close;

    public void show(final Context context, String money, String info) {
        dialog = new MyDialog(context, R.style.TDialog, R.layout.dialog_home_receive);
        dialog.show();
        dialog.setCancelable(false);
        PublicTools.setDialogSize(context, dialog, 0.8f);

        moneyView = (TextView) dialog.findViewById(R.id.money);
        infoView = (TextView) dialog.findViewById(R.id.info);
        share = (TextView) dialog.findViewById(R.id.share);
        close = (ImageView) dialog.findViewById(R.id.close);
        //切换字体
        TypefaceUtil.replaceFont(moneyView,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(infoView,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(share,"fonts/acumin_regularPro.ttf");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onShure != null) {
                    onShure.close();
                }
                dialog.dismiss();
            }
        });

        //切换字体
        TypefaceUtil.replaceFont(moneyView,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(infoView,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(share,"fonts/acumin_regularPro.ttf");

        moneyView.setText(String.format("You received $%s Credit", money));
        infoView.setText(Html.fromHtml("Your Friend <font color='#e77830'>" + info + "</font>" + " has made their first transaction with payo. As a reward you get a $20 voucher to use on a meal that's $40 or more. Now that's mates rates!"));

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onShure != null) {
                    onShure.shure();
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    public interface OnShure {
        void shure();
        void close();
    }

    private OnShure onShure;

    public void setOnShure(OnShure onShure) {
        this.onShure = onShure;
    }
}
