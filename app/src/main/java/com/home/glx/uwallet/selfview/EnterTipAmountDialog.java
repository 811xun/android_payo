package com.home.glx.uwallet.selfview;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

@SuppressLint("ValidFragment")
public class EnterTipAmountDialog extends DialogFragment {
    private Context context;
    private TextView titleView;
    private TextView enters;
    private TextView amountStart;
    private EditText enterAmount;
    private ImageView close;
    private String firstAmount = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        View view = inflater.inflate(R.layout.dialog_enter_tip_amount, ((ViewGroup) window.findViewById(android.R.id.content)), false);
//让DialogFragment的背景为透明
        dialog.setCanceledOnTouchOutside(false); // 外部点击bu取消
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        window.setAttributes(lp);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.setLayout(-1, -2);//这2行,和上面的一样,注意顺序就行;

        show(view);
        return view;
    }


    public EnterTipAmountDialog(Context context, String amount) {
        this.context = context;
        firstAmount = amount;
    }

    public void show(View dialog) {
        titleView = dialog.findViewById(R.id.title);
        enters = dialog.findViewById(R.id.enters);
        amountStart = dialog.findViewById(R.id.amount_start);
        enterAmount = dialog.findViewById(R.id.enter_amount);
        enterAmount.setFocusableInTouchMode(true);
        enterAmount.setFocusable(true);
        enterAmount.requestFocus();
        //enterAmount.setCursorVisible(false);
        if (!TextUtils.isEmpty(firstAmount)) {
            enterAmount.setText(firstAmount);
            enterAmount.setSelection(firstAmount.length());
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        }, 350);
        close = dialog.findViewById(R.id.close);

        //切换字体
        TypefaceUtil.replaceFont(titleView, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(enters, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(amountStart, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(enterAmount, "fonts/gilroy_medium.ttf");
        enterAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.equals(".")) {
                    enterAmount.setText("");
                    return;
                }
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
//                        text = (String) s.toString().subSequence(0,
//                                s.toString().indexOf(".") + 3);
                        String[] split = s.toString().split(".");
                        text = s.toString().substring(0, s.toString().indexOf(".")) + s.toString().substring(s.toString().indexOf("."), s.toString().indexOf(".") + 3);
                        //text = String.valueOf(PublicTools.twoend(s.toString()));
                        enterAmount.setText(text);
                        enterAmount.setSelection(text.length());
                    }
                }
                if (text.equals(".") || text.equals("0..")/* || text.equals("0")*/) {
                    enterAmount.setText("0.");
                    enterAmount.setSelection(enterAmount.getText().length());
                    return;
                }
                if (text.equals("0.") || text.equals("0.0")) {
                    text = "0";
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClose != null) {
                    if (TextUtils.isEmpty(enterAmount.getText().toString()) || Float.parseFloat(enterAmount.getText().toString()) == 0) {
                        onClose.onClose("");
                    } else {
                        onClose.onClose(PublicTools.twoend(enterAmount.getText().toString()));
                    }
                    dismiss();
                }
            }
        });
    }

    public OnClose onClose;

    public interface OnClose {
        void onClose(String amount);
    }

    public void setOnClose(OnClose onClose) {
        this.onClose = onClose;
    }
}
