package com.home.glx.uwallet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.FavoriteListAdapter;
import com.home.glx.uwallet.datas.MerchantItemData;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.MerchantListener;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.MerchantModel;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.NestedListView;
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

public class FavoriteListActivity extends MainActivity {
    private ImageView back;
    private RefreshLayout refreshLayout;
    private int maxPages = 0;
    private int page = 1;
    private List<MerchantItemData> dataList = new ArrayList<>();
    private RelativeLayout noSave;
    private ConstraintLayout haveSave;
    private NestedListView listView;
    private TextView start;
    private FavoriteListAdapter adapter;
    private UserListener userListener;
    private MerchantListener merchantListener;
    private int clickPosition = -1;
    private String clickMerchantId = "";

    @Override
    public int getLayout() {
        return R.layout.activity_favorite_list;
    }

    @Override
    public void initView() {
        super.initView();
        merchantListener = new MerchantModel(this);
        userListener = new UserModel(this);
        back = (ImageView) findViewById(R.id.back);
        listView = (NestedListView) findViewById(R.id.list_view);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        noSave = (RelativeLayout) findViewById(R.id.no_save);
        haveSave = (ConstraintLayout) findViewById(R.id.have_save);
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        start = (TextView) findViewById(R.id.start);
        TextView nothing = findViewById(R.id.nothing);
        TextView title = findViewById(R.id.title);
        TextView noWorries = findViewById(R.id.no_worries);
        //切换字体
        TypefaceUtil.replaceFont(title,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(start,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(nothing,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(noWorries,"fonts/gilroy_medium.ttf");

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoriteListActivity.this, MainTab.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("num", 0);
                startActivity(intent);
                finish();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
                L.log("加载更多");
                page++;
                getList();
//                refreshLayout.finishLoadMore();
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
//                        refreshLayout.finishRefresh();
                        page = 1;
                        getList();
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 0);
            }
        });

        getList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (clickPosition != -1) {
           /* int visiblePosition = listView.getFirstVisiblePosition();
            final View view = listView.getChildAt(clickPosition - visiblePosition);
            final FavoriteListAdapter.ViewHolder holder = (FavoriteListAdapter.ViewHolder) view.getTag();*/
            Map<String, Object> map = new HashMap<>();
            merchantListener.isUserFavorite(map, clickMerchantId, new ActionCallBack() {
                @Override
                public void onSuccess(Object... o) {
                    String data = (String) o[0];
                    if (data.equals("0")) {
                        dataList.remove(dataList.get(clickPosition));
                        adapter.notifyDataSetChang(dataList);
                    }
                    //adapter.updataView(clickPosition, data, listView);
                }

                @Override
                public void onError(String e) {

                }
            });

        }
    }

    private void getList() {
        if (page != 1 && page > maxPages) {
            return;
        }

        Map<String, Object> maps = new HashMap<>();
        maps.put("s", 10);
        maps.put("p", page);
        SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.APP);
        maps.put("lat", appMsgSharePreferenceUtils.get(StaticParament.LAT, ""));
        maps.put("lng", appMsgSharePreferenceUtils.get(StaticParament.LNG, ""));

        userListener.favoriteList(maps, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                refreshLayout.finishLoadMore();
                refreshLayout.finishRefresh();
                maxPages = (Integer) o[0];
                if (page == 1) {
                    dataList.clear();
                }
                if (page == maxPages) {
                    refreshLayout.setNoMoreData(true);
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.setNoMoreData(false);
                }
                List<MerchantItemData> list = (List<MerchantItemData>) o[1];
                dataList.addAll(list);
                setData();
            }

            @Override
            public void onError(String e) {

            }
        });
    }

    private void setData() {
        listView.setFocusableInTouchMode(false);
        listView.setFocusable(false);
        if (dataList.size() == 0) {
            noSave.setVisibility(View.VISIBLE);
            haveSave.setVisibility(View.GONE);
            return;
        } else {
            noSave.setVisibility(View.GONE);
            haveSave.setVisibility(View.VISIBLE);
        }
        if (adapter != null) {
            adapter.notifyDataSetChang(dataList);
        } else {
            adapter = new FavoriteListAdapter(this, dataList);
            adapter.setOnitemClick(new FavoriteListAdapter.OnitemClick() {
                @Override
                public void unFavorite(String id) {
                    page = 1;
                    getList();
                }

                @Override
                public void itemClick(int position, String id) {
                    clickMerchantId = id;
                    clickPosition = position;
                    Intent intent = new Intent(FavoriteListActivity.this, NewMerchantInfoActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });
            listView.setAdapter(adapter);
        }
    }
}