package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.PdfActivity;
import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.datas.GetChoiceDatas;
import com.home.glx.uwallet.mvp.GetChoiceList_in;
import com.home.glx.uwallet.mvp.GetChoiceList_p;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddBankCardTipDialog implements GetChoiceList_in.View {
    private MyDialog dialog;
    private Context context;
    private TextView titleView;
    private TextView textView;
    private TextView msgView;
    private TextView msg1;
    private ImageView close;
    private TextView addBank;
    private List<ChoiceDatas> htmlList = new ArrayList<>();
    private GetChoiceList_p choiceList_p;
    private boolean showAddBankBtn = true;

    public AddBankCardTipDialog(Context context) {
        this.context = context;
    }

    public void showAddBankBtn(boolean showAddBankBtn) {
        this.showAddBankBtn = showAddBankBtn;
    }

    public void show() {
        if (showAddBankBtn) {
            dialog = new MyDialog(context, R.style.MyRadiusDialog, R.layout.dialog_add_bank_card_tip);
        } else {
            dialog = new MyDialog(context, R.style.MyRadiusDialog2, R.layout.dialog_add_bank_card_tip);

        }
        dialog.show();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        choiceList_p = new GetChoiceList_p(context, this);
        titleView = dialog.findViewById(R.id.title);
        textView = dialog.findViewById(R.id.text);
        msgView = dialog.findViewById(R.id.msg);
        msg1 = dialog.findViewById(R.id.msg1);
        close = dialog.findViewById(R.id.close);
        addBank = dialog.findViewById(R.id.add_bank);
        if (showAddBankBtn) {
            addBank.setVisibility(View.VISIBLE);
        } else {
            addBank.setVisibility(View.GONE);

        }
        Map<String, Object> maps = new HashMap<>();
        String[] keys = new String[]{"payNowTermsConditions"};
        maps.put("code", keys);
        choiceList_p.loadChoiceList(maps);

        SpannableString spannableString = new SpannableString("We’re always looking for ways to improve the experience for Payo customers. We’ve updated our payment terms with more flexible payment options. We are moving from 4 weekly payments to 25% paid at the time of the transaction and 3 remaining payments of 25% paid fortnightly. To proceed with your order, you’ll need to add a debit card or credit card. To read more, you can view our updated Terms of Service.");
        MyClickableSpan clickableSpan = new MyClickableSpan();
        spannableString.setSpan(clickableSpan, spannableString.length() - 18, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(context.getResources().getColor(R.color.colorBackGround));
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(Color.BLACK);
        spannableString.setSpan(colorSpan2, 0, spannableString.length() - 18, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan1, spannableString.length() - 18, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        msgView.setMovementMethod(LinkMovementMethod.getInstance());
        msgView.setText(spannableString);

        //切换字体
        TypefaceUtil.replaceFont(titleView, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(textView, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(msgView, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(msg1, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(addBank, "fonts/gilroy_semiBold.ttf");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        addBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onitemClick.itemClick();
            }
        });
    }

    class MyClickableSpan extends ClickableSpan {

        public MyClickableSpan() {

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
            if (htmlList.size() != 0) {
                intent.putExtra("url", htmlList.get(0).getCName());
            }
            context.startActivity(intent);
        }
    }

    @Override
    public void setChoiceList(GetChoiceDatas getChoiceDatas) {
        if (getChoiceDatas != null) {//firebase : Attempt to invoke virtual method 'java.util.List com.home.glx.uwallet.datas.GetChoiceDatas.getPayNowTermsConditions()' on a null object reference
            htmlList.addAll(getChoiceDatas.getPayNowTermsConditions());
        }
    }

    public interface OnitemClick {
        void itemClick();
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }
}
