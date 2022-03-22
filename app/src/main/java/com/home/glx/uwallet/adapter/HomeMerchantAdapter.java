package com.home.glx.uwallet.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
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
import com.home.glx.uwallet.datas.HomeMerchantItemData;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.MerchantListener;
import com.home.glx.uwallet.mvp.model.MerchantModel;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeMerchantAdapter extends RecyclerView.Adapter {
    private Context context;
    private LayoutInflater inflater;
    private MerchantListener merchantListener;
    private List<HomeMerchantItemData> listData = new ArrayList<>();
    private boolean update = true;

    public HomeMerchantAdapter(Context context, List<HomeMerchantItemData> listData) {
        this.context = context;
        this.listData = listData;
        inflater = LayoutInflater.from(context);
        merchantListener = new MerchantModel(context);
    }

    public void notifyData(List<HomeMerchantItemData> listData) {
        if (listData == null) {
            return;
        }
        this.listData = listData;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return 166;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 166) {
            return new ItemView(inflater.inflate(R.layout.item_home_merchant_list, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 166) {
            ((ItemView) holder).binData(position, listData.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (listData != null) {
            return listData.size();
        } else {
            return 0;
        }
    }

    class ItemView extends RecyclerView.ViewHolder {
        private ImageView favorite;
        public CardView info;
        public TextView discount;
        public TextView merchantName;
        public TextView tags;
        public ImageView imageView;
        private TextView last;
        private TextView first;

        public ItemView(@NonNull View itemView) {
            super(itemView);
            info = (CardView) itemView.findViewById(R.id.id_info);
            favorite = (ImageView) itemView.findViewById(R.id.favorite_flag);
            discount = (TextView) itemView.findViewById(R.id.id_discount);
            merchantName = (TextView) itemView.findViewById(R.id.id_merchant_name);
            tags = (TextView) itemView.findViewById(R.id.id_popular_list);
            imageView = (ImageView) itemView.findViewById(R.id.id_item_img);
            last = (TextView) itemView.findViewById(R.id.last_item);
            first = (TextView) itemView.findViewById(R.id.first_item);
        }

        public void binData(int position, final HomeMerchantItemData listBean) {
            //切换字体
            TypefaceUtil.replaceFont(merchantName, "fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(tags, "fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(discount, "fonts/gilroy_semiBold.ttf");
            if (!TextUtils.isEmpty(listBean.getCollectionStatus()) && listBean.getCollectionStatus().equals("1")) {
                favorite.setImageResource(R.mipmap.have_save_icon);
                favorite.setTag("1");
            } else {
                favorite.setImageResource(R.mipmap.not_save_icon);
                favorite.setTag("0");
            }
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(listBean.getMerchantId())) {
                        onitemClick.itemClick(listBean.getMerchantId());
                    }
                }
            });
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
            if (update) {
                if (!TextUtils.isEmpty(listBean.getLogoUrl()) && !listBean.getLogoUrl().equals("wu")) {
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
                            .load(StaticParament.ImgUrl + listBean.getLogoUrl())
                            .apply(/*RequestOptions.bitmapTransform(new RoundedCorners(PublicTools.dip2px(context, 18)))*/options)//圆角半径
                            .into(imageView);
                } else {
                    if (!TextUtils.isEmpty(listBean.getLogoBase64())) {
                        byte[] decode = Base64.decode(listBean.getLogoBase64()/*.split(",")[1]*/, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
            if (!TextUtils.isEmpty(listBean.getPracticalName())) {
                merchantName.setText(listBean.getPracticalName());
            }
            if (!TextUtils.isEmpty(listBean.getUserDiscount()) && Float.parseFloat(listBean.getUserDiscount()) != 0) {
                discount.setTag("yes");
                discount.setVisibility(View.VISIBLE);
                String dis = (int) CalcTool.mm(listBean.getUserDiscount(), "100") + "% OFF";
                discount.setText(Html.fromHtml((int) CalcTool.mm(listBean.getUserDiscount(), "100") + "%" + " OFF"));
            } else {
                discount.setTag("no");
                discount.setVisibility(View.GONE);
            }


            final String isFavorite = favorite.getTag().toString();
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Map<String, Object> map = new HashMap<>();
                    map.put("merchantId", listBean.getMerchantId());
                    if (favorite.getTag().equals("1")) {
                        map.put("isAdd", "0");
                        favorite.setImageResource(R.mipmap.not_save_icon);
                        favorite.setTag("0");
                        listBean.setCollectionStatus("0");
                    } else {
                        map.put("isAdd", "1");
                        favorite.setImageResource(R.mipmap.have_save_icon);
                        favorite.setTag("1");
                        listBean.setCollectionStatus("1");

                    }
                    merchantListener.addNewFavorite(map, new ActionCallBack() {
                        @Override
                        public void onSuccess(Object... o) {
                            if (!favorite.getTag().toString().equals(isFavorite)) {
                                update = false;
//                                onitemClick.changeFavorite();
                                notifyDataSetChanged();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        update = true;
                                    }
                                }, 3000);
                            }
                        }

                        @Override
                        public void onError(String e) {
                            if (favorite.getTag().equals("0")) {
                                favorite.setImageResource(R.mipmap.have_save_icon);
                                favorite.setTag("1");
                                listBean.setCollectionStatus("1");

                            }
                            if (favorite.getTag().equals("1")) {
                                favorite.setImageResource(R.mipmap.not_save_icon);
                                favorite.setTag("0");
                                listBean.setCollectionStatus("0");
                            }
                            update = false;
                            notifyDataSetChanged();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    update = true;
                                }
                            }, 3000);
                        }
                    });
                }
            });
            String tagDistance = "";
            if (listBean.getTags() != null) {
                for (int i = 0; i < listBean.getTags().size(); i++) {
                    if (i != listBean.getTags().size() - 1) {
                        tagDistance += listBean.getTags().get(i) + ", ";
                    } else {
                        tagDistance += listBean.getTags().get(i);
                    }
                }
            }
            if (!TextUtils.isEmpty(listBean.getDistance())) {
                if (!tagDistance.equals("")) {
                    tagDistance += " · " + listBean.getDistance() + "km";
                } else {
                    tagDistance = listBean.getDistance() + "km";
                }
            }
            tags.setText(tagDistance);
        }
    }

    public interface OnitemClick {
        void itemClick(String id);

        void changeFavorite();
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }
}
