package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.xzc.ChooseCardActivity;
import com.home.glx.uwallet.adapter.HomeMarketAdapter;
import com.home.glx.uwallet.datas.FenqifuStatue;
import com.home.glx.uwallet.datas.HomeMerchantListData;
import com.home.glx.uwallet.fragment.FirstFragment;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.mvp.WhetherOpenInvest_in;
import com.home.glx.uwallet.mvp.WhetherOpenInvest_p;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.MerchantListener;
import com.home.glx.uwallet.mvp.model.MerchantModel;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.BannerClickDialog;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ViewMoreActivity extends MainActivity implements WhetherOpenInvest_in.View {//从首页之广告跳转过来的
    private List<HomeMerchantListData.ExclusiveBean.ListBean> moreList = new ArrayList<>();
    private int maxPages = 0;
    private int page = 1;
    private RecyclerView moreListView;
    private ImageView back;
    private MerchantListener merchantListener;
    private RefreshLayout refreshLayout;
    private TextView title;
    private HomeMarketAdapter marketAdapter;
    //是否开通个业务
    private WhetherOpenInvest_p openInvestPresent;

    @Override
    public int getLayout() {
        return R.layout.activity_view_more;
    }

    @Override
    public void initView() {
        super.initView();
        openInvestPresent = new WhetherOpenInvest_p(this, this);
        merchantListener = new MerchantModel(this);
        title = (TextView) findViewById(R.id.title);
        moreListView = (RecyclerView) findViewById(R.id.list_more);
        String text = getIntent().getStringExtra("title");
        title.setText(text);
        back = (ImageView) findViewById(R.id.id_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");

        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
                L.log("加载更多");
                page++;
                getMoreList();
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
                        page = 1;
                        moreList.clear();
                        getMoreList();
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        moreListView.setLayoutManager(linearLayoutManager);
        getMoreList();
    }

    private void getMoreList() {
        if (page != 1 && page > maxPages) {
            return;
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("s", "5");
        maps.put("p", page);
        merchantListener.getAppHomePageViewAllExclusiveData(maps, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                maxPages = (Integer) o[0];
                if (page == 1) {
                    moreList.clear();
                }
                if (page == maxPages) {
                    refreshLayout.setNoMoreData(true);
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.setNoMoreData(false);
                }
                List<HomeMerchantListData.ExclusiveBean.ListBean> list = (List<HomeMerchantListData.ExclusiveBean.ListBean>) o[1];
                moreList.addAll(list);
                //GetHomeMerchantList.getInstance().setMoreList(moreList);
                setMoreData();
            }

            @Override
            public void onError(String e) {

            }
        });
    }

    private void setMoreData() {
        if (moreList.size() == 0) {
            return;
        }
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setEnableAutoLoadMore(true);
        //市场推广
        if (marketAdapter != null) {
            marketAdapter.notifyData(moreList);
        } else {
            marketAdapter = new HomeMarketAdapter(this, moreList);
            marketAdapter.setOnitemClick(new HomeMarketAdapter.OnitemClick() {
                @Override
                public void itemClick(HomeMerchantListData.ExclusiveBean.ListBean bean) {
                    if (bean.getRedirectType().equals("1")) {
                        Intent intent = new Intent(ViewMoreActivity.this, Web_Activity.class);
                        intent.putExtra("url", bean.getRedirectH5LinkAddress());
                        startActivity(intent);
                    } else if (bean.getRedirectType().equals("2")) {
                        if (bean.getRedirectAppLinkType().equals("1")) {
                            Intent intent = new Intent(ViewMoreActivity.this, ShareToEarnActivity.class);
                            startActivity(intent);
                        } else if (bean.getRedirectAppLinkType().equals("2")) {
                            Intent intent = new Intent(ViewMoreActivity.this, AboutActivity.class);
                            intent.putExtra("formMain", "1");
                            startActivity(intent);
                        } else if (bean.getRedirectAppLinkType().equals("3")) {
//                            openInvestPresent.loadOpenInvestStatus("sm");
                            SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(ViewMoreActivity.this, StaticParament.USER);
                            String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
                            Map<String, Object> maps = new HashMap<>();
                            maps.put("userId", userId);
                            maps.put("stripeState", 0);//用户区分接入stripe前后的新老用户
                            jiaoyanFenqifu(maps);
                        } else if (bean.getRedirectAppLinkType().equals("4")) {
                            Intent intent = new Intent(ViewMoreActivity.this, MapActivity.class);
                            startActivity(intent);
                        }
                    } else if (bean.getRedirectType().equals("4")) {
                        //自定义弹窗
                        //跳转自定义页面展示方式   redirectCustomizedDisplayType   1Full screen  2  Semi Popup
                        if (bean.getRedirectCustomizedDisplayType().equals("2")) {
                            BannerClickDialog bannerClickDialog = new BannerClickDialog(ViewMoreActivity.this,
                                    bean.getRedirectCustomizedTitle(),
                                    bean.getRedirectCustomizedContent(),
                                    bean.getRedirectCustomizedImageUrl()
                            );
                            bannerClickDialog.show();
                        } else if (bean.getRedirectCustomizedDisplayType().equals("1")) {
                            Intent intent = new Intent(ViewMoreActivity.this, BannerClickActivity.class);
                            intent.putExtra("title", bean.getRedirectCustomizedTitle());
                            intent.putExtra("text", bean.getRedirectCustomizedContent());
                            intent.putExtra("url", bean.getRedirectCustomizedImageUrl());
                            startActivity(intent);
                        }
                    }
                }
            });
            moreListView.setAdapter(marketAdapter);
        }
    }

    @Override
    public void openInvestStatus(String code, String licai, String zhifu, String fenqi) {
        if (fenqi.equals("1")) {
            Intent intent = new Intent(this, MainTab.class);
            intent.putExtra("num", 2);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
            startActivity(intent);
            finish();
        } else {
//            Intent intent = new Intent(this, FenQiFuOpen_Activity.class);
//            startActivity(intent);
        }
    }

    /**
     * 分期付开通状态
     *
     * @param maps
     */
    private void jiaoyanFenqifu(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(ViewMoreActivity.this, maps, new RequestUtils.RequestDataStr() {
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
                        new SharePreferenceUtils(ViewMoreActivity.this, StaticParament.DEVICE);
                String callPhone = (String) appMsgSharePreferenceUtils.get(StaticParament.APP_PHONE, "");

                Gson gson = new Gson();
                FenqifuStatue fenqifuStatue = gson.fromJson(dataStr, FenqifuStatue.class);
                SelectPayTypeActivity.shifoukaitongCardPay = fenqifuStatue.getCardState();//0：未开通开支付  1:开通开卡支付
                SelectPayTypeActivity.meikaitongfenqifuAndkaitongCardPay = fenqifuStatue.getCreditCardAgreementState();

                FirstFragment.backStatue = 1;
                if (fenqifuStatue.getKycState() == 0) {//未开通kyc
                    Intent intent_register = new Intent(ViewMoreActivity.this, RegistTwo_Activity.class); //跳到注册页 从注册页跳到kyc页
                    intent_register.putExtra("jumpToKyc", true);
                    startActivity(intent_register);
                } else {//已经开通kyc
                    if (fenqifuStatue.getCardState() == 0) {//未开通卡支付
                        Intent intent_kyc = new Intent(ViewMoreActivity.this, NewAddBankCardActivity.class);
                        if (fenqifuStatue.getCreditCardAgreementState() == 1 && fenqifuStatue.getInstallmentState() == 2) {//分期付删卡至没卡 需要调到不带协议的页面绑卡
                            intent_kyc.putExtra("viewmore", true);
                        } else {
                            intent_kyc.putExtra("kaitongfenqifuRoad", true);
                        }
                        startActivity(intent_kyc);
                    } else {//已经开通卡支付 //判断illion状态
                        if (fenqifuStatue.getCreditCardAgreementState() == 0) {//是否勾选过分期付绑卡协议 0：未勾选 1：已勾选
                            Intent intent_kyc = new Intent(ViewMoreActivity.this, ChooseCardActivity.class);
                            startActivity(intent_kyc);
                        } else {
                            if (fenqifuStatue.getInstallmentState() == 2) {//开通illion 开通了分期付
//                            finish();//跳到首页
                                Intent intent_fenqi = new Intent(ViewMoreActivity.this, MainTab.class);
                                intent_fenqi.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent_fenqi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent_fenqi);
                                finish();
                            } else if (fenqifuStatue.getInstallmentState() == 0 || fenqifuStatue.getInstallmentState() == 3) {//未开通！ 0. 用户未开通分期付(需要完善信息) 3. illion未授权
                                Intent intent_kyc = new Intent(ViewMoreActivity.this, ChooseIllionActivity.class);
                                startActivity(intent_kyc);
                            } else if (fenqifuStatue.getInstallmentState() == 1 || fenqifuStatue.getInstallmentState() == 5 || fenqifuStatue.getInstallmentState() == 8) {//—-失败！—— 1. 用户分期付已冻结 5. 用户分期付禁用 8 机审拒绝
                                Intent intent_kyc = new Intent(ViewMoreActivity.this, KycAndIllionResultActivity.class);
                                intent_kyc.putExtra("error", "FkReject");
                                intent_kyc.putExtra("phone", callPhone);
                                startActivity(intent_kyc);
                            } else if (fenqifuStatue.getInstallmentState() == 4 || fenqifuStatue.getInstallmentState() == 7 || fenqifuStatue.getInstallmentState() == 9) {//—-等待！——4. 等待人工审核 7. 机审中 9 分期付风控未触发
                                Intent intent_kyc = new Intent(ViewMoreActivity.this, KycAndIllionResultActivity.class);
                                intent_kyc.putExtra("error", "Waiting");
                                intent_kyc.putExtra("phone", callPhone);
                                startActivity(intent_kyc);
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

}
