package com.home.glx.uwallet.tools;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * 系统相关
 */
public class SystemUtils {

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage(String s) {
        L.log("当前语言：" + Locale.getDefault().getLanguage());
        return Locale.getDefault().getLanguage();
    }


    public static String getSysLangusge(Context context) {
        if (context == null) {
            return "en";
        }
        //获取 Locale  对象的正确姿势：
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        //获取语言的正确姿势：
        String lang = locale.getLanguage() + "-" + locale.getCountry();
        if (lang.substring(0, 2).equals("zh") || lang.equals("zh-CN")) {
            lang = "zh";
        } else {
            lang = "en";
        }

        return lang;
    }

    public static String getSysLangusge2(Context context) {
        //获取 Locale  对象的正确姿势：
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        //获取语言的正确姿势：
        String lang = locale.getLanguage() + "-" + locale.getCountry();
//        if (lang.equals("zh-CN")) {
//            lang = "zh";
//        }
        return lang;
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    public static String getIMEI(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(TELEPHONY_SERVICE);
        if (tm != null) {
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                return null;
            }
            return tm.getDeviceId();
        }
        return null;
    }


    /**
     * 获取android当前可用内存大小
     */
    private String getAvailMemory(Context ctx) {// 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(ctx, mi.availMem);// 将获取的内存大小规格化
    }

    /**
     * 获取手机内存总大小
     *
     * @return
     */
    public static String getTotalMemorySize() {
        try {
            FileReader fr = new FileReader("/proc/meminfo");
            BufferedReader br = new BufferedReader(fr, 2048);
            //            String memoryLine = br.readLine();
            String subMemoryLine = "";
            String Line = "";
            while ((Line = br.readLine()) != null) {
                if (Line.contains("MemTotal:")) {
                    subMemoryLine = Line.substring(Line.indexOf("MemTotal:"));
                    break;
                }

            }
            br.close();
            Matcher mer = Pattern.compile("^[0-9]+$").matcher(subMemoryLine.replaceAll("\\D+", ""));
            //如果为正整数就说明数据正确的，确保在Double.parseDouble中不会异常
            if (mer.find()) {
                long memSize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
                double mem = (Double.parseDouble(memSize + "") / 1024) / 1024;
                NumberFormat nf = new DecimalFormat("0.0 ");
                mem = Double.parseDouble(nf.format(mem));
                //Log.e(LOG_TAG,"=========mem================ " + mem);
                return String.valueOf(mem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unavailable";
    }


    //获取总内存的大小
    public static String getTotalMemory(Context context) {
        //        MemTotal:         341780 kB
        try {
            FileInputStream fis = new FileInputStream(new File("/proc/meminfo"));
            //包装一个一行行读取的流
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
            //取到所有的内存信息
            String memTotal = bufferedReader.readLine();
            StringBuffer sb = new StringBuffer();
            for (char c : memTotal.toCharArray()) {
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                }
            }
            //为了方便格式化 所以乘以1024
            long totalMemory = Long.parseLong(sb.toString());
            return totalMemory / 1000F / 1000F + "";
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }


    public static float getRomMemroy() {
        long[] romInfo = new long[2];
        romInfo[0] = getTotalInternalMemorySize();
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        romInfo[1] = blockSize * availableBlocks;
        float n = Float.parseFloat(romInfo[0] + "") / 1000F / 1000F / 1000F;
        return n;
    }

    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }


    /**
     * 获取手机IMEI号
     */
    public static String getIMEI2(Context context, String gaId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            if (!TextUtils.isEmpty(gaId)) {
                return gaId;
            } else {
                return getAndroidId(context);
            }
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (!TextUtils.isEmpty(gaId)) {
                    return gaId;
                } else {
                    return getAndroidId(context);
                }
            }
            String imei = null;
            try {
                imei = telephonyManager.getDeviceId();
            } catch (Exception e) {
                imei = "";
            }
            if (imei == null || imei.equals("")) {
                if (!TextUtils.isEmpty(gaId)) {
                    return gaId;
                } else {
                    return getAndroidId(context);
                }
            }
            return imei;
        }

    }

    public static String getAndroidId(Context context) {
        String deviceId = "";
        if (!TextUtils.isEmpty(Settings.System.getString(
                context.getContentResolver(), Settings.Secure.ANDROID_ID))) {
            deviceId = Settings.System.getString(
                    context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }

    /**
     * 查询手机所有app信息
     *
     * @param context
     * @return
     */
    public static ArrayList<HashMap<String, Object>> getItems(Context context) {
        PackageManager pckMan = context.getPackageManager();
        ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
        List<PackageInfo> packageInfo = pckMan.getInstalledPackages(0);
        for (PackageInfo pInfo : packageInfo) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            if (pInfo.applicationInfo != null) {
                item.put("appimage", pInfo.applicationInfo.loadIcon(pckMan));
                item.put("appName", pInfo.applicationInfo.loadLabel(pckMan).toString());
            }
            item.put("packageName", pInfo.packageName);
            item.put("versionCode", pInfo.versionCode);
            item.put("versionName", pInfo.versionName);
            items.add(item);
        }
        return items;
    }

    /**
     * 通过卡槽位置拿 IMEI
     *
     * @param slotId (0, 1卡槽位置）
     * @return
     */
    public static String getImei(Context context, int slotId) {
        if (slotId != 0 && slotId != 1) {
            return null;
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            return "";
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return tm.getDeviceId(slotId);

        } else if (slotId == 0) {
            return tm.getDeviceId();

        } else {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);// 取得相关系统服务
            Class<?> telephonyManagerClass = null;
            String imei = null;

            try {
                telephonyManagerClass = Class.forName("android.telephony.TelephonyManager");
                Method method = telephonyManagerClass.getMethod("getImei", int.class);
                imei = (String) method.invoke(telephonyManager, slotId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return imei;
        }
    }


    /**
     * 获取手机IMSI
     */
    public static String getIMSI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                //没有权限
                L.log("没有权限");
                return "";
            }
            String imsi = telephonyManager.getSubscriberId();
            if (null == imsi) {
                imsi = "";
            }
            return imsi;
        } catch (Exception e) {
            e.printStackTrace();
            L.log("IMSI获取失败");
            return "";
        }
    }


    /**
     * .获取手机MAC地址
     * 只有手机开启wifi才能获取到mac地址
     */
    private String getMacAddress(Context context) {
        String result = "";
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        result = wifiInfo.getMacAddress();
        Log.i("text", "手机macAdd:" + result);
        return result;
    }

    /**
     * 手机CPU信息
     */
    private String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};  //1-cpu型号  //2-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        Log.i("text", "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
        return cpuInfo;
    }


    // 判断一个字符串是否含有数字
    public static boolean HasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }


    /**
     * 检测是否root
     * modify by hiyi 20180928，移除雁联SDK的root检测，重新开发
     *
     * @param
     * @return
     */
    public static boolean isRoot() {
        String[] paths = {
                "/system/xbin/su",
                "/system/bin/su",
                "/system/sbin/su",
                "/sbin/su",
                "/vendor/bin/su",
                "/su/bin/su"
        };
        try {
            for (String path : paths) {
                if (new File(path).exists()) {
                    String execResult = exec(new String[]{"ls", "-l", path});
                    //形如(rooted)：-rwxr-xr-x root     root        75348 1970-01-01 08:32 su
                    if (TextUtils.isEmpty(execResult)
                            || execResult.indexOf("root") == execResult.lastIndexOf("root")) {
                        return false;
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String exec(String[] exec) {
        if (exec == null || exec.length <= 0) {
            return null;
        }
        StringBuilder ret = new StringBuilder();
        ProcessBuilder processBuilder = new ProcessBuilder(exec);
        try {
            Process process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                ret.append(line);
            }
            process.getInputStream().close();
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
