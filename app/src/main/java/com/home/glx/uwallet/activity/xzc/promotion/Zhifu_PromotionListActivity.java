package com.home.glx.uwallet.activity.xzc.promotion;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.list_adapter.AviPromotionListAdapter_zhifu;
import com.home.glx.uwallet.datas.PcDatas;
import com.home.glx.uwallet.datas.PromotionDatas;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class Zhifu_PromotionListActivity extends MainActivity {
    private ImageView idBack;

    private RecyclerView recycler_view_avi;
    private AviPromotionListAdapter_zhifu mAviAdapter;
    private List<PromotionDatas.Promotion> mPromotionAviList = new ArrayList<>();

    public static boolean showTishgiDialog = false;//不展示错误提示弹窗（错误的话 展示大碗）
    private RefreshLayout refreshLayout;
    private int page = 1;
    private int count = 10;
    private int maxPages = 0;

    private EditText mChangeLineEdittext;
    private TextView jine;
    private TextView add;
    private TextView date;
    private TextView date2;

    private TextView error;
    private String marketingId;
    private PromotionDatas.Promotion mSelectedPro = null;//当不选择时为null
    private String couponSelectedId = "";

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
        return R.layout.activity_promotion_list_zhifu;
    }

    @Override
    public void initView() {
        super.initView();
        couponSelectedId = getIntent().getStringExtra("couponId");
        couponSelectedId = couponSelectedId == null ? "" : couponSelectedId;
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        if (!couponSelectedId.equals("")) {
            mSelectedPro = new PromotionDatas().new Promotion();
            mSelectedPro.setCouponId(couponSelectedId);
            mSelectedPro.setAmount(getIntent().getStringExtra("SelectedPromotionJinE"));
        }
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
                page++;
                getProList();
                refreshLayout.finishLoadMore();
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //下拉刷新
                        refreshLayout.finishRefresh();
                        page = 1;
                        mPromotionAviList.clear();
                        getProList();
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });
        TypefaceUtil.replaceFont(findViewById(R.id.title), "fonts/gilroy_semiBold.ttf");

        initAddPro();
        getProList();

        recycler_view_avi = (RecyclerView) findViewById(R.id.recycler_view_avi);
        TypefaceUtil.replaceFont(findViewById(R.id.categories_avi), "fonts/gilroy_bold.ttf");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_avi.setLayoutManager(linearLayoutManager);
        recycler_view_avi.setNestedScrollingEnabled(false);//滑动出现冲突可添加

        if (mAviAdapter == null) {
            mAviAdapter = new AviPromotionListAdapter_zhifu(this, mPromotionAviList, couponSelectedId);
            recycler_view_avi.setAdapter(mAviAdapter);
            mAviAdapter.setOnitemClick(new AviPromotionListAdapter_zhifu.OnitemClick() {
                @Override
                public void itemClick(PromotionDatas.Promotion promotion) {
                    if (promotion == null) {
                        couponSelectedId = "";
                    } else {
                        couponSelectedId = promotion.getCouponId();
                    }
                    mSelectedPro = promotion;
                }
            });
        } else {
            mAviAdapter.notifyDataChanged(mPromotionAviList, couponSelectedId);
        }
        date2 = findViewById(R.id.date2);

        idBack = (ImageView) findViewById(R.id.id_back);
        idBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                if (mSelectedPro != null) {
                    intent.putExtra("promotionId", mSelectedPro.getCouponId());
                    intent.putExtra("promotionAmount", mSelectedPro.getAmount());
                } else {
                    intent.putExtra("promotionId", "");
                    intent.putExtra("promotionAmount", "");
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initAddPro() {
        mChangeLineEdittext = findViewById(R.id.id_first_name);
//        filterChinese(mChangeLineEdittext);//禁止输入中文
        TypefaceUtil.replaceFont(mChangeLineEdittext, "fonts/gilroy_medium.ttf");

        jine = findViewById(R.id.jine);
        TypefaceUtil.replaceFont(jine, "fonts/gilroy_semiBold.ttf");

        add = findViewById(R.id.add);
        TypefaceUtil.replaceFont(add, "fonts/gilroy_semiBold.ttf");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Map<String, Object> maps = new HashMap<>();
                maps.put("marketingId", marketingId);
                PromotionListActivity.showTishgiDialog = false;

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(Zhifu_PromotionListActivity.this, StaticParament.USER);
                String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
                maps.put("userId", userId);
                maps.put("inputCode", mChangeLineEdittext.getText().toString());
                maps.put("transAmount", getIntent().getStringExtra("transAmount"));
                maps.put("merchantId", getIntent().getStringExtra("merchantId"));
                add(maps);
            }
        });
        mChangeLineEdittext.setOnKeyListener(new View.OnKeyListener() {//如果不加这段代码  会出现输入框没有内容时 再点击删除键 页面会自动关闭  原因没找到。
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (mChangeLineEdittext.getText().toString().trim().length() == 0) {
                        return true;
                    }
                }
                return false;
            }
        });
        initEditOnEditorActionListener();
        mChangeLineEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("xmlsmmx", "onKey: 444");

                jine.setVisibility(View.INVISIBLE);
                add.setVisibility(View.INVISIBLE);
                date.setVisibility(View.INVISIBLE);
                error.setVisibility(View.INVISIBLE);
            }
        });


        error = findViewById(R.id.tv_error);
        TypefaceUtil.replaceFont(error, "fonts/gilroy_medium.ttf");
        date = findViewById(R.id.date);
        TypefaceUtil.replaceFont(date, "fonts/gilroy_medium.ttf");
    }

    private void initEditOnEditorActionListener() {
        mChangeLineEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isEmpty(mChangeLineEdittext.getText().toString())) {
//                        mChangeLineEdittext.setHint("Please fill out this section");
                        jine.setVisibility(View.INVISIBLE);
                        add.setVisibility(View.INVISIBLE);
                        date.setVisibility(View.INVISIBLE);
                        error.setVisibility(View.INVISIBLE);
                    } else if (mChangeLineEdittext.getText().toString().length() == 1) {//长度为1 不掉用接口直接显示报错信息。
                        jine.setVisibility(View.INVISIBLE);
                        add.setVisibility(View.INVISIBLE);
                        date.setVisibility(View.INVISIBLE);
                        error.setVisibility(View.VISIBLE);
                        error.setText("Promotion code does not exist or is not available");
                    } else {
                        final Map<String, Object> maps = new HashMap<>();
                        maps.put("code", mChangeLineEdittext.getText().toString());

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(Zhifu_PromotionListActivity.this, StaticParament.USER);
                        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
                        maps.put("userId", userId);
                        PromotionListActivity.showTishgiDialog = true;
                        mChangeLineEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                return true;
                            }
                        });
                        serach(maps);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void getProList() {//搜索营销码
        final Map<String, Object> maps = new HashMap<>();
        maps.put("s", count + "");
        maps.put("p", page + "");
        maps.put("listType", "1");
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(Zhifu_PromotionListActivity.this, StaticParament.USER);
        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
        maps.put("userId", userId);
        maps.put("merchantId", getIntent().getStringExtra("merchantId"));

        maps.put("transAmount", getIntent().getStringExtra("transAmount"));

        showTishgiDialog = true;
        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {
                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.promotionList(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                Log.d("xzcxz1111", "resData: " + dataStr);
                if (dataStr != null) {
                    Gson gson = new Gson();
                    maxPages = gson.fromJson(pc, PcDatas.class).getMaxPages();

                    if (page == maxPages) {
                        refreshLayout.setNoMoreData(true);
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    } else {
                        refreshLayout.setNoMoreData(false);
                    }
                    try {
                        JSONArray jsonArray = new JSONArray(dataStr);
                        if (jsonArray.length() == 0) {
                            refreshLayout.setEnableRefresh(false);
                            refreshLayout.setEnableLoadMore(false);
                            findViewById(R.id.categories_avi).setVisibility(View.GONE);
                            findViewById(R.id.recycler_view_avi).setVisibility(View.GONE);
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                PromotionDatas.Promotion merchantInfoData = gson.fromJson(jsonArray.getJSONObject(i).toString(), PromotionDatas.Promotion.class);
                                mPromotionAviList.add(merchantInfoData);
                                if (mPromotionAviList.size() > 0) {
                                    refreshLayout.setEnableRefresh(true);
                                    refreshLayout.setEnableLoadMore(true);
                                    findViewById(R.id.categories_avi).setVisibility(View.VISIBLE);
                                    findViewById(R.id.recycler_view_avi).setVisibility(View.VISIBLE);
                                    Log.d("xzcxz1111", "resData: " + couponSelectedId);

                                    mAviAdapter.notifyDataChanged(mPromotionAviList, couponSelectedId);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void resError(String error) {
                findViewById(R.id.refreshLayout).setVisibility(View.INVISIBLE);
                refreshLayout.setEnableRefresh(false);
                refreshLayout.setEnableLoadMore(false);
            }
        });
    }

    private String couponStr = "";

    JSONObject jsonObject = null;

    public void serach(Map<String, Object> maps) {//搜索营销码
        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {
                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.findPromotionCode(headerMap, requestBody);
                requestUtils.Call(call, null, false);
                findViewById(R.id.avi).setVisibility(View.VISIBLE);//展示Popwindow的原因：LoadingDialog弹出会自动关闭软键盘。
            }

            @Override
            public void resData(String dataStr, String pc, String code) {//{"id":"612542776661856259","amount":"20.00","code":"NEWTEST001","minTransAmount":"30.00","validStartTime":"22:00 27\/10\/2021","validEndTime":"22:00 27\/11\/2021","description":"Test Marketing","type":1,"validityLimitState":1,"amountLimitState":1,"expiredTimeStr":"27 Nov"}
                initEditOnEditorActionListener();

                findViewById(R.id.avi).setVisibility(View.GONE);
                couponStr = dataStr;
                Log.d("sllllsl", "resData: " + dataStr);
                if (dataStr != null) {

                    try {
                        jsonObject = new JSONObject(dataStr);
                        jine.setVisibility(View.VISIBLE);
                        date2.setVisibility(View.VISIBLE);

                        add.setVisibility(View.VISIBLE);
                        date.setVisibility(View.VISIBLE);
                        error.setVisibility(View.INVISIBLE);
                        marketingId = jsonObject.getString("id");
                        jine.setText("$" + PublicTools.twoend(jsonObject.getInt("amount")));

                        date2.setText((jsonObject.getInt("validityLimitState") == 0 ? "" : "Expires on " + jsonObject.getString("expiredTimeStr") + " • ") +
                                (jsonObject.getInt("amountLimitState") == 0 ? "" : "Min. order $" + jsonObject.getString("minTransAmount").split("\\.")[0] + " • ")
                                + jsonObject.getString("description"));
                        date2.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                //这个回调会调用多次，获取完行数记得注销监听
                                try {
                                    if (date2.getLineCount() > 1) {
                                        date.setText((jsonObject.getInt("validityLimitState") == 0 ? "" : "Expires on " + jsonObject.getString("expiredTimeStr") + " • ") +
                                                (jsonObject.getInt("amountLimitState") == 0 ? "" : "Min. order $" + jsonObject.getString("minTransAmount").split("\\.")[0] + "\n• ")
                                                + jsonObject.getString("description"));
                                    } else {
                                        date.setText((jsonObject.getInt("validityLimitState") == 0 ? "" : "Expires on " + jsonObject.getString("expiredTimeStr") + " • ") +
                                                (jsonObject.getInt("amountLimitState") == 0 ? "" : "Min. order $" + jsonObject.getString("minTransAmount").split("\\.")[0] + " • ")
                                                + jsonObject.getString("description"));
                                    }

                                    date.setVisibility(View.VISIBLE);
                                    date2.setVisibility(View.GONE);
                                    date2.getViewTreeObserver().removeOnPreDrawListener(this);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    findViewById(R.id.avi).setVisibility(View.GONE);
                    jine.setVisibility(View.INVISIBLE);
                    add.setVisibility(View.INVISIBLE);
                    date.setVisibility(View.INVISIBLE);
                    date2.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void resError(String error1) {
                findViewById(R.id.avi).setVisibility(View.GONE);
                initEditOnEditorActionListener();

                jine.setVisibility(View.INVISIBLE);
                add.setVisibility(View.INVISIBLE);
                date.setVisibility(View.INVISIBLE);
                error.setVisibility(View.VISIBLE);
                error.setText(error1);
            }
        });
    }

    public void add(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(Zhifu_PromotionListActivity.this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {
                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.addPromotionCode(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                Log.d("xzcxz", "resData: " + dataStr);
                if (dataStr != null) {
                    Intent intent = getIntent();
//                    intent.putExtra("couponStr", couponStr);
                    try {
                        if (new JSONObject(dataStr).getInt("type") == 1) {//1:营销 ;2.邀请码
                            if (new JSONObject(dataStr).getInt("payUseState") == 1) {//1 可用 黄use ; 2 不可用 灰use
                                intent.putExtra("promotionId", new JSONObject(dataStr).getString("id"));
                                intent.putExtra("promotionAmount", new JSONObject(dataStr).getString("balance"));
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else {
                                jine.setVisibility(View.INVISIBLE);
                                add.setVisibility(View.INVISIBLE);
                                date.setVisibility(View.INVISIBLE);
                                error.setVisibility(View.INVISIBLE);
                                mChangeLineEdittext.setText("");

                                page = 1;
                                mPromotionAviList.clear();
                                getProList();
                            }
                        } else {
                            TiShiDialog tiShiDialog = new TiShiDialog(Zhifu_PromotionListActivity.this);
                            tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                                @Override
                                public void GuanBi() {
                                    jine.setVisibility(View.INVISIBLE);
                                    add.setVisibility(View.INVISIBLE);
                                    date.setVisibility(View.INVISIBLE);
                                    error.setVisibility(View.INVISIBLE);
                                    mChangeLineEdittext.setText("");
                                }
                            });
                            tiShiDialog.ShowDialog("The promotion has been successfully added and can be used after consumption, thank you!");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void resError(String error) {
                PromotionListActivity.showTishgiDialog = false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = getIntent();
        if (mSelectedPro != null) {
            intent.putExtra("promotionId", mSelectedPro.getCouponId());
            intent.putExtra("promotionAmount", mSelectedPro.getAmount());
        } else {
            intent.putExtra("promotionId", "");
            intent.putExtra("promotionAmount", "");
        }
        setResult(Activity.RESULT_OK, intent);
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
