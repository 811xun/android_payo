package com.home.glx.uwallet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.xzc.ChooseCardActivity;
import com.home.glx.uwallet.datas.BannerDatas;
import com.home.glx.uwallet.datas.FenqifuStatue;
import com.home.glx.uwallet.datas.MerchantInfoData;
import com.home.glx.uwallet.datas.ScanImgDatas;
import com.home.glx.uwallet.datas.ScanUserImgDatas;
import com.home.glx.uwallet.datas.TransAmountData;
import com.home.glx.uwallet.fragment.FirstFragment;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.mvp.GetApiToken;
import com.home.glx.uwallet.mvp.OpenFenQiFu_in;
import com.home.glx.uwallet.mvp.OpenFenQiFu_p;
import com.home.glx.uwallet.mvp.WhetherOpenInvest_in;
import com.home.glx.uwallet.mvp.WhetherOpenInvest_p;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.PayListener;
import com.home.glx.uwallet.mvp.model.PayModel;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.AddBankCardTipDialog;
import com.home.glx.uwallet.selfview.ConfirmPayBottomDialog;
import com.home.glx.uwallet.selfview.DampingReboundNestedScrollView;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.selfview.TiShiDialogTwo;
import com.home.glx.uwallet.selfview.TipDialog;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PayMoneyActivity extends MainActivity implements WhetherOpenInvest_in.View,
        View.OnClickListener, OpenFenQiFu_in.View {

    private PayListener payListener;
    private ScanUserImgDatas scanUserImgDatas;
    private ScanImgDatas scanImgDatas;
    //收款用户ID
    private String merchantId;
    private String recUserId;
    private String posTransNo;
    //是否开通个业务
    private WhetherOpenInvest_p openInvestPresent;

    private DampingReboundNestedScrollView scrollView;
    private ConstraintLayout oldHaveBank;
    private ConstraintLayout oldNoBank;
    private ConstraintLayout haveTwo;
    private ConstraintLayout haveBank;
    private ConstraintLayout haveInstalment;
    private ConstraintLayout haveNo;
    private TextView whyAdd;
    private TextView whyAdd1;
    private TextView oldInstalmenttext;
    private TextView oldInstalmenttext1;
    private TextView oldNoBankText;
    private TextView oldhaveBankText;
    private TextView oldhaveBankText1;
    private TextView merchantName;
    private ImageView twoInstalmentLogo;
    private ImageView logo;
    private TextView instalmentDiscountTwo;
    private TextView discountInstal1;
    private TextView discountInstal;
    private TextView discountBank;
    private TextView discountBank1;
    private TextView bankDiscountTwo;
    private TextView oldBankDiscount;
    private TextView instalmentDiscountBank;
    private TextView bankDiscountBank;
    private TextView oldNoBankDiscount;
    private ImageView id_back;
    private TextView twoBankText;
    private TextView twoInstalmentText;
    private Button next;
    private Button noPay;
    private TextView addBank;
    private TextView addBank1;
    private TextView openInstal;
    private TextView openInstal1;
    private ImageView check1Two;
    private ImageView check2Two;
    private ImageView checkOldBank;
    private EditText orderAmount;
    private TextView twoInstal;
    private TextView twoBank;
    private TextView oldBank;
    private ImageView idClearInput;
    private TextView moneyMark;
    private TextView discountView;
    private TextView address;
    private String discount;
    private String wholeSaleDiscount;
    private String logoUrl;
    private String merchantNameStr;
    private int payType = -1;
    //可用额度

    public static String availableCredit;
    public static String creditProductId;
    private String apiToken;
    private int haveFailed = 0;
    private String errorMsg = "";

    private OpenFenQiFu_p openFenQiFu_p;

    private ConfirmPayBottomDialog confirmPayBottomDialog;

    private SharePreferenceUtils user_sharePreferenceUtils;
    private String posTransAmount;
    private TextView posNumber;
    private TextView title;
    private TextView method;
    private RelativeLayout enterBackground;
    private TextView oldBankText;
    private boolean haveCard;

    private TransAmountData data = null;//xzc
    private HashMap<String, Object> requestMap = null;
    private boolean mFirst = true;
    private String creditCardState;


    private String cardState;
    private String installmentState;
    private boolean showPop;//输入金额页，点击按钮时显示距离小于500的弹窗提示

    @Override
    public int getLayout() {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return R.layout.activity_pay_moneys;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//必须要写
        String result = getIntent().getStringExtra("result");
        //是否是失败的老用户
        String oldPeople = getIntent().getStringExtra("oldPeople");
        //是否是失败的没分期付用户
        String noInstal = getIntent().getStringExtra("noInstal");
        data = (TransAmountData) getIntent().getSerializableExtra("data");
        requestMap = (HashMap<String, Object>) getIntent().getSerializableExtra("requestMap");
        if (result != null) {
            if (result.equals("error") && (posNumber.getVisibility() == View.VISIBLE)) {
                Intent intent1 = new Intent(this, NewMerchantInfoActivity.class);
                intent1.putExtra("id", merchantId);
                startActivity(intent1);
                finish();
            }
        }
        if (oldPeople != null) {
            if (haveCard) {
                Intent intentAdd = new Intent(PayMoneyActivity.this, SelectBankCardActivity.class);
                startActivity(intentAdd);
                return;
            } else {
                Intent intentAdd = new Intent(PayMoneyActivity.this, NewAddBankCardActivity.class);
                intentAdd.putExtra("fromAddCard", 1);
                startActivity(intentAdd);
                return;
            }
        }
        if (noInstal != null) {
//            user_sharePreferenceUtils.put(StaticParament.USER_RETURN_PAY, "1");
//            user_sharePreferenceUtils.put(StaticParament.FROM_PAY_MONEY, "1");
//            user_sharePreferenceUtils.save();
//            Intent intent1 = new Intent(this, FenQiFuOpen_Activity.class);
//            startActivity(intent1);
            return;
        }
        if (data != null && requestMap != null) {
            //支付失败，重新弹窗
            payType = (int) requestMap.get("payType");
            showPayNext(data, requestMap);
        }
    }

    @Override
    public void initView() {
        super.initView();
        showPop = getIntent().getBooleanExtra("showPop", false);

        payListener = new PayModel(this);
        enterBackground = findViewById(R.id.enter_background);
        oldNoBankDiscount = findViewById(R.id.old_discount_bank_flag);
        twoInstalmentLogo = (ImageView) findViewById(R.id.two_instalment_logo);
        address = (TextView) findViewById(R.id.merchant_location);
        twoBankText = (TextView) findViewById(R.id.two_bank_text);
        oldBankText = (TextView) findViewById(R.id.old_bank_text);
        twoInstalmentText = (TextView) findViewById(R.id.two_instalment_text);
        discountView = (TextView) findViewById(R.id.limit);
        posNumber = (TextView) findViewById(R.id.id_pos_amount);
        method = findViewById(R.id.method);
        user_sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        openFenQiFu_p = new OpenFenQiFu_p(this, this);
        openInvestPresent = new WhetherOpenInvest_p(this, this);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        scrollView = (DampingReboundNestedScrollView) findViewById(R.id.scroll_view);
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        // 屏幕宽度（像素）
        int width = metric.widthPixels;
        //CardView cardView = (CardView) findViewById(R.id.item_cardview);
        logo = (ImageView) findViewById(R.id.logo);
/*        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) logo.getLayoutParams();
        //LinearLayout.LayoutParams lf = (LinearLayout.LayoutParams) cardView.getLayoutParams();
        lp.height = (int) (width / 0.43);
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;

//        lf.height = (int) (((width/ 2 - 120) / 1.875f) * 2 );
//        lf.width = ViewGroup.LayoutParams.MATCH_PARENT;
        logo.setLayoutParams(lp);
        //cardView.setLayoutParams(lf);*/
        oldhaveBankText = findViewById(R.id.old_instalment);
        oldhaveBankText1 = findViewById(R.id.old_instalment1);
        oldNoBankText = findViewById(R.id.old_instalment_bank);
        noPay = findViewById(R.id.no_pay);
        moneyMark = (TextView) findViewById(R.id.money_mark);
        idClearInput = (ImageView) findViewById(R.id.id_clear_input);
        id_back = (ImageView) findViewById(R.id.id_back);
        whyAdd = findViewById(R.id.why_add);
        whyAdd1 = findViewById(R.id.why_add1);
        oldInstalmenttext = findViewById(R.id.old_instalment_text);
        oldInstalmenttext1 = findViewById(R.id.old_instalment_text1);
        next = findViewById(R.id.next);
        addBank = (TextView) findViewById(R.id.instalment_bank);
        addBank1 = (TextView) findViewById(R.id.no_bank);
        openInstal = (TextView) findViewById(R.id.bank_instalment);
        openInstal1 = (TextView) findViewById(R.id.no_instalment);
        check1Two = (ImageView) findViewById(R.id.choice_icon);
        check2Two = (ImageView) findViewById(R.id.choice_icon1);
        checkOldBank = (ImageView) findViewById(R.id.old_choice_icon1);
        whyAdd.setOnClickListener(this);
        whyAdd1.setOnClickListener(this);
        oldNoBankText.setOnClickListener(this);
        oldhaveBankText.setOnClickListener(this);
        oldhaveBankText1.setOnClickListener(this);
//        noPay.setOnClickListener(this);
        next.setOnClickListener(this);
        id_back.setOnClickListener(this);
        addBank.setOnClickListener(this);
        addBank1.setOnClickListener(this);
        openInstal.setOnClickListener(this);
        openInstal1.setOnClickListener(this);
        oldHaveBank = findViewById(R.id.instal_old);
        oldNoBank = findViewById(R.id.instal_old_no_bank);
        haveTwo = (ConstraintLayout) findViewById(R.id.have_two);
        haveBank = (ConstraintLayout) findViewById(R.id.have_bank);
        haveInstalment = (ConstraintLayout) findViewById(R.id.have_instalment);
        haveNo = (ConstraintLayout) findViewById(R.id.have_no);
        merchantName = (TextView) findViewById(R.id.merchant_name);
        instalmentDiscountTwo = (TextView) findViewById(R.id.discount_flag);
        discountInstal1 = (TextView) findViewById(R.id.discount_instal_flag1);
        discountInstal = (TextView) findViewById(R.id.discount_instal_flag);
        discountBank = (TextView) findViewById(R.id.discount_bank_flag);
        discountBank1 = (TextView) findViewById(R.id.discount_bank_flag1);
        bankDiscountTwo = (TextView) findViewById(R.id.discount_flag1);
        oldBankDiscount = (TextView) findViewById(R.id.old_discount_flag1);
        instalmentDiscountBank = (TextView) findViewById(R.id.discount1);
        bankDiscountBank = (TextView) findViewById(R.id.discount2);

        orderAmount = (EditText) findViewById(R.id.id_order_amount);
        //解决点击EditText弹出收起键盘时出现的黑屏闪现现象
        orderAmount.getRootView().setBackgroundColor(Color.WHITE);
        twoBank = (TextView) findViewById(R.id.two_bank);
        oldBank = (TextView) findViewById(R.id.old_bank);
        twoInstal = (TextView) findViewById(R.id.two_instalment);
        title = findViewById(R.id.title);
        TextView orderTextAmount = findViewById(R.id.order_text_amount);
        TextView addCardText = findViewById(R.id.add_card_text);
        TextView oldAddCardText = findViewById(R.id.old_add_card_text);
        TextView payFull = findViewById(R.id.pay_full);
        TextView oldPayFull = findViewById(R.id.old_pay_full);
        TextView payInUp = findViewById(R.id.pay_in_up);
        TextView apllyText = findViewById(R.id.aplly_text);
        TextView eatNowText = findViewById(R.id.eat_now_text);
        TextView payNowText = findViewById(R.id.pay_now_with_text);
        TextView applyText = findViewById(R.id.apply_text);
        TextView noAddCardTv = findViewById(R.id.no_add_card_text);
        TextView noInsRatNow = findViewById(R.id.no_ins_eat_now);
        TextView noAddCardFull = findViewById(R.id.no_add_card_full);
        //切换字体
        TypefaceUtil.replaceFont(noPay, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(noAddCardFull, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(noInsRatNow, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(noAddCardTv, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(applyText, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(oldInstalmenttext, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(oldInstalmenttext1, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(whyAdd, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(whyAdd1, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(moneyMark, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(payNowText, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(eatNowText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(apllyText, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(payInUp, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(payFull, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(oldPayFull, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(oldAddCardText, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(addCardText, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(twoInstalmentText, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(twoBankText, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(oldBankText, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(discountView, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(merchantName, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(address, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(orderTextAmount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(orderAmount, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(posNumber, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(method, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(next, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(instalmentDiscountTwo, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(discountInstal1, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(discountInstal, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(oldNoBankDiscount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(discountBank, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(bankDiscountTwo, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(oldBankDiscount, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(discountBank1, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(instalmentDiscountBank, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(bankDiscountBank, "fonts/gilroy_semiBold.ttf");

        idClearInput.setOnClickListener(this);
//        twoBank.setOnClickListener(this);
        twoBank.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    twoBank.setBackgroundResource(R.drawable.view_btn_4orange);
                    twoBankText.setTextColor(getResources().getColor(R.color.white));
                    bankDiscountTwo.setTextColor(getResources().getColor(R.color.white));

                    twoInstalmentLogo.setImageResource(R.mipmap.payo_logo1);
                    twoInstal.setBackgroundResource(R.drawable.view_yj_m4hui);
                    twoInstalmentText.setTextColor(getResources().getColor(R.color.pay_text_gray));
                    instalmentDiscountTwo.setTextColor(getResources().getColor(R.color.pay_text_gray));

                    check2Two.setImageDrawable(getResources().getDrawable(R.mipmap.pay_choice_on));
                    check2Two.setTag("true");
                    check1Two.setImageDrawable(getResources().getDrawable(R.mipmap.pay_unchoice_icon));
                    check1Two.setTag("false");
                }
                return true;
            }
        });
        oldBank.setOnClickListener(this);
//        twoInstal.setOnClickListener(this);
        twoInstal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    twoBank.setBackgroundResource(R.drawable.view_yj_m4hui);
                    twoBankText.setTextColor(getResources().getColor(R.color.pay_text_gray));
                    bankDiscountTwo.setTextColor(getResources().getColor(R.color.pay_text_gray));

                    twoInstalmentLogo.setImageResource(R.mipmap.payo_logo);
                    twoInstal.setBackgroundResource(R.drawable.view_btn_4orange);
                    twoInstalmentText.setTextColor(getResources().getColor(R.color.white));
                    instalmentDiscountTwo.setTextColor(getResources().getColor(R.color.white));

                    check1Two.setImageDrawable(getResources().getDrawable(R.mipmap.pay_choice_on));
                    check1Two.setTag("true");
                    check2Two.setImageDrawable(getResources().getDrawable(R.mipmap.pay_unchoice_icon));
                    check2Two.setTag("false");
                }
                return true;
            }
        });
        orderAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    moneyMark.setTextColor(getResources().getColor(R.color.colorBackGround));
                    enterBackground.setBackgroundResource(R.drawable.view_yj_m8cheng);
                } else {
                    if (TextUtils.isEmpty(orderAmount.getText().toString().trim())) {
                        moneyMark.setTextColor(Color.parseColor("#A0A0A0"));
                        enterBackground.setBackgroundResource(R.drawable.view_yj_m8gray);
                    } else {
                        moneyMark.setTextColor(getResources().getColor(R.color.colorBackGround));
                        enterBackground.setBackgroundResource(R.drawable.view_yj_m8cheng);

                    }
                }
            }
        });
        orderAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() >= 1) {

                    idClearInput.setVisibility(View.VISIBLE);
                } else {
                    idClearInput.setVisibility(View.INVISIBLE);
                }
                if (text.equals(".")) {
                    orderAmount.setText("");
                    return;
                }
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
//                        text = (String) s.toString().subSequence(0,
//                                s.toString().indexOf(".") + 3);
                        String[] split = s.toString().split(".");
                        text = s.toString().substring(0, s.toString().indexOf(".")) + s.toString().substring(s.toString().indexOf("."), s.toString().indexOf(".") + 3);
                        //text = String.valueOf(PublicTools.twoend(s.toString()));
                        orderAmount.setText(text);
                        orderAmount.setSelection(text.length());
                    }
                }
                if (text.equals(".") || text.equals("0..")/* || text.equals("0")*/) {
                    orderAmount.setText("0.");
                    orderAmount.setSelection(orderAmount.getText().length());
                    return;
                }
                if (text.equals("0.") || text.equals("0.0")) {
                    text = "0";
                }
            }
        });

        String type = getIntent().getStringExtra("type");
        if (type.equals("10")) {
            String scanData = getIntent().getStringExtra("data");
            //支付给用户
            scanUserImgDatas = new Gson().fromJson(scanData, ScanUserImgDatas.class);
            merchantId = scanUserImgDatas.getUserId();
        } else if (type.equals("20")) {
            String scanData = getIntent().getStringExtra("data");
            //支付给商户
            scanImgDatas = new Gson().fromJson(scanData, ScanImgDatas.class);
            merchantId = String.valueOf(scanImgDatas.getMerchantId());
        } else if (type.equals("30")) {
            //支付给商户
            merchantId = getIntent().getStringExtra("merchantId");
        } else if (type.equals("00")) {
            //Pos扫码订单
            merchantId = getIntent().getStringExtra("merchantId");
            posTransAmount = getIntent().getStringExtra("transAmount");
            posTransNo = getIntent().getStringExtra("posTransNo");
            posNumber.setVisibility(View.VISIBLE);
            posNumber.setText(posTransAmount);
            orderAmount.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(PayMoneyActivity.this, StaticParament.USER);
        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
        Map<String, Object> maps = new HashMap<>();
        maps.put("userId", userId);
        maps.put("stripeState", 0);//用户区分接入stripe前后的新老用户

        jiaoyanFenqifu(maps);
//        openInvestPresent.loadOpenInvestStatus("sm");
        user_sharePreferenceUtils.put(StaticParament.USER_RETURN_PAY, "0");
        user_sharePreferenceUtils.put(StaticParament.FROM_PAY_MONEY, "0");
        user_sharePreferenceUtils.save();
    }

    @Override
    public void openInvestStatus(String code, String fenqiCard, String zhifu, String fenqi) {
        haveTwo.setVisibility(View.GONE);
        haveBank.setVisibility(View.GONE);
        haveInstalment.setVisibility(View.GONE);
        oldHaveBank.setVisibility(View.GONE);
        oldNoBank.setVisibility(View.GONE);
        haveNo.setVisibility(View.GONE);
        next.setVisibility(View.VISIBLE);
        noPay.setVisibility(View.GONE);
//        enterBackground.setBackgroundResource(R.drawable.view_yj_m8cheng);
//        moneyMark.setTextColor(getResources().getColor(R.color.colorBackGround));
        if (zhifu != null && fenqi != null && fenqiCard != null) {
            if (zhifu.equals("1")) {
                haveCard = true;
            } else {
                haveCard = false;
            }
            if (zhifu.equals("1") && fenqi.equals("1") && fenqiCard.equals("1")) {
                haveTwo.setVisibility(View.VISIBLE);
            }
            if (zhifu.equals("1") && fenqi.equals("1") && fenqiCard.equals("0")) {
                oldHaveBank.setVisibility(View.VISIBLE);
            }
            if (zhifu.equals("1") && (fenqi.equals("0") || fenqi.equals("5") || fenqi.equals("2"))) {
                haveBank.setVisibility(View.VISIBLE);
            }
            if (zhifu.equals("0") && fenqi.equals("1") && fenqiCard.equals("1")) {
                haveInstalment.setVisibility(View.VISIBLE);
            }
            if (zhifu.equals("0") && fenqi.equals("1") && fenqiCard.equals("0")) {
                next.setVisibility(View.GONE);
                noPay.setVisibility(View.VISIBLE);
                oldNoBank.setVisibility(View.VISIBLE);
            }
            if ((fenqi.equals("0") || fenqi.equals("5") || fenqi.equals("2")) && zhifu.equals("0")) {
                haveInstalment.setVisibility(View.VISIBLE);
//                moneyMark.setTextColor(Color.parseColor("#A0A0A0"));
                next.setVisibility(View.VISIBLE);
                noPay.setVisibility(View.GONE);
//                enterBackground.setBackgroundResource(R.drawable.view_yj_m8gray);
            }
        }
        getMerchantDetail();
    }

    public void getMerchantDetail() {
        Map<String, Object> maps = new HashMap<>();
        maps.put("no_user_id", 1);
        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(PayMoneyActivity.this, StaticParament.USER);

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("商户详情参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.getMerchantDetail(headerMap, merchantId, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                Gson gson = new Gson();
                L.log("商户详情结果：" + dataStr);
                MerchantInfoData merchantInfoData = gson.fromJson(dataStr, MerchantInfoData.class);
                List<String> detailPhotoList = merchantInfoData.getDetailPhotoList();
                List<BannerDatas> bannerList = new ArrayList<>();

                address.setText(merchantInfoData.getFullAddress());
                if (TextUtils.isEmpty(merchantInfoData.getUserDiscount()) || Float.parseFloat(merchantInfoData.getUserDiscount()) == 0) {
                    //discountBg.setVisibility(View.GONE);
                    discountView.setVisibility(View.GONE);
                } else {
                    String wholeSaleFlag;
                    if (merchantInfoData.getHaveWholeSell() == 0) {
                        wholeSaleFlag = "Limited Time";
                    } else {
                        wholeSaleFlag = "Limited Offer";
                    }
                    String dis = (int) CalcTool.mm(merchantInfoData.getUserDiscount(), "100") + "% OFF";
                    discountView.setText(dis + " - " + wholeSaleFlag);
                }

                recUserId = merchantInfoData.getUserId();
                discount = merchantInfoData.getInstallmentDiscount();
                wholeSaleDiscount = merchantInfoData.getWholeSaleUserDiscount();
                merchantNameStr = merchantInfoData.getPracticalName();
                logoUrl = StaticParament.ImgUrl + merchantInfoData.getLogoUrl();

                for (int i = 0; i < detailPhotoList.size(); i++) {
                    bannerList.add(new BannerDatas(detailPhotoList.get(i)));
                }
                if (TextUtils.isEmpty(merchantName.getText().toString())) {
                    merchantName.setText(merchantInfoData.getPracticalName());
                }
                if (logo.getDrawable() == null) {
                    Glide.with(PayMoneyActivity.this)
                            .load(StaticParament.ImgUrl + merchantInfoData.getLogoUrl())
                            //.apply(RequestOptions.bitmapTransform(new RoundedCorners(PublicTools.dip2px(PayMoneyActivity.this, 11))))//圆角半径
                            .into(logo);
                }
                String bankDiscount = merchantInfoData.getCardPayDiscount();
                String InstalmentDiscount = merchantInfoData.getInstallmentDiscount();
                if (TextUtils.isEmpty(bankDiscount) || bankDiscount.equals("0")) {
                    bankDiscountTwo.setVisibility(View.GONE);
                    oldBankDiscount.setVisibility(View.GONE);
                    bankDiscountBank.setVisibility(View.GONE);
                    discountBank.setVisibility(View.GONE);
                    oldNoBankDiscount.setVisibility(View.GONE);
                    discountBank1.setVisibility(View.GONE);
                } else {
                    String bankDis = (int) CalcTool.mm(bankDiscount, "100") + "% OFF";
                    bankDiscountTwo.setVisibility(View.VISIBLE);
                    oldBankDiscount.setVisibility(View.VISIBLE);
                    bankDiscountBank.setVisibility(View.VISIBLE);
                    discountBank.setVisibility(View.VISIBLE);
                    oldNoBankDiscount.setVisibility(View.VISIBLE);
                    discountBank1.setVisibility(View.VISIBLE);
                    bankDiscountTwo.setText(bankDis);
                    oldBankDiscount.setText(bankDis);
                    oldNoBankDiscount.setText(bankDis);
                    bankDiscountBank.setText(bankDis);
                    discountBank.setText(bankDis);
                    discountBank1.setText(bankDis);
                }
                if (TextUtils.isEmpty(InstalmentDiscount) || InstalmentDiscount.equals("0")) {
                    instalmentDiscountTwo.setVisibility(View.GONE);
                    instalmentDiscountBank.setVisibility(View.GONE);
                    discountInstal.setVisibility(View.GONE);
                    discountInstal1.setVisibility(View.GONE);
                } else {
                    String instalmentDis = (int) CalcTool.mm(InstalmentDiscount, "100") + "% OFF";
                    instalmentDiscountTwo.setVisibility(View.VISIBLE);
                    instalmentDiscountBank.setVisibility(View.VISIBLE);
                    discountInstal.setVisibility(View.VISIBLE);
                    discountInstal1.setVisibility(View.VISIBLE);
                    instalmentDiscountTwo.setText(instalmentDis);
                    instalmentDiscountBank.setText(instalmentDis);
                    discountInstal.setText(instalmentDis);
                    discountInstal1.setText(instalmentDis);
                }
            }


            @Override
            public void resError(String error) {
            }
        });
    }

    public static TransAmountData data11;

    private void showPayNext(final TransAmountData data, final HashMap<String, Object> maps) {
        data.setMerchantName(merchantNameStr);
        data.setAvailableCredit(availableCredit);
        if (data.getRepeatSaleState().equals("1")) {
            TiShiDialogTwo overDue = new TiShiDialogTwo();
            overDue.setOnShure(new TiShiDialogTwo.OnShure() {
                @Override
                public void shure() {
                    Intent intent = new Intent(PayMoneyActivity.this, ConfirmPayActivity.class);
                    intent.putExtra("data", (Serializable) data);
                    data11 = data;
                    intent.putExtra("requestMap", maps);
                    intent.putExtra("productId", creditProductId);
                    intent.putExtra("merchantId", merchantId);
                    intent.putExtra("haveCard", haveCard);
                    startActivity(intent);
                }
            });

            overDue.show(PayMoneyActivity.this, "",
                    String.format("Hang on. You just made a payment of $%s. Are you sure you want to make the payment again?", data.getPayAmount()),
                    "Cancel", "Confirm to pay", "");
            return;
        }
        if (Float.parseFloat(data.getPayAmount()) < Float.parseFloat(data.getMixPayAmount())) {
            TiShiDialog tiShiDialog = new TiShiDialog(PayMoneyActivity.this);
            tiShiDialog.ShowDialog("The BNPL feature is only available for transactions above $" + data.getMixPayAmount());
            return;
        }
        if (payType == 4 && !TextUtils.isEmpty(availableCredit) && Float.parseFloat(availableCredit) < Float.parseFloat(data.getCreditRealPayAmount())) {
            TiShiDialog tiShiDialog = new TiShiDialog(PayMoneyActivity.this);
            tiShiDialog.ShowDialog("Insufficient instalment balance available to pay the current order amount.");
            return;
        }
        Intent intent = new Intent(PayMoneyActivity.this, ConfirmPayActivity.class);
        intent.putExtra("data", (Serializable) data);
        data11 = data;

        intent.putExtra("requestMap", maps);
        intent.putExtra("productId", creditProductId);
        intent.putExtra("merchantId", merchantId);
        intent.putExtra("haveCard", haveCard);
        startActivity(intent);
    }

    public void getQrPayTransAmount(final HashMap<String, Object> maps) {
        payListener.getPayTransAmountDetail(maps, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                TransAmountData data = (TransAmountData) o[0];
                showPayNext(data, maps);
                PayMoneyActivity.this.data = data;
                PayMoneyActivity.this.requestMap = maps;
            }

            @Override
            public void onError(String e) {//Insufficient instalment balance available to pay the current order amount.
//                TiShiDialog tiShiDialog = new TiShiDialog(PayMoneyActivity.this);
//                tiShiDialog.ShowDialog(e, "Confirm");


            }
        });

        /*RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(PayMoneyActivity.this, StaticParament.USER);

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("获取扫码支付的交易金额参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.getQrPayTransAmount(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("获取扫码支付的交易金额：" + dataStr);
                Gson gson = new Gson();
                PayTransAmountData payTransAmountData = gson.fromJson(dataStr, PayTransAmountData.class);
                final Map<String, Object> map = new HashMap<>();
                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(PayMoneyActivity.this, StaticParament.USER);
                String payUserId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
                //更改费率使用的入参
                map.put("no_user_id", 1);
                map.put("payUserId", payUserId);
                map.put("merchantId", merchantId);
                if (posNumber.getVisibility() == View.VISIBLE) {
                    map.put("transAmount", posTransAmount);
                    map.put("posTransNo", posTransNo);
                } else {
                    map.put("transAmount", orderAmount.getText().toString().trim());
                }
                map.put("payType", 0);
                map.put("recUserId", recUserId);

                map.put("mixPayAmount", payTransAmountData.getMixPayAmount());
                map.put("apiToken", apiToken);
                map.put("productId", creditProductId);
                map.put("wholeSalesAmount", payTransAmountData.getWholeSalesAmount());
                map.put("recUserId", recUserId);
                map.put("availableCredit", availableCredit);
                map.put("payType", payType);
                map.put("logoUrl", logoUrl);
                map.put("merchantId", merchantId);
                map.put("merchantName", merchantNameStr);
                map.put("discount", payTransAmountData.getNormalSalesUserDiscount());
                map.put("wholeSaleDiscount", payTransAmountData.getWholeSalesUserDiscount());
                map.put("transNo", payTransAmountData.getTransNo());
                map.put("transAmount", payTransAmountData.getTransAmount());
                //用户可用红包金额总量
                map.put("redEnvelopeAmount", payTransAmountData.getTotalRedEnvelopeAmount());
                map.put("payAmount", payTransAmountData.getPayAmount());
                map.put("payAmountUseRedEnvelope", payTransAmountData.getPayAmountUseRedEnvelope());
                map.put("normalSaleUserDiscountAmount", payTransAmountData.getNormalSaleUserDiscountAmount());
                map.put("wholeSaleUserDiscountAmount", payTransAmountData.getWholeSaleUserDiscountAmount());
                map.put("wholeSaleAmount", payTransAmountData.getWholeSalesAmount());
                map.put("normalSaleAmount", payTransAmountData.getNormalSaleAmount());
                map.put("channelFeeRedEnvelope", payTransAmountData.getChannelFeeRedEnvelope());
                map.put("channelFeeNoRedEnvelope", payTransAmountData.getChannelFeeNoRedEnvelope());
                map.put("transChannelFeeRate", payTransAmountData.getTransChannelFeeRate());
                if (haveFailed == 0 && payTransAmountData.getRepeatSaleState().equals("1")) {
                    TiShiDialogTwo overDue = new TiShiDialogTwo();
                    overDue.setOnShure(new TiShiDialogTwo.OnShure() {
                        @Override
                        public void shure() {
                            confirmPayBottomDialog = new ConfirmPayBottomDialog(PayMoneyActivity.this, map, posTransNo);
                            confirmPayBottomDialog.setOnitemClick(new ConfirmPayBottomDialog.OnitemClick() {
                                @Override
                                public void itemClick() {
                                    Intent intent = new Intent(PayMoneyActivity.this, BankCardListActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("from", "payMoney");
                                    intent.putExtras(bundle);//将Bundle添加到Intent,也可以在Bundle中添加相应数据传递给下个页面,例如：bundle.putString("abc", "bbb");
                                    startActivityForResult(intent, 0);// 跳转并要求返回值，0代表请求值(可以随便写)
                                }
                            });
                            payResultCallBack();
                            confirmPayBottomDialog.show();
                        }
                    });

                    overDue.show(PayMoneyActivity.this, "",
                            String.format("Hang on. You just made a payment of $%s. Are you sure you want to make the payment again?", map.get("transAmount")),
                            "Cancel", "Confirm to pay", "");
                } else {
                    if (haveFailed == 0) {
                        confirmPayBottomDialog = new ConfirmPayBottomDialog(PayMoneyActivity.this, map, posTransNo);
                        confirmPayBottomDialog.setOnitemClick(new ConfirmPayBottomDialog.OnitemClick() {
                            @Override
                            public void itemClick() {
                                Intent intent = new Intent(PayMoneyActivity.this, BankCardListActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("from", "payMoney");
                                intent.putExtras(bundle);//将Bundle添加到Intent,也可以在Bundle中添加相应数据传递给下个页面,例如：bundle.putString("abc", "bbb");
                                startActivityForResult(intent, 0);// 跳转并要求返回值，0代表请求值(可以随便写)
                            }
                        });
                        payResultCallBack();
                        confirmPayBottomDialog.show();
                        return;
                    }
                    if (confirmPayBottomDialog != null) {
                        confirmPayBottomDialog.ChangeRepay(apiToken, payTransAmountData.getTransNo());
                    }
                    if (haveFailed == 1) {
                        haveFailed = 0;
                        TipDialog tiShiDialog = new TipDialog(PayMoneyActivity.this);
                        tiShiDialog.showDialog("Payment failed", errorMsg, "Close");
                    }
                }

            }


            @Override
            public void resError(String error) {
            }
        });*/
    }

    public void payResultCallBack() {
        confirmPayBottomDialog.setOnPayFail(new ConfirmPayBottomDialog.OnPayFail() {
            @Override
            public void payFail(String errorMessage, boolean isPos) {
                if (isPos) {
                    TipDialog tiShiDialog = new TipDialog(PayMoneyActivity.this);
                    tiShiDialog.setOnClose(new TipDialog.Close() {
                        @Override
                        public void Close() {
                            Intent intent1 = new Intent(PayMoneyActivity.this, NewMerchantInfoActivity.class);
                            intent1.putExtra("id", merchantId);
                            startActivity(intent1);
                            finish();
                        }
                    });
                    tiShiDialog.showDialog("Payment failed", errorMessage, "Close");
                    return;
                }
                apiToken = "";
                haveFailed = 1;
                errorMsg = errorMessage;
                if (posNumber.getVisibility() == View.GONE) {
                    GetApiToken getApiToken = new GetApiToken(PayMoneyActivity.this);
                    getApiToken.getApiToken(new HashMap<String, Object>());
                    getApiToken.setOnGetApiToken(new GetApiToken.OnGetApiToken() {
                        @Override
                        public void getToken(String token) {
                            if (token != null) {
                                apiToken = token;
                                L.log("apiToken：" + apiToken);
                                /*if (confirmPayBottomDialog != null) {
                                    confirmPayBottomDialog.disMiss();
                                    confirmPayBottomDialog = null;
                                }*/
                                payNext();
                            }
                        }
                    });
                }
            }

            @Override
            public void noResult(String errorMessage, final boolean isPos) {
                TipDialog tiShiDialog = new TipDialog(PayMoneyActivity.this);
                tiShiDialog.setOnClose(new TipDialog.Close() {
                    @Override
                    public void Close() {
                        if (isPos) {
                            Intent intent1 = new Intent(PayMoneyActivity.this, NewMerchantInfoActivity.class);
                            intent1.putExtra("id", merchantId);
                            startActivity(intent1);
                            finish();
                        } else {
                            new SharePreferenceUtils(PayMoneyActivity.this, StaticParament.USER).put(StaticParament.PAY_SUCCESS, "1").commit();//为了wallet页面刷新
                            Intent it = new Intent(PayMoneyActivity.this, MainTab.class);
                            it.putExtra("num", 2);
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
                            startActivity(it);
                            finish();
                        }
                    }
                });
                tiShiDialog.showDialog("Payment processing", errorMessage, "Close");
            }
        });
    }

    public void creditInfo(Map<String, Object> maps, final boolean instal) {
        RequestUtils requestUtils1 = new RequestUtils(this, maps, StaticParament.FENQIFU, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(PayMoneyActivity.this, StaticParament.USER);

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("分期付状态参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.creditInfo(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("分期付状态：" + dataStr);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataStr);
                    //'分期付状态：10未开通 11冻结 20机审通过 21机审拒绝 22机审中 30待人工审核
                    // 31人工审核通过 32人工审核拒绝 40待落地复核 50系统异常, 待人工核查',
                    String creditState = jsonObject.getString("creditState");
                    //用户可用额度
                    availableCredit = jsonObject.getString("availableCredit");
                    creditProductId = jsonObject.getString("creditProductId");
                    if (!instal) {
                        return;
                    }
                    if (creditState.equals("11")) {
                        TiShiDialog tiShiDialog = new TiShiDialog(PayMoneyActivity.this);
                        tiShiDialog.ShowDialog(getString(R.string.please_repay_money_title), getString(R.string.please_repay_money), "Confirm");
                    } else /*if (creditState.equals("20") || creditState.equals("31"))*/ {
                        HashMap<String, Object> maps = new HashMap<>();
                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(PayMoneyActivity.this, StaticParament.USER);
                        String payUserId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        maps.put("no_user_id", 1);
                        maps.put("payUserId", payUserId);
                        maps.put("merchantId", merchantId);
                        if (posNumber.getVisibility() == View.VISIBLE) {
                            maps.put("transAmount", posTransAmount);
                            maps.put("posTransNo", posTransNo);
                        } else {
                            maps.put("transAmount", orderAmount.getText().toString().trim());
                        }
                        maps.put("payType", 4);
                        maps.put("recUserId", recUserId);

                        getQrPayTransAmount(maps);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void resError(String error) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            if (confirmPayBottomDialog != null) {
                Bundle bundle = data.getExtras();
                String cardNo = bundle.getString("cardNo");
                String cardId = bundle.getString("cardId");
                //confirmPayBottomDialog.setBankData(cardNo, cardId);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (!PublicTools.isFastClick()) {
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.id_back:
                finish();
                break;
//            case R.id.no_pay:
            case R.id.why_add:
            case R.id.why_add1:
            case R.id.old_instalment1:
                AddBankCardTipDialog dialog1 = new AddBankCardTipDialog(this);
                dialog1.setOnitemClick(new AddBankCardTipDialog.OnitemClick() {
                    @Override
                    public void itemClick() {
                        if (haveCard) {
                            Intent intent = new Intent(PayMoneyActivity.this, SelectBankCardActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(PayMoneyActivity.this, NewAddBankCardActivity.class);
//                            intent.putExtra("fromAddCard", 1);
                            intent.putExtra("kaitongfenqifuRoad", true);
                            intent.putExtra("oldHaveCard", true);
                            startActivity(intent);
                        }
                    }
                });
                dialog1.show();
                break;
            case R.id.next:
                next();
//                if (haveNo.getVisibility() == View.VISIBLE) {
//                    return;
//                }
                break;
            case R.id.old_instalment_bank:
            case R.id.instalment_bank:
            case R.id.no_bank:
                Intent intent = new Intent(this, NewAddBankCardActivity.class);
                intent.putExtra("from", "cardList");
                startActivity(intent);
                break;
            case R.id.bank_instalment:
            case R.id.no_instalment:
                mFirst = false;
                user_sharePreferenceUtils.put(StaticParament.USER_RETURN_PAY, "1");
                user_sharePreferenceUtils.put(StaticParament.FROM_PAY_MONEY, "1");
                user_sharePreferenceUtils.save();
                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(PayMoneyActivity.this, StaticParament.USER);
                String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
                Map<String, Object> maps = new HashMap<>();
                maps.put("userId", userId);
                maps.put("stripeState", 0);//用户区分接入stripe前后的新老用户
                jiaoyanFenqifu(maps);
//                Intent intent1 = new Intent(this, FenQiFuOpen_Activity.class);
//                startActivity(intent1);
                break;
            case R.id.old_instalment:
                oldhaveBankText.setBackgroundResource(R.drawable.view_btn_4gray);
                oldBank.setBackgroundResource(R.drawable.view_yj_m4hui);
                oldBankText.setTextColor(getResources().getColor(R.color.pay_text_gray));
                oldBankDiscount.setTextColor(getResources().getColor(R.color.pay_text_gray));
                oldNoBankDiscount.setTextColor(getResources().getColor(R.color.pay_text_gray));
                checkOldBank.setImageDrawable(getResources().getDrawable(R.mipmap.pay_unchoice_icon));
                checkOldBank.setTag("false");
                AddBankCardTipDialog dialog = new AddBankCardTipDialog(this);
                dialog.setOnitemClick(new AddBankCardTipDialog.OnitemClick() {
                    @Override
                    public void itemClick() {
                        if (haveCard) {
                            Intent intent = new Intent(PayMoneyActivity.this, SelectBankCardActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(PayMoneyActivity.this, NewAddBankCardActivity.class);
                            intent.putExtra("kaitongfenqifuRoad", true);
                            intent.putExtra("oldHaveCard", true);
                            startActivity(intent);
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.old_bank:
                oldhaveBankText.setBackgroundResource(R.drawable.view_yj_m4hui);
                oldBank.setBackgroundResource(R.drawable.view_btn_4orange);
                oldBankText.setTextColor(getResources().getColor(R.color.white));
                oldBankDiscount.setTextColor(getResources().getColor(R.color.white));
                oldNoBankDiscount.setTextColor(getResources().getColor(R.color.white));
                checkOldBank.setImageDrawable(getResources().getDrawable(R.mipmap.pay_choice_on));
                checkOldBank.setTag("true");
                break;

            case R.id.id_clear_input:
                orderAmount.setText("");
                break;
        }
    }

    private void next() {
        if (oldNoBank.getVisibility() == View.VISIBLE) {
            AddBankCardTipDialog dialog = new AddBankCardTipDialog(this);
            dialog.setOnitemClick(new AddBankCardTipDialog.OnitemClick() {
                @Override
                public void itemClick() {
                    if (haveCard) {
                        Intent intent = new Intent(PayMoneyActivity.this, SelectBankCardActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(PayMoneyActivity.this, NewAddBankCardActivity.class);
//                                intent.putExtra("fromAddCard", 1);
                        intent.putExtra("kaitongfenqifuRoad", true);
                        intent.putExtra("oldHaveCard", true);
                        startActivity(intent);
                    }
                }
            });
            dialog.show();
            return;
        }
        if (oldHaveBank.getVisibility() == View.VISIBLE && checkOldBank.getTag().toString().equals("false")) {
            AddBankCardTipDialog dialogs = new AddBankCardTipDialog(this);
            dialogs.setOnitemClick(new AddBankCardTipDialog.OnitemClick() {
                @Override
                public void itemClick() {
                    if (haveCard) {
                        Intent intent = new Intent(PayMoneyActivity.this, SelectBankCardActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(PayMoneyActivity.this, NewAddBankCardActivity.class);
//                                intent.putExtra("fromAddCard", 1);
                        intent.putExtra("kaitongfenqifuRoad", true);
                        intent.putExtra("oldHaveCard", true);
                        startActivity(intent);
                    }
                }
            });
            dialogs.show();
            return;
        }
        if (posNumber.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(posTransAmount)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//大于等于9.0
                    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/gilroy_medium.ttf");
                    SpannableString efr = new SpannableString("Please enter the payment amount");
                    efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Toast.makeText(this, efr, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please enter the payment amount", Toast.LENGTH_SHORT).show();

                }
                return;
            }
        } else {
            if (TextUtils.isEmpty(orderAmount.getText().toString().trim()) || Float.parseFloat(orderAmount.getText().toString().trim()) == 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//大于等于9.0
                    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/gilroy_medium.ttf");
                    SpannableString efr = new SpannableString("Please enter the payment amount");
                    efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Toast.makeText(this, efr, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please enter the payment amount", Toast.LENGTH_SHORT).show();

                }
                return;
            }
        }

        if (showPop) {
            TiShiDialogTwo tiShiDialogTwo = new TiShiDialogTwo();
            tiShiDialogTwo.show(PayMoneyActivity.this, "Is this the right amount?", "Please confirm the amount with the venue before you continue.", "Cancel", "Confirm");
            tiShiDialogTwo.setOnShure(new TiShiDialogTwo.OnShure() {
                @Override
                public void shure() {
                    if (installmentState.equals("2") && creditCardState.equals("0") && cardState.equals("0")&&check2Two.getTag().equals("true")) {
                        payNext();
                        return;
                    }
                    if (installmentState.equals("2") && creditCardState.equals("0")) {
                        Intent intentAdd = new Intent(PayMoneyActivity.this, NewAddBankCardActivity.class);
                        intentAdd.putExtra("fromAddCard", 1);
                        intentAdd.putExtra("isCreditCard", 1);
                        intentAdd.putExtra("kaitongfenqifuRoad", true);
                        startActivity(intentAdd);
                        return;
                    }
                    payNext();
                }
            });
        } else {
            if (installmentState.equals("2") && creditCardState.equals("0") && cardState.equals("0")&&check2Two.getTag().equals("true")) {
                payNext();
                return;
            }
            if (installmentState.equals("2") && creditCardState.equals("0")) {
                Intent intentAdd = new Intent(PayMoneyActivity.this, NewAddBankCardActivity.class);
                intentAdd.putExtra("fromAddCard", 1);
                intentAdd.putExtra("isCreditCard", 1);
                intentAdd.putExtra("kaitongfenqifuRoad", true);
                startActivity(intentAdd);
                return;
            }
            payNext();
        }
    }

    private boolean fenqifuKaitongWeibangka = false;

    /**
     * 分期付开通状态
     *
     * @param maps
     */
    private void jiaoyanFenqifu(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(PayMoneyActivity.this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {
                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.jiaoyanFenqifu(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                Gson gson = new Gson();
                FenqifuStatue fenqifuStatue = gson.fromJson(dataStr, FenqifuStatue.class);
                FirstFragment.backStatue = 3;
                if (mFirst) {
                    haveTwo.setVisibility(View.GONE);
                    haveBank.setVisibility(View.GONE);
                    haveInstalment.setVisibility(View.GONE);
                    oldHaveBank.setVisibility(View.GONE);
                    oldNoBank.setVisibility(View.GONE);
                    haveNo.setVisibility(View.GONE);
                    next.setVisibility(View.VISIBLE);
                    noPay.setVisibility(View.GONE);
//                    enterBackground.setBackgroundResource(R.drawable.view_yj_m8cheng);
//                    moneyMark.setTextColor(getResources().getColor(R.color.colorBackGround));
                    //fenqiCard：	 userMsgDatas.getCreditCardState()
                    // zhifu：    userMsgDatas.getCardState()
                    // fenqi：    userMsgDatas.getInstallmentState()
                    creditCardState = String.valueOf(fenqifuStatue.getCreditCardState());
                    cardState = String.valueOf(fenqifuStatue.getCardState());
                    installmentState = String.valueOf(fenqifuStatue.getInstallmentState());
                    if (cardState.equals("1")) {
                        haveCard = true;
                        fenqifuKaitongWeibangka = false;
                    } else {
                        haveCard = false;
                    }
                    if (cardState.equals("1") && installmentState.equals("2") && !creditCardState.equals("2")) {//都绑了
                        haveTwo.setVisibility(View.VISIBLE);
                    }
                    if (cardState.equals("1") && installmentState.equals("2") && creditCardState.equals("2")) {//老用户 已经绑卡
                        oldHaveBank.setVisibility(View.VISIBLE);
                    }
                    if (cardState.equals("1") && (!installmentState.equals("2"))) {//分期付没有开通 卡支付开通了了
                        haveBank.setVisibility(View.VISIBLE);
                    }
                    if (cardState.equals("0") && installmentState.equals("2") && !creditCardState.equals("2")) {//开通分期付 没有绑卡 (支付失败把所有卡都删除了)
//                        haveInstalment.setVisibility(View.VISIBLE);
                        haveTwo.setVisibility(View.VISIBLE);

                        fenqifuKaitongWeibangka = true;
//                        haveBank.setVisibility(View.VISIBLE);
//                        next.setVisibility(View.VISIBLE);
//                        noPay.setVisibility(View.GONE);
                    }

                    if (cardState.equals("0") && installmentState.equals("2") && String.valueOf(fenqifuStatue.getCreditCardAgreementState()).equals("1")) {
                        fenqifuKaitongWeibangka = true;
                    }

                    if (installmentState.equals("2") && creditCardState.equals("2")) {//老用户 (无论有没有卡都按照有卡处理)
                        next.setVisibility(View.VISIBLE);
                        noPay.setVisibility(View.GONE);
//                        oldNoBank.setVisibility(View.VISIBLE);
                        oldHaveBank.setVisibility(View.VISIBLE);
                    }
//                    if (cardState.equals("0") && installmentState.equals("2") && creditCardState.equals("2")) {//老用户 未绑卡
////                        next.setVisibility(View.GONE);
////                        noPay.setVisibility(View.VISIBLE);
////                        oldNoBank.setVisibility(View.VISIBLE);
//                        haveBank.setVisibility(View.VISIBLE);
////                moneyMark.setTextColor(Color.parseColor("#A0A0A0"));
//                        next.setVisibility(View.VISIBLE);
//                        noPay.setVisibility(View.GONE);
//                    }

                    if (cardState.equals("0") && !installmentState.equals("2")) {//xzc 都没开通

                        haveBank.setVisibility(View.VISIBLE);
//                moneyMark.setTextColor(Color.parseColor("#A0A0A0"));
                        next.setVisibility(View.VISIBLE);
                        noPay.setVisibility(View.GONE);

//                        haveNo.setVisibility(View.VISIBLE);
//                        moneyMark.setTextColor(Color.parseColor("#A0A0A0"));
//                        next.setVisibility(View.GONE);
//                        noPay.setVisibility(View.VISIBLE);
//                        enterBackground.setBackgroundResource(R.drawable.view_yj_m8gray);
                    }
                    getMerchantDetail();
                } else {
                    SharePreferenceUtils appMsgSharePreferenceUtils =
                            new SharePreferenceUtils(PayMoneyActivity.this, StaticParament.DEVICE);
                    String callPhone = (String) appMsgSharePreferenceUtils.get(StaticParament.APP_PHONE, "");


                    SelectPayTypeActivity.shifoukaitongCardPay = fenqifuStatue.getCardState();//0：未开通开支付  1:开通开卡支付
                    SelectPayTypeActivity.meikaitongfenqifuAndkaitongCardPay = fenqifuStatue.getCreditCardAgreementState();

                    if (fenqifuStatue.getKycState() == 0) {//未开通kyc
                        Intent intent_register = new Intent(PayMoneyActivity.this, RegistTwo_Activity.class); //跳到注册页 从注册页跳到kyc页
                        intent_register.putExtra("jumpToKyc", true);
                        startActivity(intent_register);
                    } else {//已经开通kyc
                        if (fenqifuStatue.getCardState() == 0) {//未开通卡支付
                            Intent intent_kyc = new Intent(PayMoneyActivity.this, NewAddBankCardActivity.class);
                            intent_kyc.putExtra("kaitongfenqifuRoad", true);
                            startActivity(intent_kyc);
                        } else {//已经开通卡支付 //判断illion状态
                            if (fenqifuStatue.getCreditCardAgreementState() == 0) {//是否勾选过分期付绑卡协议 0：未勾选 1：已勾选
                                Intent intent_kyc = new Intent(PayMoneyActivity.this, ChooseCardActivity.class);
                                startActivity(intent_kyc);
                            } else {
                                if (fenqifuStatue.getInstallmentState() == 2) {//开通illion 开通了分期付
                                    Intent intent = new Intent(PayMoneyActivity.this, ConfirmPayActivity.class);
                                    intent.putExtra("data", (Serializable) data);
                                    data11 = data;

                                    intent.putExtra("requestMap", PayMoneyActivity.this.requestMap);
                                    intent.putExtra("productId", creditProductId);
                                    intent.putExtra("merchantId", merchantId);
                                    intent.putExtra("haveCard", haveCard);
                                    startActivity(intent);
                                } else if (fenqifuStatue.getInstallmentState() == 0 || fenqifuStatue.getInstallmentState() == 3) {//未开通！ 0. 用户未开通分期付(需要完善信息) 3. illion未授权
                                    Intent intent_kyc = new Intent(PayMoneyActivity.this, ChooseIllionActivity.class);
                                    startActivity(intent_kyc);
                                } else if (fenqifuStatue.getInstallmentState() == 1 || fenqifuStatue.getInstallmentState() == 5 || fenqifuStatue.getInstallmentState() == 8) {//—-失败！—— 1. 用户分期付已冻结 5. 用户分期付禁用 8 机审拒绝
                                    Intent intent_kyc = new Intent(PayMoneyActivity.this, KycAndIllionResultActivity.class);
                                    intent_kyc.putExtra("error", "FkReject");
                                    intent_kyc.putExtra("phone", callPhone);
                                    startActivity(intent_kyc);
                                } else if (fenqifuStatue.getInstallmentState() == 4 || fenqifuStatue.getInstallmentState() == 7 || fenqifuStatue.getInstallmentState() == 9) {//—-等待！——4. 等待人工审核 7. 机审中 9 分期付风控未触发
                                    Intent intent_kyc = new Intent(PayMoneyActivity.this, KycAndIllionResultActivity.class);
                                    intent_kyc.putExtra("error", "Waiting");
                                    intent_kyc.putExtra("phone", callPhone);
                                    startActivity(intent_kyc);
                                }
                            }
                        }
                    }
                    mFirst = true;
                }
                Log.d("xunzhic", "resData: " + dataStr);
            }

            @Override
            public void resError(String error) {

            }
        });
    }

    private void payNext() {
        //0：卡支付 4：分期付
        if (oldHaveBank.getVisibility() == View.VISIBLE && checkOldBank.getTag().toString().equals("true")) {
            payType = 0;
        }
        if (haveTwo.getVisibility() == View.VISIBLE) {
            if (check1Two.getTag().equals("true")) {
                payType = 4;
            }
            if (check2Two.getTag().equals("true")) {
                payType = 0;
            }
        }
        if (haveBank.getVisibility() == View.VISIBLE) {
            payType = 0;
        }
        if (haveInstalment.getVisibility() == View.VISIBLE) {
            payType = 4;
        }

        HashMap<String, Object> maps = new HashMap<>();
        if (payType == 4) {
            if (fenqifuKaitongWeibangka) {
                Intent intent = new Intent(PayMoneyActivity.this, NewAddBankCardActivity.class);
                startActivity(intent);
            } else {
                creditInfo(maps, true);
            }
        } else {
            //查询是否激活分期付
            openFenQiFu_p.loadFenQiFuStatus(new HashMap<String, Object>());
            SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(PayMoneyActivity.this, StaticParament.USER);
            String payUserId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

            maps.put("no_user_id", 1);
            maps.put("payUserId", payUserId);
            maps.put("merchantId", merchantId);
            if (posNumber.getVisibility() == View.VISIBLE) {
                maps.put("transAmount", posTransAmount);
                maps.put("posTransNo", posTransNo);
            } else {
                maps.put("transAmount", orderAmount.getText().toString().trim());
            }
            maps.put("payType", 0);
            maps.put("recUserId", recUserId);
            getQrPayTransAmount(maps);
        }
    }

    @Override
    public void setFenQiFuStatus(String status, String illingUrl) {
        if (status == null) {
            return;
        }

        /**
         * 0，用户未开通分期付(需要完善信息)
         * 1，用户分期付已冻结
         * 2，用户分期付开通但会员未开通
         * 3，用户分期付开通并且会员已开通
         * 4，等待人工审核
         * 5，用户分期付处于禁用状态
         * 6，分期付信息未完善：尚未绑卡
         * 7、风控中
         * */
        L.log("分期付状态：" + status);
        switch (status) {
            case "0":
//                TiShiDialog tiShiDialog = new TiShiDialog(this);
//                tiShiDialog.ShowDialog(getString(R.string.please_repay_money_title), getString(R.string.please_repay_money), "");
                break;
            default:
                HashMap<String, Object> maps = new HashMap<>();
                creditInfo(maps, false);
        }
    }

    /**
     * 重写dispatchTouchEvent
     * 点击软键盘外面的区域关闭软键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                //根据判断关闭软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断用户点击的区域是否是输入框
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
