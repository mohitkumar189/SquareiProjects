package com.app.justclap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelService;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterReviews extends RecyclerView.Adapter<AdapterReviews.CustomViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterReviews(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {
        this.detail = list;
        this.mContext = context;
        this.listener = lis;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_review_adapter, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int position) {

        customViewHolder.text_desc.setText(detail.get(position).getReviewText());
        customViewHolder.text_title.setText(detail.get(position).getReviewSubject());
        customViewHolder.text_name.setText(detail.get(position).getUserName());

        customViewHolder.rl_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickListener(position, 11);
            }
        });

    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_background;
        TextView text_name, text_desc, text_title;

        public CustomViewHolder(View view) {
            super(view);
            this.text_desc = (TextView) view.findViewById(R.id.text_desc);
            this.text_title = (TextView) view.findViewById(R.id.text_title);
            this.text_name = (TextView) view.findViewById(R.id.text_name);
            this.rl_background = (RelativeLayout) view.findViewById(R.id.rl_background);
        }
    }


}