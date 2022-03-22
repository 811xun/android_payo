package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.BorrowByPayDayV2Data;
import com.home.glx.uwallet.datas.UserRepayFeeData;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

public class RepayMerchantsView {
    private View thisView;
    private Context context;
    private LayoutInflater inflater;
    private UserRepayFeeData.OrderDataBean data;
    private TextView merchantName;
    private TextView amount;
    private String lateFeeAmount;

    public RepayMerchantsView(Context context, UserRepayFeeData.OrderDataBean data) {
        this.context = context;
        this.data = data;
        init();
    }

    public RepayMerchantsView(Context context, String lateFeeAmount) {
        this.context = context;
        this.lateFeeAmount = lateFeeAmount;
        init();
    }

    public View getView() {
        return thisView;
    }

    private void init() {
        inflater = LayoutInflater.from(context);
        thisView = inflater.inflate(R.layout.view_repay_merchants, null);
        merchantName = thisView.findViewById(R.id.merchant_name);
        amount = thisView.findViewById(R.id.amount);
        //切换字体|
        TypefaceUtil.replaceFont(merchantName,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(amount,"fonts/gilroy_semiBold.ttf");
        if (!TextUtils.isEmpty(lateFeeAmount)) {
            merchantName.setText("Late fee");
            amount.setText("$" + PublicTools.twoend(lateFeeAmount));
        } else {
            merchantName.setText(data.getPracticalName());
            amount.setText("$" + PublicTools.twoend(data.getRepayAmount()));
        }
    }
}
