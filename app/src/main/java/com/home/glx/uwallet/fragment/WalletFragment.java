package com.home.glx.uwallet.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.ChooseIllionActivity;
import com.home.glx.uwallet.activity.FenQuBanks_Activity;
import com.home.glx.uwallet.activity.InstalmentPendingNextActivity;
import com.home.glx.uwallet.activity.KycAndIllionResultActivity;
import com.home.glx.uwallet.activity.NewAddBankCardActivity;
import com.home.glx.uwallet.activity.RegistTwo_Activity;
import com.home.glx.uwallet.activity.SelectBankCardActivity;
import com.home.glx.uwallet.activity.SelectPayTypeActivity;
import com.home.glx.uwallet.activity.xzc.ChooseCardActivity;
import com.home.glx.uwallet.datas.BorrowByPayDayV2Data;
import com.home.glx.uwallet.datas.FenQiMsgDatas;
import com.home.glx.uwallet.datas.FenqifuStatue;
import com.home.glx.uwallet.datas.PayAllData;
import com.home.glx.uwallet.mvp.CheckPayPassword_in;
import com.home.glx.uwallet.mvp.CheckPayPassword_p;
import com.home.glx.uwallet.mvp.JiHuoFenQi_in;
import com.home.glx.uwallet.mvp.JiHuoFenQi_p;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.AddBankCardTipDialog;
import com.home.glx.uwallet.selfview.ConfirmRepayDialog;
import com.home.glx.uwallet.selfview.DampingReboundNestedScrollView;
import com.home.glx.uwallet.selfview.RepayPawDialog;
import com.home.glx.uwallet.tools.CalcTool;
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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class WalletFragment extends FirstTabFragment implements View.OnClickListener,
        JiHuoFenQi_in.View, CheckPayPassword_in.View {
    private TextView instalment;
    private TextView transactions;
    private TextView overDue;
    private TextView overDueTv;
    private DampingReboundNestedScrollView haveNo;
    public SmartRefreshLayout refreshLayout;
    private ConstraintLayout haveIns;
    private CardView noIns;
    private CardView instalmentOpen;
    private CardView cardOpen;
    private TextView unLock;
    private TextView payLater;
    private TextView payLess;
    private TextView payLaterText;
    private TextView payLaterText1;
    private TextView payLessText1;
    private TextView payLessText;
    private TextView title;
    private TextView toUser;
    private TextView title1;
    //是否开通个业务
    private JiHuoFenQi_p presentJiHuo;
    private TextView instalmentText;
    private TextView idCanUse;
    private ConstraintLayout overdueLl;
    private ConstraintLayout newOverdueLl;
    private TextView idHaveUse;
    private TextView idAllMoney;
    private TextView pending;
    private TextView account;
    private String card = "";
    private ConstraintLayout selectOneTab;
    private ConstraintLayout layout_title;
    private ConstraintLayout selectTwoTab;
    private ConstraintLayout payAllCl;
    private TransactionFragment transactionFragment;
    private NewInstalmentFragment instalmentFragment;
    private CardView all;
    private CardView instalmentCard;
    private CardView bankCard;
    private CardView bankAccount;
    private TextView allText;
    private TextView instalmentText1;
    private TextView chooseDate;
    private TextView cardText;
    private CardView addCard;
    private TextView addTv;
    private TextView fullTv;
    private CardView addCard1;
    private TextView addTv1;
    private TextView fullTv1;
    private TextView selectSort;
    private TextView selectTop;
    private ImageView choiceIcon;
    private TextView payAll;
    private NestedScrollView scrollView;
    private UserListener userListener;
    private float downY;
    private float upY;
    public static boolean hasExpaned = true;

    private int tabNum = 0;
    private SharePreferenceUtils sharePreferenceUtils;
    private MainTab.MyOnTouchListener myOnTouchListener;
    private ConstraintLayout mTitle;
    public boolean noInsFlag = false;

    private CheckPayPassword_p checkPayPassword_p;

    private ConfirmRepayDialog confirmRepayDialog;
    private ImageView noInsCard;
    private ConstraintLayout oldInstal;
    private TextView oldInstalText;
    private boolean haveCard;
    private boolean isOverDue;
    private TextView allMoney;
    private TextView payAllBtn;
    private String outstandingMoney = "";
    private List<String> borrowIdList;
    private List<String> repayIdList;
    private List<BorrowByPayDayV2Data> borrowData;

    public List<String> getRepayIdList() {
        return repayIdList;
    }

    public void setRepayIdList(List<String> repayIdList) {
        this.repayIdList = repayIdList;
    }

    public String getOutstandingMoney() {
        return outstandingMoney;
    }

    private String creditCardState;
    private String cardState;
    private String installmentState;
    private boolean mFirst = true;

    public static boolean exe = false;

    public void setOutstandingMoney(String outstandingMoney) {
        this.outstandingMoney = outstandingMoney;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_wallet;
    }


    public static WalletFragment newInstance() {
        Bundle args = new Bundle();
        WalletFragment fragment = new WalletFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();
        checkPayPassword_p = new CheckPayPassword_p(getContext(), this);
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        int width = metric.widthPixels;
        userListener = new UserModel(getContext());
        sharePreferenceUtils = new SharePreferenceUtils(getContext(), StaticParament.USER);
        presentJiHuo = new JiHuoFenQi_p(getContext(), this);
        scrollView = (NestedScrollView) view.findViewById(R.id.scroll_view);
        payAll = (TextView) view.findViewById(R.id.pay_all);
        addCard = (CardView) view.findViewById(R.id.add_card);
        addCard1 = (CardView) view.findViewById(R.id.add_card1);
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        overDue = view.findViewById(R.id.over_due);
        overDueTv = view.findViewById(R.id.over_instalment_text);
        haveNo = (DampingReboundNestedScrollView) view.findViewById(R.id.have_no);
        payAllCl = view.findViewById(R.id.pay_all_cl);
        selectOneTab = (ConstraintLayout) view.findViewById(R.id.choose_one_tab);
        selectTwoTab = (ConstraintLayout) view.findViewById(R.id.choose_two_tab);
        unLock = (TextView) view.findViewById(R.id.unlocak);
        instalmentOpen = (CardView) view.findViewById(R.id.id_info);
        haveIns = (ConstraintLayout) view.findViewById(R.id.have_ins);
        layout_title = (ConstraintLayout) view.findViewById(R.id.layout_title);
        noIns = (CardView) view.findViewById(R.id.no_ins);
        toUser = (TextView) view.findViewById(R.id.to_use);
        addTv = view.findViewById(R.id.add);
        fullTv = view.findViewById(R.id.full);
        addTv1 = view.findViewById(R.id.add11);
        fullTv1 = view.findViewById(R.id.full11);
        cardOpen = (CardView) view.findViewById(R.id.id_info1);
        instalment = (TextView) view.findViewById(R.id.instalment);
        idCanUse = (TextView) view.findViewById(R.id.available_amount);
        instalmentText = (TextView) view.findViewById(R.id.instalment_text);
        idHaveUse = (TextView) view.findViewById(R.id.have_use_money);
        idAllMoney = (TextView) view.findViewById(R.id.all_money);
        transactions = (TextView) view.findViewById(R.id.transactions);
        overdueLl = (ConstraintLayout) view.findViewById(R.id.over_due_flag);
        newOverdueLl = (ConstraintLayout) view.findViewById(R.id.new_over_due_flag);
        payLater = (TextView) view.findViewById(R.id.start_now);
        TextView startLess = (TextView) view.findViewById(R.id.start_less);
        payLaterText = (TextView) view.findViewById(R.id.id_merchant_name);
        payLaterText1 = (TextView) view.findViewById(R.id.id_popular_list);
        payLessText = (TextView) view.findViewById(R.id.id_merchant_name1);
        payLessText1 = (TextView) view.findViewById(R.id.id_popular_list1);
        title = (TextView) view.findViewById(R.id.title);
        title1 = (TextView) view.findViewById(R.id.title1);
        pending = (TextView) view.findViewById(R.id.pending);
        account = (TextView) view.findViewById(R.id.account);
        chooseDate = (TextView) view.findViewById(R.id.choose_date);
        all = (CardView) view.findViewById(R.id.card_all);
        instalmentCard = (CardView) view.findViewById(R.id.card_instalment);
        bankCard = (CardView) view.findViewById(R.id.card_bank);
        allText = (TextView) view.findViewById(R.id.all_text);
        instalmentText1 = (TextView) view.findViewById(R.id.instalment_text1);
        cardText = (TextView) view.findViewById(R.id.card_text);
        bankAccount = view.findViewById(R.id.bank_account);
        mTitle = view.findViewById(R.id.title_fragment);
        selectSort = view.findViewById(R.id.select);
        selectTop = view.findViewById(R.id.select_top);
        choiceIcon = view.findViewById(R.id.choice_icon);
        noInsCard = view.findViewById(R.id.no_instal_card);
        oldInstal = view.findViewById(R.id.old_instal_cl);
        oldInstalText = view.findViewById(R.id.why_do);

        oldInstalText.setOnClickListener(this);
        noInsCard.setOnClickListener(this);
        payAll.setOnClickListener(this);
        selectSort.setOnClickListener(this);
        addCard.setOnClickListener(this);
        addCard1.setOnClickListener(this);
        all.setOnClickListener(this);
        instalmentCard.setOnClickListener(this);
        bankCard.setOnClickListener(this);
        chooseDate.setOnClickListener(this);

        instalmentOpen.setOnClickListener(this);
        cardOpen.setOnClickListener(this);
//        instalment.setOnClickListener(this);
        noIns.setOnClickListener(this);
        transactions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setTabSelection(0);
                }
                return true;
            }
        });
        instalment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setTabSelection(1);
                }
                return true;
            }
        });
//        transactions.setOnClickListener(this);
        bankAccount.setOnClickListener(this);

        if (((MainTab) getActivity()).isFromFirst()) {
            //为了判断老用户分期付是否绑卡，若没绑卡需要弹窗提示。因此调用接口成功后再判断是弹窗还是跳转页面。
//            setTabSelection(1);
//            ((MainTab)getActivity()).setFromFirst(false);
        } else {
            setTabSelection(1);
        }
        TextView outStand = view.findViewById(R.id.out_stand);
        allMoney = view.findViewById(R.id.pay_all_money);
        payAllBtn = view.findViewById(R.id.pay_all_btn);
        TextView noInsName = view.findViewById(R.id.id_merchant_names);
        TextView noInsText = view.findViewById(R.id.id_popular_lists);
        TextView add1 = view.findViewById(R.id.add1);
        payAllBtn.setOnClickListener(this);
        //切换字体|
        TypefaceUtil.replaceFont(outStand, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(allMoney, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(payAllBtn, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(oldInstalText, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(startLess, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(add1, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(noInsName, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(noInsText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(payAll, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(instalmentText1, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(allText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(cardText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(addTv, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(fullTv, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(addTv1, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(fullTv1, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(chooseDate, "fonts/acumin_boldPro.ttf");
        TypefaceUtil.replaceFont(account, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(idAllMoney, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(idHaveUse, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(pending, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(idCanUse, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(overDue, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(overDueTv, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(toUser, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(instalment, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(instalmentText, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(transactions, "fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(unLock, "fonts/casta_bold.ttf");
        TypefaceUtil.replaceFont(payLater, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(payLess, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(payLaterText, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(payLaterText1, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(payLessText, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(payLessText1, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(title, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(title1, "fonts/gilroy_semiBold.ttf");
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
                L.log("加载更多");
                if (tabNum == 0) {
                    if (transactionFragment != null) {
                        transactionFragment.loadMore();
                    }
                } else {
                    if (instalmentFragment != null) {
                        //instalmentFragment.loadMore();
                    }
                }
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
                        SharePreferenceUtils sharePreferenceUtils1 = new SharePreferenceUtils(getActivity(), StaticParament.USER);
                        String userId1 = (String) sharePreferenceUtils1.get(StaticParament.USER_ID, "");
                        Map<String, Object> maps1 = new HashMap<>();
                        maps1.put("userId", userId1);
                        maps1.put("stripeState", 0);//用户区分接入stripe前后的新老用户

                        jiaoyanFenqifu(maps1);
//                        if (tabNum == 0) { //目的：通过h5进行支付或者还款 页面刷新时两个页签下的列表都得刷新。
                            if (transactionFragment != null) {
                                transactionFragment.refresh();
                            }
//                        } else {
                            if (instalmentFragment != null) {
                                instalmentFragment.refresh();
                            }
//                        }
                        refreshLayout.setNoMoreData(false);//恢复上拉状态
                    }
                }, 2000);
            }
        });

        /*myOnTouchListener = new MainTab.MyOnTouchListener() {
            @Override
            public boolean onTouch(MotionEvent event) {
                if (isHidden()) {
                    return false;
                }
                if (refreshLayout.getVisibility() == View.GONE) {
                    return false;
                }
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        downY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        upY = event.getY();
                        int location = getLocation(mTitle);
                       *//* if (location == titleFirstToTop) {
                            Log.d("liucl", "Scroll到顶了: 需要显示布局哦");
                            mAppbarLayout.setExpanded(true);
                            isScollToTop = false;
                            return true;
                        }*//*
                        Log.d("liucl", "距离顶部距离： " + location);
                        if (downY - upY > 50){
                            Log.d("liucl", "onTouch: 需要隐藏布局");
                            if (hasExpaned) {
                                hasExpaned = false;
                                mAppbarLayout.setExpanded(false);
                                isScollToTop = false;
                                if (instalmentFragment != null) {
                                    instalmentFragment.changeList(1);
                                }
                            }
                            //refreshLayout.scrollTo(0, 200);
//                            mainCl.scrollTo(0, 200);
                        } else if (upY - downY > 50 && location > 200){
                            Log.d("liucl", "onTouch: 需要显示布局");
                            if (!hasExpaned) {
                                hasExpaned = true;
                                mAppbarLayout.setExpanded(true);
                                isScollToTop = false;
                                if (instalmentFragment != null) {
                                    instalmentFragment.changeList(0);
                                }
                            }
                        }
                        break;

                }
//                gestureDetector.onTouchEvent(ev);
                return false;
            }
        };
        ((MainTab) getActivity()).registerMyOnTouchListener(myOnTouchListener);*/
    }

    public void setPayAllData(String money, List<String> borrowIdLists, List<String> repayIdLists, List<BorrowByPayDayV2Data> data) {
        if (!TextUtils.isEmpty(money) && Float.parseFloat(money) != 0) {
            setOutstandingMoney(money);
            this.borrowIdList = borrowIdLists;
            this.repayIdList = repayIdLists;
            this.borrowData = data;
            allMoney.setText("$" + PublicTools.twoend(money));
            if (tabNum == 1) {
                payAllCl.setVisibility(View.VISIBLE);
            }
        } else {
            setOutstandingMoney("");
            payAllCl.setVisibility(View.GONE);
        }
    }

    // View纵坐标
    public int getLocation(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        int distance = location[1];
        return distance;
    }

    public void setTabSelection(int index) {
        /*if (index == 1 && noInsCard.getVisibility() == View.VISIBLE) {
            AddBankCardTipDialog dialog = new AddBankCardTipDialog(getContext());
            dialog.setOnitemClick(new AddBankCardTipDialog.OnitemClick() {
                @Override
                public void itemClick() {
                    if (haveCard) {
                        Intent intent = new Intent(getContext(), SelectBankCardActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getContext(), NewAddBankCardActivity.class);
                        intent.putExtra("fromAddCard", 1);
                        startActivity(intent);
                    }
                }
            });
            dialog.show();
            return;
        }*/
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        hideFragment(ft);
        switch (index) {
            case 0:
                scrollView.scrollTo(0, 0);
                payAllCl.setVisibility(View.GONE);
                if (isOverDue) {
                    newOverdueLl.setVisibility(View.VISIBLE);
                    instalment.setText("");
                }
                selectOneTab.setVisibility(View.VISIBLE);
                selectTwoTab.setVisibility(View.GONE);
                if (instalmentText.getTag().toString().equals("1")) {
                    instalmentText.setTextColor(getResources().getColor(R.color.pay_text_gray));
                    TypefaceUtil.replaceFont(instalmentText, "fonts/gilroy_semiBold.ttf");
                }
                TypefaceUtil.replaceFont(transactions, "fonts/gilroy_bold.ttf");
                TypefaceUtil.replaceFont(instalment, "fonts/gilroy_semiBold.ttf");
                transactions.setTextColor(getResources().getColor(R.color.colorBackGround));
                transactions.setBackgroundResource(R.drawable.wallet_black_line);
                instalment.setTextColor(getResources().getColor(R.color.pay_text_gray));
                instalment.setBackgroundResource(R.drawable.wallet_gray_line_right);

                refreshLayout.setEnableLoadMore(true);
                tabNum = 0;
                if (transactionFragment == null) {
                    transactionFragment = new TransactionFragment();
                    ft.add(R.id.fl, transactionFragment);
                } else {
                    ft.show(transactionFragment);
                }
                break;

            case 1:
                scrollView.scrollTo(0, 0);
                if (!TextUtils.isEmpty(getOutstandingMoney()) && Float.parseFloat(getOutstandingMoney()) != 0) {
                    payAllCl.setVisibility(View.VISIBLE);
                }
                selectOneTab.setVisibility(View.GONE);
                if (!noInsFlag) {
                    selectTwoTab.setVisibility(View.VISIBLE);
                }
                if (instalmentText.getTag().toString().equals("1")) {
                    instalmentText.setTextColor(getResources().getColor(R.color.colorBackGround));
                    TypefaceUtil.replaceFont(instalmentText, "fonts/gilroy_bold.ttf");
                }
                newOverdueLl.setVisibility(View.GONE);
                instalment.setText("Instalment");
                TypefaceUtil.replaceFont(transactions, "fonts/gilroy_semiBold.ttf");
                instalment.setTextColor(getResources().getColor(R.color.colorBackGround));
                instalment.setBackgroundResource(R.drawable.wallet_black_line);
                TypefaceUtil.replaceFont(instalment, "fonts/gilroy_bold.ttf");
                transactions.setTextColor(getResources().getColor(R.color.pay_text_gray));
                transactions.setBackgroundResource(R.drawable.wallet_gray_line_left);

                refreshLayout.setEnableLoadMore(false);
                tabNum = 1;
                if (instalmentFragment == null) {
                    instalmentFragment = new NewInstalmentFragment();
                    ft.add(R.id.fl, instalmentFragment);
                } else {
                    ft.show(instalmentFragment);
                }
                break;
        }
        ft.commitAllowingStateLoss();
    }

    //用于隐藏fragment
    private void hideFragment(FragmentTransaction ft) {
        if (transactionFragment != null) {
            ft.hide(transactionFragment);
        }
        if (instalmentFragment != null) {
            ft.hide(instalmentFragment);
        }
    }

    public void refrsh() {
        SharePreferenceUtils sharePreferenceUtils1 = new SharePreferenceUtils(getActivity(), StaticParament.USER);
        String userId1 = (String) sharePreferenceUtils1.get(StaticParament.USER_ID, "");
        Map<String, Object> maps1 = new HashMap<>();
        maps1.put("userId", userId1);
        maps1.put("stripeState", 0);//用户区分接入stripe前后的新老用户

        jiaoyanFenqifu(maps1);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {

            String payFlag = (String) sharePreferenceUtils.get(StaticParament.PAY_SUCCESS, "");
            if (payFlag.equals("1")||mFirstTime) {
                SharePreferenceUtils sharePreferenceUtils1 = new SharePreferenceUtils(getActivity(), StaticParament.USER);
                String userId1 = (String) sharePreferenceUtils1.get(StaticParament.USER_ID, "");
                Map<String, Object> maps1 = new HashMap<>();
                maps1.put("userId", userId1);
                maps1.put("stripeState", 0);//用户区分接入stripe前后的新老用户
                jiaoyanFenqifu(maps1);

                if (transactionFragment != null) {
                    transactionFragment.refresh();
                }
                if (instalmentFragment != null) {
                    instalmentFragment.refresh();
                }
                sharePreferenceUtils.put(StaticParament.PAY_SUCCESS, "0").commit();
            }
            mFirstTime = false;

        }
    }

    @Override
    public void onClick(View v) {
        if (!PublicTools.isFastClick()) {
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.pay_all_btn:
//                SharePreferenceUtils sharePreferenceUtils1 = new SharePreferenceUtils(getActivity(), StaticParament.USER);
//                String userId1 = (String) sharePreferenceUtils1.get(StaticParament.USER_ID, "");
//                Map<String, Object> maps1 = new HashMap<>();
//                maps1.put("userId", userId1);
//                jiaoyanFenqifu(maps1);

                if (installmentState.equals("2") && creditCardState.equals("2")) {
                    userListener.repayTotalInfo(new HashMap<String, Object>(), new ActionCallBack() {//展示Pay all的弹窗
                        @Override
                        public void onSuccess(Object... o) {//按钮展示详情信息
                            PayAllData payAllData = (PayAllData) o[0];
                            //if (payAllData.getHaveData().equals("true")) {
                            confirmRepayDialog = new ConfirmRepayDialog(getContext(), payAllData);
                            confirmRepayDialog.show();
                            //}
                        }

                        @Override
                        public void onError(String e) {

                        }
                    });
                } else {
                    Intent intentPay = new Intent(getContext(), InstalmentPendingNextActivity.class);
                    intentPay.putExtra("payAll", "payAll");
                    String feeAmount = "0";
                    List<String> overdueFeeIds = new ArrayList<>();
                    for (int i = 0; i < borrowData.size(); i++) {
                        if (borrowData.get(i).getList().get(borrowData.get(i).getList().size() - 1).getType().equals("2")) {
                            feeAmount = CalcTool.add(feeAmount, borrowData.get(i).getList().get(borrowData.get(i).getList().size() - 1).getRepayAmount());
                        }
                        overdueFeeIds.addAll(borrowData.get(i).getOverdueFeeIdList());
                    }
                    if (Float.parseFloat(feeAmount) != 0) {
                        intentPay.putExtra("lateFeeAmount", feeAmount);
                    }
                    intentPay.putExtra("overdueFeeId", (Serializable) overdueFeeIds);
                    intentPay.putExtra("borrowIds", (Serializable) borrowIdList);
                    intentPay.putExtra("repayIds", (Serializable) repayIdList);
                    intentPay.putExtra("chooseAmount", getOutstandingMoney());
                    startActivity(intentPay);
                }
                break;
            case R.id.no_instal_card:
                if (haveCard) {
                    Intent intent = new Intent(getContext(), SelectBankCardActivity.class);
                    intent.putExtra("fromCreateFenqi", 1);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), NewAddBankCardActivity.class);
                    intent.putExtra("kaitongfenqifuRoad", true);
                    intent.putExtra("clickXiaosuo", true);
//                    intent.putExtra("fromAddCard", 1);
                    startActivity(intent);
                }
                break;
            case R.id.why_do:
                AddBankCardTipDialog dialog1 = new AddBankCardTipDialog(getContext());
                dialog1.setOnitemClick(new AddBankCardTipDialog.OnitemClick() {
                    @Override
                    public void itemClick() {
                        if (haveCard) {
                            Intent intent = new Intent(getContext(), SelectBankCardActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getContext(), NewAddBankCardActivity.class);
                            intent.putExtra("kaitongfenqifuRoad", true);
                            intent.putExtra("clickXiaosuo", true);
                            startActivity(intent);
                        }
                    }
                });
                dialog1.show();
                break;
            case R.id.pay_all:
                userListener.repayTotalInfo(new HashMap<String, Object>(), new ActionCallBack() {
                    @Override
                    public void onSuccess(Object... o) {
                        PayAllData payAllData = (PayAllData) o[0];
                        //if (payAllData.getHaveData().equals("true")) {
                        confirmRepayDialog = new ConfirmRepayDialog(getContext(), payAllData);
                        confirmRepayDialog.show();
                        //}
                    }

                    @Override
                    public void onError(String e) {

                    }
                });
                break;
            case R.id.select:
                initPopWindow(selectTop);
                break;
            case R.id.instalment:
                setTabSelection(1);
                break;
            case R.id.transactions:
                setTabSelection(0);
                break;
            case R.id.id_info:
            case R.id.no_ins:
                mFirst = false;
                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(getActivity(), StaticParament.USER);
                String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
                Map<String, Object> maps = new HashMap<>();
                maps.put("userId", userId);
                maps.put("stripeState", 0);//用户区分接入stripe前后的新老用户

                jiaoyanFenqifu(maps);
//                Intent intent = new Intent(getContext(), FenQiFuOpen_Activity.class);
//                startActivity(intent);
                break;
            case R.id.id_info1:
            case R.id.add_card:
            case R.id.add_card1:
                Intent intentBank = new Intent(getContext(), NewAddBankCardActivity.class);
                intentBank.putExtra("from", "cardList");
                startActivity(intentBank);
                break;
            case R.id.card_all:
                if (transactionFragment != null) {
                    transactionFragment.changeTab(0);
                }
                all.setCardBackgroundColor(getContext().getResources().getColor(R.color.colorBackGround));
                allText.setTextColor(getContext().getResources().getColor(R.color.white));
                instalmentCard.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
                instalmentText1.setTextColor(getContext().getResources().getColor(R.color.pay_text_gray));
                bankCard.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
                cardText.setTextColor(getContext().getResources().getColor(R.color.pay_text_gray));
                break;
            case R.id.card_instalment:
                if (transactionFragment != null) {
                    transactionFragment.changeTab(1);
                }
                all.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
                allText.setTextColor(getContext().getResources().getColor(R.color.pay_text_gray));
                instalmentCard.setCardBackgroundColor(getContext().getResources().getColor(R.color.colorBackGround));
                instalmentText1.setTextColor(getContext().getResources().getColor(R.color.white));
                bankCard.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
                cardText.setTextColor(getContext().getResources().getColor(R.color.pay_text_gray));
                break;
            case R.id.card_bank:
                if (transactionFragment != null) {
                    transactionFragment.changeTab(2);
                }
                all.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
                allText.setTextColor(getContext().getResources().getColor(R.color.pay_text_gray));
                instalmentCard.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
                instalmentText1.setTextColor(getContext().getResources().getColor(R.color.pay_text_gray));
                bankCard.setCardBackgroundColor(getContext().getResources().getColor(R.color.colorBackGround));
                cardText.setTextColor(getContext().getResources().getColor(R.color.white));
                break;
            case R.id.choose_date:
                if (transactionFragment != null) {
                    transactionFragment.filter();
                }
                break;
            case R.id.bank_account:
                showPayPwdDialog();
                break;
        }
    }

    /**
     * 分期付开通状态
     *
     * @param maps
     */
    private boolean mFirst1 = true;

    private void jiaoyanFenqifu(Map<String, Object> maps) {
        RequestUtils requestUtils1 = new RequestUtils(getActivity(), maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {
                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.jiaoyanFenqifu(headerMap, requestBody);
                if (mFirst1) {
                    requestUtils.Call(call);
                } else {
                    requestUtils.Call(call, null, false);
                }
                mFirst1 = false;
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                Gson gson = new Gson();
                FenqifuStatue fenqifuStatue = gson.fromJson(dataStr, FenqifuStatue.class);
                if (mFirst) {
                    creditCardState = String.valueOf(fenqifuStatue.getCreditCardState());//0：新用户未绑卡 1：已绑卡  2：老用户未绑卡
                    cardState = String.valueOf(fenqifuStatue.getCardState());//0：未绑卡 1：已绑卡
                    installmentState = String.valueOf(fenqifuStatue.getInstallmentState());//2 分期付开通 ；！2分期付没有开通
                    if (!installmentState.equals("2")) {
                        noInsFlag = false;
                    } else {
                        noInsFlag = true;
                    }
                    //判断卡或者分期状态
                    if (!installmentState.equals("2") && cardState.equals("0")) {
                        haveNo.setVisibility(View.VISIBLE);
                        refreshLayout.setVisibility(View.GONE);
                        layout_title.setVisibility(View.VISIBLE);
                        payAllCl.setVisibility(View.GONE);
                    } else {///
                        haveNo.setVisibility(View.GONE);
                        addCard.setVisibility(View.GONE);
                        //addCard1.setVisibility(View.GONE);
                        refreshLayout.setVisibility(View.VISIBLE);
                        layout_title.setVisibility(View.GONE);
                        if (!installmentState.equals("2")) {
                            bankAccount.setVisibility(View.GONE);
                            selectTwoTab.setVisibility(View.GONE);
                            noIns.setVisibility(View.VISIBLE);
                            if (instalmentFragment != null) {
                                instalmentFragment.instalmentFlag(0);
                            }
                            haveIns.setVisibility(View.GONE);

                        } else {
                            //bankAccount.setVisibility(View.VISIBLE);
                            if (tabNum == 1) {
                                selectTwoTab.setVisibility(View.VISIBLE);
                            }
//                            if (exe){
//                                setTabSelection(1);
//                            }
                            noIns.setVisibility(View.GONE);
                            if (instalmentFragment != null) {
                                instalmentFragment.instalmentFlag(1);
                            }
                            if (creditCardState.equals("2")) {
                                //noInsCard.setVisibility(View.VISIBLE);
                                oldInstal.setVisibility(View.VISIBLE);
                                haveIns.setVisibility(View.GONE);
                            } else {
                                //noInsCard.setVisibility(View.GONE);
                                haveIns.setVisibility(View.VISIBLE);//
                                oldInstal.setVisibility(View.GONE);
                            }
                        }
                        if (cardState.equals("0")) {
                            haveCard = false;
                            addCard.setVisibility(View.VISIBLE);
                            //addCard1.setVisibility(View.VISIBLE);
                        } else if (cardState.equals("1")) {//
                            haveCard = true;
                        }
           /* if (titleFirstToTop == -1) {
                titleFirstToTop = getLocation(mTitle);
            }
            Log.d("liucl", "顶bu距离: " + titleFirstToTop);*/
                        //if (code.equals("sm")) {
                        if (installmentState.equals("2")) {
                            presentJiHuo.loadFenQiMsg(new HashMap<String, Object>());
                        }
                        //}
                        if (((MainTab) getActivity()).isFromFirst()) {
                            setTabSelection(1);
                            ((MainTab) getActivity()).setFromFirst(false);
                        }
                    }
                } else {
                    SharePreferenceUtils appMsgSharePreferenceUtils =
                            new SharePreferenceUtils(getActivity(), StaticParament.DEVICE);
                    String callPhone = (String) appMsgSharePreferenceUtils.get(StaticParament.APP_PHONE, "");

                    SelectPayTypeActivity.shifoukaitongCardPay = fenqifuStatue.getCardState();//0：未开通开支付  1:开通开卡支付
                    SelectPayTypeActivity.meikaitongfenqifuAndkaitongCardPay = fenqifuStatue.getCreditCardAgreementState();

                    FirstFragment.backStatue = 2;
                    mFirst = true;
                    if (fenqifuStatue.getKycState() == 0) {//未开通kyc
                        Intent intent_register = new Intent(getActivity(), RegistTwo_Activity.class); //跳到注册页 从注册页跳到kyc页
                        intent_register.putExtra("jumpToKyc", true);
                        startActivity(intent_register);
                    } else {//已经开通kyc
                        if (fenqifuStatue.getCardState() == 0) {//未开通卡支付
                            Intent intent_kyc = new Intent(getActivity(), NewAddBankCardActivity.class);
                            intent_kyc.putExtra("kaitongfenqifuRoad", true);
                            startActivity(intent_kyc);
                        } else {//已经开通卡支付 //判断illion状态
                            if (fenqifuStatue.getCreditCardAgreementState() == 0) {//是否勾选过分期付绑卡协议 0：未勾选 1：已勾选
                                Intent intent_kyc = new Intent(getActivity(), ChooseCardActivity.class);
                                startActivity(intent_kyc);
                            } else {
                                if (fenqifuStatue.getInstallmentState() == 0 || fenqifuStatue.getInstallmentState() == 3) {//未开通！ 0. 用户未开通分期付(需要完善信息) 3. illion未授权
                                    Intent intent_kyc = new Intent(getActivity(), ChooseIllionActivity.class);
                                    startActivity(intent_kyc);
                                } else if (fenqifuStatue.getInstallmentState() == 1 || fenqifuStatue.getInstallmentState() == 5 || fenqifuStatue.getInstallmentState() == 8) {//—-失败！—— 1. 用户分期付已冻结 5. 用户分期付禁用 8 机审拒绝
                                    Intent intent_kyc = new Intent(getActivity(), KycAndIllionResultActivity.class);
                                    intent_kyc.putExtra("error", "FkReject");
                                    intent_kyc.putExtra("phone", callPhone);
                                    startActivity(intent_kyc);
                                } else if (fenqifuStatue.getInstallmentState() == 4 || fenqifuStatue.getInstallmentState() == 7 || fenqifuStatue.getInstallmentState() == 9) {//—-等待！——4. 等待人工审核 7. 机审中 9 分期付风控未触发
                                    Intent intent_kyc = new Intent(getActivity(), KycAndIllionResultActivity.class);
                                    intent_kyc.putExtra("error", "Waiting");
                                    intent_kyc.putExtra("phone", callPhone);
                                    startActivity(intent_kyc);
                                }
                            }
                        }
                    }
                }
                exe = false;
                Log.d("xunzhic", "resData: " + dataStr);
            }

            @Override
            public void resError(String error) {
                exe = false;
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        ((MainTab) getActivity()).unregisterMyOnTouchListener(myOnTouchListener);
    }

    private boolean mFirstTime = true;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            setTabSelection(1);

            //调完接口，根据老用户绑卡状态展示
            /*if (((MainTab)getActivity()).isFromFirst()) {
                setTabSelection(1);
                ((MainTab)getActivity()).setFromFirst(false);
            }*/

            String payFlag = (String) sharePreferenceUtils.get(StaticParament.PAY_SUCCESS, "");
            if (payFlag.equals("1") || mFirstTime) {
                SharePreferenceUtils sharePreferenceUtils1 = new SharePreferenceUtils(getActivity(), StaticParament.USER);
                String userId1 = (String) sharePreferenceUtils1.get(StaticParament.USER_ID, "");
                Map<String, Object> maps1 = new HashMap<>();
                maps1.put("userId", userId1);
                maps1.put("stripeState", 0);//用户区分接入stripe前后的新老用户
                jiaoyanFenqifu(maps1);

                if (transactionFragment != null) {
                    transactionFragment.refresh();
                }
                if (instalmentFragment != null) {
                    instalmentFragment.refresh();
                }
                sharePreferenceUtils.put(StaticParament.PAY_SUCCESS, "0").commit();
            }
//            if (repayFlag.equals("1")) {
//                if (instalmentFragment != null) {
//                    instalmentFragment.refresh();
//                }
//                sharePreferenceUtils.put(StaticParament.REPAY_SUCCESS, "0");
//            }
//            sharePreferenceUtils.save();
            mFirstTime = false;
        } else {
        }
    }

    @Override
    public void setFenQiStatus(String status) {

    }

    @Override
    public void setFenQiMsg(FenQiMsgDatas fenQiMsg) {
        String canUse = "";
        String allMoney = "";
        String haveUse = "";
        if (fenQiMsg.getAvailableCredit() != null) {
            canUse = fenQiMsg.getAvailableCredit();
        }
        if (fenQiMsg.getCreditAmount() != null) {
            allMoney = fenQiMsg.getCreditAmount();
        }
        if (fenQiMsg.getAmount() != null) {
            haveUse = fenQiMsg.getAmount();/*PublicTools.twoend(CalcTool.sub(Double.parseDouble(allMoney), Double.parseDouble(canUse)));*/
        }
        idCanUse.setText("$" + PublicTools.twoend(canUse));
        idHaveUse.setText("$" + PublicTools.twoend(haveUse));
        idAllMoney.setText("$" + PublicTools.twoend(allMoney));

        if (fenQiMsg.getOverdueAmount() != null) {
            String overDueMoney = fenQiMsg.getOverdueAmount();
            if (Float.parseFloat(overDueMoney) > 0) {
                //overdueLl.setVisibility(View.VISIBLE);
                //instalmentText.setTag("1");
                isOverDue = true;
                if (tabNum == 0) {
                    newOverdueLl.setVisibility(View.VISIBLE);
                    instalment.setText("");
                } else {
                    newOverdueLl.setVisibility(View.GONE);
                    instalment.setText("Instalment");
                }
            } else {
                //overdueLl.setVisibility(View.GONE);
                isOverDue = false;
                newOverdueLl.setVisibility(View.GONE);
                instalment.setText("Instalment");
            }
        }
        if (fenQiMsg.getBankcardNumber() != null) {
            card = "****" + fenQiMsg.getBankcardNumber().
                    substring(fenQiMsg.getBankcardNumber().length() - 4, fenQiMsg.getBankcardNumber().length());
            sharePreferenceUtils.put(StaticParament.USER_FQ_ACCOUNT, card);
            sharePreferenceUtils.save();
            if (confirmRepayDialog != null) {
                confirmRepayDialog.changeNum(card);
            }
        }
        accountId = fenQiMsg.getAccountId();
        sharePreferenceUtils.put(StaticParament.USER_FQ_ACCOUNT, fenQiMsg.getBankcardNumber());
        sharePreferenceUtils.save();
    }

    /**
     * 弹出输入支付（PIN）密码弹窗
     */
    private void showPayPwdDialog() {
        RepayPawDialog repayPawDialog = new RepayPawDialog(getContext());
        repayPawDialog.setOnRepayPwd(new RepayPawDialog.OnRepayPwd() {
            @Override
            public void inputPwd(String password) {
                Map<String, Object> maps = new HashMap<>();
                maps.put("payPassword", password);
                checkPayPassword_p.checkPayPassword(maps);
            }
        });
        repayPawDialog.show();
    }

    @Override
    public void setFenQiLimit(String limit) {

    }

    @Override
    public void setNoLoginFenQiLimit(String limit) {

    }

    private void initPopWindow(View v) {
        /*// 设置背景颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;//调节透明度
        getWindow().setAttributes(lp);*/
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.popup_sort_instalment, null, false);
        TextView topText = (TextView) view1.findViewById(R.id.top_text);
        TextView byDate = (TextView) view1.findViewById(R.id.by_date);
        TextView byOrder = (TextView) view1.findViewById(R.id.by_order);
        TypefaceUtil.replaceFont(topText, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(byDate, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(byOrder, "fonts/gilroy_medium.ttf");
        if (selectSort.getTag().toString().equals("1")) {
            topText.setText("Sort by due date");
            byDate.setVisibility(View.GONE);
        } else {
            topText.setText("Sort by order");
            byOrder.setVisibility(View.GONE);
        }
        choiceIcon.setImageResource(R.mipmap.choice_gray_up);
        selectSort.setTextColor(getResources().getColor(R.color.pay_text_gray));
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view1,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

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
                /*WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);*/
                choiceIcon.setImageResource(R.mipmap.choose_black_icon);
                selectSort.setTextColor(Color.BLACK);
            }
        });

        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, -PublicTools.dip2px(getContext(), 3), 0);

        //设置popupWindow里的按钮的事件
        byDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectSort.getTag().toString().equals("1")) {
                    selectSort.setText("Sort by due date");
                    selectSort.setTag("1");
                    if (instalmentFragment != null) {
                        //instalmentFragment.sortByDateList();
                    }
                    refreshLayout.setEnableLoadMore(false);
                }
                popWindow.dismiss();

            }
        });
        byOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectSort.getTag().toString().equals("2")) {
                    selectSort.setText("Sort by order");
                    selectSort.setTag("2");
                    if (instalmentFragment != null) {
                        //instalmentFragment.sortByOrder();
                    }
                    refreshLayout.setEnableLoadMore(true);
                }
                popWindow.dismiss();
            }
        });
    }

    public static String accountId;

    @Override
    public void setPasswordStatus(String status) {
        if (status == null) {
            return;
        }
        Intent intent1 = new Intent(getContext(), FenQuBanks_Activity.class);
        intent1.putExtra("accountId", accountId);
        startActivity(intent1);
    }

}
