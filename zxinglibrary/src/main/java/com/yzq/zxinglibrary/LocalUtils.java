package com.yzq.zxinglibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

public class LocalUtils {

    public static Context attachBaseContext(Context context) {
        Locale chooseLocale = Locale.ENGLISH;
        SharePreferenceUtils sharePreferenceUtils =
                new SharePreferenceUtils(context, "app");

        if (sharePreferenceUtils.get("language", "").equals("0")) {
            chooseLocale = Locale.ENGLISH;
        } else if (sharePreferenceUtils.get("language", "").equals("1")) {
            chooseLocale = Locale.CHINESE;
        } else {
            return context;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, chooseLocale);
        } else {
            //7.0之前
            Configuration configuration = context.getResources().getConfiguration();
            configuration.locale = chooseLocale;
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            context.getResources().updateConfiguration(configuration, displayMetrics);
            return context;
        }
    }


    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        return context.createConfigurationContext(configuration);
    }

}

