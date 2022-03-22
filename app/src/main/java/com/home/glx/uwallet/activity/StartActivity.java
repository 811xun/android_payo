package com.home.glx.uwallet.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.MyApplication;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.xzc.LoginActivity_inputNumber;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.mvp.GetHomeMerchantList;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;


public class StartActivity extends MainActivity {

    private Handler handler;
    private ImageView idStartImg;
    private String routeValue;
    private RelativeLayout all;

    private ViewPager viewPagerGuide;
    private int[] images = {R.drawable.guide1, R.drawable.guide2, R.drawable.guide3};
    private List<ImageView> imageViews;//用来存放几个imageview的实例
    private int currentItem = 0;
    public static boolean exeNFC = true;

    @Override
    public int getLayout() {
        setGetPermission(false);
        getPermission = false;
        //全屏设置
//        hideSystemNavigationBar();
        return R.layout.activity_start;
    }

    @Override
    public void getLayoutTop(Bundle savedInstanceState) {
        super.getLayoutTop(savedInstanceState);
        Window window = getWindow();
        //After LOLLIPOP not translucent status bar
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //Then call setStatusBarColor.
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.orange1));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.orange1));
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.black));
            //getWindow().setNavigationBarColor(Color.BLUE);
        }
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void initView() {
        super.initView();
        SharePreferenceUtils sharePreferenceUtilsLanguage =
                new SharePreferenceUtils(MyApplication.context, StaticParament.APP);
        sharePreferenceUtilsLanguage.put(StaticParament.LANGUAGE, "0");
        sharePreferenceUtilsLanguage.save();
        GetHomeMerchantList.getInstance().getCache(this);
        all =  findViewById(R.id.all);
        SharePreferenceUtils adSharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.DEVICE);
        //是否收藏商户，若有首页刷新的标识
        adSharePreferenceUtils.put(StaticParament.CHANGE_FAVORITE, "0");
        adSharePreferenceUtils.put(StaticParament.DEVICE_AD, "0");
        adSharePreferenceUtils.put(StaticParament.CHECK_FLAG, "0");
        adSharePreferenceUtils.put(StaticParament.SPLIT_FLAG, "0");
        adSharePreferenceUtils.put(StaticParament.LOCATION_FLAG, "0");
        adSharePreferenceUtils.put(StaticParament.MAP_LOCATION_FLAG, "0");
        adSharePreferenceUtils.put(StaticParament.DISTANCE_LOCATION_FLAG, "0");
        adSharePreferenceUtils.save();

        SharePreferenceUtils app_sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.APP);
        app_sharePreferenceUtils.put(StaticParament.LOOK_MONEY, false);
        app_sharePreferenceUtils.put(StaticParament.CHANGE_LANGUAGE, "0");
        app_sharePreferenceUtils.save();

        SharePreferenceUtils u_sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        u_sharePreferenceUtils.put(StaticParament.FROM_PAY_MONEY, "0");
        u_sharePreferenceUtils.put(StaticParament.FROM_SELECT_PAY, "0");
        u_sharePreferenceUtils.put(StaticParament.REPAY_SUCCESS, "0");
        u_sharePreferenceUtils.put(StaticParament.PAY_SUCCESS, "1");
        u_sharePreferenceUtils.save();
        //实现wallet页面的刷新
//        SharePreferenceUtils sharePreferenceUtils2 = new SharePreferenceUtils(this, StaticParament.USER);
//        sharePreferenceUtils2.put(StaticParament.PAY_SUCCESS, "1");
        String action = getIntent().getAction();
//        if (Intent.ACTION_VIEW.equals(action)) {//通过链接启动本app执行这儿
//            Uri uri = getIntent().getData();
//            if (uri != null) {
//                final String uriStr = uri.toString();
//                L.log("schemeUri:", uriStr);//就是点击的那个链接 https://mediacampa.onelink.me/g2dd/Stylemag
//                EventBus.getDefault().postSticky(new MessageEvent(uriStr));
//            } else {
//                L.log("Uri is null");
//            }
//
//        }
//        else {
//        }
        String guideFlag = (String) adSharePreferenceUtils.get(StaticParament.DEVICE_GUIDE, "");
        if (TextUtils.isEmpty(guideFlag)) {
            //showPageDialog(StartActivity.this);
            adSharePreferenceUtils.put(StaticParament.DEVICE_GUIDE, "1");
            adSharePreferenceUtils.save();
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent1 = new Intent(StartActivity.this, NewGuideActivity.class);
                    startActivity(intent1);
//                        overridePendingTransition(android.R.anim.fade_out, 0);
                    finish();
                }
            }, 1300);
        } else {
            if (getIntent().getExtras() != null) {
                for (String key : getIntent().getExtras().keySet()) {
                    if (key.equals("route")) {
                        routeValue = getIntent().getExtras().getString(key);
                        L.log("route 键值对", "Key: " + key + " Value: " + routeValue);
                    }
                }
            }

            //首次启动 Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT 为 0，再次点击图标启动时就不为零了
            if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
                String token = (String) sharePreferenceUtils.get(StaticParament.USER_TOKEN, "");
                if (!TextUtils.isEmpty(routeValue) && routeValue.equals("1") && !TextUtils.isEmpty(token)) {
//                    Intent intent = new Intent(this, FenQiFuBill_Activity.class);
//                    startActivity(intent);
                }
                finish();
//                    overridePendingTransition(android.R.anim.fade_out, 0);
                return;
            }

//        handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                changeImg();
//            }
//        }, 1600);

            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    openNext();
                }
            }, 1000);
        }
    }

    private void changeImg() {
        //idStartImg = (ImageView) findViewById(R.id.id_start_img);
//        idStartImg.setImageResource(R.mipmap.start_img2);
        idStartImg.setScaleType(ImageView.ScaleType.FIT_CENTER);

//        StatusBarUtil.setTranslucentForImageView(this, 80, idStartImg);
//        Glide.with(this).asGif().load("file:///android_asset/start_gif.gif").listener(new RequestListener<GifDrawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
//                try {
//                    Field gifStateField = GifDrawable.class.getDeclaredField("state");
//                    gifStateField.setAccessible(true);
//                    Class gifStateClass = Class.forName("com.bumptech.glide.load.resource.gif.GifDrawable$GifState");
//                    Field gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader");
//                    gifFrameLoaderField.setAccessible(true);
//                    Class gifFrameLoaderClass = Class.forName("com.bumptech.glide.load.resource.gif.GifFrameLoader");
//                    Field gifDecoderField = gifFrameLoaderClass.getDeclaredField("gifDecoder");
//                    gifDecoderField.setAccessible(true);
//                    Class gifDecoderClass = Class.forName("com.bumptech.glide.gifdecoder.GifDecoder");
//                    Object gifDecoder = gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)));
//                    Method getDelayMethod = gifDecoderClass.getDeclaredMethod("getDelay", int.class);
//                    getDelayMethod.setAccessible(true);
//                    //设置只播放一次
//                    resource.setLoopCount(1);
//                    //获得总帧数
//                    int count = resource.getFrameCount();
//                    int delay = 0;
//                    for (int i = 0; i < count; i++) {
//                        //计算每一帧所需要的时间进行累加
//                        delay += (int) getDelayMethod.invoke(gifDecoder, i);
//                    }
//                    idStartImg.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
////                            if (gifListener != null) {
////                                gifListener.gifPlayComplete();
////                            }
//                        }
//                    }, delay);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return false;
//            }
//        }).into(idStartImg);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openNext();
            }
        }, 0);
    }

    /**
     * Gif播放完毕回调
     */
    public interface GifListener {
        void gifPlayComplete();
    }

    private void openNext() {
      /*  SharePreferenceUtils guideSharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.DEVICE);
        String guideFlag = (String) guideSharePreferenceUtils.get(StaticParament.DEVICE_GUIDE, "");
        if (TextUtils.isEmpty(guideFlag)) {
            //打开引导页
            Intent intent = new Intent(StartActivity.this, MainTab.class);
            startActivity(intent);
            guideSharePreferenceUtils.put(StaticParament.DEVICE_GUIDE, "1");
            guideSharePreferenceUtils.save();
            finish();
            return;
        }*/

        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(StartActivity.this, StaticParament.USER);
        sharePreferenceUtils.put(StaticParament.USER_RETURN_PAY, "0");
        sharePreferenceUtils.save();
        long go_away_time = (long) sharePreferenceUtils.get(StaticParament.GO_AWAY_TIME, 0L);
        String token = (String) sharePreferenceUtils.get(StaticParament.USER_TOKEN, "");
        String pin = (String) sharePreferenceUtils.get(StaticParament.PIN_NUMBER, "");
        if (token.equals("")) {
            //打开主页面
            Intent intent = new Intent(StartActivity.this, LoginActivity_inputNumber.class);
            intent.putExtra("start", "1");
            startActivity(intent);
            finish();
//            overridePendingTransition(android.R.anim.fade_out, 0);
            return;
        } else {
//            if (!TextUtils.isEmpty(routeValue) && routeValue.equals("1")) {
//                Intent intent = new Intent(StartActivity.this, FenQiFuBill_Activity.class);
//                startActivity(intent);
//                finish();
//                overridePendingTransition(android.R.anim.fade_out, 0);
//                return;
//            }
            Intent intent = new Intent(StartActivity.this, MainTab.class);
            intent.putExtra("haveCheckUpdate", "1");
            startActivity(intent);
            finish();
//            overridePendingTransition(android.R.anim.fade_out, 0);
        }

   /*     if (pin.equals("")) {
            //如果登录了，没有设置PIN
            Intent intent = new Intent(StartActivity.this, MainTab.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_out, 0);
            return;
        }*/

//        if (System.currentTimeMillis() - go_away_time > 180000) {
//        inputPinNum();
//        }else {
//            Intent intent = new Intent(StartActivity.this, MainTab.class);
//            startActivity(intent);
//            finish();
//        }
    }

    private void showPageDialog(final Context context) {

        // 提示对话框
        final Dialog dialog = new Dialog(context, R.style.dialog_style);
        View view = View.inflate(context,
                R.layout.dialog_guide, null);
        dialog.setCancelable(false);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
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
            //使用LayoutParams改变控件的位置
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
                currentItem = position;//获取位置，即第几页
            }

            //滑动的时候
            @Override
            public void onPageScrolled(int position, float posionOffset, int arg2) {
                // TODO Auto-generated method stub
                System.out.println(posionOffset);//滑动的百分比
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });

        viewPagerGuide.setOnTouchListener(new View.OnTouchListener() {
            float startX;
            float startY;//没有用到
            float endX;
            float endY;//没有用到

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

                        //获取屏幕的宽度
                        Point size = new Point();
                        windowManager.getDefaultDisplay().getSize(size);
                        int width = size.x;

                        //首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
                        if (currentItem == (imageViews.size() - 1) && startX - endX >= (width / 10)) {
                            //Toast.makeText(GuideActivity.this, "最后一翻",Toast.LENGTH_SHORT).show();
                            //打开主页面
                            dialog.dismiss();
                            handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    openNext();
                                }
                            }, 0);
                            //onitemClick.itemClick();
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

}
