package com.home.glx.uwallet.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.home.glx.uwallet.R;
import com.home.glx.uwallet.datas.MessageData;
import com.home.glx.uwallet.selfview.SwipeView;
import com.home.glx.uwallet.tools.L;
import com.home.glx.uwallet.tools.PublicTools;
import com.home.glx.uwallet.tools.TypefaceUtil;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter {

    private Context context;
    private LayoutInflater inflater;
    private List<MessageData> listData = new ArrayList<>();

    public MessageAdapter(Context context, List<MessageData> listData) {
        this.context = context;
        this.listData = listData;
        inflater = LayoutInflater.from(context);
    }

    public void notifyData(List<MessageData> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemView(inflater.inflate(R.layout.item_message, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((ItemView) viewHolder).bindData(listData.get(i));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    class ItemView extends RecyclerView.ViewHolder {

        private TextView idTitle;
        private TextView idTime;
        private LinearLayout idMessageLay;
        private TextView idLookDetail;
        private Button delete;
        private View idReadView;
        private SwipeView swipeView;

        public ItemView(@NonNull View itemView) {
            super(itemView);
            swipeView = (SwipeView) itemView.findViewById(R.id.swipeview);
            idTitle = (TextView) itemView.findViewById(R.id.id_title);
            idTime = (TextView) itemView.findViewById(R.id.id_time);
            idLookDetail = (TextView) itemView.findViewById(R.id.id_look_detail);
            idMessageLay = (LinearLayout) itemView.findViewById(R.id.id_message_lay);
            delete = (Button) itemView.findViewById(R.id.delete);
            idReadView = (View) itemView.findViewById(R.id.id_read_view);
        }

        public void bindData(final MessageData messageData) {
//            idTitle.setText(messageData.getContent());
            //切换字体
            TypefaceUtil.replaceFont(idTitle,"fonts/gilroy_semiBold.ttf");
            TypefaceUtil.replaceFont(idTime,"fonts/gilroy_medium.ttf");
            idTitle.setText(messageData.getTitle());
            if (messageData.getCreateTimes() != null) {
                //idTime.setText(PublicTools.dateToString2(Long.parseLong(messageData.getCreatedDate())));
                idTime.setText(messageData.getCreateTimes());
            }

            if (messageData.getIsRead() == 1) {
                idTitle.setTextColor(context.getResources().getColor(R.color.text_gray));
                idReadView.setVisibility(View.INVISIBLE);
            } else {
                idTitle.setTextColor(context.getResources().getColor(R.color.text_black));
                idReadView.setVisibility(View.VISIBLE);
            }

            idMessageLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    L.log("消息点击");
                    if (onItemClick != null) {
                        onItemClick.itemClick(messageData);
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    L.log("删除点击");
                    if (onItemClick != null) {
                        swipeView.close();
                        onItemClick.itemDeleteClick(messageData);
                    }
                }
            });
        }
    }

    public interface OnItemClick {
        void itemClick(MessageData messageData);

        void itemDeleteClick(MessageData messageData);
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

}
