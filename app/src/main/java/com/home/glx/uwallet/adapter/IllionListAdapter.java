package com.home.glx.uwallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.IllionListData;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.List;

public class IllionListAdapter extends BaseAdapter {
    private Context context;
    private List<IllionListData> data;
    private boolean showImage = true;

    public IllionListAdapter(Context context, List<IllionListData> data,boolean showImage) {
        this.context = context;
        this.data = data;
        this.showImage = showImage;
    }

    public void notifyDataSetChang(List<IllionListData> data) {
        this.data = data;
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_illion_bank, parent, false);
            viewHolder.bankName = (TextView) convertView.findViewById(R.id.bank_name);
            viewHolder.logo = (ImageView) convertView.findViewById(R.id.bank_logo);
            viewHolder.enter = (ImageView) convertView.findViewById(R.id.enter);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (showImage){
            viewHolder.logo.setVisibility(View.VISIBLE);
        }else {
            viewHolder.logo.setVisibility(View.GONE);
            viewHolder.bankName.setPadding(0,0,0,0);
        }
        //切换字体
        TypefaceUtil.replaceFont(viewHolder.bankName, "fonts/gilroy_regular.ttf");
        /*TextView bankName = (TextView) convertView.findViewById(R.id.bank_name);
        ImageView logo = (ImageView) convertView.findViewById(R.id.bank_logo);
        ImageView enter = (ImageView) convertView.findViewById(R.id.enter);*/
        viewHolder.bankName.setText(data.get(position).getName());
        RequestOptions options = new RequestOptions()
                //.placeholder(R.mipmap.illion_default_icon)  //加载成功之前占位图
                .error(R.mipmap.illion_default_icon) //加载错误之后的错误图
                .skipMemoryCache(true)  //跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.ALL)   //缓存所有版本的图像
                .diskCacheStrategy(DiskCacheStrategy.NONE)  //跳过磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.DATA)  //只缓存原来分辨率的图片
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);  //只缓存最终的图片
        Glide.with(context)
                .load(StaticParament.ImgUrl + data.get(position).getImg())
                .apply(options)
                .into(viewHolder.logo);
        viewHolder.bankName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onitemClick.itemClick(data.get(position).getSlug());
            }
        });
        return convertView;
    }

    private final class ViewHolder {
        public TextView bankName;
        public ImageView logo;
        public ImageView enter;
    }

    public interface OnitemClick {
        void itemClick(String name);
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }
}
