package com.home.glx.uwallet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
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

public class MapListActivity extends MainActivity {
    private ImageView back;
    private RefreshLayout refreshLayout;
    private int maxPages = 0;
    private int page = 1;
    private List<MerchantItemData> dataList = new ArrayList<>();
    private ConstraintLayout haveSave;
    private TextView openMap;
    private ListView listView;
    private FavoriteListAdapter adapter;
    private MerchantListener merchantListener;
    private String leftUpLng;
    private String leftUpLat;
    private String rightUpLng;
    private String rightUpLat;
    private String leftDownLng;
    private String leftDownLat;
    private String rightDownLng;
    private String rightDownLat;
    private String searchText;
    private TextView noResult;

    @Override
    public int getLayout() {
        return R.layout.activity_map_list;
    }

    @Override
    public void initView() {
        super.initView();
        merchantListener = new MerchantModel(this);
        back = (ImageView) findViewById(R.id.back);
        listView = (ListView) findViewById(R.id.list_view);
        openMap = (TextView) findViewById(R.id.open_list);
        noResult = findViewById(R.id.no_result);
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchText = getIntent().getStringExtra("searchText");
        if (!TextUtils.isEmpty(searchText)) {
            openMap.setVisibility(View.GONE);
        }
        leftUpLng = getIntent().getStringExtra("leftUpLng");
        leftUpLat = getIntent().getStringExtra("leftUpLat");
        rightUpLng = getIntent().getStringExtra("rightUpLng");
        rightUpLat = getIntent().getStringExtra("rightUpLat");
        leftDownLng = getIntent().getStringExtra("leftDownLng");
        leftDownLat = getIntent().getStringExtra("leftDownLat");
        rightDownLng = getIntent().getStringExtra("rightDownLng");
        rightDownLat = getIntent().getStringExtra("rightDownLat");
        TextView title = findViewById(R.id.title);
        //切换字体
        TypefaceUtil.replaceFont(openMap,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(title,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(noResult,"fonts/acumin_regularPro.ttf");

        haveSave = (ConstraintLayout) findViewById(R.id.have_save);
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
                L.log("加载更多");
                page++;
                getList();
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
                        getList();
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });

        getList();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getList() {
        if (page != 1 && page > maxPages) {
            return;
        }

        Map<String, Object> maps = new HashMap<>();
        maps.put("s", 5);
        maps.put("p", page);
        maps.put("orderType", "2");
        SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.APP);
        maps.put("lat", appMsgSharePreferenceUtils.get(StaticParament.LAT, ""));
        maps.put("lng", appMsgSharePreferenceUtils.get(StaticParament.LNG, ""));
        if (!TextUtils.isEmpty(searchText)) {
            maps.put("searchKeyword", searchText);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("leftUpLng", leftUpLng);
            map.put("leftUpLat", leftUpLat);
            map.put("rightUpLng", rightUpLng);
            map.put("rightUpLat", rightUpLat);
            map.put("leftDownLng", leftDownLng);
            map.put("leftDownLat", leftDownLat);
            map.put("rightDownLng", rightDownLng);
            map.put("rightDownLat", rightDownLat);
            maps.put("rangeInfo", map);
        }

        merchantListener.merchantList(maps, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
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
            noResult.setVisibility(View.VISIBLE);
            haveSave.setVisibility(View.GONE);
            return;
        } else {
            noResult.setVisibility(View.GONE);
            haveSave.setVisibility(View.VISIBLE);
        }
        if (adapter != null) {
            adapter.notifyDataSetChang(dataList);
        } else {
            adapter = new FavoriteListAdapter(this, dataList);
            adapter.setOnitemClick(new FavoriteListAdapter.OnitemClick() {
                @Override
                public void unFavorite(String id) {

                }

                @Override
                public void itemClick(int position, String id) {
                    Intent intent = new Intent(MapListActivity.this, MapActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });
            listView.setAdapter(adapter);
        }
    }
}