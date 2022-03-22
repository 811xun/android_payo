package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.xzc.ChooseCardActivity;
import com.home.glx.uwallet.activity.xzc.KycActivity_first_IDTypeChoose;
import com.home.glx.uwallet.datas.FenqifuStatue;
import com.home.glx.uwallet.fragment.FirstFragment;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.AddMaidian;
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

public class SelectPayTypeActivity extends MainActivity implements View.OnClickListener {
    private RelativeLayout bankCard;
    private RelativeLayout instalment;
    private ImageView id_back;
    private TextView skip;
    private SharePreferenceUtils user_sharePreferenceUtils;
    private TextView title;
    private TextView instalment_text;
    private TextView instalment_text1;
    private TextView bank_card_text;
    private TextView bank_card_text1;
    private String userId;
    public static int shifoukaitongCardPay = -1;//0：未开通开支付  1:开通开卡支付

    public static int meikaitongfenqifuAndkaitongCardPay = -1;

    @Override
    public int getLayout() {
        return R.layout.activity_select_pay_type;
    }

    @Override
    public void initView() {
        super.initView();
        user_sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        id_back = (ImageView) findViewById(R.id.id_back);
        bankCard = (RelativeLayout) findViewById(R.id.bank_card);
        instalment = (RelativeLayout) findViewById(R.id.instalment);
        instalment_text = findViewById(R.id.instalment_text);
        instalment_text1 = findViewById(R.id.instalment_text1);
        bank_card_text = findViewById(R.id.bank_card_text);
        bank_card_text1 = findViewById(R.id.bank_card_text1);
        skip = (TextView) findViewById(R.id.skip1);
        title = findViewById(R.id.id_title);
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.id_hint), "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(instalment_text, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(instalment_text1, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.hint), "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(bank_card_text, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(bank_card_text1, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(skip, "fonts/gilroy_medium.ttf");

        skip.setOnClickListener(this);
        id_back.setOnClickListener(this);
        bankCard.setOnClickListener(this);
        instalment.setOnClickListener(this);

        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
        final Map<String, Object> maps = new HashMap<>();
        maps.put("userId", userId);
        maps.put("type", "1");//页面标识 1：二选一 2：证件类型页 3:地址页 4:个人信息填写页 5:绑卡页 6:illion默认机构页 7:illion 第一次验证页，8:illion第二次验证页 9:分期付开通等待页
        AddMaidian.addMaidian(this, maps);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user_sharePreferenceUtils.put(StaticParament.FROM_SELECT_PAY, "0");
        user_sharePreferenceUtils.save();
    }

    @Override
    public void onBackPressed() {
        Intent jumpToMainTab = new Intent(SelectPayTypeActivity.this, MainTab.class);
        jumpToMainTab.putExtra("haveCheckUpdate", "1");
        jumpToMainTab.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        jumpToMainTab.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
        startActivity(jumpToMainTab);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.id_back:
            case R.id.skip1:
                if (!PublicTools.isFastClick()) {
                    return;
                }
                Intent jumpToMainTab = new Intent(SelectPayTypeActivity.this, MainTab.class);
                jumpToMainTab.putExtra("haveCheckUpdate", "1");
                jumpToMainTab.putExtra("num", 0);
                jumpToMainTab.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                jumpToMainTab.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
                startActivity(jumpToMainTab);
                finish();
                break;
            case R.id.instalment:
                if (!PublicTools.isFastClick()) {
                    return;
                }
                //  Intent intentBank = new Intent(this, NewAddBankCardActivity.class);   intentBank.putExtra("payType", 1);  !!!!xzc
//                user_sharePreferenceUtils.put(StaticParament.FROM_SELECT_PAY, "1");
//                user_sharePreferenceUtils.save();
//                Intent intent = new Intent(this, FenQiFuOpen_Activity.class);
//                intent.putExtra("payType", 1);
//                startActivity(intent);
//                finish();
                Map<String, Object> maps = new HashMap<>();
                maps.put("userId", userId);
                maps.put("stripeState", 0);//用户区分接入stripe前后的新老用户
                jiaoyanFenqifu(maps);
                break;
            case R.id.bank_card://绑卡
                if (!PublicTools.isFastClick()) {
                    return;
                }
                Intent intentBank = new Intent(this, NewAddBankCardActivity.class);
                intentBank.putExtra("payType", 1);
                intentBank.putExtra("from", "erxuanyi");
                startActivity(intentBank);
                finish();
                break;
        }
    }

    /**
     * 分期付开通状态
     *
     * @param maps
     */
    public void jiaoyanFenqifu(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {
                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.jiaoyanFenqifu(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                SharePreferenceUtils appMsgSharePreferenceUtils =
                        new SharePreferenceUtils(SelectPayTypeActivity.this, StaticParament.DEVICE);
                String callPhone = (String) appMsgSharePreferenceUtils.get(StaticParament.APP_PHONE, "");
                FirstFragment.backStatue = 1;//从登陆注册进入的 开通分期付返回到首页
                Gson gson = new Gson();
                FenqifuStatue fenqifuStatue = gson.fromJson(dataStr, FenqifuStatue.class);
                shifoukaitongCardPay = fenqifuStatue.getCardState();//0：未开通开支付  1:开通开卡支付
                meikaitongfenqifuAndkaitongCardPay = fenqifuStatue.getCreditCardAgreementState();

                if (fenqifuStatue.getKycState() == 0) {//未开通kyc
                    Intent intent_kyc = new Intent(SelectPayTypeActivity.this, KycActivity_first_IDTypeChoose.class);
                    intent_kyc.putExtra("erxuanyi", true);
                    startActivity(intent_kyc);
                } else {//已经开通kyc
                    if (fenqifuStatue.getCardState() == 0) {//未开通卡支付
                        Intent intent_kyc = new Intent(SelectPayTypeActivity.this, NewAddBankCardActivity.class);
                        intent_kyc.putExtra("kaitongfenqifuRoad", true);
                        intent_kyc.putExtra("payType", 1);
                        startActivity(intent_kyc);
                    } else {//已经开通卡支付 //判断illion状态
                        if (fenqifuStatue.getCreditCardAgreementState() == 0) {//是否勾选过分期付绑卡协议 0：未勾选 1：已勾选
                            Intent intent_kyc = new Intent(SelectPayTypeActivity.this, ChooseCardActivity.class);
                            startActivity(intent_kyc);
                        } else {
                            if (fenqifuStatue.getInstallmentState() == 2) {//开通illion 开通了分期付
                                Intent intent_fenqi = new Intent(SelectPayTypeActivity.this, MainTab.class);
                                intent_fenqi.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent_fenqi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent_fenqi);
                                finish();
                            } else if (fenqifuStatue.getInstallmentState() == 0 || fenqifuStatue.getInstallmentState() == 3) {//未开通！ 0. 用户未开通分期付(需要完善信息) 3. illion未授权
                                Intent intent_kyc = new Intent(SelectPayTypeActivity.this, ChooseIllionActivity.class);
                                startActivity(intent_kyc);
                            } else if (fenqifuStatue.getInstallmentState() == 1 || fenqifuStatue.getInstallmentState() == 5 || fenqifuStatue.getInstallmentState() == 8) {//—-失败！—— 1. 用户分期付已冻结 5. 用户分期付禁用 8 机审拒绝
                                Intent intent_kyc = new Intent(SelectPayTypeActivity.this, KycAndIllionResultActivity.class);
                                intent_kyc.putExtra("error", "FkReject");
                                intent_kyc.putExtra("phone", callPhone);
                                startActivity(intent_kyc);
                            } else if (fenqifuStatue.getInstallmentState() == 4 || fenqifuStatue.getInstallmentState() == 7 || fenqifuStatue.getInstallmentState() == 9) {//—-等待！——4. 等待人工审核 7. 机审中 9 分期付风控未触发
                                Intent intent_kyc = new Intent(SelectPayTypeActivity.this, KycAndIllionResultActivity.class);
                                intent_kyc.putExtra("error", "Waiting");
                                intent_kyc.putExtra("phone", callPhone);
                                startActivity(intent_kyc);
                            }
                        }
                    }
                }
                Log.d("xunzhic", "resData: " + dataStr);
            }

            @Override
            public void resError(String error) {

            }
        });
    }

}
