package com.home.glx.uwallet.activity.xzc;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
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
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.ChooseIllionActivity;
import com.home.glx.uwallet.activity.KycAndIllionResultActivity;
import com.home.glx.uwallet.activity.NewAddBankCardActivity;
import com.home.glx.uwallet.activity.PdfActivity;
import com.home.glx.uwallet.activity.SelectPayTypeActivity;
import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.datas.GetChoiceDatas;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.BottomDialog;
import com.home.glx.uwallet.tools.AddMaidian;
import com.home.glx.uwallet.tools.ChangeLineEdittext;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class KycActivity_second_AddressChoose extends MainActivity {
    private TextInputLayout til_id_state;
    private ChangeLineEdittext et_state;

    private TextInputLayout til_address;
    private ChangeLineEdittext et_address;

    private TextInputLayout til_city;
    private ChangeLineEdittext et_city;


    private TextInputLayout til_postcode;
    private ChangeLineEdittext et_postcode;
    private ImageView iv_jindu;


    private Button btn_get_code;
    private TextView tv_payo;

    private List<ChoiceDatas> stateList = new ArrayList<>();


    private String IDState_code = "";//1
    private String userId;
    private RelativeLayout rl_icon;
    private ImageView icon;
    private boolean showBirthDialog = true;
    private boolean mStateFirst = true;
    private CheckBox id_protocols_check;
    private CheckBox id_protocols_check_2;
    private TextView id_protocol_text;

    @Override
    public int getLayout() {
        setStatusBarColor(KycActivity_second_AddressChoose.this, R.color.white);
        return R.layout.activity_kyc_second_address;
    }

    @Override
    public void initView() {
        super.initView();
        icon = findViewById(R.id.icon);
        rl_icon = findViewById(R.id.rl_icon);

        tv_payo = findViewById(R.id.tv_payo);
        TypefaceUtil.replaceFont(tv_payo, "fonts/gilroy_semiBold.ttf");

        til_id_state = findViewById(R.id.til_id_state);
        til_id_state.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        et_state = findViewById(R.id.et_id_state);
        et_state.setInputType(InputType.TYPE_NULL);//来禁止手机软键盘
        iv_jindu = findViewById(R.id.iv_jindu);
        TypefaceUtil.replaceFont(et_state, "fonts/gilroy_medium.ttf");


        til_city = findViewById(R.id.til_city);
        til_city.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        et_city = findViewById(R.id.et_city);
        TypefaceUtil.replaceFont(et_city, "fonts/gilroy_medium.ttf");

        til_address = findViewById(R.id.til_id_address);
        til_address.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        et_address = findViewById(R.id.et_id_address);
        TypefaceUtil.replaceFont(et_address, "fonts/gilroy_medium.ttf");

        ((TextInputLayout) findViewById(R.id.til_etc)).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        TypefaceUtil.replaceFont(findViewById(R.id.et_id_etc), "fonts/gilroy_medium.ttf");

        til_postcode = findViewById(R.id.til_postcode);
        til_postcode.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        et_postcode = findViewById(R.id.id_postcode);
        TypefaceUtil.replaceFont(et_postcode, "fonts/gilroy_medium.ttf");


        btn_get_code = findViewById(R.id.btn_get_code);
        TypefaceUtil.replaceFont(btn_get_code, "fonts/gilroy_semiBold.ttf");


        id_protocols_check = findViewById(R.id.id_protocols_check);
        id_protocols_check_2 = findViewById(R.id.id_protocols_check_2);
        id_protocol_text = findViewById(R.id.id_protocol_text);
        TypefaceUtil.replaceFont(id_protocol_text, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.id_protocol_text_2), "fonts/gilroy_medium.ttf");

        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
        final Map<String, Object> maps = new HashMap<>();
        maps.put("userId", userId);
        maps.put("type", "3");//页面标识 1：二选一 2：证件类型页 3:地址页 4:个人信息填写页 5:绑卡页 6:illion默认机构页 7:illion 第一次验证页，8:illion第二次验证页 9:分期付开通等待页
        AddMaidian.addMaidian(this, maps);

        initEvent();

        //获取state(洲)数据
        String[] keys = new String[]{"sex", "medicalCardType", "merchantState", "county", "customerApplicationFormULaypay", "payLaterApplicationForm"};
        maps.put("code", keys);
        getChoiceList(maps);

        if (SelectPayTypeActivity.shifoukaitongCardPay == 0) {
            iv_jindu.setImageResource(R.mipmap.twoof4);
        } else {
            iv_jindu.setImageResource(R.mipmap.twoof3);
        }

        addressStyle(id_protocol_text);
    }

    private void showDialog(View view, List<ChoiceDatas> listData, final String flag) {
        BottomDialog bottomDialog = new BottomDialog(this, view);
        bottomDialog.setOnChoiceItem(new BottomDialog.ChoiceItem() {
            @Override
            public void itemClick(String choiceText, String code, int choice_num) {
                if (flag.equals("chooseState")) {
                    et_state.setText(choiceText);
                    IDState_code = code;
                }
            }
        });
        bottomDialog.show(listData, 0);
    }

    private void initEvent() {
        til_id_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_state.setFocusable(true);
                et_state.setFocusableInTouchMode(true);
                et_state.requestFocus();
                showDialog(et_state, stateList, "chooseState");
            }
        });

        et_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                til_id_state.performClick();
            }
        });

        et_state.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (showBirthDialog) {
                        if (mStateFirst) {
                            showDialog(et_state, stateList, "chooseState");
                        }
                        mStateFirst = false;
                    }
                } else {
                    til_id_state.setHintTextColor(createColorStateList(KycActivity_second_AddressChoose.this, R.color.color_717171));
                    til_id_state.setHint("State");
                    et_state.setHint("");//下面的提示文字
                    et_state.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.drawable.edt_bg_un_selected));
                }
            }
        });

        et_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_id_state.setHintTextColor(createColorStateList(KycActivity_second_AddressChoose.this, R.color.color_717171));
                til_id_state.setHint("State");
                et_state.setHint("");//下面的提示文字
                et_state.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.drawable.edt_bg_un_selected));
                check(false);
            }
        });
        et_address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    til_address.setHintTextColor(createColorStateList(KycActivity_second_AddressChoose.this, R.color.color_717171));
                    til_address.setHint("Street address");
                    et_address.setHint("");//下面的提示文字
                    et_address.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.drawable.edt_bg_un_selected));
                }
            }
        });
        et_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_address.setHintTextColor(createColorStateList(KycActivity_second_AddressChoose.this, R.color.color_717171));
                check(false);

                til_address.setHint("Street address");
                et_address.setHint("");//下面的提示文字
                et_address.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.drawable.edt_bg_un_selected));
            }
        });
        et_city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    til_city.setHintTextColor(createColorStateList(KycActivity_second_AddressChoose.this, R.color.color_717171));

                    til_city.setHint("Suburb");
                    et_city.setHint("");//下面的提示文字
                    et_city.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.drawable.edt_bg_un_selected));
                }
            }
        });
        et_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_city.setHintTextColor(createColorStateList(KycActivity_second_AddressChoose.this, R.color.color_717171));
                check(false);

                til_city.setHint("Suburb");
                et_city.setHint("");//下面的提示文字
                et_city.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.drawable.edt_bg_un_selected));
            }
        });
        et_postcode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    til_postcode.setHintTextColor(createColorStateList(KycActivity_second_AddressChoose.this, R.color.color_717171));
                    til_postcode.setHint("Postcode");
                    et_postcode.setHint("");//下面的提示文字
                    et_postcode.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.drawable.edt_bg_un_selected));
                }
            }
        });
        et_postcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_postcode.setHintTextColor(createColorStateList(KycActivity_second_AddressChoose.this, R.color.color_717171));
                check(false);

                til_postcode.setHint("Postcode");
                et_postcode.setHint("");//下面的提示文字
                et_postcode.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.drawable.edt_bg_un_selected));
            }
        });


        btn_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check(true)) {
                    if (!PublicTools.isFastClick()) {
                        return;
                    }
//                    mEditInputYzm.setFocusable(false);
//                    mEditInputYzm.setFocusableInTouchMode(false);
                    btn_get_code.setText("");
                    btn_get_code.setEnabled(false);
                    Animation rotateAnimation = AnimationUtils.loadAnimation(KycActivity_second_AddressChoose.this, R.anim.rotate_anim);
                    rotateAnimation.setDuration(6500);
                    LinearInterpolator lin = new LinearInterpolator();
                    rotateAnimation.setInterpolator(lin);
                    icon.startAnimation(rotateAnimation);
                    rl_icon.setVisibility(View.VISIBLE);
                    id_protocols_check_2.setEnabled(false);
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("userId", userId);
                    map.put("checkType", 0);
                    map.put("userCitizenship", getIntent().getIntExtra("userCitizenship", 1));
//                    if (getIntent().getBooleanExtra("fromRegister", false)) {
                    if (getIntent().getStringExtra("email") != null) {
                        map.put("email", getIntent().getStringExtra("email"));
                        map.put("userFirstName", getIntent().getStringExtra("userFirstName"));
                        map.put("userMiddleName", getIntent().getStringExtra("userMiddleName"));
                        map.put("userLastName", getIntent().getStringExtra("userLastName"));
                        map.put("sex", getIntent().getStringExtra("sex"));
                        map.put("birth", getIntent().getStringExtra("birth"));
                        map.put("haveDataStatus", 0);//传递0 后台进行保存
                    } else {
                        map.put("haveDataStatus", 1);//传递1 后台去数据库找  判断firstname  lastname  birth是否同时相同。如果相同判断同一个人
                    }
                    switch (getIntent().getStringExtra("IDtype_code")) {
                        case "1"://驾照
                            map.put("driverLicence", getIntent().getStringExtra("driverLicence"));//驾照号
                            map.put("driverLicenceState", getIntent().getStringExtra("driverLicenceState"));//驾照所属州 code:state
                            break;
                        case "2"://护照
                            map.put("passport", getIntent().getStringExtra("passport"));//护照
                            map.put("passportIndate", getIntent().getStringExtra("passportIndate"));//护照有效期
//                             map.put("userCitizenship",  et_id_number.getText().toString());//护照签发国code:country
                            break;
                        case "3"://医保卡
                            map.put("medicare", getIntent().getStringExtra("medicare"));//医保卡
                            map.put("medicareType", getIntent().getIntExtra("medicareType", 1));//医保卡类型 0：黄色卡 1：绿色卡 2：蓝色卡code:medicalCardType
                            map.put("medicareRefNo", getIntent().getStringExtra("medicareRefNo"));//医保卡个人参考编号
                            map.put("medicareIndate", getIntent().getStringExtra("medicareIndate"));//医保卡有效期
                            break;
                    }
                    map.put("isAddAddress", "1");//医保卡有效期
                    map.put("address", et_address.getText().toString().trim());//医保卡有效期
                    map.put("city", et_city.getText().toString().trim());//医保卡有效期
                    map.put("state", IDState_code);//医保卡有效期
                    map.put("aptSuiteEtc", ((ChangeLineEdittext) findViewById(R.id.et_id_etc)).getText().toString().trim());//医保卡有效期
                    map.put("postcode", et_postcode.getText().toString().trim());
                    saveKycInfo(map);
                }
            }
        });

        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        id_protocols_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check(false);
            }
        });
        id_protocols_check_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                check(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private boolean check(boolean check) {
        if (TextUtils.isEmpty(et_address.getText().toString())) {
            btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.mipmap.btn_hui_jianbian));
            if (check) {
                et_address.requestFocus();
                et_address.setFocusable(true);
                et_address.setFocusableInTouchMode(true);
                et_address.post(new Runnable() {
                    @Override
                    public void run() {
                        til_address.setHintTextColor(createColorStateList(KycActivity_second_AddressChoose.this, R.color.end_orange));

                        til_address.setHint("Please fill out this section");//上边的提示文字
                        et_address.setHint("Street address");//下面的提示文字
                        et_address.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        et_address.requestFocus();
                    }
                });
            }
            return false;
        }
        if (TextUtils.isEmpty(et_city.getText().toString())) {
            btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.mipmap.btn_hui_jianbian));
            if (check) {
                et_city.requestFocus();
                et_city.setFocusable(true);
                et_city.setFocusableInTouchMode(true);
                et_city.post(new Runnable() {
                    @Override
                    public void run() {
                        til_city.setHintTextColor(createColorStateList(KycActivity_second_AddressChoose.this, R.color.end_orange));

                        til_city.setHint("Please fill out this section");//上边的提示文字
                        et_city.setHint("Suburb");//下面的提示文字
                        et_city.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        et_city.requestFocus();
                    }
                });
            }
            return false;
        }

        if (TextUtils.isEmpty(et_postcode.getText().toString())) {
            btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.mipmap.btn_hui_jianbian));
            if (check) {
                et_postcode.requestFocus();
                et_postcode.setFocusable(true);
                et_postcode.setFocusableInTouchMode(true);
                et_postcode.post(new Runnable() {
                    @Override
                    public void run() {
                        til_postcode.setHintTextColor(createColorStateList(KycActivity_second_AddressChoose.this, R.color.end_orange));

                        til_postcode.setHint("Please fill out this section");//上边的提示文字
                        et_postcode.setHint("Postcode");//下面的提示文字
                        et_postcode.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        et_postcode.requestFocus();
                    }
                });
            }
            return false;
        }

        if (TextUtils.isEmpty(et_state.getText().toString())) {
            btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.mipmap.btn_hui_jianbian));
            if (check) {
                showBirthDialog = false;
                et_state.requestFocus();
                et_state.setFocusable(true);
                et_state.setFocusableInTouchMode(true);
                et_state.post(new Runnable() {
                    @Override
                    public void run() {
                        til_id_state.setHintTextColor(createColorStateList(KycActivity_second_AddressChoose.this, R.color.end_orange));

                        til_id_state.setHint("Please fill out this section");//上边的提示文字
                        et_state.setHint("State");//下面的提示文字
                        et_state.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        showBirthDialog = false;
                        et_state.requestFocus();
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
        //        if (!id_protocols_check.isChecked()) {
//            if (check) {
//                Typeface font = Typeface.createFromAsset(getAssets(), "fonts/gilroy_medium.ttf");
//                SpannableString efr = new SpannableString(getString(R.string.please_agree_agreement));
//                efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                Toast.makeText(KycActivity_second_AddressChoose.this, efr, Toast.LENGTH_SHORT).show();
//            }
//            btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.mipmap.btn_hui_jianbian));
//            return false;
//        }
        if (!id_protocols_check_2.isChecked()) {
            if (check) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//大于等于9.0
                    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/gilroy_medium.ttf");
                    SpannableString efr = new SpannableString(getString(R.string.please_agree_agreement));
                    efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Toast.makeText(KycActivity_second_AddressChoose.this, efr, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(KycActivity_second_AddressChoose.this, getString(R.string.please_agree_agreement), Toast.LENGTH_SHORT).show();
                }
            }
            btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.mipmap.btn_hui_jianbian));
            return false;
        }
        btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_second_AddressChoose.this, R.mipmap.btn_orange_jianbian));

        return true;
    }

    private void saveKycInfo(HashMap<String, Object> map) {
        RequestUtils requestUtils1 = new RequestUtils(KycActivity_second_AddressChoose.this, map, StaticParament.zhifu,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(KycActivity_second_AddressChoose.this, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.saveKycInfo(headerMap, requestBody);
                        requestUtils.Call(call, null, false);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        L.log("用户分期信息:" + dataStr);
                        int status = 0;
                        String phone = null;
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            status = jsonObject.getInt("status");
                            //返回状态(0,”请求成功”),(1,”系统错误”),(2,”通用失败”),
                            // (3,”仅剩一次尝试次数”),(4,”错误次数达到上限”),(5,”人工审核”); (6,"名字相同")
                            phone = jsonObject.getString("phone");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        btn_get_code.setEnabled(true);
                        btn_get_code.setText("Next");
                        rl_icon.setVisibility(View.GONE);
                        id_protocols_check_2.setEnabled(true);
                        if (status == 0) {


                            if (SelectPayTypeActivity.shifoukaitongCardPay == 0) {//没有开通卡支付
                                Intent intent = new Intent(KycActivity_second_AddressChoose.this, NewAddBankCardActivity.class);
                                intent.putExtra("kaitongfenqifuRoad", true);
                                startActivity(intent);
                            } else {//开通卡支付
                                if (SelectPayTypeActivity.meikaitongfenqifuAndkaitongCardPay == 0) {//是否勾选过分期付绑卡协议 0：未勾选 1：已勾选
                                    Intent intent_kyc = new Intent(KycActivity_second_AddressChoose.this, ChooseCardActivity.class);
                                    startActivity(intent_kyc);
                                } else {
                                    Intent successIntent = new Intent(KycActivity_second_AddressChoose.this, ChooseIllionActivity.class);
                                    startActivity(successIntent);
                                    finish();
                                }
                            }
                        }/* else if (status == 6) {//(6,"名字相同")
                            Intent eIntent = new Intent(KycActivity_second_AddressChoose.this, KycExistsActivity.class);
                            startActivity(eIntent);
                            finish();
                        } */ else {
                            Intent intent = new Intent(KycActivity_second_AddressChoose.this, KycAndIllionResultActivity.class);
                            if (status == 1) {//1 2 3 6 从KycAndIllionResultActivity返回到信息录入页（注册页）  4、5:从哪儿来回哪儿去。
                                intent.putExtra("error", "kycSystem");
                            } else if (status == 2) {
                                intent.putExtra("error", "kycFirst");
                            } else if (status == 3) {
                                intent.putExtra("error", "kycTwo");
                            } else if (status == 4) {
                                intent.putExtra("error", "kycThree");
                            } else if (status == 5) {
                                intent.putExtra("error", "kycPeople");
                            } else if (status == 6) {
                                intent.putExtra("error", "kycSameName");
                            }
                            intent.putExtra("phone", phone);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void resError(String error) {
                        L.log("用户分期信息error:" + error);
                        btn_get_code.setEnabled(true);
                        btn_get_code.setText("Next");
                        rl_icon.setVisibility(View.GONE);
                        id_protocols_check_2.setEnabled(true);

                    }
                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void getChoiceList(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(KycActivity_second_AddressChoose.this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(KycActivity_second_AddressChoose.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));

                Call<ResponseBody> call = requestInterface.findByCodeList(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                Gson gson = new Gson();
                GetChoiceDatas getChoiceDatas = gson.fromJson(dataStr, GetChoiceDatas.class);
                if (getChoiceDatas != null) {
                    stateList.clear();
                    stateList.addAll(getChoiceDatas.getMerchantState());
                    htmlList.addAll(getChoiceDatas.getPayLaterApplicationForm());
                }
            }

            @Override
            public void resError(String error) {

            }
        });
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
     * 协议样式修改
     *
     * @param textView
     */
    private void addressStyle(TextView textView) {
        textView.setHighlightColor(getResources().getColor(android.R.color.transparent));
        SpannableString spannableString = new SpannableString("I have read and agree the Pay Later Customer Application Form.");
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
        MyClickableSpan clickableSpan2 = new MyClickableSpan();
        spannableString.setSpan(clickableSpan2, "I have read and agree the ".length(), spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
        spannableString.setSpan(colorSpan1, "I have read and agree the ".length(), spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), "I have read and agree the ".length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString);
    }

    private List<ChoiceDatas> htmlList = new ArrayList<>();

    class MyClickableSpan extends ClickableSpan {

        public MyClickableSpan() {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            L.log("点击");
           /* ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
            Intent intent = new Intent();
            intent.setClass(context, Web_Activity.class);
            intent.putExtra("url", StaticParament.YinSi);
            startActivity(intent);*/
            Intent intent = new Intent(KycActivity_second_AddressChoose.this, PdfActivity.class);
            intent.putExtra("url", htmlList.get(0).getCName());
            startActivity(intent);
        }
    }
}
