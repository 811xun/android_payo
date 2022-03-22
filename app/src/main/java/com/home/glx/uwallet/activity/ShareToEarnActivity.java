package com.home.glx.uwallet.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.home.glx.uwallet.MainActivity;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.activity.xzc.promotion.PromotionListActivity;
import com.home.glx.uwallet.datas.ShareData;
import com.home.glx.uwallet.mvp.LoadShareMsg;
import com.home.glx.uwallet.selfview.DampingReboundNestedScrollView;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.HashMap;

public class ShareToEarnActivity extends MainActivity {
    private Context context;
    private DampingReboundNestedScrollView scrollView;
    private TextView code;
    private TextView share;
    private ImageView back;
    private TextView shareNext;
    private TextView rewardNext;
    //预计红包
    private String expect;
    //注册人数
    private String invitationToRegister;
    //消费人数
    private String inviteConsumption;
    //实际红包
    private String accumulate;

    //private LinearLayout mail, sms, facebook, messager, instagram, whatsApp, wechat, line;
    private String shareUrl = "Join Payo to get $10 off and enjoy pay later meals, why not? Use this code to get the deal: %s https://invite.payo.com.au?inviteCode=%s";

    @Override
    public int getLayout() {
        return R.layout.activity_share_and_earn;
    }

    @Override
    public void initView() {
        super.initView();
        context = this;
        scrollView = (DampingReboundNestedScrollView) findViewById(R.id.scroll_view);
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        code = (TextView) findViewById(R.id.code);
        share = (TextView) findViewById(R.id.share_now);
        back = (ImageView) findViewById(R.id.id_back);
        shareNext = (TextView) findViewById(R.id.check_referrals);
        rewardNext = (TextView) findViewById(R.id.enter_code);
        TextView title = findViewById(R.id.title);
        TextView earnMoney = findViewById(R.id.earn_money);
        TextView earnText = findViewById(R.id.earn_text);
        TextView shareText = findViewById(R.id.share_text);
        //切换字体
        TypefaceUtil.replaceFont(title,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(earnMoney,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(earnText,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(shareText,"fonts/gilroy_bold.ttf");
        TypefaceUtil.replaceFont(code,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(share,"fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(shareNext,"fonts/gilroy_medium.ttf");
        TypefaceUtil.replaceFont(rewardNext,"fonts/gilroy_medium.ttf");

        getShareData();

        shareNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareToEarnActivity.this, ShareNextActivity.class);
                intent.putExtra("url", shareUrl);
                startActivity(intent);
            }
        });
        rewardNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCode = new Intent(ShareToEarnActivity.this, PromotionListActivity.class);
                startActivity(intentCode);
//                Intent intent = new Intent(ShareToEarnActivity.this, PromotionCodeActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicTools.copyText(ShareToEarnActivity.this, code.getText().toString());
                /*Toast toast = new Toast(ShareToEarnActivity.this);
                TextView tv = new TextView(ShareToEarnActivity.this);
                tv.setText("Copied successfully");
                toast.setView(tv);*/
                Toast toast = Toast.makeText(ShareToEarnActivity.this, "", Toast.LENGTH_SHORT);
                toast.setText("Copied successfully");
                toast.show();
                //Toast.makeText(ShareToEarnActivity.this, "Copied successfully", Toast.LENGTH_SHORT).show();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localIntent = new Intent("android.intent.action.SEND");
                localIntent.setType("text/plain");
                localIntent.putExtra("android.intent.extra.SUBJECT", "Payo");
                localIntent.putExtra("android.intent.extra.TEXT", shareUrl);
                startActivity(Intent.createChooser(localIntent,"Share & Earn PAYO"));
                //showDialog(context);
            }
        });
    }

    private void getShareData() {
        LoadShareMsg loadShareMsg = new LoadShareMsg(this);
        loadShareMsg.setOnData(new LoadShareMsg.OnShareMsg() {
            @Override
            public void msg(ShareData data) {
                code.setText(data.getInviteCode());
                //shareUrl = String.format(shareUrl, data.getInviteCode(), data.getInviteCode());
                shareUrl = data.getUrl();
                expect = data.getExpect();
                accumulate = data.getAccumulate();
                invitationToRegister = data.getInvitationToRegister();
                inviteConsumption = data.getInviteConsumption();
            }
        });
        loadShareMsg.load(new HashMap<String, Object>());
    }

    private  void showDialog(final Context context) {

        // 提示对话框
        final Dialog dialog = new Dialog(context, R.style.dialog_style);
        View view = View.inflate(context,
                R.layout.dialog_share, null);
        dialog.setCancelable(true);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(layoutParams);

        //whatsapp
        view.findViewById(R.id.share_whatsapp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isContainPackName(context,"com.whatsapp")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);//shareText 为需要分享的内容
                    sendIntent.setType("text/plain");
                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendIntent.setPackage("com.whatsapp");//packageName 为需要分享到的包名
                    try {
                        startActivity(sendIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context,"Whatsapp is not installed on this device",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context,"Whatsapp is not installed on this device",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //邮箱
        view.findViewById(R.id.fb_share_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isContainPackName(context,"com.android.email")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);//shareText 为需要分享的内容
                    sendIntent.setType("text/plain");
                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendIntent.setPackage("com.android.email");//packageName 为需要分享到的包名
                    try {
                        startActivity(sendIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context,"Mail is not installed on this device",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context,"Mail is not installed on this device",Toast.LENGTH_SHORT).show();
                }
                /*dialog.dismiss();
                Intent localIntent = new Intent("android.intent.action.SEND");
                localIntent.setType("text/plain");
                localIntent.putExtra("android.intent.extra.SUBJECT", "nigeriapro分享");
                localIntent.putExtra("android.intent.extra.TEXT", shareUrl);
                startActivity(Intent.createChooser(localIntent,"分享"));*/
            }
        });

        //短信
        view.findViewById(R.id.fb_share_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isContainPackName(context,"com.android.mms")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);//shareText 为需要分享的内容
                    sendIntent.setType("text/plain");
                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendIntent.setPackage("com.android.mms");//packageName 为需要分享到的包名
                    try {
                        startActivity(sendIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context,"SMS is not installed on this device",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context,"SMS is not installed on this device",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Facebook
        view.findViewById(R.id.fb_share_fb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isContainPackName(context,"com.facebook.katana")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);//shareText 为需要分享的内容
                    sendIntent.setType("text/plain");
                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendIntent.setPackage("com.facebook.katana");//packageName 为需要分享到的包名
                    try {
                        startActivity(sendIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context,"Facebook is not installed on this device",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context,"Facebook is not installed on this device",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Messager
        view.findViewById(R.id.share_messger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isContainPackName(context,"com.facebook.orca")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);//shareText 为需要分享的内容
                    sendIntent.setType("text/plain");
                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendIntent.setPackage("com.facebook.orca");//packageName 为需要分享到的包名
                    try {
                        startActivity(sendIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context,"Messager is not installed on this device",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context,"Messager is not installed on this device",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Instagram
        view.findViewById(R.id.share_ins).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isContainPackName(context,"com.instagram.android")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);//shareText 为需要分享的内容
                    sendIntent.setType("text/plain");
                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendIntent.setPackage("com.instagram.android");//packageName 为需要分享到的包名
                    try {
                        startActivity(sendIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context,"Instagram is not installed on this device",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context,"Instagram is not installed on this device",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Wechat
        view.findViewById(R.id.share_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isContainPackName(context,"com.tencent.mm")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);//shareText 为需要分享的内容
                    sendIntent.setType("text/plain");
                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendIntent.setPackage("com.tencent.mm");//packageName 为需要分享到的包名
                    try {
                        startActivity(sendIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context,"Wechat is not installed on this device",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context,"Wechat is not installed on this device",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Line
        view.findViewById(R.id.share_line).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (isContainPackName(context,"jp.naver.line.android")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shareUrl);//shareText 为需要分享的内容
                    sendIntent.setType("text/plain");
                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendIntent.setPackage("jp.naver.line.android");//packageName 为需要分享到的包名
                    try {
                        startActivity(sendIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context,"Line is not installed on this device",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context,"Line is not installed on this device",Toast.LENGTH_SHORT).show();
                }
            }
        });

      /*  //复制链接
        view.findViewById(R.id.fb_share_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", shareUrl);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                Toast.makeText(context,"Copied",Toast.LENGTH_SHORT).show();
            }
        });*/
        dialog.show();
    }


    public synchronized boolean isContainPackName(final Context mContext , String packName) {
        boolean isContainPack = false;
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(packName , PackageManager.GET_ACTIVITIES);
            if(info != null){
                isContainPack = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return isContainPack;
    }
}
