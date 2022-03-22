package com.home.glx.uwallet.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.home.glx.uwallet.BaseFragment;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.NewMerchantInfoActivity;
import com.home.glx.uwallet.activity.xzc.LoginActivity_inputNumber;
import com.home.glx.uwallet.adapter.MerchantInfoAdapter;
import com.home.glx.uwallet.datas.MerchantInfoData;
import com.home.glx.uwallet.mvp.IListener;
import com.home.glx.uwallet.mvp.ListenerManager;
import com.home.glx.uwallet.mvp.RefreshMerchantList;
import com.home.glx.uwallet.mvp.ReqAllMerchant;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.GetLocationDialog;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;
import java.util.Objects;

import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

/**
 * 按距离排序
 */
public class DistanceFragment extends BaseFragment implements ActivityCompat.OnRequestPermissionsResultCallback, IListener {
    private RelativeLayout loactionRl;
    private Button openLoaction;
    private RefreshLayout refreshLayout;
    private LinearLayout listLl;
    private ReqAllMerchant reqAllMerchant;
    private int width = 720;
    private int maxPages = 0;
    private int page = 1;
    private ListView id_recyclerview;
    private MerchantInfoAdapter adapter;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 0;

    @Override
    public int getLayout() {
        return R.layout.fragment_by_discount;
    }

    @Override
    public void initView() {
        ListenerManager.getInstance().registerListtener(this);

        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        width = metric.widthPixels;
        listLl = (LinearLayout) view.findViewById(R.id.list_ll);
        id_recyclerview = (ListView) view.findViewById(R.id.id_recyclerview);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        loactionRl = (RelativeLayout) view.findViewById(R.id.location_permission);
        openLoaction = (Button) view.findViewById(R.id.open_location);
        reqAllMerchant = new ReqAllMerchant(getContext());
        reqAllMerchant.setOnGetAllMerchant(new ReqAllMerchant.OnGetAllMerchant() {
            @Override
            public void getAllMerchant(List<MerchantInfoData> listData, int maxPage, String flag, String searchKeyword) {
                setData(listData, maxPage);
            }
        });
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
                L.log("加载更多");
                page++;
                reqAllMerchant.reqAllMerchant(page, maxPages, "","1");
                refreshLayout.finishLoadMore();
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshLayout) {
                L.log("下拉刷新");
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //下拉刷新
                        refreshLayout.finishRefresh();
                        RefreshMerchantList refreshMerchantList = new RefreshMerchantList(getContext());
                        refreshMerchantList.setOnGetLocation(new RefreshMerchantList.OnGetLocation() {
                            @Override
                            public void getLocation() {
                                if (!isHidden()) {
                                page = 1;
                                reqAllMerchant.reqAllMerchant(page, maxPages, "","1");
                            }}
                        });
                        refreshMerchantList.refreshMerchantList();
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });
        openLoaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DistanceFragment.this.requestPermissions(
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        });
        checkLocationPermission(1);
    }

    private void setData(List<MerchantInfoData> listData, int maxPages) {
        this.maxPages = maxPages;
        if (listData.size() > 0) {
            id_recyclerview.setVisibility(View.VISIBLE);
        } else {
            id_recyclerview.setVisibility(View.GONE);
            return;
        }

        id_recyclerview.setFocusableInTouchMode(false);
        id_recyclerview.setFocusable(false);

        if (adapter != null) {
            adapter.notifyDataSetChang(listData);
        } else {
            adapter = new MerchantInfoAdapter(getContext(), listData, width);
            adapter.setOnitemClick(new MerchantInfoAdapter.OnitemClick() {
                @Override
                public void itemClick(String id, String name) {
                    if (!isLogin()) {
//                        Intent intent_loging = new Intent(getContext(), Login_Activity.class);
                        Intent intent_loging = new Intent(getContext(), LoginActivity_inputNumber.class);
                        startActivity(intent_loging);
                        return;
                    }
                    Intent intent_about_us = new Intent(getContext(), NewMerchantInfoActivity.class);
                    intent_about_us.putExtra("id", id);
                    intent_about_us.putExtra("title", name);
                    startActivity(intent_about_us);
                }
            });
            id_recyclerview.setAdapter(adapter);
        }
    }

    /**
     * 检查设备位置权限。
     */
    public void checkLocationPermission(int refresh) {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(getContext(), StaticParament.APP);
        //纬度
        String lat = (String) appMsgSharePreferenceUtils.get(StaticParament.LAT, "");
        String lng = (String) appMsgSharePreferenceUtils.get(StaticParament.LNG, "");
        if (/*TextUtils.isEmpty(lat) && TextUtils.isEmpty(lng)*/!checkLocationPermission()) {
            mLocationPermissionGranted = false;
            listLl.setVisibility(View.GONE);
            SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(getContext(), StaticParament.DEVICE);
            String locationFlag = (String) sharePreferenceUtils.get(StaticParament.DISTANCE_LOCATION_FLAG, "");
            if (locationFlag.equals("0")) {
                sharePreferenceUtils.put(StaticParament.DISTANCE_LOCATION_FLAG,"1");
                sharePreferenceUtils.save();
                GetLocationDialog getLocationDialog = new GetLocationDialog(getContext());
                getLocationDialog.ShowDialog();
                getLocationDialog.setOnClose(new GetLocationDialog.Close() {
                    @Override
                    public void onClose() {
                        loactionRl.setVisibility(View.VISIBLE);
                    }
                });
                getLocationDialog.setOnUpdate(new GetLocationDialog.Update() {
                    @Override
                    public void onUpdate() {
                        DistanceFragment.this.requestPermissions(
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSIONS_REQUEST_FINE_LOCATION);
                    }
                });
            } else if (locationFlag.equals("1")) {
                loactionRl.setVisibility(View.VISIBLE);
            }
        } else {
            if (refresh == 1) {
                mLocationPermissionGranted = true;
                loactionRl.setVisibility(View.GONE);
                listLl.setVisibility(View.VISIBLE);
                //请求数据
                maxPages = 0;
                page = 1;
                reqAllMerchant.reqAllMerchant(page, maxPages, "", "1");
            } else {
                if (!mLocationPermissionGranted) {
                    mLocationPermissionGranted = true;
                    loactionRl.setVisibility(View.GONE);
                    listLl.setVisibility(View.VISIBLE);
                    //请求数据
                    maxPages = 0;
                    page = 1;
                    reqAllMerchant.reqAllMerchant(page, maxPages, "", "1");
                }
            }
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ((MainTab) getActivity()).getNowLocationDistance();
                    loactionRl.setVisibility(View.GONE);
                    listLl.setVisibility(View.VISIBLE);
                } else {
                    loactionRl.setVisibility(View.VISIBLE);
                    listLl.setVisibility(View.GONE);
                }
                break;
        }
    }

    /**
     * 判断位置权限
     */
    public boolean checkLocationPermission() {
        String[] PERMISSION_STORAGE =
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        if (hasPermissions(Objects.requireNonNull(getContext()), PERMISSION_STORAGE)) {
            //有权限
            return true;
        } else {
            L.log("申请权限");
            return false;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            checkLocationPermission(0);
        }
    }

    public void refresh() {
        maxPages = 0;
        page = 1;
        Log.i("wuhao", "Distance刷新");
        reqAllMerchant.reqAllMerchant(page, maxPages, "", "1");
    }

    @Override
    public void notifyAllActivity(String title, String str) {
        Log.i("wuhao", "Distance收到广播");
        if (str.equals("getPermission")) {
           refresh();
           ((AllMerchantFragment) (DistanceFragment.this.getParentFragment())).findCityStInfo();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ListenerManager.getInstance().unRegisterListener(this);
    }
}
