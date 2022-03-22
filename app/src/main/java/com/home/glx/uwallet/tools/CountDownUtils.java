package com.home.glx.uwallet.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

import com.home.glx.uwallet.R;

import java.util.Timer;
import java.util.TimerTask;

public class CountDownUtils {

    private TextView textView;
    private Button button;
    private String code;
    //计时时常，单位S
    private int long_time;
    private int start_long_time;
    private Activity activity;
    private String start_text = "";
    private SharePreferenceUtils sharePreferenceUtils;
    private Timer timer;
    private TimerTask timerTask;
    //是否正在计时
    private boolean is_run = false;
    private String end_text = "";
    private String begin_text = "";

    /**
     * 设定一个时常，从头开始计数
     *
     * @param activity
     * @param code
     * @param long_time
     */
    public CountDownUtils(Activity activity, Button button, String code, int long_time) {
        this.button = button;
        init(activity, code, long_time);
    }

    /**
     * 设定一个时常，从头开始计数
     *
     * @param activity
     * @param code
     * @param long_time
     */
    public CountDownUtils(Activity activity, Button button, String code, int long_time, String end_text) {
        this.button = button;
        this.end_text = end_text;
        init(activity, code, long_time);
    }

    /**
     * 设定一个时常，从头开始计数
     *
     * @param activity
     * @param code
     * @param long_time
     */
    public CountDownUtils(Activity activity, TextView textView, String code, int long_time) {
        this.textView = textView;
        init(activity, code, long_time);
    }

    /**
     * 设定一个时常，从头开始计数
     *
     * @param activity
     * @param code
     * @param long_time
     */
    public CountDownUtils(Activity activity, TextView textView, String code, int long_time, String begin_text, String end_text) {
        this.textView = textView;
        this.end_text = end_text;
        this.begin_text = begin_text;
        init(activity, code, long_time);
    }

    /**
     * 设定一个时常，从头开始计数
     *
     * @param activity
     * @param code
     * @param long_time
     */
    public CountDownUtils(Activity activity, TextView textView, String code, int long_time, String end_text) {
        this.textView = textView;
        this.end_text = end_text;
        init(activity, code, long_time);
    }

    private void init(Activity activity, String code, int long_time) {
        if (!code.equals("")) {
            sharePreferenceUtils = new SharePreferenceUtils(activity, code);
        }

        this.code = code;
        this.activity = activity;
        this.long_time = long_time;
        this.start_long_time = long_time;
        if (textView == null) {
            this.start_text = button.getText().toString().trim();
        } else {
            this.start_text = textView.getText().toString().trim();
        }


        long now_time = System.currentTimeMillis();
        long after_time;
        if (!code.equals("")) {
            after_time = (long) sharePreferenceUtils.get("start_time", 0L);
        } else {
            after_time = 0L;
        }

        if ((now_time - after_time) / 1000 > long_time) {

        } else {
            this.long_time = long_time - (int) ((now_time - after_time) / 1000);
            is_run = true;
            runTime();
        }
    }

    public void setLongTime(int long_time) {
        this.long_time = long_time;
    }


    /**
     * 设置接收时显示的文字
     * @param text
     */
    public void setStopText(String text) {
        this.start_text = text;
    }

    public void start(int long_time) {
        this.long_time = long_time;
        saveNowTime();
        is_run = true;
        runTime();
    }

    /**
     * 开始计时
     */
    public void start() {
        //setStopText(activity.getString(R.string.resend));
        if (!code.equals("")) {
            if (sharePreferenceUtils.is_contains("start_time")) {
                long now_time = System.currentTimeMillis();
                long after_time = (long) sharePreferenceUtils.get("start_time", 0L);
                if ((now_time - after_time) / 1000 > long_time) {
                    saveNowTime();
                } else {
                    long_time = long_time - (int) ((now_time - after_time) / 1000);
                }
            } else {
                //保存开始时间
                saveNowTime();
            }
        }
        is_run = true;
        runTime();
    }


    /**
     * 计时
     */
    private void runTime() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    long_time--;//自动减1
                    Message message = Message.obtain();
                    message.arg1 = long_time;
                    mHandler.sendMessage(message);//发送消息
                }
            }, 0, 1000);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (textView != null) {
                textView.setText(begin_text + msg.arg1 + end_text);
            } else {
                button.setText(msg.arg1 + end_text);
            }
            if (long_time > 0) {
                if (activity != null) {
                    runTime();
                } else {
                    stopTimer();
                }
            } else {
                stopTimer();
                if (textView != null) {
                    textView.setText(start_text);
                } else {
                    button.setText(start_text);
                }
                if (onTimeStop != null) {
                    onTimeStop.time_stop();
                }
            }
        }
    };

    /**
     * 停止计时器
     */
    public void stopTimer() {
        if (textView != null) {
            textView.setText("");
        } else {
            button.setText(start_text);
        }
        is_run = false;
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        long_time = start_long_time;
    }

    /**
     * 获取计时是否还在进行
     *
     * @return
     */
    public boolean getIsRun() {
        return is_run;
    }

    /**
     * 保存当前时间
     */
    private void saveNowTime() {
        if (!code.equals("")) {
            sharePreferenceUtils.put("start_time", System.currentTimeMillis());
            sharePreferenceUtils.save();
        }
    }


    public interface OnTimeStop {
        void time_stop();
    }

    private OnTimeStop onTimeStop;

    public void setOnTimeStop(OnTimeStop onTimeStop) {
        this.onTimeStop = onTimeStop;
    }

}
