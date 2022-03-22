package com.home.glx.uwallet.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.MapActivity;
import com.home.glx.uwallet.activity.SearchMerchatDataActivity;
import com.home.glx.uwallet.adapter.SearchResultAdapter;
import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.datas.GetChoiceDatas;
import com.home.glx.uwallet.mvp.GetChoiceList_in;
import com.home.glx.uwallet.mvp.GetChoiceList_p;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.MerchantListener;
import com.home.glx.uwallet.mvp.model.MerchantModel;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.selfview.WordWrapView;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.home.glx.uwallet.PermissionsBaseActivity.tabNum;
import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

public class SearchFragment extends FirstTabFragment implements View.OnClickListener, GetChoiceList_in.View {
    private ImageView close;
    private TextView openMap;
    private ImageView openMaps;
    private TextView noResult;
    private ConstraintLayout findCl;
    private ListView searchResult;
    private EditText searchEdit;
    private SearchResultAdapter adapter;
    private GetChoiceList_p choiceList_p;
    private List<ChoiceDatas> merchantCategories;
    private WordWrapView categoriesList;
    private WordWrapView topList;
    private TextView discover;
    private TextView openMapText;
    private TextView categories;
    private TextView topSearch;
    private MerchantListener merchantListener;
    private List<String> topTen;
    private List<String> tags;
    private boolean searchResultFlag = false;
    private String keyword;

    private String searchKeyword;
    private String categoriesStr;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_search;
    }

    public void show(int ms) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tabNum == 1&&searchEdit!=null) {
                    searchEdit.setFocusable(true);//自动弹出软键盘
                    searchEdit.setFocusableInTouchMode(true);
                    searchEdit.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInputFromInputMethod(searchEdit.getWindowToken(), 0);
                    imm.toggleSoftInputFromWindow(searchEdit.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }, ms);

    }

    @Override
    public void initView() {
        super.initView();
        merchantListener = new MerchantModel(getContext());
        choiceList_p = new GetChoiceList_p(getContext(), this);
        close = (ImageView) view.findViewById(R.id.close);
        openMap = (TextView) view.findViewById(R.id.open_map);
        openMaps = (ImageView) view.findViewById(R.id.open_maps);
        searchEdit = (EditText) view.findViewById(R.id.search);
        noResult = (TextView) view.findViewById(R.id.no_result);
        searchResult = (ListView) view.findViewById(R.id.search_result);
        findCl = (ConstraintLayout) view.findViewById(R.id.find_cl);
        discover = (TextView) view.findViewById(R.id.discover);
        openMapText = (TextView) view.findViewById(R.id.open_map_text);
        categories = (TextView) view.findViewById(R.id.categories);
        topSearch = (TextView) view.findViewById(R.id.top_search);
        //切换字体
        TypefaceUtil.replaceFont(searchEdit, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(discover, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(openMapText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(categories, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(topSearch, "fonts/gilroy_medium.ttf");


        categoriesList = (WordWrapView) view.findViewById(R.id.id_categories_list);
        categoriesList.setOnClickList(new WordWrapView.OnClickList() {
            @Override
            public void click(TextView choiceText) {
                status = 1;
                searchKeyword = choiceText.getText().toString();
                categoriesStr = choiceText.getTag().toString();
                jump = true;
                locationSer();
            }
        });
        topList = (WordWrapView) view.findViewById(R.id.id_top_list);
        topList.setOnClickList(new WordWrapView.OnClickList() {
            @Override
            public void click(TextView choiceText) {
                status = 2;
                jump = true;

                locationSer();
                keyword = choiceText.getText().toString().substring(1);
            }
        });
        close.setOnClickListener(this);
        openMap.setOnClickListener(this);
        openMaps.setOnClickListener(this);
        getCategories();

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(searchEdit.getText().toString().trim())) {
                        searchResultFlag = true;
                        Map<String, Object> maps = new HashMap<>();
                        final String tag = searchEdit.getText().toString();
                        if (!TextUtils.isEmpty(tag)) {
                            maps.put("searchKeyword", tag);
                        }
                        merchantListener.haveData(maps, new ActionCallBack() {
                            @Override
                            public void onSuccess(Object... o) {
                                String data = (String) o[0];
                                if (data.equals("0")) {
                                    noResult.setVisibility(View.VISIBLE);
                                    findCl.setVisibility(View.GONE);
                                    searchResult.setVisibility(View.GONE);
                                } else {
                                    searchResult.clearTextFilter();
                                    findCl.setVisibility(View.VISIBLE);
                                    searchResult.setVisibility(View.GONE);
                                    searchEdit.setText("");
                                    Intent intent = new Intent(getContext(), SearchMerchatDataActivity.class);
                                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    intent.putExtra("searchKeyword", tag);
                                    startActivityForResult(intent, 122);
                                    locationQuanxian = false;
                                }
                            }

                            @Override
                            public void onError(String e) {

                            }
                        });
                    }
                    return true;
                } else {
                    return false;
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
                if (TextUtils.isEmpty(s.toString().trim())) {
                    adapter.getFilter().filter(" ");
                    adapter.setString(null);
                    searchResult.clearTextFilter();
                    findCl.setVisibility(View.VISIBLE);
                    searchResult.setVisibility(View.GONE);
                    noResult.setVisibility(View.GONE);
                } else {
                    if (searchResultFlag) {
                        searchResultFlag = false;
                    } else {
                        if (!TextUtils.isEmpty(s.toString().trim())) {
                            adapter.setString(s.toString().trim());
                            if (adapter != null) {
                                //searchResult.setFilterText(newText);
                                findCl.setVisibility(View.GONE);
                                noResult.setVisibility(View.GONE);
                                adapter.getFilter().filter(s.toString().trim());//替换成本句后消失黑框！！！
                                searchResult.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        });
        getTags();

        searchResult.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState != 0) {
                    //关闭软键盘
                    PublicTools.closeKeybord(getActivity());
                }
            }
        });
    }

    private void getCategories() {
        Map<String, Object> maps = new HashMap<>();
        String[] keys = new String[]{"merchantCategories"};
        maps.put("code", keys);
        choiceList_p.loadChoiceList(maps);
    }

    private void getTags() {
        Map<String, Object> map = new HashMap<>();
        map.put("no_user_id", 1);
        merchantListener.getTagInfo(map, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {

                topTen = (List<String>) o[0];
                tags = (List<String>) o[1];
                if (topTen.size() > 0) {
                    topList.setSearchTopList(2 * PublicTools.dip2px(getContext(), 2), topTen);
                    topList.setTag("1");
                }

                if (tags.size() > 0) {
                    adapter = new SearchResultAdapter(getContext(), tags);
                    searchResult.setAdapter(adapter);
                    adapter.setOnitemClick(new SearchResultAdapter.OnitemClick() {
                        @Override
                        public void itemClick(final String tag) {
                            searchResultFlag = true;
                            searchEdit.setText(tag);
                            searchEdit.setSelection(tag.length());
                            Map<String, Object> maps = new HashMap<>();
                            if (!TextUtils.isEmpty(tag)) {
                                maps.put("searchKeyword", tag);
                            }
                            merchantListener.haveData(maps, new ActionCallBack() {
                                @Override
                                public void onSuccess(Object... o) {
                                    String data = (String) o[0];
                                    if (data.equals("0")) {
                                        noResult.setVisibility(View.VISIBLE);
                                        findCl.setVisibility(View.GONE);
                                        searchResult.setVisibility(View.GONE);
                                    } else {
                                        adapter.getFilter().filter(" ");
                                        adapter.setString(null);
                                        searchResult.clearTextFilter();
                                        findCl.setVisibility(View.VISIBLE);
                                        searchResult.setVisibility(View.GONE);
                                        searchEdit.setText("");
                                        Intent intent = new Intent(getContext(), SearchMerchatDataActivity.class);
                                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        intent.putExtra("keyword", tag);
                                        startActivityForResult(intent, 122);
                                        locationQuanxian = false;
                                    }
                                }

                                @Override
                                public void onError(String e) {

                                }
                            });
                        }
                    });
                    //为ListView启动过滤
                    searchResult.setTextFilterEnabled(true);
                }
            }

            @Override
            public void onError(String e) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            //PublicTools.openKeybord(searchEdit);
        }

//        if (requestCode == 122 && resultCode == RESULT_OK) {
        if (requestCode == 122) {
            show(300);
            //PublicTools.openKeybord(searchEdit);
        }
    }

    private int status = 0;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (!PublicTools.isFastClick()) {
            return;
        }
        switch (id) {
            case R.id.close:
                PublicTools.closeKeybord(getActivity());
                ((MainTab) getActivity()).changeTab(0);
                if (adapter != null) {
                    adapter.getFilter().filter(" ");
                    adapter.setString(null);
                }
                searchResult.clearTextFilter();
                findCl.setVisibility(View.VISIBLE);
                searchResult.setVisibility(View.GONE);
                searchEdit.setText("");
                break;
            case R.id.open_map:
            case R.id.open_maps:
                status = 0;
                jump = true;

                locationSer();
                break;
        }
    }

    @Override
    public void setChoiceList(GetChoiceDatas getChoiceDatas) {
        if (getChoiceDatas != null) {
            merchantCategories = getChoiceDatas.getMerchantCategories();
            categoriesList.setSearchCategoriesList(2 * PublicTools.dip2px(getContext(), 8), merchantCategories);
            categoriesList.setTag("1");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (tabNum == 1&&searchEdit!=null) {
                        searchEdit.setFocusable(true);//自动弹出软键盘
                        searchEdit.setFocusableInTouchMode(true);
                        searchEdit.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInputFromInputMethod(searchEdit.getWindowToken(), 0);
                        imm.toggleSoftInputFromWindow(searchEdit.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }, 300);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (categoriesList.getTag() == null) {
                getCategories();
            }
            if (topList.getTag() == null) {
                getTags();
            }
        }
    }

    private boolean getCallBack = false;

    private TiShiDialog dialog;
    public boolean jump = false;//控制只有点击 pay with payo按钮时才判断是否开启定位以及跳转到输入金额页；进入界面 或者返回改页面不开启定位以及跳转到输入金额页

    public void locationSer() {
        if (!isOpenLocService(getActivity()) && jump) {
            gotoLocServiceSettings();
        } else if (!checkLocationPermission() && jump) {
            if (dialog == null) {
                dialog = new TiShiDialog(getActivity());
                dialog.setOnGuanBiLeft(new TiShiDialog.GuanBiLeft() {//左边按钮
                    @Override
                    public void GuanBiLeft() {
                        locationQuanxian = false;
                        dialog = null;
                    }
                });
                dialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                    @Override
                    public void GuanBi() {
                        locationQuanxian = false;
                        ((MainTab) getActivity()).setGetLocaion(true);
                        ((MainTab) getActivity()).onResume();
                        dialog = null;
                    }
                });
                dialog.ShowDialog("Allow Location Access?", "To view nearby venues in your city to pay with this app, you must allow “Payo” to access your location.", "Allow");
            }
        } else if (jump) {//正常跳转
            if (status == 0) {
                Intent intent = new Intent(getContext(), MapActivity.class);
                startActivityForResult(intent, 122);
                locationQuanxian = false;
            } else if (status == 1) {
                Intent intent = new Intent(getContext(), SearchMerchatDataActivity.class);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                intent.putExtra("searchKeyword", searchKeyword);
                intent.putExtra("categories", categoriesStr);
                startActivityForResult(intent, 122);
                locationQuanxian = false;
            } else if (status == 2) {
                Intent intent = new Intent(getContext(), SearchMerchatDataActivity.class);
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                intent.putExtra("keyword", keyword);
                startActivityForResult(intent, 122);
                locationQuanxian = false;
            }
        }
    }

    /**
     * 判断位置权限
     */
    public boolean checkLocationPermission() {
        String[] PERMISSION_STORAGE =
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        try {
            if (hasPermissions(Objects.requireNonNull(getActivity()), PERMISSION_STORAGE)) {
                //有权限
                return true;
            } else {
                L.log("申请权限");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean locationQuanxian = false;
/*
    @Override
    public void permissionStatus(int requestCode) {
        super.permissionStatus(requestCode);
        L.log("权限Code:" + requestCode);//106
        if (requestCode == 106 && locationQuanxian) {
            locationSer();
        }
//        jump = false;
    }*/

    public static boolean isOpenLocService(final Context context) {
        boolean isGps = false; //推断GPS定位是否启动

        boolean isNetwork = false; //推断网络定位是否启动

        if (context != null) {
            LocationManager locationManager

                    = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if (locationManager != null) {
//通过GPS卫星定位，定位级别能够精确到街(通过24颗卫星定位，在室外和空旷的地方定位准确、速度快)

                isGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

//通过WLAN或移动网络(3G/2G)确定的位置(也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物(建筑群或茂密的深林等)密集的地方定位)

                isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            }

            if (isGps || isNetwork) {
                return true;
            }
        }
        return false;
    }

    public boolean jumpToSetting = false;

    public void gotoLocServiceSettings() {
        jumpToSetting = true;
        final Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, 100);
    }


}
