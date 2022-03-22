package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.IllionMfaData;
import com.home.glx.uwallet.datas.IllionPreLoadData;
import com.home.glx.uwallet.mvp.IListener;
import com.home.glx.uwallet.mvp.ListenerManager;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.KycAndIllionListener;
import com.home.glx.uwallet.mvp.model.KycAndIllionModel;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.IllionInputView;
import com.home.glx.uwallet.tools.AddMaidian;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IllionDetailActivity extends MainActivity implements View.OnClickListener, IListener {
    private ImageView back;
    private List<IllionPreLoadData> list;
    private String slug;
    private String userToken;
    private LinearLayout idContentLay;
    private Button mBtn;
    private List<IllionInputView> viewList = new ArrayList<>();
    private KycAndIllionListener kycAndIllionListener;
    private Map<String, Object> maps = new HashMap<>();
    private TextView protext;
    private RelativeLayout rl_icon;
    private ImageView icon;
    private CheckBox check_up;
    private TextView proText_up;

    @Override
    public int getLayout() {
        return R.layout.activity_illion_detail;
    }

    @Override
    public void initView() {
        super.initView();
        ListenerManager.getInstance().registerListtener(this);
        kycAndIllionListener = new KycAndIllionModel(this);
        protext = (TextView) findViewById(R.id.protext);
//        addressNewStyle(protext);
        idContentLay = (LinearLayout) findViewById(R.id.id_content_lay);
        mBtn = (Button) findViewById(R.id.fetch);
        back = (ImageView) findViewById(R.id.back);
//        illionName = (TextView) findViewById(R.id.bank_name);
//        TextView title = findViewById(R.id.title);
//        TypefaceUtil.replaceFont(title,"fonts/gilroy_semiBold.ttf");

        TypefaceUtil.replaceFont(findViewById(R.id.tv_payo), "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(protext, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(mBtn, "fonts/gilroy_semiBold.ttf");

        check_up = (CheckBox) findViewById(R.id.id_protocols_1);
        proText_up = findViewById(R.id.proText_1);
        TypefaceUtil.replaceFont(proText_up, "fonts/gilroy_medium.ttf");

        String img = getIntent().getStringExtra("img");
        String bankName = getIntent().getStringExtra("name");
        slug = getIntent().getStringExtra("slug");
        userToken = getIntent().getStringExtra("userToken");
        list = (List<IllionPreLoadData>) getIntent().getSerializableExtra("list");
        back.setOnClickListener(this);
        mBtn.setOnClickListener(this);
        icon = findViewById(R.id.icon);
        rl_icon = findViewById(R.id.rl_icon);
        TypefaceUtil.replaceFont(findViewById(R.id.tv_hint), "fonts/gilroy_medium.ttf");
        ((TextView) findViewById(R.id.tv_hint)).setText("The bank you have selected is " + bankName);
        maps.put("slug", slug);
        maps.put("userToken", userToken);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                IllionInputView illionInputView = new IllionInputView(this, list.get(i));
                illionInputView.setOnChoice(new IllionInputView.OnChoice() {
                    @Override
                    public void choiceItem(String code, String key) {
                        maps.put(key, code);
                    }
                });
                viewList.add(illionInputView);
                idContentLay.addView(illionInputView.getView());
            }
        }
        for (int i = 0; i < viewList.size(); i++) {
            IllionInputView view = viewList.get(i);
            view.setOnInputTextChange(new IllionInputView.OnInputTextChange() {
                @Override
                public void onTextChange() {
                    if (check()) {
                        mBtn.setBackground(ContextCompat.getDrawable(IllionDetailActivity.this, R.mipmap.btn_orange_jianbian));
                    } else {
                        mBtn.setBackground(ContextCompat.getDrawable(IllionDetailActivity.this, R.mipmap.btn_hui_jianbian));
                    }
                }
            });
        }

        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
        final Map<String, Object> maps1 = new HashMap<>();
        maps1.put("userId", userId);
        maps1.put("type", "7");//页面标识 1：二选一 2：证件类型页 3:地址页 4:个人信息填写页 5:绑卡页 6:illion默认机构页 7:illion 第一次验证页，8:illion第二次验证页 9:分期付开通等待页
        AddMaidian.addMaidian(this, maps1);

        check_up.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (check()) {
                    mBtn.setBackground(ContextCompat.getDrawable(IllionDetailActivity.this, R.mipmap.btn_orange_jianbian));
                } else {
                    mBtn.setBackground(ContextCompat.getDrawable(IllionDetailActivity.this, R.mipmap.btn_hui_jianbian));
                }
            }
        });

        addressNewStyle(proText_up);
    }

    private boolean check() {
        for (int i = 0; i < viewList.size(); i++) {
            IllionInputView view = viewList.get(i);
            if (!view.getValueLimit(false)) {
                return false;
            }
        }
        if (!check_up.isChecked()) {//详情时没有checkbox
            return false;
        }
        return true;
    }


    private boolean checkInput() {
        for (int i = 0; i < viewList.size(); i++) {
            IllionInputView view = viewList.get(i);
            if (!view.getValueLimit(true)) {
                return false;
            }
            String[] keyValue = view.getValue().split(",");
            if (keyValue.length == 2) {
                if (!TextUtils.isEmpty(keyValue[0]) && !TextUtils.isEmpty(keyValue[1])) {
                    maps.put(keyValue[0], keyValue[1]);
                }
            }
        }
        if (!check_up.isChecked()) {//详情时没有checkbox
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//大于等于9.0
                Typeface font = Typeface.createFromAsset(getAssets(), "fonts/gilroy_medium.ttf");
                SpannableString efr = new SpannableString(getString(R.string.please_agree_agreement));
                efr.setSpan(new TypefaceSpan(font), 0, efr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Toast.makeText(IllionDetailActivity.this, efr, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(IllionDetailActivity.this, getString(R.string.please_agree_agreement), Toast.LENGTH_SHORT).show();
            }
            mBtn.setBackground(ContextCompat.getDrawable(IllionDetailActivity.this, R.mipmap.btn_hui_jianbian));
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back:
                finish();
                break;
            case R.id.fetch:
                if (checkInput()) {
                    if (!PublicTools.isFastClick()) {
                        return;
                    }
                    mBtn.setEnabled(false);
                    mBtn.setText("");
                    Animation rotateAnimation = AnimationUtils.loadAnimation(IllionDetailActivity.this, R.anim.rotate_anim);
                    LinearInterpolator lin = new LinearInterpolator();
                    rotateAnimation.setInterpolator(lin);
                    icon.startAnimation(rotateAnimation);
                    rl_icon.setVisibility(View.VISIBLE);

                    PublicTools.closeKeybord(IllionDetailActivity.this);
                    kycAndIllionListener.fetchIllion(maps, new ActionCallBack() {
                        @Override
                        public void onSuccess(Object... o) {
                            rl_icon.setVisibility(View.GONE);
                            mBtn.setEnabled(true);
                            mBtn.setText("Next");
                            String result = (String) o[0];
                            if (result.equals("true")) {
                                ListenerManager.getInstance().sendBroadCast("illionFinish");
                                Intent intent = new Intent(IllionDetailActivity.this, InstalWaitingActivity.class);
                                startActivity(intent);
                                finish();
                            } else {//illion第二次验证
                                String title = (String) o[1];
                                List<IllionMfaData> data = (List<IllionMfaData>) o[2];
                                String token = (String) o[3];
                                Intent intent = new Intent(IllionDetailActivity.this, IllionSecondYanzhengDetailActivity.class);
                                intent.putExtra("title", title);
                                intent.putExtra("slug", slug);
                                intent.putExtra("userToken", token);
                                intent.putExtra("list", (Serializable) data);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onError(String e) {
                            rl_icon.setVisibility(View.GONE);
                            mBtn.setEnabled(true);
                            mBtn.setText("Next");
                        }
                    });

//                    ConditionDialog conditionDialog = new ConditionDialog(IllionDetailActivity.this);
//                    conditionDialog.ShowDialog("");
//                    conditionDialog.setOnGuanBi(new ConditionDialog.GuanBi() {
//                        @Override
//                        public void GuanBi() {
//
//                        }
//
//                        @Override
//                        public void queding() {
//
//                        }
//                    });
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ListenerManager.getInstance().unRegisterListener(this);
    }

    @Override
    public void notifyAllActivity(String title, String str) {
        if (str.equals("mfaFinish")) {
            finish();
        }
    }


    class MyClickCTCableSpan extends ClickableSpan {

        public MyClickCTCableSpan() {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            L.log("点击");
            ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
            Intent intent = new Intent();
            intent.setClass(IllionDetailActivity.this, Web_Activity.class);
            intent.putExtra("url", "https://bankstatements.com.au/about/terms");
            startActivity(intent);
        }
    }

    class MyClickPPableSpan extends ClickableSpan {

        public MyClickPPableSpan() {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            L.log("点击");
            ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
            Intent intent = new Intent();
            intent.setClass(IllionDetailActivity.this, Web_Activity.class);
            intent.putExtra("url", "https://www.illion.com.au/privacy-policy-risk-marketing-solutions");
            startActivity(intent);
        }
    }

    /**
     * 协议样式修改
     *
     * @param textView
     */
    private void addressNewStyle(TextView textView) {
        textView.setHighlightColor(getResources().getColor(android.R.color.transparent));
        SpannableString spannableString = new SpannableString("I agree the User Terms & Conditions and\nPrivacy Policy provided by\nBankStatements.com.au (ABN 89 166 277 845).");
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
        MyClickCTCableSpan clickableSpan2 = new MyClickCTCableSpan();

        spannableString.setSpan(clickableSpan2, 11, 35, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpan1, 40, 54, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
        spannableString.setSpan(colorSpan1, 11, 35, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan2, 40, 54, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString);
    }
}
