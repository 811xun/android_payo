package com.home.glx.uwallet.datas;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.home.glx.uwallet.BR;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.list_adapter.BindingAdapterItem;
import com.home.glx.uwallet.tools.L;


/**
 * 没有数据的展示
 */
public class NullData extends BaseObservable implements BindingAdapterItem {

    private String text = "No data";

    @Override
    public int getViewType() {
        return R.layout.view_null_data;
    }

    @Override
    public Object getViewItem() {
        return this;
    }

    @Bindable
    public String getText() {
        L.log("无数据提示：" + text);
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }
}
