package com.app.justclap.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelWallet;
import com.app.justclap.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterWallet extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ModelWallet> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterWallet(Context context, OnCustomItemClicListener lis, ArrayList<ModelWallet> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_wallet, parent, false);

            vh = new CustomViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;

    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            this.progressBar.getIndeterminateDrawable().setColorFilter(Color.rgb(255, 133, 24), PorterDuff.Mode.MULTIPLY);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {

        if (holder instanceof CustomViewHolder) {

            ModelWallet m1 = (ModelWallet) detail.get(i);

            ((CustomViewHolder) holder).text_date.setText(detail.get(i).getCreatedDate());
            ((CustomViewHolder) holder).text_credit.setText("Rs." + m1.getCredit());
            ((CustomViewHolder) holder).text_debit.setText("Rs." + m1.getDebit());
            ((CustomViewHolder) holder).text_amount.setText("Rs." + m1.getBalanceAmount());
            ((CustomViewHolder) holder).text_transaction.setText(m1.getCustomerName());
            if (m1.getCheckRecharge().equalsIgnoreCase("3")) {
                ((CustomViewHolder) holder).text_transaction.setText(m1.getServiceName());
                Picasso.with(mContext).load(R.drawable.rs_placeholder)
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.rs_placeholder)
                        .into(((CustomViewHolder) holder).image_user);
                ((CustomViewHolder) holder).rl_background.setBackgroundColor(mContext.getResources().getColor(
                        R.color.light_green));

            } else {

                ((CustomViewHolder) holder).text_transaction.setText(m1.getCustomerName());
                Picasso.with(mContext).load(mContext.getString(R.string.img_url) + m1.getCustomerImage())
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.user)
                        .into(((CustomViewHolder) holder).image_user);
                ((CustomViewHolder) holder).rl_background.setBackgroundColor(mContext.getResources().getColor(
                        R.color.text_white));

            }


        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView text_date, text_credit, text_debit, text_amount, text_transaction;
        ImageView image_user;
        CardView card_view;
        RelativeLayout rl_background;

        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.text_date = (TextView) view.findViewById(R.id.text_date);
            this.text_credit = (TextView) view.findViewById(R.id.text_credit);
            this.text_debit = (TextView) view.findViewById(R.id.text_debit);
            this.text_amount = (TextView) view.findViewById(R.id.text_amount);
            this.image_user = (ImageView) view.findViewById(R.id.image_user);
            this.text_transaction = (TextView) view.findViewById(R.id.text_transaction);
            this.card_view = (CardView) view.findViewById(R.id.card_view);
            this.rl_background = (RelativeLayout) view.findViewById(R.id.rl_background);
        }

        @Override
        public void onClick(View v) {

            listener.onItemClickListener(getPosition(), 1);

        }


    }

    @Override
    public int getItemViewType(int position) {
        ModelWallet m1 = (ModelWallet) detail.get(position);
        if (detail.get(position).getRowType() == 1) {
            return VIEW_ITEM;
        } else if (detail.get(position).getRowType() == 2) {
            return VIEW_PROG;
        }
        return -1;
    }
}