package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.IllionListAdapter;
import com.home.glx.uwallet.adapter.SearchIllionResultAdapter;
import com.home.glx.uwallet.datas.IllionListData;
import com.home.glx.uwallet.datas.IllionPreLoadData;
import com.home.glx.uwallet.fragment.FirstFragment;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.mvp.GetCacheList;
import com.home.glx.uwallet.mvp.IListener;
import com.home.glx.uwallet.mvp.ListenerManager;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.KycAndIllionListener;
import com.home.glx.uwallet.mvp.model.KycAndIllionModel;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.AddMaidian;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseIllionActivity extends MainActivity implements View.OnClickListener, IListener {
    private TextView searchEdit;
    private ImageView back;
    private TextView searchBtn;
    private TextView protext;
    private ListView bankList;
    private CheckBox checkBox;
    private TextView pleaseSelect;
    private IllionListAdapter adapter;
    private SearchIllionResultAdapter searchAdapter;
    private int firstMaxPage;
    private List<IllionListData> firstList = new ArrayList<>();
    private List<IllionListData> list = new ArrayList<>();
    private KycAndIllionListener kycAndIllionListener;
    private SmartRefreshLayout refreshLayout;
    private TextView cantSee;
    private ListView searchResult;
    private ConstraintLayout bankLayout;
    private int maxPages = 0;
    private int page = 1;

    @Override
    public int getLayout() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setStatusBarColor(ChooseIllionActivity.this, R.color.white);
        return R.layout.activity_choose_illion_bank;
    }

    @Override
    public void initView() {
        super.initView();
        kycAndIllionListener = new KycAndIllionModel(this);
        Map<String, Object> maps2 = new HashMap<>();//获取所有的银行列表 用于在搜索页面匹配
        maps2.put("isAll", "true");
        getAllBankList(maps2);

        ListenerManager.getInstance().registerListtener(this);
        checkBox = (CheckBox) findViewById(R.id.id_protocols);
        protext = (TextView) findViewById(R.id.protext);
        searchEdit = findViewById(R.id.search_edit);
        searchBtn = (TextView) findViewById(R.id.search_background);
        bankList = (ListView) findViewById(R.id.bank_list);
        back = (ImageView) findViewById(R.id.back);
        searchResult = (ListView) findViewById(R.id.search_result);
        searchResult.setVisibility(View.GONE);
        bankLayout = (ConstraintLayout) findViewById(R.id.bank_layout);
        searchAdapter = new SearchIllionResultAdapter(this, GetCacheList.getInstance().getIllionList());
        searchResult.setAdapter(searchAdapter);
        searchEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PublicTools.isFastClick()) {
                    return;
                }
                Intent intent = new Intent(ChooseIllionActivity.this, ChooseIllionSearchActivity.class);
                startActivity(intent);
            }
        });
        searchAdapter.setOnitemClick(new SearchIllionResultAdapter.OnitemClick() {
            @Override
            public void itemClick(String tag) {
                enterIllion(tag);
            }

            @Override
            public void getData(int result) {
                if (result > 0) {
                    if (TextUtils.isEmpty(searchEdit.getText().toString().trim())) {
                        searchResult.setVisibility(View.GONE);
                    } else {
                        searchResult.smoothScrollToPosition(0);
                        if (bankLayout.getVisibility() == View.VISIBLE) {
                            searchResult.setVisibility(View.GONE);
                        } else {
                            searchResult.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    searchResult.setVisibility(View.GONE);
                    bankLayout.setVisibility(View.VISIBLE);
                    pleaseSelect.setVisibility(View.VISIBLE);
                    cantSee.setVisibility(View.VISIBLE);
                }
            }
        });
        //为ListView启动过滤
        searchResult.setTextFilterEnabled(true);
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchResult.setVisibility(View.GONE);
                bankLayout.setVisibility(View.GONE);
                pleaseSelect.setVisibility(View.GONE);
                cantSee.setVisibility(View.GONE);
                searchAdapter.notifyData(GetCacheList.getInstance().getIllionList());
                if (TextUtils.isEmpty(s.toString().trim())) {
                    searchAdapter.getFilter().filter(" ");
                    searchAdapter.setString(null);
                    searchResult.clearTextFilter();
                    list = firstList;
                    maxPages = firstMaxPage;
                    page = 1;
                    setData();
                } else {
                    searchResult.setBackground(null);
                    //searchResult.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(s.toString().trim())) {
                        searchAdapter.setString(s.toString().trim());
                        if (searchAdapter != null) {
                            //searchResult.setFilterText(newText);
                            searchAdapter.getFilter().filter(s.toString().trim());//替换成本句后消失黑框！！！
                        }
                    }
                }
            }
        });
        pleaseSelect = (TextView) findViewById(R.id.please_select);
        cantSee = (TextView) findViewById(R.id.cant_see);
        cantSee.setText(Html.fromHtml("<font color='#FD7441'>Can’t see your bank?</font> Try searching in the search bar."));
        back.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        addressNewStyle(protext);
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    pleaseSelect.setVisibility(View.GONE);
                    cantSee.setVisibility(View.GONE);
                    //do something
                    //doSearch();
                    if (!TextUtils.isEmpty(searchEdit.getText().toString().trim())) {
                        //关闭软键盘
                        PublicTools.closeKeybord(ChooseIllionActivity.this);
                        searchResult.setVisibility(View.GONE);
                        bankLayout.setVisibility(View.VISIBLE);
                        page = 1;
                        Map<String, Object> maps = new HashMap<>();
                        maps.put("name", searchEdit.getText().toString().trim());
                        maps.put("pageSize", 10);
                        maps.put("pageNo", page);
                        getBankList(maps);
                        return true;
                    }
                }
                return false;
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
                L.log("加载更多");
                page++;
                Map<String, Object> maps = new HashMap<>();
                if (TextUtils.isEmpty(searchEdit.getText().toString().trim())) {
                    maps.put("name", "");
                } else {
                    maps.put("name", searchEdit.getText().toString().trim());
                }
                maps.put("pageSize", 10);
                maps.put("pageNo", page);
                getBankList(maps);
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
                        list.clear();
                        Map<String, Object> maps = new HashMap<>();
                        if (TextUtils.isEmpty(searchEdit.getText().toString().trim())) {
                            maps.put("name", "");
                        } else {
                            maps.put("name", searchEdit.getText().toString().trim());
                        }
                        maps.put("pageSize", 10);
                        maps.put("pageNo", page);
                        getBankList(maps);
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });
        Map<String, Object> maps = new HashMap<>();
        maps.put("name", "");
        maps.put("pageSize", 10);
        maps.put("pageNo", page);
        getBankList(maps);


        TextView title = findViewById(R.id.title);
        TextView searchBank = findViewById(R.id.search_bank);
        //切换字体
        TypefaceUtil.replaceFont(searchBank, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(searchEdit, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(pleaseSelect, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(cantSee, "fonts/gilroy_medium.ttf");

        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(this, StaticParament.USER);
        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
        final Map<String, Object> maps1 = new HashMap<>();
        maps1.put("userId", userId);
        maps1.put("type", "6");//页面标识 1：二选一 2：证件类型页 3:地址页 4:个人信息填写页 5:绑卡页 6:illion默认机构页 7:illion 第一次验证页，8:illion第二次验证页 9:分期付开通等待页
        AddMaidian.addMaidian(this, maps1);
    }

    private void getBankList(final Map<String, Object> maps) {
        if (page != 1 && page > maxPages) {
            return;
        }
        kycAndIllionListener.getInstitutions(maps, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                if (page == 1) {
                    list.clear();
                }
                List<IllionListData> datas = (List<IllionListData>) o[0];
                maxPages = (Integer) o[1];
                /*if (page == maxPages) {
                    refreshLayout.setNoMoreData(true);
                    refreshLayout.finishLoadMoreWithNoMoreData();
                }*/
                if (maps.get("name").equals("") && page == 1) {
                    firstList.addAll(datas);
                    firstMaxPage = maxPages;
                }
                list.addAll(datas);
                setData();
            }

            @Override
            public void onError(String e) {
                list.clear();
                setData();
            }
        });

    }

    private void getAllBankList(final Map<String, Object> maps) {
        if (page != 1 && page > maxPages) {
            return;
        }
        kycAndIllionListener.getInstitutions(maps, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                List<IllionListData> datas = (List<IllionListData>)o[0];
                GetCacheList.getInstance().setIllionList(datas);

            }

            @Override
            public void onError(String e) {
            }
        });
    }

    private void enterIllion(String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("slug", name);
        kycAndIllionListener.preLoadIllion(map, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                List<IllionPreLoadData> list = (List<IllionPreLoadData>) o[0];
                String img = (String) o[1];
                String name = (String) o[2];
                String slug = (String) o[3];
                String userToken = (String) o[4];

                Intent intent = new Intent(ChooseIllionActivity.this, IllionDetailActivity.class);
                intent.putExtra("img", img);
                intent.putExtra("name", name);
                intent.putExtra("slug", slug);
                intent.putExtra("userToken", userToken);
                intent.putExtra("list", (Serializable) list);
                startActivity(intent);
            }

            @Override
            public void onError(String e) {

            }
        });
    }

    private void setData() {

        if (list.size() > 0) {
            bankList.setVisibility(View.VISIBLE);
        } else {
            bankList.setVisibility(View.GONE);
            return;
        }

        bankList.setFocusableInTouchMode(false);
        bankList.setFocusable(false);

        if (adapter != null) {
            adapter.notifyDataSetChang(list);
        } else {
            adapter = new IllionListAdapter(this, list, true);
            adapter.setOnitemClick(new IllionListAdapter.OnitemClick() {
                @Override
                public void itemClick(String name) {
                    /*if (!checkBox.isChecked()) {
                        Toast.makeText(ChooseIllionActivity.this, R.string.please_agree_agreement, Toast.LENGTH_SHORT).show();
                        return;
                    }*/
                    enterIllion(name);
                }
            });
            bankList.setAdapter(adapter);
        }
    }

    private void back() {
        if (FirstFragment.backStatue == 1 || FirstFragment.backStatue == -1) {
            Intent intent = new Intent(ChooseIllionActivity.this, MainTab.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
            intent.putExtra("num", 0);
            startActivity(intent);
            finish();
        } else if (FirstFragment.backStatue == 2) {
            Intent it = new Intent(ChooseIllionActivity.this, MainTab.class);
            it.putExtra("num", 2);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            it.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//设置不要刷新即将跳转的页面
            startActivity(it);
            finish();
        } else if (FirstFragment.backStatue == 3) {
            Intent intent = new Intent(ChooseIllionActivity.this, PayMoneyActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }else if (FirstFragment.backStatue == 4) {
            Intent intent = new Intent(ChooseIllionActivity.this, PayFailedActivity.class);
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

    @Override
    public void onClick(View v) {
        if (!PublicTools.isFastClick()) {
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.back:
                back();
                break;
            /*case R.id.search_background:
                if (TextUtils.isEmpty(searchEdit.getText().toString().trim())) {
                    return;
                }
                page = 1;
                Map<String, Object> maps = new HashMap<>();
                maps.put("name", searchEdit.getText().toString().trim());
                maps.put("pageSize", 10);
                maps.put("pageNo", page);
                getBankList(maps);
                break;*/
        }
    }

    class MyClickCTCableSpan extends ClickableSpan {

        public MyClickCTCableSpan() {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            L.log("点击");
            ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
            Intent intent = new Intent();
            intent.setClass(ChooseIllionActivity.this, Web_Activity.class);
            intent.putExtra("url", "https://bankstatements.com.au/about/terms");
            startActivity(intent);
        }
    }

    class MyClickPPableSpan extends ClickableSpan {

        public MyClickPPableSpan() {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            L.log("点击");
            ((TextView) widget).setHighlightColor(getResources().getColor(android.R.color.transparent));
            Intent intent = new Intent();
            intent.setClass(ChooseIllionActivity.this, Web_Activity.class);
            intent.putExtra("url", "https://www.illion.com.au/privacy-policy-risk-marketing-solutions");
            startActivity(intent);
        }
    }

    /**
     * 协议样式修改
     *
     * @param textView
     */
    private void addressNewStyle(TextView textView) {
        textView.setHighlightColor(getResources().getColor(android.R.color.transparent));
        SpannableString spannableString = new SpannableString("I agree to the User Terms & Conditions and Privacy Policy provided by BankStatements.com.au (ABN89166 277 845).\n" +
                "This service is provided by BankStatements.com.au. Your log indetails are never stored, they are secured with bank level 256 bitencryption and your data doesn't leave Australia.");
//        if (SystemUtils.getSysLangusge(this).equals("zh")) {
//            //中文
//            //隐私政策
//            MyClickableSpan clickableSpan2 = new MyClickableSpan();
//            spannableString.setSpan(clickableSpan2, 7, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
//            spannableString.setSpan(colorSpan2, 7, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        } else {
        //英文
        //隐私政策
        MyClickPPableSpan clickableSpan1 = new MyClickPPableSpan();
        MyClickCTCableSpan clickableSpan2 = new MyClickCTCableSpan();

        spannableString.setSpan(clickableSpan2, 11, 38, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpan1, 43, 57, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
        ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.text_orange));
        spannableString.setSpan(colorSpan1, 11, 38, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan2, 43, 57, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spannableString);
    }

    @Override
    public void notifyAllActivity(String title, String str) {
        if (str.equals("illionFinish") || str.equals("mfaFinish")) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ListenerManager.getInstance().unRegisterListener(this);
    }
}
