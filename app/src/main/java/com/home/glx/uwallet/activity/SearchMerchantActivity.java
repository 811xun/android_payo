package com.home.glx.uwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.SearchResultAdapter;
import com.home.glx.uwallet.datas.MerchantInfoData;
import com.home.glx.uwallet.datas.TagsData;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.mvp.PhoneLocation_in;
import com.home.glx.uwallet.mvp.ReqAllMerchant;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.WordWrapView;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class SearchMerchantActivity extends MainActivity implements View.OnClickListener  {

    private WordWrapView popularList;
    private Context context;
    private LinearLayout idBack;
    private LinearLayout popular;
    private TextView popularTitle;
    private ListView searchResult;
    private EditText searchEdit;
    private List<String> topTen;
    private List<String> tags;
    private SearchResultAdapter adapter;
    private ReqAllMerchant reqAllMerchant;
    private int maxPages = 0;
    private int page = 1;
    private TextView noResult;
    private TextView searchBtn;
    private boolean searchResultFlag = false;

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
        return R.layout.activity_search_merchant;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        getLocation();
        getTagInfo();
        popularList = (WordWrapView) findViewById(R.id.id_popular_list);
        idBack = (LinearLayout) findViewById(R.id.id_back);
        popular = (LinearLayout) findViewById(R.id.id_popular);
        searchResult = (ListView) findViewById(R.id.search_result);
        searchResult.setVisibility(View.GONE);
        noResult = (TextView) findViewById(R.id.no_result);
        noResult.setVisibility(View.GONE);
        popularTitle = (TextView) findViewById(R.id.id_popular_title);
        searchBtn = (TextView) findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(this);
        searchEdit = (EditText) findViewById(R.id.search_edit);
        //设置SearchView自动缩小为图标
        //searchEdit.setIconifiedByDefault(false);//设为true则搜索栏 缩小成俄日一个图标点击展开
        idBack.setOnClickListener(this);
        reqAllMerchant = new ReqAllMerchant(this);
        reqAllMerchant.setOnGetAllMerchant(new ReqAllMerchant.OnGetAllMerchant() {
            @Override
            public void getAllMerchant(List<MerchantInfoData> listData, int maxPage, String flag, String searchKeyword) {
                if (listData.size() == 0) {
                    searchResult.setVisibility(View.GONE);
                    noResult.setVisibility(View.VISIBLE);
                    popular.setVisibility(View.GONE);
                } else {
                    Intent intent = new Intent(SearchMerchantActivity.this, SearchMerchantResultActivity.class);
                    intent.putExtra("flag", flag);
                    intent.putExtra("list", (Serializable) listData);
                    intent.putExtra("maxPage", maxPage);
                    intent.putExtra("searchKeyword", searchKeyword);
                    startActivity(intent);
                    finish();
                }
            }
        });
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (adapter == null) {
                    return;
                }
                if (TextUtils.isEmpty(s.toString().trim())){
                    adapter.getFilter().filter(" ");
                    adapter.setString(null);
                    searchResult.clearTextFilter();
                    popular.setVisibility(View.VISIBLE);
                    searchResult.setVisibility(View.GONE);
                }else{
                    if (searchResultFlag) {
                        searchResultFlag = false;
                    } else {
                        if (!TextUtils.isEmpty(s.toString().trim())) {
                            adapter.setString(s.toString().trim());
                            if (adapter != null) {
                                //searchResult.setFilterText(newText);
                                popular.setVisibility(View.GONE);
                                adapter.getFilter().filter(s.toString().trim());//替换成本句后消失黑框！！！
                                searchResult.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        });

    /*    searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    if (!TextUtils.isEmpty(searchEdit.getText().toString().trim())) {
                        searchResultFlag = true;
                        reqAllMerchant.reqAllMerchant(page, maxPages, searchEdit.getText().toString().trim(), "2");
                    }
                    return true;
                }
                return false;
            }

        });*/
        /*searchEdit.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchEdit.setIconified(false);
                return false;
            }



            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    searchResult.clearTextFilter();
                    popular.setVisibility(View.VISIBLE);
                    searchResult.setVisibility(View.GONE);
                }else{
                    if (adapter != null) {
                        //searchResult.setFilterText(newText);
                        popular.setVisibility(View.GONE);
                        adapter.getFilter().filter(newText.toString());//替换成本句后消失黑框！！！
                        searchResult.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            }

        });*/

        popularList.setOnChoiceList(new WordWrapView.OnChoiceList() {
            @Override
            public void choices(List<TextView> choiceText) {

            }

            @Override
            public void choice(TextView choiceText) {
                searchResultFlag = true;
                searchEdit.setText(choiceText.getText().toString().trim());
                searchEdit.setSelection(choiceText.getText().toString().trim().length());
                reqAllMerchant.reqAllMerchant(page, maxPages, choiceText.getText().toString().trim(), "21");
            }

            @Override
            public void unChoice(TextView choiceText) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (!PublicTools.isFastClick()) {
            return;
        }
        switch (id) {
            case R.id.id_back:
                finish();
                break;
            case R.id.search_btn:
                if (!TextUtils.isEmpty(searchEdit.getText().toString().trim())) {
                    searchResultFlag = true;
                    reqAllMerchant.reqAllMerchant(page, maxPages, searchEdit.getText().toString().trim(), "21");
                }
                break;
        }
    }

    /**
     * 获取tag,top10,全量表信息
     */
    public void getTagInfo() {
        Map<String, Object> maps = new HashMap<>();
        maps.put("no_user_id", 1);
        RequestUtils requestUtils1 = new RequestUtils(context, maps,
                new RequestUtils.RequestDataStr() {
                    @Override
                    public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                        Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                        RequestBody requestBody =
                                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));

                        L.log("top10tag参数：" + new Gson().toJson(maps));
                        Call<ResponseBody> call = requestInterface.getTagInfo(headerMap, requestBody);
                        requestUtils.Call(call);
                    }

                    @Override
                    public void resData(String dataStr, String pc, final String code) {
                        L.log("top10tag：" + dataStr);
                        Gson gson = new Gson();
                        topTen = gson.fromJson(dataStr, TagsData.class).getTopTen();
                        tags = gson.fromJson(dataStr, TagsData.class).getTags();
                        if (topTen.size() > 0) {
                            popularList.setChoiceTag(topTen);
                        }

                        if (tags.size() > 0) {
                            adapter = new SearchResultAdapter(context, tags);
                            searchResult.setAdapter(adapter);
                            adapter.setOnitemClick(new SearchResultAdapter.OnitemClick() {
                                @Override
                                public void itemClick(String tag) {
                                    searchResultFlag = true;
                                    searchEdit.setText(tag);
                                    searchEdit.setSelection(tag.length());
                                    reqAllMerchant.reqAllMerchant(page, maxPages, tag, "21");
                                }
                            });
                            //为ListView启动过滤
                            searchResult.setTextFilterEnabled(true);
                        }
                    }

                    @Override
                    public void resError(String error) {
                    }
                });
    }

    /**
     * 获取当前位置
     */
    public void getLocation() {
        getLocation(new PhoneLocation_in() {
            @Override
            public void notGetLocation() {

            }

            @Override
            public void setNewLocation(Location location) {
                Log.e("经度：", location.getLongitude() + "");
                Log.e("纬度：", location.getLatitude() + "");
                SharePreferenceUtils appMsgSharePreferenceUtils = new SharePreferenceUtils(SearchMerchantActivity.this, StaticParament.APP);
                if (appMsgSharePreferenceUtils.get(StaticParament.LAT, "").equals(location.getLongitude()) &&
                        appMsgSharePreferenceUtils.get(StaticParament.LNG, "").equals(location.getLatitude())) {

                } else {
                    appMsgSharePreferenceUtils.put(StaticParament.LAT, location.getLatitude() + "");
                    appMsgSharePreferenceUtils.put(StaticParament.LNG, location.getLongitude() + "");
                    appMsgSharePreferenceUtils.save();
                }
                /*PostUserLocation postUserLocation = new PostUserLocation(MainTab.this);
                postUserLocation.postLocation(location.getLatitude(), location.getLongitude());*/
            }

            @Override
            public void setDefaultLocation() {

            }
        });
    }
}
