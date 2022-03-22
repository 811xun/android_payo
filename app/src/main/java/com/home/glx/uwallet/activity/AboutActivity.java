package com.home.glx.uwallet.activity;

import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.SystemUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

public class AboutActivity extends MainActivity {
    private ImageView back;
    private TextView version;
    private TextView email;
    private TextView textView;
    private TextView tel;
    private TextView title;
    @Override
    public int getLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.setStatusBarColor(getResources().getColor(R.color.end_orange));
            //设置显示为白色背景，黑色字体
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        super.initView();
        back = (ImageView) findViewById(R.id.back);
        version = (TextView) findViewById(R.id.version);
        email = (TextView) findViewById(R.id.email);
        tel = (TextView) findViewById(R.id.tel);
        title = findViewById(R.id.title1);
        textView = (TextView) findViewById(R.id.text);
        if (getIntent().getStringExtra("formMain") != null) {
            //textView.setText("Dine-in or takeaway today and pay over 4 interest-free instalments");
        }
        TextView text1 = findViewById(R.id.text1);
        TextView text2 = findViewById(R.id.text2);
        TextView text3 = findViewById(R.id.text3);
        TextView versionText = findViewById(R.id.version_text);
        TextView emailText = findViewById(R.id.email_text);
        TextView telText = findViewById(R.id.tel_text);
        version.setText(SystemUtils.getVersion(this));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.DEVICE);
        String emails = (String) sharePreferenceUtils.get(StaticParament.APP_EMAIL, "");
        String tels = (String) sharePreferenceUtils.get(StaticParament.APP_PHONE, "");

        //切换字体
        TypefaceUtil.replaceFont(title,"fonts/casta_bold.ttf");
        TypefaceUtil.replaceFont(textView,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(text1,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(text2,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(text3,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(versionText,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(emailText,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(telText,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(version,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(email,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(tel,"fonts/gilroy_semiBold.ttf");

        email.setText(emails);
        tel.setText(tels);

    }
}