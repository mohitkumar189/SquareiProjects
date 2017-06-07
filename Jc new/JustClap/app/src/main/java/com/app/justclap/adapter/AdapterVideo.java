package com.app.justclap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.justclap.R;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.CustomViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterVideo(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_video_adapter, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int position) {

        Picasso.with(mContext)
                .load(detail.get(position).getVideoThumbUrl())
                .into(customViewHolder.images_banner);

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
        ImageView images_banner;
        RelativeLayout rl_background;


        public CustomViewHolder(View view) {
            super(view);

            this.images_banner = (ImageView) view.findViewById(R.id.images_banner);
            this.rl_background = (RelativeLayout) view.findViewById(R.id.rl_background);
        }
    }


}