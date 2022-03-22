package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.selfview.choicetime.wheelview.OnWheelChangedListener;
import com.home.glx.uwallet.selfview.choicetime.wheelview.OnWheelScrollListener;
import com.home.glx.uwallet.selfview.choicetime.wheelview.WheelView;
import com.home.glx.uwallet.selfview.choicetime.wheelview.adapter.AbstractWheelTextAdapter1;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部弹出选择按钮
 */
public class BottomDialog {

    private MyDialog dialog;
    private Context context;
    private View mView;
    private boolean showCircleImage = false;

    public BottomDialog(Context context) {
        this.context = context;
    }

    public BottomDialog(Context context, View mView) {
        this.context = context;
        this.mView = mView;
    }

    public void setFlag(boolean showCircleImage) {
        this.showCircleImage = showCircleImage;
    }

    private TextView idCancel;
    private TextView idSure;
    private WheelView choiceList;
    private List<ChoiceDatas> arry_all = new ArrayList<>();
    private ChoiceDatas choiceDatas;

    private int maxTextSize = 20;
    private int minTextSize = 14;

    private CalendarTextAdapter WhellViewAdapter;

    private void showNew(final List<ChoiceDatas> longTimeList, String saveText, final int choice_num) {
        arry_all = longTimeList;
        if (arry_all == null || arry_all.size() == 0) {
            return;
        }

        choiceDatas = arry_all.get(0);

        dialog = new MyDialog(context, R.style.MyBottomDialog, R.layout.dialog_bottom_choice);
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        //params.dimAmount = 0f;
        window.setAttributes(params);


        idCancel = (TextView) dialog.findViewById(R.id.id_cancel);
        idSure = (TextView) dialog.findViewById(R.id.id_sure);
        choiceList = (WheelView) dialog.findViewById(R.id.choice_list);
        choiceList.hasYuanjiao(true);
        idCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView selectPlease = dialog.findViewById(R.id.select_please);
        //切换字体
        TypefaceUtil.replaceFont(selectPlease, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(idCancel, "fonts/gilroy_semiBold.ttf");
        TypefaceUtil.replaceFont(idSure, "fonts/gilroy_semiBold.ttf");
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mView.setFocusable(false);
//                new Handler().post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mView.setFocusable(true);
//                    }
//                });
            }
        });
        if (!TextUtils.isEmpty(saveText)) {
            idSure.setText(saveText);
        }
        idSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choiceItem != null) {
                    L.log("text:" + choiceDatas.getText());
                    L.log("value:" + choiceDatas.getValue());
                    choiceItem.itemClick(choiceDatas.getText(), choiceDatas.getValue(), choice_num);
                }
                dialog.dismiss();
            }
        });

        WhellViewAdapter = new CalendarTextAdapter(context, arry_all, 0, maxTextSize, minTextSize);
        //设置可见的条目数
        choiceList.setVisibleItems(5);
        choiceList.setViewAdapter(WhellViewAdapter);
        //当前选中下标
        choiceList.setCurrentItem(0);

        choiceList.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                //当前选择
                choiceDatas = arry_all.get(wheel.getCurrentItem());
                String currentText = (String) WhellViewAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, WhellViewAdapter);
//                selectYear = currentText;
            }
        });

        choiceList.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {

            }
        });
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    public class CalendarTextAdapter extends AbstractWheelTextAdapter1 {
        List<ChoiceDatas> list = new ArrayList<>();
        private boolean is_month = false;

        protected CalendarTextAdapter(Context context, List<ChoiceDatas> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize, showCircleImage);
            this.list = list;

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
        protected CharSequence getItemText(int i) {
            return list.get(i).getText();
        }
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

    public void show(final List<ChoiceDatas> longTimeList, final int choice_num) {
        showNew(longTimeList, "", choice_num);
    }

    public void show(final List<ChoiceDatas> longTimeList, String saveText, final int choice_num) {
        showNew(longTimeList, saveText, choice_num);
    }

    public interface ChoiceItem {
        void itemClick(String choiceText, String code, int choice_num);
    }

    private ChoiceItem choiceItem;

    public void setOnChoiceItem(ChoiceItem choiceItem) {
        this.choiceItem = choiceItem;
    }

}
