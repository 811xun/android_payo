package com.home.glx.uwallet.mvp;

import com.home.glx.uwallet.datas.BankDatas;

import java.util.List;
import java.util.Map;

/**
 * 获取所有银行列表
 */
public interface GetBankList_in {

    interface View {
        void setBankList(List<BankDatas> list);
    }

    interface Model {
        void getBankList(Map<String, Object> maps);
    }

    interface Present {
        void loadBankList(Map<String, Object> maps);

        void resBankList(String data);
    }

}
