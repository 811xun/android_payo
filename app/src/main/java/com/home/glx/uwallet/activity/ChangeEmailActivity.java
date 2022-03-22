package com.home.glx.uwallet.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.mvp.ChangeEmail;
import com.home.glx.uwallet.mvp.CheckPayPassword_in;
import com.home.glx.uwallet.mvp.CheckPayPassword_p;
import com.home.glx.uwallet.selfview.PwdEditText_Fenkai;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;
import java.util.Map;

public class ChangeEmailActivity extends MainActivity implements CheckPayPassword_in.View {
    private CheckPayPassword_p checkPayPassword_p;
    private PwdEditText_Fenkai idPwsEdt;
    private ConstraintLayout enterPin;
    private ConstraintLayout enterEmail;
    private EditText email;
    private TextView next;
    private ImageView back;
    private long lastClickTime;

    @Override
    public int getLayout() {
        return R.layout.activity_change_email;
    }

    @Override
    public void initView() {
        super.initView();
        idPwsEdt = (PwdEditText_Fenkai) findViewById(R.id.id_pws_edt);
        enterPin = (ConstraintLayout) findViewById(R.id.enter_pin);
        email = (EditText) findViewById(R.id.id_input_edt);
        next = (TextView) findViewById(R.id.next);
        back = (ImageView) findViewById(R.id.back);
        TextView title = findViewById(R.id.title);
        TextView pinDigit = findViewById(R.id.pin_digit);
        TextView typePhone = findViewById(R.id.type_phone);
        //切换字体
        TypefaceUtil.replaceFont(next, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(email, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(typePhone, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(pinDigit, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        enterEmail = (ConstraintLayout) findViewById(R.id.enter_phone);

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
        checkPayPassword_p = new CheckPayPassword_p(this, this);
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
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PublicTools.isFastClick()) {
                    return;
                }
                if (!TextUtils.isEmpty(email.getText().toString())) {
                    changeEmailReq();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//大于等于9.0
                        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/gilroy_medium.ttf");
                        SpannableString efr = new SpannableString(getString(R.string.please_agree_agreement));
                        efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Toast.makeText(ChangeEmailActivity.this, "Please input your email", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChangeEmailActivity.this, "Please input your email", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void changeEmailReq() {
        if (email.getText().toString().trim().equals("")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//大于等于9.0
                Typeface font = Typeface.createFromAsset(getAssets(), "fonts/gilroy_medium.ttf");
                SpannableString efr = new SpannableString(getString(R.string.bank_email_input_error));
                efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Toast.makeText(this, efr, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.bank_email_input_error, Toast.LENGTH_SHORT).show();
            }
            return;
        }
        ChangeEmail changeEmail = new ChangeEmail(this);
        changeEmail.setOnFindList(new ChangeEmail.OnChangeSatus() {
            @Override
            public void status(String status) {
            }
        });
        Map<String, String> maps = new HashMap<>();
        maps.put("email", email.getText().toString().trim());
        changeEmail.change(maps);
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
        enterEmail.setVisibility(View.VISIBLE);
    }
}