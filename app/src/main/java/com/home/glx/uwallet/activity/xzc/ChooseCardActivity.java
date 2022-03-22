package com.home.glx.uwallet.activity.xzc;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
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

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.ChooseIllionActivity;
import com.home.glx.uwallet.activity.PayFailedActivity;
import com.home.glx.uwallet.activity.PayMoneyActivity;
import com.home.glx.uwallet.datas.BankDatas;
import com.home.glx.uwallet.fragment.FirstFragment;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ChooseCardActivity extends MainActivity {
    private CheckBox check_up;
    private TextView proText_up;
    private String selectedCardId = "";//初始值list.get(0).getId()需要更改
    private ImageView default_card_logo;
    private TextView default_card_no;
    private Button mButton;
    private ImageView icon;
    private RelativeLayout rl_icon;
    private BankDatas bankDatas;

    @Override
    public int getLayout() {
        return R.layout.activity_choose_bankcard;
    }

    @Override
    public void initView() {
        super.initView();
        defaultCardInfo();

        icon = findViewById(R.id.icon);
        rl_icon = findViewById(R.id.rl_icon);

        check_up = (CheckBox) findViewById(R.id.id_protocols_1);
        proText_up = findViewById(R.id.proText_1);
        default_card_logo = findViewById(R.id.default_card_logo);
        default_card_no = findViewById(R.id.default_card_no);
        mButton = findViewById(R.id.btn_get_code);
        TypefaceUtil.replaceFont(default_card_no, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.tv_payo), "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.tv_hint), "fonts/gilroy_medium.ttf");
        //切换字体
        TypefaceUtil.replaceFont(proText_up, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.pay_with), "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.choose_card), "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(mButton, "fonts/gilroy_semiBold.ttf");
        check_up.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mButton.setBackground(ContextCompat.getDrawable(ChooseCardActivity.this, R.mipmap.btn_orange_jianbian));
                } else {
                    mButton.setBackground(ContextCompat.getDrawable(ChooseCardActivity.this, R.mipmap.btn_hui_jianbian));
                }
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check_up.isChecked()) {
                    if (!PublicTools.isFastClick()) {
                        return;
                    }
                    mButton.setEnabled(false);
                    mButton.setText("");
                    Animation rotateAnimation = AnimationUtils.loadAnimation(ChooseCardActivity.this, R.anim.rotate_anim);
                    LinearInterpolator lin = new LinearInterpolator();
                    rotateAnimation.setInterpolator(lin);
                    icon.startAnimation(rotateAnimation);
                    rl_icon.setVisibility(View.VISIBLE);

                    SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(ChooseCardActivity.this, StaticParament.USER);
                    String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
                    final Map<String, Object> maps = new HashMap<>();
                    maps.put("userId", userId);
                    maps.put("cardId", selectedCardId);
                    updateUserCreditCardAgreementState(maps);
                }
            }
        });

        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        proText_up.setText("I authorise Payo Funds Pty Ltd ACN 638 179 567\n" +
                "and its associated party, to debit the\n" +
                "nominated bank (card) account outlined above.");

        findViewById(R.id.choose_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseCardActivity.this, ChooseCardListActivity.class);
                intent.putExtra("selectedCardId", selectedCardId);
                intent.putExtra("bankDatas", bankDatas);
                startActivityForResult(intent, 100);
            }
        });

        setTitle("");
    }

    @Override
    public void onBackPressed() {
        back();
    }

    private void back() {
        if (FirstFragment.backStatue == 1 || FirstFragment.backStatue == -1) {
            Intent intent = new Intent(ChooseCardActivity.this, MainTab.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
            intent.putExtra("num", 0);
            startActivity(intent);
            finish();
        } else if (FirstFragment.backStatue == 2) {
            Intent it = new Intent(ChooseCardActivity.this, MainTab.class);
            it.putExtra("num", 2);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
            startActivity(it);
            finish();
        } else if (FirstFragment.backStatue == 3) {
            Intent intent = new Intent(ChooseCardActivity.this, PayMoneyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else if (FirstFragment.backStatue == 4) {
            Intent intent = new Intent(ChooseCardActivity.this, PayFailedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    public void defaultCardInfo() {
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(ChooseCardActivity.this, StaticParament.USER);
        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
        final Map<String, Object> maps = new HashMap<>();
        maps.put("userId", userId);
        RequestUtils requestUtils1 = new RequestUtils(this, maps, StaticParament.zhifu, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(ChooseCardActivity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("绑卡获取卡类型接口参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.defaultCardInfo(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                BankDatas bankDatas = new BankDatas();

                L.log("xzcxzcxzc:" + dataStr);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataStr);
                    bankDatas.setCustomerCcType(jsonObject.getString("customerCcType"));
                    bankDatas.setId(jsonObject.getString("cardId"));
                    bankDatas.setCustomerCcExpmo(jsonObject.getString("customerCcExpmo"));
                    bankDatas.setCustomerCcExpyr(jsonObject.getString("customerCcExpyr"));
                    bankDatas.setCardNo(jsonObject.getString("cardNo"));
                    ChooseCardActivity.this.bankDatas = bankDatas;

                    selectedCardId = jsonObject.getString("cardId");

                    if (jsonObject.getString("customerCcType").equals("10")) {
                        default_card_logo.setImageResource(R.mipmap.pay_visa_logo_new);
                        default_card_logo.setVisibility(View.VISIBLE);

                    } else if (jsonObject.getString("customerCcType").equals("20")) {
                        default_card_logo.setImageResource(R.mipmap.pay_master_logo_new);
                        default_card_logo.setVisibility(View.VISIBLE);

                    } else if (jsonObject.getString("customerCcType").equals("60")) {
                        default_card_logo.setImageResource(R.mipmap.pay_onther_logo_new);
                        default_card_logo.setVisibility(View.VISIBLE);

                    } else {
                        default_card_logo.setVisibility(View.INVISIBLE);
                    }
                    String card = jsonObject.getString("cardNo").replaceAll(" ", "");
                    if (jsonObject.has("customerCcExpmo") && jsonObject.has("customerCcExpyr") && !TextUtils.isEmpty(jsonObject.getString("customerCcExpmo")) && !TextUtils.isEmpty(jsonObject.getString("customerCcExpyr"))) {
                        default_card_no.setText("•••• " + card.substring(card.length() - 4) + "(" + jsonObject.getString("customerCcExpmo") + "/" + jsonObject.getString("customerCcExpyr").substring(2, 4) + ")");
                    } else {
                        default_card_no.setText("•••• " + card.substring(card.length() - 4));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void resError(String error) {
                L.log("xzcxzcxzc:" + error);
            }
        });
    }

    public void updateUserCreditCardAgreementState(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(this, maps, StaticParament.zhifu, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(ChooseCardActivity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("绑卡获取卡类型接口参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.updateUserCreditCardAgreementState(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                new SharePreferenceUtils(ChooseCardActivity.this, StaticParament.USER).put(StaticParament.PAY_SUCCESS, "1").commit();//为了wallet页面刷新
                mButton.setEnabled(true);
                mButton.setText("Add");
                rl_icon.setVisibility(View.GONE);

                L.log("xzcxzcxzc:" + dataStr);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(ChooseCardActivity.this, ChooseIllionActivity.class);
                startActivity(intent);
                finish();
            }


            @Override
            public void resError(String error) {
                L.log("xzcxzcxzc:" + error);

                mButton.setEnabled(true);
                mButton.setText("Add");
                rl_icon.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                defaultCardInfo();//添加卡
            } else { //选择卡
                BankDatas bankDatas = (BankDatas) data.getSerializableExtra("bankDatas");
                ChooseCardActivity.this.bankDatas = bankDatas;
                if (bankDatas.getCustomerCcType() != null) {
                    if (bankDatas.getCustomerCcType().equals("10")) {
                        default_card_logo.setImageResource(R.mipmap.pay_visa_logo_new);
                        default_card_logo.setVisibility(View.VISIBLE);
                    } else if (bankDatas.getCustomerCcType().equals("20")) {
                        default_card_logo.setImageResource(R.mipmap.pay_master_logo_new);
                        default_card_logo.setVisibility(View.VISIBLE);

                    } else if (bankDatas.getCustomerCcType().equals("60")) {
                        default_card_logo.setImageResource(R.mipmap.pay_onther_logo_new);
                        default_card_logo.setVisibility(View.VISIBLE);

                    } else {
                        default_card_logo.setVisibility(View.INVISIBLE);
                    }
                    String card = bankDatas.getCardNo().replaceAll(" ", "");

                    if (bankDatas.getCustomerCcExpmo() == null || bankDatas.getCustomerCcExpyr() == null || TextUtils.isEmpty(bankDatas.getCustomerCcExpmo()) || TextUtils.isEmpty(bankDatas.getCustomerCcExpyr())) {
                        default_card_no.setText("•••• " + card.substring(card.length() - 4));

                    } else {
                        default_card_no.setText("•••• " + card.substring(card.length() - 4) + "(" + bankDatas.getCustomerCcExpmo() + "/" + bankDatas.getCustomerCcExpyr().substring(2, 4) + ")");
                    }

                    selectedCardId = bankDatas.getId();
                }
            }
        }
    }
}
