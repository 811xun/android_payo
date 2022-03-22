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
import com.home.glx.uwallet.fragment.WalletFragment;
import com.home.glx.uwallet.request.StaticParament;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.List;

public class BankListAdapter extends RecyclerView.Adapter {

    private String accountId;
    private Context context;
    private LayoutInflater inflater;
    private List<BankDatas> listData = new ArrayList();
    private String addType = "";
    private View.OnClickListener onClickListener;
    private String cardNo;
    private int item_style = 0;
    private int look_type = 0;
    private boolean haveChange = false;

    public BankListAdapter(Context context, List<BankDatas> listData) {
        this.context = context;
        this.listData = listData;
        inflater = LayoutInflater.from(context);
    }

    public BankListAdapter(Context context, List<BankDatas> listData, String addType) {
        this.context = context;
        this.listData = listData;
        this.addType = addType;
        inflater = LayoutInflater.from(context);
    }

    public BankListAdapter(String accountId, Context context, List<BankDatas> listData, String cardNo, View.OnClickListener onClickListener) {
        this.accountId = accountId;
        this.context = context;
        this.listData = listData;
        this.cardNo = cardNo;
        this.onClickListener = onClickListener;
        inflater = LayoutInflater.from(context);
    }

    public void setStyle(int type) {
        this.item_style = type;
    }

    public void setLookType(int type) {
        this.look_type = type;
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (i) {
            case 123:
                return new AddBottom(inflater.inflate(R.layout.item_dialog_add_bank, viewGroup, false));
            case 143:
                return new ItemView(inflater.inflate(R.layout.item_dialog_bank, viewGroup, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int viewType = getItemViewType(i);
        switch (viewType) {
            case 123:
                ((AddBottom) viewHolder).binData();
                break;
            case 143:
                ((ItemView) viewHolder).binData(listData.get(i));
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
        private ImageView idItemBankImg;

        public ItemView(@NonNull View itemView) {
            super(itemView);
            idBankName = (TextView) itemView.findViewById(R.id.id_bank_name);
            idChoiceImg = (ImageView) itemView.findViewById(R.id.id_choice_img);
            idItemBankImg = (ImageView) itemView.findViewById(R.id.id_item_bank_img);


            if (item_style == StaticParament.GONE_ADD_CHECK) {
                idChoiceImg.setVisibility(View.GONE);
            } else if (item_style == StaticParament.GONE_ADD_BANK) {
                idChoiceImg.setVisibility(View.GONE);
            }
        }

        public void binData(final BankDatas bankDatas) {
            //切换字体
            TypefaceUtil.replaceFont(idBankName, "fonts/gilroy_regular.ttf");

            //加载银行图片
//            Glide.with(context).load("http://paytest-api.loancloud.cn/app/getBankLogo?bankName=" + bankDatas.getBankName() + "&type=1").into(idItemBankImg);
            // Glide.with(context).load(PublicTools.getBankLogoUrl(bankDatas.getBankName(), "1")).into(idItemBankImg);
            if (bankDatas.getVerified() != null) {
                if (bankDatas.getVerified().equals("0")) {
                    idBankName.setTextColor(context.getResources().getColor(R.color.black));
                } else if (bankDatas.getVerified().equals("1")) {
                    idBankName.setTextColor(context.getResources().getColor(R.color.green));
                } else if (bankDatas.getVerified().equals("2")) {
                    idBankName.setTextColor(context.getResources().getColor(R.color.red));
                }
            }
            if (look_type == 0) {
                //显示银行卡号
                if (bankDatas.getCardNo().length() > 4) {
                    String card = "****" + bankDatas.getCardNo().substring(bankDatas.getCardNo().length() - 4);
                    if (!haveChange && !TextUtils.isEmpty(cardNo) && cardNo.equals(card)) {
                        idChoiceImg.setImageResource(R.mipmap.choice_on);
                        bankDatas.setChoice(true);
                        if (onItemClick != null) {
                            onItemClick.choiceBank(bankDatas);
                        }
                    }
                    idBankName.setText(card);
                } else {
                    idBankName.setText(bankDatas.getCardNo());
                }
            } else {
                //显示银行名
                idBankName.setText(bankDatas.getBankName());
            }

            if (String.valueOf(bankDatas.getId()).equals(WalletFragment.accountId)) {
                idChoiceImg.setImageResource(R.mipmap.choice_on);
            } else {
                idChoiceImg.setImageResource(R.mipmap.repay_choice_of);
            }
            idChoiceImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WalletFragment.accountId = bankDatas.getId();
                    changeChoice(bankDatas);

                    if (onItemClick != null) {
                        onItemClick.choiceBank(bankDatas);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {
                        onItemClick.bankClick(bankDatas);
                    }
                }
            });
        }
    }

    private void changeChoice(BankDatas bankDatas) {
        for (int i = 0; i < listData.size(); i++) {
            listData.get(i).setChoice(false);
        }
        haveChange = true;
        bankDatas.setChoice(true);
        notifyDataSetChanged();
    }


    class AddBottom extends RecyclerView.ViewHolder {
        private TextView idQueding;
        private RelativeLayout idAddBank;
        private TextView idAddText;
        private TextView idAddBankTopText;

        public AddBottom(@NonNull View itemView) {
            super(itemView);

            idQueding = (TextView) itemView.findViewById(R.id.id_queding);
            idAddBank = (RelativeLayout) itemView.findViewById(R.id.id_add_bank);
            idAddText = (TextView) itemView.findViewById(R.id.id_add_text);
            idAddBankTopText = (TextView) itemView.findViewById(R.id.id_add_bank_top_text);

            if (item_style == StaticParament.GONE_ADD_CHECK) {
                itemView.setVisibility(View.GONE);
            } else if (item_style == StaticParament.GONE_ADD_BANK) {
                idAddBank.setVisibility(View.GONE);
            }
        }

        public void binData() {
            //切换字体
            TypefaceUtil.replaceFont(idAddText, "fonts/gilroy_regular.ttf");
            TypefaceUtil.replaceFont(idQueding, "fonts/gilroy_semiBold.ttf");
            if (addType.equals("1")) {
                //卡支付
                idAddText.setText(R.string.add_bank_card);
            } else {
                //账户支付
                idAddText.setText(R.string.add_bank_account);
            }

            idAddBank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {
                        onItemClick.addBank();
                    }
                }
            });
            if (onClickListener == null) {
                idQueding.setVisibility(View.GONE);
                idAddBankTopText.setVisibility(View.GONE);
            } else {
                idQueding.setVisibility(View.VISIBLE);
                //idAddBankTopText.setVisibility(View.VISIBLE);
                idQueding.setOnClickListener(onClickListener);
            }
        }
    }


    public interface OnItemClick {

        void choiceBank(BankDatas bankDatas);

        void bankClick(BankDatas bankDatas);

        void addBank();
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

}
