package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.PaySuccessData;
import com.home.glx.uwallet.datas.TransationDetailData;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PaySuccessActivity extends MainActivity {

    private TextView title;
    private TextView merchantName;
    private TextView transitionNo;
    private TextView paidOnTv;
    private TextView paidTime;
    private TextView paidWith;
    private TextView paidWiths;
    private TextView nextOf;
    private TextView nextPayAmount;
    private TextView dueDays;
    private TextView dueAllAmount;
    private TextView payTotal;
    private TextView paidWithTv;
    private TextView defaultCardNo;
    private TextView dueTodayText;
    private TextView dueTodayAmount;
    private TextView transFee;
    private TextView transFeeAmount;
    private TextView totalPaid;
    private TextView totalPaidAmount;
    private TextView orderDetail;
    private TextView orderAmount;
    private TextView orderAmounts;
    private TextView discount;
    private TextView discountAmounts;
    private TextView pocketText;
    private TextView pocketAmount;
    private TextView totalAmount;
    private TextView totalAmounts;
    private TextView paidFull;
    private TextView fullCardNo;
    private TextView cardOrderDetail;
    private TextView cardOrderAmount;
    private TextView cardOrderAmounts;
    private TextView cardDiscount;
    private TextView cardDiscountAmounts;
    private TextView cardPocketText;
    private TextView cardPocketAmount;
    private TextView cardTransFee;
    private TextView cardFeeAmount;
    private TextView cardTotalPaid;
    private TextView cardTotalAmount;
    private PaySuccessData paySuccessData;
    private ConstraintLayout insResult;
    private ConstraintLayout cardResult;
    private ImageView defaultCardLogo;
    private ImageView cardLogo;
    private LinearLayout discountLl;
    private LinearLayout cardDiscountLl;
    private LinearLayout pocketLl;
    private LinearLayout cardPocketLl;
    private TextView next;
    private ImageView shareImg;
    private ConstraintLayout repayAll;
    private ConstraintLayout paidWithCl;
    private String id;
    private ImageView back;
    private LinearLayout statusLl;
    private TextView statusTv;
    private TextView status;
    private LinearLayout donateLl;
    private TextView donateText;
    private TextView donationAmount;
    private LinearLayout donateLl1;
    private TextView donateText1;
    private TextView donationAmount1;
    private LinearLayout tipLl;
    private TextView tipText;
    private TextView tipAmount;
    private LinearLayout tipLl1;
    private TextView tipText1;
    private TextView tipAmount1;

    @Override
    public int getLayout() {
        return R.layout.activity_pay_result;
    }

    @Override
    public void initView() {
        super.initView();
        back = findViewById(R.id.id_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        repayAll = findViewById(R.id.repay_all);
        paidWithCl = findViewById(R.id.paid_with_cl);
        shareImg = findViewById(R.id.share);
        next = findViewById(R.id.next);
        shareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fileImg = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/payo/";
                Bitmap bitmap = PublicTools.screenShot(getWindow());
                File file = null;
                try {
                    file = PublicTools.saveBitmapToFile(PaySuccessActivity.this, bitmap, fileImg + System.currentTimeMillis() + "_result.png", new Handler());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (file != null) {
                    Uri uri = PublicTools.getUriFromFile(PaySuccessActivity.this, file);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.setType("image/*");
                    startActivity(Intent.createChooser(intent, "Share & Earn PAYO"));
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(PaySuccessActivity.this, MainTab.class);
                it.putExtra("num", 2);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
                startActivity(it);
                finish();
            }
        });
        id = getIntent().getStringExtra("id");
        paySuccessData = (PaySuccessData) getIntent().getSerializableExtra("paySuccessData");
        insResult = findViewById(R.id.ins_result);
        tipLl = findViewById(R.id.tip_ll);
        tipText = findViewById(R.id.tip_text);
        tipAmount = findViewById(R.id.tip_amount);
        tipLl1 = findViewById(R.id.tip_ll1);
        tipText1 = findViewById(R.id.tip_text1);
        tipAmount1 = findViewById(R.id.tip_amount1);
        donateLl = findViewById(R.id.donate_ll);
        donateText = findViewById(R.id.donate_text);
        donationAmount = findViewById(R.id.donation_amount);
        donateLl1 = findViewById(R.id.donate_ll1);
        donateText1 = findViewById(R.id.donate_text1);
        donationAmount1 = findViewById(R.id.donation_amount1);
        cardResult = findViewById(R.id.card_result);
        defaultCardLogo = findViewById(R.id.default_card_logo);
        cardLogo = findViewById(R.id.full_card_logo);
        title = findViewById(R.id.title);
        discountLl = findViewById(R.id.discount_ll);
        cardDiscountLl = findViewById(R.id.card_discount_ll);
        pocketLl = findViewById(R.id.pocket_ll);
        cardPocketLl = findViewById(R.id.card_pocket_ll);
        merchantName = findViewById(R.id.merchant_name);
        transitionNo = findViewById(R.id.transition_no);
        paidOnTv = findViewById(R.id.paid_on_tv);
        paidTime = findViewById(R.id.paid_time);
        paidWith = findViewById(R.id.paid_with);
        paidWiths = findViewById(R.id.paid_with_text);
        nextOf = findViewById(R.id.next_of);
        nextPayAmount = findViewById(R.id.next_pay_amount);
        dueDays = findViewById(R.id.due_days);
        dueAllAmount = findViewById(R.id.due_all_amount);
        payTotal = findViewById(R.id.pay_total);
        addressStyle(payTotal);
        paidWithTv = findViewById(R.id.paid_withs);
        defaultCardNo = findViewById(R.id.default_card_no);
        dueTodayText = findViewById(R.id.due_today_text);
        dueTodayAmount = findViewById(R.id.due_today_amount);
        transFee = findViewById(R.id.trans_fee);
        transFeeAmount = findViewById(R.id.trans_fee_amount);
        totalPaid = findViewById(R.id.total_paid);
        totalPaidAmount = findViewById(R.id.total_paid_amount);
        orderDetail = findViewById(R.id.order_detail);
        orderAmount = findViewById(R.id.order_amount);
        orderAmounts = findViewById(R.id.order_amounts);
        discount = findViewById(R.id.discount);
        discountAmounts = findViewById(R.id.discount_amounts);
        pocketText = findViewById(R.id.pocket_text);
        pocketAmount = findViewById(R.id.pocket_amount);
        totalAmount = findViewById(R.id.total_amount);
        totalAmounts = findViewById(R.id.total_amounts);
        paidFull = findViewById(R.id.paid_full);
        fullCardNo = findViewById(R.id.full_card_no);
        cardOrderDetail = findViewById(R.id.card_order_detail);
        cardOrderAmount = findViewById(R.id.card_order_amount);
        cardOrderAmounts = findViewById(R.id.card_order_amounts);
        cardDiscount = findViewById(R.id.card_discount);
        cardDiscountAmounts = findViewById(R.id.card_discount_amounts);
        cardPocketText = findViewById(R.id.card_pocket_text);
        cardPocketAmount = findViewById(R.id.card_pocket_amount);
        cardTransFee = findViewById(R.id.card_trans_fee);
        cardFeeAmount = findViewById(R.id.card_fee_amount);
        cardTotalPaid = findViewById(R.id.card_total_paid);
        cardTotalAmount = findViewById(R.id.card_total_amount);
        statusLl = findViewById(R.id.status_ll);
        statusTv = findViewById(R.id.status_tv);
        status = findViewById(R.id.status);
        if (paySuccessData != null) {
            setData();
        } else {
            next.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
            shareImg.setVisibility(View.GONE);
            getRecordDetail(id);
        }
        //切换字体
        TypefaceUtil.replaceFont(tipText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(tipAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(tipText1, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(tipAmount1, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(donateText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(donationAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(donateText1, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(donationAmount1, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(statusTv, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(status, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(next, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(paidFull, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(fullCardNo, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(cardOrderDetail, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(cardOrderAmount, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(cardOrderAmounts, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(cardDiscount, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(cardDiscountAmounts, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(cardPocketText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(cardPocketAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(cardTransFee, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(cardFeeAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(cardTotalPaid, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(cardTotalAmount, "fonts/gilroy_bold.ttf");

        TypefaceUtil.replaceFont(discount, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(discountAmounts, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(pocketText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(pocketAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(totalAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(totalAmounts, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(orderAmounts, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(orderAmount, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(paidWiths, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(nextOf, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(nextPayAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(dueDays, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(dueAllAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(payTotal, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(paidWithTv, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(defaultCardNo, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(dueTodayText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(dueTodayAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(transFee, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(transFeeAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(totalPaid, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(totalPaidAmount, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(orderDetail, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(paidWith, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(merchantName, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(transitionNo, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(paidOnTv, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(paidTime, "fonts/gilroy_semiBold.ttf");
    }

    private void addressStyle(TextView textView) {
        textView.setHighlightColor(getResources().getColor(android.R.color.transparent));
        SpannableString spannableString = new SpannableString("Pay total outstanding");
        //英文
        //隐私政策
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
        spannableString.setSpan(colorSpan1, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    private void setData() {
        if (paySuccessData.getPayType().equals("4")) {
            if (Float.parseFloat(paySuccessData.getPayAmount()) == 0) {
                paidWithCl.setVisibility(View.GONE);
                repayAll.setVisibility(View.GONE);
            }
            repayAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PaySuccessActivity.this, InstalmentPendingDetailsActivity.class);
                    intent.putExtra("repayAll", 1);
                    intent.putExtra("id", paySuccessData.getId());
                    startActivity(intent);
                }
            });
            insResult.setVisibility(View.VISIBLE);
            nextPayAmount.setText("$" + PublicTools.twoend(paySuccessData.getCreditNextRepayAmount()));
            paidWiths.setText(String.format("1st of %s", paySuccessData.getPeriod()));
            dueAllAmount.setText(String.format("$%s left", PublicTools.twoend(paySuccessData.getRemainingCreditAmount())));
            dueTodayAmount.setText("$" + PublicTools.twoend(paySuccessData.getCreditNeedCardPayNoFeeAmount()));
            totalPaidAmount.setText("$" + PublicTools.twoend(paySuccessData.getCreditNeedCardPayAmount()));
        } else {
            cardResult.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(paySuccessData.getDonationAmount()) && Float.parseFloat(paySuccessData.getDonationAmount()) != 0) {
            donateLl.setVisibility(View.VISIBLE);
            donationAmount.setText("$" + PublicTools.twoend(paySuccessData.getDonationAmount()));
            donateLl1.setVisibility(View.VISIBLE);
            donationAmount1.setText("$" + PublicTools.twoend(paySuccessData.getDonationAmount()));
        } else {
            donateLl.setVisibility(View.GONE);
            donateLl1.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(paySuccessData.getTipAmount()) && Float.parseFloat(paySuccessData.getTipAmount()) != 0) {
            tipLl.setVisibility(View.VISIBLE);
            tipAmount.setText("$" + PublicTools.twoend(paySuccessData.getTipAmount()));
            tipLl1.setVisibility(View.VISIBLE);
            tipAmount1.setText("$" + PublicTools.twoend(paySuccessData.getTipAmount()));
        } else {
            tipLl1.setVisibility(View.GONE);
            tipLl.setVisibility(View.GONE);
        }
        merchantName.setText(paySuccessData.getMerchantName());
        transitionNo.setText(paySuccessData.getTransNo());
        paidTime.setText(paySuccessData.getOrderCreatedDate());
        if (paySuccessData.getCardCcType() != null) {
            if (paySuccessData.getCardCcType().equals("10")) {
                defaultCardLogo.setImageResource(R.mipmap.pay_visa_logo);
                cardLogo.setImageResource(R.mipmap.pay_visa_logo);
            } else if (paySuccessData.getCardCcType().equals("20")) {
                defaultCardLogo.setImageResource(R.mipmap.pay_master_logo);
                cardLogo.setImageResource(R.mipmap.pay_master_logo);
            } else if (paySuccessData.getCardCcType().equals("60")) {
                defaultCardLogo.setImageResource(R.mipmap.pay_onther_logo);
                cardLogo.setImageResource(R.mipmap.pay_onther_logo);
            } else {
                defaultCardLogo.setVisibility(View.INVISIBLE);
                cardLogo.setVisibility(View.INVISIBLE);
            }
        }
        if (paySuccessData.getCardNo() != null) {
            if (paySuccessData.getCardNo().length() > 4) {
                String card = paySuccessData.getCardNo().replaceAll(" ", "");
                card = "**** " + card.substring(card.length() - 4);
                defaultCardNo.setText(card);
                fullCardNo.setText(card);
            } else {
                defaultCardNo.setText(paySuccessData.getCardNo());
                fullCardNo.setText(paySuccessData.getCardNo());
            }
        }
        transFee.setText(String.format("Transaction fee (%s)", paySuccessData.getCardPayRate() + "%"));
        transFeeAmount.setText("$" + PublicTools.twoend(paySuccessData.getCardPayFee()));
        cardTransFee.setText(String.format("Transaction fee (%s)", paySuccessData.getCardPayRate() + "%"));
        cardFeeAmount.setText("$" + PublicTools.twoend(paySuccessData.getCardPayFee()));
        orderAmounts.setText("$" + PublicTools.twoend(paySuccessData.getTransAmount()));
        cardOrderAmounts.setText("$" + PublicTools.twoend(paySuccessData.getTransAmount()));
        //折扣内容
        int discountNum = 0;
        if ((!TextUtils.isEmpty(paySuccessData.getNormalSaleUserDiscountAmount()) && Float.parseFloat(paySuccessData.getNormalSaleUserDiscountAmount()) != 0)) {
            discountNum++;
        }
        if ((!TextUtils.isEmpty(paySuccessData.getWholeSaleUserDiscountAmount()) && Float.parseFloat(paySuccessData.getWholeSaleUserDiscountAmount()) != 0)) {
            discountNum++;
        }
        if (discountNum != 2) {
            if (discountNum == 0) {
                discountLl.setVisibility(View.GONE);
                cardDiscountLl.setVisibility(View.GONE);
            } else {
                if ((TextUtils.isEmpty(paySuccessData.getNormalSaleUserDiscountAmount()) || Float.parseFloat(paySuccessData.getNormalSaleUserDiscountAmount()) == 0)) {

                } else {
                    int defaultDis = (int) Float.parseFloat(paySuccessData.getNormalSalesUserDiscount());
                    discount.setText(/*(int) CalcTool.mm(wholeSaleDiscount, "100")*/String.format("Discount (%s)", defaultDis + "%"));
                    cardDiscount.setText(/*(int) CalcTool.mm(wholeSaleDiscount, "100")*/String.format("Discount (%s)", defaultDis + "%"));
                    discountAmounts.setText("-$" + PublicTools.twoend(paySuccessData.getNormalSaleUserDiscountAmount()));
                    cardDiscountAmounts.setText("-$" + PublicTools.twoend(paySuccessData.getNormalSaleUserDiscountAmount()));
                }
                if ((TextUtils.isEmpty(paySuccessData.getWholeSaleUserDiscountAmount()) || Float.parseFloat(paySuccessData.getWholeSaleUserDiscountAmount()) == 0)) {

                } else {
                    int wholeDis = (int) Float.parseFloat(paySuccessData.getWholeSalesUserDiscount());
                    discount.setText(/*(int) CalcTool.mm(wholeSaleDiscount, "100")*/String.format("Discount (%s)", wholeDis + "%"));
                    cardDiscount.setText(/*(int) CalcTool.mm(wholeSaleDiscount, "100")*/String.format("Discount (%s)", wholeDis + "%"));
                    cardDiscountAmounts.setText("-$" + PublicTools.twoend(paySuccessData.getWholeSaleUserDiscountAmount()));
                    discountAmounts.setText("-$" + PublicTools.twoend(paySuccessData.getWholeSaleUserDiscountAmount()));
                }
            }
        } else {
            discount.setText("Discount");
            cardDiscount.setText("Discount");
            discountAmounts.setText("-$" + PublicTools.twoend(CalcTool.add(paySuccessData.getWholeSaleUserDiscountAmount(), paySuccessData.getNormalSaleUserDiscountAmount())));
            cardDiscountAmounts.setText("-$" + PublicTools.twoend(CalcTool.add(paySuccessData.getWholeSaleUserDiscountAmount(), paySuccessData.getNormalSaleUserDiscountAmount())));
        }

        if (TextUtils.isEmpty(paySuccessData.getUseRedEnvelopeAmount()) || Float.parseFloat(paySuccessData.getUseRedEnvelopeAmount()) == 0) {
            pocketLl.setVisibility(View.GONE);
            cardPocketLl.setVisibility(View.GONE);
        } else {
            pocketLl.setVisibility(View.VISIBLE);
            cardPocketLl.setVisibility(View.VISIBLE);
            pocketAmount.setText("-$" + PublicTools.twoend(paySuccessData.getUseRedEnvelopeAmount()));
            cardPocketAmount.setText("-$" + PublicTools.twoend(paySuccessData.getUseRedEnvelopeAmount()));
        }
        totalAmounts.setText("$" + PublicTools.twoend(paySuccessData.getPayAmount()));
        cardTotalAmount.setText("$" + PublicTools.twoend(paySuccessData.getTotalAmount()));
    }

    /**
     * 查询卡支付/分期付交易明细
     */
    public void getRecordDetail(final String transId) {

        Map<String, Object> maps = new HashMap<>();
        maps.put("no_user_id", 1);

        RequestUtils requestUtils1 = new RequestUtils(this, maps, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(PaySuccessActivity.this, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("查询卡支付/分期付交易明细参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getRecordDetail(headerMap, transId, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("查询卡支付/分期付交易明细:" + dataStr);
                        Gson gson = new Gson();
                        cardResult.setVisibility(View.VISIBLE);
                        statusLl.setVisibility(View.VISIBLE);
                        TransationDetailData transationDetailData = gson.fromJson(dataStr, TransationDetailData.class);
                        status.setText(transationDetailData.getTransStateStr());
                        merchantName.setText(transationDetailData.getTradingName());
                        transitionNo.setText(transationDetailData.getTransNo());
                        paidTime.setText(transationDetailData.getDisplayDate());
                        if (transationDetailData.getCardCcType() != null) {
                            if (transationDetailData.getCardCcType().equals("10")) {
                                defaultCardLogo.setImageResource(R.mipmap.pay_visa_logo);
                                cardLogo.setImageResource(R.mipmap.pay_visa_logo);
                            } else if (transationDetailData.getCardCcType().equals("20")) {
                                defaultCardLogo.setImageResource(R.mipmap.pay_master_logo);
                                cardLogo.setImageResource(R.mipmap.pay_master_logo);
                            } else if (transationDetailData.getCardCcType().equals("60")) {
                                defaultCardLogo.setImageResource(R.mipmap.pay_onther_logo);
                                cardLogo.setImageResource(R.mipmap.pay_onther_logo);
                            } else {
                                defaultCardLogo.setVisibility(View.INVISIBLE);
                                cardLogo.setVisibility(View.INVISIBLE);
                            }
                        }
                        if (transationDetailData.getCardNo() != null) {
                            if (transationDetailData.getCardNo().length() > 4) {
                                String card = transationDetailData.getCardNo().replaceAll(" ", "");
                                card = "**** " + card.substring(card.length() - 4);
                                defaultCardNo.setText(card);
                                fullCardNo.setText(card);
                            } else {
                                defaultCardNo.setText(transationDetailData.getCardNo());
                                fullCardNo.setText(transationDetailData.getCardNo());
                            }
                        }
                        if (!TextUtils.isEmpty(transationDetailData.getDonationAmount()) && Float.parseFloat(transationDetailData.getDonationAmount()) != 0) {
                            donateLl.setVisibility(View.VISIBLE);
                            donationAmount.setText("$" + PublicTools.twoend(transationDetailData.getDonationAmount()));
                            donateLl1.setVisibility(View.VISIBLE);
                            donationAmount1.setText("$" + PublicTools.twoend(transationDetailData.getDonationAmount()));
                        } else {
                            donateLl.setVisibility(View.GONE);
                            donateLl1.setVisibility(View.GONE);
                        }
                        if (!TextUtils.isEmpty(transationDetailData.getTipAmount()) && Float.parseFloat(transationDetailData.getTipAmount()) != 0) {
                            tipLl.setVisibility(View.VISIBLE);
                            tipAmount.setText("$" + PublicTools.twoend(transationDetailData.getTipAmount()));
                            tipLl1.setVisibility(View.VISIBLE);
                            tipAmount1.setText("$" + PublicTools.twoend(transationDetailData.getTipAmount()));
                        } else {
                            tipLl1.setVisibility(View.GONE);
                            tipLl.setVisibility(View.GONE);
                        }
                        transFee.setText(String.format("Transaction fee (%s)", new BigDecimal(Float.valueOf(transationDetailData.getRate()) * 100).setScale(2, BigDecimal.ROUND_DOWN) + "%"));
                        transFeeAmount.setText("$" + PublicTools.twoend(transationDetailData.getFee()));
                        cardTransFee.setText(String.format("Transaction fee (%s)", new BigDecimal(Float.valueOf(transationDetailData.getRate()) * 100).setScale(2, BigDecimal.ROUND_DOWN) + "%"));
                        cardFeeAmount.setText("$" + PublicTools.twoend(transationDetailData.getFee()));
                        orderAmounts.setText("$" + PublicTools.twoend(transationDetailData.getTransAmount()));
                        cardOrderAmounts.setText("$" + PublicTools.twoend(transationDetailData.getTransAmount()));
                        //折扣内容
                        int discountNum = 0;
                        if ((!TextUtils.isEmpty(transationDetailData.getNormalSaleOffAmt()) && Float.parseFloat(transationDetailData.getNormalSaleOffAmt()) != 0)) {
                            discountNum++;
                        }
                        if ((!TextUtils.isEmpty(transationDetailData.getWholeSaleOffAmt()) && Float.parseFloat(transationDetailData.getWholeSaleOffAmt()) != 0)) {
                            discountNum++;
                        }
                        if (discountNum != 2) {
                            if (discountNum == 0) {
                                discountLl.setVisibility(View.GONE);
                                cardDiscountLl.setVisibility(View.GONE);
                            } else {
                                if ((TextUtils.isEmpty(transationDetailData.getNormalSaleOffAmt()) || Float.parseFloat(transationDetailData.getNormalSaleOffAmt()) == 0)) {

                                } else {
                                    int defaultDis = (int) Float.parseFloat(transationDetailData.getNormalSaleDiscountRate());
                                    discount.setText(/*(int) CalcTool.mm(wholeSaleDiscount, "100")*/String.format("Discount (%s)", defaultDis + "%"));
                                    cardDiscount.setText(/*(int) CalcTool.mm(wholeSaleDiscount, "100")*/String.format("Discount (%s)", defaultDis + "%"));
                                    discountAmounts.setText("-$" + PublicTools.twoend(transationDetailData.getNormalSaleOffAmt()));
                                    cardDiscountAmounts.setText("-$" + PublicTools.twoend(transationDetailData.getNormalSaleOffAmt()));
                                }
                                if ((TextUtils.isEmpty(transationDetailData.getWholeSaleOffAmt()) || Float.parseFloat(transationDetailData.getWholeSaleOffAmt()) == 0)) {

                                } else {
                                    int wholeDis = (int) Float.parseFloat(transationDetailData.getWholeSaleDiscountRate());
                                    discount.setText(/*(int) CalcTool.mm(wholeSaleDiscount, "100")*/String.format("Discount (%s)", wholeDis + "%"));
                                    cardDiscount.setText(/*(int) CalcTool.mm(wholeSaleDiscount, "100")*/String.format("Discount (%s)", wholeDis + "%"));
                                    cardDiscountAmounts.setText("-$" + PublicTools.twoend(transationDetailData.getWholeSaleOffAmt()));
                                    discountAmounts.setText("-$" + PublicTools.twoend(transationDetailData.getWholeSaleOffAmt()));
                                }
                            }
                        } else {
                            discount.setText("Discount");
                            cardDiscount.setText("Discount");
                            discountAmounts.setText("-$" + PublicTools.twoend(CalcTool.add(transationDetailData.getWholeSaleOffAmt(), transationDetailData.getNormalSaleOffAmt())));
                            cardDiscountAmounts.setText("-$" + PublicTools.twoend(CalcTool.add(transationDetailData.getWholeSaleOffAmt(), transationDetailData.getNormalSaleOffAmt())));
                        }

                        if (TextUtils.isEmpty(transationDetailData.getRedEnvelopeAmount()) || Float.parseFloat(transationDetailData.getRedEnvelopeAmount()) == 0) {
                            pocketLl.setVisibility(View.GONE);
                            cardPocketLl.setVisibility(View.GONE);
                        } else {
                            pocketLl.setVisibility(View.VISIBLE);
                            cardPocketLl.setVisibility(View.VISIBLE);
                            pocketAmount.setText("-$" + PublicTools.twoend(transationDetailData.getRedEnvelopeAmount()));
                            cardPocketAmount.setText("-$" + PublicTools.twoend(transationDetailData.getRedEnvelopeAmount()));
                        }
                        totalAmounts.setText("$" + PublicTools.twoend(transationDetailData.getPayAmount()));
                        cardTotalAmount.setText("$" + PublicTools.twoend(transationDetailData.getPayAmount()));
                    }


                    @Override
                    public void resError(String error) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        // 完全由自己控制返回键逻辑，系统不再控制，但是有个前提是：　　
        // 不要在Activity的onKeyDown或者OnKeyUp中拦截掉返回键　　
        // 拦截：就是在OnKeyDown或者OnKeyUp中自己处理了返回键　　
        // （这里处理之后return true.或者return false都会导致onBackPressed不会执行）
        // 不拦截：在OnKeyDown和OnKeyUp中返回super对应的方法　　
        // （如果两个方法都被覆写就分别都要返回super.onKeyDown,super.onKeyUp）
        Intent it = new Intent(PaySuccessActivity.this, MainTab.class);
        it.putExtra("num", 2);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
        startActivity(it);
        finish();
    }
}