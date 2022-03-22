package com.home.glx.uwallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.home.glx.uwallet.BaseActivity;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.xzc.LoginActivity_inputNumber;
import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.fragment.FirstFragment;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.mvp.PresencePayPassword_in;
import com.home.glx.uwallet.mvp.PresencePayPassword_p;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.BottomDialog;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.SystemUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.List;

public class Setting_Activity extends MainActivity
        implements View.OnClickListener, PresencePayPassword_in.View {

    private ImageView idBack;
    private TextView idUserEmail;
    private RelativeLayout idSetRepayPas;
    private RelativeLayout idUnlogin;
    private RelativeLayout idUsermsg;
    private TextView idUserPhone;
    private TextView idPin;
    private RelativeLayout idChangePin;
    private RelativeLayout idSetLanguage;
    private TextView idNowLanguage;

    private PresencePayPassword_p presencePayPassword_p;
    private TextView idUserPayPassword;

    private SharePreferenceUtils user_sharePreferenceUtils;
    private SharePreferenceUtils app_sharePreferenceUtils;

    private String openType = "";

    //返回不能连点
    private int backFlag = 0;
    private String from;

    @Override
    public int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        super.initView();

        openType = getIntent().getStringExtra("openType");
        from = getIntent().getStringExtra("from");

        user_sharePreferenceUtils = new SharePreferenceUtils(Setting_Activity.this, StaticParament.USER);
        presencePayPassword_p = new PresencePayPassword_p(Setting_Activity.this, this);
        idBack = (ImageView) findViewById(R.id.id_back);
        idUserEmail = (TextView) findViewById(R.id.id_user_email);
        idUsermsg = (RelativeLayout) findViewById(R.id.user_msg_btn);
        idSetRepayPas = (RelativeLayout) findViewById(R.id.id_set_repay_pas);
        idUnlogin = (RelativeLayout) findViewById(R.id.id_unlogin);
        idUserPayPassword = (TextView) findViewById(R.id.id_user_pay_password);
        idChangePin = (RelativeLayout) findViewById(R.id.id_change_pin);
        idSetLanguage = (RelativeLayout) findViewById(R.id.id_set_language);
        idNowLanguage = (TextView) findViewById(R.id.id_now_language);

        app_sharePreferenceUtils = new SharePreferenceUtils(Setting_Activity.this, StaticParament.APP);
        String language = (String) app_sharePreferenceUtils.get(StaticParament.LANGUAGE, "");
        if (language.equals("0")) {
            //英文
            idNowLanguage.setText("English");
        } else if (language.equals("1")) {
            //中文
            idNowLanguage.setText("简体中文");
        } else {
            idNowLanguage.setText("");
            String sysLang = SystemUtils.getSysLangusge(this);
            if (sysLang.equals("zh")) {
                idNowLanguage.setText("简体中文");
            } else {
                idNowLanguage.setText("English");
            }
        }

        idUserPhone = (TextView) findViewById(R.id.id_user_phone);
        idPin = (TextView) findViewById(R.id.id_pin);


        if (user_sharePreferenceUtils.get(StaticParament.USER_PAY_PWD, "").equals("1")) {
            //已经设置交易密码
            idUserPayPassword.setVisibility(View.GONE);
        } else {
            idUserPayPassword.setVisibility(View.VISIBLE);
            idUserPayPassword.setText(R.string.unsetting);
        }
        TextView settingsTitle = findViewById(R.id.settings_title);
        TextView myProfileText = findViewById(R.id.my_profile_text);
        TextView changePinText = findViewById(R.id.change_pin_text);
        TextView languageText = findViewById(R.id.language_text);
        TextView logOutText = findViewById(R.id.log_out_text);
        //切换字体
        TypefaceUtil.replaceFont(settingsTitle, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(myProfileText, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.my_about_text), "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(changePinText, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(languageText, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(idNowLanguage, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(logOutText, "fonts/gilroy_semiBold.ttf");

        idUsermsg.setOnClickListener(this);
        idBack.setOnClickListener(this);
        idSetRepayPas.setOnClickListener(this);
        idUnlogin.setOnClickListener(this);
        idChangePin.setOnClickListener(this);
        idUserPhone.setOnClickListener(this);
        idSetLanguage.setOnClickListener(this);
        findViewById(R.id.about_rl).setOnClickListener(this);

/*        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        idUserEmail.setText((String) sharePreferenceUtils.get(StaticParament.USER_EMAIL, ""));
        String phone = (String) sharePreferenceUtils.get(StaticParament.USER_EMAIL, "");
        if (!phone.equals("")) {
            String top = phone.substring(0, 2);
            String end = phone.substring(2, phone.length());
//            idUserPhone.setText((String) sharePreferenceUtils.get(StaticParament.USER_EMAIL, ""));
            if (top.equals("61")) {
                end = "4" + end;
            }
            idUserPhone.setText("(" + top + ")" + end);
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        idUserEmail.setText((String) sharePreferenceUtils.get(StaticParament.USER_EMAIL, ""));
        String phone = (String) sharePreferenceUtils.get(StaticParament.USER_EMAIL, "");
        if (!phone.equals("")) {
            String top = phone.substring(0, 2);
            String end = phone.substring(2, phone.length());
//            idUserPhone.setText((String) sharePreferenceUtils.get(StaticParament.USER_EMAIL, ""));
            idUserPhone.setText("(" + top + ")" + end);
        }
    }

    @Override
    public void onClick(View v) {
        if (!PublicTools.isFastClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.user_msg_btn:
                //查看用户信息
                Intent intent_user_msg = new Intent(Setting_Activity.this, UserMsgActivity.class);
                startActivity(intent_user_msg);
                break;
            case R.id.id_back:
                openHome();
                break;
            case R.id.id_change_pin:
                //设置PIN
//                Intent intent = new Intent(Setting_Activity.this, SetPinPwd_Activity.class);
                Intent intent = new Intent(Setting_Activity.this, InputPin_Activity.class);
                intent.putExtra("type", "set");
                intent.putExtra("openType", getIntent().getStringExtra("openType"));
                startActivity(intent);
                //finish();
                break;

            case R.id.id_set_language:
                //设置语言
                showLanguageDialog();
                break;
            case R.id.id_unlogin:
                showShureDialog();
                break;
            case R.id.about_rl:
                Intent intentAbout = new Intent(Setting_Activity.this, AboutActivity.class);
                startActivity(intentAbout);
                break;
        }
    }


    private void showShureDialog() {
        TiShiDialog tiShiDialogTwo = new TiShiDialog(this);
        tiShiDialogTwo.ShowDialog(getString(R.string.Please_confirm), getString(R.string.want_logout), "Log out");
        tiShiDialogTwo.setOnGuanBi(new TiShiDialog.GuanBi() {
            @Override
            public void GuanBi() {
                unLogin();
            }
        });
    }

    /**
     * 退出登录
     */
    private void unLogin() {
        FirstFragment.backStatue = -1;
//        FirstFragment.mFisrt = true;
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        sharePreferenceUtils.clear();
        SharePreferenceUtils adSharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.DEVICE);
        adSharePreferenceUtils.put(StaticParament.SPLIT_FLAG, "0");
        adSharePreferenceUtils.save();
        Toast.makeText(this, R.string.unlogin_success, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Setting_Activity.this, LoginActivity_inputNumber.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        for (Activity activity : BaseActivity.activityList) {
            activity.finish();
        }
        startActivity(intent);
//        openLogin();
//        finish();
    }

    @Override
    public void setPresenceStatus(String code, String status) {

    }


    private List<ChoiceDatas> languageTypes = new ArrayList<>();

    private void showLanguageDialog() {
        languageTypes.clear();
        languageTypes.add(new ChoiceDatas("0", "English", "English"));
        languageTypes.add(new ChoiceDatas("1", "Chinese", "Chinese"));
        BottomDialog bottomDialog = new BottomDialog(Setting_Activity.this);
        bottomDialog.setOnChoiceItem(new BottomDialog.ChoiceItem() {
            @Override
            public void itemClick(String choiceText, String code, int choice_num) {
                if (idNowLanguage.getText().toString().equals(choiceText)) {
                    return;
                }
                //选择返回
                app_sharePreferenceUtils =
                        new SharePreferenceUtils(Setting_Activity.this, StaticParament.APP);
                app_sharePreferenceUtils.put(StaticParament.LANGUAGE, code);
                app_sharePreferenceUtils.put(StaticParament.CHANGE_LANGUAGE, "1");
                app_sharePreferenceUtils.save();

                Intent intent = new Intent(Setting_Activity.this, Setting_Activity.class);
                intent.putExtra("openType", "lang");
                intent.putExtra("from", from);
                startActivity(intent);
                finish();
            }
        });
        bottomDialog.show(languageTypes, "Save", 0);
    }

    private void openLogin() {
        if (openType != null && openType.equals("lang")) {
            if (backFlag == 1) {
                return;
            }
            backFlag = 1;
            //切换了语言
            Intent it = new Intent(this, MainTab.class);
            //it.putExtra("num", 3);
            setResult(Activity.RESULT_OK, it);//返回页面1
            /*it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
            startActivity(it);*/
        } else {
            Intent it = new Intent(this, MainTab.class);
            setResult(Activity.RESULT_OK, it);//返回页面1
            /*it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);*/
        }
        finish();
    }

    private void openHome() {
        if (openType != null && openType.equals("lang")) {
            if (backFlag == 1) {
                return;
            }
            backFlag = 1;
            app_sharePreferenceUtils.put(StaticParament.CHANGE_LANGUAGE, "0");
            app_sharePreferenceUtils.save();
            //切换了语言
            Intent it = new Intent(this, MainTab.class);
            if (from != null && from.equals("main")) {
                it.putExtra("num", 0);
            } else {
                it.putExtra("num", 3);
            }

            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
            startActivity(it);
        }/* else {
            Intent it = new Intent(this, MainTab.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
        }*/

        finish();
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            openHome();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public void onBackPressed() {
        // 完全由自己控制返回键逻辑，系统不再控制，但是有个前提是：　　
        // 不要在Activity的onKeyDown或者OnKeyUp中拦截掉返回键　　
        // 拦截：就是在OnKeyDown或者OnKeyUp中自己处理了返回键　　
        // （这里处理之后return true.或者return false都会导致onBackPressed不会执行）
        // 不拦截：在OnKeyDown和OnKeyUp中返回super对应的方法　　
        // （如果两个方法都被覆写就分别都要返回super.onKeyDown,super.onKeyUp）
        openHome();
    }
}
