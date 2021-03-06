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

import java.util.ArrayList;
import java.util.List;

public class SelctBankListAdapter_zhifu extends RecyclerView.Adapter {
    private Context context;
    private LayoutInflater inflater;
    private int mposition = -1;
    private String selectId;
    private List<BankDatas> listData = new ArrayList();

    public SelctBankListAdapter_zhifu(Context context, List<BankDatas> listData, String selectId) {
        this.context = context;
        this.listData = listData;
        this.selectId = selectId;
        inflater = LayoutInflater.from(context);
    }

    public void notifyDataChanged(List<BankDatas> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    public void clearPosition() {
        mposition = -1;
        selectId = "";
    }

    @Override
    public int getItemViewType(int position) {
        if (position == listData.size()) {
            return 123;
        } else {
            return 143;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 123:
                return new AddBottom(inflater.inflate(R.layout.item_add_bank_zhifu, parent, false));
            case 143:
                return new ItemView(inflater.inflate(R.layout.item_select_bank_zhifu, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case 123:
                ((AddBottom) holder).binData();
                break;
            case 143:
                ((ItemView) holder).binData(position, listData.get(position));
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (listData == null) {
            return 0;
        }
        return listData.size() + 1;
    }

    class ItemView extends RecyclerView.ViewHolder {

        private TextView idBankName;
        private ImageView idChoiceImg;
        private ImageView id_item_bank_img1;
        private RelativeLayout itemRl;

        public ItemView(@NonNull View itemView) {
            super(itemView);
            itemRl = itemView.findViewById(R.id.item_rl);
            idBankName = (TextView) itemView.findViewById(R.id.id_bank_name);
            idChoiceImg = (ImageView) itemView.findViewById(R.id.id_choice_img);
            id_item_bank_img1 = (ImageView) itemView.findViewById(R.id.id_item_bank_img1);
        }

        public void binData(final int position, final BankDatas bankDatas) {
            //????????????
            TypefaceUtil.replaceFont(idBankName, "fonts/gilroy_regular.ttf");

            //??????????????????
//            Glide.with(context).load("http://paytest-api.loancloud.cn/app/getBankLogo?bankName=" + bankDatas.getBankName() + "&type=1").into(idItemBankImg);
            // Glide.with(context).load(PublicTools.getBankLogoUrl(bankDatas.getBankName(), "1")).into(idItemBankImg);
            id_item_bank_img1.setVisibility(View.VISIBLE);
            if (bankDatas.getCustomerCcType() != null) {
                if (bankDatas.getCustomerCcType().equals("10")) {
                    id_item_bank_img1.setImageResource(R.mipmap.pay_visa_logo_new);
                } else if (bankDatas.getCustomerCcType().equals("20")) {
                    id_item_bank_img1.setImageResource(R.mipmap.pay_master_logo_new);
                } else  if (bankDatas.getCustomerCcType().equals("60")){
                    id_item_bank_img1.setImageResource(R.mipmap.pay_onther_logo_new);
                }else {
                    id_item_bank_img1.setVisibility(View.INVISIBLE);
                }
            }
//            if (position==1){//??????
//                id_item_bank_img1.setVisibility(View.GONE);
//                idItemBankImg.setVisibility(View.VISIBLE);
//                idItemBankImg.setImageResource(R.mipmap.pay_onther_logo);
//            }
            //??????????????????
            if (bankDatas.getCardNo().length() > 4) {
                String card = bankDatas.getCardNo().replaceAll(" ", "");
                if (TextUtils.isEmpty(bankDatas.getCustomerCcExpmo()) || TextUtils.isEmpty(bankDatas.getCustomerCcExpyr())) {
                    card = "**** " + card.substring(card.length() - 4);
                } else {
                    card = "**** " + card.substring(card.length() - 4) + "(" + bankDatas.getCustomerCcExpmo() + "/" + bankDatas.getCustomerCcExpyr().substring(2, 4) + ")";
                }
                idBankName.setText(card);
            } else {
                idBankName.setText(bankDatas.getCardNo());
            }
            if (mposition == -1) {
                if (!TextUtils.isEmpty(selectId)) {
                    if (bankDatas.getId().equals(selectId)) {
                        idChoiceImg.setImageResource(R.mipmap.choice_orange_icon);
                        bankDatas.setChoice(true);
//                        onitemClick.itemClick(bankDatas);
                    } else {
                        idChoiceImg.setImageResource(R.mipmap.repay_choice_of);
                        bankDatas.setChoice(false);
                    }
                } else {
                    if (position == 0) {
                        idChoiceImg.setImageResource(R.mipmap.choice_orange_icon);
                        bankDatas.setChoice(true);
//                        onitemClick.itemClick(bankDatas);
                    } else {
                        idChoiceImg.setImageResource(R.mipmap.repay_choice_of);
                        bankDatas.setChoice(false);
                    }
                }
                /*if (bankDatas.getPreset().equals("1")) {
                    idChoiceImg.setImageResource(R.mipmap.choice_on);
                    bankDatas.setChoice(true);
                } else {
                    idChoiceImg.setImageResource(R.mipmap.repay_choice_of);
                    bankDatas.setChoice(false);
                }*/
            } else {
                if (position == mposition) {
                    idChoiceImg.setImageResource(R.mipmap.choice_orange_icon);
                    bankDatas.setChoice(true);
                } else {
                    idChoiceImg.setImageResource(R.mipmap.repay_choice_of);
                    bankDatas.setChoice(false);
                }
            }

            itemRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mposition != position) {
                        mposition = position;
                        notifyDataSetChanged();
                        onitemClick.itemClick(bankDatas);
                    }
                }
            });
        }
    }

    class AddBottom extends RecyclerView.ViewHolder {
        private RelativeLayout idAddBank;
        private TextView idAddText;

        public AddBottom(@NonNull View itemView) {
            super(itemView);

            idAddBank = (RelativeLayout) itemView.findViewById(R.id.id_add_bank);
            idAddText = (TextView) itemView.findViewById(R.id.id_add_text);
        }

        public void binData() {
            //????????????
            TypefaceUtil.replaceFont(idAddText, "fonts/gilroy_regular.ttf");
            idAddBank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onitemClick.addCard();
                }
            });
        }
    }

    public interface OnitemClick {
        void itemClick(BankDatas bankDatas);

        void saveCard();

        void addCard();
    }

    public OnitemClick onitemClick;

    public void setOnitemClick(OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }
}
