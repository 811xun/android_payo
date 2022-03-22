package com.home.glx.uwallet.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 消息详情
 */
public class MessageDetailActivity extends MainActivity {

    private ImageView idBack;
    private TextView idTitle;
    private TextView idContent;

    @Override
    public int getLayout() {
        return R.layout.activity_message_detail;
    }


    @Override
    public void initView() {
        super.initView();

        TextView title = findViewById(R.id.title);
        idBack = (ImageView) findViewById(R.id.id_back);
        idTitle = (TextView) findViewById(R.id.id_title);
        idContent = (TextView) findViewById(R.id.id_content);

        idBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        idTitle.setText(getIntent().getStringExtra("title"));
        idContent.setText(getIntent().getStringExtra("content"));
        //切换字体
        TypefaceUtil.replaceFont(idTitle,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(idContent,"fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(title,"fonts/gilroy_semiBold.ttf");
    }


}
