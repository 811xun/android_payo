package com.home.glx.uwallet.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.ViewMoreActivity;
import com.home.glx.uwallet.datas.HomeMerchantListData;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeMarketAdapter extends RecyclerView.Adapter {
    private Context context;
    private LayoutInflater inflater;
    private List<HomeMerchantListData.ExclusiveBean.ListBean> listData = new ArrayList<>();

    public HomeMarketAdapter(Context context, List<HomeMerchantListData.ExclusiveBean.ListBean> listData) {
        this.context = context;
        this.listData = listData;
        inflater = LayoutInflater.from(context);
    }


    public void notifyData(List<HomeMerchantListData.ExclusiveBean.ListBean> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context instanceof ViewMoreActivity) {
            return new ItemView(inflater.inflate(R.layout.item_view_market_list, parent, false));
        } else {
            return new ItemView(inflater.inflate(R.layout.item_home_market_list, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemView) holder).binData(position, listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ItemView extends RecyclerView.ViewHolder {
        public CardView info;
        public TextView merchantName;
        public TextView tags;
        public ImageView imageView;
        private TextView last;
        private TextView first;

        public ItemView(@NonNull View itemView) {
            super(itemView);
            info = (CardView) itemView.findViewById(R.id.id_info);
            merchantName = (TextView) itemView.findViewById(R.id.id_merchant_name);
            tags = (TextView) itemView.findViewById(R.id.id_popular_list);
            imageView = (ImageView) itemView.findViewById(R.id.id_item_img);
            last = (TextView) itemView.findViewById(R.id.last_item);
            first = (TextView) itemView.findViewById(R.id.first_item);
        }

        public void binData(int position, final HomeMerchantListData.ExclusiveBean.ListBean listBean) {
            //????????????
            TypefaceUtil.replaceFont(merchantName,"fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(tags,"fonts/gilroy_medium.ttf");

            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onitemClick.itemClick(listBean);
                }
            });
            if (!(context instanceof ViewMoreActivity)) {
                if (position == 0) {
                    first.setVisibility(View.VISIBLE);
                } else {
                    first.setVisibility(View.GONE);
                }
                if (position == listData.size() - 1) {
                    last.setVisibility(View.VISIBLE);
                } else {
                    last.setVisibility(View.GONE);
                }
            }
            if (!TextUtils.isEmpty(listBean.getImageUrl()) && !listBean.getImageUrl().equals("wu")) {
                //imageView.setScaleType(ImageView.ScaleType.MATRIX);
                RequestOptions options = new RequestOptions()
                        .placeholder(R.mipmap.loading_img)  //???????????????????????????
                        .error(R.mipmap.loading_img)    //??????????????????????????????
/*                    .optionalTransform(new RoundedCorners(PublicTools.dip2px(context, 18)))
                    .override(400,400)  //?????????????????????
                    //??????????????????????????????fitCenter ????????????????????????????????????????????????ImageView????????????????????????
                    .fitCenter()
                    //??????????????????????????????centerCrop ???????????????????????????????????????????????????????????????ImageView?????????????????????????????????????????????
                    .centerCrop()
                    .circleCrop()//??????????????????????????????centerCrop ????????????*/
                        .skipMemoryCache(true)  //??????????????????
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   //???????????????????????????
                        .diskCacheStrategy(DiskCacheStrategy.NONE)  //??????????????????
                        .diskCacheStrategy(DiskCacheStrategy.DATA)  //?????????????????????????????????
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)  //????????????????????????
                        ;
                Glide.with(context)
                        .load(StaticParament.ImgUrl + listBean.getImageUrl())
                        .apply(/*RequestOptions.bitmapTransform(new RoundedCorners(PublicTools.dip2px(context, 18)))*/options)//????????????
                        .into(imageView);
            } else {
                if (!TextUtils.isEmpty(listBean.getLogoBase64())) {
                    byte[] decode = Base64.decode(listBean.getLogoBase64()/*.split(",")[1]*/, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                    imageView.setImageBitmap(bitmap);
                }
            }
            if (!TextUtils.isEmpty(listBean.getTitle())) {
                merchantName.setText(listBean.getTitle());
            }
            if (!TextUtils.isEmpty(listBean.getSubTitle())) {
                tags.setText(listBean.getSubTitle());
            }
        }
    }
    public interface OnitemClick {
        void itemClick(HomeMerchantListData.ExclusiveBean.ListBean bean);
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }
}
