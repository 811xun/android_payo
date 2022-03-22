package com.home.glx.uwallet.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.home.glx.uwallet.BaseFragment;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.NewMerchantInfoActivity;
import com.home.glx.uwallet.activity.xzc.LoginActivity_inputNumber;
import com.home.glx.uwallet.datas.MerchantInfoData;
import com.home.glx.uwallet.mvp.IListener;
import com.home.glx.uwallet.mvp.ListenerManager;
import com.home.glx.uwallet.mvp.LoadFindList;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.GetLocationDialog;
import com.home.glx.uwallet.selfview.WordWrapView;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;
import com.home.glx.uwallet.tools.gps.RefreshMyLocationManager;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.MapsInitializer;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.CameraPosition;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;
import com.huawei.hms.maps.model.VisibleRegion;
import com.mxy.fpshadowlayout.FpShadowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.huawei.hms.maps.CameraUpdateFactory.newLatLngZoom;

public class HuaWeiMapFragment extends BaseFragment implements OnMapReadyCallback, IListener {
    private MapView mMapView;
    private RelativeLayout loactionRl;
    private Button openLoaction;
    private String TAG = "HuaWeiMapActivity.";
    private HuaweiMap hMap;
    private boolean mLocationPermissionGranted;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 0;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;
    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final float DEFAULT_ZOOM = /*12.83f*/16;
    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);

    private Bundle bundle;

    private RefreshMyLocationManager myLocationManager;

    private boolean createMayFlag = false;

    private FpShadowLayout shadowLayout;
    private LinearLayout merchantDialog;
    private ImageView logo;
    private TextView discount;
    private TextView categories;
    private TextView tags;
    private TextView learn_more;
    private TextView tag1, tag2, tag3;
    private TextView merchantName;
    private LinearLayout tagLl;
    private CardView img_bg;
    private WordWrapView wordWrapView;
    private TextView distance;
    private int width;
    private int flag;
    private List<Integer> sameList = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                sameList.clear();
                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).getLat().equals("") || listData.get(i).getLng().equals("")) {
                        continue;
                    }
                    for (int j = i + 1; j < listData.size(); j++) {
                        if (listData.get(j).getLat().equals(listData.get(i).getLat()) &&
                                listData.get(j).getLng().equals(listData.get(i).getLng())) {
                            sameList.add(j);
                        }
                    }
                }
                hMap.clear();
                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).getLat().equals("") || listData.get(i).getLng().equals("")) {
                        break;
                    }
                    int mipId1;
                    if (listData.get(i).getCategories().equals("1")) {
                        mipId1 = R.mipmap.casual_dining_icon1;
                    } else if (listData.get(i).getCategories().equals("2")) {
                        mipId1 = R.mipmap.cafe_icon1;
                    } else if (listData.get(i).getCategories().equals("3")) {
                        mipId1 = R.mipmap.bar_icon1;
                    } else if (listData.get(i).getCategories().equals("4")) {
                        mipId1 = R.mipmap.asian_icon1;
                    } else if (listData.get(i).getCategories().equals("5")) {
                        mipId1 = R.mipmap.fast_food_icon1;
                    } else if (listData.get(i).getCategories().equals("6")) {
                        mipId1 = R.mipmap.dessert_icon1;
                    } else {
                        mipId1 = R.mipmap.big_location;
                    }
                    if (/*flag == i*/Double.parseDouble(listData.get(i).getLat()) == Double.parseDouble(listData.get(flag).getLat())
                            && Double.parseDouble(listData.get(i).getLng()) == Double.parseDouble(listData.get(flag).getLng())) {
                        if (flag != i) {
                            continue;
                        }
                        LatLng PERTH = new LatLng(Double.parseDouble(listData.get(i).getLat()), Double.parseDouble(listData.get(i).getLng()));
                        try {
                            if (BitmapDescriptorFactory.fromResource(mipId1) != null) {
                                Marker mPerth = hMap.addMarker(new MarkerOptions()
                                        .position(PERTH)
                                        //.title(list.get(i).getPracticalName())
                                        .icon(BitmapDescriptorFactory.fromResource(mipId1)));
                                mPerth.setTag(i);
                            }
                        } catch (Exception e) {

                        }

                    } else {
                        if (sameList.contains(i)) {
                            continue;
                        }
                        int mipId;
                        if (listData.get(i).getCategories().equals("1")) {
                            mipId = R.mipmap.casual_dining_icon;
                        } else if (listData.get(i).getCategories().equals("2")) {
                            mipId = R.mipmap.cafe_icon;
                        } else if (listData.get(i).getCategories().equals("3")) {
                            mipId = R.mipmap.bar_icon;
                        } else if (listData.get(i).getCategories().equals("4")) {
                            mipId = R.mipmap.asian_icon;
                        } else if (listData.get(i).getCategories().equals("5")) {
                            mipId = R.mipmap.fast_food_icon;
                        } else if (listData.get(i).getCategories().equals("6")) {
                            mipId = R.mipmap.dessert_icon;
                        } else {
                            mipId = R.mipmap.merchant_location;
                        }
                        LatLng PERTH = new LatLng(Double.parseDouble(listData.get(i).getLat()), Double.parseDouble(listData.get(i).getLng()));
                        try {
                            if (BitmapDescriptorFactory.fromResource(mipId) != null) {
                                Marker mPerth = hMap.addMarker(new MarkerOptions()
                                        .position(PERTH)
                                        //.title(list.get(i).getPracticalName())
                                        .icon(BitmapDescriptorFactory.fromResource(mipId)));
                                mPerth.setTag(i);
                            }
                            L.log("商户名称：" + listData.get(i).getPracticalName());
                        } catch (Exception e) {

                        }
                    }
                }
            } else if (msg.what == 2) {
                sameList.clear();
                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).getLat().equals("") || listData.get(i).getLng().equals("")) {
                        continue;
                    }
                    for (int j = i + 1; j < listData.size(); j++) {
                        if (listData.get(j).getLat().equals(listData.get(i).getLat()) &&
                                listData.get(j).getLng().equals(listData.get(i).getLng())) {
                            sameList.add(j);
                        }
                    }
                }
                for (int i = 0; i < listData.size(); i++) {
                    if (listData.get(i).getLat().equals("") || listData.get(i).getLng().equals("")) {
                        continue;
                    }
                    if (sameList.contains(i)) {
                        continue;
                    }
                    int mipId;
                    if (listData.get(i).getCategories().equals("1")) {
                        mipId = R.mipmap.casual_dining_icon;
                    } else if (listData.get(i).getCategories().equals("2")) {
                        mipId = R.mipmap.cafe_icon;
                    } else if (listData.get(i).getCategories().equals("3")) {
                        mipId = R.mipmap.bar_icon;
                    } else if (listData.get(i).getCategories().equals("4")) {
                        mipId = R.mipmap.asian_icon;
                    } else if (listData.get(i).getCategories().equals("5")) {
                        mipId = R.mipmap.fast_food_icon;
                    } else if (listData.get(i).getCategories().equals("6")) {
                        mipId = R.mipmap.dessert_icon;
                    } else {
                        mipId = R.mipmap.merchant_location;
                    }
                    LatLng PERTH = new LatLng(Double.parseDouble(listData.get(i).getLat()), Double.parseDouble(listData.get(i).getLng()));
                    try {
                        if (BitmapDescriptorFactory.fromResource(mipId) != null) {
                            MarkerOptions options = new MarkerOptions()
                                    .position(PERTH)
                                    //.title(list.get(i).getPracticalName())
                                    .clusterable(true)
                                    .icon(BitmapDescriptorFactory.fromResource(mipId));
                            Marker mPerth = hMap.addMarker(options);
                            mPerth.setTag(i);
                            L.log("商户名称：" + listData.get(i).getPracticalName());
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        width = metric.widthPixels;
        // 屏幕高度（像素）
        int height = metric.heightPixels;

        ListenerManager.getInstance().registerListtener(this);
        getLayoutTop(savedInstanceState);
        /*if (view == null) {
            view = inflater.inflate(getLayout(), container, false);
        }*/
        view = inflater.inflate(getLayout(), container, false);
        mMapView = view.findViewById(R.id.huawei_map);
        loactionRl = (RelativeLayout) view.findViewById(R.id.location_permission);
        shadowLayout = view.findViewById(R.id.shadow);
        merchantDialog = (LinearLayout) view.findViewById(R.id.merchant_tag);
        logo = (ImageView) view.findViewById(R.id.logo);
        discount = (TextView) view.findViewById(R.id.discount);
        categories = (TextView) view.findViewById(R.id.categories);
        tags = (TextView) view.findViewById(R.id.tags);
        merchantName = (TextView) view.findViewById(R.id.merchant_name);
        tagLl = (LinearLayout) view.findViewById(R.id.tag_ll);
        img_bg = (CardView) view.findViewById(R.id.img_bg);
        wordWrapView = (WordWrapView) view.findViewById(R.id.id_popular_list);
        tag1 = (TextView) view.findViewById(R.id.tag1);
        tag2 = (TextView) view.findViewById(R.id.tag2);
        tag3 = (TextView) view.findViewById(R.id.tag3);
        distance = (TextView) view.findViewById(R.id.distance);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                merchantDialog.setVisibility(View.GONE);
                shadowLayout.setVisibility(View.GONE);
            }
        });
        //切换字体
        TypefaceUtil.replaceFont(merchantName, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(categories, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(discount, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(distance, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(tags, "fonts/gilroy_regular.ttf");
        /*RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) merchantDialog.getLayoutParams();
        lp.height = 117 * height / 812;
        lp.width = 343 * width / 375;
        merchantDialog.setLayoutParams(lp);

        LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) img_bg.getLayoutParams();
        lp1.height = 85 * height / 812;
        lp1.width = 110 * width / 375;
        img_bg.setLayoutParams(lp1);

        FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) logo.getLayoutParams();
        lp2.height = 85 * height / 812;
        lp2.width = 110 * width / 375;
        logo.setLayoutParams(lp2);

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        learn_more.measure(w, h);
        int hh = learn_more.getMeasuredHeight();
        int ww = learn_more.getMeasuredWidth();

        LinearLayout.LayoutParams lp3 = (LinearLayout.LayoutParams) distance.getLayoutParams();
        lp3.height = 85 * height / 812;
        int distanceWidth = 343 * width / 375 - (110 * width / 375) - ww - 20 - 12 - 10 - 5 - 20;
        *//*tag1.setMaxWidth(65 * width / 375);
        tag2.setMaxWidth(65 * width / 375);
        tag3.setMaxWidth(65 * width / 375);*//*
        distance.setMaxWidth(distanceWidth);*/

        openLoaction = (Button) view.findViewById(R.id.open_location);
        openLoaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HuaWeiMapFragment.this.requestPermissions(
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        });
        checkLocationPermission();
        if (savedInstanceState != null) {
            bundle = savedInstanceState.getBundle("MapViewBundleKey");
        }
        mMapView.getMapAsync(this);
        return view;
    }


    public VisibleRegion getFourPoint() {
        if (hMap != null) {
            VisibleRegion visibleRegion = hMap.getProjection().getVisibleRegion();
            return visibleRegion;
        } else {
            return null;
        }
    }

    private void createMapView() {
        mMapView.onCreate(bundle);
    }

    public void getLayoutTop(Bundle savedInstanceState) {
        //从保存的实例状态中检索位置和摄像机位置。
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
    }

    public static HuaWeiMapFragment newInstance() {
        Bundle args = new Bundle();
        HuaWeiMapFragment fragment = new HuaWeiMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        // 在setContentView之前设置API Key
        MapsInitializer.setApiKey("CgB6e3x9s/WsqDT7lGQkn+TmOTDfm97b+qryXe1wR0sZB5vboYHhSDaMt75OliiIjwC/pVj8yEr6E/KJeLl2hMTD");
        return R.layout.fragment_huawei_maps;
    }

    @Override
    public void initView() {

    }

    @Override
    public void onMapReady(HuaweiMap huaweiMap) {
        L.log(TAG, "onMapReady: ");
        hMap = huaweiMap;
        hMap.setOnMapClickListener(new HuaweiMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                merchantDialog.setVisibility(View.GONE);
                shadowLayout.setVisibility(View.GONE);
            }
        });
        hMap.setOnMyLocationButtonClickListener(new HuaweiMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (mLastKnownLocation != null) {
                    LatLng myLatLng = new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude());
                    //mMap.addMarker(new MarkerOptions().position(myLatLng).title("My Location"));
                    hMap.moveCamera(newLatLngZoom(myLatLng, DEFAULT_ZOOM));
                }
                return false;
            }
        });

        /**
         * 设置信息窗的放置习惯
         */
        //hMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        // Prompt the user for permission.
        getLocationPermission();
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        if (!isHidden()) {
            // 获取设备的当前位置并设置地图的位置。
            getDeviceLocation();
            reqFindList();
        }
        // 设置Marker可聚合
        //hMap.setMarkersClustering(true);
    }

    class CustomInfoWindowAdapter implements HuaweiMap.InfoWindowAdapter {
        private final View mWindow;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            TextView txtvTitle = mWindow.findViewById(R.id.title);
            TextView txtvSnippett = mWindow.findViewById(R.id.snippet);
            txtvTitle.setText(marker.getTitle());
            txtvSnippett.setText(marker.getSnippet());

            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    /**
     * 获取设备的当前位置，并定位到地图的界面。
     */
    private void getDeviceLocation() {
//        getEndGps();
        /*
         *获取设备的最佳和最新位置，在位置不可用的极少数情况下，该位置可能为空。
         */
        getGps();
        /*try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(),
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful()) {
                                    //将地图的展示位置设置为设备的当前位置。
                                    mLastKnownLocation = task.getResult();
                                    if (mLastKnownLocation != null) {
                                        hMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                                new LatLng(mLastKnownLocation.getLatitude(),
                                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                    } else {
                                        //没有获取到位置信息,通过本地方法获取经纬度
                                        getGps();
                                    }

                                } else {
                                    Log.d(TAG, "Current location is null. Using defaults.");
                                    Log.e(TAG, "Exception: %s", task.getException());
                                    hMap.moveCamera(CameraUpdateFactory
                                            .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                                    //不显示定位按钮
                                    hMap.getUiSettings().setMyLocationButtonEnabled(false);
                                }
                            }
                        });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }*/
    }

    /**
     * 本地方法获取经纬度
     */
    private void getGps() {
        myLocationManager
                = RefreshMyLocationManager.getInstance(getContext(), new RefreshMyLocationManager.OnGetGps() {
            @Override
            public void get_gps(Location location) {
                Log.e("经度：", location.getLongitude() + "");
                Log.e("纬度：", location.getLatitude() + "");
                Log.e("海拔：", "0");
                SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(getContext(), StaticParament.APP);
                appMsgSharePreferenceUtils.put(StaticParament.LAT, location.getLatitude() + "");
                appMsgSharePreferenceUtils.put(StaticParament.LNG, location.getLongitude() + "");
                appMsgSharePreferenceUtils.save();
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
//                        new LatLng(weidu, jingdu), DEFAULT_ZOOM));
                if (mLastKnownLocation == null) {
                    mLastKnownLocation = location;
                    hMap.moveCamera(newLatLngZoom(
                            new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                } else {
                    mLastKnownLocation = location;
                }

            }
        });
        myLocationManager.initLocation();
    }

    /**
     * 根据用户是否已授予位置权限来更新地图的UI设置。
     */
    private void updateLocationUI() {
        if (hMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                hMap.setMyLocationEnabled(true);
                hMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                hMap.setMyLocationEnabled(false);
                hMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * 获取列表
     */
    public void reqFindList() {
        SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(getContext(), StaticParament.APP);
        Map<String, Object> maps = new HashMap<>();
/*        maps.put("mainBusiness", "");
//        maps.put("corporateName", "");
        maps.put("sortDisCount", "");
        maps.put("sortDistance", "");*/
        //纬度
        maps.put("lat", appMsgSharePreferenceUtils.get(StaticParament.LAT, ""));
        maps.put("lng", appMsgSharePreferenceUtils.get(StaticParament.LNG, ""));
        maps.put("no_user_id", 1);
        LoadFindList loadFindList = new LoadFindList(getContext());
        loadFindList.setOnFindList(new LoadFindList.OnFindList() {
            @Override
            public void findList(String dataStr, int maxPage) {
//                maxPages = maxPage;
                Gson gson1 = new Gson();
                List<MerchantInfoData> list = gson1.fromJson(dataStr, new TypeToken<List<MerchantInfoData>>() {
                }.getType());
                addMarker(list);
            }
        });
        if (!maps.get("lat").equals("") && !maps.get("lng").equals("")) {
            loadFindList.getFindList(maps);
        }
    }

    private List<MerchantInfoData> listData = new ArrayList();

    public void checkId(String id) {
        //匹配选中的商户id是哪一个
        merchantDialog.setVisibility(View.GONE);
        shadowLayout.setVisibility(View.GONE);
        int tagId = -1;
        for (int i = 0; i < listData.size(); i++) {
            if (listData.get(i).getId().equals(id)) {
                tagId = i;
                break;
            }
        }
        if (tagId != -1) {
            handler.sendEmptyMessage(2);
            showMerchantDialog(tagId);
        }
    }

    private void showMerchantDialog(int id) {
        merchantDialog.setVisibility(View.GONE);
        shadowLayout.setVisibility(View.GONE);
        if (id == -1) {
            return;
        }
        flag = id;
        hMap.clear();
        hMap.moveCamera(newLatLngZoom(
                new LatLng(Double.parseDouble(listData.get(flag).getLat()),
                        Double.parseDouble(listData.get(flag).getLng())), hMap.getCameraPosition().zoom));
        handler.sendEmptyMessage(1);
        merchantDialog.setVisibility(View.VISIBLE);
        shadowLayout.setVisibility(View.VISIBLE);
        Glide.with(getContext())
                .load(StaticParament.ImgUrl + listData.get(flag).getLogoUrl())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(PublicTools.dip2px(getContext(), 9))))//圆角半径
                .into(logo);
        merchantName.setText(listData.get(flag).getPracticalName());
        if (!TextUtils.isEmpty(listData.get(flag).getUserDiscount()) && !listData.get(flag).getUserDiscount().equals("0")) {
            discount.setVisibility(View.VISIBLE);
            String dis = (int) CalcTool.mm(listData.get(flag).getUserDiscount(), "100") + "% OFF";
            discount.setText(dis);
           /* if (listData.get(flag).getHaveWholeSell() == 0) {
                discount.setText(dis + " - Limited Time");
            } else {
                discount.setText(dis + " - Limited Offer");
            }*/
            if (!TextUtils.isEmpty(listData.get(flag).getCategoriesStr())) {
                categories.setText(listData.get(flag).getCategoriesStr() + "  ·  ");
            }
        } else {
            discount.setVisibility(View.GONE);
            categories.setText(listData.get(flag).getCategoriesStr());
        }
        if (listData.get(flag).getTags() != null) {
            List<String> tagList = listData.get(flag).getTags();
            String tagText = "";
            for (int i = 0; i < tagList.size(); i++) {
                if (i == tagList.size() - 1) {
                    tagText += tagList.get(i);
                } else {
                    tagText += tagList.get(i) + " · ";
                }
            }
            tags.setText(tagText);
        }
        /*tagLl.setVisibility(View.VISIBLE);
        if (listData.get(flag).getTags() != null) {
            List<String> tags = listData.get(flag).getTags();
            if (listData.get(flag).getTags().size() == 0) {
                tag1.setVisibility(View.GONE);
                tag2.setVisibility(View.GONE);
                tag3.setVisibility(View.GONE);
                tagLl.setVisibility(View.GONE);
            }
            int tagWidth = 343 * width / 375 - (110 * width / 375) - 10 - 8;
            if (listData.get(flag).getTags().size() == 1) {
                tag1.setMaxWidth(tagWidth);
                tag1.setVisibility(View.VISIBLE);
                tag1.setText(" " + tags.get(0) + " ");
                tag2.setVisibility(View.GONE);
                tag3.setVisibility(View.GONE);
            }
            if (listData.get(flag).getTags().size() == 2) {
                tag1.setText(" " + tags.get(0) + " ");
                tag2.setText(" " + tags.get(1) + " ");
                int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                tag1.measure(w, h);
                int hh = tag1.getMeasuredHeight();
                int ww = tag1.getMeasuredWidth();
                if (tagWidth - ww - 4 - 5 > 0) {
                    tag2.setMaxWidth(tagWidth - ww - 4);
                } else {
                    tag2.setVisibility(View.GONE);
                }
                tag1.setVisibility(View.VISIBLE);
                tag2.setVisibility(View.VISIBLE);
                tag3.setVisibility(View.GONE);
            }
            if (listData.get(flag).getTags().size() == 3) {
                tag1.setVisibility(View.VISIBLE);
                tag2.setVisibility(View.VISIBLE);
                tag3.setVisibility(View.VISIBLE);
                tag1.setText(" " + tags.get(0) + " ");
                tag2.setText(" " + tags.get(1) + " ");
                tag3.setText(" " + tags.get(2) + " ");
                int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                tag1.measure(w, h);
                int hh = tag1.getMeasuredHeight();
                int ww = tag1.getMeasuredWidth();
                if (tagWidth - ww - 4 - 5 > 0) {
                    tag2.setMaxWidth(tagWidth - ww - 4);
                } else {
                    tag2.setVisibility(View.GONE);
                }
                int w1 = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int h1 = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                tag2.measure(w, h);
                int hh1 = tag2.getMeasuredHeight();
                int ww1 = tag2.getMeasuredWidth();
                if (tagWidth - ww - ww1 - 8 - 5 > 0) {
                    tag3.setMaxWidth(tagWidth - ww - ww1 - 8);
                } else {
                    tag3.setVisibility(View.GONE);
                }
            }
        } else {
            tag1.setVisibility(View.GONE);
            tag2.setVisibility(View.GONE);
            tag3.setVisibility(View.GONE);
            tagLl.setVisibility(View.GONE);
        }*/
        distance.setText(listData.get(flag).getDistance() + "km");
        merchantDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //merchantDialog.setVisibility(View.GONE);
                openDetails(flag);
            }
        });
    }

    /**
     * 添加标记点
     */
    private void addMarker(final List<MerchantInfoData> list) {
        listData = list;
        if (hMap != null) {
            hMap.clear();
        }
        handler.sendEmptyMessage(2);

        //设置地图中心点
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(BRISBANE));
        //设置地图中心点和缩放级别，越大显示的地区越小
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BRISBANE, 14));

        // Set a listener for marker click.
        hMap.setOnMarkerClickListener(new HuaweiMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                merchantDialog.setVisibility(View.GONE);
                shadowLayout.setVisibility(View.GONE);
                if (marker.getTag() == null) {
                    return false;
                }
                flag = (int) marker.getTag();
                showMerchantDialog(flag);
                return false;
            }
        });
    }

    private void openDetails(int num) {
        if (!isLogin()) {
//            Intent intent_loging = new Intent(getActivity(), Login_Activity.class);
            Intent intent_loging = new Intent(getActivity(), LoginActivity_inputNumber.class);
            startActivity(intent_loging);
            return;
        }
        /*Intent intent_about_us = new Intent(getContext(), Web_Activity.class);
        intent_about_us.putExtra("url", StaticParament.SHMsg + listData.get(num).getId());
        intent_about_us.putExtra("title", listData.get(num).getPracticalName());
        startActivity(intent_about_us);*/
        Intent intent_about_us = new Intent(getContext(), NewMerchantInfoActivity.class);
        intent_about_us.putExtra("from", "map");
        intent_about_us.putExtra("id", listData.get(num).getId());
        intent_about_us.putExtra("title", listData.get(num).getPracticalName());
        startActivity(intent_about_us);
    }


    /**
     * 提示用户允许使用设备位置。
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        // 检查您的应用是否具有给定的权限，以及该权限对应的App Ops是否被允许
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            mLocationPermissionGranted = false;
            // 请求授予此应用程序的权限
/*            String[] strings =
                    {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(getActivity(),
                    strings,
                    1);*/
        }
    }

    /**
     * 检查设备位置权限。
     */
    private void checkLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            createMayFlag = true;
            createMapView();
        } else {
            mLocationPermissionGranted = false;
            mMapView.setVisibility(View.GONE);
            SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(getContext(), StaticParament.DEVICE);
            String locationFlag = (String) sharePreferenceUtils.get(StaticParament.MAP_LOCATION_FLAG, "");
            if (locationFlag.equals("0")) {
                sharePreferenceUtils.put(StaticParament.MAP_LOCATION_FLAG, "1");
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
                        HuaWeiMapFragment.this.requestPermissions(
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSIONS_REQUEST_FINE_LOCATION);
                    }
                });
            } else if (locationFlag.equals("1")) {
                loactionRl.setVisibility(View.VISIBLE);
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
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
                break;

            case PERMISSIONS_REQUEST_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    loactionRl.setVisibility(View.GONE);
                    mMapView.setVisibility(View.VISIBLE);
                    createMayFlag = true;
                    createMapView();
                    if (getActivity() instanceof MainTab)
                        ((MainTab) getActivity()).getNowLocationDistance();
                } else {
                    loactionRl.setVisibility(View.VISIBLE);
                    mMapView.setVisibility(View.GONE);
                }
                break;
        }
        updateLocationUI();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        if (mLocationPermissionGranted) {
            mMapView.onSaveInstanceState(mapViewBundle);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mLocationPermissionGranted) {
            mMapView.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLocationPermissionGranted) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationPermissionGranted) {
            mMapView.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mLocationPermissionGranted) {
            mMapView.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view = null;
        if (myLocationManager != null) {
            myLocationManager.destoryLocationManager();
        }
        if (mLocationPermissionGranted) {
            mMapView.onDestroy();
        }
        ListenerManager.getInstance().unRegisterListener(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mLocationPermissionGranted) {
            mMapView.onLowMemory();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
       /* if (!hidden) {
            //getDeviceLocation();
            if (hMap != null) {
                hMap.moveCamera(newLatLngZoom(
                        new LatLng(mLastKnownLocation.getLatitude(),
                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
            }
            if (mLocationPermissionGranted) {
                reqFindList();
            }
        } else {
            if (myLocationManager != null) {
                myLocationManager.destoryLocationManager();
            }
            merchantDialog.setVisibility(View.GONE);
        }*/
    }

    @Override
    public void notifyAllActivity(String title, String str) {
        Log.i("wuhao", "华为map收到广播");
        if (str.equals("getPermission")) {
            if (hMap == null && !createMayFlag) {
                createMayFlag = true;
                mLocationPermissionGranted = true;
                loactionRl.setVisibility(View.GONE);
                mMapView.setVisibility(View.VISIBLE);
                createMapView();
            }
        }
    }
}
