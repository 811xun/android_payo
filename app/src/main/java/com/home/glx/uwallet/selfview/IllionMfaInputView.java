package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.IllionMfaData;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IllionMfaInputView {

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

    private boolean isHeader = false;
    private boolean isInstructions = false;
    private boolean isPassword = false;
    private boolean isInput = false;
    private boolean isSelect = false;
    private boolean isSetMap = false;
    private String maxInput = "";
    private List<IllionMfaData.OptionsBean> listData = new ArrayList<>();
    private IllionMfaData data;
    private int width;
    private List<IllionMfaSetMapView> viewList = new ArrayList<>();

    public boolean isSetMap() {
        return isSetMap;
    }

    public void setSetMap(boolean setMap) {
        isSetMap = setMap;
    }

    public IllionMfaInputView(Context context, IllionMfaData data, int width) {
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
        if (data.getType().equalsIgnoreCase("set")) {
            setSetMap(true);
        }

        init();
    }

    private TextView header;
    private EditText nameEdit;
    private TextView nameSelect;
    private TextView instructions;
    private LinearLayout editLl;
    private LinearLayout idContentLay;
    private LinearLayout idPasswordLay;
    private LinearLayout selectLl;
    private int passwordNum = 0;
    private Map<String, Object> maps = new HashMap<>();


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
        selectLl = (LinearLayout) thisView.findViewById(R.id.select_ll);
        nameSelect = (TextView) thisView.findViewById(R.id.select);
        instructions = (TextView) thisView.findViewById(R.id.instructions);
        editLl = (LinearLayout) thisView.findViewById(R.id.edit_ll);
        idContentLay = (LinearLayout) thisView.findViewById(R.id.id_content_lay);
        idPasswordLay = (LinearLayout) thisView.findViewById(R.id.id_password_lay);

        TypefaceUtil.replaceFont(header, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(nameEdit, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(nameSelect, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(instructions, "fonts/gilroy_medium.ttf");

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

        if (isInput || isPassword || isSelect) {
            editLl.setVisibility(View.VISIBLE);
            if (isSelect) {
                selectLl.setVisibility(View.VISIBLE);
                this.listData = data.getOptions();
                selectLl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //点击选择
                        showDialog();
                    }
                });
            } else {
                nameEdit.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(maxInput) && maxInput.length() < 3) {
                    nameEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Integer.parseInt(maxInput))});
                }
                if (isPassword) {
                    nameEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        }

        if (isInstructions) {
            instructions.setVisibility(View.VISIBLE);
            instructions.setText(data.getValue());
        }

        if (isSetMap) {
            idContentLay.setVisibility(View.VISIBLE);
            List<IllionMfaData.SubElementsBean> subElementsBeanList = data.getSubElements();
            if (subElementsBeanList.size() > 0) {
                IllionMfaData.SubElementsBean subElementsBean = subElementsBeanList.get(0);
                IllionMfaData.SubElementsBean.HtmlAttrsBean htmlAttrsBean = subElementsBean.getHtmlAttrs();
                for (int i = 0; i < subElementsBeanList.size(); i++) {
                    if (!subElementsBeanList.get(i).getType().equals("password")) {
                        IllionMfaSetMapView illionInputView = new IllionMfaSetMapView(context, subElementsBeanList.get(i));
                        illionInputView.setOnChoice(new IllionMfaSetMapView.OnChoice() {
                            @Override
                            public void choiceItem(String code, String key) {
                                maps.put(key, code);
                            }
                        });
                        viewList.add(illionInputView);
                        idContentLay.addView(illionInputView.getView());
                    } else {
                        passwordNum++;
                    }
                }
            }
        }

        if (passwordNum > 0) {
            header.setVisibility(View.VISIBLE);
            header.setText(data.getLabel());
            idPasswordLay.setVisibility(View.VISIBLE);
            List<IllionMfaData.SubElementsBean> subElementsBeanList = data.getSubElements();
            for (int i = 0; i < subElementsBeanList.size(); i++) {
                int passWidth = (width - PublicTools.dip2px(context, 34) - (passwordNum - 1) * PublicTools.dip2px(context, 8)) / passwordNum;
                //int dps = PublicTools.px2dip(context, passWidth);
                IllionMfaSetMapView illionInputView = new IllionMfaSetMapView(passWidth, context, subElementsBeanList.get(i));
                viewList.add(illionInputView);
                idPasswordLay.addView(illionInputView.getView());
            }
        }
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

    public Map<String, Object> getSetValue() {
        if (isSetMap) {
            for (int i = 0; i < viewList.size(); i++) {
                IllionMfaSetMapView view = viewList.get(i);
                String[] keyValue = view.getValue().split(",");
                if (!TextUtils.isEmpty(keyValue[0]) && !TextUtils.isEmpty(keyValue[1])) {
                    maps.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return maps;
    }

    public String getValue() {
        if (editLl.getVisibility() == View.VISIBLE && nameEdit.getVisibility() == View.VISIBLE) {
            return data.getId() + "," + nameEdit.getText().toString().trim();
        } else {
            return "";
        }
    }

    public boolean getValueLimit() {
        if (isSetMap) {
            for (int i = 0; i < viewList.size(); i++) {
                IllionMfaSetMapView view = viewList.get(i);
                if (!view.getValueLimit()) {
                    return false;
                }
            }
        }
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

}
