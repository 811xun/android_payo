package com.home.glx.uwallet.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.PayAllData;
import com.home.glx.uwallet.datas.PayDayBorrowInfoData;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.RepayMerchantResultView;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.io.File;
import java.io.IOException;

public class RepayResultActivity extends MainActivity {
    private PayAllData payAllData;
    private PayDayBorrowInfoData infoData;
    private LinearLayout contentLl;
    private TextView totalMoney;
    private TextView payTitle;
    private TextView done;
    private ImageView shareImg;
    private LinearLayout feeLl;
    private TextView transFee;
    private TextView transFeeAmount;

    @Override
    public int getLayout() {
        return R.layout.activity_repay_result;
    }

    @Override
    public void initView() {
        super.initView();
        shareImg = (ImageView) findViewById(R.id.share_img);
        feeLl = findViewById(R.id.fee_ll);
        transFee = findViewById(R.id.trans_fee);
        transFeeAmount = findViewById(R.id.fee_amount);
        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileImg = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/payo/";
                Bitmap bitmap = PublicTools.screenShot(getWindow());
                File file = null;
                try {
                    file = PublicTools.saveBitmapToFile(RepayResultActivity.this, bitmap, fileImg + System.currentTimeMillis()+"_result.png", new Handler());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file != null) {
                    Uri uri = PublicTools.getUriFromFile(RepayResultActivity.this, file);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setType("image/*");
                    startActivity(Intent.createChooser(intent, "Share & Earn PAYO"));
                }
            }
        });
        done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SharePreferenceUtils(RepayResultActivity.this, StaticParament.USER).put(StaticParament.PAY_SUCCESS, "1").commit();//为了wallet页面刷新

                Intent intent = new Intent(RepayResultActivity.this, MainTab.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        payTitle = (TextView) findViewById(R.id.pay_success_title);
        if (getIntent().getStringExtra("result") != null) {
            payTitle.setText("Payment successful");
        }
        payAllData = (PayAllData) getIntent().getSerializableExtra("payAllData");
        infoData = (PayDayBorrowInfoData) getIntent().getSerializableExtra("infoData");
        contentLl = findViewById(R.id.content_ll);
        totalMoney = findViewById(R.id.money);
        TextView total = findViewById(R.id.total);
        //切换字体
        TypefaceUtil.replaceFont(done,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(totalMoney,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(total,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(payTitle,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(transFee,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(transFeeAmount,"fonts/gilroy_medium.ttf");

        if (payAllData != null) {
            if (TextUtils.isEmpty(payAllData.getTransFee())) {
                feeLl.setVisibility(View.GONE);
            }
            transFeeAmount.setText("$" + payAllData.getTransFee());
            for (int i = 0; i < payAllData.getSuccessList().size(); i++) {
                RepayMerchantResultView view = new RepayMerchantResultView(this, payAllData.getSuccessList().get(i));
                contentLl.addView(view.getView());
            }
            totalMoney.setText("$" + PublicTools.twoend(payAllData.getTotalAmount()));
        }

        if (infoData != null) {
            if (TextUtils.isEmpty(infoData.getTransFee())) {
                feeLl.setVisibility(View.GONE);
            }
            transFeeAmount.setText("$" + infoData.getTransFee());
            for (int i = 0; i < infoData.getSuccessList().size(); i++) {
                RepayMerchantResultView view = new RepayMerchantResultView(this, infoData.getSuccessList().get(i));
                contentLl.addView(view.getView());
            }
            totalMoney.setText("$" + PublicTools.twoend(infoData.getTotalAmount()));
        }

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