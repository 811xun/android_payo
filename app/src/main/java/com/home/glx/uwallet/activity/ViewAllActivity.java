package com.home.glx.uwallet.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.FavoriteListAdapter;
import com.home.glx.uwallet.datas.MerchantItemData;
import com.home.glx.uwallet.datas.ViewBannerData;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.MerchantListener;
import com.home.glx.uwallet.mvp.model.MerchantModel;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAllActivity extends MainActivity implements View.OnClickListener {
    private TextView select;
    private ImageView back;
//    private Banner idBanner;
    private ListView listView;
    private ImageView choiceIcon;
    private MerchantListener merchantListener;
    //private TextView text;
    private ConstraintLayout bannerCl;
    private RefreshLayout refreshLayout;
    //private TextView subText;
    private TextView title;
    private ImageView viewBackground;
    private int maxPages = 0;
    private int page = 1;
    //private NestedScrollView scrollView;
    private String order;
    private String categories;
    private FavoriteListAdapter adapter;
    private List<MerchantItemData> dataList = new ArrayList<>();
    private List<ViewBannerData> bannerList;
    private int clickPosition = -1;
    private String clickMerchantId = "";
    @Override
    public int getLayout() {
        return R.layout.activity_view_all;
    }

    @Override
    public void initView() {
        super.initView();
        //scrollView = (NestedScrollView) findViewById(R.id.scroll_view);
        //scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        merchantListener = new MerchantModel(this);
        select = (TextView) findViewById(R.id.select);
        title = (TextView) findViewById(R.id.title);
        bannerCl = (ConstraintLayout) findViewById(R.id.banner_cl);
        back = (ImageView) findViewById(R.id.id_back);
        listView = (ListView) findViewById(R.id.list_view);
        choiceIcon = (ImageView) findViewById(R.id.choice_icon);
//        idBanner = (Banner) findViewById(R.id.id_banner);
        //text = (TextView) findViewById(R.id.text);
        //subText = (TextView) findViewById(R.id.sub_text);
        //viewBackground = findViewById(R.id.view_background);

        //切换字体
        //TypefaceUtil.replaceFont(text,"fonts/gilroy_medium.ttf");
        //TypefaceUtil.replaceFont(subText,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(select,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(title,"fonts/gilroy_semiBold.ttf");

        order = getIntent().getStringExtra("order");
        categories = getIntent().getStringExtra("categories");
        String text = getIntent().getStringExtra("title");
        title.setText(text);

        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
                L.log("加载更多");
                page++;
                getList(false);
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
                        getList(false);
                        getBanner();
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });

        //initBanner();
        getList(false);
        //getBanner();

        back.setOnClickListener(this);
        select.setOnClickListener(this);
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
                    adapter.updataView(clickPosition, data, listView);
                }

                @Override
                public void onError(String e) {

                }
            });

        }
    }

    private void getBanner() {
        SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.APP);
        Map<String, Object> map = new HashMap<>();
        map.put("order", order);
        map.put("latitude", appMsgSharePreferenceUtils.get(StaticParament.LAT, ""));
        map.put("longitude", appMsgSharePreferenceUtils.get(StaticParament.LNG, ""));
        merchantListener.getAppHomePageCategoryAllImgData(map, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                bannerList = (List<ViewBannerData>) o[0];
                if (bannerList.size() == 0) {
                    bannerList.add(new ViewBannerData());
                }
                //设置图片集合
//                idBanner.setImages(bannerList);
//                if (bannerList.size() > 1) {
                    //设置banner样式(显示圆形指示器)
//                    idBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
//                }
                viewBackground.setVisibility(View.VISIBLE);
//                idBanner.setVisibility(View.VISIBLE);
//                idBanner.start();
//                idBanner.isAutoPlay(false);
            }

            @Override
            public void onError(String e) {

            }
        });
     /*   idBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //text.setText(bannerList.get(position).getTitle());
                //subText.setText(bannerList.get(position).getSubTitle());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
    }

    private void getList(final boolean scrollToTop) {
        if (page != 1 && page > maxPages) {
            return;
        }

        SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.APP);
        Map<String, Object> maps = new HashMap<>();
        maps.put("lat", appMsgSharePreferenceUtils.get(StaticParament.LAT, ""));
        maps.put("lng", appMsgSharePreferenceUtils.get(StaticParament.LNG, ""));
        maps.put("s", "5");
        maps.put("p", page);
        maps.put("categories", categories);
        if (select.getTag().toString().equals("1")) {
            maps.put("orderType", 2);
        } else if (select.getTag().toString().equals("2")) {
            maps.put("orderType", 3);
        } else {
            maps.put("orderType", 1);
        }

        merchantListener.merchantList(maps, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                refreshLayout.finishLoadMore();
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
                setData(scrollToTop);
            }

            @Override
            public void onError(String e) {
            }
        });
    }

    private void setData(boolean scrollToTop) {
//        listView.setFocusableInTouchMode(false);
//        listView.setFocusable(false);
        if (dataList.size() == 0) {
            return;
        }
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setEnableAutoLoadMore(true);
        if (scrollToTop) {
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);//获取焦点
            listView.setSelection(0);
            //listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
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
                        Intent intent = new Intent(ViewAllActivity.this, NewMerchantInfoActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                });
                listView.setAdapter(adapter);
            }
        }
    }

//    private void initBanner() {
//        //设置指示器位置（指示器居右）
//        idBanner.setIndicatorGravity(BannerConfig.CENTER);
//        //设置图片加载器
//        idBanner.setImageLoader(new ViewBannerLoader());
//    }

    @Override
    public void onClick(View v) {
        if (!PublicTools.isFastClick()) {
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.id_back:
                finish();
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
        TextView nearText = view.findViewById(R.id.nearest_text);
        TextView newText = view.findViewById(R.id.new_text);
        TextView disText = view.findViewById(R.id.dis_text);
        //切换字体
        TypefaceUtil.replaceFont(nearText,"fonts/gilroy_medium.ttf");
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
                    if (!order.equals("more")) {
                        getList(true);
                    }
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
                    if (!order.equals("more")) {
                        getList(true);
                    }
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
                    if (!order.equals("more")) {
                        getList(true);
                    }
                }
                popWindow.dismiss();
            }
        });
    }
}