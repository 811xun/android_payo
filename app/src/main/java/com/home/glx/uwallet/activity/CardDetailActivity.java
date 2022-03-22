package com.home.glx.uwallet.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.BankDatas;
import com.home.glx.uwallet.mvp.UntieAccount;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.KycAndIllionListener;
import com.home.glx.uwallet.mvp.model.KycAndIllionModel;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.SwitchView;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.L;
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

public class CardDetailActivity extends MainActivity implements View.OnClickListener {
    private RelativeLayout cardRl;
    private ImageView back;
    private BankDatas bankData;
    private TextView cardNo;
    private TextView dueTime;
    private ImageView logo;
    private TextView title;
    private TextView deleteCard;
    private TextView editCard;
    private SwitchView switchView;
    private int defaultFlag;
    private KycAndIllionListener kycAndIllionListener;
    private int cardSize;

    @Override
    public int getLayout() {
        return R.layout.activity_card_detail;
    }

    @Override
    public void initView() {
        super.initView();
        kycAndIllionListener = new KycAndIllionModel(this);
        title = (TextView) findViewById(R.id.title);
        editCard = (TextView) findViewById(R.id.edit_card);
        cardRl = (RelativeLayout) findViewById(R.id.card_rl);
        deleteCard = (TextView) findViewById(R.id.delete_card);
        back = (ImageView) findViewById(R.id.back);
        bankData = (BankDatas) getIntent().getSerializableExtra("bankDatas");
        cardSize = getIntent().getIntExtra("cardSize", 0);
        defaultFlag = getIntent().getIntExtra("default", 0);
        back.setOnClickListener(this);
        editCard.setOnClickListener(this);
        deleteCard.setOnClickListener(this);
        switchView = (SwitchView) findViewById(R.id.switchView);
        if (bankData.getPreset().equals("1")) {
            switchView.setOpened(true);
            deleteCard.setTextColor(getResources().getColor(R.color.pay_text_gray));
            switchView.setTag("open");
        } else {
            if (cardSize == 1) {
                switchView.setOpened(true);
                deleteCard.setTextColor(getResources().getColor(R.color.pay_text_gray));
                switchView.setTag("open");
            } else {
                deleteCard.setTextColor(getResources().getColor(R.color.red));
                switchView.setTag("close");
            }
        }
//        if (cardSize > 1 && bankData.getPayState() == 0 && bankData.getPreset().equals("1")) {
//            switchView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return true;
//                }
//            });
////            switchView.setEnabled(false);
//        } else {
//
//        }

        switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardSize == 1 || bankData.getPreset().equals("1")) {//总共一张卡或者该卡是默认卡
                    switchView.setOpened(false);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            switchView.setOpened(true);
                        }
                    }, 350);
                    return;
                } else {
                    if (switchView.getTag().equals("open")) {
                        switchView.setOpened(false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                switchView.setOpened(true);
                            }
                        }, 350);
                        return;
//                        switchView.setTag("close");
//                        deleteCard.setTextColor(getResources().getColor(R.color.red));
//                        Map<String, Object> maps = new HashMap<>();
//                        maps.put("disable", "true");
//                        maps.put("cardId", bankData.getId());
//                        kycAndIllionListener.setDefaultCard(maps, new ActionCallBack() {
//                            @Override
//                            public void onSuccess(Object... o) {
//
//                            }
//
//                            @Override
//                            public void onError(String e) {
//                                switchView.setOpened(true);
//                            }
//                        });
                    } else {//非默认卡 可以设置成默认卡 设置成默认卡后就不能再取消。
                        switchView.setTag("open");
                        deleteCard.setTextColor(getResources().getColor(R.color.pay_text_gray));
                        Map<String, Object> maps = new HashMap<>();
                        maps.put("cardId", bankData.getId());
                        kycAndIllionListener.setDefaultCard(maps, new ActionCallBack() {
                            @Override
                            public void onSuccess(Object... o) {

                            }

                            @Override
                            public void onError(String e) {
                                switchView.setOpened(false);
                            }
                        });
                    }
                }
            }
        });
        cardNo = (TextView) findViewById(R.id.card_number);
        dueTime = (TextView) findViewById(R.id.time_text);
        logo = (ImageView) findViewById(R.id.card_logo);
           /* if (Integer.parseInt(bankData.getOrder()) % 3 == 1) {
                card.setBackgroundResource(R.mipmap.card_orange_background);
            } else if (Integer.parseInt(bankData.getOrder()) % 3 == 2) {
                card.setBackgroundResource(R.mipmap.yellow_card_background);
            } else {
                card.setBackgroundResource(R.mipmap.black_card_background);
            }*/
        cardNo.setText(bankData.getCardNo());
        if (!TextUtils.isEmpty(bankData.getCustomerCcExpmo())&&!TextUtils.isEmpty(bankData.getCustomerCcExpyr())) {
            dueTime.setText(bankData.getCustomerCcExpmo() + "/" + bankData.getCustomerCcExpyr().substring(2));
        }

//        Map<String, Object> maps = new HashMap<>();
//        maps.put("token", bankData.getCrdStrgToken());
//        getCardDetails(maps);
        if (bankData.getCustomerCcType() != null) {
            if (bankData.getCustomerCcType().equals("10")) {
                logo.setImageResource(R.mipmap.card_visa_logo);
                cardRl.setBackgroundResource(R.mipmap.card_visa_background);
                title.setText("Visa");
            } else if (bankData.getCustomerCcType().equals("20")) {
                logo.setImageResource(R.mipmap.card_master_logo);
                cardRl.setBackgroundResource(R.mipmap.card_master_background);
                title.setText("Master");
            } else if (bankData.getCustomerCcType().equals("60")) {
                title.setText("American Express");
                logo.setImageResource(R.mipmap.card_ame_logo1);
                cardRl.setBackgroundResource(R.mipmap.card_ame_logo);
            } else {
                title.setText("");
                logo.setVisibility(View.INVISIBLE);
                cardRl.setBackgroundResource(R.mipmap.card_ame_logo);
            }
        }
        TextView dateExp = findViewById(R.id.date_exp);
        TextView defaultText = findViewById(R.id.default_text);
        //切换字体
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(cardNo, "fonts/acumin_boldPro.ttf");
        TypefaceUtil.replaceFont(dateExp, "fonts/acumin_regularPro.ttf");
        TypefaceUtil.replaceFont(dueTime, "fonts/acumin_regularPro.ttf");
        TypefaceUtil.replaceFont(defaultText, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(editCard, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(deleteCard, "fonts/gilroy_semiBold.ttf");

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(cardRl, "translationX", 0, 200);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator)/*.with(objectAnimator1).with(objectAnimator2)*/;
        animatorSet.setDuration(500);

        animatorSet.start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back:
                finish();
                break;
            case R.id.edit_card:
                Intent intent = new Intent(CardDetailActivity.this, NewAddBankCardActivity.class);
                intent.putExtra("bankDatas", bankData);
                startActivity(intent);
                break;
            case R.id.delete_card:
                if (switchView.getTag().equals("open")) {
                    return;
                }

                TiShiDialog dialog = new TiShiDialog(CardDetailActivity.this);
                dialog.setOnGuanBiLeft(new TiShiDialog.GuanBiLeft() {//左边按钮
                    @Override
                    public void GuanBiLeft() {

                        UntieAccount untieAccount = new UntieAccount(CardDetailActivity.this);
                        untieAccount.setOnUntieAccount(new UntieAccount.OnUntieAccount() {
                            @Override
                            public void status(String status) {
                                //账户删除状态
                                //解绑状态
                                // 0:解绑中 1：理财解绑失败 2： 分期付解绑失败
                                // 3：账户解绑失败 4：账户解绑成功 5：理财、分期付解绑失败 6：三方解绑失败
                                UntieAccountStatus(status, "");
                            }
                        });
                        Map<String, Object> maps = new HashMap<>();
                        maps.put("cardId", bankData.getId());
                        untieAccount.getStatus(maps);
                    }
                });
                dialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                    @Override
                    public void GuanBi() {

                    }
                });
                dialog.ShowDialog("Are you sure?", "Are you sure to remove your card\nfrom your wallet?", "Cancel");
        }

    }

    /**
     * 个人账户解绑状态处理
     *
     * @param status
     */
    private void UntieAccountStatus(String status, String accountId) {
        switch (status) {
            case "0":
                TiShiDialog tiShiDialog = new TiShiDialog(this);
                tiShiDialog.ShowDialog("Card removal failed", "Confirm");
                break;

            case "3":
            case "6":
                //三方解绑失败
                //账户解绑失败
                deleteAccountOk("Card removal failed");
                break;

            case "4":
                //账户解绑成功
                TiShiDialog tiShiDialog1 = new TiShiDialog(this);
                tiShiDialog1.ShowDialog("Card removed successfully", "Confirm");
                tiShiDialog1.setOnGuanBi(new TiShiDialog.GuanBi() {
                    @Override
                    public void GuanBi() {
                        Intent intent = new Intent(CardDetailActivity.this, BankCardListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
                break;
        }
    }

    /**
     * 删除账户成功
     */
    private void deleteAccountOk(String text) {
        TiShiDialog tiShiDialog = new TiShiDialog(this);
        tiShiDialog.ShowDialog(text, "Confirm");
    }

    /**
     * 获取银行卡详情
     *
     * @param maps
     */
    public void getCardDetails(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(CardDetailActivity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("获取银行卡详情参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.getCardDetails(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("获取银行卡详情:" + dataStr);
                String customerCcExpyr = "";
                String customerCcExpmo = "";
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(dataStr);
                    customerCcExpyr = jsonObject.getString("customerCcExpyr");
                    customerCcExpmo = jsonObject.getString("customerCcExpmo");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(customerCcExpyr) && !TextUtils.isEmpty(customerCcExpmo)) {
                    dueTime.setText(customerCcExpmo + "/" + customerCcExpyr.substring(2));
                }
            }


            @Override
            public void resError(String error) {

            }
        });
    }
}