package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.PdfActivity;
import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by GLX on 2017/5/18.
 */

public class ThreeTermsAndConditionDialog {
    private Context context;
    private List<ChoiceDatas> htmlList = new ArrayList<>();

    public ThreeTermsAndConditionDialog(Context context) {
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
    public void ShowDialog(String text, List<ChoiceDatas> htmlList) {
        if (context == null) {
            return;
        }
        this.htmlList = htmlList;
        try {
            final MyDialog dialog = new MyDialog(context, R.style.tipDialog, R.layout.dialog_three_terms_and_condition);
            dialog.show();
            PublicTools.setDialogSize(context, dialog);
            dialog.setCancelable(false);
            final TextView queding = (TextView) dialog.findViewById(R.id.btn_get_code);
            TypefaceUtil.replaceFont(queding, "fonts/gilroy_semiBold.ttf");
            final CheckBox id_protocols1 = dialog.findViewById(R.id.id_protocols1);
            final CheckBox id_protocols2 = dialog.findViewById(R.id.id_protocols2);
            final CheckBox id_protocols3 = dialog.findViewById(R.id.id_protocols3);
            id_protocols1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && id_protocols2.isChecked() && id_protocols3.isChecked()) {
                        queding.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_27orange));
                    } else {
                        queding.setBackground(ContextCompat.getDrawable(context, R.mipmap.btn_hui_jianbian));

                    }
                }
            });
            id_protocols2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && id_protocols1.isChecked() && id_protocols3.isChecked()) {
                        queding.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_27orange));
                    } else {
                        queding.setBackground(ContextCompat.getDrawable(context, R.mipmap.btn_hui_jianbian));

                    }
                }
            });

            id_protocols3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && id_protocols1.isChecked() && id_protocols2.isChecked()) {
                        queding.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_27orange));
                    } else {
                        queding.setBackground(ContextCompat.getDrawable(context, R.mipmap.btn_hui_jianbian));
                    }
                }
            });

            //切换字体
            TypefaceUtil.replaceFont(dialog.findViewById(R.id.id_title), "fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(dialog.findViewById(R.id.proText1), "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(dialog.findViewById(R.id.proText2), "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(dialog.findViewById(R.id.proText3), "fonts/gilroy_medium.ttf");

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
                    if (id_protocols1.isChecked() && id_protocols2.isChecked() && id_protocols3.isChecked()) {
                        dialog.dismiss();
                        guanBi.queding();
                    }
                }
            });

            initFirstTextView(dialog);
            initSecondTextView(dialog);
            initThirdTextView(dialog);

        } catch (Exception e) {

        }
    }

    private void initFirstTextView(MyDialog dialog) {
        ((TextView) dialog.findViewById(R.id.proText1)).setHighlightColor(ContextCompat.getColor(context,android.R.color.transparent));

        SpannableString spannableString = new SpannableString("I have read and agree the Privacy Agreement.");
        MyClickableSpan1 clickableSpan = new MyClickableSpan1();
        spannableString.setSpan(clickableSpan, "I have read and agree the ".length(), spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(context.getResources().getColor(R.color.colorBackGround));
        spannableString.setSpan(colorSpan1, "I have read and agree the ".length(), spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ((TextView) dialog.findViewById(R.id.proText1)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) dialog.findViewById(R.id.proText1)).setText(spannableString);
    }

    private void initSecondTextView(MyDialog dialog) {
        ((TextView) dialog.findViewById(R.id.proText2)).setHighlightColor(ContextCompat.getColor(context,android.R.color.transparent));

        SpannableString spannableString = new SpannableString("I have read and agree the Pay Now Customer Application Form and Pay Now Terms and Conditions.");

        MyClickableSpan2 clickableSpan2 = new MyClickableSpan2();
        spannableString.setSpan(clickableSpan2, "I have read and agree the ".length(), "I have read and agree the Pay Now Customer Application Form ".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        MyClickableSpan3 clickableSpan3 = new MyClickableSpan3();
        spannableString.setSpan(clickableSpan3, "I have read and agree the Pay Now Customer Application Form and ".length(), spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(context.getResources().getColor(R.color.colorBackGround));
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(context.getResources().getColor(R.color.colorBackGround));
        spannableString.setSpan(colorSpan1, "I have read and agree the ".length(), "I have read and agree the Pay Now Customer Application Form ".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan2, "I have read and agree the Pay Now Customer Application Form and ".length(), spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ((TextView) dialog.findViewById(R.id.proText2)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) dialog.findViewById(R.id.proText2)).setText(spannableString);
    }

    private void initThirdTextView(MyDialog dialog) {
        ((TextView) dialog.findViewById(R.id.proText3)).setHighlightColor(ContextCompat.getColor(context,android.R.color.transparent));

        SpannableString spannableString = new SpannableString("I have read and agree the Pay Later Customer Application Form and Pay\n Later Terms and Conditions.");
        MyClickableSpan4 clickableSpan4 = new MyClickableSpan4();
        spannableString.setSpan(clickableSpan4, "I have read and agree the ".length(), "I have read and agree the Pay Later Customer Application Form ".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        MyClickableSpan5 clickableSpan5 = new MyClickableSpan5();
        spannableString.setSpan(clickableSpan5, "I have read and agree the Pay Later Customer Application Form and ".length(), spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(context.getResources().getColor(R.color.colorBackGround));
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(context.getResources().getColor(R.color.colorBackGround));
        spannableString.setSpan(colorSpan1, "I have read and agree the ".length(), "I have read and agree the Pay Later Customer Application Form ".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(colorSpan2, "I have read and agree the Pay Later Customer Application Form and ".length(), spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ((TextView) dialog.findViewById(R.id.proText3)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView) dialog.findViewById(R.id.proText3)).setText(spannableString);
    }

    class MyClickableSpan1 extends ClickableSpan {

        public MyClickableSpan1() {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            L.log("点击");
            Intent intent = new Intent();
            intent.setClass(context, PdfActivity.class);
            intent.putExtra("url", htmlList.get(0).getCName());
            context.startActivity(intent);
        }
    }

    class MyClickableSpan2 extends ClickableSpan {

        public MyClickableSpan2() {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            L.log("点击");
            Intent intent = new Intent();
            intent.setClass(context, PdfActivity.class);
            intent.putExtra("url", htmlList.get(1).getCName());
            context.startActivity(intent);
        }
    }

    class MyClickableSpan3 extends ClickableSpan {

        public MyClickableSpan3() {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            L.log("点击");
            Intent intent = new Intent();
            intent.setClass(context, PdfActivity.class);
            intent.putExtra("url", htmlList.get(2).getCName());
            context.startActivity(intent);
        }
    }

    class MyClickableSpan4 extends ClickableSpan {

        public MyClickableSpan4() {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            L.log("点击");
            Intent intent = new Intent();
            intent.setClass(context, PdfActivity.class);
            intent.putExtra("url", htmlList.get(3).getCName());
            context.startActivity(intent);
        }
    }

    class MyClickableSpan5 extends ClickableSpan {

        public MyClickableSpan5() {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            L.log("点击");
            Intent intent = new Intent();
            intent.setClass(context, PdfActivity.class);
            intent.putExtra("url", htmlList.get(4).getCName());
            context.startActivity(intent);
        }
    }

}
