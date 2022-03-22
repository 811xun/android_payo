package com.home.glx.uwallet.tools;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Create with Android Studio
 * Author : SUNHUI
 * DateTime : 2019/8/8 17:15
 * email : 951468815@qq.com
 * tag : 加减乘除工具类
 * 由于Java的简单类型不能够精确的对浮点数进行运算，这个工具类提供精确的浮点数运算，包括加减乘除和四舍五入。
 */

public class CalcTool {

    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    //这个类不能实例化
    private CalcTool() {
    }

    /**
     * 提供精确的加法运算。
     *
     * @return 两个参数的和
     * @paramv1 被加数
     * @paramv2 加数
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static String add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).toString();
    }

    /**
     * 提供精确的减法运算。
     *
     * @return 两个参数的差
     * @paramv1 减数
     * @paramv2 被减数
     */

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @return 两个参数的差
     * @paramv1 减数
     * @paramv2 被减数
     */

    public static String sub(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.subtract(b2).toString();
    }


    /**
     * 提供精确的乘法运算。
     *
     * @return 两个参数的积
     * @paramv1 被乘数
     * @paramv2 乘数
     */

    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
//        return b1.multiply(b2).doubleValue();
    }

    /**
     * 商业中的乘法
     * @param f1
     * @param f2
     * @return
     */
/*    public static float mm(float f1, float f2) {
        BigDecimal bigDecimal1 = new BigDecimal(Float.toString(f1));
        BigDecimal bigDecimal2 = new BigDecimal(Float.toString(f2));
        BigDecimal result = bigDecimal1.multiply(bigDecimal2);
        return result.floatValue();
    }*/
    //确保精度准确，使用string运算
    public static String mmToString(String f1, String f2) {
        BigDecimal bigDecimal1 = new BigDecimal(f1);
        BigDecimal bigDecimal2 = new BigDecimal(f2);
        BigDecimal result = bigDecimal1.multiply(bigDecimal2);
        return result.toString();
    }

    public static float mm(String f1, String f2) {
        BigDecimal bigDecimal1 = new BigDecimal(f1);
        BigDecimal bigDecimal2 = new BigDecimal(f2);
        BigDecimal result = bigDecimal1.multiply(bigDecimal2);
        return result.floatValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @return 两个参数的商
     * @paramv1 被除数
     * @paramv2 除数
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }


    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @return 两个参数的商
     * @paramv1 被除数
     * @paramv2 除数
     * @paramscale 表示表示需要精确到小数点以后几位。
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "Thescale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @return 四舍五入后的结果
     * @paramv 需要四舍五入的数字
     * @paramscale 小数点后保留几位
     */
/*    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "Thescale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }*/

    //确保精度准确，使用string运算
    public static double round(String v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "Thescale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(v);
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    //确保精度准确，使用string运算
    public static String rounded(String v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "Thescale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(v);
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String big2(String d) {
        return formatToNumber(new BigDecimal(d));
    }

    public static String big2(double d) {
        return formatToNumber(new BigDecimal(d + ""));
//        BigDecimal d1 = new BigDecimal(Double.toString(d));
//        BigDecimal d2 = new BigDecimal(Integer.toString(1));
        // 四舍五入,保留2位小数
//        return d1.divide(d2, 2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * @return
     * @desc 1.0~1之间的BigDecimal小数，格式化后失去前面的0,则前面直接加上0。
     * 2.传入的参数等于0，则直接返回字符串"0.00"
     * 3.大于1的小数，直接格式化返回字符串
     */
    public static String formatToNumber(BigDecimal obj) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (obj.compareTo(BigDecimal.ZERO) == 0) {
            return "0.00";
        } else if (obj.compareTo(BigDecimal.ZERO) > 0 && obj.compareTo(new BigDecimal(1)) < 0) {
            return "0" + df.format(obj).toString();
        } else {
            return df.format(obj).toString();
        }
    }
}