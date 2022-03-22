package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

public class UwalletFilterDialog implements View.OnClickListener {
    private Context context;
    private MyDialog dialog;
    private TextView done;
    private TextView chooseDate;
    private TextView chooseAmount;
    private ConstraintLayout first;
    private ConstraintLayout dateCl;
    private TextView chooseFro;
    private TextView chooseTo;
    private TextView chooseAnyDate;
    private TextView chooseLast50;
    private TextView chooseLast100;
    private TextView chooseFroTo;
    private ImageView chooseAnyDateImg;
    private ImageView chooseLast50Img;
    private ImageView chooseLast100Img;
    private ImageView chooseFroToImg;
    private ConstraintLayout amountCl;
    private ConstraintLayout minMaxCl;
    private TextView chooseAnyAmount;
    private TextView chooseMinMax;
    private ImageView chooseAnyAmountImg;
    private ImageView chooseMinMaxImg;
    private ConstraintLayout froToCl;
    private EditText chooseMin;
    private EditText chooseMax;
    private TextView chooseDateTv;
    private TextView chooseAmountTv;
    private ImageView back;
    private TextView title;
    private TextView fro;
    private TextView to;
    private TextView min;
    private TextView max;
    private String chooseDateStr;
    private String chooseAmountStr;

    public UwalletFilterDialog(Context context, String chooseDateStr, String chooseAmountStr) {
        this.context = context;
        this.chooseDateStr = chooseDateStr;
        this.chooseAmountStr = chooseAmountStr;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.any_date:
                froToCl.setVisibility(View.GONE);
                chooseAnyDateImg.setImageResource(R.mipmap.wallet_choice_icon);
                chooseAnyDateImg.setTag("1");
                chooseLast50Img.setTag("0");
                chooseLast100Img.setTag("0");
                chooseFroToImg.setTag("0");
                chooseLast50Img.setImageResource(R.mipmap.un_choice_icon);
                chooseLast100Img.setImageResource(R.mipmap.un_choice_icon);
                chooseFroToImg.setImageResource(R.mipmap.un_choice_icon);
                break;
            case R.id.last_50:
                froToCl.setVisibility(View.GONE);
                chooseLast50Img.setImageResource(R.mipmap.wallet_choice_icon);
                chooseLast50Img.setTag("1");
                chooseAnyDateImg.setTag("0");
                chooseLast100Img.setTag("0");
                chooseFroToImg.setTag("0");
                chooseAnyDateImg.setImageResource(R.mipmap.un_choice_icon);
                chooseLast100Img.setImageResource(R.mipmap.un_choice_icon);
                chooseFroToImg.setImageResource(R.mipmap.un_choice_icon);
                break;
            case R.id.last_100:
                froToCl.setVisibility(View.GONE);
                chooseLast100Img.setImageResource(R.mipmap.wallet_choice_icon);
                chooseLast100Img.setTag("1");
                chooseAnyDateImg.setTag("0");
                chooseLast50Img.setTag("0");
                chooseFroToImg.setTag("0");
                chooseAnyDateImg.setImageResource(R.mipmap.un_choice_icon);
                chooseLast50Img.setImageResource(R.mipmap.un_choice_icon);
                chooseFroToImg.setImageResource(R.mipmap.un_choice_icon);
                break;

            case R.id.choose_date:
                froToCl.setVisibility(View.VISIBLE);
                chooseFroToImg.setImageResource(R.mipmap.wallet_choice_icon);
                chooseFroToImg.setTag("1");
                chooseAnyDateImg.setTag("0");
                chooseLast50Img.setTag("0");
                chooseLast100Img.setTag("0");
                chooseAnyDateImg.setImageResource(R.mipmap.un_choice_icon);
                chooseLast50Img.setImageResource(R.mipmap.un_choice_icon);
                chooseLast100Img.setImageResource(R.mipmap.un_choice_icon);
                break;
            case R.id.date_range:
                //选择日期分类
                first.setVisibility(View.GONE);
                dateCl.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                chooseFroToImg.setImageResource(R.mipmap.un_choice_icon);
                chooseAnyDateImg.setImageResource(R.mipmap.un_choice_icon);
                chooseLast50Img.setImageResource(R.mipmap.un_choice_icon);
                chooseLast100Img.setImageResource(R.mipmap.un_choice_icon);
                chooseFroToImg.setTag("0");
                chooseAnyDateImg.setTag("0");
                chooseLast50Img.setTag("0");
                chooseLast100Img.setTag("0");
                if (!TextUtils.isEmpty(chooseDateStr)) {
                    if (chooseDateStr.equals("Any")) {
                        chooseAnyDateImg.setImageResource(R.mipmap.wallet_choice_icon);
                        chooseAnyDateImg.setTag("1");
                    } else if (chooseDateStr.equals("Last 50 days")) {
                        chooseLast50Img.setImageResource(R.mipmap.wallet_choice_icon);
                        chooseLast50Img.setTag("1");
                    } else if (chooseDateStr.equals("Last 100 days")) {
                        chooseLast100Img.setImageResource(R.mipmap.wallet_choice_icon);
                        chooseLast100Img.setTag("1");
                    } else if (chooseDateStr.contains("-")) {
                        String[] date = chooseDateStr.split("-");
                        froToCl.setVisibility(View.VISIBLE);
                        chooseFroToImg.setImageResource(R.mipmap.wallet_choice_icon);
                        chooseFroToImg.setTag("1");
                        chooseFro.setText(date[0]);
                        chooseTo.setText(date[1]);
                    }
                }
                break;
            case R.id.amount:
                //选择金钱分类
                amountCl.setVisibility(View.VISIBLE);
                first.setVisibility(View.GONE);
                dateCl.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
                chooseAnyAmountImg.setTag("0");
                chooseMinMaxImg.setTag("0");
                minMaxCl.setVisibility(View.GONE);
                chooseMinMaxImg.setImageResource(R.mipmap.un_choice_icon);
                chooseAnyAmountImg.setImageResource(R.mipmap.un_choice_icon);
                if (!TextUtils.isEmpty(chooseAmountStr)) {
                    if (chooseAmountStr.equals("Any")) {
                        chooseAnyAmountImg.setImageResource(R.mipmap.wallet_choice_icon);
                        chooseAnyAmountImg.setTag("1");
                    } else if (chooseAmountStr.contains("-")) {
                        String[] amount = chooseAmountStr.split("-");
                        minMaxCl.setVisibility(View.VISIBLE);
                        chooseMinMaxImg.setImageResource(R.mipmap.wallet_choice_icon);
                        chooseMinMaxImg.setTag("1");
                        chooseMin.setText(amount[0]);
                        chooseMax.setText(amount[1]);
                    }
                }

                break;
            case R.id.any_amount:
                //选择所有金钱
                chooseAnyAmountImg.setImageResource(R.mipmap.wallet_choice_icon);
                chooseAnyAmountImg.setTag("1");
                chooseMinMaxImg.setImageResource(R.mipmap.un_choice_icon);
                chooseMinMaxImg.setTag("0");
                minMaxCl.setVisibility(View.GONE);
                break;
            case R.id.min_max:
                //选择金钱区间
                chooseMinMaxImg.setImageResource(R.mipmap.wallet_choice_icon);
                chooseMinMaxImg.setTag("1");
                chooseAnyAmountImg.setImageResource(R.mipmap.un_choice_icon);
                chooseAnyAmountImg.setTag("0");
                minMaxCl.setVisibility(View.VISIBLE);
                break;
            case R.id.done:
                if (choose != null) {
                    if (dateCl.getVisibility() == View.VISIBLE) {
                        if (chooseLast100Img.getTag().toString().equals("1")) {
                            choose.onChooseDate("Last 100 days");
                        }
                        if (chooseLast50Img.getTag().toString().equals("1")) {
                            choose.onChooseDate("Last 50 days");
                        }
                        if (chooseAnyDateImg.getTag().toString().equals("1")) {
                            choose.onChooseDate("Any");
                        }
                        if (chooseFroToImg.getTag().toString().equals("1")) {
                            if (TextUtils.isEmpty(chooseFro.getText().toString())) {
                                Toast.makeText(context, "Please enter start date.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (TextUtils.isEmpty(chooseTo.getText().toString())) {
                                Toast.makeText(context, "Please enter end date.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            choose.onChooseDate(chooseFro.getText().toString() + "-" + chooseTo.getText().toString());
                        }
                    }

                    if (amountCl.getVisibility() == View.VISIBLE) {
                        if (chooseAnyAmountImg.getTag().equals("1")) {
                            choose.onChooseAmont("Any");
                        }
                        if (chooseMinMaxImg.getTag().toString().equals("1")) {
                            if (TextUtils.isEmpty(chooseMin.getText().toString())) {
                                Toast.makeText(context, "Please enter min. amount.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (TextUtils.isEmpty(chooseMax.getText().toString())) {
                                Toast.makeText(context, "Please enter max. amount.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (Integer.parseInt(chooseMax.getText().toString()) < Integer.parseInt(chooseMin.getText().toString())) {
                                Toast.makeText(context, "The max. amount should be more than the min. amount.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            choose.onChooseAmont(chooseMin.getText().toString() + "-" + chooseMax.getText().toString());
                        }
                    }
                }
                dialog.dismiss();
                break;
            case R.id.back:
                amountCl.setVisibility(View.GONE);
                first.setVisibility(View.VISIBLE);
                dateCl.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
                break;
        }
    }

    public interface Choose {
        void onChooseDate(String str);
        void onChooseAmont(String str);

    }

    public Choose choose;

    public void setOnClose(Choose choose) {
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
            dialog = new MyDialog(context, R.style.fillDialog, R.layout.dialog_uwallet_filter);
            dialog.show();
            PublicTools.setDialogSizeFill(context, dialog);
            dialog.setCancelable(false);
            done = dialog.findViewById(R.id.done);
            fro = dialog.findViewById(R.id.fro);
            to = dialog.findViewById(R.id.to);
            title = dialog.findViewById(R.id.title);
            chooseDate = dialog.findViewById(R.id.date_range);
            chooseAmount = dialog.findViewById(R.id.amount);
            first = dialog.findViewById(R.id.first_cl);
            dateCl = dialog.findViewById(R.id.date_cl);
            chooseFro = dialog.findViewById(R.id.choose_fro);
            chooseTo = dialog.findViewById(R.id.choose_to);
            chooseAnyDate = dialog.findViewById(R.id.any_date);
            chooseLast50 = dialog.findViewById(R.id.last_50);
            chooseLast100 = dialog.findViewById(R.id.last_100);
            chooseFroTo = dialog.findViewById(R.id.choose_date);
            chooseAnyDateImg = dialog.findViewById(R.id.any_img);
            chooseLast50Img = dialog.findViewById(R.id.last_50_img);
            chooseLast100Img = dialog.findViewById(R.id.last_100_img);
            chooseFroToImg = dialog.findViewById(R.id.fro_to_img);
            chooseDateTv = dialog.findViewById(R.id.choose_date_str);
            chooseAmountTv = dialog.findViewById(R.id.choose_amount_str);
            amountCl = dialog.findViewById(R.id.amount_cl);
            minMaxCl = dialog.findViewById(R.id.min_max_cl);
            chooseAnyAmount = dialog.findViewById(R.id.any_amount);
            chooseMinMax = dialog.findViewById(R.id.min_max);
            chooseAnyAmountImg = dialog.findViewById(R.id.any_amount_img);
            chooseMinMaxImg = dialog.findViewById(R.id.min_max_img);
            chooseMin = dialog.findViewById(R.id.choose_min);
            chooseMax = dialog.findViewById(R.id.choose_max);
            back = dialog.findViewById(R.id.back);
            min = dialog.findViewById(R.id.min);
            max = dialog.findViewById(R.id.max);

            TypefaceUtil.replaceFont(title,"fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(done,"fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(chooseDate,"fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(chooseDateTv,"fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(chooseAmount,"fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(chooseAmountTv,"fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(chooseAnyDate,"fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(chooseLast50,"fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(chooseLast100,"fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(chooseFroTo,"fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(fro,"fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(to,"fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(chooseFro,"fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(chooseTo,"fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(chooseAnyAmount,"fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(chooseMinMax,"fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(chooseMin,"fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(chooseMax,"fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(min,"fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(max,"fonts/gilroy_medium.ttf");

            java.util.Calendar cal = java.util.Calendar.getInstance();
            int year = cal.get(java.util.Calendar.YEAR);
            int month = cal.get(java.util.Calendar.MONTH )+1;
            int day = cal.get(java.util.Calendar.DAY_OF_MONTH);

            if (!TextUtils.isEmpty(chooseDateStr)) {
                if (chooseDateStr.equals("Any")) {
                    chooseDateTv.setText("Any");
                } else {
                    if (chooseDateStr.contains("-")) {
                        String slectDate[] = chooseDateStr.split("-");
                        if (slectDate[0].equals(slectDate[1])) {
                            String[] date0 = slectDate[0].split("/");
                            if (Integer.parseInt(date0[0]) == day && Integer.parseInt(date0[1]) == month &&
                                    Integer.parseInt(date0[2]) == year) {
                                chooseDateTv.setText("today");
                            } else {
                                chooseDateTv.setText(slectDate[0]);
                            }
                        } else {
                            String[] date0 = slectDate[1].split("/");
                            if (Integer.parseInt(date0[0]) == day && Integer.parseInt(date0[1]) == month &&
                                    Integer.parseInt(date0[2]) == year) {
                                chooseDateTv.setText(slectDate[0] + "-" + "today");
                            } else {
                                chooseDateTv.setText(chooseDateStr);
                            }
                        }
                    } else {
                        chooseDateTv.setText(chooseDateStr);
                    }
                }
            } else {
                chooseDateStr = "Any";
                chooseDateTv.setText("Any");
            }
            if (!TextUtils.isEmpty(chooseAmountStr)) {
                if (chooseAmountStr.contains("-")) {
                    String amounts[] = chooseAmountStr.split("-");
                    chooseAmountTv.setText("$" + amounts[0] + " - " + "$" + amounts[1]);
                } else {
                    chooseAmountTv.setText(chooseAmountStr);
                }
            } else {
                chooseAmountStr = "Any";
                chooseAmountTv.setText("Any");
            }

            froToCl = dialog.findViewById(R.id.fro_to);
            chooseAmount.setOnClickListener(this);
            back.setOnClickListener(this);
            chooseAnyDate.setOnClickListener(this);
            chooseLast50.setOnClickListener(this);
            chooseLast100.setOnClickListener(this);
            chooseFroTo.setOnClickListener(this);
            done.setOnClickListener(this);
            chooseDate.setOnClickListener(this);
            chooseAnyAmount.setOnClickListener(this);
            chooseMinMax.setOnClickListener(this);
            chooseFro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectDateDialog selectDateDialog = new SelectDateDialog(context, chooseFro.getText().toString(), "");
                    selectDateDialog.setOnChoose(new SelectDateDialog.Choose() {
                        @Override
                        public void onChoose(String date) {
                            chooseFro.setText(date);
                            chooseTo.setText("");
                        }
                    });
                    selectDateDialog.showDialog();
                }
            });

            chooseTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(chooseFro.getText().toString())) {
                        Toast.makeText(context, "Please choose start date first.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SelectDateDialog selectDateDialog = new SelectDateDialog(context, chooseTo.getText().toString(), chooseFro.getText().toString());
                    selectDateDialog.setOnChoose(new SelectDateDialog.Choose() {
                        @Override
                        public void onChoose(String date) {
                            chooseTo.setText(date);
                        }
                    });
                    selectDateDialog.showDialog();
                }
            });

        } catch (Exception e) {

        }
    }

}
