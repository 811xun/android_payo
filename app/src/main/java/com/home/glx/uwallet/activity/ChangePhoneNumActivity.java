package com.home.glx.uwallet.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.datas.GetChoiceDatas;
import com.home.glx.uwallet.mvp.ChangeLoginPhone;
import com.home.glx.uwallet.mvp.ChangeLoginPhone_in;
import com.home.glx.uwallet.mvp.CheckPayPassword_in;
import com.home.glx.uwallet.mvp.CheckPayPassword_p;
import com.home.glx.uwallet.mvp.GetChoiceList_in;
import com.home.glx.uwallet.mvp.GetChoiceList_p;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.PwdEditText_Fenkai;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.selfview.TipDialog;
import com.home.glx.uwallet.tools.CountDownUtils;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangePhoneNumActivity extends MainActivity implements CheckPayPassword_in.View, View.OnClickListener,
        GetChoiceList_in.View, ChangeLoginPhone_in {
    private PwdEditText_Fenkai idPwsEdt;
    private PwdEditText_Fenkai idCodeEdt;
    private CheckPayPassword_p checkPayPassword_p;
    private ConstraintLayout enterPin;
    private ConstraintLayout enterPhone;
    private ConstraintLayout enterCode;
    private EditText phone;
    private ImageView back;
    private TextView next;
    private Button send;
    private TextView title;
    //    private TextView idCountryCode;
    private List<ChoiceDatas> phoneCodeList = new ArrayList<>();
    private GetChoiceList_p choiceList_p;
    private ChangeLoginPhone changeLoginPhone;
    private String newPhoneNum;
    private CountDownUtils countDownUtils;
    private TextView times;
    private TextView idGetPhoneCode;
    //    private TextView phoneStartNum;
    private long lastClickTime;

    @Override
    public int getLayout() {
        return R.layout.activity_change_phone_num;
    }

    @Override
    public void initView() {
        super.initView();
        changeLoginPhone = new ChangeLoginPhone(this, this);
        choiceList_p = new GetChoiceList_p(this, this);
        checkPayPassword_p = new CheckPayPassword_p(this, this);
        idCodeEdt = findViewById(R.id.id_code_edt);
        enterCode = (ConstraintLayout) findViewById(R.id.enter_code);
        idGetPhoneCode = findViewById(R.id.send_text);
        times = (TextView) findViewById(R.id.times);
        idPwsEdt = (PwdEditText_Fenkai) findViewById(R.id.id_pws_edt);

        enterPin = (ConstraintLayout) findViewById(R.id.enter_pin);
        enterPhone = (ConstraintLayout) findViewById(R.id.enter_phone);
        phone = (EditText) findViewById(R.id.id_input_edt);
        next = (TextView) findViewById(R.id.next);
        send = (Button) findViewById(R.id.send);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        TextView pinDigit = findViewById(R.id.pin_digit);
        TextView codeDigit = findViewById(R.id.code_digit);

        //倒计时
        countDownUtils = new CountDownUtils(ChangePhoneNumActivity.this, times, "", 60, "s");
        countDownUtils.setOnTimeStop(new CountDownUtils.OnTimeStop() {
            @Override
            public void time_stop() {
                //倒计时结束
                idGetPhoneCode.setOnClickListener(ChangePhoneNumActivity.this);
                idGetPhoneCode.setTextColor(Color.parseColor("#FD7441"));
                times.setText("");
            }
        });

        //切换字体
        TypefaceUtil.replaceFont(findViewById(R.id.hint), "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(phone, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(idGetPhoneCode, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(times, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(send, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(codeDigit, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(pinDigit, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(next, "fonts/gilroy_medium.ttf");
        next.setOnClickListener(this);
        send.setOnClickListener(this);
        back.setOnClickListener(this);
        Map<String, Object> maps = new HashMap<>();
        String[] keys = new String[]{"phoneCode"};
        maps.put("code", keys);
        choiceList_p.loadChoiceList(maps);

        idPwsEdt.setOnTextChangeListener(new PwdEditText_Fenkai.OnTextChangeListener() {
            @Override
            public void onTextChange(String pwd) {
                if (pwd.length() == 4) {
                    long curClickTime = System.currentTimeMillis();
                    if ((curClickTime - lastClickTime) >= 350) {
                        Log.i("curClickTime: ", String.valueOf(curClickTime));
                        Map<String, Object> maps = new HashMap<>();
                        maps.put("payPassword", pwd);
                        checkPayPassword_p.checkPayPassword(maps);
                    }
                    lastClickTime = curClickTime;
                }
            }
        });
        idCodeEdt.setOnTextChangeListener(new PwdEditText_Fenkai.OnTextChangeListener() {
            @Override
            public void onTextChange(String pwd) {
                if (pwd.length() == idCodeEdt.getTextLength()) {
                    SharePreferenceUtils sharePreferenceUtils;
                    sharePreferenceUtils = new SharePreferenceUtils(ChangePhoneNumActivity.this, StaticParament.USER);
                    //更新手机号
                    changeLoginPhone.updatePhone(newPhoneNum,
                            (String) sharePreferenceUtils.get(StaticParament.USER_EMAIL, ""), idCodeEdt.getText().toString().trim());
                }
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                } else {
                    if (String.valueOf(s.toString().charAt(0)).equals("0")) {//输入第一位为0的情况，可以输入10位数字;输入第一位为不是0的情况，只能输入9位数字
                        phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                    } else {
                        phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
                    }
                }
            }
        });
    }

    @Override
    public void setPasswordStatus(String status) {
        if (status == null) {
            idPwsEdt.clearText();
            TiShiDialog tiShiDialog = new TiShiDialog(this);
            tiShiDialog.ShowDialog("Wrong Pin Number");
            tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                @Override
                public void GuanBi() {
                    idPwsEdt.setFocusable(true);//自动弹出数字软键盘
                    idPwsEdt.setFocusableInTouchMode(true);
                    idPwsEdt.requestFocus();
                    idPwsEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            idPwsEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInputFromInputMethod(idPwsEdt.getWindowToken(), 0);
                            imm.toggleSoftInputFromWindow(idPwsEdt.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                            idPwsEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                        }
                    }, 300);
                }
            });
            return;
        }
        enterPin.setVisibility(View.GONE);
        title.setText("New phone number");
        enterPhone.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (!PublicTools.isFastClick()) {
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.back:
                if (enterCode.getVisibility() == View.VISIBLE) {
                    enterCode.setVisibility(View.GONE);
                    idCodeEdt.clearText();
                    enterPhone.setVisibility(View.VISIBLE);
                } else {
                    finish();
                }
                break;
            case R.id.next:
            case R.id.send_text:
                int length = phone.getText().toString().trim().length();
                if (length == 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//大于等于9.0
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/gilroy_medium.ttf");
                        SpannableString efr = new SpannableString(getString(R.string.please_input_phone_number));
                        efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Toast.makeText(ChangePhoneNumActivity.this, efr, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChangePhoneNumActivity.this, getString(R.string.please_input_phone_number), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if (length == 9 || length == 10) {
                    if (length == 9) {
                        newPhoneNum = "61" + phone.getText().toString().trim();
                    } else {
                        newPhoneNum = "61" + phone.getText().toString().trim().substring(1);
                    }

                    //新手机号发送验证码
                    changeLoginPhone.sendSecurityCodeSMSToNew(newPhoneNum);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(phone.getWindowToken(), 0);
                }
                break;
            case R.id.send:
                if (idCodeEdt.getText().toString().length() != 6) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//大于等于9.0
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/gilroy_medium.ttf");
                        SpannableString efr = new SpannableString(getString(R.string.re_null_email_code));
                        efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Toast.makeText(ChangePhoneNumActivity.this, efr, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChangePhoneNumActivity.this, getString(R.string.re_null_email_code), Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                SharePreferenceUtils sharePreferenceUtils;
                sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
                //更新手机号
                changeLoginPhone.updatePhone(newPhoneNum,
                        (String) sharePreferenceUtils.get(StaticParament.USER_EMAIL, ""), idCodeEdt.getText().toString().trim());
                break;
//            case R.id.id_country_code:
//                选择国家号码
//                choiceType();
//                break;
        }
    }

    /*
     */

    /**
     * 选择电话国家号
     *//*

    private void choiceType() {
        BottomDialog bottomDialog = new BottomDialog(this);
        bottomDialog.setOnChoiceItem(new BottomDialog.ChoiceItem() {
            @Override
            public void itemClick(String choiceText, String code, int choice_num) {
                //选择返回
                idCountryCode.setText("+" + code);
                if (code.equals("86")) {
                    //设置限制位数
                    phoneStartNum.setVisibility(View.GONE);
                    phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11) {
                    }});
                } else {
                    phoneStartNum.setVisibility(View.VISIBLE);
                    if (phone.getText().length() > 8) {
                        phone.setText(phone.getText().toString().substring(0, 8));
                    }
                    phone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8) {
                    }});
                }
            }
        });
        if (SystemUtils.getSysLangusge(this).equals("zh")) {
            bottomDialog.show(phoneCodeList, 0);
        } else {
            bottomDialog.show(phoneCodeList, 0);
        }
    }
*/
    @Override
    public void setChoiceList(GetChoiceDatas getChoiceDatas) {
        if (getChoiceDatas != null) {
            phoneCodeList.clear();
            phoneCodeList.addAll(getChoiceDatas.getPhoneCode());

            idPwsEdt.requestFocus();//进度条消失后 再弹出软键盘！！
            idPwsEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    idPwsEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInputFromInputMethod(idPwsEdt.getWindowToken(), 0);
                    imm.toggleSoftInputFromWindow(idPwsEdt.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                    idPwsEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
            }, 300);
        }
    }

    @Override
    public void sendOldPhoneCodeRes() {

    }

    @Override
    public void checkOldPhoneCodeRes() {

    }

    @Override
    public void sendNewPhoneCodeRes() {
        idGetPhoneCode.setOnClickListener(ChangePhoneNumActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//大于等于9.0
            Typeface font = Typeface.createFromAsset(getAssets(), "fonts/gilroy_medium.ttf");
            SpannableString efr = new SpannableString(getString(R.string.emile_code_put));
            efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Toast.makeText(ChangePhoneNumActivity.this, efr, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ChangePhoneNumActivity.this, getString(R.string.emile_code_put), Toast.LENGTH_SHORT).show();
        }

        //开始倒计时
        countDownUtils.setStopText("");
        Log.i("计时器", "开始倒计时");
        countDownUtils.start();
        idGetPhoneCode.setTextColor(Color.parseColor("#707070"));
        idGetPhoneCode.setOnClickListener(null);
        enterPhone.setVisibility(View.GONE);
        enterCode.setVisibility(View.VISIBLE);
        title.setText("Change phone number");

        idCodeEdt.setFocusable(true);//自动弹出数字软键盘
        idCodeEdt.setFocusableInTouchMode(true);
        idCodeEdt.requestFocus();
        idCodeEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                idCodeEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(idCodeEdt.getWindowToken(), 0);
                imm.toggleSoftInputFromWindow(idCodeEdt.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                idCodeEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        }, 300);
    }

    @Override
    public void cheskNewPhoneCodeRes() {
        SharePreferenceUtils sharePreferenceUtils;
        sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        sharePreferenceUtils.put(StaticParament.USER_EMAIL, newPhoneNum);
        sharePreferenceUtils.save();
        TipDialog tiShiDialog = new TipDialog(this);
        tiShiDialog.setOnClose(new TipDialog.Close() {
            @Override
            public void Close() {
                finish();
            }
        });
        tiShiDialog.showDialog("", "Your phone number has been successfully changed.", "Confirm");
    }

    @Override
    public void onBackPressed() {
        if (enterCode.getVisibility() == View.VISIBLE) {
            enterCode.setVisibility(View.GONE);
            idCodeEdt.clearText();
            enterPhone.setVisibility(View.VISIBLE);
        } else {
            finish();
        }
    }

    @Override
    public void errorSexCodeRes() {
        idCodeEdt.clearText();
        TiShiDialog tiShiDialog = new TiShiDialog(this);
        tiShiDialog.ShowDialog("Verification code is not correct.");
        tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
            @Override
            public void GuanBi() {
                idCodeEdt.setFocusable(true);//自动弹出数字软键盘
                idCodeEdt.setFocusableInTouchMode(true);
                idCodeEdt.requestFocus();
                idCodeEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        idCodeEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInputFromInputMethod(idCodeEdt.getWindowToken(), 0);
                        imm.toggleSoftInputFromWindow(idCodeEdt.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                        idCodeEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                }, 300);
            }
        });

    }
}