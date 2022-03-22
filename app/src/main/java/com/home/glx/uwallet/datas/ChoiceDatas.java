package com.home.glx.uwallet.datas;

import com.home.glx.uwallet.MyApplication;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.SystemUtils;

import java.util.Locale;

public class ChoiceDatas {

    private String code;
    private String name;
    private String enName;
    private String value;

    /**
     * name	是	long	名
     * enName	是	string	英文名
     * code	是	string	code
     * value	是	string	值
     */

    public ChoiceDatas() {
    }

    public ChoiceDatas(String code, String text, String enName) {
        this.value = code;
        this.name = text;
        this.enName = enName;
    }

    public String getCName() {
        return name;
    }

    public String getEName() {
        return enName;
    }

    public String getName() {
        SharePreferenceUtils sharePreferenceUtils =
                new SharePreferenceUtils(MyApplication.context, StaticParament.APP);
        if (sharePreferenceUtils.get(StaticParament.LANGUAGE, "").equals("0")) {
            return enName;
        } else if (sharePreferenceUtils.get(StaticParament.LANGUAGE, "").equals("1")) {
            return name;
        } else {
            if (SystemUtils.getSysLangusge(MyApplication.context).equals("zh")) {
                return name;
            }
        }
        return enName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
//        return name;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        SharePreferenceUtils sharePreferenceUtils =
                new SharePreferenceUtils(MyApplication.context, StaticParament.APP);
        if (sharePreferenceUtils.get(StaticParament.LANGUAGE, "").equals("0")) {
            return enName;
        } else if (sharePreferenceUtils.get(StaticParament.LANGUAGE, "").equals("1")) {
            return name;
        } else {
            if (SystemUtils.getSysLangusge(MyApplication.context).equals("zh")) {
                return name;
            }
        }
        return enName;
    }

}
