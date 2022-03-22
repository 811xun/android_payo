package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.media.Image;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.xzc.LoginActivity_inputNumber;
import com.home.glx.uwallet.adapter.MerchantInfoAdapter;
import com.home.glx.uwallet.datas.MerchantInfoData;
import com.home.glx.uwallet.mvp.ReqAllMerchant;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

public class SearchMerchantResultActivity extends MainActivity implements View.OnClickListener {

    private TextView searchFlag;
    private RefreshLayout refreshLayout;
    private ReqAllMerchant reqAllMerchant;
    private ListView id_recyclerview;
    private MerchantInfoAdapter adapter;
    private int width = 720;
    private int maxPages = 0;
    private int page = 1;
    private ImageView id_back;
    private ImageView search;
    private String searchKeyword = "";
    private List<MerchantInfoData> list;

    @Override
    public int getLayout() {
        return R.layout.activity_search_merchant_result;
    }

    @Override
    public void initView() {
        super.initView();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        width = metric.widthPixels;
        searchFlag = (TextView) findViewById(R.id.search_flag);
        String flag = getIntent().getStringExtra("flag");
        searchKeyword = getIntent().getStringExtra("searchKeyword");
        int maxPage = getIntent().getIntExtra("maxPage", 0);
        maxPages = maxPage;
        list = (List<MerchantInfoData>) getIntent().getSerializableExtra("list");
        if (flag.equals("0")) {
            searchFlag.setText("Sorted by highest discount");
        } else {
            searchFlag.setText("Sorted by nearest distance");
        }
        id_back = (ImageView) findViewById(R.id.id_back);
        search = (ImageView) findViewById(R.id.id_select_img);
        id_back.setOnClickListener(this);
        search.setOnClickListener(this);
        id_recyclerview = (ListView) findViewById(R.id.id_recyclerview);
        reqAllMerchant = new ReqAllMerchant(this);
        if (list.size() > 0) {
            setData(list, maxPage);
        } else {
            reqAllMerchant.reqAllMerchant(page, maxPages, searchKeyword, "20");
            reqAllMerchant.setOnGetAllMerchant(new ReqAllMerchant.OnGetAllMerchant() {
                @Override
                public void getAllMerchant(List<MerchantInfoData> listData, int maxPage, String flag, String searchKeyword) {
                    setData(listData, maxPage);
                }
            });
        }
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
                L.log("加载更多");
                page++;
                reqAllMerchant.reqAllMerchant(page, maxPages, searchKeyword,"20");
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
                        reqAllMerchant.reqAllMerchant(page, maxPages, searchKeyword,"20");
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });

    }

    private void setData(List<MerchantInfoData> listData, int maxPages) {
        this.maxPages = maxPages;
        if (listData.size() > 0) {
            id_recyclerview.setVisibility(View.VISIBLE);
        } else {
            id_recyclerview.setVisibility(View.GONE);
            return;
        }

        id_recyclerview.setFocusableInTouchMode(false);
        id_recyclerview.setFocusable(false);

        if (adapter != null) {
            adapter.notifyDataSetChang(listData);
        } else {
            adapter = new MerchantInfoAdapter(this, listData, width);
            adapter.setOnitemClick(new MerchantInfoAdapter.OnitemClick() {
                @Override
                public void itemClick(String id, String name) {
                    if (!isLogin()) {
//                        Intent intent_loging = new Intent(SearchMerchantResultActivity.this, Login_Activity.class);
                        Intent intent_loging = new Intent(SearchMerchantResultActivity.this, LoginActivity_inputNumber.class);
                        startActivity(intent_loging);
                        finish();
                        return;
                    }
                    Intent intent_about_us = new Intent(SearchMerchantResultActivity.this, NewMerchantInfoActivity.class);
                    intent_about_us.putExtra("id", id);
                    intent_about_us.putExtra("title", name);
                    startActivity(intent_about_us);
                }
            });
            id_recyclerview.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.id_back:
                finish();
                break;
            case R.id.id_select_img:
                Intent intent = new Intent(this, SearchMerchantActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
