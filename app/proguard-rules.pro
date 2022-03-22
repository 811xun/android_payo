# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 设置混淆的压缩比率 0 ~ 7
    -optimizationpasses 5
    # 混淆后类名都为小写   Aa aA
    -dontusemixedcaseclassnames
    # 指定不去忽略非公共库的类
    -dontskipnonpubliclibraryclasses
    #不做预校验的操作
    -dontpreverify
    # 混淆时不记录日志
    -verbose
    # 混淆采用的算法.
    -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
    #保留代码行号，方便异常信息的追踪
    -keepattributes SourceFile,LineNumberTable
    #dump文件列出apk包内所有class的内部结构
    -dump class_files.txt
    #seeds.txt文件列出未混淆的类和成员
    -printseeds seeds.txt
    #usage.txt文件列出从apk中删除的代码
    -printusage unused.txt
    #mapping文件列出混淆前后的映射
    -printmapping mapping.txt
    #保护注解
    -keepattributes *Annotation*
#----------------------------------------------------------------------------

#---------------------------------默认保留区，避免混淆Android基本组件---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.annotation.**
-keep public class * extends android.support.v7.**

#不提示V4包下错误警告
-dontwarn android.support.v4.**
#保持下面的V4兼容包的类不被混淆
-keep class android.support.v4.**{*;}
#避免混淆所有native的方法,涉及到C、C++
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

#避免混淆枚举类
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#避免混淆自定义控件类的get/set方法和构造函数
#混淆保护自己项目的部分代码以及引用的第三方jar包library-end##################
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#避免混淆序列化类
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#不混淆Serializable和它的实现子类、其成员变量
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
#使用GSON、fastjson等框架时，所写的JSON对象类不混淆，否则无法将JSON解析成对应的对象
-keepclassmembers class * {
    public <init>(org.json.JSONObject);
}


#不混淆资源类
-keep class **.R$* {*;}

-keepclassmembers class * {
    void *(**On*Event);
}

#移除log
-assumenosideeffects class android.util.Log{
    public static int v(...);
    public static int i(...);
    public static int d(...);
    public static int w(...);
    public static int e(...);
}
#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature

#----------------------------------------------------------------------------



#---------------------------------2.第三方包-------------------------------
#SmartRefreshLayoutS 没有使用到：序列化、反序列化、JNI、反射，所以并不需要添加混淆过滤代码，并且已经混淆测试通过
#Picasso
-dontwarn com.squareup.okhttp.**

#eventBus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#jpinyin不需要


#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#okhttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

#gson
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

#newrelic
-keep class com.newrelic.** { *; }
-dontwarn com.newrelic.**
-keepattributes Exceptions, Signature, InnerClasses, LineNumberTable

#
-dontwarn com.lxj.xpopup.widget.**
-keep class com.lxj.xpopup.widget.**{*;}


#与js互相调用的类
-keepclasseswithmembers class com.demo.login.bean.ui.MainActivity$JSInterface {
      <methods>;
}

# 源文件和行号的信息不混淆
-keepattributes SourceFile,LineNumberTable

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#----------------------------------------------------------------------------

-keep public class **.*datas*.** {*;}
-keep public class **.*selfview*.** {*;}
-keep public class **.*tools*.** {*;}
-keep public class **.*mvp*.** {*;}
-keep public class **.*adapter*.** {*;}
-keep public class com.yzq.zxinglibrary.** {*;}
#-keep public class com.home.glx.uwallet.adapter.HomeProductAdapter.** {*;}

-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }

-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder
# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }

# bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#AF
#-dontwarn com.android.installreferrer
#-keep class com.appsflyer.** { *; }

#Braze
-keepnames class com.appboy.ui.** { *; }
-keepnames class com.appboy.** { *; }
-keepnames class bo.app.** { *; }

-dontwarn com.appboy.ui.**
-dontwarn com.google.firebase.messaging.**

-keepclassmembers class * {
   @android.webkit.JavascriptInterface <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}

-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception