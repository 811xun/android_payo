package com.home.glx.uwallet;

import android.Manifest;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.home.glx.uwallet.datas.TabDatas;
import com.home.glx.uwallet.fragment.FirstFragment;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.fragment.SearchFragment;
import com.home.glx.uwallet.tools.L;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

/**
 * 申请权限
 */
public abstract class PermissionsBaseActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    public static List<TabDatas> tabList = new ArrayList<>();
    public static int tabNum = 0;

    public static final int PERMISSION_STORAGE_CODE = 106;
    public static final int PERMISSION_LOCATION_CODE = 116;
    public static final int PERMISSION_STORAGE_MSG = R.string.permissions_prompt;
    public static final int PERMISSION_Camera_MSG = R.string.permissions_prompt_camera;
    //默认已进入APP请求的权限
    public static final String[] PERMISSION_STORAGE =
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.READ_PHONE_STATE
            };

    /**
     * 是否有相应权限
     *
     * @param context
     * @return
     */
    public static boolean hasStoragePermission(Context context) {
        return hasPermissions(context, PERMISSION_STORAGE);
    }

    /**
     * 调用获取权限
     */
    @AfterPermissionGranted(PERMISSION_LOCATION_CODE)
    public void initLocation() {
        String[] PERMISSION_LOCATION =
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        if (!hasPermissions(this, PERMISSION_LOCATION) && MainTab.aaaa) {
            List<String> perms = new ArrayList<>();
            perms.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                //这里表示拒绝权限后再点击出现的去设置对话框
                //这里需要重新设置Rationale和title，否则默认是英文格式
                //Rationale：对话框的提示内容，否则默认是英文格式
                // title：对话框的提示标题，否则默认是英文格式
                //这里是点击不在提示权限才会走 一个丑陋的弹窗
                new AppSettingsDialog.Builder(this)
                        .setRationale(R.string.not_permissions_prompt)
                        .setTitle(R.string.must_permissions)
                        .build()
                        .show();
            } else {
                //申请权限
                EasyPermissions.requestPermissions(this, getResources().getString(PERMISSION_STORAGE_MSG),
                        PERMISSION_LOCATION_CODE,
                        PERMISSION_LOCATION);
            }
        }
        MainTab.aaaa = true;
    }

    private int grantResult = 0;

    //拒绝时-1
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        long aaa = System.currentTimeMillis();
//        if (aaa - time > 500) {
//        }
        if (grantResults.length > 0) {
            grantResult = grantResults[0];//在开启权限的弹窗上 点击拒绝 拒绝后不再提示 返回-1
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        }
    }

    /**
     * 权限通过
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (perms.size() == 2) {
            permissionStatus(requestCode);
        } else {
            if (requestCode == PERMISSION_LOCATION_CODE || requestCode == 124) {
//                locationQuanxian = false;
                permissionStatus(requestCode);
                FirstFragment.locationQuanxian = true;
                SearchFragment.locationQuanxian = true;
            }
        }
    }


    /**
     * 权限拒绝  xzc
     *
     * @param requestCode
     * @param perms
     */
    private boolean mFirst = true;

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == PERMISSION_LOCATION_CODE) {
//        if (requestCode != PERMISSION_LOCATION_CODE) {
            permissionDenied(requestCode);
        }
        Log.d("xxxxxxxx", "onPermissionsDenied: " + EasyPermissions.somePermissionPermanentlyDenied(this, perms));
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms) && grantResult == 0 && MainTab.aaaa) {
            //这里表示拒绝权限后再点击出现的去设置对话框
            //这里需要重新设置Rationale和title，否则默认是英文格式
            //Rationale：对话框的提示内容，否则默认是英文格式
            // title：对话框的提示标题，否则默认是英文格式
            if (requestCode == PERMISSION_STORAGE_CODE || requestCode == PERMISSION_LOCATION_CODE && mFirst) {
                //这里是点击不在提示权限才会走 一个丑陋的弹窗
                new AppSettingsDialog.Builder(this)
                        .setRationale(R.string.not_permissions_prompt)
                        .setTitle(R.string.must_permissions)
//                        .setThemeResId(R.style.tipDialog2)
                        .build()
                        .show();
//                mFirst = false;
            }
        }
        MainTab.aaaa = true;
        grantResult = 0;

//        }
    }

    public abstract void haveLoactionStatus();

    public abstract void permissionStatus(int requestCode);

    public abstract void permissionDenied(int requestCode);

    /**
     * dialog提醒点击“确定”
     *
     * @param requestCode
     */
    @Override
    public void onRationaleAccepted(int requestCode) {
        L.log("权限 确定：" + requestCode);


    }

    boolean exe = true;//点击cancel 不在弹出提示开启权限的弹窗了。

    public void exe(boolean exe) {
        this.exe = exe;
    }

    /**
     * dialog提醒点击“取消”
     *
     * @param requestCode
     */
    @Override
    public void onRationaleDenied(int requestCode) {
        L.log("权限 取消：" + requestCode);
        if ((requestCode == PERMISSION_STORAGE_CODE || requestCode == PERMISSION_LOCATION_CODE) && exe) {
            return;
//            new AppSettingsDialog.Builder(this)
//                    .setRationale(R.string.not_permissions_prompt)
//                    .setTitle(R.string.must_permissions)
//                    .build()
//                    .show();
        }
        if (!exe && requestCode == PERMISSION_LOCATION_CODE) {//点击首页 小弹窗的cancel 隐藏列表数据
            ((FirstFragment) tabList.get(0).getFragment()).yincangListView();
        }
        exe = true;
    }


    /**
     * 获取位置权限
     */
    public void getLocationPermission(int requestCode) {
        String[] PERMISSION_LOCATION =
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        if (hasPermissions(this, PERMISSION_LOCATION)) {
            //有权限
            L.log("有权限");
            permissionStatus(requestCode);
        } else {
            L.log("申请权限");
            //申请权限
            ActivityCompat.requestPermissions(this,
                    PERMISSION_LOCATION,
                    requestCode);
        }
    }


    /**
     * 获取存儲权限
     */
    @AfterPermissionGranted(PERMISSION_STORAGE_CODE)
    public void initSimple() {
        if (hasStoragePermission(this)) {
            permissionStatus(PERMISSION_STORAGE_CODE);
        } else {
//            List<String> perms = new ArrayList<>();
//            perms.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            perms.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//            perms.add(Manifest.permission.READ_PHONE_STATE);
//            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//                //这里表示拒绝权限后再点击出现的去设置对话框
//                //这里需要重新设置Rationale和title，否则默认是英文格式
//                //Rationale：对话框的提示内容，否则默认是英文格式
//                // title：对话框的提示标题，否则默认是英文格式
//                //这里是点击不在提示权限才会走 一个丑陋的弹窗
//                new AppSettingsDialog.Builder(this)
//                        .setRationale(R.string.not_permissions_prompt)
//                        .setTitle(R.string.must_permissions)
//                        .build()
//                        .show();
//            } else {
//
//            }
            EasyPermissions.requestPermissions(this, getResources().getString(PERMISSION_STORAGE_MSG),
                    PERMISSION_STORAGE_CODE,
                    PERMISSION_STORAGE);

        }
    }


    /**
     * 获取相机权限
     */
    public void getCameraPermission(int requestCode) {
        String[] PERMISSION_STORAGE =
                new String[]{Manifest.permission.CAMERA};
        initSimple(requestCode, PERMISSION_STORAGE);
    }

    public void initSimple(int requestCode, String[] permissionsList) {
        if (hasPermissions(this, permissionsList)) {
            permissionStatus(requestCode);
        } else {
            List<String> perms = new ArrayList<>();
            perms.add(Manifest.permission.CAMERA);
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {//
                //这里表示拒绝权限后再点击出现的去设置对话框
                //这里需要重新设置Rationale和title，否则默认是英文格式
                //Rationale：对话框的提示内容，否则默认是英文格式
                // title：对话框的提示标题，否则默认是英文格式
                //这里是点击不在提示权限才会走 一个丑陋的弹窗
                new AppSettingsDialog.Builder(this)
                        .setRationale(R.string.not_permissions_prompt)
                        .setTitle(R.string.must_permissions)
                        .build()
                        .show();
            } else {
                EasyPermissions.requestPermissions(this, requestCode == 124 ? getResources().getString(PERMISSION_Camera_MSG) : getResources().getString(PERMISSION_STORAGE_MSG),
                        requestCode,
                        permissionsList);

            }
        }
    }

}
