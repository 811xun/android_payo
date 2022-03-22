package com.home.glx.uwallet.tools;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.home.glx.uwallet.request.StaticParament;

import java.util.Locale;

public class LocalUtils {

    public static Context attachBaseContext(Context context) {
        Locale chooseLocale = Locale.ENGLISH;
        SharePreferenceUtils sharePreferenceUtils =
                new SharePreferenceUtils(context, StaticParament.APP);

        /*if (sharePreferenceUtils.get(StaticParament.LANGUAGE, "").equals("0")) {
            chooseLocale = Locale.ENGLISH;
        } else if (sharePreferenceUtils.get(StaticParament.LANGUAGE, "").equals("1")) {
            chooseLocale = Locale.CHINESE;
        } else {
            return context;
        }*/
        chooseLocale = Locale.ENGLISH;

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


    /**
     * 设置应用语言
     *
     * @param lang 要设置的语言，如：zh、en，目前只支持中文和英文，其它的请自行添加
     */
    public static void setLang(Context context) {
        String lang = SystemUtils.getSysLangusge(context);
        /*SharePreferenceUtils sharePreferenceUtils =
                new SharePreferenceUtils(context, StaticParament.APP);
        if (sharePreferenceUtils.get(StaticParament.LANGUAGE, "").equals("0")) {
            lang = "en";
        } else if (sharePreferenceUtils.get(StaticParament.LANGUAGE, "").equals("1")) {
            lang = "zh";
        }*/
        lang = "en";
        Resources res = context.getResources();
        //应用当前语言
        String currLang = res.getConfiguration().locale.getLanguage();
        //当前语言和用户语言不一致时更改应用的当前语言
        if (!currLang.equals(lang)) {
            //设置应用语言类型
            Configuration config = res.getConfiguration();
            DisplayMetrics dm = res.getDisplayMetrics();
            config.locale = new Locale(lang);
//            一般中英法等常用文直接用Locale类列好的  有些是还没有列出来的 如印地语  需要自己手动new
            //config.locale = Locale.ENGLISH;
            res.updateConfiguration(config, dm);
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

