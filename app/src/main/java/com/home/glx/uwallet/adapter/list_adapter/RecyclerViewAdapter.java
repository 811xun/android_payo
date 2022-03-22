package com.home.glx.uwallet.adapter.list_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.home.glx.uwallet.BR;
import com.home.glx.uwallet.datas.NullData;
import com.home.glx.uwallet.tools.L;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.BindingHolder> {

    private Context context;
    private LayoutInflater inflater;
    private List<? extends BindingAdapterItem> items = new ArrayList<>();
    private ItemClickHandler itemClick;

    private NullData nullData;

    public RecyclerViewAdapter(Context context, List<? extends BindingAdapterItem> items) {
        this.context = context;
        this.items = items;
        if (this.items.size() == 0) {

        }
        inflater = LayoutInflater.from(context);
    }

    public void setItemClick(ItemClickHandler itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.size() > 0) {
            return items.get(position).getViewType();
        } else {
            return 123;
        }
    }

    public void notifyDataSetChang(List<? extends BindingAdapterItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BindingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding;
        if (viewType == 123) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), nullData.getViewType(), parent, false);
        } else {
            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        }
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BindingHolder holder, int position) {
        if (items.size() > 0) {
            holder.bindData(items.get(position), position);
        } else {
            holder.bindData(nullData, -1);
        }
    }

    public void onMove(int fromPosition, int toPosition) {
        L.log("from:" + fromPosition);
        L.log("to:" + toPosition);
        //对原数据进行移动
        Collections.swap(items, fromPosition, toPosition);
        //通知数据移动
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public int getItemCount() {
        if (items.size() > 0) {
            return items.size();
        }
        nullData = new NullData();
        return 1;
    }

    public class BindingHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public ViewDataBinding getBinding() {
            return binding;
        }

        /**
         * @param binding 可以看作是这个hodler代表的布局的马甲，getRoot()方法会返回整个holder的最顶层的view
         */
        public BindingHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(BindingAdapterItem item, int position) {
            binding.setVariable(BR.item, item);
            binding.setVariable(BR.itemClick, itemClick);

            if (onLookNum != null && position != -1) {
                onLookNum.lookNum(binding, position);
            }
        }
    }


    public interface OnLookNum {
        void lookNum(ViewDataBinding binding, int position);
    }

    private OnLookNum onLookNum;

    public void setOnLookNum(OnLookNum onLookNum) {
        this.onLookNum = onLookNum;
    }
}
