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
import com.app.justclap.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterUsersList extends RecyclerView.Adapter<AdapterUsersList.CustomViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterUsersList(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_userlist_adapter, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int position) {

        customViewHolder.text_name.setText(detail.get(position).getUserName());
        customViewHolder.text_desc.setText(detail.get(position).getUserEmail());

        Picasso.with(mContext)
                .load(detail.get(position).getImageUrl())
                .transform(new CircleTransform())
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
        TextView text_name, text_desc;

        public CustomViewHolder(View view) {
            super(view);

            this.text_desc = (TextView) view.findViewById(R.id.text_desc);
            this.text_name = (TextView) view.findViewById(R.id.text_name);
            this.images_banner = (ImageView) view.findViewById(R.id.images_banner);
            this.rl_background = (RelativeLayout) view.findViewById(R.id.rl_background);
        }
    }


}