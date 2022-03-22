package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TypefaceSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.IllionPreLoadData;
import com.home.glx.uwallet.tools.ChangeLineEdittext;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.List;

public class IllionInputView {

    private View thisView;
    private Context context;
    private LayoutInflater inflater;
    private String titleText = "";
    //hint提示
    private String hintText = "";
    //是否可为空
    private boolean isNull = true;
    //为空时提示
    private String nullPrompt = "";
    //输入的内容
    private String inputText = "";
    //底部提示信息
    private String bottomText = "";
    private int inputType;

    private boolean notInput = false;

    private String digits = "";
    private boolean isPassword = false;
    private boolean isCaptcha = false;
    private boolean isSelect = false;
    private String maxInput = "";
    private String src = "";
    private List<IllionPreLoadData.ValuesBean> listData = new ArrayList<>();
    private IllionPreLoadData data;

    public IllionInputView(Context context, IllionPreLoadData data) {
        this.context = context;
        this.data = data;
        this.titleText = data.getName();
        if (data.getType().equalsIgnoreCase("password")) {
            isPassword = true;
        }
        if (data.getType().equalsIgnoreCase("captcha")) {
            isCaptcha = true;
            this.src = data.getSrc();
        }
        if (data.getType().equalsIgnoreCase("select")) {
            isSelect = true;
        }
        IllionPreLoadData.ValidationBean validationBean = data.getValidation();
        this.maxInput = validationBean.getMaxLength();
        init();
    }

    //    private TextView nameTitle;
    private EditText nameEdit;
    private ImageView captchaImg;
    //    private TextView nameSelect;
    private LinearLayout selectLl;
    //    private LinearLayout editLl;
//    private LinearLayout passEye;
//    private ImageView passEyeIcon;
    private TextInputLayout til_id;
    private TextInputLayout til_id1;
    private ChangeLineEdittext number_edit1;

    private void init() {
        inflater = LayoutInflater.from(context);
        if (notInput) {
            //禁止输入
            thisView = inflater.inflate(R.layout.view_uninput, null);
        } else {
            thisView = inflater.inflate(R.layout.view_illion_input, null);
        }

        til_id = thisView.findViewById(R.id.til_id);
        nameEdit = (EditText) thisView.findViewById(R.id.number_edit);
        ((TextInputLayout) thisView.findViewById(R.id.til_id)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        captchaImg = (ImageView) thisView.findViewById(R.id.captcha_img);
        til_id1 = thisView.findViewById(R.id.til_id1);
        til_id1.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        number_edit1 = thisView.findViewById(R.id.number_edit1);
        selectLl = (LinearLayout) thisView.findViewById(R.id.select_ll);

        TypefaceUtil.replaceFont(nameEdit, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(number_edit1, "fonts/gilroy_medium.ttf");
        number_edit1.setInputType(InputType.TYPE_NULL);//来禁止手机软键盘
        setInitData();
        if (til_id.getVisibility() == View.VISIBLE) {

            //检查是否为空
            if (data.getOptional() != null && data.getOptional().equals("true")) {

            } else {
                nameEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String str = s.toString();
                        //检查最小输入
                        if (!data.getValidation().getMinLength().equals("0")) {
                            if (str.trim().length() >= Integer.parseInt(data.getValidation().getMinLength())) {
                                onInputTextChange.onTextChange();
                            }
                        } else {
                            onInputTextChange.onTextChange();
                        }
                    }
                });
            }
        }
    }

    private void setInitData() {
        if (isNull) {
            //可为空
            //idIsMust.setVisibility(View.GONE);
        } else {
            //idIsMust.setVisibility(View.VISIBLE);
        }

        if (titleText.equals("")) {
        } else {
            if (data.getOptional() != null && data.getOptional().equals("true")) {
                til_id.setHint(titleText + " (Optional)");
                til_id1.setHint(titleText + " (Optional)");
            } else {
                til_id.setHint(titleText);
                til_id1.setHint(titleText);

            }
        }

        if (!TextUtils.isEmpty(maxInput) && maxInput.length() < 3) {
            nameEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(maxInput))});
        }
        if (isPassword) {
//            passEye.setVisibility(View.VISIBLE);
            nameEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            TypefaceUtil.replaceFont(nameEdit, "fonts/gilroy_medium.ttf");

        }
        if (isCaptcha) {
            captchaImg.setVisibility(View.VISIBLE);
            byte[] decode = Base64.decode(src.split(",")[1], Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            captchaImg.setImageBitmap(bitmap);
        }
        if (isSelect) {
            til_id.setVisibility(View.GONE);
            captchaImg.setVisibility(View.GONE);
            selectLl.setVisibility(View.VISIBLE);
            this.listData = data.getValues();
            til_id1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    number_edit1.setFocusable(true);
                    number_edit1.setFocusableInTouchMode(true);
                    number_edit1.requestFocus();
                    //点击选择
                    showDialog();
                }
            });
            number_edit1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    number_edit1.setFocusable(true);
                    number_edit1.setFocusableInTouchMode(true);
                    number_edit1.requestFocus();
                    //点击选择
                    showDialog();
                }
            });
            selectLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    number_edit1.setFocusable(true);
                    number_edit1.setFocusableInTouchMode(true);
                    number_edit1.requestFocus();
                    //点击选择
                    showDialog();
                }
            });
        }
    }

    private void showDialog() {
        BottomIllionDialog bottomDialog = new BottomIllionDialog(context);
        bottomDialog.setOnChoiceItem(new BottomIllionDialog.ChoiceItem() {
            @Override
            public void itemClick(String choiceText, String code) {
                number_edit1.setText(choiceText);
                //选择返回
                onInputTextChange.onTextChange();
                inputText = choiceText;
                String choiceCode = code;
                String key = data.getFieldID();

                if (onChoice != null) {
                    onChoice.choiceItem(choiceCode, key);
                }
            }
        });
        bottomDialog.show(listData);
    }

    public interface OnInputTextChange {
        void onTextChange();
    }

    private OnInputTextChange onInputTextChange;

    public void setOnInputTextChange(OnInputTextChange onInputTextChange) {
        this.onInputTextChange = onInputTextChange;
    }

    public String getValue() {
        return data.getFieldID() + "," + nameEdit.getText().toString().trim();

    }


    public boolean getValueLimit(boolean toast) {
        if (til_id.getVisibility() == View.VISIBLE) {
            //检查是否为空
            if (data.getOptional() != null && data.getOptional().equals("true")) {

            } else {
                if (TextUtils.isEmpty(nameEdit.getText().toString().trim())) {
                    if (toast) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//大于等于9.0
                            Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/gilroy_medium.ttf");
                            SpannableString efr = new SpannableString("Please enter " + data.getName());
                            efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Toast.makeText(context, efr, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Please enter " + data.getName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    return false;
                }
            }
            //检查最小输入
            if (!data.getValidation().getMinLength().equals("0")) {
                if (nameEdit.getText().toString().trim().length() < Integer.parseInt(data.getValidation().getMinLength())) {
                    if (toast) {
                        Toast.makeText(context, String.format("Characters' number for %s can not be less than %s", data.getName(), data.getValidation().getMinLength()), Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            }
        } else {
            if (data.getOptional() != null && data.getOptional().equals("true")) {

            } else {
                if (TextUtils.isEmpty(number_edit1.getText().toString().trim())) {
                    if (toast) {
                        Toast.makeText(context, "Please select " + data.getName(), Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public interface OnClick {
        void clickItem();
    }

    private OnClick onClick;

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public interface OnChoice {
        void choiceItem(String code, String key);
    }

    private OnChoice onChoice;

    public void setOnChoice(OnChoice onChoice) {
        this.onChoice = onChoice;
    }

    public View getView() {
        return thisView;
    }


/*    public String getText() {
        if (!isNull && idInputEdt.getText().toString().trim().equals("")) {
            Toast.makeText(context, nullPrompt, Toast.LENGTH_SHORT).show();
            if (idEdit != null) {
                idEdit.setBackgroundResource(R.drawable.view_yj_m8chengbai);
            }
            return "";
        }
        if (idEdit != null) {
            idEdit.setBackgroundResource(R.drawable.view_yj_m8hui);
        }
        return idInputEdt.getText().toString();
    }*/
}
