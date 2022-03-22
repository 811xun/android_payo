package com.home.glx.uwallet.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.MerchantInfoData;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.WordWrapView;
import com.home.glx.uwallet.tools.CalcTool;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

import java.util.List;

public class MerchantInfoAdapter extends BaseAdapter {

    private Context context;
    private List<MerchantInfoData> data;
    private int width;

    public MerchantInfoAdapter(Context context, List<MerchantInfoData> data, int width) {
        this.context = context;
        this.data = data;
        this.width = width;
    }

    public void notifyDataSetChang(List<MerchantInfoData> listData) {
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

    private final class ViewHolder {
        public LinearLayout info;
        public TextView top;
        public TextView discount;
        public TextView merchantName;
        public TextView wholesaleFlag;
        public WordWrapView tags;
        public TextView distance;
        public ImageView imageView;
        public CardView cardView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_merchant_page_list_data, parent, false);
            viewHolder.top = (TextView) convertView.findViewById(R.id.top_tv);
            viewHolder.info = (LinearLayout) convertView.findViewById(R.id.id_info);
            viewHolder.discount = (TextView) convertView.findViewById(R.id.id_discount);
            viewHolder.merchantName = (TextView) convertView.findViewById(R.id.id_merchant_name);
            viewHolder.wholesaleFlag = (TextView) convertView.findViewById(R.id.wholesale_flag);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
            viewHolder.tags = (WordWrapView) convertView.findViewById(R.id.id_popular_list);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.id_item_img);
            viewHolder.cardView = (CardView) convertView.findViewById(R.id.id_item_img_bg);
            convertView.setTag(viewHolder);
        }  else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /*LinearLayout info = (LinearLayout) convertView.findViewById(R.id.id_info);
        TextView top = (TextView) convertView.findViewById(R.id.top_tv);
        TextView discount = (TextView) convertView.findViewById(R.id.id_discount);
        TextView merchantName = (TextView) convertView.findViewById(R.id.id_merchant_name);
        TextView wholesaleFlag = (TextView) convertView.findViewById(R.id.wholesale_flag);
        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        WordWrapView tags = (WordWrapView) convertView.findViewById(R.id.id_popular_list);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.id_item_img);
        CardView cardView = (CardView) convertView.findViewById(R.id.id_item_img_bg);*/
        //CardView cardView = (CardView) convertView.findViewById(R.id.item_cardview);
/*        SharePreferenceUtils wSharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.DEVICE);
        int width = (int) wSharePreferenceUtils.get(StaticParament.DEVICE_WIDTH, 720);*/
        if (position == 0) {
            viewHolder.top.setVisibility(View.VISIBLE);
        } else {
            viewHolder.top.setVisibility(View.GONE);
        }
        // 屏幕宽度（像素）
        FrameLayout.LayoutParams lp= (FrameLayout.LayoutParams) viewHolder.imageView.getLayoutParams();
        RelativeLayout.LayoutParams lf = (RelativeLayout.LayoutParams) viewHolder.cardView.getLayoutParams();
        lp.height = (int) (((width - PublicTools.dip2px(context, 32)) / 2.185f));
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        viewHolder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onitemClick.itemClick(data.get(position).getId(), data.get(position).getPracticalName());
            }
        });
        lf.height = (int) (((width - PublicTools.dip2px(context, 32)) / 2.185f));
        lf.width = ViewGroup.LayoutParams.MATCH_PARENT;
        viewHolder.imageView.setLayoutParams(lp);
        viewHolder.cardView.setLayoutParams(lf);

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
        if (!TextUtils.isEmpty(data.get(position).getPracticalName())) {
            viewHolder.merchantName.setText(data.get(position).getPracticalName());
        }
        if (!TextUtils.isEmpty(data.get(position).getUserDiscount()) && Float.parseFloat(data.get(position).getUserDiscount()) != 0) {
            viewHolder.discount.setTag("yes");
            String dis = (int) CalcTool.mm(data.get(position).getUserDiscount(), "100") + "% OFF";
            viewHolder.discount.setText(Html.fromHtml((int) CalcTool.mm(data.get(position).getUserDiscount(), "100") + "%" + "<font color='black'>" + "OFF" + "</font>"));
        } else {
            viewHolder.discount.setTag("no");
            viewHolder.discount.setVisibility(View.GONE);
        }
        if (data.get(position).getHaveWholeSell() == 0) {
            viewHolder.wholesaleFlag.setVisibility(View.GONE);
        } else {
            if (!viewHolder.discount.getTag().equals("no")) {
                viewHolder.wholesaleFlag.setVisibility(View.VISIBLE);
            }
        }
        if (!TextUtils.isEmpty(data.get(position).getDistance())) {
            String cityName = "";
            String distances = "";
            if (!TextUtils.isEmpty(data.get(position).getCityName())) {
                cityName = data.get(position).getCityName();
            }
            if (!TextUtils.isEmpty(data.get(position).getDistance())) {
                distances = data.get(position).getDistance();
                distances = PublicTools.twoend(distances);
            }
            if (TextUtils.isEmpty(cityName)) {
                viewHolder.distance.setText(distances + "km");
            } else {
                viewHolder.distance.setText(cityName + "·" + distances + "km");
            }
        } else {
            viewHolder.distance.setVisibility(View.GONE);
        }
        if (data.get(position).getTags() != null) {
            viewHolder.tags.setTagList(data.get(position).getTags());
        }
        return convertView;
    }

    public interface OnitemClick {
        void itemClick(String id, String name);
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }
}
