package com.home.glx.uwallet.datas;

public class BannerDatas {

    public BannerDatas(String path) {
        this.path = path;
    }

    /**
     * path : 1578534892710_530209a7dc111451b3fd2d2bacb5228d.png
     * rack :
     * skipType : 10
     * name :
     * skipRoute : www.apple.cn
     * sort :
     * state :
     */

    private String path;
    private String rack;
    private int skipType;
    private String name;
    private String skipRoute;
    private String sort;
    private String state;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public int getSkipType() {
        return skipType;
    }

    public void setSkipType(int skipType) {
        this.skipType = skipType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkipRoute() {
        return skipRoute;
    }

    public void setSkipRoute(String skipRoute) {
        this.skipRoute = skipRoute;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
