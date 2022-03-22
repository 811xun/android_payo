package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.NewAddBankCardActivity;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;


/**
 * Created by GLX on 2017/5/18.
 */

public class TiShiDialog {

    private Context context;

    public TiShiDialog(Context context) {
        this.context = context;
    }

    public interface GuanBi {
        void GuanBi();
    }

    public GuanBi guanBi;

    public void setOnGuanBi(GuanBi guanBi) {
        this.guanBi = guanBi;
    }


    public interface GuanBiLeft {
        void GuanBiLeft();
    }

    public GuanBiLeft guanBiLeft;

    public void setOnGuanBiLeft(GuanBiLeft guanBiLeft) {
        this.guanBiLeft = guanBiLeft;
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
        try {
            final MyDialog dialog = new MyDialog(context, R.style.tipDialog, R.layout.dialog_tishi);
            dialog.show();
            PublicTools.setTipDialogSize(context, dialog);
            dialog.setCancelable(false);

            TextView queding = (TextView) dialog.findViewById(R.id.id_queding);
            TextView tex = (TextView) dialog.findViewById(R.id.id_tex);
            tex.setText(text);

            //切换字体
            TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(queding, "fonts/gilroy_medium.ttf");
            queding.setFocusable(false);
            queding.setFocusableInTouchMode(false);
            queding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (guanBi != null) {
                        guanBi.GuanBi();
                    }

                }
            });
        } catch (Exception e) {

        }
    }

    public void ShowDialog_wuwaiyinying(String text) {
        if (context == null) {
            return;
        }
        try {
            final MyDialog dialog = new MyDialog(context, R.style.tipDialog_wuwaiyinying, R.layout.dialog_tishi);
            dialog.show();
            PublicTools.setTipDialogSize(context, dialog);
            dialog.setCancelable(false);

            TextView queding = (TextView) dialog.findViewById(R.id.id_queding);
            TextView tex = (TextView) dialog.findViewById(R.id.id_tex);
            tex.setText(text);

            //切换字体
            TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(queding, "fonts/gilroy_medium.ttf");
            queding.setFocusable(false);
            queding.setFocusableInTouchMode(false);
            queding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (guanBi != null) {
                        guanBi.GuanBi();
                    }

                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * 展示dialog
     */
    public void ShowDialog(String text, int size) {
        if (context == null) {
            return;
        }
        try {
            final MyDialog dialog = new MyDialog(context, R.style.tipDialog, R.layout.dialog_tishi);
            dialog.show();
            PublicTools.setTipDialogSize(context, dialog);
            dialog.setCancelable(false);

            TextView queding = (TextView) dialog.findViewById(R.id.id_queding);
            TextView tex = (TextView) dialog.findViewById(R.id.id_tex);
            tex.setTextSize(size);
            tex.setText(text);

            //切换字体
            TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(queding, "fonts/gilroy_medium.ttf");

            queding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (guanBi != null) {
                        guanBi.GuanBi();
                    }

                }
            });
        } catch (Exception e) {

        }
    }

    private MyDialog dialog;

    /**
     * 展示dialog
     */
    public void ShowDialog(String title, String text, String close) {
        if (context == null) {
            return;
        }
        try {
            dialog = new MyDialog(context, R.style.tipDialog, R.layout.dialog_tishi);
            dialog.show();
            PublicTools.setTipDialogSize(context, dialog);
            dialog.setCancelable(false);

            TextView idTitle = (TextView) dialog.findViewById(R.id.id_title);
            TextView queding = (TextView) dialog.findViewById(R.id.id_queding);
            TextView tex = (TextView) dialog.findViewById(R.id.id_tex);
            TextView id_cancel = (TextView) dialog.findViewById(R.id.id_cancel);
            if (close.equals("Yes") || close.equals("Allow") || close.equals("Cancel") || close.equals("Log out")) {//注册页面 ＜18岁的弹窗。
                id_cancel.setVisibility(View.VISIBLE);
                dialog.findViewById(R.id.line_vertical).setVisibility(View.VISIBLE);
                tex.setLineSpacing(1.2f, 1.2f);
                dialog.findViewById(R.id.line).setBackgroundColor(ContextCompat.getColor(context, R.color.color_c9c9c9));
                idTitle.setBackgroundColor(ContextCompat.getColor(context, R.color.color_f8f8f8));
//                queding.setBackgroundColor(context.getColor(R.color.color_f8f8f8));
//                id_cancel.setBackgroundColor(context.getColor(R.color.color_f8f8f8));
                tex.setBackgroundColor(ContextCompat.getColor(context, R.color.color_f8f8f8));
//                tex.setPadding(0, 20, 0, 17);
                dialog.findViewById(R.id.ll).setBackground(context.getDrawable(R.drawable.bg_14f8f8f8));
                if (close.equals("Allow")) {
                    id_cancel.setText("Not now");
                }
                if (close.equals("Log out")) {//退出app弹窗
                    id_cancel.setText("Cancel");
                }
                if (close.equals("Cancel")) {
                    id_cancel.setText("Remove");

                    tex.setLineSpacing(1.0f, 1.0f);
//                    tex.setPadding(0, 5, 0, 1);
//                    tex.setMinHeight(0);
                }/*else {
                    tex.setPadding(0, 20, 0, 17);
                }*/

            } else {
                id_cancel.setVisibility(View.GONE);
            }
            //切换字体
            TypefaceUtil.replaceFont(idTitle, "fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(id_cancel, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(queding, "fonts/gilroy_medium.ttf");
            tex.setText(text);
            if (!TextUtils.isEmpty(title)) {
                idTitle.setVisibility(View.VISIBLE);
                idTitle.setText(title);
            }

            if (!TextUtils.isEmpty(close)) {
                queding.setText(close);
            }
            id_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (guanBiLeft != null)
                        guanBiLeft.GuanBiLeft();
                }
            });
            queding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (guanBi != null) {
                        guanBi.GuanBi();
                    }

                }
            });
        } catch (Exception e) {

        }
    }

    public void dismissDia() {
        dialog.dismiss();
    }

    /**
     * 展示dialog
     */
    public void ShowDialog(String text, String close) {
        if (context == null) {
            return;
        }
        try {
            final MyDialog dialog = new MyDialog(context, R.style.tipDialog, R.layout.dialog_tishi);
            dialog.show();
            PublicTools.setTipDialogSize(context, dialog);
            dialog.setCancelable(false);

            TextView queding = (TextView) dialog.findViewById(R.id.id_queding);
            TextView tex = (TextView) dialog.findViewById(R.id.id_tex);
            //切换字体
            TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(queding, "fonts/gilroy_medium.ttf");
            tex.setText(text);
            queding.setText(close);

            queding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (guanBi != null) {
                        guanBi.GuanBi();
                    }

                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * 展示dialog
     */
    public void ShowDialog(int text) {
        final MyDialog dialog = new MyDialog(context, R.style.tipDialog, R.layout.dialog_tishi);
        dialog.show();
        PublicTools.setTipDialogSize(context, dialog);
        dialog.setCancelable(false);

        TextView queding = (TextView) dialog.findViewById(R.id.id_queding);
        TextView tex = (TextView) dialog.findViewById(R.id.id_tex);
        tex.setText(text);

        //切换字体
        TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(queding, "fonts/gilroy_medium.ttf");

        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (guanBi != null) {
                    guanBi.GuanBi();
                }
            }
        });
    }

    /**
     * 展示dialog
     */
    public void ShowDialog(String text, boolean dismiss) {
        final MyDialog dialog = new MyDialog(context, R.style.tipDialog, R.layout.dialog_tishi);
        dialog.show();
        PublicTools.setTipDialogSize(context, dialog);

        dialog.setCancelable(dismiss);

        TextView queding = (TextView) dialog.findViewById(R.id.id_queding);
        TextView tex = (TextView) dialog.findViewById(R.id.id_tex);
        tex.setText(text);

        //切换字体
        TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(queding, "fonts/gilroy_medium.ttf");

        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (guanBi != null) {
                    guanBi.GuanBi();
                }
            }
        });
    }


    /**
     * 展示dialog
     */
    public void ShowOnOffDialog(String text) {
        final MyDialog dialog = new MyDialog(context, R.style.tipDialog, R.layout.dialog_tishi);
        dialog.show();
        PublicTools.setTipDialogSize(context, dialog);

        TextView queding = (TextView) dialog.findViewById(R.id.id_queding);
        TextView tex = (TextView) dialog.findViewById(R.id.id_tex);
        tex.setText(text);

        //切换字体
        TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(queding, "fonts/gilroy_medium.ttf");

        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (guanBi != null) {
                    guanBi.GuanBi();
                }

            }
        });
    }

    /**
     * 展示dialog
     */
    public void ShowNotCloseDialog(String text, String update) {
        if (context == null) {
            return;
        }
        try {
            final MyDialog dialog = new MyDialog(context, R.style.tipDialog, R.layout.dialog_tishi);
            dialog.show();
            PublicTools.setTipDialogSize(context, dialog);
            dialog.setCancelable(false);

            TextView queding = (TextView) dialog.findViewById(R.id.id_queding);
            TextView tex = (TextView) dialog.findViewById(R.id.id_tex);
            tex.setText(text);
            //切换字体
            TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(queding, "fonts/gilroy_medium.ttf");
            queding.setText(update);
            queding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dialog.dismiss();
                    if (guanBi != null) {
                        guanBi.GuanBi();
                    }

                }
            });
        } catch (Exception e) {

        }
    }

    public void showStripeDialog() {
        final MyDialog dialog = new MyDialog(context, R.style.tipDialog, R.layout.dialog_tishi);
        dialog.show();
        PublicTools.setTipDialogSize2(context, dialog);
        dialog.setCancelable(false);

        TextView id_title = (TextView) dialog.findViewById(R.id.id_title);
        id_title.setText("Payments update");
        TypefaceUtil.replaceFont(id_title, "fonts/gilroy_semiBold.ttf");
        id_title.setVisibility(View.VISIBLE);

        TextView tex = (TextView) dialog.findViewById(R.id.id_tex);
        tex.setPadding(0,0,0,0);
        tex.setTextSize(14.5f);
        tex.setText("Good news! We’ve upgraded how\nour payments work.\nPlease re-attach your bank card to\ncontinue using Payo");
        TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
        TextView id_cancel = (TextView) dialog.findViewById(R.id.id_cancel);
        id_cancel.setText("I’ll do it later");
        id_cancel.setVisibility(View.VISIBLE);
        id_cancel.setFocusable(false);
        id_cancel.setFocusableInTouchMode(false);
        id_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (guanBiLeft != null)
                    guanBiLeft.GuanBiLeft();
            }
        });
        TypefaceUtil.replaceFont(id_cancel, "fonts/gilroy_medium.ttf");

        TextView queding = (TextView) dialog.findViewById(R.id.id_queding);
        queding.setText("Ok");
        dialog.findViewById(R.id.line_vertical).setVisibility(View.VISIBLE);

        TypefaceUtil.replaceFont(queding, "fonts/gilroy_medium.ttf");
        queding.setFocusable(false);
        queding.setFocusableInTouchMode(false);
        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (guanBi != null) {
                    guanBi.GuanBi();
                    Intent intentBank = new Intent(context, NewAddBankCardActivity.class);
                    intentBank.putExtra("from", "stripePopwindow");
                    context.startActivity(intentBank);
                }
            }
        });
    }
}
