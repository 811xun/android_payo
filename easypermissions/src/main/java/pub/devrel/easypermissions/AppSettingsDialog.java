package pub.devrel.easypermissions;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Field;

/**
 * Dialog to prompt the user to go to the app's settings screen and enable permissions. If the user
 * clicks 'OK' on the dialog, they are sent to the settings screen. The result is returned to the
 * Activity via {@see Activity#onActivityResult(int, int, Intent)}.
 * <p>
 * Use the {@link Builder} to create and display a dialog.
 */
public class AppSettingsDialog implements Parcelable {

    private static final String TAG = "EasyPermissions";

    public static final int DEFAULT_SETTINGS_REQ_CODE = 16061;

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public static final Parcelable.Creator<AppSettingsDialog> CREATOR = new Parcelable.Creator<AppSettingsDialog>() {
        @Override
        public AppSettingsDialog createFromParcel(Parcel in) {
            return new AppSettingsDialog(in);
        }

        @Override
        public AppSettingsDialog[] newArray(int size) {
            return new AppSettingsDialog[size];
        }
    };

    static final String EXTRA_APP_SETTINGS = "extra_app_settings";

    @StyleRes
    private final int mThemeResId;
    private final String mRationale;
    private final String mTitle;
    private final String mPositiveButtonText;
    private final String mNegativeButtonText;
    private final int mRequestCode;
    private final int mIntentFlags;

    private Object mActivityOrFragment;
    public Context mContext;

    private AppSettingsDialog(Parcel in) {
        mThemeResId = in.readInt();
        mRationale = in.readString();
        mTitle = in.readString();
        mPositiveButtonText = in.readString();
        mNegativeButtonText = in.readString();
        mRequestCode = in.readInt();
        mIntentFlags = in.readInt();
    }

    private AppSettingsDialog(@NonNull final Object activityOrFragment,
                              @StyleRes int themeResId,
                              @Nullable String rationale,
                              @Nullable String title,
                              @Nullable String positiveButtonText,
                              @Nullable String negativeButtonText,
                              int requestCode,
                              int intentFlags) {
        setActivityOrFragment(activityOrFragment);
        mThemeResId = themeResId;
        mRationale = rationale;
        mTitle = title;
        mPositiveButtonText = positiveButtonText;
        mNegativeButtonText = negativeButtonText;
        mRequestCode = requestCode;
        mIntentFlags = intentFlags;
    }

    static AppSettingsDialog fromIntent(Intent intent, Activity activity) {
        AppSettingsDialog dialog = intent.getParcelableExtra(AppSettingsDialog.EXTRA_APP_SETTINGS);

        // It's not clear how this could happen, but in the case that it does we should try
        // to avoid a runtime crash and just use the default dialog.
        // https://github.com/googlesamples/easypermissions/issues/278
        if (dialog == null) {
            Log.e(TAG, "Intent contains null value for EXTRA_APP_SETTINGS: "
                    + "intent=" + intent
                    + ", "
                    + "extras=" + intent.getExtras());

            dialog = new AppSettingsDialog.Builder(activity).build();
        }

        dialog.setActivityOrFragment(activity);
        return dialog;
    }

    private void setActivityOrFragment(Object activityOrFragment) {
        mActivityOrFragment = activityOrFragment;

        if (activityOrFragment instanceof Activity) {
            mContext = (Activity) activityOrFragment;
        } else if (activityOrFragment instanceof Fragment) {
            mContext = ((Fragment) activityOrFragment).getContext();
        } else {
            throw new IllegalStateException("Unknown object: " + activityOrFragment);
        }
    }

    private void startForResult(Intent intent) {
        if (mActivityOrFragment instanceof Activity) {
            ((Activity) mActivityOrFragment).startActivityForResult(intent, mRequestCode);
        } else if (mActivityOrFragment instanceof Fragment) {
            ((Fragment) mActivityOrFragment).startActivityForResult(intent, mRequestCode);
        }
    }

    /**
     * Display the built dialog.
     */
    public void show() {
        startForResult(AppSettingsDialogHolderActivity.createShowDialogIntent(mContext, this));
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Show the dialog. {@link #show()} is a wrapper to ensure backwards compatibility
     */
    AlertDialog showDialog(DialogInterface.OnClickListener positiveListener,
                           DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder;
        if (mThemeResId != -1) {
            builder = new AlertDialog.Builder(mContext, mThemeResId);
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        AlertDialog alertDialog = builder
                .setCancelable(false)
                .setTitle(mTitle)
                .setMessage(mRationale)
                .setPositiveButton(mPositiveButtonText, positiveListener)
                .setNegativeButton(mNegativeButtonText, negativeListener)
                .show();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.view_yj_dialog1);

//        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
//        Log.d("sssss", "createSupportDialog: " + dip2px(mContext, 300));
//        params.width = dip2px(mContext, 300);
//        alertDialog.getWindow().setAttributes(params);
        try {
            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object alertController = mAlert.get(alertDialog);

            Field mTitleView = alertController.getClass().getDeclaredField("mTitleView");
            mTitleView.setAccessible(true);
            TextView title = (TextView) mTitleView.get(alertController);
            title.setTextColor(Color.parseColor("#000000"));
            title.setTextSize(17);
//            title.setPadding(0, 0, 0, 0);
//            title.setWidth(dip2px(mContext, 300));
//            title.setGravity(View.TEXT_ALIGNMENT_CENTER);

            replaceFont(title, "fonts/acumin_boldPro.ttf");

            Field mMessageView = alertController.getClass().getDeclaredField("mMessageView");
            mMessageView.setAccessible(true);
            TextView titleMesg = (TextView) mMessageView.get(alertController);
//            titleMesg.setPadding(40, 0, 0, 0);
            titleMesg.setTextColor(Color.parseColor("#000000"));
            titleMesg.setTextSize(15);
//            titleMesg.setWidth(dip2px(mContext, 300));
//            titleMesg.setGravity(View.TEXT_ALIGNMENT_CENTER);

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

    /**
     * <p>Replace the font of specified view and it's children</p>
     *
     * @param root     The root view.
     * @param fontPath font file path relative to 'assets' directory.
     */
    public static void replaceFont(@NonNull View root, String fontPath) {
        if (root == null || TextUtils.isEmpty(fontPath)) {
            return;
        }


        if (root instanceof TextView) { // If view is TextView or it's subclass, replace it's font
            TextView textView = (TextView) root;
            int style = Typeface.NORMAL;
            if (textView.getTypeface() != null) {
                style = textView.getTypeface().getStyle();
            }
            textView.setTypeface(createTypeface(root.getContext(), fontPath), style);
        } else if (root instanceof ViewGroup) { // If view is ViewGroup, apply this method on it's child views
            ViewGroup viewGroup = (ViewGroup) root;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                replaceFont(viewGroup.getChildAt(i), fontPath);
            }
        }
    }

    /*
     * Create a Typeface instance with your font file
     */
    public static Typeface createTypeface(Context context, String fontPath) {
        return Typeface.createFromAsset(context.getAssets(), fontPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(mThemeResId);
        dest.writeString(mRationale);
        dest.writeString(mTitle);
        dest.writeString(mPositiveButtonText);
        dest.writeString(mNegativeButtonText);
        dest.writeInt(mRequestCode);
        dest.writeInt(mIntentFlags);
    }

    int getIntentFlags() {
        return mIntentFlags;
    }

    /**
     * Builder for an {@link AppSettingsDialog}.
     */
    public static class Builder {

        private final Object mActivityOrFragment;
        private final Context mContext;
        @StyleRes
        private int mThemeResId = -1;
        private String mRationale;
        private String mTitle;
        private String mPositiveButtonText;
        private String mNegativeButtonText;
        private int mRequestCode = -1;
        private boolean mOpenInNewTask = false;

        /**
         * Create a new Builder for an {@link AppSettingsDialog}.
         *
         * @param activity the {@link Activity} in which to display the dialog.
         */
        public Builder(@NonNull Activity activity) {
            mActivityOrFragment = activity;
            mContext = activity;
        }

        /**
         * Create a new Builder for an {@link AppSettingsDialog}.
         *
         * @param fragment the {@link Fragment} in which to display the dialog.
         */
        public Builder(@NonNull Fragment fragment) {
            mActivityOrFragment = fragment;
            mContext = fragment.getContext();
        }

        /**
         * Set the dialog theme.
         */
        @NonNull
        public Builder setThemeResId(@StyleRes int themeResId) {
            mThemeResId = themeResId;
            return this;
        }

        /**
         * Set the title dialog. Default is "Permissions Required".
         */
        @NonNull
        public Builder setTitle(@Nullable String title) {
            mTitle = title;
            return this;
        }

        /**
         * Set the title dialog. Default is "Permissions Required".
         */
        @NonNull
        public Builder setTitle(@StringRes int title) {
            mTitle = mContext.getString(title);
            return this;
        }

        /**
         * Set the rationale dialog. Default is
         * "This app may not work correctly without the requested permissions.
         * Open the app settings screen to modify app permissions."
         */
        @NonNull
        public Builder setRationale(@Nullable String rationale) {
            mRationale = rationale;
            return this;
        }

        /**
         * Set the rationale dialog. Default is
         * "This app may not work correctly without the requested permissions.
         * Open the app settings screen to modify app permissions."
         */
        @NonNull
        public Builder setRationale(@StringRes int rationale) {
            mRationale = mContext.getString(rationale);
            return this;
        }

        /**
         * Set the positive button text, default is {@link android.R.string#ok}.
         */
        @NonNull
        public Builder setPositiveButton(@Nullable String text) {
            mPositiveButtonText = text;
            return this;
        }

        /**
         * Set the positive button text, default is {@link android.R.string#ok}.
         */
        @NonNull
        public Builder setPositiveButton(@StringRes int textId) {
            mPositiveButtonText = mContext.getString(textId);
            return this;
        }

        /**
         * Set the negative button text, default is {@link android.R.string#cancel}.
         * <p>
         * To know if a user cancelled the request, check if your permissions were given with {@link
         * EasyPermissions#hasPermissions(Context, String...)} in {@see
         * Activity#onActivityResult(int, int, Intent)}. If you still don't have the right
         * permissions, then the request was cancelled.
         */
        @NonNull
        public Builder setNegativeButton(@Nullable String text) {
            mNegativeButtonText = text;
            return this;
        }

        /**
         * Set the negative button text, default is {@link android.R.string#cancel}.
         */
        @NonNull
        public Builder setNegativeButton(@StringRes int textId) {
            mNegativeButtonText = mContext.getString(textId);
            return this;
        }

        /**
         * Set the request code use when launching the Settings screen for result, can be retrieved
         * in the calling Activity's {@see Activity#onActivityResult(int, int, Intent)} method.
         * Default is {@link #DEFAULT_SETTINGS_REQ_CODE}.
         */
        @NonNull
        public Builder setRequestCode(int requestCode) {
            mRequestCode = requestCode;
            return this;
        }

        /**
         * Set whether the settings screen should be opened in a separate task. This is achieved by
         * setting {@link android.content.Intent#FLAG_ACTIVITY_NEW_TASK#FLAG_ACTIVITY_NEW_TASK} on
         * the Intent used to open the settings screen.
         */
        @NonNull
        public Builder setOpenInNewTask(boolean openInNewTask) {
            mOpenInNewTask = openInNewTask;
            return this;
        }

        /**
         * Build the {@link AppSettingsDialog} from the specified options. Generally followed by a
         * call to {@link AppSettingsDialog#show()}.
         */
        @NonNull
        public AppSettingsDialog build() {
            mRationale = TextUtils.isEmpty(mRationale) ?
                    mContext.getString(R.string.rationale_ask_again) : mRationale;
            mTitle = TextUtils.isEmpty(mTitle) ?
                    mContext.getString(R.string.title_settings_dialog) : mTitle;
            mPositiveButtonText = TextUtils.isEmpty(mPositiveButtonText) ?
                    mContext.getString(android.R.string.ok) : mPositiveButtonText;
            mNegativeButtonText = TextUtils.isEmpty(mNegativeButtonText) ?
                    mContext.getString(android.R.string.cancel) : mNegativeButtonText;
            mRequestCode = mRequestCode > 0 ? mRequestCode : DEFAULT_SETTINGS_REQ_CODE;

            int intentFlags = 0;
            if (mOpenInNewTask) {
                intentFlags |= Intent.FLAG_ACTIVITY_NEW_TASK;
            }

            return new AppSettingsDialog(
                    mActivityOrFragment,
                    mThemeResId,
                    mRationale,
                    mTitle,
                    mPositiveButtonText,
                    mNegativeButtonText,
                    mRequestCode,
                    intentFlags);
        }

    }

}
