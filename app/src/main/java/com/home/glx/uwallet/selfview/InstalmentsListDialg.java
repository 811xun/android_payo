package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.RepayFQDatas;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.List;

public class InstalmentsListDialg {
    private Context context;
    private MyDialog dialog;
    private List<RepayFQDatas> list;

    private CashlineView line1;
    private CashlineView line2;
    private CashlineView line3;
    private LinearLayout term1;
    private LinearLayout term2;
    private LinearLayout term3;
    private LinearLayout term4;
    private TextView term1Money;
    private TextView term2Money;
    private TextView term3Money;
    private TextView term4Money;
    private TextView term1Date;
    private TextView term2Date;
    private TextView term3Date;
    private TextView term4Date;
    private TextView totalMoney;
    private LinearLayout instalmentFlag;
    private TextView back;
    private TextView instalmentNum;
    private ImageView close;
    private String totals;
    private  ConfirmPayBottomDialog confirmPayBottomDialog;

    public InstalmentsListDialg(Context context, ConfirmPayBottomDialog confirmPayBottomDialog, List<RepayFQDatas> list, String totals) {
        this.context = context;
        this.list = list;
        this.totals = totals;
        this.confirmPayBottomDialog = confirmPayBottomDialog;
    }

    public void disMiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void show() {
        dialog = new MyDialog(context, R.style.MyRadiusDialog, R.layout.dialog_instalments_list);
        dialog.show();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        close = (ImageView) dialog.findViewById(R.id.close);
        instalmentFlag = (LinearLayout) dialog.findViewById(R.id.instalment_flag);
        line1 = (CashlineView) dialog.findViewById(R.id.line1);
        line2 = (CashlineView) dialog.findViewById(R.id.line2);
        line3 = (CashlineView) dialog.findViewById(R.id.line3);
        term1 = (LinearLayout) dialog.findViewById(R.id.term1);
        term2 = (LinearLayout) dialog.findViewById(R.id.term2);
        term3 = (LinearLayout) dialog.findViewById(R.id.term3);
        term4 = (LinearLayout) dialog.findViewById(R.id.term4);
        term1Money = (TextView) dialog.findViewById(R.id.term1_money);
        term2Money = (TextView) dialog.findViewById(R.id.term2_money);
        term3Money = (TextView) dialog.findViewById(R.id.term3_money);
        term4Money = (TextView) dialog.findViewById(R.id.term4_money);
        term1Date = (TextView) dialog.findViewById(R.id.term1_date);
        term2Date = (TextView) dialog.findViewById(R.id.term2_date);
        term3Date = (TextView) dialog.findViewById(R.id.term3_date);
        term4Date = (TextView) dialog.findViewById(R.id.term4_date);
        totalMoney = (TextView) dialog.findViewById(R.id.total_money);
        back = (TextView) dialog.findViewById(R.id.back);
        instalmentNum = (TextView) dialog.findViewById(R.id.instalment_num);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView week1 = dialog.findViewById(R.id.week1);
        TextView week2 = dialog.findViewById(R.id.week2);
        TextView week3 = dialog.findViewById(R.id.week3);
        TextView week4 = dialog.findViewById(R.id.week4);
        TextView totalText = dialog.findViewById(R.id.total_text);

        //切换字体
        TypefaceUtil.replaceFont(instalmentNum,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(week1,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(week2,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(week3,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(week4,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term1Date,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term2Date,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term3Date,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term4Date,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term1Money,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(term2Money,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(term3Money,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(term4Money,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(totalText,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(totalMoney,"fonts/gilroy_bold.ttf");

        if (list.size() == 1) {
            instalmentNum.setText(String.format("Pay later with %d instalment", list.size()));
        } else {
            instalmentNum.setText(String.format("Pay later with %d instalments", list.size()));
        }

        if (list.size() == 3) {
            line3.setVisibility(View.GONE);
            term4.setVisibility(View.GONE);
        }
        if (list.size() == 2) {
            line2.setVisibility(View.GONE);
            term3.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            term4.setVisibility(View.GONE);
        }
        if (list.size() == 1) {
            line1.setVisibility(View.GONE);
            term2.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
            term3.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            term4.setVisibility(View.GONE);
        }
        if (list.size() == 0) {
            instalmentFlag.setVisibility(View.GONE);
        }
        for (int i = 0; i < list.size(); i++) {
            if (getTermMoney(i) != null) {
                getTermMoney(i).setText("$" + PublicTools.twoend(list.get(i).getShouldPayAmount()));
            }
            if (getTermDate(i) != null) {
                getTermDate(i).setText(list.get(i).getExpectRepayTime());
            }
        }
        totalMoney.setText("$" + totals);
        back.setText("Pay $" + totals);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPayBottomDialog.checkFenqiPay();
            }
        });
    }
    private TextView getTermMoney(int i) {
        if (i == 0) {
            return term1Money;
        }
        if (i == 1) {
            return term2Money;
        }
        if (i == 2) {
            return term3Money;
        }
        if (i == 3) {
            return term4Money;
        }
        return null;
    }

    private TextView getTermDate(int i) {
        if (i == 0) {
            return term1Date;
        }
        if (i == 1) {
            return term2Date;
        }
        if (i == 2) {
            return term3Date;
        }
        if (i == 3) {
            return term4Date;
        }
        return null;
    }
}
