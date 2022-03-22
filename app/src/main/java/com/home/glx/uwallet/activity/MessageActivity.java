package com.home.glx.uwallet.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.MessageAdapter;
import com.home.glx.uwallet.datas.MessageData;
import com.home.glx.uwallet.datas.PcDatas;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.selfview.TiShiDialogTwo;
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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;


/**
 * 消息列表
 */
public class MessageActivity extends MainActivity {

    private LinearLayout idNullImg;
    private ImageView idBack;
    private RefreshLayout refreshLayout;
    private RecyclerView idRecyclerview;
    private MessageAdapter messageAdapter;
    private List<MessageData> listData = new ArrayList<>();
    private TextView clearAll;
    private UserListener userListener;
    private int page = 1;
    private int maxPages = 0;
    private TextView noNotify;
    private TextView noNotify1;
    private TextView title;
    private TextView line;

    @Override
    public int getLayout() {
        return R.layout.activity_message_list;
    }

    @Override
    public void initView() {
        super.initView();

        line = findViewById(R.id.line);
        noNotify = findViewById(R.id.no_notify);
        noNotify1 = findViewById(R.id.no_notify1);
        title = findViewById(R.id.title);
        idBack = (ImageView) findViewById(R.id.id_back);
        idBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userListener = new UserModel(this);
        idNullImg = (LinearLayout) findViewById(R.id.id_null_img);
        clearAll = (TextView) findViewById(R.id.clear_all);
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listData.size() == 0) {
                    return;
                }
                TiShiDialogTwo tiShiDialogTwo = new TiShiDialogTwo();
                tiShiDialogTwo.show(MessageActivity.this, "", "Are you sure you'd like to delete all the messages?", "Cancel", "Confirm");
                tiShiDialogTwo.setOnShure(new TiShiDialogTwo.OnShure() {
                    @Override
                    public void shure() {
                        Map<String, Object> map = new HashMap<>();
                        userListener.noticeClearAll(map, new ActionCallBack() {
                            @Override
                            public void onSuccess(Object... o) {
                                page = 1;
                                getListData(new HashMap<String, Object>());
                            }

                            @Override
                            public void onError(String e) {

                            }
                        });
                    }
                });
            }
        });
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
                L.log("加载更多");
                page++;
                getListData(new HashMap<String, Object>());
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
                        getListData(new HashMap<String, Object>());
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });
        idRecyclerview = (RecyclerView) findViewById(R.id.id_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        idRecyclerview.setLayoutManager(linearLayoutManager);
        //切换字体
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(clearAll, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(noNotify, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(noNotify1, "fonts/gilroy_regular.ttf");
        getListData(new HashMap<String, Object>());
    }

    private void setData(String dataStr) {

        if (page == 1) {
            listData.clear();
        }

        Gson gson1 = new Gson();
        List<MessageData> list = gson1.fromJson(dataStr, new TypeToken<List<MessageData>>() {
        }.getType());

        listData.addAll(list);
        if (listData.size() > 0) {
            line.setVisibility(View.VISIBLE);
            idNullImg.setVisibility(View.GONE);
            clearAll.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
            idNullImg.setVisibility(View.VISIBLE);
            clearAll.setVisibility(View.GONE);
        }
        if (messageAdapter != null) {
            messageAdapter.notifyDataSetChanged();
        } else {
            messageAdapter = new MessageAdapter(this, listData);
            messageAdapter.setOnItemClick(new MessageAdapter.OnItemClick() {
                @Override
                public void itemClick(MessageData messageData) {
                    //消息点击
                    readMessage(messageData.getId());
                    messageData.setIsRead(1);
                    messageAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(MessageActivity.this,
                            MessageDetailActivity.class);
                    intent.putExtra("title", messageData.getTitle());
                    intent.putExtra("id", messageData.getId());
                    intent.putExtra("content", messageData.getContent());
                    startActivity(intent);
                }

                @Override
                public void itemDeleteClick(final MessageData messageData) {
                    TiShiDialogTwo choiceYesNoDialog = new TiShiDialogTwo();
                    choiceYesNoDialog.setOnShure(new TiShiDialogTwo.OnShure() {
                        @Override
                        public void shure() {
                            deleteMessage(messageData.getId(), messageData);
                        }
                    });
                    choiceYesNoDialog.show(MessageActivity.this, "", "Are you sure you'd like to delete this message?", "Cancel", "Confirm");
                }
            });
            idRecyclerview.setAdapter(messageAdapter);
        }
    }

    public void getListData(Map<String, Object> maps) {
        if (page != 1 && page > maxPages) {
            return;
        }
        maps.put("s", 15);
        maps.put("p", page);
        maps.put("scs", "created_date(desc)");

        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(MessageActivity.this, StaticParament.USER);
                String id = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.notice(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("消息列表:" + dataStr);
                Gson gson = new Gson();
                maxPages = gson.fromJson(pc, PcDatas.class).getMaxPages();
                if (page == maxPages) {
                    refreshLayout.setNoMoreData(true);
                    refreshLayout.finishLoadMoreWithNoMoreData();
                } else {
                    refreshLayout.setNoMoreData(false);
                }
                setData(dataStr);
            }


            @Override
            public void resError(String error) {


            }
        });
    }


    /**
     * 删除消息
     */
    private void deleteMessage(final String id, final MessageData messageData) {
        Map<String, Object> maps = new HashMap<>();

        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("删除消息参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.noticeDelete(headerMap, id, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("删除消息:" + dataStr);
                TiShiDialog tiShiDialog = new TiShiDialog(MessageActivity.this);
                tiShiDialog.ShowDialog("This message has been deleted");
                tiShiDialog.setOnGuanBi(new TiShiDialog.GuanBi() {
                    @Override
                    public void GuanBi() {
                        listData.remove(messageData);
                        messageAdapter.notifyData(listData);
                    }
                });
            }

            @Override
            public void resError(String error) {

            }
        });
    }


    /**
     * 消息已读
     */
    private void readMessage(final String id) {
        Map<String, Object> maps = new HashMap<>();
        maps.put("id", id);
        maps.put("isRead", 1);

        RequestUtils requestUtils1 = new RequestUtils(this, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {

                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("消息已读参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.noticeModify(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                L.log("消息已读:" + dataStr);

            }

            @Override
            public void resError(String error) {

            }
        });
    }

}
