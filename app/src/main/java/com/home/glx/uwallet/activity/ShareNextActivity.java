package com.home.glx.uwallet.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.EarnListAdapter;
import com.home.glx.uwallet.adapter.MerchantInfoAdapter;
import com.home.glx.uwallet.adapter.list_adapter.BindingAdapterItem;
import com.home.glx.uwallet.datas.InvitedUserListData;
import com.home.glx.uwallet.datas.ShareData;
import com.home.glx.uwallet.mvp.LoadShareMsg;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.UserModel;
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

public class ShareNextActivity extends MainActivity {
    private ListView listView;
    private EarnListAdapter adapter;
    private SmartRefreshLayout refreshLayout;
    private UserListener userListener;
    private ImageView back;
    private List<InvitedUserListData> listData = new ArrayList<>();
    private int maxPages = 0;
    private int page = 1;
    private LinearLayout shareLl;
    private TextView firstPurchase;
    private String shareUrl;

    @Override
    public int getLayout() {
        return R.layout.activity_share_next;
    }

    @Override
    public void initView() {
        super.initView();
        userListener = new UserModel(this);
        back = (ImageView) findViewById(R.id.id_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.earn_list);
        TextView title = findViewById(R.id.title);
        TextView getCredit = findViewById(R.id.get_credit);
        firstPurchase = findViewById(R.id.first_purchase);
        TextView shareMore = findViewById(R.id.share_more);
        TextView invites = findViewById(R.id.invite_friends);
        shareUrl = getIntent().getStringExtra("url");
        shareLl = findViewById(R.id.share_ll);
        shareLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                if (!PublicTools.isFastClick()) {
                    return;
                }
                if (TextUtils.isEmpty(shareUrl)) {
                    LoadShareMsg loadShareMsg = new LoadShareMsg(ShareNextActivity.this);
                    loadShareMsg.setOnData(new LoadShareMsg.OnShareMsg() {
                        @Override
                        public void msg(ShareData data) {
                            //shareUrl = String.format(shareUrl, data.getInviteCode(), data.getInviteCode());
                            shareUrl = data.getUrl();
                            Intent localIntent = new Intent("android.intent.action.SEND");
                            localIntent.setType("text/plain");
                            localIntent.putExtra("android.intent.extra.SUBJECT", "Payo");
                            localIntent.putExtra("android.intent.extra.TEXT", shareUrl);
                            startActivity(Intent.createChooser(localIntent,"Share & Earn PAYO"));
                        }
                    });
                    loadShareMsg.load(new HashMap<String, Object>());
                } else {
                    Intent localIntent = new Intent("android.intent.action.SEND");
                    localIntent.setType("text/plain");
                    localIntent.putExtra("android.intent.extra.SUBJECT", "Payo");
                    localIntent.putExtra("android.intent.extra.TEXT", shareUrl);
                    startActivity(Intent.createChooser(localIntent,"Share & Earn PAYO"));
                }
            }
        });
        //切换字体
        TypefaceUtil.replaceFont(shareMore,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(invites,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(title,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(getCredit,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(firstPurchase,"fonts/gilroy_bold.ttf");

        refreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
                L.log("加载更多");
                page++;
                reqInvitedUserList();
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
                        listData.clear();
                        reqInvitedUserList();
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });

        reqInvitedUserList();
    }

    /**
     * 邀请人列表
     */
    public void reqInvitedUserList() {
        if (page != 1 && page > maxPages) {
            return;
        }

        Map<String, Object> maps = new HashMap<>();
        maps.put("s", 10);
        maps.put("p", page);
        maps.put("hasPurchased", 1);
        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(ShareNextActivity.this, StaticParament.USER);
        String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
        maps.put("inviterId", id);
        maps.put("no_user_id", 1);

        userListener.invitedUserList(maps, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                maxPages = (int)o[0];
                if (page == 1) {
                    listData.clear();
                }
                if (page == maxPages) {
                    refreshLayout.setNoMoreData(true);
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.setNoMoreData(false);
                }
                List<InvitedUserListData> data = (List<InvitedUserListData>)o[1];
                listData.addAll(data);
                setData();
            }

            @Override
            public void onError(String e) {

            }
        });
    }

    private void setData() {
        if (listData.size() == 0) {
            refreshLayout.setVisibility(View.GONE);
            shareLl.setVisibility(View.VISIBLE);
            firstPurchase.setVisibility(View.GONE);
        } else {
            shareLl.setVisibility(View.GONE);
            firstPurchase.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.VISIBLE);
        }

        listView.setFocusableInTouchMode(false);
        listView.setFocusable(false);

        if (adapter != null) {
            adapter.notifyDataSetChang(listData);
        } else {
            adapter = new EarnListAdapter(ShareNextActivity.this, listData);
            listView.setAdapter(adapter);
        }
    }
}