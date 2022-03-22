package com.home.glx.uwallet.datas;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.home.glx.uwallet.BR;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.list_adapter.BindingAdapterItem;

/**
 * 分期付交易明细时间Item
 */
public class FQRepayTimeData extends BaseObservable implements BindingAdapterItem {

    @Override
    public int getViewType() {
        return R.layout.item_fq_repay_time;
    }

    @Override
    public Object getViewItem() {
        return this;
    }

    private String year;
    private String month;
    private String money;

    public FQRepayTimeData(String year, String month, String money) {
        this.year = year;
        this.month = month;
        this.money = money;
    }

    @Bindable
    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
        notifyPropertyChanged(BR.year);
    }

    @Bindable
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
        notifyPropertyChanged(BR.month);
    }

    @Bindable
    public String getMoney() {
        return "Expense: $" + money;
    }

    public void setMoney(String money) {
        this.money = money;
        notifyPropertyChanged(BR.money);
    }

    @Bindable
    public String getTime() {
        return year/* + "," + month*/;
    }
}
