package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.selfview.PDFView;

/**
 * WebView打开pdf文件
 */
public class PdfActivity extends MainActivity {
    private ImageView back;
    private PDFView pdfView;
    private String url;
    @Override
    public int getLayout() {
        return R.layout.activity_pdf;
    }

    @Override
    public void initView() {
        super.initView();
        back = (ImageView) findViewById(R.id.id_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        pdfView = findViewById(R.id.pdfView);
        pdfView.loadOnlinePDF(url);
    }
}
