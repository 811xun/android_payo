package com.home.glx.uwallet.mvp;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.home.glx.uwallet.datas.BankDatas;
import com.home.glx.uwallet.tools.L;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetBankList_p implements GetBankList_in.Present {

    private Context context;
    private GetBankList_in.View view;
    private GetBankList_m model;

    private List<BankDatas> bankList;
    private boolean getFinancesData = true;

    public GetBankList_p(Context context, GetBankList_in.View view) {
        this.context = context;
        this.view = view;
        model = new GetBankList_m(context, this);
    }

    public void setLoadFinances(boolean type) {
        getFinancesData = type;
    }

    @Override
    public void loadBankList(Map<String, Object> maps) {
        model.getBankList(maps);
    }

    @Override
    public void resBankList(String data) {
        L.log("获取卡列表aaa:" + data);
        if (data == null) {
            view.setBankList(null);
            return;
        }
        Gson gson1 = new Gson();
        bankList = gson1.fromJson(data, new TypeToken<List<BankDatas>>() {
        }.getType());
        //if (getFinancesData) {
        //    getFinancesAccount();
        //} else {
            view.setBankList(bankList);
        //}
    }
}
