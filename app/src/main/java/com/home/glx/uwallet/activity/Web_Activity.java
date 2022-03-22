package com.home.glx.uwallet.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.mvp.JiHuoFenQi_in;
import com.home.glx.uwallet.mvp.JiHuoFenQi_p;
import com.home.glx.uwallet.selfview.TiShiDialogThree;
import com.home.glx.uwallet.tools.L;

import java.net.URISyntaxException;
import java.util.HashMap;

public class Web_Activity extends MainActivity implements JiHuoFenQi_in.ActivationInstallmentView {

    private ImageView fanhui;
    private WebView webView;

    private TextView id_title;

    private ProgressBar progressBar;

    private String url;
    private String title;
    private JiHuoFenQi_p jiHuoFenQi_p;
    private boolean zhijieback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayout() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        zhijieback = intent.getBooleanExtra("zhijieback", false);
        if (url.endsWith(".pdf")) {
            url = "https://docs.google.com/gview?embedded=true&url=" + url;
        }
        L.log("网页地址：" + url);
        title = intent.getStringExtra("title");
        if (title == null) {
            title = "";
        }
        return R.layout.activity_web;
    }

    @Override
    public void initView() {
        jiHuoFenQi_p = new JiHuoFenQi_p(this, this);
        id_title = (TextView) findViewById(R.id.id_title);
        id_title.setText(title);

        progressBar = findViewById(R.id.progressBar);

        if (getIntent().getIntExtra("text", 0) == 1) {
            openAgreementDialog();
        }

        fanhui = (ImageView) findViewById(R.id.id_fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_home();
            }
        });

        webView = (WebView) findViewById(R.id.id_webview);

        WebSettings seting = webView.getSettings();
        seting.setDomStorageEnabled(true);
        //设置支持访问文件
        seting.setAllowFileAccess(true);
        // 使用localStorage则必须打开
        seting.setGeolocationEnabled(true);
        //设置webview支持javascript脚本
        seting.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            seting.setAllowFileAccessFromFileURLs(true);
            seting.setAllowUniversalAccessFromFileURLs(true);
        }
        //设置与JS交互
        webView.addJavascriptInterface(new JavaScriptinterface(this), "App");
        //设置缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (url != null) {
            webView.loadUrl(url);
        }
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                try {
                    //如果是普通地址
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        L.log("地址：" + request.getUrl().toString());
                        view.loadUrl(request.getUrl().toString());
                    } else {
                        L.log("地址：" + request.toString());
                        view.loadUrl(request.toString());
                    }
                } catch (Exception e) {
                    return false;
                }
                return true;
            }
        });


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (shouldOverrideUrlLoadingByApp(view, url)) {
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                //打开默认浏览器
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
            }
        });


        //检测进度
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 90) {
//                    avLoadingIndicatorView.hide();
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
            }
        });
    }


    /**
     * 根据url的scheme处理跳转第三方app的业务
     */
    private boolean shouldOverrideUrlLoadingByApp(WebView view, String url) {
        return shouldOverrideUrlLoadingByAppInternal(view, url, true);
    }


    /**
     * 广告业务的特殊处理
     * 根据url的scheme处理跳转第三方app的业务
     * 如果应用程序可以处理该url,就不要让浏览器处理了,返回true;
     * 如果没有应用程序可以处理该url，先判断浏览器能否处理，如果浏览器也不能处理，直接返回false拦截该url，不要再让浏览器处理。
     * 避免出现当deepLink无法调起目标app时，展示加载失败的界面。
     * <p>
     * 某些含有deepLink链接的网页，当使用deepLink跳转目标app失败时，如果将该deepLinkUrl交给系统处理，系统处理不了，会导致加载失败；
     * 示例：
     * 网页Url：https://m.ctrip.com/webapp/hotel/hoteldetail/687592/checkin-1-7.html?allianceid=288562&sid=964106&sourceid=2504&sepopup=12
     * deepLinkUrl：ctrip://wireless/InlandHotel?checkInDate=20170504&checkOutDate=20170505&hotelId=687592&allianceid=288562&sid=960124&sourceid=2504&ouid=Android_Singapore_687592
     *
     * @param interceptExternalProtocol 是否拦截自定义的外部协议，true：拦截，无论如何都不让浏览器处理；false：不拦截，如何没有成功处理该协议，继续让浏览器处理
     */
    private boolean shouldOverrideUrlLoadingByAppInternal(WebView view, String url, boolean interceptExternalProtocol) {
        if (isAcceptedScheme(url)) {
            //如果这个地址是浏览器可以处理的地址
            return false;
        }
        Intent intent;
        try {
            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
        } catch (URISyntaxException e) {
            return interceptExternalProtocol;
        }

        intent.setComponent(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            intent.setSelector(null);
        }

        //该Intent无法被设备上的应用程序处理
        if (Web_Activity.this.getPackageManager().resolveActivity(intent, 0) == null) {
            return tryHandleByMarket(intent) || interceptExternalProtocol;
        }

        try {
            Web_Activity.this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            return interceptExternalProtocol;
        }
        return true;
    }


    private boolean tryHandleByMarket(Intent intent) {
        String packagename = intent.getPackage();
        if (packagename != null) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packagename));
            try {
                Web_Activity.this.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }


    /**
     * 该url是否属于浏览器能处理的内部协议
     */
    private boolean isAcceptedScheme(String url) {
        if (Patterns.WEB_URL.matcher(url).matches() || URLUtil.isValidUrl(url)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setFKStatus(String status, String rejectMessage) {
        if (status.equals("1")) {
//            Intent intent = new Intent(Web_Activity.this, FenQiFuAmountActivity.class);
//            startActivity(intent);
            finish();
        } else {
/*
            Intent intent = new Intent(Web_Activity.this, FenQiStatusActivity.class);
*/
//            Intent intent = new Intent(Web_Activity.this, NewFenQiStausActivity.class);
//            intent.putExtra("status", Integer.parseInt(status));
//            startActivity(intent);
//            finish();
        }
    }


    public class JavaScriptinterface {
        Context context;

        public JavaScriptinterface(Context c) {
            context = c;
        }


        @JavascriptInterface
        public void addBank() {

        }

        /*
         * JS调用android的方法
         * @JavascriptInterface仍然必不可少
         * */
        @JavascriptInterface
        public void backApp(String user_id, String status) {
            if (zhijieback) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            } else {
                L.log("illing状态:" + status);
                if (status.equals("COMPLETE")) {
                    //触发风控
                    jiHuoFenQi_p.reqActivationInstallment(new HashMap<String, Object>());
                } else if (status.equals("ERROR")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.goBack();
                        }
                    });
               /* new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        webView.goBack();
                    }
                });*/
                } else if (status.equals("WAITING")) {
//                    Intent intent = new Intent(Web_Activity.this, NewFenQiStausActivity.class);
//                    intent.putExtra("status", 0);
//                    startActivity(intent);
//                    finish();
                }
            }
        /*    //illing绑卡操作
            Intent intent = new Intent(Web_Activity.this,
                    FenQuBanks_Activity.class);
            intent.putExtra("type", "f");
            intent.putExtra("open", "s");
            startActivity(intent);
            finish();*/
        }
    }


    private void open_home() {
        if (zhijieback) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        } else {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        }
    }


    /**
     * illing认证提示弹窗
     */
    private void openAgreementDialog() {
        TiShiDialogThree tiShiDialogThree = new TiShiDialogThree(this);
        tiShiDialogThree.setOnGuanBi(new TiShiDialogThree.GuanBi() {
            @Override
            public void GuanBi() {

            }
        });
        tiShiDialogThree.ShowDialog(getString(R.string.illing_tishi));
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            open_home();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public void onBackPressed() {
        // 完全由自己控制返回键逻辑，系统不再控制，但是有个前提是：　　
        // 不要在Activity的onKeyDown或者OnKeyUp中拦截掉返回键　　
        // 拦截：就是在OnKeyDown或者OnKeyUp中自己处理了返回键　　
        // （这里处理之后return true.或者return false都会导致onBackPressed不会执行）
        // 不拦截：在OnKeyDown和OnKeyUp中返回super对应的方法　　
        // （如果两个方法都被覆写就分别都要返回super.onKeyDown,super.onKeyUp）
        open_home();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
            //恢复pauseTimers状态
            webView.resumeTimers();
            webView.reload();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
            //它会暂停所有webview的layout，parsing，javascripttimer。降低CPU功耗
            webView.pauseTimers();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

}
