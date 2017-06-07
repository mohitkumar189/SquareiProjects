package com.app.justclap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.justclap.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterServiceSubMenu extends RecyclerView.Adapter<AdapterServiceSubMenu.CustomViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterServiceSubMenu(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_submenu, viewGroup,false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int position) {

        customViewHolder.service_name.setText(detail.get(position).getServiceName());
        Picasso.with(mContext)
                .load(mContext.getResources().getString(R.string.img_url)
                        + detail.get(position).getServiceIcon())
                // .transform(new CircleTransform())
                 .placeholder(R.drawable.placeholder_category)
                .into(customViewHolder.img_service);

        if (position / 3 == 0) {

            if (position % 3 == 0) {

                customViewHolder.rl_background.setBackgroundResource(R.drawable.three_line_background);

            }
            else {
                customViewHolder.rl_background.setBackgroundResource(R.drawable.two_line_background);

            }

        }
        else {
            if (position % 3 == 0) {

                customViewHolder.rl_background.setBackgroundResource(R.drawable.three_linebottom_background);

            }
            else {
                customViewHolder.rl_background.setBackgroundResource(R.drawable.one_linebottom_background);

            }

        }



    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView service_name;
        ImageView img_service;
        RelativeLayout rl_background;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.service_name = (TextView) view.findViewById(R.id.text_category);

            this.img_service = (ImageView) view.findViewById(R.id.imag_service);
            this.rl_background = (RelativeLayout) view.findViewById(R.id.rl_background);

        }

        @Override
        public void onClick(View v) {


            listener.onItemClickListener(getPosition(), 1);


        }

           /* Log.e("position", getPosition() + "");
            listener.onItemClickListener(getPosition(), 1);*/
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