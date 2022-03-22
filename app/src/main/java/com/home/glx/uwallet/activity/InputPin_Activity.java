package com.home.glx.uwallet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.home.glx.uwallet.BaseActivity;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.MyApplication;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.xzc.LoginActivity_inputNumber;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.mvp.PinNumberAbout;
import com.home.glx.uwallet.mvp.PinNumber_in;
import com.home.glx.uwallet.mvp.ScanCodeData_in;
import com.home.glx.uwallet.mvp.ScanCodeData_p;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.PwdEditText_Fenkai;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;

/**
 * 输入PIN
 */
public class InputPin_Activity extends MainActivity
        implements View.OnClickListener, PinNumber_in, ScanCodeData_in.View {//change Pin xzc

    private ImageView idBack;
    private TextView idTitle;
    //    private PwdEditText idPwsEdt;
    private PwdEditText_Fenkai editSolid;
    private TextView idForgetPwd;
    private TextView idFaileNum;
    private PinNumberAbout pinNumberAbout;
    private ScanCodeData_p scanImgPresnet;
    private TextView forgetPin;
    private SharePreferenceUtils sharePreferenceUtils;
    private int fail_num = 0;
    private long lastClickTime;

    @Override
    public int getLayout() {
        return R.layout.activity_input_pin;
    }

    @Override
    public void initView() {
        super.initView();

        sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        fail_num = (int) sharePreferenceUtils.get(StaticParament.FAILE_NUMBER, 0);

        scanImgPresnet = new ScanCodeData_p(this, this);

        idBack = (ImageView) findViewById(R.id.id_back);
        idTitle = (TextView) findViewById(R.id.id_title);
//        idPwsEdt = (PwdEditText) findViewById(R.id.id_pws_edt);
        idForgetPwd = (TextView) findViewById(R.id.id_forget_pwd);
        idFaileNum = (TextView) findViewById(R.id.id_faile_num);
        forgetPin = findViewById(R.id.forget_pin);
        forgetPin.setOnClickListener(this);
        //切换字体
        TypefaceUtil.replaceFont(idTitle, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(forgetPin, "fonts/gilroy_semiBold.ttf");
        editSolid = (PwdEditText_Fenkai) findViewById(R.id.edit_solid);
        editSolid.setFocusable(true);//自动弹出数字软键盘
        editSolid.setFocusableInTouchMode(true);
        editSolid.requestFocus();
        editSolid.setInputType(InputType.TYPE_CLASS_NUMBER);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                editSolid.setInputType(InputType.TYPE_CLASS_NUMBER);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(editSolid.getWindowToken(), 0);
                imm.toggleSoftInputFromWindow(editSolid.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                editSolid.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        }, 300);
   /*     editSolid.addTextChangedListener(new SeparatedEditText.TextChangedListener() {
            @Override
            public void textChanged(CharSequence changeText) {

            }

            @Override
            public void textCompleted(CharSequence text) {
                //输入完成
                //校验PIN
                pinNumberAbout.checkPin(new HashMap<String, Object>(), text.toString());
            }
        });*/

        editSolid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("xzcxzccs", "afterTextChanged: " + s.toString());
                if (s.toString().length() == 4) {
                    long curClickTime = System.currentTimeMillis();
                    if ((curClickTime - lastClickTime) >= 350) {
                        Log.i("curClickTime: ", String.valueOf(curClickTime));
                        pinNumberAbout.checkPin(new HashMap<String, Object>(), s.toString());
                    }
                    lastClickTime = curClickTime;
                }
            }
        });


//        idPwsEdt.setOnTextChangeListener(new PwdEditText.OnTextChangeListener() {
//            @Override
//            public void onTextChange(String pwd) {
//                if (pwd.length() == idPwsEdt.getTextLength()) {
//                    //输入监听
//                    //校验PIN
//                    pinNumberAbout.checkPin(new HashMap<String, Object>(), pwd);
//                }
//            }
//        });
        idBack.setOnClickListener(this);
        idForgetPwd.setOnClickListener(this);

        if (getIntent().getStringExtra("type") != null
                && (getIntent().getStringExtra("type").equals("back") ||
                getIntent().getStringExtra("type").equals("main"))

        ) {
            L.log("qqqqqqccccccccccccc：" + getIntent().getStringExtra("type"));
            MyApplication.nowActivity = "SetPinPwd_Activity";
        }

        if (getIntent().getStringExtra("type") != null
                && getIntent().getStringExtra("type").equals("set")) {
            idTitle.setText(R.string.original_pin);
            idFaileNum.setText(R.string.set_4pin);
        }

        pinNumberAbout = new PinNumberAbout(this, this);
    }

    private void openNext() {
        L.log("IIIIIIIIIIIIII" + getIntent().getStringExtra("type"));
        sharePreferenceUtils.put(StaticParament.FAILE_NUMBER, 0);
        sharePreferenceUtils.save();
        if (getIntent().getStringExtra("type") != null
                && getIntent().getStringExtra("type").equals("main")) {
            MyApplication.nowActivity = "";
            //打开主页面
            Intent intent = new Intent(InputPin_Activity.this, MainTab.class);
            startActivity(intent);
            finish();
        } else if (getIntent().getStringExtra("type") != null
                && getIntent().getStringExtra("type").equals("back")) {
            MyApplication.nowActivity = "";
            finish();
        } else if (getIntent().getStringExtra("type") != null
                && getIntent().getStringExtra("type").equals("set")) {
            //设置PIN
            Intent intent = new Intent(InputPin_Activity.this, SetPinPwd_Activity.class);
            intent.putExtra("type", "set");
            intent.putExtra("fromAccountFragment", true);
            startActivity(intent);
            finish();
        } else if (getIntent().getStringExtra("nfc_id") != null) {
            //识别NFC进入
            scanImgPresnet.loadNfcCodeData(getIntent().getStringExtra("nfc_id"));
//            openRepayMoney();
        } else {
            //默认情况
            Intent intent = new Intent(InputPin_Activity.this, MainTab.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_back:
                closeApp();
                break;
            case R.id.id_forget_pwd:
                //忘记密码
                forgotPin();
                break;
            case R.id.forget_pin:
                Intent intent = new Intent(InputPin_Activity.this, SetNewPinActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void forgotPin() {
        pinNumberAbout.changePin(new HashMap<String, Object>(), "0");
        sharePreferenceUtils.clear();
        Intent intent = new Intent(InputPin_Activity.this, LoginActivity_inputNumber.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        for (Activity activity : BaseActivity.activityList) {
            activity.finish();
        }
        if (getIntent().getStringExtra("type") != null
                && getIntent().getStringExtra("type").equals("back")) {
            MyApplication.nowActivity = "";
        }
        finish();
        startActivity(intent);

    }


    @Override
    public void selectPin(String pin) {

    }

    @Override
    public void changePin(String status) {

    }

    @Override
    public void checkPin(String status) {
        if (status.equals("1")) {
            openNext();
        } else if (status.equals("0")) {//输入密码错误 点击提示框 再次弹出软键盘。
            TiShiDialog tiShiDialog = new TiShiDialog(this);
            tiShiDialog.ShowDialog("Wrong Pin Number");
            tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                @Override
                public void GuanBi() {
                    editSolid.setFocusable(true);//自动弹出数字软键盘
                    editSolid.setFocusableInTouchMode(true);
                    editSolid.requestFocus();
                    editSolid.setInputType(InputType.TYPE_CLASS_NUMBER);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            editSolid.setInputType(InputType.TYPE_CLASS_NUMBER);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInputFromInputMethod(editSolid.getWindowToken(), 0);
                            imm.toggleSoftInputFromWindow(editSolid.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                            editSolid.setInputType(InputType.TYPE_CLASS_NUMBER);
                        }
                    }, 300);
                }
            });
            editSolid.clearText();
            fail_num++;
            sharePreferenceUtils.put(StaticParament.FAILE_NUMBER, fail_num);
            sharePreferenceUtils.save();

//            if (fail_num > 3) {
//                forgotPin();
//                return;
//            }
//
//            if (fail_num == 3) {
//                idFaileNum.setText(R.string.last_chance);
//            }
//            else {
//                idFaileNum.setText("错误" + fail_num + "次，请重试");
//            }
        } else if (status.equals("2")) {
            editSolid.clearText();
            Toast.makeText(this, getString(R.string.net_work_no_tex), Toast.LENGTH_SHORT).show();
        }
    }

/*    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && getIntent().getStringExtra("type") != null
                && getIntent().getStringExtra("type").equals("back")) {
            closeApp();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DEL) {
            return true;
        }
        openHome();
        return true;
//        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public void onBackPressed() {
        // 完全由自己控制返回键逻辑，系统不再控制，但是有个前提是：　　
        // 不要在Activity的onKeyDown或者OnKeyUp中拦截掉返回键　　
        // 拦截：就是在OnKeyDown或者OnKeyUp中自己处理了返回键　　
        // （这里处理之后return true.或者return false都会导致onBackPressed不会执行）
        // 不拦截：在OnKeyDown和OnKeyUp中返回super对应的方法　　
        // （如果两个方法都被覆写就分别都要返回super.onKeyDown,super.onKeyUp）
        if (getIntent().getStringExtra("type") != null
                && getIntent().getStringExtra("type").equals("back")) {
            closeApp();
        } else {
            openHome();
        }
    }

    private String openType = "";

    /**
     * 切换APP到后台
     */
    private void closeApp() {
        if (getIntent().getStringExtra("type") != null
                && getIntent().getStringExtra("type").equals("set")) {
            openHome();
            return;
        }
//        moveTaskToBack(isFinishing());

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        System.exit(0);
    }

    private void openHome() {
        openType = getIntent().getStringExtra("openType");
        L.log("pppppppppp", openType + "l");
        if (openType != null && openType.equals("lang")) {
            //切换了语言
            Intent it = new Intent(this, MainTab.class);
            it.putExtra("num", 3);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);
        }
        finish();
    }

    /**
     * 商户信息返回
     *
     * @param type
     * @param data
     */
    @Override
    public void setScanData(String type, String data) {

    }

}
