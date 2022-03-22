package com.home.glx.uwallet.mvp;

import com.home.glx.uwallet.datas.IllionListData;

import java.util.ArrayList;
import java.util.List;

public class GetCacheList {

    /**
     * 单例模式
     */
    public static GetCacheList getCacheList;

    private List<IllionListData> illionList = new ArrayList<>();

    /**
     * 获得单例对象
     */
    public static GetCacheList getInstance()
    {
        if(getCacheList == null) {
            synchronized (GetCacheList.class) {
                if (getCacheList == null) {
                    getCacheList = new GetCacheList();
                }
            }
        }
        return getCacheList;
    }

    public List<IllionListData> getIllionList() {
        return illionList;
    }

    public void setIllionList(List<IllionListData> list) {
        this.illionList = list;
    }
}
