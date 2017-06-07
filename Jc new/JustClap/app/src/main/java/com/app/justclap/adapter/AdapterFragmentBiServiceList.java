package com.app.justclap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.app.justclap.R;
import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.model.ModelService;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterFragmentBiServiceList extends RecyclerView.Adapter<AdapterFragmentBiServiceList.CustomViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterFragmentBiServiceList(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_biservicelist, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int position) {

        customViewHolder.text_title.setText(detail.get(position).getServiceName());

        customViewHolder.text_title.setChecked(detail.get(position).isSelected());

        customViewHolder.text_title.setOnClickListener(new View.OnClickListener() {
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
        RadioButton text_title;


        public CustomViewHolder(View view) {
            super(view);

            this.rl_background = (RelativeLayout) view.findViewById(R.id.rl_background);
            this.text_title = (RadioButton) view.findViewById(R.id.text_title);
        }
    }


}