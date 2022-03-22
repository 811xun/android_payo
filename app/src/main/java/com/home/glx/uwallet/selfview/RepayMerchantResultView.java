package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.PayAllData;
import com.home.glx.uwallet.datas.PayDayBorrowInfoData;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

public class RepayMerchantResultView {
    private View thisView;
    private Context context;
    private LayoutInflater inflater;
    private TextView merchantName;
    private TextView money;
    private  PayAllData.SuccessListBean bean;
    private  PayDayBorrowInfoData.SuccessListBean listBean;

    public RepayMerchantResultView(Context context, PayAllData.SuccessListBean bean) {
        this.context = context;
        this.bean = bean;
        init();
    }

    public RepayMerchantResultView(Context context, PayDayBorrowInfoData.SuccessListBean listBean) {
        this.context = context;
        this.listBean = listBean;
        init();
    }

    public View getView() {
        return thisView;
    }

    private void init() {
        inflater = LayoutInflater.from(context);
        thisView = inflater.inflate(R.layout.view_repay_merchant_result, null);
        merchantName = thisView.findViewById(R.id.merchant_name);
        money = thisView.findViewById(R.id.money);

        //切换字体
        TypefaceUtil.replaceFont(merchantName,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(money,"fonts/gilroy_medium.ttf");

        if (bean != null) {
            merchantName.setText(bean.getTradingName());
            money.setText("$" + PublicTools.twoend(bean.getRepayAmount()));
        }
        if (listBean != null) {
            merchantName.setText(listBean.getTradingName());
            money.setText("$" + PublicTools.twoend(listBean.getRepayAmountX()));
        }
    }
}
