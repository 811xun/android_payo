package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.InstalmentPendingDetailsActivity;
import com.home.glx.uwallet.activity.InstalmentPendingNextActivity;
import com.home.glx.uwallet.adapter.BorrowByInstalmentAdapter;
import com.home.glx.uwallet.datas.BorrowByPayDayV2Data;
import com.home.glx.uwallet.datas.PayDayBorrowDetailData;
import com.home.glx.uwallet.datas.PayDayBorrowInfoData;
import com.home.glx.uwallet.mvp.WhetherOpenInvest_in;
import com.home.glx.uwallet.mvp.WhetherOpenInvest_p;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstalmentListView implements WhetherOpenInvest_in.View {
    private View thisView;
    private Context context;
    private BorrowByPayDayV2Data data;
    private LayoutInflater inflater;
    private RecyclerView list;
    private TextView date;
    private TextView payAll;
    private BorrowByInstalmentAdapter adapter;
    private int width;
    private UserListener userListener;
    //是否开通个业务
    private WhetherOpenInvest_p openInvestPresent;
    private ConfirmRepayDialog confirmRepayDialog;
    private String overdueFeeId;
    private String amount;

    public InstalmentListView(int width, Context context, BorrowByPayDayV2Data data) {
        this.context = context;
        this.width = width;
        this.data = data;
        init();
    }

    public View getView() {
        return thisView;
    }

    private void init() {
        userListener = new UserModel(context);
        openInvestPresent = new WhetherOpenInvest_p(context, this);
        inflater = LayoutInflater.from(context);
        thisView = inflater.inflate(R.layout.view_instalment_list, null);
        list = thisView.findViewById(R.id.list);
        date = thisView.findViewById(R.id.date);
        payAll = thisView.findViewById(R.id.pay_all);
        int nameWidth = width - PublicTools.dip2px(context, 46);
        payAll.setMaxWidth(nameWidth);
        //切换字体|
        TypefaceUtil.replaceFont(date, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(payAll, "fonts/gilroy_semiBold.ttf");
        date.setText(data.getRepayDayStr());
        payAll.setText(String.format("Pay $%s for %s", PublicTools.twoend(data.getRepayDayTotalAmount()), data.getRepayDayStr()));
        list.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        list.setLayoutManager(linearLayoutManager);
        payAll.setOnClickListener(new View.OnClickListener() {//橙色文字
            @Override
            public void onClick(View v) {
                openInvestPresent.loadOpenInvestStatus("card");
            }
        });
        thisView.findViewById(R.id.iv_right).setOnClickListener(new View.OnClickListener() {//橙色箭头
            @Override
            public void onClick(View v) {
                payAll.performClick();
            }
        });
        adapter = new BorrowByInstalmentAdapter(context, data);
        adapter.setOnitemClick(new BorrowByInstalmentAdapter.OnitemClick() {
            @Override
            public void onClick(String borrowId, String repayId) {
                if (!PublicTools.isFastClick()) {
                    return;
                }
                Intent intent = new Intent(context, InstalmentPendingDetailsActivity.class);
                intent.putExtra("id", borrowId);
                intent.putExtra("repayId", repayId);
                context.startActivity(intent);
            }

            @Override
            public void onClickLateFee(String overDueFeeId, String amounts) {
                if (!PublicTools.isFastClick()) {
                    return;
                }
                overdueFeeId = overDueFeeId;
                amount = amounts;
                openInvestPresent.loadOpenInvestStatus("sm");
            }
        });
        list.setAdapter(adapter);
    }

    @Override
    public void openInvestStatus(String code, String fenqiCard, String zhifu, String fenqi) {
        if (code.equals("sm")) {
            Intent intent = new Intent(context, InstalmentPendingNextActivity.class);
            List<String> overdueFeeIds = new ArrayList<>();
            overdueFeeIds.add(overdueFeeId);
            intent.putExtra("overdueFeeId", (Serializable) overdueFeeIds);
            intent.putExtra("chooseAmount", amount);
            intent.putExtra("feeDate", date.getText().toString());
            if (fenqi.equals("1") && fenqiCard.equals("1")) {
                intent.putExtra("haveFeeCard", "1");
            } else {
                intent.putExtra("haveFeeCard", "0");
            }
            context.startActivity(intent);
        } else {
//            if (fenqi.equals("1") && fenqiCard.equals("1")) {
            if ("1".equals(fenqi) && "1".equals(fenqiCard)) {
                Intent intent = new Intent(context, InstalmentPendingNextActivity.class);
                intent.putExtra("payAll", "payAllDate");
                if (!TextUtils.isEmpty(data.getList().get(data.getList().size() - 1).getType())) {
                    if (data.getList().get(data.getList().size() - 1).getType().equals("2")) {
                        intent.putExtra("lateFeeAmount", data.getList().get(data.getList().size() - 1).getRepayAmount());
                    }
                }
                intent.putExtra("overdueFeeId", (Serializable) data.getOverdueFeeIdList());
                intent.putExtra("repayDate", data.getRepayDayStr());
                intent.putExtra("repayIds", (Serializable) data.getRepayIdList());
                //intent.putExtra("borrowIds", (Serializable) data.getBorrowIdList());
                intent.putExtra("chooseAmount", data.getRepayDayTotalAmount());
                context.startActivity(intent);
            } else {
                Map<String, Object> map = new HashMap<>();
                map.put("repayDay", data.getRepayDay());
                map.put("borrowIdList", data.getBorrowIdList());
                userListener.payDayBorrowDetail(map, new ActionCallBack() {
                    @Override
                    public void onSuccess(Object... o) {
                        PayDayBorrowInfoData infoData = (PayDayBorrowInfoData) o[0];
                        List<PayDayBorrowDetailData> list = (List<PayDayBorrowDetailData>) o[1];
                        confirmRepayDialog = new ConfirmRepayDialog(context, infoData);
                        confirmRepayDialog.setOnitemClick(new ConfirmRepayDialog.OnItemClick() {
                            @Override
                            public void onClick() {
                            }
                        });
                        confirmRepayDialog.show();
                    }

                    @Override
                    public void onError(String e) {

                    }
                });
            }
        }
    }
}
