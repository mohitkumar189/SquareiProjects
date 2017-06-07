package com.app.justclap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.app.justclap.R;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelChat;
import com.app.justclap.utils.AppUtils;

import java.util.ArrayList;


/**
 * Created by admin on 26-11-2015.
 */
public class AdapterChatDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ModelChat> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;


    public AdapterChatDetail(Context context, OnCustomItemClicListener lis, ArrayList<ModelChat> messageList) {

        this.detail = messageList;
        this.mContext = context;
        this.listener = lis;

    }

    public static class senderHolder extends RecyclerView.ViewHolder {
        public TextView text_message, text_date, txtName;

        public senderHolder(View view) {
            super(view);
            text_message = (TextView) view.findViewById(R.id.text_message);
            text_date = (TextView) view.findViewById(R.id.text_date);
            txtName = (TextView) view.findViewById(R.id.text_name);
        }
    }

    public static class recieverHolder extends RecyclerView.ViewHolder {
        public TextView text_message, text_date, txtName;

        public recieverHolder(View view) {
            super(view);
            text_date = (TextView) view.findViewById(R.id.text_date);
            text_message = (TextView) view.findViewById(R.id.text_message);
            txtName = (TextView) view.findViewById(R.id.text_name);
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 1:
                View v1 = inflater.inflate(R.layout.row_sender_message, parent, false);
                viewHolder = new senderHolder(v1);
                break;
            case 2:
                View v2 = inflater.inflate(R.layout.row_reciever_message, parent, false);
                viewHolder = new recieverHolder(v2);
                break;

            default:
              /*  View v = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
                viewHolder = new RecyclerViewSimpleTextViewHolder(v);*/
                break;
        }
        return viewHolder;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case 1:
                senderHolder vh1 = (senderHolder) holder;
                // configureViewHolder1(vh1, position);
                ModelChat md1 = (ModelChat) detail.get(position);
                vh1.text_message.setText(md1.getMessage());
                vh1.text_date.setText(AppUtils.getTimeFromDateString(md1.getDate_time()));
                vh1.txtName.setText(md1.getSender_name());
                break;
            case 2:
                recieverHolder vh2 = (recieverHolder) holder;
                ModelChat md2 = (ModelChat) detail.get(position);
                vh2.text_message.setText(md2.getMessage());
                vh2.text_date.setText(AppUtils.getTimeFromDateString(md2.getDate_time()));
                vh2.txtName.setText(md2.getSender_name());
                break;
        }


    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text_date, text_message;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.text_date = (TextView) view.findViewById(R.id.text_date);
            this.text_message = (TextView) view.findViewById(R.id.text_message);

        }

        @Override
        public void onClick(View v) {

            listener.onItemClickListener(getPosition(), 1);

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (detail.get(position).getSender_id().equalsIgnoreCase(AppUtils.getUserIdChat(mContext))) {
            return 1;
        } else {
            return 2;
        }
    }


}