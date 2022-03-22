package com.home.glx.uwallet.activity.xzc;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.GetChoiceDatas;
import com.home.glx.uwallet.mvp.GetChoiceList_in;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

//
public class NewAddBankCardActivityDialog extends MainActivity implements View.OnClickListener,
        GetChoiceList_in.View {
    private ImageView back;
    private Button mBtn;
    private TextInputLayout til_id_cardno;
    private ChangeLineEdittext cardNo;

    private TextInputLayout til_id_cvv;
    private ChangeLineEdittext et_id_cvv;

    private TextView proText;

    private TextInputLayout til_date_left;
    private EditText et_date_left;

    private TextInputLayout til_date_center;
    private EditText et_date_center;

    private EditText et_date_right;

    private boolean leftFocus = false;
    private boolean rightFocus = false;

    private ImageView icon;
    private RelativeLayout rl_icon;
    long time = 0;

    private boolean exeFucusLeft = true;

    private ImageView cardLogo;
//    private ImageView cardLogo_3;

    @Override
    public int getLayout() {
        return R.layout.activity_add_bankcard_dialog;
    }

    /**
     * 设置监听事件
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
                til_id_cardno.setHintTextColor(createColorStateList(NewAddBankCardActivityDialog.this, R.color.color_717171));

                til_id_cardno.setHint("Australian bank card number");
                cardNo.setHint("");//下面的提示文字
                cardNo.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));
//                if (s.toString().trim().replaceAll(" ", "").length() < 16) {
//                }
                cardLogo.setVisibility(View.GONE);
//                cardLogo_3.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void initView() {
        super.initView();
        icon = findViewById(R.id.icon);
        rl_icon = findViewById(R.id.rl_icon);


        back = (ImageView) findViewById(R.id.id_back);

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
        spaceDivide(cardNo);
        TypefaceUtil.replaceFont(cardNo, "fonts/gilroy_medium.ttf");
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

        mBtn = (Button) findViewById(R.id.btn_get_code);
        TypefaceUtil.replaceFont(mBtn, "fonts/gilroy_semiBold.ttf");

        proText = findViewById(R.id.proText);
        cardLogo = findViewById(R.id.cardLogo);
//        cardLogo_3 = findViewById(R.id.cardLogo_3);


        //切换字体
        TypefaceUtil.replaceFont(proText, "fonts/gilroy_medium.ttf");
//        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");

        proText.setText("Your above information is protected by PCI DSS scheme (Payment Card Industry Data Security Standard) provided by Lateral Payment Solutions Pty Ltd ACN 610 150 064.");

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
                String editTextContent = cardNo.getText().toString();
                if (TextUtils.isEmpty(editTextContent) || TextUtils.isEmpty(lastString)) {
                    return;
                }
                editTextContent = addSpeaceByCredit(editTextContent);
                //如果最新的长度 < 上次的长度，代表进行了删除
                if (editTextContent.length() <= lastString.length()) {
                    add = false;
                    Log.d("skkkk", "onTextChanged: 删除");
                    deleteSelect = start;
                } else {
                    add = true;

                    Log.d("skkkk", "onTextChanged: 添加");
                    Log.d("skkkk", "start11:    " + start);
                    Log.d("skkkk", "start:22:   " + cardNo.getText().toString().substring(0, start).replace(" ", ""));
                    Log.d("skkkk", "start:33:   " + (cardNo.getText().toString().substring(0, start).replace(" ", "").length()));
//                    deleteSelect = editTextContent.length();
//                    deleteSelect = (cardNo.getText().toString().substring(0, start).replace(" ", "").length()) / 4 == 0 ? start + 2 : start + 1;
                    deleteSelect = start + 1;
                    Log.d("skkkk", "start:33:   " + deleteSelect);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                til_id_cardno.setHintTextColor(createColorStateList(NewAddBankCardActivityDialog.this, R.color.color_717171));

                til_id_cardno.setHint("Australian bank card number");
                cardNo.setHint("");//下面的提示文字
                cardNo.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));

                //获取输入框中的内容,不可以去空格
                String etContent = cardNo.getText().toString();

                //重新拼接字符串
                String newContent = addSpeaceByCredit(etContent);
                //保存本次字符串数据
                //如果有改变，则重新填充
                //防止EditText无限setText()产生死循环
                lastString = newContent;
                if (!etContent.equals(newContent)) {
                    cardNo.setText(newContent);
                    //保证光标的位置
                    if (add) {
                        String sub = cardNo.getText().toString().substring(0, deleteSelect);
                        if (sub.substring(sub.length() - 1, sub.length()).equals(" ")) {
                            cardNo.setSelection(deleteSelect + 1);

                        } else {
                            cardNo.setSelection(deleteSelect);

                        }
                    } else {
                        cardNo.setSelection(deleteSelect > newContent.length() ? newContent.length() : deleteSelect);

                    }
//                    deleteSelect = (cardNo.getText().toString().substring(0, start).replace(" ", "").length()) / 4 == 0 ? start + 2 : start + 1;

                }


                check(false);

            }
        })*/
        ;

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
                    ((TextInputLayout) findViewById(R.id.til_date_left)).setHintTextColor(createColorStateList(NewAddBankCardActivityDialog.this, R.color.color_717171));

                    ((TextInputLayout) findViewById(R.id.til_date_left)).setHint("Expiry date (mm/yy)");
                    et_date_left.setHint("");//下面的提示文字
                    et_date_left.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));

                    et_date_center.setText("/");
                    et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));
                    et_date_right.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));
                }
                if (leftFocus || rightFocus) {
                    til_date_center.setVisibility(View.VISIBLE);
                    et_date_left.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_selected));
                    et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_selected));
                    et_date_right.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_selected));
                } else {//exe
                    if ((et_date_left.getText().toString().equals(" ") || TextUtils.isEmpty(et_date_left.getText().toString().trim())) && !TextUtils.isEmpty(et_date_right.getText().toString().trim())) {
                        et_date_left.setText(" ");//空格的目的：提示文字在上面
                    }

                    et_date_left.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));
                    et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));
                    et_date_right.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));
                   /* if ((et_date_left.getText().toString().equals(" ") || TextUtils.isEmpty(et_date_left.getText().toString().trim())) && TextUtils.isEmpty(et_date_right.getText().toString().trim())) {
                        et_date_left.setText("");
                    }*/
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

                   /* if (!TextUtils.isEmpty(et_date_right.getText().toString()) && et_date_left.getText().toString().equals(" ")) {
                        et_date_left.setText("");
                    }*/

                    ((TextInputLayout) findViewById(R.id.til_date_left)).setHintTextColor(createColorStateList(NewAddBankCardActivityDialog.this, R.color.color_717171));

                    ((TextInputLayout) findViewById(R.id.til_date_left)).setHint("Expiry date (mm/yy)");
                    et_date_left.setHint("");//下面的提示文字
                    et_date_left.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));
                    et_date_center.setText("/");
                    et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));
                    et_date_right.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));
                }
                if (leftFocus || rightFocus) {//exe
                    til_date_center.setVisibility(View.VISIBLE);
                    et_date_left.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_selected));
                    et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_selected));
                    et_date_right.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_selected));
                    if (TextUtils.isEmpty(et_date_left.getText().toString().trim())) {
                        et_date_left.setText(" ");//空格的目的：提示文字在上面
                    }
                } else {
//                    exeFucusLeft = true;

                    et_date_left.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));
                    et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));
                    et_date_right.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));
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

        et_id_cvv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    til_id_cvv.setHintTextColor(createColorStateList(NewAddBankCardActivityDialog.this, R.color.color_717171));

                    til_id_cvv.setHint("CVV");
                    et_id_cvv.setHint("");//下面的提示文字
                    et_id_cvv.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));
                } else {
                    exeFucusLeft = true;
                    et_date_left.setCursorVisible(false);
                    et_date_right.setCursorVisible(false);
                    et_id_cvv.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_selected));

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
                til_id_cvv.setHintTextColor(createColorStateList(NewAddBankCardActivityDialog.this, R.color.color_717171));

                til_id_cvv.setHint("CVV");
                et_id_cvv.setHint("");//下面的提示文字
                et_id_cvv.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));
                check(false);
            }
        });
        back.setOnClickListener(this);
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
                return true;
            }
        });
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
        setTitle("");

        cardNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (cardNo.getText().toString().trim().replaceAll(" ", "").length() >= 16) {
                        Map<String, Object> maps = new HashMap<>();
                        maps.put("cardBin", cardNo.getText().toString().trim().replaceAll(" ", ""));
                        latpayGetCardType(maps);
                    } else {
                        cardLogo.setVisibility(View.GONE);
//                        cardLogo_3.setVisibility(View.GONE);
                    }
                    til_id_cardno.setHintTextColor(createColorStateList(NewAddBankCardActivityDialog.this, R.color.color_717171));

                    til_id_cardno.setHint("Australian bank card number");
                    cardNo.setHint("");//下面的提示文字
                    cardNo.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));
                } else {
                    et_date_left.setCursorVisible(false);
                    et_date_right.setCursorVisible(false);
                    cardNo.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_selected));

                }
            }
        });

        Map<String, Object> maps = new HashMap<>();
        getPayGateWay(NewAddBankCardActivityDialog.this, maps);
    }

    public void latpayGetCardType(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(this, maps, StaticParament.zhifu, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(NewAddBankCardActivityDialog.this, StaticParament.USER);
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
                    String choiceBankTypeCode = jsonObject.getString("cardType");
                    if (choiceBankTypeCode.equals("10")) {//切图大小不一样 导致的
                        cardLogo.setVisibility(View.VISIBLE);
//                        cardLogo_3.setVisibility(View.GONE);
                        cardLogo.setImageResource(R.mipmap.pay_visa_logo_new);
                    } else if (choiceBankTypeCode.equals("20")) {
                        cardLogo.setVisibility(View.VISIBLE);
//                        cardLogo_3.setVisibility(View.GONE);

                        cardLogo.setImageResource(R.mipmap.pay_master_logo_new);
                    } else if (choiceBankTypeCode.equals("60")) {
                        cardLogo.setVisibility(View.VISIBLE);
                        cardLogo.setImageResource(R.mipmap.pay_onther_logo_new);

//                        cardLogo.setVisibility(View.GONE);
//                        cardLogo_3.setVisibility(View.VISIBLE);
//                        cardLogo_3.setImageResource(R.mipmap.pay_onther_logo);
                    } else {
                        cardLogo.setVisibility(View.INVISIBLE);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.id_back:
                setResult(Activity.RESULT_CANCELED);
                finish();
                break;
            case R.id.btn_get_code:
                reqAddBankCard();
                break;
        }
    }

    private boolean check(boolean check) {
        if (TextUtils.isEmpty(cardNo.getText().toString()) || cardNo.getText().toString().replaceAll(" ", "").length() < 1 || cardNo.getText().toString().replaceAll(" ", "").length() > 20) {
            mBtn.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.mipmap.btn_hui_jianbian));
            if (check) {
                cardNo.requestFocus();
                cardNo.setFocusable(true);
                cardNo.setFocusableInTouchMode(true);
                cardNo.post(new Runnable() {
                    @Override
                    public void run() {
                        til_id_cardno.setHintTextColor(createColorStateList(NewAddBankCardActivityDialog.this, R.color.end_orange));

                        til_id_cardno.setHint("Please fill out this section");//上边的提示文字
                        cardNo.setHint("Australian bank card number");//下面的提示文字
                        cardNo.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        cardNo.requestFocus();
                    }
                });
            }
            return false;
        }
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int lastTwoWeiOfYear = Integer.valueOf(String.valueOf(year).substring(2));

        if (TextUtils.isEmpty(et_date_left.getText().toString()) || et_date_left.getText().toString().equals(" ") || et_date_left.getText().toString().equals("0") || et_date_left.getText().toString().equals("00") || Integer.valueOf(et_date_left.getText().toString()) > 12
                || TextUtils.isEmpty(et_date_right.getText().toString()) || et_date_right.getText().toString().equals("0") || et_date_right.getText().toString().equals("00") || Integer.valueOf(et_date_right.getText().toString()) < lastTwoWeiOfYear) {
            if (check) {
                et_date_left.requestFocus();//提示文字谈上去
                et_date_left.setFocusable(true);
                et_date_left.setFocusableInTouchMode(true);
                et_date_left.post(new Runnable() {
                    @Override
                    public void run() {
                        til_date_left.setHintTextColor(createColorStateList(NewAddBankCardActivityDialog.this, R.color.end_orange));
                        if (TextUtils.isEmpty(et_date_left.getText().toString()) && TextUtils.isEmpty(et_date_right.getText().toString())) {
                            et_date_left.setHint("Expiry date (mm/yy)");//下面的提示文字
                            et_date_center.setText("");
                        }
                        til_date_left.setHint("Please fill out this section");//上边的提示文字
                        et_date_left.setCursorVisible(false);//隐藏光标
                        et_date_left.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        et_date_right.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        exeFucusLeft = true;
                    }
                });
            }
            mBtn.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.mipmap.btn_hui_jianbian));
            return false;
        }

        if (TextUtils.isEmpty(et_id_cvv.getText().toString()) || (et_id_cvv.getText().toString().trim().length() != 3 && et_id_cvv.getText().toString().trim().length() != 4)) {
            mBtn.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.mipmap.btn_hui_jianbian));
            if (check) {
                exeFucusLeft = true;
                et_id_cvv.requestFocus();
                et_id_cvv.setFocusable(true);
                et_id_cvv.setFocusableInTouchMode(true);
                et_id_cvv.post(new Runnable() {
                    @Override
                    public void run() {
                        til_id_cvv.setHintTextColor(createColorStateList(NewAddBankCardActivityDialog.this, R.color.end_orange));

                        til_id_cvv.setHint("Please fill out this section");//上边的提示文字
                        et_id_cvv.setHint("CVV");//下面的提示文字
                        et_id_cvv.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        et_id_cvv.requestFocus();
                    }
                });
            }
            return false;
        }

        if (check && (!et_date_left.hasFocus() && !et_date_right.hasFocus())) {
            exeFucusLeft = true;
        }

        mBtn.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.mipmap.btn_orange_jianbian));

        return true;
    }

    /**
     * 添加银行卡
     */
    private void reqAddBankCard() {
        if (!check(true)) {
            return;
        }
        if (!PublicTools.isFastClick()) {
            return;
        }
        mBtn.setEnabled(false);
        mBtn.setText("");
        Animation rotateAnimation = AnimationUtils.loadAnimation(NewAddBankCardActivityDialog.this, R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);
        icon.startAnimation(rotateAnimation);
        rl_icon.setVisibility(View.VISIBLE);

        /*Map<String, Object> maps = new HashMap<>();
//        1、绑卡 0、绑账户
        maps.put("type", "1");
        maps.put("country", "1");
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
        addBankAccount(maps);*/
        if (type == 0)//latpay
        {
            Map<String, Object> maps = new HashMap<>();
//        1、绑卡 0、绑账户
            maps.put("type", "1");
            maps.put("country", "1");
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
            addBankAccount(maps);

        } else if (type == 8) {

            String number = cardNo.getText().toString().trim().replaceAll(" ", "");//银行卡号
            Map<String, Object> maps = new HashMap<>();
            maps.put("last4", number.substring(number.length() - 4));
            judgmentRepetition(maps);//判断重复卡号
        }

        //获取cardtoken
//        Stripe stripe = new Stripe(getApplication(), PaymentConfiguration.getInstance(getApplication()).getPublishableKey());
//        String number = cardNo.getText().toString().trim().replaceAll(" ", "");//银行卡号
//        int expMonth = Integer.parseInt(et_date_left.getText().toString());//卡过期月份
//        int expYear = Integer.parseInt(et_date_right.getText().toString());//卡过期年份
//        String cvv = et_id_cvv.getText().toString();//安全码
//        CardParams cardParams = new CardParams(number,expMonth,expYear,cvv);
//        stripe.createCardToken(cardParams, new ApiResultCallback<Token>() {
//            @Override
//            public void onSuccess(@NonNull Token token) {
//                L.log("获取cardtoken成功 token="+token.toString());
//
//                if (TextUtils.isEmpty(token.getId())) {
//                    //有待确认 如果tokenid为空怎么处理
//
//                } else {
//                    //上传tokenid到服务器绑卡
//
//
//
//                }
//            }
//
//            @Override
//            public void onError(@NonNull Exception e) {
//                L.log("获取cardtoken失败:"+e.getMessage());
//                mBtn.setEnabled(true);
//                mBtn.setText("Add");
//                rl_icon.setVisibility(View.GONE);
//                TiShiDialog tiShiDialog = new TiShiDialog(NewAddBankCardActivityDialog.this);
//                tiShiDialog.ShowDialog(e.getMessage());
//            }
//        });
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

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(NewAddBankCardActivityDialog.this, StaticParament.USER);
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
                if (redundancyState == 0) {//无重复
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
                            L.log("获取cardtoken成功 token=" + token.toString());
                            boolean zhichi = false;
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
                                            addBankAccount(maps);
                                        }
                                        break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (!zhichi) {
                                showErrorDialog("Only visa, MasterCard and AMEX are supported. Please try another card");
                            }
                        }

                        @Override
                        public void onError(@NonNull Exception e) {
                            showErrorDialog(e.getMessage());
                        }
                    });
                } else {
                    showErrorDialog("Bind Failed: The card has been bound.please change the card and try again");
                }
            }

            @Override
            public void resError(String error) {

            }
        });
    }

    private void showErrorDialog(String errorStr) {
        findViewById(R.id.ll).setVisibility(View.VISIBLE);
        TextView idTitle = (TextView) findViewById(R.id.id_title);
        TextView queding = (TextView) findViewById(R.id.id_queding);
        TextView tex = (TextView) findViewById(R.id.id_tex);
        TextView id_cancel = (TextView) findViewById(R.id.id_cancel);

        //切换字体
        TypefaceUtil.replaceFont(idTitle, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(id_cancel, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(queding, "fonts/gilroy_medium.ttf");
        tex.setText(errorStr);
//                findViewById(R.id.rl_wai).setBackgroundColor(Color.parseColor("#D0D0D0"));
        idTitle.setVisibility(View.GONE);
        back.setEnabled(false);
        mBtn.setEnabled(false);
//                findViewById(R.id.line).setBackgroundColor(Color.parseColor("#C0C0C0"));
        et_date_center.setBackground(ContextCompat.getDrawable(NewAddBankCardActivityDialog.this, R.drawable.edt_bg_un_selected));

        cardNo.setEnabled(false);
        cardNo.setFocusable(false);
        cardNo.setFocusableInTouchMode(false);

        et_id_cvv.setEnabled(false);
        et_id_cvv.setFocusable(false);
        et_id_cvv.setFocusableInTouchMode(false);

        et_date_left.setEnabled(false);
        et_date_left.setFocusable(false);
        et_date_left.setFocusableInTouchMode(false);

        et_date_right.setEnabled(false);
        et_date_right.setFocusable(false);
        et_date_right.setFocusableInTouchMode(false);

        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        mBtn.setText("Add");
        rl_icon.setVisibility(View.GONE);
    }

    private void reset() {
        findViewById(R.id.scroll_view).setEnabled(true);
        findViewById(R.id.scroll_view).setFocusable(true);
        findViewById(R.id.scroll_view).setFocusableInTouchMode(true);
        findViewById(R.id.ll).setVisibility(View.GONE);
//                        findViewById(R.id.rl_wai).setBackgroundColor(Color.parseColor("#FFFFFF"));

        back.setEnabled(true);
        mBtn.setEnabled(true);

        cardNo.setEnabled(true);
        cardNo.setFocusable(true);
        cardNo.setFocusableInTouchMode(true);

        et_id_cvv.setEnabled(true);
        et_id_cvv.setFocusable(true);
        et_id_cvv.setFocusableInTouchMode(true);

        et_date_left.setEnabled(true);
        et_date_left.setFocusable(true);
        et_date_left.setFocusableInTouchMode(true);

        et_date_right.setEnabled(true);
        et_date_right.setFocusable(true);
        et_date_right.setFocusableInTouchMode(true);
    }

    @Override
    public void setChoiceList(GetChoiceDatas getChoiceDatas) {
        if (getChoiceDatas == null) {
            return;
        }

        if (getChoiceDatas.getPayNowApplication() != null && getChoiceDatas.getPayNowTermsConditions() != null) {
        }
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

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(NewAddBankCardActivityDialog.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = null;
                if (type == 0) {
                    call = requestInterface.addBankAccount(headerMap, requestBody);
                } else if (type == 8) {
                    call = requestInterface.stripeBindCardByToken(headerMap, requestBody);
                }
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                mBtn.setEnabled(true);
                mBtn.setText("Add");
                rl_icon.setVisibility(View.GONE);
                L.log("添加银行账户:" + dataStr);
                findViewById(R.id.ll).setVisibility(View.VISIBLE);
                TextView idTitle = (TextView) findViewById(R.id.id_title);
                TextView queding = (TextView) findViewById(R.id.id_queding);
                TextView tex = (TextView) findViewById(R.id.id_tex);
                TextView id_cancel = (TextView) findViewById(R.id.id_cancel);
//                findViewById(R.id.line).setBackgroundColor(Color.parseColor("#C0C0C0"));
                //切换字体
                TypefaceUtil.replaceFont(idTitle, "fonts/gilroy_semiBold.ttf");
                TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
                TypefaceUtil.replaceFont(id_cancel, "fonts/gilroy_medium.ttf");
                TypefaceUtil.replaceFont(queding, "fonts/gilroy_medium.ttf");
                tex.setText("Your card details has been successfully saved");
//                findViewById(R.id.rl_wai).setBackgroundColor(Color.parseColor("#D0D0D0"));
                idTitle.setVisibility(View.VISIBLE);
                idTitle.setText("Successful");
                back.setEnabled(false);
                mBtn.setEnabled(false);

                cardNo.setEnabled(false);
                cardNo.setFocusable(false);
                cardNo.setFocusableInTouchMode(false);

                et_id_cvv.setEnabled(false);
                et_id_cvv.setFocusable(false);
                et_id_cvv.setFocusableInTouchMode(false);

                et_date_left.setEnabled(false);
                et_date_left.setFocusable(false);
                et_date_left.setFocusableInTouchMode(false);

                et_date_right.setEnabled(false);
                et_date_right.setFocusable(false);
                et_date_right.setFocusableInTouchMode(false);
//                if (!TextUtils.isEmpty(close)) {
//                    queding.setText(close);
//                }
//
                queding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        findViewById(R.id.ll).setVisibility(View.INVISIBLE);
//                        findViewById(R.id.rl_wai).setBackgroundColor(Color.parseColor("#FFFFFF"));
                        back.setEnabled(true);
                        mBtn.setEnabled(true);

                        cardNo.setEnabled(true);
                        cardNo.setFocusable(true);
                        cardNo.setFocusableInTouchMode(true);

                        et_id_cvv.setEnabled(true);
                        et_id_cvv.setFocusable(true);
                        et_id_cvv.setFocusableInTouchMode(true);

                        et_date_left.setEnabled(true);
                        et_date_left.setFocusable(true);
                        et_date_left.setFocusableInTouchMode(true);

                        et_date_right.setEnabled(true);
                        et_date_right.setFocusable(true);
                        et_date_right.setFocusableInTouchMode(true);
//                        findViewById(R.id.line).setBackgroundColor(Color.parseColor("#EBEBEB"));

                        setResult(Activity.RESULT_OK);
                        finish();
                        overridePendingTransition(0, 0);
                    }
                });
            }

            @Override
            public void resError(String error) {
                findViewById(R.id.ll).setVisibility(View.VISIBLE);
                TextView idTitle = (TextView) findViewById(R.id.id_title);
                TextView queding = (TextView) findViewById(R.id.id_queding);
                TextView tex = (TextView) findViewById(R.id.id_tex);
                TextView id_cancel = (TextView) findViewById(R.id.id_cancel);

                //切换字体
                TypefaceUtil.replaceFont(idTitle, "fonts/gilroy_semiBold.ttf");
                TypefaceUtil.replaceFont(tex, "fonts/gilroy_medium.ttf");
                TypefaceUtil.replaceFont(id_cancel, "fonts/gilroy_medium.ttf");
                TypefaceUtil.replaceFont(queding, "fonts/gilroy_medium.ttf");
                tex.setText(error);
//                findViewById(R.id.rl_wai).setBackgroundColor(Color.parseColor("#D0D0D0"));
                idTitle.setVisibility(View.GONE);
                back.setEnabled(false);
                mBtn.setEnabled(false);
//                findViewById(R.id.line).setBackgroundColor(Color.parseColor("#C0C0C0"));

                cardNo.setEnabled(false);
                cardNo.setFocusable(false);
                cardNo.setFocusableInTouchMode(false);

                et_id_cvv.setEnabled(false);
                et_id_cvv.setFocusable(false);
                et_id_cvv.setFocusableInTouchMode(false);

                et_date_left.setEnabled(false);
                et_date_left.setFocusable(false);
                et_date_left.setFocusableInTouchMode(false);

                et_date_right.setEnabled(false);
                et_date_right.setFocusable(false);
                et_date_right.setFocusableInTouchMode(false);


//                if (!TextUtils.isEmpty(close)) {
//                    queding.setText(close);
//                }
//
                queding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findViewById(R.id.scroll_view).setEnabled(true);
                        findViewById(R.id.scroll_view).setFocusable(true);
                        findViewById(R.id.scroll_view).setFocusableInTouchMode(true);
                        findViewById(R.id.ll).setVisibility(View.GONE);
//                        findViewById(R.id.rl_wai).setBackgroundColor(Color.parseColor("#FFFFFF"));

                        back.setEnabled(true);
                        mBtn.setEnabled(true);

                        cardNo.setEnabled(true);
                        cardNo.setFocusable(true);
                        cardNo.setFocusableInTouchMode(true);

                        et_id_cvv.setEnabled(true);
                        et_id_cvv.setFocusable(true);
                        et_id_cvv.setFocusableInTouchMode(true);

                        et_date_left.setEnabled(true);
                        et_date_left.setFocusable(true);
                        et_date_left.setFocusableInTouchMode(true);

                        et_date_right.setEnabled(true);
                        et_date_right.setFocusable(true);
                        et_date_right.setFocusableInTouchMode(true);
//                        findViewById(R.id.line).setBackgroundColor(Color.parseColor("#EBEBEB"));
                    }
                });

                mBtn.setText("Add");
                rl_icon.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
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
     * 更改式activity在屏幕中显示的位置
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //即设定DecorView在PhoneWindow里的位置
        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;//设置宽度满屏
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindowManager().updateViewLayout(view, lp);
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
                Log.d("xzc_dubug", "埋点返回：: " + dataStr);
            }


            @Override
            public void resError(String error) {

            }
        });
    }
}
