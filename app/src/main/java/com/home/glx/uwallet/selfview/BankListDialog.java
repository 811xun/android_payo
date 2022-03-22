package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.CardBankDialogAdapter;
import com.home.glx.uwallet.datas.BankDatas;
import com.home.glx.uwallet.datas.PayTransAmountData;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.tools.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class BankListDialog {
    private Context context;
    private MyDialog dialog;
    private String cardId;
    private List<BankDatas> list;
    private List<BankDatas> sortList = new ArrayList<>();
    private CardBankDialogAdapter adapter;
    private ListView listView;
    private Button back;
    private ImageView dismiss;
    private  ConfirmPayBottomDialog confirmPayBottomDialog;
    private Map<String, Object> moneyMap;
    private String posTransNo;

    public BankListDialog(Map<String, Object> moneyMap, ConfirmPayBottomDialog confirmPayBottomDialog, Context context, List<BankDatas> list, String cardId, String posTransNo) {
        this.confirmPayBottomDialog = confirmPayBottomDialog;
        this.moneyMap = moneyMap;
        this.context = context;
        this.list = list;
        this.cardId = cardId;
        this.posTransNo = posTransNo;
    }

    public void show() {
        dialog = new MyDialog(context, R.style.MyRadiusDialog, R.layout.dialog_bank_list);
        dialog.show();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        listView = dialog.findViewById(R.id.card_list);
        back = dialog.findViewById(R.id.back);
        dismiss = dialog.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getId().equals(cardId)) {
                    sortList.add(list.get(i));
                    break;
                }
            }
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).getId().equals(cardId)) {
                    sortList.add(list.get(i));
                }
            }
        }
        adapter = new CardBankDialogAdapter(context, sortList, cardId);
        listView.setAdapter(adapter);
        adapter.setOnitemClick(new CardBankDialogAdapter.OnitemClick() {
            @Override
            public void itemClick(String cardNo, String id, String type) {
                moneyMap.put("cardId", id);
                if (!TextUtils.isEmpty(posTransNo)) {
                    moneyMap.put("posTransNo", posTransNo);
                }
                adapter.notifyData(id);
                getQrPayTransAmount(cardNo, id, type, moneyMap);
                //onitemClick.itemClick(cardNo, id);
            }
        });
    }

    public interface OnitemClick {
        void itemClick(String cardNo, String id);
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }

    public void getQrPayTransAmount(final String cardNo, final String cardId, final String type, Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(context, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("更换银行卡的交易金额参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.getQrPayTransAmount(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("更换银行卡的交易金额：" + dataStr);
                Gson gson = new Gson();
                PayTransAmountData payTransAmountData = gson.fromJson(dataStr, PayTransAmountData.class);
                Map<String, Object> map = new HashMap<>();
                map.put("cardNo", cardNo);
                map.put("cardId", cardId);
                map.put("cardType", type);
                map.put("mixPayAmount", payTransAmountData.getMixPayAmount());
                map.put("payAmount", payTransAmountData.getPayAmount());
                map.put("payAmountUseRedEnvelope", payTransAmountData.getPayAmountUseRedEnvelope());
                map.put("channelFeeRedEnvelope", payTransAmountData.getChannelFeeRedEnvelope());
                map.put("channelFeeNoRedEnvelope", payTransAmountData.getChannelFeeNoRedEnvelope());
                map.put("transChannelFeeRate", payTransAmountData.getTransChannelFeeRate());
                confirmPayBottomDialog.changeFee(map);
                dialog.dismiss();
            }


            @Override
            public void resError(String error) {
                adapter.notifyData(cardId);
            }
        });
    }
}
