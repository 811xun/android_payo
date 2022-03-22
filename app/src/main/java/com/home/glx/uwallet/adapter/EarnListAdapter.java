package com.home.glx.uwallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.InvitedUserListData;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.List;

public class EarnListAdapter extends BaseAdapter {
    private Context context;
    private List<InvitedUserListData> data;
    public EarnListAdapter(Context context, List<InvitedUserListData> data) {
        this.context = context;
        this.data = data;
    }

    public void notifyDataSetChang(List<InvitedUserListData> listData) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_earn_list, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.info = (TextView) convertView.findViewById(R.id.info);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //切换字体
        TypefaceUtil.replaceFont(viewHolder.name,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(viewHolder.info,"fonts/gilroy_medium.ttf");
        viewHolder.name.setText(data.get(position).getName());
        viewHolder.info.setText(data.get(position).getInfo() + " AUD");
        return convertView;
    }

    private final class ViewHolder {
        public TextView name;
        public TextView info;
    }
}
