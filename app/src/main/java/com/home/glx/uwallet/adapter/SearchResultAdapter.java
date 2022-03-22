package com.home.glx.uwallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.tools.Pinyin4jUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<String> data;
    private Filter filter;
    private String search;

    public SearchResultAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    public void setString(String search) {
        this.search = search;
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
        convertView = LayoutInflater.from(context).inflate(R.layout.search_result_list_item, parent, false);
        TextView textView1 = (TextView) convertView.findViewById(R.id.text_result1);
        TextView textView2 = (TextView) convertView.findViewById(R.id.text_result2);
        if (data.get(position).length() >= search.length()) {
            if (search != null && data.get(position).substring(0, search.length()).equalsIgnoreCase(search)) {
                //search = search.substring(0, 1).toUpperCase() + search.substring(1);
                search = data.get(position).substring(0, search.length());
                String text = "<b><tt>" + search + "</tt></b>"
                        + data.get(position).substring(search.length());
                textView1.setText(search);
                textView2.setText(data.get(position).substring(search.length()));
                textView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onitemClick.itemClick(data.get(position));
                    }
                });
                textView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onitemClick.itemClick(data.get(position));
                    }
                });
            } else {
                //textView.setText(data.get(position));
            }
        }
        return convertView;
    }

    public interface OnitemClick {
        void itemClick(String tag);
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new PersonFilter(data);
        }
        return filter;
    }

    // 定义过滤的适配
    private class PersonFilter extends Filter {
        private List<String> bean;

        public PersonFilter(List<String> list) {
            this.bean = list;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = bean;
                results.count = bean.size();
            } else {
                List<String> mList = new ArrayList<String>();
                for (String p : bean) {
                    if (p.toUpperCase().startsWith(constraint.toString().toUpperCase())
                            || Pinyin4jUtil.getPinYin(p).toUpperCase().startsWith(constraint.toString().toUpperCase())
                    ) {
                        mList.add(p);
                    }
                }
                results.values = mList;
                results.count = mList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data = (List<String>) results.values;
            notifyDataSetChanged();
        }

    }
}
