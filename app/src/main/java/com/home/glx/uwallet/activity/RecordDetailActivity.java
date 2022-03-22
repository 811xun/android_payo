package com.home.glx.uwallet.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.TransationDetailData;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.RecordInstalmentView;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class RecordDetailActivity extends MainActivity {
    private String id;
    private TextView merchantName;
    private TextView orderAmount;
    private TextView stausText;
    private TextView transitionTime;
    private TextView cardType;
    private ImageView idBack;
    private TextView line;
    private LinearLayout instalmentLl;
    private TextView payment;
    private TextView when;
    private TextView line3;
    private TextView total;
    private TextView allOrderAmount;
    private TextView allOrderAmountMoney;
    private TextView promoAmount;
    private TextView promoAmountMoney;
    private TextView discountAmount;
    private TextView discountAmountMoney;
    private TextView dueDates;
    private ConstraintLayout instalDiscount;
    private ConstraintLayout detailCl;
    private TextView donateAmount;
    private TextView donateAmountMoney;
    private TextView tipAmount;
    private TextView tipAmountMoney;

    @Override
    public int getLayout() {
        return R.layout.activity_record_detail;
    }

    @Override
    public void initView() {
        super.initView();
        id = getIntent().getStringExtra("id");
        TextView title = findViewById(R.id.title);
        TextView where = findViewById(R.id.where);
        total = findViewById(R.id.total);
        payment = findViewById(R.id.payment);
        TextView status = findViewById(R.id.status);
        when = findViewById(R.id.when);
        line3 = findViewById(R.id.line3);
        idBack = (ImageView) findViewById(R.id.id_back);
        transitionTime = (TextView) findViewById(R.id.transition_time);
        merchantName = (TextView) findViewById(R.id.merchant_name);
        orderAmount = (TextView) findViewById(R.id.order_amount);
        stausText = (TextView) findViewById(R.id.staus_text);
        cardType = (TextView) findViewById(R.id.card_type);
        line = (TextView) findViewById(R.id.line3);
        instalmentLl = findViewById(R.id.instalment_ll);
        allOrderAmount = findViewById(R.id.all_order_amount);
        allOrderAmountMoney = findViewById(R.id.all_order_amount_money);
        promoAmount = findViewById(R.id.promo_amount);
        promoAmountMoney = findViewById(R.id.promo_amount_money);
        donateAmount = findViewById(R.id.donate_amount);
        donateAmountMoney = findViewById(R.id.donate_amount_money);
        tipAmount = findViewById(R.id.tip_amount);
        tipAmountMoney = findViewById(R.id.tip_amount_money);
        discountAmount = findViewById(R.id.discount_amount);
        discountAmountMoney = findViewById(R.id.discount_amount_money);
        instalDiscount = findViewById(R.id.instal_discount);
        dueDates = findViewById(R.id.due_dates);
        detailCl = findViewById(R.id.detail_cl);

        //切换字体
        TypefaceUtil.replaceFont(donateAmount, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(donateAmountMoney, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(tipAmount, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(tipAmountMoney, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(dueDates, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(allOrderAmount, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(allOrderAmountMoney, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(promoAmount, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(promoAmountMoney, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(discountAmount, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(discountAmountMoney, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(transitionTime, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(where, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(merchantName, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(total, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(orderAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(payment, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(cardType, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(status, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(stausText, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(when, "fonts/gilroy_regular.ttf");

        idBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getRecordDetail(id);
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

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(RecordDetailActivity.this, StaticParament.USER);
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
                        TransationDetailData transationDetailData = gson.fromJson(dataStr, TransationDetailData.class);
                        detailCl.setVisibility(View.VISIBLE);
                        if (transationDetailData.getTransType() == 2) {
                            instalDiscount.setVisibility(View.GONE);
                            cardType.setText("Card payment");
                            transitionTime.setText(transationDetailData.getDisplayDate());
                            if (TextUtils.isEmpty(transationDetailData.getPayAmount()) || Float.parseFloat(transationDetailData.getPayAmount()) == 0) {
                                orderAmount.setText("$0.00");
                            } else {
                                orderAmount.setText("$" + PublicTools.twoend(transationDetailData.getPayAmount()));
                            }
                        } else {
                            dueDates.setVisibility(View.VISIBLE);
                            transitionTime.setVisibility(View.GONE);
                            total.setText("Total order amount");
                            when.setVisibility(View.GONE);
                            payment.setText("Terms of Instalment");
                            String discountAmounts = CalcTool.add(transationDetailData.getWholeSaleOffAmt(), transationDetailData.getNormalSaleOffAmt());
                            instalDiscount.setVisibility(View.GONE);
//                            if (Float.parseFloat(discountAmounts) == 0 && Float.parseFloat(transationDetailData.getDonationAmount()) == 0 && Float.parseFloat(transationDetailData.getTipAmount()) == 0) {
//                            } else {
//                                instalDiscount.setVisibility(View.VISIBLE);
//                            }
                            allOrderAmountMoney.setText("$" + PublicTools.twoend(transationDetailData.getTransAmount()));
                            if (TextUtils.isEmpty(transationDetailData.getRedEnvelopeAmount()) || Float.parseFloat(transationDetailData.getRedEnvelopeAmount()) == 0) {
                                promoAmount.setVisibility(View.GONE);
                                promoAmountMoney.setVisibility(View.GONE);
                            } else {
                                instalDiscount.setVisibility(View.VISIBLE);

                                promoAmountMoney.setText("-$" + PublicTools.twoend(transationDetailData.getRedEnvelopeAmount()));
                            }
                            if (TextUtils.isEmpty(transationDetailData.getDonationAmount()) || Float.parseFloat(transationDetailData.getDonationAmount()) == 0) {
                                donateAmount.setVisibility(View.GONE);
                                donateAmountMoney.setVisibility(View.GONE);
                            } else {

                                instalDiscount.setVisibility(View.VISIBLE);
                                donateAmountMoney.setText("$" + PublicTools.twoend(transationDetailData.getDonationAmount()));
                            }
                            if (TextUtils.isEmpty(transationDetailData.getTipAmount()) || Float.parseFloat(transationDetailData.getTipAmount()) == 0) {
                                tipAmount.setVisibility(View.GONE);
                                tipAmountMoney.setVisibility(View.GONE);
                            } else {
                                instalDiscount.setVisibility(View.VISIBLE);
                                tipAmountMoney.setText("$" + PublicTools.twoend(transationDetailData.getTipAmount()));
                            }
                            orderAmount.setText("$" + PublicTools.twoend(transationDetailData.getPayAmount()));
                            /*String discountAmounts = CalcTool.sub(transationDetailData.getTransAmount(), transationDetailData.getPayAmount());
                            discountAmounts = CalcTool.sub(discountAmounts, transationDetailData.getRedEnvelopeAmount());*/
                            if (Float.parseFloat(discountAmounts) == 0) {
                                discountAmount.setVisibility(View.GONE);
                                discountAmountMoney.setVisibility(View.GONE);
                            } else {
                                instalDiscount.setVisibility(View.VISIBLE);
                                discountAmountMoney.setText("-$" + PublicTools.twoend(discountAmounts));
                            }
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
                                    discountAmount.setVisibility(View.GONE);
                                    discountAmountMoney.setVisibility(View.GONE);
                                } else {
                                    if ((TextUtils.isEmpty(transationDetailData.getNormalSaleOffAmt()) || Float.parseFloat(transationDetailData.getNormalSaleOffAmt()) == 0)) {

                                    } else {
                                        instalDiscount.setVisibility(View.VISIBLE);

                                        int defaultDis = (int) Float.parseFloat(transationDetailData.getNormalSaleDiscountRate());
                                        discountAmount.setText(/*(int) CalcTool.mm(wholeSaleDiscount, "100")*/String.format("Discount (%s)", defaultDis + "%"));
                                        discountAmountMoney.setText("-$" + PublicTools.twoend(transationDetailData.getNormalSaleOffAmt()));
                                    }
                                    if ((TextUtils.isEmpty(transationDetailData.getWholeSaleOffAmt()) || Float.parseFloat(transationDetailData.getWholeSaleOffAmt()) == 0)) {

                                    } else {
                                        instalDiscount.setVisibility(View.VISIBLE);

                                        int wholeDis = (int) Float.parseFloat(transationDetailData.getWholeSaleDiscountRate());
                                        discountAmount.setText(/*(int) CalcTool.mm(wholeSaleDiscount, "100")*/String.format("Discount (%s)", wholeDis + "%"));
                                        discountAmountMoney.setText("-$" + PublicTools.twoend(transationDetailData.getWholeSaleOffAmt()));
                                    }
                                }
                            } else {
                                instalDiscount.setVisibility(View.VISIBLE);
                                discountAmount.setText("Discount");
                                discountAmountMoney.setText("-$" + PublicTools.twoend(CalcTool.add(transationDetailData.getWholeSaleOffAmt(), transationDetailData.getNormalSaleOffAmt())));
                            }

                            List<TransationDetailData.RepayListBean> listBeans = transationDetailData.getRepayList();
                            if (listBeans.size() > 0) {
                                line.setVisibility(View.GONE);
                                cardType.setText(listBeans.size() + " terms");
                                for (int i = 0; i < listBeans.size(); i++) {
                                    RecordInstalmentView view = new RecordInstalmentView(RecordDetailActivity.this,
                                            listBeans.get(i).getExpectRepayTime(),
                                            /*getTerm(i) + */"$" + PublicTools.twoend(listBeans.get(i).getPayPrincipal()),
                                            listBeans.get(i).getState());
                                    instalmentLl.addView(view.getView());
                                }
                            } else {
                                dueDates.setVisibility(View.GONE);
                                payment.setVisibility(View.GONE);
                                cardType.setVisibility(View.GONE);
                                when.setVisibility(View.GONE);
                                line3.setVisibility(View.GONE);
                            }
                        }

                        merchantName.setText(transationDetailData.getTradingName());
                        if (transationDetailData.getTransStateStr().equals("Successful")) {
                            //stausText.setTextColor(getColor(R.color.greenPoint));
                        } else {
                            //stausText.setTextColor(getColor(R.color.grayPoint));
                        }
                        stausText.setText(transationDetailData.getTransStateStr());
                    }


                    @Override
                    public void resError(String error) {

                    }
                });
    }

    private String getTerm(int i) {
        if (i == 0) {
            return "1st Payment  ";
        } else if (i == 1) {
            return "2nd Payment  ";
        } else if (i == 2) {
            return "3rd Payment  ";
        } else {
            return "4th Payment  ";
        }
    }
}