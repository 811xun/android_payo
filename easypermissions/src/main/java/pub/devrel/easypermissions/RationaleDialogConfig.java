package pub.devrel.easypermissions;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;

import java.lang.reflect.Field;

import static pub.devrel.easypermissions.AppSettingsDialog.replaceFont;

/**
 * Configuration for either {@link RationaleDialogFragment} or {@link RationaleDialogFragmentCompat}.
 */
class RationaleDialogConfig {

    private static final String KEY_POSITIVE_BUTTON = "positiveButton";
    private static final String KEY_NEGATIVE_BUTTON = "negativeButton";
    private static final String KEY_RATIONALE_MESSAGE = "rationaleMsg";
    private static final String KEY_THEME = "theme";
    private static final String KEY_REQUEST_CODE = "requestCode";
    private static final String KEY_PERMISSIONS = "permissions";

    String positiveButton;
    String negativeButton;
    int theme;
    int requestCode;
    String rationaleMsg;
    String[] permissions;

    RationaleDialogConfig(@NonNull String positiveButton,
                          @NonNull String negativeButton,
                          @NonNull String rationaleMsg,
                          @StyleRes int theme,
                          int requestCode,
                          @NonNull String[] permissions) {

        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;
        this.rationaleMsg = rationaleMsg;
        this.theme = theme;
        this.requestCode = requestCode;
        this.permissions = permissions;
    }

    RationaleDialogConfig(Bundle bundle) {
        positiveButton = bundle.getString(KEY_POSITIVE_BUTTON);
        negativeButton = bundle.getString(KEY_NEGATIVE_BUTTON);
        rationaleMsg = bundle.getString(KEY_RATIONALE_MESSAGE);
        theme = bundle.getInt(KEY_THEME);
        requestCode = bundle.getInt(KEY_REQUEST_CODE);
        permissions = bundle.getStringArray(KEY_PERMISSIONS);
    }

    Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_POSITIVE_BUTTON, positiveButton);
        bundle.putString(KEY_NEGATIVE_BUTTON, negativeButton);
        bundle.putString(KEY_RATIONALE_MESSAGE, rationaleMsg);
        bundle.putInt(KEY_THEME, theme);
        bundle.putInt(KEY_REQUEST_CODE, requestCode);
        bundle.putStringArray(KEY_PERMISSIONS, permissions);

        return bundle;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    AlertDialog createSupportDialog(Context context, Dialog.OnClickListener listener) {
        AlertDialog.Builder builder;
        if (theme > 0) {
            builder = new AlertDialog.Builder(context, theme);
        } else {
            builder = new AlertDialog.Builder(context);
        }

        AlertDialog alertDialog = builder
                .setCancelable(false)
                .setMessage(rationaleMsg)
                .setPositiveButton(positiveButton, listener)
                .setNegativeButton(negativeButton, listener)
                .show();
//        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
//        Log.d("sssss", "createSupportDialog: " + dip2px(context, 300));
//        params.width = dip2px(context, 300);
//        alertDialog.getWindow().setAttributes(params);
        try {
            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object alertController = mAlert.get(alertDialog);
            alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.view_yj_dialog1);

            Field mMessageView = alertController.getClass().getDeclaredField("mMessageView");
            mMessageView.setAccessible(true);
            TextView titleMesg = (TextView) mMessageView.get(alertController);
//            titleMesg.setPadding(40, 0, 0, 0);
            titleMesg.setTextColor(Color.parseColor("#000000"));
            titleMesg.setTextSize(15);
//            titleMesg.setWidth(dip2px(context, 300));

            titleMesg.setGravity(View.TEXT_ALIGNMENT_CENTER);
            replaceFont(titleMesg, "fonts/gilroy_medium.ttf");

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FD7441"));
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setAllCaps(false);
        replaceFont(alertDialog.getButton(DialogInterface.BUTTON_POSITIVE), "fonts/gilroy_medium.ttf");

        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#717171"));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setAllCaps(false);
        replaceFont(alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE), "fonts/gilroy_medium.ttf");
        return alertDialog;
    }

    android.app.AlertDialog createFrameworkDialog(Context context, Dialog.OnClickListener listener) {
        android.app.AlertDialog.Builder builder;
        if (theme > 0) {
            builder = new android.app.AlertDialog.Builder(context, theme);
        } else {
            builder = new android.app.AlertDialog.Builder(context);
        }
        return builder
                .setCancelable(false)
                .setPositiveButton(positiveButton, listener)
                .setNegativeButton(negativeButton, listener)
                .setMessage(rationaleMsg)
                .create();
    }

}
