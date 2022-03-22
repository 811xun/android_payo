package com.home.glx.uwallet.adapter.list_adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.PromotionDatas;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.List;

public class AviPromotionListAdapter_zhifu extends RecyclerView.Adapter {
    private List<PromotionDatas.Promotion> data;
    private Context context;
    private LayoutInflater inflater;
    private String couponId = "";

    public AviPromotionListAdapter_zhifu(Context context, List<PromotionDatas.Promotion> data, String couponSelectedId) {
        this.data = data;
        this.context = context;
        this.couponId = couponSelectedId;

        inflater = LayoutInflater.from(context);
    }

    public void notifyDataChanged(List<PromotionDatas.Promotion> data, String couponSelectedId) {
        this.data = data;
        this.couponId = couponSelectedId;

        notifyDataSetChanged();
    }

    public void initSelectIndex(String couponId) {
        this.couponId = couponId;
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
        private TextView status;
        private RelativeLayout mRelativeLayout;
        private TextView date2;

        public BottomView(@NonNull View itemView) {
            super(itemView);
        }

        public void bindData(final PromotionDatas.Promotion promotion) {
            mRelativeLayout = itemView.findViewById(R.id.rl);
            jine = (TextView) itemView.findViewById(R.id.jine);
            status = (TextView) itemView.findViewById(R.id.status);
            jine.setText("$" + promotion.getAmount());
            date = itemView.findViewById(R.id.date);
            date2 = itemView.findViewById(R.id.date2);


            TypefaceUtil.replaceFont(jine, "fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(date, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(status, "fonts/gilroy_bold.ttf");
            if (promotion.getPayUseState() == 1) {
                date.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/gilroy_medium.ttf"));
                date2.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/gilroy_medium.ttf"));
                date2.setText(
                        (promotion.getValidityLimitState() == 0 ? "" : "Expires on " + promotion.getExpiredTimeStr() + " • ")
                                + (promotion.getAmountLimitState() == 0 ? "" : "Min. order $" + promotion.getMinTransAmount() + (TextUtils.isEmpty(promotion.getDescription()) ? "" : " • "))
                                + promotion.getDescription());
                date2.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        //这个回调会调用多次，获取完行数记得注销监听
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

//                        date2.setVisibility(View.GONE);
                        date.setVisibility(View.VISIBLE);
//                        TypefaceUtil.replaceFont(date, "fonts/gilroy_medium.ttf");
//                        TypefaceUtil.replaceFont(date2, "fonts/gilroy_medium.ttf");
                        date.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/gilroy_medium.ttf"));
//                        date2.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/gilroy_medium.ttf"));
                        date2.getViewTreeObserver().removeOnPreDrawListener(this);
                        return false;
                    }
                });

                mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (promotion.getCouponId().equals(couponId)) {//如果点击是以前选择的
                            initSelectIndex("");
                            onitemClick.itemClick(null);
                        } else {
                            onitemClick.itemClick(promotion);
                            initSelectIndex(promotion.getCouponId());
                        }
                        notifyDataSetChanged();
                    }
                });
                TypefaceUtil.replaceFont(date, "fonts/gilroy_medium.ttf");
                date.setTextColor(ContextCompat.getColor(context, R.color.black));
                jine.setTextColor(ContextCompat.getColor(context, R.color.black));
            } else {
                date.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/gilroy_medium.ttf"));
//                date2.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/gilroy_medium.ttf"));
                date.setText(
                        (promotion.getValidityLimitState() == 0 ? "" : "Expires on " + promotion.getExpiredTimeStr() + " • ")
                                + (promotion.getAmountLimitState() == 0 ? "" : "Min. order $" + promotion.getMinTransAmount() + (TextUtils.isEmpty(promotion.getDescription()) ? "" : " • "))
                                + promotion.getDescription());
                date.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        //这个回调会调用多次，获取完行数记得注销监听
                        date.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/gilroy_medium.ttf"));
                        date2.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/gilroy_medium.ttf"));
                        if (date.getLineCount() > 1) {
                            date2.setText(
                                    (promotion.getValidityLimitState() == 0 ? "" : "Expires on " + promotion.getExpiredTimeStr() + " • ")
                                            + (promotion.getAmountLimitState() == 0 ? "" : "Min. order $" + promotion.getMinTransAmount() + (TextUtils.isEmpty(promotion.getDescription()) ? "" : "\n• "))
                                            + promotion.getDescription());
                        } else {
                            date2.setText(
                                    (promotion.getValidityLimitState() == 0 ? "" : "Expires on " + promotion.getExpiredTimeStr() + " • ")
                                            + (promotion.getAmountLimitState() == 0 ? "" : "Min. order $" + promotion.getMinTransAmount() + (TextUtils.isEmpty(promotion.getDescription()) ? "" : " • "))
                                            + promotion.getDescription());
                        }
                        date.setVisibility(View.GONE);
                        date2.setVisibility(View.VISIBLE);
                        date.getViewTreeObserver().removeOnPreDrawListener(this);
                        return false;
                    }
                });

                TypefaceUtil.replaceFont(date, "fonts/gilroy_medium.ttf");
                date.setTextColor(ContextCompat.getColor(context, R.color.color_717171));

                jine.setTextColor(ContextCompat.getColor(context, R.color.color_717171));
            }

            if (promotion.getCouponId().equals(couponId)) {
                mRelativeLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.bg2_orange));
                status.setVisibility(View.VISIBLE);
                status.setText("Applied");
                status.setTextColor(ContextCompat.getColor(context, R.color.colorBackGround));
            } else {
                status.setVisibility(View.GONE);
                mRelativeLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.bg3));
            }
        }
    }

    public interface OnitemClick {
        void itemClick(PromotionDatas.Promotion bankDatas);
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }

}
