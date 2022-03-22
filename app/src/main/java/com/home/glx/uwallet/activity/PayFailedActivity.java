package com.home.glx.uwallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.xzc.ChooseCardActivity;
import com.home.glx.uwallet.datas.FenqifuStatue;
import com.home.glx.uwallet.datas.TransAmountData;
import com.home.glx.uwallet.fragment.FirstFragment;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.PayListener;
import com.home.glx.uwallet.mvp.model.PayModel;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.home.glx.uwallet.activity.PayMoneyActivity.creditProductId;
import static com.home.glx.uwallet.activity.PayMoneyActivity.data11;

public class PayFailedActivity extends MainActivity implements
        View.OnClickListener/*, WhetherOpenInvest_in.View*/ {

    private TextView title;
    private TextView msg;
    private TextView payIns;
    private TextView changeCard;
    private ImageView close;
    //是否开通个业务
//    private WhetherOpenInvest_p openInvestPresent;
    //0：卡支付 4：分期付
    private int payType;
    private HashMap<String, Object> requestMap;
    private PayListener payListener;
    private boolean oldPeople = false;
    private boolean noInstal = false;
    private String errorMessage;
    private int process;
    private TextView next;
    private String selectCardId;
    private int selectPayType;

    @Override
    public int getLayout() {
        return R.layout.activity_pay_failed;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void initView() {
        super.initView();
        payListener = new PayModel(this);
//        openInvestPresent = new WhetherOpenInvest_p(this, this);
        process = getIntent().getIntExtra("process", 0);
        errorMessage = getIntent().getStringExtra("errorMessage");
        requestMap = (HashMap<String, Object>) getIntent().getSerializableExtra("requestMap");
        if (requestMap != null) {
            selectCardId = (String) requestMap.get("cardId");
            selectPayType = (int) requestMap.get("payType");
        }
        assert requestMap != null;
        if (requestMap != null) {
            requestMap.remove("transNo");
            requestMap.remove("redEnvelopeAmount");
            requestMap.remove("cardId");
            requestMap.remove("donationInstiuteId");
            requestMap.remove("tipAmount");
            payType = (int) requestMap.get("payType");
        }
        title = findViewById(R.id.title);
        next = findViewById(R.id.next);
        msg = findViewById(R.id.msg);
        payIns = findViewById(R.id.pay_ins);
        changeCard = findViewById(R.id.change_card);
        close = findViewById(R.id.close);
        if (!TextUtils.isEmpty(errorMessage)) {
            msg.setText(errorMessage);
        }
        close.setOnClickListener(this);
        payIns.setOnClickListener(this);
        changeCard.setOnClickListener(this);
        //切换字体
        TypefaceUtil.replaceFont(title, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(msg, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(next, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(payIns, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(changeCard, "fonts/gilroy_semiBold.ttf");
        if (process == 1) {
            next.setVisibility(View.VISIBLE);
            title.setText("Payment processing");
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(PayFailedActivity.this, MainTab.class);
                    it.putExtra("num", 2);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
                    startActivity(it);
                    finish();
                }
            });
        } else {
            if (requestMap.containsKey("posTransNo")) {
                next.setVisibility(View.VISIBLE);
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(PayFailedActivity.this, MainTab.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
                        intent1.putExtra("merchantId", (String) requestMap.get("merchantId"));
                        startActivity(intent1);
                        finish();
                    }
                });
            } else {
                exeFenfiKaitong = false;
                SharePreferenceUtils sharePreferenceUtils1 = new SharePreferenceUtils(PayFailedActivity.this, StaticParament.USER);
                String userId1 = (String) sharePreferenceUtils1.get(StaticParament.USER_ID, "");
                Map<String, Object> maps1 = new HashMap<>();
                maps1.put("userId", userId1);
                maps1.put("stripeState", 0);//用户区分接入stripe前后的新老用户
                jiaoyanFenqifu(maps1, false);
            }
        }
    }

    private boolean exeFenfiKaitong = false;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.close:
                if (requestMap.containsKey("posTransNo")) {
                    Intent intent1 = new Intent(PayFailedActivity.this, MainTab.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
                    intent1.putExtra("id", (String) requestMap.get("merchantId"));
                    startActivity(intent1);
                    finish();
                } else {
                    Intent it = new Intent(PayFailedActivity.this, PayMoneyActivity.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
                    startActivity(it);
                    finish();
                }
                break;
            case R.id.pay_ins://
                if (oldPeople) {
                    Intent intents = new Intent(PayFailedActivity.this, PayMoneyActivity.class);
                    intents.putExtra("oldPeople", "1");
                    intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intents.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intents);
                    finish();
                } /*else if (noInstal) {
                    Intent intents = new Intent(PayFailedActivity.this, PayMoneyActivity.class);
                    intents.putExtra("noInstal", "1");
                    intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intents.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intents);
                    finish();
                }*/ else {
                    exeFenfiKaitong = true;
                    SharePreferenceUtils sharePreferenceUtils1 = new SharePreferenceUtils(PayFailedActivity.this, StaticParament.USER);
                    String userId1 = (String) sharePreferenceUtils1.get(StaticParament.USER_ID, "");
                    Map<String, Object> maps1 = new HashMap<>();
                    maps1.put("userId", userId1);
                    maps1.put("stripeState", 0);//用户区分接入stripe前后的新老用户
                    jiaoyanFenqifu(maps1, true);
//                    requestMap.put("payType", 4);
//                    getPayAmount();
                }
                break;
            case R.id.change_card:
                Intent intent = new Intent(this, SelectBankCardActivity.class);
                requestMap.put("payType", selectPayType);
                if (!TextUtils.isEmpty(selectCardId)) {
                    intent.putExtra("cardId", selectCardId);
                }
                intent.putExtra("requestMap", requestMap);
                intent.putExtra("fromPayFailed", 1);
                //startActivityForResult(intent, 106);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 106 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String cardNoSelect = bundle.getString("cardNo");
            String cardIdSelect = bundle.getString("cardId");
            requestMap.put("payType", payType);
            requestMap.put("cardId", cardIdSelect);
            getPayAmount();
        }
    }


    private void getPayAmount() {
        payListener.getPayTransAmountDetail(requestMap, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                TransAmountData data = (TransAmountData) o[0];
                Intent intent = new Intent(PayFailedActivity.this, PayMoneyActivity.class);
                intent.putExtra("data", (Serializable) data);
                intent.putExtra("requestMap", requestMap);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String e) {

            }
        });
    }

    private void jiaoyanFenqifu(Map<String, Object> maps, final boolean showProgress) {
        RequestUtils requestUtils1 = new RequestUtils(PayFailedActivity.this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {
                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.jiaoyanFenqifu(headerMap, requestBody);
                if (showProgress) {
                    requestUtils.Call(call);
                } else {
                    requestUtils.Call(call, null, false);
                }
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                Gson gson = new Gson();
                FenqifuStatue fenqifuStatue = gson.fromJson(dataStr, FenqifuStatue.class);
                String creditCardState = String.valueOf(fenqifuStatue.getCreditCardState());//0：新用户未绑卡 1：已绑卡  2：老用户未绑卡
                String cardState = String.valueOf(fenqifuStatue.getCardState());//0：未绑卡 1：已绑卡
                String installmentState = String.valueOf(fenqifuStatue.getInstallmentState());//2 分期付开通 ；！2分期付没有开通
                if (exeFenfiKaitong) {
                    SharePreferenceUtils appMsgSharePreferenceUtils =
                            new SharePreferenceUtils(PayFailedActivity.this, StaticParament.DEVICE);
                    String callPhone = (String) appMsgSharePreferenceUtils.get(StaticParament.APP_PHONE, "");

                    SelectPayTypeActivity.shifoukaitongCardPay = fenqifuStatue.getCardState();//0：未开通开支付  1:开通开卡支付
                    SelectPayTypeActivity.meikaitongfenqifuAndkaitongCardPay = fenqifuStatue.getCreditCardAgreementState();

                    FirstFragment.backStatue = 4;
                    if (fenqifuStatue.getKycState() == 0) {//未开通kyc
                        Intent intent_register = new Intent(PayFailedActivity.this, RegistTwo_Activity.class); //跳到注册页 从注册页跳到kyc页
                        intent_register.putExtra("jumpToKyc", true);
                        startActivity(intent_register);
                    } else {//已经开通kyc
                        if (fenqifuStatue.getCardState() == 0) {//未开通卡支付
                            Intent intent_kyc = new Intent(PayFailedActivity.this, NewAddBankCardActivity.class);
                            intent_kyc.putExtra("kaitongfenqifuRoad", true);
                            startActivity(intent_kyc);
                        } else {//已经开通卡支付 //判断illion状态
                            if (fenqifuStatue.getCreditCardAgreementState() == 0) {//是否勾选过分期付绑卡协议 0：未勾选 1：已勾选
                                Intent intent_kyc = new Intent(PayFailedActivity.this, ChooseCardActivity.class);
                                startActivity(intent_kyc);
                            } else {
                                if (fenqifuStatue.getInstallmentState() == 2) {//开通illion 开通了分期付
                                    availableCredit = PayMoneyActivity.availableCredit;
                                    HashMap<String, Object> maps = new HashMap<>();
                                    SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(PayFailedActivity.this, StaticParament.USER);
                                    String payUserId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                                    maps.put("no_user_id", 1);
                                    maps.put("payUserId", payUserId);
                                    maps.put("merchantId", (String) requestMap.get("merchantId"));

                                    maps.put("transAmount", PayFailedActivity.this.requestMap.get("transAmount"));
                                    maps.put("payType", 4);
                                    maps.put("recUserId", PayFailedActivity.this.requestMap.get("recUserId"));
                                    getQrPayTransAmount(maps);
                                } else if (fenqifuStatue.getInstallmentState() == 0 || fenqifuStatue.getInstallmentState() == 3) {//未开通！ 0. 用户未开通分期付(需要完善信息) 3. illion未授权
                                    Intent intent_kyc = new Intent(PayFailedActivity.this, ChooseIllionActivity.class);
                                    startActivity(intent_kyc);
                                } else if (fenqifuStatue.getInstallmentState() == 1 || fenqifuStatue.getInstallmentState() == 5 || fenqifuStatue.getInstallmentState() == 8) {//—-失败！—— 1. 用户分期付已冻结 5. 用户分期付禁用 8 机审拒绝
                                    Intent intent_kyc = new Intent(PayFailedActivity.this, KycAndIllionResultActivity.class);
                                    intent_kyc.putExtra("error", "FkReject");
                                    intent_kyc.putExtra("phone", callPhone);
                                    startActivity(intent_kyc);
                                } else if (fenqifuStatue.getInstallmentState() == 4 || fenqifuStatue.getInstallmentState() == 7 || fenqifuStatue.getInstallmentState() == 9) {//—-等待！——4. 等待人工审核 7. 机审中 9 分期付风控未触发
                                    Intent intent_kyc = new Intent(PayFailedActivity.this, KycAndIllionResultActivity.class);
                                    intent_kyc.putExtra("error", "Waiting");
                                    intent_kyc.putExtra("phone", callPhone);
                                    startActivity(intent_kyc);
                                }
                            }
                        }
                    }

                } else {
                    oldPeople = false;
                    if (cardState.equals("1") && payType == 4) {// //0：卡支付 4：分期付
                        changeCard.setVisibility(View.VISIBLE);
                        payIns.setVisibility(View.GONE);
                    }
                    if (payType == 0) {
                        payIns.setVisibility(View.VISIBLE);
                        changeCard.setVisibility(View.VISIBLE);
                    }
                    if (installmentState.equals("2") && creditCardState.equals("2")) {
                        //老用户
                        oldPeople = true;
                    }
                    if (!installmentState.equals("2")) {
                        noInstal = true;
                    }
                }
            }

            @Override
            public void resError(String error) {

            }
        });
    }

    private String availableCredit;

    public void getQrPayTransAmount(final HashMap<String, Object> maps) {
        payListener.getPayTransAmountDetail(maps, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                TransAmountData data = (TransAmountData) o[0];
                data.setMerchantName(data11.getMerchantName());
                data.setAvailableCredit(availableCredit);
                Intent intent = new Intent(PayFailedActivity.this, ConfirmPayActivity.class);
                intent.putExtra("data", data);
                PayFailedActivity.this.requestMap.put("payType", 4);//变成分期付
                intent.putExtra("requestMap", PayFailedActivity.this.requestMap);
                intent.putExtra("productId", creditProductId);
                intent.putExtra("merchantId", (String) requestMap.get("merchantId"));
                startActivity(intent);
            }

            @Override
            public void onError(String e) {


            }
        });
    }
  /*  @Override
    public void openInvestStatus(String code, String fenqiCard, String zhifu, String fenqi) {
        if (zhifu != null && fenqi != null && fenqiCard != null) {
            oldPeople = false;
            if (zhifu.equals("1") && payType == 4) {
                changeCard.setVisibility(View.VISIBLE);
                payIns.setVisibility(View.GONE);
            }
            if (payType == 0) {
                payIns.setVisibility(View.VISIBLE);
                changeCard.setVisibility(View.VISIBLE);
            }
            if (fenqi.equals("1") && fenqiCard.equals("0")) {
                //老用户
                oldPeople = true;
            }
            if (fenqi.equals("0")) {
                noInstal = true;
            }
        }
    }*/

    @Override
    public void onBackPressed() {
        // 完全由自己控制返回键逻辑，系统不再控制，但是有个前提是：　　
        // 不要在Activity的onKeyDown或者OnKeyUp中拦截掉返回键　　
        // 拦截：就是在OnKeyDown或者OnKeyUp中自己处理了返回键　　
        // （这里处理之后return true.或者return false都会导致onBackPressed不会执行）
        // 不拦截：在OnKeyDown和OnKeyUp中返回super对应的方法　　
        // （如果两个方法都被覆写就分别都要返回super.onKeyDown,super.onKeyUp）
        if (requestMap.containsKey("posTransNo")) {
            Intent intent1 = new Intent(PayFailedActivity.this, MainTab.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
            intent1.putExtra("id", (String) requestMap.get("merchantId"));
            startActivity(intent1);
            finish();
        } else {
            if (process == 1) {
                Intent it = new Intent(PayFailedActivity.this, MainTab.class);
                it.putExtra("num", 2);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
                startActivity(it);
                finish();
            } else {
                Intent intent = new Intent(PayFailedActivity.this, PayMoneyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        }
    }
}