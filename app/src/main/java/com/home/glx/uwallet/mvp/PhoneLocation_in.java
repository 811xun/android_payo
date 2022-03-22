package com.home.glx.uwallet.mvp;

import android.location.Location;

/**
 * 获取地址
 */
public interface PhoneLocation_in {
    void notGetLocation();

    void setNewLocation(Location location);

    void setDefaultLocation();

}
