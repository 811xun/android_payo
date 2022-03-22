package com.home.glx.uwallet.selfview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

/**
 * Created by wuhao on 2020/12/6.
 */
public class PDFView extends FrameLayout {
    private final static String PDFJS_PREFIX = "file:///android_asset/pdf_js/web/viewer.html?file=";
    private WebView webView;

    public PDFView(Context context) {
        super(context);
        init();
    }

    public PDFView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PDFView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        webView = new WebView(getContext());
        addView(webView);
        WebSettings settings = webView.getSettings();
        //设置缓存模式
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(false);
        settings.setJavaScriptEnabled(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
    }

    public void loadLocalPDF(String path) {
        webView.loadUrl(PDFJS_PREFIX + "file://" + path);
    }

    public void loadOnlinePDF(String url) {
        Log.i("url",PDFJS_PREFIX + url);
        webView.loadUrl(PDFJS_PREFIX + url);
    }
}
