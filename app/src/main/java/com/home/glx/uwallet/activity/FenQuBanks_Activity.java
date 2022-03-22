package com.home.glx.uwallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.BankListAdapter;
import com.home.glx.uwallet.datas.BankDatas;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.mvp.GetBankList_in;
import com.home.glx.uwallet.mvp.GetBankList_p;
import com.home.glx.uwallet.mvp.JiHuoFenQi_in;
import com.home.glx.uwallet.mvp.JiHuoFenQi_p;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.ReplenishBottomDialog;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 分期选择银行卡
 */
public class FenQuBanks_Activity extends MainActivity
        implements GetBankList_in.View, BankListAdapter.OnItemClick,
        JiHuoFenQi_in.InitFenQiView {

    private ImageView idBack;
    private RecyclerView idRecyclerview;
    private BankListAdapter adapter;
    private JiHuoFenQi_p jiHuoFenQi_p;

    private GetBankList_p present;

    private BankDatas choiceBanks;

    private ReplenishBottomDialog replenishBottomDialog;

    private int backFlag;
    private int selectFlag;
    private SharePreferenceUtils user_sharePreferenceUtils;
    private String cardNum;

    @Override
    public int getLayout() {
        return R.layout.activity_fenqibank;
    }

    @Override
    public void initView() {
        super.initView();
        user_sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        cardNum = (String) user_sharePreferenceUtils.get(StaticParament.USER_FQ_ACCOUNT, "");
        int flag = getIntent().getIntExtra("fromMain", 0);
        backFlag = getIntent().getIntExtra("fromFenQiFu", 0);
        selectFlag = getIntent().getIntExtra("selectAccount", 0);
        if (flag == 1) {
            replenishBottomDialog = new ReplenishBottomDialog(this);
            replenishBottomDialog.show();
        }
        present = new GetBankList_p(this, this);
        jiHuoFenQi_p = new JiHuoFenQi_p(this, this);

        idBack = (ImageView) findViewById(R.id.id_back);
        idBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView titleText = findViewById(R.id.title_text);
        //切换字体
        TypefaceUtil.replaceFont(titleText, "fonts/gilroy_semiBold.ttf");

        idRecyclerview = (RecyclerView) findViewById(R.id.id_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        idRecyclerview.setLayoutManager(linearLayoutManager);
        getBankList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getBankList() {
        Map<String, Object> maps = new HashMap<>();
        //0:账户  1:卡支付
        maps.put("cardType", "0");
        present.loadBankList(maps);
    }

    @Override
    public void setBankList(List<BankDatas> list) {
        if (list == null) {
            return;
        }
        choiceBanks = null;
        adapter = new BankListAdapter(getIntent().getStringExtra("accountId"),this, list, cardNum, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定按钮点击
                if (choiceBanks == null) {
                    Toast.makeText(FenQuBanks_Activity.this, R.string.select_repay_account, Toast.LENGTH_SHORT).show();
                    return;
                }
                String card = "****" + choiceBanks.getCardNo().substring(choiceBanks.getCardNo().length() - 4);
                if (card.equals(cardNum)) {
                    fenQiFile();
                    return;
                }
                //verified,卡是否通过当前支付渠道验证, 0:未验证,1:通过,2:验证失败
                //needInsert如果是true，证明是illion返回的卡列表，需要弹窗，调用tienCard接口，关半窗刷新列表。
                // 这时needinsert变成false，verified变成1，再submit调用creditTieOnCard()
                if (choiceBanks.isNeedInsert()) {
                    replenishBottomDialog = new ReplenishBottomDialog(FenQuBanks_Activity.this, choiceBanks, true);
                    replenishBottomDialog.show();
                } else {
                    //needInsert如果是false，判断verified：如果是1调用creditTieOnCard();
                    // 如果是0、2开半窗，修改完调用ChangeCredit()
                    if (choiceBanks.getVerified().equals("1")) {
                        jihuoFenQi();
                    } else {
                        replenishBottomDialog = new ReplenishBottomDialog(FenQuBanks_Activity.this, choiceBanks, false);
                        replenishBottomDialog.show();
                    }
                }

/*                if (choiceBanks.getVerified().equals("0")) {
                    //if (choiceBanks.isNeedInsert()) {
                        //绑卡
                        //reqAddBankCard(1);
                        //先弹窗提示补充信息，补充信息调用tieonCard绑定账户，成功后绑定分期付
                        ReplenishBottomDialog replenishBottomDialog = new ReplenishBottomDialog(FenQuBanks_Activity.this, choiceBanks);
                        replenishBottomDialog.show();
                    //}
                } else if (choiceBanks.getVerified().equals("1")) {
                    if (choiceBanks.isNeedInsert()) {
                        //绑卡
                        reqAddBankCard(0);
                    } else {
                        jihuoFenQi();
                    }
                }*/
            }
        });
        adapter.setOnItemClick(this);
        idRecyclerview.setAdapter(adapter);
    }

    /**
     * 添加银行卡
     */
    public void reqAddBankCard() {

        Map<String, Object> maps = new HashMap<>();
//        1、绑卡 0、绑账户
        maps.put("type", "0");
        //银行卡号
        maps.put("cardNo", String.valueOf(choiceBanks.getCardNo().replace(" ", "")));
        maps.put("bankName", String.valueOf(choiceBanks.getBankName()));
        maps.put("bsb", String.valueOf(choiceBanks.getBsb().replace(" ", "")));
        maps.put("name", String.valueOf(choiceBanks.getAccountName()));
        maps.put("email", choiceBanks.getEmail());
        addBankAccount(maps);
    }

    private void jihuoFenQi() {
        if (choiceBanks == null) {
            Toast.makeText(this, R.string.select_repay_account, Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * 只能选择银行卡
         * accountId	是	Long	账户id
         * bankcardBank	是	String	银行名称
         * bankcardCode	是	String	银行编码
         * bankcardHolderName	是	String	银行持有人姓名
         * bankcardNumber	是	String	银行卡号
         * */
        Map<String, Object> maps = new HashMap<>();
        maps.put("accountId", choiceBanks.getId());
        //maps.put("bankcardBank", choiceBanks.getBankName());
        maps.put("bankcardCode", choiceBanks.getBsb());
        //String name = choiceBanks.getFirstName() + "*" + choiceBanks.getMiddleName() + "*" + choiceBanks.getLastName();
        //maps.put("bankcardHolderName", name);
        maps.put("bankcardNumber", choiceBanks.getCardNo());

        Map<String, Object> map = new HashMap<>();
        map.put("activateCreditMessageDTO", maps);

        jiHuoFenQi_p.initFenQi(map);
    }

    private void jihuoFenQi(String id) {
        if (choiceBanks == null) {
            Toast.makeText(this, R.string.select_repay_account, Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * 只能选择银行卡
         * accountId	是	Long	账户id
         * bankcardBank	是	String	银行名称
         * bankcardCode	是	String	银行编码
         * bankcardHolderName	是	String	银行持有人姓名
         * bankcardNumber	是	String	银行卡号
         * */
        Map<String, Object> maps = new HashMap<>();
        maps.put("accountId", id);
        //maps.put("bankcardBank", choiceBanks.getBankName());
        maps.put("bankcardCode", choiceBanks.getBsb());
        //String name = choiceBanks.getFirstName() + "*" + choiceBanks.getMiddleName() + "*" + choiceBanks.getLastName();
        //maps.put("bankcardHolderName", name);
        maps.put("bankcardNumber", choiceBanks.getCardNo());

        Map<String, Object> map = new HashMap<>();
        map.put("activateCreditMessageDTO", maps);

        jiHuoFenQi_p.initFenQi(map);
    }

    @Override
    public void choiceBank(BankDatas bankDatas) {
        choiceBanks = bankDatas;
    }

    @Override
    public void bankClick(BankDatas bankDatas) {
//        choiceBanks = null;
//        Intent bankDetails = new Intent(this, BankDetailsActivity.class);
//        bankDetails.putExtra("bankDatas", bankDatas);
//        startActivity(bankDetails);
    }

    /**
     * 去添加银行账户
     */
    @Override
    public void addBank() {
//        Intent intent = new Intent(FenQuBanks_Activity.this,
//                AddBankCard_Activity.class);
//        intent.putExtra("type", "1");
//        startActivityForResult(intent, 1006);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1006 && resultCode == Activity.RESULT_OK) {
            getBankList();
        }
    }

    private void fenQiFile() {
//        Intent intent_loading = new Intent(this, FenQiLoadingActivity.class);
/*        Intent intent_loading = new Intent(this, FenQiStatusActivity.class);
        startActivity(intent_loading);*/
        /*if (backFlag != 1) {
            Intent intent_fenqi = new Intent(this,
                    FenQiFu_Activity.class);
            startActivity(intent_fenqi);
        }*/
        Intent intent;
        if (user_sharePreferenceUtils.get(StaticParament.FROM_PAY_MONEY, "").equals("1")) {
            intent = new Intent(this, PayMoneyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        } else {
            if (selectFlag == 0) {
                new SharePreferenceUtils(this, StaticParament.USER).put(StaticParament.PAY_SUCCESS, "1").commit();//为了wallet页面刷新
                intent = new Intent(this, MainTab.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        }
        finish();
    }

   /* public void showSplitError() {
        TiShiDialog tiShiDialog = new TiShiDialog(this);
        tiShiDialog.ShowDialog("Verification error, please check and confirm the information you have provided is correct.", "Try Again");
    }*/

    public void showSplitSuccess() {
        //fenQiFile();
        getBankList();
    }

    /**
     * 添加银行账户
     *
     * @param maps
     */
    public void addBankAccount(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(FenQuBanks_Activity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.addBankAccount(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("添加银行账户:" + dataStr);
                /*if (dataStr != null && !dataStr.equals("")) {
                    if (flag == 0) {
                        jihuoFenQi(dataStr);
                    }  else if (flag == 2) {
                        onResume();
                    }
                } else {
                    onResume();
                }*/
                getBankList();
                /*TiShiDialog tiShiDialog = new TiShiDialog(AddBankCard_Activity.this);
                tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                    @Override
                    public void GuanBi() {
                        finish();
                    }
                });
                if (addType == 1) {
                    //绑卡
                    tiShiDialog.ShowDialog(getString(R.string.bind_card_success));
                } else {
                    //账户
                    tiShiDialog.ShowDialog(getString(R.string.bind_account_success));
                }*/
            }


            @Override
            public void resError(String error) {
                getBankList();
            }
        });
    }

    /**
     * 激活分期付返回
     *
     * @param status
     */
    @Override
    public void setStatus(String status) {
        //触发风控
//        FenQiFK_p.reqActivationInstallment(new HashMap<String, Object>());
        fenQiFile();
    }

}
