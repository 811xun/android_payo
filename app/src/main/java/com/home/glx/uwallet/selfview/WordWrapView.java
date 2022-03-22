package com.home.glx.uwallet.selfview;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.ChoiceDatas;
import com.home.glx.uwallet.datas.GetChoiceDatas;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hxl
 */
public class WordWrapView extends ViewGroup {
    private static final int PADDING_HOR = 25;//子view水平方向padding
    private static final int PADDING_VERTICAL = 10;//子view垂直方向padding
    private static final int TEXT_MARGIN_HOR = 40;//子view之间的水平间距
    private static final int TEXT_MARGIN_VERTICAL = 40;//行间距

    private int num = 0;//最多字个数
    private int allChoice;
    private boolean searchTag = false;
    //WordWrapView两边距屏幕宽度
    private int marginHorizontal = 0;
    /**
     * @param context
     */
    public WordWrapView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public WordWrapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    /**
     * @param context
     * @param attrs
     */
    public WordWrapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int autualWidth = r - l;//当前容器宽度
        int x = 0;// 横坐标开始
        int y = 0;//纵坐标开始
        int rows = 1;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            int width = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            x += width + TEXT_MARGIN_HOR;
            if (x > autualWidth - TEXT_MARGIN_HOR) {//判断累积高度
                if (i != 0) {
                    x = width + TEXT_MARGIN_HOR;
                    rows++;
                }

                //当一个子view长度超出父view长度时
                if (x > autualWidth - TEXT_MARGIN_HOR) {//判断单个高度
                    //如果子view是textview的话处理文字
                    if (view instanceof TextView) {
                        TextView tv = ((TextView) view);
                        if (num == 0) {//只计算一次
                            int wordNum = tv.getText().toString().length();
                            num = wordNum * (autualWidth - 2 * TEXT_MARGIN_HOR) / width - 1;
                        }
                        String text = tv.getText().toString();
                        text = text.substring(0, num) + "...";
                        tv.setText(text);
                    }

                    x = autualWidth - TEXT_MARGIN_HOR;
                    width = autualWidth - (2 * TEXT_MARGIN_HOR);
                }
            }
            y = rows * (height + TEXT_MARGIN_VERTICAL);
            view.layout(x - width, y - height, x, y);

        }
    }

    public float getCharacterWidth(String text, float size) {
        if (null == text || "".equals(text))
            return 0;
        float width = 0;
        Paint paint = new Paint();
        paint.setTextSize(size);
        float text_width = paint.measureText(text);// 得到总体长度
        width = text_width / text.length();// 每一个字符的长度

        return width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int x = 0;//横坐标
        int y = 0;//纵坐标
        int rows = 1;//总行数
        int specWidth = MeasureSpec.getSize(widthMeasureSpec) - marginHorizontal;
        int actualWidth = specWidth;//实际宽度
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++) {
            View child = getChildAt(index);
            if (searchTag) {
                child.setPadding(2*PADDING_HOR, 2*PADDING_VERTICAL, 2*PADDING_HOR, 2*PADDING_VERTICAL);
            } else {
                child.setPadding(PADDING_HOR, PADDING_VERTICAL, PADDING_HOR, PADDING_VERTICAL);
            }
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            x += width + TEXT_MARGIN_HOR;
            if (x > actualWidth) {//换行
                if (index != 0) {
                    x = width + TEXT_MARGIN_HOR;
                    rows++;
                }
            }
            y = rows * (height + TEXT_MARGIN_VERTICAL);
        }
        setMeasuredDimension(actualWidth, y + TEXT_MARGIN_VERTICAL);
    }

    /**
     * 设置数据
     *
     * @param datas
     */
    public void setMapTagList(final List<String> datas) {
        removeAllViews();
        for (int i = 0; i < datas.size(); i++) {
            if (!TextUtils.isEmpty(datas.get(i))) {
                final TextView textview = new TextView(getContext());
                textview.setBackgroundResource(R.drawable.view_map_tag_background);
                textview.setTextSize(11);
                textview.setWidth(PublicTools.dip2px(getContext(),59));
                //textview.setSingleLine();
                textview.setMaxLines(1);
                textview.setEllipsize(TextUtils.TruncateAt.END);
                textview.setTextColor(getContext().getResources().getColor(R.color.text_gray3));
                textview.setText(datas.get(i));
                final int finalI = i;
                addView(textview);
            }
        }
    }

    /**
     * 设置数据
     *
     * @param datas
     */
    public void setSearchCategoriesList(int marginHorizontal, final List<ChoiceDatas> datas) {
        this.marginHorizontal = marginHorizontal;
        searchTag = true;
        removeAllViews();
        for (int i = 0; i < datas.size(); i++) {
            if (!TextUtils.isEmpty(datas.get(i).getEName())) {
                final TextView textview = new TextView(getContext());
                textview.setBackgroundResource(R.drawable.view_search_categories);
                textview.setTextSize(13);
                //textview.setWidth(PublicTools.dip2px(getContext(),59));
                //textview.setSingleLine();
                textview.setMaxLines(1);
                textview.setEllipsize(TextUtils.TruncateAt.END);
                textview.setTextColor(getContext().getResources().getColor(R.color.colorBackGround));
                textview.setText(datas.get(i).getEName());
                textview.setTag(datas.get(i).getValue());
                TypefaceUtil.replaceFont(textview,"fonts/gilroy_semiBold.ttf");
                textview.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickList.click(textview);
                    }
                });
                final int finalI = i;
                addView(textview);
            }
        }
    }

    /**
     * 设置数据
     *
     * @param datas
     */
    public void setSearchTopList(int marginHorizontal, final List<String> datas) {
        this.marginHorizontal = marginHorizontal;
        removeAllViews();
        for (int i = 0; i < datas.size(); i++) {
            if (!TextUtils.isEmpty(datas.get(i))) {
                final TextView textview = new TextView(getContext());
                textview.setTextSize(15);
                //textview.setWidth(PublicTools.dip2px(getContext(),59));
                //textview.setSingleLine();
                textview.setMaxLines(1);
                textview.setEllipsize(TextUtils.TruncateAt.END);
                textview.setTextColor(getContext().getResources().getColor(R.color.colorBackGround));
                textview.setText("#" + datas.get(i));
                TypefaceUtil.replaceFont(textview,"fonts/gilroy_medium.ttf");

                textview.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickList.click(textview);
                    }
                });
                final int finalI = i;
                addView(textview);
            }
        }
    }

    /**
     * 设置数据
     *
     * @param datas
     */
    public void setTagList(final List<String> datas) {
        removeAllViews();
        for (int i = 0; i < datas.size(); i++) {
            if (!TextUtils.isEmpty(datas.get(i))) {
                final TextView textview = new TextView(getContext());
                textview.setBackgroundResource(R.drawable.view_tag_background);
                textview.setTextSize(10);

                textview.setTextColor(getContext().getResources().getColor(R.color.text_gray3));
                textview.setText(datas.get(i));
                final int finalI = i;
                addView(textview);
            }
        }
    }

    /**
     * 设置数据
     *
     * @param datas
     */
    public void setMerchantInfoTags(final List<String> datas) {
        removeAllViews();
        for (int i = 0; i < datas.size(); i++) {
            if (!TextUtils.isEmpty(datas.get(i))) {
                final TextView textview = new TextView(getContext());
                textview.setBackgroundResource(R.drawable.view_btn_4orange);
                textview.setTextSize(11);

                textview.setTextColor(getContext().getResources().getColor(R.color.white));
                textview.setText(datas.get(i));
                final int finalI = i;
                addView(textview);
            }
        }
    }

    /**
     * 设置数据
     *
     * @param datas
     */
    public void setChoiceTag(final List<String> datas) {
        removeAllViews();
        for (int i = 0; i < datas.size(); i++) {
            final TextView textview = new TextView(getContext());
            textview.setBackgroundResource(R.drawable.view_yj_white);
            textview.setTextSize(14);

            textview.setTextColor(getContext().getResources().getColor(R.color.text_black));
            textview.setText(datas.get(i));
            final int finalI = i;
            textview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(), datas.get(finalI), Toast.LENGTH_SHORT).show();
                    if (choiceText.contains(textview)) {
                        onChoiceList.unChoice(textview);
                        choiceText.remove(textview);
                        textview.setBackgroundResource(R.drawable.view_yj_white);
                        textview.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    } else {
                        if (textview != null) {
                            onChoiceList.choice(textview);
                        }
                        if (getAllChoice() > 1) {
                            //Toast.makeText(getContext(), "getContext().getString(R.string.must_more_keywork)", Toast.LENGTH_SHORT).show();
                        } else {
                            choiceText.add(textview);
//                            textview.setBackgroundResource(R.drawable.view_yj_cheng);
//                            textview.setTextColor(getContext().getResources().getColor(R.color.white));
                        }
                    }
                }
            });
            addView(textview);
        }
    }

    /**
     * 设置数据
     *
     * @param datas
     */
    public void setTextList(final List<String> datas) {
        removeAllViews();
        for (int i = 0; i < datas.size(); i++) {
            final TextView textview = new TextView(getContext());
            textview.setBackgroundResource(R.drawable.view_yj_white);
            textview.setTextSize(14);

            textview.setTextColor(getContext().getResources().getColor(R.color.text_black));
            textview.setText(datas.get(i));
            final int finalI = i;
            textview.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(getContext(), datas.get(finalI), Toast.LENGTH_SHORT).show();
                    if (choiceText.contains(textview)) {
                        onChoiceList.unChoice(textview);
                        choiceText.remove(textview);
                        textview.setBackgroundResource(R.drawable.view_yj_white);
                        textview.setTextColor(getContext().getResources().getColor(R.color.text_black));
                    } else {
                        if (textview != null) {
                            onChoiceList.choice(textview);
                        }
                        if (getAllChoice() > 7) {
                            //Toast.makeText(getContext(), "getContext().getString(R.string.must_more_keywork)", Toast.LENGTH_SHORT).show();
                        } else {
                            choiceText.add(textview);
                            textview.setBackgroundResource(R.drawable.view_yj_cheng);
                            textview.setTextColor(getContext().getResources().getColor(R.color.white));
                        }
                    }
                }
            });
            addView(textview);
        }
    }

    public void setAllChoice(int allChoice) {
        this.allChoice = allChoice;
    }

    public int getAllChoice() {
        return this.allChoice;
    }

    /**
     * 设置默认选中
     *
     * @param datas
     */
    public void setChoiceData(List<String> datas) {
        int childNum = getChildCount();
        for (int i = 0; i < childNum; i++) {
            TextView textview = (TextView) getChildAt(i);
            for (int t = 0; t < datas.size(); t++) {
                if (textview.getText().toString().equals(datas.get(t))) {
                    onChoiceList.choice(textview);
                    choiceText.add(textview);
                    textview.setBackgroundResource(R.drawable.view_yj_cheng);
                    textview.setTextColor(getContext().getResources().getColor(R.color.white));
                }
            }
        }
    }


    private List<TextView> choiceText = new ArrayList<>();

    public List<TextView> getChoiceText() {
        L.log("选中的关键词：" + choiceText.size());
        return choiceText;
    }

    public interface OnChoiceList {
        void choices(List<TextView> choiceText);
        void choice(TextView choiceText);
        void unChoice(TextView choiceText);
    }

    private OnChoiceList onChoiceList;

    public void setOnChoiceList(OnChoiceList onChoiceList) {
        this.onChoiceList = onChoiceList;
    }


    public interface OnClickList {
        void click(TextView choiceText);
    }

    private OnClickList onClickList;

    public void setOnClickList(OnClickList onClickList) {
        this.onClickList = onClickList;
    }

}
