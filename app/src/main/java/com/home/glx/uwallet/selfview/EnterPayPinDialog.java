package com.home.glx.uwallet.selfview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.SetNewPinActivity;
import com.home.glx.uwallet.mvp.CheckPayPassword_in;
import com.home.glx.uwallet.mvp.CheckPayPassword_p;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;
import java.util.Map;

public class EnterPayPinDialog implements CheckPayPassword_in.View {
    private Context context;
    private ImageView idClose;
    private TextView payText;
    private PwdEditText_Fenkai idPwsEdt;
    private String merchantName;
    private String money;
    private String date;
    private String haveFeeCard;
    private TextView pay;
    private TextView merchantNameTv;
    private TextView moneyTv;
    private CheckPayPassword_p checkPayPassword_p;
    private MyDialog dialog;
    private TextView forgetPin;
    public static boolean showTishgiDIalog = true;

    public EnterPayPinDialog(Context context, String merchantName, String money) {
        this.context = context;
        this.merchantName = merchantName;
        this.money = money;
    }

    public EnterPayPinDialog(Context context, String date, String money, String haveFeeCard) {
        this.context = context;
        this.haveFeeCard = haveFeeCard;
        this.date = date;
        this.money = money;
    }

    /**
     * 展示dialog
     */
    public void ShowDialog() {
        if (context == null) {
            return;
        }
        Window win = ((Activity) context).getWindow();//界面整体上移 当弹出软键盘时
        WindowManager.LayoutParams params = win.getAttributes();
        win.setSoftInputMode(params.SOFT_INPUT_ADJUST_NOTHING);

//        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        checkPayPassword_p = new CheckPayPassword_p(context, this);
        try {
            dialog = new MyDialog(context, R.style.fillDialog, R.layout.dialog_enter_pay_pin);
            dialog.show();
            PublicTools.setDialogSizeFill(context, dialog);
            dialog.setCancelable(true);

            payText = (TextView) dialog.findViewById(R.id.pay_text);
            pay = dialog.findViewById(R.id.pay);
            moneyTv = dialog.findViewById(R.id.money);
            merchantNameTv = dialog.findViewById(R.id.merchant_name);
            idPwsEdt = (PwdEditText_Fenkai) dialog.findViewById(R.id.id_pws_edt);
            idPwsEdt.setFocusable(true);//自动弹出数字软键盘
            idPwsEdt.setFocusableInTouchMode(true);
            idPwsEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
            idPwsEdt.postDelayed(new Runnable() {
                @Override
                public void run() {
                    idPwsEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInputFromInputMethod(idPwsEdt.getWindowToken(), 0);
                    imm.toggleSoftInputFromWindow(idPwsEdt.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);

                }
            }, 300);

            idClose = (ImageView) dialog.findViewById(R.id.back);
            forgetPin = dialog.findViewById(R.id.forget_pin);
            forgetPin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SetNewPinActivity.class);
                    context.startActivity(intent);
                }
            });
            TextView pinDigit = dialog.findViewById(R.id.pin_digit);

            //切换字体
            TypefaceUtil.replaceFont(forgetPin, "fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(payText, "fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(pinDigit, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(pay, "fonts/gilroy_bold.ttf");
            TypefaceUtil.replaceFont(moneyTv, "fonts/gilroy_bold.ttf");
            TypefaceUtil.replaceFont(merchantNameTv, "fonts/gilroy_bold.ttf");
            if (haveFeeCard != null) {
                if (haveFeeCard.equals("true")) {
                    moneyTv.setText(Html.fromHtml("<font color='#FD7441'>" + "$" + money + "</font>" + "<font color='#000000'>" + " late fee for" + "</font>"));
                    merchantNameTv.setText(date);
                } else {
                    moneyTv.setText(Html.fromHtml("<font color='#FD7441'>" + "$" + money + "</font>" + "<font color='#000000'>" + " late fee" + "</font>"));
                    merchantNameTv.setVisibility(View.GONE);
                }
            } else {
                moneyTv.setText("$" + money);
                if (!TextUtils.isEmpty(merchantName)) {
                    merchantNameTv.setText("at " + merchantName);
                } else if (!TextUtils.isEmpty(date)) {
                    merchantNameTv.setText("for " + date);
                } else {
                    //forgetPin.setVisibility(View.VISIBLE);
                }
            }
            payText.setText(Html.fromHtml("You would like to pay <font color='#FE7644'>" + money + "</font>" + " at " + "<font color='#FE7644'>" + merchantName + "</font>"));
            idClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTishgiDIalog = true;
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(idPwsEdt.getWindowToken(), 0);
                    dialog.dismiss();
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    onRepayPwd.back();
                }
            });

            idPwsEdt.setOnTextChangeListener(new PwdEditText_Fenkai.OnTextChangeListener() {
                @Override
                public void onTextChange(String pwd) {
                    if (pwd.length() == idPwsEdt.getTextLength()) {
                        if (!PublicTools.isFastClick()) {
                            return;
                        }
                        Map<String, Object> maps = new HashMap<>();
                        maps.put("payPassword", pwd);
                        showTishgiDIalog = false;
                        checkPayPassword_p.checkPayPassword(maps);
                    }
                }
            });
        } catch (Exception e) {

        }


    }

    @Override
    public void setPasswordStatus(String status) {
        if (status == null) {
            idPwsEdt.clearText();
            TiShiDialog tiShiDialog = new TiShiDialog(context);
            tiShiDialog.ShowDialog("Wrong Pin Number");
            tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                @Override
                public void GuanBi() {
                    idPwsEdt.setFocusable(true);//自动弹出数字软键盘
                    idPwsEdt.setFocusableInTouchMode(true);
                    idPwsEdt.requestFocus();
                    idPwsEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            idPwsEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.showSoftInputFromInputMethod(idPwsEdt.getWindowToken(), 0);
                            imm.toggleSoftInputFromWindow(idPwsEdt.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                            idPwsEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
                        }
                    }, 250);
                }
            });
        } else {
            if (onRepayPwd != null) {//输入密码正确
                showTishgiDIalog = true;
                onRepayPwd.inputFinish();
            }
            dialog.dismiss();
        }
    }

    public interface OnRepayPwd {
        void inputPwd(String password);

        void inputFinish();

        void back();
    }

    private OnRepayPwd onRepayPwd;

    public void setOnRepayPwd(OnRepayPwd onRepayPwd) {
        this.onRepayPwd = onRepayPwd;
    }
}
