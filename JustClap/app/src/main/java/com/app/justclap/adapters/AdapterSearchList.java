package com.app.justclap.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.justclap.R;

import java.util.ArrayList;

import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterSearchList  extends RecyclerView.Adapter<AdapterSearchList.CustomViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterSearchList(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_searchlist, null);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {


        customViewHolder.service_name.setText(detail.get(i).getServiceName());
     /*   Picasso.with(mContext)
                .load(mContext.getResources().getString(R.string.img_url)
                        + detail.get(i).getServiceIcon())
                        // .transform(new CircleTransform())
                        // .placeholder(R.drawable.profile_about_user)
                .into(customViewHolder.img_service);*/

    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView service_name;
        ImageView img_service;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.service_name = (TextView) view.findViewById(R.id.text_category);

            this.img_service = (ImageView) view.findViewById(R.id.imag_service);

        }

        @Override
        public void onClick(View v) {


            listener.onItemClickListener(getPosition(), 1);


        }

           /* Log.e("position", getPosition() + "");
            listener.onItemClickListener(getPosition(), 1);*/
    }
    public void setFilter( ArrayList<ModelService> detailnew) {
        detail = new ArrayList<>();
        detail.addAll(detailnew);
        notifyDataSetChanged();
    }

    public ModelService getFilter(int i) {

        return  detail.get(i);
    }



}