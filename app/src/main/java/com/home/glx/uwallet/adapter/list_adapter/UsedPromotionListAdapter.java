package com.home.glx.uwallet.adapter.list_adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.PromotionDatas;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.List;

public class UsedPromotionListAdapter extends RecyclerView.Adapter {
    private List<PromotionDatas.Promotion> data;
    private Context context;
    private LayoutInflater inflater;

    public UsedPromotionListAdapter(Context context, List<PromotionDatas.Promotion> data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void notifyDataChanged(List<PromotionDatas.Promotion> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BottomView(inflater.inflate(R.layout.promotion_list_item_used, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BottomView) holder).bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class BottomView extends RecyclerView.ViewHolder {
        private TextView jine;
        private TextView date;
        private TextView date2;
        private TextView status;


        public BottomView(@NonNull View itemView) {
            super(itemView);
        }

        public void bindData(final PromotionDatas.Promotion promotion) {
            jine = (TextView) itemView.findViewById(R.id.jine);
            jine.setText("$" + promotion.getAmount());
            date = itemView.findViewById(R.id.date);
            date2 = itemView.findViewById(R.id.date2);
            date2.setVisibility(View.GONE);
            jine.setTextColor(ContextCompat.getColor(context, R.color.color_717171));
            date.setTextColor(ContextCompat.getColor(context, R.color.color_717171));
            status = (TextView) itemView.findViewById(R.id.status);
            status.setTextColor(ContextCompat.getColor(context, R.color.color_717171));
            status.setVisibility(View.VISIBLE);
            date.setSingleLine();
            date.setEllipsize(TextUtils.TruncateAt.END);
            if (promotion.getShowState() == 2) {//app展示状态 1可用 2 已使用 3 过期 4 终止
                status.setText("Used");
                date.setText(promotion.getDescription() + (TextUtils.isEmpty(promotion.getDescription()) ? "" : " • ") + promotion.getUsedTimeStr() + "," + promotion.getUsedAtMerchantName());
            } else if (promotion.getShowState() == 3) {
                status.setText("Expired");
                date.setText(promotion.getDescription() + (TextUtils.isEmpty(promotion.getDescription()) ? "" : " • ") + "Expired on " + promotion.getExpiredTimeStr());
            } else if (promotion.getShowState() == 4) {
                status.setText("Ended");
                date.setText(promotion.getDescription() + (TextUtils.isEmpty(promotion.getDescription()) ? "" : " • ") + "Ended on " + promotion.getTerminatedTimeStr());
            } else if (promotion.getShowState() == 5) {//邀请码是5
                status.setText("Unused");
                date.setText((promotion.getValidityLimitState() == 0 ? "" : "Expires on " + promotion.getExpiredTimeStr() + " • ")
                        + (promotion.getAmountLimitState() == 0 ? "" : "Min. order $" + promotion.getMinTransAmount() + (TextUtils.isEmpty(promotion.getDescription()) ? "" : " • "))
                        + promotion.getDescription());
            }
            date.setTextColor(ContextCompat.getColor(context, R.color.color_717171));
            TypefaceUtil.replaceFont(jine, "fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(date, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(status, "fonts/gilroy_medium.ttf");
        }
    }
}
