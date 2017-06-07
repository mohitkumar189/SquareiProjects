package com.app.justclap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.justclap.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterVendorHired extends RecyclerView.Adapter<AdapterVendorHired.CustomViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    int pos = 0;


    public AdapterVendorHired(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_hired_vendor, null);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        holder.text_response.setText(detail.get(position).getVendorName());
        holder.text_business.setText(detail.get(position).getServiceName());
        pos = position;
        holder.text_quote.setText(detail.get(position).getQuote());

        Picasso.with(mContext)
                .load(detail.get(position).getServiceImage())
                .into(holder.image_background);


        Picasso.with(mContext)
                .load(detail.get(position).getServiceImage())
                .into(holder.image_vendor);

        holder.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(position, 6);

            }
        });
        holder.image_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(position, 1);

            }
        });

    }


    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView text_response, text_business, text_viewDetail, text_cancelrequest, text_quote;
        ImageView image_background, img_menu, image_vendor;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.text_response = (TextView) view.findViewById(R.id.text_response);
            this.text_business = (TextView) view.findViewById(R.id.text_business);
            this.image_background = (ImageView) view.findViewById(R.id.image_background);
            this.text_viewDetail = (TextView) view.findViewById(R.id.text_viewDetail);
            this.text_cancelrequest = (TextView) view.findViewById(R.id.text_cancelrequest);
            this.text_quote = (TextView) view.findViewById(R.id.text_quote);
            this.img_menu = (ImageView) view.findViewById(R.id.img_menu);
            this.image_vendor = (ImageView) view.findViewById(R.id.image_vendor);
        }

        @Override
        public void onClick(View v) {

            listener.onItemClickListener(getPosition(), 2);


        }

    }


}

