package com.app.justclap.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
public class AdapterUserPastRequest extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<ModelRequest> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    int pos = 0;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterUserPastRequest(Context context, OnCustomItemClicListener lis, ArrayList<ModelRequest> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_user_past_request, parent, false);

            vh = new CustomViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;

    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
            this.progressBar.getIndeterminateDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof CustomViewHolder) {

            ModelRequest m1 = (ModelRequest) detail.get(position);
            pos = position;

            ((CustomViewHolder) holder).service_name.setText(m1.getServiceName());
            ((CustomViewHolder) holder).text_request_code.setText(m1.getRequestCode());
            ((CustomViewHolder) holder).text_date.setText(m1.getRequestDate() + "   " + m1.getRequestTime());

            Picasso.with(mContext)
                    .load(m1.getServiceIcon())
                    .into(((CustomViewHolder) holder).img_service);

            Picasso.with(mContext)
                    .load(m1.getRequestStatusImage())
                    .into(((CustomViewHolder) holder).image_location);

            ((CustomViewHolder) holder).rl_background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClickListener(position, 3);
                }
            });


        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView service_name, text_request_code, text_date;
        ImageView img_service, image_location, img_arrow;
        RelativeLayout rl_background;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            this.service_name = (TextView) view.findViewById(R.id.text_category);
            this.text_request_code = (TextView) view.findViewById(R.id.text_request_code);
            this.text_date = (TextView) view.findViewById(R.id.text_date);
            AppUtils.fontGotham_Medium(service_name, mContext);
            this.img_service = (ImageView) view.findViewById(R.id.imag_service);
            this.img_arrow = (ImageView) view.findViewById(R.id.img_arrow);
            this.image_location = (ImageView) view.findViewById(R.id.image_location);
            this.rl_background = (RelativeLayout) view.findViewById(R.id.rl_background);

        }

        @Override
        public void onClick(View v) {

            listener.onItemClickListener(getPosition(), 1);

        }

    }


    @Override
    public int getItemViewType(int position) {
        ModelRequest m1 = (ModelRequest) detail.get(position);
        if (detail.get(position).getRowType() == 1) {
            return VIEW_ITEM;
        } else if (detail.get(position).getRowType() == 2) {
            return VIEW_PROG;
        }
        return -1;
    }
}

