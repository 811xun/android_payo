package com.home.glx.uwallet.datas;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.home.glx.uwallet.BR;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.list_adapter.BindingAdapterItem;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.PublicTools;

public class TranstationItemData extends BaseObservable implements BindingAdapterItem {


    /**
     * id : 401899094964523008
     * createdBy : 395359685821681664
     * createdDate : 10:50 23/03/2020
     * modifiedBy : 395359685821681664
     * modifiedDate : 10:50 23/03/2020
     * status : 1
     * ip : 27.217.105.237
     * transType : 2
     * transAmount : 0.11
     * fee : 0.0
     * rate : 0.05
     * payAmount : 0.09
     * payType : null
     * showType : null
     * logoUrl : null
     * tradingName : null
     * transState : 31
     * transTypeStr : Pay with Card
     * transStateStr : Account recorded
     * creditOrderNo :
     * monthYear : March,2020
     */

    private String drawable;
    private String id;
    private String createdBy;
    private String createdDate;
    private String modifiedBy;
    private String modifiedDate;
    private int status;
    private String ip;
    private int transType;
    private String transAmount;
    private double fee;
    private double rate;
    private String payAmount;
    private Object payType;
    private Object showType;
    private String logoUrl;
    private String tradingName;
    private String transState;
    private String transTypeStr;
    private String transStateStr;
    private String displayDate;
    private String creditOrderNo;
    private String monthYear;

    @Override
    public int getViewType() {
        return R.layout.item_transaction;
    }

    @Override
    public Object getViewItem() {
        return this;
    }

    @Bindable
    public String getDisplayDate() {
        return displayDate;
    }

    public void setDisplayDate(String displayDate) {
        this.displayDate = displayDate;
    }

    //@BindingAdapter({"app:imageUrl"})
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(PublicTools.dip2px(imageView.getContext(), 11))))//圆角半径
                .into(imageView);
    }

    //@BindingAdapter({"app:point"})
    @BindingAdapter({"point"})
    public static void setPoint(View view, String drawable) {
        if (drawable.equals("0")) {
            view.setBackground(view.getContext().getResources().getDrawable(R.drawable.green_point));
        } else {
            view.setBackground(view.getContext().getResources().getDrawable(R.drawable.gray_point));
        }

    }

    //@BindingAdapter({"app:stausTextColor"})
    @BindingAdapter({"stausTextColor"})
    public static void setStausTextColor(TextView view, String drawable) {
        if (drawable.equals("0")) {
            view.setTextColor(view.getContext().getColor(R.color.greenPoint));
        } else {
            view.setTextColor(view.getContext().getColor(R.color.grayPoint));
        }

    }

    public String getDrawable() {
        if (getTransStateStr() != null && getTransStateStr().equals("Successful")) {
            return "0";
        } else {
            return "1";
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getTransType() {
        return transType;
    }

    @Bindable
    public String getType() {
        if (getTransType() == 22) {
            return "Instalment";
        } else {
            return "Bank Card";
        }
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public String getTransAmount() {
        return "$" + PublicTools.twoend(transAmount);
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Bindable
    public String getPayAmount() {
        return "$" + PublicTools.twoend(payAmount);
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public Object getPayType() {
        return payType;
    }

    public void setPayType(Object payType) {
        this.payType = payType;
    }

    public Object getShowType() {
        return showType;
    }

    public void setShowType(Object showType) {
        this.showType = showType;
    }

    @Bindable
    public String getLogoUrl() {
        if (logoUrl == null) {
            logoUrl = /*"merchantIntroduction/502272472802611200.jpeg"*/"";
        }
        return StaticParament.ImgUrl + logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Bindable
    public String getTradingName() {
        if (tradingName == null) {
            tradingName = "";
        }
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public String getTransState() {
        return transState;
    }

    public void setTransState(String transState) {
        this.transState = transState;
    }

    @Bindable
    public String getTransTypeStr() {
        return transTypeStr;
    }

    public void setTransTypeStr(String transTypeStr) {
        this.transTypeStr = transTypeStr;
    }

    @Bindable
    public String getTransStateStr() {
        return transStateStr;
    }

    public void setTransStateStr(String transStateStr) {
        this.transStateStr = transStateStr;
    }

    public String getCreditOrderNo() {
        return creditOrderNo;
    }

    public void setCreditOrderNo(String creditOrderNo) {
        this.creditOrderNo = creditOrderNo;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }
}
