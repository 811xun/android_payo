package com.home.glx.uwallet.mvp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.home.glx.uwallet.datas.MerchantInfoData;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.SystemUtils;
import com.home.glx.uwallet.tools.gps.MyLocationManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.home.glx.uwallet.tools.SystemUtils.getDeviceBrand;
import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

/**
 * 商户列表下拉刷新的逻辑
 */
public class RefreshMerchantList {
    private Context context;
    // 融合位置提供者的入口点。
    public FusedLocationProviderClient mFusedLocationProviderClient;
    public RefreshMerchantList(Context context) {
        this.context = context;
    }

    public void refreshMerchantList() {
        onCheckGooglePlayServices();
        if (checkLocationPermission()) {
            getLocation();
        } else {
            findLocationByIp();
        }
    }

    /**
     * ip定位
     */
    public void findLocationByIp() {
        SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.APP);
        Map<String, Object> maps = new HashMap<>();
        maps.put("ip", SystemUtils.getIPAddress(context));

        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.findLocationByIp(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, final String code) {
                        L.log("ip定位：" + dataStr);
                        Gson gson = new Gson();
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            String lat = jsonObject.getString("lat");
                            String lng = jsonObject.getString("lng");
                            Log.e("ip经度：", lat);
                            Log.e("ip纬度：", lng);
                            String city = jsonObject.getString("city");
                            SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.APP);
                            if (TextUtils.isEmpty(lat) && TextUtils.isEmpty(lng) && TextUtils.isEmpty(city)) {
                                onGetLocation.getLocation();
                            } else {
                                appMsgSharePreferenceUtils.put(StaticParament.LAT, lat + "");
                                appMsgSharePreferenceUtils.put(StaticParament.LNG, lng + "");
                                appMsgSharePreferenceUtils.put(StaticParament.CITY, city + "");
                                appMsgSharePreferenceUtils.save();
                                onGetLocation.getLocation();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void resError(String error) {
                        onGetLocation.getLocation();
                    }
                });
    }

    /**
     * 检查 Google Play 服务
     */
    private void onCheckGooglePlayServices() {
        // 验证是否已在此设备上安装并启用Google Play服务，以及此设备上安装的旧版本是否为此客户端所需的版本
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);
        if (code == ConnectionResult.SUCCESS) {
            L.log("支持google play 服务");
            // 支持Google服务
            //初始化位置获取
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
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
     * 判断位置权限
     */
    public boolean checkLocationPermission() {
        String[] PERMISSION_STORAGE =
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        if (hasPermissions(Objects.requireNonNull(context), PERMISSION_STORAGE)) {
            //有权限
            return true;
        } else {
            L.log("申请权限");
            return false;
        }
    }

    /**
     * 使用谷歌获取经纬度，
     * 调用之前一定要获取权限
     */
    public void getLocation() {
        //没有获取到位置信息,通过本地方法获取经纬度
        getLocalLocation();
        /*String mobileCompany = getDeviceBrand();
        if (mobileCompany.equals("HONOR") || mobileCompany.equals("HUAWEI")) {
            //没有获取到位置信息,通过本地方法获取经纬度
            getLocalLocation();
        } else {
            if (mFusedLocationProviderClient == null) {
                //没有获取到位置信息,通过本地方法获取经纬度
                getLocalLocation();
                return;
            }
            *//*
             *获取设备的最佳和最新位置，在位置不可用的极少数情况下，该位置可能为空。
             *//*
            try {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener((Activity) context,
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful()) {
                                    //将地图的展示位置设置为设备的当前位置。
                                    Location googleLocation = task.getResult();
                                    if (googleLocation != null) {
                                        //获取到了谷歌的Location
                                        SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.APP);
                                        appMsgSharePreferenceUtils.put(StaticParament.LAT, googleLocation.getLatitude() + "");
                                        appMsgSharePreferenceUtils.put(StaticParament.LNG, googleLocation.getLongitude() + "");
                                        appMsgSharePreferenceUtils.save();
                                        onGetLocation.getLocation();
                                    } else {
                                        //没有获取到位置信息,通过本地方法获取经纬度
                                        getLocalLocation();
                                    }

                                } else {
                                    //谷歌定位不可用，显示默认地点
                                    onGetLocation.getLocation();
                                }
                            }
                        });
            } catch (SecurityException e) {
                Log.e("Exception: %s", e.getMessage());
            }
        }*/
    }

    /**
     * 本地方法获取经纬度
     */
    public void getLocalLocation() {
        MyLocationManager myLocationManager
                = MyLocationManager.getInstance(context, new MyLocationManager.OnGetGps() {
            @Override
            public void get_gps(Location location) {
                SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.APP);
                appMsgSharePreferenceUtils.put(StaticParament.LAT, location.getLatitude() + "");
                appMsgSharePreferenceUtils.put(StaticParament.LNG, location.getLongitude() + "");
                appMsgSharePreferenceUtils.save();
                onGetLocation.getLocation();
            }

            @Override
            public void not_get_gps() {

            }
        });
        myLocationManager.initLocation();
    }

    public OnGetLocation onGetLocation;

    public interface OnGetLocation{
        void getLocation();
    }

    public void setOnGetLocation(OnGetLocation onGetLocation) {
        this.onGetLocation = onGetLocation;
    }
}
