package com.home.glx.uwallet.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.RepaymentSuccessData;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.io.File;
import java.io.IOException;

public class RepaySelectResultActivity extends MainActivity {
    private TextView done;
    private TextView payTitle;
    private RepaymentSuccessData data;
    private TextView merchantName;
    private TextView orderAmount;
    private TextView feeAmount;
    private LinearLayout lateLl;
    private TextView lateAmount;
    private TextView time;
    private TextView transitionNo;
    private TextView trueMoney;
    private TextView receiver;
    private TextView paymentMethod;
    private TextView orderMount;
    private TextView transFee;
    private TextView laFee;
    private TextView dateText;
    private TextView order_no;
    private TextView totalText;
    private TextView cardType;
    private ImageView shareImg;

    @Override
    public int getLayout() {
        return R.layout.activity_repay_select_result;
    }

    @Override
    public void initView() {
        super.initView();
        shareImg = (ImageView) findViewById(R.id.share_img);
        receiver = findViewById(R.id.receiver);
        paymentMethod = findViewById(R.id.payment_method);
        cardType = findViewById(R.id.card_type);
        orderMount = findViewById(R.id.order_mount);
        transFee = findViewById(R.id.trans_fee);
        laFee = findViewById(R.id.la_fee);
        lateLl = findViewById(R.id.lete_fee_ll);
        dateText = findViewById(R.id.date_text);
        order_no = findViewById(R.id.order_no);
        totalText = findViewById(R.id.total_text);
        payTitle = (TextView) findViewById(R.id.pay_success_title);
        merchantName = findViewById(R.id.merchant_name);
        orderAmount = findViewById(R.id.order_amount);
        feeAmount = findViewById(R.id.fee_amount);
        lateAmount = findViewById(R.id.late_amount);
        time = findViewById(R.id.time);
        transitionNo = findViewById(R.id.transition_no);
        trueMoney = findViewById(R.id.true_money);
        done = findViewById(R.id.done);

        //切换字体
        TypefaceUtil.replaceFont(payTitle,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(totalText,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(receiver,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(merchantName,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(paymentMethod,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(cardType,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(orderMount,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(orderAmount,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(transFee,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(feeAmount,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(laFee,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(lateAmount,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(dateText,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(time,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(order_no,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(transitionNo,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(totalText,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(trueMoney,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(done,"fonts/gilroy_semiBold.ttf");

        if (getIntent().getStringExtra("result") != null) {
            payTitle.setText("Payment successful");
        }
        data = (RepaymentSuccessData) getIntent().getSerializableExtra("data");
        if (data != null) {
            merchantName.setText(data.getTradingName());
            orderAmount.setText("$" + PublicTools.twoend(data.getInstallmentAmount()));
            if (data.getTansFee() != null && Float.parseFloat(data.getTansFee()) == 0) {
                transFee.setVisibility(View.GONE);
                feeAmount.setVisibility(View.GONE);
            } else {
                feeAmount.setText("$" + PublicTools.twoend(data.getTansFee()));
            }

            time.setText(data.getDateStr());
            if (data.getOverdueAmount() != null && Float.parseFloat(data.getOverdueAmount()) != 0) {
                lateAmount.setText("$" + PublicTools.twoend(data.getOverdueAmount()));
            } else {
                lateLl.setVisibility(View.GONE);
            }
            transitionNo.setText(data.getTransNo());
            trueMoney.setText("$" + PublicTools.twoend(data.getTotalAmount()));
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepaySelectResultActivity.this, MainTab.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileImg = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/payo/";
                Bitmap bitmap = PublicTools.screenShot(getWindow());
                File file = null;
                try {
                    file = PublicTools.saveBitmapToFile(RepaySelectResultActivity.this, bitmap, fileImg + System.currentTimeMillis()+"_result.png", new Handler());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file != null) {
                    Uri uri = PublicTools.getUriFromFile(RepaySelectResultActivity.this, file);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setType("image/*");
                    startActivity(Intent.createChooser(intent, "Share & Earn PAYO"));
                }
            }
        });
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            //禁用返回键
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}