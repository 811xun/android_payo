package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.AboutDatas;
import com.home.glx.uwallet.fragment.FirstFragment;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.CallPhoneDialog;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class KycAndIllionResultActivity extends MainActivity implements View.OnClickListener {
    private ImageView icon;
    private TextView title;
    private TextView msg;
    private Button tryAgain;
    private ImageView back;
    private TextView phone;
    private ImageView kycNumIcon;
    private String phoneNum;
    private String status;
    private SharePreferenceUtils user_sharePreferenceUtils;
    private boolean backToRegisterActivity = false;
//    private boolean mIllionFailure = false;

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
        return R.layout.activity_kyc_and_illion_result;
    }

    @Override
    public void initView() {
        super.initView();
        user_sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        kycNumIcon = (ImageView) findViewById(R.id.kyc_num_icon);
        icon = (ImageView) findViewById(R.id.kyc_oh_icon);
        title = (TextView) findViewById(R.id.kyc_title);
        msg = (TextView) findViewById(R.id.kyc_msg);
        tryAgain = (Button) findViewById(R.id.try_again);
        back = (ImageView) findViewById(R.id.back);
        phone = (TextView) findViewById(R.id.phone);

        //切换字体
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(msg, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(tryAgain, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(phone, "fonts/gilroy_semiBold.ttf");

        back.setOnClickListener(this);
        tryAgain.setOnClickListener(this);
        phone.setOnClickListener(this);
        status = getIntent().getStringExtra("error");
//        phoneNum = getIntent().getStringExtra("phone");
//        if (TextUtils.isEmpty(phoneNum)) {
//            SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.DEVICE);
//            phoneNum = (String) appMsgSharePreferenceUtils.get(StaticParament.APP_PHONE, "");
//        }
//        phone.setText(Html.fromHtml("<font color='#FE7644'>" + phoneNum + "</font>"));
        //kycPeople、FkReject、kycSystem
        //默认页面kycSystem
        Log.d("swkkwddklk", "initView: " + status);
        if (!status.equals("kycSystem")) {
            if (status.equals("Waiting")) { //哪来回哪儿去   目前不展示这儿了！！！xzc
                kycNumIcon.setVisibility(View.GONE);
                icon.setImageResource(R.mipmap.smile_icon);
                tryAgain.setVisibility(View.GONE);
                title.setText("Won’t be long");
                msg.setText("Thanks for applying. Your details are currently \nbeing processed. Please  back later to \nsee the result of your application.\n\n" +
                        "If you have any issues, please get in touch with\nour customer service team.");
                back.setTag("error");
            } else {
                if (status.equals("FkReject")) {//分期付开通失败。 哪来回哪儿去
                    kycNumIcon.setVisibility(View.GONE);
                    icon.setImageResource(R.mipmap.cry_icon);
                    //风控拒绝
                    tryAgain.setVisibility(View.GONE);
                    title.setText("Oh Snap!");
                    msg.setText("Thanks for applying. Unfortunately we can’t\napprove your ID based on the details you have\nprovided. Please try again.\n\n" +
                            " If the problem persists, please get in touch \nwith our customer service team.");
                    back.setTag("error");
//                    mIllionFailure = true;
                }
                if (status.equals("kycPeople")) {//等待页 哪来回哪儿去 11111
                    icon.setImageResource(R.mipmap.smile_icon); //风控等待、人审
                    tryAgain.setVisibility(View.GONE);
                    back.setTag("error");
                    title.setText("How exciting!");
                    msg.setText("Thanks for applying to Payo. Your details are being processed at the moment. We’ll get back to you in a few minutes.\n\nIf you have any issues, please get in touch with our customer service team.");
                }
                if (status.equals("kycSameName")) {//名字相同 //返回到信息录入页（注册页）
                    backToRegisterActivity = true;
                    kycNumIcon.setVisibility(View.GONE);
                    icon.setImageResource(R.mipmap.cry_icon);
                    tryAgain.setVisibility(View.GONE);
                    title.setText("User already exists");
                    msg.setText("The user name you have entered already exists. You can only register once on payo. If you cannot access your account, please give us a call on 1800 777 290");
                    back.setTag("error");
                }
                if (status.equals("kycFirst")) { //kyc失败1次 //返回到信息录入页（注册页）
                    kycNumIcon.setVisibility(View.VISIBLE);
                    backToRegisterActivity = true;
                    icon.setImageResource(R.mipmap.cry_icon);
                    title.setText("Oh Snap!");
                    msg.setText("We couldn’t authenticate your account \nunfortunately. These things can happen.\n\nWe need to verify your account, so please \nmake sure your information is correct so we\ncan get you qualified ASAP.");
                } else if (status.equals("kycTwo")) { //kyc失败2次 //返回到信息录入页（注册页）
                    kycNumIcon.setVisibility(View.VISIBLE);
                    backToRegisterActivity = true;
                    kycNumIcon.setImageResource(R.mipmap.kyc_second_o);
                    icon.setImageResource(R.mipmap.cry_icon);
                    title.setText("Oh Snap!");
                    msg.setText("We’re trying to verify your account but\nsomething’s not right. \n\nPlease carefully check your information before\nsubmitting again. You get only one more\nattempt before being locked out for the day.");
                } else if (status.equals("kycThree")) {//kyc失败3次 哪来回哪儿去
                    kycNumIcon.setVisibility(View.VISIBLE);
                    tryAgain.setVisibility(View.GONE);
                    kycNumIcon.setImageResource(R.mipmap.kyc_third_o);
                    icon.setImageResource(R.mipmap.cry_icon);
                    title.setText("Oh Snap!");
                    msg.setText("We’re trying to verify your account but\nsomething’s not right. Please try again\ntomorrow.\n\nIf the problem persists, get in touch with our\ncustomer service team.");
                    back.setTag("error");
                }
            }
        } else {//展示layout布局中的文字 返回到信息录入页（注册页）
            backToRegisterActivity = true;
        }

        getAboutMsg();//获取电话号码
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        if (!PublicTools.isFastClick()) {
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.back:
                if (backToRegisterActivity) {
                    ////从登陆页面---》注册---》keyc---->kyc第一次失败返回到该页面 再点击返回键 防止返回到登陆页 需要到首页
                    RegistTwo_Activity.backToMainActivity = true;
                    Intent intent_register = new Intent(KycAndIllionResultActivity.this, RegistTwo_Activity.class); //跳到注册页 从注册页跳到kyc页
                    intent_register.putExtra("jumpToKyc", true);
                    intent_register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent_register.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent_register);
                    finish();
                } else {
                    back();
                }
            /*    if (back.getTag() != null && back.getTag().equals("error")) {
                    Intent intent;
                    if (user_sharePreferenceUtils.get(StaticParament.FROM_PAY_MONEY, "").equals("1")) {
                        intent = new Intent(this, PayMoneyActivity.class);
                    } else if (user_sharePreferenceUtils.get(StaticParament.FROM_SELECT_PAY, "").equals("1")) {
                        intent = new Intent(this, SelectPayTypeActivity.class);
                    } else {
                        intent = new Intent(this, MainTab.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
                finish();*/
                break;
            case R.id.try_again:
                finish();
                break;
            case R.id.phone:
                /**
                 * 调用拨号界面
                 *
                 * @param phone 电话号码
                 */
                CallPhoneDialog callPhoneDialog = new CallPhoneDialog(this);
                callPhoneDialog.setOnCall(new CallPhoneDialog.Call() {
                    @Override
                    public void onCall() {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    @Override
                    public void onCopy() {
                        PublicTools.copyText(KycAndIllionResultActivity.this, phoneNum);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//大于等于9.0
                            Typeface font = Typeface.createFromAsset(getAssets(), "fonts/gilroy_medium.ttf");
                            SpannableString efr = new SpannableString("Copied successfully");
                            efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Toast.makeText(KycAndIllionResultActivity.this, efr, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(KycAndIllionResultActivity.this, "Copied successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                callPhoneDialog.ShowDialog();
                break;
        }
    }

    @Override
    public void onBackPressed() {
      /*  if (back.getTag() != null && back.getTag().equals("error")) {
            Intent intent;
            if (user_sharePreferenceUtils.get(StaticParament.FROM_PAY_MONEY, "").equals("1")) {
                intent = new Intent(this, PayMoneyActivity.class);
            } else if (user_sharePreferenceUtils.get(StaticParament.FROM_SELECT_PAY, "").equals("1")) {
                intent = new Intent(this, SelectPayTypeActivity.class);
            } else {
                intent = new Intent(this, MainTab.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        finish();*/

        if (backToRegisterActivity) {
            RegistTwo_Activity.backToMainActivity = true;
            Intent intent_register = new Intent(KycAndIllionResultActivity.this, RegistTwo_Activity.class); //跳到注册页 从注册页跳到kyc页
            intent_register.putExtra("jumpToKyc", true);
            intent_register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent_register.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent_register);
            finish();
        } else {
            back();
        }
    }

    private void back() {
        if (FirstFragment.backStatue == 1 || FirstFragment.backStatue == -1) {
            Intent intent = new Intent(KycAndIllionResultActivity.this, MainTab.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("num", 0);
            startActivity(intent);
            finish();
        } else if (FirstFragment.backStatue == 2) {//从前包页过来 如果illion失败 返回到illion机构列表页
            /*if (mIllionFailure) {
                Intent intent_kyc = new Intent(KycAndIllionResultActivity.this, ChooseIllionActivity.class);
                startActivity(intent_kyc);
                mIllionFailure = false;
            } else {
            }*/
            Intent it = new Intent(KycAndIllionResultActivity.this, MainTab.class);
            it.putExtra("num", 2);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
            startActivity(it);
            finish();
        } else if (FirstFragment.backStatue == 3) {
            Intent intent = new Intent(KycAndIllionResultActivity.this, PayMoneyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else if (FirstFragment.backStatue == 4) {
            Intent intent = new Intent(KycAndIllionResultActivity.this, PayFailedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    public void getAboutMsg() {
        RequestUtils requestUtils1 = new RequestUtils(KycAndIllionResultActivity.this, new HashMap<String, Object>(),
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(KycAndIllionResultActivity.this, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("关于我们参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getAboutUs(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        Gson gson = new Gson();
                        AboutDatas aboutDatas = gson.fromJson(dataStr, AboutDatas.class);
                        phoneNum = aboutDatas.getPhone();
                        phone.setText(Html.fromHtml("<font color='#FE7644'>" + phoneNum + "</font>"));
                    }


                    @Override
                    public void resError(String error) {

                    }
                });
    }
}
