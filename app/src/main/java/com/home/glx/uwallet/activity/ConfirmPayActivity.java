package com.home.glx.uwallet.activity;

import static com.home.glx.uwallet.selfview.PaymentWaitingDialog.exePayResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.braze.Braze;
import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.xzc.PaySuccessActivity_Zhifu;
import com.home.glx.uwallet.activity.xzc.promotion.PromotionListActivity;
import com.home.glx.uwallet.activity.xzc.promotion.Zhifu_PromotionListActivity;
import com.home.glx.uwallet.datas.PaySuccessData;
import com.home.glx.uwallet.datas.TransAmountData;
import com.home.glx.uwallet.mvp.GetApiToken;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.MerchantListener;
import com.home.glx.uwallet.mvp.contract.PayListener;
import com.home.glx.uwallet.mvp.model.MerchantModel;
import com.home.glx.uwallet.mvp.model.PayModel;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.AddBankCardTipDialog;
import com.home.glx.uwallet.selfview.CashlineView;
import com.home.glx.uwallet.selfview.DonateDialog;
import com.home.glx.uwallet.selfview.EnterPayPinDialog;
import com.home.glx.uwallet.selfview.EnterTipAmountDialog;
import com.home.glx.uwallet.selfview.PaymentWaitingDialog;
import com.home.glx.uwallet.selfview.SwitchView;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.BzEventParameterName;
import com.home.glx.uwallet.tools.BzEventType;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.SoftKeyBoardListener;
import com.home.glx.uwallet.tools.TypefaceUtil;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.CardParams;
import com.stripe.android.model.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ConfirmPayActivity extends MainActivity implements View.OnClickListener, View.OnTouchListener {
    private PayListener payListener;
    private TextView merchantName;
    private TextView orderAmount;
    private TextView discount;
    private TextView discountAmount;
    private TextView usePayo;
    private TextView availableAmount;
    private EditText inputPocketAmount;
    private TextView usePocketAmount;
    private TextView totalAmount;
    private TextView totalAmountTv;
    private TextView availableTv;
    private TextView availableCreditAmount;
    private TextView paymentsNum;
    private TextView everyWeekAmount;
    private TextView payFirstAmount;
    private TextView cardNo_youka;
    private TextView next;
    private ImageView back;
    private TransAmountData data;
    private HashMap<String, Object> requestMap;
    private LinearLayout discountLl;
    private LinearLayout pocketLl;
    private LinearLayout order_amount_ll;
    //订单号
    private String transNo;
    //卡id。第一次调用时传空，后续切换卡时带入
    private String cardId;
    //用户输入可用红包金额,不能为负数（如果输入金额大于实付金额 后台会抛错）。第一次进入时不传或传0
    private String redEnvelopeAmount = "0";
    //支付方式 0：卡支付  4：分期付  5:卡支付没有卡
    private int payType;
    private LinearLayout useInsLl;//分期付

    private TextView defaultCardNo;
    private ImageView cardLogo_youka;//分期付卡logo
    private SwitchView selectPocket;
    private LinearLayout pocketAmountLl;
    private ImageView defaultCardLogo;//卡支付 卡logo
    private RelativeLayout insSlectCard;//分期付
    private RelativeLayout bankSlectCard;//卡支付有卡
    private RelativeLayout add_card;//卡支付没有卡

    private RelativeLayout viewSchedulell;
    private String productId;
    private String merchantId;
    private String availableCredit;
    private String apiToken;
    //成功调用接口的红包金额
    private String haveUsePocket = "0";
    //第一次实付金额，使用红包都和这个金额比较
    private String firstPayAmount;
    private TextView donateTitle;
    private TextView donateText;
    private ImageView donateQuest;
    private CheckBox choiceDonate;
    private LinearLayout tipNo;
    private LinearLayout tip5;
    private LinearLayout tip10;
    private LinearLayout tip15;
    private LinearLayout tipEnter;
    private TextView tipNoText;
    private TextView tip5Text;
    private TextView tip5Money;
    private TextView tip10Text;
    private TextView tip10Money;
    private TextView tip15Text;
    private TextView tip15Money;
    private TextView tipEnterMoney;
    private TextView tv_fenjifen;//分期付 分几期的展示
    private boolean open = false;//控制分期详情的打开和关闭
    private TextView promotion;//展示promotion金额
    private String couponId = "";
    private String couponAmount;


    private EditText cardNo;
    private EditText et_date_left;
    private EditText et_date_center;
    private EditText et_date_right;
    private EditText et_id_cvv;
    long time = 0;
    private boolean exeFucusLeft = true;
    private boolean leftFocus = false;
    private boolean rightFocus = false;
    private ImageView cardLogo;//卡支付 输入框后面的卡logo
//    private ImageView cardLogo_3;

    private PaymentWaitingDialog paymentWaitingDialog;

    private boolean haveCard = true;

    private boolean fenqifubiankazhifu = false;
    private TextView tv_add_pro;
    private MerchantListener merchantListener;

    @Override
    public int getLayout() {
        return R.layout.activity_confirm_pay;
    }

    @Override
    public void initView() {
        super.initView();
        GetApiToken getApiToken = new GetApiToken(this);
        getApiToken.getApiToken(new HashMap<String, Object>());
        getApiToken.setOnGetApiToken(new GetApiToken.OnGetApiToken() {
            @Override
            public void getToken(String token) {
                if (token != null) {
                    apiToken = token;
                    L.log("apiToken：" + apiToken);
                }
            }
        });
        merchantListener = new MerchantModel(ConfirmPayActivity.this);

        payListener = new PayModel(this);
        productId = getIntent().getStringExtra("productId");
        merchantId = getIntent().getStringExtra("merchantId");
        haveCard = getIntent().getBooleanExtra("haveCard", true);
        requestMap = (HashMap<String, Object>) getIntent().getSerializableExtra("requestMap");
        payType = (int) requestMap.get("payType");
//        if (payType == 0) {//删除
//            payType = 5;
//        }
        back = findViewById(R.id.back);
        viewSchedulell = findViewById(R.id.view_schedule_ll);
        viewSchedulell.setOnClickListener(this);
        insSlectCard = findViewById(R.id.due_today_ll);
        bankSlectCard = findViewById(R.id.select_card);
        insSlectCard.setOnClickListener(this);
        bankSlectCard.setOnClickListener(this);
        findViewById(R.id.choose_card_fenqifu).setOnClickListener(this);
        findViewById(R.id.choose_card).setOnClickListener(this);
        selectPocket = (SwitchView) findViewById(R.id.select_pocket);
        pocketAmountLl = findViewById(R.id.pocket_amount_ll);
        cardLogo_youka = findViewById(R.id.card_logo);
        defaultCardLogo = findViewById(R.id.default_card_logo);
        useInsLl = findViewById(R.id.use_ins_ll);
        discountLl = findViewById(R.id.discount_ll);
        pocketLl = findViewById(R.id.pocket_ll);
        order_amount_ll = findViewById(R.id.order_amount_ll);
        merchantName = findViewById(R.id.merchant_name);
        TextView orderAmountTv = findViewById(R.id.order_amount_tv);
        orderAmount = findViewById(R.id.order_amount);
        promotion = findViewById(R.id.promotion);
        discount = findViewById(R.id.discount);
        tv_add_pro = findViewById(R.id.tv_add_pro);
        discountAmount = findViewById(R.id.discount_amount);
        usePayo = findViewById(R.id.use_payo_money);
        availableAmount = findViewById(R.id.available_amount);
        inputPocketAmount = findViewById(R.id.input_pocket_amount);
        usePocketAmount = findViewById(R.id.use_pocket_amount);
        totalAmount = findViewById(R.id.total_amount);
        totalAmountTv = findViewById(R.id.total_amount_tv);
        availableTv = findViewById(R.id.available_tv);
        availableCreditAmount = findViewById(R.id.available_credit_amount);
        paymentsNum = findViewById(R.id.payments_num);
        everyWeekAmount = findViewById(R.id.every_week_amount);
        payFirstAmount = findViewById(R.id.pay_first_amount);
        cardNo_youka = findViewById(R.id.card_no);
        TextView amountStart = findViewById(R.id.amount_start);
        defaultCardNo = findViewById(R.id.default_card_no);
        next = findViewById(R.id.next);
        donateTitle = findViewById(R.id.donate_title);
        donateText = findViewById(R.id.donate_text);
        donateQuest = findViewById(R.id.donate_quest);
        choiceDonate = findViewById(R.id.choice_donate);
        tipNo = findViewById(R.id.tip_no);
        tip5 = findViewById(R.id.tip_5);
        tip10 = findViewById(R.id.tip_10);
        tip15 = findViewById(R.id.tip_15);
        tipEnter = findViewById(R.id.tip_enter);
        tipNo.setOnTouchListener(this);
        tip5.setOnTouchListener(this);
        tip10.setOnTouchListener(this);
        tip15.setOnTouchListener(this);
        tipEnter.setOnTouchListener(this);
        TextView addATip = findViewById(R.id.add_a_tip);
        tipNoText = findViewById(R.id.tip_no_text);
        tip5Text = findViewById(R.id.tip_5_text);
        tip5Money = findViewById(R.id.tip_5_money);
        tip10Text = findViewById(R.id.tip_10_text);
        tip10Money = findViewById(R.id.tip_10_money);
        tip15Text = findViewById(R.id.tip_15_text);
        tip15Money = findViewById(R.id.tip_15_money);
        tipEnterMoney = findViewById(R.id.tip_enter_amount);
        tv_fenjifen = findViewById(R.id.tv_fenjifen);
        cardLogo = findViewById(R.id.cardLogo);
//        cardLogo_3 = findViewById(R.id.cardLogo_3);
        next.setOnClickListener(this);
        back.setOnClickListener(this);
        findViewById(R.id.add_pro).setOnClickListener(this);
        tv_add_pro.setOnClickListener(this);
        donateQuest.setOnClickListener(this);

        add_card = findViewById(R.id.add_card);
        if (payType == 4) {//分期付
            findViewById(R.id.available_amount_ll).setVisibility(View.VISIBLE);
            useInsLl.setVisibility(View.VISIBLE);
            bankSlectCard.setVisibility(View.GONE);
            add_card.setVisibility(View.GONE);

            findViewById(R.id.arrow1).setVisibility(View.VISIBLE);//为了右边不对其
            findViewById(R.id.arrow2).setVisibility(View.VISIBLE);
            findViewById(R.id.arrow3).setVisibility(View.VISIBLE);
            findViewById(R.id.arrow4).setVisibility(View.VISIBLE);
            findViewById(R.id.arrow5).setVisibility(View.VISIBLE);
        } else if (payType == 0) {//卡支付
            if (haveCard) {
                findViewById(R.id.available_amount_ll).setVisibility(View.GONE);
                useInsLl.setVisibility(View.GONE);
                bankSlectCard.setVisibility(View.VISIBLE);
                add_card.setVisibility(View.GONE);

                findViewById(R.id.arrow1).setVisibility(View.GONE);//为了右边对其
                findViewById(R.id.arrow2).setVisibility(View.GONE);
                findViewById(R.id.arrow3).setVisibility(View.GONE);
                findViewById(R.id.arrow4).setVisibility(View.GONE);
                findViewById(R.id.arrow5).setVisibility(View.GONE);
            } else {//卡支付没有卡
                initcardPayNoCard();
            }

        }         //切换字体
        TypefaceUtil.replaceFont(addATip, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(tipNoText, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(tip5Text, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(tip5Money, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(tip10Text, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(tip15Text, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(tip15Money, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(tipEnterMoney, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(tip10Money, "fonts/gilroy_semiBold.ttf");

        TypefaceUtil.replaceFont(donateTitle, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(donateText, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(tv_add_pro, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(amountStart, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(defaultCardNo, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.youxiaoqi), "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.youxiaoqi2), "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(next, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(tv_fenjifen, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(cardNo_youka, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(payFirstAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.tv_duetoday_hint), "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(everyWeekAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(paymentsNum, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(availableCreditAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(merchantName, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(orderAmountTv, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.promotion_tv), "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(orderAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(promotion, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(discount, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(discountAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(usePayo, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(availableAmount, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(inputPocketAmount, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(usePocketAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(totalAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(totalAmountTv, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.debit), "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(availableTv, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.choose_card_fenqifu), "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.choose_card), "fonts/gilroy_semiBold.ttf");

        data = (TransAmountData) getIntent().getSerializableExtra("data");
        firstPayAmount = data.getTransAmount();
        availableCredit = data.getAvailableCredit();
        showData();
        choiceDonate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getData();
            }
        });

        Map<String, Object> maps = new HashMap<>();
        getPayGateWay(ConfirmPayActivity.this, maps);
    }

    private void initcardPayNoCard() {
        findViewById(R.id.available_amount_ll).setVisibility(View.GONE);
        useInsLl.setVisibility(View.GONE);
        bankSlectCard.setVisibility(View.GONE);
        add_card.setVisibility(View.VISIBLE);

        cardNo = findViewById(R.id.add_card_no);
        TypefaceUtil.replaceFont(cardNo, "fonts/gilroy_medium.ttf");
        spaceDivide(cardNo);
        cardNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    et_date_left.setCursorVisible(true);
                    return false;
                }
                return false;
            }
        });
        et_date_left = (EditText) findViewById(R.id.et_date_left);
        et_date_center = (EditText) findViewById(R.id.et_date_center);
        et_date_right = (EditText) findViewById(R.id.et_date_right);
        TypefaceUtil.replaceFont(et_date_left, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(et_date_center, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(et_date_right, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.protext), "fonts/gilroy_medium.ttf");
        et_id_cvv = findViewById(R.id.et_id_cvv);
        TypefaceUtil.replaceFont(et_id_cvv, "fonts/gilroy_medium.ttf");
        findViewById(R.id.addcard_yiwen).setOnClickListener(new View.OnClickListener() {//问号的点击事件
            @Override
            public void onClick(View v) {
                AddBankCardTipDialog dialog = new AddBankCardTipDialog(ConfirmPayActivity.this);
                dialog.showAddBankBtn(false);
                dialog.show();
            }
        });
        cardNo.setText("");
        et_date_left.setText("");
        et_date_center.setVisibility(View.GONE);
        et_date_right.setText("");
        et_id_cvv.setText("");
        et_date_left.setHint("Expiry date (mm/yy)");

        et_date_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_date_left.setCursorVisible(true);
                et_date_right.setCursorVisible(false);

                et_date_left.setHint("");
                et_date_center.setVisibility(View.VISIBLE);

                cardNo.setCursorVisible(false);
                et_id_cvv.setCursorVisible(false);
                return false;
            }
        });
        et_date_right.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        boolean aa = false;
                        if (event.getAction() == MotionEvent.ACTION_UP) {

                            String context = et_date_left.getText().toString().trim();
                            if (context.length() == 2 && (context.charAt(0) == 0 || (context.charAt(0) != 0 && Integer.valueOf(context) > 0 && Integer.valueOf(context) <= 12))) {

                                et_date_right.setCursorVisible(true);
                                et_date_right.setFocusable(true);
                                et_date_right.setFocusableInTouchMode(true);
                                et_date_right.requestFocus();
                                et_date_left.setCursorVisible(false);
                                cardNo.setCursorVisible(false);
                                et_id_cvv.setCursorVisible(false);
                            } else {
                                if (et_date_center.getVisibility() == View.GONE || TextUtils.isEmpty(et_date_center.getText().toString()) || exeFucusLeft) {//没有填写月份和年份 光标定在月份上。
                                    time = System.currentTimeMillis();
                                    et_date_right.setCursorVisible(false);
                                    et_date_right.setFocusable(false);
                                    et_date_right.setFocusableInTouchMode(false);
                                    et_date_left.requestFocus();
                                    et_date_left.setHint("");
                                    et_date_center.setVisibility(View.VISIBLE);
                                    et_date_left.setCursorVisible(true);
                                    et_date_right.setCursorVisible(false);
                                    cardNo.setCursorVisible(false);
                                    et_id_cvv.setCursorVisible(false);
                                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(et_date_left, InputMethodManager.SHOW_IMPLICIT);
                                    exeFucusLeft = false;
                                    aa = false;
                                } else if (System.currentTimeMillis() - time > 500) {

                                    et_date_right.setCursorVisible(true);
                                    et_date_right.setFocusable(true);
                                    et_date_right.setFocusableInTouchMode(true);
                                    et_date_right.requestFocus();
                                    et_date_left.setCursorVisible(false);
                                    cardNo.setCursorVisible(false);
                                    et_id_cvv.setCursorVisible(false);

                                    aa = false;
                                }
                            }
                        }
                        return aa;
                    }

                });

        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {//软键盘弹出
                if (et_date_left.hasFocus()) {
                    et_date_left.setCursorVisible(true);
                    cardNo.setCursorVisible(false);
                    et_id_cvv.setCursorVisible(false);
                    et_date_left.setHint("");//下面的提示文字
                }
            }

            @Override
            public void keyBoardHide(int height) {

            }
        });
        et_date_left.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String string = charSequence.toString();
                if (string.length() == 2) {//把最前面的空格删除 保持输入两位数字
                    String s = string.substring(0, 1);
                    if (s.equals(" ")) {
                        et_date_left.setText(string.substring(1, s.length()));
                    }
                }
                String context = et_date_left.getText().toString().trim();
                if (context.length() == 2 && (context.charAt(0) == 0 || (context.charAt(0) != 0 && Integer.valueOf(context) > 0 && Integer.valueOf(context) <= 12))) {

                    et_date_right.setCursorVisible(true);
                    et_date_right.setFocusable(true);
                    et_date_right.setFocusableInTouchMode(true);
                    et_date_right.requestFocus();
                    et_date_left.setCursorVisible(false);
                    cardNo.setCursorVisible(false);
                    et_id_cvv.setCursorVisible(false);
                }

                check();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_date_left.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                leftFocus = b;
                if (!leftFocus) {
                    if (!TextUtils.isEmpty(et_date_left.getText().toString()) && !et_date_left.getText().toString().equals(" ") && (Integer.valueOf(et_date_left.getText().toString()) > 12 || et_date_left.getText().toString().equals("00") || et_date_left.getText().toString().equals("0"))) {
                        et_date_left.setText("");
                    }
                }
                if (leftFocus || rightFocus) {
                    et_date_left.setHint("");
                    et_date_center.setVisibility(View.VISIBLE);
                } else {//exe

                    if ((TextUtils.isEmpty(et_date_left.getText().toString().trim())) && TextUtils.isEmpty(et_date_right.getText().toString().trim())) {
                        et_date_center.setVisibility(View.GONE);
                        et_date_left.setHint("Expiry date (mm/yy)");
                    }
                    if (et_date_left.getText().toString().trim().length() == 1) {//＜10 前面补零
                        et_date_left.setText("0" + et_date_left.getText().toString().trim());
                    }

                }

            }
        });

        et_date_right.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                check();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_date_right.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                rightFocus = b;
                if (!rightFocus) {
                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int lastTwoWeiOfYear = Integer.valueOf(String.valueOf(year).substring(2));
                    if (!TextUtils.isEmpty(et_date_right.getText().toString()) && Integer.valueOf(et_date_right.getText().toString()) < lastTwoWeiOfYear) {
                        et_date_right.setText("");
                    }

                    et_date_left.setHint("");//下面的提示文字
                }
                if (leftFocus || rightFocus) {//exe
                    et_date_left.setHint("");//下面的提示文字
                    et_date_center.setVisibility(View.VISIBLE);
                } else {
                    if ((TextUtils.isEmpty(et_date_left.getText().toString().trim())) && TextUtils.isEmpty(et_date_right.getText().toString().trim())) {
                        et_date_center.setVisibility(View.GONE);
                        et_date_left.setHint("Expiry date (mm/yy)");
                    }

                    if (et_date_right.getText().toString().trim().length() == 1) {//＜10 前面补零
                        et_date_right.setText("0" + et_date_right.getText().toString().trim());
                    }

                }
            }
        });

        cardNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cardNo.setCursorVisible(true);
                    et_id_cvv.setCursorVisible(false);
                    et_date_left.setCursorVisible(false);
                    et_date_right.setCursorVisible(false);
                } else {
                    if (cardNo.getText().toString().trim().replaceAll(" ", "").length() >= 16) {
                        Map<String, Object> maps = new HashMap<>();
                        maps.put("cardBin", cardNo.getText().toString().trim().replaceAll(" ", ""));
                        PromotionListActivity.showTishgiDialog = true;
                        latpayGetCardType(maps);
                    } else {
                        cardLogo.setVisibility(View.GONE);
//                                cardLogo_3.setVisibility(View.GONE);
                    }
                }
            }
        });
        et_id_cvv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_id_cvv.setCursorVisible(true);
                    cardNo.setCursorVisible(false);
                    et_date_left.setCursorVisible(false);
                    et_date_right.setCursorVisible(false);
                }
            }
        });

        et_id_cvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                check();

            }
        });

        findViewById(R.id.arrow1).setVisibility(View.GONE);//为了右边对其
        findViewById(R.id.arrow2).setVisibility(View.GONE);
        findViewById(R.id.arrow3).setVisibility(View.GONE);
        findViewById(R.id.arrow4).setVisibility(View.GONE);
        findViewById(R.id.arrow5).setVisibility(View.GONE);

        next.setBackground(ContextCompat.getDrawable(this, R.drawable.view_yj_hui));
        next.setEnabled(false);
    }

    private void getData() {
        if (!TextUtils.isEmpty(transNo)) {
            requestMap.put("transNo", transNo);
        } else {
            requestMap.remove("transNo");

        }
        if (!TextUtils.isEmpty(redEnvelopeAmount)) {
            requestMap.put("redEnvelopeAmount", redEnvelopeAmount);
        }
        if (!TextUtils.isEmpty(cardId)) {
            requestMap.put("cardId", cardId);
        } else {
            requestMap.remove("cardId");
        }
        if (choiceDonate.isChecked()) {
            requestMap.put("donationInstiuteId", data.getDonationData().getId());
        } else {
            requestMap.remove("donationInstiuteId");
        }
        if (tipNo.getTag().equals("1")) {
            requestMap.remove("tipAmount");
        }
        if (tip5.getTag().equals("1")) {
            requestMap.put("tipAmount", data.getTipList().get(0).getAmount());
        }
        if (tip10.getTag().equals("1")) {
            requestMap.put("tipAmount", data.getTipList().get(1).getAmount());
        }
        if (tip15.getTag().equals("1")) {
            requestMap.put("tipAmount", data.getTipList().get(2).getAmount());
        }
        if (tipEnter.getTag().equals("1")) {
            if (tipEnterMoney.getText().toString().contains("$")) {
                requestMap.put("tipAmount", tipEnterMoney.getText().toString().substring(1));
            } else {
                requestMap.remove("tipAmount");
            }
        }
        payListener.getPayTransAmountDetail(requestMap, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                TransAmountData datas = (TransAmountData) o[0];
                haveUsePocket = redEnvelopeAmount;
                data = datas;
                showData();
            }

            @Override
            public void onError(String e) {
                redEnvelopeAmount = haveUsePocket;
                if (!haveUsePocket.equals("0")) {
                    inputPocketAmount.setText(haveUsePocket);
                } else {
                    inputPocketAmount.setText("");
                }
            }
        });
    }

    private void showData() {

        if (data.getTipList() != null) {
            tip5Text.setText(data.getTipList().get(0).getRatio());
            tip5Money.setText("$" + data.getTipList().get(0).getAmount());
            tip10Text.setText(data.getTipList().get(1).getRatio());
            tip10Money.setText("$" + data.getTipList().get(1).getAmount());
            tip15Text.setText(data.getTipList().get(2).getRatio());
            tip15Money.setText("$" + data.getTipList().get(2).getAmount());
        }
        if (!TextUtils.isEmpty(data.getCardId())) {
            cardId = data.getCardId();
            requestMap.put("cardId", data.getCardId());
        }
        if (data.getPreviewRepayPlanList() != null) {
            fenjiqiInit(data.getPreviewRepayPlanList());
        }
//        selectPocket.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (/*selectPocket.getTag().equals("choice")*/!selectPocket.isOpened()) {
//                    pocketAmountLl.setVisibility(View.GONE);
//                    selectPocket.setTag("unchoice");
//                    inputPocketAmount.setText("");
//                    redEnvelopeAmount = "0";
//                    if (haveKeybord) {
//                        PublicTools.closeKeybord(ConfirmPayActivity.this);
//                    } else {
//                        getData();
//                    }
//                } else {
//                    selectPocket.setTag("choice");
//                    pocketAmountLl.setVisibility(View.VISIBLE);
//                }
//            }
//        });

        if (data.getTransNo() != null) {
            transNo = data.getTransNo();
        }
        if (data.getMerchantName() != null) {
            merchantName.setText(data.getMerchantName());
        }
        if (payType == 4) {
            if (data.getAvailableCredit() != null) {
                availableCreditAmount.setText("$" + PublicTools.twoend(data.getAvailableCredit()));
            }
            if (data.getPeriod() != null) {
                tv_fenjifen.setText(data.getPeriod());
            }
            if (data.getCreditFirstCardPayAmount() != null) {//Due today
                payFirstAmount.setText("$" + PublicTools.twoend(data.getCreditFirstCardPayAmount()));
            }
            everyWeekAmount.setText("$" + PublicTools.twoend(data.getCreditAverageAmount()));//every Wednesday fortnightly
            Log.d("xmxmxmmxm", "showData: " + data.getPayAmount());
            if (Float.valueOf(data.getPayAmount()) < 10) {//分期付金额少于10美金  切换到卡支付
                findViewById(R.id.available_amount_ll).setVisibility(View.GONE);
                useInsLl.setVisibility(View.GONE);
                bankSlectCard.setVisibility(View.VISIBLE);
                add_card.setVisibility(View.GONE);

                findViewById(R.id.arrow1).setVisibility(View.GONE);//为了右边对其
                findViewById(R.id.arrow2).setVisibility(View.GONE);
                findViewById(R.id.arrow3).setVisibility(View.GONE);
                findViewById(R.id.arrow4).setVisibility(View.GONE);
                findViewById(R.id.arrow5).setVisibility(View.GONE);
                fenqifubiankazhifu = true;
            } else {
                findViewById(R.id.available_amount_ll).setVisibility(View.VISIBLE);
                useInsLl.setVisibility(View.VISIBLE);
                bankSlectCard.setVisibility(View.GONE);
                add_card.setVisibility(View.GONE);
                fenqifubiankazhifu = false;

                findViewById(R.id.arrow1).setVisibility(View.VISIBLE);//为了右边不对其
                findViewById(R.id.arrow2).setVisibility(View.VISIBLE);
                findViewById(R.id.arrow3).setVisibility(View.VISIBLE);
                findViewById(R.id.arrow4).setVisibility(View.VISIBLE);
                findViewById(R.id.arrow5).setVisibility(View.VISIBLE);
            }
        }
        if (changeCardInfo) {
            if (data.getCardNo() != null) {
                if (data.getCardNo().length() > 4) {
                    String card = data.getCardNo().replaceAll(" ", "");
//                initCardNo = card;
//                shijiShowCardNo = card;
                    card = "•••• " + card.substring(card.length() - 4);
                    cardNo_youka.setText(card);
                    defaultCardNo.setText(card);
                } else {
                    cardNo_youka.setText(data.getCardNo());
                    defaultCardNo.setText(data.getCardNo());
                }
                if (!TextUtils.isEmpty(data.getCustomerCcExpyr()) && !TextUtils.isEmpty(data.getCustomerCcExpmo())) {
                    ((TextView) findViewById(R.id.youxiaoqi)).setText("(" + data.getCustomerCcExpmo() + "/" + data.getCustomerCcExpyr().substring(2) + ")");//分期付 展示有效期
                    ((TextView) findViewById(R.id.youxiaoqi2)).setText("(" + data.getCustomerCcExpmo() + "/" + data.getCustomerCcExpyr().substring(2) + ")");//卡支付有卡时
                } else {
                    ((TextView) findViewById(R.id.youxiaoqi)).setText("");//分期付 展示有效期
                    ((TextView) findViewById(R.id.youxiaoqi2)).setText("");//卡支付有卡时
                }
            }
            if (data.getCardCcType() != null) {

                if (data.getCardCcType().equals("10")) {
                    cardLogo_youka.setImageResource(R.mipmap.pay_visa_logo_new);
                    defaultCardLogo.setImageResource(R.mipmap.pay_visa_logo_new);
                    cardLogo_youka.setVisibility(View.VISIBLE);
                    defaultCardLogo.setVisibility(View.VISIBLE);
                } else if (data.getCardCcType().equals("20")) {
                    cardLogo_youka.setImageResource(R.mipmap.pay_master_logo_new);
                    defaultCardLogo.setImageResource(R.mipmap.pay_master_logo_new);
                    cardLogo_youka.setVisibility(View.VISIBLE);
                    defaultCardLogo.setVisibility(View.VISIBLE);
                } else if (data.getCardCcType().equals("60")) {
                    cardLogo_youka.setImageResource(R.mipmap.pay_onther_logo_new);
                    defaultCardLogo.setImageResource(R.mipmap.pay_onther_logo_new);
                    cardLogo_youka.setVisibility(View.VISIBLE);
                    defaultCardLogo.setVisibility(View.VISIBLE);
                } else {
                    cardLogo_youka.setVisibility(View.INVISIBLE);
                    defaultCardLogo.setVisibility(View.INVISIBLE);
                }
            }
        }
        changeCardInfo = true;

        next.setText("Pay $" + PublicTools.twoend(data.getToShowFeeAllAmount()));
        totalAmount.setText("$" + PublicTools.twoend(data.getPayAmount()));
        orderAmount.setText("$" + PublicTools.twoend(data.getTransAmount()));


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
                discountLl.setVisibility(View.VISIBLE);
                if ((TextUtils.isEmpty(data.getNormalSaleUserDiscountAmount()) || Float.parseFloat(data.getNormalSaleUserDiscountAmount()) == 0)) {

                } else {
                    int defaultDis = (int) Float.parseFloat(data.getNormalSalesUserDiscount());
                    discount.setText(/*(int) CalcTool.mm(wholeSaleDiscount, "100")*/String.format("Discount (%s)", defaultDis + "%"));
                    discountAmount.setText("-$" + PublicTools.twoend(data.getNormalSaleUserDiscountAmount()));
                }
                if ((TextUtils.isEmpty(data.getWholeSaleUserDiscountAmount()) || Float.parseFloat(data.getWholeSaleUserDiscountAmount()) == 0)) {

                } else {
                    int wholeDis = (int) Float.parseFloat(data.getWholeSalesUserDiscount());
                    discount.setText(/*(int) CalcTool.mm(wholeSaleDiscount, "100")*/String.format("Discount (%s)", wholeDis + "%"));
                    discountAmount.setText("-$" + PublicTools.twoend(data.getWholeSaleUserDiscountAmount()));
                }
            }
        } else {
            discountLl.setVisibility(View.VISIBLE);
            discount.setText("Discount");
            discountAmount.setText("-$" + PublicTools.twoend(CalcTool.add(data.getWholeSaleUserDiscountAmount(), data.getNormalSaleUserDiscountAmount())));
        }
        if (TextUtils.isEmpty(data.getTotalRedEnvelopeAmount()) || Float.parseFloat(data.getTotalRedEnvelopeAmount()) == 0) {//目前不用了（红包）xzc
            pocketLl.setVisibility(View.GONE);
        } else {
            pocketLl.setVisibility(View.VISIBLE);
            availableAmount.setText(String.format("Available: $%s", PublicTools.twoend(data.getTotalRedEnvelopeAmount())));
            inputPocketAmount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString();
                    if (TextUtils.isEmpty(text)) {
                        usePocketAmount.setText("");
                        redEnvelopeAmount = "0";
                        return;
                    }
                    Float MinAmount = Float.parseFloat(data.getTotalRedEnvelopeAmount()) < Float.parseFloat(firstPayAmount) ? Float.parseFloat(data.getTotalRedEnvelopeAmount()) : Float.parseFloat(firstPayAmount);
                    if (Float.parseFloat(text) > MinAmount) {
                        String mins = CalcTool.rounded(String.valueOf(MinAmount), 2);
                        inputPocketAmount.setText(mins);
                        inputPocketAmount.setSelection(String.valueOf(mins).length());
                        return;
                    }
                    if (text.equals(".")) {
                        inputPocketAmount.setText("");
                        return;
                    }
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {
//                        text = (String) s.toString().subSequence(0,
//                                s.toString().indexOf(".") + 3);
                            String[] split = s.toString().split(".");
                            text = s.toString().substring(0, s.toString().indexOf(".")) + s.toString().substring(s.toString().indexOf("."), s.toString().indexOf(".") + 3);
                            //text = String.valueOf(PublicTools.twoend(s.toString()));
                            inputPocketAmount.setText(CalcTool.rounded(text, 2));
                            inputPocketAmount.setSelection(String.valueOf(CalcTool.rounded(text, 2)).length());
                            return;
                        }
                    }
                    usePocketAmount.setText("-$" + CalcTool.rounded(text, 2));
                    redEnvelopeAmount = CalcTool.rounded(text, 2);
                    if (text.equals(".") || text.equals("0..")/* || text.equals("0")*/) {
                        inputPocketAmount.setText("0.");
                        return;
                    }
                    inputPocketAmount.setSelection(String.valueOf(text).length());
                }
            });
        }
        if (findViewById(R.id.promotion_ll).getVisibility() == View.VISIBLE || discountLl.getVisibility() == View.VISIBLE) {
            order_amount_ll.setVisibility(View.VISIBLE);
        } else {
            order_amount_ll.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        softKeyboardStateHelper.removeSoftKeyboardStateListener(listener);
    }

    private void selectTip(int tag) {
        if (type == tag && tag != 5) {
            return;
        }
        tipNo.setTag("0");
        tipNo.setBackgroundResource(R.drawable.view_yj_m4gray);
        tipNoText.setTextColor(ContextCompat.getColor(this, R.color.black));
        tip5.setTag("0");
        tip5.setBackgroundResource(R.drawable.view_yj_m4gray);
        tip5Text.setTextColor(ContextCompat.getColor(this, R.color.black));
        tip5Money.setTextColor(Color.parseColor("#717171"));
        tip10.setTag("0");
        tip10.setBackgroundResource(R.drawable.view_yj_m4gray);
        tip10Text.setTextColor(ContextCompat.getColor(this, R.color.black));
        tip10Money.setTextColor(Color.parseColor("#717171"));
        tip15.setTag("0");
        tip15.setBackgroundResource(R.drawable.view_yj_m4gray);
        tip15Text.setTextColor(ContextCompat.getColor(this, R.color.black));
        tip15Money.setTextColor(Color.parseColor("#717171"));
        tipEnter.setTag("0");
        tipEnter.setBackgroundResource(R.drawable.view_yj_m4gray);
        tipEnterMoney.setTextColor(ContextCompat.getColor(this, R.color.black));
        if (tag == 1) {
            tipNo.setTag("1");
            tipNo.setBackgroundResource(R.drawable.view_btn_4orange);
            tipNoText.setTextColor(ContextCompat.getColor(this, R.color.white));
            getData();
        } else if (tag == 2) {
            tip5.setTag("1");
            tip5.setBackgroundResource(R.drawable.view_btn_4orange);
            tip5Text.setTextColor(ContextCompat.getColor(this, R.color.white));
            tip5Money.setTextColor(ContextCompat.getColor(this, R.color.white));
            getData();
        } else if (tag == 3) {
            tip10.setTag("1");
            tip10.setBackgroundResource(R.drawable.view_btn_4orange);
            tip10Text.setTextColor(ContextCompat.getColor(this, R.color.white));
            tip10Money.setTextColor(ContextCompat.getColor(this, R.color.white));
            getData();
        } else if (tag == 4) {
            tip15.setTag("1");
            tip15.setBackgroundResource(R.drawable.view_btn_4orange);
            tip15Text.setTextColor(ContextCompat.getColor(this, R.color.white));
            tip15Money.setTextColor(ContextCompat.getColor(this, R.color.white));
            getData();
        } else {
            tipEnter.setTag("1");
            tipEnter.setBackgroundResource(R.drawable.view_btn_4orange);
            tipEnterMoney.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    private int type = 1;

    @Override
    public void onClick(View v) {
        if (!PublicTools.isFastClick()) {
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.add_pro:
            case R.id.tv_add_pro:
                Intent intentPro = new Intent(this, Zhifu_PromotionListActivity.class);
                intentPro.putExtra("transAmount", data.getTransAmount());
                if (findViewById(R.id.promotion_ll).getVisibility() == View.VISIBLE) {
                    intentPro.putExtra("SelectedPromotionJinE", promotion.getText().toString().replace("-$", ""));
                }
                intentPro.putExtra("couponId", couponId);
                intentPro.putExtra("merchantId", merchantId);
                startActivityForResult(intentPro, 111);
                break;
            case R.id.donate_quest:
                DonateDialog donateDialog = new DonateDialog(this);
                donateDialog.show();
                break;
            case R.id.back:
                TiShiDialog dialog = new TiShiDialog(ConfirmPayActivity.this);
                dialog.setOnGuanBiLeft(new TiShiDialog.GuanBiLeft() {//左边按钮
                    @Override
                    public void GuanBiLeft() {

                    }
                });
                dialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                    @Override
                    public void GuanBi() {
                        finish();
                    }
                });
                dialog.ShowDialog("Cancel payment", "Are you sure you want to cancel\nthis order ?", "Yes");
                break;
            case R.id.next:
                //银联支付调用
               /* new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String serverMode = "01";
                        Looper.prepare();
                        UPPayAssistEx.startPay (ConfirmPayActivity.this, null, null, data.getTransNo(), serverMode);
                        Looper.loop();
                    }
                }).start();*/

                if (payType == 4 && !TextUtils.isEmpty(availableCredit) && Float.parseFloat(availableCredit) < Float.parseFloat(data.getCreditRealPayAmount())) {
                    TiShiDialog tiShiDialog = new TiShiDialog(ConfirmPayActivity.this);
                    tiShiDialog.ShowDialog("Insufficient instalment balance available to pay the current order amount.");
                    return;
                }
                final HashMap<String, Object> qrPayRequestMap = new HashMap<>();
                if (!TextUtils.isEmpty(apiToken)) {
                    qrPayRequestMap.put("apiToken", apiToken);
                }
                qrPayRequestMap.put("payUserId", requestMap.get("payUserId"));
                qrPayRequestMap.put("recUserId", requestMap.get("recUserId"));
                qrPayRequestMap.put("merchantId", merchantId);
                qrPayRequestMap.put("transAmount", data.getTransAmount());
                qrPayRequestMap.put("trulyPayAmount", data.getAllPayAmount());
                qrPayRequestMap.put("payType", requestMap.get("payType"));
                if (requestMap.containsKey("cardId")) {
                    qrPayRequestMap.put("cardId", requestMap.get("cardId"));
                }
                if (couponId != null && !TextUtils.isEmpty(couponId)) {
                    qrPayRequestMap.put("marketingId", couponId);
                }

                qrPayRequestMap.put("redEnvelopeAmount", data.getRedEnvelopeAmount());
                qrPayRequestMap.put("normalSalesAmount", data.getNormalSaleAmount());
                qrPayRequestMap.put("wholeSalesAmount", data.getWholeSalesAmount());
                qrPayRequestMap.put("userId", (String) new SharePreferenceUtils(this, StaticParament.USER).get(StaticParament.USER_ID, ""));
                if (choiceDonate.isChecked()) {
                    qrPayRequestMap.put("donationInstiuteId", data.getDonationData().getId());
                }
                if (!TextUtils.isEmpty(productId)) {
                    qrPayRequestMap.put("productId", productId);
                }
                if (tip5.getTag().equals("1")) {
                    qrPayRequestMap.put("tipAmount", data.getTipList().get(0).getAmount());
                }
                if (tip10.getTag().equals("1")) {
                    qrPayRequestMap.put("tipAmount", data.getTipList().get(1).getAmount());
                }
                if (tip15.getTag().equals("1")) {
                    qrPayRequestMap.put("tipAmount", data.getTipList().get(2).getAmount());
                }
                if (tipEnter.getTag().equals("1") && tipEnterMoney.getText().toString().contains("$")) {
                    qrPayRequestMap.put("tipAmount", tipEnterMoney.getText().toString().substring(1));
                }
                qrPayRequestMap.put("transNo", data.getTransNo());

//                Intent intentNext = new Intent(ConfirmPayActivity.this, ConfirmPayNextActivity.class);
//                intentNext.putExtra("payType", String.valueOf(payType));
//                intentNext.putExtra("data", (Serializable) data);
//                intentNext.putExtra("merchantName", merchantName.getText().toString());
//                if (payType == 4) {
//                    intentNext.putExtra("amount", data.getCreditFirstCardPayAmount());
//                } else if (payType == 0) {
//                    intentNext.putExtra("amount", data.getPayAmount());
//                }
//                intentNext.putExtra("donationAmount", data.getDonationAmount());
//                intentNext.putExtra("tipAmount", data.getTipAmount());
//                intentNext.putExtra("feeRate", data.getTransChannelFeeRate());
//                intentNext.putExtra("feeAmount", data.getChannelFeeAmount());
//                intentNext.putExtra("allPayAmount", data.getAllPayAmount());
//                intentNext.putExtra("qrPayRequestMap", qrPayRequestMap);
//                intentNext.putExtra("requestMap", requestMap);
//                startActivity(intentNext);
                if (!haveCard) {
                    if (zhifuType == 0) {//0-latpay , 8-stripe
                        Map<String, Object> maps = new HashMap<>();
//        1、绑卡 0、绑账户
                        maps.put("type", "1");
                        maps.put("country", "1");
//                    if (getIntent().getIntExtra("isCreditCard", 0) == 1) {
//                        maps.put("isCreditCard", 1);
//                    }
//                    if (registerFrom.equals("1") && cardState.equals("0") && installmentState.equals("2") && creditCardState.equals("0")) {
//                        maps.put("isCreditCard", 1);
//                    }
                        //银行卡号
                        maps.put("cardNo", cardNo.getText().toString().trim().replaceAll(" ", ""));
                        maps.put("customerCcExpmo", et_date_left.getText().toString());//卡过期月份
                        Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int qianTwoWeiOfYear = Integer.valueOf(String.valueOf(year).substring(0, 2));
                        maps.put("customerCcExpyr", qianTwoWeiOfYear + et_date_right.getText().toString());//卡过期年份
                        //卡类型 10、VISA, 20、MAST, 30、 SWITCH, 40、SOLO, 50、DELTA, 60、 AMEX
                        //maps.put("customerCcType", choiceBankTypeCode);
                        maps.put("customerCcCvc", et_id_cvv.getText().toString());//安全码

                        addBankAccount(maps, qrPayRequestMap);
                    } else if (zhifuType == 8) {//stripe

                        findViewById(R.id.next).setEnabled(false);
                        String number = cardNo.getText().toString().trim().replaceAll(" ", "");//银行卡号
                        Map<String, Object> maps1 = new HashMap<>();
                        maps1.put("last4", number.substring(number.length() - 4));
                        judgmentRepetition(maps1, qrPayRequestMap);//判断重复卡号
                    }
//
//                    Map<String, Object> maps = new HashMap<>();
////        1、绑卡 0、绑账户
//                    maps.put("type", "1");
//                    maps.put("country", "1");
////                    if (getIntent().getIntExtra("isCreditCard", 0) == 1) {
////                        maps.put("isCreditCard", 1);
////                    }
////                    if (registerFrom.equals("1") && cardState.equals("0") && installmentState.equals("2") && creditCardState.equals("0")) {
////                        maps.put("isCreditCard", 1);
////                    }
//                    //银行卡号
//                    maps.put("cardNo", cardNo.getText().toString().trim().replaceAll(" ", ""));
//                    maps.put("customerCcExpmo", et_date_left.getText().toString());//卡过期月份
//                    Calendar c = Calendar.getInstance();
//                    int year = c.get(Calendar.YEAR);
//                    int qianTwoWeiOfYear = Integer.valueOf(String.valueOf(year).substring(0, 2));
//                    maps.put("customerCcExpyr", qianTwoWeiOfYear + et_date_right.getText().toString());//卡过期年份
//                    //卡类型 10、VISA, 20、MAST, 30、 SWITCH, 40、SOLO, 50、DELTA, 60、 AMEX
//                    //maps.put("customerCcType", choiceBankTypeCode);
//                    maps.put("customerCcCvc", et_id_cvv.getText().toString());//安全码
//
//                    addBankAccount(maps, qrPayRequestMap);
                } else {
                    savePayInfo(qrPayRequestMap);
                }
                break;
            case R.id.choose_card:
            case R.id.choose_card_fenqifu:
                Intent intent = new Intent(ConfirmPayActivity.this, Zhifu_SelectBankCardActivity.class);
                if (!TextUtils.isEmpty(cardId)) {
                    intent.putExtra("cardId", cardId);
                }
                intent.putExtra("fromPay", 1);
                if (payType == 4) {
                    intent.putExtra("fromFenqi", 1);
                }
                startActivityForResult(intent, 1006);
                break;
            case R.id.view_schedule_ll:
                open = !open;
/*                if (data.getPreviewRepayPlanList() != null && data.getPreviewRepayPlanList().size() > 0) {
                    HashMap<String, Object> qrPayRequestMaps = new HashMap<>();
                    if (!TextUtils.isEmpty(apiToken)) {
                        qrPayRequestMaps.put("apiToken", apiToken);
                    }
                    qrPayRequestMaps.put("merchantId", merchantId);
                    qrPayRequestMaps.put("payUserId", requestMap.get("payUserId"));
                    qrPayRequestMaps.put("recUserId", requestMap.get("recUserId"));
                    qrPayRequestMaps.put("transAmount", data.getTransAmount());
                    qrPayRequestMaps.put("trulyPayAmount", data.getAllPayAmount());
                    qrPayRequestMaps.put("payType", requestMap.get("payType"));
                    if (requestMap.containsKey("cardId")) {
                        qrPayRequestMaps.put("cardId", requestMap.get("cardId"));
                    }
                    if (choiceDonate.isChecked()) {
                        qrPayRequestMaps.put("donationInstiuteId", data.getDonationData().getId());
                    }
                    qrPayRequestMaps.put("redEnvelopeAmount", data.getRedEnvelopeAmount());
                    qrPayRequestMaps.put("normalSalesAmount", data.getNormalSaleAmount());
                    qrPayRequestMaps.put("wholeSalesAmount", data.getWholeSalesAmount());
                    if (!TextUtils.isEmpty(productId)) {
                        qrPayRequestMaps.put("productId", productId);
                    }
                    if (tip5.getTag().equals("1")) {
                        qrPayRequestMaps.put("tipAmount", data.getTipList().get(0).getAmount());
                    }
                    if (tip10.getTag().equals("1")) {
                        qrPayRequestMaps.put("tipAmount", data.getTipList().get(1).getAmount());
                    }
                    if (tip15.getTag().equals("1")) {
                        qrPayRequestMaps.put("tipAmount", data.getTipList().get(2).getAmount());
                    }
                    if (tipEnter.getTag().equals("1") && tipEnterMoney.getText().toString().contains("$")) {
                        qrPayRequestMaps.put("tipAmount", tipEnterMoney.getText().toString().substring(1));
                    }
                    qrPayRequestMaps.put("transNo", data.getTransNo());

                    Intent intent1 = new Intent(ConfirmPayActivity.this, PayInstalmentListActivity.class);
                    intent1.putExtra("data", (Serializable) data);
                    intent1.putExtra("creditRealPayAmount", data.getCreditRealPayAmount());
                    intent1.putExtra("availableCredit", availableCredit);
                    intent1.putExtra("payType", String.valueOf(payType));
                    intent1.putExtra("donationAmount", data.getDonationAmount());
                    intent1.putExtra("merchantName", merchantName.getText().toString());
                    intent1.putExtra("feeRate", data.getTransChannelFeeRate());
                    intent1.putExtra("feeAmount", data.getChannelFeeAmount());
                    intent1.putExtra("allPayAmount", data.getAllPayAmount());
                    intent1.putExtra("amount", data.getCreditFirstCardPayAmount());
                    intent1.putExtra("list", (Serializable) data.getPreviewRepayPlanList());
                    intent1.putExtra("qrPayRequestMap", qrPayRequestMaps);
                    intent1.putExtra("requestMap", requestMap);
                    startActivity(intent1);
                }*/
                if (open) {
                    findViewById(R.id.instalment_flag).setVisibility(View.VISIBLE);

                    ((ImageView) findViewById(R.id.arrow)).setImageResource(R.mipmap.choice_gray_up);
                    if (data.getPreviewRepayPlanList() != null && data.getPreviewRepayPlanList().size() > 0) {
                        fenjiqiInit(data.getPreviewRepayPlanList());
                    } else {
                        Toast.makeText(ConfirmPayActivity.this, "No repayment plan", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ((ImageView) findViewById(R.id.arrow)).setImageResource(R.mipmap.down_arrow);
                    findViewById(R.id.instalment_flag).setVisibility(View.GONE);
                }
                break;
        }
    }

    JSONArray cardTypeList = null;

    /**
     * 判断重复
     *
     * @param maps
     */
    public void judgmentRepetition(Map<String, Object> maps, HashMap<String, Object> qrPayRequestMap) {
        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(ConfirmPayActivity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.stripeCheckCardNoRedundancy(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(final String dataStr, String pc, String code) {
                int redundancyState = 0;//重复状态： 1 有重复 0 无重复
                try {
                    redundancyState = new JSONObject(dataStr).getInt("redundancyState");
                    cardTypeList = new JSONObject(dataStr).getJSONArray("supportedList");//可以绑定的卡类型
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (redundancyState == 0) {
                    //获取cardtoken
                    Stripe stripe = new Stripe(getApplication(), PaymentConfiguration.getInstance(getApplication()).getPublishableKey());
                    int expMonth = Integer.parseInt(et_date_left.getText().toString());//卡过期月份
                    int expYear = Integer.parseInt(et_date_right.getText().toString());//卡过期年份
                    String cvv = et_id_cvv.getText().toString();//安全码
                    String number = cardNo.getText().toString().trim().replaceAll(" ", "");//银行卡号
                    CardParams cardParams = new CardParams(number, expMonth, expYear, cvv);
                    stripe.createCardToken(cardParams, new ApiResultCallback<Token>() {
                        @Override
                        public void onSuccess(@NonNull Token token) {
                            boolean zhichi = false;
                            L.log("获取cardtoken成功 token=" + token.toString());
//                            token.getCard().getBrand();//绑的卡的类型
                            for (int i = 0; i < cardTypeList.length(); i++) {
                                try {
                                    if (cardTypeList.getString(i).replace(" ", "").equals(token.getCard().getBrand().name().replace(" ", ""))) {//绑定的卡是支持的类型
                                        cardLogo.setVisibility(View.VISIBLE);
                                        defaultCardLogo.setVisibility(View.VISIBLE);
                                        if (cardTypeList.getString(i).replace(" ", "").equals("Visa")) {//切图大小不一样 导致的
//                        cardLogo_3.setVisibility(View.GONE);
                                            cardLogo.setImageResource(R.mipmap.pay_visa_logo_new);
                                            defaultCardLogo.setImageResource(R.mipmap.pay_visa_logo_new);//分期付有卡的情况 imageview也复制 作用：保存完卡信息后 在输入Pin页面返回 卡支付没卡----》卡支付有卡页面的更新
                                        } else if (cardTypeList.getString(i).replace(" ", "").equals("MasterCard")) {
//                        cardLogo.setVisibility(View.VISIBLE);
//                        cardLogo_3.setVisibility(View.GONE);
                                            cardLogo.setImageResource(R.mipmap.pay_master_logo_new);
                                            defaultCardLogo.setImageResource(R.mipmap.pay_master_logo_new);
                                        } else if (cardTypeList.getString(i).replace(" ", "").equals("AmericanExpress")) {
                                            cardLogo.setImageResource(R.mipmap.pay_onther_logo_new);

//                        cardLogo.setVisibility(View.GONE);
//                        cardLogo_3.setVisibility(View.VISIBLE);
//                        cardLogo_3.setImageResource(R.mipmap.pay_onther_logo);
                                            defaultCardLogo.setImageResource(R.mipmap.pay_onther_logo_new);
                                        } else {
                                            cardLogo.setVisibility(View.INVISIBLE);
                                            defaultCardLogo.setVisibility(View.INVISIBLE);
                                        }

                                        zhichi = true;
                                        if (TextUtils.isEmpty(token.getId())) {
                                            //有待确认 如果tokenid为空怎么处理
                                            findViewById(R.id.next).setEnabled(true);

                                        } else {
                                            //上传tokenid到服务器绑卡
                                            Map<String, Object> maps = new HashMap<>();
                                            maps.put("cardToken", token.getId());

                                            addBankAccount(maps, qrPayRequestMap);
                                        }
                                        break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (!zhichi) {
                               /* mBtn.setEnabled(true);
                                if (kaitongfenqifuRoad) {
                                    mBtn.setText("Next");
                                } else {
                                    mBtn.setText("Add");
                                }
                                rl_icon.setVisibility(View.GONE);
                                check_up.setEnabled(true);*/
                                TiShiDialog tiShiDialog = new TiShiDialog(ConfirmPayActivity.this);
                                tiShiDialog.ShowDialog("Only visa, MasterCard and AMEX are supported. Please try another card");
                            }
                        }

                        @Override
                        public void onError(@NonNull Exception e) {
//                            mBtn.setEnabled(true);
//                            if (kaitongfenqifuRoad) {
//                                mBtn.setText("Next");
//                            } else {
//                                mBtn.setText("Add");
//                            }
//                            rl_icon.setVisibility(View.GONE);

                            TiShiDialog tiShiDialog = new TiShiDialog(ConfirmPayActivity.this);
                            tiShiDialog.ShowDialog(e.getMessage());
                        }
                    });

                } else {
                    findViewById(R.id.next).setEnabled(true);
                    TiShiDialog tiShiDialog = new TiShiDialog(ConfirmPayActivity.this);
                    tiShiDialog.ShowDialog("Bind Failed: The card has been bound.please change the card and try again", "Confirm");
                }
            }

            @Override
            public void resError(String error) {
                findViewById(R.id.next).setEnabled(true);
//                mBtn.setEnabled(true);
//                if (kaitongfenqifuRoad) {
//                    mBtn.setText("Next");
//                } else {
//                    mBtn.setText("Add");
//                }
//                rl_icon.setVisibility(View.GONE);
            }
        });
    }

    private void savePayInfo(final HashMap<String, Object> qrPayRequestMap) {
        EnterPayPinDialog enterPayPinDialog = new EnterPayPinDialog(ConfirmPayActivity.this, merchantName.getText().toString(), PublicTools.twoend(data.getAllPayAmount()));
        enterPayPinDialog.setOnRepayPwd(new EnterPayPinDialog.OnRepayPwd() {
            @Override
            public void back() {//点击但会按钮页面刷新（卡支付未绑卡----->卡支付已绑卡）
//                Toast.makeText(ConfirmPayActivity.this, "关闭", Toast.LENGTH_SHORT).show();
                if (!haveCard) {
                    findViewById(R.id.available_amount_ll).setVisibility(View.GONE);
                    useInsLl.setVisibility(View.GONE);
                    bankSlectCard.setVisibility(View.VISIBLE);
                    add_card.setVisibility(View.GONE);

                    findViewById(R.id.arrow1).setVisibility(View.GONE);//为了右边对其
                    findViewById(R.id.arrow2).setVisibility(View.GONE);
                    findViewById(R.id.arrow3).setVisibility(View.GONE);
                    findViewById(R.id.arrow4).setVisibility(View.GONE);
                    findViewById(R.id.arrow5).setVisibility(View.GONE);

                    String card = cardNo.getText().toString().replaceAll(" ", "");
                    card = "•••• " + card.substring(card.length() - 4);
                    haveCard = true;
                    defaultCardNo.setText(card);
                    ((TextView) findViewById(R.id.youxiaoqi2)).setText("(" + et_date_left.getText().toString() + "/" + et_date_right.getText().toString() + ")");//卡支付有卡时
                    defaultCardLogo.setVisibility(View.VISIBLE);
                    if (choiceBankTypeCode != null)
                        if (choiceBankTypeCode.equals("10")) {//切图大小不一样 导致的
//                        cardLogo_3.setVisibility(View.GONE);
                            cardLogo.setImageResource(R.mipmap.pay_visa_logo_new);
                            defaultCardLogo.setImageResource(R.mipmap.pay_visa_logo_new);//分期付有卡的情况 imageview也复制 作用：保存完卡信息后 在输入Pin页面返回 卡支付没卡----》卡支付有卡页面的更新
                        } else if (choiceBankTypeCode.equals("20")) {
//                        cardLogo.setVisibility(View.VISIBLE);
//                        cardLogo_3.setVisibility(View.GONE);
                            cardLogo.setImageResource(R.mipmap.pay_master_logo_new);
                            defaultCardLogo.setImageResource(R.mipmap.pay_master_logo_new);
                        } else if (choiceBankTypeCode.equals("60")) {
                            cardLogo.setImageResource(R.mipmap.pay_onther_logo_new);

//                        cardLogo.setVisibility(View.GONE);
//                        cardLogo_3.setVisibility(View.VISIBLE);
//                        cardLogo_3.setImageResource(R.mipmap.pay_onther_logo);
                            defaultCardLogo.setImageResource(R.mipmap.pay_onther_logo_new);
                        } else {
                            cardLogo.setVisibility(View.INVISIBLE);
                            defaultCardLogo.setVisibility(View.INVISIBLE);
                        }
                }
            }

            @Override
            public void inputPwd(String password) {

            }

            @Override
            public void inputFinish() {
                resAFEvent(qrPayRequestMap);
                if (paymentWaitingDialog == null) {
                    if (requestMap.containsKey("posTransNo")) {
                        paymentWaitingDialog = new PaymentWaitingDialog(ConfirmPayActivity.this, (String) requestMap.get("posTransNo"), "true");
                    } else {
                        paymentWaitingDialog = new PaymentWaitingDialog(ConfirmPayActivity.this, (String) qrPayRequestMap.get("transNo"), "false");
                    }
                }
                paymentWaitingDialog.show();
                payListener.qrPay(qrPayRequestMap, new ActionCallBack() {
                    @Override
                    public void onSuccess(Object... o) {
                        if (exePayResult) {
                            String dataStr = (String) o[0];
                            showNext(dataStr);
                            paymentWaitingDialog.setResult();
                        }
                        exePayResult = true;
                    }

                    @Override
                    public void onError(String e) {
                        paymentWaitingDialog.setResult();
                        exePayResult = true;
                    }
                });
            }
        });

        enterPayPinDialog.ShowDialog();
    }

    /**
     * 添加银行账户
     *
     * @param maps
     */
    public void addBankAccount(Map<String, Object> maps, final HashMap<String, Object> qrPayRequestMap) {
        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(ConfirmPayActivity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = null;
                if (zhifuType == 0) {
                    call = requestInterface.addBankAccount(headerMap, requestBody);
                } else {
                    call = requestInterface.stripeBindCardByToken(headerMap, requestBody);
                }
                requestUtils.Call(call);
            }

            @Override
            public void resData(final String dataStr, String pc, String code) {
                findViewById(R.id.next).setEnabled(true);

                cardId = dataStr;
                requestMap.put("cardId", cardId);
                L.log("添加银行账户:" + dataStr);//是cardId
                qrPayRequestMap.put("cardId", dataStr);
                savePayInfo(qrPayRequestMap);

            }

            @Override
            public void resError(String error) {
                findViewById(R.id.next).setEnabled(true);

            }
        });
    }


    private SharePreferenceUtils sharePreferenceUtils;

    String cardCount = "";
    String deleteCardPayState = "";
    private boolean changeCardInfo = true;//删卡后 刷新页面展示默认卡；不删卡 卡的信息则不用刷新

    public void showNext(String dataStr) {
        sharePreferenceUtils = new SharePreferenceUtils(ConfirmPayActivity.this, StaticParament.USER);
        sharePreferenceUtils.put(StaticParament.PAY_SUCCESS, "1");
        sharePreferenceUtils.save();

        //交易结果 0:处理中 1：成功 2：失败  9999:3ds验证
        int resultState = -1;
        String errorMessage = "";
        JSONObject successData;
        String id = "";
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(dataStr);
            if (jsonObject.has("resultState")) {
                resultState = jsonObject.getInt("resultState");
            }
            if (resultState == 2) {
                if (jsonObject.has("deleteCardPayState") && jsonObject.getString("deleteCardPayState") != null) {//支付失败不删卡 传递的为null
                    deleteCardPayState = jsonObject.getString("deleteCardPayState");//1 表示删卡了；0未删卡
                }
                if (jsonObject.has("cardCount") && jsonObject.getString("cardCount") != null) {//支付失败不删卡 传递的为null
                    cardCount = jsonObject.getString("cardCount");
                }
                if (jsonObject.has("errorMessage")) {
                    errorMessage = jsonObject.getString("errorMessage");
                }
                transNo = "";
                TiShiDialog dialog = new TiShiDialog(ConfirmPayActivity.this);
                dialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                    @Override
                    public void GuanBi() {
                        if (payType == 4) {//4：分期付
                            if (TextUtils.isEmpty(cardCount) || cardCount.equals("null") || cardCount == null) { //不删除卡也需要刷新否则再提交订单会出现same order的提示。
                                changeCardInfo = false;
                                cardId = "";
                                getData();
                            } else if (cardCount.equals("0")) {//分期付删除完卡后没卡了 跳转到绑卡页面
                                cardId = "";
                                Intent intent_kyc = new Intent(ConfirmPayActivity.this, NewAddBankCardActivity.class);
                                intent_kyc.putExtra("deleteCard", true);
                                startActivityForResult(intent_kyc, 123);
                            } else {//还有卡
                                if (deleteCardPayState.equals("1")) {//分期付删除完卡后还有卡  页面刷新
                                    changeCardInfo = true;
                                    cardId = "";
                                    getData();
                                }
                            }
                        } else {
                            if (TextUtils.isEmpty(cardCount) || cardCount.equals("null") || cardCount == null) { //不删除卡也需要刷新否则再提交订单会出现same order的提示。
                                changeCardInfo = false;
                            } else if (deleteCardPayState.equals("1")) {
                                changeCardInfo = true;
                            }
                            cardId = "";//那张卡删除了  所以cardId需要质控
                            if (cardCount.equals("0")) {//卡都删除完了 变成了卡支付没有卡了
                                haveCard = false;
                                initcardPayNoCard();
                            }
                            getData();
                        }
                    }
                });
                dialog.ShowDialog("Payment failed", errorMessage, "Close");
            }

            if (resultState == 0) {
                if (jsonObject.has("errorMessage")) {
                    errorMessage = jsonObject.getString("errorMessage");
                }

                Intent intent = new Intent(ConfirmPayActivity.this, PayFailedActivity.class);
                intent.putExtra("errorMessage", errorMessage);
                intent.putExtra("requestMap", requestMap);
                intent.putExtra("process", 1);
                startActivity(intent);
            }

            if (resultState == 1) {
                successData = jsonObject.getJSONObject("data");
                id = jsonObject.getString("id");
                Gson gson = new Gson();
                PaySuccessData paySuccessData = gson.fromJson(successData.toString(), PaySuccessData.class);
                if (paySuccessData != null) {
                    paySuccessData.setId(id);
                    Intent intent = new Intent(ConfirmPayActivity.this, PaySuccessActivity_Zhifu.class);
                    if (fenqifubiankazhifu) {
                        paySuccessData.setPayType("0");
                    }
                    intent.putExtra("firstName", jsonObject.getString("firstName"));
                    intent.putExtra("paySuccessData", paySuccessData);
                    startActivity(intent);
                }
            }

            if (resultState == 9999) {//stripe3DS返回跳转url
                String url = jsonObject.getJSONObject("data").getString("url");
                Intent intent = new Intent(ConfirmPayActivity.this, Web_Activity.class);
                intent.putExtra("url", url);
                intent.putExtra("zhijieback", true);
                startActivityForResult(intent, 55);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1006 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String cardNoSelect = bundle.getString("cardNo");
            String cardIdSelect = bundle.getString("cardId");
//            shijiShowCardNo = bundle.getString("shijicardNo");
            defaultCardNo.setText(cardNoSelect);
            if (cardNo != null) {
                cardNo.setText(cardNoSelect);
            }
            cardId = cardIdSelect;
            getData();
        }
        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            couponId = data.getStringExtra("promotionId");
            couponAmount = data.getStringExtra("promotionAmount");
            if (!TextUtils.isEmpty(couponAmount)) {//不选择时 传递的""
                findViewById(R.id.promotion_ll).setVisibility(View.VISIBLE);
                promotion.setText("-$" + couponAmount);
                tv_add_pro.setText("Change promotion");
//                ((ImageView) findViewById(R.id.add_pro)).setImageResource(R.mipmap.change_pro);
                redEnvelopeAmount = couponAmount;
            } else {
                findViewById(R.id.promotion_ll).setVisibility(View.GONE);
                tv_add_pro.setText("Add promotion");

//                ((ImageView) findViewById(R.id.add_pro)).setImageResource(R.mipmap.add_pro);
                redEnvelopeAmount = "0";
            }

            getData();
        }
        if (requestCode == 123 && resultCode == Activity.RESULT_CANCELED) {//绑卡页面返回至输入金额页面 输入金额页面刷新
            finish();
        }
        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {//绑卡成功后返回至支付页面 支付页面刷新 展示最新的银行卡
            getData();
        }
        if (requestCode == 55 && resultCode == Activity.RESULT_CANCELED) {//3ds页面返回
            Map<String, Object> map = new HashMap<>();
            merchantListener.getRecordDetailV2(map, transNo, new ActionCallBack() {
                @Override
                public void onSuccess(Object... o) {
                    String dataStr = (String) o[0];
                    showNext(dataStr);
                }

                @Override
                public void onError(String e) {
                }
            });
//            changeCardInfo = false;
//            cardId = "";
//            getData();
        }
      /*  if (data == null) {
            return;
        }
        String str = data.getExtras().getString("pay_result");
        if ("Success".equalsIgnoreCase(str)) {
            // 支付成功后，extra中如果存在result_data，取出校验
// result_data结构见c）result_data参数说明
            if (data.hasExtra("result_data")) {
                String sign = data.getExtras().getString("result_data");
// 验签证书同后台验签证书
// 此处的verify，商户需送去商户后台做验签
                if (!TextUtils.isEmpty(sign)) {
                    //验证通过后，显示支付结果
                } else {
// 验证不通过后的处理
// 建议通过商户后台查询支付结果
                }
            } else {
// 未收到签名信息
// 建议通过商户后台查询支付结果
            }
        } else if (str.equalsIgnoreCase("R_FAIL")) {
        } else if (str.equalsIgnoreCase("R_CANCEL")) {
        }*/
    }

    private CashlineView line1;
    private CashlineView line2;
    private CashlineView line3;
    private LinearLayout term1;
    private LinearLayout term2;
    private LinearLayout term3;
    private LinearLayout term4;
    private TextView term1Money;
    private TextView term2Money;
    private TextView term3Money;
    private TextView term4Money;
    private TextView term1Date;
    private TextView term2Date;
    private TextView term3Date;
    private TextView term4Date;
    private TextView totalMoney;

    public void fenjiqiInit(List<TransAmountData.PreviewRepayPlanListBean> list) {
        line1 = (CashlineView) findViewById(R.id.line1);
        line2 = (CashlineView) findViewById(R.id.line2);
        line3 = (CashlineView) findViewById(R.id.line3);
        term1 = (LinearLayout) findViewById(R.id.term1);
        term2 = (LinearLayout) findViewById(R.id.term2);
        term3 = (LinearLayout) findViewById(R.id.term3);
        term4 = (LinearLayout) findViewById(R.id.term4);
        term1Money = (TextView) findViewById(R.id.term1_money);
        term2Money = (TextView) findViewById(R.id.term2_money);
        term3Money = (TextView) findViewById(R.id.term3_money);
        term4Money = (TextView) findViewById(R.id.term4_money);
        term1Date = (TextView) findViewById(R.id.term1_date);
        term2Date = (TextView) findViewById(R.id.term2_date);
        term3Date = (TextView) findViewById(R.id.term3_date);
        term4Date = (TextView) findViewById(R.id.term4_date);
        TypefaceUtil.replaceFont(term1Date, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term2Date, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term3Date, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term4Date, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(term1Money, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(term2Money, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(term3Money, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(term4Money, "fonts/gilroy_semiBold.ttf");

        if (list.size() == 3) {
            line3.setVisibility(View.GONE);
            term4.setVisibility(View.GONE);
        }
        if (list.size() == 2) {
            line2.setVisibility(View.GONE);
            term3.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            term4.setVisibility(View.GONE);
        }
        if (list.size() == 1) {
            line1.setVisibility(View.GONE);
            term2.setVisibility(View.GONE);
            line2.setVisibility(View.GONE);
            term3.setVisibility(View.GONE);
            line3.setVisibility(View.GONE);
            term4.setVisibility(View.GONE);
        }
        for (int i = 0; i < list.size(); i++) {
            if (getTermMoney(i) != null) {
                getTermMoney(i).setText("$" + PublicTools.twoend(list.get(i).getShouldPayAmount()));
            }
            if (getTermDate(i) != null && i > 0) {
                getTermDate(i).setText("Wed • " + list.get(i).getExpectRepayTime());
            }
        }
    }

    private TextView getTermMoney(int i) {
        if (i == 0) {
            return term1Money;
        }
        if (i == 1) {
            return term2Money;
        }
        if (i == 2) {
            return term3Money;
        }
        if (i == 3) {
            return term4Money;
        }
        return null;
    }

    private TextView getTermDate(int i) {
        if (i == 0) {
            return term1Date;
        }
        if (i == 1) {
            return term2Date;
        }
        if (i == 2) {
            return term3Date;
        }
        if (i == 3) {
            return term4Date;
        }
        return null;
    }

    /**
     * 设置监听事件 每四位加个空格 增删逻辑
     */
    private void spaceDivide(final EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;

            int location = 0;// 记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    location = mEditText.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if ((index == 4 || index == 9 || index == 14 || index == 19)) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }


                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }


                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }

                    mEditText.setText(str);
                    Editable etable = mEditText.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
                check();
                cardLogo.setVisibility(View.GONE);
//                cardLogo_3.setVisibility(View.GONE);
            }
        });
    }

    private boolean check() {
        if (TextUtils.isEmpty(cardNo.getText().toString()) || cardNo.getText().toString().replaceAll(" ", "").length() < 1 || cardNo.getText().toString().replaceAll(" ", "").length() > 20) {
            next.setBackground(ContextCompat.getDrawable(ConfirmPayActivity.this, R.mipmap.btn_hui_jianbian));
            next.setEnabled(false);
            return false;
        }

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int lastTwoWeiOfYear = Integer.valueOf(String.valueOf(year).substring(2));

        if (TextUtils.isEmpty(et_date_left.getText().toString()) || et_date_left.getText().toString().equals(" ") || et_date_left.getText().toString().equals("0") || et_date_left.getText().toString().equals("00") || Integer.valueOf(et_date_left.getText().toString()) > 12
                || TextUtils.isEmpty(et_date_right.getText().toString()) || et_date_right.getText().toString().equals("0") || et_date_right.getText().toString().equals("00") || Integer.valueOf(et_date_right.getText().toString()) < lastTwoWeiOfYear) {
            next.setBackground(ContextCompat.getDrawable(ConfirmPayActivity.this, R.mipmap.btn_hui_jianbian));
            next.setEnabled(false);

            return false;
        }

        if (TextUtils.isEmpty(et_id_cvv.getText().toString())) {
            next.setBackground(ContextCompat.getDrawable(ConfirmPayActivity.this, R.mipmap.btn_hui_jianbian));
            next.setEnabled(false);

            return false;
        }

        if ((!et_date_left.hasFocus() && !et_date_right.hasFocus())) {
            exeFucusLeft = true;
        }

        next.setBackground(ContextCompat.getDrawable(ConfirmPayActivity.this, R.drawable.bg_btn_green));
        next.setEnabled(true);
        return true;
    }

    String choiceBankTypeCode;

    public void latpayGetCardType(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(this, maps, StaticParament.zhifu, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(ConfirmPayActivity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("绑卡获取卡类型接口参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.latpayGetCardType(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("绑卡获取卡类型接口:" + dataStr);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataStr);
                    choiceBankTypeCode = jsonObject.getString("cardType");
                    cardLogo.setVisibility(View.VISIBLE);
                    defaultCardLogo.setVisibility(View.VISIBLE);
                    if (choiceBankTypeCode.equals("10")) {//切图大小不一样 导致的
//                        cardLogo_3.setVisibility(View.GONE);
                        cardLogo.setImageResource(R.mipmap.pay_visa_logo_new);
                        defaultCardLogo.setImageResource(R.mipmap.pay_visa_logo_new);//分期付有卡的情况 imageview也复制 作用：保存完卡信息后 在输入Pin页面返回 卡支付没卡----》卡支付有卡页面的更新
                    } else if (choiceBankTypeCode.equals("20")) {
//                        cardLogo.setVisibility(View.VISIBLE);
//                        cardLogo_3.setVisibility(View.GONE);
                        cardLogo.setImageResource(R.mipmap.pay_master_logo_new);
                        defaultCardLogo.setImageResource(R.mipmap.pay_master_logo_new);
                    } else if (choiceBankTypeCode.equals("60")) {
                        cardLogo.setImageResource(R.mipmap.pay_onther_logo_new);

//                        cardLogo.setVisibility(View.GONE);
//                        cardLogo_3.setVisibility(View.VISIBLE);
//                        cardLogo_3.setImageResource(R.mipmap.pay_onther_logo);
                        defaultCardLogo.setImageResource(R.mipmap.pay_onther_logo_new);
                    } else {
                        cardLogo.setVisibility(View.INVISIBLE);
                        defaultCardLogo.setVisibility(View.INVISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void resError(String error) {
                cardLogo.setVisibility(View.GONE);
//                cardLogo_3.setVisibility(View.GONE);
            }
        });
    }

//    /**
//     * 提交AF事件
//     */
//    private void resAFEvent(Map<String, Object> maps) {
//        Map<String, Object> eventValue = new HashMap<String, Object>();
//        //购买总额
//        eventValue.put(AFInAppEventParameterName.PRICE, maps.get("trulyPayAmount"));
////        eventValue.put(AFInAppEventParameterName.CONTENT_ID, "221");
//        eventValue.put(AFInAppEventParameterName.CONTENT_TYPE, maps.get("payType"));
//        //货币
//        eventValue.put(AFInAppEventParameterName.CURRENCY, "AUD");
//        eventValue.put(AFInAppEventParameterName.QUANTITY, 1);
//        eventValue.put(AFInAppEventParameterName.RECEIPT_ID, maps.get("merchantId"));
//        //eventValue.put("merchantId", merchantId);
//        AppsFlyerLib.getInstance().logEvent(ConfirmPayActivity.this, AFInAppEventType.PURCHASE, eventValue);
//    }

    /**
     * 提交AF事件
     */
    private void resAFEvent(Map<String, Object> maps) {
        Map<String, Object> eventValue = new HashMap<String, Object>();
        //购买总额
        eventValue.put(BzEventParameterName.PRICE, maps.get("trulyPayAmount"));
//        eventValue.put(AFInAppEventParameterName.CONTENT_ID, "221");
        eventValue.put(BzEventParameterName.CONTENT_TYPE, maps.get("payType"));
        //货币
        eventValue.put(BzEventParameterName.CURRENCY, "AUD");
        eventValue.put(BzEventParameterName.QUANTITY, 1);
        eventValue.put(BzEventParameterName.RECEIPT_ID, maps.get("merchantId"));
        Braze.getInstance(ConfirmPayActivity.this).getCurrentUser()
                .setCustomUserAttribute(BzEventType.PURCHASE, eventValue.toString());
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {//点击手机自动的返回键
            TiShiDialog dialog = new TiShiDialog(ConfirmPayActivity.this);
            dialog.setOnGuanBiLeft(new TiShiDialog.GuanBiLeft() {//左边按钮
                @Override
                public void GuanBiLeft() {

                }
            });
            dialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                @Override
                public void GuanBi() {
                    finish();
                }
            });
            dialog.ShowDialog("Cancel payment", "Are you sure you want to cancel\nthis order ?", "Yes");
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
//            if (!PublicTools.isFastClick()) {
//                return true;
//            }
            switch (v.getId()) {
                case R.id.tip_no:
                    selectTip(1);
                    type = 1;
                    break;
                case R.id.tip_5:
                    selectTip(2);
                    type = 2;
                    break;
                case R.id.tip_10:
                    selectTip(3);
                    type = 3;
                    break;
                case R.id.tip_15:
                    selectTip(4);
                    type = 4;
                    break;
                case R.id.tip_enter:
                    selectTip(5);
                    String enterTip = "";
                    if (tipEnterMoney.getText().toString().contains("$")) {
                        enterTip = tipEnterMoney.getText().toString().substring(1);
                    }
                    FragmentManager manager = getSupportFragmentManager();//区分是v的Fragment还是app包里面的
                    EnterTipAmountDialog dialogFragment = new EnterTipAmountDialog(ConfirmPayActivity.this, enterTip);
                    dialogFragment.show(manager, "custom");

                    dialogFragment.setOnClose(new EnterTipAmountDialog.OnClose() {
                        @Override
                        public void onClose(String amount) {
                            if (!TextUtils.isEmpty(amount)) {
                                tipEnterMoney.setText("$" + amount);
                            } else {
                                tipEnterMoney.setText("Enter\nAmount");
                            }
                            getData();
                        }
                    });
                    type = 5;
                    break;
            }
        }
        return true;
    }

    private int zhifuType;//laypay stripe

    /**
     * 注册
     */
    public void getPayGateWay(final Context context, Map<String, Object> maps) { //页面标识 1：二选一 2：证件类型页 3:地址页 4:个人信息填写页 5:绑卡页 6:illion默认机构页 7:illion 第一次验证页，8:illion第二次验证页 9:分期付开通等待页
        RequestUtils requestUtils1 = new RequestUtils(context, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.getPayGateWay(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {//type	0-latepay , 8-stripe
                try {
                    zhifuType = new JSONObject(dataStr).getInt("type");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("xzc_dubug11", dataStr);
            }


            @Override
            public void resError(String error) {

            }
        });
    }
}