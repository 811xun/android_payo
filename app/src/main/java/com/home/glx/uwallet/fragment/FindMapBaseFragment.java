package com.home.glx.uwallet.fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.home.glx.uwallet.BaseFragment;
import com.home.glx.uwallet.tools.L;

public abstract class FindMapBaseFragment extends BaseFragment {

    // 融合位置提供者的入口点。
    public FusedLocationProviderClient mFusedLocationProviderClient;



    @Override
    public void initView() {
        // Construct a FusedLocationProviderClient.
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        onCheckGooglePlayServices();
    }

    /**
     * 检查 Google Play 服务
     */
    private void onCheckGooglePlayServices() {
        // 验证是否已在此设备上安装并启用Google Play服务，以及此设备上安装的旧版本是否为此客户端所需的版本
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        if (code == ConnectionResult.SUCCESS) {
            L.log("支持google play 服务");
            // 支持Google服务
            //初始化位置获取
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
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
}
