package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.ConfirmPayActivity;
import com.home.glx.uwallet.activity.ConfirmPayNextActivity;
import com.home.glx.uwallet.activity.PayMoneyActivity;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.MerchantListener;
import com.home.glx.uwallet.mvp.model.MerchantModel;
import com.home.glx.uwallet.tools.CountDownUtils;
import com.home.glx.uwallet.tools.PublicTools;

import java.util.HashMap;
import java.util.Map;

public class PaymentWaitingDialog {
    private Context context;
    private MyDialog dialog;
    private CountDownUtils countDownUtils;
    private ImageView procession;
    private TextView title;
    private static Handler handler;
    private static Runnable runnable;
    private MerchantListener merchantListener;
    private String transNo;
    private ConfirmPayBottomDialog confirmPayBottomDialog;
    private String isPos = "false";
    public static boolean exePayResult = true;

    public PaymentWaitingDialog(ConfirmPayBottomDialog confirmPayBottomDialog, Context context, String transNo, String isPos) {
        this.context = context;
        this.transNo = transNo;
        this.confirmPayBottomDialog = confirmPayBottomDialog;
        this.isPos = isPos;
    }

    public PaymentWaitingDialog(Context context, String transNo, String isPos) {
        this.context = context;
        this.transNo = transNo;
        this.isPos = isPos;
    }

    public void show() {
        try {
            dialog = new MyDialog(context, R.style.TDialog, R.layout.dialog_payment_waiting);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
            dialog.show();
            PublicTools.setDialogSize(context, dialog);
            dialog.setCancelable(false);
            merchantListener = new MerchantModel(context);
            procession = (ImageView) dialog.findViewById(R.id.procession);
            title = (TextView) dialog.findViewById(R.id.title);
            Animation rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_anim);
            LinearInterpolator lin = new LinearInterpolator();
            rotateAnimation.setInterpolator(lin);
            procession.startAnimation(rotateAnimation);

            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    exePayResult = false;
                    //????????????????????????300ms????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    //30s????????????300ms??????????????????????????????
                    Log.i("wuhao", "????????????????????????");
                    //????????????????????????
                    Map<String, Object> map = new HashMap<>();
                    merchantListener.getRecordDetailV2(map, transNo, new ActionCallBack() {
                        @Override
                        public void onSuccess(Object... o) {
                            String dataStr = (String) o[0];
//                            ((ConfirmPayNextActivity) context).showNext(dataStr);
                            ((ConfirmPayActivity) context).showNext(dataStr);
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(String e) {
                            dialog.dismiss();
                        }
                    });
                    /*merchantListener.getRecordDetail(map, transNo, new ActionCallBack() {
                        @Override
                        public void onSuccess(Object... o) {
                            TransationNoData transationDetailData = (TransationNoData)o[0];
                            String result = transationDetailData.getTransStateStr();
                            if (result.equals("Successful")) {
                                String id = transationDetailData.getId();
                                Intent intent = new Intent(context, PaymentResultActivity.class);
                                intent.putExtra("payResult", "success");
                                intent.putExtra("isPos", isPos);
                                intent.putExtra("id", id);
                                context.startActivity(intent);
                            } else if (result.equals("Failed")){
                                String str = transationDetailData.getFailedMsg();
                                if (confirmPayBottomDialog != null) {
                                    confirmPayBottomDialog.showError(str);
                                }
                            } else {
                                String str = transationDetailData.getFailedMsg();
                                if (confirmPayBottomDialog != null) {
                                    confirmPayBottomDialog.showNoResult(str);
                                }
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(String e) {
                            dialog.dismiss();
                        }
                    });*/
               /* Intent intent = new Intent(PaymentWaitingAcivity.this, PaymentResultActivity.class);
                intent.putExtra("payResult", "noResult");
                startActivity(intent);*/
                }
            };
            handler.postDelayed(runnable, 30000);
//            handler.postDelayed(runnable, 8000);

            //?????????
            if (context instanceof ConfirmPayNextActivity) {
                countDownUtils = new CountDownUtils((ConfirmPayNextActivity) context, title, "", 30, "Please wait for ", " seconds...");
            } else {
                countDownUtils = new CountDownUtils((PayMoneyActivity) context, title, "", 30, "Please wait for ", " seconds...");
            }
            //???????????????
            countDownUtils.start();
            countDownUtils.setOnTimeStop(new CountDownUtils.OnTimeStop() {
                @Override
                public void time_stop() {
                    title.setVisibility(View.GONE);
                    //handler.postDelayed(runnable,0);
                }
            });
        } catch (Exception e) {

        }
    }

    public void setResult() {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
        if (countDownUtils != null) {
            countDownUtils.stopTimer();
        }
        dialog.dismiss();
    }

    public void setResult(String title, String str) {
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
        countDownUtils.stopTimer();
        /*if (title.equals("PayFail")) {
            Log.i("wuhao", "??????????????????????????????");
            if (handler != null && runnable != null) {
                handler.removeCallbacks(runnable);
            }
            countDownUtils.stopTimer();
        }
        if (title.equals("PayNoResult")) {
            Log.i("wuhao", "??????????????????????????????");
            if (handler != null && runnable != null) {
                handler.removeCallbacks(runnable);
            }
            countDownUtils.stopTimer();
        }*/
/*
        if (title.equals("PaySuccess")) {
            */
/*//*
/Log.i("wuhao", "??????????????????????????????");
            if (handler != null && runnable != null) {
                handler.removeCallbacks(runnable);
            }*//*

            countDownUtils.stopTimer();
            Intent intent = new Intent(context, PaymentResultActivity.class);
            intent.putExtra("payResult", "success");
            intent.putExtra("id", str);
            intent.putExtra("isPos", isPos);
            context.startActivity(intent);
        }
*/
        dialog.dismiss();
    }
}
