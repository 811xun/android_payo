package com.home.glx.uwallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.FavoriteListAdapter;
import com.home.glx.uwallet.datas.MerchantItemData;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.MerchantListener;
import com.home.glx.uwallet.mvp.model.MerchantModel;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchMerchatDataActivity extends MainActivity implements View.OnClickListener {
    private TextView select;
    private TextView search;
    private ImageView close;
    private int maxPages = 0;
    private int page = 1;
    private ListView listView;
    private String searchKeyword;
    private String keyword;
    private ImageView choiceIcon;
    private SmartRefreshLayout refreshLayout;
    private TextView noResult;
    private String categories;
    private MerchantListener merchantListener;
    private SharePreferenceUtils appMsgSharePreferenceUtils;
    private FavoriteListAdapter adapter;
    private List<MerchantItemData> dataList = new ArrayList<>();
    private int clickPosition = -1;
    private String clickMerchantId = "";

    @Override
    public int getLayout() {
        return R.layout.activity_search_merchat_data;
    }

    @Override
    public void initView() {
        super.initView();
        merchantListener = new MerchantModel(this);
        appMsgSharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.APP);
        searchKeyword = getIntent().getStringExtra("searchKeyword");
        categories = getIntent().getStringExtra("categories");
        keyword = getIntent().getStringExtra("keyword");
        search = (TextView) findViewById(R.id.search);
        if (!TextUtils.isEmpty(searchKeyword)) {
            search.setText(searchKeyword);
        }
        if (!TextUtils.isEmpty(keyword)) {
            search.setText(keyword);
        }
        close = (ImageView) findViewById(R.id.close);
        choiceIcon = (ImageView) findViewById(R.id.choice_icon);
        select = (TextView) findViewById(R.id.select);
        search.setOnClickListener(this);
        select.setOnClickListener(this);
        close.setOnClickListener(this);
        TypefaceUtil.replaceFont(search,"fonts/acumin_regularPro.ttf");
        TypefaceUtil.replaceFont(select,"fonts/gilroy_medium.ttf");
        noResult = findViewById(R.id.no_result);
        listView = (ListView) findViewById(R.id.list_view);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
                L.log("加载更多");
                page++;
                getList(true, false);
                //refreshLayout.finishLoadMore();
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
                        dataList.clear();
                        getList(true, false);
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });

        getList(false, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*page = 1;
        if (createFlag) {
            createFlag = false;
            getList(false);
        } else {
            getList(true);
        }*/
        if (clickPosition != -1) {
           /* int visiblePosition = listView.getFirstVisiblePosition();
            final View view = listView.getChildAt(clickPosition - visiblePosition);
            final FavoriteListAdapter.ViewHolder holder = (FavoriteListAdapter.ViewHolder) view.getTag();*/
            Map<String, Object> map = new HashMap<>();
            merchantListener.isUserFavorite(map, clickMerchantId, new ActionCallBack() {
                @Override
                public void onSuccess(Object... o) {
                    String data = (String) o[0];
                    adapter.updataView(clickPosition, data, listView);
                }

                @Override
                public void onError(String e) {

                }
            });

        }
    }

    private void setData(boolean scrollToTop) {
        //listView.setFocusableInTouchMode(false);
        //listView.setFocusable(false);
        if (dataList.size() == 0) {
            refreshLayout.setVisibility(View.GONE);
            noResult.setVisibility(View.VISIBLE);
            return;
        }
        refreshLayout.setVisibility(View.VISIBLE);
        noResult.setVisibility(View.GONE);
        if (scrollToTop) {
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);//获取焦点
            listView.setSelection(0);
        } else {
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
                        clickMerchantId = id;
                        clickPosition = position;
                        Intent intent = new Intent(SearchMerchatDataActivity.this, NewMerchantInfoActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                });
                listView.setAdapter(adapter);
            }
        }
    }

    private void getList(boolean refresh, final boolean scrollToTop) {
        if (page != 1 && page > maxPages) {
            return;
        }
        Map<String, Object> maps = new HashMap<>();
        if (select.getTag().toString().equals("1")) {
            maps.put("orderType", 2);
        } else if (select.getTag().toString().equals("2")) {
            maps.put("orderType", 3);
        } else {
            maps.put("orderType", 1);
        }
        if (!TextUtils.isEmpty(categories)) {
            maps.put("categories", categories);
        } else if (!TextUtils.isEmpty(keyword)){
            if (refresh) {
                maps.put("tagAdd", "0");
            } else {
                maps.put("tagAdd", "1");
            }
            maps.put("keyword", keyword);
        } else {
            if (refresh) {
                maps.put("tagAdd", "0");
            } else {
                maps.put("tagAdd", "1");
            }
            maps.put("searchKeyword", searchKeyword);
        }

        maps.put("s", 5);
        maps.put("p", page);
        maps.put("lat", appMsgSharePreferenceUtils.get(StaticParament.LAT, ""));
        maps.put("lng", appMsgSharePreferenceUtils.get(StaticParament.LNG, ""));
        maps.put("no_user_id", 1);
        merchantListener.merchantList(maps, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                refreshLayout.finishLoadMore();
                maxPages = (int) o[0];
                if (page == 1) {
                    dataList.clear();
                }
                if (page == maxPages) {
                    refreshLayout.setNoMoreData(true);
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.setNoMoreData(false);
                }
                List<MerchantItemData> data = (List<MerchantItemData>) o[1];
                dataList.addAll(data);
                setData(scrollToTop);
            }

            @Override
            public void onError(String e) {

            }
        });
    }

    @Override
    public void onClick(View v) {
       /* if (!PublicTools.isFastClick()) {
            return;
        }*/
        int id = v.getId();
        switch (id) {
            case R.id.search:
            case R.id.close:
                Intent intent = new Intent(this, MainTab.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                setResult(Activity.RESULT_OK, intent);//返回页面1
                finish();
                overridePendingTransition(0, android.R.anim.fade_out);
                break;
            case R.id.select:
                initPopWindow(select);
                break;
        }
    }

    private void initPopWindow(View v) {
        /*// 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;//调节透明度
        getWindow().setAttributes(lp);*/
        choiceIcon.setImageResource(R.mipmap.choice_gray_up);
        select.setTextColor(getResources().getColor(R.color.pay_text_gray));
        View view = LayoutInflater.from(this).inflate(R.layout.popup_select_merchant, null, false);
        ConstraintLayout nearest = (ConstraintLayout) view.findViewById(R.id.nearest);
        ConstraintLayout newVenues = (ConstraintLayout) view.findViewById(R.id.new_venues);
        ConstraintLayout discount = (ConstraintLayout) view.findViewById(R.id.discount);

        final ImageView nearestImg = (ImageView) view.findViewById(R.id.nearest_img);
        final ImageView newVenuesImg = (ImageView) view.findViewById(R.id.newVenues_img);
        final ImageView discountImg = (ImageView) view.findViewById(R.id.discount_img);

        TextView nearestText = view.findViewById(R.id.nearest_text);
        TextView newText = view.findViewById(R.id.new_text);
        TextView disText = view.findViewById(R.id.dis_text);

        //切换字体|
        TypefaceUtil.replaceFont(nearestText,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(newText,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(disText,"fonts/gilroy_medium.ttf");

        if (select.getTag() != null) {
            if (select.getTag().toString().equals("1")) {
                nearestImg.setImageResource(R.mipmap.choice_1);
                newVenuesImg.setImageResource(R.mipmap.unchoice_1);
                discountImg.setImageResource(R.mipmap.unchoice_1);
            } else if (select.getTag().toString().equals("2")) {
                nearestImg.setImageResource(R.mipmap.unchoice_1);
                newVenuesImg.setImageResource(R.mipmap.choice_1);
                discountImg.setImageResource(R.mipmap.unchoice_1);
            } else if (select.getTag().toString().equals("3")) {
                nearestImg.setImageResource(R.mipmap.unchoice_1);
                newVenuesImg.setImageResource(R.mipmap.unchoice_1);
                discountImg.setImageResource(R.mipmap.choice_1);
            }
        }
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
                choiceIcon.setImageResource(R.mipmap.choose_black_icon);
                select.setTextColor(Color.BLACK);
            }
        });

        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, 50, 0);

        //设置popupWindow里的按钮的事件
        nearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.setText("Nearest");
                nearestImg.setImageResource(R.mipmap.choice_1);
                newVenuesImg.setImageResource(R.mipmap.unchoice_1);
                discountImg.setImageResource(R.mipmap.unchoice_1);
                if (!select.getTag().toString().equals("1")) {
                    select.setTag("1");
                    page = 1;
                    getList(true, true);
                }
                popWindow.dismiss();

            }
        });
        newVenues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.setText("New Venues");
                nearestImg.setImageResource(R.mipmap.unchoice_1);
                newVenuesImg.setImageResource(R.mipmap.choice_1);
                discountImg.setImageResource(R.mipmap.unchoice_1);
                if (!select.getTag().toString().equals("2")) {
                    select.setTag("2");
                    page = 1;
                    getList(true, true);
                }
                popWindow.dismiss();
            }
        });
        discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.setText("Discount");
                nearestImg.setImageResource(R.mipmap.unchoice_1);
                newVenuesImg.setImageResource(R.mipmap.unchoice_1);
                discountImg.setImageResource(R.mipmap.choice_1);
                if (!select.getTag().toString().equals("3")) {
                    select.setTag("3");
                    page = 1;
                    getList(true, true);
                }
                popWindow.dismiss();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        search.performClick();
        return super.onKeyDown(keyCode, event);
    }
}