package com.home.glx.uwallet.selfview;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.DialogBanksAdapter;
import com.home.glx.uwallet.datas.BankDatas;
import com.home.glx.uwallet.mvp.GetBankList_in;
import com.home.glx.uwallet.mvp.GetBankList_p;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择银行卡
 */
public class ChoiceBankDialog implements GetBankList_in.View {

    private Context context;
    private MyDialog dialog;

    private ImageView idClose;
    private TextView idTitle;
    private RecyclerView idRecyclerview;
    private DialogBanksAdapter adapter;
    private DialogBanksAdapter.OnItemClick onItemClick;
    private String cardType;
    private GetBankList_p present;
    private LinearLayout.LayoutParams params;

    private int itemStyle = 0;
    private int look_type = 0;

    public ChoiceBankDialog(Context context, DialogBanksAdapter.OnItemClick onItemClick) {
        this.context = context;
        this.onItemClick = onItemClick;
        present = new GetBankList_p(context, this);
    }

    public void setItemStyle(int style) {
        itemStyle = style;
    }

    public void setLookType(int type) {
        this.look_type = type;
    }

    public void show(String title, String cardType) {
        show(title, null, cardType);
    }

    public void show(String title, List<BankDatas> list, String cardType) {
        this.cardType = cardType;
        dialog = new MyDialog(context, R.style.MyDialog, R.layout.dialog_choicebank);
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        idClose = (ImageView) dialog.findViewById(R.id.id_close);
        idClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        idTitle = (TextView) dialog.findViewById(R.id.id_title);
        //切换字体
        TypefaceUtil.replaceFont(idTitle,"fonts/gilroy_semiBold.ttf");
        idTitle.setText(title);
        idRecyclerview = (RecyclerView) dialog.findViewById(R.id.id_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        idRecyclerview.setLayoutManager(linearLayoutManager);

        if (list != null) {
            setBankList(list);
        } else {
            Map<String, Object> maps = new HashMap<>();
            //0:账户  1:卡支付
            maps.put("cardType", cardType);
            present.setLoadFinances(false);
            present.loadBankList(maps);
        }
    }

    public void dismiss() {
        dialog.dismiss();
    }

    @Override
    public void setBankList(List<BankDatas> list) {
        if (list == null) {
            return;
        }

        if (list.size() > 6) {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, PublicTools.dip2px(context, 300));
            idRecyclerview.setLayoutParams(params);
        }

        adapter = new DialogBanksAdapter(context, list, cardType);
        adapter.setStyle(itemStyle);
        if (look_type != 0) {
            adapter.setLookType(look_type);
        }
        adapter.setOnItemClick(onItemClick);
        idRecyclerview.setAdapter(adapter);
    }

}
