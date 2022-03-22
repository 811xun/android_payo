package com.home.glx.uwallet;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import com.braze.Braze;
import com.braze.BrazeActivityLifecycleCallbackListener;
import com.braze.configuration.BrazeConfig;
import com.braze.support.BrazeLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.home.glx.uwallet.activity.InputPin_Activity;
import com.home.glx.uwallet.mvp.LoadNetTime;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.AppFrontBackHelper;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;
import com.huawei.hms.maps.MapsInitializer;
import com.stripe.android.PaymentConfiguration;

import java.util.HashMap;

public class MyApplication extends Application {

    public static Context context;
    //0、刚打开APP  1、切换到后台了
    public static String openType = "0";
    public static String nowActivity = "";

    public static String openPage = "0";
    public static String isNFCMainTab = "0";


    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // 设置API Key
        MapsInitializer.setApiKey("CgB6e3x9s/WsqDT7lGQkn+TmOTDfm97b+qryXe1wR0sZB5vboYHhSDaMt75OliiIjwC/pVj8yEr6E/KJeLl2hMTD");

//        Bugly
//        CrashReport.initCrashReport(getApplicationContext(), "c209d20e56", false);

        /* Bugly SDK初始化
         * 参数1：上下文对象
         * 参数2：APPID，平台注册时得到,注意替换成你的appId
         * 参数3：是否开启调试模式，调试模式下会输出'CrashReport'tag的日志
         */
//        CrashReport.initCrashReport(getApplicationContext(), "e9ae99981c", true);//xzc 注释 1.10
//        CatchExceptionUtil crashHandler = CatchExceptionUtil.getInstance();
//        crashHandler.init(getApplicationContext());

        loadNetTime();

        AppFrontBackHelper helper = new AppFrontBackHelper();
        helper.register(this, new AppFrontBackHelper.OnAppStatusListener() {
            @Override
            public void onFront() {
                //应用切到前台处理
                loadNetTime();
                if (nowActivity.equals("SetPinPwd_Activity")) {
                    //如果当前页面是设置PIN页面
                    return;
                }
                if (openType.equals("1")) {
                    SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(MyApplication.this, StaticParament.USER);
                    long go_away_time = (long) sharePreferenceUtils.get(StaticParament.GO_AWAY_TIME, 0L);
                    String token = (String) sharePreferenceUtils.get(StaticParament.USER_TOKEN, "");
                    if (!token.equals("")) {
                        //打开主页面
                        if (System.currentTimeMillis() - go_away_time > 1800000) {
//                        if (System.currentTimeMillis() - go_away_time > 10000) {
                            //inputPinNum();
                        }
                    }
                }
            }

            @Override
            public void onBack() {
                //应用切到后台处理
                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(MyApplication.this, StaticParament.USER);
                sharePreferenceUtils.put(StaticParament.GO_AWAY_TIME, System.currentTimeMillis());
                sharePreferenceUtils.save();
                openType = "1";
            }
        });

//        initAF();

        //切换字体
        TypefaceUtil.replaceSystemDefaultFont(MyApplication.this, "fonts/font_fc.ttf");

        //stripe
        PaymentConfiguration.init(
                this,
                "pk_test_51K90efAgx3Fd2j3eMaQewzW7Tg6lf22bF2BAV6UuFI61tWVI3ych8pFQhersDxcNnmVm94FQYOFYhhr9HZLc7M3z00NqCpso3y" //测试的
//                "pk_live_51JN8U4LucMncOTu4v5hodWtxows8sNbgkUDjc3vL1iiA1sW4bNVhNPtrUEBi3HVy8eBOMoQinW1ExeGVwIw0qBk800t2vIcyvv" //发布的 正式使用的
        );

        //初始化Braze
        BrazeLogger.setLogLevel(Log.DEBUG);
        configureAppBoyAtRuntime();
        registerActivityLifecycleCallbacks(new BrazeActivityLifecycleCallbackListener());
        setupFirebaseCrashlytics();
    }

    private void configureAppBoyAtRuntime() {
        BrazeConfig brazeConfig = new BrazeConfig.Builder()
//                .setApiKey("ad215a4c-0e76-4bc1-9ce2-11cc6ecaa21a")//测试
                .setApiKey("088db92f-1e91-4d5b-8c6b-2265b89968b5")//正式
                .setCustomEndpoint("sdk.iad-05.braze.com")
                //启用设备消息的自动注册。
                .setAdmMessagingRegistrationEnabled(false)
                //设置 SDK 是否自动注册 Firebase 云消息服务。
//                .setIsFirebaseCloudMessagingRegistrationEnabled(true)
//                .setFirebaseCloudMessagingSenderIdKey("327652118015")
                //会话超时之前的时间长度（以秒为单位）。
                .setSessionTimeout(15)
                //设置是否获取屏幕唤醒锁以唤醒设备屏幕以获取推送通知。
                .setIsPushWakeScreenForNotificationEnabled(true)
                //使 Braze 能够在单击推送通知时自动打开您的应用和任何深层链接
                .setHandlePushDeepLinksAutomatically(true)
                //自动注册
//                .setFirebaseCloudMessagingSenderIdKey("230734597809")
//                .setIsFirebaseCloudMessagingRegistrationEnabled(true)
//                .setSmallNotificationIcon(resources.getResourceEntryName(R.mipmap.logo))
//                .setLargeNotificationIcon(resources.getResourceEntryName(R.mipmap.logo))
                //触发操作之间的最小秒数
                .setTriggerActionMinimumTimeIntervalSeconds(5)
                //如果为 true，则将"新闻源"可视指示器设置为启用"已读/未读"状态。
                .setNewsfeedVisualIndicatorOn(true)
                //此布尔值设置 Braze 是否应自动收集位置（如果用户允许）。
                .setIsLocationCollectionEnabled(true)
//                //推送通知的默认主题色
//                .setDefaultNotificationAccentColor(0xFFf33e3e)
//                .setBadNetworkDataFlushInterval(120)
//                .setGoodNetworkDataFlushInterval(60)
//                .setGreatNetworkDataFlushInterval(10)
                .build();

        Braze.configure(this, brazeConfig);
        initBrazePushToken();
    }

    String TAG = "tag";

    /**
     * 测试推送  生产环境注释掉
     */
    private void initBrazePushToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
//                Toast.makeText(this, "token failed", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }

            final String token = task.getResult();
            Log.i(TAG, "================");
            Log.i(TAG, "================");
            Log.i(TAG, "Registering firebase token in Application class: " + token);
//                Toast.makeText(this, "token success", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "================");
            Log.i(TAG, "================");
            Braze.getInstance(this).registerAppboyPushMessages(token);
        });
    }


    private void inputPinNum() {
        Intent intent = new Intent(this, InputPin_Activity.class);
        intent.putExtra("type", "back");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private static final String AF_DEV_KEY = "KaT6MfPMK8CZ4JB6HwgsgB";

    /**
     * 初始化AF
     */
    /*private void initAF() {
        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {

                for (String attrName : conversionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.d("LOG_TAG", "error getting conversion data: " + errorMessage);
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> conversionData) {

                for (String attrName : conversionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }

            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d("LOG_TAG", "error onAttributionFailure : " + errorMessage);
            }
        };

        AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionListener, getApplicationContext());
        AppsFlyerLib.getInstance().start(this);
    }*/

    /**
     * 关闭所有页面
     */
    public void finishAllActivity() {

    }

    //与服务器相差时间
    public static long differenceTime = 0;


    /**
     * 获取服务器时间
     */
    private void loadNetTime() {
        LoadNetTime loadNetTime = new LoadNetTime(this);
        loadNetTime.setOnKycNumer(new LoadNetTime.OnResTiem() {
            @Override
            public void netTime(long num) {
                long nowTime = System.currentTimeMillis();
                differenceTime = num - nowTime - 200;
                L.log("和后台相差时间：" + differenceTime);
            }
        });
        loadNetTime.getTime(new HashMap<String, Object>());
    }

    private void setupFirebaseCrashlytics() {
        FirebaseCrashlytics firebaseCrashlytics = FirebaseCrashlytics.getInstance();
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(BuildConfig.DEBUG);
        firebaseCrashlytics.setCustomKey("version_code", BuildConfig.VERSION_CODE);
        firebaseCrashlytics.setCustomKey("version_name", BuildConfig.VERSION_NAME);
    }
}
