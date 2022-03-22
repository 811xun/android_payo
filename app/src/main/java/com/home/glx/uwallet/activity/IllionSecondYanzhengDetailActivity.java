package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.IllionMfaData;
import com.home.glx.uwallet.mvp.ListenerManager;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.KycAndIllionListener;
import com.home.glx.uwallet.mvp.model.KycAndIllionModel;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.IllionMfaInputView;
import com.home.glx.uwallet.tools.AddMaidian;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IllionSecondYanzhengDetailActivity extends MainActivity implements View.OnClickListener {
    private TextView title;
    private String slug;
    private String userToken;
    private ImageView back;
    private List<IllionMfaData> list;
    private LinearLayout idContentLay;
    private TextView fetch;
    private List<IllionMfaInputView> viewList = new ArrayList<>();
    private KycAndIllionListener kycAndIllionListener;
    private Map<String, Object> maps = new HashMap<>();
    private int width;

    @Override
    public int getLayout() {
        return R.layout.activity_illion_mfa_detail;
    }

    @Override
    public void initView() {
        super.initView();
        kycAndIllionListener = new KycAndIllionModel(this);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        width = metric.widthPixels;

        back = (ImageView) findViewById(R.id.back);
        fetch = (TextView) findViewById(R.id.fetch);
        back.setOnClickListener(this);
        fetch.setOnClickListener(this);
        idContentLay = (LinearLayout) findViewById(R.id.id_content_lay);
        title = (TextView) findViewById(R.id.illion_title);
        String titleString = getIntent().getStringExtra("title");
        slug = getIntent().getStringExtra("slug");
        userToken = getIntent().getStringExtra("userToken");
        list = (List<IllionMfaData>) getIntent().getSerializableExtra("list");
        maps.put("slug", slug);
        maps.put("userToken", userToken);

//        TypefaceUtil.replaceFont(findViewById(R.id.top_title), "fonts/gilroy_semiBold.ttf");
//        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
//        TypefaceUtil.replaceFont(fetch, "fonts/gilroy_semiBold.ttf");

        title.setText(titleString);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                IllionMfaInputView illionInputView = new IllionMfaInputView(this, list.get(i), width);
                illionInputView.setOnChoice(new IllionMfaInputView.OnChoice() {
                    @Override
                    public void choiceItem(String code, String key) {
                        maps.put(key, code);
                    }
                });
                viewList.add(illionInputView);
                idContentLay.addView(illionInputView.getView());
            }
        }

        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
        final Map<String, Object> maps1 = new HashMap<>();
        maps1.put("userId", userId);
        maps1.put("type", "8");//页面标识 1：二选一 2：证件类型页 3:地址页 4:个人信息填写页 5:绑卡页 6:illion默认机构页 7:illion 第一次验证页，8:illion第二次验证页 9:分期付开通等待页
        AddMaidian.addMaidian(this, maps1);
    }

    private boolean checkInput() {
        for (int i = 0; i < viewList.size(); i++) {
            IllionMfaInputView view = viewList.get(i);
            if (!view.getValueLimit()) {
                return false;
            }
            if (view.isSetMap()) {
                Map<String, Object> setMap = view.getSetValue();
                maps.putAll(setMap);
            } else {
                String[] keyValue = view.getValue().split(",");
                if (!TextUtils.isEmpty(keyValue[0]) && !TextUtils.isEmpty(keyValue[1])) {
                    maps.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (!PublicTools.isFastClick()) {
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.back:
                finish();
                break;
            case R.id.fetch:
                PublicTools.closeKeybord(this);
                if (checkInput()) {
                    kycAndIllionListener.mfaSubmit(maps, new ActionCallBack() {
                        @Override
                        public void onSuccess(Object... o) {
                            String result = (String) o[0];
                            if (result.equals("true")) {
                                ListenerManager.getInstance().sendBroadCast("mfaFinish");
                                ListenerManager.getInstance().sendBroadCast("illionFinish");
                                Intent intent = new Intent(IllionSecondYanzhengDetailActivity.this, InstalWaitingActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onError(String e) {

                        }
                    });
                }
        }
    }
}
