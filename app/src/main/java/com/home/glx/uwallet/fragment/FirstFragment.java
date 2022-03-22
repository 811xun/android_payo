package com.home.glx.uwallet.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.home.glx.uwallet.BaseActivity;
import com.home.glx.uwallet.BuildConfig;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.AboutActivity;
import com.home.glx.uwallet.activity.BannerClickActivity;
import com.home.glx.uwallet.activity.ChooseIllionActivity;
import com.home.glx.uwallet.activity.KycAndIllionResultActivity;
import com.home.glx.uwallet.activity.MapActivity;
import com.home.glx.uwallet.activity.MessageActivity;
import com.home.glx.uwallet.activity.NewAddBankCardActivity;
import com.home.glx.uwallet.activity.NewMerchantInfoActivity;
import com.home.glx.uwallet.activity.RegistTwo_Activity;
import com.home.glx.uwallet.activity.SelectPayTypeActivity;
import com.home.glx.uwallet.activity.SetPinPwd_Activity;
import com.home.glx.uwallet.activity.Setting_Activity;
import com.home.glx.uwallet.activity.ShareToEarnActivity;
import com.home.glx.uwallet.activity.ViewAllActivity;
import com.home.glx.uwallet.activity.ViewMoreActivity;
import com.home.glx.uwallet.activity.Web_Activity;
import com.home.glx.uwallet.activity.xzc.ChooseCardActivity;
import com.home.glx.uwallet.adapter.HomeMarketAdapter;
import com.home.glx.uwallet.adapter.HomeMerchantAdapter;
import com.home.glx.uwallet.datas.FenqifuStatue;
import com.home.glx.uwallet.datas.HomeBannerData;
import com.home.glx.uwallet.datas.HomeMerchantListData;
import com.home.glx.uwallet.datas.HuaWeiUpdateData;
import com.home.glx.uwallet.mvp.CheckAppVersionVerify;
import com.home.glx.uwallet.mvp.GetHomeMerchantList;
import com.home.glx.uwallet.mvp.IListener;
import com.home.glx.uwallet.mvp.ListenerManager;
import com.home.glx.uwallet.mvp.LoadNotReadMessage;
import com.home.glx.uwallet.mvp.PhoneLocation_in;
import com.home.glx.uwallet.mvp.WhetherOpenInvest_in;
import com.home.glx.uwallet.mvp.WhetherOpenInvest_p;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.MerchantListener;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.MerchantModel;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.BannerClickDialog;
import com.home.glx.uwallet.selfview.HomeReceiveDialog;
import com.home.glx.uwallet.selfview.LoadingDialog;
import com.home.glx.uwallet.selfview.NoLoadingDialog;
import com.home.glx.uwallet.selfview.TCDialog;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.selfview.TiShiDialogTwo;
import com.home.glx.uwallet.selfview.UpdateDialog;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.SystemUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;
import com.home.glx.uwallet.tools.VersionChecker;
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack;
import com.huawei.updatesdk.service.otaupdate.UpdateKey;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.huawei.updatesdk.UpdateSdkAPI.checkAppUpdate;
import static com.huawei.updatesdk.service.otaupdate.UpdateStatusCode.HAS_UPGRADE_INFO;
import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

/**
 * 首页
 */
public class FirstFragment extends FirstTabFragment implements View.OnClickListener, IListener, WhetherOpenInvest_in.View {
    private MerchantListener merchantListener;
    //是否开通个业务
    private WhetherOpenInvest_p openInvestPresent;
    private UserListener userListener;
    private RefreshLayout refreshLayout;
    private RecyclerView list1;
    private RecyclerView list2;
    private RecyclerView moreList;
    private RecyclerView list3;
    private RecyclerView list4;
    private RecyclerView list5;
    private TextView search;
    private TextView text1;
    private String categoryType1;
    private String categoryOrder1;
    private TextView text2;
    private String categoryType2;
    private String categoryOrder2;
    private TextView textMore;//广告
    private String categoryTypeMore;
    private TextView text3;
    private String categoryType3;
    private String categoryOrder3;
    private TextView text4;
    private String categoryType4;
    private String categoryOrder4;
    private TextView text5;
    private String categoryType5;
    private String categoryOrder5;
    private TextView view1;
    private TextView view2;
    private TextView viewMore;
    private TextView view3;
    private TextView view4;
    private TextView view5;
    private ImageView banner;
    private TextView closeText;//黄条
    private ImageView close;
    private ImageView setting;//有banner时的设置项
    private ImageView setting1;//没有banner时的设置项
    private ImageView message;//有banner时的消息项 （铃铛）
    private ImageView message1;//没有banner时的消息项 （铃铛）
    private ConstraintLayout payoCl;//payo+铃铛+设置
    private ConstraintLayout bannerCl;
    private HomeMerchantAdapter adapter1;
    private HomeMerchantAdapter adapter2;
    private HomeMerchantAdapter adapter3;
    private HomeMerchantAdapter adapter4;
    private HomeMerchantAdapter adapter5;
    private HomeMarketAdapter marketAdapter;//市场推广（广告）的adapter位于第三项
    private NestedScrollView scrollView;
    private String version = "";
    private TiShiDialog tiShiDialog;
    private UpdateDialog updateDialog;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 0;
    private boolean createFlag = true;
    //再回首页是否需要弹窗
    private boolean checkReceiverFlag = false;
    private boolean haveGpsFlag = false;
    public static int backStatue = -1;//1:从登陆注册二选一流程+从首页进入的+ViewMoreActivity进入开通分期付 返回都进入首页 FirstFragment ；2 钱包页 ；3 金额输入页 PayMoneyActivity 4 支付失败页（PayFailedActivity 卡支付失败切换分期付）
    private ImageView iv_no_loc;
    private JSONObject jsonObject;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                updatedialogAndOtherDialog();
            }
            if (msg.what == 2) {
                tiShiDialog.ShowNotCloseDialog(" Please update your App to the latest version to continue.", "Confirm");
            } else if (msg.what == 3) {
                updateDialog.ShowDialog(version);
            } else if (msg.what == 4) {//启动到谷歌应用商店app详情界面
                launchAppDetail(getContext(), BuildConfig.APPLICATION_ID, "com.android.vending");
            }
        }
    };
    private TextView what;

    @Override
    public int getLayout() {
        return R.layout.fragment_first;
    }

    public static FirstFragment newInstance() {
        Bundle args = new Bundle();
        FirstFragment fragment = new FirstFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 获取当前位置
     */
    public void getLocation() {
        ((MainTab) getActivity()).getLocation(new PhoneLocation_in() {
            @Override
            public void notGetLocation() {

            }

            @Override
            public void setNewLocation(Location location) {
                Log.e("经度：", location.getLongitude() + "");
                Log.e("纬度：", location.getLatitude() + "");
                SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(getActivity(), StaticParament.APP);
                if (appMsgSharePreferenceUtils.get(StaticParament.LAT, "").equals(location.getLongitude()) &&
                        appMsgSharePreferenceUtils.get(StaticParament.LNG, "").equals(location.getLatitude())) {

                } else {
                    appMsgSharePreferenceUtils.put(StaticParament.LAT, location.getLatitude() + "");
                    appMsgSharePreferenceUtils.put(StaticParament.LNG, location.getLongitude() + "");
                    appMsgSharePreferenceUtils.save();
                }
                getHomeList();
                /*PostUserLocation postUserLocation = new PostUserLocation(MainTab.this);
                postUserLocation.postLocation(location.getLatitude(), location.getLongitude());*/
            }

            @Override
            public void setDefaultLocation() {
                getHomeList();
            }
        });
    }

    @Override
    public void initView() {
        super.initView();
        String check = getActivity().getIntent().getStringExtra("haveCheckUpdate");
        if (check != null && check.equals("1")) {
            ((MainTab) getActivity()).setHaveUpdateFlag(true);
        }
        getLocation();
        userListener = new UserModel(getContext());
        openInvestPresent = new WhetherOpenInvest_p(getContext(), this);
        ListenerManager.getInstance().registerListtener(this);
        merchantListener = new MerchantModel(getContext());
        scrollView = (NestedScrollView) view.findViewById(R.id.scroll_view);
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        search = (TextView) view.findViewById(R.id.edit);
        setting = (ImageView) view.findViewById(R.id.setting);
        setting1 = (ImageView) view.findViewById(R.id.setting1);
        message = (ImageView) view.findViewById(R.id.messageView);
        message1 = (ImageView) view.findViewById(R.id.messageView1);
        payoCl = (ConstraintLayout) view.findViewById(R.id.payo_cl);
        bannerCl = (ConstraintLayout) view.findViewById(R.id.banner_cl);
        close = (ImageView) view.findViewById(R.id.close);
        closeText = (TextView) view.findViewById(R.id.close_text);
        banner = (ImageView) view.findViewById(R.id.banner);
        list1 = (RecyclerView) view.findViewById(R.id.list1);
        list2 = (RecyclerView) view.findViewById(R.id.list2);
        moreList = (RecyclerView) view.findViewById(R.id.list3);
        list3 = (RecyclerView) view.findViewById(R.id.list4);
        list4 = (RecyclerView) view.findViewById(R.id.list5);
        list5 = (RecyclerView) view.findViewById(R.id.list6);
        what = view.findViewById(R.id.what);
        list1.setNestedScrollingEnabled(false);
        list2.setNestedScrollingEnabled(false);
        list3.setNestedScrollingEnabled(false);
        list4.setNestedScrollingEnabled(false);
        list5.setNestedScrollingEnabled(false);
        moreList.setNestedScrollingEnabled(false);
        text1 = (TextView) view.findViewById(R.id.text1);
        text2 = (TextView) view.findViewById(R.id.text2);
        textMore = (TextView) view.findViewById(R.id.text3);
        text3 = (TextView) view.findViewById(R.id.text4);
        text4 = (TextView) view.findViewById(R.id.text5);
        text5 = (TextView) view.findViewById(R.id.text6);
        view1 = (TextView) view.findViewById(R.id.view1);
        view2 = (TextView) view.findViewById(R.id.view2);
        viewMore = (TextView) view.findViewById(R.id.view3);
        view3 = (TextView) view.findViewById(R.id.view4);
        view4 = (TextView) view.findViewById(R.id.view5);
        view5 = (TextView) view.findViewById(R.id.view6);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        iv_no_loc = view.findViewById(R.id.iv_no_loc);
        iv_no_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationSer();
            }
        });
        yincangListView();
        SharePreferenceUtils sharePreferenceUtils1 = new SharePreferenceUtils(getActivity(), StaticParament.USER);
        String userId1 = (String) sharePreferenceUtils1.get(StaticParament.USER_ID, "");
        Map<String, Object> maps1 = new HashMap<>();
        maps1.put("userId", userId1);
        getAppHomePageReminder(maps1); //首页的几个弹窗  pin码，更新，2然后弹窗提示更新协议（老用户），定位权限(App自己判断，不需要调用接口)，查询是否有逾期/还款失败订单，查询邀请，banner，首页数据，
//        ((MainTab) getActivity()).setOnGetGpsLoaction(new MainTab.OnGetGpsLoaction() {
//            @Override
//            public void getGps() {
//                ListenerManager.getInstance().sendBroadCast("getLocation");
//            }
//
//            @Override
//            public void notGetGps() {
//
//            }
//        });
//        ((MainTab) getActivity()).getNowLocation();

        //切换字体
        TypefaceUtil.replaceFont(closeText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(what, "fonts/gilroy_extraBold.ttf");
        TypefaceUtil.replaceFont(search, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(text1, "fonts/gilroy_extraBold.ttf");
        TypefaceUtil.replaceFont(text2, "fonts/gilroy_extraBold.ttf");
        TypefaceUtil.replaceFont(text3, "fonts/gilroy_extraBold.ttf");
        TypefaceUtil.replaceFont(text4, "fonts/gilroy_extraBold.ttf");
        TypefaceUtil.replaceFont(text5, "fonts/gilroy_extraBold.ttf");
        TypefaceUtil.replaceFont(textMore, "fonts/gilroy_extraBold.ttf");
        TypefaceUtil.replaceFont(view1, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(view2, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(view3, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(view4, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(view5, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(viewMore, "fonts/gilroy_medium.ttf");

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshLayout) {
                L.log("下拉刷新");
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //下拉刷新
                        refreshLayout.finishRefresh();
                        loadNotReadMessageCount();
                        getHomeBanner();
                        haveGpsFlag = false;
//                        ((MainTab) getActivity()).getNowLocation();
                        ((MainTab) getActivity()).setOnGetGpsLoaction(new MainTab.OnGetGpsLoaction() {//
                            @Override
                            public void getGps() {//更新当前位置
                                if (!haveGpsFlag) {
                                    haveGpsFlag = true;
                                    getHomeList();
                                }
                            }

                            @Override
                            public void notGetGps() {
                                getHomeList();
                            }
                        });
                        ((MainTab) getActivity()).getNowLocation();
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });

        loadNotReadMessageCount();
        search.setOnClickListener(this);
        setting.setOnClickListener(this);
        setting1.setOnClickListener(this);
        message.setOnClickListener(this);
        message1.setOnClickListener(this);
        view1.setOnClickListener(this);
        view2.setOnClickListener(this);
        view3.setOnClickListener(this);
        view4.setOnClickListener(this);
        view5.setOnClickListener(this);
        viewMore.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        list1.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        list2.setLayoutManager(linearLayoutManager1);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        moreList.setLayoutManager(linearLayoutManager2);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        list3.setLayoutManager(linearLayoutManager3);
        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        list4.setLayoutManager(linearLayoutManager4);
        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        list5.setLayoutManager(linearLayoutManager5);
        Log.d("qwwedfff", "4444: ");

        setData();
        setBannerData();
        if (!TextUtils.isEmpty(GetHomeMerchantList.getInstance().getMerchantList1().getDescription())) {
            text1.setText(GetHomeMerchantList.getInstance().getMerchantList1().getDescription());
            view1.setText("View all " + GetHomeMerchantList.getInstance().getMerchantList1().getCategoryName());
            categoryType1 = GetHomeMerchantList.getInstance().getMerchantList1().getCategoryType();
            categoryOrder1 = GetHomeMerchantList.getInstance().getMerchantList1().getDisplayOrder();
            text2.setText(GetHomeMerchantList.getInstance().getMerchantList2().getDescription());
            view2.setText("View all " + GetHomeMerchantList.getInstance().getMerchantList2().getCategoryName());
            categoryType2 = GetHomeMerchantList.getInstance().getMerchantList2().getCategoryType();
            categoryOrder2 = GetHomeMerchantList.getInstance().getMerchantList2().getDisplayOrder();
            text3.setText(GetHomeMerchantList.getInstance().getMerchantList3().getDescription());
            view3.setText("View all " + GetHomeMerchantList.getInstance().getMerchantList3().getCategoryName());
            categoryType3 = GetHomeMerchantList.getInstance().getMerchantList3().getCategoryType();
            categoryOrder3 = GetHomeMerchantList.getInstance().getMerchantList3().getDisplayOrder();
            text4.setText(GetHomeMerchantList.getInstance().getMerchantList4().getDescription());
            view4.setText("View all " + GetHomeMerchantList.getInstance().getMerchantList4().getCategoryName());
            categoryType4 = GetHomeMerchantList.getInstance().getMerchantList4().getCategoryType();
            categoryOrder4 = GetHomeMerchantList.getInstance().getMerchantList4().getDisplayOrder();
            text5.setText(GetHomeMerchantList.getInstance().getMerchantList5().getDescription());
            view5.setText("View all " + GetHomeMerchantList.getInstance().getMerchantList5().getCategoryName());
            categoryOrder5 = GetHomeMerchantList.getInstance().getMerchantList5().getDisplayOrder();
            categoryType5 = GetHomeMerchantList.getInstance().getMerchantList5().getCategoryType();
        }
        ((MainTab) getActivity()).checkNfc();

        //更新banner
        getHomeBanner();
     /*   //请求列表
        ((MainTab) getActivity()).setOnGetGpsLoaction(new MainTab.OnGetGpsLoaction() {//
            @Override
            public void getGps() {//更新当前位置
                if (!haveGpsFlag) {
                    haveGpsFlag = true;
                    getHomeList();
                }
            }

            @Override
            public void notGetGps() {
                getHomeList();
            }
        });
        ((MainTab) getActivity()).getNowLocation();*/
    }

    public void killAppProcess() {
        //注意：不能先杀掉主进程，否则逻辑代码无法继续执行，需先杀掉相关进程最后杀掉主进程
        ActivityManager mActivityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mList = mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : mList) {
            if (runningAppProcessInfo.pid != android.os.Process.myPid()) {
                android.os.Process.killProcess(runningAppProcessInfo.pid);
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        for (Activity activity : BaseActivity.activityList) {
            activity.finish();
        }
        getActivity().finish();
        MainTab.exit = true;
    }

    public boolean mFisrt = true;

    private void getAppHomePageReminder(Map<String, Object> maps) {

        RequestUtils requestUtils1 = new RequestUtils(getActivity(), maps, StaticParament.MainUrl,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(getActivity(), StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("查询PIN参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getAppHomePageReminder(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        try {
                            jsonObject = new JSONObject(dataStr);
                            if (jsonObject.getInt("pinState") == 1) { //是否需要设置pin码 ：0：不需要 1：需要
                                //没有设置PIN
                                Intent intent = new Intent(getActivity(), SetPinPwd_Activity.class);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                if (mFisrt) {

                                    //检查更新
                                    showView();
                                }
                                mFisrt = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        L.log("查询PIN:" + dataStr);
                    }

                    @Override
                    public void resError(String error) {


                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (createFlag) {
            createFlag = false;
            return;
        }
//        if (!isHidden()) {//从我的---》消息列表页--返回到我的 这儿不会执行。
        loadNotReadMessageCount();
        SharePreferenceUtils adSharePreferenceUtils = new SharePreferenceUtils(getContext(), StaticParament.DEVICE);
        //是否收藏商户，若有首页刷新的标识
        if (adSharePreferenceUtils.get(StaticParament.CHANGE_FAVORITE, "").equals("1")) {
            getHomeList();
            adSharePreferenceUtils.put(StaticParament.CHANGE_FAVORITE, "0");
            adSharePreferenceUtils.save();
        }
//        }
    }

    @Override
    public void onClick(View v) {
        if (!PublicTools.isFastClick()) {
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.edit:
                ((MainTab) getActivity()).changeTab(1);
                break;
            case R.id.setting:
            case R.id.setting1:
                Intent intentMsg = new Intent(getContext(), Setting_Activity.class);
                intentMsg.putExtra("from", "main");
                startActivity(intentMsg);
                locationQuanxian = false;

                break;
            case R.id.messageView:
            case R.id.messageView1:
                Intent intent_msg = new Intent(getContext(), MessageActivity.class);
                startActivity(intent_msg);
                locationQuanxian = false;

                break;
            case R.id.view3:
                if (GetHomeMerchantList.getInstance().getMoreList() != null && GetHomeMerchantList.getInstance().getMoreList().size() > 0) {
                    Intent intent = new Intent(getContext(), ViewMoreActivity.class);
                    intent.putExtra("title", textMore.getText().toString());
                    startActivity(intent);
                    locationQuanxian = false;

                }
                break;
            case R.id.view1:
                if (!TextUtils.isEmpty(text1.getText().toString()) && categoryType1 != null) {
                    Intent intent = new Intent(getContext(), ViewAllActivity.class);
                    intent.putExtra("order", categoryOrder1);
                    intent.putExtra("categories", categoryType1);
                    intent.putExtra("title", /*view1.getText().toString().substring(9)*/text1.getText().toString());
                    startActivity(intent);
                    locationQuanxian = false;

                }
                break;
            case R.id.view2:
                if (!TextUtils.isEmpty(text2.getText().toString()) && categoryType2 != null) {
                    Intent intent2 = new Intent(getContext(), ViewAllActivity.class);
                    intent2.putExtra("order", categoryOrder2);
                    intent2.putExtra("categories", categoryType2);
                    intent2.putExtra("title",  /*view2.getText().toString().substring(9)*/text2.getText().toString());
                    startActivity(intent2);
                    locationQuanxian = false;

                }
                break;
            case R.id.view4:
                if (!TextUtils.isEmpty(text3.getText().toString()) && categoryType3 != null) {
                    Intent intent3 = new Intent(getContext(), ViewAllActivity.class);
                    intent3.putExtra("title",  /*view3.getText().toString().substring(9)*/text3.getText().toString());
                    intent3.putExtra("order", categoryOrder3);
                    intent3.putExtra("categories", categoryType3);
                    startActivity(intent3);
                    locationQuanxian = false;

                }
                break;
            case R.id.view5:
                if (!TextUtils.isEmpty(text4.getText().toString()) && categoryType4 != null) {
                    Intent intent4 = new Intent(getContext(), ViewAllActivity.class);
                    intent4.putExtra("title", /*view4.getText().toString().substring(9)*/text4.getText().toString());
                    intent4.putExtra("order", categoryOrder4);
                    intent4.putExtra("categories", categoryType4);
                    startActivity(intent4);
                    locationQuanxian = false;

                }
                break;
            case R.id.view6:
                if (!TextUtils.isEmpty(text5.getText().toString()) && categoryType5 != null) {
                    Intent intent5 = new Intent(getContext(), ViewAllActivity.class);
                    intent5.putExtra("title", /*view5.getText().toString().substring(9)*/text5.getText().toString());
                    intent5.putExtra("order", categoryOrder5);
                    intent5.putExtra("categories", categoryType5);
                    startActivity(intent5);
                    locationQuanxian = false;

                }
                break;
        }
    }

    /**
     * 获取未读信息数量
     */
    private void loadNotReadMessageCount() {
        LoadNotReadMessage loadNotReadMessage = new LoadNotReadMessage(getContext());
        loadNotReadMessage.setOnNotRead(new LoadNotReadMessage.OnNotRead() {
            @Override
            public void notReadCount(int num) {
                if (num == 0) {
                    message.setImageResource(R.mipmap.no_message1);
                    message1.setImageResource(R.mipmap.no_message);
                } else {
                    message.setImageResource(R.mipmap.have_message1);
                    message1.setImageResource(R.mipmap.have_message);
                }
            }
        });
        loadNotReadMessage.getAllNoticeHasRead();
    }

    public void getChangedList() {  //首页的几个弹窗  查询是否有逾期/还款失败订单，查询邀请
        //查询是否有逾期
        //先弹还款失败、再逾期。取消弹窗再调营销码；若去钱包回来再调营销码。
        if (userListener == null) {
            userListener = new UserModel(getContext());
        }
        Map<String, Object> map = new HashMap<>();
        userListener.queryRepayInfo(map, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                final int overDueOrderCount = (Integer) o[0];
                int repayFailedOrderCount = (Integer) o[1];
                String repayFailedAmount = (String) o[2];
                if (repayFailedOrderCount > 0) {
                    TiShiDialogTwo tiShiDialogTwo = new TiShiDialogTwo();
                    tiShiDialogTwo.setOnCancel(new TiShiDialogTwo.OnCancel() {
                        @Override
                        public void cancel() {
                            if (overDueOrderCount > 0) {
                                //查询逾期
                                TiShiDialogTwo overDue = new TiShiDialogTwo();
                                overDue.setOnShure(new TiShiDialogTwo.OnShure() {
                                    @Override
                                    public void shure() {
                                        checkReceiverFlag = true;
                                        ((MainTab) getActivity()).changeTab(2);
                                    }
                                });
                                overDue.setOnCancel(new TiShiDialogTwo.OnCancel() {
                                    @Override
                                    public void cancel() {
                                        //判断邀请人
                                        checkReceiver();
                                    }
                                });
                                overDue.show(getContext(), "You have an overdue payment.",
                                        "Please repay now to keep using Payo.",
                                        "Not now", "Repay now", "");
                            }
                        }
                    });
                    tiShiDialogTwo.setOnShure(new TiShiDialogTwo.OnShure() {
                        @Override
                        public void shure() {
                            checkReceiverFlag = true;
                            WalletFragment.exe = true;
                            ((MainTab) getActivity()).changeTab(2);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ((MainTab) getActivity()).change();//展示列表数据
                                }
                            }, 700);
                        }
                    });
                    Spanned failedMsg = Html.fromHtml("Your direct debit repayment of <font color='#FD7441'>" + "$" + PublicTools.twoend(repayFailedAmount) + "</font>" + " was unsuccessful. Please organise payment within the next 24 hours to avoid any late fees and to allow you to continue using payo.");
                    tiShiDialogTwo.show(getContext(), "Payment unsuccessful", failedMsg,
                            "Not now", "Try again", "");
                } else {

                    if (overDueOrderCount > 0) {
                        //查询逾期
                        TiShiDialogTwo overDue = new TiShiDialogTwo();
                        overDue.setOnShure(new TiShiDialogTwo.OnShure() {
                            @Override
                            public void shure() {
                                checkReceiverFlag = true;
                                ((MainTab) getActivity()).changeTab(2);
                            }
                        });
                        overDue.setOnCancel(new TiShiDialogTwo.OnCancel() {
                            @Override
                            public void cancel() {
                                //判断邀请人
                                checkReceiver();
                            }
                        });
                        overDue.show(getContext(), "You have an overdue payment.",
                                "Please repay now to keep using Payo.",
                                "Not now", "Repay now", "");
                    } else {
                        checkReceiver();
                    }
                }
            }

            @Override
            public void onError(String e) {

            }
        });

    }

    //判断邀请人弹窗
    private void checkReceiver() {
        final Map<String, Object> map1 = new HashMap<>();
        if (userListener == null) {
            userListener = new UserModel(getContext());
        }
        userListener.getReceived(map1, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                String money = (String) o[0];
                String info = (String) o[1];
                if (!money.equals("0")) {
                    HomeReceiveDialog receiveDialog = new HomeReceiveDialog();
                    receiveDialog.setOnShure(new HomeReceiveDialog.OnShure() {
                        @Override
                        public void shure() {
                            userListener.saveReceivedIsShow(map1);
                            Intent intent = new Intent(getContext(), ShareToEarnActivity.class);
                            startActivity(intent);
                            locationQuanxian = false;

                        }

                        @Override
                        public void close() {
                            userListener.saveReceivedIsShow(map1);
                        }
                    });
                    receiveDialog.show(getContext(), money, info);
                }
            }

            @Override
            public void onError(String e) {

            }
        });
    }

    private void getHomeBanner() {
        //更新banner
        Map<String, Object> mapBanner = new HashMap<>();
        mapBanner.put("timestamp", GetHomeMerchantList.getInstance().getBannerTimestamp());
        mapBanner.put("bannerId", GetHomeMerchantList.getInstance().getBannerId());
        GetHomeMerchantList.getInstance().getAppHomePageTopBanner(getContext(), mapBanner, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                String update = (String) o[0];
                if (update.equals("true")) {
                    setBannerData();
                }
            }

            @Override
            public void onError(String e) {

            }
        });
    }

    public void getHomeList() {
        //更新位置，请求列表
        SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(getContext(), StaticParament.APP);
        Map<String, Object> maps = new HashMap<>();
        if (TextUtils.isEmpty((String) appMsgSharePreferenceUtils.get(StaticParament.LAT, "")) || TextUtils.isEmpty((String) appMsgSharePreferenceUtils.get(StaticParament.LNG, ""))) {
            return;
        }
        maps.put("latitude", appMsgSharePreferenceUtils.get(StaticParament.LAT, ""));
        maps.put("longitude", appMsgSharePreferenceUtils.get(StaticParament.LNG, ""));
        maps.put("category1Timestamp", GetHomeMerchantList.getInstance().getCategory1Timestamp());
        maps.put("category2Timestamp", GetHomeMerchantList.getInstance().getCategory2Timestamp());
        maps.put("category3Timestamp", GetHomeMerchantList.getInstance().getCategory3Timestamp());
        maps.put("category4Timestamp", GetHomeMerchantList.getInstance().getCategory4Timestamp());
        maps.put("category5Timestamp", GetHomeMerchantList.getInstance().getCategory5Timestamp());
        maps.put("exclusiveTimestamp", GetHomeMerchantList.getInstance().getExclusiveTimestamp());

        GetHomeMerchantList.getInstance().getAppHomePageBottomData(getContext(), maps, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                List<String> changed = (List<String>) o[0];
                if (changed.size() > 0) {
                    for (int i = 0; i < changed.size(); i++) {
                        Log.d("qwwedfff", "33333: ");

                        changeList(changed.get(i));
                    }
                }
            }

            @Override
            public void onError(String e) {
            }
        });
    }

    public static boolean isOpenLocService(final Context context) {
        boolean isGps = false; //推断GPS定位是否启动

        boolean isNetwork = false; //推断网络定位是否启动

        if (context != null) {
            LocationManager locationManager

                    = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if (locationManager != null) {
//通过GPS卫星定位，定位级别能够精确到街(通过24颗卫星定位，在室外和空旷的地方定位准确、速度快)

                isGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

//通过WLAN或移动网络(3G/2G)确定的位置(也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物(建筑群或茂密的深林等)密集的地方定位)

                isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            }

            if (isGps || isNetwork) {
                return true;
            }
        }
        return false;
    }

    public boolean jumpToSetting = false;

    public void gotoLocServiceSettings() {
        if (isAdded()) {
            jumpToSetting = true;
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, 100);
        }
    }

    private TiShiDialog dialog;
    public boolean jump = true;//控制只有点击 pay with payo按钮时才判断是否开启定位以及跳转到输入金额页；进入界面 或者返回改页面不开启定位以及跳转到输入金额页

    public void yincangListView() {
        text1.setVisibility(View.GONE);
        text2.setVisibility(View.GONE);
        text3.setVisibility(View.GONE);
        text4.setVisibility(View.GONE);
        text5.setVisibility(View.GONE);
        textMore.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        view3.setVisibility(View.GONE);
        view4.setVisibility(View.GONE);
        view5.setVisibility(View.GONE);
        viewMore.setVisibility(View.GONE);
        list1.setVisibility(View.GONE);
        list2.setVisibility(View.GONE);
        list3.setVisibility(View.GONE);
        list4.setVisibility(View.GONE);
        list5.setVisibility(View.GONE);
        moreList.setVisibility(View.GONE);
        iv_no_loc.setVisibility(View.VISIBLE);
    }

    public void xianshiListView() {
        text1.setVisibility(View.VISIBLE);
        text2.setVisibility(View.VISIBLE);
        text3.setVisibility(View.VISIBLE);
        text4.setVisibility(View.VISIBLE);
        text5.setVisibility(View.VISIBLE);
        textMore.setVisibility(View.VISIBLE);
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.VISIBLE);
        view3.setVisibility(View.VISIBLE);
        view4.setVisibility(View.VISIBLE);
        view5.setVisibility(View.VISIBLE);
        viewMore.setVisibility(View.VISIBLE);
        list1.setVisibility(View.VISIBLE);
        list2.setVisibility(View.VISIBLE);
        list3.setVisibility(View.VISIBLE);
        list4.setVisibility(View.VISIBLE);
        list5.setVisibility(View.VISIBLE);
        moreList.setVisibility(View.VISIBLE);
        iv_no_loc.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
//        jump = false;
        locationQuanxian = false;
        super.onPause();
    }

    public void aaa() {
        if (isOpenLocService(getActivity()) && checkLocationPermission()) {
            xianshiListView();
            getHomeList();
        }
    }

    public static boolean locationQuanxian = false;

    public void locationSer() {
        if (!isOpenLocService(getActivity()) && jump) {
            yincangListView();

            gotoLocServiceSettings();
        } else if (!checkLocationPermission() && jump) {
            Log.d("xzcxzvcxzc", "locationSer: 1000");
            yincangListView();
            if (dialog == null) {
                dialog = new TiShiDialog(getActivity());
                dialog.setOnGuanBiLeft(new TiShiDialog.GuanBiLeft() {//左边按钮
                    @Override
                    public void GuanBiLeft() {
                        dialog = null;
                        yincangListView();
                        getChangedList();
                    }
                });
                dialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                    @Override
                    public void GuanBi() {
                        yincangListView();
                        locationQuanxian = true;
                        ((MainTab) getActivity()).exe(false);//点击cancel 不在弹出提示开启权限的弹窗了。
                        ((MainTab) getActivity()).setGetLocaion(true);
                        ((MainTab) getActivity()).onResume();
                        dialog = null;
                    }
                });
                dialog.ShowDialog("Allow Location Access?", "To view nearby venues in your city to pay with this app, you must allow “Payo” to access your location.", "Allow");
            }
        } else if (jump) {//正常跳转
            xianshiListView();

            ((MainTab) getActivity()).setOnGetGpsLoaction(new MainTab.OnGetGpsLoaction() {
                @Override
                public void getGps() {
                    if (!haveGpsFlag) {
                        haveGpsFlag = true;
                        getChangedList();//首页的几个弹窗
                    }
                }

                @Override
                public void notGetGps() {
                    getChangedList();//首页的几个弹窗
                }
            });
            ((MainTab) getActivity()).getNowLocation();

        }
    }

    private void setBannerData() {
        final HomeBannerData bannerData = GetHomeMerchantList.getInstance().getHomeBannerData();
        //banner图
        if (bannerData != null && bannerData.getBase64Img() != null) {
            if (GetHomeMerchantList.getInstance().getHomeBannerData().getBase64Img() != null) {
                byte[] decode = Base64.decode(GetHomeMerchantList.getInstance().getHomeBannerData().getBase64Img()/*.split(",")[1]*/, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                banner.setImageBitmap(bitmap);
            } else {
                return;
            }
            bannerCl.setVisibility(View.VISIBLE);
            payoCl.setVisibility(View.GONE);

            banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!PublicTools.isFastClick()) {
                        return;
                    }
                    if (bannerData.getRedirectType().equals("1")) {
                        Intent intent = new Intent(getContext(), Web_Activity.class);
                        intent.putExtra("url", GetHomeMerchantList.getInstance().getHomeBannerData().getRedirectH5LinkAddress());
                        startActivity(intent);
                        locationQuanxian = false;

                    } else if (bannerData.getRedirectType().equals("2")) {
                        if (bannerData.getRedirectAppLinkType().equals("1")) {
                            Intent intent = new Intent(getContext(), ShareToEarnActivity.class);
                            startActivity(intent);
                            locationQuanxian = false;

                        } else if (bannerData.getRedirectAppLinkType().equals("2")) {
                            Intent intent = new Intent(getContext(), AboutActivity.class);
                            intent.putExtra("formMain", "1");
                            startActivity(intent);
                            locationQuanxian = false;

                        } else if (bannerData.getRedirectAppLinkType().equals("3")) {
                            SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(getActivity(), StaticParament.USER);
                            String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
                            Map<String, Object> maps = new HashMap<>();
                            maps.put("userId", userId);
                            maps.put("stripeState", 0);//用户区分接入stripe前后的新老用户
                            jiaoyanFenqifu(maps);
                            locationQuanxian = false;

//                            openInvestPresent.loadOpenInvestStatus("sm");
                        } else if (bannerData.getRedirectAppLinkType().equals("4")) {
                            Intent intent = new Intent(getContext(), MapActivity.class);
                            startActivity(intent);
                            locationQuanxian = false;

                        }
                    } else if (bannerData.getRedirectType().equals("4")) {
                        //自定义弹窗
                        //跳转自定义页面展示方式   redirectCustomizedDisplayType   1Full screen  2  Semi Popup
                        if (bannerData.getRedirectCustomizedDisplayType().equals("2")) {
                            BannerClickDialog bannerClickDialog = new BannerClickDialog(getContext(),
                                    bannerData.getRedirectCustomizedTitle(),
                                    bannerData.getRedirectCustomizedContent(),
                                    bannerData.getRedirectCustomizedImageUrl()
                            );
                            bannerClickDialog.show();
                        } else if (bannerData.getRedirectCustomizedDisplayType().equals("1")) {
                            Intent intent = new Intent(getContext(), BannerClickActivity.class);
                            intent.putExtra("title", bannerData.getRedirectCustomizedTitle());
                            intent.putExtra("text", bannerData.getRedirectCustomizedContent());
                            intent.putExtra("url", bannerData.getRedirectCustomizedImageUrl());
                            startActivity(intent);
                            locationQuanxian = false;
                        }
                    }
                }
            });

            //关闭条
            if (GetHomeMerchantList.getInstance().getHomeBannerData().getTurnOffEffectStatus().equals("1")) {
                closeText.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bannerCl.setVisibility(View.GONE);
                        payoCl.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                closeText.setVisibility(View.GONE);
                close.setVisibility(View.GONE);
            }
            closeText.setText(GetHomeMerchantList.getInstance().getHomeBannerData().getTurnOffTextDisplay());
            closeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    TCDialog tcDialog = new TCDialog(getActivity());
//                    tcDialog.setOnGuanBi(new TCDialog.GuanBi() {
//                        @Override
//                        public void GuanBi() {
//                            showToumingTuceng();
////                        killAppProcess();
//                        }
//
//                        @Override
//                        public void queding() {
//                            SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(getActivity(), StaticParament.USER);
//                            String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
//                            final Map<String, Object> maps = new HashMap<>();
//                            maps.put("userId", userId);
//                            updateUserAgreementState(maps);
//                        }
//                    });
//                    tcDialog.ShowDialog("");
//
////
//////                    Intent intent_kyc = new Intent(getActivity(), ChooseCardActivity.class);
//////                    startActivity(intent_kyc);
//                    TiShiDialog tiShiDialog = new TiShiDialog(getActivity());
//                    tiShiDialog.showStripeDialog();
//                    tiShiDialog.setOnGuanBiLeft(new TiShiDialog.GuanBiLeft() {
//                        @Override
//                        public void GuanBiLeft() {
//                            locationSer();
//                        }
//                    });
//                    tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
//                        @Override
//                        public void GuanBi() {//去绑卡
//
//                        }
//                    });
//                    if (true) {
//                        return;
//                    }
                    if (!PublicTools.isFastClick()) {
                        return;
                    }
                    if (bannerData.getTurnOffRedirectType().equals("1")) {
                        Intent intent = new Intent(getContext(), Web_Activity.class);
                        intent.putExtra("url", GetHomeMerchantList.getInstance().getHomeBannerData().getTurnOffRedirectH5Link());
                        startActivity(intent);
                        locationQuanxian = false;

                    } else if (bannerData.getTurnOffRedirectType().equals("2")) {
                        if (bannerData.getTurnOffRedirectAppLinkType().equals("1")) {
                            Intent intent = new Intent(getContext(), ShareToEarnActivity.class);
                            startActivity(intent);
                            locationQuanxian = false;

                        } else if (bannerData.getTurnOffRedirectAppLinkType().equals("2")) {
                            Intent intent = new Intent(getContext(), AboutActivity.class);
                            intent.putExtra("formMain", "1");
                            startActivity(intent);
                            locationQuanxian = false;

                        } else if (bannerData.getTurnOffRedirectAppLinkType().equals("3")) {
                            SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(getActivity(), StaticParament.USER);
                            String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
                            Map<String, Object> maps = new HashMap<>();
                            maps.put("userId", userId);
                            maps.put("stripeState", 0);//用户区分接入stripe前后的新老用户

                            jiaoyanFenqifu(maps);
//                            openInvestPresent.loadOpenInvestStatus("sm");
                        } else if (bannerData.getTurnOffRedirectAppLinkType().equals("4")) {
                            Intent intent = new Intent(getContext(), MapActivity.class);
                            startActivity(intent);
                            locationQuanxian = false;

                        }
                    }
                }
            });
        } else {
            banner.setImageResource(R.mipmap.first_banner_default);
            banner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!PublicTools.isFastClick()) {
                        return;
                    }
                    Intent intent = new Intent(getContext(), AboutActivity.class);
                    intent.putExtra("formMain", "1");
                    startActivity(intent);
                    locationQuanxian = false;

                }
            });
        }
    }

    private void updateUserAgreementState(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(getActivity(), maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {
                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.updateUserAgreementState(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {//从jsonObject中取stripeState字段 确定是否弹stripe弹窗
                Log.d("xzcxzvcxzc", "locationSer: 1");

                //是否弹出stripe弹窗 0：否 1：是
                try {
                    if (jsonObject.has("stripeState")) {
                        if (jsonObject.getString("stripeState").equals("0")) {
                            locationSer();
                        } else if (jsonObject.getString("stripeState").equals("1")) {
                            TiShiDialog tiShiDialog = new TiShiDialog(getActivity());
                            tiShiDialog.showStripeDialog();
                            tiShiDialog.setOnGuanBiLeft(new TiShiDialog.GuanBiLeft() {
                                @Override
                                public void GuanBiLeft() {
                                    locationSer();
                                }
                            });
                            tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                                @Override
                                public void GuanBi() {//去绑卡
                                    locationSer();
                                }
                            });
                        }
                    } else {
                        locationSer();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void resError(String error) {

            }
        });
    }

    private boolean H5 = false;

    /**
     * 分期付开通状态
     *
     * @param maps
     */
    private void jiaoyanFenqifu(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(getActivity(), maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {
                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.jiaoyanFenqifu(headerMap, requestBody);
                requestUtils.Call(call, null, false);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                SharePreferenceUtils appMsgSharePreferenceUtils =
                        new SharePreferenceUtils(getActivity(), StaticParament.DEVICE);
                String callPhone = (String) appMsgSharePreferenceUtils.get(StaticParament.APP_PHONE, "");

                Gson gson = new Gson();
                FenqifuStatue fenqifuStatue = gson.fromJson(dataStr, FenqifuStatue.class);
                SelectPayTypeActivity.shifoukaitongCardPay = fenqifuStatue.getCardState();//0：未开通开支付  1:开通开卡支付
                SelectPayTypeActivity.meikaitongfenqifuAndkaitongCardPay = fenqifuStatue.getCreditCardAgreementState();
                backStatue = 1;

                if (fenqifuStatue.getInstallmentState() == 2 && fenqifuStatue.getCreditCardState() == 0) {
                    H5 = true;
                } else {
                    H5 = false;
                }

                if (fenqifuStatue.getKycState() == 0) {//未开通kyc
                    Intent intent_register = new Intent(getActivity(), RegistTwo_Activity.class); //跳到注册页 从注册页跳到kyc页
                    intent_register.putExtra("jumpToKyc", true);
                    intent_register.putExtra("zhijiefinish", true);
                    startActivity(intent_register);
                    locationQuanxian = false;
                } else {//已经开通kyc
                    if (fenqifuStatue.getCardState() == 0) {//未开通卡支付
                        Intent intent_kyc = new Intent(getActivity(), NewAddBankCardActivity.class);
                        if (fenqifuStatue.getCreditCardAgreementState() == 1 && fenqifuStatue.getInstallmentState() == 2) {//分期付删卡至没卡 需要调到不带协议的页面绑卡
                            intent_kyc.putExtra("backToMain", true);

                        } else {

                            if (H5) {
                                intent_kyc.putExtra("H5", H5);
                            } else {
                                intent_kyc.putExtra("kaitongfenqifuRoad", true);
                            }
                            locationQuanxian = false;
                        }
                        intent_kyc.putExtra("fromFirstFragment", true);
                        startActivity(intent_kyc);
                    } else {//已经开通卡支付 //判断illion状态
                        if (fenqifuStatue.getCreditCardAgreementState() == 0) {//是否勾选过分期付绑卡协议 0：未勾选 1：已勾选
                            Intent intent_kyc = new Intent(getActivity(), ChooseCardActivity.class);
                            startActivity(intent_kyc);
                        } else {
                            if (fenqifuStatue.getInstallmentState() == 2) {//开通illion 开通了分期付
                                ((MainTab) getActivity()).changeTab(2);
                            } else if (fenqifuStatue.getInstallmentState() == 0 || fenqifuStatue.getInstallmentState() == 3) {//未开通！ 0. 用户未开通分期付(需要完善信息) 3. illion未授权
                                Intent intent_kyc = new Intent(getActivity(), ChooseIllionActivity.class);
                                startActivity(intent_kyc);
                                locationQuanxian = false;

                            } else if (fenqifuStatue.getInstallmentState() == 1 || fenqifuStatue.getInstallmentState() == 5 || fenqifuStatue.getInstallmentState() == 8) {//—-失败！—— 1. 用户分期付已冻结 5. 用户分期付禁用 8 机审拒绝
                                Intent intent_kyc = new Intent(getActivity(), KycAndIllionResultActivity.class);
                                intent_kyc.putExtra("error", "FkReject");
                                intent_kyc.putExtra("phone", callPhone);
                                startActivity(intent_kyc);
                                locationQuanxian = false;

                            } else if (fenqifuStatue.getInstallmentState() == 4 || fenqifuStatue.getInstallmentState() == 7 || fenqifuStatue.getInstallmentState() == 9) {//—-等待！——4. 等待人工审核 7. 机审中 9 分期付风控未触发
                                Intent intent_kyc = new Intent(getActivity(), KycAndIllionResultActivity.class);
                                intent_kyc.putExtra("error", "Waiting");
                                intent_kyc.putExtra("phone", callPhone);
                                startActivity(intent_kyc);
                                locationQuanxian = false;

                            }
                        }
                    }
                }
                Log.d("xunzhic", "resData: " + dataStr);
            }

            @Override
            public void resError(String error) {

            }
        });
    }

    private void setData() {
        if (GetHomeMerchantList.getInstance().getMoreList() != null) {
            //市场推广
            moreList.setFocusableInTouchMode(false);
            moreList.setFocusable(false);
            if (marketAdapter != null) {
                marketAdapter.notifyData(GetHomeMerchantList.getInstance().getMoreList());
            } else {
                marketAdapter = new HomeMarketAdapter(getContext(), GetHomeMerchantList.getInstance().getMoreList());
                marketAdapter.setOnitemClick(new HomeMarketAdapter.OnitemClick() {
                    @Override
                    public void itemClick(HomeMerchantListData.ExclusiveBean.ListBean bean) {
                        if (!PublicTools.isFastClick()) {
                            return;
                        }
                        if (bean.getRedirectType().equals("1")) {
                            Intent intent = new Intent(getContext(), Web_Activity.class);
                            intent.putExtra("url", bean.getRedirectH5LinkAddress());
                            startActivity(intent);
                        } else if (bean.getRedirectType().equals("2")) {
                            if (bean.getRedirectAppLinkType().equals("1")) {
                                Intent intent = new Intent(getContext(), ShareToEarnActivity.class);
                                startActivity(intent);
                            } else if (bean.getRedirectAppLinkType().equals("2")) {
                                Intent intent = new Intent(getContext(), AboutActivity.class);
                                intent.putExtra("formMain", "1");
                                startActivity(intent);
                                locationQuanxian = false;

                            } else if (bean.getRedirectAppLinkType().equals("3")) {
                                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(getActivity(), StaticParament.USER);
                                String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
                                Map<String, Object> maps = new HashMap<>();
                                maps.put("userId", userId);
                                maps.put("stripeState", 0);//用户区分接入stripe前后的新老用户

                                jiaoyanFenqifu(maps);
                                Log.d("qwwedfff", "11111: ");

//                                openInvestPresent.loadOpenInvestStatus("sm");
                            } else if (bean.getRedirectAppLinkType().equals("4")) {
                                Intent intent = new Intent(getContext(), MapActivity.class);
                                startActivity(intent);
                                locationQuanxian = false;

                            }
                        } else if (bean.getRedirectType().equals("4")) {
                            //自定义弹窗
                            //跳转自定义页面展示方式   redirectCustomizedDisplayType   1Full screen  2  Semi Popup
                            if (bean.getRedirectCustomizedDisplayType() != null) {
                                if (bean.getRedirectCustomizedDisplayType().equals("2")) {
                                    BannerClickDialog bannerClickDialog = new BannerClickDialog(getContext(),
                                            bean.getRedirectCustomizedTitle(),
                                            bean.getRedirectCustomizedContent(),
                                            bean.getRedirectCustomizedImageUrl()
                                    );
                                    bannerClickDialog.show();
                                } else if (bean.getRedirectCustomizedDisplayType().equals("1")) {
                                    Intent intent = new Intent(getContext(), BannerClickActivity.class);
                                    intent.putExtra("title", bean.getRedirectCustomizedTitle());
                                    intent.putExtra("text", bean.getRedirectCustomizedContent());
                                    intent.putExtra("url", bean.getRedirectCustomizedImageUrl());
                                    startActivity(intent);
                                    locationQuanxian = false;

                                }
                            }
                        }
                    }
                });
                moreList.setAdapter(marketAdapter);
            }
        }
        if (GetHomeMerchantList.getInstance().getMerchantList1().getMerchants() != null) {
            list1.setFocusableInTouchMode(false);
            list1.setFocusable(false);
            if (adapter1 != null) {
                adapter1.notifyData(GetHomeMerchantList.getInstance().getMerchantList1().getMerchants());
            } else {
                adapter1 = new HomeMerchantAdapter(getContext(), GetHomeMerchantList.getInstance().getMerchantList1().getMerchants());
                adapter1.setOnitemClick(new HomeMerchantAdapter.OnitemClick() {
                    @Override
                    public void itemClick(String id) {
                        if (!PublicTools.isFastClick()) {
                            return;
                        }
                        Intent intent = new Intent(getContext(), NewMerchantInfoActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        locationQuanxian = false;
                    }

                    @Override
                    public void changeFavorite() {
                        getHomeList();
                    }
                });
                list1.setAdapter(adapter1);
            }
        }

        if (GetHomeMerchantList.getInstance().getMerchantList2().getMerchants() != null) {
            list2.setFocusableInTouchMode(false);
            list2.setFocusable(false);
            if (adapter2 != null) {
                adapter2.notifyData(GetHomeMerchantList.getInstance().getMerchantList2().getMerchants());
            } else {
                adapter2 = new HomeMerchantAdapter(getContext(), GetHomeMerchantList.getInstance().getMerchantList2().getMerchants());
                adapter2.setOnitemClick(new HomeMerchantAdapter.OnitemClick() {
                    @Override
                    public void itemClick(String id) {
                        if (!PublicTools.isFastClick()) {
                            return;
                        }
                        Intent intent = new Intent(getContext(), NewMerchantInfoActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        locationQuanxian = false;
                    }

                    @Override
                    public void changeFavorite() {
                        getHomeList();
                    }
                });
                list2.setAdapter(adapter2);
            }
        }

        if (GetHomeMerchantList.getInstance().getMerchantList3().getMerchants() != null) {
            list3.setFocusableInTouchMode(false);
            list3.setFocusable(false);
            if (adapter3 != null) {
                adapter3.notifyData(GetHomeMerchantList.getInstance().getMerchantList3().getMerchants());
            } else {
                adapter3 = new HomeMerchantAdapter(getContext(), GetHomeMerchantList.getInstance().getMerchantList3().getMerchants());
                adapter3.setOnitemClick(new HomeMerchantAdapter.OnitemClick() {
                    @Override
                    public void itemClick(String id) {
                        if (!PublicTools.isFastClick()) {
                            return;
                        }
                        Intent intent = new Intent(getContext(), NewMerchantInfoActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        locationQuanxian = false;
                    }

                    @Override
                    public void changeFavorite() {
                        getHomeList();
                    }
                });
                list3.setAdapter(adapter3);
            }
        }

        if (GetHomeMerchantList.getInstance().getMerchantList4().getMerchants() != null) {
            list4.setFocusableInTouchMode(false);
            list4.setFocusable(false);
            if (adapter4 != null) {
                adapter4.notifyData(GetHomeMerchantList.getInstance().getMerchantList4().getMerchants());
            } else {
                adapter4 = new HomeMerchantAdapter(getContext(), GetHomeMerchantList.getInstance().getMerchantList4().getMerchants());
                adapter4.setOnitemClick(new HomeMerchantAdapter.OnitemClick() {
                    @Override
                    public void itemClick(String id) {
                        if (!PublicTools.isFastClick()) {
                            return;
                        }
                        Intent intent = new Intent(getContext(), NewMerchantInfoActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }

                    @Override
                    public void changeFavorite() {
                        getHomeList();
                    }
                });
                list4.setAdapter(adapter4);
            }
        }

        if (GetHomeMerchantList.getInstance().getMerchantList5().getMerchants() != null) {
            list5.setFocusableInTouchMode(false);
            list5.setFocusable(false);
            if (adapter5 != null) {
                adapter5.notifyData(GetHomeMerchantList.getInstance().getMerchantList5().getMerchants());
            } else {
                adapter5 = new HomeMerchantAdapter(getContext(), GetHomeMerchantList.getInstance().getMerchantList5().getMerchants());
                adapter5.setOnitemClick(new HomeMerchantAdapter.OnitemClick() {
                    @Override
                    public void itemClick(String id) {
                        if (!PublicTools.isFastClick()) {
                            return;
                        }
                        Intent intent = new Intent(getContext(), NewMerchantInfoActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        locationQuanxian = false;
                    }

                    @Override
                    public void changeFavorite() {
                        getHomeList();
                    }
                });
                list5.setAdapter(adapter5);
            }
        }
    }

    private void changeList(String index) {

        if (index.equals("1")) {
            text1.setText(GetHomeMerchantList.getInstance().getMerchantList1().getDescription());
            view1.setText("View all " + GetHomeMerchantList.getInstance().getMerchantList1().getCategoryName());
            categoryType1 = GetHomeMerchantList.getInstance().getMerchantList1().getCategoryType();
            categoryOrder1 = GetHomeMerchantList.getInstance().getMerchantList1().getDisplayOrder();
            if (GetHomeMerchantList.getInstance().getMerchantList1().getMerchants() != null) {
                if (isOpenLocService(getActivity()) && checkLocationPermission()) {
                    xianshiListView();
                } else {
                    yincangListView();
                }

                list1.setFocusableInTouchMode(false);
                list1.setFocusable(false);
                if (adapter1 != null) {
                    adapter1.notifyData(GetHomeMerchantList.getInstance().getMerchantList1().getMerchants());
                } else {
                    adapter1 = new HomeMerchantAdapter(getContext(), GetHomeMerchantList.getInstance().getMerchantList1().getMerchants());
                    adapter1.setOnitemClick(new HomeMerchantAdapter.OnitemClick() {
                        @Override
                        public void itemClick(String id) {
                            if (!PublicTools.isFastClick()) {
                                return;
                            }
                            Intent intent = new Intent(getContext(), NewMerchantInfoActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                            locationQuanxian = false;
                        }

                        @Override
                        public void changeFavorite() {
                            getHomeList();
                        }
                    });
                    list1.setAdapter(adapter1);
                }
            }
        } else if (index.equals("2")) {
            text2.setText(GetHomeMerchantList.getInstance().getMerchantList2().getDescription());
            view2.setText("View all " + GetHomeMerchantList.getInstance().getMerchantList2().getCategoryName());
            categoryType2 = GetHomeMerchantList.getInstance().getMerchantList2().getCategoryType();
            categoryOrder2 = GetHomeMerchantList.getInstance().getMerchantList2().getDisplayOrder();
            if (GetHomeMerchantList.getInstance().getMerchantList2().getMerchants() != null) {
                list2.setFocusableInTouchMode(false);
                list2.setFocusable(false);
                if (adapter2 != null) {
                    adapter2.notifyData(GetHomeMerchantList.getInstance().getMerchantList2().getMerchants());
                } else {
                    adapter2 = new HomeMerchantAdapter(getContext(), GetHomeMerchantList.getInstance().getMerchantList2().getMerchants());
                    adapter2.setOnitemClick(new HomeMerchantAdapter.OnitemClick() {
                        @Override
                        public void itemClick(String id) {
                            if (!PublicTools.isFastClick()) {
                                return;
                            }
                            Intent intent = new Intent(getContext(), NewMerchantInfoActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                            locationQuanxian = false;
                        }

                        @Override
                        public void changeFavorite() {
                            getHomeList();
                        }
                    });
                    list2.setAdapter(adapter2);
                }
            }
        } else if (index.equals("3")) {
            text3.setText(GetHomeMerchantList.getInstance().getMerchantList3().getDescription());
            view3.setText("View all " + GetHomeMerchantList.getInstance().getMerchantList3().getCategoryName());
            categoryType3 = GetHomeMerchantList.getInstance().getMerchantList3().getCategoryType();
            categoryOrder3 = GetHomeMerchantList.getInstance().getMerchantList3().getDisplayOrder();
            if (GetHomeMerchantList.getInstance().getMerchantList3().getMerchants() != null) {
                list3.setFocusableInTouchMode(false);
                list3.setFocusable(false);
                if (adapter3 != null) {
                    adapter3.notifyData(GetHomeMerchantList.getInstance().getMerchantList3().getMerchants());
                } else {
                    adapter3 = new HomeMerchantAdapter(getContext(), GetHomeMerchantList.getInstance().getMerchantList3().getMerchants());
                    adapter3.setOnitemClick(new HomeMerchantAdapter.OnitemClick() {
                        @Override
                        public void itemClick(String id) {
                            if (!PublicTools.isFastClick()) {
                                return;
                            }
                            Intent intent = new Intent(getContext(), NewMerchantInfoActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                            locationQuanxian = false;
                        }

                        @Override
                        public void changeFavorite() {
                            getHomeList();
                        }
                    });
                    list3.setAdapter(adapter3);
                }
            }
        } else if (index.equals("4")) {
            text4.setText(GetHomeMerchantList.getInstance().getMerchantList4().getDescription());
            view4.setText("View all " + GetHomeMerchantList.getInstance().getMerchantList4().getCategoryName());
            categoryType4 = GetHomeMerchantList.getInstance().getMerchantList4().getCategoryType();
            categoryOrder4 = GetHomeMerchantList.getInstance().getMerchantList4().getDisplayOrder();
            if (GetHomeMerchantList.getInstance().getMerchantList4().getMerchants() != null) {
                list4.setFocusableInTouchMode(false);
                list4.setFocusable(false);
                if (adapter4 != null) {
                    adapter4.notifyData(GetHomeMerchantList.getInstance().getMerchantList4().getMerchants());
                } else {
                    adapter4 = new HomeMerchantAdapter(getContext(), GetHomeMerchantList.getInstance().getMerchantList4().getMerchants());
                    adapter4.setOnitemClick(new HomeMerchantAdapter.OnitemClick() {
                        @Override
                        public void itemClick(String id) {
                            if (!PublicTools.isFastClick()) {
                                return;
                            }
                            Intent intent = new Intent(getContext(), NewMerchantInfoActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                            locationQuanxian = false;
                        }

                        @Override
                        public void changeFavorite() {
                            getHomeList();
                        }
                    });
                    list4.setAdapter(adapter4);
                }
            }
        } else if (index.equals("5")) {
            text5.setText(GetHomeMerchantList.getInstance().getMerchantList5().getDescription());
            view5.setText("View all " + GetHomeMerchantList.getInstance().getMerchantList5().getCategoryName());
            categoryType5 = GetHomeMerchantList.getInstance().getMerchantList5().getCategoryType();
            categoryOrder5 = GetHomeMerchantList.getInstance().getMerchantList5().getDisplayOrder();
            if (GetHomeMerchantList.getInstance().getMerchantList5().getMerchants() != null) {
                list5.setFocusableInTouchMode(false);
                list5.setFocusable(false);
                if (adapter5 != null) {
                    adapter5.notifyData(GetHomeMerchantList.getInstance().getMerchantList5().getMerchants());
                } else {
                    adapter5 = new HomeMerchantAdapter(getContext(), GetHomeMerchantList.getInstance().getMerchantList5().getMerchants());
                    adapter5.setOnitemClick(new HomeMerchantAdapter.OnitemClick() {
                        @Override
                        public void itemClick(String id) {
                            if (!PublicTools.isFastClick()) {
                                return;
                            }
                            Intent intent = new Intent(getContext(), NewMerchantInfoActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                            locationQuanxian = false;
                        }

                        @Override
                        public void changeFavorite() {
                            getHomeList();
                        }
                    });
                    list5.setAdapter(adapter5);
                }
            }
        } else if (index.equals("6")) {
            if (GetHomeMerchantList.getInstance().getMoreList() != null) {
                //市场推广
                moreList.setFocusableInTouchMode(false);
                moreList.setFocusable(false);
                if (marketAdapter != null) {
                    marketAdapter.notifyData(GetHomeMerchantList.getInstance().getMoreList());
                } else {
                    marketAdapter = new HomeMarketAdapter(getContext(), GetHomeMerchantList.getInstance().getMoreList());
                    marketAdapter.setOnitemClick(new HomeMarketAdapter.OnitemClick() {
                        @Override
                        public void itemClick(HomeMerchantListData.ExclusiveBean.ListBean bean) {
                            if (!PublicTools.isFastClick()) {
                                return;
                            }
                            if (bean.getRedirectType().equals("1")) {
                                Intent intent = new Intent(getContext(), Web_Activity.class);
                                intent.putExtra("url", bean.getRedirectH5LinkAddress());
                                startActivity(intent);
                                locationQuanxian = false;
                            } else if (bean.getRedirectType().equals("2")) {
                                if (bean.getRedirectAppLinkType().equals("1")) {
                                    Intent intent = new Intent(getContext(), ShareToEarnActivity.class);
                                    startActivity(intent);
                                    locationQuanxian = false;
                                } else if (bean.getRedirectAppLinkType().equals("2")) {
                                    Intent intent = new Intent(getContext(), AboutActivity.class);
                                    intent.putExtra("formMain", "1");
                                    startActivity(intent);
                                    locationQuanxian = false;
                                } else if (bean.getRedirectAppLinkType().equals("3")) {
                                    SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(getActivity(), StaticParament.USER);
                                    String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
                                    Map<String, Object> maps = new HashMap<>();
                                    maps.put("userId", userId);
                                    maps.put("stripeState", 0);//用户区分接入stripe前后的新老用户
                                    jiaoyanFenqifu(maps);
                                    Log.d("qwwedfff", "2222: ");

//                                    openInvestPresent.loadOpenInvestStatus("sm");
                                } else if (bean.getRedirectAppLinkType().equals("4")) {
                                    Intent intent = new Intent(getContext(), MapActivity.class);
                                    startActivity(intent);
                                    locationQuanxian = false;
                                }
                            } else if (bean.getRedirectType().equals("4")) {
                                //自定义弹窗
                                //跳转自定义页面展示方式   redirectCustomizedDisplayType   1Full screen  2  Semi Popup
                                if (bean.getRedirectCustomizedDisplayType() != null) {
                                    if (bean.getRedirectCustomizedDisplayType().equals("2")) {
                                        BannerClickDialog bannerClickDialog = new BannerClickDialog(getContext(),
                                                bean.getRedirectCustomizedTitle(),
                                                bean.getRedirectCustomizedContent(),
                                                bean.getRedirectCustomizedImageUrl()
                                        );
                                        bannerClickDialog.show();
                                    } else if (bean.getRedirectCustomizedDisplayType().equals("1")) {
                                        Intent intent = new Intent(getContext(), BannerClickActivity.class);
                                        intent.putExtra("title", bean.getRedirectCustomizedTitle());
                                        intent.putExtra("text", bean.getRedirectCustomizedContent());
                                        intent.putExtra("url", bean.getRedirectCustomizedImageUrl());
                                        startActivity(intent);
                                        locationQuanxian = false;
                                    }
                                }
                            }
                        }
                    });
                    moreList.setAdapter(marketAdapter);
                }
            }
        }
    }

    /*private void changeList(String num) {
        if (num.equals("6")) {
            //市场推广
            moreList.setFocusableInTouchMode(false);
            moreList.setFocusable(false);
            if (marketAdapter != null) {
                marketAdapter.notifyData(GetHomeMerchantList.getInstance().getMoreList());
            } else {
                marketAdapter = new HomeMarketAdapter(getContext(), GetHomeMerchantList.getInstance().getMoreList());
                marketAdapter.setOnitemClick(new HomeMarketAdapter.OnitemClick() {
                    @Override
                    public void itemClick(HomeMerchantListData.ExclusiveBean.ListBean bean) {
                        if (bean.getRedirectType().equals("1")) {
                            Intent intent = new Intent(getContext(), Web_Activity.class);
                            intent.putExtra("url", GetHomeMerchantList.getInstance().getHomeBannerData().getRedirectH5LinkAddress());
                            startActivity(intent);
                        }else if (bean.getRedirectType().equals("2")) {
                            if (bean.getRedirectAppLinkType().equals("1")) {
                                Intent intent = new Intent(getContext(), ShareToEarnActivity.class);
                                startActivity(intent);
                            } else if (bean.getRedirectAppLinkType().equals("2")) {
                                Intent intent = new Intent(getContext(), AboutActivity.class);
                                intent.putExtra("formMain", "1");
                                startActivity(intent);
                            } else if (bean.getRedirectAppLinkType().equals("3")) {
                                openInvestPresent.loadOpenInvestStatus("sm");
                            } else if (bean.getRedirectAppLinkType().equals("4")) {
                                Intent intent = new Intent(getContext(), MapActivity.class);
                                startActivity(intent);
                            }
                        } else if (bean.getRedirectType().equals("4")) {
                            //自定义弹窗
                            //跳转自定义页面展示方式   redirectCustomizedDisplayType   1Full screen  2  Semi Popup
                            if (bean.getRedirectCustomizedDisplayType().equals("2")) {
                                BannerClickDialog bannerClickDialog = new BannerClickDialog(getContext(),
                                        bean.getRedirectCustomizedTitle(),
                                        bean.getRedirectCustomizedContent(),
                                        bean.getRedirectCustomizedImageUrl()
                                );
                                bannerClickDialog.show();
                            } else if (bean.getRedirectCustomizedDisplayType().equals("1")) {
                                Intent intent = new Intent(getContext(), BannerClickActivity.class);
                                intent.putExtra("title", bean.getRedirectCustomizedTitle());
                                intent.putExtra("text", bean.getRedirectCustomizedContent());
                                intent.putExtra("url", bean.getRedirectCustomizedImageUrl());
                                startActivity(intent);
                            }
                        }
                    }
                });
                moreList.setAdapter(marketAdapter);
            }
        } else {
            HomeMerchantAdapter adapters = null;
            RecyclerView lists = null;
            List<HomeMerchantItemData> merchants = null;
            if (num.equals("1")) {
                adapters = adapter1;
                lists = list1;
                merchants = GetHomeMerchantList.getInstance().getMerchantList1().getMerchants();
                text1.setText(GetHomeMerchantList.getInstance().getMerchantList1().getDescription());
                view1.setText("View all " + GetHomeMerchantList.getInstance().getMerchantList1().getCategoryName());
                categoryType1 = GetHomeMerchantList.getInstance().getMerchantList1().getCategoryType();
                categoryOrder1 = GetHomeMerchantList.getInstance().getMerchantList1().getDisplayOrder();
            } else if (num.equals("2")) {
                adapters = adapter2;
                lists = list2;
                merchants = GetHomeMerchantList.getInstance().getMerchantList2().getMerchants();
                text2.setText(GetHomeMerchantList.getInstance().getMerchantList2().getDescription());
                view2.setText("View all " + GetHomeMerchantList.getInstance().getMerchantList2().getCategoryName());
                categoryType2 = GetHomeMerchantList.getInstance().getMerchantList2().getCategoryType();
                categoryOrder2 = GetHomeMerchantList.getInstance().getMerchantList2().getDisplayOrder();
            } else if (num.equals("3")) {
                adapters = adapter3;
                lists = list3;
                merchants = GetHomeMerchantList.getInstance().getMerchantList3().getMerchants();
                text3.setText(GetHomeMerchantList.getInstance().getMerchantList3().getDescription());
                view3.setText("View all " + GetHomeMerchantList.getInstance().getMerchantList3().getCategoryName());
                categoryType3 = GetHomeMerchantList.getInstance().getMerchantList3().getCategoryType();
                categoryOrder3 = GetHomeMerchantList.getInstance().getMerchantList3().getDisplayOrder();
            } else if (num.equals("4")) {
                adapters = adapter4;
                lists = list4;
                merchants = GetHomeMerchantList.getInstance().getMerchantList4().getMerchants();
                text4.setText(GetHomeMerchantList.getInstance().getMerchantList4().getDescription());
                view4.setText("View all " + GetHomeMerchantList.getInstance().getMerchantList4().getCategoryName());
                categoryType4 = GetHomeMerchantList.getInstance().getMerchantList4().getCategoryType();
                categoryOrder4 = GetHomeMerchantList.getInstance().getMerchantList4().getDisplayOrder();
            } else if (num.equals("5")) {
                adapters = adapter5;
                lists = list5;
                merchants = GetHomeMerchantList.getInstance().getMerchantList5().getMerchants();
                text5.setText(GetHomeMerchantList.getInstance().getMerchantList5().getDescription());
                view5.setText("View all " + GetHomeMerchantList.getInstance().getMerchantList5().getCategoryName());
                categoryType5 = GetHomeMerchantList.getInstance().getMerchantList5().getCategoryType();
                categoryOrder5 = GetHomeMerchantList.getInstance().getMerchantList5().getDisplayOrder();
            }
            lists.setFocusableInTouchMode(false);
            lists.setFocusable(false);
            if (adapters != null) {
                adapters.notifyData(merchants);
            } else {
                adapters = new HomeMerchantAdapter(getContext(), merchants);
                adapters.setOnitemClick(new HomeMerchantAdapter.OnitemClick() {
                    @Override
                    public void itemClick(String id) {
                        Intent intent = new Intent(getContext(), NewMerchantInfoActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }

                    @Override
                    public void changeFavorite() {
                        getHomeList();
                    }
                });
                lists.setAdapter(adapters);
            }
        }
    }*/

    @Override
    public void notifyAllActivity(String title, String str) {
        Log.i("wuhao", "首页收到广播");
        if (str.equals("getLocation")) {
            getChangedList();//首页的几个弹窗
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ListenerManager.getInstance().unRegisterListener(this);
    }

    @Override
    public void openInvestStatus(String code, String licai, String zhifu, String fenqi) {
        if (fenqi == null) {
            return;
        }
        if (fenqi.equals("1")) {
            ((MainTab) getActivity()).changeTab(2);
        } else {
//            Intent intent = new Intent(getContext(), FenQiFuOpen_Activity.class);
//            startActivity(intent);
            locationQuanxian = false;
        }
    }

    public class UpdateCallBack implements CheckUpdateCallBack {
        @Override
        public void onUpdateInfo(Intent intent) {
            try {
                if (intent != null) {
                    //更新状态信息
                    int status = intent.getIntExtra(UpdateKey.STATUS, -1);
                    //返回错误码，建议打印
                    int rtnCode = intent.getIntExtra(UpdateKey.FAIL_CODE, -1);
                    //失败信息，建议打印
                    String reason = intent.getStringExtra(UpdateKey.FAIL_REASON);
                    if (status == HAS_UPGRADE_INFO) {
                        L.log("need");
                        //获取更新信息
                        Serializable info = intent.getSerializableExtra(UpdateKey.INFO);
//                String hwData = info.toString().replaceAll("_", "").trim();
                        Gson gson = new Gson();
                        String s = gson.toJson(info);
                        HuaWeiUpdateData huaWeiUpdateData = gson.fromJson(s, HuaWeiUpdateData.class);
                        version = huaWeiUpdateData.getVersion();
                        getHuaWeiUpdateResult(version);
                        L.log("version :" + version);
                    } else {//华为版本app不需要更新
//                        updatedialogAndOtherDialog();
                        version = BuildConfig.VERSION_NAME;
                        getHuaWeiUpdateResult(version);
                        L.log("noNeed");
                    }
                } else {
                    version = "";
                    getHuaWeiUpdateResult(version);
                }
            } catch (Exception e) {
                e.printStackTrace();
                version = "";
                e.printStackTrace();
            }
        }

        @Override
        public void onMarketInstallInfo(Intent intent) {

        }

        @Override
        public void onMarketStoreError(int i) {

        }

        @Override
        public void onUpdateStoreError(int i) {

        }
    }

    //检查更新
    private void showView() {
        if (((MainTab) getActivity()).isHaveUpdateFlag()) {
            int marketType = BuildConfig.marketType;
            //if (sharePreferenceUtils.get(StaticParament.CHECK_FLAG, "").equals("0")) {
            if (marketType == 1) {
                //检查华为市场
                checkAppUpdate(getContext(), new UpdateCallBack(), false, false);
            } else if (marketType == 0) {
                final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
                loadingDialog.showNotMiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismiss();
                    }
                }, StaticParament.server.equals("1") ? 3 * 1000 : 6 * 1000);
                //检查谷歌
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        checkGoogleUpdate();
                    }
                }).start();
            } else {
                locationSer();
            }
        }
        ((MainTab) getActivity()).setHaveUpdateFlag(false);//确保仅仅谈一次开启权限的弹窗！！
    }

    private void getHuaWeiUpdateResult(final String version) {
        CheckAppVersionVerify checkAppVersionVerify = new CheckAppVersionVerify(getContext());
        Map<String, Object> maps = new HashMap<>();
        maps.put("currentVersionId", BuildConfig.currentVersionId);
        maps.put("newVersionNo",/*BuildConfig.VERSION_NAME*/version);
        maps.put("no_user_id", 1);
//        maps.put("phoneModel", "android," + android.os.Build.BRAND + "," + android.os.Build.MODEL + "," + android.os.Build.VERSION.RELEASE);//手机厂商 +手机型号 +手机系统版本号
//
        maps.put("phoneModel", android.os.Build.BRAND);//机型
        maps.put("phoneSystem", 1);//手机系统 1安卓 2ios
        maps.put("phoneSystemVersion", android.os.Build.VERSION.RELEASE);//系统版本
        maps.put("mobileModel", android.os.Build.MODEL);//手机型号
        checkAppVersionVerify.appVersionVerify(maps);
        checkAppVersionVerify.setOnCheckVersion(new CheckAppVersionVerify.onCheckVersion() {
            @Override
            public void getFlag(String flag) {
                if (flag != null) {
                    if (flag.equals("1")) {//强更
                        if (tiShiDialog == null) {
                            tiShiDialog = new TiShiDialog(getContext());
                            tiShiDialog.ShowNotCloseDialog(" Please update your App to the latest version to continue.", "Confirm");
                        }
                        tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                            @Override
                            public void GuanBi() {
                                launchAppDetail(getContext(), BuildConfig.APPLICATION_ID, "com.huawei.appmarket");
                            }
                        });
                    } else if (flag.equals("0")) {//非强更
                        if (updateDialog == null) {
                            updateDialog = new UpdateDialog(getContext());
                            updateDialog.ShowDialog(version);
                        }
                        updateDialog.setOnUpdate(new UpdateDialog.Update() {
                            @Override
                            public void onUpdate() {
                                launchAppDetail(getContext(), BuildConfig.APPLICATION_ID, "com.huawei.appmarket");
                            }
                        });
                        updateDialog.setOnClose(new UpdateDialog.Close() {
                            @Override
                            public void onClose() {
                                updatedialogAndOtherDialog();
                            }
                        });
                    } else if (flag.equals("2")) {//不更   华为手机时  华为Id时 走这儿!!!!!!!!
                        updatedialogAndOtherDialog();
                    }
                }
            }
        });
    }

    public void updatedialogAndOtherDialog() {
        try {
            if (jsonObject.getInt("agreementState") == 1) { //协议弹窗 ：0：不弹 1：弹
                TCDialog tcDialog = new TCDialog(getActivity());
                tcDialog.setOnGuanBi(new TCDialog.GuanBi() {
                    @Override
                    public void GuanBi() {
                        showToumingTuceng();
//                        killAppProcess();
                    }

                    @Override
                    public void queding() {
                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(getActivity(), StaticParament.USER);
                        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
                        final Map<String, Object> maps = new HashMap<>();
                        maps.put("userId", userId);
                        updateUserAgreementState(maps);
                    }
                });
                tcDialog.ShowDialog("");
            } else {
                Log.d("xzcxzvcxzc", "locationSer: 3");
                if (jsonObject.has("stripeState")) {
                    if (jsonObject.getString("stripeState").equals("0")) {
                        locationSer();
                    } else if (jsonObject.getString("stripeState").equals("1")) {
                        TiShiDialog tiShiDialog = new TiShiDialog(getActivity());
                        tiShiDialog.showStripeDialog();
                        tiShiDialog.setOnGuanBiLeft(new TiShiDialog.GuanBiLeft() {
                            @Override
                            public void GuanBiLeft() {
                                locationSer();
                            }
                        });
                        tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                            @Override
                            public void GuanBi() {//去绑卡
                                locationSer();
                            }
                        });
                    }
                } else {

                    locationSer();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showToumingTuceng() {
        NoLoadingDialog loadingDialog = new NoLoadingDialog(getActivity());
        loadingDialog.show(true);
        loadingDialog.setShowTCDialog(new NoLoadingDialog.ShowTCDialog() {
            @Override
            public void ShowTCDialog() {
                TCDialog tcDialog = new TCDialog(getActivity());
                tcDialog.setOnGuanBi(new TCDialog.GuanBi() {
                    @Override
                    public void GuanBi() {
                        showToumingTuceng();
//                        killAppProcess();
                    }

                    @Override
                    public void queding() {
                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(getActivity(), StaticParament.USER);
                        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
                        final Map<String, Object> maps = new HashMap<>();
                        maps.put("userId", userId);
                        updateUserAgreementState(maps);
                    }
                });
                tcDialog.ShowDialog("");
            }
        });
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

    private void checkGoogleUpdate() {

        VersionChecker versionChecker = new VersionChecker();
        try {
            version = versionChecker.execute().get();
            if (version != null) {
                //修改逻辑，不判断版本大小，每次都调用后台接口
                getGoogleUpdateResult(version);
            } else {

                version = "";
                getGoogleUpdateResult("");
            }
        } catch (Exception e) {
            version = "";
            getGoogleUpdateResult("");
            e.printStackTrace();
        }
    }

    private void getGoogleUpdateResult(String version) {
        Map<String, Object> maps = new HashMap<>();
        CheckAppVersionVerify checkAppVersionVerify = new CheckAppVersionVerify(getContext());
        maps.put("currentVersionId", BuildConfig.currentVersionId);
        maps.put("newVersionNo", version);
        maps.put("no_user_id", 1);
//        maps.put("phoneModel", "android," + android.os.Build.BRAND + "," + android.os.Build.MODEL + "," + android.os.Build.VERSION.RELEASE);//手机厂商 +手机型号 +手机系统版本号
//
        maps.put("phoneModel", android.os.Build.BRAND);//机型
        maps.put("phoneSystem", 1);//手机系统 1安卓 2ios
        maps.put("phoneSystemVersion", android.os.Build.VERSION.RELEASE);//系统版本
        maps.put("mobileModel", android.os.Build.MODEL);//手机型号
        checkAppVersionVerify.appVersionVerify(maps);
        checkAppVersionVerify.setOnCheckVersion(new CheckAppVersionVerify.onCheckVersion() {
            @Override
            public void getFlag(String flag) {
                if (flag != null) {
                    if (flag.equals("1")) {//强更
                        if (tiShiDialog == null) {
                            tiShiDialog = new TiShiDialog(getContext());
                            handler.sendEmptyMessage(2);
                        }
                        tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                            @Override
                            public void GuanBi() {
                                handler.sendEmptyMessage(4);
                            }
                        });
                    } else if (flag.equals("0")) {//非强更
                        if (updateDialog == null) {
                            updateDialog = new UpdateDialog(getContext());
                            handler.sendEmptyMessage(3);
                        }
                        updateDialog.setOnUpdate(new UpdateDialog.Update() {
                            @Override
                            public void onUpdate() {
                                handler.sendEmptyMessage(4);
                            }
                        });
                        updateDialog.setOnClose(new UpdateDialog.Close() {
                            @Override
                            public void onClose() {
                                handler.sendEmptyMessage(1);
                            }
                        });
                    } else {//不更
                        handler.sendEmptyMessage(1);
                    }
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    /**
     * 判断位置权限
     */
    public boolean checkLocationPermission() {
        String[] PERMISSION_STORAGE =
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        try {
            if (hasPermissions(getActivity(), PERMISSION_STORAGE)) {
                //有权限
                return true;
            } else {
                L.log("申请权限");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            loadNotReadMessageCount();
            if (checkReceiverFlag) {
                checkReceiverFlag = false;
                checkReceiver();    //判断邀请人弹窗
            }
        }
    }
}