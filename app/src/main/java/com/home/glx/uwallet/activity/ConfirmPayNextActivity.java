package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.braze.Braze;
import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.PaySuccessData;
import com.home.glx.uwallet.datas.TransAmountData;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.PayListener;
import com.home.glx.uwallet.mvp.model.PayModel;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.EnterPayPinDialog;
import com.home.glx.uwallet.selfview.PaymentWaitingDialog;
import com.home.glx.uwallet.tools.BzEventParameterName;
import com.home.glx.uwallet.tools.BzEventType;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmPayNextActivity extends MainActivity {

    private String merchantName;
    private String amount;
    private String feeRate;
    private String feeAmount;
    private ImageView back;
    private TextView merchantNameTv;
    private TextView title;
    private TextView orderAmountTv;
    private TextView orderAmount;
    private TextView feeTv;
    private TextView feeAmountTv;
    private TextView allTv;
    private TextView allAmount;
    private TextView next;
    private String allPayAmount;
    private String payType;
    private PayListener payListener;
    private HashMap<String, Object> qrPayRequestMap;
    private HashMap<String, Object> requestMap;
    private SharePreferenceUtils sharePreferenceUtils;
    private PaymentWaitingDialog paymentWaitingDialog;
    private String donationAmount;
    private LinearLayout donateLl;
    private TextView donateAmount;
    private TextView donateAmountTv;
    private LinearLayout tipLl;
    private TextView tipAmount;
    private TextView tipTv;
    private String tipAmounts;
    private ConstraintLayout insCl;
    private TextView cardTitle;
    private TextView cardAmountTv;
    private TextView cardAmount;
    private TextView discountTv;
    private TextView discountAmount;
    private TextView pocketTv;
    private TextView pocketAmount;
    private TextView cardDonateAmountTv;
    private TextView cardDonateAmount;
    private TextView cardTipTv;
    private TextView cardTipAmount;
    private TextView cardFeeTv;
    private TextView cardFeeAmount;
    private TextView cardAllTv;
    private TextView cardAllAmount;
    private ConstraintLayout cardCl;
    private TransAmountData data;
    private LinearLayout discountLl;
    private LinearLayout pocketLl;
    private LinearLayout cardDonateLl;
    private LinearLayout cardTipLl;

    @Override
    public int getLayout() {
        return R.layout.activity_confirm_pay_next;
    }

    @Override
    public void initView() {
        super.initView();
        sharePreferenceUtils = new SharePreferenceUtils(ConfirmPayNextActivity.this, StaticParament.USER);
        payListener = new PayModel(this);
        data = (TransAmountData) getIntent().getSerializableExtra("data");
        requestMap = (HashMap<String, Object>) getIntent().getSerializableExtra("requestMap");
        qrPayRequestMap = (HashMap<String, Object>) getIntent().getSerializableExtra("qrPayRequestMap");
        donationAmount = getIntent().getStringExtra("donationAmount");
        tipAmounts = getIntent().getStringExtra("tipAmount");
        payType = getIntent().getStringExtra("payType");
        merchantName = getIntent().getStringExtra("merchantName");
        amount = getIntent().getStringExtra("amount");
        feeRate = getIntent().getStringExtra("feeRate");
        feeAmount = getIntent().getStringExtra("feeAmount");
        allPayAmount = getIntent().getStringExtra("allPayAmount");
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        insCl = findViewById(R.id.ins_cl);
        cardCl = findViewById(R.id.card_cl);
        donateLl = findViewById(R.id.donate_ll);
        donateAmount = findViewById(R.id.donate_amount);
        donateAmountTv = findViewById(R.id.donate_amount_tv);
        tipLl = findViewById(R.id.tip_ll);
        tipAmount = findViewById(R.id.tip_amount);
        tipTv = findViewById(R.id.tip_tv);
        merchantNameTv = findViewById(R.id.merchant_name);
        title = findViewById(R.id.title);
        orderAmountTv = findViewById(R.id.order_amount_tv);
        orderAmount = findViewById(R.id.order_amount);
        feeTv = findViewById(R.id.fee_tv);
        feeAmountTv = findViewById(R.id.fee_amount);
        allTv = findViewById(R.id.all_tv);
        allAmount = findViewById(R.id.all_amount);
        next = findViewById(R.id.next);

        cardTitle = findViewById(R.id.card_title);
        cardAmountTv = findViewById(R.id.card_amount_tv);
        cardAmount = findViewById(R.id.card_amount);
        discountLl = findViewById(R.id.discount_ll);
        discountTv = findViewById(R.id.discount_tv);
        discountAmount = findViewById(R.id.discount_amount);
        pocketLl = findViewById(R.id.pocket_ll);
        pocketTv = findViewById(R.id.pocket_tv);
        pocketAmount = findViewById(R.id.pocket_amount);
        cardDonateLl = findViewById(R.id.card_donate_ll);
        cardDonateAmountTv = findViewById(R.id.card_donate_amount_tv);
        cardDonateAmount = findViewById(R.id.card_donate_amount);
        cardTipLl = findViewById(R.id.card_tip_ll);
        cardTipTv = findViewById(R.id.card_tip_tv);
        cardTipAmount = findViewById(R.id.card_tip_amount);
        cardFeeTv = findViewById(R.id.card_fee_tv);
        cardFeeAmount = findViewById(R.id.card_fee_amount);
        cardAllTv = findViewById(R.id.card_all_tv);
        cardAllAmount = findViewById(R.id.card_all_amount);
        //切换字体
        TypefaceUtil.replaceFont(cardTitle,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(cardAmountTv,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(cardAmount,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(discountTv,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(discountAmount,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(pocketTv,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(pocketAmount,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(cardDonateAmountTv,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(cardDonateAmount,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(cardTipTv,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(cardTipAmount,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(cardFeeTv,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(cardFeeAmount,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(cardAllTv,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(cardAllAmount,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(donateAmount,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(donateAmountTv,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(tipAmount,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(tipTv,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(merchantNameTv,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(title,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(orderAmountTv,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(orderAmount,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(next,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(feeTv,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(feeAmountTv,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(allTv,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(allAmount,"fonts/gilroy_bold.ttf");

        if (payType.equals("0")) {
            insCl.setVisibility(View.GONE);
            cardCl.setVisibility(View.VISIBLE);
            cardAmount.setText("$" + PublicTools.twoend(data.getTransAmount()));

            //折扣内容
            int discountNum = 0;
            if ((!TextUtils.isEmpty(data.getNormalSaleUserDiscountAmount()) && Float.parseFloat(data.getNormalSaleUserDiscountAmount()) != 0)) {
                discountNum++;
            }
            if ((!TextUtils.isEmpty(data.getWholeSaleUserDiscountAmount()) && Float.parseFloat(data.getWholeSaleUserDiscountAmount()) != 0)) {
                discountNum++;
            }
            if (discountNum != 2) {
                if (discountNum == 0) {
                    discountLl.setVisibility(View.GONE);
                } else {
                    if ((TextUtils.isEmpty(data.getNormalSaleUserDiscountAmount()) || Float.parseFloat(data.getNormalSaleUserDiscountAmount()) == 0)) {

                    } else {
                        int defaultDis = (int) Float.parseFloat(data.getNormalSalesUserDiscount());
                        discountTv.setText(/*(int) CalcTool.mm(wholeSaleDiscount, "100")*/String.format("Discount (%s)", defaultDis + "%"));
                        discountAmount.setText("-$" + PublicTools.twoend(data.getNormalSaleUserDiscountAmount()));
                    }
                    if ((TextUtils.isEmpty(data.getWholeSaleUserDiscountAmount()) || Float.parseFloat(data.getWholeSaleUserDiscountAmount()) == 0)) {

                    } else {
                        int wholeDis = (int) Float.parseFloat(data.getWholeSalesUserDiscount());
                        discountTv.setText(/*(int) CalcTool.mm(wholeSaleDiscount, "100")*/String.format("Discount (%s)", wholeDis + "%"));
                        discountAmount.setText("-$" + PublicTools.twoend(data.getWholeSaleUserDiscountAmount()));
                    }
                }
            } else {
                discountTv.setText("Discount");
                discountAmount.setText("-$" + PublicTools.twoend(CalcTool.add(data.getWholeSaleUserDiscountAmount(), data.getNormalSaleUserDiscountAmount())));
            }
            if (TextUtils.isEmpty(donationAmount) || Float.parseFloat(donationAmount) == 0) {
                cardDonateLl.setVisibility(View.GONE);
            } else {
                cardDonateAmount.setText("$" + PublicTools.twoend(donationAmount));
            }
            if (TextUtils.isEmpty(tipAmounts) || Float.parseFloat(tipAmounts) == 0) {
                cardTipLl.setVisibility(View.GONE);
            } else {
                cardTipAmount.setText("$" + PublicTools.twoend(tipAmounts));
            }
            if (TextUtils.isEmpty(data.getRedEnvelopeAmount()) || Float.parseFloat(data.getRedEnvelopeAmount()) == 0) {
                pocketLl.setVisibility(View.GONE);
            } else {
                pocketLl.setVisibility(View.VISIBLE);
                pocketAmount.setText("-$" + PublicTools.twoend(data.getRedEnvelopeAmount()));
            }
            cardFeeTv.setText(String.format("Transaction fee (%s)", feeRate + "%"));
            cardFeeAmount.setText("$" + PublicTools.twoend(feeAmount));
            cardAllAmount.setText("$" + PublicTools.twoend(allPayAmount));
        } else {
            if (TextUtils.isEmpty(donationAmount) || Float.parseFloat(donationAmount) == 0) {
                donateLl.setVisibility(View.GONE);
            } else {
                donateAmount.setText("$" + PublicTools.twoend(donationAmount));
            }

            if (TextUtils.isEmpty(tipAmounts) || Float.parseFloat(tipAmounts) == 0) {
                tipLl.setVisibility(View.GONE);
            } else {
                tipAmount.setText("$" + PublicTools.twoend(tipAmounts));
            }
            orderAmount.setText("$" + PublicTools.twoend(amount));
            feeTv.setText(String.format("Transaction fee (%s)", feeRate + "%"));
            feeAmountTv.setText("$" + PublicTools.twoend(feeAmount));
            allAmount.setText("$" + PublicTools.twoend(allPayAmount));
        }
        merchantNameTv.setText(merchantName);
        next.setText("Pay $" + PublicTools.twoend(allPayAmount));
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnterPayPinDialog enterPayPinDialog = new EnterPayPinDialog(ConfirmPayNextActivity.this, merchantName, PublicTools.twoend(allPayAmount));
                enterPayPinDialog.setOnRepayPwd(new EnterPayPinDialog.OnRepayPwd() {
                    @Override
                    public void inputPwd(String password) {

                    }

                    @Override
                    public void back() {

                    }

                    @Override
                    public void inputFinish() {
                        resAFEvent(qrPayRequestMap);
                        if (paymentWaitingDialog == null) {
                            if (requestMap.containsKey("posTransNo")) {
                                paymentWaitingDialog = new PaymentWaitingDialog(ConfirmPayNextActivity.this, (String) requestMap.get("posTransNo"), "true");
                            } else {
                                paymentWaitingDialog = new PaymentWaitingDialog(ConfirmPayNextActivity.this, (String) qrPayRequestMap.get("transNo"), "false");
                            }
                        }
                        paymentWaitingDialog.show();
                        payListener.qrPay(qrPayRequestMap, new ActionCallBack() {
                            @Override
                            public void onSuccess(Object... o) {
                               String dataStr = (String) o[0];
                               showNext(dataStr);
                            }

                            @Override
                            public void onError(String e) {

                            }
                        });
                    }
                });
                enterPayPinDialog.ShowDialog();
            }
        });
    }

    public void showNext(String dataStr) {
        sharePreferenceUtils = new SharePreferenceUtils(ConfirmPayNextActivity.this, StaticParament.USER);
        sharePreferenceUtils.put(StaticParament.PAY_SUCCESS, "1");
        sharePreferenceUtils.save();

        //交易结果 0:处理中 1：成功 2：失败
        String resultState = "";
        String errorMessage = "";
        String successData = "";
        String id = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(dataStr);
            if (jsonObject.has("resultState")) {
                resultState = jsonObject.getString("resultState");
            }
            paymentWaitingDialog.setResult();
            if (resultState.equals("2")) {
                if (jsonObject.has("errorMessage")) {
                    errorMessage = jsonObject.getString("errorMessage");
                }
                Intent intent = new Intent(ConfirmPayNextActivity.this, PayFailedActivity.class);
                intent.putExtra("errorMessage", errorMessage);
                intent.putExtra("requestMap", requestMap);
                startActivity(intent);
            }
            if (resultState.equals("0")) {
                if (jsonObject.has("errorMessage")) {
                    errorMessage = jsonObject.getString("errorMessage");
                }
                Intent intent = new Intent(ConfirmPayNextActivity.this, PayFailedActivity.class);
                intent.putExtra("errorMessage", errorMessage);
                intent.putExtra("requestMap", requestMap);
                intent.putExtra("process", 1);
                startActivity(intent);
            }
            if (resultState.equals("1")) {
                successData = jsonObject.getString("data");
                id = jsonObject.getString("id");
                Gson gson = new Gson();
                PaySuccessData paySuccessData = gson.fromJson(successData, PaySuccessData.class);
                if (paySuccessData != null) {
                    paySuccessData.setId(id);
                    Intent intent = new Intent(ConfirmPayNextActivity.this, PaySuccessActivity.class);
                    intent.putExtra("paySuccessData", paySuccessData);
                    startActivity(intent);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

   /* *//**
     * 提交AF事件
     *//*
    private void resAFEvent(Map<String, Object> maps) {
        Map<String, Object> eventValue = new HashMap<String, Object>();
        //购买总额
        eventValue.put(AFInAppEventParameterName.PRICE, maps.get("trulyPayAmount"));
//        eventValue.put(AFInAppEventParameterName.CONTENT_ID, "221");
        eventValue.put(AFInAppEventParameterName.CONTENT_TYPE, maps.get("payType"));
        //货币
        eventValue.put(AFInAppEventParameterName.CURRENCY, "AUD");
        eventValue.put(AFInAppEventParameterName.QUANTITY, 1);
        eventValue.put(AFInAppEventParameterName.RECEIPT_ID, maps.get("merchantId"));
        //eventValue.put("merchantId", merchantId);
        AppsFlyerLib.getInstance().logEvent(ConfirmPayNextActivity.this, AFInAppEventType.PURCHASE, eventValue);
    }*/


    /**
     * 提交AF事件
     */
    private void resAFEvent(Map<String, Object> maps) {
        Map<String, Object> eventValue = new HashMap<String, Object>();
        //购买总额
        eventValue.put(BzEventParameterName.PRICE, maps.get("trulyPayAmount"));
        eventValue.put(BzEventParameterName.CONTENT_TYPE, maps.get("payType"));
        //货币
        eventValue.put(BzEventParameterName.CURRENCY, "AUD");
        eventValue.put(BzEventParameterName.QUANTITY, 1);
        eventValue.put(BzEventParameterName.RECEIPT_ID, maps.get("merchantId"));
        Braze.getInstance(ConfirmPayNextActivity.this).getCurrentUser()
                .setCustomUserAttribute(BzEventType.PURCHASE, eventValue.toString());
    }
}