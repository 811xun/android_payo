package com.home.glx.uwallet.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.ImageLoader;
import com.home.glx.uwallet.datas.BannerDatas;
import com.home.glx.uwallet.datas.MerchantInfoData;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.mvp.PhoneLocation_in;
import com.home.glx.uwallet.mvp.WhetherOpenInvest_in;
import com.home.glx.uwallet.mvp.WhetherOpenInvest_p;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.MerchantListener;
import com.home.glx.uwallet.mvp.model.MerchantModel;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.ChooseGoogleMapDialog;
import com.home.glx.uwallet.selfview.DampingReboundNestedScrollView;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

public class NewMerchantInfoActivity extends MainActivity implements View.OnClickListener, WhetherOpenInvest_in.View {
    private DampingReboundNestedScrollView scrollView;
    private Banner idBanner;
    private ConstraintLayout checkPay;
    private ImageButton payBtn;
    private String id;
    private ImageView back;
    private ImageView copyIcon;
    private ImageView locationIcon;
    private ImageView callIcon;
    private TextView tags;
    private TextView merchantName;
    private TextView adress;
    private TextView discount;
    private TextView introduction;
    private TextView copy;
    private TextView getLocation;
    private TextView call;
    private TextView favoriteText;
    private TextView infoText;
    private TextView categoryText;
    private TextView category;
    private TextView aboutText;
    private ImageView addFavorite;

    private String phoneNum;
    private String loactionText;
    private String merchantId;
    private String lat;
    private String lng;
    //是否开通个业务
    private WhetherOpenInvest_p openInvestPresent;
    private MerchantListener merchantListener;
    private boolean fromMain = false;
    private boolean getCallBack = false;
    private String from;

    @Override
    public int getLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //设置修改状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏的颜色，和你的app主题或者标题栏颜色设置一致就ok了
            window.setStatusBarColor(getResources().getColor(R.color.white));
            //设置显示为白色背景，黑色字体
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        return R.layout.activity_merchant_info_new;
    }

    @Override
    public void initView() {
        super.initView();
        merchantListener = new MerchantModel(this);
        openInvestPresent = new WhetherOpenInvest_p(this, this);
        from = getIntent().getStringExtra("from");
        scrollView = (DampingReboundNestedScrollView) findViewById(R.id.scroll_view);
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        id = getIntent().getStringExtra("id");
        idBanner = (Banner) findViewById(R.id.id_banner);
        idBanner.setVisibility(View.INVISIBLE);
        initBanner();
        payBtn = findViewById(R.id.pay_btn);
        infoText = (TextView) findViewById(R.id.info_title);
        aboutText = (TextView) findViewById(R.id.about_text);
        categoryText = (TextView) findViewById(R.id.category_text);
        category = (TextView) findViewById(R.id.category);
        addFavorite = (ImageView) findViewById(R.id.is_favorite);
        tags = (TextView) findViewById(R.id.merchant_tag);
        copyIcon = (ImageView) findViewById(R.id.copy_icon);
        favoriteText = (TextView) findViewById(R.id.add_favorite);
        locationIcon = (ImageView) findViewById(R.id.merchant_location_icon);
        callIcon = (ImageView) findViewById(R.id.merchant_call_icon);
        back = (ImageView) findViewById(R.id.back);
        checkPay = (ConstraintLayout) findViewById(R.id.check_pay);
        merchantName = (TextView) findViewById(R.id.merchant_name);
        adress = (TextView) findViewById(R.id.merchant_location);
        discount = (TextView) findViewById(R.id.limit);
        introduction = (TextView) findViewById(R.id.introduction);
        copy = (TextView) findViewById(R.id.copy);
        getLocation = (TextView) findViewById(R.id.get_location);
        call = (TextView) findViewById(R.id.call);
        TextView payWith = findViewById(R.id.pay_with);
        if (from != null) {
            checkPay.setVisibility(View.VISIBLE);
            payBtn.setVisibility(View.GONE);
        }
        //切换字体
        TypefaceUtil.replaceFont(payWith, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(introduction, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(aboutText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(category, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(adress, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(categoryText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(infoText, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(favoriteText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(merchantName, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(tags, "fonts/gilroy_light.ttf");
        TypefaceUtil.replaceFont(discount, "fonts/gilroy_semiBold.ttf");
//        TypefaceUtil.replaceFont(payBtn, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(copy, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(getLocation, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(call, "fonts/gilroy_medium.ttf");
        getMerchantDetail();

        favoriteText.setOnClickListener(this);
        addFavorite.setOnClickListener(this);
        back.setOnClickListener(this);
        copy.setOnClickListener(this);
        payBtn.setOnClickListener(this);
        checkPay.setOnClickListener(this);
        getLocation.setOnClickListener(this);
        call.setOnClickListener(this);
    }

    private void initBanner() {
        //设置指示器位置（指示器居右）
        idBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载器
        idBanner.setImageLoader(new ImageLoader());
    }

    @Override
    public void onClick(View v) {
        if (!PublicTools.isFastClick()) {
            return;
        }
        int ids = v.getId();
        if (TextUtils.isEmpty(id)) {
            return;
        }
        switch (ids) {
            case R.id.back:
                finish();
                break;
            case R.id.copy:
                PublicTools.copyText(NewMerchantInfoActivity.this, loactionText);
                Toast.makeText(NewMerchantInfoActivity.this, "Copied successfully", Toast.LENGTH_SHORT).show();
                break;
            case R.id.check_pay:
            case R.id.pay_btn:
                //openInvestPresent.loadOpenInvestStatus("sm");
                locationSer();

                break;
            case R.id.get_location:
                if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lng)) {
                    return;
                }
                ChooseGoogleMapDialog chooseGoogleMapDialog = new ChooseGoogleMapDialog(this);
                chooseGoogleMapDialog.setOnUpdate(new ChooseGoogleMapDialog.Open() {
                    @Override
                    public void onOpen() {
                        startNaviGoogle();
                    }
                });
                chooseGoogleMapDialog.ShowDialog("");
                break;
            case R.id.call:
                /**
                 * 调用拨号界面
                 *
                 * @param phone 电话号码
                 */
                if (TextUtils.isEmpty(phoneNum)) {
                    return;
                }
                ChooseGoogleMapDialog chooseDialog = new ChooseGoogleMapDialog(this);
                chooseDialog.setOnUpdate(new ChooseGoogleMapDialog.Open() {
                    @Override
                    public void onOpen() {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                chooseDialog.ShowDialog(phoneNum);
                break;
            case R.id.add_favorite:
            case R.id.is_favorite:
                if (TextUtils.isEmpty(id)) {
                    return;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("merchantId", id);
                if (addFavorite.getTag().toString().equals("1")) {
                    map.put("isAdd", "0");
                    addFavorite.setImageResource(R.mipmap.not_save_icon);
                    addFavorite.setTag("0");
                    //favoriteText.setVisibility(View.VISIBLE);
                } else {
                    map.put("isAdd", "1");
                    addFavorite.setImageResource(R.mipmap.have_save_icon);
                    addFavorite.setTag("1");
                    //favoriteText.setVisibility(View.GONE);
                }
                merchantListener.addNewFavorite(map, new ActionCallBack() {
                    @Override
                    public void onSuccess(Object... o) {
                        MainTab.alex();
                    }

                    @Override
                    public void onError(String e) {
                        if (addFavorite.getTag().toString().equals("1")) {
                            addFavorite.setImageResource(R.mipmap.not_save_icon);
                            addFavorite.setTag("0");
                            //favoriteText.setVisibility(View.VISIBLE);
                        } else {
                            addFavorite.setImageResource(R.mipmap.have_save_icon);
                            addFavorite.setTag("1");
                            //favoriteText.setVisibility(View.GONE);
                        }
                    }
                });
                break;
        }
    }

    private JSONObject result = null;//data数据

    private void checkPay() {
        SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.APP);
        //纬度
        String latt = (String) appMsgSharePreferenceUtils.get(StaticParament.LAT, "");
        String lngg = (String) appMsgSharePreferenceUtils.get(StaticParament.LNG, "");
        if (latt.equals("") || lngg.equals("")) {
            Toast.makeText(NewMerchantInfoActivity.this, R.string.gps_not_use, Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("merchantId", id);
        maps.put("lng", lngg);
        maps.put("lat", latt);
        merchantListener.checkPayDistance(maps, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                try {
                    result = new JSONObject((String) o[0]);
                    //是否弹出stripe弹框：0：否 1：是
                    if (result.getInt("stripeState") == 0) {
                        if (result.getBoolean("distanceState") && isOpenLocService(NewMerchantInfoActivity.this) && checkLocationPermission()) {
                            if (!TextUtils.isEmpty(merchantId)) {
                                //openInvestPresent.loadOpenInvestStatus("sm");
                                Intent intentPay = new Intent(NewMerchantInfoActivity.this, PayMoneyActivity.class);
                                intentPay.putExtra("merchantId", merchantId);
                                intentPay.putExtra("showPop", true);//输入金额页，点击按钮时显示距离小于500的弹窗提示
                                intentPay.putExtra("type", "30");
                                startActivity(intentPay);
                            }

                          /*  TiShiDialogTwo tiShiDialogTwo = new TiShiDialogTwo();
                            tiShiDialogTwo.show(NewMerchantInfoActivity.this, "Is this the right amount?", "Please confirm the amount with the venue before you continue.", "Cancel", "Confirm");
                            tiShiDialogTwo.setOnShure(new TiShiDialogTwo.OnShure() {
                                @Override
                                public void shure() {

                                }
                            });*/
                        } else { //xzc 正式上架，需要使用下面注释的代码！！！！
                           /* if (!TextUtils.isEmpty(merchantId)) {
                                //openInvestPresent.loadOpenInvestStatus("sm");
                                Intent intentPay = new Intent(NewMerchantInfoActivity.this, PayMoneyActivity.class);
                                intentPay.putExtra("merchantId", merchantId);
                                intentPay.putExtra("type", "30");
                                startActivity(intentPay);
                            }*/
                            TiShiDialog tiShiDialog = new TiShiDialog(NewMerchantInfoActivity.this);
                            tiShiDialog.ShowDialog("You have to pay in person", "You’re apparently not at the venue, please visit the venue to checkout.", "Close");
                        }
                    } else {
                        TiShiDialog tiShiDialog = new TiShiDialog(NewMerchantInfoActivity.this);
                        tiShiDialog.showStripeDialog();
                        tiShiDialog.setOnGuanBiLeft(new TiShiDialog.GuanBiLeft() {
                            @Override
                            public void GuanBiLeft() {

                            }
                        });
                        tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                            @Override
                            public void GuanBi() {//去绑卡

                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String e) {

            }
        });
    }

    //谷歌地图,起点就是定位点
    // 终点是LatLng ll = new LatLng("你的latitude","你的longitude");
    public void startNaviGoogle() {
        if (PublicTools.isAvilible(this, "com.google.android.apps.maps")) {
            //Uri gmmIntentUri = Uri.parse("google.navigation:q=" + ll.latitude + "," + ll.longitude);
            /*StringBuffer stringBuffer = new StringBuffer(*//*"google.navigation:q="*//*"geo:0,0?q=").append(lat).append(",").append(lng).append("&mode=d");
            Uri gmmIntentUri = Uri.parse(stringBuffer.toString());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);*/
            // 从哪到哪的路线
            /* Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d&daddr="
             *//*+ lat
                    + ","
                    + lng + "&q=" *//* + loactionText));
            // Search for restaurants nearby
            *//*Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + loactionText);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);*//*
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);*/

            //            Uri gmmIntentUri = Uri.parse("google.navigation:q="  //从人的当前位置导航到餐馆
//                    + lat + "," + lng
//                  /*  + ", + Sydney +Australia"*/);
//            Intent mapIntent = new Intent(Intent.ACTION_VIEW,
//                    gmmIntentUri);
//            mapIntent.setPackage("com.google.android.apps.maps");
//         startActivity(mapIntent);

            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,  //好用 xzc
                    Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q=" + lat + "," + lng));

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

            intent.setClassName("com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity");

            startActivity(intent);
        } else {
            //openWebGoogleNavi();
            Toast.makeText(this, "Google maps is not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void openInvestStatus(String code, String licai, String zhifu, String fenqi) {
        if (zhifu != null) {
            zhiFuStatusNext(code, zhifu, fenqi);
        }
    }

    /**
     * 获取到支付状态以后的操作
     */
    public void zhiFuStatusNext(String code, String zhifu, String fenqi) {
        //判断卡或者分期状态
        if ((fenqi.equals("0") || fenqi.equals("5") || fenqi.equals("2")) && zhifu.equals("0")) {
            Intent intent = new Intent(this, SelectPayTypeActivity.class);
            startActivity(intent);
        } else {
            Intent intentPay = new Intent(this, PayMoneyActivity.class);
            intentPay.putExtra("merchantId", merchantId);
            intentPay.putExtra("type", "30");
            startActivity(intentPay);
        }
    }

    public void getMerchantDetail() {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("no_user_id", 1);
        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(NewMerchantInfoActivity.this, StaticParament.USER);

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("商户详情参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.getMerchantDetail(headerMap, id, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("商户详情结果：" + dataStr);
                Gson gson = new Gson();
                MerchantInfoData merchantInfoData = gson.fromJson(dataStr, MerchantInfoData.class);
                List<String> detailPhotoList = merchantInfoData.getDetailPhotoList();
                List<BannerDatas> bannerList = new ArrayList<>();
                for (int i = 0; i < detailPhotoList.size(); i++) {
                    bannerList.add(new BannerDatas(detailPhotoList.get(i)));
                }
                //设置图片集合
                idBanner.setImages(bannerList);
                if (bannerList.size() > 1) {
                    //设置banner样式(显示圆形指示器)
                    idBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                }
                idBanner.setVisibility(View.VISIBLE);
                idBanner.start();
                idBanner.isAutoPlay(false);
                addFavorite.setVisibility(View.VISIBLE);
                if (merchantInfoData.getIsFavorite() != null && merchantInfoData.getIsFavorite().equals("1")) {
                    addFavorite.setImageResource(R.mipmap.have_save_icon);
                    addFavorite.setTag("1");
                    //favoriteText.setVisibility(View.GONE);
                } else {
                    addFavorite.setImageResource(R.mipmap.not_save_icon);
                    addFavorite.setTag("0");
                    //favoriteText.setVisibility(View.VISIBLE);
                }
                merchantName.setText(merchantInfoData.getPracticalName());
                if (TextUtils.isEmpty(merchantInfoData.getFullAddress().trim())) {
                    //stressLl.setVisibility(View.GONE);
                    copyIcon.setVisibility(View.GONE);
                    copy.setVisibility(View.GONE);
                    locationIcon.setVisibility(View.GONE);
                    getLocation.setVisibility(View.GONE);
                } else {
                    loactionText = merchantInfoData.getFullAddress();
                    adress.setText(merchantInfoData.getFullAddress());
                }
                if (TextUtils.isEmpty(merchantInfoData.getBusinessPhone().trim())) {
                    callIcon.setVisibility(View.GONE);
                    call.setVisibility(View.GONE);
                } else {
                    phoneNum = merchantInfoData.getBusinessPhone();
                    //phoneNum.setText(merchantInfoData.getBusinessPhone());
                }
                if (TextUtils.isEmpty(merchantInfoData.getUserDiscount()) || Float.parseFloat(merchantInfoData.getUserDiscount()) == 0) {
                    //discountBg.setVisibility(View.GONE);
                    discount.setVisibility(View.GONE);
                } else {
                    String wholeSaleFlag;
                    if (merchantInfoData.getHaveWholeSell() == 0) {
                        wholeSaleFlag = "Limited Time";
                    } else {
                        wholeSaleFlag = "Limited Offer";
                    }
                    String dis = (int) CalcTool.mm(merchantInfoData.getUserDiscount(), "100") + "% OFF";
                    discount.setText(dis + " - " + wholeSaleFlag);
                }
                if (merchantInfoData.getTags() != null && merchantInfoData.getTags().size() > 0) {
                    String tag = "";
                    for (int i = 0; i < merchantInfoData.getTags().size(); i++) {
                        tag += merchantInfoData.getTags().get(i);
                        if (i != merchantInfoData.getTags().size() - 1) {
                            tag += " · ";
                        }
                    }
                    tags.setText(tag);
                } else {
                    tags.setVisibility(View.GONE);
                }
                introduction.setText(merchantInfoData.getIntro());
                category.setText(merchantInfoData.getCategoriesStr());
                merchantId = merchantInfoData.getId();
                lat = merchantInfoData.getLat();
                lng = merchantInfoData.getLng();
            }


            @Override
            public void resError(String error) {
            }
        });
    }

    /**
     * 获取当前位置
     */
    public void getNowLocation() {
        getLocation(new PhoneLocation_in() {
            @Override
            public void notGetLocation() {
                if (!getCallBack) {
                    getCallBack = true;
                    checkPay();
                }
            }

            @Override
            public void setNewLocation(Location location) {
                Log.e("经度：", location.getLongitude() + "");
                Log.e("纬度：", location.getLatitude() + "");
                SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(NewMerchantInfoActivity.this, StaticParament.APP);
                appMsgSharePreferenceUtils.put(StaticParament.LAT, location.getLatitude() + "");
                appMsgSharePreferenceUtils.put(StaticParament.LNG, location.getLongitude() + "");
                appMsgSharePreferenceUtils.save();
                if (!getCallBack) {
                    getCallBack = true;
                    checkPay();
                }
            }

            @Override
            public void setDefaultLocation() {//关闭权限走这儿
//                Toast.makeText(NewMerchantInfoActivity.this, "11112222", Toast.LENGTH_SHORT).show();
                checkPay();
            }
        });
    }

    private TiShiDialog dialog;

    private void locationSer() {
//        if (StaticParament.server.equals("1")) {
//            if (isOpenLocService(this) && checkLocationPermission()) {
//                Intent intentPay = new Intent(this, PayMoneyActivity.class);
//                intentPay.putExtra("merchantId", merchantId);
//                intentPay.putExtra("type", "30");
//                startActivity(intentPay);
//                if (dialog != null) {
//                    dialog.dismissDia();
//                }
//            } else {
//                TiShiDialog tiShiDialog = new TiShiDialog(NewMerchantInfoActivity.this);
//                tiShiDialog.ShowDialog("You have to pay in person", "You’re apparently not at the venue, please visit the venue to checkout.", "Close");
//            }
//        } else {
//            getCallBack = false;
//            getNowLocation();
//        }

        getCallBack = false;
        getNowLocation();
    }


    @Override
    public void permissionStatus(int requestCode) {
        super.permissionStatus(requestCode);
    }

    /**
     * 判断位置权限
     */
    public boolean checkLocationPermission() {
        String[] PERMISSION_STORAGE =
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        try {
            if (hasPermissions(Objects.requireNonNull(this), PERMISSION_STORAGE)) {
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
}
