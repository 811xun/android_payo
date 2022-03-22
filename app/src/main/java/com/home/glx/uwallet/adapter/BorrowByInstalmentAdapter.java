package com.home.glx.uwallet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.BorrowByInstalmentData;
import com.home.glx.uwallet.datas.BorrowByPayDayV2Data;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

public class BorrowByInstalmentAdapter extends RecyclerView.Adapter  {
    private Context context;
    private LayoutInflater inflater;
    private BorrowByPayDayV2Data data;

    public BorrowByInstalmentAdapter(Context context, BorrowByPayDayV2Data data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemView(inflater.inflate(R.layout.item_borrow_by_instalment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemView) holder).binData(position, data.getList().get(position));
    }

    @Override
    public int getItemCount() {
        return data.getList().size();
    }

    class ItemView extends RecyclerView.ViewHolder {
        public CardView info;
        public ImageView logo;
        public ImageView overItem;
        public TextView money;
        public TextView merchantName;
        private TextView endTv;
        private TextView overDueFlag;
        private TextView overDueText;

        public ItemView(@NonNull View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.id_info);
            logo = itemView.findViewById(R.id.id_item_img);
            overItem = itemView.findViewById(R.id.id_over_due_img);
            money = (TextView) itemView.findViewById(R.id.money);
            merchantName = (TextView) itemView.findViewById(R.id.merchant_name);
            endTv = itemView.findViewById(R.id.end_text);
            overDueFlag = itemView.findViewById(R.id.over_due_flag);
            overDueText = itemView.findViewById(R.id.over_due_text);
        }

        public void binData(int position, final BorrowByPayDayV2Data.ListBean bean) {

            if (bean != null) {
                //切换字体|
                TypefaceUtil.replaceFont(merchantName,"fonts/gilroy_medium.ttf");
                TypefaceUtil.replaceFont(money,"fonts/gilroy_semiBold.ttf");
                TypefaceUtil.replaceFont(overDueText,"fonts/gilroy_semiBold.ttf");
                if (position != 0 && position == data.getList().size() - 1) {
                    endTv.setVisibility(View.VISIBLE);
                } else {
                    endTv.setVisibility(View.GONE);
                }
                if (position == data.getList().size() - 1 && bean.getType().equals("2")) {
                    overDueFlag.setVisibility(View.GONE);
                    overItem.setVisibility(View.VISIBLE);
                    logo.setVisibility(View.GONE);
                    merchantName.setText("Late fee");
                    info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onitemClick != null) {
                                onitemClick.onClickLateFee(bean.getOverdueFeeId(), bean.getRepayAmount());
                            }
                        }
                    });
                    money.setText("$" + PublicTools.twoend(bean.getRepayAmount()));
                } else {
                    logo.setVisibility(View.VISIBLE);
                    overItem.setVisibility(View.GONE);
                    if (bean.getHaveOverDue().equals("true")) {
                        //overDueFlag.setAlpha(0.95f);
                        overDueFlag.setVisibility(View.VISIBLE);
                        overDueText.setVisibility(View.VISIBLE);
                    } else {
                        overDueFlag.setVisibility(View.GONE);
                        overDueText.setVisibility(View.GONE);
                    }
                    money.setText("$" + PublicTools.twoend(bean.getRepayAmount()));
                    merchantName.setText(bean.getTradingName());
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.mipmap.loading_img)  //加载成功之前占位图
                            .error(R.mipmap.loading_img)    //加载错误之后的错误图
/*                    .optionalTransform(new RoundedCorners(PublicTools.dip2px(context, 18)))
                    .override(400,400)  //指定图片的尺寸
                    //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
                    .fitCenter()
                    //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
                    .centerCrop()
                    .circleCrop()//指定图片的缩放类型为centerCrop （圆形）*/
                            .skipMemoryCache(true)  //跳过内存缓存
                            .diskCacheStrategy(DiskCacheStrategy.ALL)   //缓存所有版本的图像
                            .diskCacheStrategy(DiskCacheStrategy.NONE)  //跳过磁盘缓存
                            .diskCacheStrategy(DiskCacheStrategy.DATA)  //只缓存原来分辨率的图片
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)  //只缓存最终的图片
                            ;
                    Glide.with(context)
                            .load(StaticParament.ImgUrl + bean.getLogoUrl())
                            .apply(/*RequestOptions.bitmapTransform(new RoundedCorners(PublicTools.dip2px(context, 18)))*/options)//圆角半径
                            .into(logo);
                    info.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onitemClick != null) {
                                onitemClick.onClick(bean.getBorrowId(), bean.getRepayId());
                            }
                        }
                    });
                }
            }
        }
    }

    public interface OnitemClick {
        void onClick(String borrowId, String repayId);
        void onClickLateFee(String overdueFeeId, String amount);
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }
}
