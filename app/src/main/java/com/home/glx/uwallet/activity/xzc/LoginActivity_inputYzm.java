package com.home.glx.uwallet.activity.xzc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.TypefaceSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.braze.Braze;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.home.glx.uwallet.BuildConfig;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.MyApplication;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.RegistTwo_Activity;
import com.home.glx.uwallet.datas.LoginDatas;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.mvp.GetHomeMerchantList;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.BzEventType;
import com.home.glx.uwallet.tools.ChangeLineEdittext;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.SystemUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class LoginActivity_inputYzm extends MainActivity {
    private String phonenum = "";
    private TextInputLayout mHintInputNumber;
    private ChangeLineEdittext mEditInputYzm;
    private Button btn_get_code;
    private ImageView icon;
    private TextView tv_payo;
    private TextView tv_show_phoneNo;
    private TextView show_daojishi;
    private TextView resend;

    private Timer SMStimer;
    private TimerTask SMStask;
    private int SmsTimeCount = 21;
    private RelativeLayout rl_icon;
    public static List<Activity> mActivityList = new ArrayList<>();

    @Override
    public int getLayout() {
//        setStatusBarColor(LoginActivity_inputYzm.this, R.color.white);
        return R.layout.activity_input_yzm;
    }

    @Override
    public void initView() {
        super.initView();
        phonenum = getIntent().getStringExtra("phonenum");

        icon = findViewById(R.id.icon);
        rl_icon = findViewById(R.id.rl_icon);

        tv_payo = findViewById(R.id.tv_payo);
        TypefaceUtil.replaceFont(tv_payo, "fonts/gilroy_semiBold.ttf");

        tv_show_phoneNo = findViewById(R.id.tv_show_phoneno);
        TypefaceUtil.replaceFont(tv_show_phoneNo, "fonts/gilroy_semiBold.ttf");
        tv_show_phoneNo.setText("Sent to +" + phonenum.substring(0, 2) + " " + phonenum.substring(2, 5) + " " + phonenum.substring(5, 8) + " " + phonenum.substring(8, 11));

        show_daojishi = findViewById(R.id.show_daojishi);
        TypefaceUtil.replaceFont(show_daojishi, "fonts/gilroy_semiBold.ttf");

        resend = findViewById(R.id.resend);
        TypefaceUtil.replaceFont(resend, "fonts/gilroy_semiBold.ttf");

        mHintInputNumber = findViewById(R.id.til_account);
        mHintInputNumber.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        mEditInputYzm = findViewById(R.id.et_yzm);
        TypefaceUtil.replaceFont(mEditInputYzm, "fonts/gilroy_medium.ttf");

        btn_get_code = findViewById(R.id.btn_get_code);
        TypefaceUtil.replaceFont(btn_get_code, "fonts/gilroy_semiBold.ttf");

        initEvent();
        startSMSCutDownTime();
    }

    private void initEvent() {
        mEditInputYzm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mEditInputYzm.setFocusable(true);
                mEditInputYzm.setFocusableInTouchMode(true);
                return false;
            }
        });
        mEditInputYzm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 6) {
                    btn_get_code.setBackground(ContextCompat.getDrawable(LoginActivity_inputYzm.this, R.mipmap.btn_orange_jianbian));
                } else {
                    btn_get_code.setBackground(ContextCompat.getDrawable(LoginActivity_inputYzm.this, R.mipmap.btn_hui_jianbian));
                }
            }
        });

        btn_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = mEditInputYzm.getText().toString().length();
                if (length == 6) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditInputYzm.getWindowToken(), 0);
                    mEditInputYzm.setFocusable(false);
                    mEditInputYzm.setFocusableInTouchMode(false);
                    btn_get_code.setText("");
                    btn_get_code.setEnabled(false);
                    Animation rotateAnimation = AnimationUtils.loadAnimation(LoginActivity_inputYzm.this, R.anim.rotate_anim10s);
                    LinearInterpolator lin = new LinearInterpolator();
                    rotateAnimation.setInterpolator(lin);
                    icon.startAnimation(rotateAnimation);
                    rl_icon.setVisibility(View.VISIBLE);

                    Map<String, Object> maps = new HashMap<>();
                    maps.put("phoneNumber", phonenum);//手机号（61开头、 11位）
                    maps.put("nodeType", "1");//节点类型 1： 用户注册
                    maps.put("verificationCode", mEditInputYzm.getText().toString());
                    maps.put("imeiNo", SystemUtils.getIMEI2(LoginActivity_inputYzm.this, gaId));
                    maps.put("pushToken", new SharePreferenceUtils(LoginActivity_inputYzm.this, StaticParament.APP).get(StaticParament.PUSH_TOKEN, ""));//谷歌推送Token，如果有，App才能收到消息
                    maps.put("appVersionId", BuildConfig.currentVersionId);

//                    maps.put("phoneModel", "android," + android.os.Build.BRAND + "," + android.os.Build.MODEL + "," + android.os.Build.VERSION.RELEASE);//手机厂商 +手机型号 +手机系统版本号
//
                    maps.put("phoneModel", android.os.Build.BRAND);//机型
                    maps.put("phoneSystem", 1);//手机系统 1安卓 2ios
                    maps.put("phoneSystemVersion", android.os.Build.VERSION.RELEASE);//系统版本
                    maps.put("mobileModel", android.os.Build.MODEL);//手机型号
                    jiaoyanYzm(maps);
                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> maps = new HashMap<>();
                maps.put("phoneNumber", phonenum);//手机号（61开头、 11位）
                maps.put("nodeType", "1");//节点类型 1： 用户注册
                getYzm(maps, phonenum);
            }
        });

        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 获取验证码
     *
     * @param maps
     */
    public void jiaoyanYzm(final Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {
                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.checkVerificationCode(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                resAFEvent(maps); //登陆成功 添加AF事件
                SharePreferenceUtils sharePreferenceUtils_app = new SharePreferenceUtils(LoginActivity_inputYzm.this, StaticParament.APP);//登陆成功 保存账号 用于复显
                sharePreferenceUtils_app.put(StaticParament.USER_PHONE, phonenum);
                sharePreferenceUtils_app.save();
                try {
                    if (new JSONObject(dataStr).has("registrationStatus") && new JSONObject(dataStr).getString("registrationStatus").equals("0")) {//未注册 跳转到注册页面。
                        Intent intent = new Intent(LoginActivity_inputYzm.this, RegistTwo_Activity.class);
                        intent.putExtra("phone", phonenum);
                        intent.putExtra("fromInputYzm", true);
                        intent.putExtra("verificationCode", mEditInputYzm.getText().toString());
                        startActivity(intent);
                        btn_get_code.setEnabled(true);
                        btn_get_code.setText("Next");
                        rl_icon.setVisibility(View.GONE);
                        mEditInputYzm.setText("");// 从注册页面返回到该页面后验证码是清空的
                    } else {//已经注册  保存一些信息。
                        /*if (icon.getAnimation() != null) {
                            icon.clearAnimation();
                        }*/
                        Animation rotateAnimation = AnimationUtils.loadAnimation(LoginActivity_inputYzm.this, R.anim.rotate_anim10s);
                        LinearInterpolator lin = new LinearInterpolator();
                        rotateAnimation.setInterpolator(lin);
                        icon.startAnimation(rotateAnimation);
                        Gson gson = new Gson();
                        LoginDatas loginDatas = gson.fromJson(dataStr, LoginDatas.class);
                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(LoginActivity_inputYzm.this, StaticParament.USER);
                        sharePreferenceUtils.put(StaticParament.USER_TOKEN, loginDatas.getAuthorization());
                        sharePreferenceUtils.put(StaticParament.USER_ID, loginDatas.getUserInfo().getId());
                        sharePreferenceUtils.put(StaticParament.USER_MSG, new Gson().toJson(loginDatas.getUserInfo()));
                        sharePreferenceUtils.put(StaticParament.USER_EMAIL, loginDatas.getUserInfo().getPhone());
                        sharePreferenceUtils.put(StaticParament.USER_NAME1, loginDatas.getFirstName());
                        sharePreferenceUtils.put("registerFrom", loginDatas.getUserInfo().getRegisterFrom());//注册来源  0：app;1：h5
                        if (loginDatas.getMiddleName() != null) {
                            sharePreferenceUtils.put(StaticParament.USER_NAME2, loginDatas.getMiddleName());
                        }
                        sharePreferenceUtils.put(StaticParament.USER_NAME3, loginDatas.getLastName());
                        sharePreferenceUtils.save();
                        if (getIntent().getStringExtra("open") != null
                                && getIntent().getStringExtra("open").equals("main")) {
                            MyApplication.openPage = "0";
                        }

                        String deviceId = Braze.getInstance(MyApplication.context).getDeviceId();
                        L.log("deviceId==" + deviceId+"&&userId="+loginDatas.getUserInfo().getId());
                        Braze.getInstance(MyApplication.context).changeUser(loginDatas.getUserInfo().getId());
                        Braze.getInstance(MyApplication.context).requestImmediateDataFlush();

                        if (MyApplication.openPage == "0") {
                            //刚打开APP
                            Intent intent = new Intent(LoginActivity_inputYzm.this, MainTab.class);
                            intent.putExtra("num", 0);
                            intent.putExtra("haveCheckUpdate", "1");
                            startActivity(intent);
                            for (Activity activity : mActivityList) {
                                activity.finish();
                            }
                        } else {
                            //防止中途异地登录，重新登录页面不对
                            Intent intent = new Intent(LoginActivity_inputYzm.this, MainTab.class);
                            intent.putExtra("haveCheckUpdate", "1");
                            intent.putExtra("num", 0);
//                            intent_register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//会出现白屏
//                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
                            startActivity(intent);
                            for (Activity activity : mActivityList) {
                                activity.finish();
                            }
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btn_get_code.setEnabled(true);
                                btn_get_code.setText("Next");
                                rl_icon.setVisibility(View.GONE);
                            }
                        }, 800);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                GetHomeMerchantList.getInstance().clearCache(LoginActivity_inputYzm.this);
                            }
                        }).start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void resError(String error) {
                btn_get_code.setEnabled(true);
                btn_get_code.setText("Next");
                rl_icon.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 提交AF事件
     */
    private void resAFEvent(Map<String, Object> eventValue) {
        L.log("登录AF参数：" + eventValue.toString());
//        AppsFlyerLib.getInstance().logEvent(this, AFInAppEventType.LOGIN, eventValue);
        Braze.getInstance(getApplicationContext()).getCurrentUser()
                .setCustomUserAttribute(BzEventType.LOGIN, eventValue.toString());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mEditInputYzm.setFocusable(false);
        mEditInputYzm.setFocusableInTouchMode(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//关闭软键盘
        imm.hideSoftInputFromWindow(mEditInputYzm.getWindowToken(), 0);
        return super.onTouchEvent(event);
    }

    /**
     * 获取验证码
     *
     * @param maps
     */
    public void getYzm(Map<String, Object> maps, final String phoneNo) {
        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {
                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.getYzm(headerMap, requestBody, phoneNo, "1");
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//大于等于9.0
                    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/gilroy_medium.ttf");
                    SpannableString efr = new SpannableString(getString(R.string.emile_code_put));
                    efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Toast.makeText(LoginActivity_inputYzm.this, efr, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity_inputYzm.this, getString(R.string.emile_code_put), Toast.LENGTH_SHORT).show();
                }
                startSMSCutDownTime();
            }

            @Override
            public void resError(String error) {

            }
        });
    }

    private void startSMSCutDownTime() {
        if (SMStimer == null) {
            SMStimer = new Timer();
            SMStask = new TimerTask() {
                @Override
                public void run() {
                    if (SmsTimeCount > 1) {
                        SmsTimeCount -= 1;
                        show_daojishi.post(new Runnable() {
                            @Override
                            public void run() {
                                resend.setVisibility(View.GONE);
                                show_daojishi.setVisibility(View.VISIBLE);
                                show_daojishi.setText("Resend code in " + SmsTimeCount + "s");
                            }
                        });
                    } else {
                        SMStimer.cancel();
                        SMStimer = null;
                        SmsTimeCount = 21;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resend.setVisibility(View.VISIBLE);
                                findViewById(R.id.show_daojishi).setVisibility(View.GONE);
                            }
                        });
                    }
                }
            };
            SMStimer.schedule(SMStask, 0, 1000);
        }
    }
}
