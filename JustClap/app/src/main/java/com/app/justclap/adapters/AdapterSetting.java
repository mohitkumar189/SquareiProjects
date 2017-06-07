package com.app.justclap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.justclap.R;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;

import java.util.ArrayList;


/**
 * Created by admin on 26-11-2015.
 */
public class AdapterSetting extends RecyclerView.Adapter<AdapterSetting.CustomViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    int pos = 0;

    public AdapterSetting(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_setting, null);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;

    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {

        customViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(i, 1);

            }
        });
        customViewHolder.text_city.setText(detail.get(i).getName());

    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView text_city;
        RelativeLayout card_view;


        public CustomViewHolder(View view) {
            super(view);

            this.text_city = (TextView) view.findViewById(R.id.text_city);
            this.card_view = (RelativeLayout) view.findViewById(R.id.card_view);
        }
    }
}