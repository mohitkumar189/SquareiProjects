package com.app.justclap.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.justclap.R;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.justclap.interfaces.OnCustomItemClicListener;
import com.app.justclap.models.ModelService;
import com.squareup.picasso.Picasso;

/**
 * Created by admin on 26-11-2015.
 */
public class AdapterCancelledBooking extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ModelService> detail;
    Context mContext;
    OnCustomItemClicListener listener;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public AdapterCancelledBooking(Context context, OnCustomItemClicListener lis, ArrayList<ModelService> list) {

        this.detail = list;
        this.mContext = context;
        this.listener = lis;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.row_booking_canceled, parent, false);

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
            this.progressBar.getIndeterminateDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof CustomViewHolder) {

            ModelService m1 = (ModelService) detail.get(position);

            ((CustomViewHolder) holder).text_services_name.setText(m1.getServiceName());
            ((CustomViewHolder) holder).text_created.setText(m1.getCreatedDate());
            ((CustomViewHolder) holder).text_requestId.setText(m1.getRequestId());

            if (m1.getCancel_reason().equalsIgnoreCase("0")) {

                ((CustomViewHolder) holder).text_cancelled.setText("Reason : Due to my personal reason");
            } else if (m1.getCancel_reason().equalsIgnoreCase("1")) {

                ((CustomViewHolder) holder).text_cancelled.setText("Reason : I don't like responses");
            }else if (m1.getCancel_reason().equalsIgnoreCase("2")) {

                ((CustomViewHolder) holder).text_cancelled.setText("Reason : Submit by mistake");
            }


        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }


    @Override
    public int getItemCount() {
        return detail.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView text_services_name, text_created, text_requestId, text_cancelled;


        public CustomViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.text_services_name = (TextView) view.findViewById(R.id.text_services_name);
            this.text_created = (TextView) view.findViewById(R.id.text_created);
            this.text_requestId = (TextView) view.findViewById(R.id.text_requestId);
            this.text_cancelled = (TextView) view.findViewById(R.id.text_cancelled);

        }

        @Override
        public void onClick(View v) {

            listener.onItemClickListener(getPosition(), 1);


        }

    }

    @Override
    public int getItemViewType(int position) {
        ModelService m1 = (ModelService) detail.get(position);
        if (detail.get(position).getRowType() == 1) {
            return VIEW_ITEM;
        } else if (detail.get(position).getRowType() == 2) {
            return VIEW_PROG;
        }
        return -1;
    }


}

