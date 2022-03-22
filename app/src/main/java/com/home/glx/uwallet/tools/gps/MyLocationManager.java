package com.home.glx.uwallet.tools.gps;

/**
 * Created by GLX on 2017/6/7.
 */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import java.util.List;

import static com.home.glx.uwallet.fragment.MainTab.showLocationToast;

public class MyLocationManager {
    private static Context mContext;
    private LocationManager gpsLocationManager;
    private LocationManager networkLocationManager;
    private static final int MINTIME = 5000;
    private static final int MININSTANCE = 1;
    private static MyLocationManager instance;
    private Location lastLocation = null;

    public Location getGpsNum() {
        return lastLocation;
    }

    private static void init(Context context) {
        mContext = context;
    }

    private MyLocationManager() {
        //        mContext = context;
        //        get_quanxian();
    }

    public void initLocation() {
        try {
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            //获取GPS是否打开
            boolean ok = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //获取网络定位是否打开
            boolean net_work_ok = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!ok && !net_work_ok) {
                if (showLocationToast)
                    tishi();
            } else {
                //有定位服务打开
                is_open(locationManager);
            }
            showLocationToast = true;

        } catch (Exception e) {

        }
    }


    private void get_quanxian() {

    }


    private void tishi() {
        Toast.makeText(mContext, "Location service is unavailable, please turn on location", Toast.LENGTH_SHORT).show();
    }

    private void Gps() {
        // Gps 定位
        gpsLocationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location gpsLocation = gpsLocationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation == null) {
            gpsLocation = getLastKnownLocation(gpsLocationManager);
        }
        if (gpsLocation != null) {
            updateLocation(gpsLocation);
        } else {
            NetWork();
        }
    }

    private Location getLastKnownLocation(LocationManager locationManager) {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    public void NetWork() {
        //基站定位
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            return;
        }
        // 基站定位
        networkLocationManager = (LocationManager) mContext
                .getSystemService(Context.LOCATION_SERVICE);
        Location networkLocation = networkLocationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (networkLocation != null) {
            updateLocation(networkLocation);
        } else {
            if (getGps != null) {
                //经纬度回调
                getGps.not_get_gps();
                //destoryLocationManager();
                gpsLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, MINTIME, MININSTANCE,
                        locationListener);
                networkLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, MINTIME, MININSTANCE,
                        locationListener);
            }
        }
    }


    /**
     * 判断是否有gps、基站定位，不管是否打开
     *
     * @param locationManager
     */
    private void is_open(LocationManager locationManager) {
        if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
            //            Toast.makeText(mContext, "GPS定位服务可用", Toast.LENGTH_SHORT).show();
            Gps();
        } else if (locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
            //            Toast.makeText(mContext, "基站定位服务可用", Toast.LENGTH_SHORT).show();
            NetWork();
        } else {
            if (locationManager.getProvider(LocationManager.GPS_PROVIDER) != null
                    || locationManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {

            } else {
                //无法定位：1、提示用户打开定位服务；2、跳转到设置界面
                Toast.makeText(mContext, R.string.gps_not_use, Toast.LENGTH_SHORT).show();
                Intent i = new Intent();
                i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(i);
            }
        }
    }


    public static MyLocationManager getInstance(Context context, OnGetGps onGetGps) {
        init(context);
        if (null == instance) {
            instance = new MyLocationManager();
        }
        instance.get_quanxian();
        instance.getOnGps(onGetGps);
        return instance;
    }

    private void updateLocation(Location location) {
        //if (lastLocation == null) {
        lastLocation = location;
        Log.i("经度：", location.getLongitude() + "");
        Log.i("纬度：", location.getLatitude() + "");
        SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(mContext, StaticParament.APP);
        appMsgSharePreferenceUtils.put(StaticParament.LAT, location.getLatitude() + "");
        appMsgSharePreferenceUtils.put(StaticParament.LNG, location.getLongitude() + "");
        appMsgSharePreferenceUtils.save();
        if (getGps != null) {
            //经纬度回调
            getGps.get_gps(location);
            destoryLocationManager();
        }
        instance = null;
        //}
    }

    private LocationListener locationListener = new LocationListener() {

        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onProviderDisabled(String provider) {

        }

        public void onLocationChanged(Location location) {
            Log.i("位置回调", location.getLatitude() + " " + location.getLongitude());
            //if (lastLocation == null) {
            updateLocation(location);
            //}
            //destoryLocationManager();
        }
    };

    public void destoryLocationManager() {
        if (gpsLocationManager != null) {
            gpsLocationManager.removeUpdates(locationListener);
            gpsLocationManager = null;
        }
        if (networkLocationManager != null) {
            networkLocationManager.removeUpdates(locationListener);
            networkLocationManager = null;
        }
        if (locationListener != null) {
            locationListener = null;
        }
        instance = null;
    }

    public interface OnGetGps {
        void get_gps(Location location);

        void not_get_gps();
    }

    public OnGetGps getGps;

    public void getOnGps(OnGetGps getGps) {
        this.getGps = getGps;
    }
}
