package com.app.schoolapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.schoolapp.R;
import com.app.schoolapp.interfaces.OnCustomItemClicListener;
import com.app.schoolapp.model.ModelData;

import java.util.ArrayList;

/**
 * Created by Hemanta on 21-04-2017.
 */
public class AdapterParentdashbooard extends RecyclerView.Adapter<AdapterParentdashbooard.CustomViewHolder> {

    ArrayList<ModelData> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    Integer[] imgaes;

    public AdapterParentdashbooard(Context context, OnCustomItemClicListener lis, ArrayList<ModelData> list, Integer[] imgaes) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;
        this.imgaes = imgaes;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_parent_dashboard, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int i) {

        customViewHolder.text_name.setText(detail.get(i).getTitle());

        customViewHolder.imageview.setImageResource(imgaes[i]);

        customViewHolder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClickListener(i, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView text_name;
        ImageView imageview;
        LinearLayout linear;

        public CustomViewHolder(View view) {
            super(view);
            this.text_name = (TextView) view.findViewById(R.id.text_name);
            this.imageview = (ImageView) view.findViewById(R.id.imageview);
            this.linear = (LinearLayout) view.findViewById(R.id.linear);
        }
    }
}