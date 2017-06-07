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
import com.app.justclap.model.ModelRequest;
import com.app.justclap.utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterUserRequests extends RecyclerView.Adapter<AdapterUserRequests.CustomViewHolder> {
    ArrayList<ModelRequest> detail;
    Context mContext;
    OnCustomItemClicListener listener;


    public AdapterUserRequests(Context context, OnCustomItemClicListener lis, ArrayList<ModelRequest> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_user_request, viewGroup, false);

        final CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, final int position) {

        customViewHolder.service_name.setText(detail.get(position).getServiceName());
        customViewHolder.text_request_code.setText(detail.get(position).getRequestCode());
        customViewHolder.text_date.setText(detail.get(position).getRequestDate() + "   " + detail.get(position).getRequestTime());
        if (detail.get(position).getQuoteCount() > 0) {
            customViewHolder.text_new_cont.setText("" + detail.get(position).getQuoteCount());
            customViewHolder.text_new_cont.setVisibility(View.VISIBLE);
        } else {
            customViewHolder.text_new_cont.setVisibility(View.GONE);
        }

        Picasso.with(mContext)
                .load(detail.get(position).getServiceIcon())
                .into(customViewHolder.img_service);

        customViewHolder.rl_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClickListener(position, 2);
            }
        });


    }

    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView service_name, text_new_cont, text_request_code, text_date;
        ImageView img_service, image_location, img_arrow;
        RelativeLayout rl_background;


        public CustomViewHolder(View view) {
            super(view);

            this.service_name = (TextView) view.findViewById(R.id.text_category);
            this.text_new_cont = (TextView) view.findViewById(R.id.text_new_cont);
            this.text_request_code = (TextView) view.findViewById(R.id.text_request_code);
            this.text_date = (TextView) view.findViewById(R.id.text_date);
            AppUtils.fontGotham_Medium(service_name, mContext);
            this.img_service = (ImageView) view.findViewById(R.id.imag_service);
            this.img_arrow = (ImageView) view.findViewById(R.id.img_arrow);
            this.image_location = (ImageView) view.findViewById(R.id.image_location);
            this.rl_background = (RelativeLayout) view.findViewById(R.id.rl_background);

        }
    }

    public void setFilter(ArrayList<ModelRequest> detailnew) {
        detail = new ArrayList<>();
        detail.addAll(detailnew);
        notifyDataSetChanged();
    }

    public ModelRequest getFilter(int i) {

        return detail.get(i);
    }

}