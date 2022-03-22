package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

public class RepayPawDialog {

    private Context context;
    private MyDialog dialog;

    private ImageView idClose;
    private PwdEditText_Fenkai idPwsEdt;

    public RepayPawDialog(Context context) {
        this.context = context;
    }

    public void show() {
        dialog = new MyDialog(context, R.style.fillDialog, R.layout.dialog_repay_paw);
        dialog.show();
        PublicTools.setDialogSizeFill(context, dialog);
        dialog.setCancelable(false);
        /*Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);*/

        TextView pinDigit = dialog.findViewById(R.id.pin_digit);
        idClose = (ImageView) dialog.findViewById(R.id.id_close);
        idClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        idPwsEdt = (PwdEditText_Fenkai) dialog.findViewById(R.id.id_pws_edt);

        idPwsEdt.setFocusable(true);//自动弹出数字软键盘
        idPwsEdt.setFocusableInTouchMode(true);
        idPwsEdt.requestFocus();
        idPwsEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
        idPwsEdt.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(idPwsEdt.getWindowToken(), 0);
                imm.toggleSoftInputFromWindow(idPwsEdt.getWindowToken(), 0, InputMethodManager.HIDE_IMPLICIT_ONLY);
                idPwsEdt.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
        }, 100);
        //切换字体
        TypefaceUtil.replaceFont(pinDigit, "fonts/gilroy_medium.ttf");
  /*      new Handler().postDelayed(new Runnable(){
            public void run() {
                idPwsEdt.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(idPwsEdt, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 10);   //0.1秒*/
        idPwsEdt.setOnTextChangeListener(new PwdEditText_Fenkai.OnTextChangeListener() {
            @Override
            public void onTextChange(String pwd) {
                if (pwd.length() == idPwsEdt.getTextLength()) {
                    if (onRepayPwd != null) {
                        onRepayPwd.inputPwd(pwd);
                    }
                    dialog.dismiss();
                }
            }
        });
    }

    public interface OnRepayPwd {
        void inputPwd(String password);
    }

    private OnRepayPwd onRepayPwd;

    public void setOnRepayPwd(OnRepayPwd onRepayPwd) {
        this.onRepayPwd = onRepayPwd;
    }

}
