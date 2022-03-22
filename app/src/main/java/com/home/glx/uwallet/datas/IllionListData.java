package com.home.glx.uwallet.datas;

import androidx.work.Data;

import java.io.Serializable;

public class IllionListData implements Serializable {

    /**
     * img :
     * name : Australian Military Bank
     * slug : adcu
     */

    private String img;
    private String name;
    private String slug;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
