package com.home.glx.uwallet.selfview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.home.glx.uwallet.R;
import com.home.glx.uwallet.tools.PublicTools;

import java.util.HashMap;
import java.util.Map;

public class SelectDateDialog implements
        CalendarView.OnCalendarSelectListener{
    private Context context;
    private MyDialog dialog;
    private CalendarView mCalendarView;
    private TextView yearMonthView;
    private int mYear;
    private int mDay;
    private int mMonth;
    private TextView title;
    private ImageView pre;
    private ImageView next;
    private TextView cancel;
    private TextView ok;
    private String chooseDate;
    private String firstDate;

    public SelectDateDialog(Context context, String chooseDate, String firstDate) {
        this.context = context;
        this.firstDate = firstDate;
        this.chooseDate = chooseDate;
    }

    public interface Choose {
        void onChoose(String date);
    }

    public Choose choose;

    public void setOnChoose(Choose choose) {
        this.choose = choose;
    }

    /**
     * 展示dialog
     */
    public void showDialog() {
        if (context == null) {
            return;
        }
        try {
            dialog = new MyDialog(context, R.style.fillWhiteDialog, R.layout.dialog_select_date);
            dialog.show();
            PublicTools.setDialogSize(context, dialog);
            dialog.setCancelable(false);

            mCalendarView = dialog.findViewById(R.id.calendarView);
            yearMonthView = dialog.findViewById(R.id.year_month);
            title = dialog.findViewById(R.id.title);
            if (!TextUtils.isEmpty(firstDate)) {
                title.setText("To date");
            }
            mYear = mCalendarView.getCurYear();
            if (!TextUtils.isEmpty(chooseDate)) {
                String[] date = chooseDate.split("/");
                if (date.length == 3) {
                    mCalendarView.scrollToCalendar(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
                    yearMonthView.setText(PublicTools.getEnglishMonth(Integer.parseInt(date[1])) + " " + Integer.parseInt(date[2]));
                }
            } else {
                yearMonthView.setText(PublicTools.getEnglishMonth(mCalendarView.getCurMonth()) + " " + mYear);
            }
            java.util.Calendar cal = java.util.Calendar.getInstance();
            int year = cal.get(java.util.Calendar.YEAR);
            int month = cal.get(java.util.Calendar.MONTH )+1;
            int day = cal.get(java.util.Calendar.DAY_OF_MONTH);
            Map<String, Calendar> map = new HashMap<>();
            if (TextUtils.isEmpty(firstDate)) {
                mCalendarView.setRange(2015, 1, 1, year, month, day);
            } else {
                String[] first = firstDate.split("/");
                for (int i = 1; i < Integer.parseInt(first[0]); i++) {
                    map.put(getSchemeCalendar(Integer.parseInt(first[2]), Integer.parseInt(first[1]), i, 0xFFA0A0A0, "").toString(),
                            getSchemeCalendar(year, month, i, 0xFFA0A0A0, ""));
                }
                mCalendarView.setRange(Integer.parseInt(first[2]), Integer.parseInt(first[1]), Integer.parseInt(first[0]), year, month, day);
            }
            mCalendarView.setOnCalendarSelectListener(this);
            for (int i = day + 1; i <= 31; i++) {
                map.put(getSchemeCalendar(year, month, i, 0xFFA0A0A0, "").toString(),
                        getSchemeCalendar(year, month, i, 0xFFA0A0A0, ""));
            }
            //此方法在巨大的数据量上不影响遍历性能，推荐使用
            mCalendarView.setSchemeDate(map);

            pre = dialog.findViewById(R.id.pre);
            next = dialog.findViewById(R.id.next);
            cancel = dialog.findViewById(R.id.cancel);
            ok = dialog.findViewById(R.id.ok);
            pre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCalendarView.scrollToPre();
                }
            });
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCalendarView.scrollToNext();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String day;
                    String month;
                    String year;
                    if (mCalendarView.getSelectedCalendar().getDay() < 10) {
                        day = "0" + mCalendarView.getSelectedCalendar().getDay();
                    } else {
                        day = "" + mCalendarView.getSelectedCalendar().getDay();
                    }
                    if (mCalendarView.getSelectedCalendar().getMonth() < 10) {
                        month = "0" + mCalendarView.getSelectedCalendar().getMonth();
                    } else {
                        month = "" + mCalendarView.getSelectedCalendar().getMonth();
                    }
                    year = "" + mCalendarView.getSelectedCalendar().getYear();
                    if (choose != null) {
                        choose.onChoose(day + "/" + month + "/" + year);
                    }
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            Log.i("error", e.toString());
        }
}

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }


    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        //calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        //calendar.setScheme(text);
        return calendar;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        mYear = calendar.getYear();
        yearMonthView.setText(PublicTools.getEnglishMonth(calendar.getMonth()) + " " + mYear);
    }
}
