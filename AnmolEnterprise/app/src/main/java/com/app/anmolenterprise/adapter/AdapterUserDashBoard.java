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
public class AdapterUserDashBoard extends RecyclerView.Adapter<AdapterUserDashBoard.CustomViewHolder> {
    ArrayList<ModelData> detail;
    Context mContext;
    OnCustomItemClicListener listener;

    public AdapterUserDashBoard(Context context, OnCustomItemClicListener lis, ArrayList<ModelData> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_dashboard_service, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {

        customViewHolder.service_name.setText(detail.get(i).getCategory_name());
        customViewHolder.text_services_charge.setText(detail.get(i).getService_charge() + " Rs");

        Picasso.with(mContext)
                .load(detail.get(i).getImage())
                // .transform(new CircleTransform())
                .into(customViewHolder.image);

        customViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onItemClickListener(i, 1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView service_name, text_services_charge;
        ImageView image;
        CardView card_view;


        public CustomViewHolder(View view) {
            super(view);
            this.service_name = (TextView) view.findViewById(R.id.text_services_name);
            this.text_services_charge = (TextView) view.findViewById(R.id.text_services_charge);
            this.image = (ImageView) view.findViewById(R.id.image_background);
            this.card_view = (CardView) view.findViewById(R.id.card_view);

        }

    }


}