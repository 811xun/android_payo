package com.home.glx.uwallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.home.glx.uwallet.mvp.PhoneLocation_in;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.MyDialog;
import com.home.glx.uwallet.selfview.NoNetWorkDialog;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.AdvertisingIdClient;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.LocalUtils;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.SystemUtils;
import com.home.glx.uwallet.tools.gps.MyLocationManager;

import java.util.ArrayList;
import java.util.HashMap;

import static com.home.glx.uwallet.activity.xzc.LoginActivity_inputYzm.mActivityList;
import static com.home.glx.uwallet.tools.SystemUtils.getDeviceBrand;

public abstract class MainActivity extends BaseActivity {

    public MyDialog loadDialog;

    // 融合位置提供者的入口点。
    public FusedLocationProviderClient mFusedLocationProviderClient;

    public int receiveNFC = 1;
    private NoNetWorkDialog noNetWorkDialog;
    private TiShiDialog mustUpdateDialog;

    public String gaId = "";

    @Override
    public void initView() {
        mActivityList.add(this);
        activityList.add(this);
        // Construct a FusedLocationProviderClient.
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        onCheckGooglePlayServices();
        initNetDialog();
//        registerActivityListener();//activity管理类  华为手机会崩溃。
        //是否有通知权限
        //boolean falg = isOpenNotify(this);

        /*if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                if (key.equals("route")) {
                    routeValue = getIntent().getExtras().getString(key);
                    L.log("route 键值对", "Key: " + key + " Value: " + routeValue);
                }
            }
        }

        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        String token = (String) sharePreferenceUtils.get(StaticParament.USER_TOKEN, "");
        if (!TextUtils.isEmpty(routeValue) && routeValue.equals("1") && !TextUtils.isEmpty(token)) {
            Intent intent = new Intent(this, FenQiFuBill_Activity.class);
            startActivity(intent);
        }*/
    }


    //是否有通知权限
    public static boolean isOpenNotify(Context context) {
        boolean isOpened = false;
        try {
            NotificationManagerCompat from = NotificationManagerCompat.from(context);
            isOpened = from.areNotificationsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOpened;
    }

    /**
     * 获取GAID
     */
    public void getGaid() {
        getGAID(this);
        SetOnGetGaid(new onGetGaid() {
            @Override
            public void getGaid(String gaid) {
                if (!TextUtils.isEmpty(gaid)) {
                    gaId = gaid;
                }
            }
        });
    }

    /**
     * 检查 Google Play 服务
     */
    private void onCheckGooglePlayServices() {
        // 验证是否已在此设备上安装并启用Google Play服务，以及此设备上安装的旧版本是否为此客户端所需的版本
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (code == ConnectionResult.SUCCESS) {
            L.log("支持google play 服务");
            // 支持Google服务
            //初始化位置获取
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        } else {
            /**
             * 依靠 Play 服务 SDK 运行的应用在访问 Google Play 服务功能之前，应始终检查设备是否拥有兼容的 Google Play 服务 APK。
             * 我们建议您在以下两个位置进行检查：主 Activity 的 onCreate() 方法中，及其 onResume() 方法中。
             * onCreate() 中的检查可确保该应用在检查成功之前无法使用。
             * onResume() 中的检查可确保当用户通过一些其他方式返回正在运行的应用（比如通过返回按钮）时，检查仍将继续进行。
             * 如果设备没有兼容的 Google Play 服务版本，您的应用可以调用以下方法，以便让用户从 Play 商店下载 Google Play 服务。
             * 它将尝试在此设备上提供Google Play服务。如果Play服务已经可用，则Task可以立即完成返回。
             */
//            GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);

        }
    }

    /**
     * 强制更新弹窗
     */
    public void showMustUpdateDialog() {
        if (mustUpdateDialog == null) {
            mustUpdateDialog = new TiShiDialog(this);
            mustUpdateDialog.ShowNotCloseDialog(" Please update your App to the latest version to continue.", "Confirm");
        }
        mustUpdateDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
            @Override
            public void GuanBi() {
                launchAppDetail(MainActivity.this, BuildConfig.APPLICATION_ID, "com.huawei.appmarket");
            }
        });
    }


    /**
     * 网络状态
     *
     * @param status
     */
    @Override
    public void netWorkStatus(String status) {
        if (status.equals("0")) {
            Toast.makeText(this, "No network currently", Toast.LENGTH_SHORT).show();
            if (noNetWorkDialog == null) {
                initNetDialog();
            }
            noNetWorkDialog.show(this,this);
        } else {
            if (noNetWorkDialog != null) {
                noNetWorkDialog.dismiss();
            }
        }
    }

    private void initNetDialog() {
        noNetWorkDialog = NoNetWorkDialog.getInstance(this);
    }

    public boolean isLogin() {
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_TOKEN, "");
        L.log("登录状态：" + userId);
        if (userId.equals("")) {
            return false;
        }
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalUtils.attachBaseContext(newBase));
    }

    @Override
    public void getLayoutTop(Bundle savedInstanceState) {

    }

    /**
     * 使用谷歌获取经纬度，
     * 调用之前一定要获取权限
     */
    public void getLocation(final PhoneLocation_in phoneLocation_in) {
        String mobileCompany = getDeviceBrand();
        if (mobileCompany.equals("HONOR") || mobileCompany.equals("HUAWEI")) {
            //没有获取到位置信息,通过本地方法获取经纬度
            getLocalLocation(phoneLocation_in);
        } else {
            if (mFusedLocationProviderClient == null) {
                //没有获取到位置信息,通过本地方法获取经纬度
                getLocalLocation(phoneLocation_in);
                return;
            }
            /*
             *获取设备的最佳和最新位置，在位置不可用的极少数情况下，该位置可能为空。
             */
            try {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this,
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful()) {
                                    //将地图的展示位置设置为设备的当前位置。
                                    Location googleLocation = task.getResult();
                                    if (googleLocation != null) {

                                        //获取到了谷歌的Location
                                        phoneLocation_in.setNewLocation(googleLocation);
                                    } else {
                                        //没有获取到位置信息,通过本地方法获取经纬度
                                        getLocalLocation(phoneLocation_in);
                                    }

                                } else {
                                    //谷歌定位不可用，显示默认地点
                                    phoneLocation_in.setDefaultLocation();
                                }
                            }
                        });
            } catch (SecurityException e) {
                Log.e("Exception11111: %s", e.getMessage());
            }
        }
    }

    /**
     * 本地方法获取经纬度
     */
    public void getLocalLocation(final PhoneLocation_in phoneLocation_in) {
        MyLocationManager myLocationManager
                = MyLocationManager.getInstance(this, new MyLocationManager.OnGetGps() {
            @Override
            public void get_gps(Location location) {

                phoneLocation_in.setNewLocation(location);
            }

            @Override
            public void not_get_gps() {
                phoneLocation_in.notGetLocation();
            }
        });
        myLocationManager.initLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadDialog != null) {
            loadDialog.dismiss();
        }
    }


    private PowerManager.WakeLock wakeLock = null;

    /**
     * 获取电源锁，保持该服务在屏幕熄灭时仍然获取CPU时，保持运行
     */
    private void acquireWakeLock() {
        if (null == wakeLock) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE, getClass()
                    .getCanonicalName());
            if (null != wakeLock) {
                Log.i("TAG", "call acquireWakeLock");
                wakeLock.acquire();
            }
        }
    }

    // 释放设备电源锁
    private void releaseWakeLock() {
        if (null != wakeLock && wakeLock.isHeld()) {
            Log.i("TAG", "call releaseWakeLock");
            wakeLock.release();
            wakeLock = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        acquireWakeLock();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseWakeLock();
    }


    interface onGetGaid {
        void getGaid(String gaid);
    }

    private onGetGaid onGetGaid;

    private void SetOnGetGaid(onGetGaid onGetGaid) {
        this.onGetGaid = onGetGaid;
    }

    /**
     * 获取GAID
     *
     * @param context
     */
    private void getGAID(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AdvertisingIdClient.AdInfo adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
                    String GAID = adInfo.getId();
                    onGetGaid.getGaid(GAID);
                } catch (Exception e) {
                    onGetGaid.getGaid(null);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void hideSystemNavigationBar() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View view = this.getWindow().getDecorView();
            view.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 启动到应用商店app详情界面
     *
     * @param appPkg      目标App的包名
     * @param marketPkg 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面
     */

    public static void launchAppDetail(Context mContext, String appPkg, String marketPkg) {

        try {

            if (TextUtils.isEmpty(appPkg)) {

                return;

            }

            Uri uri = Uri.parse("market://details?id=" + appPkg);

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           /* if (!TextUtils.isEmpty(marketPkg)) {

                intent.setPackage(marketPkg);

            }*/
            ArrayList<HashMap<String, Object>> items = SystemUtils.getItems(mContext);
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).get("packageName").equals(marketPkg)) {
                    intent.setPackage(marketPkg);
                    mContext.startActivity(intent);
                    break;
                } else {
                    if (i == items.size() - 1) {
                        if (marketPkg.equals("com.android.vending")) {
                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID));
                            if (intent.resolveActivity(mContext.getPackageManager()) != null) { //可以接收
                                mContext.startActivity(intent);
                            }/*else {
                            Toast.makeText(mContext,"Fail to update",Toast.LENGTH_SHORT).show();
                        }*/
                        } else {
                            intent.setData(Uri.parse("https://appgallery.huawei.com/"));
                            if (intent.resolveActivity(mContext.getPackageManager()) != null) { //可以接收
                                mContext.startActivity(intent);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void setStatusBarColor(Activity activity, int Color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(Color));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏为白色 图标显示深色
        }
    }
}
