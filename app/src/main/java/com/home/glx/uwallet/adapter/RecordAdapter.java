package com.home.glx.uwallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.RecordData;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter  {
    private Context context;
    private LayoutInflater inflater;
    private List<RecordData> data;

    public RecordAdapter(Context context, List<RecordData> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }
    public interface OnitemClick {
        void itemClick(String payAmount, String transType, String id);
        void itemClickLateFee(String id);
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemView(inflater.inflate(R.layout.item_record_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemView) holder).binData(position, data.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void notifyDataSetChang(List<RecordData> listData) {
        this.data = listData;
        notifyDataSetChanged();
    }

    class ItemView extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView merchantName;
        public TextView patType;
        public TextView money;
        private ConstraintLayout item;

        public ItemView(@NonNull View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            merchantName = (TextView) itemView.findViewById(R.id.merchant_name);
            patType = (TextView) itemView.findViewById(R.id.patType);
            money = (TextView) itemView.findViewById(R.id.money);
            item = (ConstraintLayout) itemView.findViewById(R.id.item);
            //切换字体
            TypefaceUtil.replaceFont(date,"fonts/gilroy_bold.ttf");
            TypefaceUtil.replaceFont(merchantName,"fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(patType,"fonts/gilroy_regular.ttf");
            TypefaceUtil.replaceFont(money,"fonts/gilroy_medium.ttf");
        }

        public void binData(final int position, final RecordData listBean) {

            String dateStr = data.get(position).getDisplayDate();
            String month = dateStr.substring(0, dateStr.length() - 5);
            String year = dateStr.substring(dateStr.length() - 4);
            date.setText(month + "\n" + year);
            merchantName.setText(data.get(position).getTradingName());
            if (data.get(position).getTransType().equals("2")) {
                patType.setText("Card payment");
            } else if (data.get(position).getTransType().equals("22")){
                patType.setText("Instalment payment");
            } else {
                patType.setText("Card payment");
                merchantName.setText("Late fee");
            }

            money.setText("$" + PublicTools.twoend(data.get(position).getTotalAmount()));
            if (data.get(position).getTransType().equals("33")) {
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onitemClick.itemClickLateFee(data.get(position).getId());
                    }
                });
            } else {
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onitemClick.itemClick(data.get(position).getPayAmount(), data.get(position).getTransType(), data.get(position).getId());
                    }
                });
            }

        }
    }
}
