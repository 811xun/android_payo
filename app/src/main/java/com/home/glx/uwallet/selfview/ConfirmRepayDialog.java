package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.FenQuBanks_Activity;
import com.home.glx.uwallet.activity.RepayKeyiActivity;
import com.home.glx.uwallet.activity.RepayResultActivity;
import com.home.glx.uwallet.datas.PayAllData;
import com.home.glx.uwallet.datas.PayDayBorrowInfoData;
import com.home.glx.uwallet.mvp.CheckPayPassword_in;
import com.home.glx.uwallet.mvp.CheckPayPassword_p;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ConfirmRepayDialog implements CheckPayPassword_in.View {
    private MyDialog dialog;

    private View thisView;
    private Context context;
    private LayoutInflater inflater;
    private ImageView close;
    private TextView merchantName;
    private TextView transactionFee;
    private TextView transaction;
    private TextView instalment;
    private TextView instalmentAmount;
    private TextView bank;
    private TextView bankNum;
    private TextView total;
    private TextView totalAmount;
    private TextView title;
    private TextView pay;
    private String payMoney;
    private String feeMoney;
    //手续费费率方式 0：固定值 ，费率 = 费率值；1：百分比，费率 = 金额*费率值
    private String feeTypes = "0";
    //费率值
    private String rates = "0";
    private SharePreferenceUtils sharePreferenceUtils;
    private PayDayBorrowInfoData infoData;
    private PayAllData payAllData;
    private CheckPayPassword_p checkPayPassword_p;
    private UserListener userListener;
    private int enterBank = 0;
    private int enterPay = 0;
    private TextView late;
    private TextView lateFee;

    public ConfirmRepayDialog(Context context, PayDayBorrowInfoData infoData) {
        this.context = context;
        this.infoData = infoData;
        checkPayPassword_p = new CheckPayPassword_p(context, this);
        userListener = new UserModel(context);
    }

    public ConfirmRepayDialog(Context context, PayAllData payAllData) {
        this.context = context;
        this.payAllData = payAllData;
        checkPayPassword_p = new CheckPayPassword_p(context, this);
        userListener = new UserModel(context);
    }


    public void changeNum(String card) {
        bankNum.setText(card);
    }

    /**
     * 弹出输入支付（PIN）密码弹窗
     */
    private void showPayPwdDialog() {
        RepayPawDialog repayPawDialog = new RepayPawDialog(context);
        repayPawDialog.setOnRepayPwd(new RepayPawDialog.OnRepayPwd() {
            @Override
            public void inputPwd(String password) {
                Map<String, Object> maps = new HashMap<>();
                maps.put("payPassword", password);
                checkPayPassword_p.checkPayPassword(maps);
            }
        });
        repayPawDialog.show();
    }

    @Override
    public void setPasswordStatus(String status) {
        if (status == null) {
            return;
        }
        if (enterPay == 1) {
            if (infoData != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("repayAmount", infoData.getRepayAmount());
                map.put("isPayAll", "false");
                map.put("overDueIds", infoData.getOverDueIds());
                map.put("repayIds", infoData.getNeedPayRepayIdList());
                userListener.repaymentV2(map, new ActionCallBack() {
                    @Override
                    public void onSuccess(Object... o) {
                        sharePreferenceUtils.put(StaticParament.REPAY_SUCCESS, "1");
                        sharePreferenceUtils.save();
                        String dataStr = (String) o[0];
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            if (jsonObject.has("hasOverDue") && jsonObject.getString("hasOverDue").equals("true")) {
                                //String hasOverDue = jsonObject.getString("hasOverDue");
                                //if (!TextUtils.isEmpty(hasOverDue) && hasOverDue.equals("true")) {
                                String title = jsonObject.getString("overDueTitle");
                                String message = jsonObject.getString("overDueMessage");
                                TiShiDialog tiShiDialog = new TiShiDialog(context);
                                tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                                    @Override
                                    public void GuanBi() {
                                        Intent intent = new Intent(context, RepayResultActivity.class);
                                        intent.putExtra("infoData", (Serializable) infoData);
                                        context.startActivity(intent);
                                    }
                                });
                                tiShiDialog.ShowDialog(title, message, "Close");
                                //}
                            } else {
                                String state = jsonObject.getString("state");
                                String message = "";
                                if (state.equals("100200")) {
                                    //还款成功
                                    Intent intent = new Intent(context, RepayResultActivity.class);
                                    intent.putExtra("infoData", (Serializable) infoData);
                                    intent.putExtra("result", "success");
                                    context.startActivity(intent);
                                } else if (state.equals("100300")) {
                                    //还款处理中
//                                Intent intent = new Intent(context, RepayResultActivity.class);
//                                intent.putExtra("infoData", (Serializable)infoData);
                                    Intent intent = new Intent(context, RepayKeyiActivity.class);
                                    context.startActivity(intent);
                                } else if (state.equals("100400")) {
                                    //交易失败
                                    message = context.getString(R.string.transaction_failed);
                                    TiShiDialog tiShiDialog = new TiShiDialog(context);
                                    tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                                        @Override
                                        public void GuanBi() {
                                        }
                                    });
                                    tiShiDialog.ShowDialog(message);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String e) {

                    }
                });
            }
            if (payAllData != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("repayAmount", payAllData.getRepayAmount());
                map.put("isPayAll", "true");
                map.put("borrowIds", payAllData.getBorrowIds());
                map.put("overDueIds", payAllData.getOverDueIds());
                userListener.repaymentV2(map, new ActionCallBack() {
                    @Override
                    public void onSuccess(Object... o) {
                        sharePreferenceUtils.put(StaticParament.REPAY_SUCCESS, "1");
                        sharePreferenceUtils.save();
                        String dataStr = (String) o[0];
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            if (jsonObject.has("hasOverDue") && jsonObject.getString("hasOverDue").equals("true")) {
                                //String hasOverDue = jsonObject.getString("hasOverDue");
                                //if (!TextUtils.isEmpty(hasOverDue) && hasOverDue.equals("true")) {
                                String title = jsonObject.getString("overDueTitle");
                                String message = jsonObject.getString("overDueMessage");
                                TiShiDialog tiShiDialog = new TiShiDialog(context);
                                tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                                    @Override
                                    public void GuanBi() {
                                        Intent intent = new Intent(context, RepayResultActivity.class);
                                        intent.putExtra("payAllData", (Serializable) payAllData);
                                        context.startActivity(intent);
                                        dialog.dismiss();
                                    }
                                });
                                tiShiDialog.ShowDialog(title, message, "Close");
                                //}
                            } else {
                                String state = jsonObject.getString("state");
                                String message = "";
                                if (state.equals("100200")) {
                                    //还款成功
                                    Intent intent = new Intent(context, RepayResultActivity.class);
                                    intent.putExtra("payAllData", (Serializable) payAllData);
                                    intent.putExtra("result", "success");
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                } else if (state.equals("100300")) {
                                    //还款处理中
//                                Intent intent = new Intent(context, RepayResultActivity.class);
//                                intent.putExtra("payAllData", (Serializable)payAllData);
                                    Intent intent = new Intent(context, RepayKeyiActivity.class);
                                    context.startActivity(intent);
                                    dialog.dismiss();
                                } else if (state.equals("100400")) {
                                    //交易失败
                                    message = context.getString(R.string.transaction_failed);
                                    TiShiDialog tiShiDialog = new TiShiDialog(context);
                                    tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                                        @Override
                                        public void GuanBi() {
                                        }
                                    });
                                    tiShiDialog.ShowDialog(message);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onError(String e) {

                    }
                });
            }
        }
        if (enterBank == 1) {
            Intent intent = new Intent(context, FenQuBanks_Activity.class);
            intent.putExtra("selectAccount", 1);
            context.startActivity(intent);
        }
    }

    public interface OnItemClick {
        void onClick();
    }

    public OnItemClick onItemClick;

    public void setOnitemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void show() {
        sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);

        dialog = new MyDialog(context, R.style.MyRadiusDialog, R.layout.view_confirm_pay);
        dialog.show();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);

        close = dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        merchantName = dialog.findViewById(R.id.merchant_name);
        transactionFee = dialog.findViewById(R.id.transaction_fee);
        transaction = dialog.findViewById(R.id.transaction);
        instalment = dialog.findViewById(R.id.instalment);
        instalmentAmount = dialog.findViewById(R.id.instalment_amount);
        late = dialog.findViewById(R.id.late);
        lateFee = dialog.findViewById(R.id.late_fee);
        bank = dialog.findViewById(R.id.bank);
        bankNum = dialog.findViewById(R.id.bank_num);
        total = dialog.findViewById(R.id.total);
        totalAmount = dialog.findViewById(R.id.total_amount);
        title = dialog.findViewById(R.id.title);
        pay = dialog.findViewById(R.id.pay);

        String s = (String) sharePreferenceUtils.get(StaticParament.USER_FQ_ACCOUNT, "");
        bankNum.setText("****" + s.substring(s.length() - 4));
        String name = "";

        if (infoData != null) {
            instalmentAmount.setText("$" + PublicTools.twoend(infoData.getInstallmentAmount()));
            if (!TextUtils.isEmpty(infoData.getViolateAmount()) && Float.parseFloat(infoData.getViolateAmount()) != 0) {
                lateFee.setText("$" + PublicTools.twoend(infoData.getViolateAmount()));
            } else {
                late.setVisibility(View.GONE);
                lateFee.setVisibility(View.GONE);
            }
            for (int i = 0; i < infoData.getTradingNameList().size(); i++) {
                if (i == infoData.getTradingNameList().size() - 1) {
                    name += infoData.getTradingNameList().get(i);
                } else {
                    name += infoData.getTradingNameList().get(i) + ", ";
                }
            }
            merchantName.setText(name);
            transactionFee.setText("$" + PublicTools.twoend(infoData.getTransFee()));
            totalAmount.setText("$" + PublicTools.twoend(infoData.getTotalAmount()));
            pay.setText("Pay all $" + PublicTools.twoend(infoData.getTotalAmount()));
        }
        if (payAllData != null) {
            instalmentAmount.setText("$" + PublicTools.twoend(payAllData.getInstallmentAmount()));
            if (!TextUtils.isEmpty(payAllData.getViolateAmount()) && Float.parseFloat(payAllData.getViolateAmount()) != 0) {
                lateFee.setText("$" + PublicTools.twoend(payAllData.getViolateAmount()));
            } else {
                late.setVisibility(View.GONE);
                lateFee.setVisibility(View.GONE);
            }

            for (int i = 0; i < payAllData.getTradingNameList().size(); i++) {
                if (i == payAllData.getTradingNameList().size() - 1) {
                    name += payAllData.getTradingNameList().get(i);
                } else {
                    name += payAllData.getTradingNameList().get(i) + ", ";
                }
            }
            merchantName.setText(name);
            transactionFee.setText("$" + PublicTools.twoend(payAllData.getTransFee()));
            totalAmount.setText("$" + PublicTools.twoend(payAllData.getTotalAmount()));
            pay.setText("Pay all $" + PublicTools.twoend(payAllData.getTotalAmount()));
        }
        bankNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterBank = 1;
                enterPay = 0;
                showPayPwdDialog();
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterBank = 0;
                enterPay = 1;
                showPayPwdDialog();
            }
        });

        //切换字体
        TypefaceUtil.replaceFont(pay, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(merchantName, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(title, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(instalment, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(instalmentAmount, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(late, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(lateFee, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(transactionFee, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(transaction, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(bank, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(bankNum, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(total, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(totalAmount, "fonts/gilroy_bold.ttf");
    }
}
