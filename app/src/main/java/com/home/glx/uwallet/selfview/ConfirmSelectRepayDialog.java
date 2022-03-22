package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.FenQuBanks_Activity;
import com.home.glx.uwallet.activity.RepayKeyiActivity;
import com.home.glx.uwallet.activity.RepaySelectResultActivity;
import com.home.glx.uwallet.datas.RepaymentSuccessData;
import com.home.glx.uwallet.mvp.CheckPayPassword_in;
import com.home.glx.uwallet.mvp.CheckPayPassword_p;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmSelectRepayDialog implements CheckPayPassword_in.View {
    private MyDialog dialog;
    private Context context;
    private SharePreferenceUtils sharePreferenceUtils;
    private ImageView close;
    private TextView terms;
    private TextView termsNum;
    private TextView subtotal;
    private TextView subtotalAmount;
    private TextView totalAmount;
    private TextView transactionFee;
    private TextView transactionAmount;
    private TextView lateFee;
    private TextView lateFeeAmount;
    private TextView bank;
    private TextView bankNum;
    private TextView title;
    private TextView pay;
    private String chooseAmount;
    private String feeAmount;
    private int termsN;
    private UserListener userListener;
    private List<String> repayIds;
    private String totals;
    private CheckPayPassword_p checkPayPassword_p;
    private int enterBank = 0;
    private int enterPay = 0;

    public ConfirmSelectRepayDialog(Context context, String chooseAmount, String feeAmount, int termsN, List<String> repayIds) {
        this.context = context;
        this.chooseAmount = chooseAmount;
        this.feeAmount = feeAmount;
        this.termsN = termsN;
        this.repayIds = repayIds;
        userListener = new UserModel(context);
        checkPayPassword_p = new CheckPayPassword_p(context, this);
    }

    public void changeNum(String card) {
        bankNum.setText("****" + card.substring(card.length() - 4));
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

        dialog = new MyDialog(context, R.style.MyRadiusDialog, R.layout.view_confirm_select_pay);
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
        terms = dialog.findViewById(R.id.terms);
        termsNum = dialog.findViewById(R.id.term_num);
        subtotal = dialog.findViewById(R.id.subtotal);
        subtotalAmount = dialog.findViewById(R.id.subtotal_amount);
        transactionFee = dialog.findViewById(R.id.transaction_fee);
        transactionAmount = dialog.findViewById(R.id.transaction);
        lateFee = dialog.findViewById(R.id.late);
        lateFeeAmount = dialog.findViewById(R.id.late_fee);
        bank = dialog.findViewById(R.id.bank);
        bankNum = dialog.findViewById(R.id.bank_num);
        totalAmount = dialog.findViewById(R.id.total_amount);
        title = dialog.findViewById(R.id.title);
        pay = dialog.findViewById(R.id.pay);

        //切换字体
        TypefaceUtil.replaceFont(lateFee, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(lateFeeAmount, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(pay, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(totalAmount, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(title, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(terms, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(termsNum, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(transactionFee, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(transactionAmount, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(subtotal, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(subtotalAmount, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(bank, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(bankNum, "fonts/gilroy_medium.ttf");

        if (Float.parseFloat(feeAmount) == 0) {
            lateFee.setVisibility(View.GONE);
            lateFeeAmount.setVisibility(View.GONE);
        }
        String card = (String) sharePreferenceUtils.get(StaticParament.USER_FQ_ACCOUNT, "");
        bankNum.setText("****" + card.substring(card.length() - 4));
        bankNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterBank = 1;
                enterPay = 0;
                showPayPwdDialog();
            }
        });
        termsNum.setText(termsN + "");
        subtotalAmount.setText("$" + PublicTools.twoend(chooseAmount));
        transactionFee.setText("$0.22");
        lateFeeAmount.setText("$" + PublicTools.twoend(feeAmount));
        totals = CalcTool.add(chooseAmount, feeAmount);
        totals = CalcTool.add(totals, "0.22");
        totalAmount.setText("$" + PublicTools.twoend(totals));
        pay.setText("Pay $" + PublicTools.twoend(totals));
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterBank = 0;
                enterPay = 1;
                showPayPwdDialog();
            }
        });
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
            Map<String, Object> map = new HashMap<>();
            String repayTotals = CalcTool.sub(totals, "0.22");
            map.put("repayAmount", repayTotals);
            map.put("isPayAll", "false");
            map.put("repayIds", repayIds);
            userListener.repaymentV2(map, new ActionCallBack() {
                @Override
                public void onSuccess(Object... o) {
                    sharePreferenceUtils.put(StaticParament.REPAY_SUCCESS, "1");
                    sharePreferenceUtils.save();
                    String dataStr = (String) o[0];
                    Gson gson = new Gson();
                    final RepaymentSuccessData data = gson.fromJson(dataStr, RepaymentSuccessData.class);
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
                                    Intent intent = new Intent(context, RepaySelectResultActivity.class);
                                    intent.putExtra("data", (Serializable) data);
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
                                Intent intent = new Intent(context, RepaySelectResultActivity.class);
                                intent.putExtra("data", (Serializable) data);
                                intent.putExtra("result", "success");
                                context.startActivity(intent);
                            } else if (state.equals("100300")) {
                                //还款处理中
//                                Intent intent = new Intent(context, RepaySelectResultActivity.class);
//                                intent.putExtra("data", (Serializable)data);
//                                context.startActivity(intent);
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
        if (enterBank == 1) {
            Intent intent = new Intent(context, FenQuBanks_Activity.class);
            intent.putExtra("selectAccount", 1);
            context.startActivity(intent);
        }
    }
}
