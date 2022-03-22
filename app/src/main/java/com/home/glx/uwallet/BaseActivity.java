package com.home.glx.uwallet;

import android.app.Activity;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.LocalUtils;
import com.home.glx.uwallet.tools.network.NetStatusReceiver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends PermissionsBaseActivity {

    public boolean getPermission = true;
    public boolean getLocaion = false;
    public static List<Activity> activityList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.setStatusBarColor(getResources().getColor(R.color.white));
            //设置显示为白色背景，黑色字体
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏状态栏
        //动态设置语言
        LocalUtils.setLang(this);
        getLayoutTop(savedInstanceState);
        if (getLayout() != 0) {
            setContentView(getLayout());
        }

//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {//https://www.jianshu.com/p/f915d5981923
//            fixOrientation();
//        }
//
        initView();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


    private boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    //实现字体不随调节改变
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    //实现字体不随调节改变
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    public boolean isGetLocaion() {
        return getLocaion;
    }

    public void setGetLocaion(boolean getLocaion) {
        this.getLocaion = getLocaion;
    }

    public void setGetPermission(boolean thereGet) {
        this.getPermission = thereGet;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getPermission) {
            //获取权限
            initSimple();
            //注册网络监听广播
            initReceiver();
        }
        if (getLocaion) {
            initLocation();
        }
    }


    public abstract void getLayoutTop(Bundle savedInstanceState);

    public abstract int getLayout();

    public abstract void initView();

    public abstract void netWorkStatus(String status);

    /**
     * 权限通过
     */
    @Override
    public void permissionStatus(int requestCode) {

    }

    @Override
    public void permissionDenied(int requestCode) {

    }

    @Override
    public void haveLoactionStatus() {

    }

    private NetStatusReceiver receiver;
    private boolean mReceiverTag = true;

    //运行广播,监听网络变化
    public void initReceiver() {
        mReceiverTag = true;
        receiver = new NetStatusReceiver();
        receiver.setOnCallBackNetWorkState(new NetStatusReceiver.CallBackNetWorkState() {
            @Override
            public void callBackState(boolean status) {
                L.log("广播监听的网络状态：" + status);
                if (status) {
                    netWorkStatus("1");
                } else {
                    netWorkStatus("0");
                }
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, intentFilter);
    }


    @Override
    protected void onPause() {
        L.log("onPause");
        //界面销毁不要忘了注销广播
        if (receiver != null && mReceiverTag && getPermission) {
            unregisterReceiver(receiver);
            mReceiverTag = false;
            receiver = null;
        }
        super.onPause();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (receiver != null && mReceiverTag) {
//            unregisterReceiver(receiver);
//            mReceiverTag = false;
//            receiver = null;
//        }
//    }
}
