package com.home.glx.uwallet.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.home.glx.uwallet.BaseFragment;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.LateFeeRecordDetailActivity;
import com.home.glx.uwallet.activity.PaySuccessActivity;
import com.home.glx.uwallet.activity.RecordDetailActivity;
import com.home.glx.uwallet.adapter.RecordAdapter;
import com.home.glx.uwallet.datas.RecordData;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.selfview.UwalletFilterDialog;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionFragment extends BaseFragment implements View.OnClickListener {
    private int maxPages = 0;
    private int page = 1;
    private RecyclerView listView;
    private UserListener userListener;
    private RecordAdapter adapter;
    private TextView noStr;
    private int currentTab = 0;
    private String chooseDate = "";
    private String chooseAmount = "";
    private LinearLayout noResult;
    private List<RecordData> dataList = new ArrayList<>();

   /* @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transaction, container, false);
        context = getActivity();
        chooseDate = "Any";
        chooseAmount = "Any";
        initView();
        return view;
    }*/

    @Override
    public int getLayout() {
        chooseDate = "Any";
        chooseAmount = "Any";
        return R.layout.fragment_transaction;
    }

    public void loadMore() {
        if (noResult.getVisibility() == View.VISIBLE) {
            return;
        }
        page++;
        getList();
    }

    public void refresh() {
        page = 1;
        chooseDate = "Any";
        chooseAmount = "Any";
        getList();
    }

    public void filter() {
        UwalletFilterDialog uwalletFilterDialog = new UwalletFilterDialog(getContext(), chooseDate, chooseAmount);
        uwalletFilterDialog.setOnClose(new UwalletFilterDialog.Choose() {
            @Override
            public void onChooseDate(String str) {
                if (!TextUtils.isEmpty(str) && !chooseDate.equals(str)) {
                    chooseDate = str;
                    page = 1;
                    getList();
                }
            }

            @Override
            public void onChooseAmont(String str) {
                if (!TextUtils.isEmpty(str) && !chooseAmount.equals(str)) {
                    chooseAmount = str;
                    page = 1;
                    getList();
                }
            }
        });
        uwalletFilterDialog.showDialog();
    }

    public void changeTab(int tab) {
        if (tab == 0) {
            if (currentTab != 0) {
                currentTab = 0;
                dataList.clear();
                if (adapter != null) {
                    adapter.notifyDataSetChang(dataList);
                }
                page = 1;
                getList();
            }
        } else if (tab == 1) {
            if (currentTab != 1) {
                currentTab = 1;
                dataList.clear();
                if (adapter != null) {
                    adapter.notifyDataSetChang(dataList);
                }
                page = 1;
                getList();
            }
        } else if (tab == 2) {
            if (currentTab != 2) {
                currentTab = 2;
                dataList.clear();
                if (adapter != null) {
                    adapter.notifyDataSetChang(dataList);
                }
                page = 1;
                getList();
            }
        }
    }

    @Override
    public void initView() {
        userListener = new UserModel(getContext());
        listView = (RecyclerView) view.findViewById(R.id.list_view);
        listView.setNestedScrollingEnabled(false);
        listView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        listView.setLayoutManager(linearLayoutManager1);
        /*listView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!listView.canScrollVertically(-1)){
                    ((WalletFragment)getParentFragment()).isScollToTop = true;
                    Log.i("liucl","已经滚动到顶部");
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });*/
        noResult = view.findViewById(R.id.no_result);
        noStr = view.findViewById(R.id.no_str);

        //切换字体
        TypefaceUtil.replaceFont(noStr, "fonts/gilroy_semiBold.ttf");

        getList();

    }

    private void getList() {
        if (page != 1 && page > maxPages) {
            return;
        }

        Map<String, Object> maps = new HashMap<>();
        maps.put("s", 10);
        maps.put("p", page);
        if (!TextUtils.isEmpty(chooseDate)) {
            if (chooseDate.equals("Last 50 days")) {
                maps.put("lastDays", "50");
            } else if (chooseDate.equals("Last 100 days")) {
                maps.put("lastDays", "100");
            } else if (chooseDate.contains("-")) {
                maps.put("queryDate", chooseDate);
            }
        }
        if (!TextUtils.isEmpty(chooseAmount)) {
            if (chooseAmount.contains("-")) {
                String[] amount = chooseAmount.split("-");
                maps.put("amtStart", amount[0]);
                maps.put("amtEnd", amount[1]);
            }
        }
        if (currentTab == 1) {
            maps.put("transType", "22");
        } else if (currentTab == 2) {
            maps.put("transType", "2");
        }
        if (userListener != null)
            userListener.getRecordNew(maps, new ActionCallBack() {
                @Override
                public void onSuccess(Object... o) {
                    maxPages = (Integer) o[0];
                    if (page == 1) {
                        dataList.clear();
                    }
                    if (page == maxPages) {
                        ((WalletFragment) getParentFragment()).refreshLayout.setNoMoreData(true);
                        ((WalletFragment) getParentFragment()).refreshLayout.finishLoadMoreWithNoMoreData();
                    } else {
                        ((WalletFragment) getParentFragment()).refreshLayout.setNoMoreData(false);
                    }
                    List<RecordData> recordDataList = (List<RecordData>) o[1];
                    dataList.addAll(recordDataList);
                    setData();
                }

                @Override
                public void onError(String e) {
                    noResult.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    ((WalletFragment) getParentFragment()).refreshLayout.setNoMoreData(true);
                    ((WalletFragment) getParentFragment()).refreshLayout.finishLoadMoreWithNoMoreData();
                }
            });
    }

    private void setData() {
        if (dataList.size() == 0) {
            noResult.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
            ((WalletFragment) getParentFragment()).refreshLayout.setNoMoreData(true);
            ((WalletFragment) getParentFragment()).refreshLayout.finishLoadMoreWithNoMoreData();
            return;
        } else {
            noResult.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            //((WalletFragment)getParentFragment()).refreshLayout.setNoMoreData(false);
        }


        listView.setFocusableInTouchMode(false);
        listView.setFocusable(false);

        if (adapter != null) {
            adapter.notifyDataSetChang(dataList);
        } else {
            adapter = new RecordAdapter(getContext(), dataList);
            adapter.setOnitemClick(new RecordAdapter.OnitemClick() {
                @Override
                public void itemClick(String payAmount, String transType, String id) {
                    Intent intent;
                    if (transType.equals("22")) {
                        intent = new Intent(getContext(), RecordDetailActivity.class);
                    } else {
                        intent = new Intent(getContext(), PaySuccessActivity.class);
                    }
                    intent.putExtra("id", id);
                    startActivity(intent);

                }

                @Override
                public void itemClickLateFee(String id) {
                    Intent intent = new Intent(getContext(), LateFeeRecordDetailActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

        }
    }
}