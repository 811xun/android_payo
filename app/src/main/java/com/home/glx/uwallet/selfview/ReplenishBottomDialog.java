package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.FenQuBanks_Activity;
import com.home.glx.uwallet.activity.PdfActivity;
import com.home.glx.uwallet.adapter.DialogBanksAdapter;
import com.home.glx.uwallet.datas.BankDatas;
import com.home.glx.uwallet.datas.ChoiceBanksData;
import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.datas.FenQiMsgDatas;
import com.home.glx.uwallet.datas.GetChoiceDatas;
import com.home.glx.uwallet.datas.UserAllMsgDatas;
import com.home.glx.uwallet.mvp.ChangeCardMessage_in;
import com.home.glx.uwallet.mvp.ChangeCardMessage_p;
import com.home.glx.uwallet.mvp.GetChoiceList_in;
import com.home.glx.uwallet.mvp.GetChoiceList_p;
import com.home.glx.uwallet.mvp.GetUserAllMsg_in;
import com.home.glx.uwallet.mvp.GetUserAllMsg_p;
import com.home.glx.uwallet.mvp.JiHuoFenQi_in;
import com.home.glx.uwallet.mvp.JiHuoFenQi_p;
import com.home.glx.uwallet.mvp.LoadChoiceBanks;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReplenishBottomDialog implements JiHuoFenQi_in.View,
        GetUserAllMsg_in.View, ChangeCardMessage_in.View,
        GetChoiceList_in.View{
    private MyDialog dialog;
    private BankDatas bankDatas;
    private Context context;
    private JiHuoFenQi_p presentJiHuo;
    private GetUserAllMsg_p present;
    private ChangeCardMessage_p presentChange;
    private EditText idAccountName;
    private LayoutInflater inflater;
    private View thisView;
    private TextView idProtocolText;
    private TextView idChoiceAccountBankName;
    private EditText idAccountBankName;
    private EditText idAccountBsb;
    private EditText idAccountAccount;
    private EditText idAccountEmail;
    private Button confirm;
    private TextView splitHtml;
    private CheckBox idProtocol;
    private ChoiceBankDialog choiceBankDialog;
    private String accountName;
    private String cardId;
    private boolean needInsert;
    private GetChoiceList_p choiceList_p;

    private List<ChoiceDatas> htmlList = new ArrayList<>();

    public ReplenishBottomDialog(Context context, BankDatas choiceBanks, boolean needInsert) {
        this.context = context;
        this.bankDatas = choiceBanks;
        this.needInsert = needInsert;
        initView();
    }

    public ReplenishBottomDialog(Context context) {
        this.context = context;
        initView();
    }

    private void initView() {
        presentJiHuo = new JiHuoFenQi_p(context, this);
        present = new GetUserAllMsg_p(context, this);
        inflater = LayoutInflater.from(context);
        thisView = inflater.inflate(R.layout.dialog_bottom_replenish, null);
        presentChange = new ChangeCardMessage_p(context, this);
        choiceList_p = new GetChoiceList_p(context, this);
        Map<String, Object> maps = new HashMap<>();
        String[] keys = new String[]{"directDebitTerms", "payLaterTermsConditions"};
        maps.put("code", keys);
        choiceList_p.loadChoiceList(maps);
    }

    public void show() {

        dialog = new MyDialog(context, R.style.MyDialog, R.layout.dialog_bottom_replenish);
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);

        idAccountName = (EditText) dialog.findViewById(R.id.id_account_name);
        idProtocolText = (TextView) dialog.findViewById(R.id.id_protocol_text);
        //选择银行名
        idChoiceAccountBankName = (TextView) dialog.findViewById(R.id.id_choice_account_bank_name);
        idAccountBankName = (EditText) dialog.findViewById(R.id.id_account_bank_name);
        idAccountBsb = (EditText) dialog.findViewById(R.id.id_account_bsb);
        idAccountAccount = (EditText) dialog.findViewById(R.id.id_account_account);
        idAccountEmail = (EditText) dialog.findViewById(R.id.id_account_email);
        confirm = (Button) dialog.findViewById(R.id.id_add_bank);
        idProtocol = (CheckBox) dialog.findViewById(R.id.id_protocol);
        splitHtml = (TextView) dialog.findViewById(R.id.split_html);
        idChoiceAccountBankName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选择银行名称
                choiceBanksDialog();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check()) {
                    return;
                } else {
                    if (bankDatas == null) {
                        Map<String, Object> maps = new HashMap<String, Object>();
                        maps.put("cardId", cardId);
                        maps.put("type", "0");
                        maps.put("cardNo", idAccountAccount.getText().toString().replace(" ", ""));
                        maps.put("bsb", idAccountBsb.getText().toString().replace(" ", ""));
                        maps.put("name", idAccountName.getText().toString());
                        if (accountName != null) {
                            maps.put("accountName", accountName);
                        }
                        maps.put("bankName", idAccountBankName.getText().toString());
                        maps.put("email", idAccountEmail.getText().toString());
                        presentChange.reqChangeCardMessage(maps);
                    } else {
                        if (needInsert) {
                            dialog.dismiss();
                            Map<String, Object> maps = new HashMap<>();
                            //        1、绑卡 0、绑账户
                            maps.put("type", "0");
                            //银行卡号
                            maps.put("cardNo", idAccountAccount.getText().toString().replace(" ", ""));
                            maps.put("bankName", idAccountBankName.getText().toString());
                            maps.put("bsb", idAccountBsb.getText().toString().replace(" ", ""));
                            maps.put("name", idAccountName.getText().toString());
                            maps.put("email", idAccountEmail.getText().toString());
                            ((FenQuBanks_Activity) context).addBankAccount(maps);
                        } else {
                            Map<String, Object> maps = new HashMap<String, Object>();
                            maps.put("cardId", bankDatas.getId());
                            maps.put("type", "0");
                            maps.put("cardNo", idAccountAccount.getText().toString().replace(" ", ""));
                            maps.put("bsb", idAccountBsb.getText().toString().replace(" ", ""));
                            maps.put("name", idAccountName.getText().toString());
                            if (accountName != null) {
                                maps.put("accountName", accountName);
                            }
                            maps.put("bankName", idAccountBankName.getText().toString());
                            maps.put("email", idAccountEmail.getText().toString());
                            presentChange.reqChangeCardMessage(maps);
                        }
                    }
                }
            }
        });

        presentJiHuo.loadFenQiMsg(new HashMap<String, Object>());

        //获取用户信息
        present.loadUserAllMsg(new HashMap<String, Object>());
    }

    private boolean check() {
        if (idAccountBankName.getText().toString().length() == 0) {
            //请输入银行名
            Toast.makeText(context, R.string.bank_name_input_error, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (idAccountName.getText().toString().length() == 0) {
            //请输入账户名
            Toast.makeText(context, R.string.input_bank_account_name, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (idAccountName.getText().toString().length() < 1 || idAccountName.getText().toString().length() > 80) {
            //请输入账户名
            Toast.makeText(context, "Invalid account holder", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (idAccountBsb.getText().toString().length() == 0) {
            //请输入BSB
            Toast.makeText(context, R.string.bank_bsb_input_error, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (idAccountBsb.getText().toString().length() < 6) {
            //请输入BSB
            Toast.makeText(context, "Invalid BSB", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (idAccountAccount.getText().toString().length() == 0) {
            //请输入银行账号
            Toast.makeText(context, R.string.input_bank_account, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (idAccountAccount.getText().toString().length() < 6 || idAccountAccount.getText().toString().length() > 9) {
            //请输入银行账号
            Toast.makeText(context, "6-9 digit bank account number", Toast.LENGTH_SHORT).show();
            return false;
        }
        /*if (idAccountName.getText().toString().length() < 4) {
            //请输入账户名
            Toast.makeText(context, R.string.input_bank_account_name, Toast.LENGTH_SHORT).show();
            return false;
        }*/
        if (idAccountEmail.getText().toString().length() < 1 || !PublicTools.isEmail(idAccountEmail.getText().toString().trim())) {
            //没有邮箱
            Toast.makeText(context, R.string.bank_email_input_error, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!idProtocol.isChecked()) {
            Toast.makeText(context, R.string.please_agree_agreement, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 选择银行名
     */
    private void choiceBanksDialog() {
        LoadChoiceBanks loadChoiceBanks = new LoadChoiceBanks(context);
        loadChoiceBanks.setOnFindList(new LoadChoiceBanks.OnBanks() {
            @Override
            public void allBanks(List<ChoiceBanksData> bankList) {

                List<BankDatas> bank_list = new ArrayList<>();
                for (int i = 0; i < bankList.size(); i++) {
                    BankDatas bankDatas = new BankDatas();
                    bankDatas.setSmallLogo(bankList.get(i).getSmallLogo());
                    bankDatas.setBankName(bankList.get(i).getBankName());
                    bankDatas.setCardNo("");
                    bank_list.add(bankDatas);
                }

                BankDatas bankDatas = new BankDatas();
                bankDatas.setBankName("Other");
                bankDatas.setCardNo("");
                bank_list.add(bankDatas);

                choiceBankDialog = new ChoiceBankDialog(context,
                        new DialogBanksAdapter.OnItemClick() {
                            @Override
                            public void bankClick(BankDatas bankDatas) {
                                L.log("选择的银行卡：" + bankDatas.getBankName());
                                idChoiceAccountBankName.setText(bankDatas.getBankName());
                                if (bankDatas.getBankName().equals("Other")) {
                                    idAccountBankName.setVisibility(View.VISIBLE);
                                    idAccountBankName.setText("");
                                } else {
                                    idAccountBankName.setVisibility(View.GONE);
                                    idAccountBankName.setText(bankDatas.getBankName());
                                }
                                choiceBankDialog.dismiss();
                            }

                            @Override
                            public void addBank() {

                            }
                        });
                choiceBankDialog.setItemStyle(StaticParament.GONE_ADD_CHECK);
                choiceBankDialog.setLookType(1);
                choiceBankDialog.show(context.getString(R.string.select_bank_name), bank_list, "1");
            }
        });
        loadChoiceBanks.getChoiceBanks(new HashMap<String, Object>());
    }

    @Override
    public void setFenQiStatus(String status) {

    }

    @Override
    public void setFenQiMsg(FenQiMsgDatas fenQiMsg) {
        if (fenQiMsg.getAccountId() != null) {
            cardId = fenQiMsg.getAccountId();
        }
        if (fenQiMsg.getBankcardHolderName() != null) {
            accountName = fenQiMsg.getBankcardHolderName();
        }
        if (bankDatas == null) {
            if (!TextUtils.isEmpty(fenQiMsg.getBankcardHolderName())) {
                //if (!fenQiMsg.getBankcardHolderName().contains("null") && !fenQiMsg.getBankcardHolderName().contains("Null")) {
                idAccountName.setText(fenQiMsg.getBankcardHolderName());
                //}
            }
            idAccountBankName.setText(fenQiMsg.getBankcardBank());
            idChoiceAccountBankName.setText(fenQiMsg.getBankcardBank());
            idAccountBsb.setText(fenQiMsg.getBankcardCode().replace(" ", ""));
            idAccountAccount.setText(fenQiMsg.getBankcardNumber().replace(" ", ""));
        } else {
            if (!TextUtils.isEmpty(bankDatas.getName())) {
                //if (!bankDatas.getName().contains("null") && !bankDatas.getName().contains("Null")) {
                idAccountName.setText(bankDatas.getName());
            }
            idAccountBankName.setText(bankDatas.getBankName());
            idChoiceAccountBankName.setText(bankDatas.getBankName());
            idAccountBsb.setText(bankDatas.getBsb().replace(" ", ""));
            idAccountAccount.setText(bankDatas.getCardNo().replace(" ", ""));
        }
    }

    @Override
    public void setFenQiLimit(String limit) {

    }

    @Override
    public void setNoLoginFenQiLimit(String limit) {

    }

    @Override
    public void setUserMsg(UserAllMsgDatas userMsg) {
        if (bankDatas == null) {
            idAccountEmail.setText(userMsg.getEmail());
        } else {
            if (!TextUtils.isEmpty(bankDatas.getEmail())) {
                idAccountEmail.setText(bankDatas.getEmail());
            } else {
                idAccountEmail.setText(userMsg.getEmail());
            }
        }

        String firstName = "";
        String middleName = "";
        String lastName = "";
        if (userMsg.getUserFirstName() != null) {
            firstName = userMsg.getUserFirstName();
        }
        if (userMsg.getUserMiddleName() != null) {
            middleName = userMsg.getUserMiddleName();
        }
        if (userMsg.getUserLastName() != null){
            lastName = userMsg.getUserLastName();
        }
        if (TextUtils.isEmpty(idAccountName.getText().toString().trim())) {
            idAccountName.setText(firstName + middleName + lastName);
        }
    }

    @Override
    public void setChangeCardMessage(String dataStr) {
        dialog.dismiss();
        if (dataStr == null) {
            //((FenQuBanks_Activity) context).showSplitError();
        } else {
            ((FenQuBanks_Activity) context).showSplitSuccess();
        }
    }

    @Override
    public void setChoiceList(GetChoiceDatas getChoiceDatas) {
        if (getChoiceDatas.getPayLaterTermsConditions() != null) {
            htmlList.addAll(getChoiceDatas.getPayLaterTermsConditions());
            addressNewStyle(idProtocolText);
            splitHtml.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PdfActivity.class);
                    // String url = "https://docs.google.com/viewer?url=";
                    intent.putExtra("url", htmlList.get(0).getCName());
                    context.startActivity(intent);
                }
            });
        }
    }

    /**
     * 协议样式修改
     *
     * @param textView
     */
    private void addressNewStyle(TextView textView) {
        textView.setHighlightColor(context.getResources().getColor(android.R.color.transparent));
        SpannableString spannableString = new SpannableString("I agree to the Buy Now Pay Later Terms and Conditions and authorise Payo Funds Pty Ltd ACN 638 179 567 and its associated party, to debit the nominated bank account outlined above.");
//        if (SystemUtils.getSysLangusge(this).equals("zh")) {
//            //中文
//            //隐私政策
//            MyClickableSpan clickableSpan2 = new MyClickableSpan();
//            spannableString.setSpan(clickableSpan2, 7, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
//            spannableString.setSpan(colorSpan2, 7, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        } else {
        //英文
        //隐私政策
        MyClickPPableSpan clickableSpan1 = new MyClickPPableSpan();

        spannableString.setSpan(clickableSpan1, 15, "I agree to the Buy Now Pay Later Terms and Conditions".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(context.getResources().getColor(R.color.text_orange));
        spannableString.setSpan(colorSpan1, 15, "I agree to the Buy Now Pay Later Terms and Conditions".length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        //设置下划线
        spannableString.setSpan(new UnderlineSpan(), 15, "I agree to the Buy Now Pay Later Terms and Conditions".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString);
    }

    class MyClickPPableSpan extends ClickableSpan {

        public MyClickPPableSpan() {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(true);
        }

        @Override
        public void onClick(View widget) {
            L.log("点击");
            ((TextView) widget).setHighlightColor(context.getResources().getColor(android.R.color.transparent));
            Intent intent = new Intent();
            intent.setClass(context, PdfActivity.class);
            intent.putExtra("url", htmlList.get(0).getCName());
            context.startActivity(intent);
        }
    }

}
