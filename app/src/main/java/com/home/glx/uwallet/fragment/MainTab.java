package com.home.glx.uwallet.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.home.glx.uwallet.MyApplication;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.FenQuBanks_Activity;
import com.home.glx.uwallet.activity.NFCActivity;
import com.home.glx.uwallet.activity.NewMerchantInfoActivity;
import com.home.glx.uwallet.activity.PayMoneyActivity;
import com.home.glx.uwallet.activity.SelectPayTypeActivity;
import com.home.glx.uwallet.activity.SetPinPwd_Activity;
import com.home.glx.uwallet.activity.xzc.LoginActivity_inputNumber;
import com.home.glx.uwallet.datas.AboutDatas;
import com.home.glx.uwallet.datas.MessageEvent;
import com.home.glx.uwallet.datas.TabDatas;
import com.home.glx.uwallet.mvp.AboutMsg_in;
import com.home.glx.uwallet.mvp.AboutMsg_p;
import com.home.glx.uwallet.mvp.ListenerManager;
import com.home.glx.uwallet.mvp.PhoneLocation_in;
import com.home.glx.uwallet.mvp.PinNumber_in;
import com.home.glx.uwallet.mvp.ScanCodeData_in;
import com.home.glx.uwallet.mvp.ScanCodeData_p;
import com.home.glx.uwallet.mvp.WhetherOpenInvest_in;
import com.home.glx.uwallet.mvp.WhetherOpenInvest_p;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.MerchantListener;
import com.home.glx.uwallet.mvp.model.MerchantModel;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.NfcUtils;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.SystemUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.AppSettingsDialogHolderActivity;
import retrofit2.Call;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.home.glx.uwallet.activity.xzc.PaySuccessActivity_Zhifu.showLeft;
import static com.home.glx.uwallet.tools.SystemUtils.getDeviceBrand;
import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

public class MainTab extends NFCActivity
        implements View.OnClickListener, WhetherOpenInvest_in.View,
        ScanCodeData_in.View, AboutMsg_in.View, PinNumber_in {

    private boolean checkUpdateFlag = false;
    private FirstFragment firstFragment;
    private FrameLayout idTabFrame;
    private LinearLayout idHomeLay;
    private ImageView idHomeImg;
    private TextView idHomeText, idLicaiText, idFindText, idUserText;
    private LinearLayout idLicaiLay;
    private ImageView idLicaiImg;
    private ImageView idScanImg;
    private LinearLayout idFindLay;
    private ImageView idFindImg;
    private LinearLayout idUsreLay;
    private ImageView idUserImg;
    private RelativeLayout mainRl;
    private LinearLayout bottomLl;

    private List<ImageView> tabImages = new ArrayList<>();

    //?????????????????????
    private WhetherOpenInvest_p openInvestPresent;
    private AboutMsg_p aboutMsg_p;
    private ScanCodeData_p scanImgPresnet;
//    private PinNumberAbout pinNumberAbout;

    private String nfcId;
    private String qrId;
    private String posId;

    public boolean isHaveUpdateFlag() {
        return checkUpdateFlag;
    }

    public void setHaveUpdateFlag(boolean checkUpdateFlag) {
        this.checkUpdateFlag = checkUpdateFlag;
    }

    public void setChangeFlag(boolean changeFlag) {
        this.changeFlag = changeFlag;
    }

    public boolean isChangeFlag() {
        return changeFlag;
    }

    private boolean changeFlag = true;

    FragmentManager fm;

    private boolean mLocationPermissionGranted;

    private ViewPager viewPagerGuide;
    private int[] images = {R.drawable.guide1, R.drawable.guide2, R.drawable.guide3};
    private List<ImageView> imageViews;//??????????????????imageview?????????
    private int currentItem = 0;
    private MerchantListener merchantListener;
    public static boolean exit = false;


    private String provider;//???????????????
    private LocationManager locationManager;//????????????
    private Location location;

    @Override
    public int getLayout() {
        return R.layout.activity_maintab;
    }

    public static boolean showLocationToast = true;//??????????????????????????????

//    @SuppressLint("MissingSuperCall")
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//????????????
        int regist = getIntent().getIntExtra("regist", 0);
        if (regist == 1) {
            Intent intentPay = new Intent(this, SelectPayTypeActivity.class);
            startActivity(intentPay);
            return;
        }
        int noResult = getIntent().getIntExtra("noResult", 0);
        if (noResult == 1) {
//            Intent intentPay = new Intent(this, FenQiRepayListActivity.class);
//            startActivity(intentPay);
            return;
        }
        String merchantId = getIntent().getStringExtra("merchantId");
        if (merchantId != null) {
            Intent intent1 = new Intent(MainTab.this, NewMerchantInfoActivity.class);
            intent1.putExtra("id", merchantId);
            startActivity(intent1);
            return;
        }
        int num = getIntent().getIntExtra("num", 5);
        if (num != 5) {
            changTab(num, 1);
        }
        Log.i("wuhao", "singleTop???");
    }


    @Override
    public void initView() {
        super.initView();
        if (exit) {
            finish();
        }
        activityList.add(this);

        merchantListener = new MerchantModel(this);
        fm = getSupportFragmentManager();
        MyApplication.openPage = "1";
//        pinNumberAbout = new PinNumberAbout(MainTab.this, this);
        //??????PIN
//        pinNumberAbout.selectPin(new HashMap<String, Object>());


        aboutMsg_p = new AboutMsg_p(this, this);
        scanImgPresnet = new ScanCodeData_p(this, this);
        openInvestPresent = new WhetherOpenInvest_p(this, this);

        //??????
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        int regist = getIntent().getIntExtra("regist", 0);
        if (regist == 1) {
            Intent intentPay = new Intent(this, SelectPayTypeActivity.class);
            startActivity(intentPay);
        }

        String check = getIntent().getStringExtra("haveCheckUpdate");
        if (check != null && check.equals("1")) {
            setHaveUpdateFlag(true);
        }

        mainRl = (RelativeLayout) findViewById(R.id.main_rl);
        idTabFrame = (FrameLayout) findViewById(R.id.id_tab_frame);
        idHomeLay = (LinearLayout) findViewById(R.id.id_home_lay);
        idHomeText = (TextView) findViewById(R.id.id_home_text);
        idLicaiText = (TextView) findViewById(R.id.id_licai_text);
        idFindText = (TextView) findViewById(R.id.id_find_text);
        idUserText = (TextView) findViewById(R.id.id_user_text);
        idHomeImg = (ImageView) findViewById(R.id.id_home_img);
        idLicaiLay = (LinearLayout) findViewById(R.id.id_licai_lay);
        idLicaiImg = (ImageView) findViewById(R.id.id_licai_img);
        idScanImg = (ImageView) findViewById(R.id.id_scan_img);
        idFindLay = (LinearLayout) findViewById(R.id.id_find_lay);
        idFindImg = (ImageView) findViewById(R.id.id_find_img);
        idUsreLay = (LinearLayout) findViewById(R.id.id_usre_lay);
        idUserImg = (ImageView) findViewById(R.id.id_user_img);
        bottomLl = (LinearLayout) findViewById(R.id.bottom_ll);

        idHomeLay.setOnClickListener(this);
        idLicaiLay.setOnClickListener(this);
        idScanImg.setOnClickListener(this);
        idFindLay.setOnClickListener(this);
        idUsreLay.setOnClickListener(this);

        addFragment();
        aboutMsg_p.getAboutMsg(new HashMap<String, Object>());

        tabImages.clear();
        tabImages.add(idHomeImg);
        tabImages.add(idLicaiImg);
        tabImages.add(idFindImg);
        tabImages.add(idUserImg);

        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_TOKEN, "");
//        if (!userId.equals("")) {
//            getNowLocation();
//        }
        changTab(getIntent().getIntExtra("num", 0), 1);


//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);//??????????????????
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_LOW);//?????????????????????????????????????????????????????????location???
//        criteria.setAltitudeRequired(false);//???????????????
//        criteria.setBearingRequired(false);//???????????????
//        criteria.setCostAllowed(true);// ???????????????
//        criteria.setPowerRequirement(Criteria.POWER_LOW);//?????????
//
//        provider = locationManager.getBestProvider(criteria, true);
//
////        provider = judgeProvider(locationManager);
//        Log.d("lsslslsl", "initView: " + provider);
//        if (provider != null) {//???????????????????????????
//            //????????????getLastKnownLocation???????????????
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//            location = locationManager.getLastKnownLocation(provider);
//            if (location != null) {
//                Toast.makeText(this, location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
//
//            } else {
//
//                Toast.makeText(this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
//            }
//            locationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER,
//                    10,
//                    10, new LocationListener() {
//                        @Override
//                        public void onLocationChanged(Location location) {
//                            Toast.makeText(MainTab.this, location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
//
//                        }
//
//                        @Override
//                        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//                        }
//
//                        @Override
//                        public void onProviderEnabled(String provider) {
//
//                        }
//
//                        @Override
//                        public void onProviderDisabled(String provider) {
//
//                        }
//                    });
//            Log.d("GPS", "GPS Enabled");
//            if (locationManager != null) {
//                location = locationManager
//                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                if (location != null) {
//                    Toast.makeText(this, location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        } else {//?????????????????????????????????
//            Toast.makeText(this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
//
//        }
    }

    /**
     * ???????????????????????????????????????
     *
     * @return ???????????????null
     */
    private String judgeProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if (prodiverlist.contains(NETWORK_PROVIDER)) {
            return NETWORK_PROVIDER;
        } else if (prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        } else {
            Toast.makeText(MainTab.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    public void setClickable(boolean flag) {
        bottomLl.setEnabled(flag);
    }

    public void addFragment(int tabBum) {
        addFragment();
    }

    public void addFragment() {
        //Fragment homeFragment = HomeFragment.newInstance();
        String mobileCompany = getDeviceBrand();
        Fragment homeFragment = null;
        firstFragment = null;
//        Fragment licaiFragemnt = LiCaiFragment.newInstance();
        Fragment licaiFragemnt = null;
        Fragment findFragment = null;
        Fragment hwMapFragment = null;
        Fragment userFragment = null;
        tabList.clear();
        ArrayList<HashMap<String, Object>> items = SystemUtils.getItems(this);
 /*       for (int i = 0; i < items.size(); i++) {
            if (items.get(i).get("packageName").equals("com.huawei.hwid")) {
                String version = items.get(i).get("versionName").toString();
                version = version.substring(0,version.indexOf("."));
                if (Integer.parseInt(version) < 4) {
                    Toast.makeText(this,"111111",Toast.LENGTH_SHORT).show();
                } else {
                    break;
                }
            } else {
                if (i == items.size() - 1) {
                    Toast.makeText(this,"111111",Toast.LENGTH_SHORT).show();
                }
            }
        }*/
        tabList.add(new TabDatas(R.string.home, R.mipmap.home_icon_of, R.mipmap.home_icon_on, homeFragment));
        tabList.add(new TabDatas(R.string.find, R.mipmap.find_icopn_of1, R.mipmap.find_icon_on1, findFragment));
        /*if (mobileCompany.equals("HONOR") || mobileCompany.equals("HUAWEI")) {
            tabList.add(new TabDatas(R.string.licai, R.mipmap.licai_icon_of, R.mipmap.licai_icon_on, hwMapFragment));
        } else {
            tabList.add(new TabDatas(R.string.licai, R.mipmap.licai_icon_of, R.mipmap.licai_icon_on, licaiFragemnt));
        }*/
        tabList.add(new TabDatas(R.string.licai, R.mipmap.licai_icon_of, R.mipmap.licai_icon_on, licaiFragemnt));
        tabList.add(new TabDatas(R.string.user, R.mipmap.user_bottom_icon, R.mipmap.user_icon_on, userFragment));
    }

    private boolean mFirst = true;

    @Override
    protected void onResume() {
        super.onResume();
        checkNfc();
        if (tabNum == 3 || tabNum == 4) {
            setGetLocaion(false);
        }
        if (tabNum == 0) {
            exe(false);
        }
        if (tabNum == 0) {
            if (((FirstFragment) tabList.get(0).getFragment()).jumpToSetting) {
                if (!isOpenLocService(this)) {//????????????????????????????????????????????????
                    ((FirstFragment) tabList.get(0).getFragment()).jump = false;

                } else {
                    ((FirstFragment) tabList.get(0).getFragment()).jump = true;
                    if (!mFirst) {
                        Log.d("xzcxzvcxzc", "locationSer: 4");

                        ((FirstFragment) tabList.get(0).getFragment()).locationSer();
                    }
                    mFirst = false;
                }
            }
            ((FirstFragment) tabList.get(0).getFragment()).jumpToSetting = false;
        }

        if (tabNum == 1) {
            if (((SearchFragment) tabList.get(1).getFragment()).jumpToSetting) {
                if (!isOpenLocService(this)) {//????????????????????????????????????????????????
                    ((SearchFragment) tabList.get(1).getFragment()).jump = false;
                } else {
                    ((SearchFragment) tabList.get(1).getFragment()).jump = true;
                }
                ((SearchFragment) tabList.get(1).getFragment()).locationSer();
            }
            ((SearchFragment) tabList.get(1).getFragment()).jumpToSetting = false;
        }
    }

    public static void alex() {
        ((FirstFragment) tabList.get(0).getFragment()).getHomeList();
    }

    /**
     * NFC????????????
     *
     * @param nfcId
     * @param nfcText
     */
    @Override
    public void nfcData(String nfcId, String nfcText) {
        if (!isLogin()) {
//            Intent intent_loging = new Intent(this, Login_Activity.class);
            Intent intent_loging = new Intent(this, LoginActivity_inputNumber.class);
            startActivity(intent_loging);
            finish();
        } else {
            //????????????
            this.nfcId = nfcId;
            openInvestPresent.loadOpenInvestStatus("nfc");
        }
    }

    public boolean fromFirst;

    public boolean isFromFirst() {
        return fromFirst;
    }

    public void setFromFirst(boolean fromFirst) {
        this.fromFirst = fromFirst;
    }

    public void change() {
        ((WalletFragment) tabList.get(2).getFragment()).refrsh();
    }

    public void changeTab(int num) {
        if (num == 2) {
            setFromFirst(true);
        } else {
            setFromFirst(false);
        }
        changTab(num);
    }

    private boolean click = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_home_lay:
                //??????
                changTab(0);
                click = true;
                break;
            case R.id.id_licai_lay:
                click = true;
                //??????
                changTab(1);
                break;
            case R.id.id_scan_img:
                if (!PublicTools.isFastClick()) {
                    return;
                }
                SharePreferenceUtils sharePreferenceUtils1 = new SharePreferenceUtils(MainTab.this, StaticParament.USER);
                String userId1 = (String) sharePreferenceUtils1.get(StaticParament.USER_ID, "");
                Map<String, Object> maps1 = new HashMap<>();
                maps1.put("userId", userId1);
                getAppHomePageReminder(maps1, 0);
                break;
            case R.id.id_find_lay:
                if (tabNum == 2) {
                    click = false;
                } else {
                    click = true;
                }
                //??????
                changTab(2);
                break;
            case R.id.id_usre_lay:
                click = true;
                //??????
                changTab(3);
                break;
        }
    }

    /**
     * ??????????????????
     */
    public boolean checkLocationPermission() {
        String[] PERMISSION_STORAGE =
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        try {
            if (hasPermissions(Objects.requireNonNull(this), PERMISSION_STORAGE)) {
                //?????????
                return true;
            } else {
                L.log("????????????");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void permissionStatus(int requestCode) {
        super.permissionStatus(requestCode);
        L.log("??????Code:" + requestCode);

        switch (requestCode) {
            case 116:
                if (tabNum == 0) {
                    if (requestCode == 106 && (((FirstFragment) tabList.get(0).getFragment()).locationQuanxian || checkLocationPermission())) {
//                ((SearchFragment) tabList.get(1).getFragment()).locationSer();
                        ((FirstFragment) tabList.get(0).getFragment()).xianshiListView();
                        return;
                    }
                }
                if (tabNum == 1) {
                    if (requestCode == 106 && ((SearchFragment) tabList.get(1).getFragment()).locationQuanxian) {
                        ((SearchFragment) tabList.get(1).getFragment()).locationSer();
                        return;
                    }
                }
                setOnGetGpsLoaction(new MainTab.OnGetGpsLoaction() {
                    @Override
                    public void getGps() {
                        ListenerManager.getInstance().sendBroadCast("getLocation");
                    }

                    @Override
                    public void notGetGps() {

                    }
                });
                getNowLocation();
                break;
            case 124:
                openScanImg();//????????????????????????????????????  xzc
                //????????????
//                jyscan();
                break;
            case 106:
                if (tabNum == 0) {
                    ((FirstFragment) tabList.get(0).getFragment()).aaa();
                }

//                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.DEVICE);
//                String locationFlag = (String) sharePreferenceUtils.get(StaticParament.LOCATION_FLAG, "");
//                if (locationFlag.equals("0")) {
//                    sharePreferenceUtils.put(StaticParament.LOCATION_FLAG,"1");
//                    sharePreferenceUtils.save();
//                    getLocationPermission(125);
//                } else {
//                    if (checkLocationPermission(false)) {
//                        if (!mLocationPermissionGranted) {
//                            mLocationPermissionGranted = true;
//                            checkLocationPermission(1);
//                            Log.i("wuhao", "????????????");
//                        }
//                    }
//                }
                break;
            case 125:
                //???????????????????????????????????????
                Log.i("??????????????????", "??????????????????");
                if (onGetLoactionPermission != null) {
                    onGetLoactionPermission.getPermission();
                }
                break;
        }
    }

    /**
     * ??????????????????
     */
    public boolean checkLocationPermission(boolean haveFlag) {
        String[] PERMISSION_STORAGE =
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        if (hasPermissions(this, PERMISSION_STORAGE)) {
            //?????????
            if (haveFlag) {
                mLocationPermissionGranted = true;
            }
            return true;
        } else {
            L.log("????????????");
            if (haveFlag) {
                mLocationPermissionGranted = false;
            }
            return false;
        }
    }


    @Override
    public void permissionDenied(int requestCode) {//????????????  ??????????????????  ????????????
        super.permissionDenied(requestCode);
        setGetLocaion(false);
        ((FirstFragment) tabList.get(0).getFragment()).yincangListView();
        if (requestCode == 116 && tabNum == 0) {
            ((FirstFragment) tabList.get(0).getFragment()).getChangedList();
        }
        L.log("??????Code:" + requestCode);
        switch (requestCode) {
            case 125:
                //???????????????????????????????????????
                Log.i("??????????????????", "??????????????????");
                onGetLoactionPermission.getPermission();
                break;
        }
    }

    @Override
    public void haveLoactionStatus() {
        super.haveLoactionStatus();
        //??????????????????
        onGetLoactionPermission.getPermission();
    }

    public OnGetLoactionPermission onGetLoactionPermission;

    interface OnGetLoactionPermission {
        void getPermission();
    }

    public void setOnGetLoactionPermission(OnGetLoactionPermission onGetLoactionPermission) {
        this.onGetLoactionPermission = onGetLoactionPermission;
    }

    public void scanImg() {
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_TOKEN, "");
        if (userId.equals("")) {
//            Intent intent_loging = new Intent(this, Login_Activity.class);
            Intent intent_loging = new Intent(this, LoginActivity_inputNumber.class);
            startActivity(intent_loging);
            return;
        }
        //??????????????????
        getCameraPermission(124);
    }

    public void jyscan() {
        openInvestPresent.loadOpenInvestStatus("sm");
    }

    /**
     * ??????????????????
     */
    public void openScanImg() {
//        Intent intent = new Intent(this, CaptureActivity.class);
//        startActivityForResult(intent, 123);
        Intent intent = new Intent(this, CaptureActivity.class);
        ZxingConfig config = new ZxingConfig();
        // config.setPlayBeep(false);//???????????????????????? ?????????true
        // config.setShake(false);//????????????  ?????????true
        // config.setDecodeBarCode(false);//????????????????????? ?????????true
        config.setReactColor(R.color.text_orange);//????????????????????????????????? ???????????????
        // config.setFrameLineColor(R.color.colorAccent);//??????????????????????????? ????????????
        // config.setScanLineColor(R.color.colorAccent);//???????????????????????? ????????????
        config.setFullScreenScan(false);//??????????????????  ?????????true  ??????false??????????????????????????????
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        startActivityForResult(intent, 123);
    }

    public static boolean aaaa = true;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("lsllslslsls", "onActivityResult: " + requestCode);
        if (requestCode == 16061 && resultCode == RESULT_CANCELED) {
            if (tabNum == 0) {
                ((FirstFragment) tabList.get(0).getFragment()).yincangListView();
//                ((FirstFragment) tabList.get(0).getFragment()).getChangedList();
            }
            aaaa = false;
        }
        if (requestCode == AppSettingsDialogHolderActivity.APP_SETTINGS_RC) {//???????????????????????????????????????

            String[] PERMISSION_STORAGE =
                    new String[]{Manifest.permission.CAMERA};
            if (hasPermissions(this, PERMISSION_STORAGE)) {
//                tabList.
            }
        }

        if (requestCode == 123 && resultCode == RESULT_CANCELED && tabNum == 1) {//?????????????????????
            ((SearchFragment) tabList.get(1).getFragment()).show(100);
        }
        if (requestCode == 123 && resultCode == RESULT_OK) {
            String dataText = data.getStringExtra(Constant.CODED_CONTENT);
//            String dataText = data.getStringExtra("data");
            L.log("???????????????" + dataText);//https://qrcode.payo.com.au?qrCode=597924560232861696
            try {
                JSONObject jsonObject = new JSONObject(dataText);
                String code = jsonObject.getString("code");
                if (TextUtils.isEmpty(code)) {
                    //??????????????????
                    TiShiDialog tiShiDialog = new TiShiDialog(this);
                    tiShiDialog.ShowDialog(getString(R.string.qr_code_invalid));
                } else {
                    scanImgPresnet.loadImgCodeData(code);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                String[] c_list = dataText.split("=");
                if (c_list.length > 1) {
                    String qrCode = c_list[1];
                    if (dataText.contains("posNo")) {
                        getPosDetail(qrCode);
                    } else {
                        scanImgPresnet.loadImgCodeData(qrCode);
                    }
                }
            }
        }
    }

    private void getPosDetail(final String posCode) {
        Map<String, Object> maps = new HashMap<>();
        maps.put("transNo", posCode);
        merchantListener.showPosTransAmount(maps, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                String transAmount = (String) o[0];
                String merchantId = (String) o[1];
                Intent intent = new Intent(MainTab.this, PayMoneyActivity.class);
                intent.putExtra("transAmount", transAmount);
                intent.putExtra("merchantId", merchantId);
                intent.putExtra("posTransNo", posCode);
                intent.putExtra("type", "00");
                startActivity(intent);
            }

            @Override
            public void onError(String e) {

            }
        });
    }

    public void changTab(final int num, int type) {
        if (type != 1 && tabNum == num) {
            return;
        }
        tabNum = num;
        reductionTabStyle();
        idHomeText.setTextColor(0xffCBCED5);
        idLicaiText.setTextColor(0xffCBCED5);
        idFindText.setTextColor(0xffCBCED5);
        idUserText.setTextColor(0xffCBCED5);
        //????????????
        TypefaceUtil.replaceFont(idHomeText, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(idLicaiText, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(idFindText, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(idUserText, "fonts/gilroy_semiBold.ttf");
        if (num == 0) {
            TypefaceUtil.replaceFont(idHomeText, "fonts/gilroy_bold.ttf");
            idHomeText.setTextColor(getResources().getColor(R.color.end_orange));
        }
        if (num == 1) {
            TypefaceUtil.replaceFont(idLicaiText, "fonts/gilroy_bold.ttf");
            idLicaiText.setTextColor(getResources().getColor(R.color.end_orange));
        }
        if (num == 2) {
            TypefaceUtil.replaceFont(idFindText, "fonts/gilroy_bold.ttf");
            idFindText.setTextColor(getResources().getColor(R.color.end_orange));
        }
        if (num == 3) {
            TypefaceUtil.replaceFont(idUserText, "fonts/gilroy_bold.ttf");
            idUserText.setTextColor(getResources().getColor(R.color.end_orange));
        }
        tabImages.get(num).setImageResource(tabList.get(num).getOn_img());
        // ??????Fragment??????
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
/*        // ????????????Fragment???????????????id_content?????????
        transaction.replace(R.id.id_tab_frame, tabList.get(num).getFragment());
        if (type == 1) {
            transaction.commitAllowingStateLoss();
        } else {
            transaction.commit();
        }*/
        Fragment homeFragment = FirstFragment.newInstance();
        firstFragment = FirstFragment.newInstance();
        Fragment licaiFragemnt = WalletFragment.newInstance();
        Fragment findFragment = SearchFragment.newInstance();
        Fragment userFragment = MineFragment.newInstance();
        if (tabList.get(num).getFragment() == null) {
            if (num == 0) {
                transaction.add(R.id.id_tab_frame, homeFragment);
                tabList.get(num).setFragment(homeFragment);
            }
            if (num == 1) {
                transaction.add(R.id.id_tab_frame, findFragment);
                tabList.get(num).setFragment(findFragment);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        ((SearchFragment) tabList.get(1).getFragment()).show(150);
//
//                    }
//                }, 200);
            }
            if (num == 2) {
                /*String mobileCompany = getDeviceBrand();
                if (mobileCompany.equals("HONOR") || mobileCompany.equals("HUAWEI")) {
                    transaction.add(R.id.id_tab_frame, hwMapFragment);
                    tabList.get(num).setFragment(hwMapFragment);
                } else {
                    transaction.add(R.id.id_tab_frame, licaiFragemnt);
                    tabList.get(num).setFragment(licaiFragemnt);
                }*/
                transaction.add(R.id.id_tab_frame, licaiFragemnt);
                tabList.get(num).setFragment(licaiFragemnt);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        if (getIntent().getBooleanExtra("showleft", false)) {
                        if (showLeft) {
                            ((WalletFragment) tabList.get(num).getFragment()).setTabSelection(0);
                        } else {
                            if (click) {
                                ((WalletFragment) tabList.get(num).getFragment()).setTabSelection(1);
                            }
                        }
                        showLeft = false;
                    }
                }, 300);
            }
            if (num == 3) {
                transaction.add(R.id.id_tab_frame, userFragment);
                tabList.get(num).setFragment(userFragment);
            }

        } else {
            transaction.show(tabList.get(num).getFragment());
            if (num == 1) {
                ((SearchFragment) tabList.get(1).getFragment()).show(0);

            }
            if (num == 2) {
                if (showLeft) {
                    ((WalletFragment) tabList.get(num).getFragment()).setTabSelection(0);
                } else {
                    if (click) {
                        ((WalletFragment) tabList.get(num).getFragment()).setTabSelection(1);
                    }
                }
                showLeft = false;
            }
        }
        click = false;
        transaction.commit();
    }

    //????????????fragment
    private void hideFragment(FragmentTransaction ft) {
        for (int i = 0; i < tabList.size(); i++) {
            if (tabList.get(i).getFragment() != null) {
                ft.hide(tabList.get(i).getFragment());
            }
        }
    }

    /**
     * ??????fragment
     */
    public void changTab(int num) {
        if (isChangeFlag()) {
            changTab(num, 0);
        }
    }


    private void reductionTabStyle() {
        for (int i = 0; i < tabList.size(); i++) {
            tabImages.get(i).setImageDrawable(getResources().getDrawable(tabList.get(i).getOf_img()));
        }
    }

    /**
     * ????????????
     *
     * @param data
     */
    @Override
    public void setScanData(String type, String data) {
        Intent intent = new Intent(MainTab.this, PayMoneyActivity.class);
        intent.putExtra("data", data);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    private void getAppHomePageReminder(Map<String, Object> maps, int flag) {//0 ?????? ???1 nfc

        RequestUtils requestUtils1 = new RequestUtils(MainTab.this, maps, StaticParament.MainUrl,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(MainTab.this, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("??????PIN?????????" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getAppHomePageReminder(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        try {
                            JSONObject jsonObject = new JSONObject(dataStr);
                            if (jsonObject.has("stripeState")) {                    //????????????stripe?????????0?????? 1??????
                                if (jsonObject.getString("stripeState").equals("0")) {
                                    if (flag == 0) {
                                        //??????
                                        scanImg();
                                    } else {
                                        //??????????????????
                                        scanImgPresnet.loadNfcCodeData(nfcId);
                                        MainTab.this.nfcId = "";
                                    }
                                } else if (jsonObject.getString("stripeState").equals("1")) {
                                    if (NfcUtils.nfcPhone(MainTab.this)) {
                                        //????????????????????????
                                        if (CaptureActivity.mNfcAdapter != null) {
                                            CaptureActivity.mNfcAdapter.disableForegroundDispatch(MainTab.this);
                                        }
                                    }
                                    TiShiDialog tiShiDialog = new TiShiDialog(MainTab.this);
                                    tiShiDialog.showStripeDialog();
                                    tiShiDialog.setOnGuanBiLeft(new TiShiDialog.GuanBiLeft() {
                                        @Override
                                        public void GuanBiLeft() {
                                            if (flag == 1) {//???????????????????????????nfc?????? ??????latpay????????????????????????????????????????????????nfc???????????????????????????nfc?????????????????????????????????????????????????????????app?????????nfc
                                                onResume();
                                            }
                                        }
                                    });
                                    tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                                        @Override
                                        public void GuanBi() {//?????????
                                            if (flag == 1) {
                                                onResume();
                                            }
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        L.log("??????PIN:" + dataStr);
                    }

                    @Override
                    public void resError(String error) {


                    }
                });
    }

    /**
     * ??????????????????
     *
     * @param aboutMsg
     */
    @Override
    public void setAboutMsg(AboutDatas aboutMsg) {
        if (aboutMsg != null) {
            SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.DEVICE);
            sharePreferenceUtils.put(StaticParament.APP_EMAIL, aboutMsg.getEmail());
            sharePreferenceUtils.put(StaticParament.APP_PHONE, aboutMsg.getPhone());
            sharePreferenceUtils.save();
            L.log("????????????");
        }
    }

    @Override
    public void openInvestStatus(String code, String licai, String zhifu, String fenqi) {
        if (zhifu != null) {
            zhiFuStatusNext(code, zhifu, fenqi);
        }
    }

    /**
     * ????????????????????????????????????
     */
    public void zhiFuStatusNext(String code, String zhifu, String fenqi) {
        //???????????????????????????
        if ((fenqi.equals("0") || fenqi.equals("5") || fenqi.equals("2")) && zhifu.equals("0")) {
            scanImgPresnet.loadNfcCodeData(nfcId);
            this.nfcId = "";
        } else {
            if (code.equals("sm")) {
                //??????
                openScanImg();
            } else if (code.equals("nfc")) {
                SharePreferenceUtils sharePreferenceUtils1 = new SharePreferenceUtils(MainTab.this, StaticParament.USER);
                String userId1 = (String) sharePreferenceUtils1.get(StaticParament.USER_ID, "");
                Map<String, Object> maps1 = new HashMap<>();
                maps1.put("userId", userId1);
                getAppHomePageReminder(maps1, 1);

            } else if (code.equals("qrCode")) {
                scanImgPresnet.loadImgCodeData(qrId);
                this.qrId = "";
            } else if (code.equals("posNo")) {
                getPosDetail(posId);
                this.posId = "";
            }
        }
        /*if (!fenqi.equals("1")) {
            TiShiDialog tiShiDialog = new TiShiDialog(this);
            tiShiDialog.ShowDialog(getString(R.string.please_Pay_Later));
        } else {
            switch (zhifu) {
                case "1":
                case "5":
                    //??????
                    if (code.equals("sm")) {
                        //??????
                        openScanImg();
                    } else if (code.equals("nfc")) {
                        //??????????????????
                        scanImgPresnet.loadNfcCodeData(nfcId);
                        this.nfcId = "";
                    }
                    break;

                case "0":
                    //?????????
                    Intent intent_msg = new Intent(MainTab.this,
                            KYCZhiFuActivity.class);
                    startActivity(intent_msg);
                    break;

                case "2":
                case "4":
                    //????????????
                    jujue();
                    break;
                case "3":
                    //?????????
                    shenhe();
                    break;
            }
        }*/
    /*    switch (zhifu) {
            case "1":
            case "5":
                //??????
                if (code.equals("sm")) {
                    //??????
                    openScanImg();
                } else if (code.equals("nfc")) {
                    //??????????????????
                    scanImgPresnet.loadImgCodeData(nfcId);
                    this.nfcId = "";
                }
                break;

            case "0":
                //?????????
                Intent intent_msg = new Intent(MainTab.this,
                        KYCZhiFuActivity.class);
                startActivity(intent_msg);
                break;

            case "2":
            case "4":
                //????????????
                jujue();
                break;
            case "3":
                //?????????
                shenhe();
                break;
        }*/
    }

    public OnGetGpsLoaction onGetGpsLoaction;

    interface OnGetGpsLoaction {
        void getGps();

        void notGetGps();
    }

    public void setOnGetGpsLoaction(OnGetGpsLoaction onGetGpsLoaction) {
        this.onGetGpsLoaction = onGetGpsLoaction;
    }

    /**
     * ??????????????????
     */
    public void getLocation() {
        getLocation(new PhoneLocation_in() {
            @Override
            public void notGetLocation() {

            }

            @Override
            public void setNewLocation(Location location) {
                Log.e("?????????", location.getLongitude() + "");
                Log.e("?????????", location.getLatitude() + "");
                SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(MainTab.this, StaticParament.APP);
                if (appMsgSharePreferenceUtils.get(StaticParament.LAT, "").equals(location.getLongitude()) &&
                        appMsgSharePreferenceUtils.get(StaticParament.LNG, "").equals(location.getLatitude())) {

                } else {
                    appMsgSharePreferenceUtils.put(StaticParament.LAT, location.getLatitude() + "");
                    appMsgSharePreferenceUtils.put(StaticParament.LNG, location.getLongitude() + "");
                    appMsgSharePreferenceUtils.save();
                }
                /*PostUserLocation postUserLocation = new PostUserLocation(MainTab.this);
                postUserLocation.postLocation(location.getLatitude(), location.getLongitude());*/
            }

            @Override
            public void setDefaultLocation() {

            }
        });
    }

    /**
     * ??????????????????
     */
    public void getNowLocation() {
        showLocationToast = false;
        getLocation(new PhoneLocation_in() {
            @Override
            public void notGetLocation() {
                if (onGetGpsLoaction != null) {
                    onGetGpsLoaction.notGetGps();
                }
            }

            @Override
            public void setNewLocation(Location location) {
                Log.e("??????1111???", location.getLongitude() + "");
                Log.e("??????1111???", location.getLatitude() + "");
                SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(MainTab.this, StaticParament.APP);
               /* if (appMsgSharePreferenceUtils.get(StaticParament.LAT, "").equals(location.getLongitude()) &&
                        appMsgSharePreferenceUtils.get(StaticParament.LNG, "").equals(location.getLatitude())) {

                } else {
                    String nowLat = (String) appMsgSharePreferenceUtils.get(StaticParament.LAT, "");
                    String nowLng = (String) appMsgSharePreferenceUtils.get(StaticParament.LNG, "");
                    String newLat = String.valueOf(location.getLatitude());
                    String newLng = String.valueOf(location.getLongitude());*/
//                    if (nowLat.equals(newLat) &&
//                            nowLng.equals(newLng)) {
//
//                    } else {
                appMsgSharePreferenceUtils.put(StaticParament.LAT, location.getLatitude() + "");
                appMsgSharePreferenceUtils.put(StaticParament.LNG, location.getLongitude() + "");
                appMsgSharePreferenceUtils.save();
                if (onGetGpsLoaction != null) {
                    onGetGpsLoaction.getGps();
                }
//                    }
                //}
                /*PostUserLocation postUserLocation = new PostUserLocation(MainTab.this);
                postUserLocation.postLocation(location.getLatitude(), location.getLongitude());*/
            }

            @Override
            public void setDefaultLocation() {

            }
        });
    }

    /**
     * ??????????????????
     */
    public void getNowLocation(final boolean refresh) {
        getLocation(new PhoneLocation_in() {
            @Override
            public void notGetLocation() {

            }

            @Override
            public void setNewLocation(Location location) {
                Log.e("?????????", location.getLongitude() + "");
                Log.e("?????????", location.getLatitude() + "");
                SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(MainTab.this, StaticParament.APP);
                if (appMsgSharePreferenceUtils.get(StaticParament.LAT, "").equals(location.getLongitude()) &&
                        appMsgSharePreferenceUtils.get(StaticParament.LNG, "").equals(location.getLatitude())) {

                } else {
                    String nowLat = (String) appMsgSharePreferenceUtils.get(StaticParament.LAT, "");
                    String nowLng = (String) appMsgSharePreferenceUtils.get(StaticParament.LNG, "");
                    String newLat = String.valueOf(location.getLatitude());
                    String newLng = String.valueOf(location.getLongitude());
                    if (refresh) {
                        appMsgSharePreferenceUtils.put(StaticParament.LAT, location.getLatitude() + "");
                        appMsgSharePreferenceUtils.put(StaticParament.LNG, location.getLongitude() + "");
                        appMsgSharePreferenceUtils.save();
                        onGetGpsLoaction.getGps();
                    } else {
                        if (nowLat.equals(newLat) &&
                                nowLng.equals(newLng)) {

                        } else {
                            appMsgSharePreferenceUtils.put(StaticParament.LAT, location.getLatitude() + "");
                            appMsgSharePreferenceUtils.put(StaticParament.LNG, location.getLongitude() + "");
                            appMsgSharePreferenceUtils.save();
                            onGetGpsLoaction.getGps();
                        }
                    }
                }
                /*PostUserLocation postUserLocation = new PostUserLocation(MainTab.this);
                postUserLocation.postLocation(location.getLatitude(), location.getLongitude());*/
            }

            @Override
            public void setDefaultLocation() {

            }
        });
    }

    /**
     * ??????????????????
     */
    public void getNowLocationDistance() {
        getLocation(new PhoneLocation_in() {
            @Override
            public void notGetLocation() {

            }

            @Override
            public void setNewLocation(Location location) {
                Log.e("?????????", location.getLongitude() + "");
                Log.e("?????????", location.getLatitude() + "");
                SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(MainTab.this, StaticParament.APP);
                if (appMsgSharePreferenceUtils.get(StaticParament.LAT, "").equals(location.getLongitude()) &&
                        appMsgSharePreferenceUtils.get(StaticParament.LNG, "").equals(location.getLatitude())) {

                } else {
                    appMsgSharePreferenceUtils.put(StaticParament.LAT, location.getLatitude() + "");
                    appMsgSharePreferenceUtils.put(StaticParament.LNG, location.getLongitude() + "");
                    appMsgSharePreferenceUtils.save();
                    ListenerManager.getInstance().sendBroadCast("getPermission");
                }
                //onGetGpsLoaction.getLocation();
                /*PostUserLocation postUserLocation = new PostUserLocation(MainTab.this);
                postUserLocation.postLocation(location.getLatitude(), location.getLongitude());*/
            }

            @Override
            public void setDefaultLocation() {

            }
        });
    }

    /**
     * ??????PIN??????
     *
     * @param pin
     */
    @Override
    public void selectPin(String pin) {
        Log.d("okssksksks", "selectPin: " + pin);
        if (TextUtils.isEmpty(pin)) {
            //????????????PIN
            Intent intent = new Intent(MainTab.this, SetPinPwd_Activity.class);
            startActivity(intent);
        }
    }

    @Override
    public void changePin(String status) {

    }

    @Override
    public void checkPin(String status) {

    }

    private void showPageDialog(final Context context) {

        // ???????????????
        final Dialog dialog = new Dialog(context, R.style.dialog_style);
        View view = View.inflate(context,
                R.layout.dialog_guide, null);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        //??????Dialog?????????????????????
        assert dialogWindow != null;
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(layoutParams);

        viewPagerGuide = (ViewPager) view.findViewById(R.id.viewPager);
        viewPagerGuide.setAdapter(new GuideAdapter());
        imageViews = new ArrayList<ImageView>();
        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(images[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);
            //??????LayoutParams?????????????????????
            LinearLayout.LayoutParams layoutParamsGuide = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                layoutParamsGuide.leftMargin = 20;
            }
        }
        viewPagerGuide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                currentItem = position;//???????????????????????????
            }

            //???????????????
            @Override
            public void onPageScrolled(int position, float posionOffset, int arg2) {
                // TODO Auto-generated method stub
                System.out.println(posionOffset);//??????????????????
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });

        viewPagerGuide.setOnTouchListener(new View.OnTouchListener() {
            float startX;
            float startY;//????????????
            float endX;
            float endY;//????????????

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        endY = event.getY();
                        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

                        //?????????????????????
                        Point size = new Point();
                        windowManager.getDefaultDisplay().getSize(size);
                        int width = size.x;

                        //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????4???????????????????????????????????????
                        if (currentItem == (imageViews.size() - 1) && startX - endX >= (width / 10)) {
                            //Toast.makeText(GuideActivity.this, "????????????",Toast.LENGTH_SHORT).show();
                            //???????????????
                            dialog.dismiss();
                            onitemClick.itemClick();
                        }
                        break;
                }
                return false;
            }
        });


        dialog.show();
    }

    class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(imageViews.get(position));
            return imageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
        }
    }

    public interface OnitemClick {
        void itemClick();
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }

    //??????scheme??????
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        Log.d("EvenBus", messageEvent.getMessage());
        handleData(messageEvent.getMessage());
        EventBus.getDefault().removeStickyEvent(messageEvent);
    }

    /**
     * ??????????????????URL
     *
     * @param data
     */
    private void handleData(String data) {
        if (!isLogin()) {
            Intent intent_loging = new Intent(this, LoginActivity_inputNumber.class);
            startActivity(intent_loging);
            finish();
        } else {
            L.log("url???" + data);
            String[] list = data.split("/");
            String last = list[list.length - 1];
            String[] idList = last.split("=");
            if (last.contains("posNo")) {
                posId = idList[idList.length - 1 /*2*/];
                if (TextUtils.isEmpty(posId) || posId.equals("null")) {
                    //??????????????????
                    TiShiDialog tiShiDialog = new TiShiDialog(this);
                    tiShiDialog.ShowDialog(getString(R.string.qr_code_invalid));
                } else {
                    openInvestPresent.loadOpenInvestStatus("posNo");
                }
            } else if (last.contains("qrCode")) {
                //????????????id
                qrId = idList[idList.length - 1 /*2*/];
                if (TextUtils.isEmpty(qrId) || qrId.equals("null")) {
                    //??????????????????
                    TiShiDialog tiShiDialog = new TiShiDialog(this);
                    tiShiDialog.ShowDialog(getString(R.string.qr_code_invalid));
                } else {
                    openInvestPresent.loadOpenInvestStatus("qrCode");
                }
            } else {
                //????????????id
                nfcId = idList[idList.length - 1 /*2*/];
                if (TextUtils.isEmpty(nfcId) || nfcId.equals("null")) {
                    TiShiDialog tiShiDialog = new TiShiDialog(this);
                    tiShiDialog.ShowDialog("NFC code invalid");
                } else {
                    L.log("nfc_id???" + nfcId);
                    openInvestPresent.loadOpenInvestStatus("nfc");
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //??????
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public interface MyOnTouchListener {
        public boolean onTouch(MotionEvent ev);
    }

    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(
            10);

    //    fragment??????????????????????????????????????????????????????????????????fragment
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            if (listener != null) {
                listener.onTouch(ev);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public static boolean isOpenLocService(final Context context) {
        boolean isGps = false; //??????GPS??????????????????

        boolean isNetwork = false; //??????????????????????????????

        if (context != null) {
            LocationManager locationManager

                    = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if (locationManager != null) {
//??????GPS?????????????????????????????????????????????(??????24?????????????????????????????????????????????????????????????????????)

                isGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

//??????WLAN???????????????(3G/2G)???????????????(?????????AGPS?????????GPS??????????????????????????????????????????(??????????????????????????????)?????????????????????)

                isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            }

            if (isGps || isNetwork) {
                return true;
            }
        }
        return false;
    }
}
