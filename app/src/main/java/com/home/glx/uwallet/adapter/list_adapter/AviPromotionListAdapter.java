package com.home.glx.uwallet.adapter.list_adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.PromotionDatas;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.List;

public class AviPromotionListAdapter extends RecyclerView.Adapter {
    private List<PromotionDatas.Promotion> data;
    private Context context;
    private LayoutInflater inflater;

    public AviPromotionListAdapter(Context context, List<PromotionDatas.Promotion> data) {
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
        return new BottomView(inflater.inflate(R.layout.promotion_list_item, parent, false));
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

        public BottomView(@NonNull View itemView) {
            super(itemView);
        }

        public void bindData(final PromotionDatas.Promotion promotion) {
            jine = (TextView) itemView.findViewById(R.id.jine);
            jine.setText("$" + promotion.getAmount());
            date = itemView.findViewById(R.id.date);
            date2 = itemView.findViewById(R.id.date2);
            TypefaceUtil.replaceFont(date, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(date2, "fonts/gilroy_medium.ttf");
            date2.setText(
                    (promotion.getValidityLimitState() == 0 ? "" : "Expires on " + promotion.getExpiredTimeStr() + " • ")
//                            + (promotion.getAmountLimitState() == 0 ? "" : "Min. order $" + promotion.getMinTransAmount().split("\\.")[0] + " • ")
                            + (promotion.getAmountLimitState() == 0 ? "" : "Min. order $" + promotion.getMinTransAmount() + (TextUtils.isEmpty(promotion.getDescription()) ? "" : " • "))
                            + promotion.getDescription());

            date2.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    //这个回调会调用多次，获取完行数记得注销监听
                    date2.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (date2.getLineCount() > 1) {
                        date.setText(
                                (promotion.getValidityLimitState() == 0 ? "" : "Expires on " + promotion.getExpiredTimeStr() + " • ")
                                        + (promotion.getAmountLimitState() == 0 ? "" : "Min. order $" + promotion.getMinTransAmount() + (TextUtils.isEmpty(promotion.getDescription()) ? "" : "\n• "))
                                        + promotion.getDescription());
                    } else {
                        date.setText(
                                (promotion.getValidityLimitState() == 0 ? "" : "Expires on " + promotion.getExpiredTimeStr() + " • ")
                                        + (promotion.getAmountLimitState() == 0 ? "" : "Min. order $" + promotion.getMinTransAmount() + (TextUtils.isEmpty(promotion.getDescription()) ? "" : " • "))
                                        + promotion.getDescription());
                    }
                    date2.setVisibility(View.GONE);
                    return false;
                }
            });

            TypefaceUtil.replaceFont(jine, "fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(date, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(date2, "fonts/gilroy_medium.ttf");
        }
    }

}
