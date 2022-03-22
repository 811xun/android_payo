package com.home.glx.uwallet.fragment;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.home.glx.uwallet.BaseFragment;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.BorrowByPayDayV2Data;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.selfview.InstalmentListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewInstalmentFragment extends BaseFragment {
    private LinearLayout contentLl;
    private ImageView noBills;
    private ImageView noInstal;
    private UserListener userListener;
    private int width;

    @Override
    public int getLayout() {
        return R.layout.fragment_new_instalment;
    }

    @Override
    public void initView() {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        noBills = view.findViewById(R.id.no_bills);
        noInstal = view.findViewById(R.id.no_instal);
        width = metric.widthPixels;
        userListener = new UserModel(getContext());
        contentLl = view.findViewById(R.id.content_ll);
        if (((WalletFragment) getParentFragment()).noInsFlag) {
            noBills.setVisibility(View.VISIBLE);
            noInstal.setVisibility(View.GONE);
            getData(false);
        } else {
            noInstal.setVisibility(View.VISIBLE);
            noBills.setVisibility(View.GONE);
        }
    }

    public void instalmentFlag(int flag) {
        if (flag == 0) {//没有开通分期付
            noInstal.setVisibility(View.VISIBLE);
            noBills.setVisibility(View.GONE);

        } else {
            if (noInstal.getVisibility() == View.VISIBLE) {
                getData(true);
            }
//            noBills.setVisibility(View.VISIBLE);  //不能有这样代码 否则点击首页的未支付订单弹窗 会展示列表+noBills view
            noInstal.setVisibility(View.GONE);
        }
    }

    private void getData(final boolean refresh) {
        Map<String, Object> map = new HashMap<>();
        userListener.borrowByPaydayV2(map, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                if (refresh) {
                    contentLl.removeAllViews();
                }
                List<BorrowByPayDayV2Data> data = (List<BorrowByPayDayV2Data>) o[0];
                if (data.size() == 0) {
                    noBills.setVisibility(View.VISIBLE);
                    noInstal.setVisibility(View.GONE);
                } else {
                    noBills.setVisibility(View.GONE);
                    noInstal.setVisibility(View.GONE);
                }
                List<String> borrowIdList = new ArrayList<>();
                List<String> repayIdList = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    borrowIdList.addAll(data.get(i).getBorrowIdList());
                    repayIdList.addAll(data.get(i).getRepayIdList());
                }
                String totalAmount = (String) o[1];
                if (!TextUtils.isEmpty(totalAmount) && Float.parseFloat(totalAmount) > 0) {
                    ((WalletFragment) getParentFragment()).setPayAllData(totalAmount, borrowIdList, repayIdList, data);
                } else {
                    ((WalletFragment) getParentFragment()).setPayAllData("", borrowIdList, repayIdList, data);
                }
                for (int i = 0; i < data.size(); i++) {
                    InstalmentListView instalmentListView = new InstalmentListView(width, getContext(), data.get(i));
                    contentLl.addView(instalmentListView.getView());
                }
            }

            @Override
            public void onError(String e) {

            }
        });
    }

    public void refresh() {
        if (noInstal.getVisibility() == View.GONE) {
            getData(true);
        }
    }

}
