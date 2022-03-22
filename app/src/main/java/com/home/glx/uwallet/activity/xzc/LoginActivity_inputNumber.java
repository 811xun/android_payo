package com.home.glx.uwallet.activity.xzc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.home.glx.uwallet.BuildConfig;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.HuaWeiUpdateData;
import com.home.glx.uwallet.mvp.CheckAppVersionVerify;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.selfview.UpdateDialog;
import com.home.glx.uwallet.tools.ChangeLineEdittext;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.SystemUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;
import com.home.glx.uwallet.tools.VersionChecker;
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack;
import com.huawei.updatesdk.service.otaupdate.UpdateKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.huawei.updatesdk.UpdateSdkAPI.checkAppUpdate;
import static com.huawei.updatesdk.service.otaupdate.UpdateStatusCode.HAS_UPGRADE_INFO;

public class LoginActivity_inputNumber extends MainActivity {
    private String phone = "";
    private UpdateDialog updateDialog;
    private TiShiDialog tiShiDialog;
    private String version = "";
    private boolean requestPermission = true;
    private TextInputLayout mHintInputNumber;
    private ChangeLineEdittext mEditInputNumber;
    private Button mBtnGetCode;
    private ImageView icon;
    private RelativeLayout rl_icon;

    private ChangeLineEdittext iv_sex_one;
    private ChangeLineEdittext sex_one_shenyanse;
    private TextInputLayout til_sex_one;
    private TextInputLayout til_sex_one_shenyanse;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                showNext();
            }
            if (msg.what == 2) {
                tiShiDialog.ShowNotCloseDialog(" Please update your App to the latest version to continue.", "Confirm");
            } else if (msg.what == 3) {
                updateDialog.ShowDialog(version);
            } else if (msg.what == 4) {
                launchAppDetail(LoginActivity_inputNumber.this, BuildConfig.APPLICATION_ID, "com.android.vending");
            }
        }
    };

    @Override
    public int getLayout() {
        setStatusBarColor(LoginActivity_inputNumber.this, R.color.white);
        getPermission = false;
        return R.layout.activity_inputnumber;
    }

    @Override
    public void initView() {
        super.initView();
        getGaid();
        icon = findViewById(R.id.icon);
        rl_icon = findViewById(R.id.rl_icon);
        mHintInputNumber = findViewById(R.id.til_account);
        mEditInputNumber = findViewById(R.id.et_account);
        til_sex_one = findViewById(R.id.til_sex_one);
        sex_one_shenyanse = findViewById(R.id.iv_sex_one_shenyanse);
        til_sex_one_shenyanse = findViewById(R.id.til_sex_one_shenyanse);
        iv_sex_one = findViewById(R.id.iv_sex_one);
        mBtnGetCode = findViewById(R.id.btn_get_code);
        TypefaceUtil.replaceFont(findViewById(R.id.hint), "fonts/gilroy_semiBold.ttf");
        mHintInputNumber.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        til_sex_one.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        til_sex_one_shenyanse.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        TypefaceUtil.replaceFont(iv_sex_one, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(sex_one_shenyanse, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(mEditInputNumber, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(mBtnGetCode, "fonts/gilroy_semiBold.ttf");

        //启动页进来的才判断更新
        String startFlag = getIntent().getStringExtra("start");
        if (startFlag != null) {
            showView();
        } else {
            showNext();
        }

        SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.DEVICE);
        phone = (String) appMsgSharePreferenceUtils.get(StaticParament.APP_PHONE, "");
        setOldPhone();
        initEvent();
    }


    private void initEvent() {
        mEditInputNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mEditInputNumber.setFocusable(true);
                mEditInputNumber.setFocusableInTouchMode(true);
                return false;
            }
        });

        mEditInputNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    mEditInputNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                } else {
                    if (String.valueOf(s.toString().trim().charAt(0)).equals("0")) {//输入第一位为0的情况，可以输入10位数字;输入第一位为不是0的情况，只能输入9位数字
                        mEditInputNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                    } else {
                        mEditInputNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
                    }
                }
                if (s.toString().trim().length() >= 9) {
                    mBtnGetCode.setBackground(ContextCompat.getDrawable(LoginActivity_inputNumber.this, R.mipmap.btn_orange_jianbian));
                } else {
                    mBtnGetCode.setBackground(ContextCompat.getDrawable(LoginActivity_inputNumber.this, R.mipmap.btn_hui_jianbian));
                }
            }
        });
        iv_sex_one.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        sex_one_shenyanse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mEditInputNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mHintInputNumber.setHint("");
                    til_sex_one.setVisibility(View.GONE);
                    til_sex_one_shenyanse.setVisibility(View.VISIBLE);
                    mHintInputNumber.setPadding(dip2px(LoginActivity_inputNumber.this, 27), 0, 0, 0);

                    String s = mEditInputNumber.getText().toString().trim();
                    if (TextUtils.isEmpty(s)) {
                        mEditInputNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                    } else {
                        if (String.valueOf(s.toString().trim().charAt(0)).equals("0")) {//输入第一位为0的情况，可以输入10位数字;输入第一位为不是0的情况，只能输入9位数字
                            mEditInputNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                        } else {
                            mEditInputNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
                        }
                    }

                    mEditInputNumber.setBackground(ContextCompat.getDrawable(LoginActivity_inputNumber.this, R.drawable.edt_bg_selected));
                } else {
                    if (TextUtils.isEmpty(mEditInputNumber.getText().toString().trim())) {
                        mEditInputNumber.setText("");
                        til_sex_one.setVisibility(View.GONE);
                        til_sex_one_shenyanse.setVisibility(View.GONE);
                        mHintInputNumber.setHint("Phone number");
                        mHintInputNumber.setPadding(0, 0, 0, 0);
                    } else {
                        mHintInputNumber.setPadding(dip2px(LoginActivity_inputNumber.this, 27), 0, 0, 0);

                        til_sex_one.setVisibility(View.VISIBLE);
                        til_sex_one_shenyanse.setVisibility(View.GONE);
                        mHintInputNumber.setHint("");
                    }

                    mEditInputNumber.setBackground(ContextCompat.getDrawable(LoginActivity_inputNumber.this, R.drawable.edt_bg_un_selected));
                }
            }
        });


        mBtnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = mEditInputNumber.getText().toString().trim().length();
                if (length == 9 || length == 10) {
                    mBtnGetCode.setEnabled(false);
                    mEditInputNumber.setFocusable(false);
                    mEditInputNumber.setFocusableInTouchMode(false);

                    mBtnGetCode.setText("");
                    Animation rotateAnimation = AnimationUtils.loadAnimation(LoginActivity_inputNumber.this, R.anim.rotate_anim);
                    LinearInterpolator lin = new LinearInterpolator();
                    rotateAnimation.setInterpolator(lin);
                    icon.startAnimation(rotateAnimation);
                    rl_icon.setVisibility(View.VISIBLE);
                    String phoneNo = "";
                    if (length == 9) {
                        phoneNo = "61" + mEditInputNumber.getText().toString().trim();
                    } else {
                        phoneNo = "61" + mEditInputNumber.getText().toString().trim().substring(1);
                    }
                    Log.d("smkxamlxmsmxm", "onClick: " + phoneNo);
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("phoneNumber", phoneNo);//手机号（61开头、 11位）
                    maps.put("nodeType", "1");//节点类型 1： 用户注册
                    getYzm(maps, phoneNo);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditInputNumber.getWindowToken(), 0);
                   /* DatePickerUtil datePickerUtil=new DatePickerUtil(LoginActivity_inputNumber.this);

                    try {
                        JSONObject jsonDateObj=new JSONObject();

                        jsonDateObj.put("defaultDate","2016-11-12");

                        jsonDateObj.put("startDate","1911-01-01");
                        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date(System.currentTimeMillis());

                        jsonDateObj.put("endDate",myFmt.format(date));
                        datePickerUtil.showDatePicker(jsonDateObj.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/

//                    Intent intent = new Intent(LoginActivity_inputNumber.this, LoginActivity_inputYzm.class);
//                    intent.putExtra("phonenum", phoneNo);
//                    startActivity(intent);

//                    choiceBirthday();
                }
            }
        });
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
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//大于等于9.0
                    Typeface font = Typeface.createFromAsset(getAssets(), "fonts/gilroy_medium.ttf");
                    SpannableString efr = new SpannableString(getString(R.string.emile_code_put));
                    efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    Toast.makeText(LoginActivity_inputNumber.this, efr, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity_inputNumber.this, getString(R.string.emile_code_put), Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(LoginActivity_inputNumber.this, LoginActivity_inputYzm.class);
                intent.putExtra("phonenum", phoneNo);
                intent.putExtra("open", getIntent().getStringExtra("open"));//从InputPin_Activity跳转的传递了该值 其他地方都没传递 xzc
                startActivity(intent);
                mBtnGetCode.setEnabled(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBtnGetCode.setText("Send verification code");
                        rl_icon.setVisibility(View.GONE);
                    }
                }, 200);
            }

            @Override
            public void resError(String error) {
                mBtnGetCode.setEnabled(true);
                mBtnGetCode.setText("Send verification code");
                rl_icon.setVisibility(View.GONE);
            }
        });
    }

    private void showNext() {
        getPermission = true;
        super.onResume();
//        onResume();
    }

    private void showView() {
        int marketType = BuildConfig.marketType;
        if (marketType == 1) {
            //检查华为市场
            checkAppUpdate(this, new UpdateCallBack(), false, false);
        } else if (marketType == 0) {
            //检查谷歌
            new Thread(new Runnable() {
                @Override
                public void run() {
                    checkGoogleUpdate();
                }
            }).start();
        } else {
            showNext();
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void setOldPhone() {
        SharePreferenceUtils sharePreferenceUtils_app = new SharePreferenceUtils(this, StaticParament.APP);
        String phone = (String) sharePreferenceUtils_app.get(StaticParament.USER_PHONE, "");
        if (phone.length() == 11) {//61后面跟9位数
            mEditInputNumber.setText(phone.substring(2));
            if (String.valueOf(phone.substring(2).charAt(0)).equals("0")) {//输入第一位为0的情况，可以输入10位数字;输入第一位为不是0的情况，只能输入9位数字
                mEditInputNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            } else {
                mEditInputNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
            }
            mBtnGetCode.setBackground(ContextCompat.getDrawable(LoginActivity_inputNumber.this, R.mipmap.btn_orange_jianbian));
            til_sex_one.setVisibility(View.VISIBLE);
            til_sex_one_shenyanse.setVisibility(View.GONE);
            mHintInputNumber.setHint("");
            mHintInputNumber.setPadding(dip2px(LoginActivity_inputNumber.this, 27), 0, 0, 0);


        } else {
            mEditInputNumber.setText("");
            til_sex_one.setVisibility(View.GONE);
            til_sex_one_shenyanse.setVisibility(View.GONE);
            mHintInputNumber.setHint("Phone number");
            mHintInputNumber.setPadding(0, 0, 0, 0);
        }
    }

    @Override
    public void permissionStatus(int requestCode) {
        super.permissionStatus(requestCode);
        switch (requestCode) {
            case 106:
                if (requestPermission) {
                    requestPermission = false;
                    getLocationPermission(125);
                }
                break;
        }
    }

    public class UpdateCallBack implements CheckUpdateCallBack {

        @Override
        public void onUpdateInfo(Intent intent) {
            try {
                if (intent != null) {
                    //更新状态信息
                    int status = intent.getIntExtra(UpdateKey.STATUS, -1);
                    //返回错误码，建议打印
                    int rtnCode = intent.getIntExtra(UpdateKey.FAIL_CODE, -1);
                    //失败信息，建议打印
                    String reason = intent.getStringExtra(UpdateKey.FAIL_REASON);
                    if (status == HAS_UPGRADE_INFO) {
                        //获取更新信息
                        Serializable info = intent.getSerializableExtra(UpdateKey.INFO);
//                String hwData = info.toString().replaceAll("_", "").trim();
                        Gson gson = new Gson();
                        String s = gson.toJson(info);
                        HuaWeiUpdateData huaWeiUpdateData = gson.fromJson(s, HuaWeiUpdateData.class);
                        version = huaWeiUpdateData.getVersion();
                        getHuaWeiUpdateResult(version);
/*                        CheckAppVersionVerify checkAppVersionVerify = new CheckAppVersionVerify(getContext());
                        Map<String, Object> maps = new HashMap<>();
                        maps.put("currentVersionId", BuildConfig.currentVersionId);
                        maps.put("newVersionNo",*//*BuildConfig.VERSION_NAME*//*version);
                        maps.put("no_user_id", 1);
                        checkAppVersionVerify.appVersionVerify(maps);
                        checkAppVersionVerify.setOnCheckVersion(new CheckAppVersionVerify.onCheckVersion() {
                            @Override
                            public void getFlag(String flag) {
                                if (flag != null) {
                                    if (flag.equals("1")) {
                                        if (tiShiDialog == null) {
                                            tiShiDialog = new TiShiDialog(getContext());
                                            tiShiDialog.ShowNotCloseDialog(" Please update your App to the latest version to continue.", "Confirm");
                                        }
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                ((MainTab) getActivity()).setChangeFlag(true);
                                            }
                                        }, 2000);
                                        tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                                            @Override
                                            public void GuanBi() {
                                                launchAppDetail(getContext(), BuildConfig.APPLICATION_ID, "com.huawei.appmarket");
                                            }
                                        });
                                    } else {
                                        if (updateDialog == null) {
                                            updateDialog = new UpdateDialog(getContext());
                                            updateDialog.ShowDialog(version);
                                        }
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                ((MainTab) getActivity()).setChangeFlag(true);
                                            }
                                        }, 2000);
                                        updateDialog.setOnUpdate(new UpdateDialog.Update() {
                                            @Override
                                            public void onUpdate() {
                                                launchAppDetail(getContext(), BuildConfig.APPLICATION_ID, "com.huawei.appmarket");

                                            }
                                        });
                                        updateDialog.setOnClose(new UpdateDialog.Close() {
                                            @Override
                                            public void onClose() {
                                                getNextDialog();
                                            }
                                        });
                                    }
                                } else {
                                    ((MainTab) getActivity()).setChangeFlag(true);
                                    getNextDialog();
                                }
                            }
                        });*/
                        L.log("version :" + version);
                    } else {
                        version = BuildConfig.VERSION_NAME;
                        getHuaWeiUpdateResult(version);
                        L.log("noNeed");
                    }
                } else {
                    version = "";
                    getHuaWeiUpdateResult(version);
                }
            } catch (Exception e) {
                e.printStackTrace();
                version = "";
                getHuaWeiUpdateResult(version);
            }
        }

        @Override
        public void onMarketInstallInfo(Intent intent) {

        }

        @Override
        public void onMarketStoreError(int i) {

        }

        @Override
        public void onUpdateStoreError(int i) {

        }
    }

    private void getHuaWeiUpdateResult(final String version) {
        CheckAppVersionVerify checkAppVersionVerify = new CheckAppVersionVerify(this);
        Map<String, Object> maps = new HashMap<>();
        maps.put("currentVersionId", BuildConfig.currentVersionId);
        maps.put("newVersionNo",/*BuildConfig.VERSION_NAME*/version);
        maps.put("no_user_id", 1);
        checkAppVersionVerify.appVersionVerify(maps);
        checkAppVersionVerify.setOnCheckVersion(new CheckAppVersionVerify.onCheckVersion() {
            @Override
            public void getFlag(String flag) {
                if (flag != null) {
                    if (flag.equals("1")) {
                        if (tiShiDialog == null) {
                            tiShiDialog = new TiShiDialog(LoginActivity_inputNumber.this);
                            tiShiDialog.ShowNotCloseDialog(" Please update your App to the latest version to continue.", "Confirm");
                        }
                        tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                            @Override
                            public void GuanBi() {
                                launchAppDetail(LoginActivity_inputNumber.this, BuildConfig.APPLICATION_ID, "com.huawei.appmarket");
                            }
                        });
                    } else if (flag.equals("0")) {
                        if (updateDialog == null) {
                            updateDialog = new UpdateDialog(LoginActivity_inputNumber.this);
                            updateDialog.ShowDialog(version);
                        }
                        updateDialog.setOnUpdate(new UpdateDialog.Update() {
                            @Override
                            public void onUpdate() {
                                launchAppDetail(LoginActivity_inputNumber.this, BuildConfig.APPLICATION_ID, "com.huawei.appmarket");

                            }
                        });
                        updateDialog.setOnClose(new UpdateDialog.Close() {
                            @Override
                            public void onClose() {
                                showNext();
                            }
                        });
                    } else {
                        showNext();
                    }
                } else {
                    showNext();
                }
            }
        });
    }

    /**
     * 启动到应用商店app详情界面
     *
     * @param appPkg      目标App的包名
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面
     */

    public static void launchAppDetail(Context mContext, String appPkg, String marketPkg) {
        try {

            if (TextUtils.isEmpty(appPkg)) {

                return;

            }

            Uri uri = Uri.parse("market://details?id=" + appPkg);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           /* if (!TextUtils.isEmpty(marketPkg)) {

                intent.setPackage(marketPkg);

            }*/
            ArrayList<HashMap<String, Object>> items = SystemUtils.getItems(mContext);
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).get("packageName").equals(marketPkg)) {
                    intent.setPackage(marketPkg);
                    mContext.startActivity(intent);
                    break;
                } else {
                    if (i == items.size() - 1) {
                        if (marketPkg.equals("com.android.vending")) {
                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID));
                            if (intent.resolveActivity(mContext.getPackageManager()) != null) { //可以接收
                                mContext.startActivity(intent);
                            }/*else {
                            Toast.makeText(mContext,"Fail to update",Toast.LENGTH_SHORT).show();
                        }*/
                        } else {
                            intent.setData(Uri.parse("https://appgallery.huawei.com/"));
                            if (intent.resolveActivity(mContext.getPackageManager()) != null) { //可以接收
                                mContext.startActivity(intent);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    private void checkGoogleUpdate() {
        VersionChecker versionChecker = new VersionChecker();
        try {
            version = versionChecker.execute().get();
            if (version != null) {
                //修改逻辑，不判断版本大小，每次都调用后台接口
                getGoogleUpdateResult(version);
            } else {
                /*((MainTab)getActivity()).setChangeFlag(true);
                handler.sendEmptyMessage(1);*/
                version = "";
                getGoogleUpdateResult("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            /*if ((MainTab)getActivity() != null) {
                  ((MainTab) getActivity()).setChangeFlag(true);
            }
            handler.sendEmptyMessage(1);*/
            version = "";
            getGoogleUpdateResult("");
        }
    }

    private void getGoogleUpdateResult(String version) {
        Map<String, Object> maps = new HashMap<>();
        CheckAppVersionVerify checkAppVersionVerify = new CheckAppVersionVerify(this);
        maps.put("currentVersionId", BuildConfig.currentVersionId);
        maps.put("newVersionNo", /*BuildConfig.VERSION_NAME*/version);
        maps.put("no_user_id", 1);
        checkAppVersionVerify.appVersionVerify(maps);
        checkAppVersionVerify.setOnCheckVersion(new CheckAppVersionVerify.onCheckVersion() {
            @Override
            public void getFlag(String flag) {
                if (flag != null) {
                    if (flag.equals("1")) {
                        if (tiShiDialog == null) {
                            tiShiDialog = new TiShiDialog(LoginActivity_inputNumber.this);
                            handler.sendEmptyMessage(2);
                        }
                        tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                            @Override
                            public void GuanBi() {
                                handler.sendEmptyMessage(4);
                            }
                        });
                    } else if (flag.equals("0")) {
                        if (updateDialog == null) {
                            updateDialog = new UpdateDialog(LoginActivity_inputNumber.this);
                            handler.sendEmptyMessage(3);
                        }
                        updateDialog.setOnUpdate(new UpdateDialog.Update() {
                            @Override
                            public void onUpdate() {
                                handler.sendEmptyMessage(4);
                            }
                        });
                        updateDialog.setOnClose(new UpdateDialog.Close() {
                            @Override
                            public void onClose() {
                                handler.sendEmptyMessage(1);
                            }
                        });
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mEditInputNumber.setFocusable(false);
        mEditInputNumber.setFocusableInTouchMode(false);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//关闭软键盘
        imm.hideSoftInputFromWindow(mEditInputNumber.getWindowToken(), 0);
        return super.onTouchEvent(event);
    }
}
