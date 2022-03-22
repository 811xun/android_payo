package com.home.glx.uwallet.fragment;

import android.content.Intent;

import com.home.glx.uwallet.BaseFragment;
import com.home.glx.uwallet.activity.BankCardListActivity;
import com.home.glx.uwallet.activity.UserMsgActivity;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

/**
 * 用来处理各种认证状态
 */
public abstract class FirstTabFragment extends BaseFragment {

    public SharePreferenceUtils user_sharePreferenceUtils;

    @Override
    public void initView() {
        user_sharePreferenceUtils = new SharePreferenceUtils(getContext(), StaticParament.USER);
    }

    /**
     * 支付KYC认证通过，之后的操作
     *
     * @param code
     */
    public void zhifuOkNext(String code) {
        user_sharePreferenceUtils.put(StaticParament.USER_ZHIFU, "1");
        user_sharePreferenceUtils.save();
        switch (code) {
            case "sk":
                //收款
                //打开收款码
                break;

            case "qb":
                //钱包
                //打开钱包
//                Intent intent_qb = new Intent(getActivity(), Wallet_Activity.class);
//                startActivity(intent_qb);
                break;

            case "zf":
                //查看用户信息
                Intent intent_user_msg = new Intent(getActivity(), UserMsgActivity.class);
                startActivity(intent_user_msg);
                break;
            case "jymx":
                //交易明细
//                Intent intent_list = new Intent(getContext(), WalletTransactionList_Activity.class);
//                startActivity(intent_list);
                break;
            case "bank":
                Intent intent_userbank_msg = new Intent(getActivity(), BankCardListActivity.class);
                startActivity(intent_userbank_msg);
                break;
            case "wallet":
//                Intent intent_wallet = new Intent(getActivity(), Wallet_Activity.class);
//                startActivity(intent_wallet);
                break;
        }
    }
}
