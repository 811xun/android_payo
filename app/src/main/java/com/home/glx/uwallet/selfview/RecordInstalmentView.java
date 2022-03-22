package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.tools.TypefaceUtil;

public class RecordInstalmentView {

    private View thisView;
    private Context context;
    private LayoutInflater inflater;
    private String date;
    private String money;
    private TextView dateView;
    private TextView moneyView;
    private TextView statusView;
    private ImageView statusImg;
    private int status;


    public RecordInstalmentView(Context context, String date, String money, int status) {
        this.context = context;
        this.date = date;
        this.money = money;
        this.status = status;
        init();
    }


    private void init() {
        inflater = LayoutInflater.from(context);
        thisView = inflater.inflate(R.layout.view_record_instalment, null);
        dateView = thisView.findViewById(R.id.date);
        moneyView = thisView.findViewById(R.id.money);
        statusView = thisView.findViewById(R.id.status);
        statusImg = thisView.findViewById(R.id.icon);
        //切换字体
        TypefaceUtil.replaceFont(dateView, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(moneyView, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(statusView, "fonts/gilroy_semiBold.ttf");

        dateView.setText(date);
        moneyView.setText(money);
        //分期付还款计划还款状态, 结清状态 0：未结清，1：已结清 2：处理中 3：Refund 4：Cancelled
        if (status == 1) {
            statusImg.setImageResource(R.mipmap.record_choice_on);
            statusView.setText("Completed");
        } else if (status == 2) {
            statusImg.setImageResource(R.mipmap.repay_choice_of);
            statusView.setText("Processing");
        } else if (status == 3) {
            statusImg.setImageResource(R.mipmap.record_choice_on);
            statusView.setText("Refund");
        } else if (status == 4) {
            statusImg.setImageResource(R.mipmap.repay_choice_of);
            statusView.setText("Cancelled");
            statusView.setTextColor(ContextCompat.getColor(context,R.color.color_717171));
        } else {
            statusImg.setImageResource(R.mipmap.repay_choice_of);
        }
    }

    public View getView() {
        return thisView;
    }

}
