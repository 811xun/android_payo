package com.home.glx.uwallet.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.GetChoiceDatas;
import com.home.glx.uwallet.datas.UserAllMsgDatas;
import com.home.glx.uwallet.mvp.ChangeEmail;
import com.home.glx.uwallet.mvp.GetChoiceList_in;
import com.home.glx.uwallet.mvp.GetChoiceList_p;
import com.home.glx.uwallet.mvp.GetUserAllMsg_in;
import com.home.glx.uwallet.mvp.GetUserAllMsg_p;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.DampingReboundNestedScrollView;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class UserMsgActivity extends MainActivity implements OnClickListener, GetChoiceList_in.View,
        GetUserAllMsg_in.View  {
    private GetChoiceList_p choiceList_p;
    private GetChoiceDatas getChoiceDatas;
    private GetUserAllMsg_p present;
    private TextView save;
    private TextView nationality;
    private TextView nationalityTv;
    private TextView firstName;
    private TextView lastName;
    private TextView gender;
    private TextView genderTv;
    private TextView email;
    private TextView tel;
    private TextView dob;
    private TextView driverLicence;
    private TextView licenceText;
    private TextView nationalityLl;
    private TextView genderLl;
    private DampingReboundNestedScrollView scrollView;
    private String defaultEmail;
    private ImageView back;
    private TextView emailLl;
    private TextView telLl;

    @Override
    public int getLayout() {
        return R.layout.activity_user_msgs;
    }

    @Override
    public void initView() {
        super.initView();
        present = new GetUserAllMsg_p(this, this);
        choiceList_p = new GetChoiceList_p(this, this);
        scrollView = findViewById(R.id.scroll_view);
        nationalityLl = findViewById(R.id.nationality_ll);
        genderTv = findViewById(R.id.gender_text);
        nationalityTv = findViewById(R.id.nationality_text);
        genderLl = findViewById(R.id.gender_ll);
        save = (TextView) findViewById(R.id.save);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nationality = (TextView) findViewById(R.id.nationality);
        firstName = (TextView) findViewById(R.id.first_name);
        lastName = (TextView) findViewById(R.id.last_name);
        gender = (TextView) findViewById(R.id.gender);
        email = (TextView) findViewById(R.id.email);
        emailLl = findViewById(R.id.email_ll);
        telLl = findViewById(R.id.tel_ll);
        tel = (TextView) findViewById(R.id.tel);
        dob = (TextView) findViewById(R.id.dob);
        licenceText = (TextView) findViewById(R.id.licence_text);
        driverLicence = (TextView) findViewById(R.id.driver_licence);
        TextView title = findViewById(R.id.title);
        TextView firstNameText = findViewById(R.id.first_name_text);
        TextView lastNameText = findViewById(R.id.last_name_text);
        TextView genderText = findViewById(R.id.gender_text);
        TextView emailText = findViewById(R.id.email_text);
        TextView telText = findViewById(R.id.tel_text);
        TextView dobText = findViewById(R.id.dob_text);
        //切换字体
        TypefaceUtil.replaceFont(nationalityTv,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(title,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(firstNameText,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(lastNameText,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(genderText,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(emailText,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(telText,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(dobText,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(licenceText,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(firstName,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(nationality,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(lastName,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(gender,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(email,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(tel,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(dob,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(driverLicence,"fonts/gilroy_semiBold.ttf");

        emailLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PublicTools.isFastClick()) {
                    return;
                }
                Intent intent = new Intent(UserMsgActivity.this, ChangeEmailActivity.class);
                startActivity(intent);
            }
        });
        /*save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.getText().toString().equals(defaultEmail)) {
                    //修改邮箱
                    changeEmailReq();
                }
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        String phone = (String) sharePreferenceUtils.get(StaticParament.USER_EMAIL, "");
        if (!phone.equals("")) {
            String top = phone.substring(0, 2);
            String end = phone.substring(2, phone.length());
//            idUserPhone.setText((String) sharePreferenceUtils.get(StaticParament.USER_EMAIL, ""));
            tel.setText("(" + top + ")" + end);
            telLl.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!PublicTools.isFastClick()) {
                        return;
                    }
                    Intent intent = new Intent(UserMsgActivity.this, ChangePhoneNumActivity.class);
                    startActivity(intent);
                }
            });
        }
        //获取选项
        Map<String, Object> maps = new HashMap<>();
        String[] keys = new String[]{"sex", "county", "merchantState", "medicalCardType"};
        maps.put("code", keys);
        choiceList_p.loadChoiceList(maps);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setChoiceList(GetChoiceDatas getChoiceDatas) {
        if (getChoiceDatas != null) {
            this.getChoiceDatas = getChoiceDatas;
            //获取用户信息
            present.loadUserAllMsg(new HashMap<String, Object>());
        }
    }

    @Override
    public void setUserMsg(UserAllMsgDatas userMsg) {
        if (userMsg == null) {
            return;
        }
        scrollView.setVisibility(View.VISIBLE);
        /*if (TextUtils.isEmpty(userMsg.getUserCitizenship())) {
            nationality.setVisibility(View.GONE);
            nationalityLl.setVisibility(View.GONE);
            nationalityTv.setVisibility(View.GONE);
        } else {
            nationality.setVisibility(View.VISIBLE);
            nationalityLl.setVisibility(View.VISIBLE);
            nationalityTv.setVisibility(View.VISIBLE);
            if (userMsg.getUserCitizenship().equals("1")) {
                nationality.setText(getResources().getString(R.string.australia));
            }
            if (userMsg.getUserCitizenship().equals("2")) {
                nationality.setText(getResources().getString(R.string.china));
            }
        }*/
        firstName.setText(userMsg.getUserFirstName());
        lastName.setText(userMsg.getUserLastName());
        for (int i = 0; i < getChoiceDatas.getSex().size(); i++) {
            if (userMsg.getSex().equals(getChoiceDatas.getSex().get(i).getValue())) {
//                gender.setText(getChoiceDatas.getSex().get(i).getName());
//                gender.setVisibility(View.VISIBLE);
//                genderLl.setVisibility(View.VISIBLE);
//                genderTv.setVisibility(View.VISIBLE);
                break;
            }
            if (i == getChoiceDatas.getSex().size() - 1) {
//                gender.setVisibility(View.GONE);
//                genderLl.setVisibility(View.GONE);
//                genderTv.setVisibility(View.GONE);
            }
        }
        defaultEmail = userMsg.getEmail();
        email.setText(userMsg.getEmail());
        dob.setText(userMsg.getBirth());

        if (!userMsg.getDriverLicence().equals("")) {
            //驾照号码
            String driver;
            driver = userMsg.getDriverLicence() + "\n";
            for (int i = 0; i < getChoiceDatas.getMerchantState().size(); i++) {
                if (getChoiceDatas.getMerchantState().get(i).getValue().equals(userMsg.getDriverLicenceState())) {
                    driver += getChoiceDatas.getMerchantState().get(i).getName();
                    break;
                }
            }
            licenceText.setText("Driver’s Licence\nState of Issuance");
            driverLicence.setText(driver);
            return;
        }

        if (!userMsg.getPassport().equals("")) {
            //护照
            String passText = "Passport";
            String pass = userMsg.getPassport();
            if (!TextUtils.isEmpty(userMsg.getPassportIndate())) {
                passText += "\nDate of expiry";
                pass += "\n" + userMsg.getPassportIndate();
            }
            for (int i = 0; i < getChoiceDatas.getCounty().size(); i++) {
                if (getChoiceDatas.getCounty().get(i).getValue().equals(userMsg.getPassportCountry())) {
                    passText += "\nCountry of Issuance";
                    pass += "\n" + getChoiceDatas.getCounty().get(i).getName();
                    break;
                }
            }
            licenceText.setText(passText);
            driverLicence.setText(pass);
            return;
        }

        if (!userMsg.getMedicare().equals("")) {
            //医保卡
            String medicareText = "Medicare Card";
            String medicare = userMsg.getMedicare();

            if (!TextUtils.isEmpty(userMsg.getMedicareRefNo())) {
                medicareText += "\nPersonal reference number";
                medicare += "\n" + userMsg.getMedicareRefNo();
            }
            for (int i = 0; i < getChoiceDatas.getMedicalCardType().size(); i++) {
                if (getChoiceDatas.getMedicalCardType().get(i).getValue().equals(userMsg.getMedicareType())) {
                    medicareText += "\nCard Type";
                    medicare += "\n" + getChoiceDatas.getMedicalCardType().get(i).getName();
                    break;
                }
            }
            medicareText += "\nMaturity Date";
            medicare += "\n" + userMsg.getMedicareIndate();
            licenceText.setText(medicareText);
            driverLicence.setText(medicare);
        }
    }

    private void changeEmailReq() {
        if (email.getText().toString().trim().equals("")) {
            Toast.makeText(this, R.string.bank_email_input_error, Toast.LENGTH_SHORT).show();
            return;
        }
        ChangeEmail changeEmail = new ChangeEmail(this);
        changeEmail.setOnFindList(new ChangeEmail.OnChangeSatus() {
            @Override
            public void status(String status) {

            }
        });
        Map<String, String> maps = new HashMap<>();
        maps.put("email", email.getText().toString().trim());
        changeEmail.change(maps);
    }

}