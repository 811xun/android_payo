package com.home.glx.uwallet.selfview.choicetime;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.selfview.choicetime.wheelview.OnWheelChangedListener;
import com.home.glx.uwallet.selfview.choicetime.wheelview.OnWheelScrollListener;
import com.home.glx.uwallet.selfview.choicetime.wheelview.WheelView;
import com.home.glx.uwallet.selfview.choicetime.wheelview.adapter.AbstractWheelTextAdapter2;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Author:  Chen.yuan
 * Email:   hubeiqiyuan2010@163.com
 * Date:    2016/7/28 17:37
 * Description:日期选择window
 */
public class ChangeDatePopwindow2 extends PopupWindow implements View.OnClickListener {

    private Context context;
    private WheelView wvYear;
    private WheelView wvMonth;
    private WheelView wvDay;

    private TextView idCancel;
    private TextView btnSure;
    private TextView btnCancel;

    private ArrayList<String> arry_years = new ArrayList<String>();
    private ArrayList<String> arry_months = new ArrayList<String>();
    private ArrayList<String> arry_days = new ArrayList<String>();
    private CalendarTextAdapter mYearAdapter;
    private CalendarTextAdapter mMonthAdapter;
    private CalendarTextAdapter mDaydapter;

    private String month;
    private String day;

    private String currentYear = getYear();
    private String currentMonth = getMonth();
    private String currentDay = getDay();

    private int maxTextSize = 24;
    private int minTextSize = 14;

    private boolean issetdata = false;

    private String selectYear;
    private String selectMonth;
    private String selectDay;
    private boolean mDayGONE = false;
    private OnBirthListener onBirthListener;

    //private String[] all_months = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    private String[] all_months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private String year_t = "";
    private String day_t = "";
    private String month_t = "";

    private int maxYear;
    private int minYear;
    private View mView;


    public ChangeDatePopwindow2(final Context context) {
        new ChangeDatePopwindow2(context, Integer.parseInt(getYear()), 1900);
        init(context, Integer.parseInt(getYear()), 1900);
    }

    public ChangeDatePopwindow2(final Context context, int maxYear, int minYear) {
        super(context);
        init(context, maxYear, minYear);
    }

    public ChangeDatePopwindow2(final Context context, int maxYear, int minYear, View view,boolean mDayGONE) {
        super(context);
        this.mDayGONE = mDayGONE;
        init(context, maxYear, minYear);
        mView = view;
    }

    public void setDayGONE(boolean mDayGONE) {
    }

    private void init(final Context context, int maxYear, int minYear) {
        this.context = context;
        this.maxYear = maxYear;
        this.minYear = minYear;
        final View view = View.inflate(context, R.layout.dialog_myinfo_changebirth2, null);

        PublicTools.closeKeybord(((Activity) context));

        idCancel = (TextView) view.findViewById(R.id.id_cancel);
        idCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        wvYear = (WheelView) view.findViewById(R.id.wv_birth_year);
        wvYear.hasYuanjiao(false);
        wvMonth = (WheelView) view.findViewById(R.id.wv_birth_month);
        wvMonth.hasYuanjiao(false);
        wvDay = (WheelView) view.findViewById(R.id.wv_birth_day);
        wvDay.hasYuanjiao(false);
        btnSure = (TextView) view.findViewById(R.id.btn_myinfo_sure);
//		btnCancel = (TextView) view.findViewById(R.id.btn_myinfo_cancel);
        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        TypefaceUtil.replaceFont(btnSure, "fonts/gilroy_semiBold.ttf");
        btnSure.setOnClickListener(this);
//		btnCancel.setOnClickListener(this);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                mView.setFocusable(false);
            }
        });
        // 单击弹出窗以外处 关闭弹出窗
        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = view.findViewById(R.id.ly_myinfo_changeaddress_child).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        if (!issetdata) {
            initData();
        }
        initYears();
        Calendar c = Calendar.getInstance();
        if (mDayGONE) {
            mYearAdapter = new CalendarTextAdapter(context, arry_years, 0, maxTextSize, minTextSize);
        } else {
            mYearAdapter = new CalendarTextAdapter(context, arry_years, c.get(Calendar.YEAR) - 1900, maxTextSize, minTextSize);
        }
        wvYear.setVisibleItems(5);
        wvYear.setViewAdapter(mYearAdapter);

        int year = c.get(Calendar.YEAR);

        if (String.valueOf(year).equals((minYear + 1) + "")) {
            wvYear.setCurrentItem(0);
        } else {
            wvYear.setCurrentItem(c.get(Calendar.YEAR) - 1900);
        }

        initMonths(Integer.parseInt(month));
        mMonthAdapter = new CalendarTextAdapter(context, arry_months, c.get(Calendar.MONTH), maxTextSize, minTextSize, true);
        wvMonth.setVisibleItems(5);
        wvMonth.setViewAdapter(mMonthAdapter);
        wvMonth.setCurrentItem(c.get(Calendar.MONTH));

        initDays(Integer.parseInt(day));
        mDaydapter = new CalendarTextAdapter(context, arry_days, c.get(Calendar.DAY_OF_MONTH) - 1, maxTextSize, minTextSize);
        wvDay.setVisibleItems(5);
        wvDay.setViewAdapter(mDaydapter);
        wvDay.setCurrentItem(c.get(Calendar.DAY_OF_MONTH) - 1);

        wvYear.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                selectYear = currentText;
                setTextviewSize(currentText, mYearAdapter);
                if (currentText.substring(currentText.length() - 1, currentText.length()).equals(year_t)) {
                    currentYear = currentText.substring(0, currentText.length() - 1).toString();
                } else {
                    currentYear = currentText;
                }

                Log.d("currentYear==", currentYear);

//                setYear(currentYear);
//                initMonths(Integer.parseInt(month));
//                mMonthAdapter = new CalendarTextAdapter(context, arry_months, 0, maxTextSize, minTextSize, true);
//                wvMonth.setVisibleItems(5);
//                wvMonth.setViewAdapter(mMonthAdapter);
//                wvMonth.setCurrentItem(0);
//                calDays(currentYear, month);
            }
        });

        wvYear.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mYearAdapter);
            }
        });

        wvMonth.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = "";
                if (mMonthAdapter.is_month) {
                    currentText = (wheel.getCurrentItem() + 1) + "";
                } else {
                    currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
                }

                selectMonth = currentText;
                setTextviewSize(currentText, mMonthAdapter);


//                setMonth(currentText.substring(0, 1));
//                initDays(Integer.parseInt(day));
//                mDaydapter = new CalendarTextAdapter(context, arry_days, 0, maxTextSize, minTextSize);
//                wvDay.setVisibleItems(5);
//                wvDay.setViewAdapter(mDaydapter);
//                wvDay.setCurrentItem(0);
//                calDays(currentYear, month);
            }
        });

        wvMonth.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mMonthAdapter);
            }
        });

        wvDay.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mDaydapter);
                selectDay = currentText;
            }
        });

        wvDay.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mDaydapter);
            }
        });
    }


    public void setMaxYears(int maxYear, int minYear) {
        arry_years.clear();
        for (int i = minYear; i < maxYear; i++) {
            arry_years.add(i + year_t);
        }
    }


    public void initYears() {
        arry_years.clear();
        if (maxYear != 0 && minYear != 0) {
            for (int i = minYear + 1; i <= maxYear; i++) {
                arry_years.add(i + year_t);
            }
        } else {
            for (int i = minYear + 1; i <= Integer.parseInt(getYear()); i++) {
                arry_years.add(i + year_t);
            }
        }
    }

    /**
     * 初始化月份
     *
     * @param months
     */
    public void initMonths(int months) {
        arry_months.clear();
        for (int i = 1; i <= months; i++) {
            arry_months.add(i + month_t);
        }
    }

    public void initDays(int days) {
        arry_days.clear();
        for (int i = 1; i <= days; i++) {
            arry_days.add(i + day_t);
        }
    }

    private class CalendarTextAdapter extends AbstractWheelTextAdapter2 {
        ArrayList<String> list = new ArrayList<>();
        private boolean is_month = false;

        protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.birth_item, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;

            setItemTextResource(R.id.tempValue);
        }

        protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize, boolean is_month) {
            super(context, R.layout.birth_item, NO_RESOURCE, currentItem, maxsize, minsize);
            this.is_month = is_month;
            for (int i = 0; i < list.size(); i++) {
                this.list.add(all_months[i]);
            }
            setItemTextResource(R.id.tempValue);
        }

        public boolean is_month() {
            return is_month;
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";

        }
    }

    public void setBirthdayListener(OnBirthListener onBirthListener) {
        this.onBirthListener = onBirthListener;
    }

    @Override
    public void onClick(View v) {
        if (v == btnSure) {
            if (onBirthListener != null) {

                if (selectMonth.equals("2")) {
                    if (isLeayyear(selectYear)) {
                        //29天
                        if (Integer.parseInt(selectDay) > 29) {
                            Toast.makeText(context, R.string.not_greater, Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        if (selectYear.equals("2000") && Integer.parseInt(selectDay) <= 29) {

                        } else {
                            if (Integer.parseInt(selectDay) > 28) {
                                Toast.makeText(context, R.string.not_greater2, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                }


                if (selectMonth.equals("4") || selectMonth.equals("6")
                        || selectMonth.equals("9") || selectMonth.equals("11")) {
                    if (Integer.parseInt(selectDay) > 30) {
                        Toast.makeText(context, R.string.time_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (Integer.valueOf(selectDay) < 10) {
                    selectDay = "0" + selectDay;
                }
                if (Integer.valueOf(selectMonth) < 10) {
                    selectMonth = "0" + selectMonth;
                }

                onBirthListener.onClick(selectYear, selectMonth, mDayGONE ? "" : selectDay);
                Log.d("cy", "" + selectYear + "" + selectMonth + "" + selectDay);
            }
        }
        dismiss();
    }

    public interface OnBirthListener {
        public void onClick(String year, String month, String day);

    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, CalendarTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(maxTextSize);
            } else {
                textvew.setTextSize(minTextSize);
            }
        }
    }

    public String getYear() {
        if (maxYear != 0) {
            return maxYear + "";
        }
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) + "";
    }

    public String getMonth() {
//        Calendar c = Calendar.getInstance();
//        return c.get(Calendar.MONTH) + 1 + "";
        return "12";
    }

    public String getDay() {
//        Calendar c = Calendar.getInstance();
//        return c.get(Calendar.DATE) + "";
        return "31";
    }

    public void initData() {
        setDate(getYear(), getMonth(), getDay());
        this.currentDay = 1 + "";
        this.currentMonth = 1 + "";
    }


    /**
     * 设置年月日
     *
     * @param year
     * @param month
     * @param day
     */
    public void setDate(String year, String month, String day) {
        if (mDayGONE) {
            wvDay.setVisibility(View.GONE);
        } else {
            wvDay.setVisibility(View.VISIBLE);
        }
        selectYear = year + year_t;
        selectMonth = month + month_t;
        selectDay = day + day_t;
        issetdata = true;
        this.currentYear = year;
        this.currentMonth = month;
        this.currentDay = day;
        if (year.equals((minYear + 1) + "")) {
            wvYear.setCurrentItem(0);
        } else {
            wvYear.setCurrentItem(setYear(currentYear));
        }

        wvMonth.setCurrentItem(setMonth(currentMonth));
        wvDay.setCurrentItem(Integer.parseInt(currentDay) - 1);

//        if (year == getYear()) {
//            this.month = getMonth();
//        } else {
        this.month = 12 + "";
//        }
        calDays(year, month);
    }

    /**
     * 设置年份
     *
     * @param year
     */
    public int setYear(String year) {
        int yearIndex = 0;
//        if (!year.equals(getYear())) {
        this.month = 12 + "";
//        } else {
//            this.month = getMonth();
//        }
        for (int i = 1900; i < Integer.parseInt(getYear()); i++) {
            if (i == Integer.parseInt(year)) {
                return yearIndex;
            }
            yearIndex++;
        }
        return yearIndex;
    }

    /**
     * 设置月份
     *
     * @param month
     * @param month
     * @return
     */
    public int setMonth(String month) {
        int monthIndex = 0;
        calDays(currentYear, month);
        for (int i = 1; i < Integer.parseInt(this.month); i++) {
            if (Integer.parseInt(month) == i) {
                return monthIndex;
            } else {
                monthIndex++;
            }
        }
        return monthIndex;
    }


    private boolean isLeayyear(String year) {
        if (Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0) {
            return true;
        }
        return false;
    }

    /**
     * 计算每月多少天
     *
     * @param month
     * @param year
     */
    public void calDays(String year, String month) {
        boolean leayyear = false;
        if (Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0) {
            leayyear = true;
        } else {
            leayyear = false;
        }
        for (int i = 1; i <= 12; i++) {
            switch (Integer.parseInt(month)) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    this.day = 31 + "";
                    break;
                case 2:
                    if (leayyear) {
                        this.day = 29 + "";
                    } else {
                        this.day = 28 + "";
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    this.day = 30 + "";
                    break;
            }
        }

        if (year.equals(getYear()) && month.equals(getMonth())) {
            this.day = getDay();
        }
    }
}