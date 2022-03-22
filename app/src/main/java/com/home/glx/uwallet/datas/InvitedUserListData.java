package com.home.glx.uwallet.datas;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.home.glx.uwallet.BR;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.adapter.list_adapter.BindingAdapterItem;

public class InvitedUserListData{

    /**
     * name : dfrdd sffcc
     * userId : 495850156304584704
     * info : Pending
     */

    private String name;
    private long userId;
    private String info;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
