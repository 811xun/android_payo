package com.home.glx.uwallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.CardBankReListAdapter;
import com.home.glx.uwallet.datas.BankDatas;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class BankCardListActivity extends MainActivity {
    private RecyclerView cardList;
    private ImageView idBack;
    private CardBankReListAdapter adapter;
    private LinearLayout id_null_img;
    private String from;
    private RefreshLayout refreshLayout;
    private List<BankDatas> cardLists;
    private UserListener userListener;

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
        return R.layout.activity_bank_card_list;
    }

    @Override
    public void initView() {
        super.initView();
        userListener = new UserModel(this);
        from = getIntent().getStringExtra("from");
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        cardList = (RecyclerView) findViewById(R.id.card_list);
        TextView idTitle = findViewById(R.id.id_title);
        TypefaceUtil.replaceFont(idTitle, "fonts/gilroy_semiBold.ttf");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        cardList.setLayoutManager(linearLayoutManager);
        cardList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (cardLists.size() == 0) {
                    return;
                }
                //if (parent.getChildPosition(view) != (cardLists.size() - 1)) {
                int dis = PublicTools.dip2px(BankCardListActivity.this, 30);
                //outRect.bottom = -dis;
                outRect.bottom = dis;
                //}
            }
        });
        id_null_img = (LinearLayout) findViewById(R.id.id_null_img);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshLayout) {
                L.log("下拉刷新");
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //下拉刷新
                        refreshLayout.finishRefresh();
                        onResume();
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });
        idBack = (ImageView) findViewById(R.id.id_back);
        idBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean mShowLatpayPop = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (mShowLatpayPop) {
            SharePreferenceUtils sharePreferenceUtils1 = new SharePreferenceUtils(BankCardListActivity.this, StaticParament.USER);
            String userId1 = (String) sharePreferenceUtils1.get(StaticParament.USER_ID, "");
            Map<String, Object> maps1 = new HashMap<>();
            maps1.put("userId", userId1);
            getAppHomePageReminder(maps1);
        }
        mShowLatpayPop = false;

        Map<String, Object> maps = new HashMap<>();
        //0:账户  1:卡支付
        maps.put("cardType", "1");
        userListener.getCardList(maps, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                List<BankDatas> list = (List<BankDatas>) o[0];
                setBankList(list);
            }

            @Override
            public void onError(String e) {
            }
        });
    }

    private void getAppHomePageReminder(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(BankCardListActivity.this, maps, StaticParament.MainUrl,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(BankCardListActivity.this, StaticParament.USER);
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
                            JSONObject jsonObject = new JSONObject(dataStr);
                            if (jsonObject.has("stripeState")) {                    //是否弹出stripe弹框：0：否 1：是
                                if (jsonObject.getString("stripeState").equals("0")) {

                                } else if (jsonObject.getString("stripeState").equals("1")) {
                                    TiShiDialog tiShiDialog = new TiShiDialog(BankCardListActivity.this);
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

    public void setBankList(List<BankDatas> list) {
        if (list == null) {
            Toast.makeText(this, "Poor network connection. Please try again..", Toast.LENGTH_SHORT).show();
            return;
        }
        cardLists = list;
       /* if (list == null || list.size() == 0) {
            cardList.setVisibility(View.GONE);
            id_null_img.setVisibility(View.VISIBLE);
            return;
        }*/
        id_null_img.setVisibility(View.GONE);
        cardList.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new CardBankReListAdapter(this, list);
            cardList.setAdapter(adapter);
        } else {
            adapter.notifyDataChanged(list);
        }
        adapter.setOnitemClick(new CardBankReListAdapter.OnitemClick() {
            @Override
            public void itemClick(String cardNo, String id) {
                if (!TextUtils.isEmpty(from) && from.equals("payMoney")) {
                    Intent intent = BankCardListActivity.this.getIntent();
                    Bundle bundle = intent.getExtras();
                    bundle.putString("cardNo", cardNo);//添加要返回给页面1的数据
                    bundle.putString("cardId", id);//添加要返回给页面1的数据
                    intent.putExtras(bundle);
                    BankCardListActivity.this.setResult(Activity.RESULT_OK, intent);//返回页面1
                    BankCardListActivity.this.finish();
                }
            }

            @Override
            public void itemChoice(BankDatas bankDatas) {
                Intent intent = new Intent(BankCardListActivity.this, CardDetailActivity.class);
                intent.putExtra("bankDatas", bankDatas);
                intent.putExtra("cardSize", cardLists.size());
                /*if (cardLists.get(0).getId().equals(bankDatas.getId())) {
                    intent.putExtra("default", 1);
                }*/
                startActivity(intent);
                overridePendingTransition(0, android.R.anim.fade_out);
            }

            @Override
            public void addCard() {
                if (!PublicTools.isFastClick()) {
                    return;
                }
                Intent intent = new Intent(BankCardListActivity.this, NewAddBankCardActivity.class);
                intent.putExtra("from", "cardList");
                startActivity(intent);
            }
        });
    }

}
