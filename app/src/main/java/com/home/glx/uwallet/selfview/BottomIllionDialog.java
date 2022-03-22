package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.IllionPreLoadData;

import java.util.ArrayList;
import java.util.List;

public class BottomIllionDialog {
    private MyDialog dialog;
    private Context context;

    public BottomIllionDialog(Context context) {
        this.context = context;
    }

    public void show(final List<IllionPreLoadData.ValuesBean> longTimeList) {
        dialog = new MyDialog(context, R.style.MyDialog, R.layout.dialog_bottomchoice);
        dialog.show();

        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);


        ListView idListview = (ListView) dialog.findViewById(R.id.id_listview);
        idListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (choiceItem != null) {
                    choiceItem.itemClick(longTimeList.get(position).getKeyValue(),
                            longTimeList.get(position).getKeyName());
                }
                dialog.dismiss();
            }
        });

        if (longTimeList.size() > 7) {
            final float scale = context.getResources().getDisplayMetrics().density;
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) (350 * scale + 0.5f));
            idListview.setLayoutParams(params1);
        }

        List<String> newList = new ArrayList<>();

        for (int i = 0; i < longTimeList.size(); i++) {
            newList.add(longTimeList.get(i).getKeyValue());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(context, R.layout.item_bottomdialog, newList);
        idListview.setAdapter(arrayAdapter);
    }

    public interface ChoiceItem {
        void itemClick(String choiceText, String code);
    }

    private ChoiceItem choiceItem;

    public void setOnChoiceItem(ChoiceItem choiceItem) {
        this.choiceItem = choiceItem;
    }
}
