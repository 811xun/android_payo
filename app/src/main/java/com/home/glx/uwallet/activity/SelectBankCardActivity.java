package com.home.glx.uwallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.SelctBankListAdapter;
import com.home.glx.uwallet.datas.BankDatas;
import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.datas.GetChoiceDatas;
import com.home.glx.uwallet.datas.TransAmountData;
import com.home.glx.uwallet.mvp.GetChoiceList_in;
import com.home.glx.uwallet.mvp.GetChoiceList_p;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.KycAndIllionListener;
import com.home.glx.uwallet.mvp.contract.PayListener;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.KycAndIllionModel;
import com.home.glx.uwallet.mvp.model.PayModel;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class SelectBankCardActivity extends MainActivity implements GetChoiceList_in.View {
    private RecyclerView recyclerView;
    private UserListener userListener;
    private KycAndIllionListener kycAndIllionListener;
    private List<BankDatas> cardLists;
    private SelctBankListAdapter adapter;
    private BankDatas choiceData;
    private ImageView back;
    private TextView proText;
    private TextView grayBtn;
    private TextView next;
    private CheckBox checkBox;
    private int fromPay;
    private int fromCreateFenqi;
    private String selectCardId;
    private GetChoiceList_p choiceList_p;
    private List<ChoiceDatas> htmlList1 = new ArrayList<>();
    private HashMap<String, Object> requestMap;
    private int fromPayFailed;
    private PayListener payListener;
    private TextView protocolTv;
    private int fromFenqi;

    @Override
    public int getLayout() {
        return R.layout.activity_select_bank_card;
    }

    @Override
    public void initView() {
        super.initView();
        payListener = new PayModel(this);
        requestMap = (HashMap<String, Object>) getIntent().getSerializableExtra("requestMap");
        fromPayFailed = getIntent().getIntExtra("fromPayFailed", 0);
        //上次选中的卡id
        selectCardId = getIntent().getStringExtra("cardId");
        fromFenqi = getIntent().getIntExtra("fromFenqi", 0);
        fromPay = getIntent().getIntExtra("fromPay", 0);
        fromCreateFenqi = getIntent().getIntExtra("fromCreateFenqi", 0);
        back = findViewById(R.id.id_back);
        if (fromCreateFenqi == 1) {
            back.setVisibility(View.GONE);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userListener = new UserModel(this);
        kycAndIllionListener = new KycAndIllionModel(this);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        TextView title = findViewById(R.id.title);
        proText = findViewById(R.id.id_protocol_text);
        protocolTv = findViewById(R.id.id_protocol_tv);
        grayBtn = findViewById(R.id.gray_btn);
        next = findViewById(R.id.next);

        checkBox = findViewById(R.id.id_protocols_check);
        choiceList_p = new GetChoiceList_p(this, this);
        Map<String, Object> maps = new HashMap<>();
        String[] keys = new String[]{"bnpl-tac"};
        maps.put("code", keys);
//        choiceList_p.loadChoiceList(maps);

        if (fromPay == 1 || fromPayFailed == 1) {
            checkBox.setVisibility(View.GONE);
            grayBtn.setVisibility(View.GONE);
            next.setVisibility(View.VISIBLE);
            proText.setVisibility(View.GONE);
            if (fromFenqi == 1) {
                protocolTv.setVisibility(View.VISIBLE);
            }
        } else {

        }
//        if (getIntent().getBooleanExtra("showWenan",false)){
//            protocolTv.setVisibility(View.VISIBLE);
//        }
        proText.setText("I authorise Payo Funds Pty Ltd ACN 638 179 567\n" +
                "and its associated party, to debit the\n" +
                "nominated bank (card) account outlined above.");
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && choiceData != null) {
                    grayBtn.setVisibility(View.GONE);
                    next.setVisibility(View.VISIBLE);
                } else {
                    grayBtn.setVisibility(View.VISIBLE);
                    next.setVisibility(View.GONE);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choiceData != null) {
                    if (fromPay == 1) {
                        Intent intent = SelectBankCardActivity.this.getIntent();
                        Bundle bundle = intent.getExtras();
                        String cardN = choiceData.getCardNo().replaceAll(" ", "");
                        cardN = "**** " + cardN.substring(cardN.length() - 4);
                        bundle.putString("cardNo", cardN);//添加要返回给页面1的数据
                        bundle.putString("cardId", choiceData.getId());//添加要返回给页面1的数据
                        intent.putExtras(bundle);
                        SelectBankCardActivity.this.setResult(Activity.RESULT_OK, intent);//返回页面1
                        SelectBankCardActivity.this.finish();
                    } else if (fromPayFailed == 1) {
                        requestMap.put("cardId", choiceData.getId());
                        getPayAmount();
                    } else {
                        Map<String, Object> map = new HashMap<>();
                        map.put("cardId", choiceData.getId());
                        map.put("isCreditCard", "1");
                        kycAndIllionListener.setDefaultCard(map, new ActionCallBack() {
                            @Override
                            public void onSuccess(Object... o) {
                                TiShiDialog tiShiDialog = new TiShiDialog(SelectBankCardActivity.this);
                                tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                                    @Override
                                    public void GuanBi() {
//                                        Intent intent = new Intent(SelectBankCardActivity.this, FenQiFuOpen_Activity.class);
//                                        intent.putExtra("result", "finish");
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                tiShiDialog.ShowDialog("Your bank card has been successfully added", 17);
                            }

                            @Override
                            public void onError(String e) {

                            }
                        });
                    }
                }
            }
        });
        //切换字体
        TypefaceUtil.replaceFont(protocolTv, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(next, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(proText, "fonts/gilroy_regular.ttf");
        getBankList();
    }

    private void getPayAmount() {
        //支付失败获取交易详情
        payListener.getPayTransAmountDetail(requestMap, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                TransAmountData data = (TransAmountData) o[0];
                Intent intent = new Intent(SelectBankCardActivity.this, PayMoneyActivity.class);
                intent.putExtra("data", (Serializable) data);
                intent.putExtra("requestMap", requestMap);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String e) {

            }
        });
    }

//    private void getBankList() {
//        Map<String, Object> maps = new HashMap<>();
//        //0:账户  1:卡支付
//        maps.put("cardType", "1");
//        userListener.getCardList(maps, new ActionCallBack() {
//            @Override
//            public void onSuccess(Object... o) {
//                List<BankDatas> list = (List<BankDatas>) o[0];
//                setBankList(list);
//            }
//
//            @Override
//            public void onError(String e) {
//
//            }
//        });
//    }

    public void getBankList() {
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(SelectBankCardActivity.this, StaticParament.USER);
        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
        final Map<String, Object> maps = new HashMap<>();
        RequestUtils requestUtils1 = new RequestUtils(this, maps, StaticParament.zhifu, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(SelectBankCardActivity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("绑卡获取卡类型接口参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.allCardList(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                Gson gson1 = new Gson();
                List<BankDatas> bankList = gson1.fromJson(dataStr, new TypeToken<List<BankDatas>>() {
                }.getType());
                setBankList(bankList);
            }

            @Override
            public void resError(String error) {
                L.log("xzcxzcxzc:" + error);
            }
        });
    }

    public void setBankList(List<BankDatas> list) {
        if (list == null) {
            Toast.makeText(this, "Poor network connection. Please try again..", Toast.LENGTH_SHORT).show();
            return;
        }
        cardLists = list;
        if (adapter == null) {
            adapter = new SelctBankListAdapter(this, list, selectCardId);
            recyclerView.setAdapter(adapter);
            adapter.setOnitemClick(new SelctBankListAdapter.OnitemClick() {
                @Override
                public void itemClick(BankDatas bankDatas) {
                    choiceData = bankDatas;
                    if (checkBox.isChecked() && grayBtn.getVisibility() == View.VISIBLE) {
                        grayBtn.setVisibility(View.GONE);
                        next.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void saveCard() {

                }

                @Override
                public void addCard() {
                    if (!PublicTools.isFastClick()) {
                        return;
                    }
                    Intent intent = new Intent(SelectBankCardActivity.this, NewAddBankCardActivity.class);
                    intent.putExtra("fromSelect", 1);
                    intent.putExtra("from", "fenqiList");
                    startActivityForResult(intent, 1006);
                }
            });
        } else {
            adapter.notifyDataChanged(list);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1006 && resultCode == Activity.RESULT_OK) {
            selectCardId = "";
            if (adapter != null) {
                adapter.clearPosition();
            }
            getBankList();
        }
    }

    private void addressStyle(TextView textView) {
        textView.setHighlightColor(getResources().getColor(android.R.color.transparent));
        SpannableString spannableString = new SpannableString("I have read and agree the Pay Later Terms and Conditions and authorise Payo Funds Pty Ltd ACN 638 179 567 and its associated party, to debit the nominated bank (card) account outlined above.");
        //英文
        //隐私政策
        MyClickableSpan clickableSpan2 = new MyClickableSpan();
        spannableString.setSpan(clickableSpan2, "I have read and agree the ".length(), "I have read and agree the Pay Later Terms and Conditions".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
        spannableString.setSpan(colorSpan1, "I have read and agree the ".length(), "I have read and agree the Pay Later Terms and Conditions".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), "I have read and agree the ".length(), "I have read and agree the Pay Later Terms and Conditions".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString);
    }

    @Override
    public void setChoiceList(GetChoiceDatas getChoiceDatas) {
        if (getChoiceDatas != null) {
            /*if (getChoiceDatas.getPrivacyAgreement() != null && getChoiceDatas.getTermAndConditions() != null) {
                htmlList1.addAll(getChoiceDatas.getPrivacyAgreement());
                htmlList.addAll(getChoiceDatas.getTermAndConditions());
                addressNewStyle(proText);
            }*/

            if (getChoiceDatas.getBnplTac() != null) {
                htmlList1.addAll(getChoiceDatas.getBnplTac());
                addressStyle(proText);
            }
        }
    }

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
            ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
            Intent intent = new Intent();
            intent.setClass(SelectBankCardActivity.this, PdfActivity.class);
            intent.putExtra("url", htmlList1.get(0).getCName());
            startActivity(intent);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //do something.
            //禁用返回键
            if (fromCreateFenqi == 1) {
                return true;
            } else {
                finish();
                return super.dispatchKeyEvent(event);
            }
        } else {
            return super.dispatchKeyEvent(event);
        }
    }
}