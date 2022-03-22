package com.home.glx.uwallet.tools;

import android.util.Log;

import com.home.glx.uwallet.BuildConfig;

import java.math.BigDecimal;

/**
 * Created by GLX on 2017/5/8.
 */

public class L {

    public static boolean isDebug = BuildConfig.DEBUG;

    public static void log(String title, String msg) {
        if (isDebug) {
            Log.e(title, msg);
        }
    }

    public static void log(String msg) {
        if (isDebug && msg != null) {
            Log.e("打印数值：", msg);
        }
    }

    /**
     * 保留两位小数
     *
     * @param f
     * @return
     */
    public static double baoliuliangwei(double f) {
        BigDecimal bg = new BigDecimal(f);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }

}
