package com.home.glx.uwallet.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.FeeTransactionData;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;
import java.util.Map;

public class LateFeeRecordDetailActivity extends MainActivity {

    private ImageView idBack;
    private TextView date;
    private TextView lateFeeAmount;
    private TextView transFee;
    private TextView transFeeAmount;
    private TextView totalAmount;
    private TextView status;
    private UserListener userListener;
    private String id;

    @Override
    public int getLayout() {
        return R.layout.activity_late_fee_record_detail;
    }

    @Override
    public void initView() {
        super.initView();
        id = getIntent().getStringExtra("id");
        userListener = new UserModel(this);
        idBack = findViewById(R.id.id_back);
        idBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = findViewById(R.id.title);
        TextView feeFor = findViewById(R.id.fee_for);
        date = findViewById(R.id.date);
        TextView lateFee = findViewById(R.id.late_fee);
        lateFeeAmount = findViewById(R.id.late_fee_amount);
        transFee = findViewById(R.id.trans_fee);
        transFeeAmount = findViewById(R.id.trans_fee_amount);
        TextView total = findViewById(R.id.total);
        totalAmount = findViewById(R.id.total_amount);
        TextView termsOf = findViewById(R.id.terms_of);
        TextView terms = findViewById(R.id.terms);
        TextView statusText = findViewById(R.id.status_text);
        status = findViewById(R.id.status);
        Map<String, Object> maps = new HashMap<>();
        maps.put("no_user_id", 1);
        maps.put("id", id);
        userListener.getTransactionRecordById(maps, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                FeeTransactionData data = (FeeTransactionData) o[0];
                date.setText(data.getDisplayDate());
                lateFeeAmount.setText("$" + PublicTools.twoend(data.getTransAmount()));
                if (!TextUtils.isEmpty(data.getFee())) {
                    transFee.setText(String.format("Transaction fee (%s)", CalcTool.mm(data.getFee(), "100") + "%"));
                }
                transFeeAmount.setText("$" + PublicTools.twoend(data.getTradingFee()));
                totalAmount.setText("$" + PublicTools.twoend(data.getPayAmount()));
                if (data.getTransState().equals("0")) {
                    status.setText("Unsettled");
                } else if (data.getTransState().equals("1")) {
                    status.setText("Settled");
                } else if (data.getTransState().equals("2")) {
                    status.setText("Processing");
                }
            }

            @Override
            public void onError(String e) {

            }
        });
        //切换字体
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(feeFor, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(date, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(lateFee, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(lateFeeAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(transFee, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(transFeeAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(total, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(totalAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(termsOf, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(terms, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(statusText, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(status, "fonts/gilroy_semiBold.ttf");
    }
}