package com.app.justclap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.interfaces.OnCustomItemClicListener;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterCityList extends RecyclerView.Adapter<AdapterCityList.CustomViewHolder> {
    ArrayList<String> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterCityList(Context context, OnCustomItemClicListener lis, ArrayList<String> list) {
        this.detail = list;
        this.mContext = context;
        this.listener = lis;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_citylist, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int position) {

        customViewHolder.text_city.setText(detail.get(position));

        customViewHolder.imag_close.setOnClickListener(new View.OnClickListener() {
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
        RelativeLayout rl_background;
        TextView text_city;
        ImageView imag_close;

        public CustomViewHolder(View view) {
            super(view);
            this.text_city = (TextView) view.findViewById(R.id.text_city);
            this.rl_background = (RelativeLayout) view.findViewById(R.id.rl_background);
            imag_close = (ImageView) view.findViewById(R.id.imag_close);
        }
    }


}