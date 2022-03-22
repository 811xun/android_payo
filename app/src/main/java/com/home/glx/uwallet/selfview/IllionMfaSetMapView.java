package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.IllionMfaData;

import java.util.ArrayList;
import java.util.List;

public class IllionMfaSetMapView {
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
    private boolean isHeader = false;
    private boolean isInstructions = false;
    private boolean isPassword = false;
    private boolean isInput = false;
    private boolean isSelect = false;
    private boolean isSetMap = false;
    private String maxInput = "";
    private List<IllionMfaData.OptionsBean> listData = new ArrayList<>();
    private IllionMfaData.SubElementsBean data;
    private int width;

    public IllionMfaSetMapView(Context context, IllionMfaData.SubElementsBean data) {
        this.context = context;
        this.data = data;

        if (data.getType().equalsIgnoreCase("header")) {
            isHeader = true;
        }
        if (data.getType().equalsIgnoreCase("instructions")) {
            isInstructions = true;
        }
        if (data.getType().equalsIgnoreCase("input")) {
            isInput = true;
        }
        if (data.getType().equalsIgnoreCase("password")) {
            isPassword = true;
        }
        if (data.getType().equalsIgnoreCase("options")) {
            isSelect = true;
        }

        init();
    }

    public IllionMfaSetMapView(int width, Context context, IllionMfaData.SubElementsBean data) {
        this.context = context;
        this.data = data;
        this.width = width;

        if (data.getType().equalsIgnoreCase("header")) {
            isHeader = true;
        }
        if (data.getType().equalsIgnoreCase("instructions")) {
            isInstructions = true;
        }
        if (data.getType().equalsIgnoreCase("input")) {
            isInput = true;
        }
        if (data.getType().equalsIgnoreCase("password")) {
            isPassword = true;
        }
        if (data.getType().equalsIgnoreCase("options")) {
            isSelect = true;
        }

        init();
    }

    private TextView header;
    private EditText nameEdit;
    private TextView nameSelect;
    private TextView instructions;
    private LinearLayout editLl;
    private LinearLayout idContentLay;
    private TextView passTxt;
    private EditText passEdit;


    private void init() {
        inflater = LayoutInflater.from(context);
        if (notInput) {
            //禁止输入
            thisView = inflater.inflate(R.layout.view_uninput, null);
        } else {
            thisView = inflater.inflate(R.layout.view_illion_mfa_input, null);
        }

        header = (TextView) thisView.findViewById(R.id.header);
        nameEdit = (EditText) thisView.findViewById(R.id.number_edit);
        nameSelect = (TextView) thisView.findViewById(R.id.select);
        instructions = (TextView) thisView.findViewById(R.id.instructions);
        editLl = (LinearLayout) thisView.findViewById(R.id.edit_ll);
        idContentLay = (LinearLayout) thisView.findViewById(R.id.id_content_lay);
        passTxt = (TextView) thisView.findViewById(R.id.pass_txt);
        passEdit = (EditText) thisView.findViewById(R.id.pass_edit);

        setInitData();
    }

    private void setInitData() {
        if (isNull) {
            //可为空
            //idIsMust.setVisibility(View.GONE);
        } else {
            //idIsMust.setVisibility(View.VISIBLE);
        }

        if (isHeader) {
            if (data.getValue() != null) {
                header.setVisibility(View.VISIBLE);
                header.setText(data.getValue());
            }
        }

        if (isInput /*|| isPassword*/ || isSelect) {
            editLl.setVisibility(View.VISIBLE);
            if (isSelect) {
                nameSelect.setVisibility(View.VISIBLE);
                this.listData = data.getOptions();
                nameSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击选择
                        showDialog();
                    }
                });
            } else {
                nameEdit.setVisibility(View.VISIBLE);
                if (maxInput.length() < 3) {
                    nameEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(maxInput))});
                }
                /*if (isPassword) {
                    nameEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }*/
            }
        }

        if (isPassword) {
            if (data.getActive().equals("false")) {
                passTxt.setVisibility(View.VISIBLE);
                // 屏幕宽度（像素）
                LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) passTxt.getLayoutParams();
                lp.height = width;
                lp.width = width;
                passTxt.setLayoutParams(lp);
            } else {
                String maxInputs = data.getHtmlAttrs().getMaxlength();
                passEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(maxInputs))});
                passEdit.setVisibility(View.VISIBLE);
                // 屏幕宽度（像素）
                LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) passEdit.getLayoutParams();
                lp.height = width;
                lp.width = width;
                passEdit.setLayoutParams(lp);
            }
        }

        if (isInstructions) {
            instructions.setVisibility(View.VISIBLE);
            instructions.setText(data.getValue());
        }

    }

    public String getValue() {
            if (editLl.getVisibility() == View.VISIBLE && nameEdit.getVisibility() == View.VISIBLE) {
                return data.getId() + "," + nameEdit.getText().toString().trim();
            } else  if (passEdit.getVisibility() == View.VISIBLE) {
                return data.getId() + "," + passEdit.getText().toString().trim();
            } else {
                return "";
            }

    }


    public boolean getValueLimit() {
        if (isPassword) {
            if (passEdit.getVisibility() == View.VISIBLE) {
                if (TextUtils.isEmpty(passEdit.getText().toString().trim())) {
                    Toast.makeText(context, "Incomplete information", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } else {
            if (editLl.getVisibility() == View.VISIBLE) {
                if (isSelect) {
                    if (TextUtils.isEmpty(nameSelect.getText().toString().trim())) {
                        Toast.makeText(context, "Incomplete information", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    //检查是否为空
                    if (nameEdit.getVisibility() == View.VISIBLE) {
                        if (TextUtils.isEmpty(nameEdit.getText().toString().trim())) {
                            Toast.makeText(context, "Incomplete information", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }


    private void showDialog() {
        BottomIllionMfaDialog bottomDialog = new BottomIllionMfaDialog(context);
        bottomDialog.setOnChoiceItem(new BottomIllionMfaDialog.ChoiceItem() {
            @Override
            public void itemClick(String choiceText, String code) {
                //选择返回
                nameSelect.setText(choiceText);
                inputText = choiceText;
                String choiceCode = code;
                String key = data.getId();

                if (onChoice != null) {
                    onChoice.choiceItem(choiceCode, key);
                }
            }
        });
        bottomDialog.show(listData);
    }

    /*public boolean getValueLimit() {
        if (editLl.getVisibility() == View.VISIBLE) {
            //检查是否为空
            if (data.getOptional() != null && data.getOptional().equals("true")) {

            } else {
                if (TextUtils.isEmpty(nameEdit.getText().toString().trim())) {
                    Toast.makeText(context, "Please enter " +  data.getName(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            //检查最小输入
            if (!data.getValidation().getMinLength().equals("0")) {
                if (nameEdit.getText().toString().trim().length() < Integer.parseInt(data.getValidation().getMinLength())) {
                    Toast.makeText(context, String.format("Characters' number for %s can not be less than %s", data.getName(), data.getValidation().getMinLength()), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } else {
            if (data.getOptional() != null && data.getOptional().equals("true")) {

            } else {
                if (TextUtils.isEmpty(nameSelect.getText().toString().trim())) {
                    Toast.makeText(context, "Please select " + data.getName(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }*/

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

}
