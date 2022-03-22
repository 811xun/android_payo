package com.home.glx.uwallet.adapter;

import android.Manifest;
import android.content.Context;
import android.location.LocationManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.FavoriteListActivity;
import com.home.glx.uwallet.activity.SearchMerchatDataActivity;
import com.home.glx.uwallet.activity.ViewAllActivity;
import com.home.glx.uwallet.datas.MerchantItemData;
import com.home.glx.uwallet.mvp.contract.ActionCallBack;
import com.home.glx.uwallet.mvp.contract.MerchantListener;
import com.home.glx.uwallet.mvp.model.MerchantModel;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static pub.devrel.easypermissions.EasyPermissions.hasPermissions;

public class FavoriteListAdapter extends BaseAdapter {
    private Context context;
    private List<MerchantItemData> data;
    private MerchantListener merchantListener;
    private int width;

    public static boolean isOpenLocService(final Context context) {
        boolean isGps = false; //推断GPS定位是否启动

        boolean isNetwork = false; //推断网络定位是否启动

        if (context != null) {
            LocationManager locationManager

                    = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            if (locationManager != null) {
//通过GPS卫星定位，定位级别能够精确到街(通过24颗卫星定位，在室外和空旷的地方定位准确、速度快)

                isGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

//通过WLAN或移动网络(3G/2G)确定的位置(也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物(建筑群或茂密的深林等)密集的地方定位)

                isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            }

            if (isGps || isNetwork) {
                return true;
            }
        }
        return false;
    }

    public boolean checkLocationPermission() {
        String[] PERMISSION_STORAGE =
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        try {
            if (hasPermissions(Objects.requireNonNull(context), PERMISSION_STORAGE)) {
                //有权限
                return true;
            } else {
                L.log("申请权限");
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    boolean localOpen = true;

    public FavoriteListAdapter(Context context, List<MerchantItemData> data) {
        this.context = context;


        this.data = data;
        merchantListener = new MerchantModel(context);
    }

    public void notifyDataSetChang(List<MerchantItemData> listData) {
        this.data = listData;
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

    public void updataView(int posi, String flag, ListView listView) {
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();
        View view = listView.getChildAt(posi - visibleFirstPosi);
        ViewHolder holder = (ViewHolder) view.getTag();
        if (flag.equals("1")) {
            holder.favorite.setImageResource(R.mipmap.have_save_icon);
            holder.favorite.setTag("1");
        } else {
            holder.favorite.setImageResource(R.mipmap.not_save_icon);
            holder.favorite.setTag("0");
        }
            /*MerchantItemData merchantItemData = data.get(posi);
            merchantItemData.setIsFavorite(flag);
            data.set(posi, merchantItemData);
            notifyDataSetChanged();*/
    }

    public final class ViewHolder {
        public ImageView favorite;
        public CardView info;
        public TextView discount;
        public TextView merchantName;
        public TextView tags;
        public ImageView imageView;
        private TextView top;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (!isOpenLocService(context) || !checkLocationPermission()) {
            localOpen = false;
        } else {
            localOpen = true;
        }
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_favorite_list, parent, false);
            viewHolder.info = (CardView) convertView.findViewById(R.id.id_info);
            viewHolder.favorite = (ImageView) convertView.findViewById(R.id.favorite_flag);
            viewHolder.discount = (TextView) convertView.findViewById(R.id.id_discount);
            viewHolder.merchantName = (TextView) convertView.findViewById(R.id.id_merchant_name);
            viewHolder.tags = (TextView) convertView.findViewById(R.id.id_popular_list);
            viewHolder.top = (TextView) convertView.findViewById(R.id.top);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.id_item_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //切换字体
        TypefaceUtil.replaceFont(viewHolder.merchantName, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(viewHolder.tags, "fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(viewHolder.discount, "fonts/gilroy_semiBold.ttf");
        if (position == 0 && (context instanceof ViewAllActivity || context instanceof SearchMerchatDataActivity)) {
            viewHolder.top.setVisibility(View.GONE);
        } else {
            viewHolder.top.setVisibility(View.VISIBLE);
        }
        viewHolder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onitemClick.itemClick(position, data.get(position).getMerchantId());
            }
        });

        if (!TextUtils.isEmpty(data.get(position).getLogoUrl())) {
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
                    .load(StaticParament.ImgUrl + data.get(position).getLogoUrl())
                    .apply(/*RequestOptions.bitmapTransform(new RoundedCorners(PublicTools.dip2px(context, 18)))*/options)//圆角半径
                    .into(viewHolder.imageView);
        }

        if (!TextUtils.isEmpty(data.get(position).getTradingName())) {
            viewHolder.merchantName.setText(data.get(position).getTradingName());
        }
        if (!TextUtils.isEmpty(data.get(position).getUserDiscount()) && Float.parseFloat(data.get(position).getUserDiscount()) != 0) {
            viewHolder.discount.setVisibility(View.VISIBLE);
            String dis = (int) CalcTool.mm(data.get(position).getUserDiscount(), "100") + "% OFF";
            viewHolder.discount.setText(Html.fromHtml((int) CalcTool.mm(data.get(position).getUserDiscount(), "100") + "%" + " OFF"));
        } else {
            viewHolder.discount.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(data.get(position).getIsFavorite()) && data.get(position).getIsFavorite().equals("1")) {
            viewHolder.favorite.setImageResource(R.mipmap.have_save_icon);
            viewHolder.favorite.setTag("1");
        } else {
            viewHolder.favorite.setImageResource(R.mipmap.not_save_icon);
            viewHolder.favorite.setTag("0");
        }
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalViewHolder.favorite.getTag().equals("0") && context instanceof FavoriteListActivity) {
                    return;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("merchantId", data.get(position).getMerchantId());
                if (finalViewHolder.favorite.getTag().equals("1")) {
                    map.put("isAdd", "0");
                    finalViewHolder.favorite.setImageResource(R.mipmap.not_save_icon);
                    finalViewHolder.favorite.setTag("0");
                } else {
                    map.put("isAdd", "1");
                    finalViewHolder.favorite.setImageResource(R.mipmap.have_save_icon);
                    finalViewHolder.favorite.setTag("1");
                }
                merchantListener.addNewFavorite(map, new ActionCallBack() {
                    @Override
                    public void onSuccess(Object... o) {
                        onitemClick.unFavorite(data.get(position).getMerchantId());
                    }

                    @Override
                    public void onError(String e) {
                        if (finalViewHolder.favorite.getTag().equals("0")) {
                            finalViewHolder.favorite.setImageResource(R.mipmap.have_save_icon);
                            finalViewHolder.favorite.setTag("1");
                        } else {
                            finalViewHolder.favorite.setImageResource(R.mipmap.not_save_icon);
                            finalViewHolder.favorite.setTag("0");
                        }
                    }
                });
            }
        });

        String tagDistance = "";
        if (data.get(position).getTags() != null) {
            for (int i = 0; i < data.get(position).getTags().size(); i++) {
                if (i != data.get(position).getTags().size() - 1) {
                    tagDistance += data.get(position).getTags().get(i) + ", ";
                } else {
                    tagDistance += data.get(position).getTags().get(i);
                }
            }
        }
        if (!TextUtils.isEmpty(data.get(position).getDistance()) && localOpen) {
            if (!tagDistance.equals("")) {
                tagDistance += " · " + data.get(position).getDistance() + "km";
            } else {
                tagDistance = data.get(position).getDistance() + "km";
            }
        }
        viewHolder.tags.setText(tagDistance);
        return convertView;
    }

    public interface OnitemClick {
        void unFavorite(String id);

        void itemClick(int position, String id);
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }
}
