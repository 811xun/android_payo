package com.home.glx.uwallet.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;
import java.util.Objects;

import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

/**
 * 按折扣排序
 */
public class DiscountFragment extends Fragment implements IListener {
    private int width = 720;
    private int maxPages = 0;
    private int page = 1;
    private RefreshLayout refreshLayout;
    private ReqAllMerchant reqAllMerchant;
    private ListView id_recyclerview;
    private MerchantInfoAdapter adapter;
    private View view;
    private boolean mLocationPermissionGranted;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_by_discount, container, false);
        initView();
        return view;
    }

    public void initView() {
        ListenerManager.getInstance().registerListtener(this);

        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        width = metric.widthPixels;
        id_recyclerview = (ListView) view.findViewById(R.id.id_recyclerview);
        reqAllMerchant = new ReqAllMerchant(getContext());
        reqAllMerchant.reqAllMerchant(page, maxPages, "", "0");
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
                reqAllMerchant.reqAllMerchant(page, maxPages, "","0");
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
                                    reqAllMerchant.reqAllMerchant(page, maxPages, "", "0");
                                }}
                        });
                        refreshMerchantList.refreshMerchantList();
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });
        checkLocationPermission(true);
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Log.i("wwwww","显示中");
        }
    }

    /**
     * 判断位置权限
     */
    public boolean checkLocationPermission(boolean haveFlag) {
        String[] PERMISSION_STORAGE =
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        if (hasPermissions(Objects.requireNonNull(getContext()), PERMISSION_STORAGE)) {
            //有权限
            if (haveFlag) {
                mLocationPermissionGranted = true;
            }
            return true;
        } else {
            L.log("申请权限");
            if (haveFlag) {
                mLocationPermissionGranted = false;
            }
            return false;
        }
    }

    public boolean isLogin() {
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(getContext(), StaticParament.USER);
        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_TOKEN, "");
        L.log("登录状态：" + userId);
        if (userId.equals("")) {
            return false;
        }
        return true;
    }

    public void refresh() {
        if (checkLocationPermission(false)) {
            if (!mLocationPermissionGranted) {
                mLocationPermissionGranted = true;
                maxPages = 0;
                page = 1;
                Log.i("wuhao", "Dicount刷新");
                reqAllMerchant.reqAllMerchant(page, maxPages, "","0");
            }
        }
    }

    @Override
    public void notifyAllActivity(String title, String str) {
        Log.i("wuhao", "Discount收到广播");
        if (str.equals("getPermission")) {
            //refresh();
            maxPages = 0;
            page = 1;
            Log.i("wuhao", "Dicount刷新");
            reqAllMerchant.reqAllMerchant(page, maxPages, "","0");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ListenerManager.getInstance().unRegisterListener(this);
    }
}
