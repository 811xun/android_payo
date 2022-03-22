package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.braze.Braze;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.BankDatas;
import com.home.glx.uwallet.datas.FenQiRepayDatas;
import com.home.glx.uwallet.datas.RepayFQDatas;
import com.home.glx.uwallet.mvp.CheckPayPassword_in;
import com.home.glx.uwallet.mvp.CheckPayPassword_p;
import com.home.glx.uwallet.mvp.ScanRepay_in;
import com.home.glx.uwallet.mvp.ScanRepay_p;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.BzEventParameterName;
import com.home.glx.uwallet.tools.BzEventType;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmPayBottomDialog implements ScanRepay_in.View,
        CheckPayPassword_in.View {
    private MyDialog dialog;
    private Context context;
    private LayoutInflater inflater;
    private View thisView;
    private Map<String, Object> map;
    private ImageView cardLogo;
    private TextView merchantName;
    private TextView transitionNo;
    private TextView payMoney;
    private TextView wholeSalePer;
    private TextView wholeSaleDis;
    private TextView defaultSalePer;
    private TextView defaultSaleDis;
    private TextView feePercent;
    private TextView feeAmount;
    private TextView pocketAmount;
    private TextView payMethod;
    private TextView userPayText;
    private LinearLayout card_ll;
    private LinearLayout whole_sale_ll;
    private LinearLayout default_sale_ll;
    private LinearLayout pocket_ll;
    private TextView next;
    private SwitchView select_pocket;
    private ImageView close;
    private LinearLayout pocketAvailableLl;
    private LinearLayout pocketAmountLl;
    private EditText inputPocketEdit;
    private TextView usePocketAmount;
    private TextView totalMoney;

    private ScanRepay_p scanRepay_p;
    private String bankNo;
    private String bankId;
    private String bankType;
    private String recUserId;
    private String merchantId;
    private String transAmount;
    private String trueMoney;
    private String redEnvelopeAmount;
    private String wholeSalesAmount;
    private String productId;
    private String mixPayAmount;
    private String payAmount;
    private String payAmountUseRedEnvelope;
    private String channelFeeRedEnvelope;
    private String channelFeeNoRedEnvelope;
    private String transChannelFeeRate;
    private int patType;
    private String apiToken;
    private String transNo;
    private String merchantNames;
    private LinearLayout availableLl;
    private TextView availableAmount;
    private ImageView enterIcon;

    private CheckPayPassword_p checkPayPassword_p;

    private List<BankDatas> list;
    private List<RepayFQDatas> listData;

    private Date payDate;
    private Date resultDate;

    private Handler handler;
    private Runnable runnable;
    private String availableCredit;

    private LoadingDialog loadingDialog;
    private PaymentWaitingDialog paymentWaitingDialog;
    private InstalmentsListDialg instalmentsListDialg;
    private UserListener userListener;
    private SharePreferenceUtils sharePreferenceUtils;

    public static final int DIFF_TIME = 10000;
    private String posTransNo;
    private TextView methodPayment;

    public ConfirmPayBottomDialog(Context context, Map<String, Object> map, String posTransNo) {
        this.context = context;
        this.map = map;
        this.posTransNo = posTransNo;
        userListener = new UserModel(context);
        initView();
    }

    public void setBankData(String bankData, String cardId, String type){
        bankNo = bankData;
        bankId = cardId;
        bankType = type;
        bankData = bankData.replaceAll(" ","");
        payMethod.setText(bankData.substring(bankData.length() - 8));
        cardLogo.setVisibility(View.VISIBLE);
        if (bankType.equals("10")) {
            cardLogo.setImageResource(R.mipmap.pay_visa_logo);
        } else if (bankType.equals("20")) {
            cardLogo.setImageResource(R.mipmap.pay_master_logo);
        } else {
            cardLogo.setImageResource(R.mipmap.pay_onther_logo);
        }
    }

    private void initView() {
        scanRepay_p = new ScanRepay_p(context, this);
        checkPayPassword_p = new CheckPayPassword_p(context, this);
        inflater = LayoutInflater.from(context);
        thisView = inflater.inflate(R.layout.dialog_bottom_confirm_pay, null);
    }

    public void ChangeRepay(String apiToken, String transNo) {
        this.apiToken = apiToken;
        this.transNo = transNo;
        transitionNo.setText(transNo);
    }

    public void disMiss() {
        if (instalmentsListDialg != null) {
            instalmentsListDialg.disMiss();
        }
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void show() {

        dialog = new MyDialog(context, R.style.MyRadiusDialog, R.layout.dialog_bottom_confirm_pay);
        dialog.show();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);

        cardLogo = (ImageView) dialog.findViewById(R.id.card_logo);
        enterIcon = (ImageView) dialog.findViewById(R.id.enter_icon);
        card_ll = (LinearLayout) dialog.findViewById(R.id.card_ll);
        whole_sale_ll = (LinearLayout) dialog.findViewById(R.id.whole_sale_ll);
        default_sale_ll = (LinearLayout) dialog.findViewById(R.id.default_sale_ll);
        pocket_ll = (LinearLayout) dialog.findViewById(R.id.pocket_ll);
        pocketAvailableLl = (LinearLayout) dialog.findViewById(R.id.pocket_available);
        pocketAmountLl = (LinearLayout) dialog.findViewById(R.id.pocket_amount_ll);
        inputPocketEdit = (EditText) dialog.findViewById(R.id.input_pocket_amount);
        usePocketAmount = (TextView) dialog.findViewById(R.id.use_pocket_amount);
        merchantName = (TextView) dialog.findViewById(R.id.merchant_name);
        transitionNo = (TextView) dialog.findViewById(R.id.transition_no);
        payMoney = (TextView) dialog.findViewById(R.id.pay_money);
        userPayText = (TextView) dialog.findViewById(R.id.use_payo_money);
        wholeSalePer = (TextView) dialog.findViewById(R.id.discount_1);
        wholeSaleDis = (TextView) dialog.findViewById(R.id.whole_sale_de);
        defaultSalePer = (TextView) dialog.findViewById(R.id.default_discount);
        defaultSaleDis = (TextView) dialog.findViewById(R.id.default_sale_de);
        feePercent = (TextView) dialog.findViewById(R.id.fee_percent);
        feeAmount = (TextView) dialog.findViewById(R.id.fee_money);
        pocketAmount = (TextView) dialog.findViewById(R.id.pocket_amount);
        payMethod = (TextView) dialog.findViewById(R.id.pay_method);
        next = (TextView) dialog.findViewById(R.id.next);
        select_pocket = (SwitchView) dialog.findViewById(R.id.select_pocket);
        close = (ImageView) dialog.findViewById(R.id.close);
        availableLl = dialog.findViewById(R.id.available_amount_ll);
        availableAmount = dialog.findViewById(R.id.available_amount);
        totalMoney = (TextView) dialog.findViewById(R.id.total_money);
        TextView orderAmountText = dialog.findViewById(R.id.order_amount_text);
        methodPayment = dialog.findViewById(R.id.method_payment);
        TextView totalText = dialog.findViewById(R.id.total_text);
        TextView availableText = dialog.findViewById(R.id.available_text);
        TextView availableCreditText = dialog.findViewById(R.id.available_credit_text);

        //切换字体
        TypefaceUtil.replaceFont(merchantName,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(transitionNo,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(orderAmountText,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(userPayText,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(feePercent,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(methodPayment,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(payMoney,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(feeAmount,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(payMethod,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(totalText,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(totalMoney,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(next,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(availableText,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(inputPocketEdit,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(availableCreditText,"fonts/gilroy_medium.ttf");

        apiToken = (String) map.get("apiToken");
        wholeSalesAmount = (String) map.get("wholeSalesAmount");
        productId = (String) map.get("productId");
        recUserId = (String) map.get("recUserId");
        availableCredit = (String) map.get("availableCredit");
        patType = (int) map.get("payType");
        merchantId = (String) map.get("merchantId");
        String wholeSaleAmount = (String) map.get("wholeSaleAmount");
        String normalSaleAmount = (String) map.get("normalSaleAmount");
        String discount = (String) map.get("discount");
        String wholeSaleDiscount = (String) map.get("wholeSaleDiscount");
        String wholeSaleUserDiscountAmount = (String) map.get("wholeSaleUserDiscountAmount");
        String normalSaleUserDiscountAmount = (String) map.get("normalSaleUserDiscountAmount");
        redEnvelopeAmount = (String) map.get("redEnvelopeAmount");
        payAmountUseRedEnvelope = (String) map.get("payAmountUseRedEnvelope");
        merchantNames = (String) map.get("merchantName");
        transNo = (String) map.get("transNo");
        transAmount = (String) map.get("transAmount");
        mixPayAmount = (String) map.get("mixPayAmount");
        payAmount = (String) map.get("payAmount");
        String logoUrl = (String) map.get("logoUrl");
        channelFeeRedEnvelope = (String) map.get("channelFeeRedEnvelope");
        channelFeeNoRedEnvelope = (String) map.get("channelFeeNoRedEnvelope");
        transChannelFeeRate = (String) map.get("transChannelFeeRate");
        
        merchantName.setText(merchantNames);
        transitionNo.setText(transNo);
        payMoney.setText("$" + PublicTools.twoend(transAmount));
        if (patType == 0) {
            card_ll.setVisibility(View.VISIBLE);
            methodPayment.setText("Pay in full");
            payMethod.setText("Bank Card");
            Map<String, Object> maps = new HashMap<>();
            //0:账户  1:卡支付
            maps.put("cardType", "1");
            userListener.getCardList(maps, new ActionCallBack() {
                @Override
                public void onSuccess(Object... o) {
                    List<BankDatas> list = (List<BankDatas>) o[0];
                    setBankList(list);
                }

                @Override
                public void onError(String e) {

                }
            });
            final Map<String, Object> moneyMap = new HashMap<>();
            SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
            String payUserId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
            moneyMap.put("no_user_id", 1);
            moneyMap.put("payUserId", payUserId);
            moneyMap.put("merchantId", merchantId);
            moneyMap.put("transAmount", transAmount);
            moneyMap.put("payType", 0);
            moneyMap.put("recUserId", recUserId);
            payMethod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转到银行卡列表
                    //onitemClick.itemClick();
                    if (list == null) {
                        Toast.makeText(context, "Please wait for card information to be obtained.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //改为dilaog展示列表
                    BankListDialog bankListDialog = new BankListDialog(moneyMap, ConfirmPayBottomDialog.this, context, list, bankId, posTransNo);
                    bankListDialog.show();
                    bankListDialog.setOnitemClick(new BankListDialog.OnitemClick() {
                        @Override
                        public void itemClick(String cardNo, String id) {
                            //setBankData(cardNo, id);
                        }
                    });
                }
            });
            enterIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list == null) {
                        Toast.makeText(context, "Please wait for card information to be obtained.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    BankListDialog bankListDialog = new BankListDialog(moneyMap, ConfirmPayBottomDialog.this, context, list, bankId, posTransNo);
                    bankListDialog.show();
                    bankListDialog.setOnitemClick(new BankListDialog.OnitemClick() {
                        @Override
                        public void itemClick(String cardNo, String id) {
                            //setBankData(cardNo, id);
                        }
                    });
                }
            });
            if (TextUtils.isEmpty(channelFeeNoRedEnvelope) || Double.parseDouble(channelFeeNoRedEnvelope) == 0) {
                card_ll.setVisibility(View.GONE);
            } else {
                card_ll.setVisibility(View.VISIBLE);
                feeAmount.setText("$" + PublicTools.twoend(channelFeeNoRedEnvelope));
                //String dis = (int) CalcTool.mm(transChannelFeeRate, "100") + "%";
                feePercent.setText("Transaction fee");
                feeAmount.setText("$" + PublicTools.twoend(channelFeeNoRedEnvelope));
                //String dis = (int) CalcTool.mm(transChannelFeeRate, "100") + "%";
                    feePercent.setText("Transaction Fee"/*String.format("Transaction Fee(%s)",transChannelFeeRate + "%")*/);
            }
        } else {
            methodPayment.setText("Bank account");
            availableAmount.setText("$" + PublicTools.twoend(availableCredit));
            availableLl.setVisibility(View.VISIBLE);
            payMethod.setText("Pay with Payo");
            payMethod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFQList();
                    /*if (listData.size() == 0) {
                        Toast.makeText(context, "No repayment plan", Toast.LENGTH_SHORT).show();
                    } else {
                        InstalmentsListDialg instalmentsListDialg = new InstalmentsListDialg(context, listData);
                        instalmentsListDialg.show();
                    }*/
                }
            });
            enterIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openFQList();
                }
            });
            card_ll.setVisibility(View.GONE);
        }
        int discountNum = 0;
        if ((!TextUtils.isEmpty(normalSaleUserDiscountAmount) && Float.parseFloat(normalSaleUserDiscountAmount) != 0)) {
            discountNum++;
        }
        if ((!TextUtils.isEmpty(wholeSaleUserDiscountAmount) && Float.parseFloat(wholeSaleUserDiscountAmount) != 0)) {
            discountNum++;
        }
        if (discountNum != 2) {
            if ((TextUtils.isEmpty(normalSaleUserDiscountAmount) || Float.parseFloat(normalSaleUserDiscountAmount) == 0)) {
                default_sale_ll.setVisibility(View.GONE);
            } else {
                int defaultDis = (int) Float.parseFloat(discount);
                defaultSalePer.setText(/*(int) CalcTool.mm(wholeSaleDiscount, "100")*/String.format("Discount (%s)", defaultDis + "%"));
                if (!TextUtils.isEmpty(normalSaleUserDiscountAmount) && Float.parseFloat(normalSaleUserDiscountAmount) > 0) {
                    defaultSaleDis.setText("-$" + normalSaleUserDiscountAmount);
                }
            }
            if ((TextUtils.isEmpty(wholeSaleUserDiscountAmount) || Float.parseFloat(wholeSaleUserDiscountAmount) == 0)) {
                whole_sale_ll.setVisibility(View.GONE);
            } else {
                int wholeDis = (int) Float.parseFloat(wholeSaleDiscount);
                wholeSalePer.setText(/*(int) CalcTool.mm(wholeSaleDiscount, "100")*/String.format("Discount (%s)", wholeDis + "%"));
                if (!TextUtils.isEmpty(wholeSaleUserDiscountAmount) && Float.parseFloat(wholeSaleUserDiscountAmount) > 0) {
                    wholeSaleDis.setText("-$" + wholeSaleUserDiscountAmount);
                }
            }
        } else {
            default_sale_ll.setVisibility(View.GONE);
            wholeSalePer.setText("Discount");
            wholeSaleDis.setText("-$" + CalcTool.add(wholeSaleUserDiscountAmount, normalSaleUserDiscountAmount));
        }

        if (TextUtils.isEmpty(redEnvelopeAmount) || Float.parseFloat(redEnvelopeAmount) == 0) {
            pocket_ll.setVisibility(View.GONE);
        } else {
            pocket_ll.setVisibility(View.VISIBLE);
            inputPocketEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString();
                    inputPocketEdit.setSelection(String.valueOf(text).length());
                    if (TextUtils.isEmpty(text)) {
                        usePocketAmount.setText("");
                        next.setText("Pay $" + payAmount);
                        totalMoney.setText("$" + payAmount);
                        return;
                    }
                    Float MinAmount = Float.parseFloat(redEnvelopeAmount) < Float.parseFloat(payAmount)? Float.parseFloat(redEnvelopeAmount):Float.parseFloat(payAmount);
                    if (Float.parseFloat(text) > MinAmount) {
                        inputPocketEdit.setText( CalcTool.rounded(String.valueOf(MinAmount), 2));
                        return;
                       /* usePocketAmount.setText(String.valueOf(MinAmount));
                        String finalMoney = CalcTool.rounded(CalcTool.sub(payAmount, String.valueOf(MinAmount)), 2);
                        next.setText("Pay $" + finalMoney);
                        totalMoney.setText("$" + finalMoney);*/
                    }
                    if (text.equals(".")) {
                        inputPocketEdit.setText("");
                        return;
                    }
                    if (s.toString().contains(".")) {
                        if (s.length() - 1 - s.toString().indexOf(".") > 2) {
//                        text = (String) s.toString().subSequence(0,
//                                s.toString().indexOf(".") + 3);
                            String[] split = s.toString().split(".");
                            text = s.toString().substring(0,s.toString().indexOf(".")) + s.toString().substring(s.toString().indexOf("."),s.toString().indexOf(".") + 3);
                            //text = String.valueOf(PublicTools.twoend(s.toString()));
                            inputPocketEdit.setText(CalcTool.rounded(text, 2));
                            return;
                        }
                    }
                    usePocketAmount.setText("-$" + CalcTool.rounded(text, 2));
                    String finalMoney = CalcTool.rounded(CalcTool.sub(payAmount, text), 2);
                    next.setText("Pay $" + finalMoney);
                    totalMoney.setText("$" + finalMoney);
                    if (text.equals(".") || text.equals("0..")/* || text.equals("0")*/) {
                        inputPocketEdit.setText("0.");
                        return;
                    }
                }
            });
            pocketAmount.setText("$" + PublicTools.twoend(redEnvelopeAmount));
        }

        next.setText("Pay $" + PublicTools.twoend(payAmount));
        totalMoney.setText("$" + payAmount);
        trueMoney = next.getText().toString().substring(5);

        select_pocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select_pocket.getTag().equals("choice")) {
                    userPayText.setTextColor(context.getResources().getColor(R.color.pay_text_gray));
                    pocketAvailableLl.setVisibility(View.GONE);
                    pocketAmountLl.setVisibility(View.GONE);
                    select_pocket.setTag("unchoice");
                    next.setText("Pay $" + PublicTools.twoend(payAmount));
                    totalMoney.setText("$" + PublicTools.twoend(payAmount));
                } else {
                    select_pocket.setTag("choice");
                    userPayText.setTextColor(context.getResources().getColor(R.color.black));
                    pocketAvailableLl.setVisibility(View.VISIBLE);
                    pocketAmountLl.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(usePocketAmount.getText().toString())) {
                        next.setText("Pay $" + PublicTools.twoend(payAmount));
                        totalMoney.setText("$" + PublicTools.twoend(payAmount));
                    } else {
                        String finalMoney = CalcTool.rounded(CalcTool.sub(payAmount, inputPocketEdit.getText().toString()), 2);
                        next.setText("Pay $" + finalMoney);
                        totalMoney.setText("$" + finalMoney);
                    }
                }
                trueMoney = next.getText().toString().substring(5);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trueMoney = next.getText().toString().substring(5);
                if (patType == 4) {
                    checkFenqiPay();
                    /*Map<String, Object> maps1 = new HashMap<>();
                    maps1.put("merchantId", merchantId);
                    scanRepay_p.getFenQiLimitData(maps1);*/
                } else {
                    if (TextUtils.isEmpty(bankNo)) {
                        Toast.makeText(context, "Please select your Bank card", Toast.LENGTH_SHORT).show();
                    } else {
                        showPayPwdDialog();
                    }
                }
            }
        });
    }

    public void checkFenqiPay() {
        if (!TextUtils.isEmpty(availableCredit) && Float.parseFloat(availableCredit) < Float.parseFloat(trueMoney)) {
            Toast.makeText(context, context.getString(R.string.ins_balance), Toast.LENGTH_SHORT).show();
        } else {
            if (Double.parseDouble(trueMoney) < Double.parseDouble(mixPayAmount)) {
                Toast.makeText(context, "The BNPL feature is only available for transactions above $" + mixPayAmount, Toast.LENGTH_SHORT).show();
            } else {
                showPayPwdDialog();
            }
        }
    }

    public void changeFee(Map<String, Object> map) {
        String cardNo = (String) map.get("cardNo");
        String cardId = (String) map.get("cardId");
        String cardType = (String) map.get("cardType");
        setBankData(cardNo, cardId, cardType);
        payAmountUseRedEnvelope = (String) map.get("payAmountUseRedEnvelope");
        mixPayAmount = (String) map.get("mixPayAmount");
        payAmount = (String) map.get("payAmount");
        channelFeeRedEnvelope = (String) map.get("channelFeeRedEnvelope");
        channelFeeNoRedEnvelope = (String) map.get("channelFeeNoRedEnvelope");
        transChannelFeeRate = (String) map.get("transChannelFeeRate");
        if (TextUtils.isEmpty(channelFeeNoRedEnvelope) || Float.parseFloat(channelFeeNoRedEnvelope) == 0) {
            card_ll.setVisibility(View.GONE);
        } else {
            card_ll.setVisibility(View.VISIBLE);
            feeAmount.setText("$" + PublicTools.twoend(channelFeeNoRedEnvelope));
            //String dis = (int) CalcTool.mm(transChannelFeeRate, "100") + "%";
            feePercent.setText("Transaction fee");
        }
        if (select_pocket.getTag().equals("choice")) {
            if (!TextUtils.isEmpty(usePocketAmount.getText().toString())) {
                String finalMoney = CalcTool.rounded(CalcTool.sub(payAmount, inputPocketEdit.getText().toString()), 2);
                next.setText("Pay $" + finalMoney);
                totalMoney.setText("$" + finalMoney);
            }
        } else {
            next.setText("Pay $" + PublicTools.twoend(payAmount));
            totalMoney.setText("$" + PublicTools.twoend(payAmount));
            trueMoney = next.getText().toString().substring(5);
        }
    }

    private void openFQList() {
        Map<String, Object> maps = new HashMap<>();
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
        maps.put("payUserId", sharePreferenceUtils.get(StaticParament.USER_ID, ""));
        maps.put("recUserId", recUserId);
        maps.put("no_user_id", 1);
        maps.put("merchantId", merchantId);
        //交易金额
        maps.put("transAmount", transAmount);
        //实付金额
        trueMoney = next.getText().toString().substring(5);
        maps.put("trulyPayAmount", trueMoney);
        //使用的红包金额
        if (select_pocket.getTag().equals("choice")) {
            if (TextUtils.isEmpty(inputPocketEdit.getText().toString())) {
                maps.put("redEnvelopeAmount", "0");
            } else {
                maps.put("redEnvelopeAmount", inputPocketEdit.getText().toString());
            }
        } else {
            maps.put("redEnvelopeAmount", "0");
        }

        //使用的整体出售金额
        maps.put("wholeSalesAmount", wholeSalesAmount);
        maps.put("productId", productId);
        maps.put("payType", patType);
        if (patType == 0) {
            maps.put("cardId", bankId);
        }
        maps.put("isShow", 1);
        maps.put("transNo", transNo);
        L.log("支付1money:" + trueMoney);
        scanRepay_p.reqFenQiRepqy(maps);
    }

    /**
     * 弹出输入支付（PIN）密码弹窗
     */
    public void showPayPwdDialog() {
/*        RepayPawDialog repayPawDialog = new RepayPawDialog(context);
        repayPawDialog.setOnRepayPwd(new RepayPawDialog.OnRepayPwd() {
            @Override
            public void inputPwd(String password) {
                Map<String, Object> maps = new HashMap<>();
                maps.put("payPassword", password);
                checkPayPassword_p.checkPayPassword(maps);
            }
        });
        repayPawDialog.show();*/

        EnterPayPinDialog enterPayPinDialog = new EnterPayPinDialog(context, merchantNames, next.getText().toString().substring(4));
        enterPayPinDialog.setOnRepayPwd(new EnterPayPinDialog.OnRepayPwd() {
            @Override
            public void back() {

            }

            @Override
            public void inputPwd(String password) {
                Map<String, Object> maps = new HashMap<>();
                maps.put("payPassword", password);
                checkPayPassword_p.checkPayPassword(maps);
            }

            @Override
            public void inputFinish() {

            }
        });
        enterPayPinDialog.ShowDialog();
    }

    @Override
    public void setFenQiData(FenQiRepayDatas fenQiRepayDatas) {

    }

    @Override
    public void setFenQiLimitData(FenQiRepayDatas fenQiRepayDatas) {
        String credit = "0";
        String quoto = "0";
        String limitMoney;
        String trueMoney;
        if (fenQiRepayDatas.getAvailableCredit().equals("")) {
            credit = "0";
        } else {
            credit = fenQiRepayDatas.getAvailableCredit();
        }
        if (fenQiRepayDatas.getTemporaryQuota().equals("")) {
            quoto = "0";
        } else {
            quoto = fenQiRepayDatas.getTemporaryQuota();
        }
        limitMoney = CalcTool.add(credit, quoto);
        trueMoney = next.getText().toString().substring(5);
        if (Float.parseFloat(limitMoney) != 0 && Float.parseFloat(limitMoney) < Float.parseFloat(trueMoney)) {
            Toast.makeText(context, context.getString(R.string.ins_balance), Toast.LENGTH_SHORT).show();
        } else {

        }
    }

    @Override
    public void setFenQiLimitData1(FenQiRepayDatas fenQiRepayDatas) {

    }

    @Override
    public void setRepayStatus(String status, String data) {

    }

    @Override
    public void setFenQiRepayStatus(String status) {
        try {
            JSONObject jsonObject = new JSONObject(status);
            String list = jsonObject.getString("list");
            Gson gson = new Gson();
            List<RepayFQDatas> listData = gson.fromJson(list, new TypeToken<List<RepayFQDatas>>() {
            }.getType());
            if (listData.size() == 0) {
                Toast.makeText(context, "No repayment plan", Toast.LENGTH_SHORT).show();
            } else {
                //payMethod.setText(String.format("Pay Later with %s instalments", listData.size()));
                instalmentsListDialg = new InstalmentsListDialg(context, ConfirmPayBottomDialog.this, listData, next.getText().toString().substring(5));
                instalmentsListDialg.show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "No repayment plan", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setQrPayStatus(String status) {
        apiToken = null;
        if (status == null) {
            /*onPayFail.payFail();
            Intent intent = new Intent(context, PaymentResultActivity.class);
            intent.putExtra("payResult", "fail");
            dialog.dismiss();
            context.startActivity(intent);*/
            return;
        }
        sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
        sharePreferenceUtils.put(StaticParament.PAY_SUCCESS, "1");
        sharePreferenceUtils.save();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }

       /* resultDate = new Date(System.currentTimeMillis());
        long diff = resultDate.getTime() - payDate.getTime();
        Log.i("wuhao","支付耗时：" + String.valueOf(diff));
        if (diff <= DIFF_TIME) {
            if (handler != null && runnable != null) {
                handler.removeCallbacks(runnable);
            }
        }*/

        //交易结果 0:处理中 1：成功 2：失败
        String resultState = "";
        String borrowCode = "";
        String createdDate;
        String errorMessage = "";
        if (status != null) {
            try {
                JSONObject jsonObject = new JSONObject(status);
                resultState = jsonObject.getString("resultState");
                if (jsonObject.has("id")) {
                    borrowCode = jsonObject.getString("id");
                }
                if (jsonObject.has("errorMessage")) {
                    errorMessage = jsonObject.getString("errorMessage");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (resultState.equals("2")) {
                if (!TextUtils.isEmpty(posTransNo)) {
                    onPayFail.payFail(errorMessage, true);
                } else {
                    onPayFail.payFail(errorMessage, false);
                }
                paymentWaitingDialog.setResult("PayFail", errorMessage);
                /*if (diff <= DIFF_TIME) {
                    Intent intent = new Intent(context, PaymentResultActivity.class);
                    intent.putExtra("payResult", "fail");
                    intent.putExtra("message", errorMessage);
                    dialog.dismiss();
                    context.startActivity(intent);
                } else {
                    final String finalErrorMessage = errorMessage;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //考虑页面间跳转损耗150毫秒左右，延迟300毫秒发广播，避免跳转时发送广播下个页面收不到
                            Log.i("wuhao", "发送支付结果广播");
                            ListenerManager.getInstance().sendBroadCast("PayFail", finalErrorMessage);
                        }
                    },300);
                }*/

            } else if (resultState.equals("0")) {
                if (!TextUtils.isEmpty(posTransNo)) {
                    onPayFail.noResult(errorMessage, true);
                } else {
                    onPayFail.noResult(errorMessage, false);
                }
                paymentWaitingDialog.setResult("PayNoResult", errorMessage);
            } else {
                paymentWaitingDialog.setResult("PaySuccess", borrowCode);
                /*if (diff <= DIFF_TIME) {
                    Intent intent = new Intent(context, PaymentResultActivity.class);
                    intent.putExtra("payResult", "success");
                    intent.putExtra("id", borrowCode);
                    dialog.dismiss();
                    context.startActivity(intent);
                } else {
                    final String finalBorrowCode = borrowCode;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //考虑页面间跳转损耗150毫秒左右，延迟300毫秒发广播，避免跳转时发送广播下个页面收不到
                            Log.i("wuhao", "发送支付结果广播");
                            ListenerManager.getInstance().sendBroadCast("PaySuccess", finalBorrowCode);
                        }
                    },300);
                }*/
            }
        }
    }

    public void showError(String str) {
        if (!TextUtils.isEmpty(posTransNo)) {
            onPayFail.payFail(str, true);
        } else {
            onPayFail.payFail(str, false);
        }
    }

    public void showNoResult(String str) {
        if (!TextUtils.isEmpty(posTransNo)) {
            onPayFail.noResult(str, true);
        } else {
            onPayFail.noResult(str, false);
        }
    }

    @Override
    public void unOpenFQzhekou(String zhekou) {
    }

    @Override
    public void setPasswordStatus(String status) {
        if (status == null) {
            return;
        }
        if (instalmentsListDialg != null) {
            instalmentsListDialg.disMiss();
        }
        /*SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        payDate = new Date(System.currentTimeMillis());
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                Intent intent = new Intent(context, PaymentWaitingAcivity.class);
                intent.putExtra("transNo", transNo);
                Date result = new Date(System.currentTimeMillis());
                Log.i("wuhao","跳转前：" + String.valueOf(result));
                context.startActivity(intent);
            }
        };
        handler.postDelayed(runnable, DIFF_TIME);*/
        fenQiRepay();
    }

    /**
     * 分期支付
     */
    private void fenQiRepay() {
        Map<String, Object> maps = new HashMap<>();
        if (!TextUtils.isEmpty(apiToken)) {
            maps.put("apiToken", apiToken);
        }
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
        maps.put("payUserId", sharePreferenceUtils.get(StaticParament.USER_ID, ""));
        maps.put("recUserId", recUserId);
        maps.put("merchantId", merchantId);
        //交易金额
        maps.put("transAmount", transAmount);
        //实付金额
        trueMoney = next.getText().toString().substring(5);
        maps.put("trulyPayAmount", trueMoney);
        //使用的红包金额
        if (select_pocket.getTag().equals("choice")) {
            if (TextUtils.isEmpty(inputPocketEdit.getText().toString())) {
                maps.put("redEnvelopeAmount", "0");
            } else {
                maps.put("redEnvelopeAmount", inputPocketEdit.getText().toString());
            }
        } else {
            maps.put("redEnvelopeAmount", "0");
        }

        //使用的整体出售金额
        maps.put("wholeSalesAmount", wholeSalesAmount);
        maps.put("productId", productId);
        maps.put("payType", patType);
        if (patType == 0) {
            maps.put("cardId", bankId);
        }
        maps.put("isShow", 0);
        maps.put("transNo", transNo);
        L.log("支付1money:" + trueMoney);
        resAFEvent(maps);
        if (paymentWaitingDialog == null) {
            if (TextUtils.isEmpty(posTransNo)) {
                paymentWaitingDialog = new PaymentWaitingDialog(this,context, transNo, "false");
            } else {
                paymentWaitingDialog = new PaymentWaitingDialog(this,context, transNo, "true");

            }
        }
        paymentWaitingDialog.show();
        scanRepay_p.reqQrPqyNew(maps);
        /*if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(context);
        }
        loadingDialog.show(true);*/
    }

    public void setBankList(List<BankDatas> list) {
        if (list == null) {
            TiShiDialog tiShiDialog = new TiShiDialog(context);
            tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                @Override
                public void GuanBi() {
                    dialog.dismiss();
                }
            });
            tiShiDialog.ShowDialog("Poor network connection. Please try again..");
            return;
        }
        // 反转lists
        //Collections.reverse(list);
        this.list = list;
        if (list.size() > 0) {
            setBankData(list.get(0).getCardNo(), list.get(0).getId(), list.get(0).getCustomerCcType());
        }
    }

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
        eventValue.put(BzEventParameterName.RECEIPT_ID, merchantId);
        //eventValue.put("merchantId", merchantId);
//        AppsFlyerLib.getInstance().logEvent(context, AFInAppEventType.PURCHASE, eventValue);

        Braze.getInstance(context).getCurrentUser()
                .setCustomUserAttribute(BzEventType.PURCHASE, eventValue.toString());
    }

    public interface OnitemClick {
        void itemClick();
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }

    public interface OnPayFail {
        void payFail(String errorMessage, boolean isPos);
        void noResult(String errorMessage, boolean isPos);
    }

    public OnPayFail onPayFail;

    public void setOnPayFail(OnPayFail onPayFail) {
        this.onPayFail = onPayFail;
    }

}
