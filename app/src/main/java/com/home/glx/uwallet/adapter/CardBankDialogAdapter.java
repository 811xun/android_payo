package com.home.glx.uwallet.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.BankDatas;

import java.util.List;

public class CardBankDialogAdapter extends BaseAdapter {
    private List<BankDatas> data;
    private Context context;
    private String cardId;

    public CardBankDialogAdapter(Context context, List<BankDatas> data, String cardId) {
        this.data = data;
        this.context = context;
        this.cardId = cardId;
    }

    public void notifyData(String cardId) {
        this.cardId = cardId;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_bank_card_dialog, parent, false);
        LinearLayout card = (LinearLayout) convertView.findViewById(R.id.card_ll);
        TextView name = (TextView) convertView.findViewById(R.id.bank_name);
        TextView num = (TextView) convertView.findViewById(R.id.card_num);
        ImageView select = (ImageView) convertView.findViewById(R.id.select_icon);
        if (cardId.equals(data.get(position).getId())) {
            card.setBackgroundColor(context.getResources().getColor(R.color.pink));
            select.setVisibility(View.VISIBLE);
        } else {
            card.setBackgroundColor(context.getResources().getColor(R.color.white));
            select.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(data.get(position).getCardNo())) {
            num.setText(data.get(position).getCardNo());
            name.setText(data.get(position).getBankName());
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onitemClick.itemClick(data.get(position).getCardNo(),data.get(position).getId(), data.get(position).getCustomerCcType() );
                }
            });
        }
        return convertView;
    }

    public interface OnitemClick {
        void itemClick(String cardNo, String id, String type);
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }
}

