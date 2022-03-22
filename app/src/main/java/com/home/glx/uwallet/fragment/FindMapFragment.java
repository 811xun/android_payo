package com.home.glx.uwallet.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.NewMerchantInfoActivity;
import com.home.glx.uwallet.activity.xzc.LoginActivity_inputNumber;
import com.home.glx.uwallet.datas.MerchantInfoData;
import com.home.glx.uwallet.mvp.IListener;
import com.home.glx.uwallet.mvp.ListenerManager;
import com.home.glx.uwallet.mvp.LoadFindList;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.GetLocationDialog;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;
import com.home.glx.uwallet.tools.gps.RefreshMyLocationManager;
import com.mxy.fpshadowlayout.FpShadowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 地图
 */
public class FindMapFragment extends FindMapBaseFragment
        implements OnMapReadyCallback, IListener {

    private static final String TAG = FindMapFragment.class.getSimpleName();
    private GoogleMap mMap;
    private MapView mapView;
    private RelativeLayout loactionRl;
    private Button openLoaction;
    private CameraPosition mCameraPosition;

    // The entry point to the Places API.
    private PlacesClient mPlacesClient;

    private ImageView idBack;
    private ImageView idLineModel;
    private boolean hideFlag = false;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final float DEFAULT_ZOOM = /*12.83f*/16;
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 0;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted = false;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private List[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;

    private Bundle bundle;

    private RefreshMyLocationManager myLocationManager;
    private Marker myMarker;
    private int createFlag = 1;
    private boolean createMayFlag = false;
    private FpShadowLayout shadowLayout;
    private LinearLayout merchantDialog;
    private ImageView logo;
    private CardView img_bg;
    private TextView learn_more;
    private RelativeLayout locationBtn;
    private TextView discount;
    private TextView categories;
    private TextView tags;
    private TextView tag1, tag2, tag3;
    private TextView merchantName;
    private LinearLayout tagLl;
    private TextView distance;
    private RelativeLayout mapRl;
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
                                Marker mPerth = mMap.addMarker(new MarkerOptions()
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
                        LatLng PERTH = new LatLng(Double.parseDouble(listData.get(i).getLat()), Double.parseDouble(listData.get(i).getLng()));
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
                        try {
                            if (BitmapDescriptorFactory.fromResource(mipId) != null) {
                                Marker mPerth = mMap.addMarker(new MarkerOptions()
                                        .position(PERTH)
                                        //.title(list.get(i).getPracticalName())
                                        .icon(BitmapDescriptorFactory.fromResource(mipId)));
                                mPerth.setTag(i);
                            }
                        } catch (Exception e) {

                        }

                    }
                    L.log("商户名称：" + listData.get(i).getPracticalName());
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
                    LatLng PERTH = new LatLng(Double.parseDouble(listData.get(i).getLat()), Double.parseDouble(listData.get(i).getLng()));
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
                    try {
                        if (BitmapDescriptorFactory.fromResource(mipId) != null) {
                            Marker mPerth = mMap.addMarker(new MarkerOptions()
                                    .position(PERTH)
                                    //.title(list.get(i).getPracticalName())
                                    .icon(BitmapDescriptorFactory.fromResource(mipId)));
                            mPerth.setTag(i);
                            L.log("商户名称：" + listData.get(i).getPracticalName());
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }
    };

    @Override
    public int getLayout() {
        return R.layout.fragment_find_maps;
    }


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
        this.bundle = savedInstanceState;
        mapView = (MapView) view.findViewById(R.id.mapview);
        locationBtn = (RelativeLayout) view.findViewById(R.id.my_location_btn);
        loactionRl = (RelativeLayout) view.findViewById(R.id.location_permission);
        shadowLayout = view.findViewById(R.id.shadow);
        merchantDialog = (LinearLayout) view.findViewById(R.id.merchant_tag);
        logo = (ImageView) view.findViewById(R.id.logo);
        discount = (TextView) view.findViewById(R.id.discount);
        categories = (TextView) view.findViewById(R.id.categories);
        tags = (TextView) view.findViewById(R.id.tags);
        merchantName = (TextView) view.findViewById(R.id.merchant_name);
        tagLl = (LinearLayout) view.findViewById(R.id.tag_ll);
        distance = (TextView) view.findViewById(R.id.distance);
        tag1 = (TextView) view.findViewById(R.id.tag1);
        tag2 = (TextView) view.findViewById(R.id.tag2);
        tag3 = (TextView) view.findViewById(R.id.tag3);
        img_bg = (CardView) view.findViewById(R.id.img_bg);
        openLoaction = (Button) view.findViewById(R.id.open_location);
        //切换字体
        TypefaceUtil.replaceFont(merchantName, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(categories, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(discount, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(distance, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(tags, "fonts/gilroy_regular.ttf");
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLastKnownLocation != null)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                merchantDialog.setVisibility(View.GONE);
                shadowLayout.setVisibility(View.GONE);
            }
        });

        openLoaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindMapFragment.this.requestPermissions(
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_FINE_LOCATION);
            }
        });

        /*RelativeLayout.LayoutParams lp= (RelativeLayout.LayoutParams) merchantDialog.getLayoutParams();
        lp.height = 117 * height / 812 ;
        lp.width = 343 * width / 375 ;
        merchantDialog.setLayoutParams(lp);

        LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) img_bg.getLayoutParams();
        lp1.height = 85 * height / 812 ;
        lp1.width = 110 * width / 375;
        img_bg.setLayoutParams(lp1);

        FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) logo.getLayoutParams();
        lp2.height = 85 * height / 812 ;
        lp2.width = 110 * width / 375;
        logo.setLayoutParams(lp2);

        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        learn_more.measure(w, h);
        int hh =learn_more.getMeasuredHeight();
        int ww =learn_more.getMeasuredWidth();

        LinearLayout.LayoutParams lp3 = (LinearLayout.LayoutParams) distance.getLayoutParams();
        lp3.height = 85 * height / 812;
        int distanceWidth = 343 * width / 375 - (110 * width / 375) - ww - 20 - 12 - 10 - 5 - 20;
        *//*tag1.setMaxWidth(65 * width / 375);
        tag2.setMaxWidth(65 * width / 375);
        tag3.setMaxWidth(65 * width / 375);*//*
        distance.setMaxWidth(distanceWidth);*/

        checkLocationPermission();
        return view;
    }

    private void createMapView() {
        mapView.onCreate(bundle);
        mapView.onResume();// needed to get the map to display immediately
    }


    public void getLayoutTop(Bundle savedInstanceState) {
        //从保存的实例状态中检索位置和摄像机位置。
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
    }

    public VisibleRegion getFourPoint() {
        if (mMap != null) {
            VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();
            return visibleRegion;
        } else {
            return null;
        }
    }


    @Override
    public void initView() {
        super.initView();
        // Construct a PlacesClient
        Places.initialize(getContext().getApplicationContext(), "AIzaSyC80QKGvifQBeg6uTmYpNayVAQXrkDTqxk");
        mPlacesClient = Places.createClient(getContext());

//        // Construct a FusedLocationProviderClient.
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        idBack = (ImageView) view.findViewById(R.id.id_back);
        idLineModel = (ImageView) view.findViewById(R.id.id_line_model);


//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        mapView.getMapAsync(this);
    }

    /**
     * 活动暂停时保存地图的状态。
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    public static FindMapFragment newInstance() {
        Bundle args = new Bundle();
        FindMapFragment fragment = new FindMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.parseDouble(listData.get(tagId).getLat()),
                            Double.parseDouble(listData.get(tagId).getLng())), DEFAULT_ZOOM));
        }
    }


    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                merchantDialog.setVisibility(View.GONE);
                shadowLayout.setVisibility(View.GONE);
            }
        });
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (mLastKnownLocation != null) {
                    LatLng myLatLng = new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude());
                    //mMap.addMarker(new MarkerOptions().position(myLatLng).title("My Location"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, DEFAULT_ZOOM));
                }

                return false;
            }
        });

        // 使用自定义信息窗口适配器可处理信息窗口内容中的多行文本。
//        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//
//            @Override
//            // Return null here, so that getInfoContents() is called next.
//            public View getInfoWindow(Marker arg0) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//                // Inflate the layouts for the info window, title and snippet.
////                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
////                        (FrameLayout) view.findViewById(R.id.map), false);
////
////                TextView title = infoWindow.findViewById(R.id.title);
////                title.setText(marker.getTitle());
////
////                TextView snippet = infoWindow.findViewById(R.id.snippet);
////                snippet.setText(marker.getSnippet());
////
////                return infoWindow;
//            }
//        });


        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        if (!hideFlag) {
            //reqFindList();
            // 获取设备的当前位置并设置地图的位置。
            getDeviceLocation();
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
        try {
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
                                        if (mLocationPermissionGranted) {
                                            reqFindList();
                                        }
                                        if (createFlag == 1) {
                                            createFlag = 0;
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                                    new LatLng(mLastKnownLocation.getLatitude(),
                                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        } else {
                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                                    new LatLng(mLastKnownLocation.getLatitude(),
                                                            mLastKnownLocation.getLongitude()), mMap.getCameraPosition().zoom));
                                        }
                                        if (myMarker != null) {
                                            myMarker.remove();
                                        }
                                        myMarker = mMap.addMarker(new MarkerOptions()
                                                .title("My Location")
                                                .position(new LatLng(mLastKnownLocation.getLatitude(),
                                                        mLastKnownLocation.getLongitude()))
                                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.my_location)));
                                    } else {
                                        //没有获取到位置信息,通过本地方法获取经纬度
                                        getGps();
                                    }

                                } else {
                                    Log.d(TAG, "Current location is null. Using defaults.");
                                    Log.e(TAG, "Exception: %s", task.getException());
                                    if (createFlag == 1) {
                                        createFlag = 0;
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                                new LatLng(mLastKnownLocation.getLatitude(),
                                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                    } else {
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                                new LatLng(mLastKnownLocation.getLatitude(),
                                                        mLastKnownLocation.getLongitude()), mMap.getCameraPosition().zoom));
                                    }
                                    //不显示定位按钮
                                    mMap.getUiSettings().setMyLocationButtonEnabled(false);
                                }
                            }
                        });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    private void getEndGps() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double lLatitude = location.getLatitude();
                            double lLongitude = location.getLongitude();
                            L.log("经度：" + lLongitude);
                            L.log("纬度：" + lLatitude);
                        }
                    }
                });
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
                    if (createFlag == 1) {
                        createFlag = 0;
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    } else {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), mMap.getCameraPosition().zoom));
                    }
                } else {
                    mLastKnownLocation = location;
                }

                if (myMarker != null) {
                    myMarker.remove();
                }
                // 添加默认标记，因为用户尚未选择位置。
                MarkerOptions MyMarker = new MarkerOptions()
                        //.title("My Location")
                        .position(new LatLng(mLastKnownLocation.getLatitude(),
                                mLastKnownLocation.getLongitude()))
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.my_location));

                myMarker = mMap.addMarker(MyMarker);

                if (mLocationPermissionGranted) {
                    reqFindList();
                }
            }
        });
        myLocationManager.initLocation();
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
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            mLocationPermissionGranted = false;
            /*ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);*/
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
            mapView.setVisibility(View.GONE);
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
                        FindMapFragment.this.requestPermissions(
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
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
                break;
            case PERMISSIONS_REQUEST_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    loactionRl.setVisibility(View.GONE);
                    mapView.setVisibility(View.VISIBLE);
                    createMayFlag = true;
                    createMapView();
                    if (getActivity() instanceof MainTab)
                        ((MainTab) getActivity()).getNowLocationDistance();
                } else {
                    loactionRl.setVisibility(View.VISIBLE);
                    mapView.setVisibility(View.GONE);
                }
                break;
        }
        updateLocationUI();
    }


    /**
     * 提示用户从可能的地点列表中选择当前地点，并在地图上显示当前地点（前提是用户已授予位置权限）。
     */
    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final Task<FindCurrentPlaceResponse> placeResult =
                    mPlacesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();

                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        mLikelyPlaceNames = new String[count];
                        mLikelyPlaceAddresses = new String[count];
                        mLikelyPlaceAttributions = new List[count];
                        mLikelyPlaceLatLngs = new LatLng[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            // Build a list of likely places to show the user.
                            mLikelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                            mLikelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                            mLikelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                    .getAttributions();
                            mLikelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        // Show a dialog offering the user the list of likely places, and add a
                        // marker at the selected place.
                        openPlacesDialog();
                    } else {
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // 添加默认标记，因为用户尚未选择位置。
            mMap.addMarker(new MarkerOptions()
                    .title("默认地点")
                    .position(mDefaultLocation)
                    .snippet("默认地点信息"));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    /**
     * 显示允许用户从可能的地点列表中选择地点的表单。
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = mLikelyPlaceLatLngs[which];
                String markerSnippet = mLikelyPlaceAddresses[which];
                if (mLikelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(mLikelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.pick_place)
                .setItems(mLikelyPlaceNames, listener)
                .show();
    }

    /**
     * 根据用户是否已授予位置权限来更新地图的UI设置。
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                //mMap.setMyLocationEnabled(true);
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void showMerchantDialog(int id) {
        merchantDialog.setVisibility(View.GONE);
        shadowLayout.setVisibility(View.GONE);
        if (id == -1) {
            return;
        }
        flag = id;
        mMap.clear();
        if (myMarker != null) {
            myMarker.remove();
        }
        // 添加默认标记，因为用户尚未选择位置。
        MarkerOptions MyMarker = new MarkerOptions()
                //.title("My Location")
                .position(new LatLng(mLastKnownLocation.getLatitude(),
                        mLastKnownLocation.getLongitude()))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.my_location));

        myMarker = mMap.addMarker(MyMarker);
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
                int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                tag1.measure(w, h);
                int hh =tag1.getMeasuredHeight();
                int ww =tag1.getMeasuredWidth();
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
                int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                tag1.measure(w, h);
                int hh =tag1.getMeasuredHeight();
                int ww =tag1.getMeasuredWidth();
                if (tagWidth - ww - 4 - 5 > 0) {
                    tag2.setMaxWidth(tagWidth - ww - 4);
                } else {
                    tag2.setVisibility(View.GONE);
                }
                int w1 = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                int h1 = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                tag2.measure(w, h);
                int hh1 =tag2.getMeasuredHeight();
                int ww1 =tag2.getMeasuredWidth();
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
        handler.sendEmptyMessage(2);

        //设置地图中心点
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(BRISBANE));
        //设置地图中心点和缩放级别，越大显示的地区越小
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BRISBANE, 14));

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                //VisibleRegion visibleRegion = mMap.getProjection().getVisibleRegion();
                merchantDialog.setVisibility(View.GONE);
                shadowLayout.setVisibility(View.GONE);
                if (marker.getTag() == null) {
                    return false;
                }
                showMerchantDialog((int) marker.getTag());
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
        /*if (!maps.get("lat").equals("") && !maps.get("lng").equals("")) {
            LatLng myLatLng = new LatLng((Double.parseDouble((String) appMsgSharePreferenceUtils.get(StaticParament.LAT, ""))),
                    (Double.parseDouble((String) appMsgSharePreferenceUtils.get(StaticParament.LNG, ""))));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, DEFAULT_ZOOM));
        }*/

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
        if (maps.get("lat") != null && maps.get("lng") != null)
            if (!maps.get("lat").equals("") && !maps.get("lng").equals("")) {
                loadFindList.getFindList(maps);
            }
    }

    private List<MerchantInfoData> listData = new ArrayList();


    @Override
    public void onResume() {
        super.onResume();
        if (mLocationPermissionGranted && !hideFlag) {
            mapView.onResume();
        }
        Log.i("SiteMapFragment", "onResume");


    }

    @Override
    public void onDetach() {
        // TODO Auto-generated method stub
        super.onDetach();
        Log.i("SiteMapFragment", "onDetach");
    }

    @Override
    public void onAttach(Context context) {
        // TODO Auto-generated method stub
        super.onAttach(context);
        Log.i("SiteMapFragment", "onAttach");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationPermissionGranted) {
            mapView.onPause();
        }
        Log.i("SiteMapFragment", "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view = null;
        if (mLocationPermissionGranted) {
            mapView.onDestroy();
        }
        if (myLocationManager != null) {
            myLocationManager.destoryLocationManager();
        }
        ListenerManager.getInstance().unRegisterListener(this);
        Log.i("SiteMapFragment", "onDestroy");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mLocationPermissionGranted) {
            mapView.onLowMemory();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("SiteMapFragment", "onDestroyView");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        /*if (!hidden) {
            hideFlag = false;
            if (mMap != null) {
                mMap.clear();
            }
            getDeviceLocation();
            if (mLocationPermissionGranted) {
                //reqFindList();
            } else {
                checkLocationPermission();
            }
            onResume();
        } else {
            hideFlag = true;
            if (myLocationManager != null) {
                myLocationManager.destoryLocationManager();
            }
            onPause();
            merchantDialog.setVisibility(View.GONE);
            shadowLayout.setVisibility(View.GONE);
        }*/
    }

    @Override
    public void notifyAllActivity(String title, String str) {
        Log.i("wuhao", "谷歌map收到广播");
        if (str.equals("getPermission")) {
            if (mMap == null && !createMayFlag) {
                createMayFlag = true;
                mLocationPermissionGranted = true;
                loactionRl.setVisibility(View.GONE);
                mapView.setVisibility(View.VISIBLE);
                Log.i("wuhao", "创建google map");
                createMapView();
            }
        }
    }
}
