package com.home.glx.uwallet.activity.xzc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.ChooseCardListPopAdapter;
import com.home.glx.uwallet.datas.BankDatas;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseCardListActivity extends MainActivity {
    private ChooseCardListPopAdapter adapter;
    private RecyclerView recyclerView;
    private UserListener userListener;
    private List<BankDatas> list = new ArrayList<>();
    private String selectId;
    private BankDatas bankDatas;

    @Override
    public int getLayout() {
        return R.layout.pop_choose_bank_card;
    }

    /**
     * 更改式activity在屏幕中显示的位置
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //即设定DecorView在PhoneWindow里的位置
        View view = getWindow().getDecorView();
        WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;//设置宽度满屏
        lp.height = dp2px(this, 400);
        getWindowManager().updateViewLayout(view, lp);
    }

    @Override
    public void initView() {
        super.initView();
        TypefaceUtil.replaceFont(findViewById(R.id.tv_payo1), "fonts/gilroy_semiBold.ttf");

        this.selectId = getIntent().getStringExtra("selectedCardId");
        this.bankDatas = (BankDatas) getIntent().getSerializableExtra("bankDatas");
        userListener = new UserModel(this);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        getBankList();

        TypefaceUtil.replaceFont(findViewById(R.id.tv_payo1), "fonts/gilroy_bold.ttf");
        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getBankList() {
        Map<String, Object> maps = new HashMap<>();
        //0:账户  1:卡支付
        maps.put("cardType", "1");
        userListener.getCardList(maps, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                list = (List<BankDatas>) o[0];

                List<BankDatas> newList = new ArrayList<>();
                newList.add(bankDatas);
                for (BankDatas datas : list) {//把选中的排在第一个 用于弹窗展示。
                    if (!datas.getCardNo().equals(bankDatas.getCardNo())) {
                        newList.add(datas);
                    }
                }
                list.clear();
                list.addAll(newList);

                if (adapter == null) {
                    adapter = new ChooseCardListPopAdapter(ChooseCardListActivity.this, list, selectId);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnitemClick(new ChooseCardListPopAdapter.OnitemClick() {
                        @Override
                        public void itemClick(BankDatas bankDatas) {
                            Intent intent = getIntent();
                            intent.putExtra("bankDatas", bankDatas);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void saveCard() {

                        }

                        @Override
                        public void addCard() {//添加银行卡
                            if (!PublicTools.isFastClick()) {
                                return;
                            }
                            startActivityForResult(new Intent(ChooseCardListActivity.this, NewAddBankCardActivityDialog.class), 100);
                        }
                    });
                } else {
                    adapter.notifyDataChanged(list);
                }
            }

            @Override
            public void onError(String e) {
            }
        });
    }


    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp
     * @return px
     */
    public static int dp2px(Context context, int dpValue) {
        return (int) (dpValue * getDensity(context) + 0.5f);
    }
    /**
     * 获取当前屏幕分辨率
     *
     * @param context 上下文
     * @return 分辨率
     */
    public static float getDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }
}
