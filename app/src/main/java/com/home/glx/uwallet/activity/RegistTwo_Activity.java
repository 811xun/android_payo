package com.home.glx.uwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.braze.Braze;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.home.glx.uwallet.BuildConfig;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.MyApplication;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.xzc.KycActivity_first_IDTypeChoose;
import com.home.glx.uwallet.activity.xzc.LoginActivity_inputNumber;
import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.datas.GetChoiceDatas;
import com.home.glx.uwallet.datas.UserAllMsgDatas;
import com.home.glx.uwallet.fragment.FirstFragment;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.mvp.GetChoiceList_in;
import com.home.glx.uwallet.mvp.GetChoiceList_p;
import com.home.glx.uwallet.mvp.Login_in;
import com.home.glx.uwallet.mvp.Login_p;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.BottomDialog;
import com.home.glx.uwallet.selfview.ThreeTermsAndConditionDialog;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.selfview.TipDialog;
import com.home.glx.uwallet.selfview.choicetime.ChangeDatePopwindow2;
import com.home.glx.uwallet.tools.AddMaidian;
import com.home.glx.uwallet.tools.BzEventType;
import com.home.glx.uwallet.tools.ChangeLineEdittext;
import com.home.glx.uwallet.tools.IdcardUtils;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.SystemUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

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

/**
 * 注册第二步
 */
public class RegistTwo_Activity extends MainActivity
        implements View.OnClickListener, Login_in.View,
        GetChoiceList_in.View {

    private ImageView idBack;
    private ChangeLineEdittext idEmail;
    private ChangeLineEdittext idFirstName;
    private ChangeLineEdittext id_middle_name;
    private ChangeLineEdittext idLastName;
    private ChangeLineEdittext postCode;
    private ChangeLineEdittext idBirthday;
    private CheckBox idProtocols;
    private TextView idRegist;
    private LinearLayout idMainLay;

    private Login_p present;
    private String phone;
    private String password;
    private String securityCode;
    //邀请码
    private String referralCode;
    private GetChoiceList_p choiceList_p;
    private List<ChoiceDatas> htmlList1 = new ArrayList<>();
    private TextView proText;
    private TextView almost;
    private boolean isTongguoJiaoyan = false;
    private RelativeLayout rl_icon;
    private ImageView icon;
    private String verificationCode;
    private boolean jumpToKyc = false;//从判断开通kyc的流程接口就过来的（除了登陆过来的）
    private boolean showBirthDialog = true;
    private TextInputLayout til_gender;
    private ChangeLineEdittext id_gender;
    private String genderCode = "";//3
    private List<ChoiceDatas> genderList = new ArrayList<>();
    public static boolean backToMainActivity = false;
    private TextInputLayout til_first_name;
    private boolean mGenderFirst = true;
    private boolean mBirthFirst = true;
//     NoLoadingDialog loadingDialog = new NoLoadingDialog(this);

    public static boolean showXieyi = true;

    @Override
    public int getLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.setStatusBarColor(getResources().getColor(R.color.white));
            //设置显示为白色背景，黑色字体
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        return R.layout.activity_regist_two;
    }

    @Override
    public void initView() {
        super.initView();
        getGaid();
        phone = getIntent().getStringExtra("phone");
        password = getIntent().getStringExtra("password");
        verificationCode = getIntent().getStringExtra("verificationCode");
        securityCode = getIntent().getStringExtra("securityCode");
        referralCode = getIntent().getStringExtra("referralCode");
        //获取state(洲)数据
        final Map<String, Object> maps3 = new HashMap<>();
        //获取state(洲)数据
        String[] keys2 = new String[]{"privacyAgreementNew", "payLaterApplicationForm", "payLaterTermsConditions", "payNowApplication", "payNowTermsConditions", "sex", "medicalCardType", "merchantState", "county", "customerApplicationFormULaypay", "bnpl"};
        maps3.put("code", keys2);
        getChoiceList(maps3);

        present = new Login_p(this, this);
        til_first_name = ((TextInputLayout) findViewById(R.id.til_first_name));
        postCode = (ChangeLineEdittext) findViewById(R.id.post_code);
        proText = (TextView) findViewById(R.id.proText);
        idBack = (ImageView) findViewById(R.id.id_back);
        idEmail = (ChangeLineEdittext) findViewById(R.id.id_email);
        idFirstName = (ChangeLineEdittext) findViewById(R.id.id_first_name);
        id_middle_name = (ChangeLineEdittext) findViewById(R.id.id_middle_name);
        idLastName = (ChangeLineEdittext) findViewById(R.id.id_last_name);
        idBirthday = (ChangeLineEdittext) findViewById(R.id.id_birthday);
        idProtocols = (CheckBox) findViewById(R.id.id_protocols);
        idRegist = (TextView) findViewById(R.id.id_regist);
        idMainLay = (LinearLayout) findViewById(R.id.id_main_lay);
        almost = findViewById(R.id.almost);
        idBirthday.setFocusable(true);
        idBirthday.setInputType(InputType.TYPE_NULL);//来禁止手机软键盘
        rl_icon = findViewById(R.id.rl_icon);
        icon = findViewById(R.id.icon);

        //切换字体
        TypefaceUtil.replaceFont(idFirstName, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(id_middle_name, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(idLastName, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(idEmail, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(idBirthday, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(postCode, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(proText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(idRegist, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(almost, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.optional), "fonts/gilroy_medium.ttf");
        til_first_name.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        ((TextInputLayout) findViewById(R.id.til_middle_name)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        ((TextInputLayout) findViewById(R.id.til_last_name)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        ((TextInputLayout) findViewById(R.id.til_email)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        ((TextInputLayout) findViewById(R.id.til_birthday)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        ((TextInputLayout) findViewById(R.id.til_post_code)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        ((TextInputLayout) findViewById(R.id.til_optional)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体

        til_gender = findViewById(R.id.til_gender);
        til_gender.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        id_gender = findViewById(R.id.id_gender);
        id_gender.setInputType(InputType.TYPE_NULL);//来禁止手机软键盘
        TypefaceUtil.replaceFont(id_gender, "fonts/gilroy_medium.ttf");

        idProtocols.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    inputCheck(false);
                }
            }
        });
        idFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    idFirstName.setHint("");//下面的提示文字
                    idFirstName.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected));
                    til_first_name.setHint(getString(R.string.re_t_first_name));
                    til_first_name.setHintTextColor(createColorStateList(RegistTwo_Activity.this, R.color.color_717171));
                }
            }
        });
        idFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                idFirstName.setHint("");//下面的提示文字
                idFirstName.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected));
                til_first_name.setHint(getString(R.string.re_t_first_name));
                til_first_name.setHintTextColor(createColorStateList(RegistTwo_Activity.this, R.color.color_717171));
                inputCheck(false);

            }
        });
        idLastName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ((TextInputLayout) findViewById(R.id.til_last_name)).setHintTextColor(createColorStateList(RegistTwo_Activity.this, R.color.color_717171));
                    ((TextInputLayout) findViewById(R.id.til_last_name)).setHint(getString(R.string.re_t_last_name));
                    idLastName.setHint("");//下面的提示文字
                    idLastName.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected));
                }
            }
        });
        idLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ((TextInputLayout) findViewById(R.id.til_last_name)).setHintTextColor(createColorStateList(RegistTwo_Activity.this, R.color.color_717171));
                ((TextInputLayout) findViewById(R.id.til_last_name)).setHint(getString(R.string.re_t_last_name));
                idLastName.setHint("");//下面的提示文字
                idLastName.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected));
                inputCheck(false);

            }
        });
        idEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ((TextInputLayout) findViewById(R.id.til_email)).setHintTextColor(createColorStateList(RegistTwo_Activity.this, R.color.color_717171));
                    ((TextInputLayout) findViewById(R.id.til_email)).setHint("Email");
                    idEmail.setHint("");//下面的提示文字
                    idEmail.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected));
                }
            }
        });
        idEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ((TextInputLayout) findViewById(R.id.til_email)).setHintTextColor(createColorStateList(RegistTwo_Activity.this, R.color.color_717171));
                ((TextInputLayout) findViewById(R.id.til_email)).setHint("Email");
                idEmail.setHint("");//下面的提示文字
                idEmail.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected));
                inputCheck(false);

            }
        });

        idBack.setOnClickListener(this);
        idRegist.setOnClickListener(this);
        findViewById(R.id.til_birthday).setOnClickListener(this);
        findViewById(R.id.id_birthday).setOnClickListener(this);

        choiceList_p = new GetChoiceList_p(this, this);
        Map<String, Object> maps = new HashMap<>();
        String[] keys = new String[]{"privacyAgreement", "termAndConditions", "privacyAgreement2021"};
        maps.put("code", keys);
        choiceList_p.loadChoiceList(maps);
        idBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (showBirthDialog) {
                        if (mBirthFirst) {
                            choiceBirthday();
                        }
                        mBirthFirst = false;
                    }
                } else {
                    ((TextInputLayout) findViewById(R.id.til_birthday)).setHintTextColor(createColorStateList(RegistTwo_Activity.this, R.color.color_717171));

                    ((TextInputLayout) findViewById(R.id.til_birthday)).setHint("Date of birth (dd/mm/yyyy)");
                    idBirthday.setHint("");//下面的提示文字
                    idBirthday.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected));
                }
            }
        });

        idBirthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ((TextInputLayout) findViewById(R.id.til_birthday)).setHintTextColor(createColorStateList(RegistTwo_Activity.this, R.color.color_717171));

                ((TextInputLayout) findViewById(R.id.til_birthday)).setHint("Date of birth (dd/mm/yyyy)");
                idBirthday.setHint("");//下面的提示文字
                idBirthday.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected));
                inputCheck(false);

            }
        });

        postCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    findViewById(R.id.ll).setVisibility(View.GONE);
                    postCode.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_selected));
                } else {
                    if (!TextUtils.isEmpty(postCode.getText().toString())) {
                        ((TextInputLayout) findViewById(R.id.til_optional)).setHint("");
                        ((ChangeLineEdittext) findViewById(R.id.optional)).setText("Optional");

                    } else {
                        ((TextInputLayout) findViewById(R.id.til_optional)).setHint("Optional");
                        ((ChangeLineEdittext) findViewById(R.id.optional)).setText("");
                    }
                    findViewById(R.id.ll).setVisibility(View.VISIBLE);
                    postCode.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected));
                }
            }
        });
        jumpToKyc = getIntent().getBooleanExtra("jumpToKyc", false);
        if (jumpToKyc) {//除了从登陆页进来的以外 都加埋点
            SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
            String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
            final Map<String, Object> maps1 = new HashMap<>();
            maps1.put("userId", userId);
            maps1.put("type", "4");//页面标识 1：二选一 2：证件类型页 3:地址页 4:个人信息填写页 5:绑卡页 6:illion默认机构页 7:illion 第一次验证页，8:illion第二次验证页 9:分期付开通等待页
            AddMaidian.addMaidian(this, maps1);

            Map<String, Object> maps2 = new HashMap<>();
            maps2.put("userId", userId);
            getKycNum(maps2);//获取今天kyc剩余次数。
            showInfo();//展示详情信息。

            findViewById(R.id.ll_code).setVisibility(View.GONE);
        } else {
            TiShiDialog tiShiDialog = new TiShiDialog(RegistTwo_Activity.this);
            tiShiDialog.ShowDialog("Please make sure your information details match your ID for verification.",
                    "Got it");
        }

        til_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_gender.setFocusable(true);
                id_gender.setFocusableInTouchMode(true);
                id_gender.requestFocus();
                showDialog(id_gender, genderList, "choosegender");
            }
        });

        id_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                til_gender.performClick();
            }
        });

        id_gender.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mGenderFirst) {
                        showDialog(id_gender, genderList, "choosegender");
                    }
                    mGenderFirst = false;
                } else {
                    til_gender.setHintTextColor(createColorStateList(RegistTwo_Activity.this, R.color.color_717171));

                    til_gender.setHint("Gender");
                    id_gender.setHint("");//下面的提示文字
                    id_gender.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected));
                }
            }
        });

        id_gender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_gender.setHintTextColor(createColorStateList(RegistTwo_Activity.this, R.color.color_717171));

                til_gender.setHint("Gender");
                id_gender.setHint("");//下面的提示文字
                id_gender.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected));
                inputCheck(false);

            }
        });

        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
        if (!TextUtils.isEmpty(userId)) {
            final Map<String, Object> maps1 = new HashMap<>();
            maps1.put("userId", userId);
            maps1.put("type", "4");//页面标识 1：二选一 2：证件类型页 3:地址页 4:个人信息填写页 5:绑卡页 6:illion默认机构页 7:illion 第一次验证页，8:illion第二次验证页 9:分期付开通等待页
            AddMaidian.addMaidian(this, maps1);
        }
    }

    private static ColorStateList createColorStateList(Context context, int color) {
        int[] colors = new int[]{ContextCompat.getColor(context, color), ContextCompat.getColor(context, color)};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_checked};
        states[1] = new int[]{-android.R.attr.state_checked};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        jumpToKyc = getIntent().getBooleanExtra("jumpToKyc", false);
        if (backToMainActivity) {//除了从登陆页进来的以外 都加埋点

            SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
            String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
            Map<String, Object> maps2 = new HashMap<>();
            maps2.put("userId", userId);
            getKycNum(maps2);//获取今天kyc剩余次数。
            showInfo();//展示详情信息。

            findViewById(R.id.ll_code).setVisibility(View.GONE);
            jumpToKyc = getIntent().getBooleanExtra("jumpToKyc", false);
        }

    }

    /**
     * 注册
     */
    public void regist2() {
        final Map<String, Object> maps = new HashMap<>();
        maps.put("phoneNumber", phone);
        maps.put("email", idEmail.getText().toString().trim());
        maps.put("stripeState", 0);//用户区分接入stripe前后的新老用户
        String[] time = idBirthday.getText().toString().trim().split("/");
        if (Integer.parseInt(time[0]) < 10) {
            time[0] = "0" + Integer.parseInt(time[0]);
        }
        if (Integer.parseInt(time[1]) < 10) {
            time[1] = "0" + Integer.parseInt(time[1]);
        }
        String times = time[0] + "-" + time[1] + "-" + time[2];
        maps.put("birth", times);
        maps.put("userFirstName", idFirstName.getText().toString().trim());
        maps.put("userMiddleName", id_middle_name.getText().toString().trim());
        maps.put("userLastName", idLastName.getText().toString().trim());
        maps.put("promotionCode", postCode.getText().toString().trim());
        maps.put("verificationCode", verificationCode);
        Log.d("savegender", "savegender: " + genderCode);
        maps.put("sex", genderCode);
        maps.put("readAgreementState", 1);
        maps.put("appVersionId", BuildConfig.currentVersionId);
//        maps.put("phoneModel", "android," + android.os.Build.BRAND + "," + android.os.Build.MODEL + "," + android.os.Build.VERSION.RELEASE);//手机厂商 +手机型号 +手机系统版本号
//
        maps.put("phoneModel", android.os.Build.BRAND);//机型
        maps.put("phoneSystem", 1);//手机系统 1安卓 2ios
        maps.put("phoneSystemVersion", android.os.Build.VERSION.RELEASE);//系统版本
        maps.put("mobileModel", android.os.Build.MODEL);//手机型号

        maps.put("pushToken", new SharePreferenceUtils(RegistTwo_Activity.this, StaticParament.APP).get(StaticParament.PUSH_TOKEN, ""));//谷歌推送Token，如果有，App才能收到消息
        maps.put("imeiNo", SystemUtils.getIMEI2(RegistTwo_Activity.this, gaId));
        if (!IdcardUtils.isAdult(time[2] + time[1] + time[0])) {
            TiShiDialog tiShiDialog = new TiShiDialog(RegistTwo_Activity.this);
            tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                @Override
                public void GuanBi() {

                    ThreeTermsAndConditionDialog threeTermsAndConditionDialog = new ThreeTermsAndConditionDialog(RegistTwo_Activity.this);
                    threeTermsAndConditionDialog.setOnGuanBi(new ThreeTermsAndConditionDialog.GuanBi() {
                        @Override
                        public void GuanBi() {

                        }

                        @Override
                        public void queding() {
                            saveRegisterInfo(maps);
                        }
                    });
                    threeTermsAndConditionDialog.ShowDialog("", htmlList);
                }
            });
            tiShiDialog.ShowDialog("You must be over 18 to pay in 4", "To pay with payo instalments, you\nmust be over 18.\nYou will still be able to pay in full\nwith your bank card.\nAre you sure you want to\ncontinue?", "Yes");
        } else {
            ThreeTermsAndConditionDialog threeTermsAndConditionDialog = new ThreeTermsAndConditionDialog(RegistTwo_Activity.this);
            threeTermsAndConditionDialog.setOnGuanBi(new ThreeTermsAndConditionDialog.GuanBi() {
                @Override
                public void GuanBi() {

                }

                @Override
                public void queding() {
                    saveRegisterInfo(maps);
                }
            });
            threeTermsAndConditionDialog.ShowDialog("", htmlList);
        }
    }

    private void saveRegisterInfo(final Map<String, Object> maps) {

        idRegist.setOnClickListener(null);
        idRegist.setText("");
        Animation rotateAnimation = AnimationUtils.loadAnimation(RegistTwo_Activity.this, R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);
        icon.startAnimation(rotateAnimation);
        rl_icon.setVisibility(View.VISIBLE);
//        loadingDialog.show(true);
        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(RegistTwo_Activity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("注册参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.NewRegist(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataStr);// 如果登录时没有注册 登录时没有这俩字段信息 所以需要保存。
                    SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(RegistTwo_Activity.this, StaticParament.USER);
                    sharePreferenceUtils.put(StaticParament.USER_TOKEN, jsonObject.getString("Authorization"));
                    sharePreferenceUtils.put(StaticParament.USER_ID, jsonObject.getString("userId"));
                    sharePreferenceUtils.save();

                    String deviceId = Braze.getInstance(MyApplication.context).getDeviceId();
                    L.log("deviceId==" + deviceId+"userId="+jsonObject.getString("userId"));
                    Braze.getInstance(MyApplication.context).changeUser(jsonObject.getString("userId"));
                    Braze.getInstance(MyApplication.context).requestImmediateDataFlush();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                idRegist.setOnClickListener(RegistTwo_Activity.this);
                idRegist.setText("Next");
                rl_icon.setVisibility(View.GONE);
//                loadingDialog.dismiss();

                resAFEvent(maps);//注册成功 加AF埋点
                Intent intent = new Intent(RegistTwo_Activity.this, SetPinPwd_Activity.class);//设置P码
                intent.putExtra("regist", 1);
                startActivity(intent);
            }

            @Override
            public void resError(String error) {
                Log.d("skskskks", "resError: " + error);
                if (error.contains("100410")) {
                    TipDialog tiShiDialog = new TipDialog(RegistTwo_Activity.this);
                    tiShiDialog.setOnClose(new TipDialog.Close() {
                        @Override
                        public void Close() {
                            Intent intent = new Intent(RegistTwo_Activity.this, LoginActivity_inputNumber.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    tiShiDialog.showDialog("", error.replace("100410", ""), "Confirm");
                }
                idRegist.setOnClickListener(RegistTwo_Activity.this);
                idRegist.setText("Next");
                rl_icon.setVisibility(View.GONE);
//                loadingDialog.dismiss();
            }
        });
    }

    private void showDialog(View view, List<ChoiceDatas> listData, final String flag) {
        if (!showBirthDialog) {
            return;
        }
        BottomDialog bottomDialog = new BottomDialog(this, view);
        bottomDialog.setOnChoiceItem(new BottomDialog.ChoiceItem() {
            @Override
            public void itemClick(String choiceText, String code, int choice_num) {
                id_gender.setText(choiceText);
                genderCode = code;
            }
        });
//        if (!bottomDialog.isShowing()) {
//        }
        bottomDialog.show(listData, 0);
    }

    /**
     * 提交AF事件
     */
    private void resAFEvent(Map<String, Object> eventValue) {
        L.log("注册完成AF统计：" + eventValue.toString());
      /*  eventValue.put(AFInAppEventParameterName.REGSITRATION_METHOD, "phone");
        AppsFlyerLib.getInstance().logEvent(getApplicationContext(),
                AFInAppEventType.COMPLETE_REGISTRATION, eventValue);*/

        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(RegistTwo_Activity.this, StaticParament.USER);
        String userid = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
//        eventValue.put(BzEventParameterName.REGSITRATION_METHOD, "phone");
        eventValue.put("userId", userid);

        Braze.getInstance(getApplicationContext()).getCurrentUser()
                .setCustomUserAttribute(BzEventType.COMPLETE_REGISTRATION, eventValue.toString());
    }

    /**
     * 登录返回
     *
     * @param status
     */
    @Override
    public void setLoginStatus(String status) {
        idRegist.setOnClickListener(RegistTwo_Activity.this);
        if (status.equals("1")) {
            Intent intent = new Intent(RegistTwo_Activity.this, MainTab.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        } else if (status.equals("6")) {
            postReceiver();
            //登录成功，没有设置PIN
            Intent intent = new Intent(RegistTwo_Activity.this, SetPinPwd_Activity.class);
            intent.putExtra("regist", 1);
            intent.putExtra("addbank", 1);
            startActivity(intent);
            finish();
        }
    }


    /**
     * 发送注册完成广播，关闭注册第一页
     */
    private void postReceiver() {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent("self.fenqi_regist_success");
        localBroadcastManager.sendBroadcast(intent);
    }

    private void back() {
        if (!getIntent().getBooleanExtra("zhijiefinish", false)) {
            if (FirstFragment.backStatue == 2) {
                Intent it = new Intent(RegistTwo_Activity.this, MainTab.class);
                it.putExtra("num", 2);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
                startActivity(it);
            } else if (FirstFragment.backStatue == 3) {
                Intent intent = new Intent(RegistTwo_Activity.this, PayMoneyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            } else if (FirstFragment.backStatue == 4) {
                Intent intent = new Intent(RegistTwo_Activity.this, PayFailedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            } else if (FirstFragment.backStatue == 1 || backToMainActivity) {
                Intent intent = new Intent(RegistTwo_Activity.this, MainTab.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("num", 0);
                startActivity(intent);
            }
        }
        finish();
        backToMainActivity = false;
        showXieyi = true;
    }


    @Override
    public void onBackPressed() {
        back();
        super.onBackPressed();
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_back:
                back();
                break;
            case R.id.id_birthday:
            case R.id.til_birthday:
                ((TextInputLayout) findViewById(R.id.til_birthday)).setHintTextColor(createColorStateList(RegistTwo_Activity.this, R.color.color_717171));

                ((TextInputLayout) findViewById(R.id.til_birthday)).setHint("Date of birth (dd/mm/yyyy)");
                idBirthday.setHint("");//下面的提示文字
                idBirthday.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected));
                choiceBirthday();
                idBirthday.setFocusable(true);
                idBirthday.setFocusableInTouchMode(true);
                break;
            case R.id.id_regist:
                //注册
                if (inputCheck(true)) {
                    if (!PublicTools.isFastClick()) {
                        return;
                    }
                    String[] time = idBirthday.getText().toString().trim().split("/");
                    if (Integer.parseInt(time[0]) < 10) {
                        time[0] = "0" + Integer.parseInt(time[0]);
                    }
                    if (Integer.parseInt(time[1]) < 10) {
                        time[1] = "0" + Integer.parseInt(time[1]);
                    }
                    final String times = time[0] + "-" + time[1] + "-" + time[2];
//                    if (jumpToKyc || backToMainActivity || !showXieyi) {//分期付没有开通，从注册跳到kyc
                    if (backToMainActivity || !showXieyi) {//从kyc失败页返回到此页backToMainActivity=true  从kyc页返回到此页showXieyi=false
                        jumpToKycActivity(time, times);
                    } else {
                        if (!jumpToKyc) {//分期付没有开通，从注册跳到kyc
                            regist2();
                        } else if (jumpToKyc) {
                            jumpToKycActivity(time, times);
                        }
                    }
                }
                backToMainActivity = false;
                showXieyi = true;
                break;
        }
    }

    private void jumpToKycActivity(String[] time, final String times) {
        if (!IdcardUtils.isAdult(time[2] + time[1] + time[0])) {
            TiShiDialog tiShiDialog = new TiShiDialog(RegistTwo_Activity.this);
            tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                @Override
                public void GuanBi() {

                    Intent intent_kyc = new Intent(RegistTwo_Activity.this, KycActivity_first_IDTypeChoose.class);
                    intent_kyc.putExtra("email", idEmail.getText().toString().trim());
                    intent_kyc.putExtra("userFirstName", idFirstName.getText().toString().trim());
                    intent_kyc.putExtra("userMiddleName", id_middle_name.getText().toString().trim());
                    intent_kyc.putExtra("userLastName", idLastName.getText().toString().trim());
                    intent_kyc.putExtra("sex", genderCode);
                    intent_kyc.putExtra("birth", times);
                    intent_kyc.putExtra("jumpToKyc", jumpToKyc);
                    startActivity(intent_kyc);
                }
            });
            tiShiDialog.ShowDialog("You must be over 18 to pay in 4", "To pay with payo instalments, you\nmust be over 18.\nYou will still be able to pay in full\nwith your bank card.\nAre you sure you want to\ncontinue?", "Yes");
        } else {
            Intent intent_kyc = new Intent(RegistTwo_Activity.this, KycActivity_first_IDTypeChoose.class);
            intent_kyc.putExtra("email", idEmail.getText().toString().trim());
            intent_kyc.putExtra("userFirstName", idFirstName.getText().toString().trim());
            intent_kyc.putExtra("userMiddleName", id_middle_name.getText().toString().trim());
            intent_kyc.putExtra("userLastName", idLastName.getText().toString().trim());
            intent_kyc.putExtra("sex", genderCode);
            intent_kyc.putExtra("birth", times);
            intent_kyc.putExtra("jumpToKyc", jumpToKyc);
            startActivity(intent_kyc);
        }
    }

    /**
     * 选择生日/有效期
     */
    private ChangeDatePopwindow2 mChangeBirthDialog;

    private void choiceBirthday() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        mChangeBirthDialog = new ChangeDatePopwindow2(RegistTwo_Activity.this, year, 1899, idBirthday, false);
        mChangeBirthDialog.setDate(year + "", month + "", day + "");
        if (!mChangeBirthDialog.isShowing()) {
            mChangeBirthDialog.showAtLocation(idMainLay, Gravity.BOTTOM, 0, 0);
        }
        mChangeBirthDialog.setBirthdayListener(new ChangeDatePopwindow2.OnBirthListener() {
            @Override
            public void onClick(String year, String month, String day) {
                idBirthday.setText(day + "/" + month + "/" + year);
            }
        });
//        Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH) + 1;
//        int day = c.get(Calendar.DAY_OF_MONTH);
//
////        String birth = idBirthday.getText().toString();
////        String bir[] = birth.split("/");
//
//        mChangeBirthDialog = new ChangeDatePopwindow2(this, year, 1899, idBirthday, false);
//        mChangeBirthDialog.setDate(year + "", month + "", day + "");
////        if (TextUtils.isEmpty(birth)) {
////        } else {
////            mChangeBirthDialog.setDate(bir[2] + "", bir[1] + "", bir[0] + "");
////        }
//        //            mChangeBirthDialog.setDate(bir[2] + "", bir[1] + "", bir[0] + "");
//
//        if (!mChangeBirthDialog.isShowing()) {
//            mChangeBirthDialog.showAtLocation(idMainLay, Gravity.BOTTOM, 0, 0);
//        }
//        mChangeBirthDialog.setBirthdayListener(new ChangeDatePopwindow2.OnBirthListener() {
//            @Override
//            public void onClick(String year, String month, String day) {
//                idBirthday.setText(day + "/" + month + "/" + year);
//            }
//        });

     /*   DatePickerUtil datePickerUtil = new DatePickerUtil(this); //日期选择 样式和设计图不一样，能实现对初始和截止日期的控制

        try {
            JSONObject jsonDateObj = new JSONObject();

            jsonDateObj.put("defaultDate", "2016-11-12");

            jsonDateObj.put("startDate", "2016-09-01");

            jsonDateObj.put("endDate", "2017-01-12");
            datePickerUtil.showDatePicker(jsonDateObj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * 校验输入
     *
     * @return
     */
    private boolean inputCheck(boolean check) {
        isTongguoJiaoyan = false;

        if (TextUtils.isEmpty(idFirstName.getText().toString().trim())) {
            idRegist.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.mipmap.btn_hui_jianbian));
            if (check) {
                idFirstName.requestFocus();
                idFirstName.setFocusable(true);
                idFirstName.setFocusableInTouchMode(true);

                idFirstName.post(new Runnable() {
                    @Override
                    public void run() {
                        idFirstName.setHint("Legal first name");//下面的提示文字
                        idFirstName.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        idFirstName.requestFocus();
                        til_first_name.setHintTextColor(createColorStateList(RegistTwo_Activity.this, R.color.end_orange));
                        til_first_name.setHint("Please fill out this section");//上边的提示文字
                    }
                });

            }
            return false;
        }

        if (TextUtils.isEmpty(idLastName.getText().toString().trim())) {
            idRegist.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.mipmap.btn_hui_jianbian));
            if (check) {
                idLastName.requestFocus();
                idLastName.setFocusable(true);
                idLastName.setFocusableInTouchMode(true);

                idLastName.post(new Runnable() {
                    @Override
                    public void run() {
                        ((TextInputLayout) findViewById(R.id.til_last_name)).setHintTextColor(createColorStateList(RegistTwo_Activity.this, R.color.end_orange));

                        ((TextInputLayout) findViewById(R.id.til_last_name)).setHint("Please fill out this section");//上边的提示文字
                        idLastName.setHint("Legal last name");//下面的提示文字
                        idLastName.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        idLastName.requestFocus();
                    }
                });
            }
            return false;
        }
        if (TextUtils.isEmpty(idEmail.getText().toString().trim())) {
            idRegist.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.mipmap.btn_hui_jianbian));
            if (check) {
                idEmail.requestFocus();
                idEmail.setFocusable(true);
                idEmail.setFocusableInTouchMode(true);

                idEmail.post(new Runnable() {
                    @Override
                    public void run() {
                        ((TextInputLayout) findViewById(R.id.til_email)).setHintTextColor(createColorStateList(RegistTwo_Activity.this, R.color.end_orange));

                        ((TextInputLayout) findViewById(R.id.til_email)).setHint("Please fill out this section");//上边的提示文字
                        idEmail.setHint("Email");//下面的提示文字
                        idEmail.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        idEmail.requestFocus();
                    }
                });
            }
            return false;
        }

//        if (!PublicTools.isEmail(idEmail.getText().toString().trim())) {
//            idRegist.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.mipmap.btn_hui_jianbian));
//            if (check) {
//                idEmail.requestFocus();
//                idEmail.setFocusable(true);
//                idEmail.setFocusableInTouchMode(true);
//
//                idEmail.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        ((TextInputLayout) findViewById(R.id.til_email)).setHintTextAppearance(R.style.box2);//黄字
//                        ((TextInputLayout) findViewById(R.id.til_email)).setHint("Enter a valid Email address");//上边的提示文字
//                        idEmail.setHint("Email");//下面的提示文字
//                        idEmail.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected_orange));//黄线
//                        idEmail.requestFocus();
//                    }
//                });
//            }
//            return false;
//        }

        if (idBirthday.getText().toString().trim().length() < 3) {
            idRegist.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.mipmap.btn_hui_jianbian));
            if (check) {
                showBirthDialog = false;
                idBirthday.requestFocus();
                idBirthday.setFocusable(true);
                idBirthday.setFocusableInTouchMode(true);

                idBirthday.post(new Runnable() {
                    @Override
                    public void run() {
                        ((TextInputLayout) findViewById(R.id.til_birthday)).setHintTextColor(createColorStateList(RegistTwo_Activity.this, R.color.end_orange));

                        ((TextInputLayout) findViewById(R.id.til_birthday)).setHint("Please fill out this section");//上边的提示文字
                        idBirthday.setHint("Date of birth (dd/mm/yyyy)");//下面的提示文字
                        idBirthday.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        showBirthDialog = false;
                        idBirthday.requestFocus();
                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showBirthDialog = true;
                    }
                }, 1500);
            }
            return false;
        }
        if (TextUtils.isEmpty(id_gender.getText().toString())) {
            idRegist.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.mipmap.btn_hui_jianbian));
            if (check) {
                showBirthDialog = false;

                id_gender.requestFocus();
                id_gender.setFocusable(true);
                id_gender.setFocusableInTouchMode(true);
                id_gender.post(new Runnable() {
                    @Override
                    public void run() {
                        til_gender.setHintTextColor(createColorStateList(RegistTwo_Activity.this, R.color.end_orange));

                        til_gender.setHint("Please fill out this section");//上边的提示文字
                        id_gender.setHint("Gender");//下面的提示文字
                        id_gender.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        showBirthDialog = false;
                        id_gender.requestFocus();
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showBirthDialog = true;
                    }
                }, 800);

            }
            return false;

        }

       /* if (!idProtocols.isChecked()) {
            if (check) {
                Typeface font = Typeface.createFromAsset(getAssets(), "fonts/gilroy_medium.ttf");
                SpannableString efr = new SpannableString(getString(R.string.please_agree_agreement));
                efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Toast.makeText(RegistTwo_Activity.this, efr, Toast.LENGTH_SHORT).show();
            }
            idRegist.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.mipmap.btn_hui_jianbian));
            return false;
        }*/
        isTongguoJiaoyan = true;
        idRegist.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.mipmap.btn_orange_jianbian));
        return true;
    }

    @Override
    public void setChoiceList(GetChoiceDatas getChoiceDatas) {
//        if (getChoiceDatas != null) {
//            if (getChoiceDatas.getPayNowTermsConditions() != null) {
//                htmlList1.addAll(getChoiceDatas.getPrivacyAgreement2021());
//                addressNewStyle(proText);
//            }
//        }
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
            ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
            Intent intent = new Intent();
            intent.setClass(RegistTwo_Activity.this, PdfActivity.class);
            intent.putExtra("url", htmlList1.get(0).getCName());
            startActivity(intent);
        }
    }

    /**
     * 协议样式修改
     *
     * @param textView
     */
    private void addressNewStyle(TextView textView) {
        textView.setHighlightColor(getResources().getColor(android.R.color.transparent));
        SpannableString spannableString = new SpannableString("I have read and agree the Privacy Agreement.");
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
        MyClickPPableSpan clickableSpan = new MyClickPPableSpan();

        spannableString.setSpan(clickableSpan, "I have read and agree the ".length(), spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
        spannableString.setSpan(colorSpan1, "I have read and agree the ".length(), spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new UnderlineSpan(), "I have read and agree the ".length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString);
    }


    /**
     * 删除消息
     */
    private void showInfo() {
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        final String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
        Map<String, Object> maps = new HashMap<>();

        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("删除消息参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.selectAccountUser(headerMap, id, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("删除消息:" + dataStr);
                Gson gson = new Gson();
                UserAllMsgDatas userAllMsgDatas = gson.fromJson(dataStr, UserAllMsgDatas.class);

                genderCode = userAllMsgDatas.getSex();
                idEmail.setText(userAllMsgDatas.getEmail());
                idFirstName.setText(userAllMsgDatas.getUserFirstName());
                id_middle_name.setText(userAllMsgDatas.getUserMiddleName());
                idLastName.setText(userAllMsgDatas.getUserLastName());
                idBirthday.setText(userAllMsgDatas.getBirth().replace("-", "/"));
                idProtocols.setChecked(true);
                idRegist.setBackground(ContextCompat.getDrawable(RegistTwo_Activity.this, R.mipmap.btn_orange_jianbian));
                for (ChoiceDatas choiceDatas : genderList) {
                    if (userAllMsgDatas.getSex().equals(choiceDatas.getValue())) {
                        id_gender.setText(choiceDatas.getEnName());

                    }
                }
            }

            @Override
            public void resError(String error) {

            }
        });
    }

    public void getKycNum(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(RegistTwo_Activity.this, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(RegistTwo_Activity.this, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("获取当日KYC剩余次数参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.kycResult(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("获取当日KYC剩余次数:" + dataStr);
//                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            //{"chance":1,"phone":"1800 733 669","message":"KYC verification limit exceeded, please try tomorrow."}
                            int chance = jsonObject.getInt("chance");
                            String phone = jsonObject.getString("phone");
                            String message = jsonObject.getString("message");
                            if (chance == 0)//剩余0次
                            {
                                Intent intent = new Intent(RegistTwo_Activity.this, KycAndIllionResultActivity.class);
                                intent.putExtra("error", "kycThree");
                                startActivity(intent);
                            } else {//可以继续kyc
                                if (getIntent().getBooleanExtra("showPop", true)) {
                                    TiShiDialog tiShiDialog = new TiShiDialog(RegistTwo_Activity.this);
                                    tiShiDialog.ShowDialog("Please make sure your information details match your ID for verification.",
                                            "Got it");
                                }
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

    private List<ChoiceDatas> htmlList = new ArrayList<>();

    /**
     * 提交澳大利亚身份信息
     */
    public void getChoiceList(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(RegistTwo_Activity.this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(RegistTwo_Activity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));

                Call<ResponseBody> call = requestInterface.findByCodeList(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                Log.d("gender", "resData: " + dataStr);
                Gson gson = new Gson();
                GetChoiceDatas getChoiceDatas = gson.fromJson(dataStr, GetChoiceDatas.class);
                if (getChoiceDatas != null) {
                    genderList.clear();
                    genderList.addAll(getChoiceDatas.getSex());
                    if (genderCode != null) {
                        for (ChoiceDatas choiceDatas : genderList) {
                            if (genderCode.equals(choiceDatas.getValue())) {
                                id_gender.setText(choiceDatas.getEnName());
                            }
                        }
                    }
                    htmlList.clear();
                    htmlList.addAll(getChoiceDatas.getPrivacyAgreementNew());
                    htmlList.addAll(getChoiceDatas.getPayNowApplication());
                    htmlList.addAll(getChoiceDatas.getPayNowTermsConditions());
                    htmlList.addAll(getChoiceDatas.getPayLaterApplicationForm());
                    htmlList.addAll(getChoiceDatas.getPayLaterTermsConditions());
                }
            }

            @Override
            public void resError(String error) {

            }
        });
    }


}
