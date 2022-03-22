package com.home.glx.uwallet.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.IllionListData;
import com.home.glx.uwallet.tools.Pinyin4jUtil;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchIllionResultAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<IllionListData> data;
    private Filter filter;
    private String search;

    public void notifyData(List<IllionListData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public SearchIllionResultAdapter(Context context, List<IllionListData> data) {
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
        //切换字体
        TypefaceUtil.replaceFont(textView1, "fonts/gilroy_regular.ttf");
        TypefaceUtil.replaceFont(textView2, "fonts/gilroy_regular.ttf");
        if (search != null && data.get(position).getName().length() >= search.length()) {
            if (data.get(position).getName().substring(0, search.length()).equalsIgnoreCase(search)) {
                Log.d("sxjjkkxxk", "getView: " + data.get(position).getName());
                //search = search.substring(0, 1).toUpperCase() + search.substring(1);
                search = data.get(position).getName().substring(0, search.length());
                String text = "fonts/gilroy_regular.ttf<b>" + search + "</b>"
                        + data.get(position).getName().substring(search.length());
                TypefaceUtil.replaceFont(textView1, "fonts/gilroy_regular.ttf");

                textView1.setText(search);
                TextPaint tp = textView1.getPaint();
                tp.setFakeBoldText(true);
//                TypefaceUtil.replaceFont(textView1,"fonts/gilroy_regular.ttf");
//                textView1.postInvalidate();
//                textView1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

                textView2.setText(data.get(position).getName().substring(search.length()));
                textView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onitemClick.itemClick(data.get(position).getSlug());
                    }
                });
                textView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onitemClick.itemClick(data.get(position).getSlug());
                    }
                });
            } else {
                //textView.setText(data.get(position));
                textView2.setText(data.get(position).getName());
                textView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onitemClick.itemClick(data.get(position).getSlug());
                    }
                });
                textView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onitemClick.itemClick(data.get(position).getSlug());
                    }
                });
            }
        }
        return convertView;
    }

    public interface OnitemClick {
        void itemClick(String tag);

        void getData(int result);
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
        private List<IllionListData> bean;

        public PersonFilter(List<IllionListData> list) {
            this.bean = list;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = bean;
                results.count = bean.size();
            } else {
                List<IllionListData> mList = new ArrayList<IllionListData>();
                for (IllionListData p : bean) {
                    if (p.getName() == null) {
                        continue;
                    }
                    if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase())
                            || Pinyin4jUtil.getPinYin(p.getName()).toUpperCase().startsWith(constraint.toString().toUpperCase())
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
            data = (List<IllionListData>) results.values;
            if (data == null) {
                return;
            }
            onitemClick.getData(data.size());
            notifyDataSetChanged();
        }

    }
}
