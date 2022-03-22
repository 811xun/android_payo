package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.xzc.PaySuccessActivity_Zhifu;
import com.home.glx.uwallet.datas.RepaySuccessData;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.io.File;
import java.io.IOException;

public class RepaySuccessActivity extends MainActivity {

    private TextView merchantName;
    private TextView orderNo;
    private TextView date;
    private TextView cardNo;
    private TextView totalAmount;
    private TextView feeAmount;
    private TextView totalPaid;
    private TextView totalPaidAmount;
    private TextView done;
    private String repay;
    private RepaySuccessData data;
    private ImageView cardLogo;
    private TextView title;
    private ImageView shareImg;
    private String feeDate;
    private TextView empty;
    private TextView emptyFeeTwo;
    private TextView emptyFeeOne;
    private TextView total;

    @Override
    public int getLayout() {
        return R.layout.activity_repay_success;
    }

    @Override
    public void initView() {
        super.initView();
        //实现wallet页面的刷新
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(RepaySuccessActivity.this, StaticParament.USER);
        sharePreferenceUtils.put(StaticParament.PAY_SUCCESS, "1").commit();
        feeDate = getIntent().getStringExtra("feeDate");
        data = (RepaySuccessData) getIntent().getSerializableExtra("data");
        repay = getIntent().getStringExtra("repay");
        title = findViewById(R.id.title);
//        if (data.getState().equals("100300")) {
//            //title.setText("Payment processing");
//        }
        shareImg = findViewById(R.id.share_img);
        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileImg = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/payo/";
                Bitmap bitmap = PublicTools.screenShot(getWindow());
                File file = null;
                try {
                    file = PublicTools.saveBitmapToFile(RepaySuccessActivity.this, bitmap, fileImg + System.currentTimeMillis() + "_result.png", new Handler());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file != null) {
                    Uri uri = PublicTools.getUriFromFile(RepaySuccessActivity.this, file);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setType("image/*");
                    startActivity(Intent.createChooser(intent, "Share & Earn PAYO"));
                }
            }
        });
        empty = findViewById(R.id.empty);
        emptyFeeTwo = findViewById(R.id.empty_fee_two);
        emptyFeeOne = findViewById(R.id.empty_fee_one);
        merchantName = findViewById(R.id.merchant_name);
        orderNo = findViewById(R.id.order_no);
        TextView paidOn = findViewById(R.id.paid_on);
        date = findViewById(R.id.date);
        TextView payWith = findViewById(R.id.pay_with);
        cardNo = findViewById(R.id.default_card_no);
        cardLogo = findViewById(R.id.default_card_logo);
        total = findViewById(R.id.total);
        totalAmount = findViewById(R.id.total_amount);
        TextView fee = findViewById(R.id.fee);
        feeAmount = findViewById(R.id.fee_amount);
        totalPaid = findViewById(R.id.total_paid);
        totalPaidAmount = findViewById(R.id.total_paid_amount);
        if (data != null) {
            if (repay == null) {
                merchantName.setText(data.getTradingName());
                orderNo.setText(data.getTransNo());
            } else if (repay.equals("fee")) {
                total.setText("Total late fee");
                if (feeDate != null) {
                    merchantName.setText("Late fee for\n" + feeDate);
                    emptyFeeTwo.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                } else {
                    merchantName.setText("Late fee");
                    emptyFeeOne.setVisibility(View.VISIBLE);
                    empty.setVisibility(View.GONE);
                }
            } else if (repay.equals("all")) {
                merchantName.setText("Pay total outstanding");
                total.setText("Total outstanding");
            } else if (repay.equals("allDate")) {
                merchantName.setText("Pay total outstanding");
            }
            date.setText(data.getDateStr());
            totalAmount.setText("$" + PublicTools.twoend(data.getRepayAmount()));
            if (repay != null && repay.equals("fee") && feeDate == null) {

            } else {
                if (!TextUtils.isEmpty(data.getChargeRate())) {
                    fee.setText(String.format("Transaction fee (%s)", data.getChargeRate() + "%"));
                }
            }
            feeAmount.setText("$" + PublicTools.twoend(data.getTansFee()));
            totalPaidAmount.setText("$" + PublicTools.twoend(data.getTotalAmount()));
            if (data.getCardNo() != null) {
                cardNo.setText(data.getCardNo());
            }
            if (data.getCardCCType() != null) {
                if (data.getCardCCType().equals("10")) {
                    cardLogo.setImageResource(R.mipmap.pay_visa_logo);
                } else if (data.getCardCCType().equals("20")) {
                    cardLogo.setImageResource(R.mipmap.pay_master_logo);
                } else if (data.getCardCCType().equals("60")){
                    cardLogo.setImageResource(R.mipmap.pay_onther_logo);
                }else {
                    cardLogo.setVisibility(View.INVISIBLE);
                }
            }
        }
        done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepaySuccessActivity.this, MainTab.class);
                intent.putExtra("num", 2);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        //切换字体
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(done, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(merchantName, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(orderNo, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(paidOn, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(date, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(payWith, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(cardNo, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(total, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(totalAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(fee, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(feeAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(totalPaid, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(totalPaidAmount, "fonts/gilroy_bold.ttf");
    }

    @Override
    public void onBackPressed() {
        // 完全由自己控制返回键逻辑，系统不再控制，但是有个前提是：　　
        // 不要在Activity的onKeyDown或者OnKeyUp中拦截掉返回键　　
        // 拦截：就是在OnKeyDown或者OnKeyUp中自己处理了返回键　　
        // （这里处理之后return true.或者return false都会导致onBackPressed不会执行）
        // 不拦截：在OnKeyDown和OnKeyUp中返回super对应的方法　　
        // （如果两个方法都被覆写就分别都要返回super.onKeyDown,super.onKeyUp）
        Intent intent = new Intent(RepaySuccessActivity.this, MainTab.class);
        intent.putExtra("num", 2);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}