package com.home.glx.uwallet.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.BankDatas;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.List;


public class CardBankReListAdapter extends RecyclerView.Adapter {
    private List<BankDatas> data;
    private Context context;
    private LayoutInflater inflater;

    public CardBankReListAdapter(Context context, List<BankDatas> data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void notifyDataChanged(List<BankDatas> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()) {
            return 166;
        } else {
            return 156;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 156) {
            return new BottomView(inflater.inflate(R.layout.item_bank_card_more, parent, false));
        } else {
            return new ItemView(inflater.inflate(R.layout.item_bank_card, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == 156){
            ((BottomView) holder).bindData(data.get(position));
        } else {
            ((ItemView) holder).bindData();
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    class BottomView extends RecyclerView.ViewHolder {

        private TextView cardNo;
        private TextView dueTime;
        private RelativeLayout card;
        private ImageView logo;
        private ImageView defaultIcon;
        private TextView dateExp;

        public BottomView(@NonNull View itemView) {
            super(itemView);
        }

        public void bindData(final BankDatas bankData) {
            card = (RelativeLayout) itemView.findViewById(R.id.card_rl);
            cardNo = (TextView) itemView.findViewById(R.id.card_number);
            dueTime = (TextView) itemView.findViewById(R.id.time_text);
            logo = (ImageView) itemView.findViewById(R.id.card_logo);
            dateExp = itemView.findViewById(R.id.date_exp);
            defaultIcon = itemView.findViewById(R.id.default_icon);

            //切换字体
            TypefaceUtil.replaceFont(cardNo,"fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(dateExp,"fonts/gilroy_medium.ttf");
            TypefaceUtil.replaceFont(dueTime,"fonts/gilroy_medium.ttf");
           /* if (Integer.parseInt(bankData.getOrder()) % 3 == 1) {
                card.setBackgroundResource(R.mipmap.card_orange_background);
            } else if (Integer.parseInt(bankData.getOrder()) % 3 == 2) {
                card.setBackgroundResource(R.mipmap.yellow_card_background);
            } else {
                card.setBackgroundResource(R.mipmap.black_card_background);
            }*/
            cardNo.setText(bankData.getCardNo());
            if (!TextUtils.isEmpty(bankData.getCustomerCcExpmo()) && !TextUtils.isEmpty(bankData.getCustomerCcExpyr())) {
                dueTime.setText(bankData.getCustomerCcExpmo() + "/" + bankData.getCustomerCcExpyr().substring(2));
            }
            if (bankData.getCustomerCcType() != null) {//10、VISA, 20、MAST, 30、 SWITCH,  40、SOLO,  50、DELTA, 60、 AMEX,
                if (bankData.getCustomerCcType().equals("10")) {
                    logo.setImageResource(R.mipmap.card_visa_logo);
                    card.setBackgroundResource(R.mipmap.card_visa_background);
                } else if (bankData.getCustomerCcType().equals("20")) {
                    logo.setImageResource(R.mipmap.card_master_logo);
                    card.setBackgroundResource(R.mipmap.card_master_background);
                } else if (bankData.getCustomerCcType().equals("60")){
                    logo.setImageResource(R.mipmap.card_ame_logo1);
                    card.setBackgroundResource(R.mipmap.card_ame_logo);
                }else {
                    logo.setVisibility(View.INVISIBLE);
                    card.setBackgroundResource(R.mipmap.card_ame_logo);
                }
            }
            if (bankData.getPreset().equals("1") || data.size() == 1) {
                defaultIcon.setVisibility(View.VISIBLE);
            } else {
                defaultIcon.setVisibility(View.INVISIBLE);
            }
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //onitemClick.itemClick();
                    onitemClick.itemChoice(bankData);
                }
            });
        }
    }

    class ItemView extends RecyclerView.ViewHolder {

        private RelativeLayout card;
        private TextView tv;

        public ItemView(@NonNull View itemView) {
            super(itemView);
            card = (RelativeLayout) itemView.findViewById(R.id.card_rl);
            tv = itemView.findViewById(R.id.add_text);
            TypefaceUtil.replaceFont(tv,"fonts/gilroy_medium.ttf");
//            if (data.size() == 0) {
//                tv.setText("Add bank card");
//            }
        }

        public void bindData() {
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onitemClick.addCard();
                }
            });
        }
    }


    public interface OnitemClick {
        void itemClick(String cardNo, String id);
        void itemChoice(BankDatas bankDatas);
        void addCard();
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }
}
