package com.home.glx.uwallet.activity.xzc.promotion;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.list_adapter.AviPromotionListAdapter;
import com.home.glx.uwallet.adapter.list_adapter.UsedPromotionListAdapter;
import com.home.glx.uwallet.datas.PcDatas;
import com.home.glx.uwallet.datas.PromotionDatas;
import com.home.glx.uwallet.datas.ShareData;
import com.home.glx.uwallet.mvp.LoadShareMsg;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PromotionListActivity extends MainActivity {
    private String shareUrl = "Join Payo to get $10 off and enjoy pay later meals, why not? Use this code to get the deal: %s https://invite.payo.com.au?inviteCode=%s";
    private ImageView idBack;

    private RecyclerView recycler_view_avi;
    private AviPromotionListAdapter mAviAdapter;
    private List<PromotionDatas.Promotion> mPromotionAviList = new ArrayList<>();

    private RecyclerView recycler_view_used;
    private UsedPromotionListAdapter mUsedAdapter;
    private List<PromotionDatas.Promotion> mPromotionUsedList = new ArrayList<>();
    public static boolean showTishgiDialog = false;//true：不展示错误提示弹窗
    private RefreshLayout refreshLayout;
    private int page = 1;
    private int count = 10;
    private int maxPages = 0;

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
        return R.layout.activity_promotion_list;
    }

    @Override
    public void initView() {
        super.initView();
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
                L.log("加载更多");
                page++;

                getProList();
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
                        mPromotionAviList.clear();
                        mPromotionUsedList.clear();
                        getProList();
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });
        TypefaceUtil.replaceFont(findViewById(R.id.title), "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.add_pro), "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.invite2), "fonts/gilroy_semiBold.ttf");
        findViewById(R.id.invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadShareMsg loadShareMsg = new LoadShareMsg(PromotionListActivity.this);
                loadShareMsg.setOnData(new LoadShareMsg.OnShareMsg() {
                    @Override
                    public void msg(ShareData data) {
                        //shareUrl = String.format(shareUrl, data.getInviteCode(), data.getInviteCode());
                        shareUrl = data.getUrl();
                        Intent localIntent = new Intent("android.intent.action.SEND");
                        localIntent.setType("text/plain");
                        localIntent.putExtra("android.intent.extra.SUBJECT", "Payo");
                        localIntent.putExtra("android.intent.extra.TEXT", shareUrl);
                        startActivity(Intent.createChooser(localIntent, "Share & Earn PAYO"));
                    }
                });
                loadShareMsg.load(new HashMap<String, Object>());
            }
        });
        findViewById(R.id.invite2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.invite).performClick();
            }
        });

        findViewById(R.id.add_pro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();//区分是v的Fragment还是app包里面的
                PupwindowAddPro dialogFragment = new PupwindowAddPro(findViewById(R.id.rl_wai),PromotionListActivity.this,PromotionListActivity.this);
                dialogFragment.show(manager, "custom");
                dialogFragment.setOnitemClick(new PupwindowAddPro.OnitemClick() {
                    @Override
                    public void itemClick() {
                        page = 1;
                        mPromotionAviList.clear();
                        mPromotionUsedList.clear();
                        getProList();
                    }
                });
            }
        });


        getProList();

        recycler_view_avi = (RecyclerView) findViewById(R.id.recycler_view_avi);
        recycler_view_used = (RecyclerView) findViewById(R.id.recycler_view_used);
        TypefaceUtil.replaceFont(findViewById(R.id.iv_invite), "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.categories_avi), "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(findViewById(R.id.categories_used), "fonts/gilroy_bold.ttf");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_avi.setLayoutManager(linearLayoutManager);
        recycler_view_avi.setNestedScrollingEnabled(false);//滑动出现冲突可添加
        recycler_view_used.setLayoutManager(linearLayoutManager2);
        recycler_view_used.setNestedScrollingEnabled(false);
   /*     recycler_view_avi.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                if (cardLists.size() == 0) {
                    return;
                }
                int dis = PublicTools.dip2px(PromotionListActivity.this, 30);
                outRect.bottom = dis;
            }
        });*/
        if (mAviAdapter == null) {
            mAviAdapter = new AviPromotionListAdapter(this, mPromotionAviList);
            recycler_view_avi.setAdapter(mAviAdapter);
        } else {
            mAviAdapter.notifyDataChanged(mPromotionAviList);
        }

        if (mUsedAdapter == null) {
            mUsedAdapter = new UsedPromotionListAdapter(this, mPromotionUsedList);
            recycler_view_used.setAdapter(mUsedAdapter);
        } else {
            mUsedAdapter.notifyDataChanged(mPromotionUsedList);
        }

        idBack = (ImageView) findViewById(R.id.id_back);
        idBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getProList() {//搜索营销码
        final Map<String, Object> maps = new HashMap<>();
        maps.put("s", count + "");
        maps.put("p", page + "");
        maps.put("listType", "0");
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(PromotionListActivity.this, StaticParament.USER);
        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
        maps.put("userId", userId);
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
                Log.d("xzcxz", "resData: " + dataStr);
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
                            findViewById(R.id.rl_has_no).setVisibility(View.VISIBLE);
                            findViewById(R.id.invite).setVisibility(View.GONE);
                            findViewById(R.id.refreshLayout).setVisibility(View.GONE);
                        } else {
                            boolean hasData = false;
                            findViewById(R.id.rl_has_no).setVisibility(View.GONE);
                            findViewById(R.id.invite).setVisibility(View.VISIBLE);
                            findViewById(R.id.refreshLayout).setVisibility(View.VISIBLE);

                            findViewById(R.id.categories_avi).setVisibility(View.GONE);
                            findViewById(R.id.recycler_view_avi).setVisibility(View.GONE);
                            findViewById(R.id.categories_used).setVisibility(View.GONE);
                            findViewById(R.id.recycler_view_used).setVisibility(View.GONE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                PromotionDatas merchantInfoData = gson.fromJson(jsonArray.getJSONObject(i).toString(), PromotionDatas.class);
                                if (merchantInfoData.getDataType() == 1) {
                                    mPromotionAviList.addAll(merchantInfoData.getList());
                                    if (mPromotionAviList.size() > 0) {
                                        findViewById(R.id.categories_avi).setVisibility(View.VISIBLE);
                                        findViewById(R.id.recycler_view_avi).setVisibility(View.VISIBLE);
//                                        mAviAdapter = new AviPromotionListAdapter(PromotionListActivity.this, mPromotionAviList);
//                                        recycler_view_avi.setAdapter(mAviAdapter);
                                        mAviAdapter.notifyDataChanged(mPromotionAviList);
                                        hasData = true;
                                    }
                                } else if (merchantInfoData.getDataType() == 2) {
                                    mPromotionUsedList.addAll(merchantInfoData.getList());
                                    if (mPromotionUsedList.size() > 0) {
                                        findViewById(R.id.categories_used).setVisibility(View.VISIBLE);
                                        findViewById(R.id.recycler_view_used).setVisibility(View.VISIBLE);
//                                        mUsedAdapter = new UsedPromotionListAdapter(PromotionListActivity.this, mPromotionUsedList);
//                                        recycler_view_used.setAdapter(mUsedAdapter);
                                        mUsedAdapter.notifyDataChanged(mPromotionUsedList);
                                        hasData = true;
                                    }
                                }
                            }

                            if (!hasData) {
                                findViewById(R.id.rl_has_no).setVisibility(View.VISIBLE);
                                findViewById(R.id.invite).setVisibility(View.GONE);
                                findViewById(R.id.refreshLayout).setVisibility(View.GONE);
                                findViewById(R.id.categories_avi).setVisibility(View.GONE);
                                findViewById(R.id.recycler_view_avi).setVisibility(View.GONE);
                                findViewById(R.id.categories_used).setVisibility(View.GONE);
                                findViewById(R.id.recycler_view_used).setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void resError(String error) {
                findViewById(R.id.rl_has_no).setVisibility(View.VISIBLE);
                findViewById(R.id.invite).setVisibility(View.GONE);
                findViewById(R.id.refreshLayout).setVisibility(View.GONE);
            }
        });
    }

}
