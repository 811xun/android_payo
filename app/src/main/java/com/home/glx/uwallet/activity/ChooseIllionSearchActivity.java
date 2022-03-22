package com.home.glx.uwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.IllionListAdapter;
import com.home.glx.uwallet.adapter.SearchIllionResultAdapter;
import com.home.glx.uwallet.datas.IllionListData;
import com.home.glx.uwallet.datas.IllionPreLoadData;
import com.home.glx.uwallet.mvp.GetCacheList;
import com.home.glx.uwallet.mvp.IListener;
import com.home.glx.uwallet.mvp.ListenerManager;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.KycAndIllionListener;
import com.home.glx.uwallet.mvp.model.KycAndIllionModel;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
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

public class ChooseIllionSearchActivity extends MainActivity implements View.OnClickListener, IListener {
    private EditText searchEdit;
    private ImageView back;
    private ListView bankList;
    private IllionListAdapter adapter;
    private SearchIllionResultAdapter searchAdapter;
    private int firstMaxPage;
    private List<IllionListData> firstList = new ArrayList<>();
    private List<IllionListData> list = new ArrayList<>();
    private KycAndIllionListener kycAndIllionListener;
    private SmartRefreshLayout refreshLayout;
    private ListView searchResult;
    private int maxPages = 0;
    private int page = 1;

    @Override
    public int getLayout() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setStatusBarColor(ChooseIllionSearchActivity.this, R.color.white);
        return R.layout.activity_choose_illion_bank_second;
    }

    @Override
    public void initView() {
        super.initView();

        ListenerManager.getInstance().registerListtener(this);
        searchEdit = findViewById(R.id.search_edit);
        bankList = (ListView) findViewById(R.id.bank_list);
        back = (ImageView) findViewById(R.id.back);
        searchResult = (ListView) findViewById(R.id.search_result);
        searchResult.setVisibility(View.GONE);
        searchAdapter = new SearchIllionResultAdapter(this, GetCacheList.getInstance().getIllionList());
        searchResult.setAdapter(searchAdapter);
//        searchEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(ChooseIllionSearchActivity.this,ChooseIllionSearchActivity.class);
//                startActivity(intent);
//            }
//        });

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
                      /*  if (bankLayout.getVisibility() == View.VISIBLE) {
                            searchResult.setVisibility(View.GONE);
                        } else {
                        }*/
                        searchResult.setVisibility(View.VISIBLE);
                    }
                } else {
                    searchResult.setVisibility(View.GONE);
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
                    searchAdapter.setString(s.toString().trim());

                    if (searchAdapter != null) {
                        //searchResult.setFilterText(newText);
                        searchAdapter.getFilter().filter(s.toString().trim());//替换成本句后消失黑框！！！
                    }
                }
            }
        });
        back.setOnClickListener(this);
        kycAndIllionListener = new KycAndIllionModel(this);
        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //do something
                    //doSearch();
                    if (!TextUtils.isEmpty(searchEdit.getText().toString().trim())) {
                        //关闭软键盘
                        PublicTools.closeKeybord(ChooseIllionSearchActivity.this);
                        searchResult.setVisibility(View.GONE);
                        page = 1;
                        Map<String, Object> maps = new HashMap<>();
                        maps.put("name", searchEdit.getText().toString().trim());
                        maps.put("pageSize", 15);
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
                maps.put("pageSize", 15);
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
                        maps.put("pageSize", 15);
                        maps.put("pageNo", page);
                        getBankList(maps);
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });
//        Map<String, Object> maps = new HashMap<>();
//        maps.put("name", "");
//        maps.put("pageSize", 10);
//        maps.put("pageNo", page);
//        getBankList(maps);

        TextView title = findViewById(R.id.title);
        TextView searchBank = findViewById(R.id.search_bank);
        //切换字体
        TypefaceUtil.replaceFont(searchBank, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(searchEdit, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        searchEdit.requestFocus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(searchEdit.getWindowToken(), 0);
                imm.toggleSoftInputFromWindow(searchEdit.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 300);
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

                Intent intent = new Intent(ChooseIllionSearchActivity.this, IllionDetailActivity.class);
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
            adapter = new IllionListAdapter(this, list, false);
            adapter.setOnitemClick(new IllionListAdapter.OnitemClick() {
                @Override
                public void itemClick(String name) {
                    /*if (!checkBox.isChecked()) {
                        Toast.makeText(ChooseIllionSearchActivity.this, R.string.please_agree_agreement, Toast.LENGTH_SHORT).show();
                        return;
                    }*/
                    enterIllion(name);
                }
            });
            bankList.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View v) {
        if (!PublicTools.isFastClick()) {
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.back:
                finish();
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
            intent.setClass(ChooseIllionSearchActivity.this, Web_Activity.class);
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
            intent.setClass(ChooseIllionSearchActivity.this, Web_Activity.class);
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
