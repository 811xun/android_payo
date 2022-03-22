package com.home.glx.uwallet.activity.xzc.promotion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.request.RequestInterface;
import com.home.glx.uwallet.request.RequestUtils;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.selfview.TiShiDialog;
import com.home.glx.uwallet.tools.ChangeLineEdittext;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.SharePreferenceUtils;
import com.home.glx.uwallet.tools.TypefaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static android.content.Context.INPUT_METHOD_SERVICE;

@SuppressLint("ValidFragment")
public class PupwindowAddPro extends DialogFragment {
    public PupwindowAddPro() { //谷歌上获取到的bug: Unable to instantiate fragment com.home.glx.uwallet.activity.xzc.promotion.PupwindowAddPro: could not find Fragment constructor
    }

    private Context context;
    private ChangeLineEdittext mChangeLineEdittext;
    private TextView jine;
    private TextView add;
    private TextView date;
    private TextView date2;
    private TextView error;
    private String marketingId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        view = inflater.inflate(R.layout.pop_add_pro, ((ViewGroup) window.findViewById(android.R.id.content)), false);
//让DialogFragment的背景为透明
        dialog.setCanceledOnTouchOutside(false); // 外部点击bu取消
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        window.setAttributes(lp);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        window.setLayout(-1, -2);//这2行,和上面的一样,注意顺序就行;

        ShowDialog(view);
        return view;
    }


    public PupwindowAddPro(View view, Context context, Activity activity) {
        this.context = context;
    }

    /**
     * 获取当前屏幕分辨率
     *
     * @param context 上下文
     * @return 分辨率
     */
    public static float getDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp
     * @return px
     */
    public static int dp2px(Context context, int dpValue) {
        return (int) (dpValue * getDensity(context) + 0.5f);
    }

    public void ShowDialog(final View view) {
        if (context == null) {
            return;
        }
        TypefaceUtil.replaceFont(view.findViewById(R.id.title), "fonts/gilroy_bold.ttf");

        mChangeLineEdittext = (ChangeLineEdittext) view.findViewById(R.id.id_first_name);
//        filterChinese(mChangeLineEdittext);//禁止输入中文
        ((TextInputLayout) view.findViewById(R.id.til_first_name)).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/gilroy_semiBold.ttf"));//TextInputLayout提示文字的字体
        TypefaceUtil.replaceFont(mChangeLineEdittext, "fonts/gilroy_medium.ttf");

        jine = view.findViewById(R.id.jine);
        TypefaceUtil.replaceFont(jine, "fonts/gilroy_semiBold.ttf");

        add = view.findViewById(R.id.add);
        TypefaceUtil.replaceFont(add, "fonts/gilroy_semiBold.ttf");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Map<String, Object> maps = new HashMap<>();
                maps.put("marketingId", marketingId);
                PromotionListActivity.showTishgiDialog = true;

                SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
                maps.put("userId", userId);
                maps.put("inputCode", mChangeLineEdittext.getText().toString());
                add(maps);
            }
        });

        error = view.findViewById(R.id.tv_error);
        TypefaceUtil.replaceFont(error, "fonts/gilroy_medium.ttf");
        date = view.findViewById(R.id.date);
        date2 = view.findViewById(R.id.date2);
        TypefaceUtil.replaceFont(date, "fonts/gilroy_medium.ttf");

        mChangeLineEdittext.setInputType(InputType.TYPE_CLASS_TEXT);
//        String digits = context.getString(R.string.rule_text);
//        mChangeLineEdittext.setKeyListener(DigitsKeyListener.getInstance(digits));
//        mChangeLineEdittext.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mChangeLineEdittext.setFocusable(true);//自动弹出数字软键盘
        mChangeLineEdittext.setFocusableInTouchMode(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInputFromInputMethod(mChangeLineEdittext.getWindowToken(), 0);
                imm.toggleSoftInputFromWindow(mChangeLineEdittext.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 250);

        view.findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mChangeLineEdittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    ((TextInputLayout) view.findViewById(R.id.til_first_name)).setHintTextColor(createColorStateList(context, R.color.color_717171));

                    ((TextInputLayout) view.findViewById(R.id.til_first_name)).setHint("Enter promo code");
                    mChangeLineEdittext.setHint("");//下面的提示文字
                    mChangeLineEdittext.setBackground(ContextCompat.getDrawable(context, R.drawable.edt_bg_un_selected));
                }
            }
        });
        initEditOnEditorActionListener();
        mChangeLineEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ((TextInputLayout) view.findViewById(R.id.til_first_name)).setHintTextColor(createColorStateList(context, R.color.color_717171));

                ((TextInputLayout) view.findViewById(R.id.til_first_name)).setHint("Enter promo code");
                mChangeLineEdittext.setHint("");//下面的提示文字
                mChangeLineEdittext.setBackground(ContextCompat.getDrawable(context, R.drawable.edt_bg_un_selected));

                jine.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
                date.setVisibility(View.GONE);
                error.setVisibility(View.GONE);
            }
        });


    }

    private void initEditOnEditorActionListener() {
        mChangeLineEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isEmpty(mChangeLineEdittext.getText().toString())) {
                        mChangeLineEdittext.requestFocus();
                        mChangeLineEdittext.setFocusable(true);
                        mChangeLineEdittext.setFocusableInTouchMode(true);
                        mChangeLineEdittext.post(new Runnable() {
                            @Override
                            public void run() {
                                ((TextInputLayout) view.findViewById(R.id.til_first_name)).setHintTextColor(createColorStateList(context, R.color.end_orange));

                                ((TextInputLayout) view.findViewById(R.id.til_first_name)).setHint("Please fill out this section");//上边的提示文字
                                mChangeLineEdittext.setHint("Enter promo code");//下面的提示文字
                                mChangeLineEdittext.setBackground(ContextCompat.getDrawable(context, R.drawable.edt_bg_un_selected_orange));//黄线
                                mChangeLineEdittext.requestFocus();
                                jine.setVisibility(View.GONE);
                                add.setVisibility(View.GONE);
                                date.setVisibility(View.GONE);
                                error.setVisibility(View.GONE);
                            }
                        });
                    } else if (mChangeLineEdittext.getText().toString().length() == 1) {//长度为1 不掉用接口直接显示报错信息。
                        jine.setVisibility(View.GONE);
                        add.setVisibility(View.GONE);
                        date.setVisibility(View.GONE);
                        error.setVisibility(View.VISIBLE);
                        error.setText("Promotion code does not exist or is not available");
                    } else {
//                        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
//                        imm.showSoftInputFromInputMethod(mChangeLineEdittext.getWindowToken(), 0);
//                        imm.toggleSoftInputFromWindow(mChangeLineEdittext.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                        final Map<String, Object> maps = new HashMap<>();
                        maps.put("code", mChangeLineEdittext.getText().toString());

                        SharePreferenceUtils sharePreferenceUtils = new SharePreferenceUtils(context, StaticParament.USER);
                        String userId = (String) sharePreferenceUtils.get(StaticParament.USER_ID, "");
                        maps.put("userId", userId);
                        PromotionListActivity.showTishgiDialog = true;
                        mChangeLineEdittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                return true;
                            }
                        });
                        serach(maps);
//                        imm.showSoftInputFromInputMethod(mChangeLineEdittext.getWindowToken(), 0);
//                        imm.toggleSoftInputFromWindow(mChangeLineEdittext.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private static ColorStateList createColorStateList(Context context, int color) {
        int[] colors = new int[]{ContextCompat.getColor(context, color), ContextCompat.getColor(context, color)};
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_checked};
        states[1] = new int[]{-android.R.attr.state_checked};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    //    private LoadingPopwindow mLoadingPopwindow;
    JSONObject jsonObject = null;

    public void serach(Map<String, Object> maps) {//搜索营销码
        RequestUtils requestUtils1 = new RequestUtils(getActivity(), maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {
                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.findPromotionCode(headerMap, requestBody);

                requestUtils.Call(call, null, false);
                view.findViewById(R.id.avi).setVisibility(View.VISIBLE);//展示Popwindow的原因：LoadingDialog弹出会自动关闭软键盘。
//                mLoadingPopwindow=    new LoadingPopwindow(view, context);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                view.findViewById(R.id.avi).setVisibility(View.GONE);
                initEditOnEditorActionListener();

                if (dataStr != null) {
                    try {
                        jsonObject = new JSONObject(dataStr);
                        jine.setVisibility(View.VISIBLE);
                        add.setVisibility(View.VISIBLE);
                        date.setVisibility(View.VISIBLE);
                        date2.setVisibility(View.VISIBLE);
                        PupwindowAddPro.this.error.setVisibility(View.GONE);
                        marketingId = jsonObject.getString("id");
                        jine.setText("$" + PublicTools.twoend(jsonObject.getInt("amount")));

                        date2.setText((jsonObject.getInt("validityLimitState") == 0 ? "" : "Expires on " + jsonObject.getString("expiredTimeStr") + " • ") +
                                (jsonObject.getInt("amountLimitState") == 0 ? "" : "Min. order $" + jsonObject.getString("minTransAmount").split("\\.")[0] + " • ")
                                + jsonObject.getString("description"));
                        date2.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                //这个回调会调用多次，获取完行数记得注销监听
                                try {
                                    if (date2.getLineCount() > 1) {
                                        date.setText((jsonObject.getInt("validityLimitState") == 0 ? "" : "Expires on " + jsonObject.getString("expiredTimeStr") + " • ") +
                                                (jsonObject.getInt("amountLimitState") == 0 ? "" : "Min. order $" + jsonObject.getString("minTransAmount").split("\\.")[0] + "\n• ")
                                                + jsonObject.getString("description"));
                                    } else {

                                        date.setText((jsonObject.getInt("validityLimitState") == 0 ? "" : "Expires on " + jsonObject.getString("expiredTimeStr") + " • ") +
                                                (jsonObject.getInt("amountLimitState") == 0 ? "" : "Min. order $" + jsonObject.getString("minTransAmount").split("\\.")[0] + " • ")
                                                + jsonObject.getString("description"));
                                    }

                                    date.setVisibility(View.VISIBLE);
                                    date2.setVisibility(View.GONE);
                                    date.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/gilroy_medium.ttf"));
                                    date2.getViewTreeObserver().removeOnPreDrawListener(this);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    jine.setVisibility(View.GONE);
                    add.setVisibility(View.GONE);
                    date.setVisibility(View.GONE);
                    date2.setVisibility(View.GONE);
                    PupwindowAddPro.this.error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void resError(String error) {
//                mLoadingPopwindow.disPop();
                view.findViewById(R.id.avi).setVisibility(View.GONE);
                initEditOnEditorActionListener();

                jine.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
                date.setVisibility(View.GONE);
                date2.setVisibility(View.GONE);
                PupwindowAddPro.this.error.setVisibility(View.VISIBLE);
                PupwindowAddPro.this.error.setText(error);
            }
        });
    }

    public void add(Map<String, Object> maps) {//搜索营销码
        RequestUtils requestUtils1 = new RequestUtils(context, maps, new RequestUtils.RequestDataStr() {
            @Override
            public void topData(RequestUtils requestUtils, RequestInterface requestInterface,
                                Map<String, String> headerMap, String sign, String time, Map<String, Object> maps) {
                RequestBody requestBody =
                        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new Gson().toJson(maps));
                L.log("参数：" + new Gson().toJson(maps));
                Call<ResponseBody> call = requestInterface.addPromotionCode(headerMap, requestBody);
                requestUtils.Call(call);
            }

            @Override
            public void resData(String dataStr, String pc, String code) {
                Log.d("xzcxz", "resData: " + dataStr);
                if (dataStr != null) {
                    dismiss();
                    onitemClick.itemClick();
                }
            }

            @Override
            public void resError(String error) {
                TiShiDialog tiShiDialog = new TiShiDialog(context);
                tiShiDialog.ShowDialog_wuwaiyinying(error);
                PromotionListActivity.showTishgiDialog = false;
            }
        });
    }

    public interface OnitemClick {
        void itemClick();
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }

}
