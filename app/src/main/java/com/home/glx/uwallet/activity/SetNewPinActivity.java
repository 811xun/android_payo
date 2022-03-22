package com.home.glx.uwallet.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.mvp.PinNumberAbout;
import com.home.glx.uwallet.mvp.PinNumber_in;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.PwdEditText_Fenkai;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.selfview.TipDialog;
import com.home.glx.uwallet.tools.CountDownUtils;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class SetNewPinActivity extends MainActivity implements View.OnClickListener, PinNumber_in {
    private ConstraintLayout inputCode;
    private ConstraintLayout setPinCl;
    private ConstraintLayout reSetPinCl;
    private CountDownUtils countDownUtils;
    private TextView idGetPhoneCode;
    private TextView times;
    private TextView title;
    private TextView enterDigit;
    private PwdEditText_Fenkai idCodeEdt;
    private PwdEditText_Fenkai firstPin;
    private PwdEditText_Fenkai lastPin;
    private TextView reEnter;
    private UserListener userListener;
    private String phone;
    private TextView setDigit;
    private String firstPinNum;
    private ImageView back;

    private PinNumberAbout pinNumberAbout;//忘记Pin页
    private long lastClickTime;

    @Override
    public int getLayout() {
        return R.layout.activity_set_new_pin;
    }

    @Override
    public void initView() {
        super.initView();
        pinNumberAbout = new PinNumberAbout(this, this);
        userListener = new UserModel(this);
        back = findViewById(R.id.id_back);
        idGetPhoneCode = findViewById(R.id.send_text);
        times = (TextView) findViewById(R.id.times);
        idCodeEdt = (PwdEditText_Fenkai) findViewById(R.id.id_code_edt);

        title = findViewById(R.id.id_title);
        enterDigit = findViewById(R.id.code_digit);
        inputCode = findViewById(R.id.enter_code);
        setPinCl = findViewById(R.id.set_pin_cl);
        setDigit = findViewById(R.id.set_digit);
        firstPin = findViewById(R.id.first_pin);
        firstPin.setFocusable(false);//自动弹出数字软键盘
        firstPin.setFocusableInTouchMode(false);
        reSetPinCl = findViewById(R.id.re_set_pin_cl);
        lastPin = findViewById(R.id.last_pin);
        lastPin.setFocusable(false);//自动弹出数字软键盘
        lastPin.setFocusableInTouchMode(false);
        reEnter = findViewById(R.id.re_enter);
        back.setOnClickListener(this);
        //切换字体
        TypefaceUtil.replaceFont(enterDigit, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(idGetPhoneCode, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(times, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(setDigit, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(reEnter, "fonts/gilroy_medium.ttf");
        //倒计时
        countDownUtils = new CountDownUtils(SetNewPinActivity.this, times, "", 60, "s");
        countDownUtils.setOnTimeStop(new CountDownUtils.OnTimeStop() {
            @Override
            public void time_stop() {
                //倒计时结束
                idGetPhoneCode.setOnClickListener(SetNewPinActivity.this);
                idGetPhoneCode.setTextColor(Color.parseColor("#FD7441"));
                times.setText("");
            }
        });
        getEmailCode();
        idCodeEdt.setOnTextChangeListener(new PwdEditText_Fenkai.OnTextChangeListener() {
            @Override
            public void onTextChange(String pwd) {
                if (pwd.length() == idCodeEdt.getTextLength()) {
                    long curClickTime = System.currentTimeMillis();
                    if ((curClickTime - lastClickTime) >= 350) {
                        Log.i("curClickTime: ", String.valueOf(curClickTime));
                        Map<String, Object> map = new HashMap<>();
                        map.put("phone", phone);
                        map.put("code", idCodeEdt.getText().toString().trim());
                        map.put("sendNode", "38");
                        userListener.verifyCode(map, new ActionCallBack() {
                            @Override
                            public void onSuccess(Object... o) {
                                inputCode.setVisibility(View.GONE);

                                setPinCl.setVisibility(View.VISIBLE);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        firstPin.setFocusable(true);//自动弹出数字软键盘
                                        firstPin.setFocusableInTouchMode(true);
                                        firstPin.requestFocus();
                                        firstPin.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.showSoftInputFromInputMethod(firstPin.getWindowToken(), 0);
                                        imm.toggleSoftInputFromWindow(firstPin.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                                        firstPin.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    }
                                }, 300);
                            }

                            @Override
                            public void onError(String e) {//Verification code is not correct.
                                idCodeEdt.clearText();
                                TiShiDialog tiShiDialog = new TiShiDialog(SetNewPinActivity.this);
                                tiShiDialog.ShowDialog(e);
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
                        });
                    }
                    lastClickTime = curClickTime;
                }
            }
        });
        firstPin.setOnTextChangeListener(new PwdEditText_Fenkai.OnTextChangeListener() {
            @Override
            public void onTextChange(String pwd) {
                if (pwd.length() == firstPin.getTextLength()) {
                    firstPinNum = firstPin.getText().toString();
                    firstPin.clearText();
                    setPinCl.setVisibility(View.GONE);
                    reSetPinCl.setVisibility(View.VISIBLE);

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(firstPin.getWindowToken(), 0);
//
//                    lastPin.setFocusable(true);
//                    lastPin.setFocusableInTouchMode(true);
//                    lastPin.requestFocus();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lastPin.setFocusable(true);//自动弹出数字软键盘
                            lastPin.setFocusableInTouchMode(true);
                            lastPin.requestFocus();
                            lastPin.setInputType(InputType.TYPE_CLASS_NUMBER);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInputFromInputMethod(lastPin.getWindowToken(), 0);
                            imm.toggleSoftInputFromWindow(lastPin.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                            lastPin.setInputType(InputType.TYPE_CLASS_NUMBER);
                        }
                    }, 200);

                }
            }
        });
        lastPin.setOnTextChangeListener(new PwdEditText_Fenkai.OnTextChangeListener() {
            @Override
            public void onTextChange(String pwd) {
                if (pwd.length() == lastPin.getTextLength()) {
                    if (!firstPinNum.equals(lastPin.getText().toString())) {
                        TipDialog tipDialog = new TipDialog(SetNewPinActivity.this);
                        tipDialog.setOnClose(new TipDialog.Close() {
                            @Override
                            public void Close() {
                                reSetPinCl.setVisibility(View.GONE);
                                lastPin.clearText();
                                setPinCl.setVisibility(View.VISIBLE);
                                firstPin.clearText();

                                firstPin.setFocusable(true);//自动弹出数字软键盘
                                firstPin.setFocusableInTouchMode(true);
                                firstPin.requestFocus();
                                firstPin.setInputType(InputType.TYPE_CLASS_NUMBER);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        firstPin.setInputType(InputType.TYPE_CLASS_NUMBER);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.showSoftInputFromInputMethod(firstPin.getWindowToken(), 0);
                                        imm.toggleSoftInputFromWindow(firstPin.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                                        firstPin.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    }
                                }, 300);
                            }
                        });
                        tipDialog.showDialog("", "PINs don’t match", "Confirm");
                    } else {
                        //设置PIN
                        pinNumberAbout.changePin(new HashMap<String, Object>(), pwd);
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.send_text:
                getEmailCode();
                break;
            case R.id.id_back:
                if (inputCode.getVisibility() == View.VISIBLE || setPinCl.getVisibility() == View.VISIBLE) {
                    finish();
                }
                if (reSetPinCl.getVisibility() == View.VISIBLE) {
                    reSetPinCl.setVisibility(View.GONE);
                    lastPin.clearText();
                    setPinCl.setVisibility(View.VISIBLE);
                    firstPinNum = "";
                }
                break;
        }
    }

    public void getEmailCode() {
        SharePreferenceUtils sharePreferenceUtils_app = new SharePreferenceUtils(this, StaticParament.APP);
        phone = (String) sharePreferenceUtils_app.get(StaticParament.USER_PHONE, "");

        Map<String, Object> maps = new HashMap<>();
        maps.put("phone", phone);
        maps.put("sendNode", "38");

        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(SetNewPinActivity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.GetRegistCode(headerMap, requestBody,
                        phone, "38");
                requestUtils.Call(call);
            }


            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("获取验证码" + dataStr);
                idGetPhoneCode.setOnClickListener(SetNewPinActivity.this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//大于等于9.0
                    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/gilroy_medium.ttf");
                    SpannableString efr = new SpannableString(getString(R.string.emile_code_put));
                    efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Toast.makeText(SetNewPinActivity.this, efr, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SetNewPinActivity.this, getString(R.string.emile_code_put), Toast.LENGTH_SHORT).show();
                }

                //开始倒计时
                countDownUtils.setStopText("");
                Log.i("计时器", "开始倒计时");
                countDownUtils.start();
                idGetPhoneCode.setTextColor(Color.parseColor("#707070"));
                idGetPhoneCode.setOnClickListener(null);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        idCodeEdt.setFocusable(true);//自动弹出数字软键盘
                        idCodeEdt.setFocusableInTouchMode(true);
                        idCodeEdt.requestFocus();
                        idCodeEdt.setInputType(InputType.TYPE_CLASS_NUMBER);

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInputFromInputMethod(idCodeEdt.getWindowToken(), 0);
                        imm.toggleSoftInputFromWindow(idCodeEdt.getWindowToken(), 0, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        idCodeEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                }, 300);
            }


            @Override
            public void resError(String error) {
                idGetPhoneCode.setOnClickListener(SetNewPinActivity.this);

            }
        });
    }

    @Override
    public void selectPin(String pin) {

    }

    @Override
    public void changePin(String status) {
        if (status.equals("1")) {
            TipDialog tipDialog = new TipDialog(SetNewPinActivity.this);
            tipDialog.setOnClose(new TipDialog.Close() {
                @Override
                public void Close() {
                    finish();
                }
            });
            tipDialog.showDialog("", "Your 4-digit PIN has been successfully changed.", "Close");
        }
    }

    @Override
    public void checkPin(String status) {

    }

}