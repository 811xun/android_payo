package com.home.glx.uwallet.datas;

import androidx.fragment.app.Fragment;

public class TabDatas {

    private int title;
    private int of_img;
    private int on_img;
    private Fragment fragment;

    public TabDatas(int title, int of_img, int on_img) {
        this.title = title;
        this.of_img = of_img;
        this.on_img = on_img;
    }

    public TabDatas(int title, int of_img, int on_img, Fragment fragment) {
        this.title = title;
        this.of_img = of_img;
        this.on_img = on_img;
        this.fragment = fragment;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getOf_img() {
        return of_img;
    }

    public void setOf_img(int of_img) {
        this.of_img = of_img;
    }

    public int getOn_img() {
        return on_img;
    }

    public void setOn_img(int on_img) {
        this.on_img = on_img;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
