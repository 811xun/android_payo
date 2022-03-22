package com.home.glx.uwallet.tools;

import android.text.TextUtils;

import java.util.Calendar;
import java.util.Date;

import static android.text.TextUtils.isEmpty;

public class IdcardUtils {

    public static int getRealYear(String birthDate) {

        Calendar cal = Calendar.getInstance();
        //当前年
        int currentYear = cal.get(Calendar.YEAR);
        //当前月
        int currentMonth = (cal.get(Calendar.MONTH)) + 1;
        //当前月的第几天：即当前日
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);


        int birthYear = 0;
        int birthMonth = 0;
        int birthDay = 0;
        int realYear = 0;

        if (!isEmpty(birthDate)) {

            if (!isEmpty(birthDate) && birthDate.length() == 8) {
                birthYear = Integer.valueOf(birthDate.substring(0, 4));
                birthMonth = Integer.valueOf(birthDate.substring(4, 6));
                birthDay = Integer.valueOf(birthDate.substring(6, 8));
            }

            realYear = currentYear - birthYear;

            if (birthMonth > currentMonth) {
                realYear = realYear - 1;
            } else if (birthMonth == currentMonth) {
                if (birthDay > currentDay) {
                    realYear = realYear - 1;
                } else {
                    realYear = realYear;
                }
            } else {
                realYear = realYear;
            }
        }

        return realYear;
    }

    /**
     * 判断是否小于或者等于当前age的年龄
     *
     * @param age
     * @return
     */
    public static boolean isChildUnderTargetAge(String idNum, int age, boolean isIncludeAge) {
        int realYear = getRealYear(idNum);
        if (isIncludeAge) {
            if (realYear <= age) {
                return true;
            } else {
                return false;
            }
        } else {
            if (realYear < age) {
                return true;
            } else {
                return false;
            }
        }

    }


    /**
     * 判断是否  是  > 2002年出生的     就是符合18岁以下（含18岁）
     *
     * @param birthDay
     * @return
     */
    public static boolean isChildUnder18(String birthDay) {
        return isChildUnderTargetAge(birthDay, 18, true);
    }

    // 判断是否成年
    public static boolean isAdult(String birthday){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Integer.parseInt(birthday.substring(0, 4))+18, Integer.parseInt(birthday.substring(4, 6))-1, Integer.parseInt(birthday.substring(6)),23,59);
        return 0<=System.currentTimeMillis()-calendar.getTimeInMillis();
    }

}
