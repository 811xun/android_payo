package com.home.glx.uwallet.push;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.SharePreferenceUtils;

/**
 * 获取谷歌推送token
 */
/*
public class GetPushToken {

    private Context context;

    public GetPushToken(Context context) {
        this.context = context;
    }

    public void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Google Push Token:", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        L.log("tttttttttttttt:" + token);

                        SharePreferenceUtils app_sharePreferenceUtils =
                                new SharePreferenceUtils(context, StaticParament.APP);
                        app_sharePreferenceUtils.put(StaticParament.PUSH_TOKEN, token);
                        app_sharePreferenceUtils.save();

                    }
                });
    }

}
*/
