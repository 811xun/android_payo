package com.home.glx.uwallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.FenQiMsgDatas;
import com.home.glx.uwallet.datas.RepaySuccessData;
import com.home.glx.uwallet.datas.UserRepayFeeData;
import com.home.glx.uwallet.mvp.CheckPayPassword_in;
import com.home.glx.uwallet.mvp.CheckPayPassword_p;
import com.home.glx.uwallet.mvp.JiHuoFenQi_in;
import com.home.glx.uwallet.mvp.JiHuoFenQi_p;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.PayListener;
import com.home.glx.uwallet.mvp.model.PayModel;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.EnterPayPinDialog;
import com.home.glx.uwallet.selfview.RepayMerchantsView;
import com.home.glx.uwallet.selfview.RepayPawDialog;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstalmentPendingNextActivity extends MainActivity implements View.OnClickListener,
        CheckPayPassword_in.View, JiHuoFenQi_in.View {

    private TextView terms;
    private TextView merchantName;
    private TextView orderNo;
    private TextView amount;
    private TextView amounts;
    private TextView feeAmount;
    private TextView totalAmount;
    private TextView cardNo;
    private TextView pay;
    private ImageView back;
    private TextView date;
    private ConstraintLayout feeCl;
    private ConstraintLayout nomalCl;
    private ConstraintLayout allCl;
    private List<String> overdueFeeId;
    private String payAll;
    private TextView lateFeeDate;
    private TextView feeMerchantName;
    private TextView allTransaction;
    private TextView allTransactionAmount;
    private PayListener payListener;
    private String chooseAmount;
    private String allAmount;
    private String name;
    private String orderNos;
    private TextView fee;
    private String selectTerm;
    private ImageView cardLogo;
    private String feeDate;
    private List<String> repayIds;
    private LinearLayout selectCard;
    //换卡后的卡id
    private String cardId;
    private List<String> borrowIds;
    private String repayDate;
    private String cardCCType;
    private SharePreferenceUtils sharePreferenceUtils;
    private LinearLayout merchantsLl;
    //全部还款中的逾期费
    private String lateFeeAmount;
    private String haveFeeCard;
    private CheckPayPassword_p checkPayPassword_p;
    private JiHuoFenQi_p presentJiHuo;
    private TextView lateFee;
    private TextView select;
    private TextView payTotal;

    @Override
    public int getLayout() {
        return R.layout.activity_instalment_pending_next;
    }

    @Override
    public void initView() {
        super.initView();
        presentJiHuo = new JiHuoFenQi_p(this, this);
        checkPayPassword_p = new CheckPayPassword_p(this, this);
        sharePreferenceUtils = new SharePreferenceUtils(InstalmentPendingNextActivity.this, StaticParament.USER);
        payListener = new PayModel(this);
        haveFeeCard = getIntent().getStringExtra("haveFeeCard");
        lateFeeAmount = getIntent().getStringExtra("lateFeeAmount");
        repayDate = getIntent().getStringExtra("repayDate");
        borrowIds = (List<String>) getIntent().getSerializableExtra("borrowIds");
        repayIds = (List<String>) getIntent().getSerializableExtra("repayIds");
        feeDate = getIntent().getStringExtra("feeDate");
        selectTerm = getIntent().getStringExtra("selectTerm");
        chooseAmount = getIntent().getStringExtra("chooseAmount");
        name = getIntent().getStringExtra("name");
        orderNos = getIntent().getStringExtra("orderNo");
        overdueFeeId = (List<String>) getIntent().getSerializableExtra("overdueFeeId");
        payAll = getIntent().getStringExtra("payAll");
        merchantsLl = findViewById(R.id.merchants_ll);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        selectCard = findViewById(R.id.select_card);
        nomalCl = findViewById(R.id.nomal_cl);
        allCl = findViewById(R.id.all_cl);
        feeCl = findViewById(R.id.fee_cl);

        cardLogo = findViewById(R.id.default_card_logo);
        terms = findViewById(R.id.terms);
        merchantName = findViewById(R.id.merchant_name);
        orderNo = findViewById(R.id.order_no);
        amount = findViewById(R.id.amount);
        amounts = findViewById(R.id.amounts);
        fee = findViewById(R.id.fee);
        feeAmount = findViewById(R.id.fee_amount);
        TextView total = findViewById(R.id.total);
        totalAmount = findViewById(R.id.total_amount);
        select = findViewById(R.id.select);
        TextView payWith = findViewById(R.id.pay_with);
        cardNo = findViewById(R.id.default_card_no);
        pay = findViewById(R.id.pay);
        feeMerchantName = findViewById(R.id.fee_merchant_name);
        lateFee = findViewById(R.id.late_fee);
        lateFeeDate = findViewById(R.id.late_fee_date);
        payTotal = findViewById(R.id.pay_total);
        TextView venue = findViewById(R.id.venue);
        TextView outStandFee = findViewById(R.id.out_stand_fee);
        allTransaction = findViewById(R.id.all_transaction);
        allTransactionAmount = findViewById(R.id.all_transaction_amount);

        if (payAll == null && feeDate != null) {
            feeCl.setVisibility(View.VISIBLE);
            nomalCl.setVisibility(View.GONE);
            //lateFeeDate.setText(feeDate);
            totalAmount.setText("$" + PublicTools.twoend(chooseAmount));
            pay.setText("Pay $" + PublicTools.twoend(chooseAmount));
            if (haveFeeCard != null && haveFeeCard.equals("0")) {
                lateFee.setText("Date");
                select.setText("Select bank account");
                cardNo.setText((String) sharePreferenceUtils.get(StaticParament.USER_FQ_ACCOUNT, ""));
                cardNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPayPwdDialog();
                    }
                });
                cardLogo.setVisibility(View.GONE);
                getDetail(true);
            } else {
                getDetail(true);
            }
        } else if (payAll != null) {
            if (repayDate != null) {
                payTotal.setText(repayDate);
            }
            getDetail(true);
            allCl.setVisibility(View.VISIBLE);
            nomalCl.setVisibility(View.GONE);
        } else {
            getDetail(true);
            merchantName.setText(name);
            orderNo.setText(orderNos);
            amounts.setText("$" + PublicTools.twoend(chooseAmount));
            terms.setText(selectTerm);
        }
        selectCard.setOnClickListener(this);
        pay.setOnClickListener(this);
        //切换字体
        TypefaceUtil.replaceFont(payTotal, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(venue, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(outStandFee, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(allTransaction, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(allTransactionAmount, "fonts/gilroy_semiBold.ttf");

        TypefaceUtil.replaceFont(lateFee, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(lateFeeDate, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(feeMerchantName, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(date, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(terms, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(merchantName, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(orderNo, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(amount, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(amounts, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(fee, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(feeAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(total, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(totalAmount, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(select, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(payWith, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(cardNo, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(pay, "fonts/gilroy_semiBold.ttf");
    }

    private void getDetail(final boolean create) {
        Map<String, Object> map = new HashMap<>();
        map.put("amount", chooseAmount);
        map.put("no_user_id", 1);
        if (!TextUtils.isEmpty(cardId)) {
            map.put("cardId", cardId);
        }
        if (feeDate != null) {
            map.put("repayId", overdueFeeId.get(0));
        }
        if (create && payAll != null) {
            //获取商户列表展示
            map.put("repayIds", repayIds);
        }
        payListener.getUserRepayFeeData(map, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                pay.setEnabled(true);
                UserRepayFeeData data = (UserRepayFeeData) o[0];
                if (feeDate != null) {
                    feeDate = data.getOverdueDateStr();
                    lateFeeDate.setText(feeDate);
                }
                cardId=data.getCardId();
                if (feeDate == null) {
                    fee.setText(String.format("Transaction fee (%s)", data.getFeeRate() + "%"));
                    allTransaction.setText(String.format("Transaction fee (%s)", data.getFeeRate() + "%"));
                    feeAmount.setText("$" + PublicTools.twoend(data.getFee()));
                    allTransactionAmount.setText("$" + PublicTools.twoend(data.getFee()));
                    allAmount = PublicTools.twoend(CalcTool.add(data.getFee(), chooseAmount));
                    totalAmount.setText("$" + allAmount);
                    pay.setText("Pay $" + allAmount);
                    if (create) {
                        if (data.getOrderData() != null) {
                            for (int i = 0; i < data.getOrderData().size(); i++) {
                                RepayMerchantsView view = new RepayMerchantsView(InstalmentPendingNextActivity.this, data.getOrderData().get(i));
                                merchantsLl.addView(view.getView());
                            }
                        }
                        if (lateFeeAmount != null) {
                            RepayMerchantsView view = new RepayMerchantsView(InstalmentPendingNextActivity.this, lateFeeAmount);
                            merchantsLl.addView(view.getView());
                        }
                    }
                }
                if (data.getCardNo() != null) {
                    if (data.getCardNo().length() > 4) {
                        String card = data.getCardNo().replaceAll(" ", "");
                        card = "**** " + card.substring(card.length() - 4);
                        cardNo.setText(card);
                    } else {
                        cardNo.setText(data.getCardNo());
                    }
                }
                if (data.getCardCcType() != null) {
                    cardCCType = data.getCardCcType();
                    if (data.getCardCcType().equals("10")) {
                        cardLogo.setImageResource(R.mipmap.pay_visa_logo);
                    } else if (data.getCardCcType().equals("20")) {
                        cardLogo.setImageResource(R.mipmap.pay_master_logo);
                    } else if (data.getCardCcType().equals("60")) {
                        cardLogo.setImageResource(R.mipmap.pay_onther_logo);
                    } else {
                        cardLogo.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onError(String e) {//Please add a bank card  删除完了所有卡
                pay.setEnabled(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (haveFeeCard != null && haveFeeCard.equals("0")) {
            presentJiHuo.loadFenQiMsg(new HashMap<String, Object>());
        }
    }

    /**
     * 弹出输入支付（PIN）密码弹窗
     */
    private void showPayPwdDialog() {
        RepayPawDialog repayPawDialog = new RepayPawDialog(this);
        repayPawDialog.setOnRepayPwd(new RepayPawDialog.OnRepayPwd() {
            @Override
            public void inputPwd(String password) {
                Map<String, Object> maps = new HashMap<>();
                maps.put("payPassword", password);
                checkPayPassword_p.checkPayPassword(maps);
            }
        });
        repayPawDialog.show();
    }


    @Override
    public void onClick(View v) {
        if (!PublicTools.isFastClick()) {
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.back:
                finish();
                break;
            case R.id.select_card:
                Intent intent = new Intent(InstalmentPendingNextActivity.this, SelectBankCardActivity.class);
                if (!TextUtils.isEmpty(cardId)) {
                    intent.putExtra("cardId", cardId);
                }
                intent.putExtra("fromPay", 1);
                intent.putExtra("showWenan", true);
                startActivityForResult(intent, 1006);
                break;
            case R.id.pay:

                EnterPayPinDialog enterPayPinDialog;
                if (feeDate != null) {
                    if (haveFeeCard.equals("1")) {
                        enterPayPinDialog = new EnterPayPinDialog(InstalmentPendingNextActivity.this, feeDate, PublicTools.twoend(chooseAmount), "true");
                    } else {
                        enterPayPinDialog = new EnterPayPinDialog(InstalmentPendingNextActivity.this, feeDate, PublicTools.twoend(chooseAmount), "false");
                    }
                } else {
                    if (repayDate != null) {
                        enterPayPinDialog = new EnterPayPinDialog(InstalmentPendingNextActivity.this, repayDate, allAmount, null);
                    } else {
                        enterPayPinDialog = new EnterPayPinDialog(InstalmentPendingNextActivity.this, name, allAmount);
                    }
                }
                enterPayPinDialog.setOnRepayPwd(new EnterPayPinDialog.OnRepayPwd() {
                    @Override
                    public void inputPwd(String password) {

                    }

                    @Override
                    public void back() {

                    }

                    @Override
                    public void inputFinish() {
                        final Map<String, Object> map = new HashMap<>();
                        map.put("isPayAll", "false");
                        if (payAll != null && borrowIds != null) {
                            map.put("isPayAll", "true");
                            map.put("borrowIds", borrowIds);
                        } else {
                            map.put("repayIds", repayIds);
                        }
                        if (overdueFeeId != null && overdueFeeId.size() > 0) {
                            map.put("overDueIds", overdueFeeId);
                        }
                        map.put("repayAmount", chooseAmount);
                        if (!TextUtils.isEmpty(cardId)) {
                            map.put("cardId", cardId);
                        }
                        payListener.repaymentV2(map, new ActionCallBack() {
                            @Override
                            public void onSuccess(Object... o) {
                                sharePreferenceUtils.put(StaticParament.REPAY_SUCCESS, "1");
                                sharePreferenceUtils.save();
                                RepaySuccessData data = (RepaySuccessData) o[0];
                                data.setCardNo(cardNo.getText().toString());
                                data.setCardCCType(cardCCType);
                                if (data.getState().equals("100300")) {//xzc
                                    Intent intent = new Intent(InstalmentPendingNextActivity.this, RepayKeyiActivity.class);
                                    startActivity(intent);
                                } else if (data.getState().equals("100400")) {
                                    //交易失败
                                    String message = "Payment failed";
                                    TiShiDialog tiShiDialog = new TiShiDialog(InstalmentPendingNextActivity.this);
                                    tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                                        @Override
                                        public void GuanBi() {
                                        }
                                    });
                                    tiShiDialog.ShowDialog(message);
                                } else {
                                    Intent intent = new Intent(InstalmentPendingNextActivity.this, RepaySuccessActivity.class);
                                    if (/*map.containsKey("overDueIds")*/feeDate != null) {
                                        intent.putExtra("repay", "fee");
                                        if (haveFeeCard.equals("1")) {
                                            intent.putExtra("feeDate", feeDate);
                                        }
                                    }
                                    if (payAll != null) {
                                        if (payAll.equals("payAllDate")) {
                                            intent.putExtra("repay", "allDate");
                                        } else {
                                            intent.putExtra("repay", "all");
                                        }
                                    }
                                    intent.putExtra("data", data);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onError(String e) {

                            }
                        });
                    }
                });
                enterPayPinDialog.ShowDialog();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1006 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String cardNoSelect = bundle.getString("cardNo");
            String cardIdSelect = bundle.getString("cardId");
            cardNo.setText(cardNoSelect);
            cardId = cardIdSelect;
            getDetail(false);
        }
    }

    @Override
    public void setPasswordStatus(String status) {
        if (status == null) {
            return;
        }
        Intent intent = new Intent(this, FenQuBanks_Activity.class);
        intent.putExtra("selectAccount", 1);
        startActivity(intent);
    }

    @Override
    public void setFenQiStatus(String status) {

    }

    @Override
    public void setFenQiMsg(FenQiMsgDatas fenQiMsg) {
        if (fenQiMsg.getBankcardNumber() != null) {
            String card = "****" + fenQiMsg.getBankcardNumber().
                    substring(fenQiMsg.getBankcardNumber().length() - 4, fenQiMsg.getBankcardNumber().length());
            sharePreferenceUtils.put(StaticParament.USER_FQ_ACCOUNT, card);
            sharePreferenceUtils.save();
            cardNo.setText(card);
        }
    }

    @Override
    public void setFenQiLimit(String limit) {

    }

    @Override
    public void setNoLoginFenQiLimit(String limit) {

    }
}