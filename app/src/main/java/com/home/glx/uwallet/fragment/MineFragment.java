package com.home.glx.uwallet.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.AgreementActivity;
import com.home.glx.uwallet.activity.BankCardListActivity;
import com.home.glx.uwallet.activity.FavoriteListActivity;
import com.home.glx.uwallet.activity.MessageActivity;
import com.home.glx.uwallet.activity.Setting_Activity;
import com.home.glx.uwallet.activity.ShareToEarnActivity;
import com.home.glx.uwallet.activity.UserMsgActivity;
import com.home.glx.uwallet.activity.xzc.LoginActivity_inputNumber;
import com.home.glx.uwallet.activity.xzc.promotion.PromotionListActivity;
import com.home.glx.uwallet.datas.UserAllMsgDatas;
import com.home.glx.uwallet.mvp.GetUserAllMsg_in;
import com.home.glx.uwallet.mvp.GetUserAllMsg_p;
import com.home.glx.uwallet.mvp.LoadNotReadMessage;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.UserListener;
import com.home.glx.uwallet.mvp.model.UserModel;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;
import java.util.Map;

public class MineFragment extends FirstTabFragment implements GetUserAllMsg_in.View, View.OnClickListener {
    private UserListener userListener;
    private TextView savedMoney;
    private ImageView messageImg;
    private GetUserAllMsg_p present;
    private TextView name;
    private ImageView defaultSet;
    private RelativeLayout cardLl;
    private RelativeLayout shareLl;
    private RelativeLayout moneyLl;
    private RelativeLayout favoriteLl;
    private RelativeLayout agreement;
    private ImageView settingImg;
    private TextView payoSaved;
    private TextView inthe;
    private TextView cards;
    private TextView shares;
    private TextView favorites;
    private TextView moneys;
//xzc 租金半年省的钱 （View id:have_save）隐藏了  10.27
    @Override
    public int getLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView() {
        super.initView();
        userListener = new UserModel(getContext());
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        int width = metric.widthPixels;
        present = new GetUserAllMsg_p(getContext(), this);
        favoriteLl = (RelativeLayout) view.findViewById(R.id.favorite_ll);
        savedMoney = (TextView) view.findViewById(R.id.saved_money);
        inthe = view.findViewById(R.id.in_the);
        defaultSet = (ImageView) view.findViewById(R.id.set_default);
        moneyLl = (RelativeLayout) view.findViewById(R.id.money_ll);
        payoSaved = view.findViewById(R.id.payo_saved);
        name = (TextView) view.findViewById(R.id.name);
        /*int nameWidth = width - PublicTools.dip2px(getContext(), 150);
        name.setMaxWidth(nameWidth);*/
        shareLl = (RelativeLayout) view.findViewById(R.id.share_ll);
        messageImg = (ImageView) view.findViewById(R.id.messageView);
        cardLl = (RelativeLayout) view.findViewById(R.id.card_ll);
        settingImg = (ImageView) view.findViewById(R.id.setting);
        agreement = (RelativeLayout) view.findViewById(R.id.agreement);
        cards = view.findViewById(R.id.cards);
        shares = view.findViewById(R.id.shares);
        favorites = view.findViewById(R.id.favorites);
        moneys = view.findViewById(R.id.moneys);
        //切换字体
        TypefaceUtil.replaceFont(agreement, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(payoSaved, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(savedMoney, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(inthe, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(name, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(cards, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(shares, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(favorites, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(moneys, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(view.findViewById(R.id.tv_agree), "fonts/gilroy_semiBold.ttf");

        defaultSet.setOnClickListener(this);
        favoriteLl.setOnClickListener(this);
        name.setOnClickListener(this);
        shareLl.setOnClickListener(this);
        messageImg.setOnClickListener(this);
        cardLl.setOnClickListener(this);
        moneyLl.setOnClickListener(this);
        agreement.setOnClickListener(this);
        settingImg.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
//            getSavedMoney();
            //获取用户信息
            present.loadUserAllMsg(new HashMap<String, Object>());
            loadNotReadMessageCount();
        }
    }

    /**
     * 获取未读信息数量
     */
    private void loadNotReadMessageCount() {
        LoadNotReadMessage loadNotReadMessage = new LoadNotReadMessage(getContext());
        loadNotReadMessage.setOnNotRead(new LoadNotReadMessage.OnNotRead() {
            @Override
            public void notReadCount(int num) {
                if (num == 0) {
                    messageImg.setImageResource(R.mipmap.no_message);
                } else {
                    messageImg.setImageResource(R.mipmap.have_message);
                }
            }
        });
        loadNotReadMessage.getAllNoticeHasRead();
    }

    private void getSavedMoney() {
        Map<String, Object> map = new HashMap<>();
        userListener.getUserSavedAmount(map, new ActionCallBack() {
            @Override
            public void onSuccess(Object... o) {
                String money = (String) o[0];
                savedMoney.setText("$" + PublicTools.twoend(money));
            }

            @Override
            public void onError(String e) {

            }
        });
    }

    public static MineFragment newInstance() {
        Bundle args = new Bundle();
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserMsg(UserAllMsgDatas userMsg) {
        if (userMsg == null) {
            return;
        }
        String firstName = "";
        String middleName = "";
        String lastName = "";
        if (userMsg.getUserFirstName() != null) {
            firstName = userMsg.getUserFirstName();
        }
        if (userMsg.getUserMiddleName() != null) {
            middleName = userMsg.getUserMiddleName();
        }
        if (userMsg.getUserLastName() != null) {
            lastName = userMsg.getUserLastName();
        }
        name.setText(firstName + " " + lastName);
    }

    @Override
    public void onClick(View v) {
        if (!PublicTools.isFastClick()) {
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.set_default:
            case R.id.name:
                Intent intent = new Intent(getContext(), UserMsgActivity.class);
                startActivity(intent);
                break;
            case R.id.card_ll:
                Intent intentCard = new Intent(getContext(), BankCardListActivity.class);
                startActivity(intentCard);
                break;
            case R.id.setting:
                PublicTools.setLastClickTime();
                Intent intentMsg = new Intent(getContext(), Setting_Activity.class);
                startActivityForResult(intentMsg, 202);
                break;
            case R.id.share_ll:
                Intent intentShare = new Intent(getContext(), ShareToEarnActivity.class);
                startActivity(intentShare);
                break;
            case R.id.money_ll:
                Intent intentCode = new Intent(getContext(), PromotionListActivity.class);
                startActivity(intentCode);
//                Intent intentCode = new Intent(getContext(), PromotionCodeActivity.class);
//                startActivity(intentCode);
                break;
            case R.id.favorite_ll:
                Intent intentFavorite = new Intent(getContext(), FavoriteListActivity.class);
                startActivity(intentFavorite);
                break;
            case R.id.messageView:
                Intent intent_msg = new Intent(getContext(), MessageActivity.class);
                startActivity(intent_msg);
                break;
            case R.id.agreement:
                Intent intentAgreement = new Intent(getContext(), AgreementActivity.class);
                startActivity(intentAgreement);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 202 && resultCode == Activity.RESULT_OK) {
//            Intent intent = new Intent(getContext(), Login_Activity.class);
            Intent intent = new Intent(getContext(), LoginActivity_inputNumber.class);
            startActivity(intent);
            ((Activity) getContext()).finish();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onResume();
        }
    }
}
