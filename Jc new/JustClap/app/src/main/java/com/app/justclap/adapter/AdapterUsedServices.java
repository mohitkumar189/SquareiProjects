package com.app.justclap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelService;
import com.app.justclap.utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterUsedServices extends RecyclerView.Adapter<AdapterUsedServices.CustomViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterUsedServices(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_used_services, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int position) {
        customViewHolder.service_name.setText(detail.get(position).getCategoryName());
        customViewHolder.text_desc.setText(detail.get(position).getDescription());
        customViewHolder.text_discount.setText(detail.get(position).getServiceOffer());
        Picasso.with(mContext)
                .load(detail.get(position).getCategoryBGImage())
                .into(customViewHolder.img_service);
        customViewHolder.ratingbar.setRating(detail.get(position).getServiceRatting());
        customViewHolder.rl_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickListener(position, 3);
            }
        });
    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView service_name, text_desc, text_book_now, text_discount;
        ImageView img_service;
        RatingBar ratingbar;
        RelativeLayout rl_background;


        public CustomViewHolder(View view) {
            super(view);

            this.service_name = (TextView) view.findViewById(R.id.text_category);
            this.text_desc = (TextView) view.findViewById(R.id.text_desc);
            this.text_book_now = (TextView) view.findViewById(R.id.text_book_now);
            this.text_discount = (TextView) view.findViewById(R.id.text_discount);
            this.ratingbar = (RatingBar) view.findViewById(R.id.ratingbar);
            AppUtils.fontGotham_Book(service_name, mContext);
            AppUtils.fontGotham_Book(text_desc, mContext);
            AppUtils.fontGotham_Book(text_book_now, mContext);
            this.img_service = (ImageView) view.findViewById(R.id.imag_service);
            this.rl_background = (RelativeLayout) view.findViewById(R.id.rl_background);

        }


    }

    public void setFilter(ArrayList<ModelService> detailnew) {
        detail = new ArrayList<>();
        detail.addAll(detailnew);
        notifyDataSetChanged();
    }

    public ModelService getFilter(int i) {

        return detail.get(i);
    }

}