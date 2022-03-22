package com.home.glx.uwallet.push;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.home.glx.uwallet.BaseActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.fragment.MainTab;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    /**
     * 监控令牌的生成
     *
     * @param s
     */
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        L.log("谷歌token:" + s);
        SharePreferenceUtils app_sharePreferenceUtils =
                new SharePreferenceUtils(this, StaticParament.APP);
        app_sharePreferenceUtils.put(StaticParament.PUSH_TOKEN, s);
        app_sharePreferenceUtils.save();
//        sendRegistrationToServer(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //如果app在后台，只有data类型消息才会走进这个方法；notification类型消息不会走进来
        //如果app在前台则都会进该方法
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null && remoteMessage.getNotification().getBody() != null) {
            //sendNotification(getApplicationContext(), remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(getApplicationContext(), StaticParament.USER);
            String token = (String) sharePreferenceUtils.get(StaticParament.USER_TOKEN, "");
            if (!token.equals("")) {
                createNotification(getApplicationContext(), remoteMessage.getData().get("route"), remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }
        } else {
            sendNotification(getApplicationContext(), remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }

    public void createNotification(Context mContext, String route, String messageTitle, String messageBody) {
        L.log("接收到了消息，route:" + route);
        //route 1:还款页面 2：主页面
        /**
         * 通知栏（兼容android 8.0以上）
         */
        boolean isVibrate = true;//是否震动
        //1.获取消息服务
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        //默认通道是default
        String channelId = "default";
        //2.如果是android8.0以上的系统，则新建一个消息通道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelId = "chat";
            /*
             通道优先级别：
             * IMPORTANCE_NONE 关闭通知
             * IMPORTANCE_MIN 开启通知，不会弹出，但没有提示音，状态栏中无显示
             * IMPORTANCE_LOW 开启通知，不会弹出，不发出提示音，状态栏中显示
             * IMPORTANCE_DEFAULT 开启通知，不会弹出，发出提示音，状态栏中显示
             * IMPORTANCE_HIGH 开启通知，会弹出，发出提示音，状态栏中显示
             */
            NotificationChannel channel = new NotificationChannel(channelId, "Message notification", NotificationManager.IMPORTANCE_HIGH);
            //设置该通道的描述（可以不写）
            //channel.setDescription("重要消息，请不要关闭这个通知。");
            //是否绕过勿打扰模式
            channel.setBypassDnd(true);
            //是否允许呼吸灯闪烁
            channel.enableLights(true);
            //闪关灯的灯光颜色
            channel.setLightColor(Color.RED);
            //桌面launcher的消息角标
            channel.canShowBadge();
            //设置是否应在锁定屏幕上显示此频道的通知
            //channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            if (isVibrate) {
                //是否允许震动
                channel.enableVibration(true);
                //先震动1秒，然后停止0.5秒，再震动2秒则可设置数组为：new long[]{1000, 500, 2000}
                channel.setVibrationPattern(new long[]{1000, 500, 2000});
            } else {
                channel.enableVibration(false);
                channel.setVibrationPattern(new long[]{0});
            }
            //创建消息通道
            manager.createNotificationChannel(channel);
        }
        //3.实例化通知
        NotificationCompat.Builder nc = new NotificationCompat.Builder(mContext, channelId);
        //通知默认的声音 震动 呼吸灯
        nc.setDefaults(NotificationCompat.DEFAULT_ALL);
        //通知标题
        nc.setContentTitle(messageTitle);
        //nc.setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(messageTitle));
        //通知内容
        //nc.setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));
        nc.setContentText(messageBody);
        //设置通知的小图标
        nc.setSmallIcon(R.mipmap.logo);
        //设置通知的大图标
        nc.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.logo));
        //设定通知显示的时间
        nc.setWhen(System.currentTimeMillis());
        //设置通知的优先级
        nc.setPriority(NotificationCompat.PRIORITY_MAX);
        //设置点击通知之后通知是否消失
        nc.setAutoCancel(true);
        //点击通知打开软件
        //Context application = getApplicationContext();
        Intent resultIntent = null;
        if (route != null && route.equals("1")) {
            resultIntent = new Intent(mContext, MainTab.class);
            resultIntent.putExtra("num", 2);
        } else {
            resultIntent = new Intent(mContext, BaseActivity.class);
        }
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        //CLEAR_TOP:将栈内NewHomeActivity上的栈都销毁；
        //SINGLE_TOP：不想从头再创建一个新的NewHomeActivity，而是复用栈内的NewHomeActivity。
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = null;
        pendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, 0);

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
//            pendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);
//        } else {
//            pendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);
//        }

        nc.setContentIntent(pendingIntent);
        //4.创建通知，得到build
        Notification notification = nc.build();
        //5.发送通知
        manager.notify(1, notification);
    }

    private void sendNotification(Context iContext, String messageTitle, String messageBody) {
        L.log("接收到了消息");
        Intent intent = new Intent(this, MainTab.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = null;
        pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, 0);

//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
//            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_IMMUTABLE);
//        } else {
//            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
//        }

        //渠道通知ID
        String channelId = "-1";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle(messageTitle)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
