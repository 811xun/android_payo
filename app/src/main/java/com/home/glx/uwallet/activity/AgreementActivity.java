package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.datas.GetChoiceDatas;
import com.home.glx.uwallet.mvp.GetChoiceList_in;
import com.home.glx.uwallet.mvp.GetChoiceList_p;
import com.home.glx.uwallet.mvp.WhetherOpenInvest_in;
import com.home.glx.uwallet.mvp.WhetherOpenInvest_p;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgreementActivity extends MainActivity implements GetChoiceList_in.View,
        WhetherOpenInvest_in.View, View.OnClickListener {
    private GetChoiceList_p choiceList_p;
    private List<ChoiceDatas> htmlList = new ArrayList<>();
    //是否开通个业务
    private WhetherOpenInvest_p openInvestPresent;

    private RelativeLayout html1,html2,html3,html4, html5;
    private TextView text1,text2,text3,text4, text5;
    private ImageView back;
    @Override
    public int getLayout() {
        return R.layout.activity_agreement;
    }

    @Override
    public void initView() {
        super.initView();
        html1 = (RelativeLayout) findViewById(R.id.html1);
        html2 = (RelativeLayout) findViewById(R.id.html2);
        html3 = (RelativeLayout) findViewById(R.id.html3);
        html4 = (RelativeLayout) findViewById(R.id.html4);
        html5 = (RelativeLayout) findViewById(R.id.html5);
        text1 = (TextView) findViewById(R.id.html_text1);
        text2 = (TextView) findViewById(R.id.html_text2);
        text3 = (TextView) findViewById(R.id.html_text3);
        text4 = (TextView) findViewById(R.id.html_text4);
        text5 = (TextView) findViewById(R.id.html_text5);
        back = (ImageView) findViewById(R.id.id_back);
        TextView agreementTitle = findViewById(R.id.agreement_title);

        //切换字体
        TypefaceUtil.replaceFont(agreementTitle,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(text1,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(text2,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(text3,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(text4,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(text5,"fonts/gilroy_regular.ttf");

        back.setOnClickListener(this);
        choiceList_p = new GetChoiceList_p(this, this);
        openInvestPresent = new WhetherOpenInvest_p(this, this);
        Map<String, Object> maps = new HashMap<>();
        String[] keys = new String[]{/*"termAndConditions", "privacyAgreement", "customerApplicationFormULaypay", "directDebitTerms"*/
                "privacyAgreementNew", "payLaterApplicationForm", "payLaterTermsConditions", "payNowApplication", "payNowTermsConditions"

        };
        maps.put("code", keys);
        choiceList_p.loadChoiceList(maps);
        //openInvestPresent.loadOpenInvestStatus("kycStaus");
    }

    @Override
    public void setChoiceList(GetChoiceDatas getChoiceDatas) {
        if (getChoiceDatas!= null) {
            htmlList.addAll(getChoiceDatas.getPrivacyAgreementNew());
            htmlList.addAll(getChoiceDatas.getPayLaterApplicationForm());
            htmlList.addAll(getChoiceDatas.getPayLaterTermsConditions());
            htmlList.addAll(getChoiceDatas.getPayNowApplication());
            htmlList.addAll(getChoiceDatas.getPayNowTermsConditions());
            text1.setText(htmlList.get(0).getEName());
            text2.setText(htmlList.get(1).getEName());
            text3.setText(htmlList.get(2).getEName());
            text4.setText(htmlList.get(3).getEName());
            text5.setText(htmlList.get(4).getEName());
            html1.setOnClickListener(this);
            html2.setOnClickListener(this);
            html3.setOnClickListener(this);
            html4.setOnClickListener(this);
            html5.setOnClickListener(this);
        } else {
            html1.setVisibility(View.GONE);
            html2.setVisibility(View.GONE);
            html3.setVisibility(View.GONE);
            html4.setVisibility(View.GONE);
            html5.setVisibility(View.GONE);
        }
    }

    @Override
    public void openInvestStatus(String code, String licai, String zhifu, String fenqi) {
        if (zhifu != null) {
            //appUser 0:前两条；1：全展示；其他三条
            switch (fenqi) {
                case "0":
                    html3.setVisibility(View.GONE);
                    html4.setVisibility(View.GONE);
                    break;
                case "1":
                    break;
                default:
                    html4.setVisibility(View.GONE);
            }
            }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (!PublicTools.isFastClick()) {
            return;
        }
        switch (id) {
            case R.id.html1:
                Intent intent = new Intent(this, PdfActivity.class);
                intent.putExtra("url", htmlList.get(0).getCName());
                startActivity(intent);
                break;
            case R.id.html2:
                Intent intent1 = new Intent(this, PdfActivity.class);
                intent1.putExtra("url", htmlList.get(1).getCName());
                startActivity(intent1);
                break;
            case R.id.html3:
                Intent intent2 = new Intent(this, PdfActivity.class);
                intent2.putExtra("url", htmlList.get(2).getCName());
                startActivity(intent2);
                break;
            case R.id.html4:
                Intent intent3 = new Intent(this, PdfActivity.class);
                intent3.putExtra("url", htmlList.get(3).getCName());
                startActivity(intent3);
                break;
            case R.id.html5:
                Intent intent4 = new Intent(this, PdfActivity.class);
                intent4.putExtra("url", htmlList.get(4).getCName());
                startActivity(intent4);
                break;
            case R.id.id_back:
                finish();
                break;
        }
    }
}
