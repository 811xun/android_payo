package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.xzc.PaySuccessActivity_Zhifu;
import com.home.glx.uwallet.datas.FenQiMsgDatas;
import com.home.glx.uwallet.datas.FenqifuStatue;
import com.home.glx.uwallet.fragment.FirstFragment;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.FenqifuSuccessDialog;
import com.home.glx.uwallet.tools.AddMaidian;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class InstalWaitingActivity extends MainActivity {

    private ImageView back;
    private ViewFlipper viewFlipper;
    //    private ImageView img2;
//    private ImageView img3;
    private ImageView icon;
    private TextView title;
    private TextView text;
    //3分钟后销毁页面
    private Handler handler;
    private Runnable runnable;
    private String callPhone;
    //每两秒改一下内容
//    private Handler mHandler;
    private Runnable mRunnable;
    private int thisPage = 1;

    private Handler mGetStateHandler;
    private Runnable mGetStateRunnable;

    @Override
    public int getLayout() {
        return R.layout.activity_instal_waiting;
    }

    @Override
    public void initView() {
        super.initView();
        SharePreferenceUtils appMsgSharePreferenceUtils =
                new SharePreferenceUtils(InstalWaitingActivity.this, StaticParament.DEVICE);
        callPhone = (String) appMsgSharePreferenceUtils.get(StaticParament.APP_PHONE, "");
        back = findViewById(R.id.back);
        viewFlipper = findViewById(R.id.img);
//        viewFlipper.setImageResource(R.mipmap.instal_wait_icon1);

//        img2 = findViewById(R.id.img2);
//        img2.setImageResource(R.mipmap.instal_wait_icon2);
//
//        img3 = findViewById(R.id.img3);
//        img3.setImageResource(R.mipmap.instal_wait_icon3);

        icon = findViewById(R.id.icon);
        title = findViewById(R.id.title);
        text = findViewById(R.id.text);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        //切换字体
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(text, "fonts/gilroy_medium.ttf");
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);
        icon.startAnimation(rotateAnimation);

        //倒计时三分钟
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                /*Intent intent = new Intent(IllionWaitingActivity.this, MainTab.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
                startActivity(intent);
                finish();*/
                Intent intent_waite = new Intent(InstalWaitingActivity.this, KycAndIllionResultActivity.class);
//                if (result == 0) {
//                    Map<String, Object> maps = new HashMap<>();
//                    kycAndIllionListener.sendIllionMessage(maps);
//                }
                intent_waite.putExtra("error", "Waiting");
                intent_waite.putExtra("phone", callPhone);
                startActivity(intent_waite);
                finish();
            }
        };
        handler.postDelayed(runnable, 180000);

        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(InstalWaitingActivity.this, StaticParament.USER);
        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
        final Map<String, Object> maps = new HashMap<>();
        maps.put("userId", userId);
        maps.put("stripeState", 0);//用户区分接入stripe前后的新老用户
        mGetStateHandler = new Handler();//每两秒钟轮训一次结果状态
        mGetStateRunnable = new Runnable() {
            @Override
            public void run() {
                jiaoyanFenqifu(maps);
                mGetStateHandler.postDelayed(mGetStateRunnable, 2000);
            }
        };
        mGetStateHandler.postDelayed(mGetStateRunnable, 3000);

//        mHandler = new Handler();
//        mRunnable = new Runnable() {
//            @Override
//            public void run() {
//                thisPage++;
//                viewFlipper.removeAllViews();
//
//                if (thisPage % 3 == 1) {
//
//                    ImageView iv = new ImageView(InstalWaitingActivity.this);
//                    iv.setImageResource(R.mipmap.instal_wait_icon1);
//                    viewFlipper.addView(iv);
//                    viewFlipper.setAutoStart(true);
//                    viewFlipper.setVisibility(View.VISIBLE);
//                } else if (thisPage % 3 == 2) {
//                    ImageView iv = new ImageView(InstalWaitingActivity.this);
//                    iv.setImageResource(R.mipmap.instal_wait_icon2);
//                    viewFlipper.addView(iv);
//                    viewFlipper.setAutoStart(true);
//                    viewFlipper.setVisibility(View.VISIBLE);
//                } else {
//                    ImageView iv = new ImageView(InstalWaitingActivity.this);
//                    iv.setImageResource(R.mipmap.instal_wait_icon3);
//                    viewFlipper.addView(iv);
//                    viewFlipper.setAutoStart(true);
//                    viewFlipper.setVisibility(View.VISIBLE);
//                }
//                viewFlipper.setFlipInterval(2 * 1000);
//                viewFlipper.startFlipping();
////                mHandler.postDelayed(mRunnable, 2000);
//            }
//        };
//        mHandler.postDelayed(mRunnable, 2000);
        viewFlipper.removeAllViews();

        ImageView iv = new ImageView(InstalWaitingActivity.this);
        iv.setImageResource(R.mipmap.instal_wait_icon1);
        viewFlipper.addView(iv);
        viewFlipper.setAutoStart(true);
        viewFlipper.setVisibility(View.VISIBLE);
        ImageView iv2 = new ImageView(InstalWaitingActivity.this);
        iv2.setImageResource(R.mipmap.instal_wait_icon2);
        viewFlipper.addView(iv2);
        viewFlipper.setAutoStart(true);
        viewFlipper.setVisibility(View.VISIBLE);
        ImageView iv3 = new ImageView(InstalWaitingActivity.this);
        iv3.setImageResource(R.mipmap.instal_wait_icon3);
        viewFlipper.addView(iv3);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.startFlipping();

        final Map<String, Object> maps1 = new HashMap<>();
        maps1.put("userId", userId);
        maps1.put("type", "9");//页面标识 1：二选一 2：证件类型页 3:地址页 4:个人信息填写页 5:绑卡页 6:illion默认机构页 7:illion 第一次验证页，8:illion第二次验证页 9:分期付开通等待页
        AddMaidian.addMaidian(this, maps1);
    }

    private void back() {
        if (FirstFragment.backStatue == 1) {
            Intent intent = new Intent(InstalWaitingActivity.this, MainTab.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("num", 0);
            startActivity(intent);
            finish();

        } else if (FirstFragment.backStatue == 2) {
            Intent it = new Intent(InstalWaitingActivity.this, MainTab.class);
            it.putExtra("num", 2);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
            startActivity(it);
            finish();
        } else if (FirstFragment.backStatue == 3) {
            Intent intent = new Intent(InstalWaitingActivity.this, PayMoneyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } else if (FirstFragment.backStatue == 4) {
            Intent intent = new Intent(InstalWaitingActivity.this, PayFailedActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    /**
     * 分期付开通状态
     *
     * @param maps
     */
    private void jiaoyanFenqifu(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(InstalWaitingActivity.this, maps, new RequestUtils.RequestDataStr() {
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
                        new SharePreferenceUtils(InstalWaitingActivity.this, StaticParament.DEVICE);
                String callPhone = (String) appMsgSharePreferenceUtils.get(StaticParament.APP_PHONE, "");

                Gson gson = new Gson();
                FenqifuStatue fenqifuStatue = gson.fromJson(dataStr, FenqifuStatue.class);
                SelectPayTypeActivity.shifoukaitongCardPay = fenqifuStatue.getCardState();//0：未开通开支付  1:开通开卡支付
                SelectPayTypeActivity.meikaitongfenqifuAndkaitongCardPay = fenqifuStatue.getCreditCardAgreementState();

                if (fenqifuStatue.getInstallmentState() == 2) {//开通成功 获取额度信息
                    if (mGetStateHandler != null && mGetStateRunnable != null) {
                        mGetStateHandler.removeCallbacks(mGetStateRunnable);
                    }
                    setData();
                } else if (fenqifuStatue.getInstallmentState() == 1 || fenqifuStatue.getInstallmentState() == 5 || fenqifuStatue.getInstallmentState() == 8) {//—-失败！—— 1. 用户分期付已冻结 5. 用户分期付禁用 8 机审拒绝
                    if (mGetStateHandler != null && mGetStateRunnable != null) {
                        mGetStateHandler.removeCallbacks(mGetStateRunnable);
                    }
                    Intent intent_kyc = new Intent(InstalWaitingActivity.this, KycAndIllionResultActivity.class);
                    intent_kyc.putExtra("error", "FkReject");
                    intent_kyc.putExtra("phone", callPhone);
                    startActivity(intent_kyc);
                } else if (fenqifuStatue.getInstallmentState() == 4) {//—-等待！——4. 等待人工审核 7. 机审中 9 分期付风控未触发
                    if (mGetStateHandler != null && mGetStateRunnable != null) {
                        mGetStateHandler.removeCallbacks(mGetStateRunnable);
                    }
                    Intent intent_kyc = new Intent(InstalWaitingActivity.this, KycAndIllionResultActivity.class);
                    intent_kyc.putExtra("error", "Waiting");
                    intent_kyc.putExtra("phone", callPhone);
                    startActivity(intent_kyc);
                } else if (fenqifuStatue.getInstallmentState() == 0 || fenqifuStatue.getInstallmentState() == 3) {//跳到illion选择页面
                    if (mGetStateHandler != null && mGetStateRunnable != null) {
                        mGetStateHandler.removeCallbacks(mGetStateRunnable);
                    }
                    Intent successIntent = new Intent(InstalWaitingActivity.this, ChooseIllionActivity.class);
                    startActivity(successIntent);
                    finish();
                }
                Log.d("xunzhic", "resData: " + dataStr);
            }

            @Override
            public void resError(String error) {

            }
        });
    }

    private void setData() {//开通成功 获取额度信息
        RequestUtils requestUtils1 = new RequestUtils(InstalWaitingActivity.this, new HashMap<String, Object>(), StaticParament.FENQIFU,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(InstalWaitingActivity.this, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                        L.log("参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.userCreditMessage(headerMap, requestBody);
                        requestUtils.Call(call, null, false);
                    }

                    @Override
                    public void resData(String dataStr, String pc, String code) {
                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(InstalWaitingActivity.this, StaticParament.USER);
                        sharePreferenceUtils.put(StaticParament.PAY_SUCCESS, "1").commit();
                        L.log("用户分期信息:" + dataStr);
                        Gson gson = new Gson();
                        FenQiMsgDatas fenQiMsgDatas = gson.fromJson(dataStr, FenQiMsgDatas.class);
                        L.log("用户分期信息:" + fenQiMsgDatas.getCreditAmount());

                        FenqifuSuccessDialog fenqifuSuccessDialog = new FenqifuSuccessDialog(InstalWaitingActivity.this, InstalWaitingActivity.this);
                        fenqifuSuccessDialog.ShowDialog("$" + fenQiMsgDatas.getCreditAmount());//展示额度弹窗
                        fenqifuSuccessDialog.setOnGuanBi(new FenqifuSuccessDialog.GuanBi() {
                            @Override
                            public void GuanBi() {//在这儿写 点击成功弹窗右上角的×
                                back();
//                                Toast.makeText(InstalWaitingActivity.this, "guanbiPop!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void queding() {

                            }
                        });
                    }

                    @Override
                    public void resError(String error) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }

    }
}