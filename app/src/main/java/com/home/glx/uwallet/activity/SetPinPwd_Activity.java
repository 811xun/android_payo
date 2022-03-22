package com.home.glx.uwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.MyApplication;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.mvp.PinNumberAbout;
import com.home.glx.uwallet.mvp.PinNumber_in;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.PwdEditText_Fenkai;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.selfview.TipDialog;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;

/**
 * pin_text
 * 设置PIN码
 */
public class SetPinPwd_Activity extends MainActivity implements PinNumber_in {

    private ImageView idBack;
    private TextView idTopText;
    private PwdEditText_Fenkai idPwsEdt;
    private TextView pinText;
    private int regist;//从注册页面跳过来的会传递1 1：设置成功后跳到二选一页面； 0：跳到首页  xzc用的这个！！！

    private PinNumberAbout pinNumberAbout;
    private long lastClickTime;

    @Override
    public int getLayout() {
        return R.layout.activity_pin_pws;
    }

    @Override
    public void initView() {
        super.initView();
        regist = getIntent().getIntExtra("regist", 0);
        MyApplication.nowActivity = "SetPinPwd_Activity";
        pinNumberAbout = new PinNumberAbout(this, this);
        idBack = (ImageView) findViewById(R.id.id_back);
        idBack.setVisibility(View.GONE);
        idBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });
        pinText = (TextView) findViewById(R.id.pin_text);
        idTopText = (TextView) findViewById(R.id.id_top_text);

        idPwsEdt = findViewById(R.id.id_pws_edt);
        idPwsEdt.setFocusable(true);//自动弹出数字软键盘
        idPwsEdt.setFocusableInTouchMode(true);
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
        //切换字体
        TypefaceUtil.replaceFont(pinText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(idTopText, "fonts/gilroy_semiBold.ttf");
        if (getIntent().getStringExtra("pwd") != null) {
            idBack.setVisibility(View.VISIBLE);
            idTopText.setText("Confirm PIN");
            pinText.setText("Re-enter your PIN to confirm");
        } else {
            idTopText.setText("Set up a PIN");
            pinText.setText("Choose a 4-digit PIN to authorise your payments");
        }
        if (getIntent().getStringExtra("type") != null) {
            idBack.setVisibility(View.VISIBLE);
            idPwsEdt.clearText();
        }
        idPwsEdt.setOnTextChangeListener(new PwdEditText_Fenkai.OnTextChangeListener() {
            @Override
            public void onTextChange(String pwd) {
                if (pwd.length() == idPwsEdt.getTextLength()) {
                    long curClickTime = System.currentTimeMillis();
                    if ((curClickTime - lastClickTime) >= 350) {
                        Log.i("curClickTime: ", String.valueOf(curClickTime));
                        //输入监听
                        if (getIntent().getStringExtra("pwd") != null) {
                            //确认密码完成
                            if (pwd.equals(getIntent().getStringExtra("pwd"))) {//输入的两次密码相同
                                //设置PIN
                                pinNumberAbout.changePin(new HashMap<String, Object>(), pwd);
                            } else {//输入的两次密码不同
                                TipDialog tiShiDialog = new TipDialog(SetPinPwd_Activity.this);
                                tiShiDialog.setOnClose(new TipDialog.Close() {
                                    @Override
                                    public void Close() {
                                        idPwsEdt.clearText();
                                        Intent intent = new Intent(SetPinPwd_Activity.this, SetPinPwd_Activity.class);
//                                    intent.putExtra("type", "set");
                                        intent.putExtra("regist", regist);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                tiShiDialog.showDialog("", "PINs don’t match", "Confirm");
                            }
                        } else {//第一次
                            //再起确认密码
                            Intent intent = new Intent(SetPinPwd_Activity.this, SetPinPwd_Activity.class);
                            intent.putExtra("addbank", getIntent().getIntExtra("addbank", 0));
                            intent.putExtra("pwd", pwd);
                            intent.putExtra("regist", regist);
                            if (getIntent().getStringExtra("type") != null
                                    && getIntent().getStringExtra("type").equals("set")) {
                                intent.putExtra("type", "set");
                            }
                            intent.putExtra("fromAccountFragment", getIntent().getBooleanExtra("fromAccountFragment", false));//从(InputPin_Activity)设置PIn过来的传递的true
                            startActivity(intent);
                            finish();
                        }
                    }
                    lastClickTime = curClickTime;


                }
            }
        });
    }

    @Override
    public void selectPin(String pin) {//这儿不走

    }

    @Override
    public void changePin(String status) {
        if (status.equals("1")) {
            TiShiDialog tiShiDialog = new TiShiDialog(this);
            tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                @Override
                public void GuanBi() {
                    SharePreferenceUtils sharePreferenceUtils =
                            new SharePreferenceUtils(SetPinPwd_Activity.this, StaticParament.USER);
                    sharePreferenceUtils.put(StaticParament.PIN_NUMBER, getIntent().getStringExtra("pwd"));
                    sharePreferenceUtils.save();
                    MyApplication.nowActivity = "";
                    if (regist == 0) {
                        if (!getIntent().getBooleanExtra("fromAccountFragment", false)) {
                            //刚打开APP
                            Intent intent = new Intent(SetPinPwd_Activity.this, MainTab.class);
                            intent.putExtra("regist", 0);
                            SharePreferenceUtils app_sharePreferenceUtils = new SharePreferenceUtils(SetPinPwd_Activity.this, StaticParament.APP);
                            String language = (String) app_sharePreferenceUtils.get(StaticParament.LANGUAGE, "");
                            if (language.equals("1")) {
                                app_sharePreferenceUtils.put(StaticParament.CHANGE_LANGUAGE, "0");
                                app_sharePreferenceUtils.save();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            } else {
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
                            }
                            intent.putExtra("haveCheckUpdate", "1");
                            if (getIntent().getBooleanExtra("fromAccountFragment", false)) {
                                intent.putExtra("num", 3);
                            }//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);
                        }

                        finish();
                    } else if (regist == 1) {
                        Intent intentPay = new Intent(SetPinPwd_Activity.this, SelectPayTypeActivity.class);
                        startActivity(intentPay);
                    }
                    finish();

                }
            });

            tiShiDialog.ShowDialog("Done!", getString(R.string.PIN_Successfully), "Continue");
//            if (idTitle.getText().toString().equals("Set Your PIN")) {
//            } else {
//                tiShiDialog.ShowDialog("Your 4-digit PIN has been successfully changed.", "Close");
//            }
        }
    }

    @Override
    public void checkPin(String status) {

    }

    private void backPage() {//左上角返回按钮逻辑  xzc 第一页不用返回 第二页返回上一页。
        if (getIntent().getStringExtra("type") != null
                && getIntent().getStringExtra("type").equals("set")) {
            if (getIntent().getStringExtra("pwd") != null) {
                Intent intent = new Intent(SetPinPwd_Activity.this, SetPinPwd_Activity.class);
                if (getIntent().getStringExtra("type") != null
                        && getIntent().getStringExtra("type").equals("set")) {
                    intent.putExtra("type", "set");
                }
                intent.putExtra("regist", regist);
                startActivity(intent);
            }
            finish();
        } else {
            if (getIntent().getStringExtra("pwd") != null) {
                Intent intent = new Intent(SetPinPwd_Activity.this, SetPinPwd_Activity.class);
                if (getIntent().getStringExtra("type") != null
                        && getIntent().getStringExtra("type").equals("set")) {
                    intent.putExtra("type", "set");
                }
                intent.putExtra("regist", regist);
                startActivity(intent);
                finish();
            } else {
                //退出APP
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                System.exit(0);
            }
        }
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backPage();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //do something.
            //禁用返回键
            if (idBack.getVisibility() == View.VISIBLE) {
                backPage();
                return super.dispatchKeyEvent(event);
            } else {
                return true;
            }
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

/*    @Override
    public void onBackPressed() {
        // 完全由自己控制返回键逻辑，系统不再控制，但是有个前提是：　　
        // 不要在Activity的onKeyDown或者OnKeyUp中拦截掉返回键　　
        // 拦截：就是在OnKeyDown或者OnKeyUp中自己处理了返回键　　
        // （这里处理之后return true.或者return false都会导致onBackPressed不会执行）
        // 不拦截：在OnKeyDown和OnKeyUp中返回super对应的方法　　
        // （如果两个方法都被覆写就分别都要返回super.onKeyDown,super.onKeyUp）
        if (idBack.getVisibility() == View.VISIBLE) {
            backPage();
        }
    }*/


}
