package com.app.anmolenterprise.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.anmolenterprise.R;
import com.app.anmolenterprise.interfaces.OnCustomItemClicListener;
import com.app.anmolenterprise.model.ModelData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterNewLeads extends RecyclerView.Adapter<AdapterNewLeads.CustomViewHolder> {
    ArrayList<ModelData> detail;
    Context mContext;
    OnCustomItemClicListener listener;

    public AdapterNewLeads(Context context, OnCustomItemClicListener lis, ArrayList<ModelData> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_newlead, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {

        customViewHolder.text__name.setText(detail.get(i).getFull_name());
        customViewHolder.text__address.setText(detail.get(i).getAddress());
        customViewHolder.text__cmpnyname.setText(detail.get(i).getCompany_name());
        customViewHolder.text__date.setText(detail.get(i).getPurchase_date());
        customViewHolder.text__mobile.setText(detail.get(i).getMobile());

        if (!detail.get(i).getImage().equalsIgnoreCase("")) {
            Picasso.with(mContext).load(detail.get(i).getImage()).into(customViewHolder.image_bill);
            customViewHolder.image_bill.setVisibility(View.VISIBLE);
        }else {
            customViewHolder.image_bill.setVisibility(View.GONE);
        }
        customViewHolder.image_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onItemClickListener(i, 3);
            }
        });

        customViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onItemClickListener(i, 1);
            }
        });
        customViewHolder.text__complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onItemClickListener(i, 2);
            }
        });

    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView text__name, text__cmpnyname, text__mobile, text__address, text__date, text__complete;
        CardView card_view;
        ImageView image_bill;


        public CustomViewHolder(View view) {
            super(view);
            this.text__name = (TextView) view.findViewById(R.id.text__name);
            this.text__complete = (TextView) view.findViewById(R.id.text__complete);
            this.text__cmpnyname = (TextView) view.findViewById(R.id.text__cmpnyname);
            this.text__mobile = (TextView) view.findViewById(R.id.text__mobile);
            this.text__address = (TextView) view.findViewById(R.id.text__address);
            this.text__date = (TextView) view.findViewById(R.id.text__date);
            this.card_view = (CardView) view.findViewById(R.id.card_view);
            this.image_bill = (ImageView) view.findViewById(R.id.image_bill);
        }

    }


}