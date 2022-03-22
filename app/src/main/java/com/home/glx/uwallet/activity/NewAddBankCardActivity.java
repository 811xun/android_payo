package com.home.glx.uwallet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.BankDatas;
import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.datas.FenqifuStatue;
import com.home.glx.uwallet.datas.GetChoiceDatas;
import com.home.glx.uwallet.fragment.FirstFragment;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.mvp.GetChoiceList_in;
import com.home.glx.uwallet.mvp.GetChoiceList_p;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.selfview.TiShiDialogTwo;
import com.home.glx.uwallet.tools.AddMaidian;
import com.home.glx.uwallet.tools.ChangeLineEdittext;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class NewAddBankCardActivity extends MainActivity implements View.OnClickListener,
        GetChoiceList_in.View {
    private int payType;
    private int fromList;
    private int fromSelect;
    private int fromAddCard;
    private int fromCreateFenqi;
    private ImageView back;
    private Button mBtn;
    private TextInputLayout til_id_cardno;
    private ChangeLineEdittext cardNo;

    private TextInputLayout til_id_cvv;
    private ChangeLineEdittext et_id_cvv;

    private GetChoiceList_p choiceList_p;
    private List<ChoiceDatas> htmlList = new ArrayList<>();
    private String end_month, end_year;
    private CheckBox check_up;
    private TextView proText_up;

    private CheckBox check1;
    private TextView proText;
    private BankDatas bankData;
    private String from;//           "cardList"//从纯粹绑卡页面跳过来 （不是分期付页面调过来）

    private TextInputLayout til_date_left;
    private EditText et_date_left;

    private TextInputLayout til_date_center;
    private EditText et_date_center;

    private EditText et_date_right;

    private boolean leftFocus = false;
    private boolean rightFocus = false;
    private ImageView iv_jindu;

    private ImageView icon;
    private RelativeLayout rl_icon;
    private boolean kaitongfenqifuRoad; //当没有绑卡时，从KycActivity_second_AddressChoose跳过来 为true
    long time = 0;

    private boolean exeFucusLeft = true;
    private String registerFrom = "0";//注册来源  0：app;1：h5

    private String creditCardState;
    private String cardState;
    private String installmentState;
    private boolean H5 = false;
    private boolean deleteCard = false;//分期付 删除卡后 需要再绑卡

//    @Override
//    public void onBackPressed() {
//        if (deleteCard) {
//            setResult(Activity.RESULT_CANCELED);
//            finish();
//        } else {
//            if (kaitongfenqifuRoad) {
//                if (getIntent().getBooleanExtra("clickXiaosuo", false) || getIntent().getBooleanExtra("oldHaveCard", false)) {
//                    finish();
//                } else {
//                    back();
//                }
//            } else {
//                if ("erxuanyi".equals(getIntent().getStringExtra("from"))) {
//                    Intent intent = new Intent(NewAddBankCardActivity.this, MainTab.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("num", 0);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    finish();
//                }
//            }
//        }
//        super.onBackPressed();
//    }

    @Override
    public int getLayout() {
        setStatusBarColor(NewAddBankCardActivity.this, R.color.white);
        return R.layout.activity_add_bankcard;
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
                check(false);
                til_id_cardno.setHintTextColor(createColorStateList(NewAddBankCardActivity.this, R.color.color_717171));

                til_id_cardno.setHint("Australian bank card number");
                cardNo.setHint("");//下面的提示文字
                cardNo.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));
            }
        });
    }

    @Override
    public void initView() {
        super.initView();
        icon = findViewById(R.id.icon);
        rl_icon = findViewById(R.id.rl_icon);
        H5 = getIntent().getBooleanExtra("H5", false);
        from = getIntent().getStringExtra("from");
        if (from == null) {
            from = "noFenqi";
        }
        deleteCard = getIntent().getBooleanExtra("deleteCard", false);

        choiceList_p = new GetChoiceList_p(this, this);
        kaitongfenqifuRoad = getIntent().getBooleanExtra("kaitongfenqifuRoad", false);
        payType = getIntent().getIntExtra("payType", 0);
        fromList = getIntent().getIntExtra("fromList", 0);
        fromAddCard = getIntent().getIntExtra("fromAddCard", 0);
        fromCreateFenqi = getIntent().getIntExtra("fromCreateFenqi", 0);
        fromSelect = getIntent().getIntExtra("fromSelect", 0);
        bankData = (BankDatas) getIntent().getSerializableExtra("bankDatas");
        back = (ImageView) findViewById(R.id.id_back);
//        back.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (deleteCard) {
//                        setResult(Activity.RESULT_CANCELED);
//                        finish();
//                    } else {
//                        if (kaitongfenqifuRoad) {
//                            if (getIntent().getBooleanExtra("clickXiaosuo", false) || getIntent().getBooleanExtra("oldHaveCard", false)) {
//                                finish();
//                            } else {
//                                back();
//                            }
//                        } else {
//                            if ("erxuanyi".equals(getIntent().getStringExtra("from"))) {
//                                Intent intent = new Intent(NewAddBankCardActivity.this, MainTab.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                intent.putExtra("num", 0);
//                                startActivity(intent);
//                                finish();
//
//                            } else {
//                                finish();
//                            }
//                        }
//                    }
//                }
//                return true;
//            }
//        });
        iv_jindu = (ImageView) findViewById(R.id.iv_jindu);

        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(NewAddBankCardActivity.this, StaticParament.USER);
        String userId1 = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
        registerFrom = (String) sharePreferenceUtils.get("registerFrom", "0");
        if (registerFrom.equals("1")) {//h5注册用户需要 访问
            Map<String, Object> maps1 = new HashMap<>();
            maps1.put("userId", userId1);
            maps1.put("stripeState", 0);//用户区分接入stripe前后的新老用户
            jiaoyanFenqifu(maps1);
        }
        mBtn = (Button) findViewById(R.id.btn_get_code);
        proText_up = findViewById(R.id.proText_1);

        if (kaitongfenqifuRoad) {
            final Map<String, Object> maps = new HashMap<>();
            maps.put("userId", userId1);
            maps.put("type", "5");//页面标识 1：二选一 2：证件类型页 3:地址页 4:个人信息填写页 5:绑卡页 6:illion默认机构页 7:illion 第一次验证页，8:illion第二次验证页 9:分期付开通等待页
            AddMaidian.addMaidian(this, maps);
            iv_jindu.setVisibility(View.VISIBLE);
            findViewById(R.id.line).setVisibility(View.GONE);

        } else {
            iv_jindu.setVisibility(View.GONE);

            findViewById(R.id.tv_payo).setVisibility(View.GONE);
            findViewById(R.id.tv_hint).setVisibility(View.GONE);
            findViewById(R.id.title).setVisibility(View.VISIBLE);
            findViewById(R.id.line).setVisibility(View.VISIBLE);
            TypefaceUtil.replaceFont(findViewById(R.id.title), "fonts/gilroy_semiBold.ttf");
            mBtn.setText("Add");

        }
        if (fromCreateFenqi == 1) {
            back.setVisibility(View.GONE);
        }
        TypefaceUtil.replaceFont(findViewById(R.id.tv_payo), "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.tv_hint), "fonts/gilroy_medium.ttf");
        et_date_left = (EditText) findViewById(R.id.et_date_left);
        et_date_center = (EditText) findViewById(R.id.et_date_center);
        til_date_center = (TextInputLayout) findViewById(R.id.til_account2);
        et_date_right = (EditText) findViewById(R.id.et_date_right);
        til_date_left = (TextInputLayout) findViewById(R.id.til_date_left);
        til_date_left.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        TypefaceUtil.replaceFont(et_date_left, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(et_date_center, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(et_date_right, "fonts/gilroy_medium.ttf");

        til_id_cardno = findViewById(R.id.til_id_cardno);
        til_id_cardno.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        cardNo = (ChangeLineEdittext) findViewById(R.id.card_no);
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

        til_id_cvv = findViewById(R.id.til_id_cvv);
        til_id_cvv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        et_id_cvv = findViewById(R.id.et_id_cvv);
        TypefaceUtil.replaceFont(et_id_cvv, "fonts/gilroy_medium.ttf");

        TypefaceUtil.replaceFont(mBtn, "fonts/gilroy_semiBold.ttf");

        check_up = (CheckBox) findViewById(R.id.id_protocols_1);

        check1 = (CheckBox) findViewById(R.id.id_protocols);
        check1.setVisibility(View.GONE);
        proText = findViewById(R.id.proText);

        //切换字体
        TypefaceUtil.replaceFont(proText_up, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(proText, "fonts/gilroy_medium.ttf");

        if (bankData != null) {
            proText.setText("Your above information is protected by PCI DSS scheme (Payment Card Industry Data Security Standard) provided by Lateral Payment Solutions Pty Ltd ACN 610 150 064.");
            findViewById(R.id.rl_id_protocols).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tv_payo)).setText("Edit card details");

            findViewById(R.id.tv_payo).setVisibility(View.VISIBLE);
            findViewById(R.id.tv_hint).setVisibility(View.VISIBLE);
            findViewById(R.id.title).setVisibility(View.GONE);
            findViewById(R.id.line).setVisibility(View.GONE);
            til_id_cvv.setVisibility(View.GONE);
            cardNo.setEnabled(false);
            cardNo.setText(bankData.getCardNo());
            mBtn.setText("Confirm");
            til_date_center.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(bankData.getCustomerCcExpmo()) && !TextUtils.isEmpty(bankData.getCustomerCcExpyr())) {
                et_date_left.setText(bankData.getCustomerCcExpmo());
                et_date_right.setText(bankData.getCustomerCcExpyr().substring(2));
            } else {
                til_date_center.setVisibility(View.GONE);
            }

//            et_date_left.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_selected));
            et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));
//            et_date_right.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_selected));
//            et_date_right.setSelection(2);
//            et_date_right.setCursorVisible(false);
            mBtn.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.mipmap.btn_orange_jianbian));
//            Map<String, Object> maps = new HashMap<>();
//            maps.put("token", bankData.getCrdStrgToken());
//            getCardDetails(maps);
        } else {//非详情时 下面展示的都一样 带有checkbox
            if (kaitongfenqifuRoad || H5) {
                proText.setText("Your above information is protected by PCI DSS scheme (Payment Card Industry Data Security Standard) provided by Lateral Payment Solutions Pty Ltd ACN 610 150 064.");

                if (getIntent().getBooleanExtra("clickXiaosuo", false) || getIntent().getBooleanExtra("oldHaveCard", false)) {//带你wallet页面的小锁（老用户 没有卡进行分期付绑卡，不用勾选协议）
                    iv_jindu.setVisibility(View.GONE);

                    findViewById(R.id.rl_id_protocols).setVisibility(View.GONE);
                } else {
                    findViewById(R.id.rl_id_protocols).setVisibility(View.VISIBLE);
                    proText_up.setText("I authorise Payo Funds Pty Ltd ACN 638 179 567\n" +
                            "and its associated party, to debit the\n" +
                            "nominated bank (card) account outlined above.");
                }
            } else {
                findViewById(R.id.rl_id_protocols).setVisibility(View.GONE);
                Map<String, Object> maps = new HashMap<>();
                String[] keys = new String[]{/*"cardType", */"county", "payNowApplication", "payNowTermsConditions", "bnpl-tac"};
                maps.put("code", keys);
                choiceList_p.loadChoiceList(maps);
            }
        }

        et_date_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_date_left.setCursorVisible(true);
                et_date_right.setCursorVisible(false);

                return false;
            }
        });
        til_date_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_date_left.setCursorVisible(true);
                et_date_right.setCursorVisible(false);

                return false;
            }
        });
        et_date_right.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        boolean aa = false;
                        String context = et_date_left.getText().toString().replace(" ", "");
                        if (context.length() == 2 && (context.charAt(0) == 0 || (context.charAt(0) != 0 && Integer.valueOf(context) > 0 && Integer.valueOf(context) <= 12))) {
                            if ((et_date_left.getText().toString().equals(" ") || TextUtils.isEmpty(et_date_left.getText().toString().trim()))) {
                                et_date_left.setText(" ");//空格的目的：提示文字在上面
                            }
                            et_date_right.setCursorVisible(true);
                            et_date_right.setFocusable(true);
                            et_date_right.setFocusableInTouchMode(true);
                            et_date_right.requestFocus();
                            et_date_left.setCursorVisible(false);
                        } else {
                            if (til_date_center.getVisibility() == View.GONE || TextUtils.isEmpty(et_date_center.getText().toString()) || exeFucusLeft) {//没有填写月份和年份 光标定在月份上。
                                time = System.currentTimeMillis();
                                et_date_right.setCursorVisible(false);
                                et_date_right.setFocusable(false);
                                et_date_right.setFocusableInTouchMode(false);
                                et_date_left.requestFocus();
                                til_date_center.setVisibility(View.VISIBLE);
                                et_date_center.setText("/");
                                et_date_left.setHint("");
                                et_date_left.setCursorVisible(true);
                                et_date_right.setCursorVisible(false);
                                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                imm.showSoftInput(et_date_left, InputMethodManager.SHOW_IMPLICIT);
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.showSoftInputFromInputMethod(et_date_left.getWindowToken(), 0);
//                            imm.toggleSoftInputFromWindow(et_date_left.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                                exeFucusLeft = false;
                                aa = false;
                            } else if (System.currentTimeMillis() - time > 500) {
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.showSoftInputFromInputMethod(et_date_right.getWindowToken(), 0);
//                            imm.toggleSoftInputFromWindow(et_date_right.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                                if ((et_date_left.getText().toString().equals(" ") || TextUtils.isEmpty(et_date_left.getText().toString().trim()))) {
                                    et_date_left.setText(" ");//空格的目的：提示文字在上面
                                }
                                et_date_right.setCursorVisible(true);
                                et_date_right.setFocusable(true);
                                et_date_right.setFocusableInTouchMode(true);
                                et_date_right.requestFocus();
                                et_date_left.setCursorVisible(false);

                                aa = false;
                            }
                        }
                        return aa;
                    }
                });
        findViewById(R.id.til_account3).setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        boolean aa = false;
                        String context = et_date_left.getText().toString().replace(" ", "");
                        if (context.length() == 2 && (context.charAt(0) == 0 || (context.charAt(0) != 0 && Integer.valueOf(context) > 0 && Integer.valueOf(context) <= 12))) {
                            if ((et_date_left.getText().toString().equals(" ") || TextUtils.isEmpty(et_date_left.getText().toString().trim()))) {
                                et_date_left.setText(" ");//空格的目的：提示文字在上面
                            }
                            et_date_right.setCursorVisible(true);
                            et_date_right.setFocusable(true);
                            et_date_right.setFocusableInTouchMode(true);
                            et_date_right.requestFocus();
                            et_date_left.setCursorVisible(false);
                        } else {
                            if (til_date_center.getVisibility() == View.GONE || TextUtils.isEmpty(et_date_center.getText().toString()) || exeFucusLeft) {//没有填写月份和年份 光标定在月份上。
                                time = System.currentTimeMillis();
                                et_date_right.setCursorVisible(false);
                                et_date_right.setFocusable(false);
                                et_date_right.setFocusableInTouchMode(false);
                                et_date_left.requestFocus();
                                til_date_center.setVisibility(View.VISIBLE);
                                et_date_center.setText("/");
                                et_date_left.setHint("");
                                et_date_left.setCursorVisible(true);
                                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                imm.showSoftInput(et_date_left, InputMethodManager.SHOW_IMPLICIT);

//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.showSoftInputFromInputMethod(et_date_left.getWindowToken(), 0);
//                            imm.toggleSoftInputFromWindow(et_date_left.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                                exeFucusLeft = false;
                                aa = false;
                            } else if (System.currentTimeMillis() - time > 500) {
                                if ((et_date_left.getText().toString().equals(" ") || TextUtils.isEmpty(et_date_left.getText().toString().trim()))) {
                                    et_date_left.setText(" ");//空格的目的：提示文字在上面
                                }
                                et_date_right.setCursorVisible(true);
                                et_date_right.setFocusable(true);
                                et_date_right.setFocusableInTouchMode(true);
                                et_date_right.requestFocus();
                                et_date_left.setCursorVisible(false);
                                aa = false;
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
                    et_date_left.setHint("");//下面的提示文字
                    et_date_center.setText("/");
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
                String context = et_date_left.getText().toString().replace(" ", "");
                if (context.length() == 2 && (context.charAt(0) == 0 || (context.charAt(0) != 0 && Integer.valueOf(context) > 0 && Integer.valueOf(context) <= 12))) {
                    if ((et_date_left.getText().toString().equals(" ") || TextUtils.isEmpty(et_date_left.getText().toString().trim()))) {
                        et_date_left.setText(" ");//空格的目的：提示文字在上面
                    }
                    et_date_right.setCursorVisible(true);
                    et_date_right.setFocusable(true);
                    et_date_right.setFocusableInTouchMode(true);
                    et_date_right.requestFocus();
                    et_date_left.setCursorVisible(false);
                }

                check(false);
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
                    ((TextInputLayout) findViewById(R.id.til_date_left)).setHintTextColor(createColorStateList(NewAddBankCardActivity.this, R.color.color_717171));

                    ((TextInputLayout) findViewById(R.id.til_date_left)).setHint("Expiry date (mm/yy)");
                    et_date_left.setHint("");//下面的提示文字
                    et_date_left.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));

                    et_date_center.setText("/");
                    et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));
                    et_date_right.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));
                }
                if (leftFocus || rightFocus) {
                    til_date_center.setVisibility(View.VISIBLE);
                    et_date_left.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_selected));
                    et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_selected));
                    et_date_right.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_selected));
                } else {//exe
                    if ((et_date_left.getText().toString().equals(" ") || TextUtils.isEmpty(et_date_left.getText().toString().trim())) && !TextUtils.isEmpty(et_date_right.getText().toString().trim())) {
                        et_date_left.setText(" ");//空格的目的：提示文字在上面
                    }

                    et_date_left.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));
                    et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));
                    et_date_right.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));

                    if ((TextUtils.isEmpty(et_date_left.getText().toString().trim())) && TextUtils.isEmpty(et_date_right.getText().toString().trim())) {
                        til_date_center.setVisibility(View.GONE);
                    }
                    if (et_date_left.getText().toString().trim().length() == 1) {//＜10 前面补零
                        et_date_left.setText("0" + et_date_left.getText().toString().trim());
                    }

                }

                if (leftFocus) {
                    if (et_date_left.getText().toString().equals(" ")) {
                        et_date_left.setText("");
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
                check(false);
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

                    ((TextInputLayout) findViewById(R.id.til_date_left)).setHintTextColor(createColorStateList(NewAddBankCardActivity.this, R.color.color_717171));

                    ((TextInputLayout) findViewById(R.id.til_date_left)).setHint("Expiry date (mm/yy)");
                    et_date_left.setHint("");//下面的提示文字
                    et_date_left.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));
                    et_date_center.setText("/");
                    et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));
                    et_date_right.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));
                }
                if (leftFocus || rightFocus) {//exe
                    til_date_center.setVisibility(View.VISIBLE);
                    et_date_left.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_selected));
                    et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_selected));
                    et_date_right.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_selected));
                    if (TextUtils.isEmpty(et_date_left.getText().toString().trim())) {
                        et_date_left.setText(" ");//空格的目的：提示文字在上面
                    }
                } else {
                    et_date_left.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));
                    et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));
                    et_date_right.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));
                    if (et_date_left.getText().toString().equals(" ") && TextUtils.isEmpty(et_date_right.getText().toString().trim())) {
                        et_date_left.setText("");
                    }
                    if ((TextUtils.isEmpty(et_date_left.getText().toString().trim())) && TextUtils.isEmpty(et_date_right.getText().toString().trim()))
                        til_date_center.setVisibility(View.GONE);
                    if (et_date_right.getText().toString().trim().length() == 1) {//＜10 前面补零
                        et_date_right.setText("0" + et_date_right.getText().toString().trim());
                    }

                }
            }
        });
        cardNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    til_id_cardno.setHintTextColor(createColorStateList(NewAddBankCardActivity.this, R.color.color_717171));

                    til_id_cardno.setHint("Australian bank card number");
                    cardNo.setHint("");//下面的提示文字
                    cardNo.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));
                } else {
                    et_date_left.setCursorVisible(false);
                    et_date_right.setCursorVisible(false);
                }
            }
        });
        et_id_cvv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    til_id_cvv.setHintTextColor(createColorStateList(NewAddBankCardActivity.this, R.color.color_717171));
                    til_id_cvv.setHint("CVV");
                    et_id_cvv.setHint("");//下面的提示文字
                    et_id_cvv.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));
                } else {
                    et_date_left.setCursorVisible(false);
                    et_date_right.setCursorVisible(false);
                    exeFucusLeft = true;
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
                til_id_cvv.setHintTextColor(createColorStateList(NewAddBankCardActivity.this, R.color.color_717171));

                til_id_cvv.setHint("CVV");
                et_id_cvv.setHint("");//下面的提示文字
                et_id_cvv.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected));
                check(false);
            }
        });
        back.setOnClickListener(this);
        mBtn.setOnClickListener(this);

/*        cardNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //因为重新排序之后setText的存在
                //会导致输入框的内容从0开始输入，这里是为了避免这种情况产生一系列问题
                if (start == 0 && count > 0) {
                    return;
                }
                String editTextContent = BankCardNoUtils.getText(cardNo);
                if (TextUtils.isEmpty(editTextContent) || TextUtils.isEmpty(lastString)) {
                    return;
                }
                editTextContent = BankCardNoUtils.addSpaceByCredit(editTextContent);
            }

            @Override
            public void afterTextChanged(Editable s) {
//                String bankCard = s.toString().trim();
//                String regex = "(.{4})";
//                bankCard = bankCard.replaceAll(regex,"$1\t\t");
//                cardNumber.setText(bankCard);

                //获取输入框中的内容,不可以去空格
                String etContent = BankCardNoUtils.getText(cardNo);
                if (TextUtils.isEmpty(etContent)) {
                    return;
                }
                //重新拼接字符串
                String newContent = BankCardNoUtils.addSpaceByCredit(etContent);
                //保存本次字符串数据
                lastString = newContent;
                //如果有改变，则重新填充
                //防止EditText无限setText()产生死循环
                if (!etContent.equals(newContent)) {
                    cardNo.setText(newContent);
                    //保证光标的位置
                    //cardNo.setSelection(deleteSelect > newContent.length() ? newContent.length() : deleteSelect);
                    cardNo.setSelection(newContent.length());
                }
                check(false);

            }
        })*/
        ;

        et_id_cvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                check(false);

            }
        });

        check_up.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check(false);
            }
        });

        Map<String, Object> maps = new HashMap<>();
        getPayGateWay(NewAddBankCardActivity.this, maps);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.id_back:
//                if (payType == 1) {
//                    Intent intent = new Intent(NewAddBankCardActivity.this, SelectPayTypeActivity.class);
//                    startActivity(intent);
//                }
//                finish();
                if (deleteCard) {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                } else {
                    if (kaitongfenqifuRoad) {
                        if (getIntent().getBooleanExtra("clickXiaosuo", false) || getIntent().getBooleanExtra("oldHaveCard", false)) {
                            finish();
                        } else {
                            back();
                        }
                    } else {
                        if ("erxuanyi".equals(getIntent().getStringExtra("from"))) {
                            new SharePreferenceUtils(this, StaticParament.USER).put(StaticParament.PAY_SUCCESS, "1").commit();//为了wallet页面刷新

                            Intent intent = new Intent(NewAddBankCardActivity.this, MainTab.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("num", 0);
                            startActivity(intent);
                            finish();

                        } else {
                            finish();
                        }
                    }
                }
                break;
            case R.id.btn_get_code:
                if (bankData != null) {//展示详情以及更改详情
                    if (!check(true)) {
                        return;
                    }
                    if (!PublicTools.isFastClick()) {
                        return;
                    }
                    TiShiDialogTwo tiShiDialog = new TiShiDialogTwo();
                    tiShiDialog.setOnShure(new TiShiDialogTwo.OnShure() {
                        @Override
                        public void shure() {
                            mBtn.setEnabled(false);
                            mBtn.setText("");
                            Animation rotateAnimation = AnimationUtils.loadAnimation(NewAddBankCardActivity.this, R.anim.rotate_anim);
                            LinearInterpolator lin = new LinearInterpolator();
                            rotateAnimation.setInterpolator(lin);
                            icon.startAnimation(rotateAnimation);
                            rl_icon.setVisibility(View.VISIBLE);
                            check_up.setEnabled(false);
                            final Map<String, Object> maps = new HashMap<>();
                            // 1、绑卡 0、绑账户
                            maps.put("type", "1");
                            maps.put("country", "1");
                            //银行卡号 卡号不能更改。
                            //maps.put("cardNo", cardNo.getText().toString().trim().replaceAll(" ", ""));
                            maps.put("cardId", bankData.getId());
                            maps.put("customerCcExpmo", et_date_left.getText().toString());//卡过期月份

                            Calendar c = Calendar.getInstance();
                            int year = c.get(Calendar.YEAR);
                            int qianTwoWeiOfYear = Integer.valueOf(String.valueOf(year).substring(0, 2));


                            maps.put("customerCcExpyr", qianTwoWeiOfYear + et_date_right.getText().toString());//卡过期年份
                            //卡类型 10、VISA, 20、MAST, 30、 SWITCH, 40、SOLO, 50、DELTA, 60、 AMEX
                            //maps.put("customerCcType", choiceBankTypeCode);
//                            maps.put("customerCcCvc", et_id_cvv.getText().toString());//安全码
                            //maps.put("no_language", 1);
                            if (type == 0)//latpay
                            {
                                maps.put("customerCcCvc", et_id_cvv.getText().toString());//安全码

                            }
                            updateLatpayCardInfo(maps);
                        }
                    });

                    tiShiDialog.show(this, "", "Please confirm to modify the card"
                            , "No", "Yes");
                } else {//添加信息
                    reqAddBankCard();
                }
                break;
        }
    }

    private boolean check(boolean check) {
        if (TextUtils.isEmpty(cardNo.getText().toString()) || cardNo.getText().toString().replaceAll(" ", "").length() < 1 || cardNo.getText().toString().replaceAll(" ", "").length() > 20) {
            mBtn.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.mipmap.btn_hui_jianbian));
            if (check) {
                cardNo.requestFocus();
                cardNo.setFocusable(true);
                cardNo.setFocusableInTouchMode(true);
                cardNo.post(new Runnable() {
                    @Override
                    public void run() {
                        til_id_cardno.setHintTextColor(createColorStateList(NewAddBankCardActivity.this, R.color.end_orange));

                        til_id_cardno.setHint("Please fill out this section");//上边的提示文字
                        cardNo.setHint("Australian bank card number");//下面的提示文字
                        cardNo.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        cardNo.requestFocus();
                    }
                });
            }
            return false;
        }
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int lastTwoWeiOfYear = Integer.valueOf(String.valueOf(year).substring(2));

        if (TextUtils.isEmpty(et_date_left.getText().toString()) || et_date_left.getText().toString().equals(" ") || et_date_left.getText().toString().equals("0") || et_date_left.getText().toString().equals("00") || Integer.valueOf(et_date_left.getText().toString().trim()) > 12
                || TextUtils.isEmpty(et_date_right.getText().toString()) || et_date_right.getText().toString().equals("0") || et_date_right.getText().toString().equals("00") || Integer.valueOf(et_date_right.getText().toString()) < lastTwoWeiOfYear) {
            if (check) {
                et_date_left.requestFocus();//提示文字谈上去
                et_date_left.setFocusable(true);
                et_date_left.setFocusableInTouchMode(true);
                et_date_left.post(new Runnable() {
                    @Override
                    public void run() {
                        til_date_left.setHintTextColor(createColorStateList(NewAddBankCardActivity.this, R.color.end_orange));
                        if (TextUtils.isEmpty(et_date_left.getText().toString()) && TextUtils.isEmpty(et_date_right.getText().toString())) {
                            et_date_left.setHint("Expiry date (mm/yy)");//下面的提示文字
                            et_date_center.setText("");
                        }
                        til_date_left.setHint("Please fill out this section");//上边的提示文字
                        et_date_left.setCursorVisible(false);//隐藏光标
                        et_date_left.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        et_date_right.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        exeFucusLeft = true;
                    }
                });
            }
            mBtn.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.mipmap.btn_hui_jianbian));
            return false;
        }
        if (bankData == null) {
            if (TextUtils.isEmpty(et_id_cvv.getText().toString()) || (et_id_cvv.getText().toString().trim().length() != 3 && et_id_cvv.getText().toString().trim().length() != 4)) {
                mBtn.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.mipmap.btn_hui_jianbian));
                if (check) {
                    exeFucusLeft = true;
                    et_id_cvv.requestFocus();
                    et_id_cvv.setFocusable(true);
                    et_id_cvv.setFocusableInTouchMode(true);
                    et_id_cvv.post(new Runnable() {
                        @Override
                        public void run() {
                            til_id_cvv.setHintTextColor(createColorStateList(NewAddBankCardActivity.this, R.color.end_orange));

                            til_id_cvv.setHint("Please fill out this section");//上边的提示文字
                            et_id_cvv.setHint("CVV");//下面的提示文字
                            et_id_cvv.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_un_selected_orange));//黄线
                            et_id_cvv.requestFocus();
                        }
                    });
                }
                return false;
            }
        }

        if (check && (!et_date_left.hasFocus() && !et_date_right.hasFocus())) {
            exeFucusLeft = true;
        }

        if (!check_up.isChecked() && (findViewById(R.id.rl_id_protocols).getVisibility() == View.VISIBLE)) {//详情时没有checkbox
            if (check) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//大于等于9.0
                    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/gilroy_medium.ttf");
                    SpannableString efr = new SpannableString(getString(R.string.please_agree_agreement));
                    efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Toast.makeText(NewAddBankCardActivity.this, efr, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewAddBankCardActivity.this, getString(R.string.please_agree_agreement), Toast.LENGTH_SHORT).show();
                }
            }
            mBtn.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.mipmap.btn_hui_jianbian));
            return false;
        }
        mBtn.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.mipmap.btn_orange_jianbian));

        return true;
    }

    /**
     * 添加银行卡
     */
    private void reqAddBankCard() {
        if (!check(true)) {
            return;
        }
        mBtn.setEnabled(false);
        mBtn.setText("");
        Animation rotateAnimation = AnimationUtils.loadAnimation(NewAddBankCardActivity.this, R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);
        icon.startAnimation(rotateAnimation);
        rl_icon.setVisibility(View.VISIBLE);
        check_up.setEnabled(false);

        /*Map<String, Object> maps = new HashMap<>();
//        1、绑卡 0、绑账户
        maps.put("type", "1");
        maps.put("country", "1");
        if (getIntent().getIntExtra("isCreditCard", 0) == 1) {
            maps.put("isCreditCard", 1);
        }
        if (registerFrom.equals("1") && cardState.equals("0") && installmentState.equals("2") && creditCardState.equals("0")) {
            maps.put("isCreditCard", 1);
        }
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
        if (kaitongfenqifuRoad) {//保存打勾
            maps.put("creditCardAgreementState", "1");
        }
        addBankAccount(maps);*/
        if (type == 0)//latpay
        {
            Map<String, Object> maps = new HashMap<>();
//        1、绑卡 0、绑账户
            maps.put("type", "1");
            maps.put("country", "1");
            if (getIntent().getIntExtra("isCreditCard", 0) == 1) {
                maps.put("isCreditCard", 1);
            }
            if (registerFrom.equals("1") && cardState.equals("0") && installmentState.equals("2") && creditCardState.equals("0")) {
                maps.put("isCreditCard", 1);
            }
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
            if (kaitongfenqifuRoad) {//保存打勾
                maps.put("creditCardAgreementState", "1");
            }
            addLatPayBankAccount(maps);
        } else if (type == 8) {
            String number = cardNo.getText().toString().trim().replaceAll(" ", "");//银行卡号
            Map<String, Object> maps = new HashMap<>();
            maps.put("last4", number.substring(number.length() - 4));
            judgmentRepetition(maps);//判断重复卡号
        }
    }

    public void addLatPayBankAccount(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(NewAddBankCardActivity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.addBankAccount(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(final String dataStr, String pc, String code) {
                mBtn.setEnabled(true);
                new SharePreferenceUtils(NewAddBankCardActivity.this, StaticParament.USER).put(StaticParament.PAY_SUCCESS, "1").commit();//为了wallet页面刷新

                if (kaitongfenqifuRoad) {
                    mBtn.setText("Next");
                } else {
                    mBtn.setText("Add");
                }
                rl_icon.setVisibility(View.GONE);
                check_up.setEnabled(true);

                L.log("添加银行账户:" + dataStr);//是cardId
                TiShiDialog tiShiDialog = new TiShiDialog(NewAddBankCardActivity.this);
                tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                    @Override
                    public void GuanBi() {
                        if (deleteCard) {
                            setResult(Activity.RESULT_OK);
                        } else {
                            if (kaitongfenqifuRoad) {//开通分期付过程中 去开通illion
                                new SharePreferenceUtils(NewAddBankCardActivity.this, StaticParament.USER).put(StaticParament.PAY_SUCCESS, "1").commit();//为了wallet页面刷新
                                Intent intent = new Intent(NewAddBankCardActivity.this, ChooseIllionActivity.class);
                                startActivity(intent);
                            } else {
                                if ("erxuanyi".equals(getIntent().getStringExtra("from")) || H5 || getIntent().getBooleanExtra("backToMain", false)) {//二选一添加卡成功后 跳转到首页
                                    new SharePreferenceUtils(NewAddBankCardActivity.this, StaticParament.USER).put(StaticParament.PAY_SUCCESS, "1").commit();//为了wallet页面刷新
                                    Intent intent = new Intent(NewAddBankCardActivity.this, MainTab.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("num", 0);
                                    startActivity(intent);
                                    return;
                                }
                                if (1 == (getIntent().getIntExtra("isCreditCard", 0))) {//从payMoneyActiity 调过来的
                              /*  Intent intent = new Intent(NewAddBankCardActivity.this, MainTab.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("num", 0);
                                startActivity(intent);*/
                                } else {
                                    if (getIntent().getBooleanExtra("clickXiaosuo", false)) {
                                        new SharePreferenceUtils(NewAddBankCardActivity.this, StaticParament.USER).put(StaticParament.PAY_SUCCESS, "1").commit();//为了wallet页面刷新

                                        Intent intent = new Intent(NewAddBankCardActivity.this, MainTab.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("num", 2);
                                        startActivity(intent);
                                    }
                                    if (getIntent().getBooleanExtra("oldHaveCard", false)) {
                                        Intent intent = new Intent(NewAddBankCardActivity.this, PayMoneyActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                    }

                                    if (fromSelect == 1) {
                                        Intent intent = new Intent(NewAddBankCardActivity.this, SelectBankCardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        setResult(Activity.RESULT_OK, intent);
                                    }
                                    if (fromAddCard == 1) {
                                        //添加完回卡列表
                                        Intent intent = new Intent(NewAddBankCardActivity.this, SelectBankCardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        setResult(Activity.RESULT_OK, intent);
                                    }
                                    if (getIntent().getBooleanExtra("viewmore", false)) {
                                        Intent intent = new Intent(NewAddBankCardActivity.this, ViewMoreActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }

                                    //从支付页面---》选择卡列表---》add card --->添加成功后应该返回到支付页面（不是到银行卡列表页面）
                                    if (getIntent().getIntExtra("fromSelectZhifuList", 0) == 1) {
                                        Intent intent = getIntent();
                                        intent.putExtra("cardNo", cardNo.getText().toString().trim().replaceAll(" ", ""));
                                        intent.putExtra("cardId", dataStr);
                                        setResult(Activity.RESULT_OK, intent);
                                    }
                                }

                            }
                        }
                        finish();
                    }
                });
                //绑卡
                tiShiDialog.ShowDialog("Successful", "Your card details has been successfully saved", "Confirm");
            }

            @Override
            public void resError(String error) {
                mBtn.setEnabled(true);
                if (kaitongfenqifuRoad) {
                    mBtn.setText("Next");
                } else {
                    mBtn.setText("Add");
                }
                rl_icon.setVisibility(View.GONE);
                check_up.setEnabled(true);
            }
        });
    }

    @Override
    public void setChoiceList(GetChoiceDatas getChoiceDatas) {
        if (getChoiceDatas == null) {
            return;
        }

        if (getChoiceDatas.getPayNowApplication() != null && getChoiceDatas.getPayNowTermsConditions() != null) {
            htmlList.addAll(getChoiceDatas.getPayNowApplication());
            htmlList.addAll(getChoiceDatas.getPayNowTermsConditions());
//            if ("stripePopwindow".equals(from)) {
            if (!kaitongfenqifuRoad) {
                findViewById(R.id.rl_id_protocols).setVisibility(View.VISIBLE);

                proText.setVisibility(View.GONE);
                check_up.setVisibility(View.VISIBLE);
                proText_up.setVisibility(View.VISIBLE);
                addressNewStyle2(proText_up);//点击更新latPay提示弹窗
            } else {
                addressNewStyle(proText_up);//xzc 开通分期付和开通银行卡的提示都是这个。
            }
//                htmlList.addAll(getChoiceDatas.getBnplTac());
//                addressNewStyle1(proText);
        }
    }

    /**
     * 协议样式修改
     *
     * @param textView
     */
    private void addressNewStyle(TextView textView) {//xzc 添加银行卡
        textView.setHighlightColor(getResources().getColor(android.R.color.transparent));
        SpannableString spannableString = new SpannableString("I have read and agreed the Pay Now Customer Application Form and Pay Now Terms and Conditions.");
//        if (SystemUtils.getSysLangusge(this).equals("zh")) {
//            //中文
//            //隐私政策
//            MyClickableSpan clickableSpan2 = new MyClickableSpan();
//            spannableString.setSpan(clickableSpan2, 7, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
//            spannableString.setSpan(colorSpan2, 7, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        } else {
        //英文
        //隐私政策
        MyClickPPableSpan clickableSpan1 = new MyClickPPableSpan();
        MyClickPPableSpan1 clickableSpan2 = new MyClickPPableSpan1();

        spannableString.setSpan(clickableSpan1, "I have read and agreed the ".length(), "I have read and agreed the Pay Now Customer Application Form".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpan2, "I have read and agreed the Pay Now Customer Application Form and ".length(), spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
        spannableString.setSpan(colorSpan1, "I have read and agreed the ".length(), "I have read and agreed the Pay Now Customer Application Form".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan2, "I have read and agreed the Pay Now Customer Application Form and ".length(), spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString);
    }

    /**
     * 协议样式修改
     *
     * @param textView
     */
    private void addressNewStyle1(TextView textView) {
        textView.setHighlightColor(getResources().getColor(android.R.color.transparent));
        SpannableString spannableString = new SpannableString("I have read and agree the Pay Later Terms and Conditions and authorise Payo Funds Pty Ltd ACN 638 179 567 and its associated party, to debit the nominated bank (card) account outlined above.");
        //英文
        //隐私政策
        MyClickPPableSpan2 clickableSpan2 = new MyClickPPableSpan2();

        spannableString.setSpan(clickableSpan2, "I have read and agree the ".length(), "I have read and agree the Pay Later Terms and Conditions".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //spannableString.setSpan(clickableSpan2, 60, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
        spannableString.setSpan(colorSpan1, "I have read and agree the ".length(), "I have read and agree the Pay Later Terms and Conditions".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //spannableString.setSpan(colorSpan2, 60, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        //设置下划线
//        spannableString.setSpan(new UnderlineSpan(), "I have read and agree the ".length(), "I have read and agree the Pay Later Terms and Conditions".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString);
    }

    /**
     * 协议样式修改
     *
     * @param textView
     */
    private void addressNewStyle2(TextView textView) {
        textView.setHighlightColor(getResources().getColor(android.R.color.transparent));
        SpannableString spannableString = new SpannableString("I have read and agreed the terms of Customer Application Form- Card and Terms and Conditions");
//        if (SystemUtils.getSysLangusge(this).equals("zh")) {
//            //中文
//            //隐私政策
//            MyClickableSpan clickableSpan2 = new MyClickableSpan();
//            spannableString.setSpan(clickableSpan2, 7, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
//            spannableString.setSpan(colorSpan2, 7, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        } else {
        //英文
        //隐私政策
        MyClickPPableSpan3 clickableSpan1 = new MyClickPPableSpan3();
        MyClickPPableSpan4 clickableSpan2 = new MyClickPPableSpan4();

        spannableString.setSpan(clickableSpan1, 36, 67, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpan2, 72, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //spannableString.setSpan(clickableSpan2, 60, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
        spannableString.setSpan(colorSpan1, 36, 67, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan2, 72, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //spannableString.setSpan(colorSpan2, 60, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        //设置下划线
//        spannableString.setSpan(new UnderlineSpan(), 36, 67, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new UnderlineSpan(), 72, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString);
    }

    class MyClickPPableSpan extends ClickableSpan {

        public MyClickPPableSpan() {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            L.log("点击");
            if (widget != null) {
                ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
            }
            Intent intent = new Intent();
            intent.setClass(NewAddBankCardActivity.this, PdfActivity.class);
            intent.putExtra("url", htmlList.get(0).getCName());
            startActivity(intent);
        }
    }

    class MyClickPPableSpan1 extends ClickableSpan {

        public MyClickPPableSpan1() {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            L.log("点击");
            ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
            Intent intent = new Intent();
            intent.setClass(NewAddBankCardActivity.this, PdfActivity.class);
            intent.putExtra("url", htmlList.get(1).getCName());
            startActivity(intent);
        }
    }

    class MyClickPPableSpan2 extends ClickableSpan {

        public MyClickPPableSpan2() {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            L.log("点击");
            ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
            Intent intent = new Intent();
            intent.setClass(NewAddBankCardActivity.this, PdfActivity.class);
            intent.putExtra("url", htmlList.get(0).getCName());
            startActivity(intent);
        }
    }

    class MyClickPPableSpan3 extends ClickableSpan {

        public MyClickPPableSpan3() {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            L.log("点击");
            ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
            Intent intent = new Intent();
            intent.setClass(NewAddBankCardActivity.this, PdfActivity.class);
            intent.putExtra("url", htmlList.get(0).getCName());
            startActivity(intent);
        }
    }

    class MyClickPPableSpan4 extends ClickableSpan {

        public MyClickPPableSpan4() {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            L.log("点击");
            ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
            Intent intent = new Intent();
            intent.setClass(NewAddBankCardActivity.this, PdfActivity.class);
            intent.putExtra("url", htmlList.get(1).getCName());
            startActivity(intent);
        }
    }

    JSONArray cardTypeList = null;

    /**
     * 判断重复
     *
     * @param maps
     */
    public void judgmentRepetition(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(NewAddBankCardActivity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.stripeCheckCardNoRedundancy(headerMap, requestBody);
                requestUtils.Call(call, null, false);
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
                                        zhichi = true;
                                        if (TextUtils.isEmpty(token.getId())) {
                                            //有待确认 如果tokenid为空怎么处理

                                        } else {
                                            //上传tokenid到服务器绑卡
                                            Map<String, Object> maps = new HashMap<>();
                                            maps.put("cardToken", token.getId());
//        1、绑卡 0、绑账户
                                            //银行卡号
                                            if (kaitongfenqifuRoad) {//保存打勾
                                                maps.put("creditCardAgreementState", "1");
                                            }
                                            addBankAccount(maps);
                                        }
                                        break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (!zhichi) {
                                mBtn.setEnabled(true);
                                if (kaitongfenqifuRoad) {
                                    mBtn.setText("Next");
                                } else {
                                    mBtn.setText("Add");
                                }
                                rl_icon.setVisibility(View.GONE);
                                check_up.setEnabled(true);
                                TiShiDialog tiShiDialog = new TiShiDialog(NewAddBankCardActivity.this);
                                tiShiDialog.ShowDialog("Only visa, MasterCard and AMEX are supported. Please try another card");
                            }
                        }

                        @Override
                        public void onError(@NonNull Exception e) {//Your card is not supported.       stripe后台返回的
                            mBtn.setEnabled(true);
                            if (kaitongfenqifuRoad) {
                                mBtn.setText("Next");
                            } else {
                                mBtn.setText("Add");
                            }
                            rl_icon.setVisibility(View.GONE);
                            check_up.setEnabled(true);

                            TiShiDialog tiShiDialog = new TiShiDialog(NewAddBankCardActivity.this);
                            tiShiDialog.ShowDialog(e.getMessage());
                        }
                    });

                } else {
                    mBtn.setEnabled(true);
                    if (kaitongfenqifuRoad) {
                        mBtn.setText("Next");
                    } else {
                        mBtn.setText("Add");
                    }
                    rl_icon.setVisibility(View.GONE);
                    check_up.setEnabled(true);

                    TiShiDialog tiShiDialog = new TiShiDialog(NewAddBankCardActivity.this);
                    tiShiDialog.ShowDialog("Bind Failed: The card has been bound.please change the card and try again", "Confirm");
                }
            }

            @Override
            public void resError(String error) {
                mBtn.setEnabled(true);
                if (kaitongfenqifuRoad) {
                    mBtn.setText("Next");
                } else {
                    mBtn.setText("Add");
                }
                rl_icon.setVisibility(View.GONE);
                check_up.setEnabled(true);
            }
        });
    }

    /**
     * 添加银行账户
     *
     * @param maps
     */
    public void addBankAccount(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(NewAddBankCardActivity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.stripeBindCardByToken(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(final String dataStr, String pc, String code) {
                new SharePreferenceUtils(NewAddBankCardActivity.this, StaticParament.USER).put(StaticParament.PAY_SUCCESS, "1").commit();//为了wallet页面刷新

                mBtn.setEnabled(true);
                if (kaitongfenqifuRoad) {
                    mBtn.setText("Next");
                } else {
                    mBtn.setText("Add");
                }
                rl_icon.setVisibility(View.GONE);
                check_up.setEnabled(true);

                L.log("添加银行账户:" + dataStr);//是cardId
                TiShiDialog tiShiDialog = new TiShiDialog(NewAddBankCardActivity.this);
                tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                    @Override
                    public void GuanBi() {
                        if (deleteCard) {
                            setResult(Activity.RESULT_OK);
                        } else {
                            if (kaitongfenqifuRoad) {//开通分期付过程中 去开通illion
                                Intent intent = new Intent(NewAddBankCardActivity.this, ChooseIllionActivity.class);
                                startActivity(intent);
                            } else {
                                if ("erxuanyi".equals(getIntent().getStringExtra("from")) || H5 || getIntent().getBooleanExtra("backToMain", false)) {//二选一添加卡成功后 跳转到首页
                                    Intent intent = new Intent(NewAddBankCardActivity.this, MainTab.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("num", 0);
                                    startActivity(intent);
                                    return;
                                }

                                if ("stripePopwindow".equals(getIntent().getStringExtra("from"))) {//从更新latpay为stripe的提示弹窗跳转过来的
                                    Intent intent_userbank_msg = new Intent(NewAddBankCardActivity.this, BankCardListActivity.class);
                                    startActivity(intent_userbank_msg);
                                    finish();
                                    return;
                                }
                                if (1 == (getIntent().getIntExtra("isCreditCard", 0))) {//从payMoneyActiity 调过来的
                              /*  Intent intent = new Intent(NewAddBankCardActivity.this, MainTab.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("num", 0);
                                startActivity(intent);*/
                                } else {
                                    if (getIntent().getBooleanExtra("clickXiaosuo", false)) {
                                        new SharePreferenceUtils(NewAddBankCardActivity.this, StaticParament.USER).put(StaticParament.PAY_SUCCESS, "1").commit();//为了wallet页面刷新
                                        Intent intent = new Intent(NewAddBankCardActivity.this, MainTab.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("num", 2);
                                        startActivity(intent);
                                    }
                                    if (getIntent().getBooleanExtra("oldHaveCard", false)) {
                                        Intent intent = new Intent(NewAddBankCardActivity.this, PayMoneyActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                    }

                                    if (fromSelect == 1) {
                                        Intent intent = new Intent(NewAddBankCardActivity.this, SelectBankCardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        setResult(Activity.RESULT_OK, intent);
                                    }
                                    if (fromAddCard == 1) {
                                        //添加完回卡列表
                                        Intent intent = new Intent(NewAddBankCardActivity.this, SelectBankCardActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        setResult(Activity.RESULT_OK, intent);
                                    }
                                    if (getIntent().getBooleanExtra("viewmore", false)) {
                                        Intent intent = new Intent(NewAddBankCardActivity.this, ViewMoreActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }

                                    //从支付页面---》选择卡列表---》add card --->添加成功后应该返回到支付页面（不是到银行卡列表页面）
                                    if (getIntent().getIntExtra("fromSelectZhifuList", 0) == 1) {
                                        Intent intent = getIntent();
                                        intent.putExtra("cardNo", cardNo.getText().toString().trim().replaceAll(" ", ""));
                                        intent.putExtra("cardId", dataStr);
                                        setResult(Activity.RESULT_OK, intent);
                                    }
                                }

                            }
                        }
                        finish();
                    }
                });
                //绑卡
                tiShiDialog.ShowDialog("Successful", "Your card details has been successfully saved", "Confirm");
            }

            @Override
            public void resError(String error) {
                mBtn.setEnabled(true);
                if (kaitongfenqifuRoad) {
                    mBtn.setText("Next");
                } else {
                    mBtn.setText("Add");
                }
                rl_icon.setVisibility(View.GONE);
                check_up.setEnabled(true);
            }
        });
    }

    /**
     * 获取银行卡详情
     *
     * @param maps
     */
    public void getCardDetails(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(NewAddBankCardActivity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("获取银行卡详情参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.getCardDetails(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("获取银行卡详情:" + dataStr);
                String customerCcExpyr = "";
                String customerCcExpmo = "";
//                String cvv = "";
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataStr);
                    customerCcExpyr = jsonObject.getString("customerCcExpyr");
                    customerCcExpmo = jsonObject.getString("customerCcExpmo");
//                    cvv = jsonObject.getString("customerCcExpmo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(customerCcExpyr) && !TextUtils.isEmpty(customerCcExpmo)) {
                    end_month = customerCcExpmo;
                    end_year = customerCcExpyr.substring(2);
                    et_date_left.setText(end_month);
                    et_date_right.setText(end_year);
                }
                til_date_center.setVisibility(View.VISIBLE);
                et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivity.this, R.drawable.edt_bg_selected));
                et_date_right.setSelection(2);
                et_date_right.setCursorVisible(false);
//                et_id_cvv.setText(cvv);

            }


            @Override
            public void resError(String error) {

            }
        });
    }

    /**
     * 更新卡信息
     *
     * @param maps
     */
    public void updateLatpayCardInfo(Map<String, Object> maps) {

        et_date_left.setEnabled(false);
        et_date_right.setEnabled(false);

        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(NewAddBankCardActivity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("更新卡信息参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.updateLatpayCardInfo(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {

                et_date_left.setEnabled(true);
                et_date_right.setEnabled(true);

                mBtn.setEnabled(true);
                mBtn.setText("Confirm");
                check_up.setEnabled(true);

                rl_icon.setVisibility(View.GONE);
                String state = "11";
                TiShiDialog tiShiDialog = new TiShiDialog(NewAddBankCardActivity.this);
                tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                    @Override
                    public void GuanBi() {
                        //if (fromList == 1) {
                        Intent intent = new Intent(NewAddBankCardActivity.this, BankCardListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        //}
                        finish();
                    }
                });
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataStr);
                    state = jsonObject.getString("state");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //绑卡
                if (state.equals("11")) {
                    TiShiDialog tiShiDialog1 = new TiShiDialog(NewAddBankCardActivity.this);
                    tiShiDialog1.ShowDialog("Card updated failed", "Confirm");
                } else {
                    tiShiDialog.ShowDialog("Card updated successfully", "Confirm");
                }
            }

            @Override
            public void resError(String error) {
                mBtn.setEnabled(true);
                mBtn.setText("Confirm");
                rl_icon.setVisibility(View.GONE);
                check_up.setEnabled(true);

                et_date_left.setEnabled(true);
                et_date_right.setEnabled(true);
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //do something.
            //禁用返回键
            if (fromCreateFenqi == 1) {
                return true;
            } else {
                if (payType == 1) {
                    Intent intent = new Intent(NewAddBankCardActivity.this, SelectPayTypeActivity.class);
                    startActivity(intent);
                    finish();
                }
                return super.dispatchKeyEvent(event);
            }
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    private void back() {
        if (getIntent().getBooleanExtra("fromFirstFragment", false)) {
            finish();
        } else if (FirstFragment.backStatue == 1) {
            Intent intent = new Intent(NewAddBankCardActivity.this, MainTab.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("num", 0);
            startActivity(intent);
            finish();

        } else if (FirstFragment.backStatue == 2) {
            Intent it = new Intent(NewAddBankCardActivity.this, MainTab.class);
            it.putExtra("num", 2);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
            startActivity(it);
            finish();
        } else if (FirstFragment.backStatue == 3) {
            Intent intent = new Intent(NewAddBankCardActivity.this, PayMoneyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else if (FirstFragment.backStatue == 4) {
            Intent intent = new Intent(NewAddBankCardActivity.this, PayFailedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    /*  @Override
      public boolean onKeyDown(int keyCode, KeyEvent event) {
          if (keyCode == KeyEvent.KEYCODE_BACK*//* && event.getAction() == KeyEvent.ACTION_UP*//*) {

            back.performClick();
        }
        return true;
    }*/
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            back.performClick();

        }
        //继续执行父类其他点击事件
        return super.onKeyUp(keyCode, event);
    }

    private static ColorStateList createColorStateList(Context context, int color) {
        int[] colors = new int[]{ContextCompat.getColor(context, color), ContextCompat.getColor(context, color)};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_checked};
        states[1] = new int[]{-android.R.attr.state_checked};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    /**
     * 分期付开通状态
     *
     * @param maps
     */
    private void jiaoyanFenqifu(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(NewAddBankCardActivity.this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {
                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.jiaoyanFenqifu(headerMap, requestBody);
                requestUtils.Call(call/*, null, false*/);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                Gson gson = new Gson();
                FenqifuStatue fenqifuStatue = gson.fromJson(dataStr, FenqifuStatue.class);
                creditCardState = String.valueOf(fenqifuStatue.getCreditCardState());//0：新用户未绑卡 1：已绑卡  2：老用户未绑卡
                cardState = String.valueOf(fenqifuStatue.getCardState());//0：未绑卡 1：已绑卡
                installmentState = String.valueOf(fenqifuStatue.getInstallmentState());//2 分期付开通 ；！2分期付没有开通
                //判断卡或者分期状态
                Log.d("xunzhic", "resData: " + dataStr);
            }

            @Override
            public void resError(String error) {

            }
        });
    }

    private int type;

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
                    type = new JSONObject(dataStr).getInt("type");
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
