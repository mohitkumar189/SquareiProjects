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
import com.app.justclap.model.ModelService;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterFragmentuniServiceList extends RecyclerView.Adapter<AdapterFragmentuniServiceList.CustomViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterFragmentuniServiceList(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_uniservicelist, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int position) {

        customViewHolder.text_title.setText(detail.get(position).getCategoryName());
        customViewHolder.rl_background.setOnClickListener(new View.OnClickListener() {
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
        ImageView img_arrow;
        RelativeLayout rl_background;
        TextView text_title;


        public CustomViewHolder(View view) {
            super(view);

            this.img_arrow = (ImageView) view.findViewById(R.id.img_arrow);
            this.rl_background = (RelativeLayout) view.findViewById(R.id.rl_background);
            this.text_title = (TextView) view.findViewById(R.id.text_title);
        }
    }


}