package com.app.schoolapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.schoolapp.R;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Hemanta on 21-04-2017.
 */
public class AdapterPhotoList extends RecyclerView.Adapter<AdapterPhotoList.CustomViewHolder> {

    ArrayList<File> detail;
    Context mContext;
    OnCustomItemClicListener listener;

    public AdapterPhotoList(Context context, OnCustomItemClicListener lis, ArrayList<File> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_event_photos, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        Log.e("onBindViewHolder", "onBindViewHolderCalled");

        Picasso.with(mContext).load(detail.get(position)).into(((CustomViewHolder) holder).image);

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickListener(position, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageView image, image_download;
        CardView card_view;

        public CustomViewHolder(View view) {
            super(view);
            Log.e("CustomViewHolder", "CustomViewHolderCalled");

            this.image = (ImageView) view.findViewById(R.id.image);
            this.image_download = (ImageView) view.findViewById(R.id.image_download);
            this.card_view = (CardView) view.findViewById(R.id.card_view);

        }

    }

}