package com.home.glx.uwallet.activity.xzc;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.NewAddBankCardActivity;
import com.home.glx.uwallet.activity.RegistTwo_Activity;
import com.home.glx.uwallet.activity.SelectPayTypeActivity;
import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.datas.GetChoiceDatas;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.BottomDialog;
import com.home.glx.uwallet.selfview.choicetime.ChangeDatePopwindow2;
import com.home.glx.uwallet.tools.AddMaidian;
import com.home.glx.uwallet.tools.ChangeLineEdittext;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class KycActivity_first_IDTypeChoose extends MainActivity {
    private TextInputLayout mHintInputID;
    private ChangeLineEdittext mEditInputID;

    private TextInputLayout til_id_number;
    private ChangeLineEdittext et_id_number;

    private TextInputLayout til_id_state;
    private ChangeLineEdittext et_id_state;

    private TextInputLayout til_nationality;
    private ChangeLineEdittext et_nationality;

    private TextInputLayout til_date;
    private ChangeLineEdittext id_date;

    private TextInputLayout til_medicare_type;
    private ChangeLineEdittext id_medicare_type;

    private TextInputLayout til_reference_number;
    private ChangeLineEdittext id_reference_number;

    private Button btn_get_code;
    private TextView tv_payo;
    private ImageView iv_jindu;

    private List<ChoiceDatas> certificateList = new ArrayList<>();
    private List<ChoiceDatas> stateList = new ArrayList<>();
    private List<ChoiceDatas> nationalityList = new ArrayList<>();
    private List<ChoiceDatas> medicare_typeList = new ArrayList<>();

    private int lastIDNOPosition = -1; //三个小模块 共用一个id number布局  需要在切换类型时，把id no的值显示以前选择过的。
    private String IDtype_code = "";//1  2 3
    private String IDState_code = "";//1
    private String medicareCode = "";

    private boolean fromRegister;//从注册页面跳过来的。
    private int userCitizenship;
    private boolean showBirthDialog = true;
    private boolean mIDTYpeFirst = true;
    private boolean mStateFirst = true;
    private boolean mNationalityFirst = true;
    private boolean mBirthFirst = true;
    private boolean mMedicareTypeFirst = true;

    private String saveNumber1 = "";
    private String saveNumber2 = "";
    private String saveNumber3 = "";

    private String time1 = "";
    private String time2 = "";
    private ImageView type_image;

    @Override
    public int getLayout() {
        setStatusBarColor(KycActivity_first_IDTypeChoose.this, R.color.white);
        return R.layout.activity_kyc_first_idtype;
    }

    @Override
    public void initView() {
        super.initView();
        addCertificater();

        fromRegister = getIntent().getBooleanExtra("jumpToKyc", false);

        tv_payo = findViewById(R.id.tv_payo);
        TypefaceUtil.replaceFont(tv_payo, "fonts/gilroy_semiBold.ttf");

        mHintInputID = findViewById(R.id.til_id);
        mHintInputID.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        mEditInputID = findViewById(R.id.et_id);
        mEditInputID.setInputType(InputType.TYPE_NULL);//来禁止手机软键盘
        TypefaceUtil.replaceFont(mEditInputID, "fonts/gilroy_medium.ttf");


        type_image = findViewById(R.id.type_image);

        til_id_number = findViewById(R.id.til_id_number);
        til_id_number.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        et_id_number = findViewById(R.id.et_id_number);
        TypefaceUtil.replaceFont(et_id_number, "fonts/gilroy_medium.ttf");

        iv_jindu = findViewById(R.id.iv_jindu);

        til_id_state = findViewById(R.id.til_id_state);
        til_id_state.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        et_id_state = findViewById(R.id.et_id_state);
        et_id_state.setInputType(InputType.TYPE_NULL);//来禁止手机软键盘
        TypefaceUtil.replaceFont(et_id_state, "fonts/gilroy_medium.ttf");

        til_nationality = findViewById(R.id.til_nationality);
        til_nationality.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        et_nationality = findViewById(R.id.et_nationality);
        et_nationality.setInputType(InputType.TYPE_NULL);//来禁止手机软键盘
        TypefaceUtil.replaceFont(et_nationality, "fonts/gilroy_medium.ttf");

        til_date = findViewById(R.id.til_date);
        til_date.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        id_date = findViewById(R.id.id_date);
        id_date.setInputType(InputType.TYPE_NULL);//来禁止手机软键盘
        TypefaceUtil.replaceFont(id_date, "fonts/gilroy_medium.ttf");

        til_reference_number = findViewById(R.id.til_reference_number);
        til_reference_number.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        id_reference_number = findViewById(R.id.id_reference_number);
        TypefaceUtil.replaceFont(id_reference_number, "fonts/gilroy_medium.ttf");

        til_medicare_type = findViewById(R.id.til_medicare_type);
        til_medicare_type.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        id_medicare_type = findViewById(R.id.id_medicare_type);
        id_medicare_type.setInputType(InputType.TYPE_NULL);//来禁止手机软键盘
        TypefaceUtil.replaceFont(id_medicare_type, "fonts/gilroy_medium.ttf");

        btn_get_code = findViewById(R.id.btn_get_code);
        TypefaceUtil.replaceFont(btn_get_code, "fonts/gilroy_semiBold.ttf");

        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
        final Map<String, Object> maps = new HashMap<>();
        maps.put("userId", userId);
        maps.put("type", "2");//页面标识 1：二选一 2：证件类型页 3:地址页 4:个人信息填写页 5:绑卡页 6:illion默认机构页 7:illion 第一次验证页，8:illion第二次验证页 9:分期付开通等待页
        AddMaidian.addMaidian(this, maps);

        initEvent();

        final Map<String, Object> maps2 = new HashMap<>();
        //获取state(洲)数据
        String[] keys = new String[]{"sex", "medicalCardType", "merchantState", "county", "customerApplicationFormULaypay", "bnpl"};
        maps2.put("code", keys);
        getChoiceList(maps2);

        if (SelectPayTypeActivity.shifoukaitongCardPay == 0) {
            iv_jindu.setImageResource(R.mipmap.oneof4);
        } else {
            iv_jindu.setImageResource(R.mipmap.oneof3);
        }
    }

    private void showDialog(View view, List<ChoiceDatas> listData, final String flag) {
        if (!showBirthDialog) {
            return;
        }
        BottomDialog bottomDialog = new BottomDialog(this, view);
        bottomDialog.setFlag(true);
        bottomDialog.setOnChoiceItem(new BottomDialog.ChoiceItem() {
            @Override
            public void itemClick(String choiceText, String code, int choice_num) {
                if (flag.equals("chooseIDtype")) {
                    if (IDtype_code.equals(code)) {
                        return;
                    }
                    mEditInputID.setText(choiceText);
                    IDtype_code = code;
                    til_id_number.setVisibility(View.VISIBLE);
                    check(false);
                    switch (code) {
                        case "1":
                            type_image.setVisibility(View.GONE);

                            til_id_state.setVisibility(View.VISIBLE);
                            til_nationality.setVisibility(View.GONE);
                            til_date.setVisibility(View.GONE);
                            til_reference_number.setVisibility(View.GONE);
                            til_medicare_type.setVisibility(View.GONE);
                            if (lastIDNOPosition == 3) {
                                time2 = id_date.getText().toString();
                            }
                            if (lastIDNOPosition == 2) {
                                time1 = id_date.getText().toString();
                            }
                            til_id_number.setHint("ID No.");
                            if (lastIDNOPosition == 2) {
                                saveNumber2 = et_id_number.getText().toString().trim();
                            } else if (lastIDNOPosition == 3) {
                                saveNumber3 = et_id_number.getText().toString().trim();
                            }
                            et_id_number.setText(saveNumber1);
                            //只能输入普通文本
                            et_id_number.setInputType(InputType.TYPE_CLASS_TEXT);
                            //最大输入9位
                            et_id_number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
                            userCitizenship = 1;
                            break;
                        case "2":
                            type_image.setVisibility(View.GONE);

                            til_id_state.setVisibility(View.GONE);
                            til_nationality.setVisibility(View.VISIBLE);
                            til_date.setVisibility(View.VISIBLE);
//                            time1 = id_date.getText().toString();
                            if (lastIDNOPosition == 3) {
                                time2 = id_date.getText().toString();
                            }
                            id_date.setText(time1);
                            til_date.setHint("Date of expiry");
                            til_id_number.setHint("Document No.");
                            if (lastIDNOPosition == 1) {
                                saveNumber1 = et_id_number.getText().toString().trim();
                            } else if (lastIDNOPosition == 3) {
                                saveNumber3 = et_id_number.getText().toString().trim();
                            }
                            et_id_number.setText(saveNumber2);

                            til_reference_number.setVisibility(View.GONE);
                            til_medicare_type.setVisibility(View.GONE);

                            et_id_number.setInputType(InputType.TYPE_CLASS_TEXT);
                            //最大输入8位
                            et_id_number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
                            break;
                        case "3":
                            type_image.setVisibility(View.VISIBLE);
                            til_id_state.setVisibility(View.GONE);
                            til_nationality.setVisibility(View.GONE);
                            til_date.setVisibility(View.VISIBLE);
                            til_reference_number.setVisibility(View.VISIBLE);
                            til_medicare_type.setVisibility(View.VISIBLE);
                            if (lastIDNOPosition == 2) {
                                time1 = id_date.getText().toString();
                            }
                            id_date.setText(time2);
                            til_date.setHint("Valid to");
                            til_id_number.setHint("Medicare card number");
                            if (lastIDNOPosition == 1) {
                                saveNumber1 = et_id_number.getText().toString().trim();
                            } else if (lastIDNOPosition == 2) {
                                saveNumber2 = et_id_number.getText().toString().trim();
                            }
                            et_id_number.setText(saveNumber3);
                            //只能输入数字
                            et_id_number.setInputType(InputType.TYPE_CLASS_NUMBER);
                            //最大输入10位
                            et_id_number.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                            userCitizenship = 1;
                            break;
                    }
                    lastIDNOPosition = Integer.valueOf(code);
                } else if (flag.equals("chooseState")) {
                    et_id_state.setText(choiceText);
                    IDState_code = code;
                } else if (flag.equals("choosenationality")) {
                    et_nationality.setText(choiceText);
                    userCitizenship = Integer.valueOf(code);
                } else if (flag.equals("choosemedicare")) {
                    id_medicare_type.setText(choiceText);
                    medicareCode = code;
                    if (choiceText.equals("RHCA")) {
                        type_image.setImageResource(R.mipmap.yellow);
                    } else if (choiceText.equals("Australian Resident")) {
                        type_image.setImageResource(R.mipmap.green);
                    } else if (choiceText.equals("Interim")) {
                        type_image.setImageResource(R.mipmap.blue);
                    }
                }
            }
        });
//        if (!bottomDialog.isShowing()) {
//        }
        bottomDialog.show(listData, 0);
    }

    /**
     * 添加证件选项
     */
    private void addCertificater() {
        certificateList.clear();
        certificateList.add(new ChoiceDatas("1", "澳大利亚驾照", "Australian Driver's licence"));
        certificateList.add(new ChoiceDatas("2", "护照", "Passport"));
        certificateList.add(new ChoiceDatas("3", "澳大利亚医保卡", "Australian medicare card"));
    }

    private void initEvent() {
        mHintInputID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditInputID.setFocusable(true);
                mEditInputID.setFocusableInTouchMode(true);
                mEditInputID.requestFocus();
                showDialog(mEditInputID, certificateList, "chooseIDtype");
            }
        });

        mEditInputID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHintInputID.performClick();
            }
        });

        mEditInputID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mIDTYpeFirst) {
                        showDialog(mEditInputID, certificateList, "chooseIDtype");
                    }
                    mIDTYpeFirst = false;
                } else {
                    mHintInputID.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.color_717171));
                    mHintInputID.setHint("Select ID type");
                    mEditInputID.setHint("");//下面的提示文字
                    mEditInputID.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected));
                }
            }
        });
        mEditInputID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mHintInputID.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.color_717171));
                mHintInputID.setHint("Select ID type");
                mEditInputID.setHint("");//下面的提示文字
                mEditInputID.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected));
                check(false);
            }
        });
        et_id_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    til_id_number.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.color_717171));
                    if (IDtype_code.equals("1")) {
                        til_id_number.setHint("ID No.");
                    } else if (IDtype_code.equals("2")) {
                        til_id_number.setHint("Document No.");
                    } else {
                        til_id_number.setHint("Medicare card number");
                    }
                    et_id_number.setHint("");//下面的提示文字
                    et_id_number.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected));
                }
            }
        });
        et_id_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_id_number.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.color_717171));

                if (IDtype_code.equals("1")) {
                    til_id_number.setHint("ID No.");
                } else if (IDtype_code.equals("2")) {
                    til_id_number.setHint("Document No.");
                } else {
                    til_id_number.setHint("Medicare card number");
                }
                et_id_number.setHint("");//下面的提示文字
                check(false);
                et_id_number.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected));
            }
        });

        til_id_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_id_state.setFocusable(true);
                et_id_state.setFocusableInTouchMode(true);
                et_id_state.requestFocus();
                showDialog(et_id_state, stateList, "chooseState");
            }
        });

        et_id_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                til_id_state.performClick();
            }
        });

        et_id_state.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mStateFirst) {
                        showDialog(et_id_state, stateList, "chooseState");
                    }
                    mStateFirst = false;
                } else {
                    til_id_state.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.color_717171));

                    til_id_state.setHint("State of issurance");
                    et_id_state.setHint("");//下面的提示文字
                    et_id_state.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected));
                }

            }
        });

        et_id_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_id_state.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.color_717171));

                til_id_state.setHint("State of issurance");
                et_id_state.setHint("");//下面的提示文字
                check(false);
                et_id_state.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected));
            }
        });

        til_nationality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_nationality.setFocusable(true);
                et_nationality.setFocusableInTouchMode(true);
                et_nationality.requestFocus();
                showDialog(et_nationality, nationalityList, "choosenationality");
            }
        });
        et_nationality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                til_nationality.performClick();
            }
        });

        et_nationality.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    if (mNationalityFirst) {
                        showDialog(et_nationality, nationalityList, "choosenationality");
                    }
                    mNationalityFirst = false;
                } else {
                    til_nationality.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.color_717171));

                    til_nationality.setHint("Nationality");
                    et_nationality.setHint("");//下面的提示文字
                    et_nationality.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected));
                }
            }
        });
        et_nationality.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_nationality.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.color_717171));

                til_nationality.setHint("Nationality");
                et_nationality.setHint("");//下面的提示文字
                check(false);
                et_nationality.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected));
            }
        });

        til_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_date.setFocusable(true);
                id_date.setFocusableInTouchMode(true);
                id_date.requestFocus();
                if (IDtype_code.equals("2")) {
                    choiceBirthday();
                } else if (IDtype_code.equals("3")) {
                    choiceBirthdayOfYearMonth();
                }
            }
        });

        id_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                til_date.performClick();
            }
        });

        id_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (showBirthDialog) {
                        if (mBirthFirst) {
                            if (IDtype_code.equals("2")) {
                                choiceBirthday();
                            } else if (IDtype_code.equals("3")) {
                                choiceBirthdayOfYearMonth();
                            }
                        }
                        mBirthFirst = false;
                    }
                } else {
                    til_date.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.color_717171));
                    if (IDtype_code.equals("2")) {
                        til_date.setHint("Date of expiry");
                    } else {
                        til_date.setHint("Valid to");
                    }
                    id_date.setHint("");//下面的提示文字
                    id_date.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected));
                }
            }
        });

        id_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_date.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.color_717171));

                if (IDtype_code.equals("2")) {
                    til_date.setHint("Date of expiry");
                } else {
                    til_date.setHint("Valid to");
                }
                id_date.setHint("");//下面的提示文字
                check(false);
                id_date.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected));
            }
        });

        til_medicare_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_medicare_type.setFocusable(true);
                id_medicare_type.setFocusableInTouchMode(true);
                id_medicare_type.requestFocus();
                showDialog(id_medicare_type, medicare_typeList, "choosemedicare");
            }
        });

        id_medicare_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                til_medicare_type.performClick();
            }
        });

        id_medicare_type.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (mMedicareTypeFirst) {
                        showDialog(id_medicare_type, medicare_typeList, "choosemedicare");
                    }
                    mMedicareTypeFirst = false;
                } else {
                    til_medicare_type.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.color_717171));

                    til_medicare_type.setHint("Medicare type");
                    id_medicare_type.setHint("");//下面的提示文字
                    id_medicare_type.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected));
                }
            }
        });

        id_medicare_type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_medicare_type.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.color_717171));

                til_medicare_type.setHint("Medicare type");
                id_medicare_type.setHint("");//下面的提示文字
                check(false);
                id_medicare_type.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected));
            }
        });
        id_reference_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    til_reference_number.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.color_717171));

                    til_reference_number.setHint("Personal reference number");
                    id_reference_number.setHint("");//下面的提示文字
                    id_reference_number.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected));
                }
            }
        });
        id_reference_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_reference_number.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.color_717171));

                til_reference_number.setHint("Personal reference number");
                id_reference_number.setHint("");//下面的提示文字
                check(false);
                id_reference_number.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected));
            }
        });
        btn_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check(true)) {
                    if (!PublicTools.isFastClick()) {
                        return;
                    }
                    Intent intent = new Intent(KycActivity_first_IDTypeChoose.this, KycActivity_second_AddressChoose.class);
                    if (getIntent().getStringExtra("email") != null) {
                        intent.putExtra("email", getIntent().getStringExtra("email"));
                        intent.putExtra("userFirstName", getIntent().getStringExtra("userFirstName"));
                        intent.putExtra("userMiddleName", getIntent().getStringExtra("userMiddleName"));
                        intent.putExtra("userLastName", getIntent().getStringExtra("userLastName"));
                        intent.putExtra("sex", getIntent().getStringExtra("sex"));
                        intent.putExtra("birth", getIntent().getStringExtra("birth"));
                    }
                    intent.putExtra("fromRegister", fromRegister);
                    intent.putExtra("userCitizenship", userCitizenship);
                    intent.putExtra("haveDataStatus", fromRegister ? 0 : 1);
                    intent.putExtra("IDtype_code", IDtype_code);//不用保存到后台

                    switch (IDtype_code) {
                        case "1"://驾照
                            intent.putExtra("driverLicence", et_id_number.getText().toString());//驾照号
                            intent.putExtra("driverLicenceState", IDState_code);//驾照所属州 code:state
                            break;
                        case "2"://护照
                            intent.putExtra("passport", et_id_number.getText().toString());//护照

                            String[] time = id_date.getText().toString().trim().split("/");
                            if (Integer.parseInt(time[0]) < 10) {
                                time[0] = "0" + Integer.parseInt(time[0]);
                            }
                            if (Integer.parseInt(time[1]) < 10) {
                                time[1] = "0" + Integer.parseInt(time[1]);
                            }
                            String times = time[0] + "-" + time[1] + "-" + time[2];
                            intent.putExtra("passportIndate", times);//护照有效期
//                            intent.putExtra("userCitizenship",  et_id_number.getText().toString());//护照签发国code:country
                            break;
                        case "3"://医保卡
                            intent.putExtra("medicare", et_id_number.getText().toString());//医保卡
                            if (!TextUtils.isEmpty(medicareCode)){
                            intent.putExtra("medicareType", Integer.valueOf(medicareCode));//医保卡类型 0：黄色卡 1：绿色卡 2：蓝色卡code:medicalCardType
                            }
                            intent.putExtra("medicareRefNo", id_reference_number.getText().toString());//医保卡个人参考编号

                            String[] time2 = id_date.getText().toString().trim().split("/");
                            if (Integer.parseInt(time2[0]) < 10) {
                                time2[0] = "0" + Integer.parseInt(time2[0]);
                            }

                            String times3 = time2[0] + "-" + time2[1];
                            intent.putExtra("medicareIndate", times3);//医保卡有效期
                            break;
                    }
                    startActivity(intent);
                }
            }
        });

        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void back() {
        if (!fromRegister) {
            RegistTwo_Activity.showXieyi = false;
        }
//        if (!fromRegister) {//从登陆----》注册页过来 返回到首页
//        if (!getIntent().getBooleanExtra("fromKycResult", false)) {//从登陆----》注册页过来 返回到首页
        if (getIntent().getBooleanExtra("erxuanyi", false)) {//从登陆----》注册页过来 二选一页面返回到首页  从信息返显页过来的都是返回到信息返显页
            new SharePreferenceUtils(KycActivity_first_IDTypeChoose.this, StaticParament.USER).put(StaticParament.PAY_SUCCESS, "1").commit();//为了wallet页面刷新
            Intent intent = new Intent(KycActivity_first_IDTypeChoose.this, MainTab.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("num", 0);
            startActivity(intent);
        }
//        else {//从信息返现页 过来返回到信息返现页 并且不显示弹窗
////            Intent intent_register = new Intent(KycActivity_first_IDTypeChoose.this, RegistTwo_Activity.class); //跳到注册页 从注册页跳到kyc页
////            intent_register.putExtra("jumpToKyc", true);
////            intent_register.putExtra("showPop", false);
////            startActivity(intent_register);
//        }
        finish();
      /*  if (FirstFragment.backStatue == 1) {
            Intent intent = new Intent(KycActivity_first_IDTypeChoose.this, MainTab.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("num", 0);
            startActivity(intent);
            finish();
        } else if (FirstFragment.backStatue == 2) {
            Intent it = new Intent(KycActivity_first_IDTypeChoose.this, MainTab.class);
            it.putExtra("num", 2);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
            startActivity(it);
            finish();
        } else if (FirstFragment.backStatue == 3) {//返回到反显个人信息页（注册页面）
//            Intent intent = new Intent(KycActivity_first_IDTypeChoose.this, PayMoneyActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
            Intent intent_register = new Intent(KycActivity_first_IDTypeChoose.this, RegistTwo_Activity.class); //跳到注册页 从注册页跳到kyc页
            intent_register.putExtra("jumpToKyc", true);
            startActivity(intent_register);
            finish();
        }*/
    }

    @Override
    public void onBackPressed() {
        back();
    }

    /**
     * 提交澳大利亚身份信息
     */
    public void getChoiceList(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(KycActivity_first_IDTypeChoose.this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(KycActivity_first_IDTypeChoose.this, StaticParament.USER);
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
                    nationalityList.clear();
                    nationalityList.addAll(getChoiceDatas.getCounty());
                    medicare_typeList.clear();
                    medicare_typeList.addAll(getChoiceDatas.getMedicalCardType());
                }
            }

            @Override
            public void resError(String error) {

            }
        });
    }

    private ChangeDatePopwindow2 mChangeBirthDialog;//截止日期

    private void choiceBirthday() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        mChangeBirthDialog = new ChangeDatePopwindow2(this, year + 200, 1899, id_date, false);

        mChangeBirthDialog.setDate(year + "", month + "", day + "");
        if (!mChangeBirthDialog.isShowing()) {
            mChangeBirthDialog.showAtLocation(findViewById(R.id.rl_wai), Gravity.BOTTOM, 0, 0);
        }
        mChangeBirthDialog.setBirthdayListener(new ChangeDatePopwindow2.OnBirthListener() {
            @Override
            public void onClick(String year, String month, String day) {
                id_date.setText(day + "/" + month + "/" + year);
            }
        });
    }

    private void choiceBirthdayOfYearMonth() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        ChangeDatePopwindow2 mChangeBirthDialog = new ChangeDatePopwindow2(this, year + 200, year - 1, id_date, true);
//        mChangeBirthDialog.setDayGONE();
        mChangeBirthDialog.setDate(year + "", month + "", day + "");
        if (!mChangeBirthDialog.isShowing()) {
            mChangeBirthDialog.showAtLocation(findViewById(R.id.rl_wai), Gravity.BOTTOM, 0, 0);
        }
        mChangeBirthDialog.setBirthdayListener(new ChangeDatePopwindow2.OnBirthListener() {
            @Override
            public void onClick(String year, String month, String day) {
                id_date.setText(month + "/" + year);
            }
        });
    }

    private boolean check(boolean check) {

        if (TextUtils.isEmpty(mEditInputID.getText().toString())) {
            btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.mipmap.btn_hui_jianbian));
            if (check) {
                showBirthDialog = false;
                mEditInputID.setFocusable(true);
                mEditInputID.setFocusableInTouchMode(true);
                mEditInputID.requestFocus();
                mEditInputID.post(new Runnable() {
                    @Override
                    public void run() {
                        ((TextInputLayout) findViewById(R.id.til_id)).setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.end_orange));

                        ((TextInputLayout) findViewById(R.id.til_id)).setHint("Please fill out this section");//上边的提示文字
                        mEditInputID.setHint("Select ID type");//下面的提示文字
                        mEditInputID.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected_orange));//黄线
                        showBirthDialog = false;
                        mEditInputID.setFocusable(true);
                        mEditInputID.setFocusableInTouchMode(true);
                        mEditInputID.requestFocus();
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

        switch (IDtype_code) {
            case "1"://驾照
                if (TextUtils.isEmpty(et_id_number.getText().toString())) {
                    btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.mipmap.btn_hui_jianbian));
                    if (check) {
                        et_id_number.setFocusable(true);
                        et_id_number.setFocusableInTouchMode(true);
                        et_id_number.requestFocus();
                        et_id_number.post(new Runnable() {
                            @Override
                            public void run() {
                                til_id_number.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.end_orange));

                                til_id_number.setHint("Please fill out this section");//上边的提示文字
                                et_id_number.setHint("ID No.");

                                et_id_number.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected_orange));//黄线
                                et_id_number.requestFocus();
                            }
                        });
                    }
                    return false;

                }

                if (TextUtils.isEmpty(et_id_state.getText().toString())) {
                    btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.mipmap.btn_hui_jianbian));
                    if (check) {
                        showBirthDialog = false;
                        et_id_state.setFocusable(true);
                        et_id_state.setFocusableInTouchMode(true);
                        et_id_state.requestFocus();
                        et_id_state.post(new Runnable() {
                            @Override
                            public void run() {
                                til_id_state.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.end_orange));

                                til_id_state.setHint("Please fill out this section");//上边的提示文字
                                et_id_state.setHint("State of issurance");//下面的提示文字
                                et_id_state.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected_orange));//黄线
                                showBirthDialog = false;
                                et_id_state.requestFocus();
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
                break;
            case "2"://护照
                if (TextUtils.isEmpty(et_nationality.getText().toString())) {
                    btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.mipmap.btn_hui_jianbian));
                    if (check) {
                        showBirthDialog = false;
                        et_nationality.setFocusable(true);
                        et_nationality.setFocusableInTouchMode(true);
                        et_nationality.requestFocus();
                        et_nationality.post(new Runnable() {
                            @Override
                            public void run() {
                                til_nationality.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.end_orange));

                                til_nationality.setHint("Please fill out this section");//上边的提示文字
                                et_nationality.setHint("Nationality");//下面的提示文字
                                et_nationality.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected_orange));//黄线
                                showBirthDialog = false;
                                et_nationality.requestFocus();
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
                if (TextUtils.isEmpty(et_id_number.getText().toString())) {
                    btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.mipmap.btn_hui_jianbian));
                    if (check) {
                        et_id_number.setFocusable(true);
                        et_id_number.setFocusableInTouchMode(true);
                        et_id_number.requestFocus();
                        et_id_number.post(new Runnable() {
                            @Override
                            public void run() {
                                til_id_number.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.end_orange));

                                til_id_number.setHint("Please fill out this section");//上边的提示文字
                                et_id_number.setHint("Document No.");

                                et_id_number.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected_orange));//黄线
                                et_id_number.requestFocus();
                            }
                        });
                    }
                    return false;

                }
                if (TextUtils.isEmpty(id_date.getText().toString())) {
                    btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.mipmap.btn_hui_jianbian));
                    if (check) {
                        showBirthDialog = false;
                        id_date.setFocusable(true);
                        id_date.setFocusableInTouchMode(true);
                        id_date.requestFocus();
                        id_date.post(new Runnable() {
                            @Override
                            public void run() {
                                til_date.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.end_orange));

                                til_date.setHint("Please fill out this section");//上边的提示文字
                                id_date.setHint("Date of expiry");//下面的提示文字
                                id_date.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected_orange));//黄线
                                showBirthDialog = false;
                                id_date.requestFocus();
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
                break;

            case "3"://医保卡
                if (TextUtils.isEmpty(et_id_number.getText().toString())) {
                    btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.mipmap.btn_hui_jianbian));
                    if (check) {
                        et_id_number.setFocusable(true);
                        et_id_number.setFocusableInTouchMode(true);
                        et_id_number.requestFocus();
                        et_id_number.post(new Runnable() {
                            @Override
                            public void run() {
                                til_id_number.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.end_orange));

                                til_id_number.setHint("Please fill out this section");//上边的提示文字
                                et_id_number.setHint("Medicare card number");
                                et_id_number.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected_orange));//黄线
                                et_id_number.requestFocus();
                            }
                        });
                    }
                    return false;

                }
                if (TextUtils.isEmpty(id_medicare_type.getText().toString())) {
                    btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.mipmap.btn_hui_jianbian));
                    if (check) {
                        showBirthDialog = false;

                        id_medicare_type.setFocusable(true);
                        id_medicare_type.setFocusableInTouchMode(true);
                        id_medicare_type.requestFocus();
                        id_medicare_type.post(new Runnable() {
                            @Override
                            public void run() {
                                til_medicare_type.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.end_orange));

                                til_medicare_type.setHint("Please fill out this section");//上边的提示文字
                                id_medicare_type.setHint("Medicare type");//下面的提示文字
                                id_medicare_type.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected_orange));//黄线
                                showBirthDialog = false;
                                id_medicare_type.requestFocus();
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
                if (TextUtils.isEmpty(id_reference_number.getText().toString())) {
                    btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.mipmap.btn_hui_jianbian));
                    if (check) {
                        id_reference_number.setFocusable(true);
                        id_reference_number.setFocusableInTouchMode(true);
                        id_reference_number.requestFocus();
                        id_reference_number.post(new Runnable() {
                            @Override
                            public void run() {
                                til_reference_number.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.end_orange));

                                til_reference_number.setHint("Please fill out this section");//上边的提示文字
                                id_reference_number.setHint("Personal reference number");//下面的提示文字
                                id_reference_number.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected_orange));//黄线
                                id_reference_number.requestFocus();
                            }
                        });
                    }
                    return false;

                }
                if (TextUtils.isEmpty(id_date.getText().toString())) {
                    btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.mipmap.btn_hui_jianbian));
                    if (check) {
                        showBirthDialog = false;
                        id_date.setFocusable(true);
                        id_date.setFocusableInTouchMode(true);
                        id_date.requestFocus();
                        id_date.post(new Runnable() {
                            @Override
                            public void run() {
                                til_date.setHintTextColor(createColorStateList(KycActivity_first_IDTypeChoose.this, R.color.end_orange));

                                til_date.setHint("Please fill out this section");//上边的提示文字
                                id_date.setHint("Valid to");//下面的提示文字
                                id_date.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.drawable.edt_bg_un_selected_orange));//黄线
                                showBirthDialog = false;
                                id_date.requestFocus();
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
                break;
        }

        btn_get_code.setBackground(ContextCompat.getDrawable(KycActivity_first_IDTypeChoose.this, R.mipmap.btn_orange_jianbian));
        return true;
    }

    private static ColorStateList createColorStateList(Context context, int color) {
        int[] colors = new int[]{ContextCompat.getColor(context, color), ContextCompat.getColor(context, color)};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_checked};
        states[1] = new int[]{-android.R.attr.state_checked};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }
}
