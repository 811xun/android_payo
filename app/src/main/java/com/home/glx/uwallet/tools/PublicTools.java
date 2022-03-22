package com.home.glx.uwallet.tools;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.core.content.FileProvider;

import com.home.glx.uwallet.request.StaticParament;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

public class PublicTools {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void copyText(Context context, String text) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
    }

    /**
     * 修改dialog大小
     */
    public static void setDialogSize(Context context, Dialog dialog) {
        DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
        int width = dm2.widthPixels;
        int height = dm2.heightPixels;
        if (width == 0) {
            width = 720;
        }

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = width / 10 * 9;
        dialog.getWindow().setAttributes(params);
    }

    /**
     * 修改dialog大小
     */
    public static void setTipDialogSize(Context context, Dialog dialog) {
        DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
        int width = dm2.widthPixels;
        int height = dm2.heightPixels;
        if (width == 0) {
            width = 720;
        }

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = width / 10 * 8;
        dialog.getWindow().setAttributes(params);
    }

    /**
     * 修改dialog大小
     */
    public static void setTipDialogSize2(Context context, Dialog dialog) {
        DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
        int width = dm2.widthPixels;
        int height = dm2.heightPixels;
        if (width == 0) {
            width = 720;
        }

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = width / 97 * 73;
        dialog.getWindow().setAttributes(params);
    }

    /**
     * 修改dialog大小
     */
    public static void setDialogSize(Context context, Dialog dialog, float proportion) {
        DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
        int width = dm2.widthPixels;
        int height = dm2.heightPixels;
        if (width == 0) {
            width = 720;
        }

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (width * proportion);
        dialog.getWindow().setAttributes(params);
    }

    /**
     * 修改dialog大小
     */
    public static void setDialogSizeFill(Context context, Dialog dialog) {
        DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
        int width = dm2.widthPixels;
        int height = dm2.heightPixels;
        if (width == 0) {
            width = 720;
        }

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = width;
        params.height = height;
        dialog.getWindow().setAttributes(params);
    }


    /**
     * 自动关闭软键盘
     *
     * @param activity
     */
    public static void closeKeybord(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 自动关闭软键盘
     *
     * @param view
     */
    public static void openKeybord(final EditText view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() { //让软键盘延时弹出，以更好的加载Activity
            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager) view.getContext().
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(view, 0);
            }
        }, 500);
    }

    /**
     * 判断邮箱是否合法
     *
     * @return
     */
    public static boolean isEmail(String email) {
        //包括下划线，相当于"[A-Za-z0-9_]"
//        String pattern1 = "^\\w+@(\\w+\\.){1,2}\\w+$";
//        return Pattern.matches(pattern1, email);
        //包括下划线，相当于"[A-Za-z0-9_]"

        /*String pattern1 = "^[\\w\\.]+@(\\w+\\.){1,2}\\w+$";
        return Pattern.matches(pattern1, email);*/

        //外国邮箱输入-时上面的规则匹配失败
        /*String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(email);
        if (m.matches())
            return true;
        else
            return false;*/

        //外国邮箱输入_时上面的规则匹配失败
        if (email.contains("@")) {
            return true;
        } else {
            return false;
        }
    }


    /*时间戳转换成字符窜*/
    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String dateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyy");
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String dateToString3(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyy");
        return sf.format(d);
    }

    //HH:mm:ss
    /*时间戳转换成字符窜*/
    public static String dateToString2(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm dd-MM-yyy");
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String ddMM(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM");
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String yymm(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String yyyy(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String MM(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("MM");
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String dd(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("dd");
        return sf.format(d);
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp2(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将时间转换为时间戳
     * 2020-02-26 11:04:48
     */
    public static String dateToStamp5(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }


    /**
     * 压缩图片
     */
    public static Bitmap YaSuoBitmap(Bitmap bitmap, int size) {
        Bitmap bmp = bitmap;
        //图片大小上限kb
        size = 220;

        // 首先进行一次大范围的压缩
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
        float zoom = (float) Math.sqrt(size * 1024 / (float) output.toByteArray().length); //获取缩放比例

        // 设置矩阵数据
        Matrix matrix = new Matrix();
        matrix.setScale(zoom, zoom);

        // 根据矩阵数据进行新bitmap的创建
        Bitmap resultBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        output.reset();
        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

        // 如果进行了上面的压缩后，依旧大于1000K，就进行小范围的微调压缩
        while (output.toByteArray().length > size * 1024) {
            matrix.setScale(0.8f, 0.8f);//每次缩小 1/10

            resultBitmap = Bitmap.createBitmap(resultBitmap, 0, 0,
                    resultBitmap.getWidth(), resultBitmap.getHeight(), matrix, true);

            output.reset();
            resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        }

        return resultBitmap;
    }

    /**
     * 图片转成string(Base64)
     *
     * @param bitmap
     * @return
     */
    public static String BitmapToString(Bitmap bitmap) {

        if (bitmap == null) {
            return "";
        }

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "data:image/jpeg;base64," + result;
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

    /**
     * 保留两位小数
     *
     * @param f
     * @return
     */
    public static double baoliuliangwei(String f) {
        double ff = Double.parseDouble(f);
        BigDecimal bg = new BigDecimal(ff);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }


    public static String oneend(double f) {
        if (f == 0) {
            return "0.0";
        }
        if (f > 1) {
            return getCommaFormatOne((float) f);
        }
        String f1 = String.format("%.1f", f);
        return f1 + "";
    }


    public static String twoend(double f) {
//        if (f == 0) {
//            return "0.00";
//        }
        if (f == 0) {
            return "0.0";
        }
//        if (f > 1) {
//            return getCommaFormat((float) f);
//        }
        String f1 = String.format("%.2f", f);
        return f1 + "";
    }

    public static String twoend(String num) {
        if (num == null) {
            return "0.00";
        }
        num = num.replace(",", "");
        try {
            if (Float.parseFloat(num) == 0) {
                return "0.00";
            }
        } catch (Exception e) {
            L.log("转换错误：" + num);
            return "0.00";
        }

        double f = Double.parseDouble(num);
        return twoend(f);
    }

    //每3位中间添加逗号的格式化显示
    public static String getCommaFormat(String value) {
        value = value.replace(",", "");
        return getFormat(",###.00", new BigDecimal(value));
    }

    //每3位中间添加逗号的格式化显示
    public static String getCommaFormat(Float value) {
        return getFormat(",###.00", new BigDecimal(value));
    }

    //每3位中间添加逗号的格式化显示
    public static String getCommaFormatOne(Float value) {
        return getFormat(",###.0", new BigDecimal(value));
    }

    //每3位中间添加逗号的格式化显示
    public static String getCommaFormat2(String value) {
        return getFormat(",###", new BigDecimal(value));
    }

    //自定义数字格式方法
    public static String getFormat(String style, BigDecimal value) {
        DecimalFormat df = new DecimalFormat();
        df.applyPattern(style);// 将格式应用于格式化器
        return df.format(value.doubleValue());
    }


    // 判断一个字符是否是中文
    public static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }

    // 判断一个字符串是否含有中文
    public static boolean isChinese(String str) {
        if (str == null)
            return false;
        for (char c : str.toCharArray()) {
            if (isChinese(c))
                return true;// 有一个中文字符就返回
        }
        return false;
    }

    // 判断一个字符串是否含有英文
    public static boolean isEnglish(String str) {
        if (str == null)
            return true;
        for (char c : str.toCharArray()) {
            if (isChinese(c))
                return false;// 有一个中文字符就返回
        }
        return true;
    }


    /**
     * 获取银行logo地址
     *
     * @param bankName
     * @param type     0 、 大图  1、小图
     * @return
     */
    public static String getBankLogoUrl(String bankName, String type) {
        //http://paytest-api.loancloud.cn/app/getBankLogo?bankName=AMP&type=1
        return StaticParament.zhifu + "/app/getBankLogo?bankName=" + bankName + "&type=" + type;
    }


    /*
     * 判断是否为整数（纯数字）
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否全是字母
     *
     * @param str
     * @return
     */
    public static boolean isLetter(String str) {
        //判断字符dao串是否du全为英zhi文dao字母，是则专返属回true
        boolean isWord = str.matches("[a-zA-Z]+");
        return isWord;
    }

    // 两次点击按钮之间的点击间隔不能少于800毫秒
    private static final int MIN_CLICK_DELAY_TIME = 800;
    private static long lastClickTime;

    public static void setLastClickTime() {
        lastClickTime = 0;
    }

    public static boolean isFastClick() {
        boolean flag = false;
        Log.i("lastClickTime: ", String.valueOf(lastClickTime));
        long curClickTime = System.currentTimeMillis();
        Log.i("curClickTime: ", String.valueOf(curClickTime));
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    //验证各种导航地图是否安装
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 保存Bitmap到本地
     *
     * @param bitmap
     * @param _file
     * @throws IOException
     */
    public static File saveBitmapToFile(Context context, Bitmap bitmap, String _file, Handler handler) throws IOException {
        BufferedOutputStream os = null;
        File file = new File(_file);
        try {
            int end = _file.lastIndexOf(File.separator);
            String _filePath = _file.substring(0, end);
            File filePath = new File(_filePath);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            file.createNewFile();
            FileOutputStream fileOutputStream=   new FileOutputStream(file);
            os = new BufferedOutputStream(fileOutputStream);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);


        } finally {
            if (os != null) {
                try {
                    os.close();
                    updatePhotoMedia(file, context);
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                    return file;
                } catch (IOException e) {
                    e.fillInStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 截图
     */
    public static Bitmap screenShot(Window window) {
        View dView = window.getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
        return bitmap;
        /*if (bitmap != null) {
            try {
                // 获取内置SD卡路径
                String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                // 图片文件路径
                String fileImg = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/nbinpic";
                String filePath = System.currentTimeMillis() + ".png";
                File folder = new File(fileImg);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
                File file = new File(fileImg, filePath);
                if (!file.exists()) {
                    file.createNewFile();
                }
                *//*File file = new File("data/data/" + BuildConfig.APPLICATION_ID + "/caches.png");
                if (!file.exists()) {
                    file.createNewFile();
                }*//*
                FileOutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG,
                        100, os);
                os.flush();
                os.close();
                Log.d("a7888", "存储完成");
                return file;
            } catch (Exception e) {
                return null;
            }
        }
        return null;*/
    }

    //更新图库
    private static void updatePhotoMedia(File file, Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        context.sendBroadcast(intent);
    }

    /**
     * 获取本地文件的uri
     *
     * @param file
     * @return
     */
    public static Uri getUriFromFile(Context context, File file) {
        Uri imageUri = null;
        if (file != null && file.exists() && file.isFile()) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                imageUri = Uri.fromFile(file);
            } else {
                /**
                 * 7.0 调用系统相机拍照不再允许使用Uri方式，应该替换为FileProvider
                 * 并且这样可以解决MIUI系统上拍照返回size为0的情况
                 */
                imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
//                context.grantUriPermission("",
//                        imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            }
        }
        return imageUri;
    }

    public static String getEnglishMonth(int month) {
        if (month == 1) {
            return "January";
        } else if (month == 2) {
            return "February";
        } else if (month == 3) {
            return "March";
        } else if (month == 4) {
            return "April";
        } else if (month == 5) {
            return "May";
        } else if (month == 6) {
            return "June";
        } else if (month == 7) {
            return "July";
        } else if (month == 8) {
            return "August";
        } else if (month == 9) {
            return "September";
        } else if (month == 10) {
            return "October";
        } else if (month == 11) {
            return "November";
        } else if (month == 12) {
            return "December";
        } else {
            return "";
        }
    }

}
